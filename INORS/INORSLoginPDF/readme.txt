Steps for Development
************************

Step 1: Build Project
	mvn clean install
Step 2: Execute
	mvn exec:java


Steps for Deployment/Run
***************************

Step 1: Copy "inors-login-pdf-x.x.x.jar" in C:\Utility
Step 2: Copy "dependency" folder in C:\Utility. It will contain the following jar files:
	activation-1.1.1.jar
	aopalliance-1.0.jar
	bcmail-jdk14-1.38.jar
	bcmail-jdk14-138.jar
	bcprov-jdk14-1.38.jar
	bcprov-jdk14-138.jar
	bctsp-jdk14-1.38.jar
	commons-dbcp-1.4.jar
	commons-io-2.4.jar
	commons-logging-1.1.1.jar
	commons-pool-1.5.4.jar
	itext-2.1.7.jar
	log4j-1.2.17.jar
	mail-1.4.7.jar
	ojdbc5-11.2.0.2.jar
	spring-aop-4.0.0.RELEASE.jar
	spring-beans-4.0.0.RELEASE.jar
	spring-context-4.0.0.RELEASE.jar
	spring-core-4.0.0.RELEASE.jar
	spring-expression-4.0.0.RELEASE.jar
	spring-jdbc-4.0.0.RELEASE.jar
	spring-security-core-3.1.0.RELEASE.jar
	spring-security-crypto-3.1.0.RELEASE.jar
	spring-tx-4.0.0.RELEASE.jar

Step 3: Copy the following files in C:\Utility
	COUR.TTF
	logoCTBTASC.png
	TASCLogo.png

Step 4: Run the following command from C:\Utility

C:\Utility>java -cp inors-login-pdf-0.0.5.jar;./dependency/activation-1.1.1.jar;./dependency/aopalliance-1.0.jar;./dependency/bcmail-jdk14-1.38.jar;./dependency/bcmail-jdk14-138.jar;./dependency/bcprov-jdk14-1.38.jar;./dependency/bcprov-jdk14-138.jar;./dependency/bctsp-jdk14-1.38.jar;./dependency/commons-dbcp-1.4.jar;./dependency/commons-io-2.4.jar;./dependency/commons-logging-1.1.1.jar;./dependency/commons-pool-1.5.4.jar;./dependency/itext-2.1.7.jar;./dependency/log4j-1.2.17.jar;./dependency/mail-1.4.7.jar;./dependency/ojdbc5-11.2.0.2.jar;./dependency/spring-aop-4.0.0.RELEASE.jar;./dependency/spring-beans-4.0.0.RELEASE.jar;./dependency/spring-context-4.0.0.RELEASE.jar;./dependency/spring-core-4.0.0.RELEASE.jar;./dependency/spring-expression-4.0.0.RELEASE.jar;./dependency/spring-jdbc-4.0.0.RELEASE.jar;./dependency/spring-security-core-3.1.0.RELEASE.jar;./dependency/spring-security-crypto-3.1.0.RELEASE.jar;./dependency/spring-tx-4.0.0.RELEASE.jar com.prism.itext.UserAccountPdf L 362010

Utility Help
**************
INFO - **************************************************
INFO - args[0] - Param 1 is required.
INFO -               L = Login Pdf
INFO -               I = IC Letter
INFO -               A = All/Both Login Pdf and IC Letter
INFO - args[1] - Param 2 is required.
INFO -               Provide space separated Ids
INFO - **************************************************


SQL Queries:
**************
IC Letter: SELECT * FROM STUDENT_BIO_DIM STD, INVITATION_CODE IC, ORG_NODE_DIM ORG,
ADMIN_DIM ADM, GRADE_DIM GRD WHERE ORG.ORG_NODEID=362651 AND IC.STUDENT_BIO_ID = STD.STUDENT_BIO_ID
AND STD.ORG_NODEID = ORG.ORG_NODEID AND STD.ADMINID= ADM.ADMINID
AND STD.GRADEID= GRD.GRADEID AND IC.ACTIVATION_STATUS = 'AC' AND IC.IS_NEW_IC= 'Y' AND IC.ADMINID = STD.ADMINID
AND IC.ADMINID = (SELECT ADMINID FROM ADMIN_DIM WHERE IS_CURRENT_ADMIN = 'Y')

What is New?
*************
Version 0.0.2
	1. Support for multiple admin year
Version 0.0.3
	1. Resource path modified
	2. DB properties removed from inors.properties
