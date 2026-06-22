package com.ruoyi.touki.utils;

import java.security.SecureRandom;

public class RandomUtil {
    private static final SecureRandom RANDOM = new SecureRandom();

    private static final String NUMBER = "0123456789";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 数字
     */
    public static String randomNumber(int length) {
        return random(length, NUMBER);
    }

    /**
     * 小写字母
     */
    public static String randomLower(int length) {
        return random(length, LOWER);
    }

    /**
     * 大写字母
     */
    public static String randomUpper(int length) {
        return random(length, UPPER);
    }

    /**
     * 大小写字母
     */
    public static String randomLetter(int length) {
        return random(length, LOWER + UPPER);
    }

    /**
     * 大写字母+数字
     */
    public static String randomUpperAndNumber(int length) {
        return random(length, UPPER + NUMBER);
    }

    /**
     * 小写字母+数字
     */
    public static String randomLowerAndNumber(int length) {
        return random(length, LOWER + NUMBER);
    }

    /**
     * 大小写字母+数字
     */
    public static String randomAll(int length) {
        return random(length, LOWER + UPPER + NUMBER);
    }

    private static String random(int length, String source) {

        if (length <= 0) {
            throw new IllegalArgumentException("length必须大于0");
        }

        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(source.charAt(
                    RANDOM.nextInt(source.length())
            ));
        }

        return sb.toString();
    }
}
