public class KeyManager {
    // Jarod Esareesingh
    // 816026811

    private boolean right;
    private boolean left;
    private boolean space;
    private boolean j;
    private boolean k;
    private boolean enter;

    public boolean enterCheck() {
        return enter;
    }

    public void setEnter(boolean enter) {
        this.enter = enter;
    }

    public boolean rightCheck() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean leftCheck() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean spaceCheck() {
        return space;
    }

    public void setSpace(boolean space) {
        this.space = space;
    }

    public boolean jCheck() {
        return j;
    }

    public void setJ(boolean j) {
        this.j = j;
    }

    public boolean kCheck() {
        return k;
    }

    public void setK(boolean k) {
        this.k = k;
    }

}
