/**
 * 
 */
package com.study.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author Administrator
 *
 */
public class VTools {

	public static String getNewUUID(){
		
		String str = UUID.randomUUID().toString().replace("-", "");
	    return str;
	}
	public static boolean StringIsNullOrSpace(String paramString)
	  {
	    return (paramString == null) || ("".equals(paramString.trim())) || ("NULL".equals(paramString.trim().toUpperCase())) || ("<无>".equals(paramString.trim()));
	  }
	public static String getCurrencyTime(String type){
		SimpleDateFormat df = new SimpleDateFormat(type);
		return df.format(new Date());
	}

	public static String getCurrentDate(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
}
