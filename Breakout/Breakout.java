/*
 * File: Breakout.java
 * -------------------
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels */
    public static final int APPLICATION_WIDTH = 400;
    public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
    private static final int WIDTH = APPLICATION_WIDTH;
    private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
    private static final int PADDLE_WIDTH = 60;
    private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
    private static final int PADDLE_Y_OFFSET = 60;

/** Number of bricks per row */
    private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
    private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
    private static final int BRICK_SEP = 4;

/** Width of a brick */
    private static final int BRICK_WIDTH =
      (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
    private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
    private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
    private static final int BRICK_Y_OFFSET = 70;

/** .JAR main method */
    public static void main(String[] args) {
        new Breakout().start(args);
    }
    
/** Runs the Breakout program. */
    public void run() {                
        loader(); // load sound, event listeners, set black canvas
        letsPlay();    // draw game and begin play    
    }
    

/** Draws the board and starts the game */
    private void letsPlay() {
        // draw board
        drawBricks();                    
        drawPaddle();
        
        // start up info
        startup();
        // draw ball
        drawBall();
        // run game
        runBall();
    }
    
/** Loads sound files, event listeners, and sets canvas to black */    
    private void loader() {
        // load sound files and start music
        loadSound();
        
        // add event listeners
        addKeyListeners();
        addMouseListeners();
        
        setBackground(Color.black); // black canvas
    }

/** Load sound files and start music */    
    public void loadSound(){
        // load sfx                
        gameOver = MediaTools.loadAudioClip("sound/gameover.au");
        winClip = MediaTools.loadAudioClip("sound/win.au");
        musicClip = MediaTools.loadAudioClip("sound/superfly.au");
        bottomClip = MediaTools.loadAudioClip("sound/bottom.au");
        bounceClip = MediaTools.loadAudioClip("sound/bounce.au"); 
        bounceClip2 = MediaTools.loadAudioClip("sound/jump.au");     
        music(); // start music loop
    }
    
/** Game start banners */
    public void startup(){    
        drawInfo();
        waitForClick(); // click to start game
        pause(500);
        removeInfo();
    }
    
/** clean up game start banners */
    private void removeInfo(){        
        remove(welcome);
        remove(key);
        remove(cheat);
        remove(rules);
        remove(musicBanner);
        remove(pauseBanner);
    }

/** Draw game start banners */
    private void drawInfo(){
        welcome = new GLabel("***CLICK ANYWHERE TO START***", WIDTH/2, HEIGHT/2-20);
        welcome.setFont(new Font("Sans-Serif", Font.BOLD, 22));
        welcome.move(-welcome.getWidth()/2, -20);
        welcome.setColor(Color.green);
        add(welcome);

        rules = new GLabel("--THE PADDLE FOLLOWS YOUR MOUSE", 10,HEIGHT/2);
        rules.setFont(new Font("Sans-Serif", Font.BOLD, 18));
        rules.setColor(Color.cyan);
        add(rules);

        cheat = new GLabel("--HIT ENTER TO TOGGLE CHEESEY MODE", 10,HEIGHT/2+25);
        cheat.setFont(new Font("Sans-Serif", Font.BOLD, 18));
        cheat.setColor(Color.red);
        add(cheat);                

        key = new GLabel("--HIT UP/DOWN TO CONTROL BALL SPEED", 10,HEIGHT/2+50);
        key.setFont(new Font("Sans-Serif", Font.BOLD, 18));
        key.setColor(Color.yellow);
        add(key);

        pauseBanner = new GLabel("--HIT P TO PAUSE", 10,HEIGHT/2+75);
        pauseBanner.setFont(new Font("Sans-Serif", Font.BOLD, 18));
        pauseBanner.setColor(Color.green);
        add(pauseBanner);                        

        musicBanner = new GLabel("--HIT SPACEBAR TO TOGGLE MUSIC", 10,HEIGHT/2+100);
        musicBanner.setFont(new Font("Sans-Serif", Font.BOLD, 18));
        musicBanner.setColor(Color.orange);
        add(musicBanner);            
    }
    
/** Draw bricks and set count to zero*/
    private void drawBricks() {
        // draw bricks
        for(int i = 0; i < NBRICK_ROWS; i++) {
            for(int j = 0; j < NBRICKS_PER_ROW; j++) {
                // calculate x/y coordinates
                int y_coordinate = (i * (BRICK_HEIGHT + BRICK_SEP)) + BRICK_Y_OFFSET;
                int x_coordinate = j * (BRICK_WIDTH + BRICK_SEP);
                brick = new GRect(x_coordinate, y_coordinate, BRICK_WIDTH, BRICK_HEIGHT);
                brick.setFilled(true);
                
                // set row color, every 2 rows
                int row_num = i % 10;
                if (row_num < 2)
                    brick.setColor(Color.red);
                if (row_num >= 2 && row_num < 4)
                    brick.setColor(Color.orange);
                if (row_num >= 4 && row_num < 6)
                    brick.setColor(Color.yellow);
                if (row_num >= 6 && row_num < 8)
                    brick.setColor(Color.green);
                if (row_num >= 8)
                    brick.setColor(Color.cyan);
                // add brick to canvas
                add(brick);
                count = 0; // # of bricks broken
            }
        }
    }
    
/** Draw ball */
    private void drawBall() {
        ball = new GOval((WIDTH/2)-(BALL_RADIUS/2),(HEIGHT/2)-(BALL_RADIUS/2),BALL_RADIUS,BALL_RADIUS);
        ball.setFilled(true);
        ball.setColor(Color.white);
        add(ball);
    }
    
/** Ball physics and animation */    
    private void runBall() {            
        // set x,y velocity
        vy = 3;    
        vx = rgen.nextDouble(1.0, 3.0); 
        
        // random angle for game start
        if (rgen.nextBoolean(0.5)) 
            vx = -vx;        
                
        // loop for ball engine
        while(true) {
            // bounce off x-axis
            if (ball.getX() < 0 || ball.getX()+BALL_RADIUS > WIDTH-BALL_RADIUS/2)
                vx = -vx;
            
            // bounce off y-axis
            if (ball.getY() < 0)
                vy = -vy;
                
            // cheat mode; bounce off bottom
            if (cheatOn == true) {
                if (ball.getY() >= HEIGHT-BALL_RADIUS*3)
                    vy = -vy;
            }
                
            // if ball touches bottom, lose a turn, reset ball
            if (ball.getY() > HEIGHT+BALL_RADIUS) {
                bottomClip.play(); // bounce sound effect
                if (turns < 0) {
                    // game over label
                    GLabel over = new GLabel("GAME OVER!!!", WIDTH/2, HEIGHT/2);                    
                    over.move(-over.getWidth()/2, 0);
                    over.setColor(Color.red);
                    add(over);

                    musicClip.stop();
                    gameOver.play();
                    pause(8000);
                    removeAll();
                    
                    count = 0; // reset bricks broken count
                    turns = 3; // reset turns
                    
                    if (playMusic)
                        musicClip.loop();
                    letsPlay(); // restart game
                }
                
                // next turn banner
                GLabel restart = new GLabel("GET READY... " + turns + " TURNS REMAINING", WIDTH/2, HEIGHT/2);                
                if (turns == 1) {
                    restart.setLabel("GET READY... " + turns + " TURN REMAINING");
                }                
                restart.move(-restart.getWidth()/2, 0);
                restart.setColor(Color.green);
                add(restart);
                
                turns--; // lose a turn
                pause(3000);
                remove(restart); // remove banner
                
                // set ball to center location
                ball.setLocation((WIDTH/2)-(BALL_RADIUS/2),(HEIGHT/2)-(BALL_RADIUS/2));
            }
            else {
                // check for collisions with bricks & paddle
                collision();
            }
            
            ball.move(vx, vy);
            pause(hold);
            
        }
    }

/** Test for collisions with bricks & paddle */    
    private void collision() {
    
            //  Get ball points to test; UpperLeft/UpperRight/LowerRight/LowerLeft
            GObject colliderUL = getElementAt(ball.getX(), ball.getY());
            GObject colliderUR = getElementAt(ball.getX()+BALL_RADIUS, ball.getY());
            GObject colliderLR = getElementAt(ball.getX()+BALL_RADIUS, ball.getY()+BALL_RADIUS);
            GObject colliderLL = getElementAt(ball.getX(), ball.getY()+BALL_RADIUS);

            // bounce off paddle
            if (colliderUL == paddle || colliderUR == paddle || 
                colliderLR == paddle || colliderLL == paddle) {
                
                // set angles for trajectory
                double offset = ball.getX() - (paddle.getX() + (PADDLE_WIDTH/2));
                if (offset  <= -30)
                    vx = -3;
                else if (offset <=-20)
                    vx = -2;
                else if (offset <= -10)
                    vx = -1;
                else if (offset == 0)
                    vx = -vx;
                else if (offset < 10)
                    vx = 1;
                else if (offset < 20)
                    vx = 2;
                else if (offset <= 30)
                    vx = 3;        
                vy = -vy;                
                bounceClip2.play(); // paddle sound
            }
            // remove bricks
            else if (colliderUL != null) {
                remove(colliderUL);
                vy = -vy;
                bounceClip.play();
                count++;
            }
            else if (colliderUR != null) {
                remove(colliderUR);
                vy = -vy;
                bounceClip.play();
                count++;
            }
            else if (colliderLR != null) {
                remove(colliderLR);
                vy = -vy;
                bounceClip.play();
                count++;
            }
            else if (colliderLL != null) {
                remove(colliderLL);
                vy = -vy;
                bounceClip.play();
                count++;
            }

            // check for winning game
            if (count == 100) {
                musicClip.stop();
                winClip.play();
                
                GLabel done = new GLabel("YOU WIN!", WIDTH/2,HEIGHT/2);
                done.move(-done.getWidth()/2,0);
                done.setColor(Color.cyan);
                add(done);
                
                pause(5000);
                removeAll();
                count = 0; // reset bricks broken count
                turns = 3; // reset turns
                if (playMusic)
                    musicClip.loop();
                letsPlay(); // restart game                
            }
            
    }

/** Draw paddle */
    private void drawPaddle() {
        int x_paddle = (APPLICATION_WIDTH / 2) - (PADDLE_WIDTH / 2);
        paddle = new GRect(x_paddle, HEIGHT - PADDLE_Y_OFFSET, PADDLE_WIDTH, PADDLE_HEIGHT);
        paddle.setColor(Color.white);
        paddle.setFilled(true);
        add(paddle);        
    }
    
/** Mouse event handler */
    public void mouseMoved(MouseEvent e) {
        int ex = e.getX();
        
        // paddle follows mouse
        if (ex >= PADDLE_WIDTH / 2 && ex <= WIDTH - (PADDLE_WIDTH / 2) )
            paddle.setLocation((ex - (PADDLE_WIDTH / 2)), HEIGHT - PADDLE_Y_OFFSET);
        else if (ex >= 0 && ex < PADDLE_WIDTH / 2)
            paddle.setLocation(0, HEIGHT - PADDLE_Y_OFFSET);
        else if (ex < WIDTH && ex >= WIDTH - (PADDLE_WIDTH / 2))
            paddle.setLocation(WIDTH - PADDLE_WIDTH, HEIGHT - PADDLE_Y_OFFSET);
    }
    
/**    Keyboard event handler */
    public void keyPressed(KeyEvent e) {
    
        int ex = e.getKeyCode(); // get key pressed
        
        // speed up ball
        if (ex == KeyEvent.VK_UP && hold > 2) {
            hold -= 2;
        }
        //slow down ball
        if (ex == KeyEvent.VK_DOWN) {
            hold += 2;            
        }
        // cheat on/off
        if (ex == KeyEvent.VK_ENTER) {
            cheatOn = !cheatOn;
        }
        // sound on/off
        if (ex == KeyEvent.VK_SPACE) {
            if (playMusic) {
                musicClip.stop();
                playMusic = false;
            }
            else if (!playMusic){
                musicClip.loop();
                playMusic = true;
            }                            
        }
        // pause game
        if (ex == KeyEvent.VK_P) {
            paused = !paused;
            pauseGame();
        }
    }
    
/** Pause game by stopping ball */
    public void pauseGame(){    
        if (paused) {
            xtmp = vx;
            ytmp = vy;
            vx = 0;
            vy = 0;
        }
        else {
            vx = xtmp;
            vy = ytmp;
        }
    }
    
/** Game music loop */
    public void music() {
        if (playMusic)
            musicClip.loop();
    }
    
/** Temp velocity value storage for game pause */
    private double xtmp = 0;
    private double ytmp = 0;

/** Keyboard listener states*/
    private boolean paused = false;
    private boolean playMusic = true;
    private boolean cheatOn = false;
    
/** Player turns remaining */    
    private int turns = 3;

/** Game speed*/
    private int hold = 10; 

/** Player score; # of bricks broken */    
    private int count;

/** Sound files */
    private AudioClip bounceClip, bounceClip2, winClip, musicClip, bottomClip, gameOver;

/** Brick */
    private GRect brick;

/** Ball velocity */    
    private double vx, vy;

/** Paddle, Ball */    
    private GRect paddle;
    private GOval ball;

/** Random # generator for ball initial velocity */    
    private RandomGenerator rgen = RandomGenerator.getInstance();

/** Game start banners */
    private GLabel welcome;
    private GLabel key;
    private GLabel cheat;
    private GLabel rules;
    private GLabel musicBanner;
    private GLabel pauseBanner;    
}

    