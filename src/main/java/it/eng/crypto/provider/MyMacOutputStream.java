// Java
package it.eng.crypto.provider;

import java.io.OutputStream;
import java.io.IOException;
import javax.crypto.Mac;

public class MyMacOutputStream extends OutputStream {
    private final Mac mac;

    public MyMacOutputStream(Mac mac) {
	this.mac = mac;
    }

    @Override
    public void write(int b) throws IOException {
	mac.update((byte) b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
	mac.update(b, off, len);
    }
}
