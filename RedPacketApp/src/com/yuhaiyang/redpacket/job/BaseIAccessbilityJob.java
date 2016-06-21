package com.yuhaiyang.redpacket.job;

import android.content.Context;

import com.yuhaiyang.redpacket.Config;
import com.yuhaiyang.redpacket.ui.service.QiangHongBaoService;

public abstract class BaseIAccessbilityJob implements IAccessbilityJob {

    private QiangHongBaoService service;

    @Override
    public void onCreateJob(QiangHongBaoService service) {
        this.service = service;
    }

    public Context getContext() {
        return service.getApplicationContext();
    }

    public Config getConfig() {
        return service.getConfig();
    }

    public QiangHongBaoService getService() {
        return service;
    }
}
