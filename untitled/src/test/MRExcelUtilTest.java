package test;

import app.DataPackage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Administrator on 2016/6/8.
 */

@RunWith(Parameterized.class)
public class MRExcelUtilTest {

    private static String fileName,fileDir,packageName;
    private static int programOutputNumber;
    private static DataPackage[] datas;
    private DataPackage dataPackage;
    private  MRExcelUtil mrExcelUtil = new MRExcelUtil();

    public MRExcelUtilTest(DataPackage data){
        dataPackage = data;
    }

    public static void setProgramPath(String fileNameI,String fileDirI,String packageNameI){
        fileName = fileNameI;
        fileDir = fileDirI;
        packageName = packageNameI;
    }

    public static void setProgramOutputNumber(int programOutputNumberI){
        programOutputNumber = programOutputNumberI;
    }

    public static void setData(DataPackage[] dataI){
        datas = dataI;
    }

    @Parameterized.Parameters
    public static Collection params(){
//        Object [] data = new Object[]{datas};
//        Object [][] objects = new Object[datas.length][1];
        ArrayList<Object> list = new ArrayList<Object>();
        for(DataPackage data : datas){
            Object[] arr = new Object[]{data};
            list.add(arr);
        }

        return list;
    }

    public void checkProgramPath() throws Exception {
        if(fileName == null || fileName.equalsIgnoreCase("")||fileDir == null || fileDir.equalsIgnoreCase(""))
            throw new Exception("Program path is not located");
    }

    @Test
    public void testCallExternalProgram() throws Exception {
//        checkProgramPath();
////        String[] result = mrExcelUtil.callExternalProgram(fileName,fileDir,packageName,dataPackage.getData(),programOutputNumber);
//        for(String str : result){
//            System.out.println(str);
//        }
//        String s =  result[0];
//        assertEquals("-1", s);
    }


}
