use anyhow::{anyhow, Result};
pub use concordium_base::common::types::{AccountAddress, ACCOUNT_ADDRESS_SIZE};
use concordium_base::{
    base,
    common::{Serialize, *},
    contracts_common::{schema::VersionedModuleSchema, Amount},
    encrypted_transfers,
    encrypted_transfers::types::{
        AggregatedDecryptedAmount, EncryptedAmount, EncryptedAmountTransferData,
        IndexedEncryptedAmount, SecToPubAmountTransferData,
    },
    id,
    id::{constants::ArCurve, curve_arithmetic::Curve, elgamal, types::GlobalContext},
    transactions::{AddBakerKeysMarker, BakerKeysPayload, ConfigureBakerKeysPayload},
};
use core::slice;
use ed25519_dalek::*;

use jni::{
    objects::{JClass, JString},
    sys::{jboolean, jbyteArray, jint, jlong, jstring, JNI_FALSE},
    JNIEnv,
};
use rand::thread_rng;
use serde_json::{from_str, to_string};
use std::{
    convert::{From, TryFrom},
    i8,
    str::Utf8Error,
};
use wallet_library::wallet::{
    get_account_public_key_aux, get_account_signing_key_aux,
    get_attribute_commitment_randomness_aux, get_credential_id_aux, get_id_cred_sec_aux,
    get_prf_key_aux, get_signature_blinding_randomness_aux,
};

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

    let secret_key = match SecretKey::from_bytes(&secretKeyBytes) {
        Ok(sk) => sk,
        _ => return MALFORMED_SECRET_KEY,
    };

    let public_key: PublicKey = (&secret_key).into();

    let bytesToSign = match env.convert_byte_array(messageBytes) {
        Ok(x) => x,
        _ => return NATIVE_CONVERSION_ERROR,
    };

    let expanded_secret_key = ExpandedSecretKey::from(&secret_key);
    let signature = expanded_secret_key.sign(&bytesToSign, &public_key);
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
    let public_key = match PublicKey::from_bytes(&public_key_bytes) {
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
    let secret_key = SecretKey::generate(&mut csprng);
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

    let secret_key = match SecretKey::from_bytes(&secretKeyBytes) {
        Ok(sk) => sk,
        _ => return MALFORMED_SECRET_KEY,
    };

    let public_key: PublicKey = (&secret_key).into();
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
                errorType:    JNIErrorResponseType::ParameterSerialization,
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

type AccountSigningKeyResult = CryptoJniResult<String>;

#[no_mangle]
#[allow(non_snake_case)]
/// The JNI wrapper for the `get_account_signing_key` method.
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_getAccountSigningKey(
    env: JNIEnv,
    _: JClass,
    seedAsHex: JString,
    netAsStr: JString,
    identityProviderIndex: jlong,
    identityIndex: jlong,
    credentialCounter: jlong,
) -> jstring {
    let seed = match get_string(env, seedAsHex) {
        Ok(s) => s,
        Err(err) => return AccountSigningKeyResult::Err(err).to_jstring(&env),
    };

    let net = match get_string(env, netAsStr) {
        Ok(n) => n,
        Err(err) => return AccountSigningKeyResult::Err(err).to_jstring(&env),
    };

    // We use as u32 here which is unsafe, but we ensure that only u32 values
    // are provided from the Java
    let account_signing_key = get_account_signing_key_aux(
        seed,
        &net,
        identityProviderIndex as u32,
        identityIndex as u32,
        credentialCounter as u32,
    )
    .unwrap();

    CryptoJniResult::Ok(account_signing_key).to_jstring(&env)
}

#[no_mangle]
#[allow(non_snake_case)]
/// The JNI wrapper for the `get_account_public_key` method.
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_getAccountPublicKey(
    env: JNIEnv,
    _: JClass,
    seedAsHex: JString,
    netAsStr: JString,
    identityProviderIndex: jlong,
    identityIndex: jlong,
    credentialCounter: jlong,
) -> jstring {
    let seed = match get_string(env, seedAsHex) {
        Ok(s) => s,
        Err(err) => return AccountSigningKeyResult::Err(err).to_jstring(&env),
    };

    let net = match get_string(env, netAsStr) {
        Ok(n) => n,
        Err(err) => return AccountSigningKeyResult::Err(err).to_jstring(&env),
    };

    // We use as u32 here which is unsafe, but we ensure that only u32 values
    // are provided from the Java
    let account_public_key = get_account_public_key_aux(
        seed,
        &net,
        identityProviderIndex as u32,
        identityIndex as u32,
        credentialCounter as u32,
    )
    .unwrap();

    CryptoJniResult::Ok(account_public_key).to_jstring(&env)
}

#[no_mangle]
#[allow(non_snake_case)]
/// The JNI wrapper for the `get_account_public_key` method.
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_getIdCredSec(
    env: JNIEnv,
    _: JClass,
    seedAsHex: JString,
    netAsStr: JString,
    identityProviderIndex: jlong,
    identityIndex: jlong,
) -> jstring {
    let seed = match get_string(env, seedAsHex) {
        Ok(s) => s,
        Err(err) => return AccountSigningKeyResult::Err(err).to_jstring(&env),
    };

    let net = match get_string(env, netAsStr) {
        Ok(n) => n,
        Err(err) => return AccountSigningKeyResult::Err(err).to_jstring(&env),
    };

    // We use as u32 here which is unsafe, but we ensure that only u32 values
    // are provided from the Java
    let account_public_key = get_id_cred_sec_aux(
        seed,
        &net,
        identityProviderIndex as u32,
        identityIndex as u32,
    )
    .unwrap();

    CryptoJniResult::Ok(account_public_key).to_jstring(&env)
}

#[no_mangle]
#[allow(non_snake_case)]
/// The JNI wrapper for the `get_account_public_key` method.
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_getPrfKey(
    env: JNIEnv,
    _: JClass,
    seedAsHex: JString,
    netAsStr: JString,
    identityProviderIndex: jlong,
    identityIndex: jlong,
) -> jstring {
    let seed = match get_string(env, seedAsHex) {
        Ok(s) => s,
        Err(err) => return AccountSigningKeyResult::Err(err).to_jstring(&env),
    };

    let net = match get_string(env, netAsStr) {
        Ok(n) => n,
        Err(err) => return AccountSigningKeyResult::Err(err).to_jstring(&env),
    };

    // We use as u32 here which is unsafe, but we ensure that only u32 values
    // are provided from the Java
    let account_public_key = get_prf_key_aux(
        seed,
        &net,
        identityProviderIndex as u32,
        identityIndex as u32,
    )
    .unwrap();

    CryptoJniResult::Ok(account_public_key).to_jstring(&env)
}

#[no_mangle]
#[allow(non_snake_case)]
/// The JNI wrapper for the `get_account_public_key` method.
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_getCredentialId(
    env: JNIEnv,
    _: JClass,
    seedAsHex: JString,
    netAsStr: JString,
    identityProviderIndex: jlong,
    identityIndex: jlong,
    credentialCounter: jlong,
    onChainCommitmentKey: JString,
) -> jstring {
    let seed = match get_string(env, seedAsHex) {
        Ok(s) => s,
        Err(err) => return AccountSigningKeyResult::Err(err).to_jstring(&env),
    };

    let net = match get_string(env, netAsStr) {
        Ok(n) => n,
        Err(err) => return AccountSigningKeyResult::Err(err).to_jstring(&env),
    };

    let commitmentKey = match get_string(env, onChainCommitmentKey) {
        Ok(n) => n,
        Err(err) => return AccountSigningKeyResult::Err(err).to_jstring(&env),
    };

    let credential_id = get_credential_id_aux(
        seed,
        &net,
        identityProviderIndex as u32,
        identityIndex as u32,
        credentialCounter as u8,
        &commitmentKey,
    )
    .unwrap();

    CryptoJniResult::Ok(credential_id).to_jstring(&env)
}

#[no_mangle]
#[allow(non_snake_case)]
/// The JNI wrapper for the `get_account_public_key` method.
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_getSignatureBlindingRandomness(
    env: JNIEnv,
    _: JClass,
    seedAsHex: JString,
    netAsStr: JString,
    identityProviderIndex: jlong,
    identityIndex: jlong,
) -> jstring {
    let seed = match get_string(env, seedAsHex) {
        Ok(s) => s,
        Err(err) => return AccountSigningKeyResult::Err(err).to_jstring(&env),
    };

    let net = match get_string(env, netAsStr) {
        Ok(n) => n,
        Err(err) => return AccountSigningKeyResult::Err(err).to_jstring(&env),
    };

    let blinding_randomness = get_signature_blinding_randomness_aux(
        seed,
        &net,
        identityProviderIndex as u32,
        identityIndex as u32,
    )
    .unwrap();

    CryptoJniResult::Ok(blinding_randomness).to_jstring(&env)
}

#[no_mangle]
#[allow(non_snake_case)]
/// The JNI wrapper for the `get_account_public_key` method.
pub extern "system" fn Java_com_concordium_sdk_crypto_CryptoJniNative_getAttributeCommitmentRandomness(
    env: JNIEnv,
    _: JClass,
    seedAsHex: JString,
    netAsStr: JString,
    identityProviderIndex: jlong,
    identityIndex: jlong,
    credentialCounter: jlong,
    attribute: jint,
) -> jstring {
    let seed = match get_string(env, seedAsHex) {
        Ok(s) => s,
        Err(err) => return AccountSigningKeyResult::Err(err).to_jstring(&env),
    };

    let net = match get_string(env, netAsStr) {
        Ok(n) => n,
        Err(err) => return AccountSigningKeyResult::Err(err).to_jstring(&env),
    };

    let attribute_commitment_randomness = get_attribute_commitment_randomness_aux(
        seed,
        &net,
        identityProviderIndex as u32,
        identityIndex as u32,
        credentialCounter as u32,
        attribute as u8,
    )
    .unwrap();

    CryptoJniResult::Ok(attribute_commitment_randomness).to_jstring(&env)
}
