package spaceinvaders.objects;

import spaceinvaders.objects.primitives.*;

public class StaticObject extends GraphicObject {
    private int maxHP;
    private int currentHP;

    /**
     * Initializes a static window element at the given position, sets current
     * health to max health
     * 
     * @param maxHP starting health
     * @param pos   where
     * @param size  what size
     * @param image source image location
     */
    public StaticObject(int maxHP, Vector2D pos, Vector2D size, String image) {
        super(pos, size, image);
        this.setMaxHP(maxHP);
        this.setCurrentHP(maxHP);
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }
}
