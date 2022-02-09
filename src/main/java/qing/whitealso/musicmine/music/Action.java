package qing.whitealso.musicmine.music;

import qing.whitealso.musicmine.player.MusicPlayer;

import java.io.File;
import java.util.*;

/**
 * 关于歌曲播放方式的一点个人想法
 * @author baiye
 * @since 2022/2/7 2:23 下午
 * @see Action2
 **/
@Deprecated
public class Action {

    static int cs = 0;
    static List<String> on = new ArrayList<>();
    static List<String> inter = new ArrayList<>();
    static List<String> songs;
    static Map<String, File> map = new HashMap<>();
    static MusicPlayer player = new MusicPlayer();
//    static Runnable playThread = () -> player.playMp3(getFile(on.get(cs)));
    static Thread thread;
    static List<File> musics = new ArrayList<>();


    public static void openPlayFolder(String path) {
        File folder = new File(path);
        File[] files = folder.listFiles();
        if (files == null) {
            return;
        }
        songs = new ArrayList<>(files.length);
        for (File file : files) {
            if (file.getName().contains("mp3")) {
                songs.add(file.getName());
                musics.add(file);
                map.put(file.getName(), file);
            }
        }
    }

    public static void main(String[] args) {
        openPlayFolder("/Users/whitealso/Music/网易云音乐");
//        String songName = onByOrder();
//        System.out.println("歌曲列表：" + JSON.toJSONString(songs));
//        System.out.println("当前播放列表：" + JSON.toJSONString(on));
//        System.out.println("当前播放歌曲：" + songName);
        PlayThread thread = new PlayThread();
        thread.start();
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String next = scanner.next();
            if (next.equals("n")) {
//                String song = next();
//                System.out.println("当前播放歌曲：" + song);
                thread.next();
            }
//            else if (next.equals("pr")) {
//                String pre = pre();
//                System.out.println("当前播放歌曲：" + pre);
//            }
//            else if (next.equals("order")) {
//                song = onByOrder();
//            } else if (next.equals("random")) {
//                song = onByRandom();
//            }
//            else {
//                System.out.println("添加插播歌曲：" + next);
//                addInterCut(next);
//            }
        }
    }

    private static File getFile(String fileName) {
        return map.get(fileName);
    }

    // 播放歌曲
    private static String on(int idx) {
        cs = idx;
        String songName = on.get(idx);
//        thread = new Thread(playThread);
        System.out.println("当前播放线程：" + thread.getName());
        thread.start();
        return songName;
    }

    // 顺序播放
    private static String onByOrder() {
        if (on.isEmpty()) {
            on = new ArrayList<>(songs);
//            on = Arrays.copyOf(songs, songs.length);
        }
//        String song = on[cs];
//        on = Arrays.copyOf(songs, songs.length);
        String songName = on.get(cs);
        on = new ArrayList<>(songs);
        find(songName);
        return on(cs);
    }

    // 随机播放
    private static String onByRandom() {
        if (on.isEmpty()) {
            String songName = on.get(cs);
            random();
            find(songName);
        } else {
            random();
            int len = songs.size();
            Random random = new Random();
            cs = random.nextInt(len);
        }
        return on(cs);
    }

    // 查找歌曲索引
    private static void find(String songName) {
        for (int i = 0; i < on.size(); i++) {
            if (on.get(i).equals(songName)) {
                cs = i;
            }
        }
//        for (int i = 0; i < on.length; i++) {
//            if (on[i].equals(song)) {
//                cs = i;
//            }
//        }
    }

    // 随机播放列表
    private static void random() {
        Collections.copy(on, songs);
        Collections.shuffle(on);
//        int len = songs.size();
//        Random random = new Random();
//        on = Arrays.copyOf(songs, len);
//        for (int i = 0; i < len; i++) {
//            int idx = random.nextInt(len);
//            String v = on[idx];
//            on[idx] = on[i];
//            on[i] = v;
//        }
    }

    // 下一首播放
    private static void addInterCut(String song) {
        inter.add(song);
    }

    // 插播
    private static String interCut() {
        if (inter.isEmpty()) {
            return null;
        }
        int len = inter.size();
        String song = inter.get(len - 1);
        inter.removeIf(next -> next.equals(song));
        return song;
    }

    private static String next() {
//        String is = interCut();
//        if (is != null) {
//            return is;
//        }
        System.out.println("当前播放线程：" + thread.getName());
        thread.interrupt();
        if (cs < on.size() - 1) {
            cs++;
        } else if (cs == on.size() - 1) {
            cs = 0;
        }
        return on(cs);
    }

    private static String pre() {
        System.out.println("当前播放线程：" + thread.getName());
        thread.interrupt();
        if (cs > 0) {
            cs--;
        } else if (cs == 0) {
            cs = on.size() - 1;
        }
        return on(cs);
    }

    static class PlayThread extends Thread {
        @Override
        public void run() {
//            player.playMp3(musics);
        }

        public void next() {
            player.next();
        }
    }
}
