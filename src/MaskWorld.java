import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.*;

import java.awt.event.*;

public class MaskWorld implements Runnable, KeyListener {
    //Sets the width and height of the program window
    final int WIDTH = 1000;
    final int HEIGHT = 800;

    //Declare the variables needed for the graphics
    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;
    public BufferStrategy bufferStrategy;

    public boolean gameStart = false;

    //Declare the character objects
    public Friend1 Angela;
    public Friend2 Mia;

    //set up the theme music soundtrack
    public SoundFile themeMusic;

    //set up the maze
    public Maze randomMaze;

    //Set up array
    public Gem[][] manyGemsAngela;
    public Gem[] manyGemsMia;

    public boolean gameOver = false;

    public static void main(String[] args) {
        MaskWorld myApp = new MaskWorld();   //creates a new instance of the game
        new Thread(myApp).start();               //creates a threads & starts up the code in the run( ) method
    }

    public MaskWorld() {

        setUpGraphics();

        //setting up the maze
        randomMaze = new Maze();
        randomMaze.createWalls();

        Angela = new Friend1(30, 30, 800, 710, 0, 0, 0);
        Mia = new Friend2(30, 30, 710, 800, 0, 0, 0);
        // STEP 2 ARRAY
        manyGemsAngela = new Gem[16][8];
        for (int i = 0; i < manyGemsAngela.length; i++) {
            for (int z = 0; z < manyGemsAngela[0].length; z++) {
                if (randomMaze.mazeLayout[i][z] == 0){
                    manyGemsAngela[i][z] = new Gem((i)*50+10, (z)*50+10, 0, 0);
                } else {
                    manyGemsAngela[i][z] = new Gem((i)*50+10, (z)*50+10, 0, 0);
                    manyGemsAngela[i][z].isAlive = false;
                }
            }
            //make second array for the other character -> An array for each character
        }
        manyGemsMia = new Gem[5];
        for (int i = 0; i < manyGemsMia.length; i++) {
            manyGemsMia[i] = new Gem((i*100)+500, 550, 0, 0);
            //make second array for the other character -> An array for each character
        }

        //STEP 3: Make pics for array
        Angela.pic = Toolkit.getDefaultToolkit().getImage("Angela.png");
        Mia.pic = Toolkit.getDefaultToolkit().getImage("Mia.png");
        for (int i = 0; i < manyGemsAngela.length; i++) {
            for (int z = 0; z < manyGemsAngela[0].length; z++) {
                manyGemsAngela[i][z].pic = Toolkit.getDefaultToolkit().getImage("Angelagem.png");
            }
        }
        for (int i = 0; i < manyGemsMia.length; i++) {
            manyGemsMia[i].pic = Toolkit.getDefaultToolkit().getImage("Miagem.png");
        }

        //setting up and playing the theme music
        themeMusic = new SoundFile("Thememusic.wav");
//        themeMusic.loop();

    }

    //*******************************************************************************

    // this is the code that plays the game after you set things up
    public void run() {
        while (true) {
            if (gameOver == false) {
                moveThings();
                //move all the game objects
            }
            checkIntersections();   // check character crashes
            render();               // paint the graphics & report the score
            pause(20);
            checkKeys();// sleep for 20 ms
        }
    }

    public void checkKeys() {
        if (Angela.up == true) {
            Angela.dy = -4;
        } else if (Angela.down == true) {
            Angela.dy = 4;
        } else {
            Angela.dy = 0;
        }
        if (Angela.left == true) {
            Angela.dx = -4;
        } else if (Angela.right == true) {
            Angela.dx = 4;
        } else {
            Angela.dx = 0;
        }

        if (Mia.up == true) {
            Mia.dy = -4;
        } else if (Mia.down == true) {
            Mia.dy = 4;
        } else {
            Mia.dy = 0;
        }
        if (Mia.left == true) {
            Mia.dx = -4;
        } else if (Mia.right == true) {
            Mia.dx = 4;
        } else {
            Mia.dx = 0;
        }
    }

    public void moveThings() {
        Angela.move();
        Mia.move();
    }

    public void checkIntersections() {
        for (int i = 0; i < manyGemsAngela.length; i++) {
            for (int z = 0; z < manyGemsAngela[0].length; z++) {
                if (Angela.rec.intersects(manyGemsAngela[i][z].rec)) {
                    if (manyGemsAngela[i][z].isAlive) {
                        manyGemsAngela[i][z].isAlive = false;
                        Angela.score = Angela.score + 1;
                        new SoundFile("gemCollect.wav").play(); //allows the gem collect sound to overlap
                        // if the player is collecting gems really quickly
                        // because just using the variable does not allow for overlap
                    }
                }
//            if (Mia.rec.intersects(manyGemsMia[i].rec)) {
//                if (manyGemsMia[i].isAlive) {
//                    manyGemsMia[i].isAlive = false;
//                    Mia.score = Mia.score + 1;
//                    new SoundFile("gemCollect.wav").play();
//                }
//            }
            }
        }
    }

