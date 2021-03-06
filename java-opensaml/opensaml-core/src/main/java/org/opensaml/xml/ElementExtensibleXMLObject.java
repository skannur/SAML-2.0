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

package org.opensaml.xml;

import java.util.List;

import javax.xml.namespace.QName;

/**
 * An XMLObject whose content model contains "any" child elements.
 */
public interface ElementExtensibleXMLObject extends XMLObject {

    /**
     * Gets the list of XMLObjects added to this XMLObject as part of the "any" content model.
     * 
     * @return list of XMLObjects added to this XMLObject as part of the "any" content model
     */
    public List<XMLObject> getUnknownXMLObjects();
    
    /**
     * Gets the list of XMLObjects added to this XMLObject as part of the "any" content model,
     * and which match the specified QName.
     * 
     * @param typeOrName the QName of the statements to return
     * @return list of XMLObjects added to this XMLObject as part of the "any" content model
     */
    public List<XMLObject> getUnknownXMLObjects(QName typeOrName);
}