# casper-java-sdk

Java 8 sdk library for interacting with a CSPR node.

## What is casper-java-sdk ?

SDK  to streamline the 3rd party Java client integration processes. Such 3rd parties include exchanges & app developers. 

## How To: Install ?

The SDK is a dependency on the Sonatype maven repository:

https://search.maven.org/artifact/network.casper/casper-java-sdk

To include as a gradle dependency:

```groovy
implementation 'network.casper:casper-java-sdk:M.m.i'
```

To include as a maven dependency:

```xml
<dependency>
  <groupId>network.casper</groupId>
  <artifactId>casper-java-sdk</artifactId>
  <version>M.m.i</version>
</dependency>
```

Where M.m.i is the version number eg 0.1.0



## How To: Query a node ?

To query a node, use the CasperSDK as the entry point. Instantiate CasperSDK using the `url` and `port` of the node

```java
CasperSdk casperSdk = new CasperSdk("http://0.0.0.0", 11101);
```

Note: The above url and port hold good for local nctl based nodes. Refer to this [page](https://caspernetwork.readthedocs.io/en/latest/dapp-dev-guide/setup-nctl.html) on how to set it up. If you want to test against a real node use `http://3.136.227.9` as the url and `7777` as the port.

Once we have the instance of `CasperSDK`, any implemented query method can be executed on it. For example if we want to get information about the status of a node, use the following code snippet:

```java
try {
    String nodeStatus = casperSdk.getNodeStatus();
    System.out.println(nodeStatus);
} catch ( Exception exp ) {
    System.out.println("Exception while fetching node status");
    exp.printStackTrace();
}
```

## How To: Transfer funds between 2 accounts ?

See [here](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/how_to/HowToTransferBetweenAccounts.java).

## How To: Delegate funds to a validator ?

See [here](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/how_to/HowToDelegate.java).

## How To: Undelegate funds from a validator ?

See [here](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/how_to//HowToUnDelegate.java).

## How To: Stake funds as a validator ?

See [here](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/how_to/HowToStake.java).

## How To: Unstake funds as a validator ?

See [here](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/how_to/HowToUnstake.java).

## How To: Install a smart contract ?

See [here](https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/how_to/HowToInstallAContract.java).

## How To: Invoke a smart contract ?

See [here]https://github.com/casper-network/casper-java-sdk/blob/main/src/test/java/com/casper/sdk/how_to/HowToInvokeAContract.java).

##### 
