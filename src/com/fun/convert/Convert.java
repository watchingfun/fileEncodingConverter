package com.fun.convert;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;

public class Convert {
	public static String getFileContentFromCharset(File file,  
            String fromEncoding,boolean preview) throws Exception {  
		if(file!=null) {
        InputStream inputStream = new FileInputStream(file);  
        InputStreamReader reader = new InputStreamReader(inputStream,  
                fromEncoding);  
        char[] chs =null;  
        long filelength=0;
        if(preview) {
        if(file.exists()&&file.isFile()) {
        	filelength = file.length();
        }
        if(filelength<10240) {
        	chs = new char[(int) file.length()];
        }else if(filelength>=10240&&filelength<51200) {
        	chs = new char[(int) file.length()/10];
        }
        else if(filelength>=51200&&filelength<=204800) {
        	chs = new char[(int) file.length()/50];
        }else if(filelength>204800) {
        	chs = new char[(int) file.length()/200];
        }
        }else {
        	chs = new char[(int) file.length()];
        }
       
        reader.read(chs);  
        String str = new String(chs).trim();  
        reader.close();         
        return str;  
        }
		return "null";
    }  
	
	public static void convert(File file, String toCharsetName,  
            String content) throws Exception{
		 if (!Charset.isSupported(toCharsetName)) {  
	            throw new UnsupportedCharsetException(toCharsetName);  
	        }  
	        OutputStream outputStream = new FileOutputStream(file);  
	        OutputStreamWriter outWrite = new OutputStreamWriter(outputStream,  
	                toCharsetName);  
	        outWrite.write(content);  
	        outWrite.close();  
	}
	
	public static int[] isRightEncoding(String selectCharset,ArrayList<File> files) {
		ArrayList<Integer> indexs = new ArrayList<Integer>();
		for(int i = 0;i<files.size();i++) {
			if(!selectCharset.equals(Check.ckCharSet(files.get(i)))) {
				indexs.add(i);
			}
		}
		int [] index = new int[indexs.size()];
		int c=0;
		for(int i :indexs) {
			index[c]=i;
			c++;
		}
		
		return index;
		
	}
	  
}
