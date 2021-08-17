package Entity.Enemies;

import Entity.Animation;
import Entity.Enemy;
import TileMap.TileMap;
import com.sun.nio.zipfs.ZipFileStore;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Slugger extends Enemy {

    private BufferedImage[] sprites;

    public Slugger(TileMap tm){
        super(tm);
        vitezaMiscarii = 0.3;
        vitezaMaxima = 0.3;
        vitezaDeCadere = 0.2;
        maxVitezaDeCadere = 10.0;

        width = 30;
        height = 30;
        cwidth = 20;
        cheight = 20;

        viata = maxViata = 2;
        damage = 1;

        //load la spriteuri
        try{
            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/Enemies/slugger.gif"
                    )
            );

            sprites = new BufferedImage[3];
            for(int i = 0; i<sprites.length ; i++){
                sprites[i] = spritesheet.getSubimage(
                        i * width,
                        0,
                        width,
                        height
                );
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(300);

        dreapta = true; //la inceput, va fi cu fata spre dreapta
        facingRight = true;
    }

    private void getNextPosition(){

        //movement
        if(stanga){
            dx -= vitezaMiscarii;
            if(dx < -vitezaMaxima){
                dx = -vitezaMaxima;
            }
        }
        else if(dreapta){
            dx += vitezaMiscarii;
            if(dx > vitezaMaxima){
                dx = vitezaMaxima;
            }
        }

        if(cazatura){ //se poate intampla sa cada de pe o margine
            dy += vitezaDeCadere;
        }

    }

    public void update(){
        getNextPosition();
        checkTileMapCollision();
        setPosition(temp_x, temp_y);

        //verific flinch
        if(flinching){ //daca da flinch, trebuie sa vedem de cat timp face asta
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed > 400){ //consider ca dupa ce da 0.4 secunde flinch, il pot opri
                flinching = false;
            }
        }

        //daca se izbeste de un zid, se duce in cealalta directie
        if(dreapta && dx == 0){ //dx == 0 inseamna ca ne-am izbit de un zid, asta fiind setat in checkTileMapCollision
            dreapta = false;
            stanga = true; //schimb directia
            facingRight = false;
        }
        else if(stanga && dx == 0){
            dreapta = true;
            stanga = false;
            facingRight = true;
        }

        //update la animatie
        animation.update();
    }

    public void draw(Graphics2D g){
        //if(notOnScreen())return;
        setMapPosition();

        super.draw(g);

    }

}
