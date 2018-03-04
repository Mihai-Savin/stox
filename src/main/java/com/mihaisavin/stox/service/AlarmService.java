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

@RestController
@RequestMapping("/rest/alarms")
public class AlarmService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmService.class);

    @Autowired
    private AlarmDAO dao;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Alarm> getAllAlarms() {
        return dao.getAll();
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

        alarm.setActive();

        if (!errors.isEmpty()) {
            throw new ValidationException(errors.toArray(new String[]{}));
        }
    }
}