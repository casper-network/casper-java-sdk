[![Java CI](https://github.com/casper-network/casper-java-sdk/actions/workflows/gradle.yml/badge.svg)](https://github.com/casper-network/casper-java-sdk/actions/workflows/gradle.yml)
![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/casper-network/casper-java-sdk?sort=semver)
[![Project license](https://img.shields.io/badge/license-Apache%202-blue)](https://www.apache.org/licenses/LICENSE-2.0.txt)

# Casper Java SDK

This project implements the Java SDK that allows developers to interact with a Casper Node. It wraps the Json-RPC requests and maps the results to Java objects. 

This version is compatable with Casper Node 2.0 (Condor)

## Dependencies
- Java 8 
- Gradle
- [crypto-keys](https://github.com/crypto-keys)

## Build instructions
```bash
git clone https://github.com/casper-network/casper-java-sdk.git
cd casper-java-sdk
./gradlew build
```

## Including the library

Using gradle:

```gradle
implementation 'network.casper:casper-java-sdk:2.7.0-BETA.2'
```

Using maven:

``` xml
<dependency>
    <groupId>network.casper</groupId>
    <artifactId>casper-java-sdk</artifactId>
    <version>2.7.0-BETA.2</version>
</dependency>
```



## Quick Start

Tools are provided for Casper Node developers to start coding quickly.

[CCTL](https://github.com/casper-network/cctl) is Casper's local node network. This is a bash utility which can be used to test node queries. It can be used in one of three ways:

#### Build Local

This will build the node on the local os. Follow the instructions [here](https://github.com/casper-network/cctl/blob/main/docs/setup.md). And usage [here](https://github.com/casper-network/cctl/blob/main/docs/usage.md)

#### Build Docker Image Locally

This will clone the CCTL repo and build a local docker image. More detail [here](https://github.com/casper-network/cctl/tree/dev/docker)

```bash
bash
git clone https://github.com/casper-network/cctl.git -b dev
cd cctl
docker compose up -d
docker exec -t -i cspr-cctl /bin/bash
cctl-infra-net-status
```

#### Run Docker Container from Docker Hub

```bash
docker run --rm -it --name cspr-cctl -d -p 25101:25101 -p 11101:11101 -p 14101:14101 -p 18101:18101 -p 21101:21101 stormeye2000/cspr-cctl:feat-2.0
docker exec -t -i cspr-cctl /bin/bash
cctl-infra-net-status
```

The above CCTL command *cctl-infra-net-status* will produce the following output:

```bash
cctl@8abc69431a4b:~$ cctl-infra-net-status
validator-group-1:cctl-node-1            RUNNING   pid 887, uptime 0:04:34
validator-group-1:cctl-node-1-sidecar    RUNNING   pid 884, uptime 0:04:34
validator-group-1:cctl-node-2            RUNNING   pid 886, uptime 0:04:34
validator-group-1:cctl-node-2-sidecar    RUNNING   pid 889, uptime 0:04:34
validator-group-1:cctl-node-3            RUNNING   pid 885, uptime 0:04:34
validator-group-1:cctl-node-3-sidecar    RUNNING   pid 888, uptime 0:04:34
validator-group-2:cctl-node-4            RUNNING   pid 975, uptime 0:04:33
validator-group-2:cctl-node-4-sidecar    RUNNING   pid 977, uptime 0:04:33
validator-group-2:cctl-node-5            RUNNING   pid 974, uptime 0:04:33
validator-group-2:cctl-node-5-sidecar    RUNNING   pid 976, uptime 0:04:33
```

Type cctl-[tab] to see the full list of commands

#### Assets

CCTL is built with a set of generated user assets, including user and faucet keys. These are located here:

```bash
docker exec cspr-cctl ls /home/cctl/cctl/assets -lt

drwxr-xr-x  2 cctl cctl 4096 Jul 23 10:57 genesis
drwxr-xr-x  2 cctl cctl 4096 Jul 23 10:57 faucet
drwxr-xr-x  2 cctl cctl 4096 Jul 23 10:57 bin
drwxr-xr-x 12 cctl cctl 4096 Jul 23 10:57 users
drwxr-xr-x 12 cctl cctl 4096 Jul 23 10:57 sidecars
drwxr-xr-x 12 cctl cctl 4096 Jul 23 10:57 nodes
drwxr-xr-x  5 cctl cctl 4096 Jul 23 10:57 daemon
```

Copy from the image the required assets:

```bash
docker cp cspr-cctl:/home/cctl/cctl/assets/users/. [your-local-folder]
docker cp cspr-cctl:/home/cctl/cctl/assets/faucet/ [your-local-folder]
```





## How to

### 1. [Set-up a connection](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/service/AbstractJsonRpcTests.java#L23-L39)

```Java
casperService = CasperService.usingPeer("127.0.0.1","7777");
```

### 2. Query a block
Retrieve block info by a block identifier

#### [Last block](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/service/CasperServiceTests.java#L119)
```Java
JsonBlockData result = casperService.getBlock();
```
#### [By height](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/service/CasperServiceTests.java#L138-L139)
```Java
JsonBlockData result = casperService.getBlock(new HeightBlockIdentifier(1234));
```
#### [By hash](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/service/CasperServiceTests.java#L126-L127)
```Java
JsonBlockData blockData = casperService.getBlock(new HashBlockIdentifier("--hash--"));
```

### 3. Query transfers
Retrieve block transfers by a block identifier

#### [Last block](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/service/CasperServiceTests.java#L148)
```Java
TransferData transferData = casperService.getBlockTransfers();
```
#### [By block height](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/service/CasperServiceTests.java#L155)
```Java
TransferData transferData = casperService.getBlockTransfers(new HeightBlockIdentifier(1234));
```
#### [By block hash](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/service/CasperServiceTests.java#L170-L171)
```Java
TransferData transferData = casperService.getBlockTransfers(new HashBlockIdentifier("--hash--"));
```

### 3. Query state root hash
Retrieve the state root hash given the BlockIdentifier
#### [Last block](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/service/CasperServiceTests.java#L186)
```Java
StateRootHashData stateRootData = casperService.getStateRootHash();
```
#### [By block height](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/service/CasperServiceTests.java#L193)
```Java
StateRootHashData stateRootData = casperService.getStateRootHash(new HeightBlockIdentifier(1234));
```
#### [By block hash](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/service/CasperServiceTests.java#L201-L202)
```Java
StateRootHashData stateRootData = casperService.getStateRootHash(new HashBlockIdentifier("--hash--"));
```

### 4. [Query deploy](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/service/CasperServiceTests.java#L225-L226)
Get a Deploy from the network
```Java
DeployData deployData = casperService.getDeploy("--hash--");
```

### 5. [Query peers](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/service/CasperServiceTests.java#L111)
Get network peers data
```Java
PeerData peerData = casperService.getPeerData();
```

### 6. [Query stored value](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/service/CasperServiceTests.java#L212-L215)
Retrieve a stored value from the network
```Java
StoredValueData result = casperService.getStateItem("--stateRootHash--", "key", Arrays.asList("The path components starting from the key as base"));
```

### 7. [Get node status](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/service/CasperServiceTests.java#L242)
Return the current status of the node
```Java
StatusData status = casperService.getStatus()
```

### 8. Get account info
Returns an Account from the network
#### [By block height](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/service/CasperServiceTests.java#L280-L282)
```Java
AccountData account = casperService.getStateAccountInfo("--publicKey--", new HeightBlockIdentifier(1234));
```
#### [By block hash](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/service/CasperServiceTests.java#L268-L270)
```Java
AccountData account = casperService.getStateAccountInfo("--publicKey--", new HashBlockIdentifier("--hash--"));
```

### 9. Get auction info
Returns the Auction info for a given block
#### [By block height](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/service/CasperServiceTests.java#L302)
```Java
AuctionData auction = casperService.getStateAuctionInfo(new HeightBlockIdentifier(1234));
```
#### [By block hash](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/service/CasperServiceTests.java#L292-L293)
```Java
AuctionData auction = casperServiceMainnet.getStateAuctionInfo(new HashBlockIdentifier("--hash--"));
```

### 10. Get era info
Returns an EraInfo from the network
#### [By block height](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/service/CasperServiceTests.java#L311)
```Java
EraInfoData eraInfoData = casperService.getEraInfoBySwitchBlock(new HeightBlockIdentifier(1234));
```
#### [By block hash](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/service/CasperServiceTests.java#L325-L326)
```Java
EraInfoData eraInfoData = casperService.getEraInfoBySwitchBlock(new HashBlockIdentifier("--hash--"));
```

### 11. Deploy
#### [Transfering CSPR ](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/service/CasperDeployServiceTests.java#L73-L77)

```Java
Deploy deploy = CasperDeployService.buildTransferDeploy(from, to,
    BigInteger.valueOf(2500000000L), "casper-test",
    id, BigInteger.valueOf(100000000L), 1L, ttl, new Date(),
    new ArrayList<>());

DeployResult deployResult =  casperServiceTestnet.putDeploy(deploy);
```

### 12. Consuming Events

The Java SDK supports the consumption of casper events using the event service API. This API allows the consumer to 
choose the events to be provided as Pojos or as raw JSON via the EventTarget enum values. Each event stream is consumed 
individually by providing the required stream (main, sigs, and deploys) using the EventType parameter to the consumeEvents method.

For more information on events see: [Monitoring and Consuming Events](https://docs.casperlabs.io/dapp-dev-guide/building-dapps/monitoring-events/).

#### Consuming Raw JSON Event Strings
```Java
// Construct an events service
final EventService eventService = EventService.usingPeer(new URI("http://localhost:28101"));

// Consume the main events as raw JSON
eventService.consumeEvents(EventType.MAIN, EventTarget.RAW, 0L, new EventConsumer<String>(){
    
    @Override
    public void accept(final Event<String> event) {
        // Obtain the raw JSON event as a String
        final String json = event.getData();
        // Obtain the optional event ID
        final long id = event.getId().orElse(0L);
    }
});
```

#### Consuming Pojo Events

```Java
// Construct an events service
final EventService eventService = EventService.usingPeer(new URI("http://localhost:28101"));

// Consume the main events as Casper Java SDK Pojos
eventService.consumeEvents(EventType.MAIN, EventTarget.POJO, 0L, new EventConsumer<EventData>() {
    
    @Override
    public void accept(final Event<EventData> event) {

        switch (event.getDataType()) {

            case BLOCK_ADDED:
                handleBlockAdded(event.getId().get(), ((BlockAdded) event.getData()));
                break;
                
            // And so on...    
        }
    }
});
```
