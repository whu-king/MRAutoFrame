/*
 * Copyright (c) 2016.
 * Author:WangChen
 */

package app.repository;

import app.model.VBAModule;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/9/29.
 */
public interface VBAModuleRepo {

    public void addNew(VBAModule vbaModule) throws Exception;
    public List<VBAModule> getAll() throws IOException;
}
