package com.hwang.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropTool {

	 public static String getProperties(String filePath, String keyWord){
		Properties prop = new Properties();
	      String value = null;
	      try {
	          InputStream InputStream = new BufferedInputStream(new FileInputStream(new File(filePath)));
	          prop.load(InputStream);
	          value = prop.getProperty(keyWord);
	          System.out.println(keyWord+"="+value);
	      
	          
	      } catch (Exception e) {
	          e.printStackTrace();
	      }
	      return value;
	  }
 



}