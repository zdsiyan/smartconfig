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
package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

public class LocaleCodec implements ObjectSerializer, ObjectDeserializer {

    public final static LocaleCodec instance = new LocaleCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        if (object == null) {
            serializer.writeNull();
            return;
        }

        Locale locale = (Locale) object;
        serializer.write(locale.toString());
    }
    
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        String text = (String) parser.parse();
        
        if (text == null) {
            return null;
        }
        
        String[] items = text.split("_");
        
        if (items.length == 1) {
            return (T) new Locale(items[0]);
        }
        
        if (items.length == 2) {
            return (T) new Locale(items[0], items[1]);
        }
        
        return (T) new Locale(items[0], items[1], items[2]);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }

}
