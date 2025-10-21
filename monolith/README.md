# Monolith (Java)

This is a sample monolithic application written in Java.

## Instructions

Open up the 'monolith' folder

```shell
cd monolith
```

Check that you have Powershell 7

```shell
$PSVersionTable.PSVersion
```

Set the JAVA_HOME environment variable to point to your JDK 21 installation. Check that it's set correctly

```shell
echo $env:JAVA_HOME
```

Ensure you have JDK 21 installed

```shell
java -version
```

Build the application using Gradle

```shell
.\gradlew build
```

Run the application

```shell
.\gradlew bootRun
```

Rebuild and restart the application

```shell
.\gradlew build && .\gradlew bootRun
```

App should now be running on:
http://localhost:8080/
