import java.awt.*;

public class Friend2 {

    public int xpos;                //the x position
    public int ypos;                //the y position
    public int width;
    public int height;
    public boolean isAlive;            //a boolean to denote if the hero is alive or dead.
    public int dx;                    //the speed of the hero in the x direction
    public int dy;                    //the speed of the hero in the y direction
    public Rectangle rec;
    public Image pic;
    public int score;


    public boolean right;
    public boolean down;
    public boolean left;
    public boolean up;

    public Friend2 (int pwidth, int pheight, int pXpos, int pYpos, int dxParameter, int dyParameter, int pScore) {
        xpos = pXpos;
        ypos = pYpos;
        width = pwidth;
        height = pheight;
        dx = dxParameter;
        dy = dyParameter;
        isAlive = true;
        rec = new Rectangle(xpos, ypos, width, height);
        score = pScore;
    }

    public void move() {
        xpos = xpos + dx;
        ypos = ypos + dy;

        if (xpos<0){
            xpos = 1;
        }

        if (xpos > 1000-width){
            xpos = 1000-width;
        }

        if (ypos < 0){
            ypos = 1;
        }

        if (ypos > 800-height){
            ypos = 800-height;
        }

        rec = new Rectangle(xpos, ypos, width, height);
    }

    public Rectangle getNextRectangle() {
        return new Rectangle(rec.x + dx, rec.y + dy, rec.width, rec.height);
    }

}
