use anyhow::{anyhow, Result};
pub use concordium_base::common::types::{AccountAddress, ACCOUNT_ADDRESS_SIZE};
use concordium_base::{
    base,
    common::*,
    contracts_common::{schema::VersionedModuleSchema, Amount},
    encrypted_transfers::{self, types::{
        AggregatedDecryptedAmount, EncryptedAmount, EncryptedAmountTransferData,
        IndexedEncryptedAmount, SecToPubAmountTransferData,
    }},
    id::{self, constants::{self, ArCurve, AttributeKind}, types::AttributeTag, curve_arithmetic::Curve, elgamal, id_proof_types::AtomicStatement, types::GlobalContext},
    transactions::{AddBakerKeysMarker, BakerKeysPayload, ConfigureBakerKeysPayload}, web3id::{Request, Web3IdAttribute},
};
use core::slice;
use ed25519_dalek::*;

use jni::{
    objects::{JClass, JString, JValue, JObject},
    sys::{jboolean, jbyteArray, jint, jlong, jstring, JNI_FALSE},
    JNIEnv,
};
use rand::thread_rng;
use serde_json::{from_str, to_string};
use std::{
    collections::HashSet, convert::{From, TryFrom, TryInto}, i8, str::Utf8Error
};
use wallet_library::{
    credential::{
        self, create_unsigned_credential_v1_aux, serialize_credential_deployment_payload,
        CredentialDeploymentDetails, CredentialDeploymentPayload,
    }, identity::{create_identity_object_request_v1_aux, create_identity_recovery_request_aux}, proofs::Web3IdProofInput, statement::{AcceptableRequest, RequestCheckError}, wallet::{
        get_account_public_key_aux, get_account_signing_key_aux, get_attribute_commitment_randomness_aux, get_credential_id_aux, get_id_cred_sec_aux, get_prf_key_aux, get_signature_blinding_randomness_aux, get_verifiable_credential_backup_encryption_key_aux, get_verifiable_credential_public_key_aux, get_verifiable_credential_signing_key_aux
    }
};
use wallet_library::statement::{AcceptableAtomicStatement, WalletConfigRules};

