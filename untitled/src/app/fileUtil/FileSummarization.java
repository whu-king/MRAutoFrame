/*
 * Copyright (c) 2016.
 * Author:WangChen
 */

package app.fileUtil;

import java.io.IOException;

/**
 * Created by Administrator on 2016/9/29.
 */
public class FileSummarization {

    public String forVBAModule(String filepath) throws IOException {
        String file = Transformation.File2String(filepath);
        String[] lines = file.split("\\n");
        System.out.println(lines);
        return null;
    }

    public static void main(String[] args){

    }
}
