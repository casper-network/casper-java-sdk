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

### 1. [Set-up a connection](https://github.com/syntifi/casper-sdk/blob/ced2293f374d0c170ff4effc9eb606afb93782d4/src/test/java/com/syntifi/casper/sdk/service/AbstractJsonRpcTests.java#L23-L39)

```Java
casperService = CasperService.usingPeer("127.0.0.1","7777");
```

### 2. Query a block
Retrieve block info by a block identifier

#### [Last block](https://github.com/syntifi/casper-sdk/blob/67185568cf4df5ed09dc34cc0cbf906165b56843/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L119)
```Java
JsonBlockData result = casperService.getBlock();
```
#### [By height](https://github.com/syntifi/casper-sdk/blob/67185568cf4df5ed09dc34cc0cbf906165b56843/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L138-L139)
```Java
JsonBlockData result = casperService.getBlock(new HeightBlockIdentifier(1234));
```
#### [By hash](https://github.com/syntifi/casper-sdk/blob/67185568cf4df5ed09dc34cc0cbf906165b56843/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L126-L127)
```Java
JsonBlockData blockData = casperService.getBlock(new HashBlockIdentifier("--hash--"));
```

### 3. Query transfers
Retrieve block transfers by a block identifier

#### [Last block](https://github.com/syntifi/casper-sdk/blob/67185568cf4df5ed09dc34cc0cbf906165b56843/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L148)
```Java
TransferData transferData = casperService.getBlockTransfers();
```
#### [By block height](https://github.com/syntifi/casper-sdk/blob/67185568cf4df5ed09dc34cc0cbf906165b56843/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L155)
```Java
TransferData transferData = casperService.getBlockTransfers(new HeightBlockIdentifier(1234));
```
#### [By block hash](https://github.com/syntifi/casper-sdk/blob/67185568cf4df5ed09dc34cc0cbf906165b56843/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L170-L171)
```Java
TransferData transferData = casperService.getBlockTransfers(new HashBlockIdentifier("--hash--"));
```

### 3. Query state root hash
Retrieve the state root hash given the BlockIdentifier
#### [Last block](https://github.com/syntifi/casper-sdk/blob/67185568cf4df5ed09dc34cc0cbf906165b56843/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L186)
```Java
StateRootHashData stateRootData = casperService.getStateRootHash();
```
#### [By block height](https://github.com/syntifi/casper-sdk/blob/67185568cf4df5ed09dc34cc0cbf906165b56843/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L193)
```Java
StateRootHashData stateRootData = casperService.getStateRootHash(new HeightBlockIdentifier(1234));
```
#### [By block hash](https://github.com/syntifi/casper-sdk/blob/67185568cf4df5ed09dc34cc0cbf906165b56843/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L201-L202)
```Java
StateRootHashData stateRootData = casperService.getStateRootHash(new HashBlockIdentifier("--hash--"));
```

### 4. [Query deploy](https://github.com/syntifi/casper-sdk/blob/67185568cf4df5ed09dc34cc0cbf906165b56843/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L225-L226)
Get a Deploy from the network
```Java
DeployData deployData = casperService.getDeploy("--hash--");
```

### 5. [Query peers](https://github.com/syntifi/casper-sdk/blob/67185568cf4df5ed09dc34cc0cbf906165b56843/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L111)
Get network peers data
```Java
PeerData peerData = casperService.getPeerData();
```

### 6. [Query stored value](https://github.com/syntifi/casper-sdk/blob/67185568cf4df5ed09dc34cc0cbf906165b56843/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L212-L215)
Retrieve a stored value from the network
```Java
StoredValueData result = casperService.getStateItem("--stateRootHash--", "key", List<"path">);
```

### 7. [Get node status](https://github.com/syntifi/casper-sdk/blob/67185568cf4df5ed09dc34cc0cbf906165b56843/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L242)
Return the current status of the node
```Java
StatusData status = casperService.getStatus()
```

