#![cfg_attr(not(feature = "std"), no_std)]

//! # A Concordium V1 smart contract. This contract is only for genereating a schema to test serialization of `SchemaParameter` and is never meant to be excecuted, hence the dummy state/error/init.
use concordium_std::*;
use core::fmt::Debug;

/// Your smart contract state.
#[derive(Serialize, SchemaType)]
pub struct State {
    // Dummy state.
}

/// Dummy errors
#[derive(Debug, PartialEq, Eq, Reject, Serial, SchemaType)]
enum Error {
    /// Failed parsing the parameter.
    #[from(ParseError)]
    ParseParams,
    /// Your error
    YourError,
}

/// Dummy init functions
#[init(contract = "java_sdk_schema_unit_test")]
fn init<S: HasStateApi>(
    _ctx: &impl HasInitContext,
    _state_builder: &mut StateBuilder<S>,
) -> InitResult<State> {
    // Your code

    Ok(State {})
}


#[derive(Serialize, SchemaType)]
#[concordium(transparent)]
struct ListParam (
    Vec<ContractAddress>
);


#[derive(Serialize, SchemaType)]
struct AbstractAddressContainer {
    address:   Address,
}

#[derive(Serialize, SchemaType)]
struct AccountAddressContainer {
    address:   AccountAddress,
}

#[derive(Serialize, SchemaType)]
struct ContractAddressContainer {
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

