package game.main;

import java.awt.Color;
import java.awt.Graphics;

public class Snake {

    private int xpos, ypos, size;//width of snake = height of snake = size (square)

    public Snake(int xpos, int ypos, int size) {
        this.xpos = xpos;
        this.ypos = ypos;
        this.size = size;
    }

    public int getXpos() {
        return xpos;
    }

    public void setXpos(int xpos) {
        this.xpos = xpos;
    }

    public int getYpos() {
        return ypos;
    }

    public void setYpos(int ypos) {
        this.ypos = ypos;
    }

    public synchronized void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(xpos * size, ypos * size, size, size);
    }

}
