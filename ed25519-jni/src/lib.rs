use core::convert::Into;
use ed25519_dalek::*;
use jni::objects::JClass;
use jni::sys::{jboolean, jbyteArray, JNI_FALSE, JNI_TRUE};
use jni::JNIEnv;
use std::convert::{From, TryFrom};

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_com_concordium_sdk_crypto_ed25519_ED25519_sign(
    env: JNIEnv,
    _class: JClass,
    secretKeyBytes: jbyteArray,
    messageBytes: jbyteArray,
) -> jbyteArray {
    let secretKeyBytes = env
        .convert_byte_array(secretKeyBytes)
        .expect("todo: error handling");
    let secret_key = match SecretKey::from_bytes(&secretKeyBytes) {
        Ok(sk) => sk,
        _ => panic!("todo: error handling"),
    };

    let public_key: PublicKey = (&secret_key).into();

    let expanded_secret_key = ExpandedSecretKey::from(&secret_key);

    let bytesToSign = env
        .convert_byte_array(messageBytes)
        .expect("todo: error handling");

    let signature = expanded_secret_key.sign(&bytesToSign, &public_key);
    let signatureBytes = signature.to_bytes();

    env.byte_array_from_slice(&signatureBytes)
        .expect("todo: error handling")
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_com_concordium_sdk_crypto_ed25519_ED25519_verify(
    env: JNIEnv,
    _class: JClass,
    pub_key_bytes: jbyteArray,
    msg_bytes: jbyteArray,
    sig_bytes: jbyteArray,
) -> jboolean {
    let public_key_bytes = env
        .convert_byte_array(pub_key_bytes)
        .expect("todo: error handling");
    let public_key = PublicKey::from_bytes(&public_key_bytes).expect("todo: error handling");

    let message_bytes = env
        .convert_byte_array(msg_bytes)
        .expect("todo: error handling");

    let signature_bytes = env
        .convert_byte_array(sig_bytes)
        .expect("todo: error handling");

    let signature: Signature = match Signature::try_from(&signature_bytes[..]) {
        Ok(sig) => sig,
        _ => panic!("todo: error handling"),
    };

    match public_key.verify(&message_bytes, &signature) {
        Ok(_) => JNI_TRUE,
        _ => JNI_FALSE,
    }
}

#[no_mangle]
pub extern "system" fn Java_com_concordium_sdk_crypto_ed25519_ED25519_generateSecretKey(
    env: JNIEnv,
    _class: JClass,
) -> jbyteArray {
    let mut csprng = rand::rngs::OsRng {};
    let secret_key = SecretKey::generate(&mut csprng);
    let secret_key_bytes = secret_key.to_bytes();
    env.byte_array_from_slice(&secret_key_bytes)
        .expect("todo: error handling")
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_com_concordium_sdk_crypto_ed25519_ED25519_generatePublicKey(
    env: JNIEnv,
    _class: JClass,
    secretKeyBytes: jbyteArray,
) -> jbyteArray {
    let secretKeyBytes = env
        .convert_byte_array(secretKeyBytes)
        .expect("todo: error handling");
    let secret_key = match SecretKey::from_bytes(&secretKeyBytes) {
        Ok(sk) => sk,
        _ => panic!("todo: error handling"),
    };

    let public_key: PublicKey = (&secret_key).into();
    let public_key_bytes = public_key.to_bytes();
    env.byte_array_from_slice(&public_key_bytes)
        .expect("todo: error handling")
}
