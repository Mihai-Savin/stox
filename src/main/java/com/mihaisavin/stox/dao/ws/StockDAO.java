package com.mihaisavin.stox.dao.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class StockDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockDAO.class);

    private String API_KEY = "demo";
    private final String DEMO_SYMBOLS_SECTION = "MSFT,FB,AAPL";

    public Map<String, Double> getStockData(Collection<String> symbols) {
        LOGGER.info("Getting batch stock quote(s) for " + symbols.size() + " symbols...");

        StringBuilder response = new StringBuilder("");
        JsonNode rootNode = null;

        StringBuilder symbolsBuilder = new StringBuilder();
        symbols.forEach(s -> symbolsBuilder.append(s).append(","));

        String symbolsSection = symbolsBuilder.toString();

        if ("demo".equals(API_KEY)) symbolsSection = DEMO_SYMBOLS_SECTION;

        try {
            URL url = new URL("https://www.alphavantage.co/query?function=BATCH_STOCK_QUOTES&symbols=" +
                    symbolsSection +
                    "&apikey=" + API_KEY);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;

            LOGGER.info("Output from Server ....");
            while ((output = br.readLine()) != null) {
                response.append(output);
                LOGGER.info(output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        String json = response.toString();

        ObjectMapper mapper = new ObjectMapper();

        try {
            rootNode = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LOGGER.info(rootNode.toString());

        return extractQuotes(rootNode);
    }

    private Map<String, Double> extractQuotes(JsonNode rootNode) {
        Map<String, Double> result = new HashMap<>();

        JsonNode stockQuotes = rootNode.get("Stock Quotes");
        int size = stockQuotes.size();
        for (int i = 0; i < size; i++)
            result.put(stockQuotes.get(i).get("1. symbol").textValue(),
                       stockQuotes.get(i).get("2. price").asDouble());

        return result;
    }
}
