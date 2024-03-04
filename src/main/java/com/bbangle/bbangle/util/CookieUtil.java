package com.bbangle.bbangle.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Base64;
import org.springframework.http.ResponseCookie;
import org.springframework.util.SerializationUtils;

/**
 * 쿠키 유틸 클래스
 */
public class CookieUtil {

    /**
     * 요청값(이름, 값, 만료 기간)을 바탕으로 쿠키 추가
     *
     * @param response the response
     * @param name     the name
     * @param value    the value
     * @param maxAge   the max age
     */
    public static void addCookie(
        HttpServletResponse response,
        String name,
        String value,
        int maxAge
    ) {
      /*  Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);*/
        ResponseCookie cookie = ResponseCookie.from(name, value)
            .maxAge(maxAge)
            .value(value)
            .sameSite("None")
            .path("/")
            .secure(true)
            .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    /**
     * 쿠키의 이름을 입력받아 쿠키 삭제
     *
     * @param request  the request
     * @param response the response
     * @param name     the name
     */
    public static void deleteCookie(
        HttpServletRequest request,
        HttpServletResponse response,
        String name
    ) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    /**
     * 객체를 직렬화해 쿠키의 값으로 변환
     *
     * @param obj the obj
     * @return the string
     */
    public static String serialize(Object obj) {
        return Base64.getUrlEncoder()
            .encodeToString(SerializationUtils.serialize(obj));
    }

    /**
     * 쿠키를 역직렬화해 객체로 변환
     *
     * @param <T>    the type parameter
     * @param cookie the cookie
     * @param cls    the cls
     * @return the t
     */
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(
            SerializationUtils.deserialize(
                Base64.getUrlDecoder()
                    .decode(cookie.getValue())
            )
        );
    }


}
