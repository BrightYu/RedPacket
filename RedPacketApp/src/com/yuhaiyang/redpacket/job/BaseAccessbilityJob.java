package com.yuhaiyang.redpacket.job;

import android.content.Context;

import com.yuhaiyang.redpacket.constant.Config;
import com.yuhaiyang.redpacket.ui.service.RedPacketService;

public abstract class BaseAccessbilityJob implements IAccessbilityJob {

    private RedPacketService mRedPacketService;
    protected Context mContext;

    @Override
    public void onCreateJob(RedPacketService service) {
        mRedPacketService = service;
        mContext = service.getApplicationContext();
    }


    public Config getConfig() {
        return mRedPacketService.getConfig();
    }

    public RedPacketService getService() {
        return mRedPacketService;
    }
}
