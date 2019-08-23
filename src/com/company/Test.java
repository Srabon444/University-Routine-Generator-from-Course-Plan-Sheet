package com.company;


import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import java.io.File;
import java.time.LocalTime;
import java.util.*;

public class Test {

    private static int theoryCourseCount;
    private static int labCourseCount;
    private static Treenode root;

    private static class Treenode {

        private List<String> times;

        private String name;

        public Treenode(String data){
            this.name=data;
        }
    }

    private String inputFile;
    private static String[] rowsData;

    public void setInputFile(String inputFile){
        this.inputFile = inputFile;
    }

    public static boolean isBetween(LocalTime candidate, LocalTime start, LocalTime end) {
        return !candidate.isBefore(start) && !candidate.isAfter(end);  // Inclusive.
    }

    public void read(){
        File inputWorkBook = new File(inputFile);
        Workbook w;

        try{
            w = Workbook.getWorkbook(inputWorkBook);
            Sheet sheet = w.getSheet(0);

            int index = 0;
            rowsData = new String[sheet.getRows()];
            for(int i = 1; i < sheet.getRows(); i++){
                String str = "";
                for(int j = 0; j < sheet.getColumns(); j++){
                    Cell cell = sheet.getCell(j , i);
                    if(j < 1){
                        str += cell.getContents();
                    }else{
                        str += ';' + cell.getContents();
                    }
                }
                rowsData[index++] = str;
                //System.out.println(str);
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static boolean checkIfExists(String stringToLocate, Treenode[] treenodes) {
        for(int i = 0; i < treenodes.length; i++){
            if(stringToLocate.equals(treenodes[i].name)){
                return true;
            }
        }

        return false;
    }


    public static <T> Set<List<T>> getCombinations(List<List<T>> lists) {
        Set<List<T>> combinations = new HashSet<List<T>>();
        Set<List<T>> newCombinations;



        int index = 0;

        // extract each of the integers in the first list
        // and add each to ints as a new list
        for(T i: lists.get(0)) {
            List<T> newList = new ArrayList<T>();
            newList.add(i);
            combinations.add(newList);
        }
        index++;
        while(index < lists.size()) {
            List<T> nextList = lists.get(index);
            newCombinations = new HashSet<List<T>>();
            for(List<T> first: combinations) {
                for(T second: nextList) {
                    List<T> newList = new ArrayList<T>();
                    newList.addAll(first);
                    newList.add(second);
                    newCombinations.add(newList);
                }
            }
            combinations = newCombinations;

            index++;
        }

        return combinations;
    }

    //Generate Combinations
    private static void generateCombinations(Treenode[] treenodes, int totalDayCount) {
        List<List<String>> AllCourseTimelists = new ArrayList<List<String>>();
        for (int i = 0; i < theoryCourseCount; i++) {
            //System.out.println("Size "+treenodes[i].times.size());
            AllCourseTimelists.add(treenodes[i].times);
        }

        Set<List<String>> combs = getCombinations(AllCourseTimelists);
        /*combs contains only theory combination*/
        //System.out.println("Available Routine "+combs.size());
        List<Set<String>> finalCombs = new ArrayList<Set<String>>();
        for(List<String> c: combs){
            Set<String> s = new HashSet<>();
            String[] array = c.toArray(new String[c.size()]);

            for (int i = 0; i < array.length; i++) {
                s.add(array[i]);
            }
            if(s.size() == theoryCourseCount){
                finalCombs.add(s);
            }
        }

        List<Set<Set<String>>> output = new ArrayList<Set<Set<String>>>();
        int count = 1;
        for (Set<String> c : finalCombs) {
            Set<String> s = new HashSet<String>();
            for (int j = theoryCourseCount; j < theoryCourseCount + labCourseCount; j++) {
                List<String> testTimes = new ArrayList<String>();
                testTimes = treenodes[j].times;
                int flag = 0;
                //Set<String> s = c;
                for (String labTime : testTimes) {
                    String[] combination = c.toArray(new String[c.size()]);
                    int ok = 0;


                    for (int i = 0; i < combination.length; i++) {
                        String dc = "";

                        dc += combination[i].charAt(0);
                        dc += combination[i].charAt(1);

                        String labDay = String.valueOf(labTime.charAt(0));

                        if (dc.contains(labDay)) {

                            String newLabTime = labTime.substring(2);
                            String courseTime = combination[i].substring(3);

                            //System.out.println(newLabTime + " " +courseTime);
                            String[] timeParserLab = newLabTime.split(" - ");
                            String[] timeParserCourse = courseTime.split(" - ");

                            LocalTime time = LocalTime.parse(timeParserLab[0].substring(0, 5));
                            if (isBetween(time, LocalTime.parse(timeParserCourse[0].substring(0, 5)), LocalTime.parse(timeParserCourse[1].substring(0, 5)))) {
                                //timeConf = true;
                                //System.out.println(labTime + " " + combination[i] + "day and time");
                            } else {
                                ok++;
                            }

                        } else {
                            ok++;
                        }
                    }

                    if (ok == theoryCourseCount) {



                        //System.out.println(list.toString());
                        String[] combination1 = c.toArray(new String[c.size()]);
                        Set<Character> dayCounter = new HashSet<>();
                        for(int i = 0; i < combination1.length; i++){
                            dayCounter.add(combination1[i].charAt(0));
                            dayCounter.add(combination1[i].charAt(1));
                        }
                        dayCounter.add(labTime.charAt(0));
                        if(dayCounter.size() == totalDayCount){
                            /*System.out.printf("%-5s |",count++);
                            for(String course:c){
                                System.out.format("%-20s |", course);
                            }
                            System.out.printf("%-20s |",labTime);*/
                        }
                        //System.out.println();


                    }
                }
                /*if(count == 1){
                    System.out.println("No routine available for "+totalDayCount+" days.");
                }*/
            }
            //System.out.println(s);
            /*if(s.size() == theoryCourseCount+labCourseCount){
                output.add(s);
            }*/
        }
        /*if(count == 1){
            System.out.println("No routine available for "+totalDayCount+" days.");
        }*/



        System.out.println(output);


    }

    public static void main(String[] args) throws Exception {
        Test test = new Test();
        test.setInputFile("preadvise.xls");
        test.read();
        Scanner scanner = new Scanner(System.in);
        String id = scanner.nextLine();
        List<String> theory = new ArrayList<String>();
        List<String> lab = new ArrayList<String>();
        for(int i = 0; rowsData[i] != null; i++){
            String[] parseString = rowsData[i].split(";");

            if(parseString[2].equals(id)){
                //System.out.println(parseString[3]);
                if(parseString[3].length() == 6){
                    theory.add(parseString[3]);
                }else{
                    lab.add(parseString[3]);
                }
            }
        }
        System.out.println("Your Pre-advised Courses-");
        System.out.println("Theory:");
        for(String t:  theory){
            System.out.println(t);
        }
        System.out.println("Lab:");
        for(String l:  lab){
            System.out.println(l);
        }




        //Main test = new Main();
        test.setInputFile("nsu subjects.xls");
        test.read();

        //Scanner scanner = new Scanner(System.in);

        //System.out.println("How many theory courses you have taken?");
        theoryCourseCount = theory.size();
        //System.out.println("How many lab courses you have taken?");
        labCourseCount = lab.size();

        //System.out.println("Enter the Theory course code for your taken courses: ");
        //Set nodes based on taken courses
        Treenode[] treenodes = new Treenode[theoryCourseCount+labCourseCount];
        for(int i = 0; i < theoryCourseCount; i++){
            for(int j = 0; rowsData[j] != null; j++){
                String[] parseString = rowsData[j].split(";");
                String[] parseCourseCode = parseString[0].split("/");
                if(Arrays.asList(parseCourseCode).contains(theory.get(i))){
                    treenodes[i] = new Treenode(parseString[0]);
                }
            }
            treenodes[i].times = new ArrayList<String>();
        }
        // System.out.println("How many lab courses you have taken?");
        // int labCourseCount = scanner.nextInt();

        //System.out.println("Enter the Lab course code for your taken courses: ");
        //Set nodes based on taken courses
        for(int i = 0, in = theoryCourseCount; i < labCourseCount; i++){
            for(int j = 0; rowsData[j] != null; j++){
                String[] parseString = rowsData[j].split(";");
                String[] parseCourseCode = parseString[0].split("/");
                if(Arrays.asList(parseCourseCode).contains(lab.get(i))){
                    treenodes[in] = new Treenode(parseString[0]);
                }
            }
            treenodes[in].times = new ArrayList<String>();
            in++;
        }
        System.out.println("How many days you want to take your classes?");
        int totalDayCount = scanner.nextInt();


        String[] uniqueNodes = new String[1500];
        int c = 0;

        //Fetch taken course relatred info
        for(int i = 0; rowsData[i] != null; i++){
            String[] parseString = rowsData[i].split(";");
            if(!parseString[3].equals("TBA")){
                boolean found = checkIfExists(parseString[0],treenodes);
                if(found){
                    uniqueNodes[c++] = rowsData[i];
                }
            }
        }




        //Find all the unique name cousrse as node and insert their times in it.
        for(int i = 0; i < c; i++){
            //System.out.println(uniqueNodes[i]);

            String[] parseString = uniqueNodes[i].split(";");

            //System.out.println(parseString[3]);

            for(int j = 0; j < treenodes.length; j++){
                if(treenodes[j].name.equals(parseString[0])){
                    treenodes[j].times.add(parseString[3]);
                }
            }
        }

        System.out.println("\n\nYour available schedules are-");
        System.out.printf("%5s |","No.");
        for(int  i =  0;  i < theoryCourseCount; i++){
            //System.out.print(treenodes[i].name+" ");
            System.out.printf("%20s ", treenodes[i].name);
        }
        for(int  i =  theoryCourseCount;  i < labCourseCount+theoryCourseCount; i++){
            //System.out.print(treenodes[i].name+" ");
            System.out.printf("%30s ", treenodes[i].name);
        }
        System.out.println();

        generateCombinations(treenodes, totalDayCount);

        /*//System.out.println(treenodes[0].name + "\n" + treenodes[0].times);
        for(int  i =  0;  i < treenodes.length; i++){
            System.out.println(treenodes[i].name);
        }*/








    }
}
