package me.prouge.bowlingcounter;

public class Frame {

    private final Pin[] pins;
    private final boolean isLastFrame;

    public Frame(final boolean isLastFrame) {
        this.isLastFrame = isLastFrame;
        pins = isLastFrame ? new Pin[3] : new Pin[2];
    }

    public void setPins(int amount, int currentThrow) {
        pins[currentThrow] = new Pin(amount);
    }

    public boolean isStrike() {
        return pins[0] != null && pins[0].amount() == 10;
    }

    public boolean isSpare() {
        return pins[1] != null && pins[0].amount() + pins[1].amount() == 10;
    }

    public int getTotalScore() {
        return pins[0].amount() + pins[1].amount();
    }

    public int getTotalScoreWithBonus() {
        return pins[0].amount() + pins[1].amount() + pins[2].amount();
    }

    public boolean hasPlayedTwice() {
        return pins[1] != null;
    }

    public boolean hasPlayedOnce() {
        return pins[0] != null;
    }

    public int getFirstRollScore() {
        return pins[0].amount();
    }

    public String getDisplay() {
        if (pins[0] == null) {
            return "    ";
        }

        if (isLastFrame) {
            return getDisplayForLastFrame();
        }

        return getDisplayForRegularFrame();
    }

    private String getDisplayForLastFrame() {
        if (pins[2] != null) {
            String score = calculateScore().trim();
            if (pins[2].amount() == 10) {
                return score + " X";
            }
            if (pins[2].amount() == 0) {
                return score + " -";
            }
            if (pins[2].amount() + pins[1].amount() == 10) {
                return score + " /";
            }
            return score + " " + pins[2].amount();
        }

        if (pins[1] != null) {
            return calculateScore();
        }

        return switch (pins[0].amount()) {
            case 10 -> "X    ";
            case 0 -> "-    ";
            default -> pins[0].amount() + "    ";
        };
    }

    private String getDisplayForRegularFrame() {
        if (pins[1] != null) {
            if (pins[0].amount() + pins[1].amount() == 10) {
                return pins[0].amount() + " / ";
            }

            if (pins[0].amount() == 0 && pins[1].amount() != 0) {
                return "- " + pins[1].amount() + " ";
            }
            if (pins[1].amount() == 0 && pins[0].amount() != 0) {
                return pins[0].amount() + " - ";
            }
            if (pins[0].amount() == 0) {
                return "- - ";
            }
            return pins[0].amount() + " " + pins[1].amount() + " ";
        }

        return switch (pins[0].amount()) {
            case 10 -> "  X ";
            case 0 -> "-   ";
            default -> pins[0].amount() + "   ";
        };
    }

    private String calculateScore() {
        if (pins[0].amount() == 10 && pins[1].amount() == 10) {
            return "X X  ";
        }
        if (pins[0].amount() == 10 && pins[1].amount() != 0) {
            return "X " + pins[1].amount() + "  ";
        }

        if (pins[0].amount() == 0 && pins[1].amount() == 0) {
            return "- -  ";
        }

        if (pins[0].amount() == 0 && pins[1].amount() == 10) {
            return "- /   ";
        }

        if (pins[0].amount() == 10 && pins[1].amount() == 0) {
            return "X -  ";
        }

        if ((pins[0].amount() + pins[1].amount()) == 10) {
            return pins[0].amount() + " /  ";
        }
        return "";
    }

    public boolean hasPlayedThrice() {
        return pins[2] != null;
    }
}
