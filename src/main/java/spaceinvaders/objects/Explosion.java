package spaceinvaders.objects;

import spaceinvaders.objects.primitives.GraphicObject;
import spaceinvaders.objects.primitives.Vector2D;

public class Explosion extends GraphicObject {
    private long spawnTime;

    /**
     * Initializes a static window element with the current time stored
     * 
     * @param pos
     * @param size
     * @param image
     */
    public Explosion(Vector2D pos, Vector2D size, String image) {
        super(pos, size, image);
        spawnTime = System.currentTimeMillis();
    }

    public long getSpawnTime() {
        return spawnTime;
    }
}
