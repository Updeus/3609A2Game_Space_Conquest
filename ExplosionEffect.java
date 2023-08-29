import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Random;
//Jarod Esareesingh 
//816026811
public class ExplosionEffect {
    
    public int effect;
    public float exploSpeed;
    public double currDist;
    public Explosion explosions[];
    public float val = 1f;
    public double x;
    public double y;
    public double distMax;
    public int sizeMax;
    public Color explosionColor;

    // A class that represents an effect with multiple explosions
    public ExplosionEffect(double x, double y, int effect, int sizeMax, double distMax, float exploSpeed, Color explosionColor) {
        // Store the input parameters as member variables
        this.x = x;
        this.y = y;
        this.effect = effect;
        this.sizeMax = sizeMax;
        this.distMax = distMax;
        this.exploSpeed = exploSpeed;
        this.explosionColor = explosionColor;

        // Create the explosions randomly
        createRandomExoplosion();
    }

    // Creates the explosions randomly
    private void createRandomExoplosion() {
        explosions = new Explosion[effect];
        // Calculate the Angle between each explosion
        float eff = 360f / effect;
        Random rando = new Random();
        // Create each explosion with a random size and Angle
        for (int i = 1; i <= effect; i++) {
            // Randomize the explosion Angle within the range of each explosion's section
            int r = rando.nextInt((int) eff) + 1;
            // Randomize the size of the explosion
            int explosionSize = rando.nextInt(sizeMax) + 1;
            // Calculate the Angle of the explosion
            float explosionAngle = i * eff + r;
            // Create a new object with the random size and Angle
            explosions[i - 1] = new Explosion(explosionSize, explosionAngle);
        }
    }

    // Draws the effect on a Graphics2D object
    public void draw(Graphics2D graphics) {
        // Store the original transform and composite of the graphics object
        AffineTransform initial = graphics.getTransform();
        Composite initialComposite = graphics.getComposite();
        // Set the color of the graphics object to the effect's color
        graphics.setColor(explosionColor);
        // Translate the graphics object to the effect's position
        graphics.translate(x, y);
        // Iterate through each explosion and draw it
        for (Explosion i : explosions) {
            // Calculate the position of the explosion based on its Angle and current
            // distance from the center
            double explosionX = Math.cos(Math.toRadians(i.getExplosionAngle())) * currDist;
            double explosionY = Math.sin(Math.toRadians(i.getExplosionAngle())) * currDist;
            double explosionSize = i.getExplosionSize();
            double space = explosionSize / 2;
            // If the current distance is close to the maximum distance, decrease the
            // value of the explosion
            if (currDist >= distMax - (distMax * 0.7f)) {
                val = (float) ((distMax - currDist) / (distMax * 0.7f));
            }
            // Ensure that the value is between 0 and 1
            if (val > 1) {
                val = 1;
            } else if (val < 0) {
                val = 0;
            }
            // Set the composite of the graphics object to use the value
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, val));
            // Draw the explosion as a rectangle
            graphics.fill(new Rectangle2D.Double(explosionX - space, explosionY - space, explosionSize, explosionSize));
        }
        // Reset the transform and composite of the graphics object to the original
        // values
        graphics.setComposite(initialComposite);
        graphics.setTransform(initial);
    }

    public void updateExplosion() {
        currDist += exploSpeed;
    }

    public boolean checkExplosion() {
        return currDist < distMax;
    }
}
