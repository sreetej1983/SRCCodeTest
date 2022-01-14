package com.sreetej.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SRCCodeTest {

	static String s_productsJson = "[{\r\n" + 
			"		\"name\": \"Vegemite Scroll\",\r\n" + 
			"		\"code\": \"VS\",\r\n" + 
			"		\"packagingOptions\": [{\r\n" + 
			"				\"count\": 3,\r\n" + 
			"				\"price\": 6.99\r\n" + 
			"			},\r\n" + 
			"			{\r\n" + 
			"				\"count\": 5,\r\n" + 
			"				\"price\": 8.99\r\n" + 
			"			}\r\n" + 
			"		]\r\n" + 
			"	},\r\n" + 
			"	{\r\n" + 
			"		\"name\": \"Blueberry Muffin\",\r\n" + 
			"		\"code\": \"BM\",\r\n" + 
			"		\"packagingOptions\": [{\r\n" + 
			"				\"count\": 2,\r\n" + 
			"				\"price\": 9.95\r\n" + 
			"			},\r\n" + 
			"			{\r\n" + 
			"				\"count\": 5,\r\n" + 
			"				\"price\": 16.95\r\n" + 
			"			},\r\n" + 
			"			{\r\n" + 
			"				\"count\": 8,\r\n" + 
			"				\"price\": 24.95\r\n" + 
			"			}\r\n" + 
			"		]\r\n" + 
			"	},\r\n" + 
			"	{\r\n" + 
			"		\"name\": \"Croissant\",\r\n" + 
			"		\"code\": \"CR\",\r\n" + 
			"		\"packagingOptions\": [{\r\n" + 
			"				\"count\": 3,\r\n" + 
			"				\"price\": 5.95\r\n" + 
			"			},\r\n" + 
			"			{\r\n" + 
			"				\"count\": 5,\r\n" + 
			"				\"price\": 9.95\r\n" + 
			"			},\r\n" + 
			"			{\r\n" + 
			"				\"count\": 9,\r\n" + 
			"				\"price\": 16.95\r\n" + 
			"			}\r\n" + 
			"		]\r\n" + 
			"	}\r\n" + 
			"]";
	
	static String sampleInput = "10 VS,14 BM,13 CR,";
	static HashMap<String,Product> m_products = new HashMap<String, Product>();
	static ArrayList<HashMap<Integer,Integer>> outputUnits = new ArrayList<HashMap<Integer, Integer>>();
	public static void main(String[] args) {
		
		 JsonArray jsonObj = JsonParser.parseString(s_productsJson).getAsJsonArray();
		 
		 JsonObject productObj; 
		 for(int i = 0; i < jsonObj.size(); i++)
		 {
			 Product product = new Product();
			 productObj = jsonObj.get(i).getAsJsonObject();
			 
			 product.setProductName(productObj.get("name").getAsString());
			 product.setProductCode(productObj.get("code").getAsString());
			 
			 JsonArray packagingOptions = productObj.get("packagingOptions").getAsJsonArray();
			 
			 for(int j = 0; j < packagingOptions.size(); j++)
			 {
				 int count = packagingOptions.get(j).getAsJsonObject().get("count").getAsInt();
				 double price = packagingOptions.get(j).getAsJsonObject().get("price").getAsDouble();
				 product.addPackagingOptions(count,price);
			 }
			 
			m_products.put(product.getProductCode(),product);
		 }
		
		 ProcessInput();
	}
	
	private static void ProcessInput() {
		String[] inputs = sampleInput.split(",");
		//Logic for input[0]
		for(String input : inputs)
		{
			String[] firstInput = input.split(" ");
			int purchasedQty = Integer.parseInt(firstInput[0]);
			int[] packageUnits = m_products.get(firstInput[1]).getOptionCounts(purchasedQty);
			outputUnits.clear();
			
			for( int i = packageUnits.length - 1; i >= 0; i--)
			{
				int pkgUnit = packageUnits[i];
				
				int[] subArray = null;
				if(i > 0)
				subArray = Arrays.copyOfRange(packageUnits,0,i);
				
				//outputUnits[i] = Math.max(0, calculateOutputUnits(purchasedQty, pkgUnit,subArray));
				HashMap<Integer,Integer> out = calculateOutputUnits(purchasedQty, pkgUnit,subArray);
				outputUnits.add(out);
			}
			Product purchasedProduct = m_products.get(firstInput[1]);
			
			String output = purchasedQty + firstInput[1] + ": ";
			double totalPrice = 0.0;
			boolean found = false;
			for(int i = 0; i < outputUnits.size(); i++)
			{
				for (Map.Entry<Integer, Integer> entry : outputUnits.get(i).entrySet()) {
				    int unit = entry.getKey();
				    int count = entry.getValue();
				    
					if(count != -1)
					{
						double price = purchasedProduct.getPriceForPack(unit);
						totalPrice += price;
						output = output + " " + count + " pack of " + unit + " items ($" + price +" ),";
						found = true;
					}
				}
				if(found)
				{
					break;
				}
						
			}
			output = output + " at $" + totalPrice;
			System.out.println(output);
		}
		
		//ProcessOutput(purchasedQty,firstInput[1]);
		
	}

	private static HashMap<Integer,Integer> calculateOutputUnits(int pQty, int pkgUnit, int[] remainingpKgUnits) {
		HashMap<Integer,Integer> outputPair = new HashMap<Integer,Integer>();
		int remainingQty = pQty%pkgUnit;
		if(remainingQty == pQty)//The purhased qty cannot be broken down using this pkg unit.
		{
			outputPair.put(pkgUnit,0);
		}
		else if(remainingQty == 0)//The purhased qty is a muliple of pkg unit.
		{
			outputPair.put(pkgUnit,pQty/pkgUnit);
		}
		else if(remainingpKgUnits == null)
		{
			outputPair.put(pkgUnit,-1);
		}
		
//		else if(isValidUnit(remainingpKgUnits,pQty%pkgUnit))//Checking if remainder units can be divided into other package units.
//		{
//			return 0;
//		}
		else
		{
			int factorOfUnitsAccountedFor = pQty/pkgUnit;
			outputPair.put(pkgUnit,factorOfUnitsAccountedFor);
			int unitsAccountedFor = factorOfUnitsAccountedFor * pkgUnit;
			for( int i = remainingpKgUnits.length - 1; i >= 0; i--)
			{
				int[] subArray = null;
				if(i > 0)
				subArray = Arrays.copyOfRange(remainingpKgUnits,0,i);
				HashMap<Integer,Integer> outputAccountedFor = calculateOutputUnits(remainingQty, remainingpKgUnits[i], subArray);
				int factorOfSubUnitsAccountedFor = outputAccountedFor.get(remainingpKgUnits[i]);
				if(factorOfSubUnitsAccountedFor > 0)
				{
					unitsAccountedFor += (factorOfSubUnitsAccountedFor * remainingpKgUnits[i]);
					outputPair.put(remainingpKgUnits[i], factorOfSubUnitsAccountedFor);
					//factorOfUnitsAccountedFor += factorOfSubUnitsAccountedFor;
				}
			}
			if(unitsAccountedFor == pQty)
				return outputPair; //factorOfUnitsAccountedFor;
			else
				outputPair.put(pkgUnit,-1);
		}
		return outputPair;
	}

}
