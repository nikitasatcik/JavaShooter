import java.awt.*;

public class Player {

    private  int x;
    private  int y;
    private  int r;

    private int dx;
    private int dy;
    private int speed;

    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;

    private int lives;
    private boolean dead;
    private Color color1;
    private Color color2;

    private boolean firing;
    private long firingTimer;
    private long firingDelay;

    private boolean recovering;
    private long recoveryTimer;
    private int score;

    private int powerLevel;
    private int power;
    private int[] requiredPower = {1, 2, 3, 4, 5};

    public Player() {
        x = GamePanel.WIDTH / 2;
        y = GamePanel.HEIGHT / 2;
        r = 10;

        dx = 0;
        dy = 0;
        speed = 5;
        lives = 3;
        color1 = Color.BLUE;
        color2 = Color.RED.darker();

        firing = false;
        firingTimer = System.nanoTime();
        firingDelay = 200;

        recovering = false;
        recoveryTimer = 0;
        score = 0;
        dead = false;
    }

    public void update() {
        if (left) {
            dx = -speed;
        }
        if (right) {
            dx = speed;
        }
        if (up) {
            dy = -speed;
        }
        if (down) {
            dy = speed;
        }
        x += dx;
        y += dy;

        if (x < r) x = r;
        if (y < r) y = r;
        if (x > GamePanel.WIDTH - 2 * r) x = GamePanel.WIDTH - 2 * r;
        if (y > GamePanel.HEIGHT - 2 * r) y = GamePanel.HEIGHT - 2 * r;

        dx = 0;
        dy = 0;

        if (firing) {
            long elapsed = (System.nanoTime() - firingTimer) / 1000000;
            if (elapsed > firingDelay) {

                firingTimer = System.nanoTime();
                if (powerLevel < 2) {
                    GamePanel.bullets.add(new Bullets(270, x, y));
                }
                else if (powerLevel < 4) {
                    GamePanel.bullets.add(new Bullets(270, x + 5, y));
                    GamePanel.bullets.add(new Bullets(270, x, y));
                } else {
                    GamePanel.bullets.add(new Bullets(273, x + 5, y));
                    GamePanel.bullets.add(new Bullets(268, x - 5, y));
                    GamePanel.bullets.add(new Bullets(270, x, y));
                }
            }
        }

        long elapsedHitTime = (System.nanoTime() - recoveryTimer) / 1000000;
        if (elapsedHitTime > 3000) {
            recovering = false;
            recoveryTimer = 0;
        }

    }



    public void setLeft(boolean b) {
        left = b;
    }

    public void setRight(boolean b) {
        right = b;
    }

    public void setUp(boolean b) {
        up = b;
    }

    public void setDown(boolean b) {
        down = b;
    }

    public void setFiring(boolean b) {
        firing = b;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public boolean isRecovering() {
        return recovering;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int i) {
        score += i;
    }

    public boolean isDead() {
        return dead;
    }

    public void loseLife() {
        if (lives <= 0) {
            dead = true;
        }
        lives--;
        recovering = true;
        recoveryTimer = System.nanoTime();

    }

    public void increasePower(int i) {
        power += i;
        if (power >= requiredPower[powerLevel]) {
            power -= requiredPower[powerLevel];
            powerLevel++;
            if (powerLevel >= 4) {
                powerLevel = 4;
                power = 5;
            }
        }
    }

    public int getPower() {
        return power;
    }

    public int getRequiredPower() {
        return requiredPower[powerLevel];
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getR() {
        return r;
    }



    public void draw(Graphics2D g) {

        if (recovering) {
            g.setColor(color2);
            g.fillOval(x - r, y - r, r * 2, 3 * r);
            g.setStroke(new BasicStroke(3));
            g.setColor(Color.BLACK);
            g.drawOval(x - r, y - r, r * 2, 3 * r);
            g.setStroke(new BasicStroke(3));
            g.setColor(color2);
            g.fillOval(x - 2 * r, y + r, r, r * 2);
            g.fillOval(x + r, y + r, r, r * 2);
            g.setColor(Color.BLACK);
            g.drawOval(x - 2 * r, y + r, r, r * 2);
            g.drawOval(x + r, y + r, r, r * 2);
            g.fillOval(x - 5, y - 12, r, r);
        } else {
            g.setColor(color1);
            g.fillOval(x - r, y - r, r * 2, 3 * r);
            g.setStroke(new BasicStroke(3));
            g.setColor(Color.BLACK);
            g.drawOval(x - r, y - r, r * 2, 3 * r);
            g.setStroke(new BasicStroke(3));
            g.setColor(color1);
            g.fillOval(x - 2 * r, y + r, r, r * 2);
            g.fillOval(x + r, y + r, r, r * 2);
            g.setColor(Color.BLACK);
            g.drawOval(x - 2 * r, y + r, r, r * 2);
            g.drawOval(x + r, y + r, r, r * 2);
            g.fillOval(x - 5, y - 12, r, r);
        }
    }
}
