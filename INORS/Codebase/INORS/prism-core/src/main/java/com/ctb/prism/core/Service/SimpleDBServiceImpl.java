package com.ctb.prism.core.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.model.BatchPutAttributesRequest;
import com.amazonaws.services.simpledb.model.DeleteAttributesRequest;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.ReplaceableItem;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;
import com.ctb.prism.core.logger.IAppLogger;
import com.ctb.prism.core.logger.LogFactory;
import com.ctb.prism.core.resourceloader.IPropertyLookup;

@Service("simpleDBService")
public class SimpleDBServiceImpl implements ISimpleDBService {
	
	private static final IAppLogger logger = LogFactory.getLoggerInstance(SimpleDBServiceImpl.class.getName());

	private AmazonSimpleDB sdb;
	
	private String POSTFIX_ENV = "environment.postfix";
	
	@Autowired
	public void setSdb(AmazonSimpleDB sdb) {
	    this.sdb = sdb;
	}
	
	@Autowired
	private IPropertyLookup propertyLookup;
	
	public void addItem(String contract, String item){
		String domain = contract + propertyLookup.get(POSTFIX_ENV);//"cachelookup";
		logger.log(IAppLogger.INFO, "Cache Key will be inserted into domain" + domain);
		
		try{
			/** create domain from API (scratchpad) **/
			//CreateDomainRequest req = new CreateDomainRequest(domain);
			//sdb.createDomain(req);
			
			List<ReplaceableItem> data = new ArrayList<ReplaceableItem>();

			//data.add(new ReplaceableItem().withName("contract"+contract).withAttributes(
			data.add(new ReplaceableItem().withName(item).withAttributes(		
			   new ReplaceableAttribute().withName("contractName").withValue(contract),
			   new ReplaceableAttribute().withName("cacheKey").withValue(item)));
			
			sdb.batchPutAttributes(new BatchPutAttributesRequest(domain, data));

		} catch(AmazonClientException ex){
			ex.printStackTrace();
		}
	}
	
	public List<Item> getAllItems(String contract){
		String domain = contract + propertyLookup.get(POSTFIX_ENV);//"cachelookup";
		String query = "select * from " + domain;
		return getItems(query);
	}
	
	public List<Item> getItems(String query){
	    logger.log(IAppLogger.DEBUG, "Executing query: " + query);
	    List<Item> items  = new ArrayList<Item>();
	     
	    String nextToken = null;
	    do{
	        SelectRequest selectRequest = new SelectRequest(query);
	        selectRequest.setConsistentRead(false);
	 
	        if(nextToken != null){
	            selectRequest.setNextToken(nextToken);
	        }
	 
	        SelectResult result = sdb.select(selectRequest); 
	        items.addAll(result.getItems());
	        nextToken = result.getNextToken();
	 
	    }while(nextToken != null);

	    logger.log(IAppLogger.DEBUG, "Found matching items: " + items.size()); 
	    return items;
	}
	
	public boolean deleteItem(String contract, String key){
		String domain = contract + propertyLookup.get(POSTFIX_ENV);//"cachelookup";
		//sdb.deleteAttributes(new DeleteAttributesRequest(domain, "contract"+contract));
		sdb.deleteAttributes(new DeleteAttributesRequest(domain, key));
		return true;
	}
	
}
