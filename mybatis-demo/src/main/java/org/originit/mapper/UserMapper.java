package org.originit.mapper;

import org.apache.ibatis.annotations.Param;
import org.originit.entity.User;

import java.util.List;

public interface UserMapper {

    List<User> findAll();

    List<User> findLazyAll();

    User findAllByDepartmentId(@Param("id") String id);
}