import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
//Jarod Esareesingh 
//816026811
public class Alien extends HealthRender {
    
    public double x;
    public double y;
    public float speed = 0.4f;
    public float angle = 0;
    public Area alienShape;
    public BufferedImage alienImg;
    public static double alienRocketSize = 50;

    // Initialize an instance of the Alien class
    public Alien() {
        // Call the constructor of the parent class (Entity) with a Health object as parameter
        super(new Health(20, 20));
        // Load the Img of the alien
        this.alienImg = ImageManager.loadBufferedImage("images/alien.png");

        // Create a path for the shape of the alien ship
        Path2D path = new Path2D.Double();
        path.moveTo(0, alienRocketSize / 2);
        path.lineTo(15, 10);
        path.lineTo(alienRocketSize - 5, 13);
        path.lineTo(alienRocketSize + 10, alienRocketSize / 2);
        path.lineTo(alienRocketSize - 5, alienRocketSize - 13);
        path.lineTo(15, alienRocketSize - 10);
        // Create an Area object from the path
        alienShape = new Area(path);
    }

    // Change the location of the alien to the specified x and y values
    public void changeShipLoc(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Update the position of the alien based on its current angle and speed
    public void updateAlien() {
        x += Math.cos(Math.toRadians(angle)) * speed;
        y += Math.sin(Math.toRadians(angle)) * speed;
    }

    // Change the angle of the alien to the specified angle value
    public void modifyShipAngle(float angle) {
        // If the angle is less than 0, set it to 359
        if (angle < 0) {
            angle = 359;
        }
        // If the angle is greater than 359, set it to 0
        else if (angle > 359) {
            angle = 0;
        }
        this.angle = angle;
    }

    
    public double returnX() {
        return x;
    }

    public double returnY() {
        return y;
    }

    public float returnAngle() {
        return angle;
    }

    // Draw the alien on the specified Graphics2D object
    public void draw(Graphics2D graphics) {
        // Save the current transformation of the Graphics2D object
        AffineTransform oldTransform = graphics.getTransform();
        // Translate the Graphics2D object to the position of the alien
        graphics.translate(x, y);
        // Draw the image of the alien with a new transformation
        AffineTransform tran = new AffineTransform();
        graphics.drawImage(alienImg, tran, null);
        // Get the shape of the alien's ship and render its health bar
        Shape shap = getShipShape();
        renderHealthBar(graphics, shap, y);
        // Restore the original transformation of the Graphics2D object
        graphics.setTransform(oldTransform);
    }

    // This method returns an Area object that represents the transformed shape of
    // the alien ship
    public Area getShipShape() {
        AffineTransform afx = new AffineTransform();
        afx.translate(x, y);
        return new Area(afx.createTransformedShape(alienShape));
    }

    // This method checks if the alien rocket is out of bounds or not
    // It takes in the width and height of the screen as parameters
    // Returns true if the alien rocket is within the bounds of the screen, false
    // otherwise
    public boolean checkAlien(int width, int height) {
        Rectangle size = getShipShape().getBounds();
        if (x <= -size.getWidth() || y < -size.getHeight() || x > width || y > height) {
            return false;
        } else {
            return true;
        }
    }


}
