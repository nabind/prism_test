cd ./prism-admin
mvn -o -T 14 clean install -Dmaven.test.skip=true && copy ".\target\prism-admin-0.0.1-SNAPSHOT.jar" "..\prism-web\target\prism\WEB-INF\lib" /Y && cd ..