package com.capstone.shri.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.Map;
import java.util.UUID;

public class CommonUtil {
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null) {
            for (Map.Entry<String, Object> en : map.entrySet()) {
                json.put(en.getKey(), en.getValue());
            }
        }
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }

    public static String getJsonRes(int code, String msg, Object data) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        json.put("data", data);
        return json.toJSONString();
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static int getOffset(int cur, int limit) {
        return (cur - 1) * limit;
    }
}
