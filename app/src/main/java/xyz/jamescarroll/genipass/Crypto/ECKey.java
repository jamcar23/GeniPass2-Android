/*
 * Copyright (c) 2016
 *
 * This file, ECKey.java, is apart of GeniPass.
 *
 * GeniPass is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * GeniPass is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GeniPass.  If not, see http://www.gnu.org/licenses/.
 */

package xyz.jamescarroll.genipass.Crypto;

import android.content.Context;
import android.util.Log;
import android.util.TimingLogger;

import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.generators.SCrypt;
import org.bouncycastle.jcajce.provider.digest.Blake2b;
import org.bouncycastle.jcajce.provider.digest.SHA3;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import xyz.jamescarroll.genipass.Async.AsyncKeyGenTimeTest;


/**
 * Created by James Carroll on 1/19/16.
 *
 * A Class to define an elliptic curve extended key. It holds the normal public
 * key and the public chain code. This also has method are child key derivation
 * and testing. For more information on this crypto system, please see
 * README.md.
 */

public class ECKey extends CryptoUtil {
    private static final String TAG = "ECKey.TAG";
    private static final X9ECParameters kCurve = SECNamedCurves.getByName("secp256k1");

    private byte[] mKey;
    private byte[] mChain;
    private boolean mMaster = false;

    private ECKey(byte[] mKey, byte[] mChain) {
        this.mKey = mKey;
        this.mChain = mChain;
    }

    /**
     * A public method to derive a child key from an ECKey object. The string
     * should be either the service or tag depending on if its the 2nd or 3rd
     * generation key respectively.
     *
     * @param s Either the service or tag.
     * @return An extended public key object.
     */

    public ECKey generateChildKey(String s) {
        return calcExtPublicKey(calcChildExtPrivate(getmChain(), s));
    }

    public byte[] getmKey() {
        return mKey;
    }

    public byte[] getmChain() {
        return mChain;
    }

    public boolean ismMaster() {
        return mMaster;
    }

    private ECKey setmMaster(boolean mMaster) {
        this.mMaster = mMaster;

        return this;
    }

    /**
     * Preforms the RIPEMD-160 hash of a byte array and returns the hash as a
     * {@link BigInteger}.
     *
     * @param in input byte array to be hashed
     * @param out Preallocated byte array to be used as the output multiple
     *            times.
     * @param ripemd Preallocated {@link RIPEMD160Digest} object to be used
     *               multiple times.
     * @return a BigInteger holding the value of the hash.
     */

    private static BigInteger ripemd160ToBigInteger(byte[] in, byte[] out, RIPEMD160Digest ripemd) {
        BigInteger h;

        ripemd.update(in, 0, in.length);
        ripemd.doFinal(out, 0);
        h = new BigInteger(out);
        zeroByteArray(out);

        return h;
    }

    /**
     * Calculates a child extended private key using HMAC-SHA2-512 using a
     * parent chain code.
     *
     * @param chain parent chain code used as secret key in HMAC.
     * @param s a string to be used as data to HMAC, this is either the service
     *          or the tag.
     * @return a byte array representing the child extended private key.
     */

    private static byte[] calcChildExtPrivate(byte[] chain, String s) {
        final String hmacSHA512 = "HmacSHA512";
        Mac hmac = null;
        SecretKeySpec secret;

        try {
            hmac = Mac.getInstance(hmacSHA512);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "generateChildKey: ", e);
        }

        if (hmac != null) {
            secret = new SecretKeySpec(chain, hmacSHA512);

            try {
                hmac.init(secret);
                return hmac.doFinal(s.getBytes());
            } catch (InvalidKeyException e) {
                Log.e(TAG, "generateChildKey: ", e);
            }
        }

