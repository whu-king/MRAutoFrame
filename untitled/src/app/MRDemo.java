package app;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class MRDemo {

    public String ProgramName;
    public String inputRelation;
    public String outputRelation;
    public List<Parameter> inputParameterList;
    public List<Parameter> outputParameterList;
    public int MRNumber;



    public String getProgramName() {
        return ProgramName;
    }

    public void setProgramName(String programName) {
        ProgramName = programName;
    }

    public String getInputRelation() {
        return inputRelation;
    }

    public void setInputRelation(String inputRelation) {
        this.inputRelation = inputRelation;
    }

    public String getOutputRelation() {
        return outputRelation;
    }

    public void setOutputRelation(String outputRelation) {
        this.outputRelation = outputRelation;
    }

    public List<Parameter> getInputParameterList() {
        return inputParameterList;
    }

    public void setInputParameterList(List<Parameter> inputParameterList) {
        this.inputParameterList = inputParameterList;
    }

    public List<Parameter> getOutputParameterList() {
        return outputParameterList;
    }

    public void setOutputParameterList(List<Parameter> outputParameterList) {
        this.outputParameterList = outputParameterList;
    }

    public int getMRNumber() {
        return MRNumber;
    }

    public void setMRNumber(int MRNumber) {
        this.MRNumber = MRNumber;
    }

}
