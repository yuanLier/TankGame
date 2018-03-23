package tankgame.classes;

/**
 * 红墙：
 * 不可摧毁
 */

class RedDia {
    private int x;
    private int y;

    public RedDia(int x,int y){
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
