package com.polus.core.dbengine;

public class TransactionStatement {

	private static final String GENERIC_SQL_FILE ="genericSQL.properties";
	private static final String MYSQL_SQL_FILE ="mysqlSQL.properties";
	private static final String ORACLE_SQL_FILE = "oracleSQL.properties";

	private TransactionStatement() {
		super();
	}

	public static synchronized String getSQL(String key,String dbName) throws Exception {		
		key  = key.toLowerCase();
		String sql = ReadPropertyFile.getProperty(key, GENERIC_SQL_FILE);
		if(sql == null){
			String fileName =  getFileName(dbName);
			sql = ReadPropertyFile.getProperty(key, fileName);	
		}		
		if(sql == null){
			throw new Exception("No key found");
		}		
		return sql;
		
	}

	private static String getFileName(String dbName){
		if("mysql".equalsIgnoreCase(dbName)){
			return MYSQL_SQL_FILE;			
			
		}else if("oracle".equalsIgnoreCase(dbName)){
			return ORACLE_SQL_FILE;			
		}		
		return GENERIC_SQL_FILE;
	}

}
