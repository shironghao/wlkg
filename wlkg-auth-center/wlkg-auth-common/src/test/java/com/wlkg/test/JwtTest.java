package com.wlkg.test;

import com.wlkg.auth.pojo.UserInfo;
import com.wlkg.auth.utils.JwtUtils;
import com.wlkg.auth.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {
    private static final String pubKeyPath = "D:\\tmp\\rsa.pub";

    private static final String priKeyPath = "D:\\tmp\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU3NzMzMjQ2M30.aK8KTGb0VamqA7d_CKQTtiYWnoqapuNeyChsW3y4hFqrjK-vHJ8qswWdJWmwwh7hUmr-EE8VKTw7g3NHfUtwKyXJipMWKBOriw4ketrHssuE8KKkPxk3R_uzG0W4DTslH3jPTvZrFYgtTNnVkM9cpsHCBSEBNTALKeDJm-pKaG8";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}
