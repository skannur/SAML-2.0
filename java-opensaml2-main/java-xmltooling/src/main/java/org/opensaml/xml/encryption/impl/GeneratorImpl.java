/*
 * Copyright [2006] [University Corporation for Advanced Internet Development, Inc.]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.xml.encryption.impl;

import org.opensaml.xml.encryption.Generator;
import org.opensaml.xml.signature.impl.CryptoBinaryImpl;


/**
 * Concrete implementation of {@link org.opensaml.xml.encryption.Generator}
 */
public class GeneratorImpl extends CryptoBinaryImpl implements Generator {

    /**
     * Constructor
     *
     * @param namespaceURI
     * @param elementLocalName
     * @param namespacePrefix
     */
    protected GeneratorImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

}
