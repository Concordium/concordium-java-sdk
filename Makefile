PATH_ED25519 = ed25519-jni/
PATH_ED25519_TARGET = $(PATH_ED25519)/target/

OS_MACOS_NAME = Mac_OS_x/
OS_MACOS_ARCH = x86_64/

PATH_JAVA_SDK = concordium-sdk/
PATH_JAVA_NATIVE_RESOURCES = $(PATH_JAVA_SDK)native/
PATH_JAVA_NATIVE_RESOURCES_MACOS = $(PATH_JAVA_NATIVE_RESOURCES)$(OS_MACOS_ARCH)$(OS_MACOS_NAME)
PATH_JAVA_NATIVE_RESOURCES_ARM_LINUX = $(PATH_JAVA_NATIVE_RESOURCES)aarch64-unknown-linux-gnu/

ED25519_LIB_NAME = libed25519_jni
MACOS_EXT = .dylib
LINUX_EXT = .so
WINDOWS_EXT = .dll

OS_MACOS_TARGET = x86_64-apple-darwin
OS_ANDROID_TARGET = arm-linux-androideabi
OS_LINUX_TARGET = i686-unknown-linux-gnu


all: release-builds

debug: build-native-macos-x86-debug

release-builds: build-native-macos-x86-release

build-native-macos-x86-debug:
	cd $(PATH_ED25519) && cargo build --target $(OS_MACOS_TARGET)
	mkdir -p $(PATH_JAVA_NATIVE_RESOURCES_MACOS)
	cp $(PATH_ED25519_TARGET)$(OS_MACOS_TARGET)/debug/$(ED25519_LIB_NAME)$(MACOS_EXT) $(PATH_JAVA_NATIVE_RESOURCES_MACOS)

build-native-macos-x86-release:
	cd $(PATH_ED25519) && cargo build --release --target $(OS_MACOS_TARGET)
	mkdir -p $(PATH_JAVA_NATIVE_RESOURCES_MACOS)
	cp $(PATH_ED25519_TARGET)$(OS_MACOS_TARGET)/release/$(ED25519_LIB_NAME)$(MACOS_EXT) $(PATH_JAVA_NATIVE_RESOURCES_MACOS)


clean:
	rm -rf $(PATH_JAVA_NATIVE_RESOURCES)*
