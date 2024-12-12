package spaceinvaders;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import spaceinvaders.objects.Bullet;
import spaceinvaders.objects.DynamicObject;
import spaceinvaders.objects.primitives.Config;
import spaceinvaders.objects.primitives.Vector2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Vector;

import javax.validation.constraints.AssertTrue;

public class DynamicObjectTest {
    static DynamicObject o;
    static Bullet b;

    @BeforeAll
    static void init() {
        o = new DynamicObject(10, 10, 10, Vector2D.nullVector(), new Vector2D(10, 10),
                "assets/icon.png");
        b = o.shoot(new Vector2D(1, 0), "assets/icon.png");
    }

    @Test
    void testGetSet() {
        o.setDamage(1);
        o.setMaxHP(1);
        o.setShotSpeed(1);
        o.setCurrentHP(1);

        assertEquals(o.getDamage(), 1);
        assertEquals(o.getMaxHP(), 1);
        assertEquals(o.getShotSpeed(), 1);
        assertEquals(o.getCurrentHP(), 1);
    }

    @Test
    void testShooty() {

        assertEquals(b.getDir().length(), 3); // its hard to test these functions. :(
    }
}
