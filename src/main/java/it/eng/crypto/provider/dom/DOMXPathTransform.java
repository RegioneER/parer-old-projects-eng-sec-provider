/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Copyright 2005 The Apache Software Foundation.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 */
/*
 * $Id: DOMXPathTransform.java 375655 2006-02-07 18:35:54Z mullan $
 */
package it.eng.crypto.provider.dom;

import javax.xml.crypto.*;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.crypto.dsig.spec.XPathFilterParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

/**
 * DOM-based implementation of XPath Filtering Transform. (Uses Apache XML-Sec Transform implementation)
 *
 * @author Sean Mullan
 */
public final class DOMXPathTransform extends ApacheTransform {

    public void init(TransformParameterSpec params) throws InvalidAlgorithmParameterException {
        if (params == null) {
            throw new InvalidAlgorithmParameterException("params are required");
        } else if (!(params instanceof XPathFilterParameterSpec)) {
            throw new InvalidAlgorithmParameterException("params must be of type XPathFilterParameterSpec");
        }
        this.params = params;
    }

    public void init(XMLStructure parent, XMLCryptoContext context) throws InvalidAlgorithmParameterException {

        super.init(parent, context);
        unmarshalParams(DOMUtils.getFirstChildElement(transformElem));
    }

    private void unmarshalParams(Element paramsElem) {
        String xPath = paramsElem.getFirstChild().getNodeValue();
        // create a Map of namespace prefixes
        NamedNodeMap attributes = paramsElem.getAttributes();
        if (attributes != null) {
            int length = attributes.getLength();
            Map namespaceMap = new HashMap(length);
            for (int i = 0; i < length; i++) {
                Attr attr = (Attr) attributes.item(i);
                String prefix = attr.getPrefix();
                if (prefix != null && prefix.equals("xmlns")) {
                    namespaceMap.put(attr.getLocalName(), attr.getValue());
                }
            }
            this.params = new XPathFilterParameterSpec(xPath, namespaceMap);
        } else {
            this.params = new XPathFilterParameterSpec(xPath);
        }
    }

    public void marshalParams(XMLStructure parent, XMLCryptoContext context) throws MarshalException {

        super.marshalParams(parent, context);
        XPathFilterParameterSpec xp = (XPathFilterParameterSpec) getParameterSpec();
        Element xpathElem = DOMUtils.createElement(ownerDoc, "XPath", XMLSignature.XMLNS,
                DOMUtils.getSignaturePrefix(context));
        xpathElem.appendChild(ownerDoc.createTextNode(xp.getXPath()));

        // add namespace attributes, if necessary
        Iterator i = xp.getNamespaceMap().entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            xpathElem.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + (String) entry.getKey(),
                    (String) entry.getValue());
        }

        transformElem.appendChild(xpathElem);
    }
}
