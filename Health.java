public class Health {
    private double maxHealth;
    private double currHealth;
//Jarod Esareesingh 
//816026811
    public double getMAXhealth() {
        return maxHealth;
    }

    public void setMAXhealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public double getCurrentHealth() {
        return currHealth;
    }

    public void setCurrentHealth(double currentHp) {
        this.currHealth = currentHp;
    }

    public Health(double maxHealth, double currHealth) {
        this.maxHealth = maxHealth;
        this.currHealth = currHealth;
    }


}
