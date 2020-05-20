package com.xbcai.model;

import lombok.Data;

import java.io.Serializable;

/**
 * t_user
 * @author 
 */
@Data
public class User implements Serializable {
    private String id;

    private String name;

    private String sex;

    private String address;

    private static final long serialVersionUID = 1L;
}