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
 * $Id: DOMX509IssuerSerial.java 647272 2008-04-11 19:22:21Z mullan $
 */
package it.eng.crypto.provider.dom;

import javax.xml.crypto.*;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.keyinfo.X509IssuerSerial;

import java.math.BigInteger;
import javax.security.auth.x500.X500Principal;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * DOM-based implementation of X509IssuerSerial.
 *
 * @author Sean Mullan
 */
public final class DOMX509IssuerSerial extends DOMStructure implements X509IssuerSerial {

    private final String issuerName;
    private final BigInteger serialNumber;

    /**
     * Creates a <code>DOMX509IssuerSerial</code> containing the specified issuer distinguished name/serial number pair.
     *
     * @param issuerName
     *            the X.509 issuer distinguished name in RFC 2253 String format
     * @param serialNumber
     *            the serial number
     *
     * @throws IllegalArgumentException
     *             if the format of <code>issuerName</code> is not RFC 2253 compliant
     * @throws NullPointerException
     *             if <code>issuerName</code> or <code>serialNumber</code> is <code>null</code>
     */
    public DOMX509IssuerSerial(String issuerName, BigInteger serialNumber) {
        if (issuerName == null) {
            throw new NullPointerException("issuerName cannot be null");
        }
        if (serialNumber == null) {
            throw new NullPointerException("serialNumber cannot be null");
        }
        // check that issuer distinguished name conforms to RFC 2253
        new X500Principal(issuerName);
        this.issuerName = issuerName;
        this.serialNumber = serialNumber;
    }

    /**
     * Creates a <code>DOMX509IssuerSerial</code> from an element.
     *
     * @param isElem
     *            an X509IssuerSerial element
     */
    public DOMX509IssuerSerial(Element isElem) {
        Element iNElem = DOMUtils.getFirstChildElement(isElem);
        Element sNElem = DOMUtils.getNextSiblingElement(iNElem);
        issuerName = iNElem.getFirstChild().getNodeValue();
        serialNumber = new BigInteger(sNElem.getFirstChild().getNodeValue());
    }

    public String getIssuerName() {
        return issuerName;
    }

    public BigInteger getSerialNumber() {
        return serialNumber;
    }

    public void marshal(Node parent, String dsPrefix, DOMCryptoContext context) throws MarshalException {
        Document ownerDoc = DOMUtils.getOwnerDocument(parent);

        Element isElem = DOMUtils.createElement(ownerDoc, "X509IssuerSerial", XMLSignature.XMLNS, dsPrefix);
        Element inElem = DOMUtils.createElement(ownerDoc, "X509IssuerName", XMLSignature.XMLNS, dsPrefix);
        Element snElem = DOMUtils.createElement(ownerDoc, "X509SerialNumber", XMLSignature.XMLNS, dsPrefix);
        inElem.appendChild(ownerDoc.createTextNode(issuerName));
        snElem.appendChild(ownerDoc.createTextNode(serialNumber.toString()));
        isElem.appendChild(inElem);
        isElem.appendChild(snElem);
        parent.appendChild(isElem);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof X509IssuerSerial)) {
            return false;
        }
        X509IssuerSerial ois = (X509IssuerSerial) obj;
        return (issuerName.equals(ois.getIssuerName()) && serialNumber.equals(ois.getSerialNumber()));
    }

    public int hashCode() {
        assert false : "hashCode not designed";
        return 52;
    }
}
