package app;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Administrator on 2016/6/8.
 */

@RunWith(Parameterized.class)
public class TestCasePackage {

    private static ProgramPackage programPackage;
    private static MRDemo mr;
    private static DataPackage[] inputData;
    public static List<DataPackage> followOutputData = new ArrayList<DataPackage>(),
                                        sourceOutputData = new ArrayList<DataPackage>();

    private DataPackage singleInputRow;

    public TestCasePackage (DataPackage data){
        singleInputRow = data;
    }

    public static void setMr(MRDemo mrI){
        mr = mrI;
    }

    public static void setProgramPackage(ProgramPackage pro){
        programPackage = pro;
    }

    public static void setData(DataPackage[] dataI){
        inputData = dataI;
    }


    @Parameterized.Parameters
    public static Collection params(){
        ArrayList<Object> list = new ArrayList<Object>();
        for(DataPackage data : inputData){
            Object[] arr = new Object[]{data};
            list.add(arr);
        }
        return list;
    }

    public void checkProgramPath() throws Exception {
        if(programPackage.getProgramMainFileDir() == null || programPackage.getProgramMainFileDir().equalsIgnoreCase("")
                ||programPackage.getProgramMainFileName() == null || programPackage.getProgramMainFileName().equalsIgnoreCase(""))
            throw new Exception("Program path is not located");
    }

    @Test
    public void testCallExternalProgram() throws Exception {
        checkProgramPath();
        POIExcelUtil poiExcelUtil = new POIExcelUtil();
        String[] excelRow = singleInputRow.getData();
        long rowNum = singleInputRow.getExcelRowNum();
        String[] sourceInputs = new String[mr.getInputParameterList().size()];
        String[] followInputs = new String[mr.getInputParameterList().size()];

        for (int i = 0; i < mr.getInputParameterList().size(); i++) {
            sourceInputs[i] = excelRow[i];
        }
        for (int i = mr.getInputParameterList().size(), count = 0; i < mr.getInputParameterList().size() * 2; i++, count++) {
            followInputs[count] = excelRow[i];
        }
        String sourceOutput = ClassRunner.RunJavaProgramByReflect(programPackage,sourceInputs);
        String followOutput = ClassRunner.RunJavaProgramByReflect(programPackage,followInputs);
        sourceOutputData.add(new DataPackage(new String[]{sourceOutput},rowNum));
        followOutputData.add(new DataPackage(new String[]{followOutput}, rowNum));
    }


}
