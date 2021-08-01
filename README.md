# casper-java-sdk

Java 15 sdk library for interacting with a CSPR node.

## What is casper-java-sdk ?

SDK  to streamline the 3rd party Java client integration processes. Such 3rd parties include exchanges & app developers. 

## How To: Install ?

This version requires a JAR build using gradle

To build, first clone the main branch, then run the following gradle command in the root to build the jar:

```bash
git clone https://github.com/casper-network/casper-java-sdk.git
cd casper-java-sdk
gradle casperJar
```

This will create a JAR with all dependencies in:

```bash
casper-java-sdk/build/libs/casper-java-sdk-0.5.0.jar
```

 Now copy this JAR to a subfolder of your client application

```
[client_project]/lib/casper-java-sdk-0.5.0.jar
```

If you're using gradle for your project you can now include this in your build file:

```bash
dependencies {
    compile files('lib/casper-java-sdk-0.5.0.jar')
}
```

If you're not using gradle, you can import the JAR into the project using your IDE

## How To: Query a node ?

See [here](https://github.com/casper-network/casper-java-sdk/tree/main/src/test/java/com/casper/sdk/how_to/how_to_query_a_node/QueryANode.class).

## How To: Transfer funds between 2 accounts ?

See [here](https://github.com/casper-network/casper-java-sdk/tree/main/src/test/java/com/casper/sdk/how_to/how_to_transfer_between_accounts/TransferBetweenAccounts.class).

## How To: Delegate funds to a validator ?

See [here]

## How To: Undelegate funds from a validator ?

See [here]

## How To: Stake funds as a validator ?

See [here]

## How To: Unstake funds as a validator ?

See [here]

## How To: Install a smart contract ?

See [here]

## How To: Invoke a smart contract ?

See [here]

##### 
