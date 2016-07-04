/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yuhaiyang.redpacket.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.yuhaiyang.redpacket.modem.Notification;

/**
 * 通知管理器
 */
public class NotificationManager {
    private static NotificationManager sInstance;
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    private NotificationManager(Context context) {
        mContext = context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static NotificationManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (NotificationManager.class) {
                if (sInstance == null) {
                    sInstance = new NotificationManager(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    /**
     * 是否启动通知栏模式
     */
    public boolean isEnabled() {
        return mSharedPreferences.getBoolean(Notification.Key.ENABLE, false);
    }

    /**
     * 设置启动通知栏模式
     */
    public void setEnabled(boolean enable) {
        mSharedPreferences.edit().putBoolean(Notification.Key.ENABLE, enable).apply();
    }

    /**
     * 是否开启声音
     */
    public boolean isEnabledSound() {
        return mSharedPreferences.getBoolean(Notification.Key.ENABLE_NOTIFY_SOUND, false);
    }

    /**
     * 设置是否开启声音
     */
    public void setEnabledSound(boolean enable) {
        mSharedPreferences.edit().putBoolean(Notification.Key.ENABLE_NOTIFY_SOUND, enable).apply();
    }

    /**
     * 是否开启震动
     */
    public boolean isEnabledVibrate() {
        return mSharedPreferences.getBoolean(Notification.Key.ENABLE_NOTIFY_VIBRATE, false);
    }

    /**
     * 设置是否开启震动
     */
    public void setEnabledVibrate(boolean enable) {
        mSharedPreferences.edit().putBoolean(Notification.Key.ENABLE_NOTIFY_VIBRATE, enable).apply();
    }
}
