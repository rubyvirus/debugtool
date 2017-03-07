package com.whatistest.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rubyvirusqq@gmail.com on 2017-2-4.
 */
public class TimeUtils {

    public static String getTimeStamp() {
        Date date = new Date();
        return new SimpleDateFormat("yyyyMMddHHmmss").format(date.getTime());
    }
}
