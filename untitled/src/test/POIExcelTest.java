package test;

import app.*;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Administrator on 2016/7/12.
 */
public class POIExcelTest {

    @Test
    public void testCreateMRFromExcel() throws Exception {
        String excel = "C:\\Users\\Administrator\\Desktop\\MRAutoFrame\\7-12-test.xlsm";
        POIExcelUtil poiExcelUtil = new POIExcelUtil();
        MRDemo mr = poiExcelUtil.createMRFromExcel(excel);
        assertEquals("2 2",mr.getInputParameterList().get(0).getDataLength().trim());
        assertEquals(Parameter.DataType.ARRAY,mr.getInputParameterList().get(0).getDataType());
        assertEquals(Parameter.DataType.DOUBLE,mr.getInputParameterList().get(1).getDataType());
        assertEquals("det",mr.getOutputParameterList().get(0).getName());
    }

    @Test
    public void testCreateExcelFrame() throws Exception {
        ProjectConfig.ExcelOutputPath = "C:\\Users\\Administrator\\Desktop\\MRAutoFrame\\";
        String excel = "C:\\Users\\Administrator\\Desktop\\MRAutoFrame\\7-12-test.xlsm";
        POIExcelUtil poiExcelUtil = new POIExcelUtil();
        MRDemo mr = poiExcelUtil.createMRFromExcel(excel);
        poiExcelUtil.creatExcelFrame(mr, excel);
    }

    @Test
    public void testGenerateFollowInput() throws Exception {
        ProjectConfig.ExcelOutputPath = "C:\\Users\\Administrator\\Desktop\\MRAutoFrame\\";
        String excel = "C:\\Users\\Administrator\\Desktop\\MRAutoFrame\\7-15-test-2.xlsm";
        POIExcelUtil poiExcelUtil = new POIExcelUtil();
        MRDemo mr = poiExcelUtil.createMRFromExcel(excel);
        String newExcel = poiExcelUtil.creatExcelFrame(mr, excel);
        poiExcelUtil.generateFollowInputByMR(newExcel);
    }


    @Test
    public void testRunOnSin() throws Exception{
        ProjectConfig.ExcelOutputPath = "C:\\Users\\Administrator\\Desktop\\MRAutoFrame\\";
        String excel = "C:\\Users\\Administrator\\Desktop\\MRAutoFrame\\sin.xlsm";
        String file = "C:\\Users\\Administrator\\Desktop\\MRAutoFrame";
        ProgramPackage programPackage = new ProgramPackage();
        programPackage.setProgramMainFilePackageName("demo");
        programPackage.setProgramMainFileDir(file);
        programPackage.setProgramMainFileName("Sin.java");
        programPackage.setInvokedMethod("execute");

        POIExcelUtil poiExcelUtil = new POIExcelUtil();
        MRDemo mr = poiExcelUtil.createMRFromExcel(excel);
        String newExcel = poiExcelUtil.creatExcelFrame(mr, excel);
        poiExcelUtil.generateFollowInputByMR(newExcel);
        String finalExcel = poiExcelUtil.generateOutputByProgram(programPackage, newExcel, mr);
        poiExcelUtil.checkOutputRelation(finalExcel);
        poiExcelUtil.readCheckResult(finalExcel, mr.getOutputParameterList().size());
    }

    @Test
    public void testRunOnMatrix()throws Exception{
        ProjectConfig.ExcelOutputPath = "C:\\Users\\Administrator\\Desktop\\MRAutoFrame\\";
        String excel = "C:\\Users\\Administrator\\Desktop\\MRAutoFrame\\matrix.xlsm";
        String file = "C:\\Users\\Administrator\\Desktop\\MRAutoFrame";
        ProgramPackage programPackage = new ProgramPackage();
        programPackage.setProgramMainFilePackageName("test");
        programPackage.setProgramMainFileDir(file);
        programPackage.setProgramMainFileName("MatrixDet.java");
        programPackage.setInvokedMethod("mathDeterminantCalculation");
//        programPackage.setNeedJars();

        POIExcelUtil poiExcelUtil = new POIExcelUtil();
        MRDemo mr = poiExcelUtil.createMRFromExcel(excel);
        String newExcel = poiExcelUtil.creatExcelFrame(mr, excel);
        poiExcelUtil.generateFollowInputByMR(newExcel);
        String finalExcel = poiExcelUtil.generateOutputByProgram(programPackage, newExcel, mr);
        poiExcelUtil.checkOutputRelation(finalExcel);
        poiExcelUtil.readCheckResult(finalExcel, mr.getOutputParameterList().size());
    }

    @Test
    public void testRunOnArff()throws Exception{
        ProjectConfig.ExcelOutputPath = "C:\\Users\\Administrator\\Desktop\\MRAutoFrame\\";
        String excel = "C:\\Users\\Administrator\\Desktop\\MRAutoFrame\\7-18-test-2.xlsm";
        String file = "C:\\Users\\Administrator\\Desktop\\MRAutoFrame";
        String[] jars = new String[]{"C:\\Users\\Administrator\\Desktop\\MRAutoFrame\\weka.jar"};
        ProgramPackage programPackage = new ProgramPackage();
        programPackage.setProgramMainFilePackageName("demo");
        programPackage.setProgramMainFileDir(file);
        programPackage.setProgramMainFileName("WekaDemo.java");
        programPackage.setInvokedMethod("KNN");
        programPackage.setNeedJars(jars);

        POIExcelUtil poiExcelUtil = new POIExcelUtil();
        MRDemo mr = poiExcelUtil.createMRFromExcel(excel);
        String newExcel = poiExcelUtil.creatExcelFrame(mr, excel);
        poiExcelUtil.generateFollowInputByMR(newExcel);
        String finalExcel = poiExcelUtil.generateOutputByProgram(programPackage,newExcel,mr);
        poiExcelUtil.checkOutputRelation(finalExcel);
        poiExcelUtil.readCheckResult(finalExcel,mr.getOutputParameterList().size());
    }
}