const SUCCESS: i32 = 0;
const NATIVE_CONVERSION_ERROR: i32 = 1;
const MALFORMED_SECRET_KEY: i32 = 2;
const MALFORMED_PUBLIC_KEY: i32 = 3;
const SIGNATURE_VERIFICATION_FAILURE: i32 = 4;
const SECRET_KEY_GENERATION_FAILURE: i32 = 5;
const PUBLIC_KEY_GENERATION_FAILURE: i32 = 6;

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_sign(
    env: JNIEnv,
    _class: JClass,
    secretKeyBytes: jbyteArray,
    messageBytes: jbyteArray,
    out: jbyteArray,
) -> jint {
    let secretKeyBytes = match env.convert_byte_array(secretKeyBytes) {
        Ok(x) => x,
        _ => return NATIVE_CONVERSION_ERROR,
    };

    let secret_key_bytes_array: [u8; 32] = match secretKeyBytes.try_into() {
        Ok(s) => s,
        Err(_) => return MALFORMED_SECRET_KEY,
    };

    let signing_key = SigningKey::from_bytes(&secret_key_bytes_array);
    let bytesToSign = match env.convert_byte_array(messageBytes) {
        Ok(x) => x,
        _ => return NATIVE_CONVERSION_ERROR,
    };
    let signature = signing_key.sign(&bytesToSign);
    let signatureBytesU8 = signature.to_bytes();

    let signatureBytesI8: &[i8] = unsafe {
        slice::from_raw_parts(
            signatureBytesU8.as_ptr() as *const i8,
            signatureBytesU8.len(),
        )
    };

    match env.set_byte_array_region(out, 0, signatureBytesI8) {
        Ok(_) => SUCCESS,
        _ => NATIVE_CONVERSION_ERROR,
    }
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_verify(
    env: JNIEnv,
    _class: JClass,
    pub_key_bytes: jbyteArray,
    msg_bytes: jbyteArray,
    sig_bytes: jbyteArray,
) -> jint {
    let public_key_bytes = match env.convert_byte_array(pub_key_bytes) {
        Ok(x) => x,
        _ => return NATIVE_CONVERSION_ERROR,
    };

    let public_key_bytes_array: [u8; 32] = match public_key_bytes.try_into() {
        Ok(s) => s,
        Err(_) => return MALFORMED_PUBLIC_KEY,
    };

    let public_key = match VerifyingKey::from_bytes(&public_key_bytes_array) {
        Ok(x) => x,
        _ => return MALFORMED_PUBLIC_KEY,
    };

    let message_bytes = match env.convert_byte_array(msg_bytes) {
        Ok(x) => x,
        _ => return NATIVE_CONVERSION_ERROR,
    };

    let signature_bytes = match env.convert_byte_array(sig_bytes) {
        Ok(x) => x,
        _ => return NATIVE_CONVERSION_ERROR,
    };

    let signature: Signature = match Signature::try_from(&signature_bytes[..]) {
        Ok(sig) => sig,
        _ => return NATIVE_CONVERSION_ERROR,
    };

    match public_key.verify(&message_bytes, &signature) {
        Ok(_) => SUCCESS,
        _ => SIGNATURE_VERIFICATION_FAILURE,
    }
}

#[no_mangle]
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_generateSecretKey(
    env: JNIEnv,
    _class: JClass,
    out: jbyteArray,
) -> jint {
    let mut csprng = rand::rngs::OsRng {};
    let secret_key = SigningKey::generate(&mut csprng);
    let secret_key_bytes = secret_key.to_bytes();

    let secret_key_bytes_i8: &[i8] = unsafe {
        slice::from_raw_parts(
            secret_key_bytes.as_ptr() as *const i8,
            secret_key_bytes.len(),
        )
    };

    match env.set_byte_array_region(out, 0, secret_key_bytes_i8) {
        Ok(_) => SUCCESS,
        _ => SECRET_KEY_GENERATION_FAILURE,
    }
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_generatePublicKey(
    env: JNIEnv,
    _class: JClass,
    secretKeyBytes: jbyteArray,
    out: jbyteArray,
) -> jint {
    let secretKeyBytes = match env.convert_byte_array(secretKeyBytes) {
        Ok(x) => x,
        _ => return NATIVE_CONVERSION_ERROR,
    };

    let secret_key_bytes_array: [u8; 32] = match secretKeyBytes.try_into() {
        Ok(s) => s,
        Err(_) => return MALFORMED_SECRET_KEY,
    };

    let signing_key = SigningKey::from_bytes(&secret_key_bytes_array);
    let public_key = signing_key.verifying_key();
    let public_key_bytes = public_key.to_bytes();

    let public_key_bytes_i8: &[i8] = unsafe {
        slice::from_raw_parts(
            public_key_bytes.as_ptr() as *const i8,
            public_key_bytes.len(),
        )
    };

    match env.set_byte_array_region(out, 0, public_key_bytes_i8) {
        Ok(_) => SUCCESS,
        _ => PUBLIC_KEY_GENERATION_FAILURE,
    }
}

#[derive(SerdeSerialize, SerdeDeserialize)]
enum CryptoJniResult<T> {
    Ok(T),
    Err(JNIErrorResponse),
}

#[derive(SerdeSerialize, SerdeDeserialize)]
#[allow(non_snake_case)]
enum JNIErrorResponseType {
    ParameterSerialization,
    Utf8Decode,
    JsonDeserialization,
    NativeConversion,
    PayloadCreation,
}

#[derive(SerdeSerialize, SerdeDeserialize)]
#[allow(non_snake_case)]
struct JNIErrorResponse {
    errorType:    JNIErrorResponseType,
    errorMessage: String,
}

impl<T> From<serde_json::Error> for CryptoJniResult<T> {
    fn from(e: serde_json::Error) -> Self {
        let error = JNIErrorResponse {
            errorType:    JNIErrorResponseType::JsonDeserialization,
            errorMessage: e.to_string(),
        };
        CryptoJniResult::Err(error)
    }
}

impl<T> From<Utf8Error> for CryptoJniResult<T> {
    fn from(e: Utf8Error) -> Self {
        let error = JNIErrorResponse {
            errorType:    JNIErrorResponseType::Utf8Decode,
            errorMessage: e.to_string(),
        };
        CryptoJniResult::Err(error)
    }
}

impl<T> From<jni::errors::Error> for CryptoJniResult<T> {
    fn from(e: jni::errors::Error) -> Self {
        let error = JNIErrorResponse {
            errorType:    JNIErrorResponseType::NativeConversion,
            errorMessage: e.to_string(),
        };
        CryptoJniResult::Err(error)
    }
}

impl<T> From<concordium_base::web3id::ProofError> for CryptoJniResult<T> {
    fn from(e: concordium_base::web3id::ProofError) -> Self {
        let error = JNIErrorResponse {
            errorType:    JNIErrorResponseType::NativeConversion,
            errorMessage: e.to_string(),
        };
        CryptoJniResult::Err(error)
    }
}
/// Creates errors from strings. Used when payload creation fails as no error to
/// be passed on is generated.
impl<T> From<&str> for CryptoJniResult<T> {
    fn from(e: &str) -> Self {
        let error = JNIErrorResponse {
            errorType:    JNIErrorResponseType::PayloadCreation,
            errorMessage: e.to_string(),
        };
        CryptoJniResult::Err(error)
    }
}

impl<T> From<anyhow::Error> for CryptoJniResult<T> {
    fn from(e: anyhow::Error) -> Self {
        let error = JNIErrorResponse {
            errorType:    JNIErrorResponseType::ParameterSerialization,
            errorMessage: e.to_string(),
        };
        CryptoJniResult::Err(error)
    }
}

const PAYLOAD_CREATION_ERROR: &str = "Could not create payload";

impl<T: serde::Serialize> CryptoJniResult<T> {
    fn to_jstring(&self, env: &JNIEnv) -> jstring {
        let json_str = to_string(self).unwrap();
        let out = env.new_string(json_str).unwrap();
        out.into_inner()
    }
}

fn decrypt_encrypted_amount(
    encrypted_amount: EncryptedAmount<ArCurve>,
    secret: elgamal::SecretKey<ArCurve>,
) -> CryptoJniResult<Amount> {
    let global = id::types::GlobalContext::<id::constants::ArCurve>::generate(String::from(
        "genesis_string",
    ));
    let m = 1 << 16;
    let table = elgamal::BabyStepGiantStep::new(global.encryption_in_exponent_generator(), m);
    CryptoJniResult::Ok(
        encrypted_transfers::decrypt_amount::<id::constants::ArCurve>(
            &table,
            &secret,
            &encrypted_amount,
        ),
    )
}

#[derive(Serialize, SerdeSerialize, SerdeDeserialize)]
#[serde(bound(serialize = "C: Curve", deserialize = "C: Curve"))]
#[serde(rename_all = "camelCase")]
struct JniInput<C: Curve> {
    global:                 GlobalContext<C>,
    amount:                 Amount,
    sender_secret_key:      elgamal::SecretKey<C>,
    input_encrypted_amount: IndexedEncryptedAmount<C>,
}

type EncryptedTranfersInput = JniInput<ArCurve>;
type EncryptedTransferResult = CryptoJniResult<SecToPubAmountTransferData<ArCurve>>;

#[no_mangle]
#[allow(non_snake_case)]
/// The JNI wrapper for the `create_sec_to_pub_transfer` method.
/// The `input` parameter must be a properly initalized `java.lang.String` that
/// is non-null. The input must be valid JSON according to specified format
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_createSecToPubTransfer(
    env: JNIEnv,
    _: JClass,
    input: JString,
) -> jstring {
    let input: EncryptedTranfersInput = match env.get_string(input) {
        Ok(java_str) => match java_str.to_str() {
            Ok(rust_str) => match from_str(rust_str) {
                Ok(input) => input,
                Err(err) => return EncryptedTransferResult::from(err).to_jstring(&env),
            },
            Err(err) => return EncryptedTransferResult::from(err).to_jstring(&env),
        },
        Err(err) => return EncryptedTransferResult::from(err).to_jstring(&env),
    };

    let decrypted_amount = match decrypt_encrypted_amount(
        input.input_encrypted_amount.encrypted_chunks.clone(),
        input.sender_secret_key.clone(),
    ) {
        CryptoJniResult::Ok(amount) => amount,
        CryptoJniResult::Err(err) => return EncryptedTransferResult::Err(err).to_jstring(&env),
    };

    let input_amount: AggregatedDecryptedAmount<ArCurve> = AggregatedDecryptedAmount {
        agg_encrypted_amount: input.input_encrypted_amount.encrypted_chunks,
        agg_index:            encrypted_transfers::types::EncryptedAmountAggIndex {
            index: input.input_encrypted_amount.index.index,
        },
        agg_amount:           decrypted_amount,
    };

    let mut csprng = thread_rng();

    let payload = encrypted_transfers::make_sec_to_pub_transfer_data(
        &input.global,
        &input.sender_secret_key,
        &input_amount,
        input.amount,
        &mut csprng,
    );

    match payload {
        Some(payload) => CryptoJniResult::Ok(payload).to_jstring(&env),
        None => EncryptedTransferResult::from(PAYLOAD_CREATION_ERROR).to_jstring(&env),
    }
}

#[derive(Serialize, SerdeSerialize, SerdeDeserialize)]
#[serde(bound(serialize = "C: Curve", deserialize = "C: Curve"))]
#[serde(rename_all = "camelCase")]
struct TransferJniInput<C: Curve> {
    global:                 GlobalContext<C>,
    receiver_public_key:    elgamal::PublicKey<C>,
    sender_secret_key:      elgamal::SecretKey<C>,
    amount_to_send:         Amount,
    input_encrypted_amount: IndexedEncryptedAmount<C>,
}

type EncryptedAmountTranfersInput = TransferJniInput<ArCurve>;
type EncryptedAmountTransferResult = CryptoJniResult<EncryptedAmountTransferData<ArCurve>>;

#[no_mangle]
#[allow(non_snake_case)]
/// The JNI wrapper for the `create_sec_to_pub_transfer` method.
/// The `input` parameter must be a properly initalized `java.lang.String` that
/// is non-null. The input must be valid JSON according to specified format
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_generateEncryptedTransfer(
    env: JNIEnv,
    _: JClass,
    input: JString,
) -> jstring {
    let input: EncryptedAmountTranfersInput = match env.get_string(input) {
        Ok(java_str) => match java_str.to_str() {
            Ok(rust_str) => match from_str(rust_str) {
                Ok(input) => input,
                Err(err) => return EncryptedAmountTransferResult::from(err).to_jstring(&env),
            },
            Err(err) => return EncryptedAmountTransferResult::from(err).to_jstring(&env),
        },
        Err(err) => return EncryptedAmountTransferResult::from(err).to_jstring(&env),
    };

    let decrypted_amount = match decrypt_encrypted_amount(
        input.input_encrypted_amount.encrypted_chunks.clone(),
        input.sender_secret_key.clone(),
    ) {
        CryptoJniResult::Ok(amount) => amount,
        CryptoJniResult::Err(err) => {
            return EncryptedAmountTransferResult::Err(err).to_jstring(&env)
        }
    };

    let input_amount: AggregatedDecryptedAmount<ArCurve> = AggregatedDecryptedAmount {
        agg_encrypted_amount: input.input_encrypted_amount.encrypted_chunks,
        agg_index:            encrypted_transfers::types::EncryptedAmountAggIndex {
            index: input.input_encrypted_amount.index.index,
        },
        agg_amount:           decrypted_amount,
    };

    let mut csprng = thread_rng();

    let payload = encrypted_transfers::make_transfer_data(
        &input.global,
        &input.receiver_public_key,
        &input.sender_secret_key,
        &input_amount,
        input.amount_to_send,
        &mut csprng,
    );

    match payload {
        Some(payload) => CryptoJniResult::Ok(payload).to_jstring(&env),
        None => EncryptedAmountTransferResult::from(PAYLOAD_CREATION_ERROR).to_jstring(&env),
    }
}

#[no_mangle]
/// The JNI wrapper for the `generate_baker_keys` method.
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_generateBakerKeys(
    env: JNIEnv,
    _: JClass,
) -> jstring {
    let mut csprng = thread_rng();
    let payload = base::BakerKeyPairs::generate(&mut csprng);
    CryptoJniResult::Ok(payload).to_jstring(&env)
}

#[derive(Serialize, SerdeSerialize, SerdeDeserialize)]
#[serde(rename_all = "camelCase")]
struct AddBakerPayloadInput {
    pub sender: AccountAddress,
    pub keys:   base::BakerKeyPairs,
}

type AddBakerResult = CryptoJniResult<BakerKeysPayload<AddBakerKeysMarker>>;

#[no_mangle]
#[allow(non_snake_case)]
/// The JNI wrapper to create new configure baker keys payload.
/// Construct a BakerKeysPayload with proofs for updating baker keys.
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_generateConfigureBakerKeysPayload(
    env: JNIEnv,
    _: JClass,
    input: JString,
) -> jstring {
    let input: AddBakerPayloadInput = match env.get_string(input) {
        Ok(java_str) => match java_str.to_str() {
            Ok(rust_str) => match from_str(rust_str) {
                Ok(input) => input,
                Err(err) => return AddBakerResult::from(err).to_jstring(&env),
            },
            Err(err) => return AddBakerResult::from(err).to_jstring(&env),
        },
        Err(err) => return AddBakerResult::from(err).to_jstring(&env),
    };

    let mut csprng = thread_rng();

    let payload = ConfigureBakerKeysPayload::new(&input.keys, input.sender, &mut csprng);

    CryptoJniResult::Ok(payload).to_jstring(&env)
}

type SerializeParamResult = CryptoJniResult<String>;

#[no_mangle]
#[allow(non_snake_case)]
/// The JNI wrapper for serializing ia parameter for a receive function
/// according to a specified Schema. Returns a SerializeParamResult containg the
/// hex encoded serialized parameter.
///
/// - `parameter` is the parameter to serialize and must be a properly
///   initalized `java.lang.String` that is non-null. The parameter must be
///   valid JSON according to the provided schema.
/// - `contractName` is the name of the contract and must be a properly
///   initalized `java.lang.String` that is non-null.
/// - `methodName` is the name of the method and must be a properly initalized
///   `java.lang.String` that is non-null.
/// - `schemaBytes` is the bytes of the schema and must be a properly initalized
///   `byte[]` containing bytes representing a valid schema. The schema must
///   match the provided `contractName`.
/// - `schemaVersion` is the version of the Schema and must be an integer
///   representing a valid
///   `com.concordium.sdk.transactions.smartcontracts.SchemaVersion`.
/// - `verboseErrors`, whether errors are returned in verbose format or not, can
///   be useful when debugging why serialization fails.
///
/// If serialization fails returns a SerializeParamResult containing a JNIError
/// detailing what went wrong.
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_serializeReceiveParameter(
    env: JNIEnv,
    _: JClass,
    parameter: JString,
    contractName: JString,
    methodName: JString,
    schemaBytes: jbyteArray,
    schemaVersion: jint,
    verboseErrors: jboolean,
) -> jstring {
    let schema = match env.convert_byte_array(schemaBytes) {
        Ok(x) => x,
        Err(err) => return SerializeParamResult::from(err).to_jstring(&env),
    };

    let parameter: String = match env.get_string(parameter) {
        Ok(java_str) => match java_str.to_str() {
            Ok(rust_str) => String::from(rust_str),
            Err(err) => return SerializeParamResult::from(err).to_jstring(&env),
        },
        Err(err) => return SerializeParamResult::from(err).to_jstring(&env),
    };

    let contractName: String = match env.get_string(contractName) {
        Ok(java_str) => match java_str.to_str() {
            Ok(rust_str) => String::from(rust_str),
            Err(err) => return SerializeParamResult::from(err).to_jstring(&env),
        },
        Err(err) => return SerializeParamResult::from(err).to_jstring(&env),
    };

    let methodName: String = match env.get_string(methodName) {
        Ok(java_str) => match java_str.to_str() {
            Ok(rust_str) => String::from(rust_str),
            Err(err) => return SerializeParamResult::from(err).to_jstring(&env),
        },
        Err(err) => return SerializeParamResult::from(err).to_jstring(&env),
    };

    let version: Option<u8> = match schemaVersion {
        0 => Some(0),
        1 => Some(1),
        2 => Some(2),
        3 => Some(3),
        _ => None,
    };

    let verboseErrors = verboseErrors != JNI_FALSE;

    let serializedParameter = serialize_receive_contract_parameters_aux(
        &parameter,
        &contractName,
        &methodName,
        &schema,
        version,
        verboseErrors,
    );

    let result = match serializedParameter {
        Ok(serializedParameter) => hex::encode(serializedParameter),
        Err(err) => return SerializeParamResult::from(err).to_jstring(&env),
    };

    SerializeParamResult::Ok(result).to_jstring(&env)
}

#[allow(non_snake_case)]
/// Helper method for serialize_receive_parameters. Performs the actual
/// serialization.
pub fn serialize_receive_contract_parameters_aux(
    parameter: &str,
    contractName: &str,
    methodName: &str,
    schemaBytes: &[u8],
    schemaVersion: Option<u8>,
    verboseErrors: bool,
) -> Result<Vec<u8>> {
    let module_schema = VersionedModuleSchema::new(schemaBytes, &schemaVersion)?;
    let parameter_type = module_schema.get_receive_param_schema(contractName, methodName)?;
    let value: serde_json::Value = from_str(parameter)?;

    parameter_type
        .serial_value(&value)
        .map_err(|e| anyhow!("{}", e.display(verboseErrors)))
}

/// The JNI wrapper for serializing a parameter for an init function according
/// to a specified Schema. Returns a SerializeParamResult containg the hex
/// encoded serialized parameter.
///
/// - `parameter` is the parameter to serialize and must be a properly
///   initalized `java.lang.String` that is non-null. The parameter must be
///   valid JSON according to the provided schema.
/// - `contractName` is the name of the contract and must be a properly
///   initalized `java.lang.String` that is non-null.
/// - `schemaBytes` is the bytes of the schema and must be a properly initalized
///   `byte[]` containing bytes representing a valid schema. The schema must
///   match the provided `contractName`.
/// - `schemaVersion` is the version of the Schema and must be an integer
///   representing a valid
///   `com.concordium.sdk.transactions.smartcontracts.SchemaVersion`.
/// - `verboseErrors`, whether errors are returned in verbose format or not, can
///   be useful when debugging why serialization fails.
///
/// If serialization fails returns a SerializeParamResult containing a JNIError
/// detailing what went wrong.
#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_serializeInitParameter(
    env: JNIEnv,
    _: JClass,
    parameter: JString,
    contractName: JString,
    schemaBytes: jbyteArray,
    schemaVersion: jint,
    verboseErrors: jboolean,
) -> jstring {
    let schema = match env.convert_byte_array(schemaBytes) {
        Ok(x) => x,
        Err(err) => return SerializeParamResult::from(err).to_jstring(&env),
    };

    let parameter: String = match env.get_string(parameter) {
        Ok(java_str) => match java_str.to_str() {
            Ok(rust_str) => String::from(rust_str),
            Err(err) => return SerializeParamResult::from(err).to_jstring(&env),
        },
        Err(err) => return SerializeParamResult::from(err).to_jstring(&env),
    };

    let contractName: String = match env.get_string(contractName) {
        Ok(java_str) => match java_str.to_str() {
            Ok(rust_str) => String::from(rust_str),
            Err(err) => return SerializeParamResult::from(err).to_jstring(&env),
        },
        Err(err) => return SerializeParamResult::from(err).to_jstring(&env),
    };

    let version: Option<u8> = match schemaVersion {
        0 => Some(0),
        1 => Some(1),
        2 => Some(2),
        3 => Some(3),
        _ => None,
    };

    let verboseErrors = verboseErrors != JNI_FALSE;

    let serializedParameter =
        serialize_init_parameters_aux(&parameter, &contractName, &schema, version, verboseErrors);

    let result = match serializedParameter {
        Ok(serializedParameter) => hex::encode(serializedParameter),
        Err(err) => return SerializeParamResult::from(err).to_jstring(&env),
    };

    SerializeParamResult::Ok(result).to_jstring(&env)
}

/// Helper method for serialize_init_parameters. Performs the actual
/// serialization.
#[allow(non_snake_case)]
pub fn serialize_init_parameters_aux(
    parameter: &str,
    contractName: &str,
    schemaBytes: &[u8],
    schemaVersion: Option<u8>,
    verboseErrors: bool,
) -> Result<Vec<u8>> {
    let module_schema = VersionedModuleSchema::new(schemaBytes, &schemaVersion)?;
    let parameter_type = module_schema.get_init_param_schema(contractName)?;
    let value: serde_json::Value = from_str(parameter)?;

    parameter_type
        .serial_value(&value)
        .map_err(|e| anyhow!("{}", e.display(verboseErrors)))
}

fn get_string(env: JNIEnv, java_string: JString) -> Result<String, JNIErrorResponse> {
    let java_str = match env.get_string(java_string) {
        Ok(s) => s,
        Err(err) => {
            return Err(JNIErrorResponse {
                errorMessage: err.to_string(),
                errorType:    JNIErrorResponseType::JsonDeserialization,
            })
        }
    };

    let rust_str = match java_str.to_str() {
        Ok(s) => s,
        Err(err) => {
            return Err(JNIErrorResponse {
                errorMessage: err.to_string(),
                errorType:    JNIErrorResponseType::Utf8Decode,
            })
        }
    };

    Ok(rust_str.to_string())
}

/// Convenience struct for grouping the hex encoded seed with
/// the network in string form.
struct SeedAndNet {
    seed_as_hex: String,
    net_as_str:  String,
}

/// Parse the hex encoded seed and the network from Java types to
/// equivalent Rust types.
fn get_seed_and_net(
    seed_as_hex: JString,
    net_as_str: JString,
    env: JNIEnv,
) -> Result<SeedAndNet, JNIErrorResponse> {
    let seed = get_string(env, seed_as_hex)?;
    let net = get_string(env, net_as_str)?;
    Ok(SeedAndNet {
        seed_as_hex: seed,
        net_as_str:  net,
    })
}

type StringResult = CryptoJniResult<String>;

/// The JNI wrapper for getting the account signing key.
/// # Arguments
///
/// * `seedAsHex` - The seed as a hex string.
/// * `netAsStr` - The network type as a string. Must be 'Mainnet' or 'Testnet'.
/// * `identityProviderIndex` - The index of the identity provider which will be
///   interpreted as a u32
/// * `identityIndex` - The index of the identity which will be interpreted as a
///   u32.
/// * `credentialCounter` - The credential number of the credential to get the
///   signing key for which will be interpreted as a u32.
#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_getAccountSigningKey(
    env: JNIEnv,
    _: JClass,
    seedAsHex: JString,
    netAsStr: JString,
    identityProviderIndex: jint,
    identityIndex: jint,
    credentialCounter: jint,
) -> jstring {
    let seed_net = match get_seed_and_net(seedAsHex, netAsStr, env) {
        Ok(h) => h,
        Err(err) => return StringResult::Err(err).to_jstring(&env),
    };

    let account_signing_key = match get_account_signing_key_aux(
        seed_net.seed_as_hex,
        &seed_net.net_as_str,
        identityProviderIndex as u32,
        identityIndex as u32,
        credentialCounter as u32,
    ) {
        Ok(k) => k,
        Err(err) => return StringResult::from(err).to_jstring(&env),
    };

    CryptoJniResult::Ok(account_signing_key).to_jstring(&env)
}

