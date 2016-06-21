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

package com.yuhaiyang.redpacket.ui.fragment.base;

import android.os.Handler;

import com.baidu.mobstat.StatService;
import com.bright.common.BaseFragment;
import com.bright.common.utils.http.okhttp.OkHttpUtils;

/**
 * 基础Fragment
 */
public class RedPacketFragment extends BaseFragment {
    protected Handler mHandler;

    @Override
    public void onResume() {
        super.onResume();
        // 百度统计
        StatService.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // 百度统计
        StatService.onPause(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        OkHttpUtils.getInstance().cancelTag(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }
}
