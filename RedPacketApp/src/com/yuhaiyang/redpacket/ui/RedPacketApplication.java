/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yuhaiyang.redpacket.ui;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class RedPacketApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void showShare(final Activity activity) {

    }

    /**
     * 显示分享
     */
    public static void showShare(final Activity activity, final String shareUrl) {
    }

    /**
     * 检查更新
     */
    public static void checkUpdate(Activity activity) {

    }

    /**
     * 首个activity启动调用
     */
    public static void activityStartMain(Activity activity) {

    }

    /**
     * 每个activity生命周期里的onCreate
     */
    public static void activityCreateStatistics(Activity activity) {

    }

    /**
     * 每个activity生命周期里的onResume
     */
    public static void activityResumeStatistics(Activity activity) {

    }

    /**
     * 每个activity生命周期里的onPause
     */
    public static void activityPauseStatistics(Activity activity) {

    }

    /**
     * 事件统计
     */
    public static void eventStatistics(Context context, String event) {

    }

    /**
     * 事件统计
     */
    public static void eventStatistics(Context context, String event, String tag) {

    }
}
