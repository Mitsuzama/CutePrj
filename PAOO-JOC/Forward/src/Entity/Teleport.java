package Entity;

import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Teleport extends MapObject{

    private BufferedImage[] sprites;

    public Teleport(TileMap tm) {
        super(tm);
        facingRight = true;
        width = height = 40;
        cwidth = 20;
        cheight = 40;

        try {
            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream("/Sprites/Player/Teleport.gif")
            );
            sprites = new BufferedImage[9];
            for(int i = 0; i < sprites.length; i++) {
                sprites[i] = spritesheet.getSubimage(
                        i * width,
                        0,
                        width,
                        height
                );
            }
            animation = new Animation();
            animation.setFrames(sprites);
            animation.setDelay(1);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void update(){
        animation.update();
    }

    public void draw(Graphics2D g){
        super.draw(g);
    }
}
