::Gradle forge updated
@echo off
echo Updating forge version....
gradlew cleanCache && gradlew eclipse && gradlew genEclipseRuns && pause