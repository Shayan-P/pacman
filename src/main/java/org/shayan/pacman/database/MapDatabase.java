package org.shayan.pacman.database;

import org.shayan.pacman.model.GameMap;
import org.shayan.pacman.model.PacmanException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MapDatabase {
    public static String[] getMapPaths(){
        List<String> paths = new ArrayList<>();
        for(File file : new File("maps").listFiles()) {
            paths.add(file.getAbsolutePath());
        }
        return paths.toArray(String[]::new);
    }
    public static String getFreePath(){
        int counter = 1;
        while(new File("maps/map" + counter + ".txt").exists())
            counter += 1;
        return "maps/map" + counter + ".txt";
    }
    public static void removeMap(String mapName) throws PacmanException {
        File file = new File("maps/" + mapName);
        System.out.println(file);
        if(!file.exists())
            throw new PacmanException("no such file!");
        if(!file.delete())
            throw new PacmanException("can't delete file");
    }
    public static void saveTempMap() throws PacmanException {
        try {
            File file = new File("maps/tmp.txt");
            file.renameTo(new File(getFreePath()));
        } catch (Exception exception){
            throw new PacmanException("there is no recently random generated map!");
        }
    }
    public static void saveToTempMap(GameMap map){
        try {
            FileWriter writer = new FileWriter("maps/tmp.txt");
            writer.write(map.toString());
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
