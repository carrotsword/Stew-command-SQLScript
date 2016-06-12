package co.carrotsword;

import java.io.InputStream;

import co.carrotsword.SQLIterable;

public class Exp {
	
	public static void main(String[] args) {
		InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("co/carrotsword/test1.sql");
		
		if(resourceAsStream == null){
			System.err.println("fail1");
			return;
		}
		
		SQLIterable ite = new SQLIterable(resourceAsStream);
		
		for(String sql : ite){
			System.out.println("----------------------------------");
			System.out.println(sql);
			System.out.println("----------------------------------");
		}
		
	}
	
}
