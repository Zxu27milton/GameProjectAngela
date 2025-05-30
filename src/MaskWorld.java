import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import javax.swing.*;

public class MaskWorld implements Runnable, KeyListener {

    //Creating the start screen
    public boolean startscreen = true;
    public Image startScreenImage;

    // Sets the width and height of the program window
    final int WIDTH = 1000;
    final int HEIGHT = 800;

    // Declare the variables needed for the graphics
    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;
    public BufferStrategy bufferStrategy;

    //When all gems are collected or characters die
    public boolean gameOver = false;

    public Rectangle wallTemp = new Rectangle(0, 0, 0, 0);

    // Declare the character objects
    public Friend1 angela;
    public Friend2 mia;

    // Set up the theme music soundtrack
    public SoundFile themeMusic;

    // Set up the maze
    public Maze angelaRandomMaze;
    public Maze miaRandomMaze;

    // Set up arrays
    public Gem[][] manyGemsAngela;
    public Gem[][] manyGemsMia;

    // Set up something to count how many gems are made so the gameover code is easier to write
    public int totalAliveAngelaGems = 0;
    public int totalAliveMiaGems = 0;

    // Declare the spikes as obstacles
    public Spikes[] angelaSpike;
    public Spikes[] miaSpike;

    // Set up the levels
    public int levels;

    public static void main(String[] args) {
        MaskWorld myApp = new MaskWorld();
        new Thread(myApp).start();
    }

    public MaskWorld() {
        setUpGraphics();

        // Setting up the mazes
        angelaRandomMaze = new Maze();
        angelaRandomMaze.createWalls();

        miaRandomMaze = new Maze();
        miaRandomMaze.createWalls();

        // Set up players
        angela = new Friend1(20, 20, 10, 60, 0, 0, 0);
        mia = new Friend2(20, 20, 10, 460, 0, 0, 0);

        // Gem Array 1 for Angela
        manyGemsAngela = new Gem[16][8];
        for (int i = 0; i < manyGemsAngela.length; i++) {
            for (int z = 0; z < manyGemsAngela[0].length; z++) {
                manyGemsAngela[i][z] = new Gem(i * 50 + 10, z * 50 + 10);

                // Disable gem if it's in a wall
                if (angelaRandomMaze.mazeLayout[i][z] == 1) {
                    manyGemsAngela[i][z].isAlive = false;
                }

                // Disable gem if it's overlapping Angela
                if (Math.abs(manyGemsAngela[i][z].xpos - angela.xpos) <= angela.width &&
                        Math.abs(manyGemsAngela[i][z].ypos - angela.ypos) <= angela.height) {
                    manyGemsAngela[i][z].isAlive = false;
                }

                // Count only alive gems
                if (manyGemsAngela[i][z].isAlive) {
                    totalAliveAngelaGems++;
                }
            }
        }

        // Gem Array 2 for Mia, same as above so didn't re-comment
        manyGemsMia = new Gem[16][8];
        for (int i = 0; i < manyGemsMia.length; i++) {
            for (int z = 0; z < manyGemsMia[0].length; z++) {
                manyGemsMia[i][z] = new Gem(i * 50 + 10, z * 50 + 410);
                if (miaRandomMaze.mazeLayout[i][z] == 1) {
                    manyGemsMia[i][z].isAlive = false;
                }
                if (Math.abs(manyGemsMia[i][z].xpos - mia.xpos) <= mia.width &&
                        Math.abs(manyGemsMia[i][z].ypos - mia.ypos) <= mia.height) {
                    manyGemsMia[i][z].isAlive = false;
                }
                if (manyGemsMia[i][z].isAlive) {
                    totalAliveMiaGems++;
                }
            }
        }

        // LOAD IMAGES

        // Character Images
        angela.pic = Toolkit.getDefaultToolkit().getImage("Angela.png");
        mia.pic = Toolkit.getDefaultToolkit().getImage("Mia.png");

        // Gem Images
        for (int i = 0; i < manyGemsAngela.length; i++) {
            for (int z = 0; z < manyGemsAngela[0].length; z++) {
                manyGemsAngela[i][z].pic = Toolkit.getDefaultToolkit().getImage("Angelagem.png");
            }
        }

        for (int i = 0; i < manyGemsMia.length; i++) {
            for (int z = 0; z < manyGemsMia[0].length; z++) {
                manyGemsMia[i][z].pic = Toolkit.getDefaultToolkit().getImage("Miagem.png");
            }
        }

        //Setting up and playing the theme music
        themeMusic = new SoundFile("Thememusic.wav");
        themeMusic.loop();

        //Set up start screen imagery
        startScreenImage = Toolkit.getDefaultToolkit().getImage("Startscreen.png");
    }

