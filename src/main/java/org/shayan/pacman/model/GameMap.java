package org.shayan.pacman.model;

import javafx.util.Pair;
import org.shayan.pacman.game.MapEntity;

import java.io.FileReader;
import java.util.*;

public class GameMap {
    private int width;
    private int height;
    private MapEntity[][] cells;
    private static Random rnd = new Random();

    public Integer[] read(Scanner scanner) {
        List<Integer> ret = new ArrayList<>();
        Arrays.stream(scanner.nextLine().split(",")).forEach(e->ret.add(Integer.parseInt(e)));
        return ret.toArray(Integer[]::new);
    }

    public GameMap(FileReader reader) {
        Scanner scanner = new Scanner(reader);
        Integer[] tmp = read(scanner);
        width = tmp[0];
        height = tmp[1];
        cells = new MapEntity[width][height];
        for (int i = 0; i < width; i++) {
            tmp = read(scanner);
            for (int j = 0; j < height; j++) {
                cells[i][j] = MapEntity.getTypeById(tmp[j]);
            }
        }
    }

    // this is random generator constructor!
    public GameMap(int m){
        int n = m + rnd.nextInt(4);

        width = n;
        height = m;
        cells = new MapEntity[width][height];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                int r = rnd.nextInt(3);
                if(r == 0)
                    cells[i][j] = MapEntity.EMPTY;
                if(r == 1)
                    cells[i][j] = MapEntity.COIN;
                if(r == 2)
                    cells[i][j] = MapEntity.WALL;
            }
        }
        cells[0][0] = MapEntity.WALL;
        cells[n-1][0] = MapEntity.WALL;
        cells[0][m-1] = MapEntity.WALL;
        cells[n-1][m-1] = MapEntity.WALL;

        for(int i = 0; i < n; i++){
            if(cells[i][0].equals(MapEntity.WALL) || cells[i][m-1].equals(MapEntity.WALL)){
                cells[i][0] = MapEntity.WALL;
                cells[i][m-1] = MapEntity.WALL;
            }
        }
        for(int i = 0; i < m; i++){
            if(cells[0][i].equals(MapEntity.WALL) || cells[n-1][i].equals(MapEntity.WALL)){
                cells[0][i] = MapEntity.WALL;
                cells[n-1][i] = MapEntity.WALL;
            }
        }

        List<Pair<Integer, Integer>> list = new ArrayList<>();
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                if(cells[i][j].equals(MapEntity.EMPTY))
                    list.add(new Pair<>(i, j));
            }
        }

        Pair<Integer, Integer> pacman = list.get(rnd.nextInt(list.size()));
        cells[pacman.getKey()][pacman.getValue()] = MapEntity.PACMAN;
        list.remove(pacman);

        for(int i = 0; i < 4; i++) {
            Pair<Integer, Integer> ai = list.get(rnd.nextInt(list.size()));
            cells[ai.getKey()][ai.getValue()] = MapEntity.AI;
            list.remove(ai);
        }
        for(int i = 0; i < 4; i++){
            Pair<Integer, Integer> spc = list.get(rnd.nextInt(list.size()));
            cells[spc.getKey()][spc.getValue()] = MapEntity.SPECIAL_COIN;
            list.remove(spc);
        }
    }

    // this is another random generator
    public GameMap(){
        this(rnd.nextInt(16) + 20);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public MapEntity[][] getCells() {
        return cells;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(width).append(",").append(height).append("\n");
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                if(j != height-1)
                    sb.append(cells[i][j].id).append(",");
                else
                    sb.append(cells[i][j].id).append("\n");
            }
        }
        return sb.toString();
    }
}
