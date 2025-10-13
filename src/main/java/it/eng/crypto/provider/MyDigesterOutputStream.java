package it.eng.crypto.provider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;

public class MyDigesterOutputStream extends OutputStream {
    private final MessageDigest md;
    private final boolean cache;
    private final ByteArrayOutputStream buffer;

    public MyDigesterOutputStream(MessageDigest md) {
	this(md, false);
    }

    public MyDigesterOutputStream(MessageDigest md, boolean cache) {
	this.md = md;
	this.cache = cache;
	this.buffer = cache ? new ByteArrayOutputStream() : null;
    }

    @Override
    public void write(int b) throws IOException {
	md.update((byte) b);
	if (cache)
	    buffer.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
	md.update(b, off, len);
	if (cache)
	    buffer.write(b, off, len);
    }

    public byte[] getDigestValue() {
	return md.digest();
    }

    public InputStream getInputStream() {
	if (!cache)
	    throw new IllegalStateException("Caching not enabled");
	return new ByteArrayInputStream(buffer.toByteArray());
    }
}
