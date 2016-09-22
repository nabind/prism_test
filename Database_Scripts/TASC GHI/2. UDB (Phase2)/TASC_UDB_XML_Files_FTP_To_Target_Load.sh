#!/usr/bin/bash
#------------------------------------------------------------------------------------------------------
# Program           :  TASC_UDB_XML_Files_FTP_To_Target_Load.sh
# Author            :  Sourabh De
# Date              :  08-23-2016
# Purpose           :  Script to pull the files form SFTP to INFA Source
			XML validation check, xmllint program parses XML files against XSD schema
			Call the ETL Workflow for the valid XML files
#------------------------------------------------------------------------------------------------------
#cd /home/prism/TASC
cd /home/prismqa/TASC
. ../.bash_profile
#clear
#PRISM_HOME=/u02/app/informatica/9.1.0/server/infa_shared/PRISM_DEV
#PRISM_HOME=/u02/app/informatica/9.1.0/server/infa_shared/PRISM_QA
#PRISM_WORK_HOME=/prism_work/DEV/PRISM
#PRISM_MAIL_LIST="sourabh.de@tcs.com,sourabh.de@ctb.com,nayek.sourav@tcs.com,joykumar.pal@tcs.com"
#PRISM_MAIL_LIST="sourabh.de@tcs.com,AMariadasan@DataRecognitionCorp.com,LWren@datarecognitioncorp.com"

#SCRIPT_DIR=/home/prism/TASC
#SCRIPT_DIR=/home/prismqa/TASC

INFA_SERVICE=$PRISM_INFA_SERVICE
INFA_DOMAIN=$PRISM_INFA_DOMAIN
INFA_USER=$PRISM_INFA_USER
INFA_PWD=$PRISM_INFA_PWD
INFA_FOLDER="TASC_PRISM"
INFA_WORKFLOW="WKF_DRC_TASC_GHI_GRT_LOAD"

ERR_DIR="$PRISM_HOME/TASC/ErrDataFiles"
LOG_DIR="$PRISM_HOME/TASC/Log"
SRC_DIR="$PRISM_HOME/TASC/SrcDataFiles/UDB_SOURCE"
BKP_DIR="$PRISM_HOME/TASC/BkpDataFiles"
ZIP_DIR="$PRISM_HOME/TASC/DataZIPs"
TEMP_DIR="$PRISM_HOME/TASC/SrcDataFiles/SrcXSD"
XSD_FILENAME="$TEMP_DIR/TASC_FormGHI_StudentData_from_UDB.xsd"
FILE_NAME_SUFFIX="TASC"

SOURCE_FILE="UDBSOURCE"

TXT_FILE_DIR="${PRISM_HOME}/TASC/ParamFiles"

DT=`date +"%Y%m%d"`
LOG_FILE="${PRISM_HOME}/TASC/Log/TASC_UDB_XML_Files_FTP_To_Target_Load_$DT"
LOG_FILE="$LOG_FILE.log"


#TASC_SFTP_HOST="10.160.19.10"
#TASC_SFTP_USER="prismftpqa"
#TASC_SFTP_PASS="Aesh6aez"
#TASC_SFTP_PATH="/incoming/prism/tasc/in/sdf"

####################################################################################################################################

echo   '~~~~~~TASC UDB XML File Load process started~~~~~~' 

echo '~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~'  >> $LOG_FILE
echo   `date`' - TASC UDB XML File Load process started ...'  >> $LOG_FILE
echo '~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~'  >> $LOG_FILE

cd $TEMP_DIR/

echo '...Checking SFTP Connection!...'

#lftp -c open -e "mget $TASC_SFTP_PATH/$FILE_NAME_SUFFIX*.[xX][mM][lL] ; rm $TASC_SFTP_PATH/$FILE_NAME_SUFFIX*.[xX][mM][lL] ; bye" #-u $TASC_SFTP_USER,$TASC_SFTP_PASS sftp://$TASC_SFTP_HOST
lftp -u $TASC_SFTP_USER,$TASC_SFTP_PASS -e "mget -E $TASC_SFTP_PATH/$FILE_NAME_SUFFIX*.[xX][mM][lL]; bye"  sftp://$TASC_SFTP_HOST >> $LOG_FILE
	SFTP_RET=$?
	echo "SFTP Connection Retrun =  $SFTP_RET "
	if [ $SFTP_RET -eq 0 ]; then
		echo '...File extracted from SFTP location'  
		echo "File extracted from SFTP location."  >> $LOG_FILE
	else
		echo '***File extraction failed...'
		echo "***File extraction failed."  >> $LOG_FILE
		exit
	fi

# check Data zip file is exist
FILE_FOUND=$(ls -1 $FILE_NAME_SUFFIX*.[xX][mM][lL] 2> /dev/null | wc -l)
if [ "$FILE_FOUND" = 0 ] 
then
   echo "***TASC DATA File NOT Found in: $TEMP_DIR"  >> $LOG_FILE
   exit
