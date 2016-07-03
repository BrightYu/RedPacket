package com.yuhaiyang.redpacket.job;

import android.view.accessibility.AccessibilityEvent;

import com.yuhaiyang.redpacket.ui.service.RedPacketService;

public interface IAccessbilityJob {
    String getTargetPackageName();

    void onCreateJob(RedPacketService service);

    void onReceiveJob(AccessibilityEvent event);

    void onStopJob();

    void onNotificationPosted(IStatusBarNotification service);

    boolean isEnable();
}
