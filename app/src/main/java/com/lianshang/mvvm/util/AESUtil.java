package com.lianshang.mvvm.util;


import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

//import com.yeepay.g3.utils.common.CheckUtils;

/**
 * AES加密器<br>
 * 
 * 默认128位加密
 * 
 * @author haojie.yuan
 * 
 */
public class AESUtil {
	

    public static final String CHAR_ENCODING = "UTF-8";

    public static final String AES_ALGORITHM = "AES";

    /**
     * 加密
     * 
     * @param data
     *            待加密内容
     * @param key
     *            加密秘钥
     * @return 十六进制字符串
     */
    public static String encrypt(String data, String key) {
//        CheckUtils.notEmpty(data, "data");
//        CheckUtils.notEmpty(key, "key");
        if (key.length() < 16) {
            throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
        } else if (key.length() > 16) {
            key = key.substring(0, 16);
        }
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);// 创建密码器
            byte[] byteContent = data.getBytes(CHAR_ENCODING);
            cipher.init(Cipher.ENCRYPT_MODE, genKey(key));// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return parseByte2HexStr(result); // 加密
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 解密
     * 
     * @param data
     *            待解密内容(十六进制字符串)
     * @param key
     *            加密秘钥
     * @return
     */
    public static String decrypt(String data, String key) {
        if (key.length() < 16) {
        	 return null;
        } 
        if (key.length() > 16) {
            key = key.substring(0, 16);
        }
        try {
            byte[] decryptFrom = parseHexStr2Byte(data);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, genKey(key));// 初始化
            byte[] result = cipher.doFinal(decryptFrom);
            return new String(result, "utf-8"); // 加密
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // /**
    // * 创建加密解密密钥
    // *
    // * @param key
    // * 加密解密密钥
    // * @return
    // */
    // private static SecretKeySpec genKey(String key) {
    // byte[] enCodeFormat = { 0 };
    // try {
    // KeyGenerator kgen = KeyGenerator.getInstance("AES");
    // SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
    // secureRandom.setSeed(key.getBytes());
    // kgen.init(128, secureRandom);// 默认128位，支持256位
    // SecretKey secretKey = kgen.generateKey();
    // enCodeFormat = secretKey.getEncoded();
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    //
    // return new SecretKeySpec(enCodeFormat, "AES");
    // }

    /**
     * 创建加密解密密钥
     * 
     * @param key
     *            加密解密密钥
     * @return
     */
    private static SecretKeySpec genKey(String key) {
        SecretKeySpec secretKey;
        try {
            secretKey = new SecretKeySpec(key.getBytes(CHAR_ENCODING), AES_ALGORITHM);
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, AES_ALGORITHM);
            return seckey;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("genKey fail!", e);
        }
    }

    /**
     * 将二进制转换成16进制
     * 
     * @param buf
     * @return
     */
    private static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     * 
     * @param hexStr
     * @return
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
    
    public static void main(String[] args) {
    	String s = "2BB36F7E6DC8A82DB4E1B52C5B1040A0183B7CA60F03FFCE44EAAC08725A322DA6D0CF92D957A5354172A704A00E4BCC770";
    	System.out.println(decrypt(s, "28308fd9f2a7baf3"));
	}
    

}