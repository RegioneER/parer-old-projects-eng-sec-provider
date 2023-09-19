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
 * $Id: DOMCanonicalXMLC14NMethod.java 375655 2006-02-07 18:35:54Z mullan $
 */
package it.eng.crypto.provider.dom;

import javax.xml.crypto.*;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;

import java.security.InvalidAlgorithmParameterException;

import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;

/**
 * DOM-based implementation of CanonicalizationMethod for Canonical XML (with or without comments). Uses Apache XML-Sec
 * Canonicalizer.
 *
 * @author Sean Mullan
 */
public final class DOMCanonicalXMLC14NMethod extends ApacheCanonicalizer {

    public void init(TransformParameterSpec params) throws InvalidAlgorithmParameterException {
        if (params != null) {
            throw new InvalidAlgorithmParameterException(
                    "no parameters " + "should be specified for Canonical XML C14N algorithm");
        }
    }

    public Data transform(Data data, XMLCryptoContext xc) throws TransformException {

        // ignore comments if dereferencing same-document URI that requires
        // you to omit comments, even if the Transform says otherwise -
        // this is to be compliant with section 4.3.3.3 of W3C Rec.
        if (data instanceof DOMSubTreeData) {
            DOMSubTreeData subTree = (DOMSubTreeData) data;
            if (subTree.excludeComments()) {
                try {
                    apacheCanonicalizer = Canonicalizer.getInstance(CanonicalizationMethod.INCLUSIVE);
                } catch (InvalidCanonicalizerException ice) {
                    throw new TransformException("Couldn't find Canonicalizer for: " + CanonicalizationMethod.INCLUSIVE
                            + ": " + ice.getMessage(), ice);
                }
            }
        }

        return canonicalize(data, xc);
    }
}
