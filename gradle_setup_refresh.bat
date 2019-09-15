::Gradle forge updated
@echo off
echo Updating forge version....
gradlew cleanCache && gradlew setupDecompWorkspace --refresh-dependencies && gradlew.bat eclipse && pause