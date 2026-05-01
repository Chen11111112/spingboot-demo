package com.example.demo.util;

import java.util.Collections;
import java.util.Map;

public class SingleValueObjectData {
    public static Map<String, Object> create(String key, Object value) {
        return Collections.singletonMap(key, value);
    }
}
