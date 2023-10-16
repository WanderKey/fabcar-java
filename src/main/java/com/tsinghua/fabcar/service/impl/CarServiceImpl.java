package com.tsinghua.fabcar.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tsinghua.fabcar.dto.Car;
import com.tsinghua.fabcar.dto.CarQueryResult;
import com.tsinghua.fabcar.service.CarService;
import com.tsinghua.fabcar.utils.FabricProperties;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.hyperledger.fabric.client.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {

    final Gateway gateway;

    final Contract contract;

    @Override
    public void init() throws EndorseException, CommitException, SubmitException, CommitStatusException {
        contract.submitTransaction("initLedger");
    }

    @Override
    public Car queryCarByKey(String key) throws GatewayException {
        byte[] resultData = contract.evaluateTransaction("queryCar", key);
        String jsonData = StringUtils.newStringUtf8(resultData);
        Car car = JSON.parseObject(jsonData, Car.class);
        car.setKey(key);
        return car;
    }

    @Override
    public void createCar(Car car) throws EndorseException, CommitException, SubmitException, CommitStatusException {

        contract.submitTransaction("createCar", car.getKey(), car.getMake(), car.getModel(), car.getColor(), car.getOwner());

    }

    @Override
    public List<CarQueryResult> queryAllCars() throws GatewayException {
        byte[] resultData = contract.evaluateTransaction("queryAllCars");
        String jsonData = StringUtils.newStringUtf8(resultData);
        return JSON.parseArray(jsonData, CarQueryResult.class);
    }
}
