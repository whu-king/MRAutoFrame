package test;

import java.io.*;

import java.util.HashMap;
import java.util.Map;


import jxl.Cell;

import jxl.CellType;

import jxl.Sheet;

import jxl.Workbook;

import jxl.write.*;
import jxl.write.Number;


public class ReadExcel

{

    private static Map<String,String> universityCity =  new HashMap<String,String>();
    private static Map<String,Integer> key = new HashMap<String,Integer>();
    private static Map<String,Integer> province = new HashMap<String,Integer>();
    private static String type = "M";
//    private static String year = "2011";
    private static String originalType = "青年项目";

    public static void main(String[] args)

    {
//        countKeywordInYear();
        testVba();
    }

    public void mergeTeacher(){

    }



    private static void putInMap(String s){
        if(key.containsKey(s)){
            key.put(s,key.get(s) + 1);
        }else{
            key.put(s,1);
        }
    }

    private static void keywordCount(String content){
        String[] keywords = content.split("；");
        if (keywords.length == 1){
            keywords = keywords[0].split("，");
            if(keywords.length == 1){
                keywords = keywords[0].split(";");
                if(keywords.length == 1){
                    keywords = keywords[0].split("、");
                    if(keywords.length == 1){
                        keywords = keywords[0].split(" ");
                        if(keywords.length == 1){
                            keywords = keywords[0].split("，");
                        }
                    }
                }
            }
        }
        for (String s : keywords){
            System.out.print(s + "--");
            putInMap(s);
        }


    }

    public static void getUniversityCity(){

        jxl.Workbook readwb = null;

        try

        {

            //构建Workbook对象, 只读Workbook对象

            //直接从本地文件创建Workbook


                String fileName = "e:/"  + "大学城市.xls";
                InputStream instream = new FileInputStream(fileName);

                readwb = Workbook.getWorkbook(instream);



                //Sheet的下标是从0开始

                //获取第一张Sheet表

                Sheet readsheet = readwb.getSheet(0);

                //获取Sheet表中所包含的总列数

                int rsColumns = readsheet.getColumns();

                //获取Sheet表中所包含的总行数

                int rsRows = readsheet.getRows();

                //获取指定单元格的对象引用

                for (int i = 1; i < rsRows; i++)

                {
                    Cell universitycell = readsheet.getCell(0, i);
                    Cell procell = readsheet.getCell(1,i);
                    String uniName = universitycell.getContents().trim();
                    String province = procell.getContents().trim();
                    universityCity.put(uniName,province);
                }
                for(Map.Entry<String,String> entry : universityCity.entrySet()){
                    System.out.println(entry.getKey() + "-" + entry.getValue());
                }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            readwb.close();

        }
    }


    public static void countKeywordInYear(){
        jxl.Workbook readwb = null;

        try

        {

            //构建Workbook对象, 只读Workbook对象

            //直接从本地文件创建Workbook
            for(int yearindex = 2011; yearindex < 2016; yearindex ++){

                String fileName = "e:/" + originalType + "/" + yearindex + ".xls";
                InputStream instream = new FileInputStream(fileName);

                readwb = Workbook.getWorkbook(instream);



                //Sheet的下标是从0开始

                //获取第一张Sheet表

                Sheet readsheet = readwb.getSheet(0);

                //获取Sheet表中所包含的总列数

                int rsColumns = readsheet.getColumns();

                //获取Sheet表中所包含的总行数

                int rsRows = readsheet.getRows();

                //获取指定单元格的对象引用

                for (int i = 1; i < rsRows; i++)

                {
                    Cell cell = readsheet.getCell(2, i);
                    System.out.print(cell.getContents() + " ");
                    System.out.println();
                    keywordCount(cell.getContents());
                }
            }


            String targetFile = "F:/" + type + "/11-16 Q.xls";
            File filewrite=new File(targetFile);

            filewrite.createNewFile();

            OutputStream os=new FileOutputStream(filewrite);

            WritableWorkbook wwb = Workbook.createWorkbook(os);

            //创建Excel工作表 指定名称和位置

            WritableSheet ws = wwb.createSheet("Test Sheet 1",0);

            /**************往工作表中添加数据*****************/

            Label title1 = new Label(0,0,"关键字");
            Label title2 = new Label(1,0,"出现次数");
            ws.addCell(title1);
            ws.addCell(title2);
            int index = 1;
            for(Map.Entry<String,Integer> entry : key.entrySet()){
                System.out.println(entry.getKey() + ":" + entry.getValue());
                Label keylabel = new Label(0,index,entry.getKey());
                Number labelN = new Number(1,index,entry.getValue());
                ws.addCell(keylabel);
                ws.addCell(labelN);
                index ++;
            }

            wwb.write();

            wwb.close();


        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            readwb.close();

        }

    }


