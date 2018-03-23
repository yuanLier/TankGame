package tankgame.classes;

/**
 * 游戏面板：
 * 代码最乱的地方
 * 改不动了真的[哭唧唧]
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import static tankgame.classes.EnumDirect.*;

class MyPanel extends JPanel implements KeyListener,Runnable{
    /**
     * 我的坦克
     * 坦克的初始坐标
     */
    MyTank myTank;
    int initX = 700;
    int initY = 450;

    /**
     * 敌人坦克及坦克规模
     * 记录信息的info
     * 是否碰到墙
     */
    Vector<EnemyTank> ets = new Vector<>( );
    int etsSize = 9;
    Vector<Info> infos = new Vector<> ( );

    /**
     * 炸弹组
     */
    Vector<Bomb> bombs = new Vector<> ( );
    Image img1 = null;
    Image img2 = null;
    Image img3 = null;

    /**
     * 医疗包
     * 控制补给次数
     * 选择出现区域
     */
    Vector<Remedy> remedies = new Vector<> ( );
    Remedy remedy1 = new Remedy(40+(int)(Math.random()*100+1),300+(int)(Math.random()*200+1));
    Remedy remedy2 = new Remedy(200+(int)(Math.random()*380+1),20+(int)(Math.random()*60+1));
    Remedy remedy3 = new Remedy(580+(int)(Math.random()*130+1),200+(int)(Math.random()+150+1));
    Remedy remedy = null;
    //    Remedy remedy2 = new Remedy(580,80);
    Image remedyImg = null;
    int area = (int)(Math.random()*3);
    boolean supply = false;

    /**
     * 地基
     */
    Tower tower1 = new Tower ( 30,40 );
    Tower tower2 = new Tower ( 650,30 );
    Image towerImg = null;

    /**
     * 红白墙
     * time用于初始化
     */
    Vector<RedDia> redDias = new Vector<> ( );
    Vector<WhiteDia> whiteDias = new Vector<> ( );
    int time = 0;

    /**
     * 用来判断是新游戏还是继续游戏的flag
     */
    String flag;

    public MyPanel(String f){
        flag = f;

        //创建我的坦克
        myTank = new MyTank ( 700,450 );

        //判断是新游戏还是继续游戏
        if (flag.equals ( "newGame" )) {
            //创建敌人的坦克
            for(int i = 0; i < etsSize; i++){
                //创建敌人的坦克并初始化位置
                EnemyTank et;
                if (i < 1){
                    //最右下角一个
                    et = new EnemyTank ( 420,500 );
                }else if(i < 3){
                    //右边中间的
                    et = new EnemyTank ( 640,120+i*100 );
                }else if(i < 5){
                    //左下角两个
                    et = new EnemyTank ( 40,i*120 );
                }else if(i < 8){
                    //右上角三个
                    et = new EnemyTank ( i*100-200,60 );
                }else{
                    //最左上守塔的
                    et = new EnemyTank ( 60,60 );
                }

                //初始化敌人坦克的方向
                et.setEnumDirect( UP );
                //初始化敌人坦克的类型(颜色)
                et.setType ( 1 );
                //将创建出来的坦克添加到Vector
                ets.add ( et );
                //启动线程
                Thread t = new Thread ( et );
                t.start ();
            }
        }else if(flag.equals ( "continueGame" )){
            infos = new Recorder ().getInfo ();
            for(int i = 0; i < infos.size (); i++){
                Info info = infos.get ( i );
                EnemyTank et = new EnemyTank ( info.getX(),info.getY() );
                et.setEnumDirect( et.getEnumDirection(info.getDirection()) );
                et.setType ( 1 );
                ets.add ( et );

                Thread t = new Thread ( et );
                t.start ();
            }
        }

        //初始化爆炸时和塔和医疗包的的图片
        try {
            this.img1 = ImageIO.read ( new File
                    ( "src/tankgame/images/bomb1.jpg" ) );
            this.img2 = ImageIO.read ( new File
                    ( "src/tankgame/images/bomb1.jpg" ) );
            this.img3 = ImageIO.read ( new File
                    ( "src/tankgame/images/bomb1.jpg" ) );
            this.towerImg = ImageIO.read ( new File
                    ( "src/tankgame/images/tower.jpg" ) );
            this.remedyImg = ImageIO.read ( new File
                    ( "src/tankgame/images/remedy.jpg" ) );
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    /**
     * 重写paint
     * @param g 画笔
     */
    @Override
    public void paint(Graphics g){
        super.paint ( g );

        //首先画一条线，让游戏区和成绩区分隔开
        g.setColor ( Color.decode ( "0x1A79D0" ) );
        g.fillRect ( 750,0,1,600 );

        //然后来画环境，红白墙和小地基
        if (time == 0) {
            //首先是不会被打坏的红石头墙
            for(int i = 0; i < 22; i++){
                this.drawRedDia ( 1+25*i,245,g );
                this.drawRedDia ( 725-25*i,390,g );
            }
            for(int i = 0; i < 8; i++){
                this.drawRedDia ( 120,25*i,g );
            }
            for(int i = 0; i < 17; i++){
                this.drawRedDia ( 725-25*i,120,g );
            }
            //然后是会被打穿的白石头墙
            for(int i = 0; i < 6; i++){
                g.setColor ( Color.lightGray );
                this.drawWhiteDia ( 250,415+1+20*i,g );
                this.drawWhiteDia ( 500+1,270-1+20*i,g );
            }
            for(int i = 0; i < 5; i++){
                this.drawWhiteDia ( 375,225-20*i,g );
            }
        }else{
            //红石头墙
            for(int i = 0; i < redDias.size (); i++){
                RedDia rd = redDias.get ( i );
                g.setColor ( Color.decode ( "0xFF8080" ) );
                g.fill3DRect ( rd.getX(),rd.getY(),25,25,false );
            }
            //白石头墙
            for(int i = 0; i < whiteDias.size (); i++){
                WhiteDia wd = whiteDias.get ( i );
                g.setColor ( Color.lightGray );
                g.fill3DRect ( wd.getX(),wd.getY(),25,20,false );
            }
        }

        //两个塔
        if(Recorder.getTowerLife1 () > 0){
            g.drawImage ( towerImg,tower1.getX(),tower1.getY(),50,65,this);
        }
        if(Recorder.getTowerLife2 () > 0){
            g.drawImage ( towerImg,tower2.getX(),tower2.getY(),50,65,this);
        }

        //添加三个区域的医疗包，并随机选一个作为最终生成的那个
        remedies.add(remedy1);
        remedies.add(remedy2);
        remedies.add(remedy3);
        remedy = remedies.get(area);
        //只剩一条命时出现医疗包
        if(Recorder.getMyLife() == 1){
            //在初次补给时生成
            if(!supply){
                g.drawImage(remedyImg,remedy.getX(),remedy.getY(),20,20,this);
            }
        }

        //坦克和塔的计分
        this.drawTank ( 800,50,g,UP,1 );
        this.drawTank ( 900,50,g,UP,0 );
        g.setColor ( Color.white );
        g.drawString ( Recorder.getEnemyNum ()+"",850,75 );
        //控制数字显示，避免出现负数
        if (Recorder.getMyLife () > 0) {
            g.drawString ( Recorder.getMyLife ()+"",950,75 );
        }else {
            g.drawString ( 0+"",950,75 );
        }
        g.drawImage ( towerImg,780,120,50,65,this);
        g.drawImage ( towerImg,880,120,50,65,this);
        if (Recorder.getTowerLife1 () > 0) {
            g.drawString ( Recorder.getTowerLife1 ()+"",850,155 );
        }else {
            g.drawString ( 0+"",850,155 );
        }
        if (Recorder.getTowerLife2 () > 0) {
            g.drawString ( Recorder.getTowerLife2 ()+"",950,155 );
        }else {
            g.drawString ( 0+"",950,155 );
        }

        //然后调用函数画一个我的坦克
        if (Recorder.getMyLife () > 0) {
            this.drawTank ( myTank.getX (),myTank.getY (),g,myTank.getEnumDirect(),0 );
        }

        //画字
        //我坦克生命为0：game over
        if(Recorder.getMyLife () <= 0){
            g.setColor ( Color.cyan );
            g.setFont ( new Font ( "隶书",Font.BOLD,80 ) );
            g.drawString ( "Game Over!",190,260 );
        }
        //敌军坦克数为0或地基均被摧毁：win
        if(Recorder.getMyLife () > 0 && Recorder.getEnemyNum () == 0 ){
            g.setColor ( Color.cyan );
            g.setFont ( new Font ( "隶书",Font.BOLD,120 ) );
            g.drawString ( "You win!",125,275 );
        }
        if(Recorder.getMyLife () > 0 && Recorder.getTowerLife1 () <= 0 && Recorder.getTowerLife2 () <= 0){
            g.setColor ( Color.cyan );
            g.setFont ( new Font ( "隶书",Font.BOLD,120 ) );
            g.drawString ( "You win!",125,275 );
        }

        //再画出我的子弹
        drawShots(g,myTank);

        //再用个循环画敌人的坦克
        // （注意这里i是小于ets.size而非etsSize，因为在游戏过程中坦克数量会变）
        for(int i = 0; i < ets.size (); i++){
            EnemyTank et = ets.get ( i );
            //将Panel中敌人坦克的信息传给EnemyTank
            et.getInfo ( ets );
            if (et.isLive()) {
                this.drawTank ( et.getX(),et.getY(), g,et.getEnumDirect(),1 );
                drawShots(g,et);
            }
        }

        //然后来画炸弹
        for(int i = 0; i < bombs.size (); i++){
            //取出炸弹
            Bomb b = bombs.get ( i );
            //判断生命值，选择表示此时爆炸状态的图片
            if(b.life == 3){
                //添加爆炸的音效
                b.apw.start();
                g.drawImage ( img1,b.getX(),b.getY(),30,30,this );
            }else if(b.life == 2){
                g.drawImage ( img2,b.getX(),b.getY(),30,30,this );
            }else if(b.life == 1){
                g.drawImage ( img3,b.getX(),b.getY(),30,30,this );
            }
            //使生命值减少
            b.loseLife ();
            //当生命为0时，移出Vector
            if(!b.isLive){
                bombs.remove ( b );
            }
        }
    }

    /**
     * 画不可击破的红墙
     * @param x 横坐标
     * @param y 纵坐标
     * @param g 画笔
     */
    public void drawRedDia(int x,int y, Graphics g){
        g.setColor ( Color.decode ( "0xFF8080" ) );
        g.fill3DRect ( x,y,25,25,false );
        RedDia redDia = new RedDia ( x,y );
        redDias.add ( redDia );
    }

    /**
     * 画可击破的白墙
     * @param x 横坐标
     * @param y 纵坐标
     * @param g 画笔
     */
    public void drawWhiteDia(int x,int y,Graphics g){
        g.setColor ( Color.white );
        g.fill3DRect ( x,y,25,20,false );
        WhiteDia whiteDia = new WhiteDia ( x,y );
        whiteDias.add ( whiteDia );
    }

    /**
     * 画坦克（注意一定要传画笔！！！）
     * @param x 坦克横坐标
     * @param y 坦克纵坐标
     * @param g 画笔
     * @param direct 坦克运动方向
     * @param type 坦克类型（敌/友）
     */
    public void drawTank(int x,int y,Graphics g,EnumDirect direct,int type){
        //首先判断是什么类型的坦克(是敌是友)
        switch (type){
            case 0:
                g.setColor ( Color.orange );break;
            case 1:
                g.setColor ( Color.cyan );break;
            case 2:
                g.setColor ( Color.blue );break;
            case 3:
                g.setColor ( Color.green );break;
            default:
        }

        //然后判断它移动的方向
        switch (direct){
            case UP:
                //先来左边的长方形（记得用3D画出凹凸效果）
                g.fill3DRect ( x,y,5,30,false );
                //然后是右边的长方形
                g.fill3DRect ( x+15,y,5,30,false);
                //再是中间的小矩形
                g.fill3DRect ( x+5,y+5,10,20,false );
                //然后是中间的圆
                g.drawOval ( x+7,y+12,5,5 );
                //最后是炮筒
                g.drawLine ( x+10,y+11,x+10,y+2 );
                //还是给它们画个轮子吧=w=
                g.drawLine ( x,y+3,x+5,y+3 );
                g.drawLine ( x,y+10,x+5,y+10 );
                g.drawLine ( x,y+17,x+5,y+17 );
                g.drawLine ( x,y+24,x+5,y+24 );
                g.drawLine ( x+15,y+3,x+20-1,y+3 );
                g.drawLine ( x+15,y+10,x+20-1,y+10 );
                g.drawLine ( x+15,y+17,x+20-1,y+17 );
                g.drawLine ( x+15,y+24,x+20-1,y+24 );
                break;
            case DOWN:
                g.fill3DRect ( x,y,5,30,false );
                g.fill3DRect ( x+15,y,5,30,false);
                g.fill3DRect ( x+5,y+5,10,20,false );
                g.drawOval ( x+7,y+12,5,5 );
                g.drawLine ( x+10,y+28,x+10,y+18 );
                g.drawLine ( x,y+4,x+5-1,y+4 );
                g.drawLine ( x,y+11,x+5-1,y+11 );
                g.drawLine ( x,y+18,x+5-1,y+18 );
                g.drawLine ( x,y+25,x+5-1,y+25 );
                g.drawLine ( x+15,y+4,x+20-1,y+4 );
                g.drawLine ( x+15,y+11,x+20-1,y+11 );
                g.drawLine ( x+15,y+18,x+20-1,y+18 );
                g.drawLine ( x+15,y+25,x+20-1,y+25 );
                break;
            case LEFT:
                g.fill3DRect ( x,y,30,5,false );
                g.fill3DRect ( x,y+15,30,5,false);
                g.fill3DRect ( x+5,y+5,20,10,false );
                g.drawOval ( x+12,y+7,5,5 );
                g.drawLine ( x+2,y+10,x+13,y+10 );
                g.drawLine ( x+4,y,x+4,y+5-1 );
                g.drawLine ( x+11,y,x+11,y+5-1 );
                g.drawLine ( x+18,y,x+18,y+5-1 );
                g.drawLine ( x+25,y,x+25,y+5-1 );
                g.drawLine ( x+4,y+15,x+4,y+20-1 );
                g.drawLine ( x+11,y+15,x+11,y+20-1 );
                g.drawLine ( x+18,y+15,x+18,y+20-1 );
                g.drawLine ( x+25,y+15,x+25,y+20-1 );
                break;
            case RIGHT:
                g.fill3DRect ( x,y,30,5,false );
                g.fill3DRect ( x,y+15,30,5,false);
                g.fill3DRect ( x+5,y+5,20,10,false );
                g.drawOval ( x+12,y+7,5,5 );
                g.drawLine ( x+18,y+10,x+28,y+10 );
                g.drawLine ( x+3,y,x+3,y+5 );
                g.drawLine ( x+10,y,x+10,y+5 );
                g.drawLine ( x+17,y,x+17,y+5 );
                g.drawLine ( x+24,y,x+24,y+5 );
                g.drawLine ( x+3,y+15,x+3,y+20-1 );
                g.drawLine ( x+10,y+15,x+10,y+20-1 );
                g.drawLine ( x+17,y+15,x+17,y+20-1 );
                g.drawLine ( x+24,y+15,x+24,y+20-1 );
                break;
            default:
        }
    }

    /**
     * 画子弹
     * @param g 画笔
     * @param tank 传入要发射子弹的那个坦克
     */
    public void drawShots(Graphics g, Tank tank){
        for (int i = 0; i < tank.shots.size (); i++ ) {
            if(tank.shots.get ( i ) != null && tank.shots.get ( i ).isLive()){
                g.draw3DRect ( tank.shots.get ( i ).getX(),tank.shots.get ( i ).getY(),1,1,false );
            }else if(!tank.shots.get ( i ).isLive()){
                //就把子弹从集合里取出来
                tank.shots.remove ( tank.shots.get ( i ) );
            }
        }
    }

    /**
     * 判断是否击中红墙
     * @param shot 传入子弹
     */
    public void hitRedDia(Shot shot){
        for(int i = 0; i < redDias.size (); i++){
            RedDia rd = redDias.get ( i );
            if(shot.getX() >= rd.getX() && shot.getX() <= rd.getX()+25 &&
                    shot.getY() >= rd.getY() && shot.getY() <= rd.getY()+25){
                shot.setLive(false);
            }
        }
    }

    /**
     * 判断是否击中白墙
     * @param shot 传入子弹
     */
    public void hitWhiteDia(Shot shot){
        for(int i = 0; i < whiteDias.size (); i++){
            WhiteDia wd = whiteDias.get ( i );
            if(shot.getX() >= wd.getX() && shot.getX() <= wd.getX()+25 &&
                    shot.getY() >= wd.getY() && shot.getY() <= wd.getY()+20){
                shot.setLive(false);
                whiteDias.remove ( wd );
            }
        }
    }

    /**
     * 判断敌军是否碰到墙
     * @return 是否碰撞
     */
    public void enTouchDia(Vector<EnemyTank> enemyTanks){
        for(int i = 0; i < enemyTanks.size (); i++){
            EnemyTank et = enemyTanks.get ( i );
            et.setTouch(false);
            //白墙
            for(int j = 0; j < whiteDias.size (); j++){
                WhiteDia wd = whiteDias.get ( j );
                switch (et.getEnumDirect()){
                    case UP:
                        if(et.getX()>=wd.getX() && et.getY()>=wd.getY() &&
                                et.getX()<=wd.getX()+25 && et.getY()<=wd.getY()+20){
                            et.setTouch(true);
                        }
                        if(et.getX()+20>=wd.getX() && et.getY()>=wd.getY() &&
                                et.getX()+20<=wd.getX()+25 && et.getY()<=wd.getY()+20){
                            et.setTouch(true);
                        }
                        break;
                    case DOWN:
                        if(et.getX()>=wd.getX() && et.getY()+30>=wd.getY() &&
                                et.getX()<=wd.getX()+25 && et.getY()+30<=wd.getY()+20){
                            et.setTouch(true);
                        }
                        if(et.getX()+20>=wd.getX() && et.getY()+30>=wd.getY() &&
                                et.getX()+20<=wd.getX()+25 && et.getY()+30<=wd.getY()+20){
                            et.setTouch(true);
                        }
                        break;
                    case LEFT:
                        if(et.getX()>=wd.getX() && et.getY()>=wd.getY() &&
                                et.getX()<=wd.getX()+25 && et.getY()<=wd.getY()+20){
                            et.setTouch(true);
                        }
                        if(et.getX()>=wd.getX() && et.getY()+20>=wd.getY() &&
                                et.getX()<=wd.getX()+25 && et.getY()+20<=wd.getY()+20){
                            et.setTouch(true);
                        }
                        break;
                    case RIGHT:
                        if(et.getX()+30>=wd.getX() && et.getY()>=wd.getY() &&
                                et.getX()+30<=wd.getX()+25 && et.getY()<=wd.getY()+20){
                            et.setTouch(true);
                        }
                        if(et.getX()+30>=wd.getX() && et.getY()+20>=wd.getY() &&
                                et.getX()+30<=wd.getX()+25 && et.getY()+20<=wd.getY()+20){
                            et.setTouch(true);
                        }
                        break;
                    default:
                }
            }
            //红墙
            for(int j = 0; j < redDias.size (); j++){
                RedDia rd = redDias.get ( j );
                switch (et.getEnumDirect()){
                    case UP:
                        if(et.getX()>=rd.getX() && et.getY()>=rd.getY() &&
                                et.getX()<=rd.getX()+25 && et.getY()<=rd.getY()+25){
                            et.setTouch(true);
                        }
                        if(et.getX()+20>=rd.getX() && et.getY()>=rd.getY() &&
                                et.getX()+20<=rd.getX()+25 && et.getY()<=rd.getY()+25){
                            et.setTouch(true);
                        }
                        break;
                    case DOWN:
                        if(et.getX()>=rd.getX() && et.getY()+30>=rd.getY() &&
                                et.getX()<=rd.getX()+25 && et.getY()+30<=rd.getY()+25){
                            et.setTouch(true);
                        }
                        if(et.getX()+20>=rd.getX() && et.getY()+30>=rd.getY() &&
                                et.getX()+20<=rd.getX()+25 && et.getY()+30<=rd.getY()+25){
                            et.setTouch(true);
                        }
                        break;
                    case LEFT:
                        if(et.getX()>=rd.getX() && et.getY()>=rd.getY() &&
                                et.getX()<=rd.getX()+25 && et.getY()<=rd.getY()+25){
                            et.setTouch(true);
                        }
                        if(et.getX()>=rd.getX() && et.getY()+20>=rd.getY() &&
                                et.getX()<=rd.getX()+25 && et.getY()+20<=rd.getY()+25){
                            et.setTouch(true);
                        }
                        break;
                    case RIGHT:
                        if(et.getX()+30>=rd.getX() && et.getY()>=rd.getY() &&
                                et.getX()+30<=rd.getX()+25 && et.getY()<=rd.getY()+25){
                            et.setTouch(true);
                        }
                        if(et.getX()+30>=rd.getX() && et.getY()+20>=rd.getY() &&
                                et.getX()+30<=rd.getX()+25 && et.getY()+20<=rd.getY()+25){
                            et.setTouch(true);
                        }
                        break;
                    default:
                }
            }
        }
    }

    /**
     * 判断我的坦克是否碰到墙
     * @param mt 传入我的坦克
     */
    public void myTouchDia(MyTank mt){
        //白墙
        for(int i = 0; i <  whiteDias.size (); i++){
            WhiteDia wd = whiteDias.get ( i );
            switch (mt.getEnumDirect()){
                case UP:
                    //若坦克方向为向上（从方块下方碰）
                    if(mt.getX ()>=wd.getX() && mt.getY ()>=wd.getY() &&
                            mt.getX ()<=wd.getX()+25 && mt.getY ()<=wd.getY()+20){
                        mt.setY(mt.getY()+5);
                    }
                    if(mt.getX ()+20>=wd.getX() && mt.getY ()>=wd.getY() &&
                            mt.getX ()+20<=wd.getX()+25 && mt.getY ()<=wd.getY()+20){
                        mt.setY(mt.getY()+5);
                    }
                    break;
                case DOWN:
                    //若坦克方向向下（从方块上面碰）
                    if(mt.getX ()>=wd.getX() && mt.getY ()+30>=wd.getY() &&
                            mt.getX ()<=wd.getX()+25 && mt.getY ()+30<=wd.getY()+20){
                        mt.setY(mt.getY()-5);
                    }
                    if(mt.getX ()+20>=wd.getX() && mt.getY ()+30>=wd.getY() &&
                            mt.getX()+20<=wd.getX()+25 && mt.getY ()+30<=wd.getY()+20){
                        mt.setY(mt.getY()-5);
                    }
                    break;
                case LEFT:
                    //若坦克方向向左（从方块右边碰）
                    if(mt.getX ()>=wd.getX() && mt.getY ()>=wd.getY() &&
                            mt.getX ()<=wd.getX()+25 && mt.getY ()<=wd.getY()+20){
                        mt.setX(mt.getX()+5);
                    }
                    if(mt.getX ()>=wd.getX() && mt.getY ()+20>=wd.getY() &&
                            mt.getX ()<=wd.getX()+25 && mt.getY ()+20<=wd.getY()+20){
                        mt.setX(mt.getX()+5);
                    }
                    break;
                case RIGHT:
                    //若坦克方向向右（从方块左边碰）
                    if(mt.getX ()+30>=wd.getX() && mt.getY ()>=wd.getY() &&
                            mt.getX ()+30<=wd.getX()+25 && mt.getY ()<=wd.getY()+20){
                        mt.setX(mt.getX()-5);
                    }
                    if(mt.getX ()+30>=wd.getX() && mt.getY ()+20>=wd.getY() &&
                            mt.getX ()+30<=wd.getX()+25 && mt.getY ()+20<=wd.getY()+20){
                        mt.setX(mt.getX()-5);
                    }
                    break;
                default:
            }
        }
        //红墙
        for(int i = 0; i < redDias.size (); i++){
            RedDia rd = redDias.get ( i );
            switch (mt.getEnumDirect()){
                case UP:
                    if(mt.getX ()>=rd.getX() && mt.getY ()>=rd.getY() &&
                            mt.getX ()<=rd.getX()+25 && mt.getY ()<=rd.getY()+25){
                        mt.setY(mt.getY()+5);
                    }
                    if(mt.getX ()+20>=rd.getX() && mt.getY ()>=rd.getY() &&
                            mt.getX ()+20<=rd.getX()+25 && mt.getY ()<=rd.getY()+25){
                        mt.setY(mt.getY()+5);
                    }
                    break;
                case DOWN:
                    if(mt.getX ()>=rd.getX() && mt.getY ()+30>=rd.getY() &&
                            mt.getX ()<=rd.getX()+25 && mt.getY ()+30<=rd.getY()+25){
                        mt.setY(mt.getY()-5);
                    }
                    if(mt.getX ()+20>=rd.getX() && mt.getY ()+30>=rd.getY() &&
                            mt.getX ()+20<=rd.getX()+25 && mt.getY ()+30<=rd.getY()+25){
                        mt.setY(mt.getY()-5);
                    }
                    break;
                case LEFT:
                    if(mt.getX ()>=rd.getX() && mt.getY ()>=rd.getY() &&
                            mt.getX ()<=rd.getX()+25 && mt.getY ()<=rd.getY()+25){
                        mt.setX(mt.getX()+5);
                    }
                    if(mt.getX ()>=rd.getX() && mt.getY ()+20>=rd.getY() &&
                            mt.getX ()<=rd.getX()+25 && mt.getY ()+20<=rd.getY()+25){
                        mt.setX(mt.getX()+5);
                    }
                    break;
                case RIGHT:
                    if(mt.getX ()+30>=rd.getX() && mt.getY ()>=rd.getY() &&
                            mt.getX ()+30<=rd.getX()+25 && mt.getY ()<=rd.getY()+25){
                        mt.setX(mt.getX()-5);
                    }
                    if(mt.getX ()+30>=rd.getX() && mt.getY ()+20>=rd.getY() &&
                            mt.getX ()+30<=rd.getX()+25 && mt.getY ()+20<=rd.getY()+25){
                        mt.setX(mt.getX()-5);
                    }
                    break;
                default:
            }
        }
    }

    /**
     * 判断我的子弹是否击中地基
     * @param shot 传入子弹
     */
    public void hitTower(Shot shot){
        if(shot.getX() > tower1.getX() && shot.getX() < tower1.getX()+50
                && shot.getY() > tower1.getY() && shot.getY() < tower1.getY()+65){
            shot.setLive(false);
            Recorder.reduceTowerLife1 ();
        }
        if(shot.getX() > tower2.getX() && shot.getX() < tower2.getX()+50
                && shot.getY() > tower2.getY() && shot.getY() < tower2.getY()+65){
            shot.setLive(false);
            Recorder.reduceTowerLife2 ();
        }
    }

    /**
     * 坦克死亡
     * @param shot 传入打中坦克的子弹
     * @param tank 传入坦克（我方或敌方）
     */
    public void tankDie(Shot shot,Tank tank){
        //击中后，子弹、坦克死亡
        shot.setLive(false);
        tank.setLive(false);
        //创建Bomb实例
        Bomb bomb = new Bomb ( tank.getX(),tank.getY() );
        //将创建好的炸弹放入Vector中
        bombs.add ( bomb );
        //移除坦克
        ets.remove ( tank );
    }

    /**
     * 判断我的子弹是否敌人的击中坦克
     * @param shot 传入子弹
     * @param et 传入敌人的坦克
     */
    public void hitTank(Shot shot,EnemyTank et){
        switch (et.getEnumDirect()){
            case UP:
            case DOWN:
                if(shot.getX() > et.getX() && shot.getX() < et.getX()+20
                        && shot.getY() > et.getY() && shot.getY() < et.getY()+30){
                    //敌军坦克死亡
                    tankDie(shot,et);
                    //将敌军坦克移除
                    ets.remove ( et );
                    //坦克数量-1
                    Recorder.reduceNum ();
                }break;
            case LEFT:
            case RIGHT:
                if(shot.getX() > et.getX() && shot.getX() < et.getX()+30
                        && shot.getY() > et.getY() && shot.getY() < et.getY()+20){
                    tankDie(shot,et);
                    ets.remove ( et );
                    //坦克数量-1
                    Recorder.reduceNum ();
                }break;
            default:
        }
    }

    /**
     * 判断敌人的子弹是否击中我的坦克
     * @param shot 传入敌人子弹
     * @param mt 传入我的坦克
     */
    public void hitMe(Shot shot,MyTank mt){
        switch (mt.getEnumDirect()){
            case UP:
            case DOWN:
                if(shot.getX() > mt.getX () && shot.getX() < mt.getX ()+20
                        && shot.getY() > mt.getY () && shot.getY() < mt.getY ()+30){
                    //我方坦克死亡，生命减一
                    tankDie(shot, mt);
                    Recorder.reduceLife();
                    //死亡后回到初始位置
                    mt.setX ( initX );
                    mt.setY ( initY );
                }break;
            case LEFT:
            case RIGHT:
                if(shot.getX() > mt.getX () && shot.getX() < mt.getX ()+30
                        && shot.getY() > mt.getY () && shot.getY() < mt.getY ()+20){
                    tankDie(shot, mt);
                    Recorder.reduceLife();
                    mt.setX ( initX );
                    mt.setY ( initY );
                }break;
            default:
        }
    }

    /**
     * 捡医疗包
     */
    public void getRemedy(MyTank mt,Remedy rmd){
        switch (mt.getEnumDirect()){
            case UP:
                if(mt.getX ()>=rmd.getX() && mt.getY ()>=rmd.getY() &&
                        mt.getX ()<=rmd.getX()+20 && mt.getY ()<=rmd.getY()+20){
                    //补给结束，生命加一
                    supply = true;
                    Recorder.riseLife();
                }
                if(mt.getX ()+20>=rmd.getX() && mt.getY ()>=rmd.getY() &&
                        mt.getX ()+20<=rmd.getX()+20 && mt.getY ()<=rmd.getY()+20){
                    supply = true;
                    Recorder.riseLife();
                }
                break;
            case DOWN:
                if(mt.getX ()>=rmd.getX() && mt.getY ()+30>=rmd.getY() &&
                        mt.getX ()<=rmd.getX()+20 && mt.getY ()+30<=rmd.getY()+20){
                    supply = true;
                    Recorder.riseLife();
                }
                if(mt.getX ()+20>=rmd.getX() && mt.getY ()+30>=rmd.getY() &&
                        mt.getX()+20<=rmd.getX()+20 && mt.getY ()+30<=rmd.getY()+20){
                    supply = true;
                    Recorder.riseLife();
                }
                break;
            case LEFT:
                if(mt.getX ()>=rmd.getX() && mt.getY ()>=rmd.getY() &&
                        mt.getX ()<=rmd.getX()+20 && mt.getY ()<=rmd.getY()+20){
                    supply = true;
                    Recorder.riseLife();
                }
                if(mt.getX ()>=rmd.getX() && mt.getY ()+20>=rmd.getY() &&
                        mt.getX ()<=rmd.getX()+20 && mt.getY ()+20<=rmd.getY()+20){
                    supply = true;
                    Recorder.riseLife();
                }
                break;
            case RIGHT:
                if(mt.getX ()+30>=rmd.getX() && mt.getY ()>=rmd.getY() &&
                        mt.getX ()+30<=rmd.getX()+20 && mt.getY ()<=rmd.getY()+20){
                    mt.setX(mt.getX()-5);
                    supply = true;
                    Recorder.riseLife();
                }
                if(mt.getX ()+30>=rmd.getX() && mt.getY ()+20>=rmd.getY() &&
                        mt.getX ()+30<=rmd.getX()+20 && mt.getY ()+20<=rmd.getY()+20){
                    supply = true;
                    Recorder.riseLife();
                }
                break;
            default:
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //最外层的if和else if：限制我坦克的活动范围
        if (myTank.getX()<720 && myTank.getX()>0 && myTank.getY()<500+2 && myTank.getY()>0) {
            //是否碰到方块
            myTouchDia ( myTank );
            //是否碰到敌人坦克
            myTank.touchEnemy(ets);
            if(e.getKeyCode () == KeyEvent.VK_UP){
                myTank.setEnumDirect( UP );
                myTank.moveUp ();
            }else if(e.getKeyCode () == KeyEvent.VK_DOWN){
                myTank.setEnumDirect( DOWN );
                myTank.moveDown ();
            }else if(e.getKeyCode () == KeyEvent.VK_LEFT) {
                myTank.setEnumDirect( LEFT );
                myTank.moveLeft ();
            }else if(e.getKeyCode () == KeyEvent.VK_RIGHT) {
                myTank.setEnumDirect( RIGHT );
                myTank.moveRight ();
            }
        }else if(myTank.getX () >= 720){
            int x = myTank.getX ();
            x -= myTank.getSpeed();
            myTank.setX ( x );
        }else if(myTank.getY () >= 500+2){
            int y = myTank.getY ();
            y -= myTank.getSpeed();
            myTank.setY ( y );
        }else if(myTank.getX () <= 0){
            int x = myTank.getX ();
            x += myTank.getSpeed();
            myTank.setX ( x );
        }else if(myTank.getY () <= 0){
            int y = myTank.getY ();
            y -= myTank.getSpeed();
            myTank.setY ( y );
        }

        //绘制子弹
        if(e.getKeyCode () == KeyEvent.VK_SPACE){
            if (myTank.shots.size () < 10) {
                myTank.shot ();
            }
        }

        //最后最最重要的：你要重绘它
        repaint ();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {
        //一定要放进循环一定要放进循环一定要放进循环！！！！
        while (true) {
            try {
                Thread.sleep ( 100 );
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }

            //判断敌人坦克是否与墙相撞
            enTouchDia(ets);
            //判断敌人坦克是否与我相撞
            for(int i = 0; i < ets.size(); i++){
                EnemyTank et = ets.get(i);
                et.touch(myTank);
            }

            //判断我的子弹是否击中敌人坦克或墙或地基
            for(int i = 0; i < myTank.shots.size (); i++){
                Shot s1 = myTank.shots.get ( i );
                //判断子弹是否活着
                if(s1.isLive()){
                    //依次取出敌军坦克并让该子弹分别与其进行判断
                    for(int j = 0; j < ets.size (); j++){
                        EnemyTank enemyTank = ets.get ( j );
                        hitTank ( s1, enemyTank );
                    }
                    //判断该子弹是否击中墙
                    hitRedDia ( s1 );
                    hitWhiteDia ( s1 );
                    //判断该子弹是否击中地基
                    hitTower ( s1 );
                }
            }

            //判断敌人的子弹是否击中我的坦克或墙
            for(int i = 0; i < ets.size (); i++){
                EnemyTank enemyTank = ets.get ( i );
                for(int j = 0; j < enemyTank.shots.size (); j++){
                    Shot s2 = enemyTank.shots.get ( j );
                    if(s2.isLive()){
                        hitMe ( s2,myTank );
                        hitRedDia ( s2 );
                        hitWhiteDia ( s2 );
                    }
                }
            }

            //捡医疗包
            if(Recorder.getMyLife() == 1 && !supply){
                getRemedy(myTank,remedy);
            }

            //初始化完成后令time=1
            //按白方块存活情况来画墙
            time = 1;

            repaint ();
        }
    }
}
