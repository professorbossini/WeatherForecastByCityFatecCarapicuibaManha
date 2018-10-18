package br.com.bossini.weatherforecastbycityfateccarapicuibamanha;

import java.sql.Time;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Weather {
    public final String dayOfWeek;
    public final String maxTemp, minTemp, humidity, description, iconName;

    public Weather (long timeStamp, double minTemp,
                    double maxTemp, double humidity, String description, String iconName){
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);
        this.minTemp = numberFormat.format(minTemp) + "\u00B0C";
        this.maxTemp = numberFormat.format(maxTemp) + "\u00B0C";
        this.humidity = NumberFormat.getPercentInstance().format(humidity / 100.);
        this.description = description;
        this.iconName = iconName;
        this.dayOfWeek = convertTimestampToday(timeStamp);
    }

    private String convertTimestampToday (long timeStamp){
        Calendar agora = Calendar.getInstance();
        agora.setTimeInMillis(timeStamp * 1000);
        TimeZone timeZone = TimeZone.getDefault();
        agora.add(Calendar.MILLISECOND, timeZone.getOffset(agora.getTimeInMillis()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        return simpleDateFormat.format(agora.getTime());
    }
}
