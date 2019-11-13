package com.hwang.common.util;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    private StringBuffer sb = null;
    private String sysLineSeparator = null;

    private static Pattern numericPattern = Pattern.compile("^[0-9\\-]+$");
    private static Pattern numericStringPattern = Pattern
            .compile("^[0-9\\-\\-]+$");
    private static Pattern floatNumericPattern = Pattern
            .compile("^[0-9\\-\\.]+$");
    private static Pattern abcPattern = Pattern.compile("^[a-z|A-Z]+$");
    public static final String splitStrPattern = ",|，|;|；|、|\\.|。|-|_|\\(|\\)|\\[|\\]|\\{|\\}|\\\\|/| |　|\"";
	private static XMLDecoder decoder;

    public StringUtil() {
        if (sb != null) {
            sb = null;
        } else {
            sb = new StringBuffer();
        }
        sysLineSeparator = System.getProperty("line.separator");
    }

    public static String requote(String str) {
        if (str == null)
            str = "";
        if ("null".equalsIgnoreCase(str))
            str = "";
        return str;
    }

    public static String objectToString(Object objectStr) {
        if (objectStr == null)
            objectStr = "";
        if ("null".equalsIgnoreCase(String.valueOf(objectStr)))
            objectStr = "";
        return String.valueOf(objectStr);
    }

    public static String objectToString(Object objectStr, String e) {
        if (objectStr == null)
            objectStr = e;
        if ("null".equalsIgnoreCase(String.valueOf(objectStr)))
            objectStr = e;
        return String.valueOf(objectStr);
    }


    public static boolean equalsIgnoreCase(String srcStr, String destSrc) {
        if (isNotEmpty(srcStr) && isNotEmpty(destSrc)) {
            return srcStr.equalsIgnoreCase(destSrc);
        } else {
            return false;
        }
    }


    public static boolean isBlankOrNull(String str) {
        return "".equals(requote(str));
    }
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isNull(Object obj) {
        return null == obj;
    }

 
    public static boolean isBlank(String str) {
        int strLen;
        if ((str == null) || ((strLen = str.length()) == 0))
            return true;
        for (int i = 0; i < strLen; i++)
            if (!Character.isWhitespace(str.charAt(i)))
                return false;
        return true;
    }

  
    public static String getRepeatedStr(String str, int num){
        String strs="";
        if(num==0)
            return strs;
        for(int i=0;i<num;i++){
            strs+=str;
        }
        return strs;
    }

    public static String GBKToISO(String str) throws Exception {
        if (str != null) {
            return new String(str.getBytes("GBK"), "ISO8859_1");
        } else {
            return "";
        }
    }

   
    public static String ISOToGBK(String str) throws Exception {
        if (str != null) {
            return new String(str.getBytes("ISO8859_1"), "GBK");
        } else {
            return "";
        }
    }

  
    public static String gbToUtf8(String src) {
        byte[] b = src.getBytes();
        char[] c = new char[b.length];
        for (int i = 0; i < b.length; i++) {
            c[i] = (char) (b[i] & 0x00FF);
        }
        return new String(c);
    }

   
    public static String null2Zero(Object obj) {

        if ((obj == null) || (obj.equals("")) || (obj.equals("null"))) {
            return "0";
        } else {
            return obj.toString().trim();
        }
    }

   
    public static String null2Str(Object obj) {
        if ((obj == null) || (obj.equals("null")) || (obj.equals(""))) {
            return "";
        } else {
            return obj.toString().trim();
        }
    }

   
    public static String[] split(String source, char useChar) {
        List<String> list = new ArrayList<String>();
        String sub;
        String[] result;

        if (source.charAt(0) == useChar)
            source = source.substring(1, source.length());
        if (source.charAt(source.length() - 1) == useChar)
            source = source.substring(0, source.length() - 1);

        int start = 0;
        int end = source.indexOf(useChar);
        while (end > 0) {
            sub = source.substring(start, end);
            list.add(sub);
            start = end + 1;
            end = source.indexOf(useChar, start);
        }

        sub = source.substring(start, source.length());
        list.add(sub);

        result = new String[list.size()];

        Iterator<String> iter = list.iterator();
        int i = 0;
        while (iter.hasNext()) {
            result[i++] = (String) iter.next();
        }
        return result;
    }

   
    public static List<String> splitList(String source, char useChar) {
        List<String> list = new ArrayList<String>();
        String sub;

        if (source.charAt(0) == useChar)
            source = source.substring(1, source.length());
        if (source.charAt(source.length() - 1) == useChar)
            source = source.substring(0, source.length() - 1);

        int start = 0;
        int end = source.indexOf(useChar);
        while (end > 0) {
            sub = source.substring(start, end);
            list.add(sub);
            start = end + 1;
            end = source.indexOf(useChar, start);
        }

        sub = source.substring(start, source.length());
        list.add(sub);

        return list;
    }

   
    public static boolean isIn(String subStr, String str) {
        if (subStr == null || str == null) {
            return false;
        }
        return str.indexOf(subStr) != -1;
    }

    public static String join(String[] str) {
        return join(str, ",");
    }


    public static String join(String[] str, String join) {
        if (str == null || join == null) {
            return "";
        }
        String rtnStr = "";
        for (int i = 0; i < str.length; i++) {
            rtnStr += join + str[i];
        }
        if (rtnStr.indexOf(join) != -1) {
            rtnStr = rtnStr.substring(join.length());
        }
        return rtnStr;
    }


    public static String join(Set<String> set, String join) {
        String[] str = set.toArray(new String[0]);
        return join(str, join);
    }

    public static String join(List<?> list, String join) {
        Object[] str = list.toArray(new Object[0]);

        if (str == null || join == null) {
            return "";
        }
        String rtnStr = "";
        for (int i = 0; i < str.length; i++) {
            rtnStr += join + str[i].toString();
        }
        if (rtnStr.indexOf(join) != -1) {
            rtnStr = rtnStr.substring(join.length());
        }
        return rtnStr;
    }

 
    public static final String replace(String line, String oldString,
                                       String newString) {
        if (line == null) {
            return null;
        }
        int i = 0;
        if ((i = line.indexOf(oldString, i)) >= 0) {
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = line.indexOf(oldString, i)) > 0) {
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            return buf.toString();
        }
        return line;
    }

  
    public static final String replaceIgnoreCase(String line, String oldString,
                                                 String newString) {
        if (line == null) {
            return null;
        }
        String lcLine = line.toLowerCase();
        String lcOldString = oldString.toLowerCase();
        int i = 0;
        if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = lcLine.indexOf(lcOldString, i)) > 0) {
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            return buf.toString();
        }
        return line;
    }

   
    public static final String replace(String line, String oldString,
                                       String newString, int[] count) {
        if (line == null) {
            return null;
        }
        int i = 0;
        if ((i = line.indexOf(oldString, i)) >= 0) {
            int counter = 0;
            counter++;
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = line.indexOf(oldString, i)) > 0) {
                counter++;
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            count[0] = counter;
            return buf.toString();
        }
        return line;
    }

    
    public static final String int2Date(Integer date, String interval) {
        String line = String.valueOf(date);
        if (line.length() != 8) {
            return null;
        } else {
            StringBuffer buf = new StringBuffer(10);
            buf.append(line.substring(0, 4));
            buf.append(interval);
            buf.append(line.subSequence(4, 6));
            buf.append(interval);
            buf.append(line.substring(6, 8));
            return buf.toString();
        }

    }

    
    public static final String long2Date(Long date, String interval) {
        String line = String.valueOf(date);
        if (line.length() != 8) {
            return null;
        } else {
            StringBuffer buf = new StringBuffer(10);
            buf.append(line.substring(0, 4));
            buf.append(interval);
            buf.append(line.subSequence(4, 6));
            buf.append(interval);
            buf.append(line.substring(6, 8));
            return buf.toString();
        }

    }

   
    public static boolean checkDataValid(String input) {
        String strTemp = new String(input);
        if (strTemp == null || strTemp.length() == 0) {
            return false;
        }
        strTemp = strTemp.trim();
        return strTemp.length() != 0;
    }

    
    public static String escapeHTML(String input) {

        if (input == null || input.length() == 0) {
            return input;
        }
        input = input.trim();
        StringBuffer buf = new StringBuffer();
        char ch = ' ';
        for (int i = 0; i < input.length(); i++) {
            ch = input.charAt(i);
            if (ch == '<') {
                buf.append("&lt;");
            } else if (ch == '>') {
                buf.append("&gt;");
            } else if (ch == '\n') {
                buf.append("<br/>");
            } else if (ch == ' ') {
                buf.append("&nbsp;");
            } else if (ch == '\'') {
                buf.append("''");
            } else {
                buf.append(ch);
            }
        } // end for loop
        return buf.toString();
    }

   
    public static final String escapeHTMLTags(String input) {
        if (input == null || input.length() == 0) {
            return input;
        }
        StringBuffer buf = new StringBuffer(input.length());
        char ch = ' ';
        for (int i = 0; i < input.length(); i++) {
            ch = input.charAt(i);
            if (ch == '<') {
                buf.append("&lt;");
            } else if (ch == '>') {
                buf.append("&gt;");
            } else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }

    public void append(String str) {
        sb.append(str);
    }

   
    public void appendln(String str) {
        appendln(str, false);
    }

   
    public void appendln(String str, boolean useSysLineSeparator) {
        if (useSysLineSeparator) {
            sb.append(str);
            sb.append(this.sysLineSeparator);
        } else {
            sb.append(str);
            sb.append("\n");
        }
    }

   
    public String toStr() {
        return sb.toString();
    }

   
    public static final String[] toLowerCaseWordArray(String text) {
        if (text == null || text.length() == 0) {
            return new String[0];
        }
        StringTokenizer tokens = new StringTokenizer(text, " ,\r\n.:/\\+");
        String[] words = new String[tokens.countTokens()];
        for (int i = 0; i < words.length; i++) {
            words[i] = tokens.nextToken().toLowerCase();
        }
        return words;
    }

    
    public static String[] objectArrayToStringArray(Object[] objs) {
        if (objs == null)
            return null;
        String[] s = new String[objs.length];
        System.arraycopy(objs, 0, s, 0, s.length);
        return s;
    }

 
    public static final String encrypt(String str) {

        String string_in = "YN8K1JOZVURB3MDETS5GPL27AXWIHQ94C6F0#$_@!~`%^&*()-+=[]{}'|?;:/,<>.\"\\ ";
        String string_out = " @!~`%^&*()-+=[]{}'|?;:/,<>._$#ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789\"\\";
        String outpass = "";
        try {
            if (str != null) {
                int offset = 0;
                Calendar calendar = Calendar.getInstance();
                int ss = calendar.get(Calendar.SECOND);
                offset = ss % 68;
                if (offset > 0)
                    offset = offset - 1;
                outpass = string_in.substring(offset, offset + 1);
                string_in = string_in + string_in;
                string_in = string_in.substring(offset, offset + 69);
                outpass = outpass + translate(str, string_in, string_out);
                outpass = strToAscStr(outpass,"-");
                return outpass;
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }


    public static final String simpleEncrypt1(String str){
        String in = strToAscStr(str,"-");
        return in;
    }

    public static final String simpleDisencrypt(String str){
        String out = ascToStr(str,"-");
        return out;
    }

 
    public static final String disencrypt(String str) {
        str = ascToStr(str,"-");
        String string_in = "YN8K1JOZVURB3MDETS5GPL27AXWIHQ94C6F0#$_@!~`%^&*()-+=[]{}'|?;:/,<>.\"\\ ";
        String string_out = " @!~`%^&*()-+=[]{}'|?;:/,<>._$#ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789\"\\";
        try {
            int offset = 0;
            char c = str.charAt(0);
            offset = string_in.indexOf(c);
            string_in = string_in + string_in;
            string_in = string_in.substring(offset, offset + 69);
            String s = str.substring(1);
            s = s.toUpperCase();
            String inpass = translate(s, string_out, string_in);
            return inpass;
        } catch (Exception e) {
            return "";
        }
    }

 
    private static final String translate(String str, String string_in,
                                          String string_out) {

        String s = str.toUpperCase();
        char[] outc = new char[s.length()];
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int j = string_in.indexOf(c);
            outc[i] = string_out.charAt(j);

        }
        String outs = new String(outc);

        return outs;
    }

    
    public static String encode(String inputStr) {

        String tempStr = "";
        try {
            tempStr = new String(inputStr.getBytes("ISO-8859-1"));
        } catch (Exception ex) {
            System.out.print("encode() error: " + ex.toString());
        }
        return tempStr;
    }

    
    public static String urlEncode(String url) {
        if (url == null || "".equals(url))
            return "";
        try {
            return URLEncoder.encode(url, "GBK");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static String urlDecode(String encodedUrl) {
        if (encodedUrl == null || "".equals(encodedUrl))
            return "";
        try {
            return java.net.URLDecoder.decode(encodedUrl, "GBK");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str) || "".equals(str.trim()) || str.equals("null") || str.equals("undefined");
    }




    public static String getRoundPercent(double f) {
        DecimalFormat df = new DecimalFormat("#####.00");

        return df.format(f);
    }

   
    public static String formartDouble(double f) {
        DecimalFormat df = new DecimalFormat("#,##0.0#");
        return df.format(f);
    }

  
    public static Double parseDouble(String str) {
        try {
            DecimalFormat df = new DecimalFormat("#,##0.0#");
            return Double.valueOf(String.valueOf(df.parseObject(str)));
        } catch (Exception e) {
            return new Double(0.0);
        }
    }

   
    public static double getDoubledigit(double f) {
        DecimalFormat df = new DecimalFormat("#####.00");
        return Double.parseDouble(df.format(f));
    }


    public static int getIntRoundPercent(double f) {

        DecimalFormat df = new DecimalFormat("#####");
        Double.parseDouble(df.format(f));

        return Integer.parseInt(df.format(f));
    }

   
    public static int getRoundPercent(float f) {
        double r = f * 100;
        String round = String.valueOf(r);
        if (round.indexOf(".") > 0) {
            round = round.substring(0, round.indexOf("."));
            int intValue = Integer.parseInt(round);
            if (r - intValue >= 0.5)
                intValue += 1;
            round = String.valueOf(intValue);
        }
        return Integer.parseInt(round);
    }

   
    public static String getYYYYString() {
        Calendar now = Calendar.getInstance();
        String number = "" + now.get(Calendar.YEAR) + "-"
                + now.getTimeInMillis();
        return number;
    }

  
    public static String removeUnderScores(String data) {
        String temp = null;
        StringBuffer out = new StringBuffer();
        temp = data;

        StringTokenizer st = new StringTokenizer(temp, "_");
        while (st.hasMoreTokens()) {
            String element = (String) st.nextElement();
            out.append(capitalize(element));
        }
        return out.toString();
    }

  
    public static String capitalize(String data) {
        StringBuffer sbuf = new StringBuffer(data.length());
        sbuf.append(data.substring(0, 1).toUpperCase()).append(
                data.substring(1));
        return sbuf.toString();
    }

    /**
     * 将指定字符串的第pos个字符大写
     *
     * @param data
     *            指定字符串
     * @param pos
     *            第pos个字符
     * @return 返回类型 String 返回改变后的字符串
     *
     */
    public static String capitalize(String data, int pos) {
        StringBuffer buf = new StringBuffer(data.length());
        buf.append(data.substring(0, pos - 1));
        buf.append(data.substring(pos - 1, pos).toUpperCase());
        buf.append(data.substring(pos, data.length()));
        return buf.toString();
    }

    /**
     * 将指定字符串的第pos个字符小写
     *
     * @param data
     *            指定字符串
     * @param pos
     *            第pos个字符
     * @return 返回类型 String 返回改变后的字符窜
     *
     */
    public static String unCapitalize(String data, int pos) {
        StringBuffer buf = new StringBuffer(data.length());
        buf.append(data.substring(0, pos - 1));
        buf.append(data.substring(pos - 1, pos).toLowerCase());
        buf.append(data.substring(pos, data.length()));
        return buf.toString();
    }

    /**
     * 将一个字符串按给定分割字符串分割成数组
     *
     * @param text
     *            字符串
     * @param separator
     *            分割字符串
     * @return 返回类型 String[] 数组字符串
     *
     */
    public static String[] split(String text, String separator) {
        StringTokenizer st = new StringTokenizer(text, separator);
        String[] values = new String[st.countTokens()];
        int pos = 0;
        while (st.hasMoreTokens()) {
            values[pos++] = st.nextToken();
        }
        return values;
    }

    /**
     * 将字符串中所有的tag全部替换成为指定的info
     *
     * @param source
     *            原来的字符串
     * @param info
     *            替换tag的字符串
     * @param startTag
     *            被替换字符串起始点
     * @param endTag
     *            被替换字符串结束点
     * @return 返回类型 String 替换后的字符串
     *
     */
    public static String replaceAll(String source, String info,
                                    String startTag, String endTag) {
        if ((source == null) || (source.length() == 0)) {
            return "";
        }
        if ((info == null) || (startTag == null) || (startTag.length() == 0)
                || (endTag == null) || (endTag.length() == 0)) {
            return source;
        }
        int sIndex = source.indexOf(startTag);
        int eIndex = source.indexOf(endTag);
        boolean valid = (sIndex >= 0 && eIndex >= 0);
        if (!valid) {
            return source;
        } else {
            if (sIndex > eIndex) {
                eIndex = source.indexOf(endTag, sIndex);
            }
        }
        StringBuffer ret = new StringBuffer();
        int start = 0;
        while (valid) {
            info = source.substring(sIndex + 1, eIndex).trim();
            ret.append(source.substring(start, sIndex + 1)).append(info)
                    .append(endTag);
            start = eIndex + 1;
            sIndex = source.indexOf(startTag, start);
            eIndex = source.indexOf(endTag, start);
            valid = (sIndex >= 0 && eIndex >= 0 && eIndex > sIndex);
        }
        ret.append(source.substring(start));
        return ret.toString();
    }

    /**
     * 将输入字符中的SQL保留字进行替换，目前只替换英文半角的单引号'
     * 单引号替换方法：一个单引号换成连续的两个单引号，例如'ABC'D替换成''ABC''D
     *
     * @param s
     *            源字符串
     * @return 返回类型 String 替换后字符串
     *
     */
    public static String getSQLencode(String s) {
        if ((s == null) || (s.length() == 0))
            return "";
        StringBuffer sb = new StringBuffer();
        char c;
        for (int i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            switch (c) {
                case '\'':
                    sb.append("''");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 将输入字符中的格式化成precision指定的程度,截掉的部分用“..”补齐
     *
     * @param s
     *            被格式化字符串
     * @param precision
     *            格式化长度
     * @return 返回类型 String 格式化后字符串
     *
     */
    public static String getFormatString(String s, int precision) {
        String retValue = "";
        if ((s == null) || (s.length() == 0))
            retValue = "";
        if (s.length() <= precision)
            retValue = s;
        if (s.length() == precision + 1)
            retValue = s;
        if (s.length() > precision + 1)
            retValue = s.substring(0, precision - 1) + "..";
        return retValue;
    }



    /**
     * 生成树形CODE码
     *
     * @param title
     *            头
     * @param tail
     *            尾
     * @param tailLength
     *            长度
     * @return 返回类型 String 返回生成的code码
     *
     */
    public static String ensureLengthWith0(String title, String tail,
                                           int tailLength) {
        int len = tail.length();

        if (len == tailLength)
            return title.concat(tail);
        if (len > tailLength)
            return title.concat(tail.substring(0, tailLength));

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < tailLength - len; i++)
            sb.append('0');

        return title.concat(sb.toString().concat(tail));
    }

    public static final String filterHalfWord(String sourceStr)
            throws UnsupportedEncodingException {
        String result = "";
        char[] cc = sourceStr.toCharArray();
        for (int i = 0; i < cc.length; i++) {
            byte[] c = String.valueOf(cc[i]).getBytes("UTF-8");
            String hex = encodeHex(c);

            if (!"0".equals(hex.trim())) {
                result += String.valueOf(cc[i]);
            }
        }
        return result;
    }

    public static final String encodeHex(byte[] bytes) {
        StringBuffer buff = new StringBuffer(bytes.length * 2);
        String b;
        for (int i = 0; i < bytes.length; i++) {
            b = Integer.toHexString(bytes[i]);
            buff.append(b.length() > 2 ? b.substring(6, 8) : b);
            buff.append(" ");
        }
        return buff.toString();
    }

    public static String getRandom18Str() {
        String random18Str = "";
        random18Str = (new Date().getTime() + "" + Math
                .floor(Math.random() * 100000));
        random18Str = random18Str.substring(0, random18Str.indexOf("."));
        if (null != random18Str && 18 == random18Str.length()) {
            return random18Str;
        } else {
            return getRandom18Str();
        }
    }

    public static void main(String[] args) {
        System.out.println(encrypt("111111"));
        System.out.println(disencrypt("5d-3f-3f-3f-3f-3f-3f"));
        System.out.println(equalsIgnoreCase("dest", "DEst"));

    }

    /**
     * 字符转ASC
     *
     * @param str
     * @param splitStr
     * @return
     */
    public static String strToAscStr(String str,String splitStr) {
        String retStr = "";
        if(null==str){
            return "";
        }
        byte[] byteArray = str.getBytes();
        for(int i=0;i<byteArray.length;i++){
            retStr+=Integer.toHexString(byteArray[i])+splitStr;
        }
        if(retStr.length()>splitStr.length()){
            return retStr.substring(0,retStr.length()-splitStr.length());
        }else{
            return retStr;
        }
    }

    /**
     * ASC转字符
     *
     * @param str
     * @param split
     * @return
     */
    public static String ascToStr(String str, String split) {
        String retStr = "";
        if(null==str){
            return retStr;
        }
        String[] strArray = str.split(split);
        if(null!=strArray&&0<strArray.length){
            for(int i=0;i<strArray.length;i++){
                retStr += (char)Integer.parseInt(strArray[i], 16);
            }
        }
        return retStr;
    }

    /**
     * 取得指定子串在字符串中出现的次数。
     * <p/>
     * <p>
     * 如果字符串为<code>null</code>或空，则返回<code>0</code>。
     * <pre>
     * StringUtil.countMatches(null, *)       = 0
     * StringUtil.countMatches("", *)         = 0
     * StringUtil.countMatches("abba", null)  = 0
     * StringUtil.countMatches("abba", "")    = 0
     * StringUtil.countMatches("abba", "a")   = 2
     * StringUtil.countMatches("abba", "ab")  = 1
     * StringUtil.countMatches("abba", "xxx") = 0
     * </pre>
     * </p>
     * @param str    要扫描的字符串
     * @param subStr 子字符串
     * @return 子串在字符串中出现的次数，如果字符串为<code>null</code>或空，则返回<code>0</code>
     */
    public static int countMatches(String str, String subStr) {
        if ((str == null) || (str.length() == 0) || (subStr == null) || (subStr.length() == 0)) {
            return 0;
        }

        int count = 0;
        int index = 0;

        while ((index = str.indexOf(subStr, index)) != -1) {
            count++;
            index += subStr.length();
        }

        return count;
    }

    /**
     * 去掉字符串中的html源码。<br>
     * @param con  内容
     * @return 去掉后的内容
     */

    public static String clearHTML(String con) {
        String content = con;
        if(con!=null){
            content=con.replaceAll("</?[^>]+>","");//剔出了<html>的标签
            content=content.replace("&nbsp;","");
            content=content.replace(".","");
            content=content.replace("\"","‘");
            content=content.replace("'","‘");
        }
        return content;
    }

    /**
     * 获取字段名对应的setter方法
     * @param name
     * @return
     */
    public static String getSetMethod(String name) {
        if (name == null || name.length() == 0)	return "";

        return name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
    }

    /**
     * 判断是否数字表示
     *
     * @param src
     *            源字符串
     * @return 是否数字的标志
     */
    public static boolean isNumeric(String src) {
        boolean return_value = false;
        if (src != null && src.length() > 0) {
            Matcher m = numericPattern.matcher(src);
            if (m.find()) {
                return_value = true;
            }
        }
        return return_value;
    }

    /**
     * 判断是否数字表示
     *
     * @param src
     *            源字符串
     * @return 是否数字的标志
     */
    public static boolean isNumericString(String src) {
        boolean return_value = false;
        if (src != null && src.length() > 0) {
            Matcher m = numericStringPattern.matcher(src);
            if (m.find()) {
                return_value = true;
            }
        }
        return return_value;
    }

    /**
     * 判断是否纯字母组合
     *
     * @param src
     *            源字符串
     * @return 是否纯字母组合的标志
     */
    public static boolean isABC(String src) {
        boolean return_value = false;
        if (src != null && src.length() > 0) {
            Matcher m = abcPattern.matcher(src);
            if (m.find()) {
                return_value = true;
            }
        }
        return return_value;
    }

    /**
     * 判断是否浮点数字表示
     *
     * @param src
     *            源字符串
     * @return 是否数字的标志
     */
    public static boolean isFloatNumeric(String src) {
        boolean return_value = false;
        if (src != null && src.length() > 0) {
            Matcher m = floatNumericPattern.matcher(src);
            if (m.find()) {
                return_value = true;
            }
        }
        return return_value;
    }

    /**
     * 把string array or list用给定的符号symbol连接成一个字符串
     *
     * @param array
     * @param symbol
     * @return
     */
    public static String joinString(List<?> array, String symbol) {
        String result = "";
        if (array != null) {
            for (int i = 0; i < array.size(); i++) {
                String temp = array.get(i).toString();
                if (temp != null && temp.trim().length() > 0)
                    result += (temp + symbol);
            }
            if (result.length() > 1)
                result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    public static String subStringNotEncode(String subject, int size) {
        if (subject != null && subject.length() > size) {
            subject = subject.substring(0, size) + "...";
        }
        return subject;
    }

    /**
     * 截取字符串　超出的字符用symbol代替 　　
     *
     * @param len
     *            　字符串长度　长度计量单位为一个GBK汉字　　两个英文字母计算为一个单位长度
     * @param str
     * @param symbol
     * @return
     */
    public static String getLimitLengthString(String str, int len, String symbol) {
        int iLen = len * 2;
        int counterOfDoubleByte = 0;
        String strRet = "";
        try {
            if (str != null) {
                byte[] b = str.getBytes("GBK");
                if (b.length <= iLen) {
                    return str;
                }
                for (int i = 0; i < iLen; i++) {
                    if (b[i] < 0) {
                        counterOfDoubleByte++;
                    }
                }
                if (counterOfDoubleByte % 2 == 0) {
                    strRet = new String(b, 0, iLen, "GBK") + symbol;
                    return strRet;
                } else {
                    strRet = new String(b, 0, iLen - 1, "GBK") + symbol;
                    return strRet;
                }
            } else {
                return "";
            }
        } catch (Exception ex) {
            return str.substring(0, len);
        } finally {
            strRet = null;
        }
    }

    /**
     * 截取字符串　超出的字符用symbol代替 　　
     *
     * @param len
     *            　字符串长度　长度计量单位为一个GBK汉字　　两个英文字母计算为一个单位长度
     * @param str
     * @param len
     * @return12
     */
    public static String getLimitLengthString(String str, int len) {
        return getLimitLengthString(str, len, "...");
    }

    /**
     *
     * 截取字符，不转码
     *
     * @param subject
     * @param size
     * @return
     */
    public static String subStrNotEncode(String subject, int size) {
        if (subject.length() > size) {
            subject = subject.substring(0, size);
        }
        return subject;
    }

    /**
     * 把string array or list用给定的符号symbol连接成一个字符串
     *
     * @param array
     * @param symbol
     * @return
     */
    public static String joinString(String[] array, String symbol) {
        String result = "";
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                String temp = array[i];
                if (temp != null && temp.trim().length() > 0)
                    result += (temp + symbol);
            }
            if (result.length() > 1)
                result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * 取得字符串的实际长度（考虑了汉字的情况）
     *
     * @param SrcStr
     *            源字符串
     * @return 字符串的实际长度
     */
    public static int getStringLen(String SrcStr) {
        int return_value = 0;
        if (SrcStr != null) {
            char[] theChars = SrcStr.toCharArray();
            for (int i = 0; i < theChars.length; i++) {
                return_value += (theChars[i] <= 255) ? 1 : 2;
            }
        }
        return return_value;
    }

    /**
     * 检查数据串中是否包含非法字符集
     *
     * @param str
     * @return [true]|[false] 包含|不包含
     */
    public static boolean check(String str) {
        String sIllegal = "'\"";
        int len = sIllegal.length();
        if (null == str)
            return false;
        for (int i = 0; i < len; i++) {
            if (str.indexOf(sIllegal.charAt(i)) != -1)
                return true;
        }

        return false;
    }

    /***************************************************************************
     * getHideEmailPrefix - 隐藏邮件地址前缀。
     *
     * @param email
     *            - EMail邮箱地址 例如: linwenguo@koubei.com 等等...
     * @return 返回已隐藏前缀邮件地址, 如 *********@koubei.com.
     * @version 1.0 (2006.11.27) Wilson Lin
     **************************************************************************/
    public static String getHideEmailPrefix(String email) {
        if (null != email) {
            int index = email.lastIndexOf('@');
            if (index > 0) {
                email = repeat("*", index).concat(email.substring(index));
            }
        }
        return email;
    }

    /***************************************************************************
     * repeat - 通过源字符串重复生成N次组成新的字符串。
     *
     * @param src
     *            - 源字符串 例如: 空格(" "), 星号("*"), "浙江" 等等...
     * @param num
     *            - 重复生成次数
     * @return 返回已生成的重复字符串
     * @version 1.0 (2006.10.10) Wilson Lin
     **************************************************************************/
    public static String repeat(String src, int num) {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < num; i++)
            s.append(src);
        return s.toString();
    }

    /**
     * 根据指定的字符把源字符串分割成一个数组
     *
     * @param src
     * @return
     */
    public static List<String> parseString2ListByCustomerPattern(
            String pattern, String src) {

        if (src == null)
            return null;
        List<String> list = new ArrayList<String>();
        String[] result = src.split(pattern);
        for (int i = 0; i < result.length; i++) {
            list.add(result[i]);
        }
        return list;
    }

    /**
     * 根据指定的字符把源字符串分割成一个数组
     *
     * @param src
     * @return
     */
    public static List<String> parseString2ListByPattern(String src) {
        String pattern = "，|,|、|。";
        return parseString2ListByCustomerPattern(pattern, src);
    }

    /**
     * 格式化一个float
     *
     * @param format
     *            要格式化成的格式 such as #.00, #.#
     */

    public static String formatFloat(float f, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(f);
    }


    public static List<String> splitToList(String split, String src) {
        // 默认,
        String sp = ",";
        if (split != null && split.length() == 1) {
            sp = split;
        }
        List<String> r = new ArrayList<String>();
        int lastIndex = -1;
        int index = src.indexOf(sp);
        if (-1 == index && src != null) {
            r.add(src);
            return r;
        }
        while (index >= 0) {
            if (index > lastIndex) {
                r.add(src.substring(lastIndex + 1, index));
            } else {
                r.add("");
            }

            lastIndex = index;
            index = src.indexOf(sp, index + 1);
            if (index == -1) {
                r.add(src.substring(lastIndex + 1, src.length()));
            }
        }
        return r;
    }

    /**
     * 把 名=值 参数表转换成字符串 (a=1,b=2 =>a=1&b=2)
     *
     * @param map
     * @return
     */
    public static String linkedHashMapToString(LinkedHashMap<String, String> map) {
        if (map != null && map.size() > 0) {
            String result = "";
            Iterator<String> it = map.keySet().iterator();
            while (it.hasNext()) {
                String name = (String) it.next();
                String value = map.get(name);
                result += (result.equals("")) ? "" : "&";
                result += String.format("%s=%s", name, value);
            }
            return result;
        }
        return null;
    }

    /**
     * 解析字符串返回 名称=值的参数表 (a=1&b=2 => a=1,b=2)
     *
     * @param str
     * @return
     */
    public static LinkedHashMap<String, String> toLinkedHashMap(String str) {
        if (str != null && !str.equals("") && str.indexOf("=") > 0) {
            LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();

            String name = null;
            String value = null;
            int i = 0;
            while (i < str.length()) {
                char c = str.charAt(i);
                switch (c) {
                    case 61: // =
                        value = "";
                        break;
                    case 38: // &
                        if (name != null && value != null && !name.equals("")) {
                            result.put(name, value);
                        }
                        name = null;
                        value = null;
                        break;
                    default:
                        if (value != null) {
                            value = (value != null) ? (value + c) : "" + c;
                        } else {
                            name = (name != null) ? (name + c) : "" + c;
                        }
                }
                i++;

            }

            if (name != null && value != null && !name.equals("")) {
                result.put(name, value);
            }

            return result;

        }
        return null;
    }

    /**
     * 根据输入的多个解释和下标返回一个值
     *
     * @param captions
     *            例如:"无,爱干净,一般,比较乱"
     * @param index
     *            1
     * @return 一般
     */
    public static String getCaption(String captions, int index) {
        if (index > 0 && captions != null && !captions.equals("")) {
            String[] ss = captions.split(",");
            if (ss != null && ss.length > 0 && index < ss.length) {
                return ss[index];
            }
        }
        return null;
    }

    /**
     * 数字转字符串,如果num<=0 则输出"";
     *
     * @param num
     * @return
     */
    public static String numberToString(Object num) {
        if (num == null) {
            return null;
        } else if (num instanceof Integer && (Integer) num > 0) {
            return Integer.toString((Integer) num);
        } else if (num instanceof Long && (Long) num > 0) {
            return Long.toString((Long) num);
        } else if (num instanceof Float && (Float) num > 0) {
            return Float.toString((Float) num);
        } else if (num instanceof Double && (Double) num > 0) {
            return Double.toString((Double) num);
        } else {
            return "";
        }
    }

    /**
     * 货币转字符串
     *
     * @param money
     * @param style
     *            样式 [default]要格式化成的格式 such as #.00, #.#
     * @return
     */

    public static String moneyToString(Object money, String style) {
        if (money != null && style != null
                && (money instanceof Double || money instanceof Float)) {
            Double num = (Double) money;

            if (style.equalsIgnoreCase("default")) {
                // 缺省样式 0 不输出 ,如果没有输出小数位则不输出.0
                if (num == 0) {
                    // 不输出0
                    return "";
                } else if ((num * 10 % 10) == 0) {
                    // 没有小数
                    return Integer.toString(num.intValue());
                } else {
                    // 有小数
                    return num.toString();
                }

            } else {
                DecimalFormat df = new DecimalFormat(style);
                return df.format(num);
            }
        }
        return null;
    }

    /**
     * 在sou中是否存在finds 如果指定的finds字符串有一个在sou中找到,返回true;
     *
     * @param sou
     * @param finds
     * @return
     */
    public static boolean strPos(String sou, String... finds) {
        if (sou != null && finds != null && finds.length > 0) {
            for (int i = 0; i < finds.length; i++) {
                if (sou.indexOf(finds[i]) > -1)
                    return true;
            }
        }
        return false;
    }

    public static boolean strPos(String sou, List<String> finds) {
        if (sou != null && finds != null && finds.size() > 0) {
            for (String s : finds) {
                if (sou.indexOf(s) > -1)
                    return true;
            }
        }
        return false;
    }

    public static boolean strPos(String sou, String finds) {
        List<String> t = splitToList(",", finds);
        return strPos(sou, t);
    }

    /**
     * 判断两个字符串是否相等 如果都为null则判断为相等,一个为null另一个not null则判断不相等 否则如果s1=s2则相等
     *
     * @param s1
     * @param s2
     * @return
     */
    public static boolean equals(String s1, String s2) {
        if (StringUtil.isEmpty(s1) && StringUtil.isEmpty(s2)) {
            return true;
        } else if (!StringUtil.isEmpty(s1) && !StringUtil.isEmpty(s2)) {
            return s1.equals(s2);
        }
        return false;
    }

    /**
     * 返回小写
     * @param str
     * @return
     */
    public static String lowerCase(String str) {
        return (null == str) ? "" : str.toLowerCase();
    }

    /**
     * 返回大写
     * @param str
     * @return
     */
    public static String upperCase(String str) {
        return (null == str) ? "" : str.toUpperCase();
    }

    public static int toInt(String s) {
        if (s != null && !"".equals(s.trim())) {
            try {
                return Integer.parseInt(s);
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    public static double toDouble(String s) {
        if (s != null && !"".equals(s.trim())) {
            return Double.parseDouble(s);
        }
        return 0;
    }

    public static boolean isPhone(String phone) {
        if (phone == null && "".equals(phone)) {
            return false;
        }
        String[] strPhone = phone.split("-");
        try {
            for (int i = 0; i < strPhone.length; i++) {
                Long.parseLong(strPhone[i]);
            }

        } catch (Exception e) {
            return false;
        }
        return true;

    }

    /**
     * 把xml 转为object
     *
     * @param xml
     * @return
     */
    public static Object xmlToObject(String xml) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(xml
                    .getBytes("UTF8"));
            decoder = new XMLDecoder(new BufferedInputStream(in));
            return decoder.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long toLong(String s) {
        try {
            if (s != null && !"".equals(s.trim()))
                return Long.parseLong(s);
        } catch (Exception exception) {
        }
        return 0L;
    }

    public static String simpleEncrypt(String str) {
        if (str != null && str.length() > 0) {
            // str = str.replaceAll("0","a");
            str = str.replaceAll("1", "b");
            // str = str.replaceAll("2","c");
            str = str.replaceAll("3", "d");
            // str = str.replaceAll("4","e");
            str = str.replaceAll("5", "f");
            str = str.replaceAll("6", "g");
            str = str.replaceAll("7", "h");
            str = str.replaceAll("8", "i");
            str = str.replaceAll("9", "j");
        }
        return str;

    }


    public static String removeURL(String str) {
        if (str != null)
            str = str.toLowerCase()
                    .replaceAll("(http|www|com|cn|org|\\.)+", "");
        return str;
    }


    public static String replaceWapStr(String str) {
        if (str != null) {
            str = str.replaceAll("<span class=\"keyword\">", "");
            str = str.replaceAll("</span>", "");
            str = str.replaceAll("<strong class=\"keyword\">", "");
            str = str.replaceAll("<strong>", "");
            str = str.replaceAll("</strong>", "");

            str = str.replace('$', '＄');

            str = str.replaceAll("&amp;", "＆");
            str = str.replace('&', '＆');

            str = str.replace('<', '＜');

            str = str.replace('>', '＞');

        }
        return str;
    }

    /**
     * 字符串转float 如果异常返回0.00
     *
     * @param s
     *            输入的字符串
     * @return 转换后的float
     */
    public static Float toFloat(String s) {
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {
            return new Float(0);
        }
    }


    public static String replaceBlank(String str) {
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            str = m.replaceAll("");
        }
        return str;
    }

    public static String Q2B(String QJstr) {
        String outStr = "";
        String Tstr = "";
        byte[] b = null;
        for (int i = 0; i < QJstr.length(); i++) {
            try {
                Tstr = QJstr.substring(i, i + 1);
                b = Tstr.getBytes("unicode");
            } catch (UnsupportedEncodingException e) {

            }
            if (b[3] == -1) {
                b[2] = (byte) (b[2] + 32);
                b[3] = 0;
                try {
                    outStr = outStr + new String(b, "unicode");
                } catch (UnsupportedEncodingException ex) {

                }
            } else {
                outStr = outStr + Tstr;
            }
        }
        return outStr;
    }

    /**
     *
     * 转换编码
     *
     * @param s
     *            源字符串
     * @param fencode
     *            源编码格式
     * @param bencode
     *            目标编码格式
     * @return 目标编码
     */
    public static String changCoding(String s, String fencode, String bencode) {
        try {
            String str = new String(s.getBytes(fencode), bencode);
            return str;
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }

    /**
     * *************************************************************************
     * 修改：刘黎明 修改时间:2007/3/1
     *
     * @param str
     * @return
     *************************************************************************
     */
    public static String removeHTMLLableExe(String str) {
        str = stringReplace(str, ">\\s*<", "><");
        str = stringReplace(str, "&nbsp;", " ");// 替换空格
        str = stringReplace(str, "<br ?/?>", "\n");// 去<br><br />
        str = stringReplace(str, "<([^<>]+)>", "");// 去掉<>内的字符
        str = stringReplace(str, "\\s\\s\\s*", " ");// 将多个空白变成一个空格
        str = stringReplace(str, "^\\s*", "");// 去掉头的空白
        str = stringReplace(str, "\\s*$", "");// 去掉尾的空白
        str = stringReplace(str, " +", " ");
        return str;
    }

    /**
     * 除去html标签
     *
     * @param str
     *            源字符串
     * @return 目标字符串
     */
    public static String removeHTMLLable(String str) {
        str = stringReplace(str, "\\s", "");// 去掉页面上看不到的字符
        str = stringReplace(str, "<br ?/?>", "\n");// 去<br><br />
        str = stringReplace(str, "<([^<>]+)>", "");// 去掉<>内的字符
        str = stringReplace(str, "&nbsp;", " ");// 替换空格
        str = stringReplace(str, "&(\\S)(\\S?)(\\S?)(\\S?);", "");// 去<br><br />
        return str;
    }

    /**
     * 去掉HTML标签之外的字符串
     *
     * @param str
     *            源字符串
     * @return 目标字符串
     */
    public static String removeOutHTMLLable(String str) {
        str = stringReplace(str, ">([^<>]+)<", "><");
        str = stringReplace(str, "^([^<>]+)<", "<");
        str = stringReplace(str, ">([^<>]+)$", ">");
        return str;
    }

    /**
     *
     * 字符串替换
     *
     * @param str
     *            源字符串
     * @param sr
     *            正则表达式样式
     * @param sd
     *            替换文本
     * @return 结果串
     */
    public static String stringReplace(String str, String sr, String sd) {
        String regEx = sr;
        Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        str = m.replaceAll(sd);
        return str;
    }

    /**
     *
     * 将html的省略写法替换成非省略写法
     *
     * @param str
     *            html字符串
     * @param pt
     *            标签如table
     * @return 结果串
     */
    public static String fomateToFullForm(String str, String pt) {
        String regEx = "<" + pt + "\\s+([\\S&&[^<>]]*)/>";
        Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        String[] sa = null;
        String sf = "";
        String sf2 = "";
        String sf3 = "";
        for (; m.find();) {
            sa = p.split(str);
            if (sa == null) {
                break;
            }
            sf = str.substring(sa[0].length(), str
                    .indexOf("/>", sa[0].length()));
            sf2 = sf + "></" + pt + ">";
            sf3 = str.substring(sa[0].length() + sf.length() + 2);
            str = sa[0] + sf2 + sf3;
            sa = null;
        }
        return str;
    }

    /**
     *
     * 得到字符串的子串位置序列
     *
     * @param str
     *            字符串
     * @param sub
     *            子串
     * @param b
     *            true子串前端,false子串后端
     * @return 字符串的子串位置序列
     */
    public static int[] getSubStringPos(String str, String sub, boolean b) {
        // int[] i = new int[(new Integer((str.length()-stringReplace( str , sub
        // , "" ).length())/sub.length())).intValue()] ;
        String[] sp = null;
        int l = sub.length();
        sp = splitString(str, sub);
        if (sp == null) {
            return null;
        }
        int[] ip = new int[sp.length - 1];
        for (int i = 0; i < sp.length - 1; i++) {
            ip[i] = sp[i].length() + l;
            if (i != 0) {
                ip[i] += ip[i - 1];
            }
        }
        if (b) {
            for (int j = 0; j < ip.length; j++) {
                ip[j] = ip[j] - l;
            }
        }
        return ip;
    }

    /**
     *
     * 根据正则表达式分割字符串
     *
     * @param str
     *            源字符串
     * @param ms
     *            正则表达式
     * @return 目标字符串组
     */
    public static String[] splitString(String str, String ms) {
        String regEx = ms;
        Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        String[] sp = p.split(str);
        return sp;
    }


    // ★传入一个字符串，把符合pattern格式的字符串放入字符串数组
    // java.utils.regex是一个用正则表达式所订制的模式来对字符串进行匹配工作的类库包
    public static String[] getStringArrayByPattern(String str, String pattern) {
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(str);
        // 范型
        Set<String> result = new HashSet<String>();// 目的是：相同的字符串只返回一个。。。 不重复元素
        // boolean find() 尝试在目标字符串里查找下一个匹配子串。
        while (matcher.find()) {
            for (int i = 0; i < matcher.groupCount(); i++) { // int groupCount()
                // 返回当前查找所获得的匹配组的数量。
                // System.out.println(matcher.group(i));
                result.add(matcher.group(i));

            }
        }
        String[] resultStr = null;
        if (result.size() > 0) {
            resultStr = new String[result.size()];
            return result.toArray(resultStr);// 将Set result转化为String[] resultStr
        }
        return resultStr;

    }



    public static String[] midString(String s, String b, String e) {
        int i = s.indexOf(b) + b.length();
        int j = s.indexOf(e, i);
        String[] sa = new String[2];
        if (i < b.length() || j < i + 1 || i > j) {
            sa[1] = s;
            sa[0] = null;
            return sa;
        } else {
            sa[0] = s.substring(i, j);
            sa[1] = s.substring(j);
            return sa;
        }
    }


    public static String stringReplace(String s, String pf, String pb, int start) {
        Pattern pattern_hand = Pattern.compile(pf);
        Matcher matcher_hand = pattern_hand.matcher(s);
        int gc = matcher_hand.groupCount();
        int pos = start;
        String sf1 = "";
        String sf2 = "";
        String sf3 = "";
        int if1 = 0;
        String strr = "";
        while (matcher_hand.find(pos)) {
            sf1 = matcher_hand.group();
            if1 = s.indexOf(sf1, pos);
            if (if1 >= pos) {
                strr += s.substring(pos, if1);
                pos = if1 + sf1.length();
                sf2 = pb;
                for (int i = 1; i <= gc; i++) {
                    sf3 = "\\" + i;
                    sf2 = replaceAll(sf2, sf3, matcher_hand.group(i));
                }
                strr += sf2;
            } else {
                return s;
            }
        }
        strr = s.substring(0, start) + strr;
        return strr;
    }


    public static String replaceAll(String s, String sf, String sb) {
        int i = 0, j = 0;
        int l = sf.length();
        boolean b = true;
        boolean o = true;
        String str = "";
        do {
            j = i;
            i = s.indexOf(sf, j);
            if (i > j) {
                str += s.substring(j, i);
                str += sb;
                i += l;
                o = false;
            } else {
                str += s.substring(j);
                b = false;
            }
        } while (b);
        if (o) {
            str = s;
        }
        return str;
    }


    public static boolean isMatch(String str, String pattern) {
        Pattern pattern_hand = Pattern.compile(pattern);
        Matcher matcher_hand = pattern_hand.matcher(str);
        boolean b = matcher_hand.matches();
        return b;
    }


    public static String subStringExe(String s, String jmp, String sb, String se) {
        if (isEmpty(s)) {
            return "";
        }
        int i = s.indexOf(jmp);
        if (i >= 0 && i < s.length()) {
            s = s.substring(i + 1);
        }
        i = s.indexOf(sb);
        if (i >= 0 && i < s.length()) {
            s = s.substring(i + 1);
        }
        if (se == "") {
            return s;
        } else {
            i = s.indexOf(se);
            if (i >= 0 && i < s.length()) {
                s = s.substring(i + 1);
            }
            return s;
        }
    }


    public static String URLEncode(String src) {
        String return_value = "";
        try {
            if (src != null) {
                return_value = URLEncoder.encode(src, "GBK");

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return_value = src;
        }

        return return_value;
    }

    public static String getGBK(String str) {

        return transfer(str);
    }

    public static String transfer(String str) {
        Pattern p = Pattern.compile("&#\\d+;");
        Matcher m = p.matcher(str);
        while (m.find()) {
            String old = m.group();
            str = str.replaceAll(old, getChar(old));
        }
        return str;
    }

    public static String getChar(String str) {
        String dest = str.substring(2, str.length() - 1);
        char ch = (char) Integer.parseInt(dest);
        return "" + ch;
    }


    public static String subYhooString(String subject, int size) {
        subject = subject.substring(1, size);
        return subject;
    }

    public static String subYhooStringDot(String subject, int size) {
        subject = subject.substring(1, size) + "...";
        return subject;
    }


    public static <T> String listTtoString(List<T> list) {
        if (list == null || list.size() < 1)
            return "";
        Iterator<T> i = list.iterator();
        if (!i.hasNext())
            return "";
        StringBuilder sb = new StringBuilder();
        for (;;) {
            T e = i.next();
            sb.append(e);
            if (!i.hasNext())
                return sb.toString();
            sb.append(",");
        }
    }


    public static String intArraytoString(int[] a) {
        if (a == null)
            return "";
        int iMax = a.length - 1;
        if (iMax == -1)
            return "";
        StringBuilder b = new StringBuilder();
        for (int i = 0;; i++) {
            b.append(a[i]);
            if (i == iMax)
                return b.toString();
            b.append(",");
        }
    }

    public static boolean isContentRepeat(String content) {
        int similarNum = 0;
        int forNum = 0;
        int subNum = 0;
        int thousandNum = 0;
        String startStr = "";
        String nextStr = "";
        boolean result = false;
        float endNum = (float) 0.0;
        if (content != null && content.length() > 0) {
            if (content.length() % 1000 > 0)
                thousandNum = (int) Math.floor(content.length() / 1000) + 1;
            else
                thousandNum = (int) Math.floor(content.length() / 1000);
            if (thousandNum < 3)
                subNum = 100 * thousandNum;
            else if (thousandNum < 6)
                subNum = 200 * thousandNum;
            else if (thousandNum < 9)
                subNum = 300 * thousandNum;
            else
                subNum = 3000;
            for (int j = 1; j < subNum; j++) {
                if (content.length() % j > 0)
                    forNum = (int) Math.floor(content.length() / j) + 1;
                else
                    forNum = (int) Math.floor(content.length() / j);
                if (result || j >= content.length())
                    break;
                else {
                    for (int m = 0; m < forNum; m++) {
                        if (m * j > content.length()
                                || (m + 1) * j > content.length()
                                || (m + 2) * j > content.length())
                            break;
                        startStr = content.substring(m * j, (m + 1) * j);
                        nextStr = content.substring((m + 1) * j, (m + 2) * j);
                        if (startStr.equals(nextStr)) {
                            similarNum = similarNum + 1;
                            endNum = (float) similarNum / forNum;
                            if (endNum > 0.4) {
                                result = true;
                                break;
                            }
                        } else
                            similarNum = 0;
                    }
                }
            }
        }
        return result;
    }


    public static String AsciiToChar(int asc) {
        String TempStr = "A";
        char tempchar = (char) asc;
        TempStr = String.valueOf(tempchar);
        return TempStr;
    }


    public static String isEmpty(String s, String result) {
        if (s != null && !s.equals("")) {
            return s;
        }
        return result;
    }


    public static String htmlcodeToSpecialchars(String str) {
        str = str.replaceAll("&amp;", "&");
        str = str.replaceAll("&quot;", "\"");
        str = str.replaceAll("&#039;", "'");
        str = str.replaceAll("&lt;", "<");
        str = str.replaceAll("&gt;", ">");
        return str;
    }


    public static String removeHtmlTag(String htmlstr) {
        Pattern pat = Pattern.compile("\\s*<.*?>\\s*", Pattern.DOTALL
                | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE); // \\s?[s|Sc|Cr|Ri|Ip|Pt|T]
        Matcher m = pat.matcher(htmlstr);
        String rs = m.replaceAll("");
        rs = rs.replaceAll("&nbsp", " ");
        rs = rs.replaceAll("&lt;", "<");
        rs = rs.replaceAll("&gt;", ">");
        return rs;
    }


    public static String getStrAfterRegex(String captions, String regex) {
        if (!isEmpty(captions) && !isEmpty(regex)) {
            int pos = captions.indexOf(regex);
            if (pos != -1 && pos < captions.length() - 1) {
                return captions.substring(pos + 1);
            }
        }
        return "";
    }


    public static String byte2hex(byte bytes[]) {
        StringBuffer retString = new StringBuffer();
        for (int i = 0; i < bytes.length; ++i) {
            retString.append(Integer.toHexString(0x0100 + (bytes[i] & 0x00FF))
                    .substring(1).toUpperCase());
        }
        return retString.toString();
    }

    public static byte[] hex2byte(String hex) {
        byte[] bts = new byte[hex.length() / 2];
        for (int i = 0; i < bts.length; i++) {
            bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2),
                    16);
        }
        return bts;
    }


    public static String getStringByInt(int length, String data) {
        String s_data = "";
        int datalength = data.length();
        if (length > 0 && length >= datalength) {
            for (int i = 0; i < length - datalength; i++) {
                s_data += "0";
            }
            s_data += data;
        }

        return s_data;
    }


    public static boolean isNumericAndCanNull(String src) {
        Pattern numericPattern = Pattern.compile("^[0-9]+$");
        if (src == null || src.equals(""))
            return true;
        boolean return_value = false;
        if (src != null && src.length() > 0) {
            Matcher m = numericPattern.matcher(src);
            if (m.find()) {
                return_value = true;
            }
        }
        return return_value;
    }


    public static boolean isFloatAndCanNull(String src) {
        Pattern numericPattern = Pattern
                .compile("^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$");
        if (src == null || src.equals(""))
            return true;
        boolean return_value = false;
        if (src != null && src.length() > 0) {
            Matcher m = numericPattern.matcher(src);
            if (m.find()) {
                return_value = true;
            }
        }
        return return_value;
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !str.equals("");
    }

    public static boolean isDate(String date) {
        String regEx = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(date);
        boolean result = m.find();
        return result;
    }

    public static boolean isFormatDate(String date, String regEx) {
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(date);
        boolean result = m.find();
        return result;
    }


    public static String listToString(List<Integer> list) {
        String str = "";
        if (list != null && list.size() > 0) {
            for (int id : list) {
                str = str + id + ",";
            }
            if (!"".equals(str) && str.length() > 0)
                str = str.substring(0, str.length() - 1);
        }
        return str;
    }


    public static String replaceStr(String str) {
        if (str != null && str.length() > 0) {
            str = str.replaceAll("~", ",");
            str = str.replaceAll(" ", ",");
            str = str.replaceAll("　", ",");
            str = str.replaceAll(" ", ",");
            str = str.replaceAll("`", ",");
            str = str.replaceAll("!", ",");
            str = str.replaceAll("@", ",");
            str = str.replaceAll("#", ",");
            str = str.replaceAll("\\$", ",");
            str = str.replaceAll("%", ",");
            str = str.replaceAll("\\^", ",");
            str = str.replaceAll("&", ",");
            str = str.replaceAll("\\*", ",");
            str = str.replaceAll("\\(", ",");
            str = str.replaceAll("\\)", ",");
            str = str.replaceAll("-", ",");
            str = str.replaceAll("_", ",");
            str = str.replaceAll("=", ",");
            str = str.replaceAll("\\+", ",");
            str = str.replaceAll("\\{", ",");
            str = str.replaceAll("\\[", ",");
            str = str.replaceAll("\\}", ",");
            str = str.replaceAll("\\]", ",");
            str = str.replaceAll("\\|", ",");
            str = str.replaceAll("\\\\", ",");
            str = str.replaceAll(";", ",");
            str = str.replaceAll(":", ",");
            str = str.replaceAll("'", ",");
            str = str.replaceAll("\\\"", ",");
            str = str.replaceAll("<", ",");
            str = str.replaceAll(">", ",");
            str = str.replaceAll("\\.", ",");
            str = str.replaceAll("\\?", ",");
            str = str.replaceAll("/", ",");
            str = str.replaceAll("～", ",");
            str = str.replaceAll("`", ",");
            str = str.replaceAll("！", ",");
            str = str.replaceAll("＠", ",");
            str = str.replaceAll("＃", ",");
            str = str.replaceAll("＄", ",");
            str = str.replaceAll("％", ",");
            str = str.replaceAll("︿", ",");
            str = str.replaceAll("＆", ",");
            str = str.replaceAll("×", ",");
            str = str.replaceAll("（", ",");
            str = str.replaceAll("）", ",");
            str = str.replaceAll("－", ",");
            str = str.replaceAll("＿", ",");
            str = str.replaceAll("＋", ",");
            str = str.replaceAll("＝", ",");
            str = str.replaceAll("｛", ",");
            str = str.replaceAll("［", ",");
            str = str.replaceAll("｝", ",");
            str = str.replaceAll("］", ",");
            str = str.replaceAll("｜", ",");
            str = str.replaceAll("＼", ",");
            str = str.replaceAll("：", ",");
            str = str.replaceAll("；", ",");
            str = str.replaceAll("＂", ",");
            str = str.replaceAll("＇", ",");
            str = str.replaceAll("＜", ",");
            str = str.replaceAll("，", ",");
            str = str.replaceAll("＞", ",");
            str = str.replaceAll("．", ",");
            str = str.replaceAll("？", ",");
            str = str.replaceAll("／", ",");
            str = str.replaceAll("·", ",");
            str = str.replaceAll("￥", ",");
            str = str.replaceAll("……", ",");
            str = str.replaceAll("（", ",");
            str = str.replaceAll("）", ",");
            str = str.replaceAll("——", ",");
            str = str.replaceAll("-", ",");
            str = str.replaceAll("【", ",");
            str = str.replaceAll("】", ",");
            str = str.replaceAll("、", ",");
            str = str.replaceAll("”", ",");
            str = str.replaceAll("’", ",");
            str = str.replaceAll("《", ",");
            str = str.replaceAll("》", ",");
            str = str.replaceAll("“", ",");
            str = str.replaceAll("。", ",");
        }
        return str;
    }


    public static String full2Half(String str) {
        if (str == null || "".equals(str))
            return "";
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (c >= 65281 && c < 65373)
                sb.append((char) (c - 65248));
            else
                sb.append(str.charAt(i));
        }

        return sb.toString();

    }


    public static String replaceBracketStr(String str) {
        if (str != null && str.length() > 0) {
            str = str.replaceAll("（", "(");
            str = str.replaceAll("）", ")");
        }
        return str;
    }

    /**
     * 分割字符，从开始到第一个split字符串为止
     *
     * @param src
     *            源字符串
     * @param split
     *            截止字符串
     * @return
     */
    public static String subStr(String src, String split) {
        if (!isEmpty(src)) {
            int index = src.indexOf(split);
            if (index >= 0) {
                return src.substring(0, index);
            }
        }
        return src;
    }


    public static String getKeyWord(String params, String qString) {
        String keyWord = "";
        if (qString != null) {
            String param = params + "=";
            int i = qString.indexOf(param);
            if (i != -1) {
                int j = qString.indexOf("&", i + param.length());
                if (j > 0) {
                    keyWord = qString.substring(i + param.length(), j);
                }
            }
        }
        return keyWord;
    }


    public static Map<String, String> parseQuery(String query, char split1,
                                                 char split2, String dupLink) {
        if (!isEmpty(query) && query.indexOf(split2) > 0) {
            Map<String, String> result = new HashMap<String, String>();

            String name = null;
            String value = null;
            String tempValue = "";
            int len = query.length();
            for (int i = 0; i < len; i++) {
                char c = query.charAt(i);
                if (c == split2) {
                    value = "";
                } else if (c == split1) {
                    if (!isEmpty(name) && value != null) {
                        if (dupLink != null) {
                            tempValue = result.get(name);
                            if (tempValue != null) {
                                value += dupLink + tempValue;
                            }
                        }
                        result.put(name, value);
                    }
                    name = null;
                    value = null;
                } else if (value != null) {
                    value += c;
                } else {
                    name = (name != null) ? (name + c) : "" + c;
                }
            }

            if (!isEmpty(name) && value != null) {
                if (dupLink != null) {
                    tempValue = result.get(name);
                    if (tempValue != null) {
                        value += dupLink + tempValue;
                    }
                }
                result.put(name, value);
            }

            return result;
        }
        return null;
    }

    /**
     * 将list 用传入的分隔符组装为String
     *
     * @param list
     * @param slipStr
     * @return String
     */
    public static String listToStringSlipStr(List<?> list, String slipStr) {
        StringBuffer returnStr = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                returnStr.append(list.get(i)).append(slipStr);
            }
        }
        if (returnStr.toString().length() > 0)
            return returnStr.toString().substring(0,
                    returnStr.toString().lastIndexOf(slipStr));
        else
            return "";
    }

    /**
     * 获取从start开始用*替换len个长度后的字符串
     *
     * @param str
     *            要替换的字符串
     * @param start
     *            开始位置
     * @param len
     *            长度
     * @return 替换后的字符串
     */
    public static String getMaskStr(String str, int start, int len) {
        if (StringUtil.isEmpty(str)) {
            return str;
        }
        if (str.length() < start) {
            return str;
        }

        // 获取*之前的字符串
        String ret = str.substring(0, start);

        // 获取最多能打的*个数
        int strLen = str.length();
        if (strLen < start + len) {
            len = strLen - start;
        }

        // 替换成*
        for (int i = 0; i < len; i++) {
            ret += "*";
        }

        // 加上*之后的字符串
        if (strLen > start + len) {
            ret += str.substring(start + len);
        }

        return ret;
    }

    /**
     * 根据传入的分割符号,把传入的字符串分割为List字符串
     *
     * @param slipStr
     *            分隔的字符串
     * @param src
     *            字符串
     * @return 列表
     */
    public static List<String> stringToStringListBySlipStr(String slipStr,
                                                           String src) {

        if (src == null)
            return null;
        List<String> list = new ArrayList<String>();
        String[] result = src.split(slipStr);
        for (int i = 0; i < result.length; i++) {
            list.add(result[i]);
        }
        return list;
    }

    /**
     * 截取字符串
     *
     * @param str
     *            原始字符串
     * @param len
     *            要截取的长度
     * @param tail
     *            结束加上的后缀
     * @return 截取后的字符串
     */
    public static String getHtmlSubString(String str, int len, String tail) {
        if (str == null || str.length() <= len) {
            return str;
        }
        int length = str.length();
        char c = ' ';
        String tag = null;
        String name = null;
        int size = 0;
        String result = "";
        boolean isTag = false;
        List<String> tags = new ArrayList<String>();
        int i = 0;
        for (int end = 0, spanEnd = 0; i < length && len > 0; i++) {
            c = str.charAt(i);
            if (c == '<') {
                end = str.indexOf('>', i);
            }

            if (end > 0) {
                // 截取标签
                tag = str.substring(i, end + 1);
                int n = tag.length();
                if (tag.endsWith("/>")) {
                    isTag = true;
                } else if (tag.startsWith("</")) { // 结束符
                    name = tag.substring(2, end - i);
                    size = tags.size() - 1;
                    // 堆栈取出html开始标签
                    if (size >= 0 && name.equals(tags.get(size))) {
                        isTag = true;
                        tags.remove(size);
                    }
                } else { // 开始符
                    spanEnd = tag.indexOf(' ', 0);
                    spanEnd = spanEnd > 0 ? spanEnd : n;
                    name = tag.substring(1, spanEnd);
                    if (name.trim().length() > 0) {
                        // 如果有结束符则为html标签
                        spanEnd = str.indexOf("</" + name + ">", end);
                        if (spanEnd > 0) {
                            isTag = true;
                            tags.add(name);
                        }
                    }
                }
                // 非html标签字符
                if (!isTag) {
                    if (n >= len) {
                        result += tag.substring(0, len);
                        break;
                    } else {
                        len -= n;
                    }
                }

                result += tag;
                isTag = false;
                i = end;
                end = 0;
            } else { // 非html标签字符
                len--;
                result += c;
            }
        }
        // 添加未结束的html标签
        for (String endTag : tags) {
            result += "</" + endTag + ">";
        }
        if (i < length) {
            result += tail;
        }
        return result;
    }

  




}