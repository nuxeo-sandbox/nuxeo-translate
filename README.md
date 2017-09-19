# nuxeo-translate

**WORK IN PROGRESS**

## About
This plugin provides a service allowing text to be translated to/from English.

To cite Google Translate API documentation:

Cloud Translation API provides a simple programmatic interface for translating an arbitrary string into any supported language using state-of-the-art Neural Machine Translation. Translation API is highly responsive, so websites and applications can integrate with Translation API for fast, dynamic translation of source text from the source language to a target language (e.g., French to English). Language detection is also available in cases where the source language is unknown. The underlying technology pushes the boundary of Machine Translation and is updated constantly to seamlessly improve translations and introduce new languages and language pairs.

## Usage
 
### Limitations

The service accepts a string to be directly processed. Documents and Blobs will be supported *very* soon.

### Example of JS Automation Using an Operation
var str = 'Hola';

var result = Services.TranslationOnStringOp(
    str, {
      'outputVariable': "response"
    });
  
var response = ctx.response;

Console.log('WE TRANSLATED TEXT FOR YOU!!!!!!!!!!!!!!!' + response);

## Google Translate API: Authenticating to the Service

- Enable the Translate API from the google developer console
- As of August 2017, billing must be activated in your google account in order to use the Translate API
- Get a credential file from the Google Developer Console
- Store it in in you Dev/Test/Prod Nuxeo server
- Set the `org.nuxeo.translate.google.credential` (in nuxeo.conf) parameter to the full path to this credentials file.

#### Algorithm Used when Connecting to the Service
 1. If the `org.nuxeo.translate.google.credential` configuraiton parameter is set, use it
 2. Else, read the `GOOGLE_APPLICATION_CREDENTIALS` Environment Variable (this is common Google variable, set to access its misc. APIs)
 3. If none of the previous returned a value, the call will fail

 
## Build
#### Requirements
Build requires the following software:
- git
- maven

```
git clone https://github.com/nuxeo-sandbox/nuxeo-translate
cd nuxeo-translate
mvn clean install
```
Notice that for unit test, credentials are set differenty (see below or in the source code). To compile without running the tests:

```
mvn clean install -DskipTests=true
#to compile the test without running them:
mvn test-compile
```

## Deploy

As of this version, Google is only supported as a provider:

- Enable the Translate API from the google developer console
- As of August 2017, billing must be activated in your google account in order to use the Translate API
- Get a credential file from the Google Developer Console
- Store it in in you Dev/Test/Prod Nuxeo server
- Edit nuxeo.conf and add the path to the credentials file
```
org.nuxeo.translate.google.credential=PATH_TO_JSON_CREDENTIAL_FILE
```
  - Alternatively, you can set the `GOOGLE_APPLICATION_CREDENTIALS` environmenent variable
 
- Install the marketplace package
 
# Resources (Documentation and other links)

[Google Translate API](https://cloud.google.com/translate)
 
# License
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
 
# About Nuxeo
The [Nuxeo Platform](http://www.nuxeo.com/products/content-management-platform/) is an open source customizable and extensible content management platform for building business applications. It provides the foundation for developing [document management](http://www.nuxeo.com/solutions/document-management/), [digital asset management](http://www.nuxeo.com/solutions/digital-asset-management/), [case management application](http://www.nuxeo.com/solutions/case-management/) and [knowledge management](http://www.nuxeo.com/solutions/advanced-knowledge-base/). You can easily add features using ready-to-use addons or by extending the platform using its extension point system.
 
The Nuxeo Platform is developed and supported by Nuxeo, with contributions from the community.
 
Nuxeo dramatically improves how content-based applications are built, managed and deployed, making customers more agile, innovative and successful. Nuxeo provides a next generation, enterprise ready platform for building traditional and cutting-edge content oriented applications. Combining a powerful application development environment with
SaaS-based tools and a modular architecture, the Nuxeo Platform and Products provide clear business value to some of the most recognizable brands including Verizon, Electronic Arts, Netflix, Sharp, FICO, the U.S. Navy, and Boeing. Nuxeo is headquartered in New York and Paris.
More information is available at [www.nuxeo.com](http://www.nuxeo.com).
