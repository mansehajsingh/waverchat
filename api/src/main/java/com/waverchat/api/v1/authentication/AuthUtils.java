package com.waverchat.api.v1.authentication;

import com.waverchat.api.v1.EnvironmentVariables;
import com.waverchat.api.v1.authentication.session.SessionConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class AuthUtils {

    private AuthUtils() {}

    public static String issueAccessToken(UUID sessionId, UUID userId) {
        // Setting age of access token to be 10 hours
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.HOUR_OF_DAY, SessionConstants.ACCESS_TOKEN_MAX_AGE_HOURS);
        Date expiry = calendar.getTime();

        // fetching the key from the environment variables
        byte[] decodedKey = Base64.getDecoder()
                .decode(EnvironmentVariables.get(SessionConstants.TOKEN_SECRET_KEY_ENV));
        Key key = new SecretKeySpec(decodedKey, 0, decodedKey.length, SessionConstants.SIGNING_ALGORITHM);

        // creating the access token
        String accessToken = Jwts.builder()
                .setId(sessionId.toString())
                .setSubject(userId.toString())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();

        return accessToken;
    }

    public static String issueRefreshToken(UUID sessionId, UUID userId) {
        // Setting age of refresh token
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MONTH, SessionConstants.REFRESH_TOKEN_MAX_AGE_MONTHS);
        Date expiry = calendar.getTime();

        // fetching the key from the environment variables
        byte[] decodedKey = Base64.getDecoder()
                .decode(EnvironmentVariables.get(SessionConstants.TOKEN_SECRET_KEY_ENV));
        Key key = new SecretKeySpec(decodedKey, 0, decodedKey.length, SessionConstants.SIGNING_ALGORITHM);

        String refreshToken = Jwts.builder()
                .setId(sessionId.toString())
                .setSubject(userId.toString())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();

        return refreshToken;
    }

    public static Claims getClaimsFromToken(String token) throws SignatureException, MalformedJwtException {
        // fetching the signing key
        byte[] decodedKey = Base64.getDecoder()
                .decode(EnvironmentVariables.get(SessionConstants.TOKEN_SECRET_KEY_ENV));
        Key key = new SecretKeySpec(decodedKey, 0, decodedKey.length, SessionConstants.SIGNING_ALGORITHM);

        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

}
