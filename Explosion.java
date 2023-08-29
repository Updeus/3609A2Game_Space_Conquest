public class Explosion {
//Jarod Esareesingh 
//816026811
    private double explosionSize;
    private float explosionAngle;

    public Explosion(double explosionSize, float explosionAngle) {
        this.explosionSize = explosionSize;
        this.explosionAngle = explosionAngle;
    }

    public float getExplosionAngle() {
        return explosionAngle;
    }

    public double getExplosionSize() {
        return explosionSize;
    }

    public void setExplosionSize(double explosionSize) {
        this.explosionSize = explosionSize;
    }

}
