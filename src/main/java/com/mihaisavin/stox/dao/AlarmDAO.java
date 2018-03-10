package com.mihaisavin.stox.dao;

import com.mihaisavin.stox.domain.Alarm;
import java.util.Collection;

public interface AlarmDAO extends BaseDAO<Alarm>  {
    Collection<Alarm> searchByName(String query);
    Collection<String> getWatchedSymbols();
    Collection<Alarm> getActiveAlarms();
    Alarm getAlarmByName (String alarmName);
}