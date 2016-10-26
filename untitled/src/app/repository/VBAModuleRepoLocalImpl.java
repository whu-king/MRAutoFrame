/*
 * Copyright (c) 2016.
 * Author:WangChen
 */

package app.repository;

import app.ProjectConfig;
import app.fileUtil.Transformation;
import app.model.VBAModule;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/29.
 */
public class VBAModuleRepoLocalImpl implements VBAModuleRepo {

    @Override
    public void addNew(VBAModule vbaModule) throws Exception {
        //TODO add user interaction for this function
        Gson gson = new Gson();
        String vbaModuleString = gson.toJson(vbaModule);
        Transformation.String2File("#" + vbaModuleString + "\n", ProjectConfig.VBARepo);
    }

    @Override
    public List<VBAModule> getAll() throws IOException {
        List<VBAModule> allModules = new ArrayList<VBAModule>();
        String allModuleString = Transformation.File2String(new File(ProjectConfig.VBARepo));
        String[] moduleStringArrays = allModuleString.split("#");
        Gson gson = new Gson();
        for(String moduleString : moduleStringArrays){
            if(!moduleString.equalsIgnoreCase("")){
                VBAModule vbaModule = gson.fromJson(moduleString,VBAModule.class);
                allModules.add(vbaModule);
            }
        }
        return allModules;
    }
}
