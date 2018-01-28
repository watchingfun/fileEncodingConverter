package com.fun.convert;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import info.monitorenter.cpdetector.io.*;

public class Check {
	public static String ckCharSet(URL url) {
		
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		detector.add(JChardetFacade.getInstance());
		detector.add(ASCIIDetector.getInstance());
		detector.add(UnicodeDetector.getInstance());
		java.nio.charset.Charset charset = null;
		try {
			charset = detector.detectCodepage(url);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (charset != null) {
			System.out.println("编码是：" + charset.name());
		} else
			System.out.println("未知");
		
		return charset.name();
		
	}

	public static String ckCharSet(String str) {
		File f = new File(str);
		String charset = null;
		if (f.exists()) {
			
			try {
				charset = ckCharSet(f.toURI().toURL());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("文件不存在");
		}
		return charset;
	}

	public static String ckCharSet(File f) {
		String charset = null;
		if (f!=null&&f.exists()&&f.isFile()) {
			try {
				charset = ckCharSet(f.toURI().toURL());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("文件不存在或不是文件");
		}
		return charset;
	}

}
