import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable, KeyListener {
    public static int WIDTH = 500;
    public static int HEIGHT = 600;
    private Thread thread;
    private boolean running;
    private BufferedImage image;
    private Graphics2D g;
    public static Player player;
    public static ArrayList<Bullets> bullets;
    public static ArrayList<Enemy> enemies;
    private static ArrayList<PowerUp> powerUps;
    private long waveStartTimer;
    private long waveStartDifference;
    private int waveNumber;
    private boolean waveStart;
    private int waveDelay = 2000;


    public GamePanel() {
        super();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();
    }

    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
        addKeyListener(this);
    }

    public void run() {
        running = true;
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        player = new Player();
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        powerUps = new ArrayList<>();
        waveStartTimer = 0;
        waveStartDifference = 0;
        waveStart = true;
        waveNumber = 0;

        long startTime;
        long URDTimeMillis;
        long waitTime;
        long totalTime = 0;
        int FPS = 30;
        long targetTime = 1000 / FPS;
        int frameCount = 0;
        //   int maxFrameCount = 60;

        //Game loop
        while (running) {
            gameUpdate();
            gameRender();
            gameDraw();

            //FPS limiter
            startTime = System.nanoTime();
            URDTimeMillis = (System.nanoTime() - startTime) / 1000000; //Measure how long some code takes to execute
            waitTime = targetTime - URDTimeMillis;  //wait until 30 milliseconds passed
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.getMessage();
            }
            totalTime += System.nanoTime() - startTime;
            frameCount++;
        }

        //Game Over
        g.setColor(new Color(0, 100, 255));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Century Gothic", Font.PLAIN, 20));
        String s = " G A M E    O V E R";
        g.drawString(s, WIDTH / 2 - 100, HEIGHT / 2 - 100);
        s = "Your score : " + player.getScore();
        g.drawString(s, WIDTH / 2 - 75, HEIGHT / 2 - 50);
        gameDraw();
    }

    // Updating All objects in game
    private void gameUpdate() {

        //New Wave
        if (waveStartTimer == 0 && enemies.size() == 0) {
            waveNumber++;
            waveStart = false;
            waveStartTimer = System.nanoTime();
        } else {
            waveStartDifference = (System.nanoTime() - waveStartTimer) / 1000000;
            if (waveStartDifference > waveDelay) {
                waveStart = true;
                waveStartTimer = 0;
                waveStartDifference = 0;
            }
        }


        //Create Enemies
        if (waveStart && enemies.size() == 0) {
            createNewEnemies();
        }


        // Player update
        player.update();


        // Shooting update
        for (int i = 0; i < bullets.size(); i++) {
            boolean remove = bullets.get(i).update();
            if (remove) {
                bullets.remove(i);
                i--;
            }
        }

        //Enemies update
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).update();
        }

        // PowerUp Update
        for (int i = 0; i < powerUps.size(); i++) {
            boolean remove = powerUps.get(i).update();
            if (remove) {
                powerUps.remove(i);
                i--;
            }
        }

        // Bullet-Enemy Collisions
        for (int i = 0; i < bullets.size(); i++) {

            Bullets bullet = bullets.get(i);
            double bx = bullet.getX();
            double by = bullet.getY();
            double br = bullet.getR();

            for (int j = 0; j < enemies.size(); j++) {
                Enemy enemy = enemies.get(j);
                double ex = enemy.getX();
                double ey = enemy.getY();
                double er = enemy.getR();
                double dx = bx - ex;
                double dy = by - ey;
                double distance = Math.sqrt(dx * dx + dy * dy);
                if (distance < (br + er)) {
                    enemy.hit();
                    bullets.remove(i);
                    i--;
                    break;
                }
            }
        }

        //  Enemy - Enemy Collision (some minor changes in collision.. mmmm)
        for (int i = 0; i < enemies.size(); i++) {
            long startTime;
            long timePassed;
            long waitTime = 5000 ;
            long targetTime ;

            startTime = System.nanoTime();

            Enemy enemy1 = enemies.get(i);
            double e1x = enemy1.getX();
            double e1y = enemy1.getY();
            double e1r = enemy1.getR();
            for (int j = 0; j < enemies.size(); j++) {
                Enemy enemy2 = enemies.get(j);
                double e2x = enemy2.getX();
                double e2y = enemy2.getY();
                double e2r = enemy2.getR();
                double dx = e1x - e2x;
                double dy = e1y - e2y;
                double distance = Math.sqrt(dx * dx + dy * dy);
                timePassed = (System.nanoTime() - startTime)/ 1000000;
                targetTime = waitTime  - timePassed;
                if (targetTime > 4000){
                    if (distance < (e1r + e2r) - 4) {
                     //  enemy1.changeDirection();
                   //     enemy2.changeDirection();
                        break;
                    }
                } else break;

            }
        }


        // Check Wave End
        if (waveNumber == 6) {
            running = false;
        }

        //Check Player Dead
        if (player.isDead()) {
            running = false;
        }

        // Check dead enemies
        for (int i = 0; i < enemies.size(); i++) {
            if (enemies.get(i).isDead()) {
                Enemy enemy = enemies.get(i);

                // Time to PowerUp
                double rand = Math.random();
                if (rand < 0.01) {
                    powerUps.add(new PowerUp(1, enemy.getX(), enemy.getY()));
                } else if (rand < 0.08) {
                    powerUps.add(new PowerUp(2, enemy.getX(), enemy.getY()));

                } else if (rand < 0.05) {
                    powerUps.add(new PowerUp(3, enemy.getX(), enemy.getY()));

                }

                player.addScore(enemy.getRank() + enemy.getType());
                enemies.remove(i);
                i--;
            }
        }


        // Player - Enemy Collision
        if (!player.isRecovering()) {
            int px = player.getX();
            int py = player.getY();
            int pr = player.getR();

            for (int i = 0; i < enemies.size(); i++) {
                Enemy enemy = enemies.get(i);
                double ex = enemy.getX();
                double ey = enemy.getY();
                double er = enemy.getR();

                double dx = px - ex;
                double dy = py - ey;

                double distance = Math.sqrt(dx * dx + dy * dy);
                if (distance < pr + er) {
                    player.loseLife();
                }
            }
        }

        // Player - PowerUp Collision
        if (!player.isRecovering()) {
            int px = player.getX();
            int py = player.getY();
            int pr = player.getR();

            for (int i = 0; i < powerUps.size(); i++) {
                PowerUp powerUp = powerUps.get(i);
                double pwrX = powerUp.getX();
                double pwrY = powerUp.getY();
                double pwrR = powerUp.getR();

                double dx = px - pwrX;
                double dy = py - pwrY;

                double distance = Math.sqrt(dx * dx + dy * dy);
                if (distance < pr + pwrR) {
                    int type = powerUp.getType();
                    if (type == 1) {
                        player.setLives(player.getLives() + 1);
                        if (player.getLives() >= 5) {
                            player.setLives(5);
                        }
                    }
                    if (type == 2) {
                        player.increasePower(1);
                    }

                    if (type == 3) {
                        player.increasePower(2);
                    }

                    powerUps.remove(i);
                    i--;
                }


            }
        }
    }

    private void gameRender() {
        //Draw BackGround
        g.setColor(new Color(0, 100, 255));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        //Draw player
        player.draw(g);


        //Draw Bullets
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).draw(g);
        }

        //Draw Enemies
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }

        //Draw PowerUps
        for (int i = 0; i < powerUps.size(); i++) {
            powerUps.get(i).draw(g);
        }

        // Draw Player Power
        g.setColor(Color.YELLOW);
        g.fillRect(25, 55, player.getPower() * 8, 8);
        g.setColor(Color.YELLOW.darker());
        g.setStroke(new BasicStroke(2));
        for (int i = 0; i < player.getRequiredPower(); i++) {
            g.drawRect(25 + 8 * i, 55, 8, 8);
            g.setStroke(new BasicStroke(1));

        }

        //Draw Wave Number
        if (waveStartTimer != 0) {
            g.setFont(new Font("Century Gothic", Font.PLAIN, 18));
            String s = " - W A V E " + waveNumber + " - ";
            int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            int alpha = (int) (255 * Math.sin(3.14 * waveStartDifference / waveDelay));
            if (alpha > 255) {
                alpha = 255;
            }
            g.setColor(new Color(255, 255, 255, alpha));
            g.drawString(s, WIDTH / 2 - 50, HEIGHT / 2 - 50);
        }

        //Draw Player Lives
        for (int i = 0; i < player.getLives(); i++) {
            g.setColor(Color.WHITE);
            g.fillOval(25 + (25 * i), 25, player.getR() * 2, player.getR() * 2);
            g.setStroke(new BasicStroke(3));
            g.setColor(Color.WHITE.darker());
            g.drawOval(25 + (25 * i), 25, player.getR() * 2, player.getR() * 2);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Century Gothic", Font.PLAIN, 14));
            g.drawString("Lives", 25, 15);

        }

        //Draw Player Score
        g.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        g.setColor(Color.WHITE);
        String s = " Score: " + player.getScore();
        g.drawString(s, 410, 20);


    }

    private void gameDraw() {
        Graphics g2 = this.getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
    }

    private void createNewEnemies() {
        enemies.clear();
        Enemy enemy;
        if (waveNumber == 1) {
            for (int i = 0; i < 15; i++) {
                enemies.add(new Enemy(1, 1));
            }
        }
        if (waveNumber == 2) {
            for (int i = 0; i < 15; i++) {
                enemies.add(new Enemy(1, 1));
                enemies.add(new Enemy(1, 2));
            }
        }
        if (waveNumber == 3) {
            for (int i = 0; i < 20; i++) {
                enemies.add(new Enemy(1, 2));
                enemies.add(new Enemy(1, 1));
            }
        }
        if (waveNumber == 4) {
            for (int i = 0; i < 40; i++) {

                enemies.add(new Enemy(1, 1));

                enemies.add(new Enemy(1, 2));
                enemies.add(new Enemy(1, 3));
            }
        }

        if (waveNumber == 5) {
            for (int i = 0; i < 20; i++) {

                enemies.add(new Enemy(1, 1));
                enemies.add(new Enemy(1, 1));

                enemies.add(new Enemy(1, 2));
                enemies.add(new Enemy(1, 3));
                enemies.add(new Enemy(1, 4));
            }
        }

    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent key) {
        int keyCode = key.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT) {
            player.setLeft(true);
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            player.setRight(true);
        }
        if (keyCode == KeyEvent.VK_UP) {
            player.setUp(true);
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            player.setDown(true);
        }

        if (keyCode == KeyEvent.VK_SPACE) {
            player.setFiring(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent key) {
        int keyCode = key.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT) {
            player.setLeft(false);
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            player.setRight(false);
        }
        if (keyCode == KeyEvent.VK_UP) {
            player.setUp(false);
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            player.setDown(false);
        }

        if (keyCode == KeyEvent.VK_SPACE) {
            player.setFiring(false);
        }
    }
}