    public static void countProvinceInYear(){
        jxl.Workbook readwb = null;

        try

        {

            //构建Workbook对象, 只读Workbook对象

            //直接从本地文件创建Workbook
            for(int yearindex = 2006; yearindex < 2011; yearindex ++){

                String fileName = "e:/" + originalType + "/" + yearindex + ".xls";
                InputStream instream = new FileInputStream(fileName);

                readwb = Workbook.getWorkbook(instream);



                //Sheet的下标是从0开始

                //获取第一张Sheet表

                Sheet readsheet = readwb.getSheet(0);

                //获取Sheet表中所包含的总列数

                int rsColumns = readsheet.getColumns();

                //获取Sheet表中所包含的总行数

                int rsRows = readsheet.getRows();

                //获取指定单元格的对象引用

                for (int i = 1; i < rsRows; i++)

                {
                    Cell cell = readsheet.getCell(1, i);
                    provinceCount(cell.getContents());
                }
            }



            String targetFile = "F:/" + type + "/06-10面上省份.xls";
            File filewrite=new File(targetFile);

            filewrite.createNewFile();

            OutputStream os=new FileOutputStream(filewrite);

            WritableWorkbook wwb = Workbook.createWorkbook(os);

            //创建Excel工作表 指定名称和位置

            WritableSheet ws = wwb.createSheet("Test Sheet 1",0);

            /**************往工作表中添加数据*****************/

            Label title1 = new Label(0,0,"省份");
            Label title2 = new Label(1,0,"出现次数");
            ws.addCell(title1);
            ws.addCell(title2);
            int index = 1;
            for(Map.Entry<String,Integer> entry : province.entrySet()){
                System.out.println(entry.getKey() + ":" + entry.getValue());
                Label keylabel = new Label(0,index,entry.getKey());
                Number labelN = new Number(1,index,entry.getValue());
                ws.addCell(keylabel);
                ws.addCell(labelN);
                index ++;
            }

            wwb.write();

            wwb.close();


        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            readwb.close();

        }
    }


    public static void testVba(){
        jxl.Workbook readwb = null;
        try
        {
            //构建Workbook对象, 只读Workbook对象
            //直接从本地文件创建Workbook
                String fileName = "c:/vbaTest.xls";
                InputStream instream = new FileInputStream(fileName);
                readwb = Workbook.getWorkbook(instream);
                //Sheet的下标是从0开始
                //获取第一张Sheet表
                Sheet readsheet = readwb.getSheet(0);
          readsheet.getSettings().setSelected(true);
                //获取Sheet表中所包含的总列数
                int rsColumns = readsheet.getColumns();
                //获取Sheet表中所包含的总行数
                int rsRows = readsheet.getRows();
                //获取指定单元格的对象引用
                for (int i = 0; i < rsRows; i++)
                {
                    Cell cell = readsheet.getCell(i, 0);
                    System.out.println(cell.getContents());
                }
        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            readwb.close();

        }
    }

    public static void provinceCount(String str){
        String uniName = str.trim();
        if(!universityCity.containsKey(uniName)){
            System.out.println("************" + uniName + " NO City **********");
        }else{
            String proName = universityCity.get(uniName);
            if(province.containsKey(proName)){
                province.put(proName,province.get(proName) + 1);
            }else{
                province.put(proName,1);
            }
        }

    }


}