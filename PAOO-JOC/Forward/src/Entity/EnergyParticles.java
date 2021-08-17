package Entity;

import Helper.Content;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnergyParticles  extends MapObject{


    private int count;
    private boolean remove;

    private BufferedImage[] sprites;

    public static int UP = 0;
    public static int LEFT = 1;
    public static int DOWN = 2;
    public static int RIGHT = 3;

    public EnergyParticles(TileMap tm, double x, double y, int directie) {
        super(tm);
        this.x = x;
        this.y = y;
        double d1 = Math.random() * 2.5 - 1.25;
        double d2 = -Math.random() - 0.08;
        if(directie == UP){
            dx = d1;
            dy = d2;
        }
        else if(directie == LEFT){
            dx = d2;
            dy = d1;
        }
        else if(directie == DOWN) {
            dx = d1;
            dy = -d2;
        }
        else {
            dx = -d2;
            dy = d1;
        }

        count = 0 ;

        animation = new Animation();
        sprites = Content.EnergyParticle[0];
        animation.setFrames(sprites);
        animation.setDelay(-1);
    }

    public void update(){
        x += dx;
        y += dy;
        count++;
        if(count == 60) remove = true;
    }

    public boolean shouldRemove(){return remove;}

    public void draw(Graphics2D g){
        super.draw(g);
    }

}
