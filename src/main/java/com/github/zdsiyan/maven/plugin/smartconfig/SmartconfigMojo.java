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
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Profile;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.PluginParameterExpressionEvaluator;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import com.github.zdsiyan.maven.plugin.smartconfig.model.ConfigFile;
import com.github.zdsiyan.maven.plugin.smartconfig.model.Smartconfig;
import com.github.zdsiyan.maven.plugin.smartconfig.model.PointHandle;

/**
 * 配置资源文件插件.
 *
 * @author Kevin Zou <kevinz@skfiy.org>
 */
@Mojo(name = "configure", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class SmartconfigMojo extends AbstractMojo {

    /**
     * fast-config.xml文件.
     */
    @Parameter(required = true)
    private File config;
    /**
     * 目标文件的编码.
     */
    @Parameter(required = true, readonly = true, defaultValue = "${project.build.sourceEncoding}")
    private String encoding;
    /**
     * 资源文件的输出目录.
     */
    @Parameter(readonly = true, defaultValue = "${project.build.outputDirectory}")
    private File outputDirectory;

    @Parameter(readonly = true, defaultValue = "${session}")
    private MavenSession session;
    @Parameter(readonly = true, defaultValue = "${mojo}")
    private MojoExecution execution;

    @Override
    public void execute() throws MojoExecutionException {
        if (config == null || !config.exists()) {
            getLog().warn("no fast-config file is provided, skipping running.");
        }

        Smartconfig fastconfig;
        try {
            fastconfig = buildFastconfig();
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }

        BasicProcessor processor = new BasicProcessor(encoding);
        for (ConfigFile cf : fastconfig.getConfigFiles()) {
            if (processor.isSupported(cf.getFile())) {
                processor.process(cf);
            }
        }
    }

    private Smartconfig buildFastconfig() throws Exception {
        PluginParameterExpressionEvaluator pel = new PluginParameterExpressionEvaluator(session, execution);
        Smartconfig fastconfig = new Smartconfig();

        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(config);
        Element root = doc.getRootElement();
        
        // use scriptEngine, maybe we can extend it, not only javascript 
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        
        // load profile
        List<Profile> profiles = session.getCurrentProject().getActiveProfiles();
        profiles.forEach(profile->profile.getProperties().keySet().forEach(key->{
        	Object value = profile.getProperties().get(key);
        	engine.put(key.toString(), value);
        	//getLog().warn("profile:"+key);
        }));
        // load user properties
        session.getUserProperties().keySet().forEach(key->{
        	Object value = session.getUserProperties().get(key);
        	engine.put(key.toString(), value);
        	//getLog().warn("user:"+key);
        });
        /* load sys properties
        session.getSystemProperties().keySet().forEach(key->{
        	Object value = session.getSystemProperties().get(key);
        	engine.put(key.toString(), value);
        	getLog().warn("sys:"+key);
        });
        */
        
        session.getCurrentProject().getProperties().keySet().forEach(key->{
        	Object value = session.getCurrentProject().getProperties().get(key);
        	engine.put(key.toString(), value);
        	//getLog().warn("prop:"+key);
        });

        // config-file
        for (Element cf : root.getChildren()) {
            String path = String.valueOf(pel.evaluate(cf.getAttributeValue("path")));
            File file = new File(path);
            if (!file.isAbsolute()) {
                file = new File(outputDirectory, path);
            }
            
            boolean disable = false;
            //eval the script
            if (StringUtils.isNotEmpty(cf.getAttributeValue("disable"))){
            	Object result = engine.eval(cf.getAttributeValue("disable"));
            	if(Boolean.TRUE.equals(result)){
            		disable = true;
            	}
            }
            if(disable == true){
            	continue;
            }
            
            //rename to
            if (StringUtils.isNotEmpty(cf.getAttributeValue("replace"))){
            	String replace = String.valueOf(pel.evaluate(cf.getAttributeValue("replace")));
            	//getLog().warn("filepath:"+file.getPath());
            	File refile = new File(file.getParent()+File.separator+replace);
            	//getLog().warn("refilepath:"+refile.getPath());
            	FileUtils.rename(file, refile);
            	continue;
            }

            ConfigFile.Mode mode;
            if (StringUtils.isNotEmpty(cf.getAttributeValue("mode"))) {
                mode = ConfigFile.Mode.valueOf(cf.getAttributeValue("mode"));
            } else {
                mode = toConfigMode(path.substring(path.lastIndexOf(".") + 1));
            }

            if (mode == null) {
                throw new SmartconfigException("Not found file[" + path + "] replace mode");
            }
            
            

            ConfigFile configFile = new ConfigFile(file, mode);
            
            for (Element rt : cf.getChildren()) {
                String expression = rt.getAttributeValue("expression");
                String value = String.valueOf(pel.evaluate(rt.getTextTrim()));
                PointHandle.Mode phMode;
                if (StringUtils.isNotEmpty(rt.getAttributeValue("mode"))) {
                	phMode = PointHandle.Mode.valueOf(rt.getAttributeValue("mode"));
                }else{
                	phMode = PointHandle.Mode.replace;
                }
                if (mode == null) {
                    throw new SmartconfigException("Not found pointhandle mode");
                }
                configFile.addPointHandle(new PointHandle(expression, value, phMode));
                
            }
            fastconfig.addConfigFile(configFile);
        }
        return fastconfig;
    }

    private ConfigFile.Mode toConfigMode(String suffix) {
        if ("xml".equals(suffix)) {
            return ConfigFile.Mode.xpath;
        } else if ("json".equals(suffix)) {
            return ConfigFile.Mode.jsonpath;
        } else if ("properties".equals(suffix)) {
            return ConfigFile.Mode.property;
        }
        return null;
    }
}
