package app.objects;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

public class Event {
    private User creator;
    private String title;
    private Date start;
    private Date end;
    private Calendar originCalendar;
    private boolean repeatable;

    private String description;
    private HashSet<String> filters;
    private HashSet<User> sharedUsers;

    private SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");

    public Event(User creator, String title, String start, String end, String description, Calendar originCalendar,
            boolean repeatable) {
        this.creator = creator;
        this.title = title;

        // Date Conversion
        this.start = convertStringToDate(start);
        this.end = convertStringToDate(end);

        this.originCalendar = originCalendar;
        this.description = description;
        this.repeatable = repeatable;

        filters = new HashSet<>();
        addFilter();

        sharedUsers = new HashSet<User>();
    }

    public void setTitle(String title) {
        if (title.length() != 0) {
            this.title = title;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setStart(String date) {
        if (date.length() != 0) {
            this.start = convertStringToDate(date);
        }
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public String getDetails() {
        return description;
    }

    public String getRepeat() {
        if (repeatable) {
            return "Repeat every week";
        }
        return "Does not repeat";
    }

    public String getSharedUser() {
        String result = "";
        Iterator<User> users = sharedUsers.iterator();
        while (users.hasNext()) {
            result += "   " + users.next().getUserId() + '\n';
        }
        return result;
    }

    private Date convertStringToDate(String date) {
        Date convertedDate = new Date();
        try {
            convertedDate = formatter.parse(date);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please update event");
        }
        return convertedDate;
    }

    public void setEnd(String date) {
        if (date.length() != 0) {
            this.end = convertStringToDate(date);
        }
    }

    public void setDetails(String description) {
        if (description.length() != 0)
            this.description = description;
    }

    public void addUser(User user) {
        sharedUsers.add(user);
    }

    public void addFilter() {
        Collections.addAll(filters, title.split(" "));
        Collections.addAll(filters, description.split(" "));
    }

    public void removeFilter(String filter) {
        filters.remove(filter);
    }

    public HashSet<String> getFilter() {
        return filters;
    }

    public User getCreator() {
        return creator;
    }

    public void removeUser(User user) {
        sharedUsers.remove(user);
    }
}