package com.sreetej.src;

import java.util.NavigableMap;
import java.util.TreeMap;

public class Product {

	private String m_name;
	private String m_code;
	private TreeMap<Integer, Double> m_packagingOptions = new TreeMap<Integer,Double>();
	private int[] m_optionCounts;
	
	public Product() {
		
	}
	public Product(String name, String code, TreeMap<Integer, Double> packagingOptions) {
		m_name = name;
		m_code = code;
		m_packagingOptions = packagingOptions;
	}
	
	public void setProductName(String name) {
		m_name = name;
	}
	
	public void setProductCode(String code) {
		m_code = code;
	}

	public String getProductName() {
		return m_name;
	}

	public String getProductCode() {
		return m_code;
	}

	public TreeMap<Integer, Double> getPackagingOptions() {
		return m_packagingOptions;
	}

	public void addPackagingOptions(int count, double price) {
		m_packagingOptions.put(count, price);
	}

	public int[] getOptionCounts(int val) {
		NavigableMap<Integer, Double> sotree_map = m_packagingOptions.headMap(val, true);
		m_optionCounts = new int[sotree_map.keySet().size()];
	      int counter = 0;
	      for(Integer key : sotree_map.keySet())
	      {
	    	  m_optionCounts[counter] = key.intValue();
	          counter++;
	      }
		
		
		return m_optionCounts;
	}
	
	public Double getPriceForPack(int count)
	{
		return m_packagingOptions.get(count);
	}
	
}
