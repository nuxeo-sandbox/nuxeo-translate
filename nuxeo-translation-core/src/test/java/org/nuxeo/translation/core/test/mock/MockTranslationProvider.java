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
 * 	   Jackie Aldama
 *     Thibaud Arguillere (via natural-language)
 */
package org.nuxeo.translation.core.test.mock;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

import org.nuxeo.translation.service.api.TranslationProvider;
import org.nuxeo.translation.service.api.TranslationResponse;

/**
 *
 * @since 9.2
 */
public class MockTranslationProvider implements TranslationProvider {

    public static final String NAME = "mock";

    public MockTranslationProvider(Map<String, String> parameters) {

    }

	@Override
	public TranslationResponse translateText(String text, String sourceLanguage, String targetLanguage)
			throws IOException, GeneralSecurityException, IllegalStateException {
		return new MockTranslationResponse();
	}

	@Override
	public Object getNativeClient() {
		return null;
	}

}
