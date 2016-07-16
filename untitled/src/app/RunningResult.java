/*
 * Copyright (c) 2016.
 * Author:WangChen
 */

package app;

/**
 * Created by Administrator on 2016/7/16.
 */
public class RunningResult {

    private int rowNum;
    private double runtime;
    private double coverRate;
    private boolean isSuccess;

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public double getRuntime() {
        return runtime;
    }

    public void setRuntime(double runtime) {
        this.runtime = runtime;
    }

    public double getCoverRate() {
        return coverRate;
    }

    public void setCoverRate(double coverRate) {
        this.coverRate = coverRate;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
