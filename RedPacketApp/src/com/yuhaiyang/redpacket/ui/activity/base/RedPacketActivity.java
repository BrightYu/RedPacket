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

package com.yuhaiyang.redpacket.ui.activity.base;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.mobstat.StatService;
import com.bright.common.BaseActivity;
import com.bright.common.utils.PackagesUtils;
import com.bright.common.utils.http.okhttp.OkHttpUtils;


public class RedPacketActivity extends BaseActivity {
    private static final String TAG = "RedPacketActivity";
    protected Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetStatusBar();
    }

    protected void resetStatusBar() {
        // TODO
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 百度统计
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 百度统计
        StatService.onPause(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消请求接口
        OkHttpUtils.getInstance().cancelTag(this);
        // 清除Handler预防内存泄露
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }


    /**
     * 是否需要显示升级dialog
     */
    protected boolean needShowUpdateVersionDialog() {
        return true;
    }


    /**
     * 是不是第一次进入这个版本
     */
    protected boolean isFirstEnterThisVerison() {
        // 获取之前保存的版本信息
        final int version = getSharedPreferences().getInt(PackagesUtils.VERSION_CODE, 0);
        final String versionName = getSharedPreferences().getString(PackagesUtils.VERSION_NAME, null);
        // 获取当前版本号
        final int _version = PackagesUtils.getVersion(this);
        final String _versionName = PackagesUtils.getVersionName(this);
        Log.d(TAG, "originVersion = " + version + " ,localVersion = " + _version);

        // 保存现在的版本号
        saveInt(PackagesUtils.VERSION_CODE, _version);
        saveString(PackagesUtils.VERSION_NAME, _versionName);

        // 如果当前版本比保存的版本大，说明APP更新了
        // 版本名称不相等且版本code比上一个版本大 才进行走ViewPager
        return (!TextUtils.equals(versionName, _versionName) && _version > version);
    }
}
