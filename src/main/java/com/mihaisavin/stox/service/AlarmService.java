package com.mihaisavin.stox.service;

import com.mihaisavin.stox.dao.AlarmDAO;
import com.mihaisavin.stox.domain.Alarm;
import com.mihaisavin.stox.domain.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/alarms")
public class AlarmService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmService.class);

    @Autowired
    private AlarmDAO dao;

    @Autowired
    StockService stockService;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Alarm> getAllAlarms(long userId) {
        Collection<Alarm> alarms = dao.getAll(userId);
        if (alarms.size() > 0) updateWithNowValues(alarms);

        return alarms;
    }

    private void updateWithNowValues(Collection<Alarm> alarms) {
        Collection<String> watchedSymbols = getWatchedSymbols();
        Map<String, Long> stockData = stockService.getStockData(watchedSymbols);

        for (Alarm alarm : alarms) {
            alarm.setNowValue(stockData.get(alarm.getSymbol()));
        }
    }

    @RequestMapping(method = RequestMethod.GET, params = "query")
    Collection<Alarm> search(@RequestParam(value = "query") String query) {
        LOGGER.debug("Searching for " + query);
        return dao.searchByName(query);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public boolean delete(@PathVariable("id") Long id) {
        LOGGER.debug("Deleting alarm for id: " + id);
        Alarm alarm = dao.findById(id);
        if (alarm != null) {
            dao.delete(alarm);
            return true;
        }

        return false;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Alarm get(@PathVariable("id") Long id) {
        LOGGER.debug("Getting alarm for id: " + id);
        return dao.findById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void save(Alarm alarm) throws ValidationException {
        LOGGER.debug("Saving: " + alarm);
        validate(alarm);

        dao.update(alarm);
    }

    public Collection<String> getWatchedSymbols() {
        return dao.getWatchedSymbols();
    }

    private void validate(Alarm alarm) throws ValidationException {
        List<String> errors = new LinkedList<String>();

        //TODO validation code

        //didn't wanna loose too much time with thymeleaf's stupid checkbox
        if (alarm.getState().equals("enabled")) {
            alarm.setActive();
        } else {
            alarm.setInactive();
        }

        if (alarm.getId() == 0 && alarm.getOriginalValue() == 0) {
            Map<String, Long> stockData = stockService.getStockData(alarm.getSymbol());
            alarm.setOriginalValue(stockData.get(alarm.getSymbol()));
            alarm.setActive();
        } else if (alarm.getOriginalValue() == 0) {
            double unchanged = get(alarm.getId()).getOriginalValue();
            alarm.setOriginalValue(unchanged);
        }


        if (!errors.isEmpty()) {
            throw new ValidationException(errors.toArray(new String[]{}));
        }
    }

    public Collection<Alarm> getActiveAlarms() {
        return dao.getActiveAlarms();
    }

    public double getAlarmThreshold(Alarm alarm) {
        return alarm.getOriginalValue() * (100 + alarm.getVariance()) / 100;
    }

    public void disableAlarm(long id) {
        Alarm alarm = get(id);
        alarm.setInactive();

        try {
            save(alarm);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

}