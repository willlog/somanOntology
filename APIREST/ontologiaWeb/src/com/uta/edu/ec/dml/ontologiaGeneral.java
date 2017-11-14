package com.uta.edu.ec.dml;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.uta.edu.ec.connection.*;
import com.uta.edu.ec.data.*;

public class ontologiaGeneral {
	
	
	public static boolean isNumeric(String str) {
        return (str.matches("[+-]?\\d*(\\.\\d+)?") && str.equals("")==false);
    }
	
	public static ArrayList<String> select(String sql,ArrayList<ontologiaParameterMappedTo> params) throws SQLException{
		java.sql.Connection connection = null;
		java.sql.PreparedStatement ps=null;
		java.sql.ResultSet rs=null;
		String resultado="";
		int numparameter=1;
		if(params.size()>0) {
			for(int i=0; i<params.size(); i++){ 
				if(params.get(i).get_Tipo().toString().equals("condition")) {
					sql+=" "+params.get(i).get_Value().toString();
				}					
			}
		}
		System.out.println("sql"+sql);
		ArrayList<String> datoarray=new ArrayList();
		
		try{
			if(connection==null || connection.isClosed()){
				connection=Connection.getConnection();
			}
			
			ps =connection.prepareStatement(sql);
			if(params.size()>0) {
				numparameter=1;
				for(int i=0; i<params.size(); i++){ 
					if(!params.get(i).get_Tipo().toString().equals("condition")) {
						
						//ps.setInt(numparameter,Integer.parseInt((params.get(i).get_Value().toString())));
						
						if(isNumeric(params.get(i).get_Value()))						
							ps.setInt(numparameter,Integer.parseInt((params.get(i).get_Value().toString())));
						else
							ps.setString(numparameter,(params.get(i).get_Value().toString()));
						numparameter++;
					}					
				}
			}
			
			//System.out.println(ps);
			rs=ps.executeQuery();
			resultado=convertToJSON(rs).toString();
			
			datoarray.add(resultado);
			
		
			
			ResultSetMetaData rsmd=rs.getMetaData();
			
			
			//ontologiaDatos datos=new ontologiaDatos();
			//ontologiaDatosDml insert= new ontologiaDatosDml();
			
			/*for(int i=1;i<=rsmd.getColumnCount();i++){
				datos.set_tablename(rsmd.getTableName(i));
				datos.set_columnType(rsmd.getColumnType(i));
				datos.set_columnDisplaySize(rsmd.getColumnDisplaySize(i));
				datos.set_columnTypeName(rsmd.getColumnTypeName(i));
				datos.set_columnLabel(rsmd.getColumnLabel(i));
				datos.set_columnName(rsmd.getColumnName(i));
				datos.set_tipogrid("string");
				datos.set_tipoText(rsmd.getColumnLabel(i));
				datos.set_tipo("texto");
				resultado=insert.selectOntologiaData(datos);
				if(!resultado.equals("")){
					datoarray.add(resultado);
					
				}else{
					insert.insertOntologiaData(datos);
				}
				
			}*/
			
			
		}catch(Exception ex){
			resultado="\"\"";
			System.out.println(ex);
		}
		return datoarray;
	}
	
	public static JSONArray convertToJSON(java.sql.ResultSet resultSet)
            throws Exception {
        JSONArray jsonArray = new JSONArray();
        ResultSetMetaData rsmd = resultSet.getMetaData();
        String column_name="";
        String label_name="";
        
        while (resultSet.next()) {
            int total_rows = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 0; i < total_rows; i++) {
            	column_name = rsmd.getColumnName(i+1);
            	label_name=rsmd.getColumnLabel(i + 1);
            	
            	/*for Mysql
            	 * 4 int
            	 * 12 varchar
            	 * -1 longtext
            	 * 93 timestamp
            	 * 3 decimal
            	 */
            	
            	switch( rsmd.getColumnType( i+1 ) ) {
                
            	case java.sql.Types.BIGINT:
                  obj.put(label_name, resultSet.getInt(column_name));       break;
                case java.sql.Types.BOOLEAN:
                  obj.put(label_name, resultSet.getBoolean(column_name));   break;
                case java.sql.Types.BLOB:
                  obj.put(label_name, resultSet.getBlob(column_name));      break;
                case java.sql.Types.DOUBLE:
                  obj.put(label_name, resultSet.getDouble(column_name));    break;
                case java.sql.Types.FLOAT:
                  obj.put(label_name, resultSet.getFloat(column_name));     break;
                case java.sql.Types.INTEGER:
                  obj.put(label_name, resultSet.getInt(column_name));       break;
                case java.sql.Types.NVARCHAR:
                  obj.put(label_name, resultSet.getNString(column_name));   break;
                case java.sql.Types.VARCHAR:
                  obj.put(label_name, resultSet.getString(column_name));    break;
                case java.sql.Types.TINYINT:
                  obj.put(label_name, resultSet.getInt(column_name));       break;
                case java.sql.Types.SMALLINT:
                  obj.put(label_name, resultSet.getInt(column_name));       break;
                case java.sql.Types.DATE:
                  obj.put(label_name, resultSet.getDate(column_name));      break;
                case java.sql.Types.TIMESTAMP:
                	DateFormat df= new SimpleDateFormat("dd-MM-yyyy");
                	try {
                		obj.put(label_name, df.format(resultSet.getTimestamp(column_name)));
                	}catch(Exception e) {
                		
                	}
                	break;
                default:
                  obj.put(label_name, resultSet.getObject(column_name));    break;
              }
                //obj.put(resultSet.getMetaData().getColumnLabel(i + 1), resultSet.getObject(i + 1));
            }
          jsonArray.put(obj);
        }
        return jsonArray;
    }
}
