package co.carrotsword;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import net.argius.stew.Command;
import net.argius.stew.CommandException;
import net.argius.stew.Logger;
import net.argius.stew.Parameter;

public class SQLScript extends Command {

    private static final Logger log = Logger.getLogger(SQLScript.class);	
	@Override
	public void execute(Connection conn, Parameter parameter)
			throws CommandException {
		
        final File file = resolvePath(parameter.at(1));
        System.out.println("SQL Script:" + file.getAbsolutePath());

        if (log.isDebugEnabled()) {
            log.debug("file: " + file.getAbsolutePath());
        }
        SQLIterable sis;
        try{
        	 sis = new SQLIterable(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new CommandException(e);
		}
        
        try {
			conn.setAutoCommit(false);
			for(String sql : sis ){
				
		        if (log.isDebugEnabled()) {
		            log.debug("Executing: " + sql);
		        }
		        
		        try(Statement statement = conn.createStatement()){
		        	statement.execute(sql);
		        }catch(SQLException e){
		        	System.err.println("Error " + e.getMessage() + "on:");
		        	System.err.println(sql);
		        	log.warn("Error: {} ", e.getMessage());
		        	log.error(e);
		        }
			}
			
			conn.commit();
        } catch (SQLException e) {
			throw new CommandException(e);
		}
	}

}
