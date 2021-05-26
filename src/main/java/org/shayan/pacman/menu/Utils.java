package org.shayan.pacman.menu;

import org.shayan.pacman.database.Settings;
import org.shayan.pacman.extra.AlertBox;

public class Utils {
    public static void exitRequest(){
        if(new AlertBox().display("Are you sure you want to exit?")) {
            Settings.saveToDatabase();
            System.exit(0);
        }
    }
}
