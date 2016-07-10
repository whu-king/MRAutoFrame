package test;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/16.
 */
public class POITest {

    public static void main(String[] args) throws Exception {
//        test();
        mergeTeacher();
    }


    public static void test() throws Exception {
        String file1 = "F:\\1.xlsx";
        String file2 = "F:\\2.xlsx";
        String result = "F:\\3.xlsx";
        XSSFWorkbook insertbook = new XSSFWorkbook(new FileInputStream(file1));
        XSSFSheet targetSheet = insertbook.getSheetAt(0);
        XSSFWorkbook databook = new XSSFWorkbook(new FileInputStream(file2));
        XSSFSheet dataSheet = databook.getSheetAt(0);

        XSSFRow dataRow = dataSheet.getRow(0);
        int insertRowNum = 1;
        targetSheet.shiftRows(insertRowNum, targetSheet.getLastRowNum(), 1, true, false);
        XSSFRow row =  targetSheet.createRow(insertRowNum);
        for(int i = 0; i < 2; i++){
            if(dataRow.getCell(i).getCellType() == 1 ){
                String text = dataRow.getCell(i).getStringCellValue();
                row.createCell(i).setCellValue(text);

            }else{
                double text = dataRow.getCell(i).getNumericCellValue();
                row.createCell(i).setCellValue(text);
            }
        }
        FileOutputStream out = new FileOutputStream(new File(result));
        insertbook.write(out);
        out.close();

    }

    public static Map<String,Integer> rowNumAndName = new HashMap<String,Integer>();
    private  static int rowNum = 0;

    public static void mergeTeacher() throws Exception {
        String file2015 = "F:\\2015年.xlsx";
        String file2014 = "F:\\2014年.xlsx";
        String file2013 = "F:\\2013年.xlsx";

        String result = "F:\\all.xlsx";

        XSSFWorkbook workbook;
        workbook = new XSSFWorkbook(new FileInputStream(file2015));
        XSSFSheet sheet2015 = workbook.getSheetAt(0);

        XSSFRow row2015 = null;
        for (int i = 2; sheet2015.getRow(i)!=null; i++) {
            row2015 =sheet2015.getRow(i);
            String name = row2015.getCell(16).getStringCellValue().trim();
            rowNumAndName.put(name,row2015.getRowNum());
        }

        XSSFWorkbook workbook2;
        workbook2 = new XSSFWorkbook(new FileInputStream(file2014));
        XSSFSheet sheet2014 = workbook2.getSheetAt(0);

        XSSFRow row2014 = null;
        for (int i = 2; sheet2014.getRow(i)!=null; i++) {
            row2014 = sheet2014.getRow(i);
            insertIntoSheet2015(row2014, sheet2015);
        }
        workbook2.close();

//        XSSFWorkbook workbook3;
//        workbook3 = new XSSFWorkbook(new FileInputStream(file2013));
//        XSSFSheet sheet2013 = workbook3.getSheetAt(0);
//
//        XSSFRow row2013 = null;
//        for (int i = 2; sheet2013.getRow(i)!=null; i++) {
//            row2013 =sheet2013.getRow(i);
//            insertIntoSheet2015(row2013, sheet2015);
//        }
//        workbook3.close();


        FileOutputStream out = new FileOutputStream(new File(result));
        workbook.write(out);
        out.close();
        System.out.println("xlsm created successfully..");
    }

    private static void insertIntoSheet2015(XSSFRow row2013, XSSFSheet sheet2015) {

        String name = row2013.getCell(16).getStringCellValue().trim();
        System.out.println("people name : " + name);
        if(name.equalsIgnoreCase("")) return;

        int insertRowNum = 0;
        if(rowNumAndName.containsKey(name)) {
            insertRowNum = rowNumAndName.get(name) + 1;
            sheet2015.createRow(sheet2015.getLastRowNum()+1);
            sheet2015.shiftRows(insertRowNum, sheet2015.getLastRowNum(), 1, true, false);
            XSSFRow row =  sheet2015.createRow(insertRowNum);
           for(int i = 0; i < 18; i++){
               if(row2013.getCell(i) == null) continue;
               if(row2013.getCell(i).getCellType() == 1 ){
                   String text = row2013.getCell(i).getStringCellValue();
                   row.createCell(i).setCellValue(text);

               }else{
                   double text = row2013.getCell(i).getNumericCellValue();
                   row.createCell(i).setCellValue(text);
               }
           }

        }else{
            insertRowNum = rowNum + 1;
            XSSFRow row =  sheet2015.createRow(insertRowNum);
            for(int i = 0; i < 18; i++){
                if(row2013.getCell(i) == null) continue;
                if(row2013.getCell(i).getCellType() == 1 ){
                    String text = row2013.getCell(i).getStringCellValue();
                    row.createCell(i).setCellValue(text);

                }else{
                    double text = row2013.getCell(i).getNumericCellValue();
                    row.createCell(i).setCellValue(text);
                }
            }
        }

        rowNumAndName.put(name,insertRowNum);
    }


    public static void xlsm(){
        String fileName = "C:\\new_file.xlsm";

        try {

            XSSFWorkbook workbook;
            workbook = new XSSFWorkbook(new FileInputStream("c:\\vbaTest.xlsm"));
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow row = null;
            for (int i = 0; sheet.getRow(i)!=null; i++) {
                row=sheet.getRow(i);
                for (int j = 0; row.getCell(j)!=null; j++) {
                    System.out.print(row.getCell(j)+"");
                    if(j == 0)
                    row.getCell(j).setCellValue("duxieceshi");
                }
                System.out.println();
            }

            //DO STUF WITH WORKBOOK

            FileOutputStream out = new FileOutputStream(new File(fileName));
            workbook.write(out);
            out.close();
            System.out.println("xlsm created successfully..");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }
}
