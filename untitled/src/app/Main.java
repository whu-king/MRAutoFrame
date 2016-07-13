//package app;
//
//import java.util.Scanner;
//
///**
// * Created by Administrator on 2016/6/20.
// */
//public class Main {
//    public static void main(String[] args) throws Exception {
//        String filaName = "Sin.java";
//        String fileDir = "C:\\";
//        String packageName = "test";
//        String filePath = "F:\\620-Test.xlsm";
//        ProgramPackage programPackage = new ProgramPackage();
//        programPackage.setProgramMainFileDir(fileDir);
//        programPackage.setProgramMainFileName(filaName);
//        programPackage.setProgramMainFilePackageName(packageName);
//
//        POIExcelUtil poi = new POIExcelUtil();
//        MRDemo mr = poi.createMRFromExcel(filePath);
//        String excelPath = poi.creatExcelFrame(mr,filePath);
//        poi.generateFollowInputByMR(excelPath);
//        excelPath = poi.generateSourceOutputByProgram(programPackage,excelPath,mr);
//        poi.generateFollowOutputByMR(excelPath);
//        excelPath = poi.generateFollowOutputByProgram(programPackage,excelPath,mr);
//    }
//}
