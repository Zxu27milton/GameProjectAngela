
import java.awt.*;

public class Gem {

    public int xpos;                //the x position
    public int ypos;                //the y position
    public int width;
    public int height;
    public boolean isAlive;            //a boolean to denote if the gem has been collected
    public int dx;                    //the speed of the hero in the x direction
    public int dy;                    //the speed of the hero in the y direction
    public Rectangle rec;
    public Image pic;
    public int hits;

    // METHOD DEFINITION SECTION

    //This is a constructor that takes 3 parameters.  This allows us to specify the object's name and position when we build it.
    // if you put in a String, an int and an int the program will use this constructor instead of the one above.


    public Gem(int pXpos, int pYpos, int dxParameter, int dyParameter) {

        xpos = pXpos;
        ypos = pYpos;
        width = 10;
        height = 10;
        dx = dxParameter;
        dy = dyParameter;
        isAlive = true;
        hits = 0;
        rec = new Rectangle(xpos, ypos, width, height);


    }

}
