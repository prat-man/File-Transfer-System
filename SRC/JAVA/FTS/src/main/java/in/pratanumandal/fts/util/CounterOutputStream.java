package in.pratanumandal.fts.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicLong;

public class CounterOutputStream extends OutputStream {
	
	private AtomicLong counter;
	
	public CounterOutputStream() {
		super();
		this.counter = new AtomicLong(0);
	}
	
	@Override
	public void write(int b) throws IOException {
		counter.incrementAndGet();
	}
	
	@Override
	public void write(byte[] b) throws IOException {
		counter.addAndGet(b.length);
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		counter.addAndGet(len);
	}

	public long getCounter() {
		return counter.get();
	}

}
