package spaceinvaders;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;
import javax.json.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import spaceinvaders.objects.Bullet;
import spaceinvaders.objects.DynamicObject;
import spaceinvaders.objects.Explosion;
import spaceinvaders.objects.StaticObject;
import spaceinvaders.objects.primitives.GraphicObject;
import spaceinvaders.objects.primitives.Player;
import spaceinvaders.objects.primitives.Vector2D;
import spaceinvaders.objects.primitives.Config;

public class GameWindow extends JFrame implements KeyListener {

    // Keys
    private boolean isLeftKeyPressed = false;
    private boolean isRightKeyPressed = false;
    private boolean isShotKeyPressed = false;

    // Window objects

    private GamePanel currentPanel;
    private Font regular = Font.getFont(Font.SANS_SERIF);
    private Font bold = Font.getFont(Font.SANS_SERIF);
    private GraphicObject leftBoundary;
    private GraphicObject rightBoundary;
    private GraphicObject background;
    private GraphicObject menuicon;

    // Game objects, faster to load here

    private ArrayList<StaticObject> walls = new ArrayList<>();
    private ArrayList<DynamicObject> enemies = new ArrayList<>();
    private ArrayList<Bullet> enemyBullets = new ArrayList<>();
    private ArrayList<Bullet> playerBullets = new ArrayList<>();
    private ArrayList<Explosion> explosions = new ArrayList<>();
    private DynamicObject player;
    private ArrayList<Player> toplist = new ArrayList<>();

    // other

    private State currentState = State.MENU;
    private boolean direction = true; // enemies current direction: true = right, false = left
    private boolean selectedMenu = false; // currently selected menu: false = START, true = SCOREBOARD
    private Random rand = new Random();
    private int score = 1000;
    private String playerName = "";

    public GameWindow() {
        super();
        ImageIcon icon = new ImageIcon("assets/icon.png");
        new Config("Config.json");
        score = Config.DEF_ScoreDEF;
        initAssets();
        initTop();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Space Invaders");
        setIconImage(icon.getImage());

        currentPanel = new GamePanel(Config.DEF_WindowW, Config.DEF_WindowH);
        currentPanel.setPreferredSize(new Dimension(Config.DEF_WindowW, Config.DEF_WindowH));
        add(currentPanel);
        pack();

        addKeyListener(this);
    }

    /**
     * Loads image files before opening the window.
     */
    private void initAssets() {
        try {
            regular = Font
                    .createFont(Font.TRUETYPE_FONT, new File("assets/karma/Karma Suture.otf"))
                    .deriveFont(20f);
            bold = Font.createFont(Font.TRUETYPE_FONT, new File("assets/karma/Karma Future.otf"))
                    .deriveFont(38f);

            background = new GraphicObject(Vector2D.nullVector(),
                    new Vector2D(Config.DEF_WindowW, Config.DEF_WindowH),
                    "assets/background/red.png");
            menuicon = new GraphicObject(
                    new Vector2D(Config.DEF_WindowW / 2 - 160, Config.DEF_WindowH / 2 - 280),
                    new Vector2D(320, 320), "assets/menuicon.gif");

            leftBoundary = new StaticObject(Integer.MAX_VALUE, new Vector2D(-64, 0),
                    new Vector2D(64, Config.DEF_WindowW), "assets/icon.png"); // placeholder image
            rightBoundary = new StaticObject(Integer.MAX_VALUE, new Vector2D(Config.DEF_WindowW, 0),
                    new Vector2D(64, Config.DEF_WindowH), "assets/icon.png"); // placeholder image
        } catch (Exception e) {
            System.out.println("Failed to load assets.");
        }
    }

