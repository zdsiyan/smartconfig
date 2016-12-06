/**
 * Copyright (C) 2014-2015 The Skfiy Open Association.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.util.TypeUtils;

public class JavaObjectDeserializer implements ObjectDeserializer {

    public final static JavaObjectDeserializer instance = new JavaObjectDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            if (componentType instanceof TypeVariable) {
                TypeVariable<?> componentVar = (TypeVariable<?>) componentType;
                componentType = componentVar.getBounds()[0];
            }

            List<Object> list = new ArrayList<Object>();
            parser.parseArray(componentType, list);
            Class<?> componentClass;
            if (componentType instanceof Class) {
                componentClass = (Class<?>) componentType;
                if (componentClass == boolean.class) {
                    return (T) TypeUtils.cast(list, boolean[].class, parser.getConfig());
                } else if (componentClass == short.class) {
                    return (T) TypeUtils.cast(list, short[].class, parser.getConfig());
                } else if (componentClass == int.class) {
                    return (T) TypeUtils.cast(list, int[].class, parser.getConfig());
                } else if (componentClass == long.class) {
                    return (T) TypeUtils.cast(list, long[].class, parser.getConfig());
                } else if (componentClass == float.class) {
                    return (T) TypeUtils.cast(list, float[].class, parser.getConfig());
                } else if (componentClass == double.class) {
                    return (T) TypeUtils.cast(list, double[].class, parser.getConfig());
                }

                Object[] array = (Object[]) Array.newInstance(componentClass, list.size());
                list.toArray(array);
                return (T) array;
            } else {
                return (T) list.toArray();
            }

        }

        return (T) parser.parse(fieldName);
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}
