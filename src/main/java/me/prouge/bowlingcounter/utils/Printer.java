package me.prouge.bowlingcounter.utils;

import me.prouge.bowlingcounter.Frame;

import java.util.List;

public class Printer {

    public void printTitle() {
        System.out.println(".______     ______   ____    __    ____  __       __  .__   __.   _______      ______   ______    __    __  .__   __. .___________. _______ .______      \n" +
                "|   _  \\   /  __  \\  \\   \\  /  \\  /   / |  |     |  | |  \\ |  |  /  _____|    /      | /  __  \\  |  |  |  | |  \\ |  | |           ||   ____||   _  \\     \n" +
                "|  |_)  | |  |  |  |  \\   \\/    \\/   /  |  |     |  | |   \\|  | |  |  __     |  ,----'|  |  |  | |  |  |  | |   \\|  | `---|  |----`|  |__   |  |_)  |    \n" +
                "|   _  <  |  |  |  |   \\            /   |  |     |  | |  . `  | |  | |_ |    |  |     |  |  |  | |  |  |  | |  . `  |     |  |     |   __|  |      /     \n" +
                "|  |_)  | |  `--'  |    \\    /\\    /    |  `----.|  | |  |\\   | |  |__| |    |  `----.|  `--'  | |  `--'  | |  |\\   |     |  |     |  |____ |  |\\  \\----.\n" +
                "|______/   \\______/      \\__/  \\__/     |_______||__| |__| \\__|  \\______|     \\______| \\______/   \\______/  |__| \\__|     |__|     |_______|| _| `._____|\n" +
                "                                                                                                                                                         ");
    }

    private void clear() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    public void printBoard(final List<Frame> frames, final List<Integer> scores, int totalScore) {
        clear();
        printTitle();
        String frameRow = "| 1   | 2   | 3   | 4   | 5   | 6   | 7   | 8   | 9   | 10    | Total  |";


        StringBuilder frameBuilder = new StringBuilder();
        StringBuilder total = new StringBuilder();


        for (Frame frame : frames) {
            frameBuilder.append("| ")
                    .append(frame.getDisplay());
        }


        for (int i = 0; i < 10; i++) {
            int sc = scores.get(i);
            total.append("| ");
            if (sc == -1) {
                total.append("    ");
                continue;
            }

            if (i == 9 && frames.get(i).hasPlayedThrice()) {
                total.append("  ");
            }
            if (sc < 10) {
                total.append("  ");
            } else if (sc < 100) {
                total.append(" ");
            }
            total.append(sc).append(" ");
        }


        if (!frames.get(9).hasPlayedOnce()) {
            frameBuilder.append("  |");
        } else {
            frameBuilder.append(" |");
        }

        if (frames.get(9).hasPlayedThrice()) {
            total.append("|   ");
        } else {
            total.append("  |   ");
        }

        frameBuilder.append("        |");

        total.append(totalScore);
        if (totalScore < 10) {
            total.append("    |");
        } else if (totalScore < 100) {
            total.append("   |");
        } else {
            total.append("  |");
        }


        String line = "――――――――――――――――――――――――――――――――――――――――――――";
        System.out.println(line);
        System.out.println(frameRow);
        System.out.println(line);
        System.out.println(frameBuilder);
        System.out.println(total);
        System.out.println(line);


    }

    public void printThrow() {
        System.out.println("Wie viele Pins hast du getroffen?");
    }


}
