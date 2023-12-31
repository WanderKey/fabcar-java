package com.tsinghua.fabcar.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tsinghua.fabcar.dto.Car;
import com.tsinghua.fabcar.dto.CarQueryResult;
import com.tsinghua.fabcar.dto.Result;
import com.tsinghua.fabcar.service.CarService;
import com.tsinghua.fabcar.utils.FabricProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.hyperledger.fabric.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/car")
@Slf4j
@AllArgsConstructor
public class CarController {

    final Gateway gateway;

    final Contract contract;

    final FabricProperties fabricProperties;

    @Autowired
    private CarService carService;

    /**
     * 初始化账本
     * @return
     * @throws GatewayException
     */

    @PostMapping("/init")
    public Result initLedeger() throws EndorseException, CommitException, SubmitException, CommitStatusException {
        log.info("========初始化车辆账本===========");
        carService.init();
        return Result.success();
    }

    /**
     * 根据key查询车辆信息
     * @param key
     * @return
     * @throws GatewayException
     */

    @GetMapping("/{key}")
    public Result queryCarByKey(@PathVariable("key") String key) throws GatewayException {

        log.info("查询车辆信息，key: {}", key);
        Car car = carService.queryCarByKey(key);
        return Result.success(car);
    }


    /**
     * 创建车辆
     * @param car
     * @return
     * @throws GatewayException
     */
    @PostMapping("/createCar")
    public Result createCar(@RequestBody Car car) throws GatewayException, CommitException {
        log.info("新增车辆信息，car: {}", car);
        carService.createCar(car);
        return Result.success();
    }

    @GetMapping("/all")
    public Result queryAllCars() throws GatewayException {
        log.info("查询所有车辆信息");
        List<CarQueryResult> carList = carService.queryAllCars();
        return Result.success(carList);
    }

    @PutMapping("/changeCarOwner")
    public Result changeCarOwner(@RequestBody Car car) throws GatewayException {
        log.info("修改车辆归属信息");
        Car newCar = carService.ChangeCarOwner(car);
        return Result.success(newCar);
    }
}
