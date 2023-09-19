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

import java.security.InvalidAlgorithmParameterException;
import java.security.spec.AlgorithmParameterSpec;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.spec.DigestMethodParameterSpec;

import org.jcp.xml.dsig.internal.dom.DOMDigestMethod;
import org.jcp.xml.dsig.internal.dom.DOMStructure;
import org.jcp.xml.dsig.internal.dom.DOMUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class MyDOMDigestMethod extends DOMStructure implements DigestMethod {
    static final String SHA384 = "http://www.w3.org/2001/04/xmldsig-more#sha384";
    private DigestMethodParameterSpec params;

    MyDOMDigestMethod(AlgorithmParameterSpec paramAlgorithmParameterSpec) throws InvalidAlgorithmParameterException {
        if ((paramAlgorithmParameterSpec != null)
                && (!(paramAlgorithmParameterSpec instanceof DigestMethodParameterSpec)))
            throw new InvalidAlgorithmParameterException("params must be of type DigestMethodParameterSpec");
        checkParams((DigestMethodParameterSpec) paramAlgorithmParameterSpec);
        this.params = ((DigestMethodParameterSpec) paramAlgorithmParameterSpec);
    }

    MyDOMDigestMethod(Element paramElement) throws MarshalException {
        Element localElement = DOMUtils.getFirstChildElement(paramElement);
        if (localElement != null)
            this.params = unmarshalParams(localElement);
        try {
            checkParams(this.params);
        } catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException) {
            throw new MarshalException(localInvalidAlgorithmParameterException);
        }
    }

    static DigestMethod unmarshal(Element paramElement) throws MarshalException {
        String str = DOMUtils.getAttributeValue(paramElement, "Algorithm");
        if (str.equals("http://www.w3.org/2000/09/xmldsig#sha1"))
            return new SHA1(paramElement);
        if (str.equals("http://www.w3.org/2001/04/xmlenc#sha256"))
            return new SHA256(paramElement);
        if (str.equals("http://www.w3.org/2001/04/xmldsig-more#sha384"))
            return new SHA384(paramElement);
        if (str.equals("http://www.w3.org/2001/04/xmlenc#sha512"))
            return new SHA512(paramElement);
        throw new MarshalException("unsupported DigestMethod algorithm: " + str);
    }

    void checkParams(DigestMethodParameterSpec paramDigestMethodParameterSpec)
            throws InvalidAlgorithmParameterException {
        if (paramDigestMethodParameterSpec == null)
            return;
        throw new InvalidAlgorithmParameterException(
                "no parameters should be specified for the " + getMessageDigestAlgorithm() + " DigestMethod algorithm");
    }

    public final AlgorithmParameterSpec getParameterSpec() {
        return this.params;
    }

    DigestMethodParameterSpec unmarshalParams(Element paramElement) throws MarshalException {
        throw new MarshalException(
                "no parameters should be specified for the " + getMessageDigestAlgorithm() + " DigestMethod algorithm");
    }

    public void marshal(Node paramNode, String paramString, DOMCryptoContext paramDOMCryptoContext)
            throws MarshalException {
        Document localDocument = DOMUtils.getOwnerDocument(paramNode);
        Element localElement = DOMUtils.createElement(localDocument, "DigestMethod",
                "http://www.w3.org/2000/09/xmldsig#", paramString);
        DOMUtils.setAttribute(localElement, "Algorithm", getAlgorithm());
        if (this.params != null)
            marshalParams(localElement, paramString);
        paramNode.appendChild(localElement);
    }

    public boolean equals(Object paramObject) {
        if (this == paramObject)
            return true;
        if (!(paramObject instanceof DigestMethod))
            return false;
        DigestMethod localDigestMethod = (DigestMethod) paramObject;
        boolean bool = (this.params == null) ? false : (localDigestMethod.getParameterSpec() == null) ? true
                : this.params.equals(localDigestMethod.getParameterSpec());
        return ((getAlgorithm().equals(localDigestMethod.getAlgorithm())) && (bool));
    }

    public int hashCode() {
        // if (!($assertionsDisabled))
        // throw new AssertionError("hashCode not designed");
        return 51;
    }

    void marshalParams(Element paramElement, String paramString) throws MarshalException {
        throw new MarshalException(
                "no parameters should be specified for the " + getMessageDigestAlgorithm() + " DigestMethod algorithm");
    }

    abstract String getMessageDigestAlgorithm();

    static final class SHA512 extends MyDOMDigestMethod {
        SHA512(AlgorithmParameterSpec paramAlgorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(paramAlgorithmParameterSpec);
        }

        SHA512(Element paramElement) throws MarshalException {
            super(paramElement);
        }

        public String getAlgorithm() {
            return "http://www.w3.org/2001/04/xmlenc#sha512";
        }

        String getMessageDigestAlgorithm() {
            return "SHA-512";
        }
    }

    static final class SHA384 extends MyDOMDigestMethod {
        SHA384(AlgorithmParameterSpec paramAlgorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(paramAlgorithmParameterSpec);
        }

        SHA384(Element paramElement) throws MarshalException {
            super(paramElement);
        }

        public String getAlgorithm() {
            return "http://www.w3.org/2001/04/xmldsig-more#sha384";
        }

        String getMessageDigestAlgorithm() {
            return "SHA-384";
        }
    }

    static final class SHA256 extends MyDOMDigestMethod {
        SHA256(AlgorithmParameterSpec paramAlgorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(paramAlgorithmParameterSpec);
        }

        SHA256(Element paramElement) throws MarshalException {
            super(paramElement);
        }

        public String getAlgorithm() {
            return "http://www.w3.org/2001/04/xmlenc#sha256";
        }

        String getMessageDigestAlgorithm() {
            return "SHA-256";
        }
    }

    static final class SHA1 extends MyDOMDigestMethod {
        SHA1(AlgorithmParameterSpec paramAlgorithmParameterSpec) throws InvalidAlgorithmParameterException {
            super(paramAlgorithmParameterSpec);
        }

        SHA1(Element paramElement) throws MarshalException {
            super(paramElement);
        }

        public String getAlgorithm() {
            return "http://www.w3.org/2000/09/xmldsig#sha1";
        }

        String getMessageDigestAlgorithm() {
            return "SHA-1";
        }
    }
}

/*
 * Location: C:\workspace_formats\cryptoSigner\lib\xmlsec-1.4.3.jar Qualified Name:
 * org.jcp.xml.dsig.internal.dom.DOMDigestMethod Java Class Version: 1.4 (48.0) JD-Core Version: 0.5.3
 */
