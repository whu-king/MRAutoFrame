/*
 * Copyright (c) 2016.
 * Author:WangChen
 */

package test;

import app.repository.MRDescriptionRepoLocalImpl;
import app.model.MRModel;
import app.model.Parameter;
import org.junit.Test;

/**
 * Created by Administrator on 2016/9/28.
 */
public class MRDescriptionRepoLocalImplTest {

    @Test
    public void testAddNew() throws Exception {
        MRModel model = new MRModel();
        model.setDomain("Math");
        model.setProgramName("Sin");
        String inputParamString = "double input";
        String outputParamString = "double output";
        model.setInputParameterList(Parameter.valueOf(inputParamString));
        model.setOutputParameterList(Parameter.valueOf(outputParamString));
        model.setInputRelation("follow-input = - source-input");
        model.setOutputRelation("follow-output = - source-output");
        new MRDescriptionRepoLocalImpl().addNew(model);
    }
}
