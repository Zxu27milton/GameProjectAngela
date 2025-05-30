import javax.swing.plaf.ColorUIResource;
import java.awt.*;

public class Maze {
   //Credit Noah and google for most of this class.
    // I watched so many youtube tutorials....

    public int [][] mazeLayout;
    Wall[] walls;
    int blockSize = 50;

    public Maze(){
        mazeLayout = new int[][]{
                {1,0,1,1,1,1,1,1},
                {1,0,0,0,1,0,0,1},
                {1,0,1,0,1,1,0,1},
                {1,0,1,0,0,0,0,1},
                {1,0,1,1,1,1,0,1},
                {1,0,0,0,0,1,0,1},
                {1,1,1,1,0,1,0,1},
                {1,0,1,1,0,1,0,1},
                {1,0,0,0,0,1,0,1},
                {1,1,1,0,1,1,0,1},
                {1,1,1,0,1,1,0,1},
                {1,1,1,0,1,1,0,1},
                {1,0,0,0,1,1,0,1},
                {1,0,1,1,1,1,0,1},
                {1,0,0,0,0,1,0,1},
                {1,1,1,1,1,1,0,1}
        };

        createWalls();
        getMazeLayout();
    }

    public void createWalls(){
        walls = new Wall[100];
        int wallIndex = 0; //counts how many walls add
        //also keeps track of next empty spot

        for (int row = 0; row<mazeLayout.length; row = row + 1){
            for (int column = 0; column<mazeLayout[row].length; column = column+1){
                if (mazeLayout[row][column] == 1){
                    walls[wallIndex] = new Wall (row * blockSize,
                            column * blockSize, blockSize, blockSize);
                    wallIndex = wallIndex+1;
                }
            }
        }
    }

    public void draw(Graphics2D g, int yposition){ //pasted part of this from the hackathon
        g.setColor(Color.white);
        for (int i = 0; i < walls.length; i++) {
            if (walls[i] != null) {
                g.fillRect(walls[i].xpos, walls[i].ypos+yposition, walls[i].width, walls[i].height);
                g.setColor(Color.white);
            }
        }
    }

    public int[][] getMazeLayout() {
        return mazeLayout;
    }

}
