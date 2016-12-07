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
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import com.github.zdsiyan.maven.plugin.smartconfig.Configurator;

import com.github.zdsiyan.maven.plugin.smartconfig.model.PointHandle;

/**
 * regex配置器.
 *
 * @author Kevin Zou <kevinz@skfiy.org>
 */
public class RegexConfigurator implements Configurator {

  @Override
  public ByteArrayOutputStream execute(InputStream in, Charset charset,
          List<PointHandle> pointhandles) throws IOException {
    String text = IOUtils.toString(in, charset);

    Pattern pattern;
    Matcher matcher;
    for (PointHandle point : pointhandles) {
    	
      pattern = Pattern.compile(point.getExpression());
      matcher = pattern.matcher(text);

      while (matcher.find()) {
    	  switch(point.getMode()){
			case insert:
				break;
			case delete:
				break;
	  		case replace:
	  		default:
	  			text = matcher.replaceAll(point.getValue());
	  			break;
    	  }
      }
    }

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    out.write(text.getBytes(charset));
    return out;
  }

}
