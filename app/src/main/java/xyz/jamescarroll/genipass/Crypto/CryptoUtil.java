package xyz.jamescarroll.genipass.Crypto;

/**
 * Created by James Carroll on 1/20/16.
 */
public class CryptoUtil {

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

    protected static byte[] zeroByteArray(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = 0;
        }

        return bytes;
    }
}
