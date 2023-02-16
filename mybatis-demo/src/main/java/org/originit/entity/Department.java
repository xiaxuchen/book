package org.originit.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class Department implements Serializable {

    
    private String id;
    
    private String name;
    
    private String tel;

    private Set<User> users;
    
    // getter setter toString equals hashcode
}