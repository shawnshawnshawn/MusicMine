package qing.whitealso.musicmine.music;

import qing.whitealso.musicmine.player.MusicPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author baiye
 * @since 2022/2/9 4:51 下午
 **/
public class Action2 {

    static MusicPlayer player = new MusicPlayer();
    static List<File> files;

    public static void main(String[] args) {
        files = openPlayFolder("/Users/whitealso/Music/网易云音乐");
        PlayThread thread = new PlayThread();
        thread.start();
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String next = scanner.next();
            switch (next) {
                case "n":
                    thread.next();
                    break;
                case "pr":
                    thread.pre();
                    break;
                case "order":
                    thread.order();
                    break;
                case "random":
                    thread.random();
                    break;
            }
        }
    }

    public static List<File> openPlayFolder(String path) {
        List<File> songs = new ArrayList<>();
        File folder = new File(path);
        File[] files = folder.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.getName().contains("mp3")) {
                songs.add(file);
            }
        }
        return songs;
    }

    static class PlayThread extends Thread {
        @Override
        public void run() {
            player.play(files);
        }

        public void next() {
            player.stop();
        }

        public void pre() {
            player.pre();
        }

        public void order() {
            player.onByOrder();
        }

        public void random() {
            player.onByRandom();
        }
    }
}
