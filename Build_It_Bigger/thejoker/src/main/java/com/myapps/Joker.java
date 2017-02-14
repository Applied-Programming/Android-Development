package com.myapps;

import java.util.Random;

public class Joker {

    private String[] jokes;
    private Random random;

    public Joker() {
        jokes = new String[5];
        jokes[0] = "Isn't it great to live in the 21st century? Where deleting history has become more important than making it.";
        jokes[1] = "Nothing ruins a Friday more than an understanding that today is Tuesday.";
        jokes[2] = "Time waits for no man, time is obviously a woman.";
        jokes[3] = "Apparently I snore so loudly that it scares everyone in the car I'm driving.";
        jokes[4] = "Wifi went down during family dinner tonight. One kid started talking and I didn't know who he was.";
        random = new Random();
    }

    public String[] getJokes() {
        return jokes;
    }

    public String getRandomJoke() {
        return jokes[random.nextInt(jokes.length)];
    }

}
