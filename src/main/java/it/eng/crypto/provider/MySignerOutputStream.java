/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna <p/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version. <p/> This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Affero General Public License for more details. <p/> You should
 * have received a copy of the GNU Affero General Public License along with this program. If not,
 * see <https://www.gnu.org/licenses/>.
 */

package it.eng.crypto.provider;

import java.io.OutputStream;
import java.io.IOException;
import java.security.Signature;

public class MySignerOutputStream extends OutputStream {
    private final Signature signature;

    public MySignerOutputStream(Signature signature) {
        this.signature = signature;
    }

    @Override
    public void write(int b) throws IOException {
        try {
            signature.update((byte) b);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        try {
            signature.update(b, off, len);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
