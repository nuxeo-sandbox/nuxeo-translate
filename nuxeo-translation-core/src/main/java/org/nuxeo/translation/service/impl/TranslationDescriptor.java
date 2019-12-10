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
 *     Thibaud Arguillere (via natural-language)
 *     Jackie Aldama
 */
package org.nuxeo.translation.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;
import org.nuxeo.translation.service.api.Translation;

@XObject("configuration")
public class TranslationDescriptor {

	@XNode("defaultProviderName")
	protected String defaultProviderName;

	public String getDefaultProviderName() {
		if (StringUtils.isBlank(defaultProviderName)) {
			return Translation.DEFAULT_PROVIDER_NAME;
		}
		return defaultProviderName;
	}
}
