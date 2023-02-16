package org.originit.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import org.originit.entity.Department;

import java.util.List;

public interface DepartmentDao {
    
    List<Department> findAll();
    
    Department findById(String id);

    List<Department> findLazyAll();
}