    public void restartGame() {
        gameOver = false;
        resetCharactersandScoresandGems();
        render();
    }

    public void level2() {
        resetCharactersandScoresandGems();
        render();
    }

    public void resetCharactersandScoresandGems() {
        //just re-copied most of MaskWorld so everything would reset
        //Reset players
        angela = new Friend1(20, 20, 10, 60, 0, 0, 0);
        mia = new Friend2(20, 20, 10, 460, 0, 0, 0);

        //Reset scores
        angela.score = 0;
        mia.score = 0;

        //Reset gems & Spikes
        totalAliveAngelaGems = 0;
        totalAliveMiaGems = 0;

        // Gem Array 1 for Angela
        manyGemsAngela = new Gem[16][8];
        for (int i = 0; i < manyGemsAngela.length; i++) {
            for (int z = 0; z < manyGemsAngela[0].length; z++) {
                manyGemsAngela[i][z] = new Gem(i * 50 + 10, z * 50 + 10);
                // Disable gem if it's in a wall
                if (angelaRandomMaze.mazeLayout[i][z] == 1) {
                    manyGemsAngela[i][z].isAlive = false;
                }

                // Disable gem if it's overlapping Angela
                if (Math.abs(manyGemsAngela[i][z].xpos - angela.xpos) <= angela.width &&
                        Math.abs(manyGemsAngela[i][z].ypos - angela.ypos) <= angela.height) {
                    manyGemsAngela[i][z].isAlive = false;
                }

                // Count only alive gems
                if (manyGemsAngela[i][z].isAlive) {
                    totalAliveAngelaGems++;
                }
            }
        }

        // Spike Array for Angela
        angelaSpike = new Spikes[30];
        for (int i = 0; i < angelaSpike.length; i++) {
            int randomInt1 = (int) (Math.random() * 400 + 1);
            int randomInt2 = (int) (Math.random() * 800 + 1);
            angelaSpike[i] = new Spikes(randomInt2, randomInt1, 10, 10);
            angelaSpike[i].pic = Toolkit.getDefaultToolkit().getImage("Spikes.png");
            if (Math.abs(angelaSpike[i].xpos - angela.xpos) <= angela.width &&
                    Math.abs(angelaSpike[i].ypos - angela.ypos) <= angela.height) {
                angelaSpike[i].isAlive = false;
            } else {
                angelaSpike[i].isAlive = true;
            }
            for (int a = 0; a < angelaRandomMaze.walls.length; a++) {
//                System.out.println("rec " + AngelarandomMaze.walls[a].rec);
                if (angelaRandomMaze.walls[a] != null && angelaSpike[i].rec.intersects(angelaRandomMaze.walls[a].rec)) {
//                    System.out.println("killing angela spike because in the wall");
                    angelaSpike[i].isAlive = false;
                }
            }
            for (int b = 0; b < manyGemsAngela.length; b++) {
                for (int z = 0; z < manyGemsAngela[0].length; z++) {
                    if (angelaSpike[i].rec.intersects(manyGemsAngela[b][z].rec)){
                        angelaSpike[i].isAlive = false;
                    }
                }
            }
        }

        // Gem Array 2 for Mia, same as above so didn't re-comment
        manyGemsMia = new Gem[16][8];
        for (int i = 0; i < manyGemsMia.length; i++) {
            for (int z = 0; z < manyGemsMia[0].length; z++) {
                manyGemsMia[i][z] = new Gem(i * 50 + 10, z * 50 + 410);
                if (miaRandomMaze.mazeLayout[i][z] == 1) {
                    manyGemsMia[i][z].isAlive = false;
                }
                if (Math.abs(manyGemsMia[i][z].xpos - mia.xpos) <= mia.width &&
                        Math.abs(manyGemsMia[i][z].ypos - mia.ypos) <= mia.height) {
                    manyGemsMia[i][z].isAlive = false;
                }
                if (manyGemsMia[i][z].isAlive) {
                    totalAliveMiaGems++;
                }
            }
        }

        // Spike Array for Mia
        miaSpike = new Spikes[30];
        for (int i = 0; i < miaSpike.length; i++) {
            int randomInt3 = (int) (Math.random() * 400 + 401);
            int randomInt4 = (int) (Math.random() * 800 + 1);
            miaSpike[i] = new Spikes(randomInt4, randomInt3, 10, 10);
            miaSpike[i].pic = Toolkit.getDefaultToolkit().getImage("Spikes.png");
            if (Math.abs(miaSpike[i].xpos - mia.xpos) <= mia.width &&
                    Math.abs(miaSpike[i].ypos - mia.ypos) <= mia.height) {
                miaSpike[i].isAlive = false;
            } else {
                miaSpike[i].isAlive = true;
            }
            for (int c = 0; c < miaRandomMaze.walls.length; c++) {
                if (miaRandomMaze.walls[c] != null) {
                    Rectangle r = new Rectangle(miaRandomMaze.walls[c].rec.x, miaRandomMaze.walls[c].rec.y+400, miaRandomMaze.walls[c].rec.width, miaRandomMaze.walls[c].rec.height);
                    if (miaSpike[i].rec.intersects(r)) {
                        miaSpike[i].isAlive = false;
                    }
                }
            }
            for (int b = 0; b < manyGemsMia.length; b++) {
                for (int z = 0; z < manyGemsMia[0].length; z++) {
                    if (miaSpike[i].rec.intersects(manyGemsMia[b][z].rec)){
                        miaSpike[i].isAlive = false;
                    }
                }
            }
        }

        angela.pic = Toolkit.getDefaultToolkit().getImage("Angela.png");
        mia.pic = Toolkit.getDefaultToolkit().getImage("Mia.png");

        for (int i = 0; i < manyGemsAngela.length; i++) {
            for (int z = 0; z < manyGemsAngela[0].length; z++) {
                manyGemsAngela[i][z].pic = Toolkit.getDefaultToolkit().getImage("Angelagem.png");
            }
        }

        for (int i = 0; i < manyGemsMia.length; i++) {
            for (int z = 0; z < manyGemsMia[0].length; z++) {
                manyGemsMia[i][z].pic = Toolkit.getDefaultToolkit().getImage("Miagem.png");
            }
        }
    }

