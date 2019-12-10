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
 *     Thibaud Arguillere (via natural-language)
 *	   Jackie Aldama
 */

package org.nuxeo.translation.google;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.translation.service.api.TranslationProvider;
import org.nuxeo.translation.service.api.TranslationResponse;

/**
 * Implementation of the "google" provider, using Google Translate API
 *
 * @since 9.2
 */
public class GoogleTranslationProvider implements TranslationProvider {

	public static final String APP_NAME_PARAM = "appName";

	public static final String CREDENTIAL_PATH_PARAM = "credentialFilePath";

	public static final String CREDENTIAL_PATH_CONFIGURATION_PARAM = "org.nuxeo.translation.google.credentials";

	public static final String CREDENTIAL_ENV_VARIABLE = "GOOGLE_APPLICATION_CREDENTIALS";

	protected Map<String, String> params;

	protected String credentialsFilePath = null;

	protected Translate translationServiceClient = null;

	public GoogleTranslationProvider(Map<String, String> parameters) {
		params = parameters;
	}

	protected Translate getTranslationServiceClient() throws IOException {

		if (translationServiceClient == null) {
			synchronized (this) {
				if (translationServiceClient == null) {
					try (InputStream is = new FileInputStream(new File(getCredentialFilePath()))) {
						
						final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);
						translationServiceClient = TranslateOptions.newBuilder().setCredentials(myCredentials).build().getService();
					} catch (IOException ioe) {
						throw new NuxeoException(ioe);
					}

				}
			}
		}

		return translationServiceClient;
	}

	@Override
	public TranslationResponse translateText(String text, String sourceLanguage, String targetLanguage) throws IOException {

		Translate translationService = getTranslationServiceClient();

		Translation response = translationService.translate(text);
		//response = translationService.translate(text, TranslateOption.sourceLanguage(sourceLanguage), TranslateOption.targetLanguage(targetLanguage));
		//Translation response = translationService.translate(text, Translate.TranslateOption.sourceLanguage("es"));
		//System.out.println(response.getTranslatedText());
		GoogleTranslationResponse gtr = new GoogleTranslationResponse(response);

		return gtr;
	}

	@Override
	public Translate getNativeClient() {
		try {
			return getTranslationServiceClient();
		} catch (IOException e) {
			throw new NuxeoException(e);
		}
	}

	protected String getCredentialFilePath() {

		if (credentialsFilePath == null) {
			credentialsFilePath = params.get(CREDENTIAL_PATH_PARAM);
			if (StringUtils.isBlank(credentialsFilePath)) {
				credentialsFilePath = System.getProperty(CREDENTIAL_ENV_VARIABLE);
				if (StringUtils.isBlank(credentialsFilePath)) {
					try {
						credentialsFilePath = System.getenv(CREDENTIAL_ENV_VARIABLE);
					} catch (SecurityException e) {
						// We just ignore the error in this case
					}
				}
			}
		}
		return credentialsFilePath;
	}

	protected String getAppName() {
		return params.get(APP_NAME_PARAM);
	}

	/**
	 * @param value
	 * @since 9.2
	 */
	public void setCredentialFilePath(String value) {
		params.put(CREDENTIAL_PATH_PARAM, value);
	}

}
