package me.prouge.bowlingcounter;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector();
        Game game = injector.getInstance(Game.class);
        game.start();


    }


}
