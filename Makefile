PATH_ED25519 := ed25519-jni/
PATH_ED25519_TARGET = $(PATH_ED25519)target/

PATH_JAVA_SDK := concordium-sdk/
PATH_JAVA_NATIVE_RESOURCES := $(PATH_JAVA_SDK)native/

OS := $(shell uname -s)

ifeq ($(OS),Darwin)
all:
	git submodule update --init --recursive
	cd $(PATH_ED25519) && cargo build
	mkdir -p $(PATH_JAVA_SDK)native
	cp $(PATH_ED25519_TARGET)debug/libed25519_jni.dylib $(PATH_JAVA_NATIVE_RESOURCES)
else ifeq ($(OS),Linux)
all:
	git submodule update --init --recursive
	cd $(PATH_ED25519) && cargo build
	mkdir -p $(PATH_JAVA_SDK)native
	cp $(PATH_ED25519_TARGET)debug/libed25519_jni.so $(PATH_JAVA_NATIVE_RESOURCES)
else
all:
	git submodule update --init --recursive
	cd $(PATH_ED25519) && cargo build
	mkdir -p $(PATH_JAVA_SDK)native
	cp $(PATH_ED25519_TARGET)debug/ed25519_jni.dll $(PATH_JAVA_NATIVE_RESOURCES)
endif

clean:
	rm -rf $(PATH_JAVA_NATIVE_RESOURCES)*
