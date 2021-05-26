package org.shayan.pacman.game;

public enum MapEntity {
    EMPTY(0), WALL(1), AI(2), PACMAN(3), COIN(4), SPECIAL_COIN(5);
    public int id;
    MapEntity(int id){
        this.id = id;
    }
    public static MapEntity getTypeById(int id){
        if(id == 0)
            return EMPTY;
        if(id == 1)
            return WALL;
        if(id == 2)
            return AI;
        if(id == 3)
            return PACMAN;
        if(id == 4)
            return COIN;
        if(id == 5)
            return SPECIAL_COIN;
        return null;
    }
}
