package GameState;

import Audio.JukeBox;
import Entity.*;
import TileMap.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Nivel3 extends  GameState{
    private Background bg;
    private Font fontGeneral;
    private int optiuneCurenta = 0;
    private String[] optiuniButoane ={
            "Revino la meniu",
            "Ieși"
    };
    TileMap tileMap;

    public Nivel3(GameStateManager gsm){
        super(gsm);
        init();
        fontGeneral = new Font("Source Code Pro", Font.BOLD, 12);
    }
    @Override
    public void init() {
        bg = new Background("/Backgrounds/win.gif",0.1);

        JukeBox.stop("nivel1");
        JukeBox.load("win", "/Melodie/win.mp3");
        JukeBox.loop("win", 600, JukeBox.getFrames("win") - 2200);

    }

    @Override
    public void draw(Graphics2D g) {

        bg.draw(g);

        //draw - optiuni meniu
        g.setFont(fontGeneral);
        for(int i=0; i<optiuniButoane.length ; i++){
            if(i == optiuneCurenta){
                g.setColor(Color.BLACK);
            }
            else{
                g.setColor(Color.RED);
            }
            g.drawString(optiuniButoane[i], 120, 200+i*20);

        }
    }

    @Override
    public void update() {
        bg.update();

    }
    private void select(){
        if(optiuneCurenta == 0){
            JukeBox.stop("win");
            gsm.setState(0);
        }
        if(optiuneCurenta == 1) {
            System.exit(0);
        }
    }

    @Override
    public void keyPressed(int k) {
        if(k == KeyEvent.VK_Q) gsm.setState(0);
        if(k == KeyEvent.VK_ENTER){
            select();
        }
        if(k == KeyEvent.VK_UP){
            optiuneCurenta--;
            if(optiuneCurenta == -1){
                optiuneCurenta = optiuniButoane.length - 1;
            }
        }
        if(k == KeyEvent.VK_DOWN){
            optiuneCurenta++;
            if(optiuneCurenta == optiuniButoane.length){
                optiuneCurenta = 0;
            }
        }
    }

    @Override
    public void keyReleased(int k) {

    }
}
