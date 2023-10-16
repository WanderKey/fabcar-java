package com.tsinghua.fabcar.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.hyperledger.fabric.client.ChaincodeEvent;
import org.hyperledger.fabric.client.CloseableIterator;
import org.hyperledger.fabric.client.Network;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * 链码事件监听对象
 */
@Slf4j
public class ChaincodeEventListener implements Runnable{

    final Network network;
    final FabricProperties fabricProperties;



    public ChaincodeEventListener(Network network, FabricProperties fabricProperties) {
        this.network = network;
        this.fabricProperties = fabricProperties;

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                thread.setName(this.getClass() + "chaincode_event_listener");
                return thread;
            }
        });
        executor.execute(this);
    }

    @Override
    public void run() {
        CloseableIterator<ChaincodeEvent> events = network.getChaincodeEvents(fabricProperties.getChaincodeName());
        log.info("chaincodeEvents {} " , events);

        while(events.hasNext()) {
            ChaincodeEvent event = events.next();

            log.info("receive chaincode event {} , transaction id {} ,  block number {} , payload {} "
                    , event.getEventName() , event.getTransactionId() , event.getBlockNumber() , StringUtils.newStringUtf8(event.getPayload()));

        }
    }
}
