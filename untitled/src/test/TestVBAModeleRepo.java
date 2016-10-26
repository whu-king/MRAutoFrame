/*
 * Copyright (c) 2016.
 * Author:WangChen
 */

package test;

import app.model.VBAModule;
import app.repository.VBAModuleRepoLocalImpl;
import org.junit.Test;

/**
 * Created by Administrator on 2016/9/29.
 */
public class TestVBAModeleRepo {

    @Test
    public void test() throws Exception {
        VBAModule module = new VBAModule();
        module.setName("Math");
        module.setMethodDescription("include power,sin,cos etc");
        module.setOperationOnDataType("Number");
        module.setLocalPath("C:\\Users\\Administrator\\Desktop\\common1.bas");
        new VBAModuleRepoLocalImpl().addNew(module);
    }
}
