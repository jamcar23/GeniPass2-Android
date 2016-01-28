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

import xyz.jamescarroll.genipass.R;

/**
 * Created by jamescarroll on 1/28/16.
 */
public class TestManager {
    private static TestManager ourInstance = new TestManager();
    private boolean mStartTest = false;
    private boolean mEndTest = false;

    public static TestManager getInstance() {
        return ourInstance;
    }

    private TestManager() {
    }

    public boolean ismStartTest() {
        return mStartTest;
    }

    public void setmStartTest(boolean mStartTest) {
        this.mStartTest = mStartTest;
    }

    public boolean ismEndTest() {
        return mEndTest;
    }

    public void setmEndTest(boolean mEndTest) {
        this.mEndTest = mEndTest;
    }

    public Vector[] getTestVectors(Context context) {
        return new Vector[] {
                new Vector("", "vector1", "facebook", "", context.getString(R.string.vec_username),
                        context.getString(R.string.vec1_password), context.getString(R.string.vec1_master_private),
                        context.getString(R.string.vec1_master_public), context.getString(R.string.vec1_first_child_private),
                        context.getString(R.string.vec1_first_child_public), context.getString(R.string.vec1_second_child_private),
                        context.getString(R.string.vec1_second_child_public), "cyma nauseated fettuccine schlep arbitrariness evert "),
                new Vector("", "vector2", "github", "alpha", context.getString(R.string.vec_username),
                        context.getString(R.string.vec2_password), context.getString(R.string.vec2_master_private),
                        context.getString(R.string.vec2_master_public), context.getString(R.string.vec2_first_child_private),
                        context.getString(R.string.vec2_first_child_public), context.getString(R.string.vec2_second_child_private),
                        context.getString(R.string.vec2_second_child_public), "parulidae epona jaculate lamaist equalize quis "),
                new Vector("", "vector3", "google", "main", context.getString(R.string.vec_username),
                        context.getString(R.string.vec3_password), context.getString(R.string.vec3_master_private),
                        context.getString(R.string.vec3_master_public), context.getString(R.string.vec3_first_child_private),
                        context.getString(R.string.vec3_first_child_public), context.getString(R.string.vec3_second_child_private),
                        context.getString(R.string.vec3_second_child_public), "sicence dapper rectitude beakless ievidelicet codling ")
        };
    }

    public static class Vector {
        public static final String kRipemdUsername = "RIPEMD Username";
        public static final String kRipemdPassword = "RIPEMD Password";
        public static final String kMasterExtPrivate = "Master Extended Private";
        public static final String kMasterExtPublic = "Master Extended Public";
        public static final String kFirstChildExtPrivate = "First Child Extender Private";
        public static final String kFirstChildExtPublic = "First Child Extender Public";
        public static final String kSecondChildExtPrivate = "Second Child Extender Private";
        public static final String kSecondChildExtPublic = "Second Child Extender Public";
        public static final String kGenPassword = "Generated Password";

        private String mUsername;
        private String mPassword;
        private String mService;
        private String mTag;
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

        public Vector(String mUsername, String mPassword, String mService, String mTag) {
            this.mUsername = mUsername;
            this.mPassword = mPassword;
            this.mService = mService;
            this.mTag = mTag;
        }

        public Vector(String mUsername, String mPassword, String mService, String mTag,
                      String mRipemdUsername, String mRipemdPassword, String mMasterExtPrivate,
                      String mMasterExtPublic, String mFirstChildExtPrivate,
                      String mFirstChildExtPublic, String mSecondChildExtPrivate,
                      String mSecondChildExtPublic, String mChildPassword) {
            this.mUsername = mUsername;
            this.mPassword = mPassword;
            this.mService = mService;
            this.mTag = mTag;
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
               test += " PASS: " + in;
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
            return mNumPass + " out of 9 passed. \n\n" + mResult;
        }
    }
}