    public void run() {
        while (true) {
            if (startscreen == true){
                renderstartscreen();
            } else {
                if (gameOver == false) {
                    moveThings(); // Move all the game objects
                }
                checkIntersections();   // Check character collisions
                render(); // Paint graphics & report the score
            }
            pause(20);
            checkKeys();
        }
    }

    public void moveThings() {
        boolean AngelawillCollide = false;
        boolean MiawillCollide = false;

        Rectangle AngelanextPosition = angela.getNextRectangle();
        Rectangle MianextPosition = mia.getNextRectangle();

        for (int i = 0; i < angelaRandomMaze.mazeLayout.length; i++) {
            for (int z = 0; z < angelaRandomMaze.mazeLayout[0].length; z++) {
                if (angelaRandomMaze.mazeLayout[i][z] == 1) {
                    Rectangle wallTemp = new Rectangle(i * 50, z * 50, 50, 50);
                    if (AngelanextPosition.intersects(wallTemp)) {
                        AngelawillCollide = true;
                        break; //means stop nearest loop
                    }
                }
            }
            if (AngelawillCollide) break;
        }

        for (int i = 0; i < miaRandomMaze.mazeLayout.length; i++) {
            for (int z = 0; z < miaRandomMaze.mazeLayout[0].length; z++) {
                if (miaRandomMaze.mazeLayout[i][z] == 1) {
                    Rectangle wallTemp = new Rectangle(i * 50, z * 50 + 400, 50, 50);
                    if (MianextPosition.intersects(wallTemp)) {
                        MiawillCollide = true;
                        break;
                    }
                }
            }
            if (MiawillCollide) break;
        }

        if (AngelawillCollide == false) {
            angela.move();
        } else {
            System.out.println("Angela is touching a wall and cannot move");
        }

        if (MiawillCollide == false){
            mia.move();
        } else {
            System.out.println("Mia is touching a wall and cannot move");
        }
    }

