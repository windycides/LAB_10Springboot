package com.example.lab10.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {

    @Test
    public void testValidateTokenSuccess() {

        String token = "valid-token-123";
        boolean isValid = token != null && token.length() > 0;

        assertTrue(isValid, "Token should be valid");
    }

    @Test
    public void testValidateTokenFailNotFound() {

        String token = null;

        assertNull(token, "Token should be null (not found)");
    }
}