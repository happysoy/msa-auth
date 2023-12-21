package auth.jwt.util;

import auth.jwt.exception.ExceptionStatus;
import auth.jwt.exception.GlobalException;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Component
public class PasswordEncoder {

    // Salt 값 생성
    public static String getSalt() {

        // random 하게 salt 생성
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[32]; //TODO 32byte 인 이유

        // 난수 생성
        random.nextBytes(salt);

        // 10진수 문자열로 변경
        return bytesToString(salt);
    }

    private static String bytesToString(byte[] bytes) {
        StringBuilder buffer = new StringBuilder();
        for (byte b : bytes) {
            buffer.append(String.format("%02x", b));
        }
        return buffer.toString();
    }

    // SHA-256 알고리즘 적용
    public static String getEncryption(String salt, String password) {
        String original = salt + password;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(original.getBytes());
            return bytesToString(md.digest());

        } catch (NoSuchAlgorithmException e) {
            throw new GlobalException(ExceptionStatus.ENCRYPT_ERROR);
        }

    }

    public static Boolean isSameCheck(String savedPassword, String requestPassword, String userSalt) {
        return getEncryption(userSalt, requestPassword).equals(savedPassword);
    }
}