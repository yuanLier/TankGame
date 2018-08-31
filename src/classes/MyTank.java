package classes;

import java.util.Vector;

/**
 * 我的坦克：
 * 上下左右控制移动，空格键发射子弹
 */

class MyTank extends Tank {
    public MyTank(int x, int y) {
        super(x, y);
    }

    /**
     * 检查是否碰到敌军坦克
     * @param enemyTanks 传入敌军坦克群
     */
    public void touchEnemy(Vector<EnemyTank> enemyTanks){
        for(int i = 0; i < enemyTanks.size(); i++){
            EnemyTank et = enemyTanks.get(i);
            touch(et);
        }
    }
}
