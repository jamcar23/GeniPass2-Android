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

    private static int indexChar(char c) {
        if (c < 'a' || c > 'z') {
            return 0;
        }

        return c - 'a' + 1;
    }

    private static int getCharSpaceSize(String s) {
        int count = 0;

        if (s.matches(".*[a-z].*")) {
            count += 26;
        }

        if (s.matches(".*[A-Z].*")) {
            count += 26;
        }

        if (s.matches(".*[0-9].*")) {
            count += 10;
        }

        if (s.matches(".*[!@#$%^&*()].*")) {
            count += 10;
        }

        if (s.matches(".*[;:'<>,.?/~`].*")) {
            count += 6;
        }

        if (s.matches(".*[ ].*")) {
            ++count;
        }

        return count;
    }

    private static double[] calcFrequencyTable() {
        final char s = ' ';
        String freq = "gL6-A$A:#@'$KT#<c\"06\"8*\"Wb\"5-$Kr t%#f+\"S4$7;$ v!R=%!6 g@#s}&'P#rM\"a8\"K'!3= #V }o wx*bZ\"X=\"De#2 #~B!dL!\\\\\"(u qj#Pf L_\"e (1K$9--x6 SE\"x- .$)/s%rK(w\"\"QV\"oQ ;; {_ [| q@(8t-G7#nU MW cS2$9 UJ GH <]*#S !: >!'kQ \\Z @A)O8 5& E (L\"\"YZ r_&g2 66 ~& *L!n* zO,|Y)B8 V.#X: qI'V0 B7 s@4<B)>= I-$wt!;A =h ?G+y: p/ |k\"}} &w\"]?#72 W) j$ @w S !I700V'gf (Q cf!9{9cL Qa .i #Z, F *M NZ!0c RX!+%((N S$ ]o#hU#{o Z2#j_ 6P d% I!!wX _Z.nH!i.!@y!6T#a$\"Yt ?]!kv Jl\"]{ sh!2$&x~\"5F.8X AF!n2 :A1G@('.%5} 24!TZ XY \\P zq G,).J)>} GD _o Xu*di&m0!%Q  L-KA @I ei%4P <S 8m+NI l& Gg&m+\"nS$T{%&? YT e( Mu g2 ~., B(\\P N6 .d .M:^6 gg\"]s\"k2':6 LU /h#k_ 6-#$3%p9 W$ 7x&)(%_-\">[#Q8 VM y( 3q c* vn/va-K> ^N Il LQ/y9 qF *< /k+^O zp K;\"bZ!M\\!>(*NW L' .-#=<\"Uz$^:$o? C] F' /)\"+) FC+8H$M&!l1&2'#n\"&K<!d-#\"_ jw A{ J(#~e$Yi#(v.Ng#NF!V8 zM\"Q4*RH&.X E\"\"n; ^5 41 gM!xj(\"Q1![ bn #!\";?1la yU mR A6)GK @=\"w5!k$!bX\"b[)K-!R) Mq Fl&uz Zx&p\\ <A X' f&!B_ xj)GW.3F <G *5 .e-}6 (6 C-!U>)S< :r#=;#:C 9Q!xJ,7< w\" Tp#*1%|F#Ze&JW Dn /$ ](\"4( H{&>!.@h W[ .F\"G}/#k :_ ,U ;$04E \\,!/P)h9!Hq f4( f S& 18 M^\":'#w|#]o 7= !# Y]\"*4 YZ-<E0$]\"B^ QO bn/$G )> zO e>-|+ \\? SY (d$nj b%)p+%aQ 2P oa!U\\ mp$x: S: *$ &.!9( 3-3KD&WW WG\"jB'`),:t I<'T_ I=($C Pd!;M 5I 3a\"h %_G $, .E (_%wZ(WC!*] w> \"$ s} WT h;*Kd UY!JW\")u\",)\"v6!AU\"Mv RS\"hZ \"(\"Ya&e/%5S-w\\\"/W#b1 lF*fp%a6$3c$aH#uf!e> cH!]v 58*En+r< Ic v< gh,&\\ +A ;a$2!)I. !8 :w$>E 2Q fH+LA$)$ Gl+NL\"y9\"43$30 \"8 d% 8l u% cV$pm 6C 2O uH .S K@ $= DB B(!f6 =9 <R *I uH A@ dG )A `K NX 3L !sopL %: o1 h0 9/ (U(5?-Q8!&=!iX\"f(0\\[ ~V!ii )\\,<? 2W!l*!}#\"\\8#NX)pn S0 Iv!{M$Rs#A2#s\"!$R k& ]J!Yi {#4jR%m` --$\\D ;(*>X ]K lS\"H~'c; ;K#hP!/^!(l !S#z+##d [H F\\%Z3/b-#)f /O c% 66!YO 2w+{3,}? {N I1  O3^6 !? xV\"s5-3I `R wn (E wj Kq';: KA XU&_'#b|$<N$2C qX w# iu!yO Fr($1\"T@\"pg\"eA#+3'g<!w$\":w `7$\\ !Z)#qf%I4%$x*pf!kN\"/* T9(Z])+?&O5\"9\"!nP 2< {9 5q R-'0~5)4 ^= j8 us9#/ @1 }3 Ul0+- U6 *K!&? +K!]p()0 Z$ va!e} Y< fl!rQ *) C( KJ#f4 $;'v.5aO W` w*!A<0IC yG ru!KR-*/ <\\ ,-!&3!Ap&Jc'+` V& s5\"gB$gF!`>!B0 hO g. ;w\"I5 |v7:@&:o %h#zz H\"(wx tL rk!q\\*w9 {D Q-!T_!lF!O$%vF&cB 8 \"Mp!SZ'he!HP rq!>N\"BA\"Y3 wR8^U$.~ ee#K[\";,#IZ ;f!KI!eE!XQ d-#s/$JY%Pz%/F$+Y#2$ XX#Ez'5g$z9\"9$!1y vB 51!La @T*\\f0)j ek L3!^@1t- II Zn m@,08 )0!-@!<-! |\"Fp&Oj A& N_ U[ P>!-h$D` 3a!K' `d#uc%";
        double i;
        double[] freqList = new double[freq.length() / 3];
        int j = 0;

        while (freq.length() > 0) {
            i = freq.charAt(0) - s;
            i /= 95;
            i += freq.charAt(1) - s;
            i /= 95;
            i += freq.charAt(2) - s;
            i /= 95;

            freq = freq.substring(3, freq.length());
            freqList[j] = i;

            j++;
        }

        return freqList;
    }

    public static int calcEntropy(String s) {
        final char[] chars = s.toLowerCase().toCharArray();
        double a, b, c, d, e = 0;
        double[] freq;

        if (s.isEmpty()) {
            return 0;
        }

        d = Math.log(getCharSpaceSize(s)) / Math.log(2);
        a = indexChar(chars[0]);
        freq = calcFrequencyTable();

        for (char ch : chars) {
            b = indexChar(ch);
            c = 1.0 - freq[((int) (a * 27 + b))];
            e += d * c * c * c;
            a = b;
        }

        return (int) e;
    }

    public static double calcTimeToCrack(int entropy) {
        return 0.5 * Math.pow(2, entropy) * (0.01 / 100);

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


    public static String pickPassword(byte[] bytes, Context context) {
        return pickPassword(bytesToHex(bytes), context);
    }

    public static String pickPassword(String hex, Context context) {
        String t;
        int n = 0;
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
                    pass[wordLoc.get(n)] = t + " ";
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
