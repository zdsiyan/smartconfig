/*-
 * ========================LICENSE_START=================================
 * Smartconfig Maven Plugin
 * *
 * Copyright (C) 2016 BruceZhang
 * *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */

package com.github.zdsiyan.maven.plugin.smartconfig.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.IOUtils;
import com.github.zdsiyan.maven.plugin.smartconfig.Configurator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.parser.Feature;
import com.github.zdsiyan.maven.plugin.smartconfig.model.PointHandle;

/**
 * jsonpath配置器.
 *
 * @author Kevin Zou <kevinz@skfiy.org>
 */
public class JSONPathConfigurator implements Configurator {

  @Override
  public ByteArrayOutputStream execute(InputStream in, Charset charset,
          List<PointHandle> pointhandles) throws IOException {
    Object json = JSON.parse(IOUtils.toByteArray(new InputStreamReader(in), charset), Feature.OrderedField);
    //JSONObject jsonObject = JSON.parseObject(IOUtils.toString(in, charset), Feature.OrderedField);

    JSONPath path;
    for (PointHandle point : pointhandles) {
    	path = JSONPath.compile(point.getExpression());
    	switch(point.getMode()){
			case insert:
				//jsonObject.put(path, point.getValue());
				path.set(json, point.getValue());
				break;
			case delete:
				//jsonObject.remove(path);
				path.set(json, "");
				break;
    		case replace:
    		default:
	        	path.set(json, point.getValue());
    			//path.set(jsonObject, point.getValue());
	        	break;
    	}
      
    }

    String text = JSON.toJSONString(json, true);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    //out.write(jsonObject.toString().getBytes(charset));
    out.write(text.getBytes(charset));
    return out;
  }

}
