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
import android.view.View;

import com.bright.common.widget.TopBar;
import com.yuhaiyang.redpacket.R;
import com.yuhaiyang.redpacket.ui.activity.base.RedPacketActivity;
import com.yuhaiyang.redpacket.ui.widget.InputEdit;

/**
 * 延迟时间设置
 */
public class DelayTimeActivity extends RedPacketActivity {
    public static final String DELAY_TIME = "key_delay_time";
    private InputEdit mTimeInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delay_time);
        String time = getIntent().getStringExtra(DELAY_TIME);
        mTimeInput.setInputText(time);
        mTimeInput.focus();
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

        mTimeInput = (InputEdit) findViewById(R.id.time);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        Intent intent = new Intent();
        intent.putExtra(DELAY_TIME, mTimeInput.getInputText());
        setResult(RESULT_OK, intent);
        this.finish();
    }
}