/// The JNI wrapper for getting the account public key.
/// # Arguments
///
/// * `seedAsHex` - The seed as a hex string.
/// * `netAsStr` - The network type as a string. Must be 'Mainnet' or 'Testnet'.
/// * `identityProviderIndex` - The index of the identity provider which will be
///   interpreted as a u32
/// * `identityIndex` - The index of the identity which will be interpreted as a
///   u32.
/// * `credentialCounter` - The credential number of the credential to get the
///   public key for which will be interpreted as a u32.
#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_getAccountPublicKey(
    env: JNIEnv,
    _: JClass,
    seedAsHex: JString,
    netAsStr: JString,
    identityProviderIndex: jint,
    identityIndex: jint,
    credentialCounter: jint,
) -> jstring {
    let seed_net = match get_seed_and_net(seedAsHex, netAsStr, env) {
        Ok(h) => h,
        Err(err) => return StringResult::Err(err).to_jstring(&env),
    };

    let account_public_key = match get_account_public_key_aux(
        seed_net.seed_as_hex,
        &seed_net.net_as_str,
        identityProviderIndex as u32,
        identityIndex as u32,
        credentialCounter as u32,
    ) {
        Ok(k) => k,
        Err(err) => return StringResult::from(err).to_jstring(&env),
    };

    CryptoJniResult::Ok(account_public_key).to_jstring(&env)
}

