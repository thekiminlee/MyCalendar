package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

import app.objects.Event;
import app.objects.MyCalendar;

public class App {
    // Menu options for UI
    private final String MENU_OPTIONS = "   LGT : Logout\n" + "   D   : Display account information\n"
            + "   U   : User options\n" + "   C   : Calendar options\n" + "   E   : Event options\n"
            + "   S   : Setting\n" + "   Q   : Quit";

    // Initialize calendar instance and Scanner for user input
    private MyCalendar myCalendar;
    private Scanner input;
    private String userName;

    public App() {
        myCalendar = new MyCalendar();
        input = new Scanner(System.in);
    }

    public void loginUI() {
        // Prompts the user for login credential.
        // Upon failure, user can try again or create new user account.
        if (!myCalendar.isLoggedIn()) {
            System.out.print("Please enter username: ");
            userName = input.next();
            while (!myCalendar.login(userName)) { // if the login fails
                System.out.print(
                        "User \"" + userName + "\" does not exist. Would you like to create new account? (Y/N): ");
                String logInAttempt = input.next();
                switch (logInAttempt.toLowerCase()) {
                case "y":
                    System.out.print("Please enter your new username: ");
                    userName = input.next();
                    myCalendar.addUser(userName);
                    System.out.println("    User account created for \"" + userName + "\"");
                    break;
                case "n":
                    System.out.print("Please enter username: ");
                    userName = input.next();
                    break;
                }
            }
            System.out.println("Welcome " + userName); // login successful
        }
    }

    public void run() {
        // Basic UI with CLI
        boolean running = true;
        while (running) {
            // User response to the menu
            System.out.println(MENU_OPTIONS);
            System.out.print(userName + ": ");
            String prompt = input.next();
            // System response to user response
            switch (prompt.toLowerCase()) {
            case "lgt":
                logout();
                break;
            case "d":
                displayAccountInfo();
                break;
            case "u":
                userOptions();
                break;
            case "c":
                calendarOptions();
                break;
            case "e":
                eventOptions();
                break;
            case "s":
                setting();
                break;
            case "q":
                running = false;
                break;
            }
        }
    }

    public void exit() {
        input.close();
    }

    private void logout() {
        if (myCalendar.logout()) {
            System.out.println("Logout successful");
            loginUI();
        } else {
            System.out.println("Logout failed");
        }
    }

    private void displayAccountInfo() {
        String currentUser = myCalendar.getCurrentUser().getUserId();
        System.out.println("Current user is: " + currentUser);
        System.out.println("Current time zone is: " + myCalendar.getTimeZone().getDisplayName());
    }

    private void userOptions() {
        String user_name;
        final String options = "   A   : Add user\n" + "   R   : Remove user";
        System.out.println(options);
        System.out.print(userName + ": ");
        String prompt = input.next();
        switch (prompt.toLowerCase()) {
        case "a":
            System.out.print("Please enter new user name: ");
            user_name = input.next();
            if (myCalendar.addUser(user_name)) {
                System.out.println("User successfully added");
            } else {
                System.out.println("Cannot add user");
            }
            break;
        case "r":
            System.out.print("Please enter user name for removal: ");
            user_name = input.next();
            if (myCalendar.removeUser(user_name)) {
                System.out.println("User successfully removed");
            } else {
                System.out.println("Cannot remove user");
            }
            break;
        }
    }

    private void calendarOptions() {
        final String options = "   PBC  : Create public calendar\n" + "   PRC  : Create private calendar\n"
                + "   RC   : Remove calendar\n" + "   SC   : Share calendar\n" + "   UC   : Update calendar\n"
                + "   VC   : View calendars";
        System.out.println(options);
        System.out.print(userName + ": ");
        String prompt = input.next();
        switch (prompt.toLowerCase()) {
        case "pbc":
            myCalendar.addCalendar(myCalendar.getCurrentUser().createCalendar(true));
            System.out.println("Public calendar successfully added");
            break;
        case "prc":
            myCalendar.addCalendar(myCalendar.getCurrentUser().createCalendar(false));
            System.out.println("Private calendar successfully added");
            break;
        case "rc":
            System.out.print("Calendar # to remove: ");
            int calendarNum = input.nextInt();
            myCalendar.removeCalendar(calendarNum, myCalendar.getCurrentUser());
            System.out.println("Calendar " + Integer.toString(calendarNum) + " successfully removed");
            break;
        case "sc":
            System.out.print("Calendar # to share: ");
            int calendarNum_share = input.nextInt();
            System.out.println("Shared user name: ");
            String sharedUser = input.next();
            if (myCalendar.shareCalendar(calendarNum_share, sharedUser)) {
                System.out.println("Calendar " + calendarNum_share + " successfully shared to " + sharedUser);
            } else {
                System.out.println("Calendar share failed");
            }
        case "uc":
            System.out.print("Calendar # to update: ");
            int calendarNum_update = input.nextInt();
            System.out.print("Private or Public (PR / PB): ");
            String privacy = input.next();
            if (privacy.toLowerCase().equals("pr")) {
                myCalendar.updateCalendar(calendarNum_update, false);
            } else if (privacy.toLowerCase().equals("pb")) {
                myCalendar.updateCalendar(calendarNum_update, true);
            } else {
                System.out.println("Invalid input");
            }
            break;
        case "vc":
            System.out.println(myCalendar.displayCalendar());
            break;
        }

    }

