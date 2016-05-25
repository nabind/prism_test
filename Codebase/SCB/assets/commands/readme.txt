Follow the steps .. checking conflict
#------------------- STEP 1 -------------------
Download maven apache-maven-3.0.2

#------------------- STEP 2 -------------------
Open "Environment Variable" window (My Computer > Properties > Advanced)
Add new variable as follows:

Variable 1
	Variable Name: PRISM_HOME
	Variable Value: <<provide your prism source code path>> (e.q. C:\workspace\istep_prism)
	
Variable 2
	Variable Name: M2_Home
	Variable Value: <<provide your maven root dir>> (e.q. C:\apache-maven-3.0.2)

Add Maven into PATH
	Edit PATH variable and add Maven bin directory into that (e.q. Variable Value: %PATH%;C:\apache-maven-3.0.2\bin)

#------------------- STEP 3 -------------------
Run the following executable file to update maven repository (make sure JAVA_HOME is set correctly and JAVA_HOME\bin is in classpath)

maven-install-etc.bat
maven-install-jasper.bat
maven-install-specific.bat

#------------------- STEP 4 -------------------
Build code using the following command from %ISTEP_HOME%
mvn install

Clean code using the following command
mvn clean

#------------------- STEP 5 -------------------
Take the WAR file from %ISTEP_HOME%\prism-web\target




#====================== OTHER OPTIONAL STEPS =========================

#------------------- Deploy to tomcat server with maven -------------------

1. Add user details in tomcat users xml file located @  %TOMCAT_PATH%/conf/tomcat-users.xml

<tomcat-users>
  <role rolename="manager"/>
  <role rolename="admin"/>
  <user username="admin" password="password" roles="admin,manager"/>
</tomcat-users>

2. Add user authentication in maven settings located @ %MAVEN_PATH%/conf/settings.xml

<server>
	<id>TomcatServer</id>
	<username>admin</username>
	<password>password</password>
</server>

3. Update POM.xml to add maven plugin (already done in project codebase)

<plugin>
	<groupId>org.codehaus.mojo</groupId>
	<artifactId>tomcat-maven-plugin</artifactId>
	<configuration>
		<url>http://127.0.0.1:8080/manager</url>
		<server>TomcatServer</server>
		<path>/mkyongWebApp</path>
	</configuration>
</plugin>

4. Run command to deploy
mvn tomcat:deploy

5. Run command to re-deploy
mvn tomcat:redeploy


#------------------- Settings for PROXY in maven -------------------

1. Add proxy settings located @ %MAVEN_PATH%/conf/settings.xml
<proxy>
      <id>tcs_proxy</id>
      <active>true</active>
      <protocol>http</protocol>
      <username>india\167657</username>
      <password>******</password>
      <host>172.18.18.12</host>
      <port>8080</port>
      <nonProxyHosts>local.net,some.host.com</nonProxyHosts>
</proxy>

#------------------- Import code into eclipse -------------------

Open eclipse with Maven plugin

Import project as maven project 

File -> Import -> Maven -> Existing Maven Projects

#------------------- Remote debug with eclipse -------------------
Open catalina.bat from TOMCAT_HOME/bin

add the following line after these lines
							:noJuliConfig
							set JAVA_OPTS=%JAVA_OPTS% %LOGGING_CONFIG%

set JAVA_OPTS=%JAVA_OPTS% -Xrunjdwp:transport=dt_socket,address=8787,server=y,suspend=n

Open eclipse > debug > configuration > add new remote java application 
select Project > prism-web
connection properties > port > 8787
Add source (all prism modules)

Apply

#------------------- CVS Location -------------------
Host: 168.116.31.218
Path: /data/cvs/

Project location -> Head > prism > Phase II > prism

#------------------- Checkstyle plugin -------------------
1. Build the project
mvn clean install

2. Generate checkstyle report
mvn checkstyle:checkstyle

#------------------- Findbugs Plugin -------------------
1. Build the project
mvn clean install

2. Generate findbugs report
mvn findbugs:findbugs

3. View findbugs report
mvn findbugs:gui

#------------------- PMD Code analysis plugin -------------------
1. Build the project
mvn clean install

2. Generate PMD report
mvn pmd:pmd

#------------------- Javadoc Plugin -------------------
1. Build the project
mvn clean install

2. Generate Javadoc
mvn javadoc:javadoc
