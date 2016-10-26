package app.model;

/**
 * Created by Administrator on 2016/6/14.
 */
public class DataPackage {

    private String[] data;
    private long excelRowNum;

    public DataPackage(){}
    public DataPackage(String[] data, long rowNum){
        this.data = data;
        this.excelRowNum = rowNum;
    }
    public long getExcelRowNum() {
        return excelRowNum;
    }

    public void setExcelRowNum(long excelRowNum) {
        this.excelRowNum = excelRowNum;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public static DataPackage findExpectedOutputByRow(long rowNum,DataPackage[] datas){
        for(int i = 0 ; i < datas.length; i++){
            if(datas[i].getExcelRowNum() == rowNum) return datas[i];
        }
        return null;
    }
}