/// The JNI wrapper for getting the IdCredSec.
/// # Arguments
///
/// * `seedAsHex` - The seed as a hex string.
/// * `netAsStr` - The network type as a string. Must be 'Mainnet' or 'Testnet'.
/// * `identityProviderIndex` - The index of the identity provider which will be
///   interpreted as a u32
/// * `identityIndex` - The index of the identity which will be interpreted as a
///   u32.
#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_getIdCredSec(
    env: JNIEnv,
    _: JClass,
    seedAsHex: JString,
    netAsStr: JString,
    identityProviderIndex: jint,
    identityIndex: jint,
) -> jstring {
    let seed_net = match get_seed_and_net(seedAsHex, netAsStr, env) {
        Ok(h) => h,
        Err(err) => return StringResult::Err(err).to_jstring(&env),
    };

    let id_cred_sec = match get_id_cred_sec_aux(
        seed_net.seed_as_hex,
        &seed_net.net_as_str,
        identityProviderIndex as u32,
        identityIndex as u32,
    ) {
        Ok(k) => k,
        Err(err) => return StringResult::from(err).to_jstring(&env),
    };

    CryptoJniResult::Ok(id_cred_sec).to_jstring(&env)
}

/// The JNI wrapper for getting the PRF key.
/// # Arguments
///
/// * `seedAsHex` - The seed as a hex string.
/// * `netAsStr` - The network type as a string. Must be 'Mainnet' or 'Testnet'.
/// * `identityProviderIndex` - The index of the identity provider which will be
///   interpreted as a u32
/// * `identityIndex` - The index of the identity which will be interpreted as a
///   u32.
#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_getPrfKey(
    env: JNIEnv,
    _: JClass,
    seedAsHex: JString,
    netAsStr: JString,
    identityProviderIndex: jint,
    identityIndex: jint,
) -> jstring {
    let seed_net = match get_seed_and_net(seedAsHex, netAsStr, env) {
        Ok(h) => h,
        Err(err) => return StringResult::Err(err).to_jstring(&env),
    };

    let prf_key = match get_prf_key_aux(
        seed_net.seed_as_hex,
        &seed_net.net_as_str,
        identityProviderIndex as u32,
        identityIndex as u32,
    ) {
        Ok(k) => k,
        Err(err) => return StringResult::from(err).to_jstring(&env),
    };

    CryptoJniResult::Ok(prf_key).to_jstring(&env)
}

