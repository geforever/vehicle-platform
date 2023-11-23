package org.platform.vehicle.utils.phone;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegUtil {
    private static String REGEX_PHONE = "^\\d{11}$";

    /**
     * 校验手机号格式是否正确
     * @param phone
     * @return
     */
    public static boolean regexPhone(String phone){
        boolean b;
        if(phone.length() != 11){
            b = false;
        }else{
            Pattern p = Pattern.compile(REGEX_PHONE);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            if(isMatch){
                b = true;
            } else {
                b = false;
            }
        }
        return b;
    }

    public static void main(String[] args) {
        System.out.println(regexPhone("156768798901"));
    }
}
