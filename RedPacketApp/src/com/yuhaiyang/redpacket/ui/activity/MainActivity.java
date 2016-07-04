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

package com.yuhaiyang.redpacket.ui.activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.bright.common.widget.SwitchButton;
import com.bright.common.widget.TopBar;
import com.bright.common.widget.YToast;
import com.bright.common.widget.dialog.BaseDialog;
import com.yuhaiyang.redpacket.R;
import com.yuhaiyang.redpacket.constant.Config;
import com.yuhaiyang.redpacket.manager.WeChatManager;
import com.yuhaiyang.redpacket.ui.activity.base.RedPacketActivity;
import com.yuhaiyang.redpacket.ui.service.RedPacketService;

public class MainActivity extends RedPacketActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "MainActivity";
    private DrawerArrowDrawable mArrowDrawable;
    private long mLastTime;
    private boolean mNotificationChangeByUser;


    private Dialog mTipsDialog;
    private DrawerLayout mContent;
    private SwitchButton mMainServerControl;
    private SwitchButton mNotifyServerControl;

    private Config mConfig;
    private WeChatManager mWeChatManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver();
        mWeChatManager = WeChatManager.getInstance(this);
        mConfig = Config.getConfig(this);
    }


    @Override
    protected void initViews() {
        super.initViews();
        mArrowDrawable = new DrawerArrowDrawable(this);
        mArrowDrawable.setColor(Color.WHITE);

        TopBar topBar = (TopBar) findViewById(R.id.top_bar);
        topBar.setOnTopBarListener(this);
        topBar.setLeftImageDrawable(mArrowDrawable);

        mContent = (DrawerLayout) findViewById(R.id.content);
        mContent.addDrawerListener(mDrawerListener);

        mMainServerControl = (SwitchButton) findViewById(R.id.main_server_control);
        mMainServerControl.setOnCheckedChangeListener(this);

        mNotifyServerControl = (SwitchButton) findViewById(R.id.notify_server_control);
        mNotifyServerControl.setOnCheckedChangeListener(this);

        View notifySettings = findViewById(R.id.nofiy_settings);
        notifySettings.setOnClickListener(this);

        View wechatSettings = findViewById(R.id.wechat_setting);
        wechatSettings.setOnClickListener(this);

        View mentService = findViewById(R.id.menu_service);
        mentService.setOnClickListener(this);

        View mentNotify = findViewById(R.id.menu_notify);
        mentNotify.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();

        updateNotifyControl();

        if (RedPacketService.isRunning()) {
            if (mTipsDialog != null) {
                mTipsDialog.dismiss();
            }
        } else {
            showOpenAccessibilityServiceDialog();
        }

        // 是否同意免责
        boolean isAgreement = Config.getConfig(this).isAgreement();
        if (!isAgreement) {
            showAgreementDialog();
        }

        mMainServerControl.setChecked(mWeChatManager.isEnable());
        mNotifyServerControl.setChecked(mConfig.isEnableNotificationService());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(mConnectReceiver);
        } catch (Exception e) {
            Log.i(TAG, "onDestroy: e = " + e);
        }
        mTipsDialog = null;
    }

    @Override
    public void onBackPressed() {
        long nowtime = System.currentTimeMillis();
        if (nowtime - mLastTime < 2000) {
            super.onBackPressed();
        } else {
            toast(R.string.click_again_exit);
            mLastTime = nowtime;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_service:
                openAccessibilityServiceSettings();
                break;
            case R.id.menu_notify:
            case R.id.nofiy_settings:
                openNotificationServiceSettings();
                break;
            case R.id.wechat_setting:
                Intent intent = new Intent(this, WechatSettingsActivity.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onLeftClick(View v) {
        if (mContent.isDrawerOpen(GravityCompat.START)) {
            mContent.closeDrawer(GravityCompat.START);
        } else {
            mContent.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.main_server_control:
                if (isChecked && !RedPacketService.isRunning()) {
                    showOpenAccessibilityServiceDialog();
                }

                mWeChatManager.setEnalbe(isChecked);
                break;
            case R.id.notify_server_control:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    YToast.makeText(this, R.string.tip_notif_can_not_open, Toast.LENGTH_SHORT).show();
                    return;
                }
                updateNotifiyContrlSettings(isChecked);
                mConfig.setNotificationServiceEnable(isChecked);
                break;
        }
    }

    /**
     * 显示未开启辅助服务的对话框
     */
    private void showOpenAccessibilityServiceDialog() {
        if (mTipsDialog != null && mTipsDialog.isShowing()) {
            return;
        }
        View view = getLayoutInflater().inflate(R.layout.dialog_open_server, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAccessibilityServiceSettings();
            }
        });
        BaseDialog.Builder builder = new BaseDialog.Builder(this);
        builder.setView(view);
        builder.setPositiveButton(R.string.open_service_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openAccessibilityServiceSettings();
            }
        });
        mTipsDialog = builder.show();
    }

    /**
     * 打开辅助服务的设置
     */
    private void openAccessibilityServiceSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, R.string.tips, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开通知栏设置
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private void openNotificationServiceSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, R.string.tips, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateNotifyControl() {
        boolean running = RedPacketService.isNotificationServiceRunning();
        boolean enable = Config.getConfig(this).isEnableNotificationService();
        if (enable && running && !mNotifyServerControl.isChecked()) {
            mNotifyServerControl.setChecked(true);
        } else if ((!enable || !running) && mNotifyServerControl.isChecked()) {
            mNotifyServerControl.setChecked(false);
        }
    }

    private void updateNotifiyContrlSettings(boolean isChecked) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Toast.makeText(this, R.string.unsport_notifi, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!mNotificationChangeByUser) {
            mNotificationChangeByUser = true;
            return;
        }

        Config.getConfig(this).setNotificationServiceEnable(isChecked);

        if (isChecked && !RedPacketService.isNotificationServiceRunning()) {
            openNotificationServiceSettings();
        }
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT);
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);
        filter.addAction(Config.ACTION_NOTIFY_LISTENER_SERVICE_DISCONNECT);
        filter.addAction(Config.ACTION_NOTIFY_LISTENER_SERVICE_CONNECT);
        registerReceiver(mConnectReceiver, filter);
    }

    private DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            mArrowDrawable.setProgress(slideOffset);
        }

        @Override
        public void onDrawerOpened(View drawerView) {

        }

        @Override
        public void onDrawerClosed(View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };


    private BroadcastReceiver mConnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isFinishing()) {
                Log.i(TAG, "onReceive: finishing just return");
                return;
            }
            String action = intent.getAction();
            Log.d(TAG, "onReceive: action = " + action);

            if (Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT.equals(action)) {
                if (mTipsDialog != null) {
                    mTipsDialog.dismiss();
                }
            } else if (Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT.equals(action)) {
                showOpenAccessibilityServiceDialog();
            } else if (Config.ACTION_NOTIFY_LISTENER_SERVICE_CONNECT.equals(action)) {
                mNotificationChangeByUser = false;
                updateNotifyControl();
            } else if (Config.ACTION_NOTIFY_LISTENER_SERVICE_DISCONNECT.equals(action)) {
                mNotificationChangeByUser = false;
                updateNotifyControl();
            }
        }
    };


}
