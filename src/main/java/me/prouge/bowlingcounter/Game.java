package me.prouge.bowlingcounter;

import com.google.inject.Inject;
import me.prouge.bowlingcounter.utils.Printer;
import me.prouge.bowlingcounter.utils.Reader;

import java.util.ArrayList;
import java.util.List;

public class Game {

    @Inject
    private Printer printer;

    @Inject
    private Reader reader;

    private final ArrayList<Frame> frames = new ArrayList<>();

    private int currentFrame = 0;

    public void start() {
        printer.printTitle();

        for (int i = 0; i < 10; i++) {
            if (i == 9) {
                frames.add(new Frame(true));
                break;
            }
            frames.add(new Frame(false));
        }
        printer.printBoard(frames, calculateScores(), getTotalScore());

        for (int i = 0; i < 10; i++) {
            playFrame();
        }
    }

    public void playFrame() {
        if (currentFrame == 9) {
            for (int i = 0; i < 3; i++) {
                if (i == 2 && (!frames.get(currentFrame).isSpare() && !frames.get(currentFrame).isStrike())) {
                    break;

                }
                throwPins(i);
                printer.printBoard(frames, calculateScores(), getTotalScore());
            }
            return;
        }

        for (int i = 0; i < 2; i++) {
            throwPins(i);
            printer.printBoard(frames, calculateScores(), getTotalScore());
            if (frames.get(currentFrame).getFirstRollScore() == 10) {
                currentFrame++;
                return;
            }
        }
        currentFrame++;
    }

    private void throwPins(final int currentThrow) {
        try {
            printer.printThrow();
            int pins = reader.getPins();
            if (pins > 10 || pins < 0 || (currentThrow == 1 && pins + frames.get(currentFrame).getFirstRollScore() > 10 && currentFrame != 9)) {
                throwPins(currentThrow);
                return;
            }
            frames.get(currentFrame).setPins(pins, currentThrow);
        } catch (Exception e) {
            throwPins(currentThrow);
        }

    }


    private List<Integer> calculateScores() {
        ArrayList<Integer> scores = new ArrayList<>();

        for (int i = 0; i < 8; i++) {

            Frame currentFrame = frames.get(i);
            Frame nextFrame = frames.get(i + 1);
            Frame thirdFrame = frames.get(i + 2);
            if (currentFrame.isStrike() && nextFrame.isStrike() && thirdFrame.isStrike()) {
                scores.add(30);
                continue;
            }
            if (currentFrame.isStrike() && nextFrame.isStrike() && thirdFrame.hasPlayedOnce()) {
                scores.add(20 + thirdFrame.getFirstRollScore());
                continue;
            }

            if (currentFrame.isStrike() && nextFrame.hasPlayedTwice()) {
                scores.add(10 + nextFrame.getTotalScore());
                continue;
            }

            if (currentFrame.isSpare() && nextFrame.hasPlayedOnce()) {
                scores.add(10 + nextFrame.getFirstRollScore());
                continue;
            }

            if (currentFrame.hasPlayedTwice() && !currentFrame.isSpare()) {
                scores.add(currentFrame.getTotalScore());
                continue;
            }
            scores.add(-1);

        }

        Frame currentFrame = frames.get(8);

        if (currentFrame.isStrike() && frames.get(9).hasPlayedTwice()) {
            scores.add(10 + frames.get(9).getTotalScore());
        } else if (currentFrame.isSpare() && frames.get(9).hasPlayedOnce()) {
            scores.add(10 + frames.get(9).getFirstRollScore());
        } else if (currentFrame.hasPlayedTwice() && !currentFrame.isSpare()) {
            scores.add(currentFrame.getTotalScore());
        } else {
            scores.add(-1);
        }
        currentFrame = frames.get(9);
        if ((currentFrame.isStrike() || currentFrame.isSpare()) && currentFrame.hasPlayedThrice()) {
            scores.add(currentFrame.getTotalScoreWithBonus());
        } else if (currentFrame.hasPlayedTwice() && (!currentFrame.isSpare() && !currentFrame.isStrike())) {
            scores.add(currentFrame.getTotalScore());
        } else {
            scores.add(-1);
        }
        return scores;
    }

    private int getTotalScore() {
        return calculateScores().stream().filter(sc -> sc != -1).mapToInt(i -> i).sum();
    }


}