    /**
     * Initializes ArrayList<Player> toplist from top.json
     */
    private void initTop() {
        try {
            JsonReader reader = Json.createReader(new FileInputStream("top.json"));
            JsonArray top = reader.readArray();

            for (int i = 0; i < top.size(); i++) {
                toplist.add(new Player(top.get(i).asJsonObject().getString("name"),
                        top.get(i).asJsonObject().getInt("score")));
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Failed to read toplist file.");
        }

    }

    /**
     * Writes data from ArrayList<Player> toplist to top.json
     */
    private void writeTop() {
        try {
            if (playerName.length() <= 0) {
                playerName = "Anon";
            }
            Player newtop = new Player(playerName, score);
            for (int i = 0; i < toplist.size(); i++) {
                if (toplist.get(i).getScore() < score) {
                    toplist.add(i, newtop);
                    break;
                }
            }
            if (!toplist.contains(newtop)) {
                toplist.add(new Player(playerName, score));
            }

            JsonWriter writer = Json.createWriter(new FileOutputStream("top.json"));
            JsonArrayBuilder jab = Json.createArrayBuilder();
            for (int i = 0; i < toplist.size(); i++) {
                jab.add(Json.createObjectBuilder().add("name", toplist.get(i).getName())
                        .add("score", toplist.get(i).getScore()).build());
            }
            writer.writeArray(jab.build());
            writer.close();
        } catch (Exception er) {
            System.out.println("Failed to write toplist file.");
        }
    }

    // state machine

    private enum State {
        MENU, GAMEINIT, GAME, SCORE, SCOREBOARD
    }

    /**
     * Sets visibility true for window, handles state machine
     */
    public void open() {
        this.setVisible(true);
        long time = System.currentTimeMillis();
        int i = 0;
        while (i < 1) {
            if (System.currentTimeMillis() - time > 1000 / Config.DEF_Speed) {
                time = System.currentTimeMillis();
                switch (currentState) {
                    case MENU: {
                        menuState();
                    }
                        break;
                    case SCOREBOARD: {
                        scoreBoardState();
                    }
                        break;
                    case GAMEINIT: {
                        gameInit();
                        currentState = State.GAME;
                    }
                        break;
                    case GAME: {
                        gameState();
                        if (enemies.size() <= 0)
                            currentState = State.SCORE;
                    }
                        break;
                    case SCORE: {
                        scoreState();
                    }
                        break;
                }
            }
        }

    }

    // Game states

    /**
     * Repaints current window with menu elements
     */
    public void menuState() {
        currentPanel.clear();
        currentPanel.paintObject(background);
        currentPanel.paintObject(menuicon);
        currentPanel.paintTextCenter(bold, "START", Config.DEF_WindowW, getHeight() / 2 + 80);
        currentPanel.paintTextCenter(bold, "SCOREBOARD", Config.DEF_WindowW, getHeight() / 2 + 150);
        currentPanel.paintTextCenter(regular,
                "CONTROLS: left = " + (char) Config.leftKey + ", right = " + (char) Config.rightKey + ", shoot = "
                        + (char) Config.shotKey,
                Config.DEF_WindowW, getHeight() / 2 + 250);
        currentPanel.paintTextCenter(regular, "Press ENTER to select, press ANY other button to switch menu",
                Config.DEF_WindowW, getHeight() / 2 + 280);

        if (selectedMenu) {
            currentPanel
                    .paintObject(new GraphicObject(new Vector2D(Config.DEF_WindowW / 2 - 120, getHeight() / 2 + 105),
                            new Vector2D(250, 60), "assets/menu.png"));

        } else {
            currentPanel
                    .paintObject(new GraphicObject(new Vector2D(Config.DEF_WindowW / 2 - 120, getHeight() / 2 + 35),
                            new Vector2D(250, 60), "assets/menu.png"));

        }

        currentPanel.repaint();
    }

    /**
     * Initializes objects on window for gameState
     */
    public void gameInit() {
        currentPanel.clear();
        currentPanel.paintObject(background);

        for (int i = 0; i < Config.DEF_WindowW / 250; i++) {
            initWall(new Vector2D(i * 250 + 61, Config.DEF_WindowH - 198));
        }

        initEnemies(new Vector2D(Config.DEF_WindowW * 0.2 / 2, 0));
        initPlayer(new Vector2D(468, Config.DEF_WindowH - 64));

        currentPanel.paintText(regular, "Score: " + score, 10, 590);

        currentPanel.repaint();
    }

    /**
     * Repaints current game state
     */
    public void gameState() {
        currentPanel.clear();
        currentPanel.paintObject(background);

        walls.forEach(x -> currentPanel.paintObject(x));
        moveEnemies();
        movePlayer();
        movePlayerBullets();
        moveEnemyBullets();
        shootPlayer();
        shootEnemy();
        updateHearts();
        enemyHitPlayer();
        enemyHitWall();
        updateExplosions();

        currentPanel.paintText(regular, "Score: " + score, 10, Config.DEF_WindowH - 10);

        score -= 1;

        currentPanel.repaint();
    }

    /**
     * Repaints current window with end of game scoreboard
     */
    public void scoreState() {
        currentPanel.clear();
        currentPanel.paintObject(background);
        currentPanel.paintTextCenter(regular, "YOUR SCORE:", Config.DEF_WindowW, Config.DEF_WindowH - 200);
        currentPanel.paintTextCenter(bold, score + "", Config.DEF_WindowW, Config.DEF_WindowH - 150);
        currentPanel.paintTextCenter(regular, "YOUR NAME:", Config.DEF_WindowW, Config.DEF_WindowH - 100);
        currentPanel.paintTextCenter(bold, playerName, Config.DEF_WindowW, Config.DEF_WindowH - 50);

        currentPanel.paintTextCenter(bold, "TOP: ", Config.DEF_WindowW, 100);
        currentPanel.paintText(regular, "NAME", 200, 150);
        currentPanel.paintTextRight(regular, "SCORE", Config.DEF_WindowW - 200, 150);

        for (int i = 0; i < 10 && toplist.size() > i; i++) {
            currentPanel.paintText(regular, toplist.get(i).getName(), 200, 200 + i * 25);
            currentPanel.paintTextRight(regular, toplist.get(i).getScore() + "", Config.DEF_WindowW - 200,
                    200 + i * 25);
        }

        currentPanel.repaint();
    }

    /**
     * Repaints current window with previous scores and names
     */
    public void scoreBoardState() {
        currentPanel.clear();
        currentPanel.paintObject(background);

        currentPanel.paintTextCenter(bold, "TOP: ", Config.DEF_WindowW, 50);
        currentPanel.paintText(regular, "NAME", 200, 100);
        currentPanel.paintTextRight(regular, "SCORE", Config.DEF_WindowW - 200, 100);

        for (int i = 0; i < (Config.DEF_WindowH - 250) / 25 && toplist.size() > i; i++) {
            currentPanel.paintText(regular, toplist.get(i).getName(), 200, 150 + i * 25);
            currentPanel.paintTextRight(regular, toplist.get(i).getScore() + "", Config.DEF_WindowW - 200,
                    150 + i * 25);
        }

        currentPanel.paintTextCenter(bold, "PRESS ENTER TO GO BACK", Config.DEF_WindowW,
                Config.DEF_WindowH - 50);

        currentPanel.repaint();
    }

    // initialize objects

    /**
     * Initializes wall objects at given parameter
     * 
     * @param at where
     */
    private void initWall(Vector2D at) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if (!((i == 0 && j == 0) || (i == 3 && j == 0) || (i == 1 && j == 2) || (i == 2 && j == 2))) {
                    Vector2D current = new Vector2D(at);
                    current.add(i * 32, j * 32);
                    StaticObject object = new StaticObject(Config.DEF_WallHP, current, new Vector2D(32, 32),
                            "assets/blocks/green.gif");
                    walls.add(object);

                    currentPanel.paintObject(object);
                }
            }
        }
    }

    /**
     * Initializes enemy objects at given parameter
     * 
     * @param at where
     */
    private void initEnemies(Vector2D at) {

        for (int i = 0; i < (Config.DEF_WindowW - Config.DEF_WindowW * 0.2) / 80; i++) {
            for (int j = 0; j < 5; j++) {
                Vector2D current = new Vector2D(at);
                current.add(i * 80, j * 64);
                DynamicObject object;

                if (j >= 3) {
                    object = new DynamicObject(Config.DEF_EnemyHP, Config.DEF_ShotSpeed * 4, Config.DEF_EnemyDMG,
                            current,
                            new Vector2D(64, 64), "assets/enemies/octo.gif");
                } else if (j == 0) {
                    object = new DynamicObject(Config.DEF_EnemyHP * 2, Config.DEF_ShotSpeed * 2,
                            Config.DEF_EnemyDMG * 2, current,
                            new Vector2D(64, 64), "assets/enemies/ufo.gif");
                } else {
                    object = new DynamicObject(Config.DEF_EnemyHP * 2, Config.DEF_ShotSpeed, Config.DEF_EnemyDMG,
                            current,
                            new Vector2D(64, 64), "assets/enemies/spidey.gif");
                }
                enemies.add(object);

                currentPanel.paintObject(object);
            }
        }
    }

    /**
     * Initializes player object at given parameter
     * 
     * @param at where
     */
    private void initPlayer(Vector2D at) {
        DynamicObject object = new DynamicObject(Config.DEF_PlayerHP, Config.DEF_ShotSpeed, Config.DEF_PlayerDMG, at,
                new Vector2D(64, 32), "assets/player/green.png");
        player = object;

        currentPanel.paintObject(object);
    }

    // update objects

    /**
     * Updates the state of explosion effects
     */
    private void updateExplosions() {
        for (Explosion explosion : explosions) {
            if (explosion.getSpawnTime() >= System.currentTimeMillis() - 10000 / Config.DEF_Speed) {
                currentPanel.paintObject(explosion);
            } else {
                explosions.remove(explosion);
                break;
            }
        }
    }

    /**
     * Updates the number of current player health displayed
     */
    private void updateHearts() {
        for (int i = 0; i < player.getCurrentHP(); i++) {
            GraphicObject object = new GraphicObject(
                    new Vector2D(Config.DEF_WindowW - (30 + i * 25), Config.DEF_WindowH - 30),
                    new Vector2D(20, 20),
                    "assets/player/heart.png");
            currentPanel.paintObject(object);
        }
    }

    /**
     * Moves enemies' positions for the next frame
     */
    private void moveEnemies() {
        double diri = 1;
        if (!direction)
            diri = -1;
        final double dirif = diri;

        enemies.forEach(x -> x.getPos().add(dirif, 0));

        // test for flip
        for (GraphicObject enemy : enemies) {
            if (enemy.collides(rightBoundary) || enemy.collides(leftBoundary)) {
                enemies.forEach(x -> x.getPos().add(0,
                        Math.round(((double) Config.DEF_WindowH) / 200)));
                direction = !direction;
                break;
            }
        }

        enemies.forEach(x -> currentPanel.paintObject(x));
    }

    /**
     * Moves player's position for the next frame
     */
    private void movePlayer() {

        if (!player.collides(rightBoundary) && isRightKeyPressed)
            player.getPos().add(2, 0);
        if (!player.collides(leftBoundary) && isLeftKeyPressed)
            player.getPos().add(-2, 0);

        currentPanel.paintObject(player);
    }

    /**
     * Moves player bullets' positions for the next frame
     */
    private void movePlayerBullets() {
        for (Bullet bullet : playerBullets) {
            if (hitWall(bullet) || hitEnemy(bullet))
                break;
        }

        playerBullets.forEach(x -> x.getPos().add(x.getDir()));
        playerBullets.forEach(x -> currentPanel.paintObject(x));
    }

    /**
     * Moves enemy bullets' positions for the next frame
     */
    private void moveEnemyBullets() {
        for (Bullet bullet : enemyBullets) {
            if (hitPlayer(bullet) || hitWall(bullet))
                break;
        }

        enemyBullets.forEach(x -> x.getPos().add(x.getDir()));
        enemyBullets.forEach(x -> currentPanel.paintObject(x));
    }

    /**
     * Generates bullets at player's position, handles cooldown of shooting
     */
    private void shootPlayer() {
        boolean oncooldown = false;
        for (Bullet bullet : playerBullets) {
            if (bullet.getSpawnTime() + 1000 / player.getShotSpeed() > System.currentTimeMillis())
                oncooldown = true;
        }
        if (!oncooldown && isShotKeyPressed) {
            playerBullets.add(player.shoot(new Vector2D(0, -1), "assets/bullets/playerbullet.png"));
        }
    }

    /**
     * Generates bullets at the enemies' positions, handles cooldown of shooting
     */
    private void shootEnemy() {
        for (DynamicObject enemy : enemies) {
            if (rand.nextInt(0, 8000 / enemy.getShotSpeed()) == 1) {
                enemyBullets.add(enemy.shoot(new Vector2D(0, 1), "assets/bullets/enemybullet.png"));
            }
        }
    }

    // Hit test

    /**
     * Detects if any enemy overlaps with a wall, if so destroys it
     */
    private void enemyHitWall() {
        for (DynamicObject enemy : enemies)
            for (StaticObject wall : walls) {
                if (enemy.collides(wall)) {
                    walls.remove(wall);
                    Explosion boom = new Explosion(wall.getPos(), wall.getSize(), "assets/blocks/red.png");
                    explosions.add(boom);
                    break;
                }
            }
    }

    /**
     * Detects if any enemy overlaps with the player, if so moves to score state
     */
    private void enemyHitPlayer() {
        for (DynamicObject enemy : enemies)
            if (enemy.collides(player))
                currentState = State.SCORE;
    }

    /**
     * Tests if bullet given as parameter hits any of the walls
     * 
     * @param bullet
     * @return returns true if yes
     */
    private boolean hitWall(Bullet bullet) {
        for (StaticObject wall : walls) {
            if (wall.collides(bullet)) {
                int dmg = 0;
                if (enemyBullets.contains(bullet)) {
                    dmg = Config.DEF_EnemyDMG;
                    enemyBullets.remove(bullet);
                }

                if (playerBullets.contains(bullet)) {
                    dmg = Config.DEF_PlayerDMG;
                    playerBullets.remove(bullet);
                }

                wall.setCurrentHP(wall.getCurrentHP() - dmg);

                Explosion boom = new Explosion(wall.getPos(), wall.getSize(), "assets/blocks/red.png");
                explosions.add(boom);

                if (wall.getCurrentHP() <= 0) {
                    walls.remove(wall);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Tests if bullet given as parameter hits player
     * 
     * @param bullet
     * @return returns true if yes
     */
    private boolean hitPlayer(Bullet bullet) {
        if (player.collides(bullet)) {
            enemyBullets.remove(bullet);
            player.setCurrentHP(player.getCurrentHP() - Config.DEF_EnemyDMG);

            Explosion boom = new Explosion(player.getPos(), player.getSize(), "assets/player/red.png");
            explosions.add(boom);

            if (player.getCurrentHP() <= 0) {
                currentState = State.SCORE;
            }

            return true;
        }
        return false;
    }

    /**
     * Tests if bullet given as parameter hits any of the enemies
     * 
     * @param bullet
     * @return returns true if yes
     */
    private boolean hitEnemy(Bullet bullet) {
        for (DynamicObject enemy : enemies) {
            if (enemy.collides(bullet)) {
                playerBullets.remove(bullet);
                enemy.setCurrentHP(enemy.getCurrentHP() - Config.DEF_PlayerDMG);

                Explosion boom = new Explosion(enemy.getPos(), enemy.getSize(), "assets/enemies/red.png");
                explosions.add(boom);

                if (enemy.getCurrentHP() <= 0) {
                    enemies.remove(enemy);
                    score += 1000 * Config.DEF_ScoreMULT;
                }

                return true;
            }
        }
        return false;
    }

    // keyListener

    @Override
    public void keyPressed(KeyEvent e) {
        boolean isLeftKeyHeld = isLeftKeyPressed;
        boolean isRightKeyHeld = isRightKeyPressed;
        boolean isShotKeyHeld = isShotKeyPressed;
        if (e.getKeyCode() == Config.leftKey) {
            isLeftKeyPressed = true;
        }
        if (e.getKeyCode() == Config.rightKey) {
            isRightKeyPressed = true;
        }
        if (e.getKeyCode() == Config.shotKey) {
            isShotKeyPressed = true;
        }

        if (currentState == State.SCORE)
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                writeTop();
                this.dispose();
                System.exit(0);
            }

        if (currentState == State.MENU)
            if (e.getKeyCode() == KeyEvent.VK_ENTER)
                if (!selectedMenu)
                    currentState = State.GAMEINIT;
                else
                    currentState = State.SCOREBOARD;
            else
                selectedMenu = !selectedMenu;
        else if (currentState == State.SCOREBOARD)
            if (e.getKeyCode() == KeyEvent.VK_ENTER)
                currentState = State.MENU;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        boolean isLeftKeyHeld = isLeftKeyPressed;
        boolean isRightKeyHeld = isRightKeyPressed;
        boolean isShotKeyHeld = isShotKeyPressed;
        if (e.getKeyCode() == Config.leftKey) {
            isLeftKeyPressed = false;
        }
        if (e.getKeyCode() == Config.rightKey) {
            isRightKeyPressed = false;
        }
        if (e.getKeyCode() == Config.shotKey) {
            isShotKeyPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        String accepted = "0123456789AÁBCDEÉFGHIÍJKLMNOÓÖŐPQRSTUÚÜŰVWXYZaábcdeéfghiíjklmnoóöőpqrstuúüűvwxyz _-.";
        if (currentState == State.SCORE) {
            if (e.getKeyChar() == '\b') {
                if (playerName.length() > 0)
                    playerName = playerName.substring(0, playerName.length() - 1);
            } else if (accepted.indexOf(e.getKeyChar()) >= 0 && playerName.length() <= 40)
                playerName += e.getKeyChar();
        }
    }
}
