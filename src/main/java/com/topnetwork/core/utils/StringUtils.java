package com.topnetwork.core.utils;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    /**
     * Determines whether the given string is a blank string. A blank string is a string consisting of Spaces,
     * tabs, carriage returns, and newlines. true is returned if the input string is null or empty
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input)) {
            return true;
        }

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotEmpty(String input) {
        return !isEmpty(input);
    }

    /**
     * 判断一个对象是否为空；
     */
    public final static boolean isEmpty(Object o) {
        return (o == null);
    }

    public final static boolean isEmpty(String[] array) {
        return array == null || array.length == 0;
    }

    public final static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public final static boolean isEmpty(StringBuffer sb) {
        return sb == null || sb.length() == 0;
    }

    public final static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    public final static boolean isEmpty(Set set) {
        return set == null || set.size() == 0;
    }

    public final static boolean isEmpty(Map map) {
        return map == null || map.size() == 0;
    }

    public final static boolean isSame(String value1, String value2) {
        if (isEmpty(value1) && isEmpty(value2)) {
            return true;
        } else if (!isEmpty(value1) && !isEmpty(value2)) {
            return (value1.trim().equalsIgnoreCase(value2.trim()));
        } else {
            return false;
        }
    }


    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }


    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    public final static boolean isInt(String value) {
        if (isEmpty(value))
            return false;

        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }


    public final static boolean isDouble(String value) {
        if (isEmpty(value))
            return false;
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public final static int parseInt(String value) {
        if (isInt(value))
            return Integer.parseInt(value);
        return 0;
    }

    public final static int parseInt(String value, int defaultValue) {
        if (isInt(value))
            return Integer.parseInt(value);
        return defaultValue;
    }


    public final static boolean isFloat(String value) {
        if (isEmpty(value))
            return false;

        try {
            Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }


    public final static float parseFloat(String value) {
        if (isFloat(value))
            return Float.parseFloat(value);
        return 0f;
    }


    public final static double parseDouble(String value) {
        if (isDouble(value))
            return Double.parseDouble(value);
        return 0;
    }

    public final static double parseDouble(String value, double defaultValue) {
        if (isDouble(value))
            return Double.parseDouble(value);
        return defaultValue;
    }


    public final static boolean parseBoolean(String value) {
        return parseBoolean(value, false);
    }

    public final static boolean parseBoolean(String value, boolean defaultValue) {
        if (isEmpty(value))
            return defaultValue;

        return StringUtils.isSame(value, "true");
    }

    public static double round(double num, int n) {
        return Math.round(num * Math.pow(10, n)) / Math.pow(10, n);
    }

    public static double round(int num, int n) {
        return Math.round(1.0 * num * Math.pow(10, n)) / Math.pow(10, n);
    }


    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }


    public final static String format(String value) {
        return format(value, "");
    }

    public final static String format(String value, String defaultValue) {
        if (isEmpty(value))
            return defaultValue;
        else
            return value.trim();
    }

    /**
     * Determines whether the specified data exists in the specified array
     */
    public final static boolean isContain(String[] array, String value) {
        if (isEmpty(array) || isEmpty(value))
            return false;

        int size = size(array);
        for (int i = 0; i < size; i++) {
            if (isSame(array[i], value))
                return true;
        }

        return false;
    }

    public final static boolean isContain(String content, String value) {
        if (isEmpty(content) || isEmpty(value))
            return false;

        return (content.indexOf(value) != -1);
    }

    public final static boolean isContain(List list, Object object) {
        if (isEmpty(list))
            return false;

        return list.contains(object);
    }

    /**
     * Gets the size of the specified collection
     */
    public final static int size(List list) {
        if (isEmpty(list))
            return 0;
        else
            return list.size();
    }

    public final static int size(Map map) {
        if (isEmpty(map))
            return 0;
        else
            return map.size();
    }

    public final static int size(String[] array) {
        if (isEmpty(array))
            return 0;
        else
            return array.length;
    }

    public final static int size(Object[] array) {
        if (isEmpty(array))
            return 0;
        else
            return array.length;
    }

    /***************************************************************************
     * Determine how a time compares to the present time(<0，date2<date1) (=0， date2=date1) (>0， date2>date1)
     *
     * @param date1
     * @param date2
     * @return
     */
    public final static int getDaysBetween(Date date1, Date date2) {
        if (StringUtils.isEmpty(date1) || StringUtils.isEmpty(date2)) {
            return 1;
        }
        Calendar d1 = Calendar.getInstance();
        d1.setTime(date1);
        Calendar d2 = Calendar.getInstance();
        d2.setTime(date2);
        int days = d2.get(Calendar.DAY_OF_YEAR)
                - d1.get(Calendar.DAY_OF_YEAR);
        if (d1.get(Calendar.YEAR) > d2.get(Calendar.YEAR)) {
            d1 = (Calendar) d1.clone();
            do {
                days -= d1.getActualMaximum(Calendar.DAY_OF_YEAR);
                // System.out.println("-----"+days);
                d2.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != d2
                    .get(Calendar.YEAR));
        }
        if (d1.get(Calendar.YEAR) < d2.get(Calendar.YEAR)) {
            d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
                // System.out.println("+++++"+days);
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != d2
                    .get(Calendar.YEAR));
        }
        return days;
    }

    /**
     * Formatting time
     *
     * @param date
     * @return
     */
    public final static String formatDateTime(Date date) {
        return formatDateTime("yyyy-MM-dd HH:mm:ss", date, null);
    }

    public final static String formatDateTime(String style, Date date,
                                              String defaultValue) {
        if (isEmpty(style) || isEmpty(date))
            return defaultValue;

        SimpleDateFormat sdf = new SimpleDateFormat(style);
        return StringUtils.format(sdf.format(date), defaultValue);
    }

    /**
     * Compares a time with the current time <0, specifies that the time is earlier than today ==0
     * , specifies that the date is equal to today >0, specifies that the date is later than today
     */
    public final static int timeCompareWithNow(Date date) {
        if (isEmpty(date))
            return 1;
        Date now = new Date();
        return (int) ((date.getTime() - now.getTime()) / (24 * 3600 * 1000));
    }

    /**
     * Calculate the difference between the two calendars
     */
    public final static int dateBetween(Date c1, Date c2) {
        if (isEmpty(c1) || isEmpty(c2))
            return 0;

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(c1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(c2);

        return (int) ((calendar1.getTime().getTime() - calendar2.getTime().getTime()));
    }

    /**
     * Resolution date
     */
    public final static Date parseDate(long milliSeconds) {
        return new Date(milliSeconds);
    }

    public final static Date parseDate(String date) {
        return parseDate("yyyy-MM-dd HH:mm:ss", date);
    }

    public final static Date parseDate(String date, Date defaultValue) {
        if (isEmpty(date))
            return defaultValue;

        return parseDate("yyyy-MM-dd", date);
    }

    public final static Date parseDate(String style, String date) {
        if (isEmpty(style) || isEmpty(date))
            return new Date();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(style);
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    /**
     * get month
     *
     * @param date
     * @return
     */
    public static int parseMonth(String date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(parseDate("yyyy-MM-dd", date));
        int month = cal.get(Calendar.MONTH) + 1;
        return month;
    }

    /**
     * get year
     *
     * @param date
     * @return
     */
    public static int parseYear(String date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(parseDate("yyyy-MM-dd", date));
        int month = cal.get(Calendar.YEAR);
        return month;
    }

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };


    /**
     * Verify email
     *
     * @param email
     * @return
     */
    public final static boolean isEmail(String email) {
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }

    /**
     * String conversion to hexadecimal (no Unicode encoding required)
     *
     * @param str
     * @return
     */
    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            // sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * Hexadecimal direct conversion to string (no Unicode decoding required)
     *
     * @param hexStr
     * @return
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /**
     * String reserved two bits
     */
    public static String strM2(String str) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(str);
    }

    /**
     * Format a string, each three digits separated by commas
     *
     * @param str
     * @return
     */
    public static String addComma(String str) {
        str = new StringBuilder(str).reverse().toString();
        if (str.equals("0")) {
            return str;
        }
        String str2 = "";
        for (int i = 0; i < str.length(); i++) {
            if (i * 3 + 3 > str.length()) {
                str2 += str.substring(i * 3, str.length());
                break;
            }
            str2 += str.substring(i * 3, i * 3 + 3) + ",";
        }
        if (str2.endsWith(",")) {
            str2 = str2.substring(0, str2.length() - 1);
        }
        //Finally, reverse the order
        String temp = new StringBuilder(str2).reverse().toString();
        //Take the last one off
        return temp.substring(0, temp.lastIndexOf(",")) + temp.substring(temp.lastIndexOf(",") + 1, temp.length());
    }

}
