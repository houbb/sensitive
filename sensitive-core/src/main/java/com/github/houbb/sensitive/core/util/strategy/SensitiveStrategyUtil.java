package com.github.houbb.sensitive.core.util.strategy;

import com.github.houbb.heaven.constant.PunctuationConst;
import com.github.houbb.heaven.util.lang.StringUtil;

/**
 * 脱敏策略工具类
 * （1）提供常见的脱敏策略
 * （2）主要供单独的字符串处理使用
 * @author binbin.hou
 * @since 0.0.6
 */
public final class SensitiveStrategyUtil {

    private SensitiveStrategyUtil(){}

    /**
     * 脱敏密码
     * @param password 原始密码
     * @return 结果
     */
    public static String password(final String password) {
        return null;
    }

    /**
     * 脱敏电话号码
     * @param phone 电话号码
     * @return 结果
     */
    public static String phone(final String phone) {
        if(StringUtil.isEmpty(phone)) {
            return phone;
        }

        final int prefixLength = 3;
        final String middle = "****";
        return StringUtil.buildString(phone, middle, prefixLength);
    }

    /**
     * 脱敏邮箱
     * @param email 邮箱
     * @return 结果
     */
    public static String email(final String email) {
        if(StringUtil.isEmpty(email)) {
            return null;
        }

        final int prefixLength = 3;

        final int atIndex = email.indexOf(PunctuationConst.AT);
        String middle = "****";

        if(atIndex > 0) {
            int middleLength = atIndex - prefixLength;
            middle = StringUtil.repeat(PunctuationConst.STAR, middleLength);
        }
        return StringUtil.buildString(email, middle, prefixLength);
    }

    /**
     * 脱敏中文名称
     * @param chineseName 中文名称
     * @return 脱敏后的结果
     */
    public static String chineseName(final String chineseName) {
        if(StringUtil.isEmpty(chineseName)) {
            return chineseName;
        }

        final int nameLength = chineseName.length();
        if(1 == nameLength) {
            return chineseName;
        }

        if(2 == nameLength) {
            return PunctuationConst.STAR + chineseName.charAt(1);
        }

        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(chineseName.charAt(0));
        for(int i = 0; i < nameLength-2; i++) {
            stringBuffer.append(PunctuationConst.STAR);
        }
        stringBuffer.append(chineseName.charAt(nameLength -1));
        return stringBuffer.toString();
    }

    /**
     * 脱敏卡号
     * @param cardId 卡号
     * @return 脱敏结果
     */
    public static String cardId(final String cardId) {
        if(StringUtil.isEmpty(cardId)) {
            return cardId;
        }

        final int prefixLength = 6;
        final String middle = "**********";
        return StringUtil.buildString(cardId, middle, prefixLength);
    }

    /**
     * 脱敏身份证（兼容15位和18位）
     * @param idNo 身份证
     * @return 脱敏结果
     * @since 0.0.15
     */
    public static String idNo(final String idNo) {
        if(StringUtil.isEmpty(idNo)) {
            return idNo;
        }

        final int prefixLength = 3;
        String middle = "*************";
        //兼容 15 位身份证
        if(idNo.length() == 15) {
            middle = "**********";
        }
        return StringUtil.buildString(idNo, middle, prefixLength);
    }

}
