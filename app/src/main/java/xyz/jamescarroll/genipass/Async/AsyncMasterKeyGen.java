/*
 * Copyright (c) 2016
 *
 * This file, AsyncMasterKeyGen.java, is apart of GeniPass.
 *
 * GeniPass is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * GeniPass is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GeniPass.  If not, see http://www.gnu.org/licenses/.
 */

package xyz.jamescarroll.genipass.Async;

import xyz.jamescarroll.genipass.Crypto.ECKey;

/**
 * Created by James Carroll on 1/20/16.
 */
public class AsyncMasterKeyGen extends AsyncKeyGen {

    public AsyncMasterKeyGen() {
        super();
    }

    @Override
    protected ECKey doInBackground(String... params) {
        return ECKey.genFromSeeds(params[0], params[1]);
    }

    @Override
    protected void onPostExecute(ECKey key) {
        mListener.onKeyGeneration(key);
    }
}
