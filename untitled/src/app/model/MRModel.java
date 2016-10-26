package app.model;



import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class MRModel {

    public String ProgramName;
    public String inputRelation;
    public String outputRelation;
    public List<Parameter> inputParameterList;
    public List<Parameter> outputParameterList;
    public int MRNumber;
    public String domain;


    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

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

    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("%N " + getProgramName() + "\n");
        sb.append("%D " + domain + "\n");

        sb.append("%IN ");
        for(Parameter p : getInputParameterList()){
            sb.append(p.getName() + " ");
        }
        sb.append("\n");
        sb.append("%IT ");
        for(Parameter p : getInputParameterList()){
            sb.append(p.getDataType().getName() + " ");
        }
        sb.append("\n");
        sb.append("%ON ");
        for(Parameter p : getOutputParameterList()){
            sb.append(p.getName() + " ");
        }
        sb.append("\n");
        sb.append("%OT ");
        for(Parameter p : getOutputParameterList()){
            sb.append(p.getDataType().getName() + " ");
        }
        sb.append("\n");
        sb.append("%IR " + inputRelation + "\n");
        sb.append("%OR " + outputRelation + "\n");
        sb.append("\n");
        return sb.toString();
    }

    public boolean equalsInProgramInfoWith(Object anObject){
        if(this == anObject) return true;
        if(anObject instanceof MRModel){
            MRModel anotherModel = (MRModel) anObject;
            if(!this.getProgramName().equalsIgnoreCase(anotherModel.getProgramName())) return false;
            if(!this.getDomain().equalsIgnoreCase(anotherModel.getDomain())) return false;
            for(int i = 0; i < this.getInputParameterList().size(); i++){
                Parameter p1 = this.getInputParameterList().get(i);
                Parameter p2 = anotherModel.getInputParameterList().get(i);
                if(!p1.getDataType().getName().equalsIgnoreCase(p2.getDataType().getName())) return false;
            }

            for(int i = 0; i < this.getOutputParameterList().size(); i++){
                Parameter p1 = this.getOutputParameterList().get(i);
                Parameter p2 = anotherModel.getOutputParameterList().get(i);
                if(!p1.getDataType().getName().equalsIgnoreCase(p2.getDataType().getName())) return false;
            }
        }
        return true;
    }

    public boolean equalsInRelationWith(Object anObject){
        //TODO use wordnet to do semantic compare
        if(this == anObject) return true;
        if(anObject instanceof MRModel){
            MRModel anotherModel = (MRModel) anObject;
            if(!this.getInputRelation().equalsIgnoreCase(anotherModel.getInputRelation())) return false;
            if(!this.getOutputRelation().equalsIgnoreCase(anotherModel.getOutputRelation())) return false;
        }
        return true;
    }

    public boolean equals(Object anObject){
        if(this.equalsInProgramInfoWith(anObject) &&
                this.equalsInRelationWith(anObject))
            return true;
        else return false;
    }

    public void copyProgramInfoFrom(MRModel model){
        this.setDomain(model.getDomain());
        this.setProgramName(model.getProgramName());
        this.setInputParameterList(model.getInputParameterList());
        this.setOutputParameterList(model.getOutputParameterList());
    }




}
