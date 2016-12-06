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

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import com.alibaba.fastjson.parser.DefaultJSONParser;

@SuppressWarnings("rawtypes")
public final class CollectionResolveFieldDeserializer extends FieldDeserializer {

    private final Collection collection;

    public CollectionResolveFieldDeserializer(DefaultJSONParser parser, Collection collection){
        super(null, null);
        this.collection = collection;
    }

    @SuppressWarnings("unchecked")
    public void setValue(Object object, Object value) {
        collection.add(value);
    }

    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {

    }

}
