package com.uta.edu.ec.ontologia;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.io.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.sparql.resultset.ResultsFormat;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.PrintUtil;
import com.hp.hpl.jena.util.iterator.Filter;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.OWL2;
import com.hp.hpl.jena.vocabulary.RDF;
import com.sun.jersey.spi.resource.Singleton;
import com.sun.org.apache.xml.internal.utils.NSInfo;
import com.uta.edu.ec.connection.*;
import com.uta.edu.ec.dml.ontologiaGeneral;
import com.uta.edu.ec.data.*;



@Singleton
@Path("/prueba")
public class HolaMundo {
	
	@GET
	@Path("/hola")
	@Produces(MediaType.APPLICATION_JSON)
	
	public String hola(@Context UriInfo info){
		String json="";
		/*if(getParameters(info).size()>0) {
			json = "{\"name\":\""+getParameters(info).get(0).get_Value().toString()+"\",\"food\":\"pizza\",\"quantity\":3}";
		
	    }else {*/
	    	 json = "{\"name\":\"vacio\",\"food\":\"pizza\",\"quantity\":3}";
			
	    //}
		return json;
	}
	
	@GET
	@Path("/sera")
	@Produces(MediaType.APPLICATION_JSON)
	public String  sera(@Context UriInfo info){
		String from = info.getQueryParameters().getFirst("recurso");
		OutputStream os = new java.io.ByteArrayOutputStream();
		
		
		Model schema = FileManager.get().loadModel("/root/eclipse-workspace/file/viewonto.owl");
		Model data = FileManager.get().loadModel("/root/eclipse-workspace/file/viewontowww.abox.owl");
		Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
		reasoner = reasoner.bindSchema(schema);
		InfModel infmodel = ModelFactory.createInfModel(reasoner, data);
		
		
		//System.out.println(getResourceTypeProperty(data,"id"));
		
		
		Resource nForce = infmodel.getResource(infmodel.getNsPrefixMap().get("")+getResourceByTitle(infmodel,from));
		
		return printStatements(nForce,infmodel,data,getResourceByTitle(infmodel,from),info);
	}
	
	@GET
	@Path("/getMenu")
	@Produces(MediaType.APPLICATION_JSON)
	public String  getMenu(){
		Model data = FileManager.get().loadModel("/root/eclipse-workspace/file/viewontowww.abox.owl");
		return getMenu(data);
	}
	
	public static String getMenu(Model m) {
		String resource="{\"Menu\":[";
		String queryString =
			    "PREFIX getTitle:<"+m.getNsPrefixMap().get("OntoView")+">"	
			    +"PREFIX getResource:<"+m.getNsPrefixMap().get("")+">"	
			    +"PREFIX getResourceowl:<"+m.getNsPrefixMap().get("owl")+">"		
			    + "SELECT  ?s   WHERE { ?s ?o getTitle:View . }"
			    + "ORDER BY ?s";	
			Query query = QueryFactory.create(queryString);
			QueryExecution qexec = QueryExecutionFactory.create(query, m);
			try {
			    ResultSet results = qexec.execSelect();
			    while(results.hasNext()) {
			        QuerySolution soln = results.nextSolution();			       
			        RDFNode subj1 = soln.get("s");
			        resource+="\""+getTitleByResource(m,subj1.asNode().getLocalName())+"\",";
			    }
			} finally {
			    qexec.close();
			}
			return resource=resource.trim().substring(0, resource.trim().length() - 1)+"]}";
	}
	
	public static String getResourceByTitle(Model m,String title) {
		String resource="";
		String queryString =
			    "PREFIX getTitle:<"+m.getNsPrefixMap().get("OntoView")+">"			 
			    + "SELECT ?x WHERE { ?x getTitle:title  \""+title+"\" . }";

			Query query = QueryFactory.create(queryString);
			QueryExecution qexec = QueryExecutionFactory.create(query, m);
			try {
			    ResultSet results = qexec.execSelect();
			    //while(results.hasNext()) {
			    	
			        QuerySolution soln = results.nextSolution();
			        
			        //System.out.println(soln);
			        
			        RDFNode subj = soln.get( "x" );
				    //System.out.println("Subject: "+subj.asNode().getLocalName());
				    //System.out.println(soln.toString());
			        resource=subj.asNode().getLocalName();
			    //}
			} finally {
			    qexec.close();
			}
			return resource;
	}
	
