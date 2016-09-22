1> GHI metadata configuration in global schema -- TASC_GHI_BR-G_BR-H_FORM_CONFIGURATION.sql
2> GHI DB Script -- TASC GHI FORM DB SCRIPT.sql
3> Deploy the latest Package from QA Tasc DB -- PKG_PRF_ORG_USR.pck 
4> Deploy the latest Package from QA Tasc DB --- PKG_STUDENT_FILE_DOWNLOAD.pck
5> Run the script in MVIEW_SCHEDULAR_CONFIGURATION.sql step by step.
6> GHI UDB Workflow --take latest from QA Repository
	--WKF_DRC_TASC_GHI_GRT_LOAD.xml
7. Create Unix folder structure as follows.
   Shell Script Location: /home/prism/TASC
   Shell Script Name: TASC_UDB_XML_Files_FTP_To_Target_Load.sh --take latest from /home/prismqa/TASC
   Create UDB_SOURCE and SrcXSD under /u02/app/informatica/9.1.0/server/infa_shared/PRISM/TASC/SrcDataFiles Location.
   Create UDBSOURCE.DAT file under UDB_SOURCE folder.
   Place Source XSD file under SrcXSD folder.
   Create a Link file BkpDataFiles under /u02/app/informatica/9.1.0/server/infa_shared/PRISM/TASC location 
   which will point to /u03/data/PROD/PRISM/TASC/BkpDataFiles location.\
8> TASC Student Data download Workflow. WKF_TASC_PRISM_STUDENT_DOWNLOAD.xml