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

    private final List<Frame> frames = new ArrayList<>();
    private int currentFrameIndex = 0;

    public void start() {
        printer.printTitle();
        initializeFrames();
        printer.printBoard(frames, calculateScores(), getTotalScore());

        for (int i = 0; i < 10; i++) {
            playFrame();
        }
    }

    public void playFrame() {
        if (currentFrameIndex == 9) {
            playLastFrame();
            return;
        }

        for (int i = 0; i < 2; i++) {
            throwPins(i);
            printer.printBoard(frames, calculateScores(), getTotalScore());
            if (frames.get(currentFrameIndex).getFirstRollScore() == 10) {
                currentFrameIndex++;
                return;
            }
        }
        currentFrameIndex++;
    }

    private void playLastFrame() {
        for (int i = 0; i < 3; i++) {
            if (i == 2 && !frames.get(currentFrameIndex).isSpare() && !frames.get(currentFrameIndex).isStrike()) {
                break;
            }
            throwPins(i);
            printer.printBoard(frames, calculateScores(), getTotalScore());
        }
    }

    private void throwPins(int currentThrow) {
        try {
            printer.printThrow();
            int pins = reader.getPins();
            if (!validatePins(currentThrow, pins)) {
                return;
            }
            validatePins(currentThrow, pins);
            frames.get(currentFrameIndex).setPins(pins, currentThrow);
        } catch (Exception e) {
            throwPins(currentThrow);
        }
    }

    private boolean validatePins(int currentThrow, int pins) {
        if (pins > 10 || pins < 0 || (currentThrow == 1 && exceedsMaxPins(currentFrameIndex, pins))) {
            throwPins(currentThrow);
            return false;
        }
        return true;
    }

    private boolean exceedsMaxPins(int currentFrame, int pins) {
        return pins + frames.get(currentFrame).getFirstRollScore() > 10 && currentFrame != 9;
    }

    private void initializeFrames() {
        for (int i = 0; i < 10; i++) {
            frames.add(new Frame(i == 9));
        }
    }

    private List<Integer> calculateScores() {
        List<Integer> scores = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            Frame currentFrame = frames.get(i);
            Frame nextFrame = frames.get(i + 1);
            Frame thirdFrame = frames.get(i + 2);

            if (isTripleStrike(currentFrame, nextFrame, thirdFrame)) {
                scores.add(30);
            } else if (isDoubleStrike(currentFrame, nextFrame, thirdFrame)) {
                scores.add(20 + thirdFrame.getFirstRollScore());
            } else if (isSingleStrike(currentFrame, nextFrame)) {
                scores.add(10 + nextFrame.getTotalScore());
            } else if (isSpare(currentFrame, nextFrame)) {
                scores.add(10 + nextFrame.getFirstRollScore());
            } else if (currentFrame.hasPlayedTwice() && !currentFrame.isSpare()) {
                scores.add(currentFrame.getTotalScore());
            } else {
                scores.add(-1);
            }
        }

        Frame ninthFrame = frames.get(8);
        addScoresForNinthFrame(scores, ninthFrame, frames.get(9));

        Frame tenthFrame = frames.get(9);
        addScoresForTenthFrame(scores, tenthFrame);

        return scores;
    }

    private void addScoresForNinthFrame(List<Integer> scores, Frame currentFrame, Frame nextFrame) {
        if (currentFrame.isStrike() && nextFrame.hasPlayedTwice()) {
            scores.add(10 + nextFrame.getTotalScore());
        } else if (currentFrame.isSpare() && nextFrame.hasPlayedOnce()) {
            scores.add(10 + nextFrame.getFirstRollScore());
        } else if (currentFrame.hasPlayedTwice() && !currentFrame.isSpare()) {
            scores.add(currentFrame.getTotalScore());
        } else {
            scores.add(-1);
        }
    }

    private void addScoresForTenthFrame(List<Integer> scores, Frame currentFrame) {
        if ((currentFrame.isStrike() || currentFrame.isSpare()) && currentFrame.hasPlayedThrice()) {
            scores.add(currentFrame.getTotalScoreWithBonus());
        } else if (currentFrame.hasPlayedTwice() && (!currentFrame.isSpare() && !currentFrame.isStrike())) {
            scores.add(currentFrame.getTotalScore());
        } else {
            scores.add(-1);
        }
    }

    private boolean isTripleStrike(Frame currentFrame, Frame nextFrame, Frame thirdFrame) {
        return currentFrame.isStrike() && nextFrame.isStrike() && thirdFrame.isStrike();
    }

    private boolean isDoubleStrike(Frame currentFrame, Frame nextFrame, Frame thirdFrame) {
        return currentFrame.isStrike() && nextFrame.isStrike() && thirdFrame.hasPlayedOnce();
    }

    private boolean isSingleStrike(Frame currentFrame, Frame nextFrame) {
        return currentFrame.isStrike() && nextFrame.hasPlayedTwice();
    }

    private boolean isSpare(Frame currentFrame, Frame nextFrame) {
        return currentFrame.isSpare() && nextFrame.hasPlayedOnce();
    }

    private int getTotalScore() {
        return calculateScores().stream().filter(sc -> sc != -1).mapToInt(Integer::intValue).sum();
    }

}

