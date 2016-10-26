/*
 * Copyright (c) 2016.
 * Author:WangChen
 */

package app.repository;

import app.ProjectConfig;
import app.fileUtil.Transformation;
import app.model.MRModel;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public class MRDescriptionRepoLocalImpl implements MRDescriptionRepo {

    @Override
    public void addNew(MRModel mrModel) throws Exception {
        List<MRModel> all = getAll();
        for(MRModel model : all){
            if(model.equals(mrModel)) return;
        }
        Gson gson = new Gson();
        String modelJson = gson.toJson(mrModel);
        Transformation.String2File("#" + modelJson + "\n",ProjectConfig.MRRepo);
    }

    @Override
    public List<MRModel> getAll() throws Exception {
        List<MRModel> allModels = new ArrayList<MRModel>();
        File ModelRepo = new File(ProjectConfig.MRRepo);
        String fileString = Transformation.File2String(ModelRepo);
        String[] modelString = fileString.split("#");
        Gson gson = new Gson();
        for(String str : modelString){
            if(!str.equalsIgnoreCase("")){
                allModels.add(gson.fromJson(str,MRModel.class));
            }
        }
        return allModels;
    }
}
