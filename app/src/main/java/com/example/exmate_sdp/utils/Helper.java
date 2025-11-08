package com.example.exmate_sdp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public  class Helper {
    public static String formatDate(Date date)
    {
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }
}
