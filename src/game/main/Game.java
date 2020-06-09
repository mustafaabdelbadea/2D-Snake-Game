package game.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;

public class Game extends JPanel implements Runnable, KeyListener {

    private Thread thread;
    private boolean running;
    private Snake s;
    private ArrayList<Snake> snake;//array list type is Snake(Capital S) and the name of array is snake(small s)

    private int xpos = 15, ypos = 15, size = 3;  //starting position of snake head

    private boolean up = false, down = false, left = false, right = false;
    private int width = 620;
    private int height = 660;
    private int square = 20;
    private int counter = 0;
    private Random random;
    private Apple apple;

    public Game() {
        addKeyListener(this);
        setFocusable(true);
        setPreferredSize(new Dimension(width, height));
        init();
    }

    @Override//method is called after our JPanel has been added to the JFrame component
    //used for various initialization tasks.

    public void addNotify() {
        super.addNotify();
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    private void CreateApple() {

        random = new Random();
        int xCoor = 1 + random.nextInt(29);// random between 1:30 because we have 30x30 grid
        int yCoor = 2 + random.nextInt(29);// random between 2:31 because we have 30x30 grid
        for (int i = 0; i < snake.size(); ++i) {
            while (xCoor == snake.get(i).getXpos() && yCoor == snake.get(i).getYpos()) {
                xCoor = 1 + random.nextInt(29);// change again
                yCoor = 2 + random.nextInt(29);
            }
        }
        apple = new Apple(xCoor, yCoor, square);// spawns apple of size 20
    }

    public void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        snake = new ArrayList<Snake>();//to create arraylis  
        s = new Snake(xpos, ypos, square);//create new head with size 20			
        snake.add(s);
        CreateApple();

    }

    public void tick() {
        if (up || down || left || right)//moves when user presses any key
        {
            move();
            eat();
            collision();
        }
    }

    public void eat() {
        //if the x and y of snake are on apple
        if (xpos == apple.getXpos() && ypos == apple.getYpos()) {
            size++;
            counter += 7;
            apple.setState(false);
        }
        if (apple.getState() == false)//to put apple every time
        {
            CreateApple();
        }
    }

    public void collision() {
        for (int i = 0; i < snake.size(); i++)//if x and y of snake head touches snake body
        {
            if (xpos == snake.get(i).getXpos() && ypos == snake.get(i).getYpos()) {
                if (i != snake.size() - 1)// if that part isn't the head 
                {
                    stop();
                }
            }
        }

        if (xpos < 1 || xpos > 29)//checks if snake passes left or right sides
        {
            stop();
        }
        if (ypos < 2 || ypos > 31)//checks if snake passes top or bottom sides
        {
            snake.remove(snake.size() - 1);//to not overlap over score boarder	
            stop();
        }
    }

    public synchronized void move() {
        if (right && !left) {
            xpos++;
        } else if (!right && left) {
            xpos--;
        } else if (!up && down) {
            ypos++;
        } else if (up && !down) {
            ypos--;
        }

        s = new Snake(xpos, ypos, square);
        snake.add(s);

        if (snake.size() > size) {
            snake.remove(0);
        }
    }

    public void paint(Graphics g) {
        g.setColor(Color.WHITE);//draws score boarder with white
        g.fillRect(0, 0, width, 20);

        g.clearRect(0, 40, width, height);//clears rectangle for game

        g.setColor(Color.BLACK);
        g.fillRect(0, 20, width, height);//draw background with black

        //for(int i=0;i<width/10;i++)
        //	g.drawLine(i*square,40,i*square,height-20);//to draw vertical lines |
        //for(int i=2;i<width/10;i++)
        //	g.drawLine(0,i*square,width,i*square);//to draw horizontal lines -
        for (int i = 0; i < snake.size(); i++) //to draw snake
        {
            snake.get(i).draw(g);
        }
        if (apple.getState() == true) //to draw apple
        {
            apple.draw(g);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("arial", Font.PLAIN, 15));	//to draw score with black
        g.drawString("Score: " + counter, 520, 14);

        g.setColor(Color.BLACK);
        g.setFont(new Font("arial", Font.PLAIN, 15));	//to draw length with black
        g.drawString("Length: " + size, 420, 14);

        g.setColor(Color.RED);//to draw boarder
        g.fillRect(0, square, width, square);//top
        g.fillRect(0, square, square, height);//left
        g.fillRect(width - square, square, square, height);//right
        g.fillRect(0, height - square, width, square);//bottom
    }

    @Override
    public void run() {
        int fps = 20;	//frames per second
        double timepertick = 1000000000 / fps;// it takes 1 billion nanosecond / fps for each tick
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        while (running) {
            now = System.nanoTime();
            delta += (now - lastTime) / timepertick;
            lastTime = now;
            if (delta >= 1) {
                tick();//game logic
                repaint();//to repaint the graphics every frame
                delta--;
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && !left) {
            right = true;
            left = false;
            up = false;
            down = false;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && !right) {
            right = false;
            left = true;
            up = false;
            down = false;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && !up) {
            right = false;
            left = false;
            up = false;
            down = true;
        } else if (e.getKeyCode() == KeyEvent.VK_UP && !down) {
            right = false;
            left = false;
            up = true;
            down = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
