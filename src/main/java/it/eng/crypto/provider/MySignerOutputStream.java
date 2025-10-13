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
