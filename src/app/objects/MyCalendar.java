package app.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class MyCalendar {
    private HashMap<String, User> userList;
    private HashMap<Integer, Calendar> calendarList;
    private TimeZone timeZone;
    private boolean darkTheme;

    private boolean loggedIn;
    private User currentUser;

    public MyCalendar() {
        userList = new HashMap<>();
        calendarList = new HashMap<>();
        loggedIn = false;
        darkTheme = false;
        timeZone = TimeZone.getDefault();
    }

    public String displayCalendar() {
        String calendars = "----------Calendar List----------\n";
        if (calendarList.isEmpty()) {
            return "No Calendars available";
        }
        for (Map.Entry<Integer, Calendar> entry : calendarList.entrySet()) {
            if (entry.getValue().getIsPublic()) {
                calendars += entry.getValue().getPublic() + " Calendar " + entry.getKey() + " created by "
                        + entry.getValue().getOwner().getUserId() + '\n';
            } else if (!(entry.getValue().getIsPublic()) && entry.getValue().getOwner().equals(currentUser)) {
                calendars += entry.getValue().getPublic() + " Calendar " + entry.getKey() + " created by "
                        + entry.getValue().getOwner().getUserId() + '\n';
            }
        }
        return calendars;

    }

    public String displayEvents(ArrayList<Event> searchResult) {
        String result = "------------------------------------------\n";
        result += "Event search result:\n";
        for (int i = 0; i < searchResult.size(); i++) {
            result += "  " + searchResult.get(i).getTitle() + " created by "
                    + searchResult.get(i).getCreator().getUserId() + "\n";
        }
        result += "------------------------------------------\n";
        return result;
    }

    public String displayEvents() {
        String result = "------------------------------------------\n";
        result += "Event list:\n";
        HashMap<Integer, Event> events = currentUser.getEvents();
        for (Map.Entry<Integer, Event> entry : events.entrySet()) {
            result += "  " + entry.getKey() + " " + entry.getValue().getTitle() + " created by "
                    + entry.getValue().getCreator().getUserId() + "\n";
        }
        result += "------------------------------------------\n";
        return result;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public boolean login(String userName) {
        if (userList.isEmpty()) {
            return false;
        } else if (userList.containsKey(userName)) {
            currentUser = userList.get(userName);
            this.loggedIn = !this.loggedIn;
            return true;
        } else {
            return false;
        }
    }

    public boolean logout() {
        if (this.loggedIn) {
            currentUser = null;
            this.loggedIn = !this.loggedIn;
            return true;
        }
        return false;
    }

    // User modification methods
    public boolean addUser(String userId) {
        if (!userList.containsKey(userId)) {
            User user = new User(userId);
            userList.put(userId, user);
            return true;
        }
        return false;
    }

    public boolean removeUser(String userId) {
        if (currentUser.getUserId().equals(userId)) {
            return false;
        } else if (userList.containsKey(userId)) {
            userList.remove(userId);
            return true;
        }
        return false;
    }

    // Calendar modification methods
    public void addCalendar(Calendar calendar) {
        calendarList.put(calendarList.size() + 1, calendar);
    }

    public boolean removeCalendar(int calendar_id, User user) {
        if (authenticateUser(calendar_id)) {
            calendarList.remove(calendar_id);
            return true;
        }
        return false;
    }

    public boolean updateCalendar(int calendar_id, boolean isPublic) {
        if (authenticateUser(calendar_id)) {
            calendarList.get(calendar_id).setPublic(isPublic);
            return true;
        }
        return false;
    }

    public boolean shareCalendar(int calendar_id, String sharedUser) {
        if (userList.containsKey(sharedUser)) {
            if (authenticateUser(calendar_id)) {
                calendarList.get(calendar_id).addSharedUser(userList.get(sharedUser));
                return true;
            }
        }
        return false;
    }

    // getters, setters, app configurations
    public TimeZone getTimeZone() {
        return timeZone;
    }

    public HashMap<String, User> getUsers() {
        return userList;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = TimeZone.getTimeZone(timeZone);
    }

    public HashMap<Integer, Calendar> getCalendars() {
        return calendarList;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void toggleDarkTheme() {
        this.darkTheme = !this.darkTheme;
    }

    public boolean authenticateUser(int calendar_id) {
        if (calendarList.containsKey(calendar_id)) {
            if (calendarList.get(calendar_id).getOwner().equals(currentUser)) {
                return true;
            }
        }
        return false;
    }

}