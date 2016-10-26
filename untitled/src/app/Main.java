package app;

import app.excel.ExcelOperatorWithLogic;
import app.model.MRModel;
import app.model.VBAModule;
import app.recommend.MRRecommend;
import app.excel.POIExcelUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/6/20.
 */
public class Main {
    public static void main(String[] args) throws Exception {

        String excelPath = "";
        String configPath = "";
        int outputNumber = 1;

        if(!UserInteraction.IsUserSelectReplicateMode()){

            MRModel targetProgram = UserInteraction.ConstructTargetProgramFromUserInput();
            ExcelOperatorWithLogic.targetProgram = targetProgram;
            outputNumber = targetProgram.getOutputParameterList().size();
            List<MRModel> MRDescriptions = MRRecommend.recommendMRDescription(targetProgram);
            UserInteraction.showMRDescriptionsToUsers(MRDescriptions);
            List<MRModel> MRRecommendedChosen = UserInteraction.acceptUserChoosingOfMRDescription(MRDescriptions);
            List<MRModel> MRMadeByUser = UserInteraction.acceptMRDescriptionMadeByUser();
//          not necessary for this version
//        UserInteraction.addUserMadeMRToRepo(MRMadeByUser, targetProgram);
            MRRecommendedChosen.addAll(MRMadeByUser);
            ExcelOperatorWithLogic.targetProgram.setMRNumber(MRRecommendedChosen.size());
            for(MRModel model : MRRecommendedChosen){
                model.copyProgramInfoFrom(targetProgram);
            }
            excelPath = ExcelOperatorWithLogic.creatExcelFrame(MRRecommendedChosen, ProjectConfig.TemplateExcel);
            List<VBAModule> vbaModules = MRRecommend.recommendMRImplementation(MRRecommendedChosen);
            UserInteraction.showVBAModulesToUsers(vbaModules);
            List<VBAModule> vbaModulesChosen = UserInteraction.acceptUserChoosingOfVBA(vbaModules);
            POIExcelUtil.importVBAModule(vbaModulesChosen, excelPath);

            VBAModule userMRExpression = UserInteraction.ConstructMRExpressionFromUserInput();
            POIExcelUtil.importVBAModule(userMRExpression, excelPath);
            List<VBAModule> userMadeVBA = UserInteraction.constructVBAModuleFromUserInput();
            POIExcelUtil.importVBAModule(userMadeVBA, excelPath);

            excelPath = UserInteraction.ImportTestDataFromUserDataFile(excelPath);
        }else{
            excelPath = UserInteraction.GetExcelPathForRunning();
            ExcelOperatorWithLogic.constructTargetProgramFromReplicateExcel(excelPath);
            outputNumber = ExcelOperatorWithLogic.targetProgram.getOutputParameterList().size();
        }

        configPath = UserInteraction.getConfigFilePathFrom(args);
        ProjectConfig.ReadClassRunningConfigFromFile(configPath);

        ExcelOperatorWithLogic.generateFollowInputByMR(excelPath);
        excelPath = ExcelOperatorWithLogic.generateOutputByProgram(ProjectConfig.RunningProgram,excelPath);
        ExcelOperatorWithLogic.checkOutputRelation(excelPath);
        ExcelOperatorWithLogic.readCheckResult(excelPath);


    }
}
