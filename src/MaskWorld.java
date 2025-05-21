import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import javax.swing.*;

public class MaskWorld implements Runnable, KeyListener {

    //Creating the start screen
    public boolean startscreen = true;
    public Image StartScreenImage;

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
    public Friend1 Angela;
    public Friend2 Mia;

    // Set up the theme music soundtrack
    public SoundFile themeMusic;

    // Set up the maze
    public Maze AngelarandomMaze;
    public Maze MiarandomMaze;

    // Set up arrays
    public Gem[][] manyGemsAngela;
    public Gem[][] manyGemsMia;

    // Set up something to count how many gems are made so the gameover code is easier to write
    public int totalAliveAngelaGems = 0;
    public int totalAliveMiaGems = 0;

    // Declare the spikes as obstacles
    public Spikes [] Angelaspike;
    public Spikes [] Miaspike;

    // Set up the levels
    public int levels;

    public static void main(String[] args) {
        MaskWorld myApp = new MaskWorld();
        new Thread(myApp).start();
    }

    public MaskWorld() {
        setUpGraphics();

        // Setting up the mazes
        AngelarandomMaze = new Maze();
        AngelarandomMaze.createWalls();

        MiarandomMaze = new Maze();
        MiarandomMaze.createWalls();

        // Set up players
        Angela = new Friend1(20, 20, 10, 60, 0, 0, 0);
        Mia = new Friend2(20, 20, 10, 460, 0, 0, 0);

        // Spike Array for Angela
        Angelaspike = new Spikes[20];
        for (int i = 0; i < Angelaspike.length; i++) {
            int randomInt1 = (int) (Math.random() * 1000 + 1);
            Angelaspike[i] = new Spikes(randomInt1, i * 50 + 10, 10, 10);
            if (Math.abs(Angelaspike[i].xpos - Angela.xpos) <= Angela.width &&
                    Math.abs(Angelaspike[i].ypos - Angela.ypos) <= Angela.height) {
                Angelaspike[i].isAlive = false;
            }
            System.out.println("spike x pos " + randomInt1);
        }

        // Gem Array 1 for Angela
        manyGemsAngela = new Gem[16][8];
        for (int i = 0; i < manyGemsAngela.length; i++) {
            for (int z = 0; z < manyGemsAngela[0].length; z++) {
                manyGemsAngela[i][z] = new Gem(i * 50 + 10, z * 50 + 10);

                // Disable gem if it's in a wall
                if (AngelarandomMaze.mazeLayout[i][z] == 1) {
                    manyGemsAngela[i][z].isAlive = false;
                    Angelaspike[i].isAlive = false;
                }

                // Disable gem if it's overlapping Angela
                if (Math.abs(manyGemsAngela[i][z].xpos - Angela.xpos) <= Angela.width &&
                        Math.abs(manyGemsAngela[i][z].ypos - Angela.ypos) <= Angela.height) {
                    manyGemsAngela[i][z].isAlive = false;
                }

                // Count only alive gems
                if (manyGemsAngela[i][z].isAlive) {
                    totalAliveAngelaGems++;
                }
            }
        }

        //Spike Array for Mia
        Miaspike = new Spikes[20];
        for (int i = 0; i < Angelaspike.length; i++) {
            int randomInt2 = (int) (Math.random() * 1000 + 1);
            Miaspike[i] = new Spikes(randomInt2, i * 50 + 10, 10, 10);
            if (Math.abs(Miaspike[i].xpos - Mia.xpos) <= Mia.width &&
                    Math.abs(Miaspike[i].ypos - Mia.ypos) <= Mia.height) {
                Miaspike[i].isAlive = false;
            }
        }

        // Gem Array 2 for Mia, same as above so didn't re-comment
        manyGemsMia = new Gem[16][8];
        for (int i = 0; i < manyGemsMia.length; i++) {
            for (int z = 0; z < manyGemsMia[0].length; z++) {
                manyGemsMia[i][z] = new Gem(i * 50 + 10, z * 50 + 410);
                if (MiarandomMaze.mazeLayout[i][z] == 1) {
                    manyGemsMia[i][z].isAlive = false;
                    Miaspike[i].isAlive = false;
                }
                if (Math.abs(manyGemsMia[i][z].xpos - Mia.xpos) <= Mia.width &&
                        Math.abs(manyGemsMia[i][z].ypos - Mia.ypos) <= Mia.height) {
                    manyGemsMia[i][z].isAlive = false;
                }
                if (manyGemsMia[i][z].isAlive) {
                    totalAliveMiaGems++;
                }
            }
        }

        // LOAD IMAGES

        // Character Images
        Angela.pic = Toolkit.getDefaultToolkit().getImage("Angela.png");
        Mia.pic = Toolkit.getDefaultToolkit().getImage("Mia.png");

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

        // Spike Images
        for (int i = 0; i < Angelaspike.length; i++) {
            Angelaspike[i].pic = Toolkit.getDefaultToolkit().getImage("Spikes.png");
        }

        for (int i = 0; i < Miaspike.length; i++) {
            Miaspike[i].pic = Toolkit.getDefaultToolkit().getImage("Spikes.png");
        }

        //Setting up and playing the theme music
        themeMusic = new SoundFile("Thememusic.wav");
        themeMusic.loop();

        //Set up start screen imagery
        StartScreenImage = Toolkit.getDefaultToolkit().getImage("StartScreen.jpeg");

        //What happens if characters die
        if (Angela.isAlive == false){
            gameOver = true;
        }
        if (Mia.isAlive == false){
            gameOver = true;
        }
    }

