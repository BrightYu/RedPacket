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

package com.yuhaiyang.redpacket.job.accessbility;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.yuhaiyang.redpacket.BuildConfig;
import com.yuhaiyang.redpacket.job.notification.IStatusBarNotification;
import com.yuhaiyang.redpacket.manager.NotificationManager;
import com.yuhaiyang.redpacket.manager.WeChatManager;
import com.yuhaiyang.redpacket.modem.WeChat;
import com.yuhaiyang.redpacket.ui.service.RedPacketService;
import com.yuhaiyang.redpacket.util.AccessibilityHelper;
import com.yuhaiyang.redpacket.util.NotifyHelper;

import java.util.List;


public class WechatAccessbilityJob extends BaseAccessbilityJob {

    private static final String TAG = "WechatAccessbilityJob";
    /**
     * 红包消息的关键字
     */
    private static final String HONGBAO_TEXT_KEY = "[微信红包]";

    private static final String BUTTON_CLASS_NAME = "android.widget.Button";

    private static final int WINDOW_NONE = 0;
    private static final int WINDOW_LUCKYMONEY_RECEIVEUI = 1;
    private static final int WINDOW_LUCKYMONEY_DETAIL = 2;
    private static final int WINDOW_LAUNCHER = 3;
    private static final int WINDOW_OTHER = -1;

    private int mCurrentWindow = WINDOW_NONE;

    private boolean isReceivingHongbao;
    private Handler mHandler = null;

    private WeChatManager mWeChatManager;
    private NotificationManager mNotificationManager;


    @Override
    public void onCreateJob(RedPacketService service) {
        super.onCreateJob(service);
        mWeChatManager = WeChatManager.getInstance(mContext);
        mNotificationManager = NotificationManager.getInstance(mContext);
    }

