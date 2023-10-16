package com.tsinghua.fabcar.service;

import com.tsinghua.fabcar.dto.Car;
import org.hyperledger.fabric.client.*;

import java.util.List;

public interface CarService {

    /**
     * 初始化账本
     */
    void init() throws EndorseException, CommitException, SubmitException, CommitStatusException;

    /**
     * 根据key获取车辆信息
     * @param key
     * @return
     */
    Car queryCarByKey(String key) throws GatewayException;

    /**
     * 新增车辆信息
     * @param car
     */
    void createCar(Car car) throws EndorseException, CommitException, SubmitException, CommitStatusException;

    /**
     * 查询全部车辆信息
     * @return
     */
    List<Car> queryAllCars() throws GatewayException;
}
