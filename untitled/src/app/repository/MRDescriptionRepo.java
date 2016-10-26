/*
 * Copyright (c) 2016.
 * Author:WangChen
 */

package app.repository;

import app.model.MRModel;

import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public interface MRDescriptionRepo {

    public void addNew(MRModel mrModel) throws Exception;
    public List<MRModel> getAll() throws Exception;

}
