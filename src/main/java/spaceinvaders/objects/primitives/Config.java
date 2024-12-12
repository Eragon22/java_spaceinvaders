package spaceinvaders.objects.primitives;

import java.awt.event.KeyEvent;
import java.io.FileInputStream;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class Config {
    public static int DEF_WindowW = 1000;
    public static int DEF_WindowH = 700;
    public static int DEF_Speed = 50;
    public static int DEF_ShotSpeed = 1;
    public static int DEF_EnemyHP = 2;
    public static int DEF_EnemyDMG = 1;
    public static int DEF_PlayerHP = 2;
    public static int DEF_PlayerDMG = 1;
    public static int DEF_WallHP = 3;
    public static int DEF_ScoreMULT = 1;
    public static int DEF_ScoreDEF = 1000;

    public static int leftKey = KeyEvent.VK_A;
    public static int rightKey = KeyEvent.VK_D;
    public static int shotKey = KeyEvent.VK_W;

    /**
     * Replaces default values of the configuration with source json file
     * 
     * @param source source file location
     */
    public Config(String source) {
        try {
            JsonReader reader = Json.createReader(new FileInputStream(source));
            JsonObject object = reader.readObject();
            JsonObject controls = object.getJsonObject("Controls");
            JsonObject settings = object.getJsonObject("Settings");
            leftKey = controls.getInt("LeftKey");
            rightKey = controls.getInt("RightKey");
            shotKey = controls.getInt("ShotKey");

            DEF_Speed = settings.getInt("Speed");
            DEF_ShotSpeed = settings.getInt("ShotSpeed");
            DEF_EnemyHP = settings.getInt("EnemyHP");
            DEF_EnemyDMG = settings.getInt("EnemyDMG");
            DEF_PlayerHP = settings.getInt("PlayerHP");
            DEF_PlayerDMG = settings.getInt("PlayerDMG");
            DEF_WallHP = settings.getInt("WallHP");
            DEF_ScoreMULT = settings.getInt("ScoreMULT");
            DEF_ScoreDEF = settings.getInt("ScoreDEF");
        } catch (Exception e) {
            System.out.println("Failed to read config file.");
        }
    }

    public static void setWindowW(int x) {
        DEF_WindowW = x;
    }

    public static int getWindowW() {
        return DEF_WindowW;
    }

    public static void setWindowH(int x) {
        DEF_WindowH = x;
    }

    public static int getWindowH() {
        return DEF_WindowH;
    }

    public static void setSpeed(int x) {
        DEF_Speed = x;
    }

    public static int getSpeed() {
        return DEF_Speed;
    }

    public static void setShotSpeed(int x) {
        DEF_ShotSpeed = x;
    }

    public static int getShotSpeed() {
        return DEF_ShotSpeed;
    }

    public static void setEnemyHP(int x) {
        DEF_EnemyHP = x;
    }

    public static int getEnemyHP() {
        return DEF_EnemyHP;
    }

    public static void setEnemyDMG(int x) {
        DEF_EnemyDMG = x;
    }

    public static int getEnemyDMG() {
        return DEF_EnemyDMG;
    }

    public static void setPlayerHP(int x) {
        DEF_PlayerHP = x;
    }

    public static int getPlayerHP() {
        return DEF_PlayerHP;
    }

    public static void setPlayerDMG(int x) {
        DEF_PlayerDMG = x;
    }

    public static int getPlayerDMG() {
        return DEF_PlayerDMG;
    }

    public static void setWallHP(int x) {
        DEF_WallHP = x;
    }

    public static int getWallHP() {
        return DEF_WallHP;
    }

    public static void setScoreMULT(int x) {
        DEF_ScoreMULT = x;
    }

    public static int getScoreMULT() {
        return DEF_ScoreMULT;
    }

    public static void setScoreDEF(int x) {
        DEF_ScoreDEF = x;
    }

    public static int getScoreDEF() {
        return DEF_ScoreDEF;
    }

    public static void setleftKey(int x) {
        leftKey = x;
    }

    public static int getleftKey() {
        return leftKey;
    }

    public static void setrightKey(int x) {
        rightKey = x;
    }

    public static int getrightKey() {
        return rightKey;
    }

    public static void setshotKey(int x) {
        shotKey = x;
    }

    public static int getshotKey() {
        return shotKey;
    }
}
