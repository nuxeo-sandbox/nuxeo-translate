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
package org.nuxeo.translation.operations;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.util.StringList;
import org.nuxeo.translation.service.api.Translation;
import org.nuxeo.translation.service.api.TranslationResponse;

/**
 * @since9.2
 */
@Operation(id = TranslationOnStringOp.ID, category = Constants.CAT_SERVICES, label = "Translation: Process String", description = "Calls translation service on input string, returns the input unchanged. Put the TranslationResponse result in the output context variable")
public class TranslationOnStringOp {

	public static final String ID = "Services.TranslationOnStringOp";

	private static final Log log = LogFactory.getLog(TranslationOnStringOp.class);

	@Context
	protected OperationContext ctx;

	@Context
	protected Translation TranslationService;

	@Param(name = "provider", description = "The Translation provider name (if empty, will use thedefault provider", required = false)
	protected String provider;

	@Param(name = "outputVariable", description = "The key of the context output variable. "
			+ "The output variable is the TranslationResponse object. ", required = true)
	protected String outputVariable;

	@Param(name = "sourceLanguage", description = "The Source Language", required = false)
	protected String sourceLanguage;

	@Param(name = "targetLanguage", description = "The Target Language", required = false)
	protected String targetLanguage;

	@OperationMethod
	public String run(String text) {
		TranslationResponse response = null;

		try {
			response = TranslationService.translateText(provider, text, sourceLanguage, targetLanguage);
			ctx.put(outputVariable, response);
		} catch (IOException | GeneralSecurityException e) {
			if (StringUtils.isEmpty(provider)) {
				log.warn("Call to the Translation API failed for the default provider " + provider, e);
			} else {
				log.warn("Call to the Translation API failed for provider " + provider, e);
			}
		}
		return text;
	}

}
