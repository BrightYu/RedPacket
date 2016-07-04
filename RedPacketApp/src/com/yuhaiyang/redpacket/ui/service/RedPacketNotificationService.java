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
import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.yuhaiyang.redpacket.BuildConfig;
import com.yuhaiyang.redpacket.constant.Config;
import com.yuhaiyang.redpacket.job.IStatusBarNotification;
import com.yuhaiyang.redpacket.manager.NotificationManager;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class RedPacketNotificationService extends NotificationListenerService {
    private static final String TAG = "NotificationService";

    private static RedPacketNotificationService service;
    private NotificationManager mNotificationManager;

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
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "onNotificationRemoved");
        }
        if (!mNotificationManager.isEnabled()) {
            return;
        }
        RedPacketService.handeNotificationPosted(new IStatusBarNotification() {
            @Override
            public String getPackageName() {
                return sbn.getPackageName();
            }

            @Override
            public Notification getNotification() {
                return sbn.getNotification();
            }
        });
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onNotificationRemoved(sbn);
        }
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "onNotificationRemoved");
        }
    }

    @Override
    public void onListenerConnected() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onListenerConnected();
        }

        Log.i(TAG, "onListenerConnected");
        service = this;
        //发送广播，已经连接上了
        Intent intent = new Intent(Config.ACTION_NOTIFY_LISTENER_SERVICE_CONNECT);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        service = null;
        //发送广播，已经连接上了
        Intent intent = new Intent(Config.ACTION_NOTIFY_LISTENER_SERVICE_DISCONNECT);
        sendBroadcast(intent);
    }

    /**
     * 是否启动通知栏监听
     */
    public static boolean isRunning() {
        if (service == null) {
            return false;
        }
        return true;
    }
}
