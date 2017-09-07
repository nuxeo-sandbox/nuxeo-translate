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
 *     Jackie Aldama
 */
package org.nuxeo.translation.google.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.Blobs;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.blobholder.SimpleBlobHolder;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.convert.api.ConversionService;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.platform.mimetype.service.MimetypeRegistryService;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.translation.google.GoogleTranslationProvider;
import org.nuxeo.translation.service.api.Translation;
import org.nuxeo.translation.service.api.TranslationProvider;
import org.nuxeo.translation.service.api.TranslationResponse;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.transaction.TransactionHelper;

import com.google.cloud.translate.Translate;

@RunWith(FeaturesRunner.class)
@Features({ PlatformFeature.class, SimpleFeatureCustom.class })
@Deploy({ "nuxeo-translation-core", "nuxeo-translation-google" })
public class TestGoogleTranslationProvider {

	@Inject
	CoreSession coreSession;

	@Inject
	protected EventService eventService;

	@Inject
	protected ConversionService conversionService;

	@Inject
	MimetypeRegistryService mimetypeRegistryService;

	@Inject
	Translation translationService;

	protected GoogleTranslationProvider googleTranslationProvider = null;

	@Before
	public void setup() {
		System.out.println("**********************in setup()******************");
		Assume.assumeTrue("Credential are not set", areCredentialsSet());

		TranslationProvider translationProvider = translationService.getProvider("google");

		assertNotNull(translationProvider);
		assertTrue(translationProvider instanceof GoogleTranslationProvider);

		googleTranslationProvider = (GoogleTranslationProvider) translationProvider;

		coreSession.save();

		TransactionHelper.commitOrRollbackTransaction();
		TransactionHelper.startTransaction();

		eventService.waitForAsyncCompletion();

	}

	@Test
	public void testTranslateText() throws IOException {

		Assume.assumeTrue("Credential are not set", areCredentialsSet());

		assertNotNull(googleTranslationProvider);

		String text = "Test Translation";

		TranslationResponse response = googleTranslationProvider.translateText(text, "en", "ru");

		assertNotNull(response);
		// String language = response.getLanguage();
		// assertEquals("en", language);

		// We check some know return values

	}

	@Test
	public void testHasNativeClient() {

		Assume.assumeTrue("Credential are not set", areCredentialsSet());

		GoogleTranslationProvider gnl = (GoogleTranslationProvider) translationService
				.getProvider("google");

		Translate client = gnl.getNativeClient();
		assertNotNull(client);

	}

	protected GoogleTranslationProvider getGoogleTranslationProvider() {
		if (googleTranslationProvider != null) {
			return googleTranslationProvider;
		}
		Map<String, String> params = new HashMap<>();
		params.put(GoogleTranslationProvider.APP_NAME_PARAM, "Nuxeo");
		params.put(GoogleTranslationProvider.CREDENTIAL_PATH_PARAM,System.getProperty(SimpleFeatureCustom.GOOGLE_CREDENTIALS_CONFIGURATION_PARAM));
		googleTranslationProvider = new GoogleTranslationProvider(params);

		return googleTranslationProvider;
	}

	protected boolean areCredentialsSet() {
		return StringUtils.isNotBlank(System.getProperty(SimpleFeatureCustom.GOOGLE_CREDENTIALS_CONFIGURATION_PARAM));
	}

}
