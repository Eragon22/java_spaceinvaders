package spaceinvaders.objects;

import spaceinvaders.objects.primitives.GraphicObject;
import spaceinvaders.objects.primitives.Vector2D;

public class DynamicObject extends GraphicObject {
    private int maxHP;
    private int currentHP;
    private int shotSpeed;
    private int damage;

    /**
     * Generates a dynamic window objects at given position, sets current HP to
     * max HP
     * 
     * @param maxHP
     * @param shotSpeed
     * @param damage
     * @param pos
     * @param size
     * @param image
     */
    public DynamicObject(int maxHP, int shotSpeed, int damage, Vector2D pos, Vector2D size, String image) {
        super(pos, size, image);
        this.setMaxHP(maxHP);
        this.setCurrentHP(maxHP);
        this.setShotSpeed(shotSpeed);
        this.setDamage(damage);
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

    public int getShotSpeed() {
        return shotSpeed;
    }

    public void setShotSpeed(int shotSpeed) {
        this.shotSpeed = shotSpeed;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Initializes a bullet object with the given dirrection and texture next to
     * this object's current position, depending on parameter
     * 
     * @param direction
     * @param texture
     * @return
     */
    public Bullet shoot(Vector2D direction, String texture) {
        Vector2D pos = new Vector2D(getPos());
        Vector2D size = new Vector2D(4, 16);

        direction.normalize();
        double x = getSize().getX() / 2 - 2;
        double y = getSize().getY() / 2 + direction.getY() * getSize().getY() / 2;
        pos.add(new Vector2D(x, y));

        Bullet bullet = new Bullet(pos, size, texture, direction, 3);
        return bullet;
    }
}
