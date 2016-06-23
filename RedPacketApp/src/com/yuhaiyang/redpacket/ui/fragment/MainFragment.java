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

package com.yuhaiyang.redpacket.ui.fragment;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.yuhaiyang.redpacket.Config;
import com.yuhaiyang.redpacket.R;
import com.yuhaiyang.redpacket.ui.activity.NotifySettingsActivity;
import com.yuhaiyang.redpacket.ui.activity.WechatSettingsActivity;
import com.yuhaiyang.redpacket.ui.fragment.base.RedPacketPreferenceFragment;
import com.yuhaiyang.redpacket.ui.service.QiangHongBaoService;

public class MainFragment extends RedPacketPreferenceFragment {

    private SwitchPreference mNotificationPreference;
    private boolean mNotificationChangeByUser = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.fragment_main);

        //微信红包开关
        Preference wechatPref = findPreference(Config.KEY_ENABLE_WECHAT);
        wechatPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((Boolean) newValue && !QiangHongBaoService.isRunning()) {
                    showOpenAccessibilityServiceDialog();
                }
                return true;
            }
        });

        mNotificationPreference = (SwitchPreference) findPreference("KEY_NOTIFICATION_SERVICE_TEMP_ENABLE");
        mNotificationPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    Toast.makeText(getActivity(), "该功能只支持安卓4.3以上的系统", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (!mNotificationChangeByUser) {
                    mNotificationChangeByUser = true;
                    return true;
                }

                boolean enalbe = (boolean) newValue;

                Config.getConfig(getActivity()).setNotificationServiceEnable(enalbe);

                if (enalbe && !QiangHongBaoService.isNotificationServiceRunning()) {
                    openNotificationServiceSettings();
                    return false;
                }
                return true;
            }
        });

        findPreference("WECHAT_SETTINGS").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), WechatSettingsActivity.class));
                return true;
            }
        });

        findPreference("NOTIFY_SETTINGS").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), NotifySettingsActivity.class));
                return true;
            }
        });

    }

    /**
     * 更新快速读取通知的设置
     */
    public void updateNotifyPreference() {
        if (mNotificationPreference == null) {
            return;
        }
        boolean running = QiangHongBaoService.isNotificationServiceRunning();
        boolean enable = Config.getConfig(getActivity()).isEnableNotificationService();
        if (enable && running && !mNotificationPreference.isChecked()) {
            mNotificationChangeByUser = false;
            mNotificationPreference.setChecked(true);
        } else if ((!enable || !running) && mNotificationPreference.isChecked()) {
            mNotificationChangeByUser = false;
            mNotificationPreference.setChecked(false);
        }
    }


    private void showOpenAccessibilityServiceDialog() {

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_open_server, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAccessibilityServiceSettings();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setPositiveButton(R.string.open_service_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openAccessibilityServiceSettings();
            }
        });
        builder.create().show();
    }

    /**
     * 打开辅助服务的设置
     */
    private void openAccessibilityServiceSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Toast.makeText(getActivity(), R.string.tips, Toast.LENGTH_LONG).show();
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
            Toast.makeText(getActivity(), R.string.tips, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        updateNotifyPreference();
    }
}
