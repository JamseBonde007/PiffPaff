package sample;

public class Score {
    private int score;
    private int level;



    public Score(int level, int score) {
        this.level = level;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return String.valueOf(level+"|"+score);
    }
}