package com.polus.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.polus.core.codetable.dto.CodeTable;
import com.polus.core.codetable.dto.CodeTableDatabus;
import com.polus.core.codetable.dto.Fields;
import com.polus.core.security.AuthenticatedUser;
//import com.polus.fibicomp.correspondence.dto.CorrespondenceDataBus;
//import com.polus.fibicomp.negotiation.dto.NegotiationDataBus;

public class QueryBuilder {
	protected static Logger logger = LogManager.getLogger(QueryBuilder.class.getName());
	
	private static final  String SELECT_QUERY = "SELECT <<columnList>> FROM <<dbTableName>> ORDER BY t1.UPDATE_TIMESTAMP DESC";
	private static final  String INSERT_INTO = "INSERT INTO ";
	
	public static String selectQuery(String dbTableName, String columnList) {
		String query = replace(SELECT_QUERY,"<<columnList>>",columnList);
		 	   query = replace(query,"<<dbTableName>>",dbTableName);
		return query;
	}
	
	private static String replace(String original, String oldString,String newString) {		
		return original.replace(oldString, newString);
	}

	public static String updateQuery(HashMap<String, Object> changedMap, HashMap<String, Object> primaryKeyMap,
			CodeTableDatabus codeTableDatabus) throws Exception {
		String sqlScript = null;
		String part1 = "UPDATE "+codeTableDatabus.getCodeTable().getDatabaseTableName()+ " SET ";
		String part4 = getUpdateTimeStampAndUser();
		String part2 = getModifiedParamSql(changedMap,codeTableDatabus);
		String part3 = getPrimaryKeyParamSql(primaryKeyMap,codeTableDatabus);
		sqlScript = part1 + part4 +  part2 + part3;
		return sqlScript;
	}

	private static String getPrimaryKeyParamSql(HashMap<String, Object> primaryKeyMap,CodeTableDatabus codeTableDatabus) {
		String part3 = "";
		Set<String> primaryKeySet = primaryKeyMap.keySet();
		for(String key : primaryKeySet){
			int lastElement = primaryKeySet.size();
			part3 =  part3 + key + " = ";
			part3 = part3 +"<<"+key+">>";
			int index = new ArrayList<>(primaryKeySet).indexOf(key);
			if(lastElement  != index+1){
				part3 = part3 +" AND ";
			}
		}
		return part3;
	}

	private static String getPrimaryKeySql(HashMap<String, Object> primaryKeyMap, CodeTableDatabus codeTableDatabus) {
		StringBuilder part3 = new StringBuilder();
		Set<String> primaryKeySet = primaryKeyMap.keySet();
		for (String key : primaryKeySet) {
			int lastElement = primaryKeySet.size();
			String datatype = getColumnDataType(codeTableDatabus.getCodeTable(), key);
			part3.append(key + " = ");
			if ((datatype != null) && (datatype.equalsIgnoreCase("String") || datatype.equalsIgnoreCase("Date"))) {
				part3.append( "\'" + primaryKeyMap.get(key) + "\'");
			} else {
				part3.append(primaryKeyMap.get(key));
			}
			int index = new ArrayList<>(primaryKeySet).indexOf(key);
			if (lastElement != index + 1) {
				part3.append(" AND ");
			}
		}
		return part3.toString();
	}


	private static String getModifiedParamSql(HashMap<String, Object> changedMap, CodeTableDatabus codeTableDatabus) {
		String part2 = "";
		Set<String> changedColumnSet = changedMap.keySet();
		for(String changedColumn: changedColumnSet){
			int lastElement = changedColumnSet.size();
			part2 = part2 + changedColumn + " = " ;
			part2 = part2 +"<<"+changedColumn+">>";
			int index = new ArrayList<>(changedColumnSet).indexOf(changedColumn);
			if(lastElement  != index+1){
				part2 = part2 + ",";
			}
		}
		part2 = part2+" WHERE ";
		return part2;
	}

