package tankgame.classes;

/**
 * 当生命值为1，丢一个医疗包
 */
public class Remedy {
    private int x;
    private int y;

    public Remedy(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
