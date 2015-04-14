package com.ctb.prism.core.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.ctb.prism.core.util.CustomStringUtil;
import com.ctb.prism.core.util.Utils;

@Service("dynamoDBService")
public class DynamoDBServiceImpl implements IDynamoDBService {

	@Autowired
	DynamoDB amazonDynamoDB;
	
    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    String cacheTableName = "_CACHE_KEY";
    String tableStart = "PRISM_";
    String wsTableName = "PRISM_WS_LOG_";
    
    public static void main(String[] args) {
    	DynamoDBServiceImpl sdb = new DynamoDBServiceImpl();
        try {
        	DynamoDBServiceImpl cc = new DynamoDBServiceImpl();
        	cc.storeWsObject("QA", "<xml> store me </xml>", 12323, true, "");
        } catch (AmazonServiceException ase) {
            System.err.println("Data load script failed.");
        }
    }
    
    @Autowired	private IRepositoryService repositoryService;
    
    public void storeWsObject(String environment, String obj, long processId, boolean requestObj, String source) {
    	if(environment != null && processId != 0) {
			String tableName = CustomStringUtil.appendString(wsTableName, environment.toUpperCase());
			Table table = amazonDynamoDB.getTable(tableName);
			String s3Location = "";
			// get roster id for OAS
			String rosterId = between(obj, "<rosterId>", "</rosterId>");
			String uuid = between(obj, "<UUID>", "</UUID>");
			try {
				// store the xml to S3
				if(requestObj) {
					s3Location = CustomStringUtil.appendString("/PRISMLOG/WSLOG/PROCESSID_" + processId, "_ROSTERID_", rosterId, "_UUID_", uuid, "_REQ") ;
				} else {
					s3Location = CustomStringUtil.appendString("/PRISMLOG/WSLOG/PROCESSID_" + processId, "_RES") ;
				}
				repositoryService.uploadAsset(s3Location, stream2file(obj));
			} catch (Exception e) {
				s3Location = "failed";
			}
			
			try {
				String type = "";
				if("-".equals(rosterId)) {
					type = CustomStringUtil.appendString(uuid, " < UUID");
				} else {
					type = CustomStringUtil.appendString(rosterId, " < Roster ID");
				}
				Item item = new Item()
						.withPrimaryKey("processid", processId)
						.withString("request_obj", requestObj? CustomStringUtil.appendString("Request xml stored into s3: ", s3Location) : obj)
						.withString("type", type)
						.withString("record_type", requestObj? "REQ" : "RES")
						.withString("roster_id", rosterId)
						.withString("uuid", uuid)
						.withString("date", Utils.getDateTime(true))
						.withString("source", source);
				table.putItem(item);
				
			} catch (Exception e) {
			}
    	}
	}
    
    public static File stream2file (String obj) throws IOException {
        final File tempFile = File.createTempFile(System.currentTimeMillis() + "", ".XML");
        tempFile.deleteOnExit();
        FileOutputStream out = null;
        try {
        	out = new FileOutputStream(tempFile);
            IOUtils.copy(IOUtils.toInputStream(obj, "UTF-8"), out);
        } finally {
        	IOUtils.closeQuietly(out);
        }
        return tempFile;
    }
    
	private static String between(String value, String pattern1, String pattern2) {
		try {
			// Return a substring between the two strings.
			StringBuilder sb = new StringBuilder();
			Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
			Matcher m = p.matcher(value);
			while (m.find()) {
			  sb.append(m.group(1)).append("-");
			}
			String ret = sb.toString();
			return (ret != null && ret.trim().length() > 0)? ret : "-";
		} catch (Exception e) {
			return "-";
		}
		
	}
    
    
	public void loadCacheKey(String environment, String contract, String key) {
		String tableName = getTableName(environment);
		Table table = amazonDynamoDB.getTable(tableName);
		try {
			System.out.println("Adding data to " + tableName);
			Item item = new Item()
					.withPrimaryKey("contract", contract)
					.withString("key", key);
			table.putItem(item);
		} catch (Exception e) {
			System.err.println("Failed to create item in " + tableName);
			System.err.println(e.getMessage());
		}

	}
	
	
	public Iterator<Item> fetchCacheKeys(String environment, String contract) {
		String tableName = getTableName(environment);
		Table table = amazonDynamoDB.getTable(tableName);

		QuerySpec querySpec = new QuerySpec().withHashKey("contract", contract);
		ItemCollection<QueryOutcome> items = table.query(querySpec);
		Iterator<Item> iterator = items.iterator();

		System.out.println("Query: printing results...");

		/*while (iterator.hasNext()) {
			//System.out.println(iterator.next().toJSONPretty());
			System.out.println((String) iterator.next().get("key"));
		}*/
		
		return iterator;
	}
	
	/**
	 * delete all keys for provided contract
	 * @param environment
	 * @param contract
	 */
	public void deleteContractKeys(String environment, String contract) {
		String tableName = getTableName(environment);
		Table table = amazonDynamoDB.getTable(tableName);

		QuerySpec querySpec = new QuerySpec().withHashKey("contract", contract);
		ItemCollection<QueryOutcome> items = table.query(querySpec);
		Iterator<Item> iterator = items.iterator();

		while (iterator.hasNext()) {
			table.deleteItem("contract", contract, "key", (String) iterator.next().get("key"));
		}
		
		System.out.println("All cache key are deleted from " + tableName + " for contract " + contract);
	}
	
	/**
	 * Delete a single item for given contract and key
	 * @param environment
	 * @param contract
	 * @param key
	 */
	public void deleteItems(String environment, String contract, String key) {
		String tableName = getTableName(environment);
		System.out.println("Deleting data from " + tableName);
        Table table = amazonDynamoDB.getTable(tableName);
        try {
            DeleteItemOutcome outcome = table.deleteItem("contract", contract, "key", key);

            // Check the response.
            System.out.println("Printing item that was deleted...");
            System.out.println(outcome.getItem().toJSONPretty());

        } catch (Exception e) {
            System.err.println("Error deleting item in " + tableName);
            System.err.println(e.getMessage());
        }
    }
	
	private String getTableName(String environment) {
		return CustomStringUtil.appendString(tableStart, environment.toUpperCase(), cacheTableName);
	}


}