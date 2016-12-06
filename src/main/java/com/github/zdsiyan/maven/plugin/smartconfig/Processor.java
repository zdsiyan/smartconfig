/**
 * Copyright (C) 2014-2015 The Skfiy Open Association.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.zdsiyan.maven.plugin.smartconfig;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.github.zdsiyan.maven.plugin.smartconfig.internal.JSONPathConfigurator;
import com.github.zdsiyan.maven.plugin.smartconfig.internal.PropertyConfigurator;
import com.github.zdsiyan.maven.plugin.smartconfig.internal.RegexConfigurator;
import com.github.zdsiyan.maven.plugin.smartconfig.internal.XPathConfigurator;
import com.github.zdsiyan.maven.plugin.smartconfig.model.ConfigFile;

/**
 * &lt;config-file&gt; 处理器抽象类.
 *
 * @author Kevin Zou <kevinz@skfiy.org>
 */
public abstract class Processor {

  private static final Map<ConfigFile.Mode, Configurator> configurators = new HashMap<>();

  static {
    configurators.put(ConfigFile.Mode.property, new PropertyConfigurator());
    configurators.put(ConfigFile.Mode.regex, new RegexConfigurator());
    configurators.put(ConfigFile.Mode.xpath, new XPathConfigurator());
    configurators.put(ConfigFile.Mode.jsonpath, new JSONPathConfigurator());
  }

  /**
   * 文件编码.
   */
  protected final Charset charset;

  /**
   * 构建处理器.
   *
   * @param encoding 文件编码
   */
  public Processor(String encoding) {
    this.charset = Charset.forName(encoding);
  }

  /**
   * 替换配置文件.
   *
   * @param configFile 配置目标
   */
  public abstract void process(ConfigFile configFile);

  /**
   * 判断是否支持处理该文件.
   *
   * @param file 目标文件
   * @return {@code true}可替换/{@code false}不可替换
   */
  public abstract boolean isSupported(File file);

  /**
   * 根据替换模式查找对应的替换器.
   *
   * @param mode 模式
   * @return 替换器
   */
  protected final Configurator getConfigurator(ConfigFile.Mode mode) {
    return configurators.get(mode);
  }

}
