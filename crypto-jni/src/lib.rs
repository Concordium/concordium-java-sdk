pub use concordium_base::common::types::{AccountAddress, ACCOUNT_ADDRESS_SIZE};
use concordium_base::common::Serialize;
use concordium_base::contracts_common::{Amount, schema::VersionedModuleSchema};
use concordium_base::encrypted_transfers;
use concordium_base::encrypted_transfers::types::{
    AggregatedDecryptedAmount, EncryptedAmount, EncryptedAmountTransferData,
    IndexedEncryptedAmount, SecToPubAmountTransferData,
};
use concordium_base::id;
use concordium_base::id::curve_arithmetic::Curve;
use concordium_base::id::elgamal;
use concordium_base::id::{constants::ArCurve, types::GlobalContext};
use concordium_base::transactions::{
    AddBakerKeysMarker, BakerKeysPayload, ConfigureBakerKeysPayload,
};
use concordium_base::{base, common::*};
use core::slice;
use ed25519_dalek::*;
use jni::sys::{jstring, jboolean, JNI_FALSE};
use rand::thread_rng;
use serde_json::{from_str, to_string};
use std::convert::{From, TryFrom};
use std::i8;
use std::str::Utf8Error;
use hex;

use jni::{
    objects::{JClass, JString},
    sys::{jbyteArray, jint},
    JNIEnv,
};

use anyhow::{anyhow, Result};

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
    ParameterSerializationError,
    Utf8DecodeError,
    JsonDeserializationError,
    NativeConversionError,
    PayloadCreationError,
}


#[derive(SerdeSerialize, SerdeDeserialize)]
#[allow(non_snake_case)]
struct JNIErrorResponse {
    errorType: JNIErrorResponseType,
    errorMessage: String,
    
}

impl<T> From<serde_json::Error> for CryptoJniResult<T> {
    fn from(e: serde_json::Error) -> Self {
        let error = JNIErrorResponse {
            errorType: JNIErrorResponseType::JsonDeserializationError,
            errorMessage: e.to_string(),
        };
        CryptoJniResult::Err(error)
    }
}

impl<T> From<Utf8Error> for CryptoJniResult<T> {
    fn from(e: Utf8Error) -> Self {
        let error = JNIErrorResponse {
            errorType: JNIErrorResponseType::Utf8DecodeError,
            errorMessage: e.to_string(),
        };
        CryptoJniResult::Err(error)
    }
}

impl<T> From<jni::errors::Error> for CryptoJniResult<T> {
    fn from(e: jni::errors::Error) -> Self {
        let error = JNIErrorResponse {
            errorType: JNIErrorResponseType::NativeConversionError,
            errorMessage: e.to_string(),
        };
        CryptoJniResult::Err(error)
    }
}

/**
 * Creates errors from strings.
 * Used when payload creation fails as no error to be passed on is generated.
 */
impl<T> From<&str> for CryptoJniResult<T> {
    fn from(e: &str) -> Self {
        let error = JNIErrorResponse {
            errorType: JNIErrorResponseType::PayloadCreationError,
            errorMessage: e.to_string(),
        };
        CryptoJniResult::Err(error)
    }
}

impl<T> From<anyhow::Error> for CryptoJniResult<T> {
    fn from(e: anyhow::Error) -> Self {
        let error = JNIErrorResponse {
            errorType: JNIErrorResponseType::ParameterSerializationError,
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
    global: GlobalContext<C>,
    amount: Amount,
    sender_secret_key: elgamal::SecretKey<C>,
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
        CryptoJniResult::Err(err) => return EncryptedTransferResult::Err(err).to_jstring(&env)
    };

    let input_amount: AggregatedDecryptedAmount<ArCurve> = AggregatedDecryptedAmount {
        agg_encrypted_amount: input.input_encrypted_amount.encrypted_chunks,
        agg_index: encrypted_transfers::types::EncryptedAmountAggIndex {
            index: input.input_encrypted_amount.index.index,
        },
        agg_amount: decrypted_amount,
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
        None =>
        return EncryptedTransferResult::from(PAYLOAD_CREATION_ERROR).to_jstring(&env)
    }
}

#[derive(Serialize, SerdeSerialize, SerdeDeserialize)]
#[serde(bound(serialize = "C: Curve", deserialize = "C: Curve"))]
#[serde(rename_all = "camelCase")]
struct TransferJniInput<C: Curve> {
    global: GlobalContext<C>,
    receiver_public_key: elgamal::PublicKey<C>,
    sender_secret_key: elgamal::SecretKey<C>,
    amount_to_send: Amount,
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
        CryptoJniResult::Err(err) => return EncryptedAmountTransferResult::Err(err).to_jstring(&env),
    };