fi


#XML file validation and add the actual data filename into the temp file 
echo `cp -f /dev/null $SRC_DIR/$SOURCE_FILE.DAT`   
echo `chmod 777 $SRC_DIR/$SOURCE_FILE.DAT`

echo '...XML Validation Check!...'	   
for i in `ls -1tr $FILE_NAME_SUFFIX*.[xX][mM][lL]`
    do
       PFILE=$i
       echo $PFILE  >> $LOG_FILE
       FILENAME=`basename $PFILE`
       echo "XML Filename = $FILENAME"

#XSD Validation for each file
    OP=0
    ErrXml="ErrorXml_$FILENAME.log"
    xmllint --noout --schema $XSD_FILENAME $FILENAME >& $ErrXml
    xmllint --noout --schema $XSD_FILENAME $FILENAME >/dev/null 2>&1 || OP=?!
	XML_RET=$?
	echo "XML Return = $XML_RET | OP = $OP"
    if [ $OP -eq 0 ]
    then
		echo -e "Passed XML Validation, File: $FILENAME"
        echo -e "Passed XML Validation, File: $FILENAME" >> $LOG_FILE
		echo $FILENAME >> $SRC_DIR/$SOURCE_FILE.DAT
		echo `mv -f $FILENAME $SRC_DIR`
    else
		echo -e "***Failed XML Validation, File: $FILENAME"
		echo "***XML Validation, Error File = $ErrXml"
		echo -e "***Failed XML Validation, File: $FILENAME" >> $LOG_FILE
		echo `mv -f $TEMP_DIR/$FILENAME $ERR_DIR/$FILENAME`
		echo "`date` - XML File (UDB to Prism) Validation Error!... ***Failed XML Validation, File: $FILENAME. Please check the attached log file for more details." | mailx -s "TASC- UDB To PRISM XML Validation Check Failed ***Error***" -a $ErrXml $PRISM_MAIL_LIST
    fi
done

VALID_FILE_FOUND=$(ls -1 $SRC_DIR/$FILE_NAME_SUFFIX*.[xX][mM][lL] 2> /dev/null | wc -l)
if [ "$VALID_FILE_FOUND" = 0 ] 
then
   echo "***No Valid XML Files Found To Load"  >> $LOG_FILE
   exit
else

# call the INFA Workflow
echo '...Calling Workflow Data Laod!...'
echo "...Calling Workflow Data Laod script"  >> $LOG_FILE


          # workflow error log
          WKF_LOG=$LOG_DIR/$INFA_WORKFLOW.log
          # start the workflow
          echo "...Processing Data LOAD workflow"
		  echo "`date +"%H:%M:%S"` Starting Workflow LOAD $INFA_WORKFLOW ." >> $LOG_FILE
          pmcmd startworkflow -sv $INFA_SERVICE -d $INFA_DOMAIN -u $INFA_USER -p $INFA_PWD -f $INFA_FOLDER -wait $INFA_WORKFLOW > $WKF_LOG 2>&1
          RC=$?
          . $SCRIPT_DIR/PMCMD_ERR_MESSAGE.sh $RC
          RC_WKF=$?
          echo "Workflow PMCMD Return Code = $RC - $ERR_MSG" 
          echo "Workflow PMCMD Return Code = $RC_WKF"
          echo `cat $WKF_LOG`
          if [[ $RC -ne 0 ]];
          then
              ERROR_CODE=1
              error=" `date` ERROR TASC PRISM Data Load Workflow [WKF_TASC_FILES_LOAD]; PMCMD Return Code: ${RC} - ${ERR_MSG}; Workflow Return Code: ${RC_WKF}"
              echo $error >> $WKF_LOG
              mail -s "TASC-PRISM Data Load **ERROR**" $PRISM_MAIL_LIST < $WKF_LOG
			  WKF_RESULT="2"
              echo $error
          else
			 WKF_RESULT="0"
          fi
          echo `cat $WKF_LOG >> $LOG_FILE`

for sfile in `cat $SRC_DIR/$SOURCE_FILE.DAT`
	do
	if [ "$WKF_RESULT" = "2" ]
	then
		echo `mv -f $SRC_DIR/$sfile $ERR_DIR/$sfile`
	else
		echo `mv -f $SRC_DIR/$sfile $BKP_DIR/$sfile`
	fi
done    
fi

cd $SCRIPT_DIR
#------------calling URL to Clear Tomcat Cache
N=0
while read LINE ; do
	N=$((N+1))
       echo "<^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^>"
	echo "PRISM TOMCAT App Server#$N= $LINE"
	echo ""
	#echo `wget $LINE`
	#if [ "$?" != 0 ]; then
      	#    echo "***WebSite connection failed!..." 
	#fi

	echo `curl $LINE`
	
done < PrismUrlSites.txt