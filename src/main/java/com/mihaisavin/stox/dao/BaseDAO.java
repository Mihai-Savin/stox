package com.mihaisavin.stox.dao;

import com.mihaisavin.stox.domain.AbstractModel;
import java.util.Collection;

public interface BaseDAO<T extends AbstractModel> {

    Collection<T> getAll(long param);

    T findById(Long id);

    T update(T model);

    boolean delete(T model);
}