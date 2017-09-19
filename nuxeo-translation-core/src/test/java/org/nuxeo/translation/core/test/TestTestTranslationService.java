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
 *     Jackie Aldama
 *     Thibaud Arguillere (via natural-language)
 */
package org.nuxeo.translation.core.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.translation.core.test.mock.MockTranslationProvider;
import org.nuxeo.translation.service.api.Translation;
import org.nuxeo.translation.service.api.TranslationProvider;
import org.nuxeo.translation.service.api.TranslationResponse;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.LocalDeploy;

import com.google.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features({ PlatformFeature.class })
@Deploy("nuxeo-translation-core")
@LocalDeploy({ "nuxeo-translation:OSGI-INF/mock-provider-contrib.xml" })
public class TestTestTranslationService {

	@Inject
	protected Translation translation;

	@Test
	public void testNamedProvider() throws IllegalStateException, IOException, GeneralSecurityException {
		TranslationProvider provider = translation.getProvider(MockTranslationProvider.NAME);
		assertNotNull(provider);
		assertTrue(provider instanceof MockTranslationProvider);

		TranslationResponse response = translation.translateText(MockTranslationProvider.NAME, "hola", null, null);
		assertNotNull(response);
	}

	@Test
	public void testDefaultProvider() throws IllegalStateException, IOException, GeneralSecurityException {
		String name = translation.getDefaultProviderName();
		assertEquals(MockTranslationProvider.NAME, name);

		TranslationResponse response = translation.translateText(null, "hola", null, null);
		assertNotNull(response);
	}

	@Test
	public void tesGetProviders() {
		Map<String, TranslationProvider> providers = translation.getProviders();
		assertNotNull(providers);
		assertEquals(1, providers.size());
		TranslationProvider provider = providers.get(MockTranslationProvider.NAME);
		assertNotNull(provider);
		assertTrue(provider instanceof MockTranslationProvider);
	}

}
