# Scripts
This readme describes the usage of scripts in this folder.
## bootstrap
The bootstrap script fetches the lasted from the 'cspr-standard-test-resources' git submodule that contains the e2e 
cucumber features and associated test resources. The bootstrap script also creates a symbolic link needed by IntelliJ 
to perform debugging of the features files within the IDE rather than having to remote debug to a gradle build.

## docker-bash
Opens a bash terminal to the cspr-nctl docker container

## docker-copy-assets
Copies the required assent such a account key pairs from a running casper-nctl docker container

## docker-run
Runs the casper-nctl docker container

## docker-stop
Stops a running casper-nctl docker container

## e2e-remote-debug
Remote debugs one or more cucumber e2e tests, the 1st parameter is the cucumber name regex. If not specified all test run, eg:

```./script/e2e-remote-debug info_get_peers```

The above example will start the JVM in debug mode and only execute the info_get_peers scenario.

## e2e-test
Executes the cucumber e2e tests against a docker instance. To run tests filtered by regex use the following:

```./script/e2e-test -Dcucumber.name=info_get_status```

where info_get_status is the pattern to filter on

## remote-test-resources
Deletes the git submodule 'cspr-standard-test-resources'. 

**IMPORTANT NOTE**: DO NOT RUN UNLESS YOU REALLY WANT TO DELETE SUBMODULES

## test
Executes the JUnit tests
