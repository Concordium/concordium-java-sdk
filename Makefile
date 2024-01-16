PATH_CRYPTO := crypto-jni/
PATH_CRYPTO_TARGET = $(PATH_CRYPTO)target/

PATH_JAVA_SDK := concordium-sdk/
PATH_JAVA_NATIVE_RESOURCES := $(PATH_JAVA_SDK)native/

PATH_ANDROID_SDK := concordium-android-sdk/
PATH_ANDROID_NATIVE_RESOURCES := $(PATH_ANDROID_SDK)native/

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

MIN_ANDROID_VER := "26"

define android-command
	mkdir -p $(PATH_ANDROID_NATIVE_RESOURCES)$(2)
	cd $(PATH_CRYPTO) && cargo ndk --target $(1) --platform $(MIN_ANDROID_VER) -- build --release
	cp $(PATH_CRYPTO_TARGET)$(1)/release/libcrypto_jni.so $(PATH_ANDROID_NATIVE_RESOURCES)$(2)/
endef

add-android-targets:
	cargo install --version 3.4.0 cargo-ndk --locked
	rustup target add aarch64-linux-android
	rustup target add armv7-linux-androideabi
	rustup target add i686-linux-android
	rustup target add x86_64-linux-android


android:
	mkdir -p $(PATH_ANDROID_NATIVE_RESOURCES)
	$(call android-command,aarch64-linux-android,arm64-v8a)
	$(call android-command,armv7-linux-androideabi,armeabi-v7a)
	$(call android-command,x86_64-linux-android,x86_64)
	$(call android-command,i686-linux-android,x86)


clean:
	rm -rf $(PATH_JAVA_NATIVE_RESOURCES)*
	rm -rf $(PATH_ANDROID_NATIVE_RESOURCES)*
