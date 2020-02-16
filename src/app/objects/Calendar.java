package app.objects;

import java.util.ArrayList;
import java.util.HashSet;

import app.components.Month;

public class Calendar {
    private ArrayList<Month> months;
    private String displayType;
    private boolean isPublic;
    private User owner;
    private HashSet<User> sharedUsers;

    public Calendar(boolean isPublic, User owner) {
        this.isPublic = isPublic;
        this.owner = owner;
        displayType = "YEAR";
        sharedUsers = new HashSet<>();
    }

    public void addSharedUser(User user) {
        sharedUsers.add(user);
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getPublic() {
        if (isPublic) {
            return "Public";
        }
        return "Private";
    }

    public User getOwner() {
        return owner;
    }

}