package com.uta.edu.ec.connection;



import java.sql.ResultSetMetaData;

import javax.naming.InitialContext;


import javax.sql.DataSource;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

public class Connection {

	public static java.sql.Connection conn = null;
	
	
	
	static {
	    try{
	    	javax.naming.Context initCtx =  new InitialContext(); 
	    	DataSource ds =(DataSource)initCtx.lookup("java:comp/env/jdbc/sigma2db"); 
	    	conn=ds.getConnection();
	    	
	    }
	    catch(Exception ex){
	    	System.out.println(ex);
	    }
	    
	}
	
	public static java.sql.Connection getConnection(){
		return conn;
	}

	
}