	public static String getTitleByResource(Model m,String title) {
		String resource="";
		String queryString =
			    "PREFIX getTitle:<"+m.getNsPrefixMap().get("OntoView")+">"	
			    +"PREFIX getResource:<"+m.getNsPrefixMap().get("")+">"		
			    + "SELECT  distinct ?x WHERE { getResource:"+title+" getTitle:title ?x . }";

			Query query = QueryFactory.create(queryString);
			QueryExecution qexec = QueryExecutionFactory.create(query, m);
			try {
			    ResultSet results = qexec.execSelect();
			    while(results.hasNext()) {
			        QuerySolution soln = results.nextSolution();
			        RDFNode subj = soln.get( "x" );
				    resource=subj.toString();
			    }
			} finally {
			    qexec.close();
			}
			return resource;
	}
	
	
	public static String getResourceTypeProperty(Model m,String predicate) {
		String resource="{";
		
		String queryString =
			    "PREFIX getTitle:<"+m.getNsPrefixMap().get("OntoView")+">"	
			    +"PREFIX getResource:<"+m.getNsPrefixMap().get("")+">"	
			    +"PREFIX getResourceowl:<"+m.getNsPrefixMap().get("owl")+">"		
			    + "SELECT ?o ?x WHERE { getResource:"+predicate+" ?x  ?o ."
			    		+ "	FILTER(?o NOT IN (getTitle:Descriptor, getTitle:NamedIndividual,getResourceowl:NamedIndividual))		 }";

			Query query = QueryFactory.create(queryString);
			QueryExecution qexec = QueryExecutionFactory.create(query, m);
			try {
			    ResultSet results = qexec.execSelect();
			    while(results.hasNext()) {
			        QuerySolution soln = results.nextSolution();
			        RDFNode subj = soln.get("x");
			        RDFNode subj1 = soln.get("o");
			        
			        if(subj1.isLiteral()) {
			        	resource+="\""+subj.asNode().getLocalName()+"\":\""+subj1.toString()+"\",";
			        }else {
			        	resource+="\""+subj.asNode().getLocalName()+"\":\""+subj1.asNode().getLocalName()+"\",";
			        }
			        
			    }
			} finally {
			    qexec.close();
			}
			return resource=resource.trim().substring(0, resource.trim().length() - 1)+"}";
	}
	
	
	
	public static Vector<String> ListPredicate(Resource s){
		StmtIterator iter= s.listProperties();
		Vector<String> predicatelist = new Vector<String>();
		int in=0;
		while(iter.hasNext()){
			Statement stmt = iter.nextStatement();
			Property predicate= stmt.getPredicate();
			
			//System.out.println(predicate.toString());
			
			if(predicatelist.size()==0){
				predicatelist.add(predicate.toString().split("#")[1]);
			}else{
				in=0;
				for(int i=0; i<predicatelist.size(); i++){
					if(predicatelist.elementAt(i).equals(predicate.toString().split("#")[1])){
						in=1;
						break;
					}
				}
				if(in!=1){
					predicatelist.add(predicate.toString().split("#")[1]);
				}
			}
			
		}
		
		return predicatelist;
	}
	
