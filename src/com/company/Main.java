package com.company;


import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import java.io.File;
import java.time.LocalTime;
import java.util.*;

public class Main {

    private static int theoryCourseCount;
    private static int labCourseCount;
    private static Treenode root;

    private static int c = 0;
    private static String[] uniqueNodes;

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
        String can = candidate.toString();
        String str = start.toString();
        String en = end.toString();
        //System.out.println(can+str+en);
        if(candidate.isAfter(start) && candidate.isBefore(end)){
            return true;
        }else if(can.equals(str) || can.equals(en)){
            return true;
        }
        return false;
    }

    public static boolean arrayCheck(String[] arr, String targetValue) {
        Set<String> set = new HashSet<String>(Arrays.asList(arr));
        return set.contains(targetValue);
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


    public static <T> Set<Set<T>> getCombinations(List<List<T>> lists) {
        Set<Set<T>> combinations = new HashSet<Set<T>>();
        Set<Set<T>> newCombinations;

        int index = 0;

        // extract each of the integers in the first list
        // and add each to ints as a new list
        for(T i: lists.get(0)) {
            Set<T> newList = new HashSet<T>();
            newList.add(i);
            combinations.add(newList);
        }
        index++;
        while(index < lists.size()) {
            List<T> nextList = lists.get(index);
            newCombinations = new HashSet<Set<T>>();
            for(Set<T> first: combinations) {
                for(T second: nextList) {
                    Set<T> newList = new HashSet<T>(first);
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
        for (int i = 0; i < labCourseCount + theoryCourseCount; i++) {
            //System.out.println("Size "+treenodes[i].times.size());
            AllCourseTimelists.add(treenodes[i].times);
        }

        Set<Set<String>> combs = new HashSet<Set<String>>();
        combs = getCombinations(AllCourseTimelists);
        /*combs contains only theory combination*/
        //Iterator<Set<String>> itr = combs.iterator();
        /*while (itr.hasNext()){
            Set<String> c = itr.next();
            if(itr.next().size() < theoryCourseCount){
                combs.remove(c);
            }
        }*/
        combs.removeIf(c -> c.size() < theoryCourseCount + labCourseCount);

        Set<List<String>> finalCombs = new HashSet<List<String>>();
        for (Set<String> c : combs) {
            Set<String> finalC = new HashSet<String>();
            String[] tempCarr = c.toArray(new String[0]);
            //Set<String> tempC = c;
            String[] fc = new String[theoryCourseCount + labCourseCount];
            for (int i = 0; i < treenodes.length; i++) {
                for (int j = 0; j < tempCarr.length; j++) {
                    //System.out.println("str"+s+" "+treenodes[i].times);
                    if (!tempCarr[j].equals("*")) {
                        String s = tempCarr[j];
                        if (treenodes[i].times.contains(s)) {
                            fc[i] = s;
                            tempCarr[j] = "*";
                            break;
                        }
                    }

                }
            }
            finalCombs.add(Arrays.asList(fc));
        }
        Set<List<String>> remove = new HashSet<List<String>>();
        //System.out.println(finalCombs);
        for (List<String> f : finalCombs) {
            for (int j = theoryCourseCount; j < labCourseCount + theoryCourseCount; j++) {
                String[] combination = f.toArray(new String[f.size()]);
                int ok = 0;

                String labTime = f.get(j);
                for (int i = 0; i < theoryCourseCount; i++) {
                    String dc = "";
                    if(combination[i] == null || labTime == null){
                        remove.add(f);
                        break;
                    }else{
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
                            LocalTime time1 = LocalTime.parse(timeParserLab[1].substring(0, 5));
                            LocalTime start = LocalTime.parse(timeParserCourse[0].substring(0, 5));
                            LocalTime end = LocalTime.parse(timeParserCourse[1].substring(0, 5));

                            //System.out.println(time + " " +time1);
                            //System.out.println(labTime + " " + combination[i] + "day and time");
                            //System.out.println(time+ " "+time1+" "+start+" "+end);
                            if (isBetween(time, start, end) || isBetween(time1, start, end))
                            {
                                //timeConf = true;
                                //System.out.println("Conf");
                            } else {
                                ok++;
                            }

                        } else {
                            ok++;
                        }

                    }



                }
                if(ok != theoryCourseCount){
                    remove.add(f);
                    break;
                }
            }


        }
        finalCombs.removeAll(remove);
        //System.out.println(finalCombs);
        remove.clear();
        int count = 1,flag=0;
        for(List<String> f:finalCombs){
            String[] combination1 = f.toArray(new String[f.size()]);
            Set<Character> dayCounter = new HashSet<>();
            for(int i = 0; i < theoryCourseCount; i++){
                if(combination1[i] == null){
                    remove.add(f);
                }else{
                    dayCounter.add(combination1[i].charAt(0));
                    dayCounter.add(combination1[i].charAt(1));
                }

            }
            for(int i = theoryCourseCount; i < theoryCourseCount+labCourseCount; i++){
                if(combination1[i] == null){
                    remove.add(f);
                }else{
                    dayCounter.add(combination1[i].charAt(0));
                }
            }

            if(dayCounter.size() != totalDayCount){
                remove.add(f);
            }
            //System.out.println(count++ + f.toString());
        }
        finalCombs.removeAll(remove);
        remove.clear();
        int sizeBefore = finalCombs.size();
        List<List<String>> collect = new ArrayList<List<String>>();
        for(List<String> f:finalCombs){
            List<String> s = new ArrayList<String>();
            //System.out.println(count++ + f.toString());
            for(int in = 0; in < f.size(); in++){
                String name = treenodes[in].name;
                String section = "";
                for(int i = 0; i < c; i++){
                    //System.out.println(uniqueNodes[i]);

                    String[] parseString = uniqueNodes[i].split(";");

                    //System.out.println(parseString[3]);


                    if(name.equals(parseString[0])){
                        if(f.contains(parseString[3])){
                            if(section.equals("")){
                                section+=parseString[1];
                            }else {
                                section += ","+parseString[1];
                            }

                        }
                    }

                }
                s.add(section);
            }
            collect.add(s);
        }
        if(collect.size() == 0){
            System.out.println("No  routine available for "+totalDayCount+" days");
        }else{
            for(List<String> f :collect){
                System.out.printf("%5s|",count++);
                for(String s:f){
                    System.out.printf("%20s===",s);
                }
                System.out.println();
            }
        }

        /*for(List<String> f :finalCombs){
            System.out.println(count++ + f.toString());
        }*/
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.setInputFile("preadvise.xls");
        main.read();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Your ID...");
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




        //Main main = new Main();
        main.setInputFile("nsu subjects.xls");
        main.read();

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


        uniqueNodes = new String[1500];


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
        System.out.printf("%5s|","No.");
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

        //System.out.println(treenodes[0].name + "\n" + treenodes[0].times);
        /*for(int  i =  0;  i < treenodes.length; i++){
            System.out.println(treenodes[i].times);
        }*/








    }
}
