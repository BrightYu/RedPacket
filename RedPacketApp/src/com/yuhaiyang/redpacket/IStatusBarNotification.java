package com.yuhaiyang.redpacket;

import android.app.Notification;

public interface IStatusBarNotification {

    String getPackageName();

    Notification getNotification();
}
