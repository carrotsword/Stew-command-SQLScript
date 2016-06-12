package co.carrotsword;

public enum SQLToken {
	Sql,
	StartString,
	EscapeString,
	String,
	EndString,
	StartBlockComment,
	BlockComment,
	EndBlockComment,
	StartLineComment,
	LineComment,
	End;
	
	char prev = '\0';
	
	public SQLToken next(char cur, char next){
		SQLToken status = transit(cur, next);
		prev = cur;
		return status;
	}
	
	SQLToken transitForSqlContext(char cur, char next){
		switch(cur){
		case '/' : 
			switch(next) {
			case '*': return StartBlockComment;
			case '\r':
			case '\n':
			case (char)-1:
				if( prev == '\r' || prev == '\n' ) return End; // sqlplus...?
			default: return Sql;
			}
		case '-': return  (next == '-')?StartLineComment:Sql;
		case '\'': return String;
		case ';': return End;
		default: return ((int)next == -1)?End:Sql;
		}
	}
	
	SQLToken transitForStringContext(char cur, char next){
		switch(cur){
		case '\'':
			return (next == '\'') ? EscapeString : EndString;
		default: 
			return ((int)next == -1)?End:String;
		}
	}

	SQLToken transit(char cur, char next){
		switch(this){			
		case Sql:
		case EndString:
		case EndBlockComment:
			return transitForSqlContext(cur, next);
		case StartString:
		case String:
			return transitForStringContext(cur, next);
		case EscapeString:
			return String;
		case StartBlockComment:
			return BlockComment; // should be *
		case BlockComment:
			return (cur == '/' && prev == '*') ? EndBlockComment:BlockComment;
		case StartLineComment:
			return LineComment; // should be -
		case LineComment:
			switch(cur){
			case '\r': return Sql; 
			case '\n': return Sql;
			default: return LineComment;	
			}
		case End:
		default:
			return this;
		}
	}

	public boolean isStatement() {
		switch(this){
		case StartBlockComment:
		case BlockComment:
		case EndBlockComment:
		case StartLineComment:
		case LineComment:
		case End:
			return false;
		default:
			return true;
		}
	}
}