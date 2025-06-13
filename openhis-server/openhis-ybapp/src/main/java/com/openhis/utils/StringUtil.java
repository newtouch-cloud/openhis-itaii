package com.openhis.utils;


import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 */
public final class StringUtil {
    public static final int XFF00 = 65280;
    public static final int XF0 = 240;
    public static final int X0F = 15;
    public static final int XFF = 255;
    public static final int X000000FF = 255;
    public static final int X0000FF00 = 65280;
    public static final int SIXTEEN = 16;
    public static final char[] HEX_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public StringUtil() {
    }

    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }

        return sb.toString();
    }

    public static String getRandomNumber(int length) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }

        return sb.toString();
    }

    public static int[] splitToIntArray(String s, String delim) {
        String[] stringValueArray = split(s, delim);
        int[] intValueArray = new int[stringValueArray.length];

        for (int i = 0; i < intValueArray.length; ++i) {
            intValueArray[i] = Integer.parseInt(stringValueArray[i]);
        }

        return intValueArray;
    }

    public static String[] split(String source, String div) {
        int arynum = 0;
        int div_length = div.length();
        int intIdx;
        if (source.compareTo("") != 0) {
            if (source.indexOf(div) != -1) {
                intIdx = source.indexOf(div);

                for (int var6 = 1; source.indexOf(div, intIdx + div_length) != -1; arynum = var6++) {
                    intIdx = source.indexOf(div, intIdx + div_length);
                }

                arynum += 2;
            } else {
                arynum = 1;
            }
        } else {
            arynum = 0;
        }

        String[] returnStr = new String[arynum];
        if (source.compareTo("") == 0) {
            return returnStr;
        } else if (source.indexOf(div) == -1) {
            returnStr[0] = source.substring(0, source.length());
            return returnStr;
        } else {
            intIdx = source.indexOf(div);
            returnStr[0] = source.substring(0, intIdx);

            int intCount;
            for (intCount = 1; source.indexOf(div, intIdx + div_length) != -1; ++intCount) {
                int intIdex = source.indexOf(div, intIdx + div_length);
                returnStr[intCount] = source.substring(intIdx + div_length, intIdex);
                intIdx = source.indexOf(div, intIdx + div_length);
            }

            returnStr[intCount] = source.substring(intIdx + div_length, source.length());
            return returnStr;
        }
    }

    public static int doNullInt(String srcInt) {
        return srcInt != null && !"".equals(srcInt) ? Integer.parseInt(srcInt) : 0;
    }

    public static int doNullInt(Object obj) {
        String srcInt = doNullStr(obj);
        return srcInt != null && !"".equals(srcInt) ? Integer.parseInt(srcInt) : 0;
    }

    public static long doNullLong(String srcInt) {
        return srcInt != null && !"".equals(srcInt) ? Long.parseLong(srcInt) : 0L;
    }

    public static long doNullLong(Object obj) {
        String srcInt = doNullStr(obj);
        return srcInt != null && !"".equals(srcInt) ? Long.parseLong(srcInt) : 0L;
    }

    public static String doNullStr(Object obj) {
        String str = "";
        if (obj != null) {
            str = String.valueOf(obj);
            if (str.equals("null")) {
                str = "";
            }
        }

        return str;
    }

    public static Integer doNullInteger(Object obj) {
        String str = doNullStr(obj);
        if (isEmpty(str)) {
            str = "0";
        } else {
            int i = str.indexOf(".");
            if (i > 0) {
                str = str.substring(0, i);
            }
        }

        return Integer.valueOf(str);
    }

    public static boolean isEmpty(String[] string) {
        return string == null || string.length == 0;
    }

    public static boolean isEmpty(String string) {
        return string == null || "".equals(string.trim()) || "null".equals(string.trim());
    }

    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }

    public static boolean isEmpty(Object o) {
        return o == null || "".equals(o);
    }

    public static String padRight(String value, int totalWidth, char paddingChar) {
        String temp = value;
        if (value.length() > totalWidth) {
            return value;
        } else {
            while (temp.length() < totalWidth) {
                temp = temp + paddingChar;
            }

            return temp;
        }
    }

    public static String padLeft(String value, int totalWidth, char paddingChar) {
        String temp = value;
        if (value.length() > totalWidth) {
            return value;
        } else {
            while (temp.length() < totalWidth) {
                temp = paddingChar + temp;
            }

            return temp;
        }
    }

    public static String reTrimByString(String value) {
        String reValue;
        if (value != null && !value.equals("")) {
            reValue = value.trim();
        } else {
            reValue = "";
        }

        return reValue;
    }

    public static String reTrimByObject(Object obj) {
        String reValue;
        if (obj != null && !obj.equals("")) {
            reValue = String.valueOf(obj).trim();
        } else {
            reValue = "";
        }

        return reValue;
    }

    public static int indexOfStringArray(String[] strArr, String str) {
        int index = -1;
        if (strArr != null && str != null) {
            for (int i = 0; i < strArr.length; ++i) {
                if (str.equals(strArr[i])) {
                    index = i;
                    break;
                }
            }
        }

        return index;
    }

    public static String replaceFirst(String whole, String strold, String strnew) {
        if (whole.indexOf(strold) > -1 && strnew != null) {
            String whole_one = whole.substring(0, whole.indexOf(strold));
            String whole_two = whole.substring(whole.indexOf(strold) + strold.length());
            whole = whole_one + strnew + whole_two;
        }

        return whole;
    }

    public static Long[] convertionToLong(String[] strs) {
        Long[] longs = null;
        if (!isEmpty(strs)) {
            longs = new Long[strs.length];

            for (int i = 0; i < strs.length; ++i) {
                String str = strs[i];
                long thelong = Long.valueOf(str).longValue();
                longs[i] = thelong;
            }
        }

        return longs;
    }

    public static Long[] convertionToLongArr(String strs, String splitChar) {
        if (isEmpty(splitChar)) {
            splitChar = ",";
        }

        Long[] result = null;
        if (!isEmpty(strs)) {
            String[] ids = strs.split(splitChar);
            result = new Long[ids.length];

            for (int i = 0; i < ids.length; ++i) {
                result[i] = new Long(ids[i]);
            }
        }

        return result;
    }

    public static String[] decodeStringToArray(String str, String div) {
        ArrayList<String> array = new ArrayList();
        StringTokenizer fenxi = new StringTokenizer(str, div);

        while (fenxi.hasMoreTokens()) {
            String s1 = fenxi.nextToken();
            array.add(s1);
        }

        String[] result = new String[array.size()];

        for (int i = 0; i < result.length; ++i) {
            result[i] = (String) array.get(i);
        }

        return result;
    }

    public static String convertionLongToString(Long[] l, String splitChar) {
        String result = null;
        if (l != null) {
            result = Arrays.toString(l);
            result = result.substring(1, result.length() - 1);
            if (!isEmpty(splitChar)) {
                result = result.replaceAll(",", splitChar);
            }
        }

        return result;
    }

    public static String convertionObjectArrayToStr(Object[] strings, String regx) {
        String result = null;
        if (regx != null) {
            result = Arrays.toString(strings);
            result = result.substring(1, result.length() - 1);
            if (!isEmpty(regx)) {
                result = result.replaceAll(",", regx);
            }
        }

        return result;
    }

    public static String charEncoding(String str) {
        try {
            str = URLDecoder.decode(str, "UTF-8");
        } catch (Exception var2) {
            str = null;
        }

        return str;
    }

    public static String getStrTransMean(String str, String sregex, String sreplace) {
        if (!isEmpty(str)) {
            str = str.replaceAll(sregex, sreplace);
        }

        return str;
    }

    public static String replaceSpecialChar(String s) {
        return s.replaceAll("/|\\\\|\\$|#|&|%|\\*|\\^|;|,|<|>|&|'|\"", "");
    }

    public static String replaceSpecialCode(String s) {
        return null != s && !"".equals(s) ? s.replaceAll("<|>|\"|%|;|\\(|\\)|&|'|\\+|\\\\", "") : s;
    }

    public static String getMapValue(Map<String, Object> map) {
        StringBuffer str = new StringBuffer();
        Iterator it = map.values().iterator();

        while (it.hasNext()) {
            String val = String.valueOf(it.next());
            str.append(val);
        }

        return str.toString();
    }

    public static String bSubstring(String s, int length) {
        try {
            byte[] bytes = s.getBytes("Unicode");
            int n = 0;

            int i;
            for (i = 2; i < bytes.length && n < length; ++i) {
                if (i % 2 == 1) {
                    ++n;
                } else if (bytes[i] != 0) {
                    ++n;
                }
            }

            if (i % 2 == 1) {
                if (bytes[i - 1] != 0) {
                    --i;
                } else {
                    ++i;
                }
            }

            return new String(bytes, 0, i, "Unicode");
        } catch (Exception var5) {
            return new String("");
        }
    }

    public static String getString(Object o) {
        return o == null ? "" : o.toString();
    }

    public static Properties getProperties(String filename) {
        Properties properties = new Properties();
        InputStream in = null;
        in = StringUtil.class.getClassLoader().getResourceAsStream(filename);

        try {
            properties.load(in);
            return properties;
        } catch (IOException var4) {
            return properties;
        }
    }

    public static String dateTostr(Date date, String format) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String dateStr = null;
        dateStr = dateFormat.format(date);
        return dateStr;
    }

    public static String iso8859ToUTF8(Object obj) {
        try {
            return obj == null ? "" : new String(obj.toString().getBytes("UTF-8"), "iso-8859-1");
        } catch (Exception var2) {
            return "";
        }
    }

    public static String utf8ToIso8859(Object obj) {
        try {
            return obj == null ? "" : new String(obj.toString().getBytes("iso-8859-1"), "UTF-8");
        } catch (Exception var2) {
            return "";
        }
    }

    public static BigDecimal getStrBigDecimal(String str) {
        return isEmpty(str) ? null : new BigDecimal(str);
    }

    public static String cutString(String str, int size) {
        return isNotEmpty(str) && str.length() > 100 ? str.substring(0, 100) : str;
    }
}
