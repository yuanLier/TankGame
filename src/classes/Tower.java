package classes;

/**
 * 塔..呸不对是地基:
 * 因为是地基所以塔克可以碾过去[一本正经.jpg]
 * 敌人子弹击中无恙，我的子弹击中十次后死亡
 */

public class Tower {
    private int x;
    private int y;

    public Tower(int x,int y ){
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