/// The JNI wrapper for getting the credential id.
/// # Arguments
///
/// * `seedAsHex` - The seed as a hex string.
/// * `netAsStr` - The network type as a string. Must be 'Mainnet' or 'Testnet'.
/// * `identityProviderIndex` - The index of the identity provider which will be
///   interpreted as a u32
/// * `identityIndex` - The index of the identity which will be interpreted as a
///   u32.
/// * `credentialCounter` - The credential number of the credential to get the
///   credential id for  which will be interpreted as a u8.
/// * `onChainCommitmentKey` - The on chain commitment key. This value can be
///   retrieved from a node through its gRPC interface.
#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_getCredentialId(
    env: JNIEnv,
    _: JClass,
    seedAsHex: JString,
    netAsStr: JString,
    identityProviderIndex: jint,
    identityIndex: jint,
    credentialCounter: jint,
    onChainCommitmentKey: JString,
) -> jstring {
    let seed_net = match get_seed_and_net(seedAsHex, netAsStr, env) {
        Ok(h) => h,
        Err(err) => return StringResult::Err(err).to_jstring(&env),
    };

    let commitmentKey = match get_string(env, onChainCommitmentKey) {
        Ok(n) => n,
        Err(err) => return StringResult::Err(err).to_jstring(&env),
    };

    let credential_id = match get_credential_id_aux(
        seed_net.seed_as_hex,
        &seed_net.net_as_str,
        identityProviderIndex as u32,
        identityIndex as u32,
        credentialCounter as u8,
        &commitmentKey,
    ) {
        Ok(k) => k,
        Err(err) => return StringResult::from(err).to_jstring(&env),
    };

    CryptoJniResult::Ok(credential_id).to_jstring(&env)
}

