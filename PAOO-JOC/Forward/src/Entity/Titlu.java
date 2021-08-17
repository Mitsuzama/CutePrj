package Entity;

import Main.GamePanel;
import TileMap.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Titlu {

    public BufferedImage image;

    public int count;
    private boolean gata;
    private boolean remove;

    private double x;
    private double y;
    private double dx;

    private int width;

    public Titlu(String s){

        try{
            image = ImageIO.read(getClass().getResourceAsStream(s));
            width = image.getWidth();
            x = -width;
            gata = false;
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Exceptie in casa TITLU/ public Titlu");
        }

    }

    public Titlu(BufferedImage image){
        this.image = image;
        width = image.getWidth();
        x -= width;
        gata = false;
    }

    public void setY(double y){
        this.y = y;
    }

    public void begin(){
        dx = 10;
    }

    public boolean shouldRemove(){
        return remove;
    }

    public void update(){
        if(!gata){
            if(x >= (GamePanel.WIDTH-width)/2){
                x = (GamePanel.WIDTH - width) / 2;
                count++;
                if(count >= 120) gata = true;
            }
            else{
                x += dx;
            }
        }
        else{
            x += dx;
            if(x > GamePanel.WIDTH) remove = true;
        }
    }

    public void draw(Graphics2D g) {
        g.drawImage(image, (int)x, (int)y, null);
    }

}
