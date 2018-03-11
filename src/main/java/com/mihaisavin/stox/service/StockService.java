package com.mihaisavin.stox.service;

import com.mihaisavin.stox.dao.ws.StockDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

@RestController
public class StockService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);

    @Autowired
    StockDAO dao;

    @Autowired
    AlarmService alarmService;

    public Map<String, Double> getStockData(Collection<String> symbols) {
        return dao.getStockData(symbols);
    }

    public Map<String, Double> getStockData(String... symbols) {
        return getStockData(Arrays.asList(symbols));
    }

    /**
     * Checks quotes for symbols that are monitored by user defined alarms that are active.
     */
    public Map<String, Double> getStocks() {

        Map<String, Double> stockData;

        LOGGER.info("I'me up and running...[getting watched symbols]");

        stockData = getStockData(alarmService.getWatchedSymbols());

        LOGGER.info("Data for " + stockData.size() + " symbols was fetched.");

        return stockData;
    }

}