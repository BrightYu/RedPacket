package com.yuhaiyang.redpacket.job;

import android.view.accessibility.AccessibilityEvent;

import com.yuhaiyang.redpacket.IStatusBarNotification;
import com.yuhaiyang.redpacket.ui.service.QiangHongBaoService;

public interface IAccessbilityJob {
    String getTargetPackageName();

    void onCreateJob(QiangHongBaoService service);

    void onReceiveJob(AccessibilityEvent event);

    void onStopJob();

    void onNotificationPosted(IStatusBarNotification service);

    boolean isEnable();
}
