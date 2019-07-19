package com.company;


import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import java.io.File;
import java.util.*;

public class Main {

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
    private static void generateCombinations(Treenode[] treenodes){
        List<List<String>> AllCourseTimelists = new ArrayList<List<String>>();
        for(int i = 0; i < treenodes.length; i++){
            //System.out.println("Size "+treenodes[i].times.size());
            AllCourseTimelists.add(treenodes[i].times);
        }

        Set<List<String>> combs = getCombinations(AllCourseTimelists);
        System.out.println("Available Routine "+combs.size());
        for(List<String> list : combs) {
            System.out.println(list.toString());
        }




       /* ArrayList<String[]> combinations = new ArrayList<String[]>();

        String[] firstCourseTimes = new String[treenodes[0].times.size()];
        firstCourseTimes = treenodes[0].times.toArray(firstCourseTimes);


        for (String firstTime :
                firstCourseTimes) {

            for(int i = 1; i < treenodes.length; i++){
                String[] nextCourseTimes = new String[treenodes[i].times.size()];
                nextCourseTimes = treenodes[i].times.toArray(nextCourseTimes);

                for(String nextTime : nextCourseTimes){

                }

                *//*if(!treenodes[i].name.equals(treenode.name)){
                    String[] times2 = new String[treenodes[i].times.size()];
                    times2 = treenodes[i].times.toArray(times2);
                    for (String time2 :
                            times2){
                        if(!time1.equals(time2)){
                            combinations.add(treenode.name +" "+ time1 + " & "+treenodes[i].name
                            +" "+time2);
                        }
                    }
                }*//*
            }
            combinations.add(combinationStringArray);
        }*/

        /*for(int i = 0; i < combinations.size(); i++){
            System.out.println(combinations.get(i));
        }*/

    }

    public static void main(String[] args) throws Exception {
        Main test = new Main();
        test.setInputFile("nsu subjects.xls");
        test.read();

        Scanner scanner = new Scanner(System.in);

        System.out.println("How many courses you have taken?");
        int courseCount = scanner.nextInt();

        System.out.println("Enter the course code for your taken courses: ");
        //Set nodes based on taken courses
        Treenode[] treenodes = new Treenode[courseCount];
        for(int i = 0; i < courseCount; i++){
            treenodes[i] = new Treenode(scanner.next());
            treenodes[i].times = new ArrayList<String>();
        }


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

        generateCombinations(treenodes);

        //System.out.println(treenodes[2].name + "\n" + treenodes[2].times);









    }
}