	private static String getUpdateTimeStampAndUser() {
		return "UPDATE_USER ='" + AuthenticatedUser.getLoginUserName() + "',UPDATE_TIMESTAMP = UTC_TIMESTAMP(),";
	}

	public static String getColumnDataType(CodeTable codetable,String changedColumn) {
		String dataType = null;
		for(Fields field : codetable.getFields()){
			if(field.getColumnName().equalsIgnoreCase(changedColumn)){
				dataType =field.getDataType();
				break;
			}
		}
		return dataType;
	}

	public static String deleteQuery(HashMap<String, Object> changedMap, HashMap<String, Object> primaryKeyMap,
			CodeTableDatabus codeTableDatabus) {
		String sqlScript = null;
		String part1 = "DELETE FROM "+codeTableDatabus.getCodeTable().getDatabaseTableName()+ " WHERE ";
		String part2 = getPrimaryKeySql(primaryKeyMap,codeTableDatabus);
		sqlScript = part1 + part2;
		return sqlScript;
	}

	public static String selectMaxEntry(CodeTable codeTable, String primaryKey) {
		String sqlScript = "SELECT MAX(";
		String datatype = getColumnDataType(codeTable,primaryKey);	
		if(datatype.equalsIgnoreCase("String")){
			sqlScript = sqlScript +"CAST("+primaryKey+" AS SIGNED)";
		}else{
			sqlScript = sqlScript + primaryKey;
		}
		sqlScript = sqlScript+") as max_data FROM "+codeTable.getDatabaseTableName() ;
		return sqlScript;
	}

	public static String insertQuery(HashMap<String, Object> changedMap, CodeTableDatabus codeTableDatabus) {
		String sqlScript = null;
		String part1 = INSERT_INTO + codeTableDatabus.getCodeTable().getDatabaseTableName() + " (";
		String part2 = getTableColumnQuery(codeTableDatabus.getCodeTable());
		String part3 = getColumnValueForParams(codeTableDatabus.getCodeTable());
		sqlScript = part1 + part2 + ",UPDATE_TIMESTAMP,UPDATE_USER) VALUES (" + part3 + ",UTC_TIMESTAMP(),'"+ AuthenticatedUser.getLoginUserName() +"')";
		return sqlScript;
	}

	private static String getColumnValueForParams(CodeTable codetable) {
		String ColumnValueForParams = "";
			for(Fields field: codetable.getFields()){
				int lastElement = codetable.getFields().size();
				
				ColumnValueForParams = ColumnValueForParams +"<<"+field.getColumnName()+">>";
				int index = codetable.getFields().indexOf(field);
				if(lastElement  != index+1){
					ColumnValueForParams = ColumnValueForParams + ",";
				}
			}

		return ColumnValueForParams;
	}

	private static String getTableColumnQuery(CodeTable codetable) {
		String tablecolumns ="";
		for(Fields fields: codetable.getFields()){
			int lastElement = codetable.getFields().size();
			tablecolumns = tablecolumns + fields.getColumnName();
			int index = codetable.getFields().indexOf(fields);
			if(lastElement  != index+1){
				tablecolumns = tablecolumns + ",";
			}
		}
		return tablecolumns;
	}

	public static String selectQueryForAttachment(String attachmentColumn, HashMap<String, Object> primaryKeyMap,
			CodeTableDatabus codeTableDatabus) {
		String query = null;
		String part1  = "SELECT "+attachmentColumn+" FROM "+codeTableDatabus.getCodeTable().getDatabaseTableName()+ " WHERE ";
		String part2 = getPrimaryKeySql(primaryKeyMap,codeTableDatabus);
		query = part1 + part2 ;
		return query;
	}




//
//	public static String selectLetterTypeCode(CorrespondenceDataBus correspondenceDataBus) {
//		String sqlScript = "";
//		try{
//			String part1 = "SELECT LETTER_TEMPLATE_TYPE_CODE FROM IRB_ACTION_LETTER_TEMPLATE WHERE ";
//			String part2 = "ACTION_TYPE_CODE = "+ correspondenceDataBus.getActionCode();
//			sqlScript = part1+part2;
//		}catch(Exception e){
//			logger.error("Exception in selectLetterTypeCode"+ e.getMessage());
//		}
//		return sqlScript;
//	}

