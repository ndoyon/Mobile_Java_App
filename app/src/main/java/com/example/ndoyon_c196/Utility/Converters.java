package com.example.ndoyon_c196.Utility;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Converters {@TypeConverter
public static Date fromTimestamp(Long value) {
    return value == null ? null : new Date(value);
}

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    public static Date StringToDate(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date returnDate = formatter.parse(date);
        return returnDate;
    }

    public static String DateToString(Date date) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String returnDate = formatter.format(date);
        return returnDate;
    }
}
