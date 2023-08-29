import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JComponent;

//Jarod Esareesingh 
//816026811
public class GamePanel extends JComponent {

    private List<Missile> missiles;
    private List<Alien> aliens;
    private List<ExplosionEffect> explosionEffects;
    private Graphics2D graphics;
    private BufferedImage buffImage;
    private Image backgroundImage;
    private int w;
    private int h;
    private Thread gameThread;
    private boolean startGameBool = true;
    private KeyManager key;
    private int missileMS;
    private final int frames = 60;
    private final int target = 1000000000 / frames;
    private SoundManger gameSounds;
    private Ship ship;

    private int score = 0;

    public void start() {
        // Get width and height of the window
        w = getWidth();
        h = getHeight();

        // Load background image and create image buffer
        backgroundImage = ImageManager.loadImage("images/Background.jpg");
        buffImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        graphics = buffImage.createGraphics();

        // Start game loop thread
        gameThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (startGameBool) {
                    long startTime = System.nanoTime();

                    // Draw game elements
                    drawBackground();
                    drawGame();

                    // Render to screen
                    renderGame();

                    // Limit frame rate
                    long time = System.nanoTime() - startTime;
                    if (time < target) {
                        long sleepTime = (target - time) / 1000000;
                        sleep(sleepTime);
                    }
                }
            }
        });

        // Initialize game objects
        initializeGameObjects();

        // Initialize keyboard input
        initializeKeys();

        // Initialize missiles
        initializeMissiles();

        // Start background music
        gameSounds.playBackground();

        // Start game loop thread
        gameThread.start();
    }

    private void addMissile() {
        // Create 3 aliens with random Y positions
        Random ran = new Random();
        int locY = ran.nextInt(h - 50) + 25;
        Alien alien = new Alien();
        alien.changeShipLoc(0, locY);
        alien.modifyShipAngle(0);
        aliens.add(alien);

        int locY2 = ran.nextInt(h - 50) + 25;
        Alien alien1 = new Alien();
        alien1.changeShipLoc(w, locY2);
        alien1.modifyShipAngle(180);
        aliens.add(alien1);

        int locY3 = ran.nextInt(h - 50) + 25;
        Alien alien3 = new Alien();
        alien3.changeShipLoc(w, locY3);
        alien3.modifyShipAngle(180);
        aliens.add(alien3);
    }

    private void initializeGameObjects() {
        // Initialize game objects
        gameSounds = new SoundManger();
        ship = new Ship();
        ship.changeShipLoc(150, 150);
        aliens = new ArrayList<>();
        explosionEffects = new ArrayList<>();

        // Start thread to spawn aliens periodically
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (startGameBool) {
                    addMissile();
                    sleep(2000);
                }
            }
        }).start();
    }

    public void reset() {
        score = 0;
        aliens.clear();
        missiles.clear();
        ship.changeShipLoc(150, 150);
        ship.resetGame();
    }

    public void initializeKeys() {
        key = new KeyManager();
        requestFocus();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                handleKeyRelease(e.getKeyCode());
            }
        });

        new Thread(() -> {
            float ang = 0.5f;
            while (startGameBool) {
                if (ship.isIntact()) {
                    float angle = ship.getAngle();
                    handleLeftRightKeys(ang, angle);
                    shootGunAndMissiles();
                    handleSpaceKey();
                    ship.updateShipLoc();
                } else {
                    handleEnterKey();
                }

                updateMissiles();
                sleep(5);
            }
        }).start();
    }

    private void handleKeyPress(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_A:
                key.setLeft(true);
                break;
            case KeyEvent.VK_D:
                key.setRight(true);
                break;
            case KeyEvent.VK_SPACE:
                key.setSpace(true);
                break;
            case KeyEvent.VK_J:
                key.setJ(true);
                break;
            case KeyEvent.VK_K:
                key.setK(true);
                break;
            case KeyEvent.VK_ENTER:
                key.setEnter(true);
                break;
            default:
                break;
        }
    }

    private void handleKeyRelease(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_A:
                key.setLeft(false);
                break;
            case KeyEvent.VK_D:
                key.setRight(false);
                break;
            case KeyEvent.VK_SPACE:
                key.setSpace(false);
                break;
            case KeyEvent.VK_J:
                key.setJ(false);
                break;
            case KeyEvent.VK_K:
                key.setK(false);
                break;
            case KeyEvent.VK_ENTER:
                key.setEnter(false);
                break;
            default:
                break;
        }
    }

    private void handleLeftRightKeys(float ang, float angle) {
        if (key.leftCheck()) {
            angle -= ang;
        }
        if (key.rightCheck()) {
            angle += ang;
        }
        ship.modifyShipAngle(angle);
    }

    private void shootGunAndMissiles() {
        if (key.jCheck() || key.kCheck()) {
            handleBulletShot();
        } else {
            missileMS = 0;
        }
    }

    private void handleBulletShot() {
        if (missileMS == 0) {
            int bulletSpeed = key.jCheck() ? 5 : 20;
            missiles.add(0, new Missile(ship.getX(), ship.getY(), ship.getAngle(), bulletSpeed, 3f));
            gameSounds.soundShoot();
        }
        missileMS++;
        if (missileMS == 15) {
            missileMS = 0;
        }
    }

    private void handleSpaceKey() {
        if (key.spaceCheck()) {
            ship.boost();
        } else {
            ship.speedDown();
        }
    }

    private void handleEnterKey() {
        if (key.enterCheck()) {
            reset();
        }
    }

    private void updateMissiles() {
        for (int i = 0; i < aliens.size(); i++) {
            Alien alien = aliens.get(i);
            if (alien != null) {
                alien.updateAlien();
                if (!alien.checkAlien(w, h)) {
                    aliens.remove(alien);
                } else {
                    if (ship.isIntact()) {
                        checkShip(alien);
                    }
                }
            }
        }
    }

    private void initializeMissiles() {
        missiles = new ArrayList<>();

        new Thread(() -> {
            while (startGameBool) {
                for (int i = 0; i < missiles.size(); i++) {
                    Missile missile = missiles.get(i);

                    if (missile != null) {
                        missile.updateMissile();
                        checkMissiles(missile);

                        if (!missile.checkMissile(w, h)) {
                            missiles.remove(missile);
                        }
                    } else {
                        missiles.remove(missile);
                    }
                }

                for (int i = 0; i < explosionEffects.size(); i++) {
                    ExplosionEffect explosionEffect1 = explosionEffects.get(i);

                    if (explosionEffect1 != null) {
                        explosionEffect1.updateExplosion();

                        if (!explosionEffect1.checkExplosion()) {
                            explosionEffects.remove(explosionEffect1);
                        }
                    } else {
                        explosionEffects.remove(explosionEffect1);
                    }
                }

                sleep(1);
            }
        }).start();
    }

    private void checkMissiles(Missile missile) {
        for (int i = 0; i < aliens.size(); i++) {
            Alien alien = aliens.get(i);
            if (alien != null) {
                Area area = new Area(missile.getMissileShape());
                area.intersect(alien.getShipShape());
                if (!area.isEmpty()) {
                    // create explosion effect at missile position
                    explosionEffects
                            .add(new ExplosionEffect(missile.returnCenterX(), missile.returnCenterY(), 5, 10, 100, 0.2f,
                                    new Color(255, 70, 70)));

                    // update alien health
                    if (!alien.updateHealth(missile.returnSize())) {
                        // alien destroyed
                        score++;
                        aliens.remove(alien);
                        gameSounds.soundDestroy();

                        // create bigger explosion effect at alien position
                        double x = alien.returnX() + Alien.alienRocketSize / 2;
                        double y = alien.returnY() + Alien.alienRocketSize / 2;
                        explosionEffects.add(new ExplosionEffect(x, y, 10, 20, 200, 0.5f, new Color(255, 200, 0)));
                        explosionEffects.add(new ExplosionEffect(x, y, 5, 40, 150, 0.4f, new Color(255, 70, 70)));
                        explosionEffects.add(new ExplosionEffect(x, y, 3, 60, 100, 0.3f, new Color(255, 255, 255)));
                    } else {
                        // alien hit
                        gameSounds.soundHit();

                        // create smaller explosion effect at alien position
                        double x = alien.returnX() + Alien.alienRocketSize / 2;
                        double y = alien.returnY() + Alien.alienRocketSize / 2;
                        explosionEffects.add(new ExplosionEffect(x, y, 5, 5, 50, 0.1f, new Color(255, 200, 0)));
                    }

                    // remove missile from list
                    missiles.remove(missile);
                }
            }
        }
    }

    private void checkShip(Alien alien) {
        if (alien == null) {
            return;
        }

        Area shipArea = new Area(ship.getShipShape());
        Area alienArea = new Area(alien.getShipShape());
        shipArea.intersect(alienArea);

        if (shipArea.isEmpty()) {
            return;
        }

        double alienHealth = alien.getHealth();
        if (!alien.updateHealth(ship.getHealth())) {
            // alien destroyed
            aliens.remove(alien);
            gameSounds.soundDestroy();
            double x = alien.returnX() + Alien.alienRocketSize / 2;
            double y = alien.returnY() + Alien.alienRocketSize / 2;
            explosionEffects.add(new ExplosionEffect(x, y, 5, 5, 75, 0.05f, new Color(255, 165, 0))); // orange
            explosionEffects.add(new ExplosionEffect(x, y, 5, 5, 75, 0.1f, new Color(255, 255, 0))); // yellow
            explosionEffects.add(new ExplosionEffect(x, y, 10, 10, 100, 0.3f, new Color(255, 69, 0))); // deep orange
            explosionEffects.add(new ExplosionEffect(x, y, 10, 5, 100, 0.5f, new Color(255, 0, 0))); // red
            explosionEffects.add(new ExplosionEffect(x, y, 10, 5, 150, 0.2f, new Color(255, 255, 255))); // white
        } else {
            gameSounds.soundHit();
        }

        if (!ship.updateHealth(alienHealth)) {
            // ship destroyed
            ship.setAlive(false);
            gameSounds.stopBackground();
            gameSounds.soundPlayerDeath();
            double x = ship.getX() + Ship.shipSize / 2;
            double y = ship.getY() + Ship.shipSize / 2;
            explosionEffects.add(new ExplosionEffect(x, y, 5, 5, 75, 0.05f, new Color(255, 165, 0))); // orange
            explosionEffects.add(new ExplosionEffect(x, y, 5, 5, 75, 0.1f, new Color(255, 255, 0))); // yellow
            explosionEffects.add(new ExplosionEffect(x, y, 10, 10, 100, 0.3f, new Color(255, 69, 0))); // deep orange
            explosionEffects.add(new ExplosionEffect(x, y, 10, 5, 100, 0.5f, new Color(255, 0, 0))); // red
            explosionEffects.add(new ExplosionEffect(x, y, 10, 5, 150, 0.2f, new Color(255, 255, 255))); // white
        }
    }

    private void drawBackground() {
        Graphics2D imageContext = (Graphics2D) buffImage.getGraphics();

        imageContext.drawImage(backgroundImage, 0, 0, null);
        imageContext.dispose();
    }

    private void sleep(long speed) {

        try {
            Thread.sleep(speed);

        } catch (InterruptedException ex) {

            System.err.println(ex);
        }
    }

    private void drawGame() {
        // Draw ship if alive
        if (ship.isIntact()) {
            ship.draw(graphics);
        }

        // Draw missiles
        for (int i = 0; i < missiles.size(); i++) {
            Missile missile = missiles.get(i);
            if (missile != null) {
                missile.draw(graphics);
            }
        }

        // Draw aliens
        for (int i = 0; i < aliens.size(); i++) {
            Alien alien = aliens.get(i);
            if (alien != null) {
                alien.draw(graphics);
            }
        }

        // Draw explosion effects
        for (int i = 0; i < explosionEffects.size(); i++) {
            ExplosionEffect explosionEffect1 = explosionEffects.get(i);
            if (explosionEffect1 != null) {
                explosionEffect1.draw(graphics);
            }
        }

        // Draw score
        graphics.setColor(Color.RED);
        graphics.setFont(getFont().deriveFont(Font.BOLD, 18f));
        graphics.drawString("Ships Destroyed : " + score, 10, 20);

        // Draw game over screen if ship is dead
        if (!ship.isIntact()) {
            // TODO: play ship dead sound

            // Draw game over text
            String text = "YOU HAVE DESTROYED " + score + " ALIEN SHIPS!";
            graphics.setFont(getFont().deriveFont(Font.BOLD, 25f));
            FontMetrics font = graphics.getFontMetrics();
            Rectangle2D rect = font.getStringBounds(text, graphics);
            double textWidth = rect.getWidth();
            double textHeight = rect.getHeight();
            double x = (w - textWidth) / 2;
            double y = (h - textHeight) / 2;
            graphics.drawString(text, (int) x, (int) y + font.getAscent());

            // Draw key prompt text
            String textKey = "Press the enter key if you wish to Continue ...";
            graphics.setFont(getFont().deriveFont(Font.BOLD, 19f));
            font = graphics.getFontMetrics();
            rect = font.getStringBounds(textKey, graphics);
            textWidth = rect.getWidth();
            textHeight = rect.getHeight();
            x = (w - textWidth) / 2;
            y = (h - textHeight) / 2;
            graphics.drawString(textKey, (int) x, (int) y + font.getAscent() + 50);
        }
    }

    private void renderGame() {
        Graphics graphics1 = getGraphics();
        graphics1.drawImage(buffImage, 0, 0, null);
        graphics1.dispose();
    }

}
