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

package com.yuhaiyang.redpacket.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.bright.common.utils.Utils;
import com.bright.common.widget.TopBar;
import com.bright.common.widget.dialog.RecycleListView;
import com.yuhaiyang.redpacket.R;
import com.yuhaiyang.redpacket.modem.interfaces.ISelect;
import com.yuhaiyang.redpacket.ui.adapter.SelectAdapter;

import java.util.List;


/**
 * 选择Dialog
 */
public class SelectDialog extends Dialog {
    private static final String TAG = "SelectDialog";
    private ListView mListView;
    private SelectAdapter mAdapter;
    private CallBack mCallBack;
    private String mTitleString;
    private int mSelectItem = -1;
    private TopBar.OnTopBarListener mTopBarListener = new TopBar.OnTopBarListener() {
        @Override
        public void onLeftClick(View v) {
            dismiss();
        }

        @Override
        public void onRightClick(View v) {
            if (mCallBack != null) {
                int position = mListView.getCheckedItemPosition();
                ISelect select = mAdapter.getItem(position);
                mCallBack.select(select);
            }
            dismiss();
        }

        @Override
        public void onTitleClick(View v) {

        }
    };

    public SelectDialog(Context context) {
        super(context, R.style.Dialog_Bottom);
        mAdapter = new SelectAdapter(getContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bottom_select);
        TopBar topBar = (TopBar) findViewById(R.id.top_bar);
        topBar.setOnTopBarListener(mTopBarListener);
        topBar.setText(mTitleString);


        mListView = (RecycleListView) findViewById(R.id.list);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setAdapter(mAdapter);

        if (mSelectItem > -1) {
            mListView.setItemChecked(mSelectItem, true);
            mListView.setSelection(mSelectItem);
            mListView.getCheckedItemPosition();
        }
    }

    public void setTitle(@StringRes int resId) {
        setTitle(getContext().getString(resId));
    }

    public void setTitle(String title) {
        mTitleString = title;
    }

    public void setData(List<ISelect> datas) {
        setData(datas, -1);
    }

    public void setData(List<ISelect> datas, int selectId) {
        if (datas == null) {
            return;
        }

        mAdapter.setData(datas);
        for (int i = 0; i < datas.size(); i++) {
            ISelect data = datas.get(i);
            if (data.getId() == selectId) {
                mSelectItem = i;
            }
        }
        Log.i(TAG, "setData: selectId = " + selectId);
        Log.i(TAG, "setData: mSelectItem = " + mSelectItem);
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = Utils.getScreenSize(getContext())[0];
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
    }

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    public interface CallBack {
        void select(ISelect select);
    }
}
