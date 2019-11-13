package com.hwang.common.util;

import java.io.*;

/*
*此类完成文件输入输出的常用功�?
*/
public class FileUtil {
	/*
	 * 将一个字符串写稿到文件中
	 */
	public static boolean writeStrToFile(String filename, String str)// 将数据写入到文件�?
	{
		try {
			File f = new File(filename);
			if (!f.exists())
				f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			OutputStreamWriter ow = new OutputStreamWriter(fos);
			ow.write(str, 0, str.length());
			ow.close();
			fos.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/*
	 * 从文件中读取�?个字符串
	 */
	public static String readStrFromFile(File f)// 从文件中读取内容
	{
		String str = "";
		char[] buf = new char[1024];
		int len = 0;
		if (!f.exists())
			return "";
		try {
			FileInputStream fin = new FileInputStream(f);
			InputStreamReader ir = new InputStreamReader(fin);
			while ((len = ir.read(buf, 0, 1024)) != -1)
				str += new String(buf, 0, len);
			ir.close();
			fin.close();
			return str;
		} catch (Exception e) {
			return "";
		}
	}

	/*
	 * 从文件中读取Object，f为已经判定过的文件句�?
	 */

	public static Object readObjFromFile(File f) {
		Object obj = null;
		try {
			FileInputStream fin = new FileInputStream(f);
			ObjectInputStream in = new ObjectInputStream(fin);
			obj = in.readObject();
			in.close();
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	/*
	 * 将object写入文件�?
	 */

	public static boolean writeObjToFile(File f, Object obj) {
		try {
			if (!f.exists())
				f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(obj);
			out.close();
			fos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/*
	 * 完成文件的拷�?
	 */

	public static boolean copyFile(File dst, File src) {
		if (!src.exists())
			return false;
		try {
			if (!dst.exists())
				dst.createNewFile();
			FileInputStream fin = new FileInputStream(src);
			FileOutputStream fout = new FileOutputStream(dst);
			byte[] buf = new byte[1024];
			int len = -1;
			while ((len = fin.read(buf)) != -1)
				fout.write(buf, 0, len);
			fout.close();
			fin.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
