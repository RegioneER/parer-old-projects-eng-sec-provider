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
 * $Id: DOMCryptoBinary.java 793943 2009-07-14 15:33:19Z coheigea $
 */
package it.eng.crypto.provider.dom;

import java.math.BigInteger;
import javax.xml.crypto.*;
import javax.xml.crypto.dom.DOMCryptoContext;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import org.apache.xml.security.utils.Base64;

/**
 * A DOM-based representation of the XML <code>CryptoBinary</code> simple type as defined in the W3C specification for
 * XML-Signature Syntax and Processing. The XML Schema Definition is defined as:
 *
 * <xmp> <simpleType name="CryptoBinary"> <restriction base = "base64Binary"> </restriction> </simpleType> </xmp>
 *
 * @author Sean Mullan
 */
public final class DOMCryptoBinary extends DOMStructure {

    private final BigInteger bigNum;
    private final String value;

    /**
     * Create a <code>DOMCryptoBinary</code> instance from the specified <code>BigInteger</code>
     *
     * @param bigNum
     *            the arbitrary-length integer
     *
     * @throws NullPointerException
     *             if <code>bigNum</code> is <code>null</code>
     */
    public DOMCryptoBinary(BigInteger bigNum) {
        if (bigNum == null) {
            throw new NullPointerException("bigNum is null");
        }
        this.bigNum = bigNum;
        // convert to bitstring
        value = Base64.encode(bigNum);
    }

    /**
     * Creates a <code>DOMCryptoBinary</code> from a node.
     *
     * @param cbNode
     *            a CryptoBinary text node
     *
     * @throws MarshalException
     *             if value cannot be decoded (invalid format)
     */
    public DOMCryptoBinary(Node cbNode) throws MarshalException {
        value = cbNode.getNodeValue();
        try {
            bigNum = Base64.decodeBigIntegerFromText((Text) cbNode);
        } catch (Exception ex) {
            throw new MarshalException(ex);
        }
    }

    /**
     * Returns the <code>BigInteger</code> that this object contains.
     *
     * @return the <code>BigInteger</code> that this object contains
     */
    public BigInteger getBigNum() {
        return bigNum;
    }

    public void marshal(Node parent, String prefix, DOMCryptoContext context) throws MarshalException {
        parent.appendChild(DOMUtils.getOwnerDocument(parent).createTextNode(value));
    }
}
