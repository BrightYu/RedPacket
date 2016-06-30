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

package com.yuhaiyang.redpacket.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuhaiyang.redpacket.R;

/**
 * 仿Preference Fragment中的Item
 */
public class NormalPreference extends LinearLayout {
    private TextView mTitleView;
    private TextView mSubTitleView;

    // Title字体大小
    private int mTitleSize;
    // SubTitle字体大小
    private int mSubTitleSize;

    // Title字体颜色
    private int mTitleColor;
    // SubTitle字体颜色
    private int mSubTitleColor;
    // Title字体
    private String mTitleString;
    // SubTitle字体颜色
    private String mSubTitleString;


    public NormalPreference(Context context) {
        this(context, null);
    }

    public NormalPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NormalPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NormalPreference);

        mTitleString = a.getString(R.styleable.NormalPreference_text);
        mSubTitleString = a.getString(R.styleable.NormalPreference_subText);

        mTitleSize = a.getDimensionPixelSize(R.styleable.NormalPreference_textSize, getDefaultTitleSize());
        mSubTitleSize = a.getDimensionPixelSize(R.styleable.NormalPreference_subTextSize, getDefaultSubTitleSize());

        mTitleColor = a.getColor(R.styleable.NormalPreference_textColor, getDefaultTitleColor());
        mSubTitleColor = a.getColor(R.styleable.NormalPreference_subTextColor, getDefaultSubTitleColor());

        final int minH = a.getDimensionPixelSize(R.styleable.NormalPreference_android_minHeight, getDefaultMinHeight());
        a.recycle();

        setMinimumHeight(minH);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        getTitles();
    }


    private void getTitles() {
        LinearLayout ll = new LinearLayout(getContext());
        ll.setGravity(Gravity.CENTER_VERTICAL);
        ll.setOrientation(VERTICAL);
        ll.addView(getTitle());
        ll.addView(getSubTitle());

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        lp.setMarginStart(getResources().getDimensionPixelSize(R.dimen.gap_grade_5));
        addView(ll, lp);
    }

    private View getTitle() {
        if (mTitleView == null) {
            mTitleView = new AppCompatTextView(getContext());
            mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSize);
            mTitleView.setTextColor(mTitleColor);
            mTitleView.setText(mTitleString);
        }
        return mTitleView;
    }

    private View getSubTitle() {
        if (mSubTitleView == null) {
            mSubTitleView = new AppCompatTextView(getContext());
            mSubTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSubTitleSize);
            mSubTitleView.setTextColor(mSubTitleColor);
            mSubTitleView.setText(mSubTitleString);
        }

        if (TextUtils.isEmpty(mSubTitleString)) {
            mSubTitleView.setVisibility(GONE);
        }

        return mSubTitleView;
    }

    public String getSubText(){
        return mSubTitleView.getText().toString();
    }

    public void setSubTitle(@StringRes int subTitle) {
        setSubTitle(getContext().getString(subTitle));
    }

    public void setSubTitle(String subTitle) {
        mSubTitleView.setText(subTitle);
        if (TextUtils.isEmpty(subTitle)) {
            mSubTitleView.setVisibility(GONE);
        } else {
            mSubTitleView.setVisibility(VISIBLE);
        }
    }

    private int getDefaultTitleColor() {
        return getContext().getResources().getColor(R.color.text_color_dark_normal);
    }

    private int getDefaultSubTitleColor() {
        return getContext().getResources().getColor(R.color.text_color_light_normal);
    }

    private int getDefaultTitleSize() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.G_title);
    }

    private int getDefaultSubTitleSize() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.I_title);
    }

    private int getDefaultMinHeight() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.dp_65);
    }
}