    public void checkIntersections() {
        //Angela collecting gems
        for (int i = 0; i < manyGemsAngela.length; i++) {
            for (int z = 0; z < manyGemsAngela[0].length; z++) {
                if (angela.rec.intersects(manyGemsAngela[i][z].rec)) {
                    if (manyGemsAngela[i][z].isAlive) {
                        manyGemsAngela[i][z].isAlive = false;
                        angela.score++;
                        new SoundFile("gemCollect.wav").play();
                        //gemCollect is here so that it can overlap
                    }
                }
            }
        }

        //Mia collecting gems
        for (int i = 0; i < manyGemsMia.length; i++) {
            for (int z = 0; z < manyGemsMia[0].length; z++) {
                if (mia.rec.intersects(manyGemsMia[i][z].rec)) {
                    if (manyGemsMia[i][z].isAlive) {
                        manyGemsMia[i][z].isAlive = false;
                        mia.score++;
                        new SoundFile("gemCollect.wav").play();
                    }
                }
            }
        }

        //Angela & Mia intersecting spikes
        if (levels == 1) {
            for (int i = 0; i < angelaSpike.length; i++) {
                if (angelaSpike[i].isAlive) {
                    if (angela.rec.intersects(angelaSpike[i].rec)) {
                        themeMusic.pause();
                        if (angela.isAlive) {
                            new SoundFile("Death.wav").play();
                            angela.isAlive = false;
                        }
                    }
                }
            }
            for (int i = 0; i < miaSpike.length; i++) {
                if (miaSpike[i].isAlive){
                    if (mia.rec.intersects(miaSpike[i].rec)){
                        themeMusic.pause();
                        if (mia.isAlive) {
                            new SoundFile("Death.wav").play();
                            angela.isAlive = false;
                        }
                    }
                }
            }
        }

        if (angela.isAlive == false){
            gameOver = true;
        }
        if (mia.isAlive == false){
            gameOver = true;
        }
    }

    public void renderstartscreen(){
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.drawImage(startScreenImage, 0,0, WIDTH, HEIGHT, null);
        g.dispose();
        bufferStrategy.show();
    }

    public void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.fillRect(0, 0, WIDTH, HEIGHT);

        Font font1 = new Font("SansSerif", Font.BOLD, 16);
        Font font2 = new Font("SanSerif", Font.BOLD, 12 );
        g.setFont(font1);
        g.setColor(Color.white);
        g.drawString("Player 1: " + angela.score, 875, 100);
        g.drawString("Player 2: " + mia.score, 875, 150);

        angelaRandomMaze.draw(g, 0);
        miaRandomMaze.draw(g, 400);

        for (int i = 0; i < manyGemsAngela.length; i++) {
            for (int z = 0; z < manyGemsAngela[0].length; z++) {
                if (manyGemsAngela[i][z].isAlive) {
                    g.drawImage(manyGemsAngela[i][z].pic, manyGemsAngela[i][z].xpos, manyGemsAngela[i][z].ypos,
                            manyGemsAngela[i][z].width, manyGemsAngela[i][z].height, null);
                }
            }
        }

        for (int i = 0; i < manyGemsMia.length; i++) {
            for (int z = 0; z < manyGemsMia[0].length; z++){
                if (manyGemsMia[i][z].isAlive) {
                    g.drawImage(manyGemsMia[i][z].pic, manyGemsMia[i][z].xpos, manyGemsMia[i][z].ypos,
                            manyGemsMia[i][z].width, manyGemsMia[i][z].height, null);
                }
            }
        }



