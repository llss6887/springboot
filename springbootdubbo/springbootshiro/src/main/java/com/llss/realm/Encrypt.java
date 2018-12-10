package com.llss.realm;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * 自定义密码加密
 */
public class Encrypt {
    public static String md5(String password, String salt){
        return new Md5Hash(password,salt,2).toString();
    }

    public static void main(String[] args) {
        String pass = md5("123456", "wangsu");
        System.out.printf(pass);
    }
}
