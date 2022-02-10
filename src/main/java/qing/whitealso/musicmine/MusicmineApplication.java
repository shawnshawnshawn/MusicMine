package qing.whitealso.musicmine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import qing.whitealso.musicmine.music.Action2;
import qing.whitealso.musicmine.player.MusicPlayer;

import java.io.File;
import java.util.List;

@SpringBootApplication
public class MusicmineApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicmineApplication.class, args);
        Action2.start();
    }

}
