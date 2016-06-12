package co.carrotsword;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

class SQLIterator implements Iterator<String> {

	InputStream is;
	String sql = ""; // initial:"", reached to end of stream :null
	boolean atEOS = false;
	IterStatus iterStatus = IterStatus.Initial;;
	SQLBuffer sqlBuffer = new SQLBuffer();
	
	public static enum IterStatus {
		
		Ready,
		Consumed,
		Initial,
		End;
		
		public boolean shoudToRead(){
			return (this == Consumed || this == Initial);
		}
	}
	
	public SQLIterator(InputStream inputStream) {
		is = inputStream;
	}

	@Override
	public boolean hasNext() {
		return standByNextSQL();
	}
	
	boolean standByNextSQL(){
		if(iterStatus.shoudToRead()){
			while(sql != null && sql.trim().length() == 0){
				sql = readStatement();
			}
			iterStatus = (sql==null)?IterStatus.End:IterStatus.Ready;
		}
		return (sql != null);
	}

	/**
	 * @return SQL String without ';'. return null when at end of stream.
	 */
	String readStatement(){
		if(atEOS){
			return null;
		}
		int cur;
		try {
			
			sqlBuffer.newBuffer();
			while((cur= is.read())!= -1){
				if(!sqlBuffer.append((char)cur)){
					return sqlBuffer.getString();
				}
			}
			
			atEOS = true;
			return sqlBuffer.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String next() {
		standByNextSQL();
		iterStatus = IterStatus.Consumed;
		return sql;
	}

}