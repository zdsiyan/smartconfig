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

/**
 * &lt;replacement&gt;节点对象.
 *
 * @author Kevin Zou <kevinz@skfiy.org>
 */
public class PointHandle {

	private String expression;
	private String value;
	private Mode mode;

	public PointHandle() {
	}

	/**
	 *
	 * @param expression
	 *            替换表达式
	 * @param value
	 *            目标值
	 */
	public PointHandle(String expression, String value, Mode mode) {
		this.expression = expression;
		this.value = value;
		this.mode = mode;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public enum Mode {
		/**
		 * replace.
		 */
		replace,
		/**
		 * insert.
		 */
		insert,
		/**
		 * delete
		 */
		delete;
	}
}
