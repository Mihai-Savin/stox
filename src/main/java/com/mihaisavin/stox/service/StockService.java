package com.mihaisavin.stox.service;

import com.mihaisavin.stox.dao.ws.StockDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

@RestController
public class StockService {

    @Autowired
    StockDAO dao;

    @Autowired
    AlarmService alarmService;

    public Map<String, Long> getStockData(Collection<String> symbols) {
        return dao.getStockData(symbols);
    }

    public Map<String, Long> getStockData(String... symbols) {
        return getStockData(Arrays.asList(symbols));
    }

    /**
     * Checks quotes for symbols that are monitored by user defined alarms that are active.
     */
    public Map<String, Long> getStocks() {

        Map<String, Long> stockData = null;

        System.out.println("I'me up and running...[getting watched symbols]");

        stockData = getStockData(alarmService.getWatchedSymbols());
        System.out.println(stockData);

        return stockData;
    }

}