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

import com.yuhaiyang.redpacket.modem.WeChat;

/**
 * 微信管理器
 */
public class WeChatManager {
    private static WeChatManager sInstance;
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    private WeChatManager(Context context) {
        mContext = context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
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
     * 设置是否可用
     */
    public void setEnalbe(boolean enable) {
        mSharedPreferences.edit().putBoolean(WeChat.Key.ENABLE, enable).apply();
    }

    /**
     * 获取是否可用
     */
    public boolean isEnable() {
        return mSharedPreferences.getBoolean(WeChat.Key.ENABLE, true);
    }


    /**
     * 设置微信红包的模式
     */
    public void setGrapMode(int mode) {
        mSharedPreferences.edit().putInt(WeChat.Key.GRAP_MODE, mode).apply();
    }

    /**
     * 获取抢微信红包的模式
     */
    public int getGrapMode() {
        return mSharedPreferences.getInt(WeChat.Key.GRAP_MODE, WeChat.Configure.GRAP_MODE_AUTO);
    }


    /**
     * 设置微信抢到红包后的事件
     */
    public void setGetHongBaoAfterEvent(int mode) {
        mSharedPreferences.edit().putInt(WeChat.Key.GET_HONGBAO_AFTER_EVENT, mode).apply();
    }


    /**
     * 微信抢到红包后的事件
     */
    public int getGetHongBaoAfterEvent() {
        return mSharedPreferences.getInt(WeChat.Key.GET_HONGBAO_AFTER_EVENT, WeChat.Configure.GET_AFTER_HONGBAO_EVENT_OPEN);
    }

    /**
     * 微信打开红包后延时时间
     */
    public void setOpenDelayTime(int time) {
        mSharedPreferences.edit().putInt(WeChat.Key.OPEN_DELAY_TIME, time).apply();
    }

    /**
     * 微信打开红包后延时时间
     */
    public int getOpenDelayTime() {
        return mSharedPreferences.getInt(WeChat.Key.OPEN_DELAY_TIME, WeChat.Configure.DEFAULT_DELAY_TIME);
    }

}
