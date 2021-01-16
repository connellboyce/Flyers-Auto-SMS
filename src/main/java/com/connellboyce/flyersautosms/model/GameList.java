package com.connellboyce.flyersautosms.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.*;

@Service
public class GameList {
    Logger logger = LoggerFactory.getLogger(GameList.class);
    private final Map<Instant, String> gameList;

    public GameList() {
        gameList = new HashMap<>();

        File scheduleFile = new File("FlyersSchedule.txt");
        try {
            Scanner fileScanner = new Scanner(scheduleFile);
            while(fileScanner.hasNextLine()) {
                String[] line = fileScanner.nextLine().split(",");

                String hour = (Integer.parseInt(line[2].substring(0,2))+5)+line[2].substring(2);
                String day = line[0];
                if(Integer.parseInt(hour.substring(0,2))>=24 && !":00".equals(line[2].substring(3))) {
                    String newHour = "0" + (Integer.parseInt(hour.substring(0,2))-24);
                    hour = newHour.substring(0,2) + hour.substring(2);
                    day = day.substring(0,3) + (Integer.parseInt(day.substring(3))+1);
                    if(day.length()==4) {
                        day = day.substring(0,3) + "0" + day.substring(3);
                    }
                    if("32".equals(day.substring(3))) {
                        day = day.substring(0,3)+"01";
                        if ("12".equals(day.substring(0,2))) {
                            day = "01"+day.substring(2);
                        } else {
                            day = (Integer.parseInt(day.substring(0,2))+1) + day.substring(2);
                            if(Integer.parseInt(day.substring(0,1))>0 && "-".equals(day.substring(1,2))) {
                                day = "0" + day;
                            }
                        }
                    }
                    if("31".equals(day.substring(3))) {
                        if ("09".equals(day.substring(0,2)) || "04".equals(day.substring(0,2)) || "06".equals(day.substring(0,2)) || "11".equals(day.substring(0,2))) {
                            day = day.substring(0,3)+"01";
                            day = (Integer.parseInt(day.substring(0,2))+1) + day.substring(2);
                            if(Integer.parseInt(day.substring(0,1))>0) {
                                day = "0" + day;
                            }
                        }
                    }
                    if("29".equals(day.substring(3)) && "02".equals(day.substring(0,2)) && "-".equals(day.substring(1,2))) {
                        day = "03-01";
                    }
                }

                Instant date = Instant.parse("2021-"+day+"T"+hour+":00.00Z");

                gameList.put(date,line[1]);
            }
        } catch(FileNotFoundException fnf) {
            logger.error("File parse failed.");
        }
    }

    public Map<Instant, String> getGameList() {
        return gameList;
    }
}
