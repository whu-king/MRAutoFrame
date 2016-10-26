/*
 * Copyright (c) 2016.
 * Author:WangChen
 */

package app.recommend;

import app.model.MRModel;
import app.model.VBAModule;
import app.repository.MRDescriptionRepo;
import app.repository.MRDescriptionRepoLocalImpl;
import app.repository.VBAModuleRepo;
import app.repository.VBAModuleRepoLocalImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public class MRRecommend {

    private static MRDescriptionRepo mrDescriptionRepo = new MRDescriptionRepoLocalImpl();
    private static VBAModuleRepo vbaModuleRepo = new VBAModuleRepoLocalImpl();

    public static List<MRModel> recommendMRDescription(MRModel targetProgram) throws Exception {
        // TODO use more clever algorithm to recommend
        List<MRModel> allModels = mrDescriptionRepo.getAll();
        List<MRModel> modelsRecommended =  new ArrayList<MRModel>();
        for(MRModel model : allModels){
            if(targetProgram.equalsInProgramInfoWith(model)){
                modelsRecommended.add(model);
            }
        }
        return modelsRecommended;
    }

    public static List<VBAModule> recommendMRImplementation(List<MRModel> mrs) throws IOException {
        List<VBAModule> allVBAModuls = vbaModuleRepo.getAll();
        List<VBAModule> recommendVBA = new ArrayList();
        for(MRModel mrModel : mrs){
            for(VBAModule vba : allVBAModuls){
                if(isStronglyRelated(mrModel,vba) && !recommendVBA.contains(vba)){
                    recommendVBA.add(vba);
                }
            }
        }
        return recommendVBA;
    }

    public static boolean isStronglyRelated(MRModel mr, VBAModule vba){
        // todo how to judge
        return true;
    }
}
