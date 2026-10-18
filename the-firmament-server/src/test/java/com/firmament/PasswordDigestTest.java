package com.firmament;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.util.Arrays;

//@SpringBootTest
public class PasswordDigestTest {

    @Test
    public void testMD5(){
        String plainText = "xla???";
        String cipherText = DigestUtils.md5DigestAsHex(plainText.getBytes());
        System.out.println(cipherText);
    }

}
