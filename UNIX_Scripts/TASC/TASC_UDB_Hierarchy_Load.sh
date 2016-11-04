#!/usr/bin/bash
#------------------------------------------------------------------------------------------------
# Program           :  TASC_UDB_Hierarchy_Load.sh
# Author            :  Sourav Nayek
# Date              :  10-06-2016
# Purpose           :  Script to pull the TASC hierarchy csv files from SFTP to INFA Source,
#					   Call the ETL Workflow for the valid XML files,
#					   Workflow process the file one by one	
#					   Send eMail notifications
#------------------------------------------------------------------------------------------------
#cd /home/prism/TASC
. ../.bash_profile
#clear
INFA_SERVICE=$PRISM_INFA_SERVICE
INFA_DOMAIN=$PRISM_INFA_DOMAIN
INFA_USER=$PRISM_INFA_USER
INFA_PWD=$PRISM_INFA_PWD
INFA_FOLDER="TASC_PRISM"
INFA_WORKFLOW="WKF_DRC_TASC_HIERARCHY_LOAD"

ERR_DIR="$PRISM_HOME/TASC/ErrDataFiles"
LOG_DIR="$PRISM_HOME/TASC/Log"
SRC_DIR="$PRISM_HOME/TASC/SrcDataFiles/HIERARCHY"
BKP_DIR="$PRISM_HOME/TASC/BkpDataFiles"
FILE_NAME_SUFFIX="HIERARCHY_TASC"

SOURCE_FILE="HIERSOURCE"

TXT_FILE_DIR="${PRISM_HOME}/TASC/ParamFiles"

DT=`date +"%Y%m%d"`
LOG_FILE="${PRISM_HOME}/TASC/Log/TASC_UDB_Hierarchy_Load_$DT"
LOG_FILE="$LOG_FILE.log"

TASC_SFTP_PATH="/incoming/prism/tasc/in/hierarchy"

#####################################################################################################

echo   '~~~~~TASC- Hierarchy Load (UDB to PRISM) Process Started - '`date`' ~~~~~~' 

echo '~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~' >> $LOG_FILE
echo   `date`' - TASC- Hierarchy (UDB to PRISM) Load Process Started ...'  >> $LOG_FILE
echo '~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~' >> $LOG_FILE

cd $SRC_DIR/

echo '...Checking SFTP Connection!...'

lftp -u $TASC_SFTP_USER,$TASC_SFTP_PASS -e "ls -l; bye" sftp://$TASC_SFTP_HOST >> $LOG_FILE

if [[ $? -eq 0 ]]; then
  lftp -u $TASC_SFTP_USER,$TASC_SFTP_PASS -e "mget -E $TASC_SFTP_PATH/$FILE_NAME_SUFFIX*.[cC][sS][vV]; bye"  sftp://$TASC_SFTP_HOST >> $LOG_FILE
	SFTP_RET=$?
	echo "SFTP Connection Retrun =  $SFTP_RET "
	if [ $SFTP_RET -eq 0 ]; then
		echo '...Hierarchy CSV File Transfer Success From SFTP Location.'  
		echo "...Hierarchy CSV File Transfer Success From SFTP Location."  >> $LOG_FILE
	else
		echo '...TASC Hierarchy File-NOT-Found...'
		echo "...TASC Hierarchy File-NOT-Found..."  >> $LOG_FILE
		exit
	fi
else
    echo "***TASC SFTP Connection Failed...." 
	echo "***TASC SFTP Connection Failed, SFTP Account: $TASC_SFTP_USER@$TASC_SFTP_HOST"  >> $LOG_FILE
	echo "`date` ***Error while connecting SFTP Account: $TASC_SFTP_USER@$TASC_SFTP_HOST" | mail -s "TASC-PRISM Hierarchy SFTP Connection Failed **ERROR**" $PRISM_MAIL_LIST
	exit
fi

# check Data file is exist
FILE_FOUND=$(ls -1 $SRC_DIR/$FILE_NAME_SUFFIX*.[cC][sS][vV] 2> /dev/null | wc -l)
if [ "$FILE_FOUND" = 0 ] 
then 
   echo "***TASC Hierarchy File Not Found in: $SRC_DIR"
   exit
else
	File_List=$(ls -1 $FILE_NAME_SUFFIX*.[cC][sS][vV])
	#echo $File_List
	echo -e "TASC Hierarchy file(s) received (UDB to Prism) in SFTP location. PRISM-TASC Hierarchy Workflow will initiate shortly...! \\nTASC Hierarchy Filename(s):- "$File_List  | mail -s "TASC-PRISM Hierarchy File Found - Process Started!..." $PRISM_MAIL_LIST
fi
 
for i in `ls -1tr $SRC_DIR/$FILE_NAME_SUFFIX*.[cC][sS][vV]`
    do
      PFILE=$i
	  PFILE_NAME=`basename $PFILE`
      echo $PFILE_NAME
      if [ ${PFILE_NAME:(0):14} == $FILE_NAME_SUFFIX ]
      then
          #PFILE_NAME=${PFILE:(-36)}
          echo '...Processing CSV File: '$PFILE_NAME
          
          # add the actual data filename into the temp file 
          echo `\cp -f /dev/null $SRC_DIR/$SOURCE_FILE.DAT`   
          echo `chmod 777 $SRC_DIR/*`
          echo $PFILE_NAME > $SRC_DIR/$SOURCE_FILE.DAT
          
          # workflow error log
          WKF_LOG=$LOG_DIR/$INFA_WORKFLOW.log
          # start the workflow
          echo "...Processing Data LOAD Workflow"
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
              error=" `date` ***ERROR TASC- HIERARCHY Load (UDB to PRISM) Workflow [WKF_DRC_TASC_HIERARCHY_LOAD]; PMCMD Return Code: ${RC} - ${ERR_MSG}; Workflow Return Code: ${RC_WKF}"
              echo $error >> $WKF_LOG
              echo -e "ERROR: Workflow [WKF_DRC_TASC_HIERARCHY_LOAD]: Execution failed.  Please check the workflow log for more information.....! \\nTASC Hierarchy Filename:- "$PFILE_NAME  | mail -s "TASC-PRISM Hierarchy Load **ERROR**" $PRISM_MAIL_LIST
	     echo `mv -f $SRC_DIR/$PFILE_NAME $ERR_DIR/$PFILE_NAME`
              echo $error
          else
             #echo "testing SUCCESS in WKF"
             echo `mv -f $SRC_DIR/$PFILE_NAME $BKP_DIR/$PFILE_NAME`
          fi
    fi
echo `cat $WKF_LOG >> $LOG_FILE`    
echo `rm -f $SRC_DIR/$PFILE_NAME`
done
