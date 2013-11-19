
call mvn install:install-file -Dfile=%PRISM_HOME%\jars\json-org-1.0.jar -DgroupId=org.json -DartifactId=json -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true;

call mvn install:install-file -Dfile=%PRISM_HOME%\jars\ojdbc5-11.2.0.2.jar -DgroupId=com.oracle -DartifactId=ojdbc5 -Dversion=11.2.0.2 -Dpackaging=jar -DgeneratePom=true;

call mvn install:install-file -Dfile=%PRISM_HOME%\jars\arial.jar -DgroupId=org.fonts -DartifactId=fonts -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true;