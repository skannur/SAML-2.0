/*
 * Copyright [2005] [University Corporation for Advanced Internet Development, Inc.]
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

/**
 * 
 */

package org.opensaml.saml2.core.impl;

import org.opensaml.common.impl.AbstractSAMLObjectUnmarshaller;
import org.opensaml.saml2.core.NameIDPolicy;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.schema.XSBooleanValue;
import org.w3c.dom.Attr;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.saml2.core.NameIDPolicy} objects.
 */
public class NameIDPolicyUnmarshaller extends AbstractSAMLObjectUnmarshaller {

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject samlObject, Attr attribute) throws UnmarshallingException {
        NameIDPolicy policy = (NameIDPolicy) samlObject;

        if (attribute.getLocalName().equals(NameIDPolicy.FORMAT_ATTRIB_NAME)) {
            policy.setFormat(attribute.getValue());
        }
        if (attribute.getLocalName().equals(NameIDPolicy.SP_NAME_QUALIFIER_ATTRIB_NAME)) {
            policy.setSPNameQualifier(attribute.getValue());
        }
        if (attribute.getLocalName().equals(NameIDPolicy.ALLOW_CREATE_ATTRIB_NAME)) {
            policy.setAllowCreate(XSBooleanValue.valueOf(attribute.getValue()));
        } else {
            super.processAttribute(samlObject, attribute);
        }
    }
}