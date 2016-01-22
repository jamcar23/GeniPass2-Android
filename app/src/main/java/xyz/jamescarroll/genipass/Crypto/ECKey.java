package xyz.jamescarroll.genipass.Crypto;

import android.util.Log;

import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.generators.SCrypt;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by James Carroll on 1/19/16.
 */
public class ECKey extends CryptoUtil {
    private static final String TAG = "ECKey.TAG";
    private static final X9ECParameters kCurve = SECNamedCurves.getByName("secp256k1");

    private byte[] mKey;
    private byte[] mChain;

    private ECKey(byte[] mKey, byte[] mChain) {
        this.mKey = mKey;
        this.mChain = mChain;
    }

    public ECKey generateChildKey(String s) {
        final String hmacSHA512 = "HmacSHA512";
        ECKey child = null;
        Mac hmac = null;
        SecretKeySpec secret;

        try {
             hmac = Mac.getInstance(hmacSHA512);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "generateChildKey: ", e);
        }

        if (hmac != null) {
            secret = new SecretKeySpec(getmChain(), hmacSHA512);

            try {
                hmac.init(secret);
                child = ECKey.splitDigestIntoECKey(hmac.doFinal(s.getBytes()));
            } catch (InvalidKeyException e) {
                Log.e(TAG, "generateChildKey: ", e);
            }
        }

        return child;
    }

    public byte[] getmKey() {
        return mKey;
    }

    public byte[] getmChain() {
        return mChain;
    }

    private static BigInteger ripemd160ToBigInteger(byte[] in, byte[] out, RIPEMD160Digest ripemd) {
        BigInteger h;

        ripemd.update(in, 0, in.length);
        ripemd.doFinal(out, 0);
        h = new BigInteger(out);
        zeroByteArray(out);

        return h;
    }

    private static byte[] calcPublicKey(byte[] bytes) {
        return kCurve.getG().multiply(new BigInteger(bytes)).getEncoded(true);
    }

    private static ECKey splitDigestIntoECKey(byte[] digest) {
        return new ECKey(calcPublicKey(Arrays.copyOfRange(digest, 0, 32)),
                calcPublicKey(Arrays.copyOfRange(digest, 32, 64)));
    }

    public static ECKey genFromSeeds(String u, String p) {
        RIPEMD160Digest ripemd = new RIPEMD160Digest();
        byte[] digest = new byte[ripemd.getDigestSize()];
        BigInteger hu, hp;

        hu = ripemd160ToBigInteger(u.getBytes(), digest, ripemd);
        hp = ripemd160ToBigInteger(p.getBytes(), digest, ripemd);
        ripemd.reset();

        digest = SCrypt.generate((hu.toString() + hp.toString()).getBytes(),
                (hu.xor(hp)).toByteArray(), (int) Math.pow(2, 16),  8, 4, 64);

        return splitDigestIntoECKey(digest);
    }
}
