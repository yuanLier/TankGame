package classes;

/**
 * 记录类：
 * 保存文件的时候用的
 */

import java.io.*;
import java.util.Vector;

class Recorder {
    private static int enemyNum = 9;
    private static int myLife = 3;
    private static int towerLife1 = 10;
    private static int towerLife2 = 10;
    private static FileWriter fw = null;
    private static FileReader fr = null;
    private static BufferedWriter bw = null;
    private static BufferedReader br = null;
    private Vector<EnemyTank> ets = new Vector<> ( );
    //将保存时的坦克信息存入Info
    static Vector<Info> infos = new Vector<> ( );

    public static int getEnemyNum() {
        return enemyNum;
    }

    public static int getMyLife() {
        return myLife;
    }

    public static int getTowerLife1(){
        return towerLife1;
    }

    public static int getTowerLife2(){
        return towerLife2;
    }

    public void setEts(Vector<EnemyTank> ets){
        this.ets = ets;
    }

    /**
     * 使所记录的坦克数量减少
     */
    public static void reduceNum(){
        enemyNum--;
    }

    /**
     * 使所记录的我的生命值减少
     */
    public static void reduceLife(){
        myLife--;
    }

    /**
     * 使我的生命值增加（捡到医疗包后）
     */
    public static void riseLife(){
        myLife++;
    }

    /**
     * 使所记录的塔1的生命值减少
     */
    public static void reduceTowerLife1(){
        towerLife1--;
    }

    /**
     * 使所记录的塔2的生命值减少
     */
    public static void reduceTowerLife2(){
        towerLife2--;
    }

    /**
     * 将方向转换成数字储存起来
     * @param direct 传入方向
     * @return
     */
    public int getDirection(EnumDirect direct){
        switch (direct){
            case UP:
                return 0;
            case DOWN:
                return 1;
            case LEFT:
                return 2;
            case RIGHT:
                return 3;
            default:
                return 1;
        }
    }

    /**
     * 保存数据并写入文件中，便于后续读取
     */
    public void record(){
        try {
            fw = new FileWriter ( "src/tankGame/records.txt" );
            bw = new BufferedWriter ( fw );

            bw.write ( getEnemyNum ()+"\r\n"+getMyLife ()+"\r\n" );
            bw.write ( getTowerLife1 ()+"\r\n"+getTowerLife2 ()+"\r\n" );
            for(int i = 0; i < ets.size (); i++){
                EnemyTank et = ets.get ( i );
                bw.write ( et.getX()+" "+et.getY()+" "+getDirection(et.getEnumDirect())+"\r\n" );
            }

        } catch (Exception e) {
            e.printStackTrace ();
        } finally {
            try {
                bw.close ();
                fw.close ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
        }
    }

    /**
     * 读取文件
     */
    public Vector<Info> getInfo(){
        try {
            fr = new FileReader ( "src/tankGame/records.txt" );
            br = new BufferedReader ( fr );

            String n = "";
            enemyNum = Integer.parseInt ( br.readLine () );
            myLife = Integer.parseInt ( br.readLine () );
            towerLife1 = Integer.parseInt ( br.readLine () );
            towerLife2 = Integer.parseInt ( br.readLine () );
            while((n = br.readLine ()) != null){
                //split:使其按空格分隔，再分别赋给数组
                String[] xyd = n.split ( " " );
                //将取出来的信息传入
                Info info = new Info(Integer.parseInt ( xyd[0] ),
                        Integer.parseInt ( xyd[1] ),Integer.parseInt ( xyd[2] ));
                infos.add ( info );
            }
        } catch (Exception e) {
            e.printStackTrace ();
        } finally {
            try {
                br.close ();
                fr.close ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
        }

        return infos;
    }
}
