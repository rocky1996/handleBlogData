package common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private final static SimpleDateFormat SDF_yyyyMMdd = new SimpleDateFormat("yyyyMMdd");

    public static String getDate() {
        return SDF_yyyyMMdd.format(new Date());
    }
}
