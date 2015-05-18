package com.codi.testjackson;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtil {

    private static final String TAG = JacksonUtil.class.getSimpleName();

    private static ObjectMapper objectMapper = null;

    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(Include.NON_EMPTY);
            // 使反json化时，实体对象不存在对应属性时不出错
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return objectMapper;
    }

    public static String serialize(Object obj) {
        try {
            return getObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            Log.e(TAG, "无法将" + obj + "转化为Json字符串");
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T readValue(String content, Class<T> valueType) {
        try {
            return getObjectMapper().readValue(content, valueType);
        } catch (Exception e) {
            Log.e(TAG, "将" + content + "转化为" + valueType + "时出错");
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> readListValue(String content, Class<T> clazz) {
        try {
            if (content == null || content.trim().equals("")) {
                return new ArrayList<T>();
            }
            List<T> list;
            JavaType javaType = getCollectionType(ArrayList.class, clazz);
            list = getObjectMapper().readValue(content, javaType);
            if (list == null) {
                return new ArrayList<T>();
            }
            return list;
        } catch (Exception e) {
            Log.e(TAG, "将" + content + "转化为" + clazz + "列表时出错");
            e.printStackTrace();
        }
        return new ArrayList<T>();
    }

    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return getObjectMapper().getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }
}
