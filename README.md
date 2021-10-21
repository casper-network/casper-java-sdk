# Casper Java SDK
 
This project implements the SDK to interact with a Casper Node. It wraps the Json-RPC requests and maps the results to Java objects. 

## Dependencies
- Java 8 
- Gradle

## Build instructions
```
./gradlew build
```

## Maven repository

Using gradle
```gradle
implementation com.syntifi.casper:casper-sdk:VERSION
```

Using maven
```xml
<dependency>
  <groupId>com.syntifi.casper</groupId>
  <artifactId>casper-sdk</artifactId>
  <version>VERSION</version>
</dependency>
```


## How to

- [Set-up a connection](https://github.com/syntifi/casper-sdk/blob/1fe4a44acf431f5e1990b1d9f331505011da4f83/casper-java-sdk/src/test/java/com/syntifi/casper/sdk/CasperSdkApplicationTests.java#L61)
- [Query block by Hash](https://github.com/syntifi/casper-sdk/blob/1fe4a44acf431f5e1990b1d9f331505011da4f83/casper-java-sdk/src/test/java/com/syntifi/casper/sdk/CasperSdkApplicationTests.java#L130)

