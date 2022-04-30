package com.novi.fassignment.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Parsers {

    public static LocalDateTime  myLocalDateTimeParserTypeYearMonthDayHourMinSec(String myString) {
        String[] dateTimeArray = myString.split(" ");
        String dateString = dateTimeArray[0];
        String timeString = dateTimeArray[1];
        String[] dateStringArray = dateString.split("-");
        String[] timeStringArray = timeString.split(":");
        int yyyy = Integer.parseInt(dateStringArray[0]);
        int mm = Integer.parseInt(dateStringArray[1]);
        int dd = Integer.parseInt(dateStringArray[2]);
        int hh = Integer.parseInt(timeStringArray[0]);
        int min = Integer.parseInt(timeStringArray[1]);
        int sec = Integer.parseInt(timeStringArray[2]);
        LocalDate date = LocalDate.of(yyyy, mm, dd);
        LocalTime time = LocalTime.of(hh, min, sec);
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
//        for(String str1 :  dateStringArray){
//            System.out.println(" dateStringArray : "+str1);
//        }
        return localDateTime;
    }

}


