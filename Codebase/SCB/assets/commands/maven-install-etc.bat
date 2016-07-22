@ECHO off
SET lib_dir=%PRISM_HOME%\jars\etc


set #=%lib_dir%
set length=0
:loop
if defined # (set #=%#:~1%&set /A length += 1&goto loop)
set /a length=%length%+1


SET jar_list=(%lib_dir%\*.jar)

SETLOCAL ENABLEDELAYEDEXPANSION


FOR %%A IN %jar_list% DO (
set jar_p=%%A
set jar_n=!jar_p:~%length%,-4!
::set wq=!wq:\=.!
ECHO ******************Installing Jar !jar_n! *************************
call mvn install:install-file -DgroupId=net.etc -DartifactId=!jar_n! -Dfile=!jar_p! -Dversion=1.0 -Dpackaging=jar
)

