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
    public Gem gem;

    //Set up array
    public Gem[] manyGems;

    public String winner = "";
    public boolean gameOver = false;

    public static void main(String[] args) {
        MaskWorld myApp = new MaskWorld();   //creates a new instance of the game
        new Thread(myApp).start();               //creates a threads & starts up the code in the run( ) method
    }

    public MaskWorld() {

        setUpGraphics();

        Angela = new Friend1(650, 250, 0, 0);
        Mia = new Friend2(200, 300, 0, 0)
        gem = new Gem(400, 300, 3, -4);
        // STEP 2 ARRAY
        manyGems = new Gem[5];
        for (int i = 0; i < manyGems.length; i++) {
            manyGems[i] = new Gem((int) (Math.random() * 900), i * 100,
                    (int) (Math.random() * 5 - 2), (int) (Math.random() * 5 - 2));
            while (manyGems[i].dx == 0) {
                manyGems[i].dx = (int) (Math.random() * 5 - 2);
            }
            while (manyGems[i].dy == 0) {
                manyGems[i].dy = (int) (Math.random() * 5 - 2);
            }
        }

        //STEP 3: Make pics for array
        gem.pic = Toolkit.getDefaultToolkit().getImage("cheese.gif");
        Angela.pic = Toolkit.getDefaultToolkit().getImage("jerry.gif");
        Mia.pic = Toolkit.getDefaultToolkit().getImage("tomCat.png");
        for (int i=0; i< manyGems.length; i++) {
            manyGems[i].pic = Toolkit.getDefaultToolkit().getImage("cheese.gif");
        }
    }

    public void setUpGraphics(){

    }

}
