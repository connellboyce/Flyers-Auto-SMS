package com.connellboyce.flyersautosms.service;

import com.connellboyce.flyersautosms.model.GameList;
import com.connellboyce.flyersautosms.payload.request.SmsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Service
@EnableScheduling
public class SMSTask  {
    Logger logger = LoggerFactory.getLogger(SMSTask.class);

    @Autowired
    private GameList gameList;

    @Autowired
    private PubSubService pubSubService;

    @Scheduled(fixedRate = 900000)
    public void processGames() {
        try {
            logger.info("Processing games...");

            Instant now = Instant.now();

            for (Map.Entry<Instant,String> entry : gameList.getGameList().entrySet()) {
                Instant gameTime = entry.getKey();
                logger.info("Opponent: {}", entry.getValue());
                logger.info("Current Date={}",now.toString());
                logger.info("Game Date={}\n",entry.getKey().toString());

                Duration duration = Duration.between(now, gameTime);
                Long difference = duration.getSeconds();
                Long fifteenMinutes = 900L;
                logger.info("Difference: {}", difference);
                logger.info("difference-fifteenMinutes={}",difference-fifteenMinutes);
                if (difference-fifteenMinutes < 0 && difference-fifteenMinutes > -900) {
                    String message = "Flyers game starting within 15 minutes " + entry.getValue() + ".";
                    SmsRequest smsRequest = new SmsRequest("Auto-Flyers-",message);
                    pubSubService.publish(smsRequest);
                }
            }
        } catch(Exception e) {
            logger.error(e.getMessage());
        }
    }
}
