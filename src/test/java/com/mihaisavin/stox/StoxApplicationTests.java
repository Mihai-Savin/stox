package com.mihaisavin.stox;

import com.mihaisavin.stox.service.StockService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StoxApplicationTests {

	@Autowired
	StockService stockService;

	@Test
	public void contextLoads() {
	}

	@Test
	public void checkGetOneStockDataGoldenPath() {
		String checkedSymbol = "MSFT";
		Map<String, Double> stockData = stockService.getStockData(checkedSymbol);

		Assert.assertNotNull(stockData);
		Assert.assertTrue(stockData.containsKey(checkedSymbol));
		Assert.assertTrue(stockData.get(checkedSymbol) > 1);
	}

	@Test
	public void checkGetThreeStockDataGoldenPath() {
		List<String> checkedSymbols = Arrays.asList("MSFT", "AAPL", "ORCL");

		Map<String, Double> stockData = stockService.getStockData(checkedSymbols);

		Assert.assertNotNull(stockData);

		Assert.assertTrue(stockData.containsKey(checkedSymbols.get(0)));
		Assert.assertTrue(stockData.containsKey(checkedSymbols.get(1)));
		Assert.assertTrue(stockData.containsKey(checkedSymbols.get(2)));

		Assert.assertTrue(stockData.get(checkedSymbols.get(0)) > 1);
		Assert.assertTrue(stockData.get(checkedSymbols.get(1)) > 1);
		Assert.assertTrue(stockData.get(checkedSymbols.get(2)) > 1);
	}

	/**
	 * No stock data should be returned in case of an invalid stock symbol.
	 */
	@Test
	public void checkGetOneStockDataInvalidSymbol() {
		String checkedSymbol = "noValidSymbol";
		Map<String, Double> stockData = stockService.getStockData(checkedSymbol);

		Assert.assertEquals(0, stockData.size());
	}

}
