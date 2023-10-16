package com.tsinghua.fabcar.controller;


import com.tsinghua.fabcar.dto.Result;
import com.tsinghua.fabcar.utils.FabricProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.client.Contract;
import org.hyperledger.fabric.client.Gateway;
import org.hyperledger.fabric.client.GatewayException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/car")
@Slf4j
@AllArgsConstructor
public class CarController {

    final Gateway gateway;

    final Contract contract;

    final FabricProperties fabricProperties;

    @GetMapping("/{key}")
    public Result queryCarByKey(@PathVariable("key") String key) throws GatewayException {

        byte[] resultData = contract.evaluateTransaction("queryCar", key);
        return Result.success(resultData);
    }
}
