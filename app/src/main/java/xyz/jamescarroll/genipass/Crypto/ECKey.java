package xyz.jamescarroll.genipass.Crypto;

import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.generators.SCrypt;

import java.math.BigInteger;

/**
 * Created by James Carroll on 1/19/16.
 */
public class ECKey extends CryptoUtil {
    private static final X9ECParameters kCurve = SECNamedCurves.getByName("secp521r1");

    private byte[] mKey;
    private byte[] mChain;

    private ECKey(byte[] mKey, byte[] mChain) {
        this.mKey = mKey;
        this.mChain = mChain;
    }

    public void generateChildKey(String s) {
        ECKey child;

        return;
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

    public static ECKey genFromSeeds(String u, String p) {
        ECKey eck;
        RIPEMD160Digest ripemd = new RIPEMD160Digest();
        byte[] digest = new byte[ripemd.getDigestSize()];
        byte[] k = new byte[32];
        byte[] c = new byte[32];
        BigInteger hu, hp;

        hu = ripemd160ToBigInteger(u.getBytes(), digest, ripemd);
        hp = ripemd160ToBigInteger(p.getBytes(), digest, ripemd);
        ripemd.reset();

        digest = SCrypt.generate((hu.toString() + hp.toString()).getBytes(),
                (hu.xor(hp)).toByteArray(), (int) Math.pow(2, 16),  8, 4, 64);

        for (int i = 0; i < digest.length; i++) {
            if (i < 32) {
                k[i] = digest[i];
            } else {
                c[i % 32] = digest[i];
            }
        }

        eck = new ECKey(k, c);
        return eck;
    }
}