    @Override
    public void onStopJob() {
        // TODO
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onNotificationPosted(IStatusBarNotification sbn) {
        Log.i(TAG, "onNotificationPosted: ");
        Notification nf = sbn.getNotification();
        String text = String.valueOf(sbn.getNotification().tickerText);
        notificationEvent(text, nf);
    }

    @Override
    public boolean isEnable() {
        return mWeChatManager.isEnabled();
    }

    @Override
    public String getTargetPackageName() {
        return WeChat.Configure.PACKAGE_NAME;
    }

    @Override
    public void onReceiveJob(AccessibilityEvent event) {
        final int eventType = event.getEventType();
        Log.i(TAG, "onReceiveJob: eventType = " + eventType);
        //通知栏事件
        if (eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            Parcelable data = event.getParcelableData();
            if (data == null || !(data instanceof Notification)) {
                return;
            }
            if (RedPacketService.isNotificationServiceRunning() && mNotificationManager.isEnabled()) { //开启快速模式，不处理
                return;
            }
            List<CharSequence> texts = event.getText();
            if (!texts.isEmpty()) {
                String text = String.valueOf(texts.get(0));
                notificationEvent(text, (Notification) data);
            }
        } else if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            openHongBao(event);
        } else if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            if (mCurrentWindow != WINDOW_LAUNCHER) { //不在聊天界面或聊天列表，不处理
                return;
            }
            if (isReceivingHongbao) {
                handleChatListHongBao();
            }
        }
    }

    /**
     * 是否为群聊天
     */
    private boolean isMemberChatUi(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return false;
        }
        String id = "com.tencent.mm:id/ces";
        int version = mWeChatManager.getWechatVersion();
        if (version <= WeChat.Configure.VERSION_CODE_6_3_8) {
            id = "com.tencent.mm:id/ew";
        } else if (version <= WeChat.Configure.VERSION_CODE_6_3_9) {
            id = "com.tencent.mm:id/cbo";
        }
        String title = null;
        AccessibilityNodeInfo target = AccessibilityHelper.findNodeInfosById(nodeInfo, id);
        if (target != null) {
            title = String.valueOf(target.getText());
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("返回");

        if (list != null && !list.isEmpty()) {
            AccessibilityNodeInfo parent = null;
            for (AccessibilityNodeInfo node : list) {
                if (!"android.widget.ImageView".equals(node.getClassName())) {
                    continue;
                }
                String desc = String.valueOf(node.getContentDescription());
                if (!"返回".equals(desc)) {
                    continue;
                }
                parent = node.getParent();
                break;
            }
            if (parent != null) {
                parent = parent.getParent();
            }
            if (parent != null) {
                if (parent.getChildCount() >= 2) {
                    AccessibilityNodeInfo node = parent.getChild(1);
                    if ("android.widget.TextView".equals(node.getClassName())) {
                        title = String.valueOf(node.getText());
                    }
                }
            }
        }


        if (title != null && title.endsWith(")")) {
            return true;
        }
        return false;
    }

    /**
     * 通知栏事件
     */
    private void notificationEvent(String ticker, Notification nf) {
        String text = ticker;
        int index = text.indexOf(":");
        if (index != -1) {
            text = text.substring(index + 1);
        }
        text = text.trim();
        if (text.contains(HONGBAO_TEXT_KEY)) { //红包消息
            newHongBaoNotification(nf);
        }
    }

    /**
     * 打开通知栏消息
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void newHongBaoNotification(Notification notification) {
        isReceivingHongbao = true;
        //以下是精华，将微信的通知栏消息打开
        PendingIntent pendingIntent = notification.contentIntent;
        boolean lock = NotifyHelper.isLockScreen(mContext);

        if (!lock) {
            NotifyHelper.send(pendingIntent);
        } else {
            NotifyHelper.showNotify(mContext, String.valueOf(notification.tickerText), pendingIntent);
        }

        if (lock || mWeChatManager.getGrapMode() != WeChat.Configure.GRAP_MODE_AUTO) {
            NotifyHelper.playEffect(mContext, mNotificationManager);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void openHongBao(AccessibilityEvent event) {
        if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI".equals(event.getClassName())) {
            mCurrentWindow = WINDOW_LUCKYMONEY_RECEIVEUI;
            //点中了红包，下一步就是去拆红包
            handleLuckyMoneyReceive();
        } else if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI".equals(event.getClassName())) {
            mCurrentWindow = WINDOW_LUCKYMONEY_DETAIL;
        } else if ("com.tencent.mm.ui.LauncherUI".equals(event.getClassName())) {
            mCurrentWindow = WINDOW_LAUNCHER;
            //在聊天界面,去点中红包
            handleChatListHongBao();
        } else {
            mCurrentWindow = WINDOW_OTHER;
        }
    }

    /**
     * 点击聊天里的红包后，显示的界面
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void handleLuckyMoneyReceive() {
        AccessibilityNodeInfo nodeInfo = getService().getRootInActiveWindow();
        if (nodeInfo == null) {
            Log.w(TAG, "rootWindow is null");
            return;
        }

        AccessibilityNodeInfo targetNode = null;
        int event = mWeChatManager.getGetHongBaoAfterEvent();
        Log.i(TAG, "handleLuckyMoneyReceive: evet = " + event);
        int wechatVersion = mWeChatManager.getWechatVersion();
        if (event == WeChat.Configure.GET_AFTER_HONGBAO_EVENT_OPEN) { //拆红包
            if (wechatVersion < WeChat.Configure.VERSION_CODE_6_3_9) {
                targetNode = AccessibilityHelper.findNodeInfosByText(nodeInfo, "拆红包");
            } else {
                String buttonId = "com.tencent.mm:id/b43";

                if (wechatVersion == WeChat.Configure.VERSION_CODE_6_3_9) {
                    buttonId = "com.tencent.mm:id/b2c";
                }
                targetNode = AccessibilityHelper.findNodeInfosById(nodeInfo, buttonId);

                if (targetNode == null) {
                    //分别对应固定金额的红包 拼手气红包
                    AccessibilityNodeInfo textNode = AccessibilityHelper.findNodeInfosByTexts(nodeInfo, "发了一个红包", "给你发了一个红包", "发了一个红包，金额随机");

                    if (textNode != null) {
                        for (int i = 0; i < textNode.getChildCount(); i++) {
                            AccessibilityNodeInfo node = textNode.getChild(i);
                            if (BUTTON_CLASS_NAME.equals(node.getClassName())) {
                                targetNode = node;
                                break;
                            }
                        }
                    }
                }

                if (targetNode == null) { //通过组件查找
                    targetNode = AccessibilityHelper.findNodeInfosByClassName(nodeInfo, BUTTON_CLASS_NAME);
                }
            }
        } else if (event == WeChat.Configure.GET_AFTER_HONGBAO_EVENT_SEE) { //看一看
            if (mWeChatManager.getWechatVersion() < WeChat.Configure.VERSION_CODE_6_3_9) { //低版本才有 看大家手气的功能
                targetNode = AccessibilityHelper.findNodeInfosByText(nodeInfo, "看看大家的手气");
            }
        } else if (event == WeChat.Configure.GET_AFTER_HONGBAO_EVENT_NONE) {
            return;
        }

        if (targetNode == null) {
            Log.i(TAG, "handleLuckyMoneyReceive: targetNode is null");
            return;
        }

        final AccessibilityNodeInfo n = targetNode;
        final long time = mWeChatManager.getOpenDelayTime();
        if (time != 0) {
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AccessibilityHelper.performClick(n);
                }
            }, time);
        } else {
            AccessibilityHelper.performClick(n);
        }

    }

    /**
     * 收到聊天里的红包
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void handleChatListHongBao() {

        int mode = mWeChatManager.getGrapMode();
        if (mode == WeChat.Configure.GRAP_MODE_MANUAL) { //只通知模式
            return;
        }

        AccessibilityNodeInfo nodeInfo = getService().getRootInActiveWindow();
        if (nodeInfo == null) {
            Log.w(TAG, "rootWindow为空");
            return;
        }

        if (mode != WeChat.Configure.GRAP_MODE_AUTO) {
            boolean isMember = isMemberChatUi(nodeInfo);
            if (mode == WeChat.Configure.GRAP_MODE_SINGLE_CHAT && isMember) {//过滤群聊
                return;
            } else if (mode == WeChat.Configure.GRAP_MODE_GROUP_CHAT && !isMember) { //过滤单聊
                return;
            }
        }

        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("领取红包");

        if (list != null && list.isEmpty()) {
            // 从消息列表查找红包
            AccessibilityNodeInfo node = AccessibilityHelper.findNodeInfosByText(nodeInfo, "[微信红包]");
            if (node != null) {
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "-->微信红包:" + node);
                }
                isReceivingHongbao = true;
                AccessibilityHelper.performClick(nodeInfo);
            }
        } else if (list != null) {
            if (isReceivingHongbao) {
                //最新的红包领起
                AccessibilityNodeInfo node = list.get(list.size() - 1);
                AccessibilityHelper.performClick(node);
                isReceivingHongbao = false;
            }
        }
    }

    private Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }
}
