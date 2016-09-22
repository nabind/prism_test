#!/bin/bash
#------------------------------------------------------------------------------------------------------
# Program           :  TASC_UDB_Reconciliation.sh
# Author            :  Sourabh De
# Date              :  08-31-2016
# Purpose           :  Script to invoke workflow
#	               Transfer the Reconciliation files to SFTP and Backup Target
#------------------------------------------------------------------------------------------------------
cd /home/prismqa/TASC
#cd /home/prismqa/TASC
. ../.bash_profile
#clear
#PRISM_HOME=/u02/app/informatica/9.1.0/server/infa_shared/PRISM_DEV
#PRISM_HOME=/u02/app/informatica/9.1.0/server/infa_shared/PRISM_QA
#PRISM_WORK_HOME=/prism_work/DEV/PRISM
#PRISM_MAIL_LIST="sourabh.de@tcs.com,sourabh.de@ctb.com,nayek.sourav@tcs.com,joykumar.pal@tcs.com"
#PRISM_MAIL_LIST="sourabh.de@tcs.com,AMariadasan@DataRecognitionCorp.com,nayek.sourav@tcs.com"

#SCRIPT_DIR=/home/prism/TASC
#SCRIPT_DIR=/home/prismqa/TASC

INFA_SERVICE=$PRISM_INFA_SERVICE
INFA_DOMAIN=$PRISM_INFA_DOMAIN
INFA_USER=$PRISM_INFA_USER
INFA_PWD=$PRISM_INFA_PWD
INFA_FOLDER="TASC_PRISM"
INFA_WORKFLOW="WKF_DRC_TASC_RECONCILIATION_LOAD"

ERR_DIR="$PRISM_HOME/TASC/ErrDataFiles"
LOG_DIR="$PRISM_HOME/TASC/Log"
TGT_DIR="$PRISM_HOME/TASC/TgtDataFiles/Reconciliation"
BKP_DIR="$PRISM_HOME/TASC/BkpDataFiles"
ZIP_DIR="$PRISM_HOME/TASC/DataZIPs"
FILE_NAME_SUFFIX="TASC"

SOURCE_FILE="UDBSOURCE"

TXT_FILE_DIR="${PRISM_HOME}/TASC/ParamFiles"

DT=`date +"%Y%m%d"`
LOG_FILE="${PRISM_HOME}/TASC/Log/TASC_UDB_XML_Files_FTP_To_Target_Load_$DT"
LOG_FILE="$LOG_FILE.log"


#TASC_SFTP_HOST="10.160.19.10"
#TASC_SFTP_USER="prismftpdev"
#TASC_SFTP_PASS="Eiqu2iGi"
TASC_SFTP_TGT_PATH="/incoming/prism/tasc/out/reconciliation"

####################################################################################################################################


echo   '~~~~~~TASC RECONCILIATION EXTRACT PROCESS ~~~~~~' 

echo '~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~'  >> $LOG_FILE
echo   `date`' - TASC RECONCILIATION EXTRACT PROCESS...'  >> $LOG_FILE
echo '~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~'  >> $LOG_FILE

cd $TGT_DIR/

# call the INFA Workflow
echo '...Calling Workflow Data Extract!...'
echo "...Calling Workflow Data Extract script"  >> $LOG_FILE


          # workflow error log
          WKF_LOG=$LOG_DIR/$INFA_WORKFLOW.log
          # start the workflow
          echo "...Processing Data Extract Workflow"
		  echo "`date +"%H:%M:%S"` Starting Workflow $INFA_WORKFLOW ." >> $LOG_FILE
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
              error=" `date` ERROR TASC PRISM Workflow [WKF_TASC_FILES_LOAD]; PMCMD Return Code: ${RC} - ${ERR_MSG}; Workflow Return Code: ${RC_WKF}"
              echo $error >> $WKF_LOG
              mail -s "TASC-PRISM Reconciliation Extract **ERROR**" $PRISM_MAIL_LIST < $WKF_LOG
			  WKF_RESULT="2"
              echo $error
          else
			  WKF_RESULT="0"
			  echo   `date`' - TASC RECONCILIATION EXTRACT Process Completed...'  >> $LOG_FILE
          fi
          echo `cat $WKF_LOG >> $LOG_FILE`
		  
if [ "$WKF_RESULT" = "0" ]
then
	echo   `date`' - File Backup Started...'  >> $LOG_FILE
	echo   `cp -f $FILE_NAME_SUFFIX*.[cC][sS][vV] $BKP_DIR/`
	echo   `date`' - File Backup End...'  >> $LOG_FILE

    echo   `date`' - SFTP File Transfer Started...'  >> $LOG_FILE
	lftp -u $TASC_SFTP_USER,$TASC_SFTP_PASS -e "cd $TASC_SFTP_TGT_PATH; mput -E $FILE_NAME_SUFFIX*.[cC][sS][vV]; bye"  sftp://$TASC_SFTP_HOST >> $LOG_FILE
	SFTP_RET=$?
	echo "SFTP Connection Retrun = $SFTP_RET "
	if [ $SFTP_RET -eq 0 ] 
	then
		echo '...Reconciliation File Transfer successful.'  
		echo "...Reconciliation File Transfer successful."  >> $LOG_FILE
	else
		echo '***Reconciliation File Transfer Failed'  
		echo "***Reconciliation File Transfer Failed"  >> $LOG_FILE
		echo "`date` - TASC-PRISM Reconciliation SFTP Failed!... ***Please check $TGT_DIR for the Files that failed transfer." | mailx -s "TASC- Reconciliation File Transfer To SFTP Server Failed ***Error***" $PRISM_MAIL_LIST
	fi
	echo   `date`' - SFTP File Transfer for Reconciliation File End ...'  >> $LOG_FILE
else
	echo   '***$INFA_WORKFLOW Failed'
	echo   `date`' - '$INFA_WORKFLOW' failed. ...'  >> $LOG_FILE
fi
