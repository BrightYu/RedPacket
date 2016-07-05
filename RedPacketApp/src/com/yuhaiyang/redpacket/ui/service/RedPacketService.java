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

package com.yuhaiyang.redpacket.ui.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import com.bright.common.widget.YToast;
import com.yuhaiyang.redpacket.R;
import com.yuhaiyang.redpacket.constant.Config;
import com.yuhaiyang.redpacket.job.accessbility.IAccessbilityJob;
import com.yuhaiyang.redpacket.job.accessbility.WechatAccessbilityJob;
import com.yuhaiyang.redpacket.job.notification.IStatusBarNotification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 抢红包服务
 */
public class RedPacketService extends AccessibilityService {

    private static final String TAG = "RedPacketService";

    private static final Class[] ACCESSBILITY_JOBS = {
            WechatAccessbilityJob.class,
    };

    private static RedPacketService sInstance;

    private List<IAccessbilityJob> mAccessbilityJobs;
    private HashMap<String, IAccessbilityJob> mPkgAccessbilityJobMap;

    /**
     * 接收通知栏事件
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void handeNotificationPosted(IStatusBarNotification notificationService) {
        Log.i(TAG, "handeNotificationPosted");
        if (notificationService == null) {
            return;
        }
        if (sInstance == null || sInstance.mPkgAccessbilityJobMap == null) {
            return;
        }
        String pack = notificationService.getPackageName();
        IAccessbilityJob job = sInstance.mPkgAccessbilityJobMap.get(pack);
        if (job == null) {
            return;
        }
        job.onNotificationPosted(notificationService);
    }

    /**
     * 判断当前服务是否正在运行
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean isRunning() {
        if (sInstance == null) {
            return false;
        }
        AccessibilityManager accessibilityManager = (AccessibilityManager) sInstance.getSystemService(Context.ACCESSIBILITY_SERVICE);
        AccessibilityServiceInfo info = sInstance.getServiceInfo();
        if (info == null) {
            return false;
        }
        List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        Iterator<AccessibilityServiceInfo> iterator = list.iterator();

        boolean isConnect = false;
        while (iterator.hasNext()) {
            AccessibilityServiceInfo i = iterator.next();
            if (i.getId().equals(info.getId())) {
                isConnect = true;
                break;
            }
        }

        return isConnect;
    }

    /**
     * 快速读取通知栏服务是否启动
     */
    public static boolean isNotificationServiceRunning() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return false;
        }
        //部份手机没有NotificationService服务
        try {
            return NotificationService.isRunning();
        } catch (Throwable t) {
            Log.i(TAG, "isNotificationServiceRunning: t = " + t);
        }
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mAccessbilityJobs = new ArrayList<>();
        mPkgAccessbilityJobMap = new HashMap<>();

        //初始化辅助插件工作
        for (Class clazz : ACCESSBILITY_JOBS) {
            try {
                Object object = clazz.newInstance();
                if (object instanceof IAccessbilityJob) {
                    IAccessbilityJob job = (IAccessbilityJob) object;
                    job.onCreateJob(this);
                    mAccessbilityJobs.add(job);
                    mPkgAccessbilityJobMap.put(job.getTargetPackageName(), job);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (mPkgAccessbilityJobMap != null) {
            mPkgAccessbilityJobMap.clear();
        }
        if (mAccessbilityJobs != null && !mAccessbilityJobs.isEmpty()) {
            for (IAccessbilityJob job : mAccessbilityJobs) {
                job.onStopJob();
            }
            mAccessbilityJobs.clear();
        }

        sInstance = null;
        mAccessbilityJobs = null;
        mPkgAccessbilityJobMap = null;
        //发送广播，已经断开辅助服务
        Intent intent = new Intent(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);
        sendBroadcast(intent);
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt");
        YToast.makeText(this, R.string.interrupt_grap_service, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "onServiceConnected");
        sInstance = this;
        //发送广播，已经连接上了 通知UI更新界面
        Intent intent = new Intent(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT);
        sendBroadcast(intent);
        YToast.makeText(this, R.string.connected_grap_service, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "onAccessibilityEvent -->" + event);
        String eventPackage = String.valueOf(event.getPackageName());
        if (mAccessbilityJobs != null) {
            for (IAccessbilityJob job : mAccessbilityJobs) {
                if (TextUtils.equals(eventPackage, job.getTargetPackageName()) && job.isEnable()) {
                    job.onReceiveJob(event);
                }
            }
        }
    }


}
