package game;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;




public class Screen extends JPanel implements Runnable, KeyListener{
	    
	    // Constants for the screen size
	    public static final int WIDTH = 400;
	    public static final int HEIGHT = 400;
	    
	    // Constants for the levels
	    public static final int EASY = 0;
	    public static final int MEDIUM = 1;
	    public static final int HARD = 2;
	    
	    // Variables for the game loop
	    private Thread thread;
	    private boolean running = false;
	    private int ticks = 0;
	    
	    // Variables for the snake and apples
	    private ArrayList<BodyPart> snake;
	    private ArrayList<Apple> apples;
	    private Random r;
	    private int xCoor = 10;
	    private int yCoor = 10;
	    private int size = 5;
	    private boolean right = true;
	    private boolean left = false;
	    private boolean up = false;
	    private boolean down = false;
	    
	    // Variables for the level
	    private int level = EASY;
	    private int speed = 2500000;
	    private int numApples = 1; 
	    //labyrinth 2d array
	    /*int[][] labyrinth = {
	    	    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
	    	    {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
	    	    {1, 0, 1, 1, 0, 1, 1, 1, 0, 1},
	    	    {1, 0, 1, 0, 0, 0, 0, 1, 0, 1},
	    	    {1, 0, 1, 0, 1, 1, 0, 1, 0, 1},
	    	    {1, 0, 0, 0, 0, 1, 0, 1, 0, 1},
	    	    {1, 0, 1, 1, 0, 1, 0, 1, 0, 1},
	    	    {1, 0, 0, 1, 0, 0, 0, 1, 0, 1},
	    	    {1, 0, 1, 0, 0, 1, 1, 1, 0, 1},
	    	    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
	    	};*/

	    
	    public Screen() {
	        setFocusable(true);
	        addKeyListener(this);
	        setPreferredSize(new Dimension(WIDTH, HEIGHT));
	        r = new Random();
	        snake = new ArrayList<BodyPart>();
	        apples = new ArrayList<Apple>();
	        start();
	    }
	    
	    void tick() {
	        if (snake.size() == 0) {
	            BodyPart b = new BodyPart(xCoor, yCoor, 10);
	            snake.add(b);
	        }
	        
	        /* for labyrinth
	        if (labyrinth[yCoor][xCoor] == 1) {
	            stop();
	        }*/
	        if (apples.size() == 0) {
	            int xCoor = r.nextInt(39);
	            int yCoor = r.nextInt(39);

	            Apple apple = new Apple(xCoor, yCoor, 10);
	            apples.add(apple);
	        }

	        for (int i = 0; i < apples.size(); i++) {
	            if (xCoor == apples.get(i).getxCoor() && yCoor == apples.get(i).getyCoor()) {
	                size++;
	                apples.remove(i);
	                i++;
	            }
	        }

	        for (int i = 0; i < snake.size(); i++) {
	            if (xCoor == snake.get(i).getxCoor() && yCoor == snake.get(i).getyCoor()) {
	                if (i != snake.size() - 1) {
	                    stop();
	                }
	            }
	        }

	        if (xCoor < 0 || xCoor > 39 || yCoor < 0 || yCoor > 39) {
	            stop();
	        }

	        ticks++;

	        // increase the speed of the snake based on its size
	    int speed = 2500000;
	    if (size >= 0 && size < 20) {
	    speed = 2000000;
	    } else if (size >= 20 && size < 40) {
	    speed = 1400000;
	    } else if (size >= 40) {
	    speed = 1400000;
	    }
	        
	    if (ticks > speed) {
	        if (right) xCoor++;
	        if (left) xCoor--;
	        if (up) yCoor--;
	        if (down) yCoor++;

	        ticks = 0;

	        BodyPart b = new BodyPart(xCoor, yCoor, 10);
	        snake.add(b);

	        if (snake.size() > size) {
	            snake.remove(0);
	        }
	    }
	    }

	    public void paint(Graphics g) {
	    g.clearRect(0, 0, WIDTH, HEIGHT);

	    // draw the grid
	    g.setColor(Color.BLACK);
	    for (int i = 0; i < WIDTH; i += 10) {
	        g.drawLine(i, 0, i, HEIGHT);
	    }
	    for (int i = 0; i < HEIGHT; i += 10) {
	        g.drawLine(0, i, WIDTH, i);
	    }

	    // draw the snake
	    for (int i = 0; i < snake.size(); i++) {
	        snake.get(i).draw(g);
	    }
	    
	    

	    // draw the apples
	    for (int i = 0; i < apples.size(); i++) {
	        apples.get(i).draw(g);
	    }

	    // draw the score
	    g.setColor(Color.BLACK);
	    g.drawString("Score: " + (size - 5), 5, 15);
	    
	 // Draw the labyrinth
	    

	    // draw the level
	    g.setColor(Color.BLACK);
	    String levelString = "";
	    switch (level) {
	        case EASY:
	            levelString = "Easy";
	            break;
	        case MEDIUM:
	            levelString = "Medium";
	            break;
	        case HARD:
	            levelString = "Hard";
	            break;
	    }
	    g.drawString("Level: " + levelString, 360, 15);

	    // draw the game over message if the game is over
	    if (!running) {
	        g.setColor(Color.RED);
	        g.drawString("Game Over", WIDTH / 2 - 30, HEIGHT / 2);
	    }
	    }
	    public void start() {
	        running = true;
	        thread = new Thread(this);
	        thread.start();
	    }
	 
	    public void stop() {
	        running = false;
	        try {
	            thread.join();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
	 
	    public void run() {
	        while (running) {
	            tick();
	            repaint();
	        }
	    }
	    
	    public void pause() {
	        running = false;
	        try {
	            thread.join();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }

	    @Override
	    public void keyPressed(KeyEvent e) {
	    int key = e.getKeyCode();
	    if (key == KeyEvent.VK_RIGHT && !left) {
	        right = true;
	        up = false;
	        down = false;
	    }

	    if (key == KeyEvent.VK_LEFT && !right) {
	        left = true;
	        up = false;
	        down = false;
	    }

	    if (key == KeyEvent.VK_UP && !down) {
	        up = true;
	        right = false;
	        left = false;
	    }

	    if (key == KeyEvent.VK_DOWN && !up) {
	        down = true;
	        right = false;
	        left = false;
	    }

	    if (key == KeyEvent.VK_ENTER) {
	        if (!running) {
	            start();
	        }
	    }

	    if (key == KeyEvent.VK_SPACE) {
	        if (running) {
	            pause();
	        } else if (!running) {
	            start();
	        }
	    }

	    if (key == KeyEvent.VK_ESCAPE) {
	        stop();
	    }
	    }

	    @Override
	    public void keyReleased(KeyEvent e) {

	    }

	    @Override
	    public void keyTyped(KeyEvent e) {

	    }
}