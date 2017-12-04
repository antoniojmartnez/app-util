import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utilidades para trabajar con fechas
 */
public class Dates {
    private App app;

    private static TimeZone serverTimezone = TimeZone.getTimeZone("UTC");
    private static TimeZone localTimezone = TimeZone.getTimeZone("Europe/Madrid");
    private static SimpleDateFormat localeDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat localeTimeFormat = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat localeDateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public Dates (App app) {
        this.app = app;
    }

    public String toDateString (Calendar calendar) {
        return this.toDateString(calendar.getTime());
    }

    public String toDateString (Date date) {
        this.localeDateFormat.setTimeZone(localTimezone);
        return this.localeDateFormat.format(date);
    }

    public String toTimeString (Calendar calendar) {
        return this.toTimeString(calendar.getTime());
    }

    public String toTimeString (Date date) {
        this.localeTimeFormat.setTimeZone(localTimezone);
        return this.localeTimeFormat.format(date);
    }

    public String toDateTimeString(String date) {
        if (date != null && date.compareTo("null") != 0 && date.compareTo("0") != 0) {
            Calendar calendar = this.getCalendarFromIsoString(date);
            return toDateTimeString(calendar);
        } else {
            return "";
        }
    }

    public String toDateTimeString (Calendar calendar) {
        return this.toDateTimeString(calendar.getTime());
    }

    public String toDateTimeString (Date date) {
        this.localeDateTimeFormat.setTimeZone(localTimezone);
        return this.localeDateTimeFormat.format(date);
    }

    public String toIsoFormat (Calendar calendar) {
        return this.toIsoFormat(calendar.getTime());
    }

    public String toIsoFormat (Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(serverTimezone);

        return df.format(date);
    }

    public Calendar getCalendarFromIsoString (String date) {
        return this.getCalendarFromCustomString(date, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    public Date getDateFromIsoString (String date) {
        return this.getDateFromCustomString(date, "yyyy-MM-dd'T'HH:mm:ss'Z'");
    }

    public Calendar getCalendarFromCustomString (String date, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.getDateFromCustomString(date, format));
        return calendar;
    }

    public Date getDateFromCustomString (String date, String format) {

//        date += date.endsWith("Z") ? "" : "Z";

        SimpleDateFormat df = new SimpleDateFormat(format);
        df.setTimeZone(serverTimezone);

        try {
            return df.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
}
