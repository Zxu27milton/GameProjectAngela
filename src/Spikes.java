
import java.awt.*;

public class Spikes {

    public int xpos;                //the x position
    public int ypos;                //the y position
    public int width;
    public int height;
    public boolean isAlive;
    public Rectangle rec;
    public Image pic;

    public Spikes(int pXpos, int pYpos, int pwidth, int pheight) {

        xpos = pXpos;
        ypos = pYpos;
        width = pwidth;
        height = pheight;
        isAlive = true;
        rec = new Rectangle(xpos, ypos, width, height);


    }

}
