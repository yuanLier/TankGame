package tankgame.classes;

/**
 * 白墙：
 * 超级脆弱但又不准坦克过
 * 子弹一打就破 子弹也over
 */

class WhiteDia {
    private int x;
    private int y;

    public WhiteDia(int x,int y){
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
