package android.endava.com.demoproject.formatter;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormats {
    public final static String yyyy_MM_dd_T_HH_mm_ss_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static String formatISO(String Date) {
        Date dateResult;
        String stringResult = "";
        SimpleDateFormat sdf = new SimpleDateFormat(yyyy_MM_dd_T_HH_mm_ss_Z);
        try {
            dateResult = sdf.parse(Date);
            stringResult = dateResult.toString();
        } catch (ParseException e) {
            Log.e("ParseException", e.toString());
        }
        return stringResult;
    }
}
