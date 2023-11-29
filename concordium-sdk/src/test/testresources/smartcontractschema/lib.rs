#![cfg_attr(not(feature = "std"), no_std)]

//! # A Concordium V1 smart contract. This contract is only for genereating a schema to test serialization of `SchemaParameter` and is never meant to be excecuted, hence the dummy state/error/init.
use concordium_std::*;

/// Your smart contract state.
#[derive(Serialize, SchemaType)]
pub struct State {
    // Dummy state.
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

// The `ParseError` in the following result types is never thrown, as the functions only exist to generate a schema for serialization of SC parameters.

/// Takes [`ListParam`] as parameter for ensuring correct serialization of the java class ListParam.
#[receive(
    contract = "java_sdk_schema_unit_test",
    name = "list_param_test",
    parameter = "ListParam",
    mutable
)]
fn list_param_test<S: HasStateApi>(
    ctx: &impl HasReceiveContext,
    _host: &mut impl HasHost<State, StateApiType = S>,
) -> Result<(), ParseError> {
    Ok(())
}

/// Takes [`AbstractAddressContainer`] as parameter for ensuring correct serialization of the java class AbstractAddress when used inside other parameters.
#[receive(
    contract = "java_sdk_schema_unit_test",
    name = "abstract_address_container_test",
    parameter = "AbstractAddressContainer",
    mutable
)]
fn abstract_address_container_test<S: HasStateApi>(
    ctx: &impl HasReceiveContext,
    _host: &mut impl HasHost<State, StateApiType = S>,
) -> Result<(), ParseError> {
    Ok(())
}

/// Takes [`Address`] as parameter for ensuring correct serialization of the java class AbstractAddressParam for passing AbstractAddress directly as a parameter.
#[receive(
    contract = "java_sdk_schema_unit_test",
    name = "abstract_address_test",
    parameter = "Address",
    mutable
)]
fn abstract_address_test<S: HasStateApi>(
    ctx: &impl HasReceiveContext,
    _host: &mut impl HasHost<State, StateApiType = S>,
) -> Result<(), ParseError> {
    Ok(())
}

/// Takes a [`ContractAddressContainer`] as parameter for ensuring correct serialization of the java class ContractAddress when used inside other parameters.
#[receive(
    contract = "java_sdk_schema_unit_test",
    name = "contract_address_container_test",
    parameter = "ContractAddressContainer",
    mutable
)]
fn contract_address_container_test<S: HasStateApi>(
    ctx: &impl HasReceiveContext,
    _host: &mut impl HasHost<State, StateApiType = S>,
) -> Result<(), ParseError> {
    Ok(())
}

/// Takes [`ContractAddress`] as parameter for ensuring correct serialization of the java class ContractAddressParam for passing ContractAddress directly as a parameter.
#[receive(
    contract = "java_sdk_schema_unit_test",
    name = "contract_address_test",
    parameter = "ContractAddress",
    mutable
)]
fn contract_address_test<S: HasStateApi>(
    ctx: &impl HasReceiveContext,
    _host: &mut impl HasHost<State, StateApiType = S>,
) -> Result<(), ParseError> {
    Ok(())
}

/// Takes an [`AccountAddressContainer`] as parameter for ensuring correct serialization of the java class AccountAddress when used inside other parameters.
#[receive(
    contract = "java_sdk_schema_unit_test",
    name = "account_address_container_test",
    parameter = "AccountAddressContainer",
    mutable
)]
fn account_address_container_test<S: HasStateApi>(
    ctx: &impl HasReceiveContext,
    _host: &mut impl HasHost<State, StateApiType = S>,
) -> Result<(), ParseError> {
    Ok(())
}

/// Takes [`AccoountAddress`] as parameter for ensuring correct serialization of the java class AccountAddressParam for passing AccountAddress directly as a parameter.
#[receive(
    contract = "java_sdk_schema_unit_test",
    name = "account_address_test",
    parameter = "AccountAddress",
    mutable
)]
fn account_address_test<S: HasStateApi>(
    ctx: &impl HasReceiveContext,
    _host: &mut impl HasHost<State, StateApiType = S>,
) -> Result<(), ParseError> {
    Ok(())
}