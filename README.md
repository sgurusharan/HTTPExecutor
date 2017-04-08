# HTTPExecutor
HTTPExecutor is a HTTP Listener that can execute system commands sent as HTTP Request and send their output in the response.
# Usage
To use the executor, first the listener should be started on an available port. This can be done in three ways; in either way, specifying the port is optional. If no port is specified a random available port is picked up automatically.

### Starting the listener as standalone from source code
* Download the source code as a ZIP and extract it.
* Execute the following command from the unzipped directory:
```bash
# Unix-based (including OSX)
./gradlew [-Dport=someport] run
# Windows
gradlew.bat [-Dport=someport] run
```

### Starting the listener as standalone from a JAR
* Download the source code as a ZIP and extract it.
* Execute the following command from the unzipped directory to generate the JAR: 
```bash
# Unix-based (including OSX)
./gradlew fatJar
# Windows
gradlew.bat fatJar
```
* The JAR would be generated in the sub directory build/lib/HTTPExecutor-_version_-SNAPSHOT.jar
* Execute the JAR using the command `java [-Dport=someport] -jar _path_to_jar_file_`

### Starting the listener from your Java project
* Add dependency to the GitHub project.
   * You can do this by generating the JAR (as mentioned above) and linking it to your project.
   * Or by adding this from JitPack repo directly. To this (in gradle for example):
      * So, add JitPack repo to your build repos and then add a compile dependency to the latest snapshot (1.0):
```groovy
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
}

dependencies {
    ...
    compile 'com.github.sgurusharan:SystemCommand:1.0-SNAPSHOT'
}
```
* Now start the listener from anywhere in the Java project:
```java
// Start in a random available port
com.automatium.system.http.listener.HTTPExecutionListener listener = new  com.automatium.system.http.listener.HTTPExecutionListener();
// Start in the specified port
// com.automatium.system.http.listener.HTTPExecutionListener listener = new  com.automatium.system.http.listener.HTTPExecutionListener(port);

// Start the listener in a separate thread
listener.start();

// You can know if the listener is running by checking on listener.isRunning()

// Stop the listener thread
listener.stop()
```

### Sending commands to the Executor
Once the listener has started you should know the host and port that its running on and is ready to receive commands.
Commands can be sent as http GET requests to the host and port by passing the required command(s) to execute as `cmd` parameter.
Optionally we may also specify the expected output as either `xml` or `json` as `format` parameter.

You would get the request format in the Console, when the listener is started.

### Stopping the Executor
The executor can be stopped by sending the `cmd` as `stop`