        return null;
    }

    /**
     * Calculates a normal public key using the secp256k1 curve.
     *
     * @param bytes normal private key
     * @return the x coordinate point
     */

    private static byte[] calcPublicKey(byte[] bytes) {
        return kCurve.getG().multiply(new BigInteger(bytes)).getEncoded(true);
    }

    /**
     * Calculates the extended public key using calcPublicKey() for the normal
     * public and 256 iterations of SHA3-256, hashing twice per iteration.
     * It also appends the iteration number to the end before the first hash.
     *
     * @param digest the private key
     * @return an ECKey object representing the extended public key.
     */

    private static ECKey calcExtPublicKey(byte[] digest) {
        SHA3.Digest256 sha = new SHA3.Digest256();
        byte[] k = Arrays.copyOfRange(digest, 0, 32);
        byte[] c = new byte[33];

        System.arraycopy(digest, 32, c, 0, 32);

        for (int i = 0; i < 256; i++) {
            c[32] = (byte) i;
            System.arraycopy(sha.digest(sha.digest(c)), 0, c, 0, 32);
        }

        return new ECKey(calcPublicKey(k), Arrays.copyOfRange(c, 0, 32));
    }

    /**
     * Calculates the master extended private key using SCrypt.
     *
     * @param u RIPEMD-160 hash of the username.
     * @param p RIPEMD-160 hash of the master password.
     * @return the SCrypt digest
     */

    private static byte[] calcMasterExtPrivateKey(BigInteger u, BigInteger p) {
        return SCrypt.generate((u.toString() + p.toString()).getBytes(),
                (u.xor(p)).toByteArray(), (int) Math.pow(2, 16),  8, 2, 64);
    }

    /**
     * Public method that generates the master extended public key.
     *
     * @param u the string of the username
     * @param p the string of the master password.
     * @return an ECKey object representing the master extended public key.
     */

    public static ECKey genFromSeeds(String u, String p) {
        RIPEMD160Digest ripemd = new RIPEMD160Digest();
        byte[] digest = new byte[ripemd.getDigestSize()];
        BigInteger hu, hp;

        hu = ripemd160ToBigInteger(u.getBytes(), digest, ripemd);
        hp = ripemd160ToBigInteger(p.getBytes(), digest, ripemd);
        ripemd.reset();

        digest = calcMasterExtPrivateKey(hu, hp);

        return calcExtPublicKey(digest).setmMaster(true);
    }

    /**
     * Inner static class for getting a rough estimate of how long crypto
     * operations take on different devices. This inner class isn't actually
     * in the production app, it's only used for debugging.
     */

    public static class ECTimeTest {

        /**
         * Public method to begin the time test. This method is meant to be
         * called inside a loop, see {@link AsyncKeyGenTimeTest}.
         * @param tl Already initialized {@link TimingLogger} to be used to
         *           keep track of time.
         * @param context a {@link Context} needed to load the word list to
         *                generate the passwords.
         * @param iter the iteration number of the test
         */

        public static void timeTest(TimingLogger tl, Context context, int iter) {
            RIPEMD160Digest ripemd = new RIPEMD160Digest();
            byte[] digest = new byte[ripemd.getDigestSize()];
            BigInteger hu, hp;
            String u = Password.pickRandomPassword(context, true);
            String p = Password.pickRandomPassword(context, true);

            tl.addSplit("--- Begin iteration: " + iter + " ---");

            hu = ripemd160ToBigInteger(u.getBytes(), digest, ripemd);
            hp = ripemd160ToBigInteger(p.getBytes(), digest, ripemd);
            ripemd.reset();

            tl.addSplit("RIPEMD 160");
            testSCrypt(2, tl, hu, hp);
        }

        private static void testSCrypt(int p, TimingLogger tl, BigInteger hu, BigInteger hp) {
            byte[] digest = SCrypt.generate((hu.toString() + hp.toString()).getBytes(),
                    (hu.xor(hp)).toByteArray(), (int) Math.pow(2, 16), 8, p, 64);
            tl.addSplit("SCrypt: 2^16, 8, " + p);

            byte[] k = Arrays.copyOfRange(digest, 0, 32);
            byte[] c = Arrays.copyOfRange(digest, 32, 64);
            byte[] kc = k.clone();
            byte[] cc = c.clone();

            tl.addSplit("Split array");

            splitDigestBlake(k, c);
            tl.addSplit("split digest - blake");

            splitDigestSha3(kc, cc);
            tl.addSplit("split digest - sha3");
        }

        private static ECKey splitDigestNoBlake(byte[] k, byte[] c) {
            return new ECKey(calcPublicKey(k), calcPublicKey(c));
        }



        private static ECKey splitDigestBlake(byte[] k, byte[] c) {
            Blake2b.Blake2b256 blake = new Blake2b.Blake2b256();

            for (int i = 0; i < 256; i++) {
                c = blake.digest(blake.digest(c));
            }

            return new ECKey(calcPublicKey(k), c);
        }

        private static ECKey splitDigestSha3(byte[] k, byte[] c) {
            SHA3.Digest256 sha = new SHA3.Digest256();

            for (int i = 0; i < 256; i++) {
                c = sha.digest(sha.digest(c));
            }

            return new ECKey(calcPublicKey(k), c);
        }
    }

    /**
     * Inner static class used to call static methods from ECKey one at a time
     * so that they can be check against a set of hardcoded test vectors, see
     * {@link TestManager}. This can be accessed in-app via Settings > Test
     * Vectors. See the above static method in ECKey for comments.
     */

    public static class UnitTest {

        public static String ripemd160Hex(String s) {
            RIPEMD160Digest ripe = new RIPEMD160Digest();
            byte[] in = s.getBytes();
            byte[] out = new byte[ripe.getDigestSize()];

            ripe.update(in, 0, in.length);
            ripe.doFinal(out, 0);

            return bytesToHex(out);
        }

        public static String genMasterExtPrivateKey(String u, String p) {
            BigInteger bu = new BigInteger(u, 16);
            BigInteger bp = new BigInteger(p, 16);

            return bytesToHex(calcMasterExtPrivateKey(bu, bp));
        }

        public static String genChildExtPrivatecKey(String par, String s) {
            byte[] p = new BigInteger(par, 16).toByteArray();
            byte[] cp = Arrays.copyOfRange(p, 32, 64);

            return bytesToHex(calcChildExtPrivate(cp, s));
        }

        public static String genExtPublicKey(String pri) {
            byte[] p = new BigInteger(pri, 16).toByteArray();
            ECKey k = calcExtPublicKey(p);

            return bytesToHex(k.getmKey()) + bytesToHex(k.getmChain());
        }
    }
}
