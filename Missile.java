import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

//Jarod Esareesingh 
//816026811
public class Missile {

    public float missileAngle;
    public double missileSize;
    public float missileSpeed = 1f;
    public double x;
    public double y;
    public Shape shape;
    public Color color = new Color(255, 0, 0);

    // Constructor for missile class
    public Missile(double x, double y, float missileAngle, double missileSize, float missileSpeed) {
        // Center missile position within player
        double centerX = x + Ship.shipSize / 2 - (missileSize / 2);
        double centerY = y + Ship.shipSize / 2 - (missileSize / 2);

        // Set missile properties
        this.x = centerX;
        this.y = centerY;
        this.missileAngle = missileAngle;
        this.missileSize = missileSize;
        this.missileSpeed = missileSpeed;
        shape = new Ellipse2D.Double(0, 0, missileSize, missileSize);
    }

    public Shape getMissileShape() {
        return new Area(new Ellipse2D.Double(x, y, missileSize, missileSize));
    }

    public double returnX() {
        return x;
    }

    public double returnY() {
        return y;
    }

    public double returnSize() {
        return missileSize;
    }

    public double returnCenterX() {
        return x + missileSize / 2;
    }

    public double returnCenterY() {
        return y + missileSize / 2;
    }

    // Update the position of the missile
    public void updateMissile() {
        x += Math.cos(Math.toRadians(missileAngle)) * missileSpeed;
        y += Math.sin(Math.toRadians(missileAngle)) * missileSpeed;
    }

    // Check if missile is out of bounds of the game window
    public boolean checkMissile(int width, int height) {
        boolean inBounds = (x > -missileSize && y > -missileSize && x < width && y < height);
        return inBounds;
    }

    // Draw the missile shape onto the graphics context
    public void draw(Graphics2D g2) {
        // Save the current graphics transform
        AffineTransform oldTransform = g2.getTransform();

        // Set missile color and position
        g2.setColor(color);
        g2.translate(x, y);

        // Draw missile shape
        g2.fill(shape);

        // Reset graphics transform
        g2.setTransform(oldTransform);
    }

}