/// The JNI wrapper for getting the signature blinding randomness.
/// # Arguments
///
/// * `seedAsHex` - The seed as a hex string.
/// * `netAsStr` - The network type as a string. Must be 'Mainnet' or 'Testnet'.
/// * `identityProviderIndex` - The index of the identity provider which will be
///   interpreted as a u32
/// * `identityIndex` - The index of the identity which will be interpreted as a
///   u32.
#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_getSignatureBlindingRandomness(
    env: JNIEnv,
    _: JClass,
    seedAsHex: JString,
    netAsStr: JString,
    identityProviderIndex: jint,
    identityIndex: jint,
) -> jstring {
    let seed_net = match get_seed_and_net(seedAsHex, netAsStr, env) {
        Ok(h) => h,
        Err(err) => return StringResult::Err(err).to_jstring(&env),
    };

    let blinding_randomness = match get_signature_blinding_randomness_aux(
        seed_net.seed_as_hex,
        &seed_net.net_as_str,
        identityProviderIndex as u32,
        identityIndex as u32,
    ) {
        Ok(k) => k,
        Err(err) => return StringResult::from(err).to_jstring(&env),
    };

    CryptoJniResult::Ok(blinding_randomness).to_jstring(&env)
}

/// The JNI wrapper for getting attribute commitment randomness
/// # Arguments
///
/// * `seedAsHex` - The seed as a hex string.
/// * `netAsStr` - The network type as a string. Must be 'Mainnet' or 'Testnet'.
/// * `identityProviderIndex` - The index of the identity provider which will be
///   interpreted as a u32
/// * `identityIndex` - The index of the identity which will be interpreted as a
///   u32.
/// * `credentialCounter` - The credential number of the credential to get the
///   attribute commitment
/// randomness for which will be interpreted as a u32.
/// * `attribute` - The attribute key which will be interpreted as a u8. is
///   provided.
#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_getAttributeCommitmentRandomness(
    env: JNIEnv,
    _: JClass,
    seedAsHex: JString,
    netAsStr: JString,
    identityProviderIndex: jint,
    identityIndex: jint,
    credentialCounter: jint,
    attribute: jint,
) -> jstring {
    let seed_net = match get_seed_and_net(seedAsHex, netAsStr, env) {
        Ok(h) => h,
        Err(err) => return StringResult::Err(err).to_jstring(&env),
    };

    let attribute_commitment_randomness = match get_attribute_commitment_randomness_aux(
        seed_net.seed_as_hex,
        &seed_net.net_as_str,
        identityProviderIndex as u32,
        identityIndex as u32,
        credentialCounter as u32,
        attribute as u8,
    ) {
        Ok(k) => k,
        Err(err) => return StringResult::from(err).to_jstring(&env),
    };

    CryptoJniResult::Ok(attribute_commitment_randomness).to_jstring(&env)
}

/// The JNI wrapper for getting the verifiable credential signing key.
/// # Arguments
///
/// * `seedAsHex` - The seed as a hex string.
/// * `netAsStr` - The network type as a string. Must be 'Mainnet' or 'Testnet'.
/// * `issuerIndex` - The issuer contract index which is interpreted as a u64.
/// * `issuerSubindex` - The issuer contract subindex which is interpreted as a
///   u64.
/// * `verifiableCredentialIndex` - The index of the verifiable credential which
///   is interpreted
/// as a u32.
#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_getVerifiableCredentialSigningKey(
    env: JNIEnv,
    _: JClass,
    seedAsHex: JString,
    netAsStr: JString,
    issuerIndex: jlong,
    issuerSubindex: jlong,
    verifiableCredentialIndex: jint,
) -> jstring {
    let seed_net = match get_seed_and_net(seedAsHex, netAsStr, env) {
        Ok(h) => h,
        Err(err) => return StringResult::Err(err).to_jstring(&env),
    };

    let verifiable_credential_signing_key = match get_verifiable_credential_signing_key_aux(
        seed_net.seed_as_hex,
        &seed_net.net_as_str,
        issuerIndex as u64,
        issuerSubindex as u64,
        verifiableCredentialIndex as u32,
    ) {
        Ok(k) => k,
        Err(err) => return StringResult::from(err).to_jstring(&env),
    };

    CryptoJniResult::Ok(verifiable_credential_signing_key).to_jstring(&env)
}

