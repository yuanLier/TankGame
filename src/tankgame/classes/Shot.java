package tankgame.classes;

/**
 * 子弹类：
 * 就是刚
 */

public class Shot implements Runnable{
    /**
     * 横纵坐标（好确定点）
     * 子弹打出的方向
     * 子弹的类型（颜色）
     * 子弹速度
     * 是否存活
     */
    private int x;
    private int y;
    private EnumDirect direction;
    private int speed = 9;
    private boolean isLive = true;

    public Shot(int x, int y, EnumDirect direction){
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

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep ( 50 );
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }

            //判断子弹发出方向并作出相应运动
            switch (direction){
                case UP:
                    y -= speed;break;
                case DOWN:
                    y += speed;break;
                case LEFT:
                    x -= speed;break;
                case RIGHT:
                    x += speed;break;
                default:
            }

            //设置子弹消失条件
            if(x<0 || x>750 || y<0 || y>600){
                this.isLive = false;
                //要加break不然子弹会一直动（无法跳出循环）break;
            }
        }
    }
}
