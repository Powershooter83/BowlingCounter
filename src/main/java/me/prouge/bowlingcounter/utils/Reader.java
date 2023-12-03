package me.prouge.bowlingcounter.utils;

import java.util.Scanner;

public class Reader {

    final Scanner scanner = new Scanner(System.in);

    public int getPins() {
        return scanner.nextInt();
    }



}