        if (mia.isAlive) {
            g.drawImage(mia.pic, mia.xpos, mia.ypos, mia.width, mia.height, null);
        }

        if (angela.isAlive) {
            g.drawImage(angela.pic, angela.xpos, angela.ypos, angela.width, angela.height, null);
        }

        if (angela.score == totalAliveAngelaGems || mia.score == totalAliveMiaGems) {
            gameOver = true;
        }

        if (levels >= 1 && angelaSpike != null) {
            // Spike Array for Angela
            for (int i = 0; i < angelaSpike.length; i++) {
                if (angelaSpike[i].isAlive == true) {
                    g.drawImage(angelaSpike[i].pic, angelaSpike[i].xpos, angelaSpike[i].ypos, angelaSpike[i].width, angelaSpike[i].height, null);
                }
            }
        }
        if (levels >=1 && miaSpike != null) {
            // Spike Array for Mia
            for (int i = 0; i < miaSpike.length; i++) {
                if (miaSpike[i].isAlive == true){
//                g.drawRect(Miaspike[i].rec.x, Miaspike[i].rec.y, Miaspike[i].rec.width, Miaspike[i].rec.height);
                    g.drawImage(miaSpike[i].pic, miaSpike[i].xpos, miaSpike[i].ypos, miaSpike[i].width, miaSpike[i].height, null);
                }
            }
        }

        if (gameOver) {
            g.setFont(font1);
            g.setColor(new Color(250, 250, 250));
            g.drawString("Gameover", 850, 450);

            g.setFont(font2);
            g.drawString("Restart: Return", 850, 470);
            g.drawString("Next Level: Shift", 850, 490);

            g.setFont(font1);
            if (angela.score > mia.score) {
                g.setColor(new Color(189, 227, 250));
                g.drawString("Player 1 Won", 850, 420);
            } else if (angela.score == mia.score) {
                g.drawString("You tied", 850, 420);
            } else {
                g.setColor(new Color(250, 230, 250));
                g.drawString("Player 2 Won", 850, 420);
            }
        } // game over screen

        g.dispose();
        bufferStrategy.show();
    }

    public void setUpGraphics() {
        frame = new JFrame("MaskWorld");
        panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setLayout(null);

        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();

        System.out.println("DONE graphic setup");

        canvas.addKeyListener(this);
    }

    public void pause(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // Ignore
        }
    }

    public void checkKeys() {
        if (angela.up) {
            angela.dy = -4;
        } else if (angela.down) {
            angela.dy = 4;
        } else {
            angela.dy = 0;
        }
        if (angela.left) {
            angela.dx = -4;
        } else if (angela.right) {
            angela.dx = 4;
        } else {
            angela.dx = 0;
        }

        if (mia.up) {
            mia.dy = -4;
        } else if (mia.down) {
            mia.dy = 4;
        } else {
            mia.dy = 0;
        }
        if (mia.left) {
            mia.dx = -4;
        } else if (mia.right) {
            mia.dx = 4;
        } else {
            mia.dx = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        System.out.println("key code: " + keyCode);

        if (keyCode == 38) angela.up = true;
        if (keyCode == 40) angela.down = true;
        if (keyCode == 37) angela.left = true;
        if (keyCode == 39) angela.right = true;

        if (keyCode == 87) mia.up = true;
        if (keyCode == 83) mia.down = true;
        if (keyCode == 65) mia.left = true;
        if (keyCode == 68) mia.right = true;

        if (keyCode == 32) startscreen = false;

        if (keyCode == 10 && gameOver == true) {
            System.out.println("Game is restarting (pressed return)");
            restartGame();
        }

        if (keyCode == 16) {
            levels++;
            if (levels == 1){
                level2();
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == 38) angela.up = false;
        if (keyCode == 40) angela.down = false;
        if (keyCode == 37) angela.left = false;
        if (keyCode == 39) angela.right = false;

        if (keyCode == 87) mia.up = false;
        if (keyCode == 83) mia.down = false;
        if (keyCode == 65) mia.left = false;
        if (keyCode == 68) mia.right = false;
    }
}
