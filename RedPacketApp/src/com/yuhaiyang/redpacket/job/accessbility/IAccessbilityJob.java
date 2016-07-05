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

package com.yuhaiyang.redpacket.job.accessbility;

import android.view.accessibility.AccessibilityEvent;

import com.yuhaiyang.redpacket.job.notification.IStatusBarNotification;
import com.yuhaiyang.redpacket.ui.service.RedPacketService;

public interface IAccessbilityJob {
    /**
     * 获取目标应用的包名
     */
    String getTargetPackageName();

    /**
     * 初始化job
     */
    void onCreateJob(RedPacketService service);

    /**
     * 获取到job任务
     */
    void onReceiveJob(AccessibilityEvent event);

    /**
     * 停止Job
     */
    void onStopJob();

    /**
     * 来通知了， 也是任务
     */
    void onNotificationPosted(IStatusBarNotification service);

    /**
     * 是否可用
     */
    boolean isEnable();
}
