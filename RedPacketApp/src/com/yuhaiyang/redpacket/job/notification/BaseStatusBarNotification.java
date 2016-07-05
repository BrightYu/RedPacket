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

package com.yuhaiyang.redpacket.job.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.os.Build;
import android.service.notification.StatusBarNotification;

/**
 * 基础通知
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BaseStatusBarNotification implements IStatusBarNotification {
    private StatusBarNotification notification;

    public BaseStatusBarNotification(StatusBarNotification notification) {
        this.notification = notification;
    }

    @Override
    public String getPackageName() {
        return notification.getPackageName();
    }

    @Override
    public Notification getNotification() {
        return notification.getNotification();
    }
}
