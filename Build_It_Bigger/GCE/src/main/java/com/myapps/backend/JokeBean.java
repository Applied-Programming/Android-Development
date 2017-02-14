package com.myapps.backend;

import com.myapps.Joker;

/** The object model for the data we are sending through endpoints */
public class JokeBean {

    private Joker joker;

    public JokeBean(){
        joker = new Joker();
    }

    public String getJoke() {
        return joker.getRandomJoke();
    }
}