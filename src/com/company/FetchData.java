package com.company;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import java.io.File;
import java.util.Scanner;

public class FetchData {

    private static String[] data;
    private String inputFile;

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
            data = new String[sheet.getRows()];
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
                data[index++] = str;
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FetchData test = new FetchData();
        test.setInputFile("preadvise.xls");
        test.read();
        Scanner scanner = new Scanner(System.in);
        String id = scanner.nextLine();
        for(int i = 0; data[i] != null; i++){
            String[] parseString = data[i].split(";");

            if(parseString[2].equals(id)){
                System.out.println(data[i]);
            }
        }
    }
}
