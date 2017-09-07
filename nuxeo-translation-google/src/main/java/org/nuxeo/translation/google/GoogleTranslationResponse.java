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
 *	   Jackie Aldama
 */
package org.nuxeo.translation.google;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.translation.service.api.TranslationResponse;
import org.nuxeo.translation.service.impl.TranslationText;

import com.google.cloud.translate.Translation;

/**
 *
 * @since 9.2
 */
public class GoogleTranslationResponse implements TranslationResponse {

	String translationResponse = null;
	Translation nativeResponse;

	public GoogleTranslationResponse(Translation response) {
		nativeResponse = response;
	}

	public String translateText() {
		return translationResponse;
	}

}