/// The JNI wrapper for getting the verifiable credential public key.
/// # Arguments
///
/// * `seedAsHex` - The seed as a hex string.
/// * `netAsStr` - The network type as a string. Must be 'Mainnet' or 'Testnet'.
/// * `issuerIndex` - The issuer contract index which is interpreted as a u64.
/// * `issuerSubindex` - The issuer contract subindex which is interpreted as a
///   u64.
/// * `verifiableCredentialIndex` - The index of the verifiable credential which
///   is interpreted
/// as a u32.
#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_getVerifiableCredentialPublicKey(
    env: JNIEnv,
    _: JClass,
    seedAsHex: JString,
    netAsStr: JString,
    issuerIndex: jlong,
    issuerSubindex: jlong,
    verifiableCredentialIndex: jint,
) -> jstring {
    let seed_net = match get_seed_and_net(seedAsHex, netAsStr, env) {
        Ok(h) => h,
        Err(err) => return StringResult::Err(err).to_jstring(&env),
    };

    let verifiable_credential_public_key = match get_verifiable_credential_public_key_aux(
        seed_net.seed_as_hex,
        &seed_net.net_as_str,
        issuerIndex as u64,
        issuerSubindex as u64,
        verifiableCredentialIndex as u32,
    ) {
        Ok(k) => k,
        Err(err) => return StringResult::from(err).to_jstring(&env),
    };

    CryptoJniResult::Ok(verifiable_credential_public_key).to_jstring(&env)
}

/// The JNI wrapper for getting the verifiable credential backup
/// encryption key.
/// * `seedAsHex` - The seed as a hex string.
/// * `netAsStr` - The network type as a string. Must be 'Mainnet' or 'Testnet'.
#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_getVerifiableCredentialBackupEncryptionKey(
    env: JNIEnv,
    _: JClass,
    seedAsHex: JString,
    netAsStr: JString,
) -> jstring {
    let seed_net = match get_seed_and_net(seedAsHex, netAsStr, env) {
        Ok(h) => h,
        Err(err) => return StringResult::Err(err).to_jstring(&env),
    };

    let verifiable_credential_backup_encryption_key =
        match get_verifiable_credential_backup_encryption_key_aux(
            seed_net.seed_as_hex,
            &seed_net.net_as_str,
        ) {
            Ok(k) => k,
            Err(err) => return StringResult::from(err).to_jstring(&env),
        };

    CryptoJniResult::Ok(verifiable_credential_backup_encryption_key).to_jstring(&env)
}

/// The JNI wrapper for creating an identity creation request.
/// * `input` - the JSON string of
///   [`wallet_library::identity::IdentityObjectRequestInput`]
#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_createIdentityRequestV1(
    env: JNIEnv,
    _: JClass,
    input: JString,
) -> jstring {
    let input_string = match get_string(env, input) {
        Ok(s) => s,
        Err(err) => return StringResult::Err(err).to_jstring(&env),
    };

    let id_request_input: wallet_library::identity::IdentityObjectRequestInput =
        match serde_json::from_str(&input_string) {
            Ok(req) => req,
            Err(err) => return StringResult::from(err).to_jstring(&env),
        };

    let request = match create_identity_object_request_v1_aux(id_request_input) {
        Ok(r) => r,
        Err(err) => return StringResult::from(err).to_jstring(&env),
    };

    CryptoJniResult::Ok(request).to_jstring(&env)
}

/// The JNI wrapper for creating an identity recovery request.
/// * `input` - the JSON string of
///   [`wallet_library::identity::IdentityRecoveryRequestInput`]
#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_createIdentityRecoveryRequest(
    env: JNIEnv,
    _: JClass,
    input: JString,
) -> jstring {
    let input_string = match get_string(env, input) {
        Ok(s) => s,
        Err(err) => return StringResult::Err(err).to_jstring(&env),
    };

    let id_recovery_request_input: wallet_library::identity::IdentityRecoveryRequestInput =
        match serde_json::from_str(&input_string) {
            Ok(req) => req,
            Err(err) => return StringResult::from(err).to_jstring(&env),
        };

    let request = match create_identity_recovery_request_aux(id_recovery_request_input) {
        Ok(r) => r,
        Err(err) => return StringResult::from(err).to_jstring(&env),
    };

    CryptoJniResult::Ok(request).to_jstring(&env)
}

/// The JNI wrapper for creating a credential deployment transaction.
/// * `input` - the JSON string of
///   [`wallet_library::credential::UnsignedCredentialInput`]
#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_createUnsignedCredentialV1(
    env: JNIEnv,
    _: JClass,
    input: JString,
) -> jstring {
    let input_string = match get_string(env, input) {
        Ok(s) => s,
        Err(err) => return StringResult::Err(err).to_jstring(&env),
    };

    let unsigned_credential_input: credential::UnsignedCredentialInput =
        match serde_json::from_str(&input_string) {
            Ok(req) => req,
            Err(err) => return StringResult::from(err).to_jstring(&env),
        };

    let request = match create_unsigned_credential_v1_aux(unsigned_credential_input) {
        Ok(r) => r,
        Err(err) => return StringResult::from(err).to_jstring(&env),
    };

    CryptoJniResult::Ok(request).to_jstring(&env)
}

/// The JNI wrapper for computing the hash to sign of a credential deployment
/// transaction payload.
/// * `input` - the JSON string of
///   [`wallet_library::credential::UnsignedCredentialInput`]
#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_computeCredentialDeploymentSignDigest(
    env: JNIEnv,
    _: JClass,
    input: JString,
) -> jstring {
    let input_string = match get_string(env, input) {
        Ok(s) => s,
        Err(err) => return StringResult::Err(err).to_jstring(&env),
    };

    let credential_deployment_details: CredentialDeploymentDetails =
        match serde_json::from_str(&input_string) {
            Ok(req) => req,
            Err(err) => return StringResult::from(err).to_jstring(&env),
        };

    let credential_deployment_sign_digest =
        credential::compute_credential_deployment_hash_to_sign(credential_deployment_details);
    CryptoJniResult::Ok(credential_deployment_sign_digest).to_jstring(&env)
}

