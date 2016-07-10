package app;

/**
 * Created by Administrator on 2016/6/14.
 */
public class DataPackage {

    private double[] data;
    private long excelRowNum;

    public DataPackage(){}
    public DataPackage(double[] data, long rowNum){
        this.data = data;
        this.excelRowNum = rowNum;
    }
    public long getExcelRowNum() {
        return excelRowNum;
    }

    public void setExcelRowNum(long excelRowNum) {
        this.excelRowNum = excelRowNum;
    }

    public double[] getData() {
        return data;
    }

    public void setData(double[] data) {
        this.data = data;
    }

    public static DataPackage findExpectedOutputByRow(long rowNum,DataPackage[] datas){
        for(int i = 0 ; i < datas.length; i++){
            if(datas[i].getExcelRowNum() == rowNum) return datas[i];
        }
        return null;
    }
}
