/*
 * Copyright (c) 2016
 *
 * This file, TestManager.java, is apart of GeniPass.
 *
 * GeniPass is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * GeniPass is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GeniPass.  If not, see http://www.gnu.org/licenses/.
 */

package xyz.jamescarroll.genipass.Crypto;

import android.content.Context;

import xyz.jamescarroll.genipass.Async.AsyncTestVector;
import xyz.jamescarroll.genipass.R;

/**
 * Created by jamescarroll on 1/28/16.
 */
public class TestManager {
    private static TestManager ourInstance = new TestManager();
    private boolean mStartTest = false;
    private boolean mEndTest = false;
    private AsyncTestVector mAsyncTestVector;

    public static TestManager getInstance() {
        return ourInstance;
    }

    private TestManager() {
    }

    public boolean ismStartTest() {
        return mStartTest;
    }

    public void startTest() {
        this.mStartTest = true;
        this.mEndTest = false;
    }

    public boolean ismEndTest() {
        return mEndTest;
    }

    public void endTest() {
        this.mEndTest = true;
    }

    public void clearTest() {
        this.mStartTest = false;
        this.mEndTest = false;
    }

    public AsyncTestVector getmAsyncTestVector() {
        return mAsyncTestVector;
    }

    public void setmAsyncTestVector(AsyncTestVector mAsyncTestVector) {
        this.mAsyncTestVector = mAsyncTestVector;
    }

    public Vector[] getTestVectors(Context context) {
        return new Vector[] {
                new Vector("", "immortal finnish dionaea cubical appallingly tips ", "facebook", "",
                        context.getString(R.string.vec1_login_text), context.getString(R.string.vec_username),
                        context.getString(R.string.vec1_password),context.getString(R.string.vec1_master_private),
                        context.getString(R.string.vec1_master_public), context.getString(R.string.vec1_first_child_private),
                        context.getString(R.string.vec1_first_child_public), context.getString(R.string.vec1_second_child_private),
                        context.getString(R.string.vec1_second_child_public), "gaskin slummock parasympathomimetic bashbazouk redbone canine "),
                new Vector("", "autochthonous perdu gecko buckskin silliness thysanoptera ", "github", "alpha",
                        context.getString(R.string.vec2_login_text), context.getString(R.string.vec_username),
                        context.getString(R.string.vec2_password), context.getString(R.string.vec2_master_private),
                        context.getString(R.string.vec2_master_public), context.getString(R.string.vec2_first_child_private),
                        context.getString(R.string.vec2_first_child_public), context.getString(R.string.vec2_second_child_private),
                        context.getString(R.string.vec2_second_child_public), "ses homophonic regulate empetrum hilariously stuffy "),
                new Vector("", "dyaus titillate roulade appelidage disputation penitentiary ", "google", "main",
                        context.getString(R.string.vec3_login_text), context.getString(R.string.vec_username),
                        context.getString(R.string.vec3_password), context.getString(R.string.vec3_master_private),
                        context.getString(R.string.vec3_master_public), context.getString(R.string.vec3_first_child_private),
                        context.getString(R.string.vec3_first_child_public), context.getString(R.string.vec3_second_child_private),
                        context.getString(R.string.vec3_second_child_public), "monario seraphina alimentative sauerkraut atropidae dictyoptera "),
                new Vector("", "chronological malady denim drip existing hellenic ", "Twitter",
                        "Some tag", context.getString(R.string.vec4_login_text), context.getString(R.string.vec_username),
                        context.getString(R.string.vec4_password), context.getString(R.string.vec4_master_private),
                        context.getString(R.string.vec4_master_public), context.getString(R.string.vec4_first_child_private),
                        context.getString(R.string.vec4_first_child_public), context.getString(R.string.vec4_second_child_private),
                        context.getString(R.string.vec4_second_child_public), "glassblower hypobetalipoproteinemia ischiagra fastened gadoid bouncer "),
                new Vector("", "louisiana ni-resist baltimore saprophyte guerilla blintz ",
                        "Another site", "", context.getString(R.string.vec5_login_text), context.getString(R.string.vec_username),
                        context.getString(R.string.vec5_password), context.getString(R.string.vec5_master_private),
                        context.getString(R.string.vec5_master_public), context.getString(R.string.vec5_first_child_private),
                        context.getString(R.string.vec5_first_child_public), context.getString(R.string.vec5_second_child_private),
                        context.getString(R.string.vec5_second_child_public), "char salis ship-breaker brydges trafalgar pure ")
        };
    }

