package classes;

import java.util.Vector;

import static classes.EnumDirect.*;

class Tank {
    /**
     * 坦克的横纵坐标
     * 坦克的速度
     * 坦克的方向
     * 坦克的类型
     * 是否存活
     *
     * 最后给每个坦克配好子弹
     */
    private int x = 0;
    private int y = 0;
    private int speed = 3;
    private EnumDirect direction = UP;
    private int type = 0;
    private boolean isLive = true;

    Vector<Shot> shots = new Vector<>();
    Shot shot = null;

    public Tank(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setEnumDirect(EnumDirect enumDirect) {
        this.direction = enumDirect;
    }

    public EnumDirect getEnumDirect() {
        return direction;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    /**
     * 以下为坦克的活动方法
     */
    public void moveUp() {
        y -= speed;
    }

    public void moveDown() {
        y += speed;
    }

    public void moveLeft() {
        x -= speed;
    }

    public void moveRight() {
        x += speed;
    }

    /**
     * 发射子弹
     */
    public void shot() {
        if (this.shots.size() < 10) {
            switch (direction) {
                case UP:
                    //添加子弹
                    addShot(this.getX() + 10 - 1, this.getY(), UP);
                    break;
                case DOWN:
                    addShot(this.getX() + 10 - 1, this.getY() + 30, DOWN);
                    break;
                case LEFT:
                    addShot(this.getX(), this.getY() + 10 - 1, LEFT);
                    break;
                case RIGHT:
                    addShot(this.getX() + 30, this.getY() + 10 - 1, RIGHT);
                    break;
                default:
            }
        }

        Thread shotThread = new Thread(shot);
        shotThread.start();
    }

    /**
     * 这是添加子弹用的
     *  @param x 初始横坐标
     * @param y 初始纵坐标
     * @param direction 发射方向
     */
    public void addShot(int x, int y, EnumDirect direction) {
        //创建一个对应的Shot实例
        shot = new Shot(x, y, direction);
        //然后把创建出的子弹添加到集合里
        shots.add(shot);
    }

    /**
     * 检查该坦克是否与其他坦克碰撞
     * @param tank 传入坦克
     * @return 返回是否碰撞
     */
    public void touch(Tank tank) {
        //默认不相撞
        boolean b = false;

        switch (this.getEnumDirect()) {
            //当该坦克向上时
            case UP:
                //当对象坦克方向为竖直时
                if (tank.getEnumDirect() == UP || tank.getEnumDirect() == DOWN) {
                    //判断第一个点
                    if (this.getX() > tank.getX() - 5 && this.getX() < tank.getX() + 20 + 5 &&
                            this.getY() > tank.getY() - 5 && this.getY() < tank.getY() + 30 + 5) {
                        b = true;
                    }
                    //判断第二个点
                    if (this.getX() + 20 > tank.getX() - 5 && this.getX() + 20 < tank.getX() + 20 + 5 &&
                            this.getY() > tank.getY() - 5 && this.getY() < tank.getY() + 30 + 5) {
                        b = true;
                    }
                }
                //当对象坦克方向为水平时
                if (tank.getEnumDirect() == LEFT || tank.getEnumDirect() == RIGHT) {
                    if (this.getX() > tank.getX() - 5 && this.getX() < tank.getX() + 30 + 5 &&
                            this.getY() > tank.getY() - 5 && this.getY() < tank.getY() + 20 + 5) {
                        b = true;
                    }
                    if (this.getX() + 20 - 5 > tank.getX() && this.getX() + 20 < tank.getX() + 30 + 5 &&
                            this.getY() > tank.getY() - 5 && this.getY() < tank.getY() + 20 + 5) {
                        b = true;
                    }
                }
                //若相撞了，让它后退
                if(b){
                    this.setY(this.getY()+this.getSpeed());
                }
                break;
            //当该坦克向下时
            case DOWN:
                //当对象坦克方向竖直
                if (tank.getEnumDirect() == UP || tank.getEnumDirect() == DOWN) {
                    if (this.getX() + 20 > tank.getX() - 5 && this.getX() + 20 < tank.getX() + 20 + 5 &&
                            this.getY() > tank.getY() - 5 && this.getY() < tank.getY() + 30 + 5) {
                        b = true;
                    }
                    if (this.getX() + 20 > tank.getX() - 5 && this.getX() + 20 < tank.getX() + 20 + 5 &&
                            this.getY() + 30 > tank.getY() - 5 && this.getY() + 30 < tank.getY() + 30 + 5) {
                        b = true;
                    }
                }
                //当对象坦克方向水平
                if (tank.getEnumDirect() == LEFT || tank.getEnumDirect() == RIGHT) {
                    if (this.getX() + 20 > tank.getX() - 5 && this.getX() + 20 < tank.getX() + 30 + 5 &&
                            this.getY() > tank.getY() - 5 && this.getY() < tank.getY() + 20 + 5) {
                        b = true;
                    }
                    if (this.getX() + 20 > tank.getX() - 5 && this.getX() + 20 < tank.getX() + 30 + 5 &&
                            this.getY() + 30 > tank.getY() - 5 && this.getY() + 30 < tank.getY() + 20 + 5) {
                        b = true;
                    }
                }
                if(b){
                    this.setY(this.getY()-this.getSpeed());
                }
                break;
            //当该坦克向左时
            case LEFT:
                //当对象坦克方向竖直
                if (tank.getEnumDirect() == UP || tank.getEnumDirect() == DOWN) {
                    if (this.getX() > tank.getX() - 5 && this.getX() < tank.getX() + 20 + 5 &&
                            this.getY() > tank.getY() - 5 && this.getY() < tank.getY() + 30 + 5) {
                        b = true;
                    }
                    if (this.getX() > tank.getX() - 5 && this.getX() < tank.getX() + 20 + 5 &&
                            this.getY() + 20 > tank.getY() - 5 && this.getY() + 20 < tank.getY() + 30 + 5) {
                        b = true;
                    }
                }
                //当对象坦克方向水平
                if (tank.getEnumDirect() == LEFT || tank.getEnumDirect() == RIGHT) {
                    if (this.getX() > tank.getX() - 5 && this.getX() < tank.getX() + 30 + 5 &&
                            this.getY() > tank.getY() - 5 && this.getY() < tank.getY() + 20 + 5) {
                        b = true;
                    }
                    if (this.getX() > tank.getX() - 5 && this.getX() < tank.getX() + 30 + 5 &&
                            this.getY() + 20 > tank.getY() - 5 && this.getY() + 20 < tank.getY() + 20 + 5) {
                        b = true;
                    }
                }
                if(b){
                    this.setX(this.getX()+this.getSpeed());
                }
                break;
            //当该坦克向右时
            case RIGHT:
                //当对象坦克方向竖直
                if (tank.getEnumDirect() == UP || tank.getEnumDirect() == DOWN) {
                    if (this.getX() + 30 > tank.getX() - 5 && this.getX() + 30 < tank.getX() + 20 + 5 &&
                            this.getY() > tank.getY() - 5 && this.getY() < tank.getY() + 30 + 5) {
                        b = true;
                    }
                    if (this.getX() + 30 > tank.getX() - 5 && this.getX() + 30 < tank.getX() + 20 + 5 &&
                            this.getY() + 20 > tank.getY() - 5 && this.getY() + 20 < tank.getY() + 30 + 5) {
                        b = true;
                    }
                }
                //当对象坦克方向水平
                if (tank.getEnumDirect() == LEFT || tank.getEnumDirect() == RIGHT) {
                    if (this.getX() + 30 > tank.getX() - 5 && this.getX() + 30 < tank.getX() + 30 + 5 &&
                            this.getY() > tank.getY() - 5 && this.getY() < tank.getY() + 20 + 5) {
                        b = true;
                    }
                    if (this.getX() + 30 > tank.getX() - 5 && this.getX() + 30 < tank.getX() + 30 + 5 &&
                            this.getY() + 20 > tank.getY() - 5 && this.getY() + 20 < tank.getY() + 20 + 5) {
                        b = true;
                    }
                }
                if(b){
                    this.setX(this.getX()-this.getSpeed());
                }
        }
    }
}