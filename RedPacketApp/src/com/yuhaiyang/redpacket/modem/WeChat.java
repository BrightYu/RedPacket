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

package com.yuhaiyang.redpacket.modem;

/**
 * 微信的模型
 */
public class WeChat {


    public static final class Key {
        /**
         * 是否开启了微信抢红包
         */
        public static final String ENABLE = "key_enable_wechat";
        /**
         * 抢红包模式
         */
        public static final String GRAP_MODE = "key_wechat_grap_mode";
        /**
         * 延长多少时间进行抢红包
         */
        public static final String OPEN_DELAY_TIME = "key_wechat_open_delay_time";
        /**
         * 点开红包后干什么
         */
        public static final String GET_HONGBAO_AFTER_EVENT = "key_wechat_after_get_hongbao_event";
    }

    public static final class Configure {
        /**
         * 6.3.8版本的 version_code
         * 这个版本之前的  的聊天界面的id不一样
         */
        public static final int VERSION_CODE_6_3_8 = 680;
        /**
         * 6.3.9版本的 version_code
         * 大于 700 版本的微信 是没有看他人手气功能的
         */
        public static final int VERSION_CODE_6_3_9 = 700;

        /**
         * 微信的包名
         */
        public static final String PACKAGE_NAME = "com.tencent.mm";

        /**********************抢红包模式(start)*************************/
        /**
         * 自动抢
         */
        public static final int GRAP_MODE_AUTO = 1;
        /**
         * 抢单聊的
         */
        public static final int GRAP_MODE_SINGLE_CHAT = 2;
        /**
         * 抢群聊的
         */
        public static final int GRAP_MODE_GROUP_CHAT = 3;
        /**
         * 通知手动抢
         */
        public static final int GRAP_MODE_MANUAL = 4;
        /**********************抢红包模式(end)*************************/

        /**********************抢到红包要干啥(start)*******************/
        /**
         * 拆红包
         */
        public static final int GET_AFTER_HONGBAO_EVENT_OPEN = 1;
        /**
         * 看大家手气
         */
        public static final int GET_AFTER_HONGBAO_EVENT_SEE = 2;
        /**
         * 什么也不干
         */
        public static final int GET_AFTER_HONGBAO_EVENT_NONE = 3;
        /**********************抢到红包要干啥(end)*************************/

        /**********************抢红包延迟时间(start)*******************/
        /**
         * 默认延迟时间
         */
        public static final int DEFAULT_DELAY_TIME = 200;
        /**********************抢红包延迟时间(end)*************************/
    }
}
