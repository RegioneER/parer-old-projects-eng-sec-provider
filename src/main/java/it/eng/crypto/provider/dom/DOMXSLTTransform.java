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
 * Copyright 2005-2009 The Apache Software Foundation.
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
 * $Id: DOMXSLTTransform.java 793943 2009-07-14 15:33:19Z coheigea $
 */
package it.eng.crypto.provider.dom;

import java.security.InvalidAlgorithmParameterException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.crypto.*;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;

/**
 * DOM-based implementation of XSLT Transform. (Uses Apache XML-Sec Transform implementation)
 *
 * @author Sean Mullan
 */
public final class DOMXSLTTransform extends ApacheTransform {

    public void init(TransformParameterSpec params) throws InvalidAlgorithmParameterException {
        if (params == null) {
            throw new InvalidAlgorithmParameterException("params are required");
        }
        if (!(params instanceof XSLTTransformParameterSpec)) {
            throw new InvalidAlgorithmParameterException("unrecognized params");
        }
        this.params = params;
    }

    public void init(XMLStructure parent, XMLCryptoContext context) throws InvalidAlgorithmParameterException {

        super.init(parent, context);
        unmarshalParams(DOMUtils.getFirstChildElement(transformElem));
    }

    private void unmarshalParams(Element sheet) {
        this.params = new XSLTTransformParameterSpec(new javax.xml.crypto.dom.DOMStructure(sheet));
    }

    public void marshalParams(XMLStructure parent, XMLCryptoContext context) throws MarshalException {
        super.marshalParams(parent, context);
        XSLTTransformParameterSpec xp = (XSLTTransformParameterSpec) getParameterSpec();
        Node xsltElem = ((javax.xml.crypto.dom.DOMStructure) xp.getStylesheet()).getNode();
        DOMUtils.appendChild(transformElem, xsltElem);
    }
}
