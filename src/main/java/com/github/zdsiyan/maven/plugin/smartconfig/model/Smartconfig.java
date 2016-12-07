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

package com.github.zdsiyan.maven.plugin.smartconfig.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * &lt;fast-config&gt;节点对象.
 *
 * @author Kevin Zou <kevinz@skfiy.org>
 */
public class Smartconfig {

  private final List<ConfigFile> configFiles = new ArrayList<>();

  public List<ConfigFile> getConfigFiles() {
    return Collections.unmodifiableList(configFiles);
  }

  public void setConfigFiles(List<ConfigFile> configFiles) {
    this.configFiles.addAll(configFiles);
  }

  public void addConfigFile(ConfigFile configFile) {
    this.configFiles.add(configFile);
  }
}