	public static String selectLetterTemplate(String templateTypeCode) {
		String sqlScript = "";
		try{
			String part1 = "SELECT CORRESPONDENCE_TEMPLATE FROM LETTER_TEMPLATE_TYPE WHERE ";
			String part2 = "LETTER_TEMPLATE_TYPE_CODE = " +"\'"+ templateTypeCode +"\'";
			sqlScript = part1+part2;
		}catch(Exception e){
			logger.error("Exception in selectLetterTypeCode"+ e.getMessage());
		}
		return sqlScript;
	}
	public static String selectQueryForMap(String dbTableName, String columnList) {
		String query  = "SELECT "+columnList+" AS RVALUE FROM "+dbTableName ;
		return query;
	}
	
	public static String selectQueryForCodetable(String dbTableName,String columnCode, String columnDesc) {		
		String query  = "SELECT "+columnCode+" AS RVALUE, "+columnDesc+" AS DESCRIPTION "+ " FROM "+dbTableName ;
		return query;
	}
	
	
	public static String selectQueryForCodetableSelectedColumn(String dbTableName,String columnCode, String columnDesc,String whereClauseValue) {		
		String query  = "SELECT "+columnCode+" AS RVALUE, "+columnDesc+" AS DESCRIPTION "+
						" FROM "+dbTableName+ " WHERE "+columnCode+ " = '"+whereClauseValue+"' ";
		return query;
	}
	
	public static String selectScoringCriteriaCalculationsForMAinPanel(Integer grantCallId) {
		String query = null;
		try {
			query = "SELECT T1.PROPOSAL_ID,IFNULL(SUM(T4.SCORE)/COUNT(*),'0') AS AVERAGE_SCORE\r\n" + 
					"FROM EPS_PROPOSAL T1\r\n" + 
					"LEFT JOIN WORKFLOW T2 ON T1.PROPOSAL_ID = T2.MODULE_ITEM_ID AND T2.MODULE_CODE = 3 AND T2.IS_WORKFLOW_ACTIVE = 'Y'\r\n" + 
					"LEFT JOIN WORKFLOW_DETAIL T3 ON T3.WORKFLOW_ID = T2.WORKFLOW_ID\r\n" + 
					"LEFT JOIN WORKFLOW_REVIEWER_SCORE T4 ON T4.WORKFLOW_DETAIL_ID = T3.WORKFLOW_DETAIL_ID \r\n" + 
					"WHERE  T1.GRANT_HEADER_ID = "+grantCallId+" AND T1.STATUS_CODE IN(38,11,40,29)\r\n" + 
					"AND T4.SCORE IS NOT NULL\r\n" + 
					"GROUP BY T2.WORKFLOW_ID";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return query;
	}

	public static String selectAgreementTemplate(String templateTypeCode) {
		String sqlScript = "";
		try {
			String part1 = "SELECT TEMPLATE FROM AGREEMENT_TYPE_TEMPLATE WHERE ";
			String part2 = "TEMPLATE_ID = " + "\'" + templateTypeCode + "\'";
			sqlScript = part1 + part2;
		} catch (Exception e) {
			logger.error("Exception in selectAgreementTemplate: {} " , e.getMessage());
		}
		return sqlScript;
	}

	public static String selectFileNameFromLetterTemplate(String templateTypeCode) {
		String sqlScript = "";
		try{
			String part1 = "SELECT FILE_NAME,PRINT_FILE_TYPE FROM LETTER_TEMPLATE_TYPE WHERE ";
			String part2 = "LETTER_TEMPLATE_TYPE_CODE = " +"\'"+ templateTypeCode +"\'";
			sqlScript = part1+part2;
		}catch(Exception e){
			logger.error("Exception in selectFileNameFromLetterTemplate"+ e.getMessage());
		}
		return sqlScript;
	}
}