    public static class Vector {
        public static final String kRipemdUsername = "RIPEMD Username";
        public static final String kRipemdPassword = "RIPEMD Password";
        public static final String kLoginText = "Blake2 Login Text";
        public static final String kMasterExtPrivate = "Master Extended Private";
        public static final String kMasterExtPublic = "Master Extended Public";
        public static final String kFirstChildExtPrivate = "First Child Extended Private";
        public static final String kFirstChildExtPublic = "First Child Extended Public";
        public static final String kSecondChildExtPrivate = "Second Child Extended Private";
        public static final String kSecondChildExtPublic = "Second Child Extended Public";
        public static final String kGenPassword = "Generated Password";

        private String mUsername;
        private String mPassword;
        private String mService;
        private String mTag;
        private String mLoginText;
        private String mRipemdUsername;
        private String mRipemdPassword;
        private String mMasterExtPrivate;
        private String mMasterExtPublic;
        private String mFirstChildExtPrivate;
        private String mFirstChildExtPublic;
        private String mSecondChildExtPrivate;
        private String mSecondChildExtPublic;
        private String mChildPassword;
        private String mResult = "";
        private int mNumPass = 0;

        private Vector(String mUsername, String mPassword, String mService, String mTag) {
            this.mUsername = mUsername;
            this.mPassword = mPassword;
            this.mService = mService;
            this.mTag = mTag;
        }

        public Vector(String mUsername, String mPassword, String mService, String mTag,
                      String mLoginText, String mRipemdUsername, String mRipemdPassword,
                      String mMasterExtPrivate, String mMasterExtPublic, String mFirstChildExtPrivate,
                      String mFirstChildExtPublic, String mSecondChildExtPrivate,
                      String mSecondChildExtPublic, String mChildPassword) {
            this.mUsername = mUsername;
            this.mPassword = mPassword;
            this.mService = mService;
            this.mTag = mTag;
            this.mLoginText = mLoginText;
            this.mRipemdUsername = mRipemdUsername;
            this.mRipemdPassword = mRipemdPassword;
            this.mMasterExtPrivate = mMasterExtPrivate;
            this.mMasterExtPublic = mMasterExtPublic;
            this.mFirstChildExtPrivate = mFirstChildExtPrivate;
            this.mFirstChildExtPublic = mFirstChildExtPublic;
            this.mSecondChildExtPrivate = mSecondChildExtPrivate;
            this.mSecondChildExtPublic = mSecondChildExtPublic;
            this.mChildPassword = mChildPassword;
        }

        public void compareTo(String in, String expected, String test) {
            if (in.equals(expected)) {
               test += " PASS:\n" + in;
                mNumPass++;
            } else {
                test += " FAIL:\nGot\n" + in + "\nExpected\n" + expected;
            }

            mResult += test + "\n\n";
        }

        public String getmUsername() {
            return mUsername;
        }

        public String getmPassword() {
            return mPassword;
        }

        public String getmService() {
            return mService;
        }

        public String getmTag() {
            return mTag;
        }

        public String getmRipemdUsername() {
            return mRipemdUsername;
        }

        public String getmRipemdPassword() {
            return mRipemdPassword;
        }

        public String getmLoginText() {
            return mLoginText;
        }

        public String getmMasterExtPrivate() {
            return mMasterExtPrivate;
        }

        public String getmMasterExtPublic() {
            return mMasterExtPublic;
        }

        public String getmFirstChildExtPrivate() {
            return mFirstChildExtPrivate;
        }

        public String getmFirstChildExtPublic() {
            return mFirstChildExtPublic;
        }

        public String getmSecondChildExtPrivate() {
            return mSecondChildExtPrivate;
        }

        public String getmSecondChildExtPublic() {
            return mSecondChildExtPublic;
        }

        public String getmChildPassword() {
            return mChildPassword;
        }

        public String getmResult() {
            return mNumPass + " out of 10 passed. \n\n" + mResult;
        }


    }
}
