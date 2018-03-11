package com.mihaisavin.stox.service;

import com.mihaisavin.stox.config.AppConfig;
import com.mihaisavin.stox.domain.Alarm;
import com.mihaisavin.stox.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Collection;
import java.util.Map;

@RestController
public class StoxEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private static final int ONE_MINUTE = 60;
    private static final String DEFAULT_CONFIG_NAME = "default";

    // Built in configurations
    private static int pollingInterval = ONE_MINUTE; //in seconds
    private static boolean started = false;

    @Autowired
    private StockService stockService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private AlarmService alarmService;
    @Autowired
    private UserService userService;


    Thread workerThread;

    /**
     * It appears that this starts automatically.
     * Needs more documenting. (name = "/start")
     */
    @RequestMapping(path="/start")
    public void start() {
        AppConfig config = getConfigbyName(DEFAULT_CONFIG_NAME);

        if (config.isStartEngine()) {
            emailService.setEnabled(config.isSendEmails());
            emailService.setEmailsSenderAccount(config.getEmailsSenderAccount());
            emailService.setEmailsSenderPassword(config.getEmailsSenderPassword());

            pollingInterval = config.getPollingInterval();
            ignite(config);
            started = true;
        }
    }

    @RequestMapping(path="/stop")
    public void stop() {
        started = false;
    }

    private void ignite(AppConfig config) {
        LOGGER.info("Starting StoxEngine thread...");

        Runnable runner = () -> {
            LOGGER.info("Running...");

            while (started) {

                doWork();

                try {
                    Thread.sleep(pollingInterval * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            workerThread.stop();
            LOGGER.info("StoxEngine stopped.");

        };

        workerThread = new Thread(runner);
        workerThread.setName("StoxEngine");

        LOGGER.info("Starting StoxEngine with following config: " + config.toString());
        workerThread.start();

        LOGGER.info("StoxEngine started.");
    }

    private void doWork() {
        LOGGER.info("Working [fetching quotes]...");
        Map<String, Long> stocksMap = stockService.getStocks();

        Collection<Alarm> activeAlarms = alarmService.getActiveAlarms();

        LOGGER.info("Working [evaluating]...");
        for (Alarm alarm : activeAlarms) {
            Long currentValue = stocksMap.get(alarm.getSymbol());
            int variance = alarm.getVariance();
            double threshold = alarmService.getAlarmThreshold(alarm);

            if ((variance > 0 && currentValue >= threshold) ||
                    (variance < 0 && currentValue <= threshold)) {
                String alarmOwnersEmail = userService.getEmailByUserId(alarm.getOwnerId());

                emailService.sendEmail(alarmOwnersEmail, "Symbol " + alarm.getSymbol() + " has reached" +
                        " targeted value of " + threshold + ".");
                alarmService.disableAlarm(alarm.getId());
            }
        }
        LOGGER.info("Working [evaluating]...[done].");
    }

    private AppConfig getConfigbyName(String configName) {

        SessionFactory factory = HibernateUtil.getSessionFactory();

        //creating session object
        Session session=factory.getCurrentSession();

        //creating transaction object
        Transaction t=session.beginTransaction();

        Query query = session.createQuery("FROM AppConfig AC WHERE AC.name = :name");
        query.setParameter("name", configName);

        AppConfig config = (AppConfig) query.uniqueResult();

        t.commit();//transaction is committed
        session.close();

        LOGGER.info("Successfully fetched [" + configName + "] app config from DB.");

        return config;
    }

}