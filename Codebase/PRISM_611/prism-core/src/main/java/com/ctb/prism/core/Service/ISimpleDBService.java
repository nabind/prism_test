package com.ctb.prism.core.Service;
import java.util.List;
import com.amazonaws.services.simpledb.model.Item;

public interface ISimpleDBService {
	public List<Item> getAllItems(String contract);
	public List<Item> getItems(String query);
	public void addItem(String contract,String item);
	public boolean deleteItem(String contract, String key);
}
