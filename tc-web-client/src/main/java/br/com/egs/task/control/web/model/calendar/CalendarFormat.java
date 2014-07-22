/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.egs.task.control.web.model.calendar;

import java.util.Calendar;

/**
 *
 */
public class CalendarFormat {
    
    
    public String calendarToString(Calendar date) {
        int day = date.get(Calendar.DAY_OF_MONTH);
        int month = date.get(Calendar.MONTH)+1;
        int year = date.get(Calendar.YEAR) % 100;

        StringBuilder sDate = new StringBuilder();
        if(day < 10){
            sDate.append(0);
        }
        sDate.append(day);
        sDate.append("/");
        if(month < 10){
            sDate.append(0);
        }
        sDate.append(month);
        sDate.append("/");
        sDate.append(year);

        return sDate.toString();
    }
}
