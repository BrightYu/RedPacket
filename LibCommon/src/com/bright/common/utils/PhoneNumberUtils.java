/**
 * Copyright (C) 2015  Haiyang Yu Android Source Project
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
package com.bright.common.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberUtils {

    public static final String ISO_CHINA = "CN";
    public static final int OPERATORS_UNKNOWN = 0;
    /**
     * 中国移动
     */
    public static final int OPERATORS_CMCC = 1;
    /**
     * 中国联通
     */
    public static final int OPERATORS_CUCC = 2;
    /**
     * 中国电信
     */
    public static final int OPERATORS_CTCC = 3;
    private static final String TAG = PhoneNumberUtils.class.getSimpleName();
    /**
     * 中国移动号码段
     */
    private static final String CMCC_NUMBERS = "/^(134|135|136|137|138|139|150|151|152|157|158|159|182|183|184|187|188|178|147)[0-9]{8}$/";
    /**
     * 中国联通号码段
     */
    private static final String CUCC_NUMBERS = "/^(130|131|132|145|155|156|176|185|186)[0-9]{8}$/";
    /**
     * 中国电信号码段
     */
    private static final String CTCC_NUMBERS = "/^(133|153|189|177|180|181)[0-9]{8}$/";

    /**
     * 格式化电话号码 例如 18366668888 -> 183 6666 8888
     */
    public static String formatNumber(String number) {
        return formatNumber(number, ISO_CHINA);
    }

    public static String formatNumber(String phoneNumber, String defaultCountryIso) {
        // Do not attempt to format numbers that start with a hash or star symbol.
        if (phoneNumber.startsWith("#") || phoneNumber.startsWith("*")) {
            return phoneNumber;
        }
        PhoneNumberUtil util = PhoneNumberUtil.getInstance();
        String result = null;
        try {
            Phonenumber.PhoneNumber pn = util.parseAndKeepRawInput(phoneNumber, defaultCountryIso);
            result = util.formatInOriginalFormat(pn, defaultCountryIso);
        } catch (NumberParseException e) {

        }
        return result;
    }

    /**
     * 判断号码是否是有效的号码
     */
    public static boolean isValidNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 11) {
            return false;
        } else {
            return isValidNumber(phoneNumber, ISO_CHINA);
        }
    }

    /**
     * 判断号码是否是有效的号码
     */
    public static boolean isValidNumber(String phoneNumber, String defaultCountryIso) {
        PhoneNumberUtil util = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber pn = util.parseAndKeepRawInput(phoneNumber, defaultCountryIso);
            return util.isValidNumber(pn);
        } catch (NumberParseException e) {
            Log.i(TAG, "NumberParseException  = " + e);
        }
        return false;
    }


    /**
     * 判断号码的运营商
     */
    public static int getOperators(String number) {
        Pattern pattern = Pattern.compile(CMCC_NUMBERS);
        Matcher matcher = pattern.matcher(number);

        if (matcher.matches()) {
            return OPERATORS_CMCC;
        }
        pattern = Pattern.compile(CUCC_NUMBERS);
        matcher = pattern.matcher(number);
        if (matcher.matches()) {
            return OPERATORS_CUCC;
        }
        pattern = Pattern.compile(CTCC_NUMBERS);
        matcher = pattern.matcher(number);
        if (matcher.matches()) {
            return OPERATORS_CTCC;
        }
        return OPERATORS_UNKNOWN;
    }
}
