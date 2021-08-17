package Main;

import GameState.GameStateManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel
        implements Runnable, KeyListener { //The JPanel class provides general-purpose containers for lightweight components.
    // By default, panels do not add colors to anything except their own background;
    // however, you can easily add borders to them and otherwise customize their painting.

    //dimensiuni
    public static final int WIDTH = 320; //final = ca sa fie prestabilita LATIMEA/WIDTH
    public static final int HEIGHT = 240;
    public static final int SCALE =2; //la final voi avea size de 640x480 (poate voi schimba)

    //game thread
    private Thread thread; //A thread is a thread of execution in a program.
    private boolean running;
    private int FPS = 60;
    private long targetTime = 1000/FPS;

    //imaginea
    private BufferedImage imaginea;
    private Graphics2D g; //apartine java.awt - clasa fudamentala pentru randare a imaginilor 2D

    //manager de stare a jocului
    private GameStateManager gsm;


    public GamePanel(){
        super();
        //ImageIcon imageIcon = new ImageIcon("/Backgrounds/eevee.png");
        //setIconImage(new ImageIcon(getClass().getResource("/Backgrounds/eevee.png").getImage())).;
        setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE)); //seteaza dimensiunea dorita a aacesstui obiect
        setFocusable(true); //poate fi focusat obiectul
        requestFocus(); //COMMENT DACA APAR PROBLEME REVIN
    }

    public void addNotify(){
        super.addNotify();//notifica componenta ca acum are un parinte
        if(thread==null){ //setez threadul
            thread = new Thread(this);
            addKeyListener(this);
            thread.start();
        }
    }

    private void init(){ //cateva initializari ca sa nu dau cluster la functia run()
        imaginea = new BufferedImage(WIDTH,
                HEIGHT, //BufferedeImage(width, height, tipul_imaginii)
                BufferedImage.TYPE_INT_RGB); //TYPE_INT_RGB = Represents an image with 8-bit RGB color components packed into integer pixels

        g = (Graphics2D) imaginea.getGraphics();

        running = true;

        gsm = new GameStateManager();

    }

    public void run(){
        init();

        long start;
        long elapsed;
        long wait;

        //jocul
        while(running){ //cat timp va merge jocul, voi face 3 lucruri

            start = System.nanoTime();

            update();
            draw();
            drawToScreen();

            //Asta imi da un exception
            elapsed = System.nanoTime() - start;
            wait = targetTime - elapsed /1000000;
            if(wait < 0 ) wait = 5;

            try{
                Thread.sleep(wait);
            }
            catch(Exception e){
                e.printStackTrace(); //poate aparea blackscreen, deci fac un try catch
            }

        }

    }

    private void update() {
        gsm.update();
    }

    private void draw(){
        gsm.draw(g);
    }

    private void drawToScreen(){
        Graphics g2 = getGraphics();
        g2.drawImage(imaginea, 0, 0,
                WIDTH * SCALE, HEIGHT * SCALE,
                null);
        g2.dispose();
    }

    public void keyTyped(KeyEvent tasta) {}

    public void keyPressed(KeyEvent tasta) {
        gsm.keyPressed(tasta.getKeyCode());
    }

    public void keyReleased(KeyEvent tasta) {
        gsm.keyReleased(tasta.getKeyCode()); //getKeyCode e de tip int pentru a face transformarea de la keyEvent la int
    }


}
