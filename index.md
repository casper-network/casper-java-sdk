---
title: Casper Java SDK
menu_item: true
menu_title: Home
layout: default
order: 1
---
[![Java CI](https://github.com/syntifi/casper-sdk/actions/workflows/gradle.yml/badge.svg)](https://github.com/syntifi/casper-sdk/actions/workflows/gradle.yml)
![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/syntifi/casper-sdk?sort=semver)
[![Project license](https://img.shields.io/badge/license-Apache%202-blue)](https://www.apache.org/licenses/LICENSE-2.0.txt)

# Casper Java SDK
 
This project implements the SDK to interact with a Casper Node. It wraps the Json-RPC requests and maps the results to Java objects. 

## Dependencies
- Java 8 
- Gradle
- [crypto-keys](https://github.com/syntifi/crypto-keys)

## Build instructions
```
./gradlew build
```

## Including the library

Using gradle:

```gradle
implementation 'com.syntifi.casper:casper-sdk:0.2.1'
```

Using maven:

``` xml
<dependency>
  <groupId>com.syntifi.casper</groupId>
  <artifactId>casper-sdk</artifactId>
  <version>0.2.1</version>
</dependency>
```

## How to

### 1. [Set-up a connection](https://github.com/syntifi/casper-sdk/blob/main/src/test/java/com/syntifi/casper/sdk/service/AbstractJsonRpcTests.java#L23-L39)

```Java
casperService = CasperService.usingPeer("127.0.0.1","7777");
```

### 2. Query a block
Retrieve block info by a block identifier

#### [Last block](https://github.com/syntifi/casper-sdk/blob/main/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L119)
```Java
JsonBlockData result = casperService.getBlock();
```
#### [By height](https://github.com/syntifi/casper-sdk/blob/main/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L138-L139)
```Java
JsonBlockData result = casperService.getBlock(new HeightBlockIdentifier(1234));
```
#### [By hash](https://github.com/syntifi/casper-sdk/blob/main/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L126-L127)
```Java
JsonBlockData blockData = casperService.getBlock(new HashBlockIdentifier("--hash--"));
```

### 3. Query transfers
Retrieve block transfers by a block identifier

#### [Last block](https://github.com/syntifi/casper-sdk/blob/main/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L148)
```Java
TransferData transferData = casperService.getBlockTransfers();
```
#### [By block height](https://github.com/syntifi/casper-sdk/blob/main/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L155)
```Java
TransferData transferData = casperService.getBlockTransfers(new HeightBlockIdentifier(1234));
```
#### [By block hash](https://github.com/syntifi/casper-sdk/blob/main/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L170-L171)
```Java
TransferData transferData = casperService.getBlockTransfers(new HashBlockIdentifier("--hash--"));
```

### 3. Query state root hash
Retrieve the state root hash given the BlockIdentifier
#### [Last block](https://github.com/syntifi/casper-sdk/blob/main/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L186)
```Java
StateRootHashData stateRootData = casperService.getStateRootHash();
```
#### [By block height](https://github.com/syntifi/casper-sdk/blob/main/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L193)
```Java
StateRootHashData stateRootData = casperService.getStateRootHash(new HeightBlockIdentifier(1234));
```
#### [By block hash](https://github.com/syntifi/casper-sdk/blob/main/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L201-L202)
```Java
StateRootHashData stateRootData = casperService.getStateRootHash(new HashBlockIdentifier("--hash--"));
```

### 4. [Query deploy](https://github.com/syntifi/casper-sdk/blob/main/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L225-L226)
Get a Deploy from the network
```Java
DeployData deployData = casperService.getDeploy("--hash--");
```

### 5. [Query peers](https://github.com/syntifi/casper-sdk/blob/main/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L111)
Get network peers data
```Java
PeerData peerData = casperService.getPeerData();
```

### 6. [Query stored value](https://github.com/syntifi/casper-sdk/blob/main/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L212-L215)
Retrieve a stored value from the network
```Java
StoredValueData result = casperService.getStateItem("--stateRootHash--", "key", Arrays.asList("The path components starting from the key as base"));
```

### 7. [Get node status](https://github.com/syntifi/casper-sdk/blob/main/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L242)
Return the current status of the node
```Java
StatusData status = casperService.getStatus()
```

### 8. Get account info
Returns an Account from the network
#### [By block height](https://github.com/syntifi/casper-sdk/blob/main/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L280-L282)
```Java
AccountData account = casperService.getStateAccountInfo("--publicKey--", new HeightBlockIdentifier(1234));
```
#### [By block hash](https://github.com/syntifi/casper-sdk/blob/main/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L268-L270)
```Java
AccountData account = casperService.getStateAccountInfo("--publicKey--", new HashBlockIdentifier("--hash--"));
```

### 9. Get auction info
Returns the Auction info for a given block
#### [By block height](https://github.com/syntifi/casper-sdk/blob/main/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L302)
```Java
AuctionData auction = casperService.getStateAuctionInfo(new HeightBlockIdentifier(1234));
```
#### [By block hash](https://github.com/syntifi/casper-sdk/blob/main/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L292-L293)
```Java
AuctionData auction = casperServiceMainnet.getStateAuctionInfo(new HashBlockIdentifier("--hash--"));
```

### 10. Get era info
Returns an EraInfo from the network
#### [By block height](https://github.com/syntifi/casper-sdk/blob/main/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L311)
```Java
EraInfoData eraInfoData = casperService.getEraInfoBySwitchBlock(new HeightBlockIdentifier(1234));
```
#### [By block hash](https://github.com/syntifi/casper-sdk/blob/main/src/test/java/com/syntifi/casper/sdk/service/CasperServiceTests.java#L325-L326)
```Java
EraInfoData eraInfoData = casperService.getEraInfoBySwitchBlock(new HashBlockIdentifier("--hash--"));
```

### 11. Deploy
#### [Transfering CSPR ](https://github.com/syntifi/casper-sdk/blob/347e8a8a3538f18a064dc4e224b3d1816b6e8f90/src/test/java/com/syntifi/casper/sdk/service/CasperDeployServiceTests.java#L73-L77)

```Java
Deploy deploy = CasperDeployService.buildTransferDeploy(from, to,
    BigInteger.valueOf(2500000000L), "casper-test",
    id, BigInteger.valueOf(100000000L), 1L, ttl, new Date(),
    new ArrayList<>());

DeployResult deployResult =  casperServiceTestnet.putDeploy(deploy);
```
