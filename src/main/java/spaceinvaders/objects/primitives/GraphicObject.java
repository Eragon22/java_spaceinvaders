package spaceinvaders.objects.primitives;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class GraphicObject {
    private Vector2D pos;
    private Vector2D size;
    private Image img;

    /**
     * Initializes window element at given position and with given texture
     * 
     * @param pos
     * @param size
     * @param image
     */
    public GraphicObject(Vector2D pos, Vector2D size, String image) {
        this.setPos(pos);
        this.setSize(size);
        ImageIcon icon = new ImageIcon(image);
        img = icon.getImage();

        this.scaleImg(size);
    }

    /**
     * Scales image to size
     * 
     * @param size
     */
    public void scaleImg(Vector2D size) {
        Image scaledImg = img.getScaledInstance(size.getXint(), size.getYint(), Image.SCALE_FAST);
        img = new ImageIcon(scaledImg).getImage();
    }

    public Image getImg() {
        return this.img;
    }

    public Vector2D getPos() {
        return pos;
    }

    public void setPos(Vector2D pos) {
        this.pos = pos;
    }

    public void setPos(int x, int y) {
        this.pos.set(x, y);
    }

    public Vector2D getSize() {
        return this.size;
    }

    public void setSize(Vector2D size) {
        this.size = size;
    }

    public void setSize(int x, int y) {
        this.size.set(x, y);
    }

    /**
     * Moves currrent position by the paramter vector
     * 
     * @param by
     */
    public void move(Vector2D by) {
        this.pos.add(by);
    }

    /**
     * Tests if object overlaps with this object
     * 
     * @param object
     * @return
     */
    public boolean collides(GraphicObject object) {
        int xoverlap = Math.max(0,
                Math.min(this.getPos().getXint() + this.getSize().getXint(),
                        object.getPos().getXint() + object.getSize().getXint())
                        - Math.max(this.getPos().getXint(), object.getPos().getXint()));
        int yoverlap = Math.max(0,
                Math.min(this.getPos().getYint() + this.getSize().getYint(),
                        object.getPos().getYint() + object.getSize().getYint())
                        - Math.max(this.getPos().getYint(), object.getPos().getYint()));
        int overlapArea = xoverlap * yoverlap;
        return overlapArea > 0;
    }

    /**
     * Tests if objects overlap
     * 
     * @param aObject object 1
     * @param bObject object 2
     * @return
     */
    public static boolean collides(GraphicObject aObject, GraphicObject bObject) {
        return aObject.collides(bObject);
    }
}
