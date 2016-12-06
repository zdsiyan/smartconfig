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
package com.alibaba.fastjson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.serializer.JSONSerializable;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class JSONPObject implements JSONSerializable {

    private String             function;

    private final List<Object> parameters = new ArrayList<Object>();
    
    public JSONPObject() {
        
    }
    
    public JSONPObject(String function) {
        this.function = function;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public List<Object> getParameters() {
        return parameters;
    }
    
    public void addParameter(Object parameter) {
        this.parameters.add(parameter);
    }

    public String toJSONString() {
        return null;
    }

    public void write(JSONSerializer serializer, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter writer = serializer.getWriter();
        writer.write(function);
        writer.write('(');
        for (int i = 0; i < parameters.size(); ++i) {
            if (i != 0) {
                writer.write(',');
            }
            serializer.write(parameters.get(i));
        }
        writer.write(')');
    }

    public String toString() {
        return JSON.toJSONString(this);
    }
}
