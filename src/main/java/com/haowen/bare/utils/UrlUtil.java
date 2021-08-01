package com.haowen.bare.utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Url工具类
 */
public class UrlUtil {

    /**
     * 获取重定向地址
     *
     * @param userAgent User-Agent
     * @param url       地址
     * @return 重定向地址
     */
    public static String getRealUrl(String userAgent, String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent(userAgent)
                .method(Connection.Method.GET).execute()
                .url().toString();
    }
}
