import java.awt.*;

public class PowerUp {
    private double x;
    private double y;
    private int r;
    private Color color;
    private int type;

    /*
     * 1 - +1 life
     * 2 - +1 power
     * 3 - +2 power
     */


    public PowerUp(int type, double x, double y) {
        this.type = type;
        this.x = x;
        this.y = y;


        if (type == 1) {
            color = Color.RED;
            r = 5;
        }
        if (type == 2 ) {
            color = Color.YELLOW;
            r =3;
        }
        if (type == 3) {
            color = Color.YELLOW;
            r = 5;
        }
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

    public int getType() {
        return type;
    }

    public boolean update() {
        y += 3;
        if (y > GamePanel.HEIGHT + r) {
            return true;
        }
        return false;
    }

    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillRect((int) (x - r), (int) (y - r), 2 * r, 2 * r);
        g.setStroke(new BasicStroke(3));
        g.setColor(color.darker());
        g.drawRect((int) (x - r), (int) (y - r), 2 * r, 2 * r);
        g.setStroke(new BasicStroke(1));
    }
}

