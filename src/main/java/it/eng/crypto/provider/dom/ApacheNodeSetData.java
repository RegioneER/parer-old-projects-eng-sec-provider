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
 * $Id: ApacheNodeSetData.java 793943 2009-07-14 15:33:19Z coheigea $
 */
package it.eng.crypto.provider.dom;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.xml.crypto.NodeSetData;
import org.w3c.dom.Node;
import org.apache.xml.security.signature.NodeFilter;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.utils.XMLUtils;

public class ApacheNodeSetData implements ApacheData, NodeSetData {

    private XMLSignatureInput xi;

    public ApacheNodeSetData(XMLSignatureInput xi) {
        this.xi = xi;
    }

    public Iterator iterator() {
        // If nodefilters are set, must execute them first to create node-set
        if (xi.getNodeFilters() != null) {
            return Collections.unmodifiableSet(getNodeSet(xi.getNodeFilters())).iterator();
        }
        try {
            return Collections.unmodifiableSet(xi.getNodeSet()).iterator();
        } catch (Exception e) {
            // should not occur
            throw new RuntimeException("unrecoverable error retrieving nodeset", e);
        }
    }

    public XMLSignatureInput getXMLSignatureInput() {
        return xi;
    }

    private Set getNodeSet(List nodeFilters) {
        if (xi.isNeedsToBeExpanded()) {
            XMLUtils.circumventBug2650(XMLUtils.getOwnerDocument(xi.getSubNode()));
        }

        Set inputSet = new LinkedHashSet();
        XMLUtils.getSet(xi.getSubNode(), inputSet, null, !xi.isExcludeComments());
        Set nodeSet = new LinkedHashSet();
        Iterator i = inputSet.iterator();
        while (i.hasNext()) {
            Node currentNode = (Node) i.next();
            Iterator it = nodeFilters.iterator();
            boolean skipNode = false;
            while (it.hasNext() && !skipNode) {
                NodeFilter nf = (NodeFilter) it.next();
                if (nf.isNodeInclude(currentNode) != 1) {
                    skipNode = true;
                }
            }
            if (!skipNode) {
                nodeSet.add(currentNode);
            }
        }
        return nodeSet;
    }
}
