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
package org.nuxeo.translation.google.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.translation.google.GoogleTranslationProvider;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.SimpleFeature;

/**
 *
 * @since 9.2
 */
public class SimpleFeatureCustom extends SimpleFeature {

	public static final String TEST_CONF_FILE = "translation-test.conf";

	public static final String GOOGLE_CREDENTIALS_CONFIGURATION_PARAM = GoogleTranslationProvider.CREDENTIAL_PATH_CONFIGURATION_PARAM;

	// Found in the pom.xml
	public static final String GOOGLE_CREDENTIALS_TEST_PARAM = "org.nuxeo.translation.test.google.credential.file";

	protected static Properties props = null;

	@Override
	public void initialize(FeaturesRunner runner) throws Exception {

		File file = null;
		FileInputStream fileInput = null;
		try {
			file = FileUtils.getResourceFileFromContext(TEST_CONF_FILE);
			fileInput = new FileInputStream(file);
			System.out.println("fileInput is null?: " + fileInput == null);
			props = new Properties();
			props.load(fileInput);

		} catch (Exception e) {
			props = null;
		} finally {
			if (fileInput != null) {
				try {
					fileInput.close();
				} catch (IOException e) {
					// Ignore
				}
				fileInput = null;
			}
		}

		// If the file was not there, try with environment variables
		if (props == null) {
			// Try to get environment variables
			addEnvironmentVariable(GOOGLE_CREDENTIALS_CONFIGURATION_PARAM);
			addEnvironmentVariable(GOOGLE_CREDENTIALS_TEST_PARAM);
		}

		filterValues();

		if (props != null) {
			Properties systemProps = System.getProperties();
			systemProps.setProperty(GOOGLE_CREDENTIALS_CONFIGURATION_PARAM, props.getProperty(GOOGLE_CREDENTIALS_CONFIGURATION_PARAM));
		}
	}

	@Override
	public void stop(FeaturesRunner runner) throws Exception {

		Properties p = System.getProperties();
		p.remove(GOOGLE_CREDENTIALS_TEST_PARAM);
		p.remove(GOOGLE_CREDENTIALS_CONFIGURATION_PARAM);
	}

	protected void filterValues() {
		if (props != null) {
			String value = props.getProperty(GOOGLE_CREDENTIALS_TEST_PARAM);
			if (value != null) {
				props.put(GOOGLE_CREDENTIALS_CONFIGURATION_PARAM, value);
			}
		}
	}

	protected void addEnvironmentVariable(String key) {
		String value = System.getenv(key);
		if (value != null) {
			if (props == null) {
				props = new Properties();
			}
			props.put(key, value);
		}
	}

}
