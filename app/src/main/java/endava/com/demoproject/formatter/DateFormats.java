package endava.com.demoproject.formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateFormats {
    public final static String yyyy_MM_dd_T_HH_mm_ss_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public final static String HH_mm_ss_dd_MM_yyyy = "HH:mm:ss dd-MM-yyyy";

    public static String formatISO(String Date) {
        Date dateResult;
        String stringResult = "";
        SimpleDateFormat sdf = new SimpleDateFormat(yyyy_MM_dd_T_HH_mm_ss_Z);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            dateResult = sdf.parse(Date);
            SimpleDateFormat sdfLocal = new SimpleDateFormat(HH_mm_ss_dd_MM_yyyy);
            sdfLocal.setTimeZone(TimeZone.getDefault());
            stringResult = sdfLocal.format(dateResult);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return stringResult;
    }
}
