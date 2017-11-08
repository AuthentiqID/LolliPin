package com.github.omadahealth.lollipin.lib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import com.github.omadahealth.lollipin.lib.interfaces.LifeCycleInterface;
import com.github.omadahealth.lollipin.lib.managers.AppLockActivity;

/**
 * com.github.omadahealth.lollipin.lib
 * AuthentiqID
 * Created by Alexandros Keramidas on 7/13/2016.
 * Copyright (c) 2016 Authentiq. All rights reserved.
 */
public class SinglePinCompatActivity extends AppCompatActivity {
    private static LifeCycleInterface mLifeCycleListener;
    private final BroadcastReceiver mPinCancelledReceiver;
    /**
     * The {@link android.content.SharedPreferences} key used to store if it should lock immediately
     */
    private static final String SHOULD_LOCK_NOW_PREFERENCE_KEY = "SHOULD_LOCK_NOW";

    public SinglePinCompatActivity() {
        super();
        mPinCancelledReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putBoolean(SHOULD_LOCK_NOW_PREFERENCE_KEY, false);
                editor.apply();
                finish();
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(AppLockActivity.ACTION_CANCEL);
        LocalBroadcastManager.getInstance(this).registerReceiver(mPinCancelledReceiver, filter);
    }

    @Override
    protected void onResume() {
        if (mLifeCycleListener != null) {
            mLifeCycleListener.onActivityResumed(SinglePinCompatActivity.this);
        }
        super.onResume();
    }

    @Override
    public void onUserInteraction() {
        if (mLifeCycleListener != null) {
            mLifeCycleListener.onActivityUserInteraction(SinglePinCompatActivity.this);
        }
        super.onUserInteraction();
    }

    @Override
    protected void onPause() {
        if (mLifeCycleListener != null) {
            mLifeCycleListener.onActivityPaused(SinglePinCompatActivity.this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPinCancelledReceiver);
    }

    public static void setListener(LifeCycleInterface listener) {
        if (mLifeCycleListener != null) {
            mLifeCycleListener = null;
        }
        mLifeCycleListener = listener;
    }

    public static void clearListeners() {
        mLifeCycleListener = null;
    }

    public static boolean hasListeners() {
        return (mLifeCycleListener != null);
    }
}
