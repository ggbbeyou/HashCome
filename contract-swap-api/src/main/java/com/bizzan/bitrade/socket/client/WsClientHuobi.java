package com.bizzan.bitrade.socket.client;

import com.bizzan.bitrade.engine.ContractCoinMatchFactory;
import com.bizzan.bitrade.entity.ContractCoin;
import com.bizzan.bitrade.job.ExchangePushJob;
import com.bizzan.bitrade.service.ContractCoinService;
import com.bizzan.bitrade.service.ContractMarketService;
import com.bizzan.bitrade.socket.ws.WebSocketHuobi;
import com.bizzan.bitrade.util.WebSocketConnectionManage;
import org.apache.http.annotation.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class WsClientHuobi {

    private ContractCoinService contractCoinService;

    private ContractMarketService marketService;

    private ExchangePushJob exchangePushJob;

    private ContractCoinMatchFactory matchFactory;

    public WsClientHuobi(ContractCoinMatchFactory factory) {
        this.matchFactory = factory;
    }

    public void run() {

        List<ContractCoin> contractCoinList = contractCoinService.findAll();

        try {
//            URI uri = new URI("wss//futures.huobi.com/ws");// 国内不被墙的地址/wss://api.huobi.pro/ws   ws://api.huobi.br.com:443/ws
            URI uri = new URI("wss://www.huobi.com/-/s/pro/ws");// 国内不被墙的地址/wss://api.huobi.pro/ws   ws://api.huobi.br.com:443/ws
            WebSocketHuobi ws = new WebSocketHuobi(uri, matchFactory, marketService, exchangePushJob, contractCoinService);

            WebSocketConnectionManage.setWebSocket(ws);
            WebSocketConnectionManage.getClient().connect(ws);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void setContractCoinService(ContractCoinService service) {
        this.contractCoinService = service;
    }
    public void setContractMarketService(ContractMarketService service) { this.marketService = service; }
    public void setExchangePushJob(ExchangePushJob pushJob) { this.exchangePushJob = pushJob; }
}
