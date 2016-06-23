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

import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.view.View;

import com.bright.common.widget.TopBar;
import com.bright.common.widget.dialog.BaseDialog;
import com.yuhaiyang.redpacket.Config;
import com.yuhaiyang.redpacket.R;
import com.yuhaiyang.redpacket.ui.activity.base.RedPacketActivity;
import com.yuhaiyang.redpacket.ui.fragment.base.RedPacketPreferenceFragment;
import com.yuhaiyang.redpacket.ui.widget.NormalPreference;

public class WechatSettingsActivity extends RedPacketActivity implements View.OnClickListener {
    private NormalPreference mGrapMode;
    private NormalPreference mDelayTime;
    private NormalPreference mOpenAfterAtion;
    private NormalPreference mGrapAfterAtion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_settigns);
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

        mGrapAfterAtion = (NormalPreference) findViewById(R.id.grab_after_doing);
        mGrapAfterAtion.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.grab_mode:
                showGrapModeDialog();
                break;
            case R.id.open_after_doing:
                showOpenAfterActionDialog();
                break;
            case R.id.grab_after_doing:
                showGrapAfterActionDialog();
                break;
        }
    }


    private void showGrapModeDialog() {
        BaseDialog.Builder builder = new BaseDialog.Builder(this, R.style.Dialog_SingleChoice);
        builder.setTitle(R.string.grab_mode);
        String[] list = getResources().getStringArray(R.array.wechat_grap_mode_list);
        builder.setSingleChoiceItems(list, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }

    private void showOpenAfterActionDialog() {
        BaseDialog.Builder builder = new BaseDialog.Builder(this, R.style.Dialog_SingleChoice);
        builder.setTitle(R.string.open_after_doing);
        String[] list = getResources().getStringArray(R.array.wechat_open_after_mode_list);
        builder.setSingleChoiceItems(list, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }

    private void showGrapAfterActionDialog() {
        BaseDialog.Builder builder = new BaseDialog.Builder(this, R.style.Dialog_SingleChoice);
        builder.setTitle(R.string.grab_after_doing);
        String[] list = getResources().getStringArray(R.array.wechat_grab_after_mode_list);
        builder.setSingleChoiceItems(list, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }

    public static class WechatSettingsFragment extends RedPacketPreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.wechat_settings);

            //微信红包模式
            final ListPreference wxMode = (ListPreference) findPreference(Config.KEY_WECHAT_MODE);
            wxMode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    int value = Integer.parseInt(String.valueOf(newValue));
                    preference.setSummary(wxMode.getEntries()[value]);
                    return true;
                }
            });
            wxMode.setSummary(wxMode.getEntries()[Integer.parseInt(wxMode.getValue())]);

            //打开微信红包后
            final ListPreference wxAfterOpenPre = (ListPreference) findPreference(Config.KEY_WECHAT_AFTER_OPEN_HONGBAO);
            wxAfterOpenPre.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    int value = Integer.parseInt(String.valueOf(newValue));
                    preference.setSummary(wxAfterOpenPre.getEntries()[value]);
                    return true;
                }
            });
            wxAfterOpenPre.setSummary(wxAfterOpenPre.getEntries()[Integer.parseInt(wxAfterOpenPre.getValue())]);

            //获取微信红包后
            final ListPreference wxAfterGetPre = (ListPreference) findPreference(Config.KEY_WECHAT_AFTER_GET_HONGBAO);
            wxAfterGetPre.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    int value = Integer.parseInt(String.valueOf(newValue));
                    preference.setSummary(wxAfterGetPre.getEntries()[value]);
                    return true;
                }
            });
            wxAfterGetPre.setSummary(wxAfterGetPre.getEntries()[Integer.parseInt(wxAfterGetPre.getValue())]);

            final EditTextPreference delayEditTextPre = (EditTextPreference) findPreference(Config.KEY_WECHAT_DELAY_TIME);
            delayEditTextPre.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if ("0".equals(String.valueOf(newValue))) {
                        preference.setSummary("");
                    } else {
                        preference.setSummary("已延时" + newValue + "毫秒");
                    }
                    return true;
                }
            });
            String delay = delayEditTextPre.getText();
            if ("0".equals(String.valueOf(delay))) {
                delayEditTextPre.setSummary("");
            } else {
                delayEditTextPre.setSummary("已延时" + delay + "毫秒");
            }
        }
    }
}
