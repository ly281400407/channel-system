package com.lesso.common.security;

public interface TokenManager {

    String createToken(String username);

    boolean checkToken(String username, String token);

}
