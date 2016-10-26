/*
 * Copyright (c) 2016.
 * Author:WangChen
 */

package app.model;

/**
 * Created by Administrator on 2016/9/29.
 */
public class VBAModule {
    private String name;
    private String OperationOnDataType;
    private String MethodDescription;
    private String localPath = "default";

    public String getMethodDescription() {
        return MethodDescription;
    }

    public void setMethodDescription(String methodDescription) {
        MethodDescription = methodDescription;
    }

    public String getOperationOnDataType() {
        return OperationOnDataType;
    }

    public void setOperationOnDataType(String operationOnDataType) {
        OperationOnDataType = operationOnDataType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
}