    let input_amount: AggregatedDecryptedAmount<ArCurve> = AggregatedDecryptedAmount {
        agg_encrypted_amount: input.input_encrypted_amount.encrypted_chunks,
        agg_index: encrypted_transfers::types::EncryptedAmountAggIndex {
            index: input.input_encrypted_amount.index.index,
        },
        agg_amount: decrypted_amount,
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
        None => return EncryptedAmountTransferResult::from(PAYLOAD_CREATION_ERROR).to_jstring(&env)
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
    return CryptoJniResult::Ok(payload).to_jstring(&env);
}

#[derive(Serialize, SerdeSerialize, SerdeDeserialize)]
#[serde(rename_all = "camelCase")]
struct AddBakerPayloadInput {
    pub sender: AccountAddress,
    pub keys: base::BakerKeyPairs,
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

    return CryptoJniResult::Ok(payload).to_jstring(&env);
}

type SerializeParamResult = CryptoJniResult<String>;

#[no_mangle]
#[allow(non_snake_case)]
/// The JNI wrapper for serializing receive parameters.
/**
 * The `parameter` parameter must be a properly initalized `java.lang.String` that is non-null. The parameter must be valid JSON according to the provided schema.
 * The `contractName` parameter must be a properly initalized `java.lang.String` that is non-null.
 * The `methodName` parameter must be a properly initalized `java.lang.String` that is non-null.
 * The `schemaBytes` parameter must be a properly initalized `byte[]` containing bytes representing a valid schema. The schema must match the provided `contractName` and `methodName`.
 * The `schemaVersion` must be a integer representing a valid `com.concordium.sdk.transactions.smartcontracts.SchemaVersion`.
 */
/// If serialization fails returns a SerializeParamResult containing a JNIError detailing what went wrong. 
/**
 *  Returns a SerializeParamResult containg the hex encoded serialized parameter. 
 */
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
        &version,
        verboseErrors
    );


    let result = match serializedParameter {
        Ok(serializedParameter) => hex::encode(serializedParameter),
        Err(err) => return SerializeParamResult::from(err).to_jstring(&env),
        
    };

    return SerializeParamResult::Ok(result).to_jstring(&env);


}


#[allow(non_snake_case)]
/**
 * Helper method for serialize_receive_parameters.
 * Performs the actual serialization.
 */
pub fn serialize_receive_contract_parameters_aux(
    parameter: &str,
    contractName: &str,
    methodName: &str,
    schemaBytes: &Vec<u8>,
    schemaVersion: &Option<u8>,
    verboseErrors: bool,
) -> Result<Vec<u8>> {
    
    let module_schema = VersionedModuleSchema::new(schemaBytes, schemaVersion)?;
    let parameter_type = module_schema.get_receive_param_schema(contractName, methodName)?;
    let value: serde_json::Value = from_str(parameter)?;

    let res = parameter_type
        .serial_value(&value)
        .map_err(|e| anyhow!("{}", e.display(verboseErrors)));
    return Ok(res?);
}

/// The JNI wrapper for serializing init parameters.
/**
 * The `parameter` parameter must be a properly initalized `java.lang.String` that is non-null. The parameter must be valid JSON according to the provided schema.
 * The `contractName` parameter must be a properly initalized `java.lang.String` that is non-null.
 * The `schemaBytes` parameter must be a properly initalized `byte[]` containing bytes representing a valid schema. The schema must match the provided `contractName`.
 * The `schemaVersion` must be a integer representing a valid `com.concordium.sdk.transactions.smartcontracts.SchemaVersion`.
 */
/// If serialization fails returns a SerializeParamResult containing a JNIError detailing what went wrong. 
/**
 *  Returns a SerializeParamResult containg the hex encoded serialized parameter. 
 */
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

    let serializedParameter = serialize_init_parameters_aux(
        &parameter, 
        &contractName, 
        &schema, 
        &version,
        verboseErrors
    );


    let result = match serializedParameter {
        Ok(serializedParameter) => hex::encode(serializedParameter),
        Err(err) => return SerializeParamResult::from(err).to_jstring(&env),
        
    };

    return SerializeParamResult::Ok(result).to_jstring(&env);


}

/**
 * Helper method for serialize_init_parameters.
 * Performs the actual serialization.
 */
#[allow(non_snake_case)]
pub fn serialize_init_parameters_aux(
    parameter: &str,
    contractName: &str,
    schemaBytes: &Vec<u8>,
    schemaVersion: &Option<u8>,
    verboseErrors: bool,
) -> Result<Vec<u8>> {
    
    let module_schema = VersionedModuleSchema::new(schemaBytes, schemaVersion)?;
    let parameter_type = module_schema.get_init_param_schema(contractName)?;
    let value: serde_json::Value = from_str(parameter)?;

    let res = parameter_type
        .serial_value(&value)
        .map_err(|e| anyhow!("{}", e.display(verboseErrors)));
    return Ok(res?);
}