import java.awt.*;

public class Enemy {

    private double x;
    private double y;
    private int r;

    private double dx;
    private double dy;

    private double speed;

    private int health;
    private int type;
    private int rank;

    private Color color;

    private boolean ready;
    private boolean dead;

    public Enemy(int type, int rank) {
        this.type = type;
        this.rank = rank;

        //Basic Enemy
        if (type == 1) {
            color = Color.BLUE;
            if (rank == 1) {
                health = 1;
                speed = 4;
                r = 5;
            }
            if (rank ==2){
                color = new Color(100,80,200);
                health = 2;
                speed = 2;
                r = 10;
            }

            if (rank ==3){
                color = new Color(100,80,200);
                health = 4;
                speed = 1.5;
                r = 15;
            }

            if (rank == 4){
                color = new Color(100,80,200);
                health = 5;
                speed = 1;
                r = 20;
            }
        }

        x = Math.random() * GamePanel.WIDTH/2  + GamePanel.WIDTH/4 ;
        y = Math.random()*r*15;

        double angle = Math.random() * 140 + 30;
        double rad = Math.toRadians(angle);

        dx = Math.cos(rad) * speed;
        dy = Math.sin(rad) * speed;

        ready = false;
        dead = false;
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getR() {
        return r;
    }

    public boolean isDead() {
        return dead;
    }

    public int getType() {
        return type;
    }

    public int getRank() {
        return rank;
    }

    public void hit() {
        health--;
        if (health <= 0) {
            dead = true;
        }
    }



    public void changeDirection(){
        dx = -dx;
        dy = -dy;
    }


    public void update() {
        x += dx;
        y += dy;

        if (!ready) {
            if (x > r && x < GamePanel.WIDTH - r && y > r && y < GamePanel.HEIGHT - r) {
                ready = true;
            }
        }

        if (x > GamePanel.WIDTH - r && dx > 0) {
            dx = -dx;
        }
        if (y > GamePanel.HEIGHT - r && dy > 0) {
            dy = -dy;
        }

        if (x < r && dx < 0) {
            dx = -dx;
        }

        if (y < r && dy < 0) {
            dy = -dy;
        }

    }


    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
        g.setStroke(new BasicStroke(3));
        g.setColor(color.darker());
        g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
        g.setStroke(new BasicStroke(1));
    }
}
