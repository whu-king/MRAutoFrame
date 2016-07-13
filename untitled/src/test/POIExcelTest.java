package test;

import app.MRDemo;
import app.POIExcelUtil;
import app.Parameter;
import app.ProjectConfig;
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
        poiExcelUtil.creatExcelFrame(mr,excel);
    }
}
