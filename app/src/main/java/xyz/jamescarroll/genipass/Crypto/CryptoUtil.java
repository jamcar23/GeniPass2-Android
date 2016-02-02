/*
 * Copyright (c) 2016
 *
 * This file, CryptoUtil.java, is apart of GeniPass.
 *
 * GeniPass is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * GeniPass is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GeniPass.  If not, see http://www.gnu.org/licenses/.
 */

package xyz.jamescarroll.genipass.Crypto;

/**
 * Created by James Carroll on 1/20/16.
 */
public class CryptoUtil {

    /**
     * Converts an array of bytes to a string of hexadecimal.
     * @param bytes Array of bytes to convert.
     * @return hexadecimal string.
     */

    protected static String bytesToHex(byte[] bytes) {
        char[] hexChar = "0123456789ABCDEF".toCharArray();
        char[] hexOut = new char[bytes.length * 2];
        int v;

        for (int i = 0; i < bytes.length; i++) {
            v = bytes[i] & 0xff;
            hexOut[i * 2] = hexChar[v >>> 4];
            hexOut[i * 2 + 1] = hexChar[v & 0x0f];
        }

        return new String(hexOut);
    }

    /**
     * Sets every value in a byte array to zero
     * @param bytes Array to zero.
     * @return
     */

    protected static byte[] zeroByteArray(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = 0;
        }

        return bytes;
    }
}
