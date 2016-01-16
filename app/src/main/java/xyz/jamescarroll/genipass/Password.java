package xyz.jamescarroll.genipass;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by James Carroll on 1/15/16.
 */
public class Password {
    private static final String TAG = "Password.TAG";
    private static final SecureRandom kRand = new SecureRandom();
    public static final int kMaxLine = 69903;


    public static int calcEntropy(String s) {
        int num = 0;
        double prob, ent = 0;
        char c;
        Map<Character, Integer> m = new HashMap<>();

        for (; num < s.length(); num++) {
            c = s.charAt(num);
            m.put(c, m.containsKey(c) ? m.get(c) + 1 : 1);
        }

        for (Map.Entry<Character, Integer> entry : m.entrySet()) {
            prob = (double) entry.getValue() / (double) num;
            ent += prob * (Math.log(prob) / Math.log(2));
        }

        ent = -ent;

        return (int) ent;
    }

    private static String bytesToHex(byte[] bytes) {
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

    public static String pickRandomPassword(Context context) {
        String p = "";
        byte[] bytes = new byte[15];

        while (calcEntropy(p) < 100) {
            kRand.nextBytes(bytes);
            p = pickPassword(bytesToHex(bytes), context);
        }

        return p;
    }

    public static String pickPassword(String hex, Context context) {
        String t;
        int n = 0, j= 0;
        char[] charHex = hex.toCharArray();
        Map<Integer, Integer> wordLoc= new HashMap<>();
        String[] pass = new String[6];
        InputStream is;
        BufferedReader br;
        StringBuilder sb;

        for (int i = 0; i < charHex.length; i += 4) {
            if (i == charHex.length - 1 || wordLoc.size() == 6) {
                break;
            }

            t = "";
            t += charHex[i];
            t += charHex[i + 1];
            t += charHex[i + 2];
            t += charHex[i + 3];

            wordLoc.put(Integer.parseInt(t, 16), n++);
        }

        is = context.getResources().openRawResource(R.raw.wordlist_53k);
        br = new BufferedReader(new InputStreamReader(is));

        n = 0;

        try {
            while ((t = br.readLine()) != null) {
                if (wordLoc.containsKey(n)) {
                    pass[wordLoc.get(n)] = t;
                }
                n++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        sb = new StringBuilder();

        for (String p : pass) {
            sb.append(p);
        }

        return sb.toString();
    }

}
