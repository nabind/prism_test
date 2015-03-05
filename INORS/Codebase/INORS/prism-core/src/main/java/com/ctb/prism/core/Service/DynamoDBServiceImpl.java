package com.ctb.prism.core.Service;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

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
        	BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAJCEB4JEZJRM2WFXQ", "lOxQhmTWGFe2tKb0YdxHsnaHTAGY3vCjddj0Lfet");
        	DynamoDB ddb = new DynamoDB(new AmazonDynamoDBClient(awsCreds));
        	sdb.amazonDynamoDB = ddb;
        	sdb.storeWsObject("qa", "<request>asas</request>", 12345, true);
        	//loadCacheKey("dev", "inors", "11111");
        	//sdb.fetchCacheKeys("dev", "tasc");
        	//sdb.deleteContractKeys("dev", "inors");
        } catch (AmazonServiceException ase) {
            System.err.println("Data load script failed.");
        }
    }
    
    public void storeWsObject(String environment, String obj, long processId, boolean requestObj) {
    	if(environment != null && processId != 0) {
			String tableName = CustomStringUtil.appendString(wsTableName, environment.toUpperCase());
			Table table = amazonDynamoDB.getTable(tableName);
			try {
				//System.out.println("Adding data to " + tableName);
				Item item = new Item()
						.withPrimaryKey("processid", processId)
						.withString("request_obj", obj)
						.withString("type", requestObj? "REQ" : "RES");
				table.putItem(item);
			} catch (Exception e) {
				System.err.println("Failed to create item in " + tableName);
				System.err.println(e.getMessage());
			}
    	}
	}
    
    
	public void loadCacheKey(String environment, String contract, String key) {
		String tableName = getTableName(environment);
		Table table = amazonDynamoDB.getTable(tableName);
		try {
			//System.out.println("Adding data to " + tableName);
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

		//System.out.println("Query: printing results...");

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
		
		//System.out.println("All cache key are deleted from " + tableName + " for contract " + contract);
	}
	
	/**
	 * Delete a single item for given contract and key
	 * @param environment
	 * @param contract
	 * @param key
	 */
	public void deleteItems(String environment, String contract, String key) {
		String tableName = getTableName(environment);
		//System.out.println("Deleting data from " + tableName);
        Table table = amazonDynamoDB.getTable(tableName);
        try {
            DeleteItemOutcome outcome = table.deleteItem("contract", contract, "key", key);

            // Check the response.
            //System.out.println("Printing item that was deleted...");
            //System.out.println(outcome.getItem().toJSONPretty());

        } catch (Exception e) {
            System.err.println("Error deleting item in " + tableName);
            System.err.println(e.getMessage());
        }
    }
	
	private String getTableName(String environment) {
		return CustomStringUtil.appendString(tableStart, environment.toUpperCase(), cacheTableName);
	}


}