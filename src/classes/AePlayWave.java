package classes;

/**
 * 放背景音乐的：
 * 注释瞎写的  理解起来全靠感觉
 */

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

class AePlayWave extends Thread{
    private String filePath;
    boolean isLoop = false;

    public AePlayWave(String filePath){
        this.filePath = filePath;
    }

    public void loop(){
        isLoop = true;
    }

    @Override
    public void run(){
        //do-while(isLoop):循环播放
        do {
            File soundFile = new File ( filePath );

            //这是一个音频流
            AudioInputStream audioInputStream = null;

            try {
                audioInputStream = AudioSystem.getAudioInputStream ( soundFile );
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace ();
            } catch (IOException e) {
                e.printStackTrace ();
            }

            AudioFormat format = audioInputStream.getFormat ();
            //这是一个接口，提供将音频数据写入数据航缓冲区的方法
            SourceDataLine auline = null;
            DataLine.Info info = new DataLine.Info ( SourceDataLine.class,format );

            try {
                //得到它需要的形式
                auline = (SourceDataLine) AudioSystem.getLine(info);
                auline.open(format);
            } catch (LineUnavailableException e) {
                e.printStackTrace ();
            }

            auline.start ();

            //下面是缓冲用的
            int nBytesRead = 0;
            byte[] abData = new byte[512];
            try {
                while (nBytesRead != -1) {
                    nBytesRead = audioInputStream.read(abData, 0, abData.length);
                    if (nBytesRead >= 0) {
                        auline.write ( abData, 0, nBytesRead );
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            } finally {
                //在关闭之前清空数据缓冲
                auline.drain();
                auline.close();
            }
        } while (isLoop);
    }
}
