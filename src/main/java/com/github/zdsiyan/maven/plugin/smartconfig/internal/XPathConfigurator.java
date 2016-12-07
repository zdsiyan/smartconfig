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
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import com.github.zdsiyan.maven.plugin.smartconfig.Configurator;

import com.github.zdsiyan.maven.plugin.smartconfig.model.PointHandle;
import com.github.zdsiyan.maven.plugin.smartconfig.util.VTDUtils;
import com.ximpleware.AutoPilot;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XMLModifier;

/**
 * xpath配置器.
 *
 * @author Kevin Zou <kevinz@skfiy.org>
 */
public class XPathConfigurator implements Configurator {

	private static final Pattern XML_ATTRIBUTE_PATTERN = Pattern.compile(".*/@[^/]+$");

	@Override
	public ByteArrayOutputStream execute(InputStream in, Charset charset, List<PointHandle> pointhandles)
			throws IOException {
		try {
			VTDGen vtdGen = new VTDGen();
			vtdGen.setDoc(IOUtils.toByteArray(in));
			vtdGen.parse(true);

			VTDNav vtdNav = vtdGen.getNav();
			VTDUtils utils = new VTDUtils(vtdNav);
			AutoPilot autoPilot = new AutoPilot(vtdNav);
			XMLModifier xmlModifier = new XMLModifier(vtdNav);

			for (PointHandle point : pointhandles) {
				switch (point.getMode()) {
				case insert:
					xmlModifier = utils.insert(autoPilot, xmlModifier, point.getExpression(), point.getValue());
					break;
				case delete:
					xmlModifier = utils.delete(autoPilot, xmlModifier, point.getExpression());
					break;
				case replace:
				default:
					xmlModifier = utils.update(autoPilot, xmlModifier, point.getExpression(), point.getValue());
					break;
				}
			}

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			xmlModifier.output(out);
			return out;
		} catch (Exception ex) {
			// FIXME
			throw new RuntimeException(ex);
		}
	}

}
