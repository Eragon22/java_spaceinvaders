package spaceinvaders;

import org.junit.jupiter.api.Test;

import spaceinvaders.objects.primitives.Vector2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Vector;

public class Vector2DTest {
    @Test
    void testAddGetSet() {
        Vector2D v = new Vector2D(0, 0);

        v.add(new Vector2D(0, 2));
        assertEquals(v.length(), 2);
        v.add(0, 2);
        assertEquals(v.length(), 4);
        v.set(0, 0);
        assertEquals(v.length(), Vector2D.nullVector().length());

        v.setX(4);
        v.setY(5);
        assertEquals(v.getX(), 4);
        assertEquals(v.getY(), 5);
        assertEquals(v.getXint(), 4);
        assertEquals(v.getYint(), 5);

    }

    @Test
    void testLen() {
        assertEquals(new Vector2D(10, 0).length(), 10);
    }

    @Test
    void testNormalize() {

        Vector2D v = new Vector2D(115, 122);
        v.normalize();
        if (v.length() <= 0.999999999 || v.length() >= 1.00000001)
            fail(); // Math.sqrt is not accurate :'(
    }

    @Test
    void testNullVec() {
        assertEquals(Vector2D.nullVector().length(), 0);
    }
}
