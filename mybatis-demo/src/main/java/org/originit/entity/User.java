package org.originit.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    
    private String id;
    
    private String name;
    
    private Integer age;
    
    private Date birthday;
    
    private Department department;
    
    // getter setter equals hashcode toString
}