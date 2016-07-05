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

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;

import com.yuhaiyang.redpacket.ui.activity.base.RedPacketActivity;

/**
 * 加载页图片
 */
public class SplashActivity extends RedPacketActivity {
    private static final String TAG = "SplashActivity";
    /**
     * 进入下一个界面
     */
    private static final int GO_TO_NEXT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new RecycleHandler();
        mHandler.sendEmptyMessageDelayed(GO_TO_NEXT, 1500);
    }


    @Override
    public void onBackPressed() {
        // 屏蔽掉Back键
    }

    @Override
    protected void resetStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    // remove the following flag for version < API 19
                    | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }


    /**
     * 父类回收
     */
    private class RecycleHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GO_TO_NEXT:
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                    break;
            }

        }
    }
}
