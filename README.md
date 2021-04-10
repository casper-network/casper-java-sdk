## Casper Labs Java SDK



Version 0.1.0 of the Java SDK

Exposes methods to access the chain

#### Installation

This version requires a JAR build using gradle

To build, first clone the main branch, then run the following gradle command in the root to build the jar:

```bash
gradle casperJar
```

This will create a JAR with all dependencies in:

```bash
[casper-java-sdk]/build/libs/casper-java-sdk-0.1.0.jar
```

 Now copy this JAR to a subfolder of your client application

```
[client_project]/lib/casper-java-sdk-0.1.0.jar
```

If you're using gradle for your project you can now include this in your build file:

```bash
dependencies {
    compile files('lib/casper-java-sdk-0.1.0.jar')
}
```

If you're not using gradle you can import the JAR into the project using your IDE



#### Running the SDK

To use the SDK, initialise the controller with the chain endpoint and the rpc port.

Chain access methods will then be exposed:

```java
final CasperSdk casperSdk = new CasperSdk("http://localhost", "40101");
        
final String accountBalance = casperSdk
   		.getAccountBalance("01a34328d2111a92d9e3c52c5c20b584cee96bb425adbdbf5df12b1ad36fff967f");       

assert accountBalance.equals("1000000000000000000000000000000000");

```





##### 
