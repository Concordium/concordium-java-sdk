package com.concordium.sdk.exceptions;

/*
 * Copyright 2011 Google Inc.
 * Copyright 2015 Andreas Schildbach
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import com.concordium.sdk.Base58;

@SuppressWarnings("serial")
public class AddressFormatException extends IllegalArgumentException {
    public AddressFormatException() {
        super();
    }

    public AddressFormatException(String message) {
        super(message);
    }

    /**
     * This exception is thrown by {@link Base58} when you try to decode data and a character isn't valid. You shouldn't allow the user to proceed in this
     * case.
     */
    public static class InvalidCharacter extends AddressFormatException {
        public final char character;
        public final int position;

        public InvalidCharacter(char character, int position) {
            super("Invalid character '" + character + "' at position " + position);
            this.character = character;
            this.position = position;
        }
    }

    /**
     * This exception is thrown by {@link Base58} when you try to decode data and the data isn't of the right size. You shouldn't allow the user to proceed
     * in this case.
     */
    public static class InvalidDataLength extends AddressFormatException {
        public InvalidDataLength() {
            super();
        }

        public InvalidDataLength(String message) {
            super(message);
        }
    }

    /**
     * This exception is thrown by {@link Base58} when you try to decode data and the checksum isn't valid. You shouldn't allow the user to proceed in this
     * case.
     */
    public static class InvalidChecksum extends AddressFormatException {
        public InvalidChecksum() {
            super("Checksum does not validate");
        }

        public InvalidChecksum(String message) {
            super(message);
        }
    }

    public static class InvalidVersion extends AddressFormatException {
        public InvalidVersion() {
            super("Version does not validate");
        }

        public InvalidVersion(String message) {
            super(message);
        }
    }

}
