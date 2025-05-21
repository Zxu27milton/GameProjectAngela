
import java.awt.*;

public class Gem {

    public int xpos;                //the x position
    public int ypos;                //the y position
    public int width;
    public int height;
    public boolean isAlive;            //a boolean to denote if the gem has been collected
    public Rectangle rec;
    public Image pic;

    public Gem(int pXpos, int pYpos) {

        xpos = pXpos;
        ypos = pYpos;
        width = 10;
        height = 10;
        isAlive = true;
        rec = new Rectangle(xpos, ypos, width, height);


    }

}