    private void eventOptions() {
        final String options = "   A   : Add event\n" + "   R   : Remove event\n" + "   U   : Update event\n"
                + "   S   : Search event\n" + "   C   : Add Countdown\n" + "  SE   : Share event\n"
                + "   V   : View events\n" + "  VE   : View specific event";
        int calendarNum;
        System.out.println(options);
        System.out.print(userName + ": ");
        String prompt = input.next();

        switch (prompt.toLowerCase()) {
        case "a":
            System.out.print("Calendar #: ");
            calendarNum = Integer.parseInt(input.next());
            ArrayList<String> userRes = eventPrompt();
            if (myCalendar.authenticateUser(calendarNum)) {
                myCalendar.getCurrentUser().createEvent(userRes, myCalendar.getCalendars().get(calendarNum));
            }
            break;
        case "r":
            System.out.print("Calendar #: ");
            calendarNum = Integer.parseInt(input.next());
            System.out.print("Event #: ");
            int rEventNum = Integer.parseInt(input.next());
            if (myCalendar.authenticateUser(calendarNum)) {
                myCalendar.getCurrentUser().removeEvent(rEventNum);
            }
            break;
        case "u":
            System.out.print("Calendar #: ");
            calendarNum = Integer.parseInt(input.next());
            System.out.print("Event #:");
            int eventNum = input.nextInt();
            ArrayList<String> userUpdate = eventPrompt();
            if (myCalendar.authenticateUser(calendarNum)) {
                myCalendar.getCurrentUser().editEvent(eventNum, userUpdate);
            }
            break;
        case "s":
            System.out.print("Search query (single word): ");
            prompt = input.next();
            ArrayList<Event> searchResult = search(prompt);
            System.out.println(myCalendar.displayEvents(searchResult));
            break;
        case "c":
            System.out.println("      V   : View timers\n" + "      A   : Add timer\n");
            System.out.print(myCalendar.getCurrentUser().getUserId() + ": ");
            String timerRes = input.next();
            switch (timerRes.toLowerCase()) {
            case "v":
                System.out.println(myCalendar.getCurrentUser().getCountdownTimer().displayTimeRemaining());
                break;
            case "a":
                System.out.print("      Event #: ");
                int eventNum_cd = Integer.parseInt(input.next());
                myCalendar.getCurrentUser().addTimer(eventNum_cd);
                break;
            }
            break;
        case "se":
            System.out.print("Event #: ");
            int eventNum_share = Integer.parseInt(input.next());
            System.out.print("User to share: ");
            String eShareUser = input.next();
            if (myCalendar.getUsers().containsKey(eShareUser)) {
                myCalendar.getCurrentUser().shareEvent(eventNum_share, myCalendar.getUsers().get(eShareUser));
                System.out.println("Event shared to " + eShareUser);
            } else {
                System.out.println("Event share failed");
            }
            break;
        case "ve":
            System.out.print("Event #: ");
            int eventNum_detail = Integer.parseInt(input.next());
            System.out.println(myCalendar.getCurrentUser().displayEvent(eventNum_detail));
            break;
        case "v":
            System.out.println(myCalendar.displayEvents());
            break;
        }
    }

    private ArrayList<Event> search(String prompt) {
        ArrayList<Event> result = new ArrayList<Event>();
        for (Map.Entry<Integer, Event> entry : myCalendar.getCurrentUser().getEvents().entrySet()) {
            if (entry.getValue().getFilter().contains(prompt)) {
                result.add(entry.getValue());
            }
        }
        return result;
    }

    private ArrayList<String> eventPrompt() {
        System.out.print("  Title: ");
        input.nextLine();
        String title = input.nextLine();
        System.out.print("  Start time (MM/DD/YYYY HH:MM): ");
        String start = input.nextLine();
        System.out.print("  End time (MM/DD/YYYY HH:MM): ");
        String end = input.nextLine();
        System.out.print("  Description: ");
        String description = input.nextLine();
        System.out.print("  Repeatable each week (Y/N): ");
        String repeatable = input.next();
        ArrayList<String> result = new ArrayList<String>(
                Arrays.asList(title, start, end, description, repeatable.toLowerCase()));
        return result;
    }

    private void setting() {
        final String options = "   TZ   : Switch timezone\n" + "   TH   : Switch theme (light/dark)";
        System.out.println(options);
        System.out.print(userName + ": ");
        String prompt = input.next();
        switch (prompt.toLowerCase()) {
        case "tz":
            System.out.print("Timezone (e.g. PST): ");
            String timeZone = input.next().toUpperCase();
            myCalendar.setTimeZone(timeZone);
        case "th":
            myCalendar.toggleDarkTheme();
        }
    }

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.loginUI();
        app.run();
        app.exit();
    }
}