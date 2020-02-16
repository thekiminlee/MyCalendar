package app.components;

import java.util.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.time.temporal.ChronoUnit;

import app.objects.Event;

public class CountdownTimer {
    private ArrayList<Event> upcomingEvents;

    public CountdownTimer() {
        upcomingEvents = new ArrayList<>();
    }

    public void addTimer(Event event) {
        upcomingEvents.add(event);
    }

    public String displayTimeRemaining() {
        String result = "-------------Remaining Timers-------------\n";
        if (!upcomingEvents.isEmpty()) {
            for (int i = 0; i < upcomingEvents.size(); i++) {
                result += "Timer " + Integer.toString(i + 1) + ": " + calculateDiff(upcomingEvents.get(i).getEnd())
                        + " days until " + upcomingEvents.get(i).getTitle() + '\n';
            }
        }
        return result;
    }

    private String calculateDiff(Date end) {
        long diff = ChronoUnit.DAYS.between(LocalDate.now(), configureZone(end));

        return Long.toString(diff);
    }

    private LocalDate configureZone(Date end) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Instant instant = end.toInstant();
        LocalDate endDate = instant.atZone(defaultZoneId).toLocalDate();
        return endDate;
    }

}