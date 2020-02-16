package app.objects;

import java.util.ArrayList;
import java.util.HashMap;

import app.components.CountdownTimer;

public class User {
    private String userId;
    private HashMap<Integer, Event> events;
    private CountdownTimer timer;

    public User(String userId) {
        this.userId = userId;
        events = new HashMap<>();
        timer = new CountdownTimer();
    }

    public Calendar createCalendar(boolean isPublic) {
        Calendar calendar = new Calendar(isPublic, this);
        return calendar;
    }

    public Event createEvent(ArrayList<String> userRes, Calendar originCalendar) {
        Event event;
        if (userRes.get(4) == "y") {
            event = new Event(this, userRes.get(0), userRes.get(1), userRes.get(2), userRes.get(3), originCalendar,
                    true);
        } else {
            event = new Event(this, userRes.get(0), userRes.get(1), userRes.get(2), userRes.get(3), originCalendar,
                    false);
        }
        addEvent(event);
        return event;
    }

    public void addEvent(Event event) {
        events.put(events.size() + 1, event);
    }

    public void editEvent(int eventNum, ArrayList<String> userRes) {
        if (events.containsKey(eventNum)) {
            Event event = events.get(eventNum);
            if (this.equals(event.getCreator())) {
                event.setTitle(userRes.get(0));
                event.setStart(userRes.get(1));
                event.setEnd(userRes.get(2));
                event.setDetails(userRes.get(3));
            }
        }
    }

    public void removeEvent(int eventID) {
        if (events.containsKey(eventID)) {
            deleteEvent(events.get(eventID));
            events.remove(eventID);
        }
    }

    public void deleteEvent(Event event) {
        event.removeUser(this);
    }

    public String displayEvent(int eventID) {
        String result = "----------Event Detail----------\n";
        if (events.containsKey(eventID)) {
            Event event = events.get(eventID);
            result += "Title: " + event.getTitle() + "\n";
            result += "Start Time: " + event.getStart() + "\n";
            result += "End Time: " + event.getEnd() + "\n";
            result += "Description: " + event.getDetails() + "\n";
            result += "Shared to: \n" + event.getSharedUser();
            result += event.getRepeat() + '\n';
        }
        return result;
    }

    public void shareEvent(int eventID, User sharedUser) {
        if (events.containsKey(eventID)) {
            sharedUser.addEvent(events.get(eventID));
            events.get(eventID).addUser(sharedUser);
        }
    }

    public HashMap<Integer, Event> getEvents() {
        return events;
    }

    public String getUserId() {
        return userId;
    }

    public CountdownTimer getCountdownTimer() {
        return timer;
    }

    public String displayTimer() {
        return this.timer.displayTimeRemaining();
    }

    public void addTimer(int eventID) {
        if (events.containsKey(eventID)) {
            timer.addTimer(events.get(eventID));
        }
    }
}