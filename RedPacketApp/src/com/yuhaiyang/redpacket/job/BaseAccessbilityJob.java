package com.yuhaiyang.redpacket.job;

import android.content.Context;

import com.yuhaiyang.redpacket.Config;
import com.yuhaiyang.redpacket.ui.service.RedPacketService;

public abstract class BaseAccessbilityJob implements IAccessbilityJob {

    private RedPacketService service;

    @Override
    public void onCreateJob(RedPacketService service) {
        this.service = service;
    }

    public Context getContext() {
        return service.getApplicationContext();
    }

    public Config getConfig() {
        return service.getConfig();
    }

    public RedPacketService getService() {
        return service;
    }
}
