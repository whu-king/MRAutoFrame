package test;

import app.MRDemo;
import app.POIExcelUtil;
import app.ProgramPackage;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;
import java.util.Scanner;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Administrator on 2016/6/19.
 */
public class POIExcelUtilTest {

    @Test
    public void TestCreateExcelFrame() throws Exception {
        String filePath = "C:\\619Test.xlsm";
//        MRDemo mrDemo = new MRDemo();
//        mrDemo.setInputNumber(1);
//        mrDemo.setOutputNumber(1);
//        mrDemo.setProgramName("Sin");
//        mrDemo.setOutputRelation("follow-output=source-output ");
//        mrDemo.setInputRelation("follow-input-1 = 3.14 - source-input-1");
//        mrDemo.setMRnumber(1);
        MRDemo mr = new POIExcelUtil().createMRFromExcel(filePath);
        new POIExcelUtil().creatExcelFrame(mr,filePath);
    }

    @Test
    public void TestCreateMRFromExcel() throws Exception {
        String filePath = "C:\\619Test.xlsm";
        MRDemo mr = new POIExcelUtil().createMRFromExcel(filePath);
        assertEquals("Sin",mr.getProgramName());
        assertEquals(" follow-output=source-output ",mr.getOutputRelation());
        assertEquals("follow-input-1 = 3.14 - source-input-1",mr.getInputRelation());
        assertEquals(1,mr.getInputNumber());
        assertEquals(1, mr.getOutputNumber());
    }

    @Test
    public void TestGfi() throws Exception {
        String filePath = "F:\\619-3.xlsm";
        new POIExcelUtil().generateFollowInputByMR(filePath);
    }

    @Test
    public void TestGfo() throws Exception {
        String filePath = "F:\\619-3.xlsm";
        new POIExcelUtil().generateFollowOutputByMR(filePath);
    }

    @Test
    public void TestCallExternalProgram() throws Exception {
        String filaName = "Sin.java";
        String fileDir = "C:\\";
        String packageName = "test";

        ProgramPackage programPackage = new ProgramPackage();
        programPackage.setProgramMainFileDir(fileDir);
        programPackage.setProgramMainFileName(filaName);
        programPackage.setProgramMainFilePackageName(packageName);

        double[] outputs = new double[1];
        outputs = new POIExcelUtil().callExternalProgram(programPackage,new double[]{1},1);
        assertEquals(Math.sin(1), outputs[0]);
    }

    @Test
    public void TestAll() throws Exception {

        String filaName = "Sin.java";
        String fileDir = "C:\\";
        String packageName = "test";
        String filePath = "F:\\620-Test.xlsm";
        ProgramPackage programPackage = new ProgramPackage();
        programPackage.setProgramMainFileDir(fileDir);
        programPackage.setProgramMainFileName(filaName);
        programPackage.setProgramMainFilePackageName(packageName);

        POIExcelUtil poi = new POIExcelUtil();
        MRDemo mr = poi.createMRFromExcel(filePath);
        String excelPath = poi.creatExcelFrame(mr,filePath);
        System.out.println("please fill in Source-Input in Excel");
        System.out.println("Excel Path : " + excelPath);

        Scanner sc = new Scanner(System.in);
        String commond = sc.next();
        if(commond.equalsIgnoreCase("gfi")){
            poi.generateFollowInputByMR(excelPath);
            excelPath = poi.generateFollowOutputByProgram(programPackage,excelPath,mr);
            poi.generateFollowOutputByMR(excelPath);
            excelPath = poi.generateFollowOutputByProgram(programPackage,excelPath,mr);
        }

    }
}
