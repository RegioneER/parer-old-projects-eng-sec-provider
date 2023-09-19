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

package it.eng.crypto.provider;

import it.eng.crypto.provider.dom.ApacheNodeSetData;
import it.eng.crypto.provider.dom.ApacheOctetStreamData;

import javax.xml.crypto.Data;
import javax.xml.crypto.URIDereferencer;
import javax.xml.crypto.URIReference;
import javax.xml.crypto.URIReferenceException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dom.DOMURIReference;
import org.apache.xml.security.Init;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.utils.IdResolver;
import org.apache.xml.security.utils.resolver.ResourceResolver;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

public class MyDOMURIDereferencer implements URIDereferencer {
    static final URIDereferencer INSTANCE = new MyDOMURIDereferencer();

    private MyDOMURIDereferencer() {
        Init.init();
    }

    public Data dereference(URIReference paramURIReference, XMLCryptoContext paramXMLCryptoContext)
            throws URIReferenceException {
        if (paramURIReference == null)
            throw new NullPointerException("uriRef cannot be null");
        if (paramXMLCryptoContext == null)
            throw new NullPointerException("context cannot be null");
        DOMURIReference localDOMURIReference = (DOMURIReference) paramURIReference;
        Attr localAttr = (Attr) localDOMURIReference.getHere();
        String str1 = paramURIReference.getURI();
        DOMCryptoContext localDOMCryptoContext = (DOMCryptoContext) paramXMLCryptoContext;
        String str2;
        Object localObject;
        if ((str1 != null) && (str1.length() != 0) && (str1.charAt(0) == '#')) {
            str2 = str1.substring(1);
            if (str2.startsWith("xpointer(id(")) {
                int i = str2.indexOf(39);
                int j = str2.indexOf(39, i + 1);
                str2 = str2.substring(i + 1, j);
            }
            localObject = localDOMCryptoContext.getElementById(str2);
            if (localObject != null)
                IdResolver.registerElementById((Element) localObject, str2);
        }
        try {
            str2 = paramXMLCryptoContext.getBaseURI();
            localObject = ResourceResolver.getInstance(localAttr, str2);
            XMLSignatureInput localXMLSignatureInput = ((ResourceResolver) localObject).resolve(localAttr, str2);
            if (localXMLSignatureInput.isOctetStream())
                return new ApacheOctetStreamData(localXMLSignatureInput);
            return new ApacheNodeSetData(localXMLSignatureInput);
        } catch (Exception localException) {
            throw new URIReferenceException(localException);
        }
    }
}

/*
 * Location: C:\workspace_formats\cryptoSigner\lib\xmlsec-1.4.3.jar Qualified Name:
 * org.jcp.xml.dsig.internal.dom.DOMURIDereferencer Java Class Version: 1.4 (48.0) JD-Core Version: 0.5.3
 */
