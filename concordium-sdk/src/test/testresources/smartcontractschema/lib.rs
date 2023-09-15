#![cfg_attr(not(feature = "std"), no_std)]

//! # A Concordium V1 smart contract
use concordium_std::*;
use core::fmt::Debug;

/// Your smart contract state.
#[derive(Serialize, SchemaType)]
pub struct State {
    // Your state
}

/// Your smart contract errors.
#[derive(Debug, PartialEq, Eq, Reject, Serial, SchemaType)]
enum Error {
    /// Failed parsing the parameter.
    #[from(ParseError)]
    ParseParams,
    /// Your error
    YourError,
}

/// Init function that creates a new smart contract.
#[init(contract = "java_sdk_schema_unit_test")]
fn init<S: HasStateApi>(
    _ctx: &impl HasInitContext,
    _state_builder: &mut StateBuilder<S>,
) -> InitResult<State> {
    // Your code

    Ok(State {})
}

// 
#[derive(Serialize, SchemaType)]
#[concordium(transparent)]
struct ListParam (
    Vec<ContractAddress>
);


#[derive(Serialize, SchemaType)]
struct AbstractAddressContainer {
    /// The amount of tokens to unwrap.
    address:   Address,
}

#[derive(Serialize, SchemaType)]
struct AccountAddressContainer {
    /// The amount of tokens to unwrap.
    address:   AccountAddress,
}

#[derive(Serialize, SchemaType)]
struct ContractAddressContainer {
    /// The amount of tokens to unwrap.
    address:   ContractAddress,
}

/// Takes ListParam as parameter for ensuring correct serialization of ListParam
#[receive(
    contract = "java_sdk_schema_unit_test",
    name = "list_param_test",
    parameter = "ListParam",
    error = "Error",
    mutable
)]
fn list_param_test<S: HasStateApi>(
    ctx: &impl HasReceiveContext,
    _host: &mut impl HasHost<State, StateApiType = S>,
) -> Result<(), Error> {
    Ok(())
}

/// Takes AddressContainer as parameter for ensuring correct serialization of Address
#[receive(
    contract = "java_sdk_schema_unit_test",
    name = "abstract_address_container_test",
    parameter = "AbstractAddressContainer",
    error = "Error",
    mutable
)]
fn abstract_address_container_test<S: HasStateApi>(
    ctx: &impl HasReceiveContext,
    _host: &mut impl HasHost<State, StateApiType = S>,
) -> Result<(), Error> {
    Ok(())
}

/// Takes Address as parameter for ensuring correct serialization of Address when passed directly
#[receive(
    contract = "java_sdk_schema_unit_test",
    name = "abstract_address_test",
    parameter = "Address",
    error = "Error",
    mutable
)]
fn abstract_address_test<S: HasStateApi>(
    ctx: &impl HasReceiveContext,
    _host: &mut impl HasHost<State, StateApiType = S>,
) -> Result<(), Error> {
    Ok(())
}

/// Takes a ContractAddressContainer as parameter for ensuring correct serialization of ContractAddress
#[receive(
    contract = "java_sdk_schema_unit_test",
    name = "contract_address_container_test",
    parameter = "ContractAddressContainer",
    error = "Error",
    mutable
)]
fn contract_address_container_test<S: HasStateApi>(
    ctx: &impl HasReceiveContext,
    _host: &mut impl HasHost<State, StateApiType = S>,
) -> Result<(), Error> {
    Ok(())
}
/// Takes Address as parameter for ensuring correct serialization of Address when passed directly
#[receive(
    contract = "java_sdk_schema_unit_test",
    name = "contract_address_test",
    parameter = "ContractAddress",
    error = "Error",
    mutable
)]
fn contract_address_test<S: HasStateApi>(
    ctx: &impl HasReceiveContext,
    _host: &mut impl HasHost<State, StateApiType = S>,
) -> Result<(), Error> {
    Ok(())
}

/// Takes an AccountAddressContainer as parameter for ensuring correct serialization of AccountAddress
#[receive(
    contract = "java_sdk_schema_unit_test",
    name = "account_address_container_test",
    parameter = "AccountAddressContainer",
    error = "Error",
    mutable
)]
fn account_address_container_test<S: HasStateApi>(
    ctx: &impl HasReceiveContext,
    _host: &mut impl HasHost<State, StateApiType = S>,
) -> Result<(), Error> {
    Ok(())
}

/// Takes Address as parameter for ensuring correct serialization of Address when passed directly
#[receive(
    contract = "java_sdk_schema_unit_test",
    name = "account_address_test",
    parameter = "AccountAddress",
    error = "Error",
    mutable
)]
fn account_address_test<S: HasStateApi>(
    ctx: &impl HasReceiveContext,
    _host: &mut impl HasHost<State, StateApiType = S>,
) -> Result<(), Error> {
    Ok(())
}

/// View function that returns the content of the state.
#[receive(contract = "java_sdk_schema_unit_test", name = "view", return_value = "State")]
fn view<'b, S: HasStateApi>(
    _ctx: &impl HasReceiveContext,
    host: &'b impl HasHost<State, StateApiType = S>,
) -> ReceiveResult<&'b State> {
    Ok(host.state())
}

#[concordium_cfg_test]
mod tests {
    use super::*;
    use test_infrastructure::*;

    type ContractResult<A> = Result<A, Error>;

    #[concordium_test]
    /// Test that initializing the contract succeeds with some state.
    fn test_init() {
        let ctx = TestInitContext::empty();

        let mut state_builder = TestStateBuilder::new();

        let state_result = init(&ctx, &mut state_builder);
        state_result.expect_report("Contract initialization results in error");
    }

    #[concordium_test]
    /// Test that invoking the `receive` endpoint with the `false` parameter
    /// succeeds in updating the contract.
    fn test_throw_no_error() {
        let ctx = TestInitContext::empty();

        let mut state_builder = TestStateBuilder::new();

        // Initializing state
        let initial_state = init(&ctx, &mut state_builder).expect("Initialization should pass");

        let mut ctx = TestReceiveContext::empty();

        let throw_error = false;
        let parameter_bytes = to_bytes(&throw_error);
        ctx.set_parameter(&parameter_bytes);

        let mut host = TestHost::new(initial_state, state_builder);

        // Call the contract function.
        let result: ContractResult<()> = receive(&ctx, &mut host);

        // Check the result.
        claim!(result.is_ok(), "Results in rejection");
    }

    #[concordium_test]
    /// Test that invoking the `receive` endpoint with the `true` parameter
    /// results in the `YourError` being thrown.
    fn test_throw_error() {
        let ctx = TestInitContext::empty();

        let mut state_builder = TestStateBuilder::new();

        // Initializing state
        let initial_state = init(&ctx, &mut state_builder).expect("Initialization should pass");

        let mut ctx = TestReceiveContext::empty();

        let throw_error = true;
        let parameter_bytes = to_bytes(&throw_error);
        ctx.set_parameter(&parameter_bytes);

        let mut host = TestHost::new(initial_state, state_builder);

        // Call the contract function.
        let error: ContractResult<()> = receive(&ctx, &mut host);

        // Check the result.
        claim_eq!(error, Err(Error::YourError), "Function should throw an error.");
    }
}
