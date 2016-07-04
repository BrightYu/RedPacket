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

package com.yuhaiyang.redpacket.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bright.common.adapter.ListAdapter;
import com.yuhaiyang.redpacket.R;
import com.yuhaiyang.redpacket.modem.interfaces.ISelect;

/**
 * 选择的Adapter
 */
public class SelectAdapter extends ListAdapter<ISelect, SelectAdapter.ViewHolder> {

    public SelectAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(int position, int type) {
        TextView item = (TextView) mLayoutInflater.inflate(R.layout.dialog_single_choice_item, null);
        return new ViewHolder(item, type);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, int type) {
        TextView item = (TextView) holder.getItemView();
        ISelect area = mData.get(position);
        item.setText(area.getDescription());
    }


    public class ViewHolder extends ListAdapter.Holder {

        public ViewHolder(View item, int type) {
            super(item, type);
        }
    }


}
