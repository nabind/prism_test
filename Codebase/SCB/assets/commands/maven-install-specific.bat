
call mvn install:install-file -Dfile=%PRISM_HOME%\jars\json-org-1.0.jar -DgroupId=org.json -DartifactId=json -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true;

call mvn install:install-file -Dfile=%PRISM_HOME%\jars\ojdbc5-11.2.0.2.jar -DgroupId=com.oracle -DartifactId=ojdbc5 -Dversion=11.2.0.2 -Dpackaging=jar -DgeneratePom=true;

call mvn install:install-file -Dfile=%PRISM_HOME%\jars\arial.jar -DgroupId=org.fonts -DartifactId=fonts -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true;


call mvn install:install-file -Dfile=%PRISM_HOME%\jars\ojdbc6.jar -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.3 -Dpackaging=jar -DgeneratePom=true

call mvn install:install-file -Dfile=%PRISM_HOME%\jars\ojdbc7.jar -DgroupId=com.oracle -DartifactId=ojdbc7 -Dversion=7 -Dpackaging=jar -DgeneratePom=true;

call mvn install:install-file -Dfile=%PRISM_HOME%\jars\sqljdbc42.jar -DgroupId=com.microsoft -DartifactId=sqljdbc42 -Dversion=4.2 -Dpackaging=jar -DgeneratePom=true;