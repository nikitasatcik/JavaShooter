import java.awt.*;

public class Bullets {
    private double x;
    private double y;
    private int r;

    private double dx;
    private double dy;

    private Color color1;

    public Bullets(double angle, int x, int y) {
        this.x = x;
        this.y = y;
        r = 3;
        double rad = Math.toRadians(angle);
        double speed = 10;
        dx = Math.cos(rad)* speed;
        dy = Math.sin(rad)* speed;

        color1 = Color.YELLOW;
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

    public boolean update() {
        x += dx;
        y += dy;
        if (x < -r || x > GamePanel.WIDTH + r || y < -r || y > GamePanel.HEIGHT + r) {
            return true;
        }
        return false;
    }

    public void draw(Graphics2D g) {
        g.setColor(color1);
        g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
    }
}
