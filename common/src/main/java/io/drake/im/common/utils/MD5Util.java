package io.drake.im.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Date: 2021/04/19/21:21
 *
 * @author : Drake
 * Description:
 */
@Slf4j
public class MD5Util {

    public static String encrypt(String word){
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.error("get md5 instance error");
        }

        char[] chars = word.toCharArray();
        byte[] bytes = new byte[chars.length];

        for(int i = 0;i < chars.length;i++){
            bytes[i] = (byte) chars[i];
        }

        byte[] md5Bytes = md5.digest(bytes);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }

}
