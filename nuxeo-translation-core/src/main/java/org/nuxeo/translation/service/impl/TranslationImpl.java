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
package org.nuxeo.translation.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.blobholder.SimpleBlobHolder;
import org.nuxeo.ecm.core.convert.api.ConversionService;
import org.nuxeo.translation.service.api.Translation;
import org.nuxeo.translation.service.api.TranslationProvider;
import org.nuxeo.translation.service.api.TranslationResponse;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

/**
 * Implementation of the Translation service
 *
 * @since 9.2
 */
public class TranslationImpl extends DefaultComponent implements Translation {

	private static final Log log = LogFactory.getLog(TranslationImpl.class);

	protected static final String CONFIG_EXT_POINT = "configuration";

	protected static final String PROVIDER_EXT_POINT = "provider";

	protected TranslationDescriptor config = null;

	protected Map<String, TranslationProvider> providers = new HashMap<>();

	/**
	 * Component activated notification. Called when the component is activated.
	 * All component dependencies are resolved at that moment. Use this method
	 * to initialize the component.
	 *
	 * @param context
	 *            the component context.
	 */
	@Override
	public void activate(ComponentContext context) {
		super.activate(context);
	}

	/**
	 * Component deactivated notification. Called before a component is
	 * unregistered. Use this method to do cleanup if any and free any resources
	 * held by the component.
	 *
	 * @param context
	 *            the component context.
	 */
	@Override
	public void deactivate(ComponentContext context) {
		super.deactivate(context);
	}

	/**
	 * Application started notification. Called after the application started.
	 * You can do here any initialization that requires a working application
	 * (all resolved bundles and components are active at that moment)
	 *
	 * @param context
	 *            the component context. Use it to get the current bundle
	 *            context
	 * @throws Exception
	 */
	@Override
	public void applicationStarted(ComponentContext context) {
	}

	@Override
	public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
		if (CONFIG_EXT_POINT.equals(extensionPoint)) {
			config = (TranslationDescriptor) contribution;
		} else if (PROVIDER_EXT_POINT.equals(extensionPoint)) {
			TranslationProviderDescriptor desc = (TranslationProviderDescriptor) contribution;
			try {
				TranslationProvider provider = (TranslationProvider) desc.getKlass().getConstructor(Map.class)
						.newInstance(desc.getParameters());
				providers.put(desc.getProviderName(), provider);
			} catch (ReflectiveOperationException e) {
				throw new NuxeoException(e);
			}
		}
	}

	@Override
	public TranslationResponse translateText(String providerName, String text, String sourceLanguage, String targetLanguage) 
		throws IOException, GeneralSecurityException, IllegalStateException {

		if (text == null) {
			throw new IllegalArgumentException("Input text cannot be null");
		}

		TranslationProvider provider = getProviderOrDefault(providerName);
		if (provider == null) {
			throw new NuxeoException("Unknown provider: "
					+ (StringUtils.isBlank(providerName) ? getDefaultProviderName() : providerName));
		}

		return provider.translateText(text, sourceLanguage, targetLanguage);
	}

	protected String extractRawText(Blob blob) throws UnsupportedEncodingException, IOException {

		SimpleBlobHolder blobHolder = new SimpleBlobHolder(blob);
		ConversionService conversionService = Framework.getLocalService(ConversionService.class);
		BlobHolder resultBlob = conversionService.convert("any2text", blobHolder, null);
		String text = new String(resultBlob.getBlob().getByteArray(), "UTF-8");

		return text;
	}
	
	@Override
	public TranslationResponse processBlob(String providerName, Blob blob)
			throws IOException, GeneralSecurityException {
		if (blob == null) {
			throw new IllegalArgumentException("Input Blob cannot be null");
		}

		TranslationProvider provider = getProviderOrDefault(providerName);
		if (provider == null) {
			throw new NuxeoException("Unknown provider: "
					+ (StringUtils.isBlank(providerName) ? getDefaultProviderName() : providerName));
		}

		String text = extractRawText(blob);
		return provider.translateText(text, null, null);
	}
	
	@Override
	public TranslationResponse processDocument(String providerName, DocumentModel doc, String xpath) throws IOException, GeneralSecurityException {

		if (doc == null) {
			throw new IllegalArgumentException("Input DocumentModel cannot be null");
		}

		Blob blob;
		if (StringUtils.isBlank(xpath)) {
			xpath = "file:content";
		}
		blob = (Blob) doc.getPropertyValue(xpath);
		return processBlob(providerName, blob);

	}


	@Override
	public String getDefaultProviderName() {
		return config.getDefaultProviderName();
	}

	@Override
	public TranslationProvider getProvider(String name) {
		return providers.get(name);
	}

	/*
	 * Utility to handle a null provider name
	 */
	protected TranslationProvider getProviderOrDefault(String name) {
		if (StringUtils.isBlank(name)) {
			name = getDefaultProviderName();
		}

		return getProvider(name);
	}

	@Override
	public Map<String, TranslationProvider> getProviders() {
		return providers;
	}

}
