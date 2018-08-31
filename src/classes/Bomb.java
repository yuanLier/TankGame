package classes;

/**
 * 爆炸类；
 * 画坦克被击中时的爆炸效果用的
 */

class Bomb {
    private int x;
    private int y;
    int life = 3;
    boolean isLive = true;
    //自带音效
    AePlayWave apw = new AePlayWave ( "src/tankgame/music/bomb.wav" );

    public Bomb(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * 减少炸弹生命
     */
    public void loseLife(){
        if(life > 0){
            life--;
        }else{
            this.isLive = false;
        }
    }


}
