::Gradle forge updated
@echo off
echo Updating forge version....
gradlew setupDecompWorkspace --refresh-dependencies && gradlew.bat eclipse && pause