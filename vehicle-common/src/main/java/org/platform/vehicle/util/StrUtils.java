package org.platform.vehicle.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class StrUtils {

    public static final int[] EMPTY = new int[0];

    public static final Integer[] EMPTY_ = new Integer[0];

    public static int[] toInts(String str, String delimiter) {
        String[] split = str.split(delimiter);
        int[] result = new int[split.length];
        for (int i = 0; i < split.length; i++)
            result[i] = Integer.parseInt(split[i]);
        return result;
    }

    public static double[] toDoubles(String str, String delimiter) {
        String[] split = str.split(delimiter);
        double[] result = new double[split.length];
        for (int i = 0; i < split.length; i++)
            result[i] = Double.parseDouble(split[i]);
        return result;
    }

    public static byte[] toBytes(String str, String delimiter) {
        String[] split = str.split(delimiter);
        byte[] result = new byte[split.length];
        for (int i = 0; i < split.length; i++)
            result[i] = (byte) Integer.parseInt(split[i]);
        return result;
    }

    public static String merge(String delimiter, Collection value) {
        if (value == null || value.size() == 0)
            return null;

        StringBuilder result = new StringBuilder(value.size() * 5);
        for (Object id : value)
            result.append(id).append(delimiter);

        return result.substring(0, result.length() - 1);
    }

    public static String merge(String delimiter, Object... value) {
        if (value == null || value.length == 0)
            return null;

        StringBuilder result = new StringBuilder(value.length * 5);
        for (Object id : value)
            result.append(id).append(delimiter);

        return result.substring(0, result.length() - 1);
    }

    public static String merge(String delimiter, int... value) {
        if (value == null || value.length == 0)
            return null;

        StringBuilder result = new StringBuilder(value.length * 5);
        for (int id : value)
            result.append(id).append(delimiter);

        return result.substring(0, result.length() - 1);
    }

    public static int[] toInts(Integer[] src) {
        if (src == null || src.length == 0)
            return EMPTY;

        int[] dest = new int[src.length];
        for (int i = 0; i < src.length; i++)
            dest[i] = src[i];
        return dest;
    }

    public static Integer[] toInts(int[] src) {
        if (src == null || src.length == 0)
            return EMPTY_;

        Integer[] dest = new Integer[src.length];
        for (int i = 0; i < src.length; i++)
            dest[i] = src[i];
        return dest;
    }

    public static Integer parseInt(String num) {
        return parseInt(num, null);
    }

    public static Integer parseInt(String num, Integer defVal) {
        if (isBlank(num))
            return defVal;
        try {
            return Integer.parseInt(num);
        } catch (NumberFormatException e) {
            return defVal;
        }
    }

    public static String toUnderline(String str) {
        StringBuilder result = new StringBuilder(str.length() + 4);
        char[] chars = str.toCharArray();

        result.append(Character.toLowerCase(chars[0]));

        for (int i = 1; i < chars.length; i++) {
            char c = chars[i];
            if (Character.isUpperCase(c))
                result.append('_').append(Character.toLowerCase(c));
            else
                result.append(c);
        }
        return result.toString();
    }

    public static String subPrefix(String str, String prefix) {
        if (str != null && str.startsWith(prefix))
            str = str.substring(prefix.length());
        return str;
    }

    public static Map newMap(Object... entrys) {
        Map result = new HashMap((int) (entrys.length / 1.5) + 1);
        for (int i = 0; i < entrys.length; )
            result.put(entrys[i++], entrys[i++]);
        return result;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isBlank(String str) {
        return str == null || str.length() == 0 || str.trim().length() == 0;
    }

    public static <T> T getDefault(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static String leftPad(String str, int size, char ch) {
        int length = str.length();
        int pads = size - length;
        if (pads > 0) {
            char[] result = new char[size];
            str.getChars(0, length, result, pads);
            while (pads > 0)
                result[--pads] = ch;
            return new String(result);
        }
        return str;
    }

    public static int[] toArray(Collection<Integer> list) {
        if (list == null || list.isEmpty())
            return null;

        int[] result = new int[list.size()];
        int i = 0;
        for (Integer e : list) {
            if (e != null)
                result[i++] = e;
        }
        return result;
    }

    public static Set<Integer> toSet(int... num) {
        if (num == null || num.length == 0) {
            return Collections.EMPTY_SET;
        }
        Set<Integer> result;
        if (num.length <= 3) {
            result = new TreeSet<>();
        } else {
            result = new HashSet<>(num.length << 1);
        }
        for (int i : num) {
            result.add(i);
        }
        return result;
    }

    public static boolean isNum(String val) {
        if (isBlank(val)) {
            return false;
        }
        int sz = val.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(val.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter(7680);
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    private static final char[] hexCode = "0123456789abcdef".toCharArray();

    public static String bytes2Hex(byte[] bytes) {
        char[] hex = new char[bytes.length << 1];
        for (int j = 0, i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            hex[j++] = hexCode[(b >> 4) & 0xF];
            hex[j++] = hexCode[(b & 0xF)];
        }
        return new String(hex);
    }

    public static byte[] hex2Bytes(String hex) {
        final int len = hex.length();

        if (len % 2 != 0) {
            throw new IllegalArgumentException("hexBinary needs to be even-length: " + hex);
        }

        byte[] out = new byte[len >> 1];
        for (int i = 0; i < len; i += 2) {

            int h = hexToBin(hex.charAt(i));
            int l = hexToBin(hex.charAt(i + 1));
            if (h == -1 || l == -1) {
                throw new IllegalArgumentException("contains illegal character for hexBinary: " + hex);
            }
            out[i >> 1] = (byte) (h * 16 + l);
        }
        return out;
    }

    public static int hexToBin(char ch) {
        if ('0' <= ch && ch <= '9') {
            return ch - '0';
        }
        if ('A' <= ch && ch <= 'F') {
            return ch - ('A' - 10);
        }
        if ('a' <= ch && ch <= 'f') {
            return ch - ('a' - 10);
        }
        return -1;
    }
}
