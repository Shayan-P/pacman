package org.shayan.pacman.menu;

import org.shayan.pacman.database.Settings;

public class Utils {
    public static void exit(){
        Settings.saveToDatabase();
        System.exit(0);
    }
}
