import java.awt.*;

public class Wall {
    int xpos;
    int ypos;
    int width;
    int height;
    Rectangle rec;

    public Wall (int paramxpos, int paramypos, int paramwidth, int paramheight){
        xpos = paramxpos;
        ypos = paramypos;
        width = paramwidth;
        height = paramheight;
        rec = new Rectangle (xpos, ypos, width, height);
    }

    public void draw(Graphics2D g){ //I asked for help on this method from Noah
        g.setColor(Color.white);
        g.fillRect(xpos, ypos, width, height);
    }
}
