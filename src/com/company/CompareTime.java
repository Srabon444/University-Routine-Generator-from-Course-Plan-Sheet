package com.company;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CompareTime {

    public static String removeChar(String str, Integer n) {
        String front = str.substring(0, n);
        String back = str.substring(n+1, str.length());
        return front + back;
    }

    public static boolean isBetween(LocalTime candidate, LocalTime start, LocalTime end) {
        return !candidate.isBefore(start) && !candidate.isAfter(end);  // Inclusive.
    }

    public static void main(String[] args) {
        List<String> combs = new ArrayList<String>();
        List<String> combs1 = new ArrayList<String>();
        combs.add("RA 02:40 PM - 04:10 PM");
        combs.add("ST 04:20 PM - 05:50 PM");
        combs.add("RA 08:00 AM - 09:30 AM");

        List<String> testTimes = new ArrayList<String>();
        testTimes.add("T 11:20 AM - 02:30 PM");
        testTimes.add("R 11:20 AM - 02:30 PM");
        testTimes.add("S 11:20 AM - 02:30 PM");
        testTimes.add("R 02:40 PM - 05:50 PM");
        testTimes.add("S 08:00 AM - 11:10 AM");
        testTimes.add("T 08:00 AM - 11:10 AM");
        testTimes.add("M 11:20 AM - 02:30 PM");
        testTimes.add("A 02:40 PM - 05:50 PM");
        testTimes.add("W 02:40 PM - 05:50 PM");
        testTimes.add("R 08:00 AM - 11:10 AM");


        /*for(String labTime: testTimes){
            String[] combination = combs.toArray(new String[combs.size()]);
            int ok = 0;


            for(int i = 0; i < combination.length; i++){
                String dc = "";
                boolean dayConf = false;
                boolean timeConf = false;
                dc+=combination[i].charAt(0);
                dc+=combination[i].charAt(1);

                String labDay = String.valueOf(labTime.charAt(0));

                if(dc.contains(labDay)){
                    dayConf = true;
                    String newLabTime = labTime.substring(2);
                    String courseTime = combination[i].substring(3);

                    //System.out.println(newLabTime + " " +courseTime);
                    String[] timeParserLab = newLabTime.split(" - ");
                    String[] timeParserCourse = courseTime.split(" - ");

                    LocalTime time = LocalTime.parse(timeParserLab[0].substring(0,5));
                    if(isBetween(time, LocalTime.parse(timeParserCourse[0].substring(0,5)), LocalTime.parse(timeParserCourse[1].substring(0,5)))){
                        timeConf = true;
                        //System.out.println(labTime + " " + combination[i] + "day and time");
                    }
                    else{
                        ok++;
                    }

                }else{
                    ok++;
                }
            }
            if(ok == 3){
                System.out.println(combs + labTime);
            }
        }*/
        //System.out.println(combs);
    }
}


