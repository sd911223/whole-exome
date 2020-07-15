package com.accurascience.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

/**
 * txt读取
 * @author zhuchaojie
 *
 */
public class TxtReadUtil {
    /**
     * 读取txt中的key数组
     * @param path
     * @return
     */
	public static String[] listKey(String path){
		
		try {
			ClassPathResource cr = new ClassPathResource(path);
			InputStream input = cr.getInputStream();
			String content = IOUtils.toString(input);
			input.close();
			cr =null;
			return content.split("\r\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * test
	 * @param args
	 */
	public static void main(String[] args) {
		String[] array = TxtReadUtil.listKey("key/all_key.txt");
		System.out.println(array);
	}
	
}
