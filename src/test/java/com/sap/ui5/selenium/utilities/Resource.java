package com.sap.ui5.selenium.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class Resource{

	private static Properties language = new Properties();
	private static String filepath;

	static {
		  FileInputStream in = null;
		  
		  String MessagePropertiesName = System.getProperty("language");
		  
		  MessagePropertiesName = MessagePropertiesName != null?MessagePropertiesName:"en";

		  filepath = "src/test/resources/languages/"+MessagePropertiesName+".properties";
		  try {
			  File propt = new File(filepath);
			  Reader reader = Files.newReader(propt, Charsets.UTF_8);
			  language.load(reader);
		  } catch (FileNotFoundException e) {
			  e.printStackTrace();
		  } catch (IOException e) {
			  e.printStackTrace();
		  }finally {
			  if(in!=null) {
				  try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			  }
		  }
	}
	

	public static String getMessage(String key,String... replacements) {
		String result = language.getProperty(key);
		for(int i = 0; i< replacements.length;i++){
			result = result.replace("{"+i+"}", replacements[i]);
		}
	  return result;
	}


}