    public void restartGame(){
        gameOver = false;
        resetCharactersandScoresandGems();
        render();
    }

    public void level2(){
        resetCharactersandScoresandGems();
    }

    public void resetCharactersandScoresandGems(){
        //just re-copied most of MaskWorld so everything would reset
        //Reset players
        Angela = new Friend1(20, 20, 10, 60, 0, 0, 0);
        Mia = new Friend2(20, 20, 10, 460, 0, 0, 0);

        //Reset scores
        Angela.score = 0;
        Mia.score = 0;

        //Reset gems & Spikes
        totalAliveAngelaGems = 0;
        totalAliveMiaGems = 0;

        // Spike Array for Angela
        Angelaspike = new Spikes[20];
        for (int i = 0; i < Angelaspike.length; i++) {
            int randomInt1 = (int) (Math.random() * 1000 + 1);
            Angelaspike[i] = new Spikes(randomInt1, i * 50 + 10, 10, 10);
            if (Math.abs(Angelaspike[i].xpos - Angela.xpos) <= Angela.width &&
                    Math.abs(Angelaspike[i].ypos - Angela.ypos) <= Angela.height) {
                Angelaspike[i].isAlive = false;
            }
            System.out.println("spike x pos " + randomInt1);
        }

        // Gem Array 1 for Angela
        manyGemsAngela = new Gem[16][8];
        for (int i = 0; i < manyGemsAngela.length; i++) {
            for (int z = 0; z < manyGemsAngela[0].length; z++) {
                manyGemsAngela[i][z] = new Gem(i * 50 + 10, z * 50 + 10);

                // Disable gem if it's in a wall
                if (AngelarandomMaze.mazeLayout[i][z] == 1) {
                    manyGemsAngela[i][z].isAlive = false;
                    Angelaspike[i].isAlive = false;
                }

                // Disable gem if it's overlapping Angela
                if (Math.abs(manyGemsAngela[i][z].xpos - Angela.xpos) <= Angela.width &&
                        Math.abs(manyGemsAngela[i][z].ypos - Angela.ypos) <= Angela.height) {
                    manyGemsAngela[i][z].isAlive = false;
                }

                // Count only alive gems
                if (manyGemsAngela[i][z].isAlive) {
                    totalAliveAngelaGems++;
                }
            }
        }

        //Spike Array for Mia
        Miaspike = new Spikes[20];
        for (int i = 0; i < Angelaspike.length; i++) {
            int randomInt2 = (int) (Math.random() * 1000 + 1);
            Miaspike[i] = new Spikes(randomInt2, i * 50 + 10, 10, 10);
            if (Math.abs(Miaspike[i].xpos - Mia.xpos) <= Mia.width &&
                    Math.abs(Miaspike[i].ypos - Mia.ypos) <= Mia.height) {
                Miaspike[i].isAlive = false;
            }
        }

        // Gem Array 2 for Mia, same as above so didn't re-comment
        manyGemsMia = new Gem[16][8];
        for (int i = 0; i < manyGemsMia.length; i++) {
            for (int z = 0; z < manyGemsMia[0].length; z++) {
                manyGemsMia[i][z] = new Gem(i * 50 + 10, z * 50 + 410);
                if (MiarandomMaze.mazeLayout[i][z] == 1) {
                    manyGemsMia[i][z].isAlive = false;
                    Miaspike[i].isAlive = false;
                }
                if (Math.abs(manyGemsMia[i][z].xpos - Mia.xpos) <= Mia.width &&
                        Math.abs(manyGemsMia[i][z].ypos - Mia.ypos) <= Mia.height) {
                    manyGemsMia[i][z].isAlive = false;
                }
                if (manyGemsMia[i][z].isAlive) {
                    totalAliveMiaGems++;
                }
            }
        }

        Angela.pic = Toolkit.getDefaultToolkit().getImage("Angela.png");
        Mia.pic = Toolkit.getDefaultToolkit().getImage("Mia.png");

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

        Rectangle AngelanextPosition = Angela.getNextRectangle();
        Rectangle MianextPosition = Mia.getNextRectangle();

        for (int i = 0; i < AngelarandomMaze.mazeLayout.length; i++) {
            for (int z = 0; z < AngelarandomMaze.mazeLayout[0].length; z++) {
                if (AngelarandomMaze.mazeLayout[i][z] == 1) {
                    Rectangle wallTemp = new Rectangle(i * 50, z * 50, 50, 50);
                    if (AngelanextPosition.intersects(wallTemp)) {
                        AngelawillCollide = true;
                        break; //means stop nearest loop
                    }
                }
            }
            if (AngelawillCollide) break;
        }

        for (int i = 0; i < MiarandomMaze.mazeLayout.length; i++) {
            for (int z = 0; z < MiarandomMaze.mazeLayout[0].length; z++) {
                if (MiarandomMaze.mazeLayout[i][z] == 1) {
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
            Angela.move();
        } else {
            System.out.println("Angela is touching a wall and cannot move");
        }

        if (MiawillCollide == false){
            Mia.move();
        } else {
            System.out.println("Mia is touching a wall and cannot move");
        }
    }

    public void checkIntersections() {
        //Angela collecting gems
        for (int i = 0; i < manyGemsAngela.length; i++) {
            for (int z = 0; z < manyGemsAngela[0].length; z++) {
                if (Angela.rec.intersects(manyGemsAngela[i][z].rec)) {
                    if (manyGemsAngela[i][z].isAlive) {
                        manyGemsAngela[i][z].isAlive = false;
                        Angela.score++;
                        new SoundFile("gemCollect.wav").play();
                        //gemCollect is here so that it can overlap
                    }
                }
            }
        }

        //Mia collecting gems
        for (int i = 0; i < manyGemsMia.length; i++) {
            for (int z = 0; z < manyGemsMia[0].length; z++) {
                if (Mia.rec.intersects(manyGemsMia[i][z].rec)) {
                    if (manyGemsMia[i][z].isAlive) {
                        manyGemsMia[i][z].isAlive = false;
                        Mia.score++;
                        new SoundFile("gemCollect.wav").play();
                    }
                }
            }
        }

        //Angela & Mia intersecting spikes
        if (levels == 2) {
            for (int i = 0; i < Angelaspike.length; i++) {
                if (Angela.rec.intersects(Angelaspike[i].rec)){
                    if (Angela.isAlive) {
                        Angela.isAlive = false;
                        new SoundFile("Death.wav").play();
                        System.out.println("Angela died at " + i + " with coords " + Angelaspike[i].rec.x + ", " + Angelaspike[i].rec.y);
                    }
                }
                if (Mia.rec.intersects(Miaspike[i].rec)){
                    if (Mia.isAlive){
                        Mia.isAlive = false;
                        new SoundFile("Death.wav").play();
                    }
                }
            }
        }
    }

    public void renderstartscreen(){
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.drawImage(StartScreenImage, 0,0, WIDTH, HEIGHT, null);
        g.dispose();
        bufferStrategy.show();
    }

    public void renderSpikes(){
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        for (int i = 0; i < Angelaspike.length; i++) {
            g.drawImage(Angelaspike[i].pic, Angelaspike[i].xpos, Angelaspike[i].ypos, 5, 5, null);
        }
        for (int i = 0; i < Miaspike.length; i++) {
            g.drawImage(Miaspike[i].pic, Miaspike[i].xpos,Miaspike[i].ypos, 5, 5, null);
        }
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
        g.drawString("Player 1: " + Angela.score, 875, 100);
        g.drawString("Player 2: " + Mia.score, 875, 150);

        AngelarandomMaze.draw(g, 0);
        MiarandomMaze.draw(g, 400);

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



        if (Mia.isAlive) {
            g.drawImage(Mia.pic, Mia.xpos, Mia.ypos, Mia.width, Mia.height, null);
        }

        if (Angela.isAlive) {
            g.drawImage(Angela.pic, Angela.xpos, Angela.ypos, Angela.width, Angela.height, null);
        }

        if (Angela.score == totalAliveAngelaGems || Mia.score == totalAliveMiaGems) {
            gameOver = true;
        }

        if (levels == 2) {
//            renderSpikes();
            for (int i = 0; i < Angelaspike.length; i++) {
                g.drawImage(Angelaspike[i].pic, Angelaspike[i].xpos, Angelaspike[i].ypos, 50, 50, null);
                g.setColor(Color.pink);
                g.drawRect(Angelaspike[i].rec.x, Angelaspike[i].rec.y, Angelaspike[i].rec.width, Angelaspike[i].rec.height);
            }
            for (int i = 0; i < Miaspike.length; i++) {
                g.drawImage(Miaspike[i].pic, Miaspike[i].xpos,Miaspike[i].ypos, 5, 5, null);
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
            if (Angela.score > Mia.score) {
                g.setColor(new Color(189, 227, 250));
                g.drawString("Player 1 Won", 850, 420);
            } else if (Angela.score == Mia.score) {
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
        if (Angela.up) {
            Angela.dy = -4;
        } else if (Angela.down) {
            Angela.dy = 4;
        } else {
            Angela.dy = 0;
        }
        if (Angela.left) {
            Angela.dx = -4;
        } else if (Angela.right) {
            Angela.dx = 4;
        } else {
            Angela.dx = 0;
        }

        if (Mia.up) {
            Mia.dy = -4;
        } else if (Mia.down) {
            Mia.dy = 4;
        } else {
            Mia.dy = 0;
        }
        if (Mia.left) {
            Mia.dx = -4;
        } else if (Mia.right) {
            Mia.dx = 4;
        } else {
            Mia.dx = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        System.out.println("key code: " + keyCode);

        if (keyCode == 38) Angela.up = true;
        if (keyCode == 40) Angela.down = true;
        if (keyCode == 37) Angela.left = true;
        if (keyCode == 39) Angela.right = true;

        if (keyCode == 87) Mia.up = true;
        if (keyCode == 83) Mia.down = true;
        if (keyCode == 65) Mia.left = true;
        if (keyCode == 68) Mia.right = true;

        if (keyCode == 32) startscreen = false;

        if (keyCode == 10 && gameOver == true) {
            restartGame();
        }

        if (keyCode == 16) {
            levels++;
            if (levels == 2){
                level2();
            }
            System.out.println("Level" + levels);
        }

        //&& gameOver == true

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == 38) Angela.up = false;
        if (keyCode == 40) Angela.down = false;
        if (keyCode == 37) Angela.left = false;
        if (keyCode == 39) Angela.right = false;

        if (keyCode == 87) Mia.up = false;
        if (keyCode == 83) Mia.down = false;
        if (keyCode == 65) Mia.left = false;
        if (keyCode == 68) Mia.right = false;
    }
}
