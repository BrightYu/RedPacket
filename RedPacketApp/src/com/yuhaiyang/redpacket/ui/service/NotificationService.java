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

package com.yuhaiyang.redpacket.ui.service;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.yuhaiyang.redpacket.constant.Config;
import com.yuhaiyang.redpacket.job.notification.BaseStatusBarNotification;
import com.yuhaiyang.redpacket.manager.NotificationManager;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationService extends NotificationListenerService {
    private static final String TAG = "NotificationService";

    private static NotificationService sInstance;
    private NotificationManager mNotificationManager;

    /**
     * 是否启动通知栏监听
     */
    public static boolean isRunning() {
        return (sInstance != null);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            onListenerConnected();
        }
        mNotificationManager = NotificationManager.getInstance(this);
    }

    @Override
    public void onNotificationPosted(final StatusBarNotification sbn) {
        Log.i(TAG, "onNotificationPosted");

        if (!mNotificationManager.isEnabled()) {
            return;
        }
        BaseStatusBarNotification notification = new BaseStatusBarNotification(sbn);
        RedPacketService.handeNotificationPosted(notification);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onNotificationRemoved(sbn);
        }
        Log.i(TAG, "onNotificationRemoved");
    }

    @Override
    public void onListenerConnected() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onListenerConnected();
        }

        Log.i(TAG, "onListenerConnected");
        sInstance = this;
        //发送广播，已经连接上了
        Intent intent = new Intent(Config.ACTION_NOTIFY_SERVICE_CONNECT);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        sInstance = null;
        //发送广播，已经断开连接
        Intent intent = new Intent(Config.ACTION_NOTIFY_SERVICE_DISCONNECT);
        sendBroadcast(intent);
    }
}
