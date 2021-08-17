package Entity;

import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class FireBall extends MapObject{

    private boolean hit; //true = a lovit ceva
    private boolean remove; //true = trebuie sa dam remove de pe harta
    private BufferedImage[] sprites;
    private BufferedImage[] hitSprites; //sprite cand loveste ceva

    public FireBall(TileMap tm, boolean right){// booleanul e pentru a sti in ce directie merge fireball-ul
        super(tm);

        facingRight = right;

        vitezaMiscarii = 3.8;
        if(right) dx = vitezaMiscarii; //daca merge la dreapta, setez viteza aferenta
        else dx = -vitezaMiscarii;

        width = 30;
        height = 30; //doar pentru importare
        cwidth = 14;
        cheight = 14;//valori reale ale bilei

        //incarcam sprite-urile
        try{

            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/fireball.gif"));

            sprites = new BufferedImage[4]; //ca e facuta din 4 animatii
            for(int i = 0; i<sprites.length; i++){
                sprites[i] = spritesheet.getSubimage(
                        i * width,
                        0,
                        width,
                        height
                );
            }

            hitSprites = new BufferedImage[3]; //ca e facuta din 4 animatii
            for(int i = 0; i < hitSprites.length; i++){
                hitSprites[i] = spritesheet.getSubimage(
                        i * width,
                        height,
                        width,
                        height
                );
            }

            animation = new Animation();
            animation.setFrames(sprites);
            animation.setDelay(70);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public void setHit(){
        if(hit) return;
        hit = true;
        animation.setFrames(hitSprites);
        animation.setDelay(70);
        dx = 0;
    }

    public boolean shouldRemove() {return remove; }
    public void update(){

        checkTileMapCollision();
        setPosition(temp_x, temp_y);

        if(dx == 0 && !hit){
            setHit();
        }

        animation.update();
        if(hit && animation.hasPlayedOnce()){ //daca am avut animatia o data, o scot din joc
            remove = true;
        }

    }

    public void draw(Graphics2D g){

        setMapPosition();

        super.draw(g);

    }
}
