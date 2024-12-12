package spaceinvaders;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import spaceinvaders.objects.primitives.GraphicObject;

public class GamePanel extends JPanel {
    private int width;
    private int height;
    private BufferedImage canvas;

    /**
     * Generates a blank black canvas with given size
     * 
     * @param w width
     * @param h height
     */
    public GamePanel(int w, int h) {
        super();
        setDoubleBuffered(true);

        width = w;
        height = h;

        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = canvas.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        g.dispose();
    }

    /**
     * Paints object on canvas
     * 
     * @param obj
     */
    public void paintObject(GraphicObject obj) {
        Graphics g = canvas.getGraphics();
        g.drawImage(obj.getImg(), obj.getPos().getXint(), obj.getPos().getYint(), null);
        g.dispose();
    }

    /**
     * Paints text at given position on canvas
     * 
     * @param font
     * @param text
     * @param pos_x
     * @param pos_y
     */
    public void paintText(Font font, String text, int pos_x, int pos_y) {
        Graphics g = canvas.getGraphics();
        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(text, pos_x, pos_y);
        g.dispose();
    }

    /**
     * Paints text aligned to the center at the given position on the canvas
     * 
     * @param font
     * @param text
     * @param pos_x
     * @param pos_y
     */
    public void paintTextCenter(Font font, String text, int pos_x, int pos_y) {
        Graphics g = canvas.getGraphics();
        g.setColor(Color.white);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        int x = (pos_x - metrics.stringWidth(text)) / 2;
        g.drawString(text, x, pos_y);
        g.dispose();
    }

    /**
     * Paints text aligned to the right at the given position on the canvas
     * 
     * @param font
     * @param text
     * @param pos_x
     * @param pos_y
     */
    public void paintTextRight(Font font, String text, int pos_x, int pos_y) {
        Graphics g = canvas.getGraphics();
        g.setColor(Color.white);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        int x = (pos_x - metrics.stringWidth(text));
        g.drawString(text, x, pos_y);
        g.dispose();
    }

    /**
     * Returns canvas to its original state
     */
    public void clear() {
        Graphics g = canvas.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);
        g.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(canvas, 0, 0, null);
    }
}
