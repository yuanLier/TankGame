package tankgame.classes;

/**
 * 开始面板：
 * 诱惑你去耍游戏的
 */

import javax.swing.*;
import java.awt.*;

class StartPanel extends JPanel implements Runnable {
    int time = 0;

    @Override
    public void paint(Graphics g){
        //调用父类完成初始化
        super.paint ( g );

        g.setColor ( Color.cyan );
        g.setFont ( new Font ( "隶书",Font.BOLD,70 ) );
        g.drawLine ( 0,420,700,550 );
        g.drawLine ( 0,100,120,550 );
        g.drawLine ( 860,0,1000,460 );
        g.drawLine ( 310,0,1000,110 );

        if (time % 2 == 0 && time != 4) {
            //然后来画个字,要能闪的那种=w=
            g.drawString ( "Start Game ？ ",250,290 );
        }else if(time == 4){
            g.drawString ( "Start Game ？ ",190,240 );
            g.drawString ( "—— Let's Go！ ",290,350 );
        }
    }

    @Override
    public void run() {
        while (true){

            try {
                Thread.sleep ( 500 );
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }

            //闪个两三下就算了哈
            if (time < 4) {
                time++;
            }

            repaint ();
        }

    }
}
