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
	
	protected String PDF_EN = "files/DocumentManagement-ES-2017.pdf";
	
	protected DocumentModel pdfEN;

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
	
	protected DocumentModel createFileDocument(DocumentModel parent, String filePath) {

		File file = FileUtils.getResourceFileFromContext(filePath);
		FileBlob fileBlob = new FileBlob(file);
		fileBlob.setFilename(file.getName());

		String mimeType = mimetypeRegistryService.getMimetypeFromBlob(fileBlob);
		// At runtime, importing a Word doc sets the correct mimetype. In
		// Eclipse, running the test in debug mode, it is set to
		// "Application/zip" (on Mac), and so, the conversion to text fails.
		if (file.getName().indexOf(".docx") > 0 && mimeType.indexOf("/zip") > 0) {
			mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		}
		fileBlob.setMimeType(mimeType);

		DocumentModel doc = coreSession.createDocumentModel(parent.getPathAsString(), file.getName(), "File");
		doc.setPropertyValue("dc:title", file.getName());
		doc.setPropertyValue("file:content", fileBlob);
		doc = coreSession.createDocument(doc);
		doc = coreSession.saveDocument(doc);

		return doc;
	}

	@Before
	public void setup() {
		Assume.assumeTrue("Credential are not set", areCredentialsSet());

		TranslationProvider translationProvider = translationService.getProvider("google");

		assertNotNull(translationProvider);
		assertTrue(translationProvider instanceof GoogleTranslationProvider);

		googleTranslationProvider = (GoogleTranslationProvider) translationProvider;
		
		DocumentModel parent = coreSession.createDocumentModel("/", "test-translation", "Folder");
		parent.setPropertyValue("dc:title", "test-translation");
		parent = coreSession.createDocument(parent);
		parent = coreSession.saveDocument(parent);

		pdfEN = createFileDocument(parent, PDF_EN);

		coreSession.save();

		TransactionHelper.commitOrRollbackTransaction();
		TransactionHelper.startTransaction();

		eventService.waitForAsyncCompletion();

	}

	@Test
	public void testTranslateText() throws IOException {

		Assume.assumeTrue("Credential are not set", areCredentialsSet());

		assertNotNull(googleTranslationProvider);

		String text = "hola";

		TranslationResponse response = googleTranslationProvider.translateText(text, "en", "es");

		assertNotNull(response);
		assertEquals("Hello", response.getTextTranslation());
		// String language = response.getLanguage();
		// assertEquals("en", language);
	}
	
	@Test
	public void testTranslateDoc() throws IOException {
		
		String text;
		String translation;
		TranslationResponse response;
		
		pdfEN.refresh();
		text = extractRawText(pdfEN);
		response = googleTranslationProvider.translateText(text, null, null);
		assertNotNull(response);
		translation = response.getTextTranslation();
	}
	
	protected String extractRawText(DocumentModel doc) throws UnsupportedEncodingException, IOException {

		BlobHolder blobHolder = doc.getAdapter(BlobHolder.class);

		return extractRawText(blobHolder.getBlob());
	}
	
	protected String extractRawText(Blob blob) throws UnsupportedEncodingException, IOException {

		SimpleBlobHolder blobHolder = new SimpleBlobHolder(blob);
		BlobHolder resultBlob = conversionService.convert("any2text", blobHolder, null);
		String text = new String(resultBlob.getBlob().getByteArray(), "UTF-8");

		return text;
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
