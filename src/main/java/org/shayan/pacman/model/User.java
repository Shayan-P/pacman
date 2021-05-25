package org.shayan.pacman.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Comparable<User>, Serializable {
    private String username, password;
    private int score = 0;
    private int lastTimeChange = -1;

    private static final List<User> users = new ArrayList<>();
    private static User currentUser;

    public User(String username, String password) throws PacmanException {
        this.username = username;
        this.password = password;
        if(username.isEmpty() || password.isEmpty())
            throw new PacmanException("username or password is empty!");
        if(getUserByUsername(username) != null)
            throw new PacmanException("this username is repeated!");
        users.add(this);
    }
    public static User getUserByUsername(String username){
        for(User user: users){
            if(user.username.equals(username))
                return user;
        }
        return null;
    }

    public static void loginCurrentUser(String username, String password) throws PacmanException {
        User user = getUserByUsername(username);
        if(user == null)
            throw new PacmanException("no such user exists");
        if(!user.password.equals(password))
            throw new PacmanException("password is wrong");
        currentUser = user;
    }
    public static void logoutCurrentUser() {
        currentUser = null;
    }


    public static User getCurrentUser() {
        return currentUser;
    }

    public int getScore() {
        return score;
    }

    public void updateScore(int score) {
        if(score > this.score){
            int timer = -1;
            for(User user: users)
                timer = Math.max(timer, user.lastTimeChange);
            this.lastTimeChange = timer + 1;
            this.score = score;
        }
    }

    @Override
    public int compareTo(User user) {
        if(this.score != user.score)
            return this.score - user.score;
        if(this.lastTimeChange != user.lastTimeChange)
            return this.lastTimeChange - user.lastTimeChange;
        return this.username.compareTo(user.username);
    }

    public void removeUser(){
        users.remove(this);
    }

    public void save(){
        users.add(this);
    }

    public static List<User> getUsers(){
        return users;
    }

    public String getUsername() {
        return username;
    }
}
