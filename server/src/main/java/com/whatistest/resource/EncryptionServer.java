package com.whatistest.resource;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by rubyvirusqq@gmail.com on 2017-1-24.
 */
public class EncryptionServer {
    private static final String HMAC_SHA1 = "HmacSHA1";

    //排序
    public String sort(List<String> entryList, Map<String, String> map) {
        Collections.sort(entryList);
        String str = concatParameters(entryList, map);
        return str;
        //return paramEncode(str);
    }

    //拼装字符串
    private String concatParameters(List<String> entryList, Map<String, String> map) {
        StringBuffer sb = new StringBuffer();
        String secret = "";
        for (String str : entryList) {
            //secret不进行字符串拼装
            if ("secret".equals(str)) {
                secret = map.get(str);
            } else {
                sb.append(str + "=" + paramEncode(map.get(str)) + "&");
            }
        }
        String parameters = sb.toString();
        //secret作为签名秘钥使用
        return getSignature(parameters, secret);
    }

    public String paramEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    //生成签名数据
    private static String getSignature(String data, String key) {
        try {
            byte[] keyBytes = key.getBytes("UTF-8");
            byte[] dataBytes = data.getBytes("UTF-8");

            return generateSignature(dataBytes, keyBytes);
        } catch (Exception e) {
            return null;
        }
    }

    // 对 data 以key 进行hash算法【MAC-SHA1】
    private static String generateSignature(byte[] data, byte[] key) throws Exception {
        SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1);
        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data);
        return getBase64Signature(rawHmac);
    }

    // 将原始二进制数据数据，进行base64编码
    private static String getBase64Signature(byte[] srcSignature) {
        byte[] base64Text = Base64.getEncoder().encode(srcSignature);
        return new String(base64Text);
    }

}
