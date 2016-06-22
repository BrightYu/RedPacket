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

import android.app.Fragment;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;

import com.yuhaiyang.redpacket.Config;
import com.yuhaiyang.redpacket.R;
import com.yuhaiyang.redpacket.ui.fragment.base.RedPacketPreferenceFragment;

public class WechatSettingsActivity extends BaseSettingsActivity {

    @Override
    public Fragment getSettingsFragment() {
        return new WechatSettingsFragment();
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
