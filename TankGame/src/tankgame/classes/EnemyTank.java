package tankgame.classes;

/**
 * 敌人坦克：
 * 自带子弹，坦克被消灭后其子弹也跟着消失
 */

import java.util.Vector;

class EnemyTank extends Tank implements Runnable{
    //检查是否与墙相撞的
    private boolean isTouch = false;

    public void setTouch(boolean isTouch){
        this.isTouch = isTouch;
    }

    //创建一个向量，得到Panel中其他坦克的信息
    Vector<EnemyTank> enemyTanks = new Vector<> (  );
    //再创建一个方法，使Panel中每创建一个敌人坦克就调用它一次
    public void getInfo(Vector<EnemyTank> enemyTank){
        this.enemyTanks = enemyTank;
    }

    /**
     * 判断是否与其他坦克相撞
     * @return 是否相撞
     */
    public void touchOthers(){
        //将敌人坦克一一取出（包括它自己）
        for (int i = 0; i < enemyTanks.size (); i++) {
            EnemyTank et = enemyTanks.get ( i );
            //当取出的坦克不它自己时，分情况讨论
            if (this != et) {
                touch(et);
            }
        }
    }

    public EnemyTank(int x, int y) {
        super ( x, y );
    }

    /**
     * 用来将info中记录的方向转换为枚举类
     * @param direction 传入记录的整型类方向
     * @return 返回枚举类的方向
     */
    public EnumDirect getEnumDirection(int direction){
        switch (direction){
            case 0:
                return EnumDirect.UP;
            case 1:
                return EnumDirect.DOWN;
            case 2:
                return EnumDirect.LEFT;
            case 3:
                return EnumDirect.RIGHT;
            default:
                return EnumDirect.UP;
        }
    }

    /**
     * 产生随机方向
     * @return 返回对应的方向
     */
    public EnumDirect getRandomDirect(){
        //注意后面是乘四，因为产生的是[0,n)之间的数，强转只取其整数
        int direction = (int)(Math.random ()*4);
        return getEnumDirection(direction);
    }

    /**
     * 以下四个是用来控制子弹发射的
     * （每轮移动发射3次）
     */
    public void shotUp(){
        for (int i = 0; i < 10; i++) {
            if(getY() > 0 && !isTouch){
                setY(getY() - getSpeed());
            }

            try {
                Thread.sleep ( 50 );
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }
        }
    }

    public void shotDown(){
        for (int i = 0; i < 10; i++) {
            if(getY() < 500 && !isTouch){
                setY(getY() + getSpeed());
            }
            try {
                Thread.sleep ( 50 );
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }
        }
    }

    public void shotLeft(){
        for (int i = 0; i < 10; i++) {
            if(getX() > 0 && isTouch){
                setX(getX() - getSpeed());
            }
            try {
                Thread.sleep ( 50 );
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }
        }
    }

    public void shotRight(){
        for (int i = 0; i < 10; i++) {
            if(getX() < 720 && isTouch){
                setX(getX() + getSpeed());
            }
            try {
                Thread.sleep ( 50 );
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }
        }
    }

    @Override
    public void run() {
        while (true){
            //检查敌军坦克之间是否相撞
            this.touchOthers();
            //让敌人的坦克自由移动（注意不要撞墙）
            switch (getEnumDirect()){
                case UP:
                    for(int i = 0; i < 3; i++){
                        shotUp();
                        shot();
                    }
                    break;
                case DOWN:
                    for(int i = 0; i < 3; i++){
                        shotDown();
                        shot();
                    }
                    break;
                case LEFT:
                    for(int i = 0; i < 3; i++){
                        shotLeft();
                        shot();
                    }
                    break;
                case RIGHT:
                    for(int i = 0; i < 3; i++){
                        shotRight();
                        shot();
                    }
                    break;
                default:
            }

            //用随机数取方向
            setEnumDirect(getRandomDirect());

            if(!this.isLive()){
                //退出循环，就假装结束了当前线程
                break;
            }
        }
    }
}
