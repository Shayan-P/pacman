package org.shayan.pacman.database;

import org.shayan.pacman.menu.GameMenu;

public class History {
    private static GameMenu lastGameMenu;

    public static void setLastGameMenu(GameMenu lastGameMenu){
        History.lastGameMenu = lastGameMenu;
    }
    public static GameMenu getLastGameMenu(){
        if(lastGameMenu == null || lastGameMenu.isFinalFinished())
            return null;
        return lastGameMenu;
    }
}
