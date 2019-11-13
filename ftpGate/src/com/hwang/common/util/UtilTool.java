package com.hwang.common.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class UtilTool {

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static String decode(String value) {
		try {
			return java.net.URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getStrChinese(String str) {
		try {
			if (str == null || str.equals("")) {
				str = "";
			} else {
				str = str.trim();
			}

			byte[] bytes = str.getBytes("ISO8859_1");
			return new String(bytes);
		} catch (Exception ex) {
			return "";
		}

	}

	public static String getStrTrim(String str) {

		if (str == null || str.equals("")) {
			return "";
		} else {
			return str.trim();
		}

	}

	public static String getNumTrim(String str) {

		if (str == null || str.equals("")) {
			return "0";
		} else {
			return str.trim();
		}

	}

	public static String getOptionSelected(String valuestr, String keystr) {
		if (valuestr.equals(keystr)) {
			return "selected";
		} else {
			return "";
		}
	}

	public static String getOptionSelected(String valuestr, String keystr, String dispstr) {
		if (valuestr.equals(keystr)) {
			return "<option value=\"" + valuestr + "\" selected>" + dispstr + "</option>";
		} else {
			return "<option value=\"" + valuestr + "\" >" + dispstr + "</option>";
		}
	}

	public static String getRadioChecked(String inputstr, String keystr) {
		if (inputstr.equals(keystr)) {
			return "checked";
		} else {
			return "";
		}
	}

	public static String getParameter(String parameterstr, String parametername) {

		String tmpstr1 = new String(parameterstr.toUpperCase());
		String tmpstr2 = ";" + parametername.toUpperCase() + "=\"";
		int ipos = tmpstr1.indexOf(tmpstr2);
		if (ipos == -1) {
			return "";
		}

		String tmpstr = parameterstr.substring(ipos + tmpstr2.length());
		ipos = tmpstr.indexOf("\";");
		return tmpstr.substring(0, ipos);
	}

	public static String[] getParameterValues(String parameterstr, String parametername) {

		String resultstr[] = new String[1];
		List<String> list = new ArrayList<String>();

		String tmpstr1 = new String(parameterstr.toUpperCase());
		String tmpstr2 = ";" + parametername.toUpperCase() + "=\"";
		String tmpstr = parameterstr;
		int ibeginpos = 0, iendpos = 0;
		while (ibeginpos >= 0) {
			ibeginpos = tmpstr1.indexOf(tmpstr2);
			if (ibeginpos == -1) {
				break;
			}
			tmpstr = tmpstr.substring(ibeginpos + tmpstr2.length());
			iendpos = tmpstr.indexOf("\";");
			if (iendpos == -1) {
				break;
			}
			list.add(tmpstr.substring(0, iendpos));
			if (iendpos == -1) {
				break;
			}
			tmpstr1 = tmpstr1.substring(ibeginpos + tmpstr2.length());
		}
		resultstr = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			resultstr[i] = (String) list.get(i);
		}
		return resultstr;

	}

	public static String[] getParameterNames(String parameterstr) {

		String resultstr[] = null;
		List<String> list = new ArrayList<String>();

		String tmpstr1 = new String(parameterstr.toUpperCase());
		String tmpstr = parameterstr;
		int ibeginpos = 0, iendpos = 0;
		while (ibeginpos >= 0) {
			ibeginpos = tmpstr1.indexOf(";");
			if (ibeginpos == -1) {
				break;
			}
			tmpstr = tmpstr1.substring(ibeginpos + 1);

			iendpos = tmpstr.indexOf("=");
			if (iendpos == -1) {
				break;
			}
			list.add(tmpstr.substring(0, iendpos));
			iendpos = tmpstr.indexOf(";");
			if (iendpos == -1) {
				break;
			}
			tmpstr1 = tmpstr.substring(iendpos);
		}
		resultstr = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			resultstr[i] = (String) list.get(i);
		}
		return resultstr;
	}

	public static String getDate() {
		GregorianCalendar gcc = new GregorianCalendar();
		int ttYear = gcc.get(Calendar.YEAR);
		int ttMonth = gcc.get(Calendar.MONTH) + 1;
		int ttDay = gcc.get(Calendar.DAY_OF_MONTH);
		Integer month = new Integer(ttMonth);
		String smonth = month.toString();
		if (smonth.length() == 1) {
			smonth = "0" + smonth;
		}
		Integer day = new Integer(ttDay);
		String sday = day.toString();
		if (sday.length() == 1) {
			sday = "0" + sday;
		}

		String day1 = ttYear + smonth + sday;

		return day1;
	}

	public static String getLastDay(String strdate) {
		GregorianCalendar gcc = new GregorianCalendar();
		Integer intYear = new Integer(strdate.substring(0, 4));
		Integer intMon = new Integer(strdate.substring(4, 6));
		Integer intDay = new Integer(1);
		gcc.set(intYear.intValue(), intMon.intValue(), intDay.intValue());
		// gcc.add(Calendar.MONTH,1);
		gcc.add(Calendar.DAY_OF_MONTH, -1);

		int ttYear = gcc.get(Calendar.YEAR);
		int ttMonth = gcc.get(Calendar.MONTH) + 1;
		int ttDay = gcc.get(Calendar.DAY_OF_MONTH);
		Integer month = new Integer(ttMonth);
		String smonth = month.toString();
		if (smonth.length() == 1) {
			smonth = "0" + smonth;
		}
		Integer day = new Integer(ttDay);
		String sday = day.toString();
		if (sday.length() == 1) {
			sday = "0" + sday;
		}

		String day1 = ttYear + smonth + sday;

		return day1;
	}

	public static String getTime() {
		GregorianCalendar gcc = new GregorianCalendar();

		int ttHour = gcc.get(Calendar.HOUR_OF_DAY);
		int ttMint = gcc.get(Calendar.MINUTE);
		int ttSeco = gcc.get(Calendar.SECOND);

		Integer hour = new Integer(ttHour);
		String shour = hour.toString();
		if (shour.length() == 1) {
			shour = "0" + shour;
		}

		Integer mint = new Integer(ttMint);
		String smint = mint.toString();
		if (smint.length() == 1) {
			smint = "0" + smint;
		}
		Integer sec = new Integer(ttSeco);
		String ssec = sec.toString();
		if (ssec.length() == 1) {
			ssec = "0" + ssec;
		}

		String time1 = shour + ":" + smint + ":" + ssec;
		return time1;
	}

	public static boolean isNumeric(String str) {
		int begin = 0;
		boolean once = true;
		if (str == null || str.trim().equals("")) {
			return false;
		}
		str = str.trim();
		if (str.startsWith("+") || str.startsWith("-")) {
			if (str.length() == 1) {
				// "+" "-"
				return false;
			}
			begin = 1;
		}
		for (int i = begin; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				if (str.charAt(i) == '.' && once) {
					// '.' can only once
					once = false;
				} else {
					return false;
				}
			}
		}
		if (str.length() == (begin + 1) && !once) {
			// "." "+." "-."
			return false;
		}
		return true;
	}

	public static boolean isInteger(String str) {
		int begin = 0;
		if (str == null || str.trim().equals("")) {
			return false;
		}
		str = str.trim();
		if (str.startsWith("+") || str.startsWith("-")) {
			if (str.length() == 1) {
				// "+" "-"
				return false;
			}
			begin = 1;
		}
		for (int i = begin; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNumericEx(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	public static boolean isIntegerEx(String str) {
		str = str.trim();
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException ex) {
			if (str.startsWith("+")) {
				return isIntegerEx(str.substring(1));
			}
			return false;
		}
	}


	public static void main(String[] args) {
		System.out.println(UtilTool.getParameter(";A1=a1;B1=b1;A1=a2;", "a1"));
		String tmpstr[] = UtilTool.getParameterNames(";A1=a1;B1=b1;A1=a2;");
		int i;
		for (i = 0; i < tmpstr.length; i++) {
			System.out.println(tmpstr[i]);
		}

		tmpstr = UtilTool.getParameterValues(";A1=a1;B1=b1;A1=a2;A1=", "a1");
		for (i = 0; i < tmpstr.length; i++) {
			System.out.println(tmpstr[i]);
		}

		double j = 1;
		for (i = 1; i < 50; i++) {
			j = j * 1.1;
		}
		System.out.print(j);
	}
}
