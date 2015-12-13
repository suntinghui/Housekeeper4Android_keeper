package com.housekeeper.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.widget.Toast;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
	
	private static final String[] selectCol = new String[]{  
	        ContactsContract.Contacts.DISPLAY_NAME,  
	        ContactsContract.Contacts.HAS_PHONE_NUMBER,  
	        ContactsContract.Contacts._ID  
	    };  
	public static final int COL_NAME = 0;  
	public static final int COL_HAS_PHONE = 1;  
	public static final int COL_ID = 2;  
	  
	// the selected cols for phones of a user  
	private static final String[] selPhoneCols = new String[] {  
	        ContactsContract.CommonDataKinds.Phone.NUMBER,  
	        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,  
	        ContactsContract.CommonDataKinds.Phone.TYPE  
	};  
	public static final int COL_PHONE_NUMBER = 0;  
	public static final int COL_PHONE_NAME = 1;  
	public static final int COL_PHONE_TYPE = 2; 
	
	
	public static boolean validateByReg(String target, String reg) {
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(target);
		return m.matches();
	}

	public static HashMap<String, String> getContactList(Context context) {  
		HashMap<String, String> contacts = new HashMap<String, String>();
	    String select = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND ("  
	       + Contacts.HAS_PHONE_NUMBER + "=1) AND ("  
	       + Contacts.DISPLAY_NAME + " != '' ))";  
	      
	    Cursor cursor = context.getContentResolver().query(  
	            ContactsContract.Contacts.CONTENT_URI, selectCol, select, null,   
	            ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");  
	    if (cursor ==null) {  
	        Toast.makeText(context, "cursor is null!", Toast.LENGTH_LONG).show();  
	        return null;  
	    }  
	    if (cursor.getCount() == 0) {  
	        Toast.makeText(context, "cursor count is zero!", Toast.LENGTH_LONG).show();  
	        return null;  
	    }  
	      
	      
	    cursor.moveToFirst();  
	    while(!cursor.isAfterLast()) {  
	        int contactId;  
	        contactId = cursor.getInt(cursor.getColumnIndex(  
	            ContactsContract.Contacts._ID));  
	        if (cursor.getInt(COL_HAS_PHONE)>0) {  
	            // the contact has numbers  
	            // 获得联系人的电话号码列表  
	            String displayName;  
	            displayName = cursor.getString(COL_NAME);  
	               Cursor phoneCursor = context.getContentResolver().query(  
	                       ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  
	                       selPhoneCols,  
	                       ContactsContract.CommonDataKinds.Phone.CONTACT_ID  
	                               + "=" + contactId, null, null);  
	               if(phoneCursor.moveToFirst()) {  
	                   do   
	                   {  
	                       //遍历所有的联系人下面所有的电话号码  
	                       String phoneNumber = phoneCursor.getString(COL_PHONE_NUMBER);
	                       contacts.put(phoneNumber, displayName);
	                   }while(phoneCursor.moveToNext());  
	               }  
	        }  
	        cursor.moveToNext();  
	    }  
	    if(cursor!=null){
	    	cursor.close();
	    }
	    return contacts;
	}
}
