/*
 * (C) Copyright 2015-2017 Nuxeo (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Michael Vachette (via nuxeo-vision)
 *     Thibaud Arguillere
 */
package org.nuxeo.translation.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XNodeMap;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * XML contribution to the service. Each provider defines their own parameters.
 *
 * @since 9.2
 */
@XObject("provider")
public class TranslationProviderDescriptor {

	@XNode("@name")
	protected String providerName;

	@XNode("@class")
	protected Class<?> klass;

	@XNodeMap(value = "parameters/parameter", key = "@name", type = HashMap.class, componentType = String.class)
	protected Map<String, String> parameters = new HashMap<>();

	public Class<?> getKlass() {
		return klass;
	}

	public String getProviderName() {
		return providerName;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}
}
