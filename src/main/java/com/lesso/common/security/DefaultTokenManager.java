package com.lesso.common.security;

import com.lesso.common.util.StringUtil;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultTokenManager implements TokenManager {

    private static Map<String, String> tokenMap = new ConcurrentHashMap<>();

    public String createToken(String username) {
        String token = UUID.randomUUID().toString();
        tokenMap.put(token, username);
        return token;
    }

    public boolean checkToken(String username, String token) {
        return !StringUtil.isEmpty(token) && tokenMap.containsKey(token);
    }
}