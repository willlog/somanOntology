package com.uta.edu.ec.dml;
import com.uta.edu.ec.data.*;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.uta.edu.ec.connection.*;

public class ontologiaDatosDml {
	
	public boolean insertOntologiaData(ontologiaDatos datos) throws SQLException{
		java.sql.Connection connection = null;
		java.sql.PreparedStatement ps=null;
		java.sql.ResultSet rs=null;
		boolean estado=true;
		String sql="INSERT INTO ontologiadatos(TableName,ColumnType,ColumnDisplaySize,ColumnTypeName,ColumnLabel,ColumnName,tipogrid,text,tipo) VALUES (?,?,?,?,?,?,?,?,?)";
		try{
			if(connection==null || connection.isClosed()){
				connection=Connection.getConnection();
			}
			ps =connection.prepareStatement(sql);
			ps.setString(1, datos.get_tablename());
			ps.setInt(2, datos.get_columnType());
			ps.setInt(3, datos.get_columnDisplaySize());
			ps.setString(4, datos.get_columnTypeName());
			ps.setString(5,datos.get_columnLabel());
			ps.setString(6,datos.get_columnName());		
			ps.setString(7,datos.get_tipogrid());	
			ps.setString(8,datos.get_tipoText());	
			ps.setString(9,datos.get_tipo());	
			ps.executeUpdate();
			estado=true;
			
		}catch(Exception ex){
			estado=false;
			
			System.out.println(ex);
		}
		return true;
	}
	
	public String selectOntologiaData(ontologiaDatos datos) throws SQLException{
		java.sql.Connection connection = null;
		java.sql.PreparedStatement ps=null;
		java.sql.ResultSet rs=null;
		String resultado="";
		String sql="SELECT tipogrid,text,tipo FROM ontologiadatos where TableName=? and ColumnType=? and ColumnDisplaySize=? and ColumnTypeName=? and ColumnLabel=? and ColumnName=?;";
		try{
			if(connection==null || connection.isClosed()){
				connection=Connection.getConnection();
			}
			
			ps =connection.prepareStatement(sql);
			ps.setString(1, datos.get_tablename());
			ps.setInt(2, datos.get_columnType());
			ps.setInt(3, datos.get_columnDisplaySize());
			ps.setString(4, datos.get_columnTypeName());
			ps.setString(5,datos.get_columnLabel());
			ps.setString(6,datos.get_columnName());		
			
			rs=ps.executeQuery();
			
			
			while(rs.next()){
				String tipogrid = rs.getString("tipogrid");
				String text=rs.getString("text");
				String tipo=rs.getString("tipo");
				resultado =datos.get_columnName()+";{\"tipokendo\":\""+tipogrid+"\",\"text\":\""+text+"\",\"tipo\":\""+tipo+"\"}";
			}
			
		}catch(Exception ex){
			System.out.println(ex);
		}
		return resultado;
	}
	
}
