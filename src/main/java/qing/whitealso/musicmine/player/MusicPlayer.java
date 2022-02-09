package qing.whitealso.musicmine.player;

import com.alibaba.fastjson.JSON;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;

import javax.sound.sampled.*;
import java.io.File;
import java.util.*;

/**
 * @author baiye
 * @since 2022/2/9 1:47 下午
 **/
public class MusicPlayer {

    private List<String> on;
    private List<String> songs;
    private Map<String, File> map = new HashMap<>();
    private boolean flag;
    private int cs = 0;
    private int end = 0;
    private boolean pre = false;

    public void play(List<File> files) {
        songs = new ArrayList<>(files.size());
        for (File file : files) {
            songs.add(file.getName());
            map.put(file.getName(), file);
        }
        on = new ArrayList<>(songs);
        onByOrder();
    }

    private void play() {
        if (end != 1) {
            try {
                playMp3(map.get(on.get(cs)));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    // 顺序播放
    public void onByOrder() {
        if (on.isEmpty()) {
            on = new ArrayList<>(songs);
        }
        String songName = on.get(cs);
        find(songName);
        on = new ArrayList<>(songs);
        System.out.println("顺序播放列表：" + JSON.toJSONString(on));
        play();
    }

    // 随机播放
    public void onByRandom() {
        if (!on.isEmpty()) {
            String songName = on.get(cs);
            random();
            find(songName);
        } else {
            random();
            int len = songs.size();
            Random random = new Random();
            cs = random.nextInt(len);
        }
        System.out.println("随机播放列表：" + JSON.toJSONString(on));
        play();
    }

    // 随机播放列表
    private void random() {
        on = new ArrayList<>(songs);
        Collections.shuffle(on);
    }

    // 查找歌曲索引
    private void find(String songName) {
        for (int i = 0; i < on.size(); i++) {
            if (on.get(i).equals(songName)) {
                cs = i;
            }
        }
    }

    private void last() {
        System.out.println("上一首");
        if (cs > 0) {
            cs = cs - 1;
        } else if (cs == 0) {
            cs = on.size() - 1;
        }
        play();
    }

    // 中断播放并标记为切换为播放上一首歌
    public void pre() {
        pre = true;
        stop();
    }

    public void next() {
        System.out.println("下一首");
        int len = on.size();
        if (cs < len - 1) {
            cs++;
        } else if (cs == len - 1) {
            cs = 0;
        }
        play();
    }

    // 中断当前播放歌曲
    public void stop() {
        flag = false;
    }

    public void playMp3(File file) throws Throwable {
        flag = true;
        pre = false;
        end = 1;
        System.out.println("当前播放歌曲：" + file.getName());
        MpegAudioFileReader reader = new MpegAudioFileReader();
        AudioInputStream audioInputStream = reader.getAudioInputStream(file);
        AudioFormat audioFormat = audioInputStream.getFormat();
        AudioFormat pcmFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(),
                16, audioFormat.getChannels(), audioFormat.getChannels() * 2, audioFormat.getSampleRate(), false);
        audioInputStream = AudioSystem.getAudioInputStream(pcmFormat, audioInputStream);
        AudioFormat target = audioInputStream.getFormat();
        DataLine.Info dInfo = new DataLine.Info(SourceDataLine.class, target, AudioSystem.NOT_SPECIFIED);
        int len;
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(dInfo);
        line.open(target);
        line.start();
        byte[] buffer = new byte[1024];
        while ((len = audioInputStream.read(buffer)) > 0 && flag) {
            line.write(buffer, 0, len);
        }
        end = 2;
        line.drain();
        line.stop();
        line.close();
        audioInputStream.close();
        if (!pre) {
            next();
        } else {
            last();
        }
    }
}
