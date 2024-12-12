package spaceinvaders.objects;

import spaceinvaders.objects.primitives.GraphicObject;
import spaceinvaders.objects.primitives.Vector2D;

public class Bullet extends GraphicObject {
    private long spawnTime;
    private Vector2D dir;
    private int speed;

    /**
     * Initializes a dynamic window element at given position, stores the time when
     * this function was called
     * 
     * @param pos
     * @param size
     * @param image
     * @param dir
     * @param speed
     */
    public Bullet(Vector2D pos, Vector2D size, String image, Vector2D dir, int speed) {
        super(pos, size, image);
        this.dir = dir;
        this.dir.normalize();
        this.dir.set(this.dir.getX() * speed, this.dir.getY() * speed);
        spawnTime = System.currentTimeMillis();
    }

    public long getSpawnTime() {
        return spawnTime;
    }

    public Vector2D getDir() {
        return dir;
    }

    public void setDir(Vector2D dir) {
        this.dir = dir;
        this.dir.normalize();
    }
}
