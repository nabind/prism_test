package com.ctb.prism.core.Service;

import java.util.Iterator;

import com.amazonaws.services.dynamodbv2.document.Item;


public interface IDynamoDBService {

	public void loadCacheKey(String environment, String contract, String key);
	
	public Iterator<Item> fetchCacheKeys(String environment, String contract);
	
	public void deleteContractKeys(String environment, String contract);
	
	public void deleteItems(String environment, String contract, String key);
	
	public void storeWsObject(String environment, String obj, long processId, boolean requestObj, String source);
	
	public void storeWsObject(String environment, String obj, long processId, boolean requestObj, String source, String rosterId);

}