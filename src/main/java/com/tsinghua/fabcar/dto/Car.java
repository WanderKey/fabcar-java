package com.tsinghua.fabcar.dto;

import lombok.Data;

/**
 * Car的实体对象类
 */

@Data
public class Car {

    private String key;
    private String make;
    private String model;
    private String color;
    private String owner;
}
