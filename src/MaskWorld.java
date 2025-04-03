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
    public Gem1 gem;

    //Set up array
    public Gem1[] manyGems;

    public String winner = "";
    public boolean gameOver = false;

    public static void main(String[] args) {
        MaskWorld myApp = new MaskWorld();   //creates a new instance of the game
        new Thread(myApp).start();               //creates a threads & starts up the code in the run( ) method
    }

    public MaskWorld(){
        setUpGraphics();

    }

    public void setUpGraphics(){

    }

}
