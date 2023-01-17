PATH_CRYPTO := crypto-jni/
PATH_CRYPTO_TARGET = $(PATH_CRYPTO)target/

PATH_JAVA_SDK := concordium-sdk/
PATH_JAVA_NATIVE_RESOURCES := $(PATH_JAVA_SDK)native/

OS := $(shell uname -s)

ifeq ($(OS),Darwin)
all:
	cd $(PATH_CRYPTO) && cargo build --release
	mkdir -p $(PATH_JAVA_SDK)native
	cp $(PATH_CRYPTO_TARGET)release/libcrypto_jni.dylib $(PATH_JAVA_NATIVE_RESOURCES)
else ifeq ($(OS),Linux)
all:
	cd $(PATH_CRYPTO) && cargo build --release
	mkdir -p $(PATH_JAVA_SDK)native
	cp $(PATH_CRYPTO_TARGET)release/libcrypto_jni.so $(PATH_JAVA_NATIVE_RESOURCES)
else
all:
	cd $(PATH_CRYPTO) && cargo build --release
	mkdir -p $(PATH_JAVA_SDK)native
	cp $(PATH_CRYPTO_TARGET)release/crypto_jni.dll $(PATH_JAVA_NATIVE_RESOURCES)
endif

clean:
	rm -rf $(PATH_JAVA_NATIVE_RESOURCES)*
