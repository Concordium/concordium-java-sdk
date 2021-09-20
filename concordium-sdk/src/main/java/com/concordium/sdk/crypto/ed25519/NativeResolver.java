package com.concordium.sdk.crypto.ed25519;

import lombok.val;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

class NativeResolver {

    private static final String BASE_LIB_NAME = "ed25519_jni";
    private static boolean LOADED;
    private static final Random random = new Random();

    static void loadLib() {
        if (!LOADED) {
            try {
                val os = NativeResolver.OS.from(System.getProperty("os.name"));
                val libName = os.getPrefix() + BASE_LIB_NAME + os.getExtension();
                val libPath = "/native/" + libName;

                val resourceAsStream = NativeResolver.class.getResourceAsStream(libPath);
                if (Objects.isNull(resourceAsStream)) {
                    throw new RuntimeException("FAILED LOADING LIB");
                }
                val tempLib = File.createTempFile(getRandomPrefix(), libName + os.getExtension());
                tempLib.deleteOnExit();

                try (FileOutputStream fos = new FileOutputStream(tempLib)) {
                    IOUtils.copy(resourceAsStream, fos);
                }
                System.load(tempLib.getAbsolutePath());
                LOADED = true;
            } catch (Exception e) {
                throw new IllegalStateException("Could not load native dependencies", e);
            }
        }
    }

    private static String getRandomPrefix() {
        val buffer = new byte[6];
        random.nextBytes(buffer);
        return Hex.encodeHexString(buffer);
    }

    enum OS {
        MACOS,
        LINUX,
        WINDOWS;

        static OS from(String osString) {
            val lowerCased = osString.toLowerCase(Locale.ROOT);
            val isWindows = lowerCased.contains("windows");
            if (isWindows) {
                return WINDOWS;
            }
            val isMacOs = lowerCased.contains("mac");
            if (isMacOs) {
                return MACOS;
            }
            val isLinux = lowerCased.contains("linux");
            if (isLinux) {
                return LINUX;
            }
            throw new RuntimeException("Unsupported OS: " + osString);
        }

        String getExtension() {
            switch (this) {
                case MACOS:
                    return ".dylib";
                case LINUX:
                    return ".so";
                case WINDOWS:
                    return ".dll";
                default:
                    throw new RuntimeException("Cannot get extension.");
            }
        }

        String getPrefix() {
            switch (this) {
                case MACOS:
                case LINUX:
                    return "lib";
                case WINDOWS:
                    return "";
                default:
                    throw new RuntimeException("Cannot get extension.");
            }
        }
    }
}
