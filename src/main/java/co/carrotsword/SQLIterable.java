package co.carrotsword;

import java.io.InputStream;
import java.util.Iterator;

public class SQLIterable implements Iterable<String> {

	InputStream inputStream;
	
	public SQLIterable(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	@Override
	public Iterator<String> iterator() {
		return new SQLIterator(inputStream);
	}

}

