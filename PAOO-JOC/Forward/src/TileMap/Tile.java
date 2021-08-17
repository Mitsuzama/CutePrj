package TileMap;

import java.awt.image.BufferedImage;

public class Tile{
    //aceasta clasa contine doar imaginea si tioul acesteia
    private BufferedImage image;
    private int tipulImaginii;

    //aici impart in 2 tipuri aferente
    public static final int NORMAL = 0;
    public static final int BLOCKED = 1;

    public Tile(BufferedImage image, int tip){ //constructor
        this.image = image;
        this.tipulImaginii = tip;
    }

    //gettere
    public BufferedImage getImage(){
        return image;
    }

    public int getType(){
        return tipulImaginii;
    }
}
