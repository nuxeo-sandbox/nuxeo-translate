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
 *	   Jackie Aldama
 */
package org.nuxeo.translation.service.api;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * A service that performs text translation ...
 *
 * @since 9.2
 */
public interface Translation {

	public static String DEFAULT_PROVIDER_NAME = "google";

	/**
	 *
	 * @param provider
	 *            Provider to use. Can be {@code null} (using default provider
	 *            then)
	 * @param text
	 *            Text to analyze
	 * @return a {@link TranslationResponse} object
	 * @since 9.2
	 */
	TranslationResponse translateText(String providerName, String text, String sourceLanguage, String targetLangugage) throws IOException, GeneralSecurityException, IllegalStateException;

	/**
	 * @return The name of default provider or {@code null} is not found
	 * @since 9.2
	 */
	String getDefaultProviderName();

	/**
	 * @param name
	 *            The name of the provider to return
	 * @return The provider object or {@code null} is not found
	 * @since 9.2
	 */
	TranslationProvider getProvider(String name);

	/**
	 * @return all registered providers
	 * @since 9.2
	 */
	Map<String, TranslationProvider> getProviders();

}