	public static String  printStatements(Resource s,Model m,Model data,String resource,@Context UriInfo info)  {
		StmtIterator iter= s.listProperties();
		String recursoGenerado="";
		
		String jsonreturn="{";
		//para ver si existe mas de un recurso en la inferencia
		String subjectactual="";
		String subjectsiguinete="recurso";
		boolean mapped=false;
		
		//para ver las relaciones objetos y atributos del recurso
		
		String predicateactual="";
		String predicatesiguiente="predicate";
		
		Vector<String> fillListPredicate=ListPredicate(s);
		Vector<String> finalPredicate = ListPredicate(s);
		String cadena="";
		ArrayList<String> resultado=null;
		
		for(int i=0; i<finalPredicate.size(); i++){
            finalPredicate.set(i, "");
        }
		
		while(iter.hasNext()){

			Statement stmt = iter.nextStatement();
			Resource subject= stmt.getSubject();
			Property predicate= stmt.getPredicate();
			RDFNode object=stmt.getObject();
			
			//System.out.println(stmt);
			//System.out.println("sssssssssss");
			
		
			subjectactual=subject.toString();
			
			if(!subjectactual.equals(subjectsiguinete)){
				jsonreturn+="\""+subject.toString().split("#")[1]+"\":";
				recursoGenerado=subject.toString().split("#")[1];
			}
			subjectsiguinete=subject.toString();
			
			for(int i=0; i<fillListPredicate.size(); i++){
				
				if(fillListPredicate.elementAt(i).equals(predicate.toString().split("#")[1])){
					
					
					try{
						if(object instanceof Resource){
							cadena=finalPredicate.elementAt(i).trim()+"\""+object.toString().split("#")[1]+"\":\""+object.toString().split("#")[1]+"\",";
							finalPredicate.set(i, cadena);
							
						}else{
							if(fillListPredicate.elementAt(i).trim().equals("mappedTo")){
								cadena=finalPredicate.elementAt(i).trim()+"\"SQL\":\""+object.toString()+"\",";
								
								ontologiaGeneral con= new ontologiaGeneral();
								resultado = con.select(object.toString(),getParameters(info,m));
								
								mapped=true;
								
								cadena+=finalPredicate.elementAt(i).trim()+"\"Data\":\"\",";
								finalPredicate.set(i, cadena);
							}else{
								cadena=finalPredicate.elementAt(i).trim()+"\""+object.toString()+"\":\""+object.toString()+"\",";
								finalPredicate.set(i, cadena);
								
							}
						}
					}
					catch(Exception ex){
						
					}
				
					break;
				}
	        }
		}
		jsonreturn+="{";
		for(int i=0; i<fillListPredicate.size(); i++){  
			
			
			if(fillListPredicate.elementAt(i).trim().equals("title")){
				jsonreturn+="\""+fillListPredicate.elementAt(i).trim()+"\":{"+"\"name\":"+finalPredicate.elementAt(i).trim().substring(0, finalPredicate.elementAt(i).trim().length() - 1).split(":")[1]+"},";
				
			}else{
				jsonreturn+="\""+fillListPredicate.elementAt(i).trim()+"\":{"+finalPredicate.elementAt(i).trim().substring(0, finalPredicate.elementAt(i).trim().length() - 1)+"},";
			}
			
        }
		
		jsonreturn=jsonreturn.trim().substring(0, jsonreturn.length()-1);
		
		if(jsonreturn.trim().length()==1){
			
			return jsonreturn="{\"recurso\":\"vacio\"}";
		}else{
			jsonreturn=jsonreturn+"}}";
			
			if(mapped!=false){
				try {
					JSONObject jsonObj = new JSONObject(jsonreturn);
					
					//System.out.print(jsonObj);
					
					JSONObject jsonObjtemporal=null;
					/*for(int i=1;i<resultado.size();i++){
						String [] cadenajsontemp=resultado.get(i).toString().split(";");
						jsonObjtemporal = new JSONObject(cadenajsontemp[1]);
						
						//System.out.println(cadenajsontemp[0]);
						
						//jsonObj.getJSONObject(recursoGenerado).getJSONObject("isDescribedBy").put(cadenajsontemp[0], jsonObjtemporal);
						
					}*/
					
					
					Iterator<?> getDataTypeDataItem=null;
					
					getDataTypeDataItem=jsonObj.getJSONObject(recursoGenerado).getJSONObject("isDescribedBy").keys();
					
					while(getDataTypeDataItem.hasNext()){
						String idDataType=getDataTypeDataItem.next().toString();
						jsonObjtemporal = new JSONObject(getResourceTypeProperty(data,idDataType));
						jsonObj.getJSONObject(recursoGenerado).getJSONObject("isDescribedBy").put(idDataType, jsonObjtemporal);
						
					}
					
					JSONArray jsonObjtemporalarray = new JSONArray (resultado.get(0));
					jsonObj.getJSONObject(recursoGenerado).getJSONObject("mappedTo").put("Data", jsonObjtemporalarray);
					
					
					JSONObject jsonObjParametetros = new JSONObject(getParameterIsRelatedBy(m,resource/*,jsonObj.getJSONObject(recursoGenerado).getJSONObject("isRelatedBy")*/));
					
					if(jsonObjParametetros.length()>0)
						jsonObj.getJSONObject(recursoGenerado).put("isRelatedBy",jsonObjParametetros);
									
					jsonreturn=jsonObj.toString();
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				
				JSONObject jsonObj;
				Iterator<?> getDataViewRelatedTo=null;
				try {
					jsonObj = new JSONObject(jsonreturn);
					
					if(jsonObj.getJSONObject(recursoGenerado).has("viewRelatedTo")) {
						
					
						getDataViewRelatedTo=jsonObj.getJSONObject(recursoGenerado).getJSONObject("viewRelatedTo").keys();
						
						while(getDataViewRelatedTo.hasNext()){
							
							Resource nForce = m.getResource(m.getNsPrefixMap().get("")+getDataViewRelatedTo.next().toString());
							Property mappedTo = m.getProperty( m.getNsPrefixMap().get("OntoView")+"mappedTo");
							Property isDescribedBy = m.getProperty( m.getNsPrefixMap().get("OntoView")+"isDescribedBy");
							
							
							StmtIterator stmtsmappedTo = nForce.listProperties(isDescribedBy);
							String jsonMappedRelateby="{";
							
							while ( stmtsmappedTo.hasNext() ) {
					        	Statement stmtisDescribedBy = stmtsmappedTo.nextStatement();
								RDFNode object=stmtisDescribedBy.getObject();
								jsonMappedRelateby+="\""+ object.asNode().getLocalName()+"\":\""+object.asNode().getLocalName()+"\",";
					        }
							jsonMappedRelateby= jsonMappedRelateby.trim().substring(0,jsonMappedRelateby.trim().length()-1)+"}";
							
							JSONObject jsonObjParametetros=null;
							if(jsonMappedRelateby.trim().length()==1)
								jsonObjParametetros =  new JSONObject();
							else
								jsonObjParametetros =  new JSONObject(jsonMappedRelateby);
				     			
				     			
					        jsonObj.getJSONObject(recursoGenerado).put("isDescribedBy", jsonObjParametetros);
					        
					        stmtsmappedTo = nForce.listProperties(mappedTo);
					       
					        
					        while ( stmtsmappedTo.hasNext() ) {
					        	Statement stmtmappedTo = stmtsmappedTo.nextStatement();
								RDFNode object=stmtmappedTo.getObject();
								System.out.println(object.toString());
						        ontologiaGeneral con= new ontologiaGeneral();
						        
						        System.out.println(jsonObj.getJSONObject(recursoGenerado).getJSONObject("isDescribedBy"));
						        
								resultado = con.select(object.toString(),getParameters(info,m));
								jsonMappedRelateby= "{\"SQL\":\""+object.toString()+"\",\"Data\":\"\"}}";
								jsonObjParametetros =  new JSONObject(jsonMappedRelateby);
								jsonObj.getJSONObject(recursoGenerado).put("mappedTo",jsonObjParametetros );
						        
								
								JSONObject jsonObjtemporal=null;
								/*for(int i=1;i<resultado.size();i++){
									String [] cadenajsontemp=resultado.get(i).toString().split(";");
									jsonObjtemporal = new JSONObject(cadenajsontemp[1]);
									jsonObj.getJSONObject(recursoGenerado).getJSONObject("isDescribedBy").put(cadenajsontemp[0], jsonObjtemporal);
									
								}*/
								
								Iterator<?> getDataTypeDataItem=null;
								
								getDataTypeDataItem=jsonObj.getJSONObject(recursoGenerado).getJSONObject("isDescribedBy").keys();
								
								while(getDataTypeDataItem.hasNext()){
									String idDataType=getDataTypeDataItem.next().toString();
									jsonObjtemporal = new JSONObject(getResourceTypeProperty(data,idDataType));
									jsonObj.getJSONObject(recursoGenerado).getJSONObject("isDescribedBy").put(idDataType, jsonObjtemporal);
									
								}
								
								JSONArray jsonObjtemporalarray = new JSONArray (resultado.get(0));
								jsonObj.getJSONObject(recursoGenerado).getJSONObject("mappedTo").put("Data", jsonObjtemporalarray);
								
								jsonObjParametetros = new JSONObject(getParameterIsRelatedBy(m,resource/*,jsonObj.getJSONObject(recursoGenerado).getJSONObject("isRelatedBy")*/));
								
								if(jsonObjParametetros.length()>0)
									jsonObj.getJSONObject(recursoGenerado).put("isRelatedBy",jsonObjParametetros);
								
					        }
		            	}
					}
					jsonreturn=jsonObj.toString();
					
				} catch (JSONException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
			}
			
			return jsonreturn;
		}
		
	}
	
	
	
	public static ontologiaParameterMappedTo getConditionSql(Model m,String source,String destination) {
		ontologiaParameterMappedTo sqlCon=new ontologiaParameterMappedTo();
		String resourceSource="";
		String resourceTarge="";
		String sqlCondition="";
		ResIterator axioms = m.listSubjectsWithProperty(RDF.type, OWL2.Axiom);
		while ( axioms.hasNext() ) {
        	Resource axiom = axioms.next();
            StmtIterator stmts = axiom.listProperties();
            resourceSource="";
            resourceTarge="";
            sqlCondition="";
            while ( stmts.hasNext() ) {
            	
        	    Statement stmt      = stmts.nextStatement();  // get next statement
        	    Property  predicate = stmt.getPredicate();   // get the predicate
        	    RDFNode   object    = stmt.getObject();      // get the object
        	    
        	    if(predicate.toString().split("#")[1].equals("sqlCond")) {
        	    	sqlCondition=object.toString();
        	    	
        	    }
        	    
        	    if(predicate.toString().split("#")[1].equals("annotatedSource")) {
	            	   
        	    	if (object instanceof Resource) {
        	    		resourceSource=object.toString().split("#")[1];
            	    } 
        	    }
        	    if(predicate.toString().split("#")[1].equals("annotatedTarget")) {
	            	   
        	    	if (object instanceof Resource) {
        	    		resourceTarge=object.toString().split("#")[1];
            	    } 
        	    }
        	    try {
            	    if(object.toString().split("#")[1].equals("Axiom")) {
            	    	break;
            	    }
        	    }catch(Exception e) {}
        	    
        	    
            }
            
            if(resourceSource.equals(source) && resourceTarge.equals(destination)) {
            	
            	sqlCon.set_Tipo("condition");
            	sqlCon.set_Value(sqlCondition);
            	
            }
            
            
		}
		
		return sqlCon;
	}
	
	public static String getParameterIsRelatedBy(Model m,String resource/*,JSONObject relatedBy*/) {
	    
		String sqlCon="",link="",resourceRelated="",jsonParameter="",relatedBys="";
		ResIterator axioms = m.listSubjectsWithProperty(RDF.type, OWL2.Axiom);
		Iterator<?> dataJsonOriginalRelateBy=null;
		Iterator<?> dataJsonOriginalRelateByLast=null;
		
        while ( axioms.hasNext() ) {
        	Resource axiom = axioms.next();
            StmtIterator stmts = axiom.listProperties();
            sqlCon="[";
            link="";
            relatedBys="";
            resourceRelated="";
            
            while ( stmts.hasNext() ) {
            	
            	    Statement stmt      = stmts.nextStatement();  // get next statement
            	    Resource  subject   = stmt.getSubject();     // get the subject
            	    Property  predicate = stmt.getPredicate();   // get the predicate
            	    RDFNode   object    = stmt.getObject();      // get the object
            	    
            	    
            	    
            	    if(predicate.toString().split("#")[1].equals("annotatedTarget")) {
	            	    if (object instanceof Resource) {
	            	    	//System.out.println(object.toString());
	            	    	relatedBys=object.toString().split("#")[1];
	            	    	link+=object.toString().split("#")[1];
	            	    } 
            	    }
            	    
            	    if(predicate.toString().split("#")[1].equals("parameter")) {
	            	    
            	    	if (object instanceof Resource) {
	            	       // System.out.println(object.toString());
            	    		//getTitleByResource(infmodel,from)
            	    		sqlCon+="{\"parameter\":\""+object.toString().split("#")[1]+"\","+"\"value\":\"\"},";
	            	    } 
            	    }
            	    
            	    if(predicate.toString().split("#")[1].equals("annotatedSource")) {
	            	   
            	    	if (object instanceof Resource) {
            	    		//System.out.println(object.toString());
            	    		resourceRelated=object.toString().split("#")[1];
	            	    } 
            	    }
            	    try {
	            	    if(object.toString().split("#")[1].equals("Axiom")) {
	            	    	break;
	            	    }
            	    }catch(Exception e) {}
            	    
            }
            
            if(resourceRelated.equals(resource)) {
            	
            	//System.out.println(link);
	    		//getTitleByResource(m,from)
            	jsonParameter+="\""+getTitleByResource(m,link)+"\":"+sqlCon.substring(0, sqlCon.length() - 1)+"],";
            }/*else {
            	dataJsonOriginalRelateBy=relatedBy.keys();
            	
            	
            	while(dataJsonOriginalRelateBy.hasNext()){
            		if(dataJsonOriginalRelateBy.next().toString().equals(relatedBys)) {
            			jsonParameter+="\""+link+"\":"+sqlCon.substring(0, sqlCon.length() - 1)+"],";
            			System.out.println(jsonParameter);
            		}
            	}
            }*/
            
        }
       
        
        if(jsonParameter.length()>0) {
        	/*JSONObject jsonObjParametetros = null;
     		try {
     			jsonObjParametetros = new JSONObject("{"+jsonParameter.substring(0, jsonParameter.length() - 1)+"}");
     			dataJsonOriginalRelateBy=relatedBy.keys();
     			
     			while(dataJsonOriginalRelateBy.hasNext()){
     				String data=dataJsonOriginalRelateBy.next().toString();
     				boolean condition=false;
     				dataJsonOriginalRelateByLast=jsonObjParametetros.keys();
    				while(dataJsonOriginalRelateByLast.hasNext()){
                		if(data.equals(dataJsonOriginalRelateByLast.next().toString())) {
                			condition=true;
                		}
                	}
                	
            		if(condition==false) {
            			//jsonParameter+="\""+data+"\":[],";
            		}
            	}
     			
     		} catch (JSONException e) {
     			// TODO Auto-generated catch block
     			e.printStackTrace();
     		}
            dataJsonOriginalRelateBy=jsonObjParametetros.keys();*/
             
        	return "{"+jsonParameter.substring(0, jsonParameter.length() - 1)+"}";
        }
        else
        	return "{}";
	}
	
	public static ArrayList<ontologiaParameterMappedTo> getParameters(@Context UriInfo info,Model m){
		MultivaluedMap<String, String> queryParams=info.getQueryParameters();
		ArrayList<ontologiaParameterMappedTo> parametrosMappedTo = new ArrayList<ontologiaParameterMappedTo>();
		
		String  source= info.getQueryParameters().getFirst("source");
		
		
		if(source!=null) {
			String  destination= info.getQueryParameters().getFirst("recurso");
			System.out.println(destination);
			//System.out.println(getResourceByTitle(m,destination));
			
			parametrosMappedTo.add(getConditionSql(m,getResourceByTitle(m,source),getResourceByTitle(m,destination)));
		}
		
		if (null != queryParams) {
	        Set<Entry<String, List<String>>> entries = queryParams.entrySet();
	        Iterator<Entry<String, List<String>>> iter = entries.iterator();
	        while (iter.hasNext()) {
	            Map.Entry<String, List<String>> entry = (Entry<String, List<String>>) iter.next();
	            String key = entry.getKey();
	            if (!parametrosMappedTo.contains(key)) {
	                if(!key.equals("recurso") && !key.equals("source") ) {
		            	ontologiaParameterMappedTo parametroMappedTo = new ontologiaParameterMappedTo();	
		            	parametroMappedTo.set_Tipo(key);
		            	parametroMappedTo.set_Value(entry.getValue().get(0));
		            	parametrosMappedTo.add(parametroMappedTo);
	            	}
	            }
	        }
	     	
	    }
		return parametrosMappedTo;
	}
	
}
