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

package com.yuhaiyang.redpacket.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.util.Log;
import android.view.View;

import com.bright.common.utils.Utils;
import com.bright.common.widget.TopBar;
import com.yuhaiyang.redpacket.R;
import com.yuhaiyang.redpacket.manager.WeChatManager;
import com.yuhaiyang.redpacket.modem.WeChat;
import com.yuhaiyang.redpacket.modem.WeChatSelect;
import com.yuhaiyang.redpacket.modem.interfaces.ISelect;
import com.yuhaiyang.redpacket.ui.activity.base.RedPacketActivity;
import com.yuhaiyang.redpacket.ui.dialog.SelectDialog;
import com.yuhaiyang.redpacket.ui.widget.NormalPreference;

import java.util.ArrayList;
import java.util.List;

public class WechatSettingsActivity extends RedPacketActivity implements View.OnClickListener {
    private static final String TAG = "WechatSettingsActivity";
    private final static int REQUEST_DELAY_TIME = 1;
    private NormalPreference mGrapMode;
    private NormalPreference mDelayTime;
    private NormalPreference mOpenAfterAtion;
    private WeChatManager mWeChatManager;

    private List<ISelect> mGrapModes;
    private List<ISelect> mGetHongBaoEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_settigns);
        mWeChatManager = WeChatManager.getInstance(this);
    }

    @Override
    protected void initViews() {
        super.initViews();
        DrawerArrowDrawable arrowDrawable = new DrawerArrowDrawable(this);
        arrowDrawable.setColor(Color.WHITE);
        arrowDrawable.setProgress(1.0f);
        TopBar topBar = (TopBar) findViewById(R.id.top_bar);
        topBar.setOnTopBarListener(this);
        topBar.setLeftImageDrawable(arrowDrawable);

        mGrapMode = (NormalPreference) findViewById(R.id.grab_mode);
        mGrapMode.setOnClickListener(this);

        mDelayTime = (NormalPreference) findViewById(R.id.grab_delay_time);
        mDelayTime.setOnClickListener(this);

        mOpenAfterAtion = (NormalPreference) findViewById(R.id.open_after_doing);
        mOpenAfterAtion.setOnClickListener(this);

        View info = findViewById(R.id.view_version_info);
        info.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGrapMode.setSubTitle(getSelectedGrapModeDescription());
        mDelayTime.setSubTitle(getString(R.string.link_delay_unit, mWeChatManager.getOpenDelayTime()));
        mOpenAfterAtion.setSubTitle(getGetHongbaoEventDescription());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.grab_mode:
                showGrapModeDialog();
                break;
            case R.id.grab_delay_time:
                Intent intent = new Intent(this, DelayTimeActivity.class);
                intent.putExtra(DelayTimeActivity.DELAY_TIME, mWeChatManager.getOpenDelayTime());
                startActivityForResult(intent, REQUEST_DELAY_TIME);
                break;
            case R.id.open_after_doing:
                showOpenAfterActionDialog();
                break;
            case R.id.view_version_info:
                dialog(String.valueOf(mWeChatManager.getWechatVersion()));
                break;
        }
    }


    private void showGrapModeDialog() {
        int mode = mWeChatManager.getGrapMode();
        SelectDialog dialog = new SelectDialog(this);
        dialog.setTitle(R.string.grab_mode);
        dialog.setData(getGrapMode(), mode);
        dialog.show();
        dialog.setCallBack(new SelectDialog.CallBack() {
            @Override
            public void select(ISelect select) {
                mWeChatManager.setGrapMode(select.getId());
                mGrapMode.setSubTitle(select.getDescription());
            }
        });
    }

    private List<ISelect> getGrapMode() {
        if (mGrapModes == null) {
            mGrapModes = new ArrayList<>();
            mGrapModes.add(new WeChatSelect(WeChat.Configure.GRAP_MODE_AUTO, getString(R.string.grap_mode_auto_description)));
            mGrapModes.add(new WeChatSelect(WeChat.Configure.GRAP_MODE_SINGLE_CHAT, getString(R.string.grap_mode_single_chat_description)));
            mGrapModes.add(new WeChatSelect(WeChat.Configure.GRAP_MODE_GROUP_CHAT, getString(R.string.grap_mode_group_chat_description)));
            mGrapModes.add(new WeChatSelect(WeChat.Configure.GRAP_MODE_MANUAL, getString(R.string.grap_mode_manual_description)));
        }
        return mGrapModes;
    }

    private String getSelectedGrapModeDescription() {
        getGrapMode();
        int mode = mWeChatManager.getGrapMode();
        for (ISelect select : mGrapModes) {
            if (select.getId() == mode) {
                return select.getDescription();
            }
        }
        return Utils.EMPTY;
    }


    private void showOpenAfterActionDialog() {
        int mode = mWeChatManager.getGetHongBaoAfterEvent();
        SelectDialog dialog = new SelectDialog(this);
        dialog.setTitle(R.string.get_after_doing);
        dialog.setData(getGetHongbaoAfterEvent(), mode);
        dialog.show();
        dialog.setCallBack(new SelectDialog.CallBack() {
            @Override
            public void select(ISelect select) {
                mWeChatManager.setGetHongBaoAfterEvent(select.getId());
                mOpenAfterAtion.setSubTitle(select.getDescription());
            }
        });
    }

    private List<ISelect> getGetHongbaoAfterEvent() {
        if (mGetHongBaoEvents == null) {
            mGetHongBaoEvents = new ArrayList<>();
            mGetHongBaoEvents.add(new WeChatSelect(WeChat.Configure.GET_AFTER_HONGBAO_EVENT_OPEN, getString(R.string.get_after_mode_open_description)));
            //mGetHongBaoEvents.add(new WeChatSelect(WeChat.Configure.GET_AFTER_HONGBAO_EVENT_SEE, getString(R.string.get_after_mode_see_description)));
            mGetHongBaoEvents.add(new WeChatSelect(WeChat.Configure.GET_AFTER_HONGBAO_EVENT_NONE, getString(R.string.get_after_mode_none_description)));
        }
        return mGetHongBaoEvents;
    }

    private String getGetHongbaoEventDescription() {
        getGetHongbaoAfterEvent();
        int mode = mWeChatManager.getGetHongBaoAfterEvent();
        for (ISelect select : mGetHongBaoEvents) {
            if (select.getId() == mode) {
                return select.getDescription();
            }
        }
        return Utils.EMPTY;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            Log.i(TAG, "onActivityResult: reuslt not ok");
            return;
        }

        switch (requestCode) {
            case REQUEST_DELAY_TIME:
                if (data == null) {
                    Log.i(TAG, "onActivityResult:  data is null");
                    return;
                }
                int time = data.getIntExtra(DelayTimeActivity.DELAY_TIME, WeChat.Configure.DEFAULT_DELAY_TIME);
                mDelayTime.setSubTitle(getString(R.string.link_delay_unit, time));
                mWeChatManager.setOpenDelayTime(time);
                break;
        }

    }

}
