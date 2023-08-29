
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

//Jarod Esareesingh 
//816026811
public class Ship extends HealthRender {

    public BufferedImage shipImage;
    public BufferedImage imgMPH;
    public BufferedImage damage;
    public BufferedImage damageBoosted;
    public static double shipSize = 64;
    public double x;
    public double y;
    public float shipMaxSpeed = 1f;
    public float speed = 0f;
    public float angle = 0f;
    public Area shipShape;
    public boolean boost;
    public boolean isShipDamaged;
    public boolean alive = true;
    public static final int SCREEN_WIDTH = 700;
    public static final int SCREEN_HEIGHT = 700;

    public Ship() {
        // Constructor for the Ship class
        // Set initial health, load images and create the shape of the ship
        super(new Health(50, 50));
        this.shipImage = ImageManager.loadBufferedImage("images/spaceShip.png");
        this.imgMPH = ImageManager.loadBufferedImage("images/SpaceShipBoost.png");
        this.damage = ImageManager.loadBufferedImage("images/ShipDamaged.png");
        this.damageBoosted = ImageManager.loadBufferedImage("images/damageBoosted.png");
        Path2D path = new Path2D.Double();
        path.moveTo(0, 15);
        path.lineTo(20, 5);
        path.lineTo(shipSize + 15, shipSize / 2);
        path.lineTo(20, shipSize - 5);
        path.lineTo(0, shipSize - 15);
        shipShape = new Area(path);
        /*
         * if(this.getHealth() <= 30){
         * isShipDamaged();
         * }
         */
    }

    public void modifyShipAngle(float angle) {
        // Change the angle of the ship
        if (angle < 0) {
            angle = 359;
        } else if (angle > 359) {
            angle = 0;
        }
        this.angle = angle;
    }

    public void changeShipLoc(double x, double y) {
        // Change the location of the ship
        this.x = x;
        this.y = y;
    }

    public void updateShipLoc() {
        // Update the location of the ship
        x += Math.cos(Math.toRadians(angle)) * speed;
        y += Math.sin(Math.toRadians(angle)) * speed;
        // Check if the ship is outside the screen's boundaries and adjust its location
        if (x < 0) {
            x = 0;
        } else if (x > SCREEN_WIDTH - shipSize) {
            x = SCREEN_WIDTH - shipSize;
        }

        if (y < 0) {
            y = 0;
        } else if (y > SCREEN_HEIGHT - shipSize) {
            y = SCREEN_HEIGHT - shipSize;
        }
    }

    public void draw(Graphics2D graphics) {
        // Draw the ship
        if (this.getHealth() > 30) {
            shipImage = ImageManager.loadBufferedImage("images/spaceShip.png");
        }
        AffineTransform oldTransform = graphics.getTransform();
        graphics.translate(x, y);
        AffineTransform tran = new AffineTransform();
        tran.rotate(Math.toRadians(angle + 0), shipSize / 2, shipSize / 2);
        if (this.getHealth() > 30) {
            graphics.drawImage(boost ? imgMPH : shipImage, tran, null);
        }
        if (this.getHealth() <= 30) {
            graphics.drawImage(boost ? damageBoosted : shipImage, tran, null);
        }
        renderHealthBar(graphics, getShipShape(), y);
        graphics.setTransform(oldTransform);
        if (this.getHealth() <= 30) {
            shipImage = damage;
        }
    }

    public Area getShipShape() {
        // Get the shape of the ship
        AffineTransform afx = new AffineTransform();
        afx.translate(x, y);
        afx.rotate(Math.toRadians(angle), shipSize / 2, shipSize / 2);
        return new Area(afx.createTransformedShape(shipShape));
    }

    public void boost() {
        boost = true;
        if (speed > shipMaxSpeed) {
            speed = shipMaxSpeed;
        } else {
            speed += 0.01f;
        }
    }

    public void speedDown() {
        boost = false;
        if (speed <= 0) {
            speed = 0;
        } else {
            speed -= 0.003f;
        }
    }

    public boolean isIntact() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void resetGame() {
        alive = true;
        resetHealth();
        angle = 0;
        speed = 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public float getAngle() {
        return angle;
    }

}
