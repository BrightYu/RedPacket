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

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.util.Log;
import android.view.View;

import com.bright.common.widget.TopBar;
import com.bright.common.widget.dialog.BaseDialog;
import com.yuhaiyang.redpacket.R;
import com.yuhaiyang.redpacket.constant.Config;
import com.yuhaiyang.redpacket.ui.activity.base.RedPacketActivity;
import com.yuhaiyang.redpacket.ui.widget.NormalPreference;

public class WechatSettingsActivity extends RedPacketActivity implements View.OnClickListener {
    private static final String TAG = "WechatSettingsActivity";
    private NormalPreference mGrapMode;
    private NormalPreference mDelayTime;
    private NormalPreference mOpenAfterAtion;

    private final static int REQUEST_DELAY_TIME = 1;
    private Config mConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_settigns);
        mConfig = Config.getConfig(this);
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] list = getResources().getStringArray(R.array.wechat_grap_mode_list);
        mGrapMode.setSubTitle(list[mConfig.getWechatMode()]);

        mDelayTime.setSubTitle(String.valueOf(mConfig.getWechatOpenDelayTime()));

        list = getResources().getStringArray(R.array.wechat_open_after_mode_list);
        mOpenAfterAtion.setSubTitle(list[mConfig.getWechatAfterGetHongBaoEvent()]);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.grab_mode:
                showGrapModeDialog();
                break;
            case R.id.grab_delay_time:
                Intent intent = new Intent(this, DelayTimeActivity.class);
                intent.putExtra(DelayTimeActivity.DELAY_TIME, mDelayTime.getSubText());
                startActivityForResult(intent, REQUEST_DELAY_TIME);
                break;
            case R.id.open_after_doing:
                showOpenAfterActionDialog();
                break;

        }
    }


    private void showGrapModeDialog() {
        BaseDialog.Builder builder = new BaseDialog.Builder(this, R.style.Dialog_SingleChoice);
        builder.setTitle(R.string.grab_mode);
        int mode = mConfig.getWechatMode();
        final String[] list = getResources().getStringArray(R.array.wechat_grap_mode_list);
        builder.setSingleChoiceItems(list, mode, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mConfig.setWechatMode(which);
                mGrapMode.setSubTitle(list[which]);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }


    private void showOpenAfterActionDialog() {
        int mode = mConfig.getWechatAfterGetHongBaoEvent();
        final String[] list = getResources().getStringArray(R.array.wechat_open_after_mode_list);
        final BaseDialog dialog = new BaseDialog.Builder(this, R.style.Dialog_SingleChoice)
                .setTitle(R.string.open_after_doing)
                .setSingleChoiceItems(list, mode, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mConfig.setWechatAfterGetHongBaoEvent(which);
                        mOpenAfterAtion.setSubTitle(list[which]);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
        dialog.show();
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
                String time = data.getStringExtra(DelayTimeActivity.DELAY_TIME);
                mDelayTime.setSubTitle(time);
                mConfig.setWechatOpenDelayTime(time);
                break;
        }

    }

}
