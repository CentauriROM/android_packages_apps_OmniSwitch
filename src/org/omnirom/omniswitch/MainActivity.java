/*
 *  Copyright (C) 2013 The OmniROM Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.omnirom.omniswitch;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private static final boolean DEBUG = false;

    private ActivityReceiver mReceiver;
    private boolean mFinishedCalled;

    public class ActivityReceiver extends BroadcastReceiver {
        public static final String ACTION_FINISH = "org.omnirom.omniswitch.ACTION_FINISH_ACTIVITY";

        @Override
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_FINISH.equals(action) && !mFinishedCalled) {
                if(DEBUG){
                    Log.d(TAG, "onReceive " + action);
                }

                mFinishedCalled = true;
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(DEBUG){
            Log.d(TAG, "onCreate");
        }

        mFinishedCalled = false;
        mReceiver = new ActivityReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ActivityReceiver.ACTION_FINISH);

        registerReceiver(mReceiver, filter);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        if(DEBUG){
            Log.d(TAG, "onPause");
        }
        Intent hideRecent = new Intent(
                SwitchService.RecentsReceiver.ACTION_HIDE_OVERLAY);
        sendBroadcast(hideRecent);
        super.onPause();
    }

    @Override
    public void onResume() {
        if(DEBUG){
            Log.d(TAG, "onResume");
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if(DEBUG){
            Log.d(TAG, "onDestroy");
        }
        unregisterReceiver(mReceiver);
        mFinishedCalled = false;
        super.onDestroy();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // dont restart activity on orientation changes
    }
}
