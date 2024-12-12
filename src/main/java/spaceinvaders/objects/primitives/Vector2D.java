package spaceinvaders.objects.primitives;

public class Vector2D {
    private double x;
    private double y;

    /**
     * Initializes a null vector
     */
    public Vector2D() {
        x = 0;
        y = 0;
    }

    /**
     * Initializes a vector with given parameters
     * 
     * @param x width
     * @param y height
     */
    public Vector2D(double x, double y) {
        this.setX(x);
        this.setY(y);
    }

    /**
     * Copy constructor
     * 
     * @param vec
     */
    public Vector2D(Vector2D vec) {
        this.setX(vec.getX());
        this.setY(vec.getY());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Adds parameter value to x
     * 
     * @param delta
     * @return
     */
    public Vector2D addX(double delta) {
        x += delta;
        return this;
    }

    /**
     * Adds parameter value to y
     * 
     * @param delta
     * @return
     */
    public Vector2D addY(double delta) {
        y += delta;
        return this;
    }

    /**
     * Adds parameter value to vector
     * 
     * @param delta
     * @return
     */
    public Vector2D add(Vector2D vec) {
        this.x += vec.getX();
        this.y += vec.getY();
        return this;
    }

    /**
     * Adds parameter value to vector
     * 
     * @param delta
     * @return
     */
    public Vector2D add(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public int getXint() {
        return Math.toIntExact(Math.round(x));
    }

    public int getYint() {
        return Math.toIntExact(Math.round(y));
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns with the vector's length
     * 
     * @return
     */
    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Normalizes vector (sets length to ~1, keeps the proportions of x and y)
     */
    public void normalize() {
        Vector2D temp = new Vector2D(x, y);
        x = x / temp.length();
        y = y / temp.length();
    }

    /**
     * Returns with initialized null vector
     * 
     * @return null vector
     */
    public static Vector2D nullVector() {
        return new Vector2D(0, 0);
    }
}
