@echo off
echo Building CQR...
.\gradlew build
echo Finished!
echo Opening Directory of build...
start .\build\libs\
pause