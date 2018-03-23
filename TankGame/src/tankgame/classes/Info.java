package tankgame.classes;

/**
 * 信息类：
 * 用来提取保存下来的信息
 */
class Info {
    private int x;
    private int y;
    private int direction;

    public Info(int x,int y,int direction){
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDirection() {
        return direction;
    }
}
