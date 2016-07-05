/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yuhaiyang.redpacket.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.yuhaiyang.redpacket.modem.WeChat;

/**
 * 微信管理器
 */
public class WeChatManager {
    private static final String TAG = "WeChatManager";

    private static WeChatManager sInstance;
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    /**
     * 微信的相关信息
     */
    private PackageInfo mPackageInfo;

    private WeChatManager(Context context) {
        mContext = context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        updatePackageInfo();
        registerReceiver();
    }

    public static WeChatManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (WeChatManager.class) {
                if (sInstance == null) {
                    sInstance = new WeChatManager(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取是否可用
     */
    public boolean isEnabled() {
        return mSharedPreferences.getBoolean(WeChat.Key.ENABLE, true);
    }

    /**
     * 设置是否可用
     */
    public void setEnabled(boolean enable) {
        mSharedPreferences.edit().putBoolean(WeChat.Key.ENABLE, enable).apply();
    }

    /**
     * 获取抢微信红包的模式
     */
    public int getGrapMode() {
        return mSharedPreferences.getInt(WeChat.Key.GRAP_MODE, WeChat.Configure.GRAP_MODE_AUTO);
    }

    /**
     * 设置微信红包的模式
     */
    public void setGrapMode(int mode) {
        mSharedPreferences.edit().putInt(WeChat.Key.GRAP_MODE, mode).apply();
    }

    /**
     * 微信抢到红包后的事件
     */
    public int getGetHongBaoAfterEvent() {
        return mSharedPreferences.getInt(WeChat.Key.GET_HONGBAO_AFTER_EVENT, WeChat.Configure.GET_AFTER_HONGBAO_EVENT_OPEN);
    }

    /**
     * 设置微信抢到红包后的事件
     */
    public void setGetHongBaoAfterEvent(int mode) {
        mSharedPreferences.edit().putInt(WeChat.Key.GET_HONGBAO_AFTER_EVENT, mode).apply();
    }

    /**
     * 微信打开红包后延时时间
     */
    public int getOpenDelayTime() {
        return mSharedPreferences.getInt(WeChat.Key.OPEN_DELAY_TIME, WeChat.Configure.DEFAULT_DELAY_TIME);
    }

    /**
     * 微信打开红包后延时时间
     */
    public void setOpenDelayTime(int time) {
        mSharedPreferences.edit().putInt(WeChat.Key.OPEN_DELAY_TIME, time).apply();
    }


    private void registerReceiver() {
        Log.i(TAG, "registerReceiver: ");
        IntentFilter filter = new IntentFilter();
        filter.addDataScheme("package");
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        mContext.registerReceiver(mReceiver, filter);
    }

    private void unregisterReceiver() {
        mContext.unregisterReceiver(mReceiver);
    }

    /**
     * 获取微信的版本
     */
    public int getWechatVersion() {
        if (mPackageInfo == null) {
            return 0;
        }
        return mPackageInfo.versionCode;
    }

    /**
     * 更新微信包信息
     */
    private void updatePackageInfo() {
        try {
            mPackageInfo = mContext.getPackageManager().getPackageInfo(WeChat.Configure.PACKAGE_NAME, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //更新安装包信息
            updatePackageInfo();
        }
    };
}
