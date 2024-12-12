package spaceinvaders;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import spaceinvaders.objects.primitives.GraphicObject;
import spaceinvaders.objects.primitives.Vector2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Vector;

public class GraphicObjectTest {
    static Vector2D size, pos1, pos2, pos3;
    static GraphicObject o1, o2, o3;

    @BeforeAll
    static void init() {
        size = new Vector2D(10, 10);
        pos1 = new Vector2D(15, 15);
        pos2 = new Vector2D(15, 20);
        pos3 = new Vector2D(30, 30);
        o1 = new GraphicObject(pos1, size, "assets/icon.png");
        o2 = new GraphicObject(pos2, size, "assets/icon.png");
        o3 = new GraphicObject(pos3, size, "assets/icon.png");
    }

    @Test
    void testGetSet() {
        o1.setPos(new Vector2D(40, 40));
        assertEquals(o1.getPos().getXint(), 40);
        assertEquals(o1.getPos().getYint(), 40);
        o1.setPos(50, 50);
        assertEquals(o1.getPos().getXint(), 50);
        assertEquals(o1.getPos().getYint(), 50);
        o1.setSize(new Vector2D(40, 40));
        assertEquals(o1.getSize().getXint(), 40);
        assertEquals(o1.getSize().getYint(), 40);
        o1.setSize(50, 50);
        assertEquals(o1.getSize().getXint(), 50);
        assertEquals(o1.getSize().getYint(), 50);
    }

    @Test
    void testCollision() {

        assertTrue(GraphicObject.collides(o1, o2));
        assertTrue(o1.collides(o2));
        assertFalse(GraphicObject.collides(o2, o3));
        assertFalse(o3.collides(o2));
    }
}
