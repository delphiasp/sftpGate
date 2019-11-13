package com.hwang.common.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
	public static int checkServerStatus(String url) throws IOException {

		URL realUrl;
		int response_code = 0;
		HttpURLConnection conn=null;
		try {
			realUrl = new URL(url);
			conn = (HttpURLConnection) realUrl.openConnection();
			conn.setConnectTimeout(10*1000);
			conn.connect();

			response_code = conn.getResponseCode();

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		
		/*
		 * if (response_code == 200) { // 正常响应 // 从流中读取响应信�? BufferedReader reader = new
		 * BufferedReader(new InputStreamReader(conn.getInputStream())); String line =
		 * null;
		 * 
		 * while ((line = reader.readLine()) != null) { // 循环从流中读�?
		 * System.out.println(line); } reader.close(); // 关闭�? }
		 */

		return response_code;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			System.out.println(HttpUtil.checkServerStatus("http://127.0.0.1:8080"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
