# Examples for [Concordium SDK](../concordium-sdk/)
This project comprises for CLI applications in the [examples](./src/main/java/com/concordium/sdk/examples/) directory. Each file is an independent CLI application which acts as an example for various methods [Concordium GRPC client](../concordium-sdk/src/main/java/com/concordium/sdk/ClientV2.java) implements

## Running Cli Applications
Each cli application can be run by standard procedures to run any java application with allows for specifying parameters. Example

```bash
make
mvn -f ./concordium-sdk/pom.xml clean install
mvn -f ./concordium-sdk-examples/pom.xml clean install
java -cp "./target/concordium-sdk-examples-jar-with-dependencies.jar" com.concordium.sdk.examples.<COMMAND_NAME> --endpoint=http://localhost:20001
```
java -cp "./target/concordium-sdk-examples-jar-with-dependencies.jar" com.concordium.sdk.examples.GetBlockItemStatus --endpoint=http://localhost:20001
