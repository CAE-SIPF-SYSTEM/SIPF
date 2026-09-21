package com.caeproject.cae;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashTest {
    @Test
    public void generateHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("====== HASH RESULT ======");
        System.out.println(encoder.encode("AdminCae@2026SIPF"));
        System.out.println("=========================");
    }
}
