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

### Set-up a connection
```Java
casperService = CasperService.usingPeer("127.0.0.1","777");
```

https://github.com/syntifi/casper-sdk/blob/ced2293f374d0c170ff4effc9eb606afb93782d4/src/test/java/com/syntifi/casper/sdk/service/AbstractJsonRpcTests.java#L23-L39

### Query a block



