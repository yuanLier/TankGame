package tankgame.classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TankGame extends JFrame implements ActionListener{
    /**
     * 定义控件
     */
    StartPanel sp;
    MyPanel mp = null;
    JMenuBar jmb;
    JMenu jm1,jm2,jm3;
    JMenuItem jmi11,jmi12,jmi13,jmi14,jmi15;
    JMenuItem jmi21;
    JMenuItem jmi31;

    public static void main(String[] args) {
        //先放个音乐
        AePlayWave apw = new AePlayWave ( "src/tankgame/music/begin.wav" );
        apw.start ();
        //然后就可以开始啦
        TankGame tankGame = new TankGame ();
    }

    public TankGame(){
        //创建菜单及菜单选项
        jmb = new JMenuBar ();
        jm1 = new JMenu ( "游戏(G)" );
        jm2 = new JMenu ( "设置(T)" );
        jm3 = new JMenu ( "帮助(H)" );
        jmi11 = new JMenuItem ( "开始新游戏(N)" );
//        jmi12 = new JMenuItem ( "暂停游戏(P)" );
        jmi13 = new JMenuItem ( "继续上一局(C)" );
        jmi14 = new JMenuItem ( "存盘退出(S)" );
        jmi15 = new JMenuItem ( "退出(E)" );
        jmi21 = new JMenuItem ( "不要点我不要点我" );
        jmi31 = new JMenuItem ( "游戏规则(R)" );
        //设置快捷方式
        jm1.setMnemonic ( 'G' );
        jm2.setMnemonic ( 'T' );
        jm3.setMnemonic ( 'H' );
        jmi11.setMnemonic ( 'N' );
//        jmi12.setMnemonic ( 'P' );
        jmi13.setMnemonic ( 'C' );
        jmi14.setMnemonic ( 'S' );
        jmi15.setMnemonic ( 'E' );
        jmi31.setMnemonic ( 'R' );
        //注册监听
        jmi11.addActionListener ( this );
        jmi11.setActionCommand ( "newGame" );
//        jmi12.addActionListener ( this );
//        jmi12.setActionCommand ( "stopGame" );
        jmi13.addActionListener ( this );
        jmi13.setActionCommand ( "continueGame" );
        jmi14.addActionListener ( this );
        jmi14.setActionCommand ( "saveGame" );
        jmi15.addActionListener ( this );
        jmi15.setActionCommand ( "exit" );
        jmi21.addActionListener ( this );
        jmi21.setActionCommand ( "setting" );
        jmi31.addActionListener ( this );
        jmi31.setActionCommand ( "rules" );
        //添加菜单
        jm1.add ( jmi11 );
//        jm1.add ( jmi12 );
        jm1.add ( jmi13 );
        jm1.add ( jmi14 );
        jm1.add ( jmi15 );
        jm2.add ( jmi21 );
        jm3.add ( jmi31 );
        jmb.add ( jm1 );
        jmb.add ( jm2 );
        jmb.add ( jm3 );
        this.add ( jmb,BorderLayout.NORTH );

        //创建控件
        sp = new StartPanel ( );

        //调一下面板的颜色
        sp.setBackground ( Color.black );

        //添加控件
        this.add ( sp );


        //启动面板的线程
        Thread startThread = new Thread ( sp );
        startThread.start ();

        //设置窗口属性
        this.setTitle ( "Tank_demo" );
        this.setSize ( 1000, 600 );
        this.setLocation ( 300, 200 );
        this.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
        //禁止调整窗口大小
        this.setResizable ( false );

        //显示窗体
        this.setVisible ( true );
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand ().equals ( "newGame" )){
            //游戏音效
            AePlayWave apw = new AePlayWave ( "src/tankgame/music/game.wav" );
            apw.start ();
            apw.loop();
            //创建新的游戏面板
            mp = new MyPanel ( "newGame" );
            mp.setBackground ( Color.black );
            //就把当前面板关掉，打开游戏界面的面板
            this.remove ( sp );
            this.add ( mp );
            //然后启动游戏面板的线程并且注册监听
            Thread panelThread = new Thread ( mp );
            panelThread.start ();
            this.addKeyListener ( mp );
            //最后很重要的是，一定要让它显示一下
            this.setVisible ( true );
        }else if(e.getActionCommand ().equals ( "stopGame" )){
            // todo
        }else if(e.getActionCommand ().equals ( "continueGame" )){
            //游戏音效
            AePlayWave apw = new AePlayWave ( "src/tankgame/music/game.wav" );
            apw.start ();
            apw.loop();
            //创建新的游戏面板
            mp = new MyPanel ("continueGame");
            mp.setBackground ( Color.black );
            //关掉旧面板，启动新面板
            this.remove ( sp );
            this.add ( mp );
            //启动线程
            Thread continueThread = new Thread ( mp );
            continueThread.start ();
            //注册监听，显示
            this.addKeyListener ( mp );
            this.setVisible ( true );
        }else if(e.getActionCommand ().equals ( "saveGame" )){
            Recorder rd = new Recorder ();
            rd.setEts ( mp.ets );
            rd.record ();
            System.exit(0);
        }else if(e.getActionCommand ().equals ( "exit" )){
            System.exit ( 0 );
        }else if(e.getActionCommand ().equals ( "setting" )){
            JOptionPane.showMessageDialog ( this,"暂时没有什么可以设置的啦~",
                    "都说了不要点了嘛",JOptionPane.INFORMATION_MESSAGE );
        }else if(e.getActionCommand ().equals ( "rules" )){
            JOptionPane.showMessageDialog ( this,"上下左右移动，空格发射，\r\n白墙打得动红墙打不动，" +
                    "\r\n敌方坦克全灭或塔全被推了就  \r\n胜利了哟=w=", "游戏规则游戏规则",JOptionPane.INFORMATION_MESSAGE );
        }
    }
}