    //paints things on the screen using bufferStrategy
    public void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        //background color
        g.fillRect(0, 0, WIDTH, HEIGHT);

        //font information for scoreboard
        Font font = new Font("SansSerif", Font.BOLD,16);
        g.setFont(font);
        g.setColor(Color.white);
        g.drawString("Player 1: " + String.valueOf(Angela.score), 875, 100);
        g.drawString("Player 2: " + String.valueOf(Mia.score), 875, 150);


        // draw characters to the screen (only draw if they are alive)
        if (Mia.isAlive == true) {
            g.drawImage(Mia.pic, Mia.xpos, Mia.ypos, Mia.width, Mia.height, null);
        }
        for (int i = 0; i < manyGemsAngela.length; i++) {
            for (int z = 0; z < manyGemsAngela[0].length; z++) {
                if (manyGemsAngela[i][z].isAlive) {
                    g.drawImage(manyGemsAngela[i][z].pic, manyGemsAngela[i][z].xpos, manyGemsAngela[i][z].ypos, manyGemsAngela[i][z].width,
                            manyGemsAngela[i][z].height, null);
                }
            }
        }
        for (int i = 0; i < manyGemsMia.length; i++) {
            if (manyGemsMia[i].isAlive) {
                g.drawImage(manyGemsMia[i].pic, manyGemsMia[i].xpos, manyGemsMia[i].ypos, manyGemsMia[i].width,
                        manyGemsMia[i].height, null);
            }
        }
        if (Angela.isAlive == true) {
            g.drawImage(Angela.pic, Angela.xpos, Angela.ypos, Angela.width, Angela.height, null);
        }


        //gameOver information
        if (Angela.score == manyGemsAngela.length*manyGemsAngela[0].length || Mia.score == manyGemsMia.length){
            gameOver = true;
        }
        if (gameOver == true) {
            g.setFont(font);
            g.setColor(new Color (250,250,250));
            g.drawString("Gameover", 875, 450);
            if (Angela.score > Mia.score){
                g.setFont(font);
                g.setColor(new Color (189,227,250));
                g.drawString("Player 1 Won", 875, 420);
            }
            if (Angela.score == Mia.score){
                g.setFont(font);
                g.setColor(new Color (250,250,250));
                g.drawString("You tied", 875, 420);
            }
            if (Mia.score > Angela.score) {
                g.setFont(font);
                g.setColor(new Color (250,230,250));
                g.drawString("Player 2 Won", 875, 420);
            }
        }

        //drawing out the maze
        randomMaze.draw(g);

        g.dispose();
        bufferStrategy.show();

    }

    //Graphics setup method
    public void setUpGraphics() {

        frame = new JFrame("MaskWorld");   //Create the program window or frame.  Names it.

        panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
        panel.setLayout(null);   //set the layout

        // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
        // and trap input events (Mouse and Keyboard events)
        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);  // adds the canvas to the panel.

        // frame operations
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
        frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
        frame.setResizable(false);   //makes it so the frame cannot be resized
        frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!

        // sets up things so the screen displays images nicely.
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
        System.out.println("DONE graphic setup");

        canvas.addKeyListener(this);
    }

    //Pauses or sleeps the computer for the amount specified in milliseconds
    public void pause(int time) {
        //sleep
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        System.out.println("key code: " + keyCode);

        if (keyCode == 38) {
            Angela.up = true;
        }
        if (keyCode == 40) {
            Angela.down = true;
        }
        if (keyCode == 37) {
            Angela.left = true;
        }

        if (keyCode == 39) {
            Angela.right = true;
        }

        if (keyCode == 87) {
            Mia.up = true;
        }
        if (keyCode == 83) {
            Mia.down = true;
        }
        if (keyCode == 65) {
            Mia.left = true;
        }

        if (keyCode == 68) {
            Mia.right = true;
        }

        if (keyCode == 32) {
            Angela.dy = -15;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == 38) {
            Angela.up = false;
        }

        if (keyCode == 40) {
            Angela.down = false;
        }

        if (keyCode == 37) {
            Angela.left = false;
        }

        if (keyCode == 39) {
            Angela.right = false;
        }

        if (keyCode == 87) {
            Mia.up = false;
        }
        if (keyCode == 83) {
            Mia.down = false;
        }
        if (keyCode == 65) {
            Mia.left = false;
        }

        if (keyCode == 68) {
            Mia.right = false;
        }
    }
}
