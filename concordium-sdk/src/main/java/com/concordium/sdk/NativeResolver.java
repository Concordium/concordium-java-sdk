package com.concordium.sdk;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Objects;

public class NativeResolver {

    private static final String LIB_NAME = "libed25519_jni";
    private static boolean LOADED;

    static {
        loadLib();
    }

    @SneakyThrows
    public static void loadLib() {
        //only allow loading library once.
        if (!LOADED) {
            val arch = System.getProperty("os.arch").replaceAll(" ", "_");
            val osName = System.getProperty("os.name").replaceAll(" ", "_");

            val OS = NativeResolver.OS.from(osName);
            val libPath = "/native/" + arch + "/" + osName + "/" + LIB_NAME + OS.getExtension();

            val resourceAsStream = Object.class.getResourceAsStream(libPath);
            if (Objects.isNull(resourceAsStream)) {
                throw new RuntimeException("FAILED LOADING LIB");
            }
            val tempLib = File.createTempFile("lib", LIB_NAME + OS.getExtension());
            tempLib.deleteOnExit();

            try (FileOutputStream fos = new FileOutputStream(tempLib)) {
                IOUtils.copy(resourceAsStream, fos);
            }
            System.load(tempLib.getAbsolutePath());
            LOADED = true;
        }
    }

    enum OS {
        MACOS,
        LINUX;

        static OS from(String osString) {
            switch (osString) {
                case "Mac_OS_X": return MACOS;
                case "Linux": return LINUX;
                default: throw new RuntimeException("Unsupported OS: " + osString);
            }
        }

         String getExtension(){
            switch (this) {
                case MACOS: return ".dylib";
                case LINUX: return ".so";
                default: throw new RuntimeException("Cannot get extension.");
            }
        }
    }
}
