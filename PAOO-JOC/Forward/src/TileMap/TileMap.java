package TileMap;

import Main.GamePanel;
import jdk.internal.dynalink.linker.LinkerServices;

import java.awt.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

public class TileMap {

    //pozitia
    private double x;
    private  double y;

    //limite
    private int x_min;
    private int x_max;
    private int y_min;
    private int y_max;

    private double smootharoo; //dam scroll mai smooth

    //harta
    private int[][] map;
    private int tileSize; //marimea unui tile
    private int linii;
    private int coloane;
    private int width;
    private int height;

    //tileset-urile
    private BufferedImage tileSet;
    private Tile[][] tiles; //acesta reprezinta tile-urile scoase din tileSet
    private int numTilesAcross; //al catelea tile din tilesetul meu facut

    /*deseneaza - aici e o eficientizare: in cazul in care as avea 100+ tilE-uri(ex) ,
    * nu vreau sa le desenez pe toate pentru ca asta mi-ar nenoroci laptopul care abia ma duce la 10 000pasi in quickSort
    * DECI voi desena doar ti;le care sunt pe ecran la momentul respectiv
    */
    private int linieOffset; //de la ce linie sa incep sa desenez
    private int coloanaOffset; //de la ce coloana sa incep sa desenez
    private int numLiniiColor; //cate linii sa colorez
    private int numColoaneColor; //cate coloane sa desenez

    private boolean shaking;
    private int intensitate; //intensitatea cu care da shake


    //constructor
    public TileMap(int tileSize){
        this.tileSize = tileSize;
        numLiniiColor = GamePanel.HEIGHT / tileSize + 2;
        numColoaneColor = GamePanel.WIDTH / tileSize +2;
        smootharoo = 0.07;
    }

    public boolean isShaking() {
        return shaking;
    }

    public int getTileSize(){return tileSize;}

    public double getx(){return x;}

    public double gety(){return y;}

    public int getWidth(){return width;}

    public int getHeight(){return height;}

    public int getType(int row, int col){ //getter pt tile type
        if(row<0 || row >= map.length)
            return Tile.NORMAL;
        if(col<0 || col>=map[0].length)
            return Tile.BLOCKED;
        int rc = map[row][col]; //rc = row column
        int r = rc / numTilesAcross;
        int c = rc % numTilesAcross;
        return tiles[r][c].getType();
    }

    public void setSmootharoo(double smootharoo) {
        this.smootharoo = smootharoo;
    }

    public void setPosition(double x, double y){
        //acesta tehnica presupune ca nu urmaresc imaginea pixel cu pixel, ci incerc sa merg incetul cu incetul in directia dorita

        this.x += (x - this.x) * smootharoo;
        this.y += (y - this.y) * smootharoo;

        fixBounds();

        //setam la ce coloana si ce linie sa incepem esenul
        coloanaOffset = (int) - this.x / tileSize;
        linieOffset = (int) - this.y / tileSize;

    }

    public void setShaking(boolean shake, int i) {
        shaking = shake;
        intensitate = i;
    }

    public void fixBounds(){//functie pentru a fi siguri ca am valori corecte transmise la limite
        if(x<x_min) x = x_min;
        if(y<y_min) y = y_min;
        if(x>x_max) x = x_max;
        if(y>y_max) y = y_max;
    }

    public void loadTiles(String s){ //da load la tile-uri
        try{
            tileSet = ImageIO.read(
                    getClass().getResourceAsStream(s)
            );
            numTilesAcross = tileSet.getWidth() / tileSize;
            tiles = new Tile[2][numTilesAcross];

            BufferedImage subimage;
            for(int j = 0; j<numTilesAcross ; j++){
                //aici extrag primul rand
                subimage = tileSet.getSubimage(
                        j * tileSize,
                        0,
                        tileSize,
                        tileSize
                );
                tiles[0][j] = new Tile(subimage, Tile.NORMAL);
                //extrag al doilea rand
                subimage = tileSet.getSubimage(
                        j*tileSize,
                        tileSize,
                        tileSize,
                        tileSize
                );
                tiles[1][j] = new Tile(subimage, Tile.BLOCKED);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void loadMap(String s){ //da load la harta

        try{

            //dam load la harta
            InputStream in = getClass().getResourceAsStream(s);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            //citim
            coloane = Integer.parseInt(br.readLine()); //prima linie are nr de coloane
            linii = Integer.parseInt(br.readLine()); // a doua are numarul de linii
            map = new int[linii][coloane]; //creez harta
            width= coloane * tileSize; //aflu lungimea prin calcul(e mai sigur)
            height = linii * tileSize;

            x_min = GamePanel.WIDTH - width;
            x_max =0;
            y_min = GamePanel.HEIGHT - height;
            y_max = 0; 

            String delimiator = "\\s+"; //spatii albe
            for(int i = 0; i < linii ; i++){
                String line = br.readLine(); //iau linia
                String[] tokens = line.split(delimiator); //ii dau split
                for(int j=0; j<coloane ; j++){ //pun pe bucati in harta
                    map[i][j] = Integer.parseInt(tokens[j]); //aici am avut o eroare de logica: eu pusesem [linii][coloane], adica var generale, nu i si j (nu stiu ce aveam in cap)
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void draw(Graphics2D g){

        for(int i = linieOffset; i < linieOffset + numLiniiColor; i++){
            if(i >= linii) break;

            for(int j = coloanaOffset ; j<coloanaOffset + numColoaneColor; j++){
                if(j >= coloane) break;
                if(map[i][j] == 0) continue;//daca nu am nimic de desenat, nu ma mai chinui sa desenez

                int rc = map[i][j];
                int r = rc / numTilesAcross;
                int c = rc % numTilesAcross;

                g.drawImage(tiles[r][c].getImage(),
                        (int)x + j * tileSize,
                        (int)y + i * tileSize,
                        null
                        );
            }
        }

    }

    public void update(){
        if(shaking) {
            this.x += Math.random() * intensitate - intensitate / 2;
            this.y += Math.random() * intensitate - intensitate / 2;
        }
    }
}