/// The JNI wrapper for getting the serialized bytes of a credential deployment
/// transaction payload, i.e. the bytes of which should be hashed and signed
/// before sending the transaction.
/// * `input` - the JSON string of
///   [`wallet_library::credential::UnsignedCredentialInput`]
#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_serializeCredentialDeploymentForSubmission(
    env: JNIEnv,
    _: JClass,
    input: JString,
) -> jstring {
    let input_string = match get_string(env, input) {
        Ok(s) => s,
        Err(err) => return StringResult::Err(err).to_jstring(&env),
    };

    let payload: CredentialDeploymentPayload = match serde_json::from_str(&input_string) {
        Ok(req) => req,
        Err(err) => return StringResult::from(err).to_jstring(&env),
    };

    let serialized_credential_deployment_payload = serialize_credential_deployment_payload(payload);

    CryptoJniResult::Ok(serialized_credential_deployment_payload).to_jstring(&env)
}

/// The JNI wrapper for creating a web3Id presentation for the given statement.
/// * `input` - the JSON string of [`wallet_library::proofs::Web3IdProofInput`]
#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_createWeb3IdProof(
    env: JNIEnv,
    _: JClass,
    input: JString,
) -> jstring {
    let input_string = match get_string(env, input) {
        Ok(s) => s,
        Err(err) => return StringResult::Err(err).to_jstring(&env),
    };

    let proofInput: Web3IdProofInput = match serde_json::from_str(&input_string) {
        Ok(req) => req,
        Err(err) => return StringResult::from(err).to_jstring(&env),
    };

    let presentation = match proofInput.create_proof() {
        Ok(r) => r,
        Err(err) => return StringResult::from(err).to_jstring(&env),
    };

    let presentation_string = match to_string(&presentation) {
        Ok(r) => r,
        Err(err) => return StringResult::from(err).to_jstring(&env),
    };

    CryptoJniResult::Ok(presentation_string).to_jstring(&env)
}

impl<T> From<wallet_library::statement::RequestCheckError> for CryptoJniResult<T> {
    fn from(e: wallet_library::statement::RequestCheckError) -> Self {
        let error = JNIErrorResponse {
            errorType:    JNIErrorResponseType::NativeConversion,
            errorMessage: e.to_string(),
        };
        CryptoJniResult::Err(error)
    }
}

/// The JNI wrapper for checking that a request is acceptable.
/// * `input` - the JSON string of [`wallet_library::proofs::RequestStatement`]
#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_isAcceptableRequest(
    env: JNIEnv,
    _: JClass,
    input: JString,
) -> jstring {
    let input_string = match get_string(env, input) {
        Ok(s) => s,
        Err(err) => return StringResult::Err(err).to_jstring(&env),
    };

    let request: Request<constants::ArCurve, AttributeKind> = match serde_json::from_str(&input_string) {
        Ok(req) => req,
        Err(err) => return StringResult::from(err).to_jstring(&env),
    };

    match request.acceptable_request(&wallet_library::statement::get_default_wallet_config()) {
        Ok(r) => r,
        Err(err) => return StringResult::from(err).to_jstring(&env),
    };

    CryptoJniResult::Ok(()).to_jstring(&env)
}

/// The JNI wrapper for checking that a request is acceptable.
/// * `input` - the JSON string of [`wallet_library::proofs::RequestStatement`]
#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_isAcceptableAtomicStatement<'a>(
    env: JNIEnv<'a>,
    _: JClass,
    request_input: JString,
    range_tags_input: JString,
    set_tags_input: JString,
    attribute_check: JObject<'a>,
) -> jstring {
    let input_string = match get_string(env, request_input) {
        Ok(s) => s,
        Err(err) => return StringResult::Err(err).to_jstring(&env),
    };

    let request: AtomicStatement<constants::ArCurve, AttributeTag, Web3IdAttribute> = match  serde_json::from_str(&input_string) {
        Ok(req) => req,
        Err(err) => return StringResult::from(err).to_jstring(&env),
    };

    let input_string = match get_string(env, set_tags_input) {
        Ok(s) => s,
        Err(err) => return StringResult::Err(err).to_jstring(&env),
    };

    let set_tags: HashSet<AttributeTag> = match  serde_json::from_str(&input_string) {
        Ok(req) => req,
        Err(err) => return StringResult::from(err).to_jstring(&env),
    };

    let input_string = match get_string(env, range_tags_input) {
        Ok(s) => s,
        Err(err) => return StringResult::Err(err).to_jstring(&env),
    };

    let range_tags: HashSet<AttributeTag> = match  serde_json::from_str(&input_string) {
        Ok(req) => req,
        Err(err) => return StringResult::from(err).to_jstring(&env),
    };

    let check = |tag: &AttributeTag, value: &Web3IdAttribute| {
        let attribute: String = match to_string(value) {
            Ok(a) => a,
            Err(_) => return Err(RequestCheckError::InvalidValue("Unable to be stringified".to_owned()))
        };
        let attribute: JString = match env.new_string(attribute) {
            Ok(a) => a,
            Err(_) => return Err(RequestCheckError::InvalidValue("Unable to be stringified".to_owned()))
        };

        let args = &[JValue::Int(tag.0.into()), attribute.into()];
        match env.call_method(attribute_check, "check_attribute", "(I)V", args) {
            Ok(_) => Ok(()),
            Err(e) => Err(RequestCheckError::InvalidValue(e.to_string()))
        }
    };

    let rules = WalletConfigRules::<constants::ArCurve, AttributeTag, Web3IdAttribute> {
        set_tags,
        range_tags,
        attribute_check: Box::new(check),
        _marker: std::marker::PhantomData
    };

    match request.acceptable_atomic_statement(&Some(rules)) {
        Ok(r) => r,
        Err(err) => return StringResult::from(err).to_jstring(&env),
    };

    CryptoJniResult::Ok(()).to_jstring(&env)
}
