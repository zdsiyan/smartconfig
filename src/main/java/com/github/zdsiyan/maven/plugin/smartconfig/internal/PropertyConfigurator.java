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

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.github.zdsiyan.maven.plugin.smartconfig.Configurator;
import com.github.zdsiyan.maven.plugin.smartconfig.model.PointHandle;

/**
 * properties配置器.
 *
 * @author Kevin Zou <kevinz@skfiy.org>
 */
public class PropertyConfigurator implements Configurator {

  @Override
  public ByteArrayOutputStream execute(InputStream in, Charset charset,
          List<PointHandle> pointhandles) throws IOException {
    try {
      PropertiesConfiguration configuration = new PropertiesConfiguration();
      configuration.load(in, charset.name());
      
      pointhandles.forEach(point ->{
    	  switch(point.getMode()){
			case insert:
				configuration.addProperty(point.getExpression(), point.getValue());
				break;
			case delete:
				configuration.setProperty(point.getExpression(), "");
				break;
	  		case replace:
	  		default:
	  			configuration.setProperty(point.getExpression(), point.getValue());
		        break;
    	  }
      });

      // output
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      configuration.save(out, charset.name());
      return out;
    } catch (ConfigurationException ex) {
      // FIXME
      throw new RuntimeException(ex);
    }
  }

}
