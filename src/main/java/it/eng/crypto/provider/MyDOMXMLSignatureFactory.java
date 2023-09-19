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

import it.eng.crypto.provider.dom.DOMReference;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.xml.crypto.Data;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.URIDereferencer;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Manifest;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignatureProperties;
import javax.xml.crypto.dsig.SignatureProperty;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.TransformService;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.XMLValidateContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.DigestMethodParameterSpec;
import javax.xml.crypto.dsig.spec.SignatureMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;

import org.jcp.xml.dsig.internal.dom.DOMCanonicalizationMethod;
import org.jcp.xml.dsig.internal.dom.DOMManifest;
import org.jcp.xml.dsig.internal.dom.DOMSignatureProperties;
import org.jcp.xml.dsig.internal.dom.DOMSignatureProperty;
import org.jcp.xml.dsig.internal.dom.DOMSignedInfo;
import org.jcp.xml.dsig.internal.dom.DOMTransform;
import org.jcp.xml.dsig.internal.dom.DOMURIDereferencer;
import org.jcp.xml.dsig.internal.dom.DOMXMLObject;
import org.jcp.xml.dsig.internal.dom.DOMXMLSignature;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public final class MyDOMXMLSignatureFactory extends XMLSignatureFactory {
    public XMLSignature newXMLSignature(SignedInfo paramSignedInfo, KeyInfo paramKeyInfo) {
        return new DOMXMLSignature(paramSignedInfo, paramKeyInfo, null, null, null);
    }

    public XMLSignature newXMLSignature(SignedInfo paramSignedInfo, KeyInfo paramKeyInfo, List paramList,
            String paramString1, String paramString2) {
        return new DOMXMLSignature(paramSignedInfo, paramKeyInfo, paramList, paramString1, paramString2);
    }

    public Reference newReference(String paramString, DigestMethod paramDigestMethod) {
        return newReference(paramString, paramDigestMethod, null, null, null);
    }

    public Reference newReference(String paramString1, DigestMethod paramDigestMethod, List paramList,
            String paramString2, String paramString3) {
        return new DOMReference(paramString1, paramString2, paramDigestMethod, paramList, paramString3, getProvider());
    }

    public Reference newReference(String paramString1, DigestMethod paramDigestMethod, List paramList1, Data paramData,
            List paramList2, String paramString2, String paramString3) {
        if (paramList1 == null)
            throw new NullPointerException("appliedTransforms cannot be null");
        if (paramList1.isEmpty())
            throw new NullPointerException("appliedTransforms cannot be empty");
        if (paramData == null)
            throw new NullPointerException("result cannot be null");
        return new DOMReference(paramString1, paramString2, paramDigestMethod, paramList1, paramData, paramList2,
                paramString3, getProvider());
    }

    public Reference newReference(String paramString1, DigestMethod paramDigestMethod, List paramList,
            String paramString2, String paramString3, byte[] paramArrayOfByte) {
        if (paramArrayOfByte == null)
            throw new NullPointerException("digestValue cannot be null");
        return new DOMReference(paramString1, paramString2, paramDigestMethod, null, null, paramList, paramString3,
                paramArrayOfByte, getProvider());
    }

    public SignedInfo newSignedInfo(CanonicalizationMethod paramCanonicalizationMethod,
            SignatureMethod paramSignatureMethod, List paramList) {
        return newSignedInfo(paramCanonicalizationMethod, paramSignatureMethod, paramList, null);
    }

    public SignedInfo newSignedInfo(CanonicalizationMethod paramCanonicalizationMethod,
            SignatureMethod paramSignatureMethod, List paramList, String paramString) {
        return new DOMSignedInfo(paramCanonicalizationMethod, paramSignatureMethod, paramList, paramString);
    }

    public XMLObject newXMLObject(List paramList, String paramString1, String paramString2, String paramString3) {
        return new DOMXMLObject(paramList, paramString1, paramString2, paramString3);
    }

    public Manifest newManifest(List paramList) {
        return newManifest(paramList, null);
    }

    public Manifest newManifest(List paramList, String paramString) {
        return new DOMManifest(paramList, paramString);
    }

    public SignatureProperties newSignatureProperties(List paramList, String paramString) {
        return new DOMSignatureProperties(paramList, paramString);
    }

    public SignatureProperty newSignatureProperty(List paramList, String paramString1, String paramString2) {
        return new DOMSignatureProperty(paramList, paramString1, paramString2);
    }

    public XMLSignature unmarshalXMLSignature(XMLValidateContext paramXMLValidateContext) throws MarshalException {
        if (paramXMLValidateContext == null)
            throw new NullPointerException("context cannot be null");
        return unmarshal(((DOMValidateContext) paramXMLValidateContext).getNode(), paramXMLValidateContext);
    }

    public XMLSignature unmarshalXMLSignature(XMLStructure paramXMLStructure) throws MarshalException {
        if (paramXMLStructure == null)
            throw new NullPointerException("xmlStructure cannot be null");
        return unmarshal(((DOMStructure) paramXMLStructure).getNode(), null);
    }

    private XMLSignature unmarshal(Node paramNode, XMLValidateContext paramXMLValidateContext) throws MarshalException {
        paramNode.normalize();
        Element localElement = null;
        if (paramNode.getNodeType() == 9)
            localElement = ((Document) paramNode).getDocumentElement();
        else if (paramNode.getNodeType() == 1)
            localElement = (Element) paramNode;
        else
            throw new MarshalException("Signature element is not a proper Node");
        String str = localElement.getLocalName();
        if (str == null)
            throw new MarshalException("Document implementation must support DOM Level 2 and be namespace aware");
        if (str.equals("Signature"))
            return new it.eng.crypto.provider.dom.DOMXMLSignature(localElement, paramXMLValidateContext, getProvider());
        throw new MarshalException("invalid Signature tag: " + str);
    }

    public boolean isFeatureSupported(String paramString) {
        if (paramString == null)
            throw new NullPointerException();
        return false;
    }

    public DigestMethod newDigestMethod(String paramString, DigestMethodParameterSpec paramDigestMethodParameterSpec)
            throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        if (paramString == null)
            throw new NullPointerException();
        if (paramString.equals("http://www.w3.org/2000/09/xmldsig#sha1"))
            return new MyDOMDigestMethod.SHA1(paramDigestMethodParameterSpec);
        if (paramString.equals("http://www.w3.org/2001/04/xmlenc#sha256"))
            return new MyDOMDigestMethod.SHA256(paramDigestMethodParameterSpec);
        if (paramString.equals("http://www.w3.org/2001/04/xmldsig-more#sha384"))
            return new MyDOMDigestMethod.SHA384(paramDigestMethodParameterSpec);
        if (paramString.equals("http://www.w3.org/2001/04/xmlenc#sha512"))
            return new MyDOMDigestMethod.SHA512(paramDigestMethodParameterSpec);
        throw new NoSuchAlgorithmException("unsupported algorithm");
    }

    public SignatureMethod newSignatureMethod(String paramString,
            SignatureMethodParameterSpec paramSignatureMethodParameterSpec)
            throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        if (paramString == null)
            throw new NullPointerException();
        if (paramString.equals("http://www.w3.org/2000/09/xmldsig#rsa-sha1"))
            return new MyDOMSignatureMethod.SHA1withRSA(paramSignatureMethodParameterSpec);
        if (paramString.equals("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256"))
            return new MyDOMSignatureMethod.SHA256withRSA(paramSignatureMethodParameterSpec);
        if (paramString.equals("http://www.w3.org/2001/04/xmldsig-more#rsa-sha384"))
            return new MyDOMSignatureMethod.SHA384withRSA(paramSignatureMethodParameterSpec);
        if (paramString.equals("http://www.w3.org/2001/04/xmldsig-more#rsa-sha512"))
            return new MyDOMSignatureMethod.SHA512withRSA(paramSignatureMethodParameterSpec);
        if (paramString.equals("http://www.w3.org/2000/09/xmldsig#dsa-sha1"))
            return new MyDOMSignatureMethod.SHA1withDSA(paramSignatureMethodParameterSpec);

        throw new NoSuchAlgorithmException("unsupported algorithm");
    }

    public Transform newTransform(String paramString, TransformParameterSpec paramTransformParameterSpec)
            throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        TransformService localTransformService;
        try {
            localTransformService = TransformService.getInstance(paramString, "DOM");
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
            localTransformService = TransformService.getInstance(paramString, "DOM", getProvider());
        }
        localTransformService.init(paramTransformParameterSpec);
        return new DOMTransform(localTransformService);
    }

    public Transform newTransform(String paramString, XMLStructure paramXMLStructure)
            throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        TransformService localTransformService;
        try {
            localTransformService = TransformService.getInstance(paramString, "DOM");
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
            localTransformService = TransformService.getInstance(paramString, "DOM", getProvider());
        }
        if (paramXMLStructure == null)
            localTransformService.init(null);
        else
            localTransformService.init(paramXMLStructure, null);
        return new DOMTransform(localTransformService);
    }

    public CanonicalizationMethod newCanonicalizationMethod(String paramString,
            C14NMethodParameterSpec paramC14NMethodParameterSpec)
            throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        TransformService localTransformService;
        try {
            localTransformService = TransformService.getInstance(paramString, "DOM");
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
            localTransformService = TransformService.getInstance(paramString, "DOM", getProvider());
        }
        localTransformService.init(paramC14NMethodParameterSpec);
        return new DOMCanonicalizationMethod(localTransformService);
    }

    public CanonicalizationMethod newCanonicalizationMethod(String paramString, XMLStructure paramXMLStructure)
            throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        TransformService localTransformService;
        try {
            localTransformService = TransformService.getInstance(paramString, "DOM");
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
            localTransformService = TransformService.getInstance(paramString, "DOM", getProvider());
        }
        if (paramXMLStructure == null)
            localTransformService.init(null);
        else
            localTransformService.init(paramXMLStructure, null);
        return new DOMCanonicalizationMethod(localTransformService);
    }

    public URIDereferencer getURIDereferencer() {
        return MyDOMURIDereferencer.INSTANCE;
    }
}

/*
 * Location: C:\workspace_formats\cryptoSigner\lib\xmlsec-1.4.3.jar Qualified Name:
 * org.jcp.xml.dsig.internal.dom.DOMXMLSignatureFactory Java Class Version: 1.4 (48.0) JD-Core Version: 0.5.3
 */
