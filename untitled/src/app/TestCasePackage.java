//package app;
//
//import org.junit.*;
//import org.junit.runner.RunWith;
//import org.junit.runners.Parameterized;
//
//import java.util.*;
//
//import static junit.framework.Assert.assertEquals;
//
///**
// * Created by Administrator on 2016/6/8.
// */
//
//@RunWith(Parameterized.class)
//public class TestCasePackage {
//
//    private static ProgramPackage programPackage;
//    private static MRDemo mr;
//    private static DataPackage[] inputData;
//    public static DataPackage[] expectedOutputData;
//    public static List<DataPackage> followOutputData = new ArrayList<DataPackage>(),
//                                        sourceOutputData = new ArrayList<DataPackage>();
//
//    private DataPackage singleInputRow;
//
//    public TestCasePackage (DataPackage data){
//        singleInputRow = data;
//    }
//
//    public static void setExpectedOutputData(DataPackage[] data){
//        expectedOutputData = data;
//    }
//
//    public static void setMr(MRDemo mrI){
//        mr = mrI;
//    }
//
//    public static void setProgramPackage(ProgramPackage pro){
//        programPackage = pro;
//    }
//
//    public static void setData(DataPackage[] dataI){
//        inputData = dataI;
//    }
//
//
//    @Parameterized.Parameters
//    public static Collection params(){
//        ArrayList<Object> list = new ArrayList<Object>();
//        for(DataPackage data : inputData){
//            Object[] arr = new Object[]{data};
//            list.add(arr);
//        }
//        return list;
//    }
//
//    public void checkProgramPath() throws Exception {
//        if(programPackage.getProgramMainFileDir() == null || programPackage.getProgramMainFileDir().equalsIgnoreCase("")
//                ||programPackage.getProgramMainFileName() == null || programPackage.getProgramMainFileName().equalsIgnoreCase(""))
//            throw new Exception("Program path is not located");
//    }
//
//    @Test
//    public void testCallExternalProgram() throws Exception {
//        checkProgramPath();
//        POIExcelUtil poiExcelUtil = new POIExcelUtil();
//        double[] excelRow = singleInputRow.getData();
//        long rowNum = singleInputRow.getExcelRowNum();
//        double[] sourceInputs = new double[mr.getInputNumber()];
//        double[] followInputs = new double[mr.getInputNumber()];
//        for (int i = 0; i < mr.getInputNumber(); i++) {
//            sourceInputs[i] = excelRow[i];
//        }
//        for (int i = mr.getInputNumber(), count = 0; i < mr.getInputNumber() * 2; i++, count++) {
//            followInputs[count] = excelRow[i];
//        }
//        double[] SourceOutput = poiExcelUtil.callExternalProgram(programPackage,sourceInputs,mr.getOutputNumber());
//        double[] FollowOutput = poiExcelUtil.callExternalProgram(programPackage,followInputs,mr.getOutputNumber());
//        sourceOutputData.add(new DataPackage(SourceOutput,rowNum));
//        followOutputData.add(new DataPackage(FollowOutput, rowNum));
//
//        DataPackage expectedOutput = DataPackage.findExpectedOutputByRow(rowNum,expectedOutputData);
//        double zero = 1E-10;
//        for(int i = 0; i < FollowOutput.length; i ++){
//            boolean flag = false;
//            if(Math.abs(FollowOutput[i]-expectedOutput.getData()[i]) < zero){
//                flag = true;
//            }
//            assertEquals(true,flag);
//        }
//    }
//
//
//}
