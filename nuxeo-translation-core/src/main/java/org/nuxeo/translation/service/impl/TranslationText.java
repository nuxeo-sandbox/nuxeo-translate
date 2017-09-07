/*
 * (C) Copyright 2017 Nuxeo SA (http://nuxeo.com/) and others.
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
 *     Thibaud Arguillere (via natural-language)
 *     Jackie Aldama
 */
package org.nuxeo.translation.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @since 9.2
 */
public class TranslationText {

	protected String text;

	public TranslationText(String text) {
		this.text = text;
	}

	/**
	 * The translated text
	 *
	 * @return value or {@code null} for none
	 */
	public String getText() {
		return text;
	}

	/**
	 * Utility method to output an TranslationText
	 *
	 * @param e
	 * @return the string representation
	 */
	@Override
	public String toString() {
		return text;
	}

	public Map<String, String> toMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("text", text);
		return map;
	}

}
