import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
//Jarod Esareesingh 
//816026811
public class HealthRender {

    private final Health health;

    public HealthRender(Health health) {
        this.health = health;
    }
    
    protected void renderHealthBar(Graphics2D g2, Shape shape, double y) {
        if (health.getCurrentHealth() != health.getMAXhealth()) {
            double healthY = shape.getBounds().getY() - y - 10;
            g2.setColor(new Color(70, 70, 70));
            g2.fill(new Rectangle2D.Double(0, healthY, Ship.shipSize, 2));
            g2.setColor(new Color(253, 91, 91));
            double healthSize = health.getCurrentHealth() / health.getMAXhealth() * Ship.shipSize;
            g2.fill(new Rectangle2D.Double(0, healthY, healthSize, 2));
        }
    }
    
    public boolean updateHealth(double damage) {
        health.setCurrentHealth(health.getCurrentHealth() - damage);
        return health.getCurrentHealth() > 0;
    }
    
    public double getHealth() {
        return health.getCurrentHealth();
    }
    
    public void resetHealth() {
        health.setCurrentHealth(health.getMAXhealth());
    }
}
