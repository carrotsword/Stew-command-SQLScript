package co.carrotsword;

class SQLBuffer{
	
	SQLToken s = SQLToken.Sql;
	StringBuilder buffer = new StringBuilder();
	char prev = '\0';
	
	/**
	 * 
	 * @param x char
	 * @return false means end of statement.
	 */
	public boolean append(char x){
		
		s = s.next((char)prev, (char)x);
		if(s.isStatement() && prev != '\0'){
			buffer.append((char) prev);
		}else if(s == SQLToken.End){
			prev = x;
			return false;
		}
		prev= x;
		
		return true;
	}
	
	public void newBuffer(){
		s = SQLToken.Sql;
		buffer = new StringBuilder();
	}
	
	public String flush(){
		append((char) -1);
		return getString();
	}
	
	public String getString(){
		return buffer.toString().trim();
	}
}