package com.tsinghua.fabcar.dto;


import lombok.Data;

/**
 * 全部车辆数据查询结果对象
 */
@Data
public class CarQueryResult {
    private String key;
    private Car record;
}
