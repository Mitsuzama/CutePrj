package Entity;

import Main.GamePanel;
import TileMap.TileMap;
import TileMap.Tile;

import java.awt.*;
//import javafx.animation.Animation;

public abstract class MapObject {

    //toate variabilele sunt protected tocmai pentru a putea fi accesate de subclase

    //tile
    protected TileMap tileMap;
    protected int tileSize;
    protected double x_map;
    protected double y_map;

    //pozitii
    protected double x;
    protected double y;
    protected double dx;
    protected double dy;

    //dimensiuni
    protected int width;
    protected int height;

    //pentru determinarea coliziunilor cu tile-uri si alti inamici
    protected int cwidth;
    protected int cheight;

    protected int linieCurenta;
    protected int coloanaCurenta;
    protected double next_x;//pentru urmatoarea pozitie
    protected double next_y;
    protected double temp_x;
    protected double temp_y;

    //vom folosi metoda celor 4 puncte pentru colizii: vedem daca oricare colt loveste un tile care blocheaza
    protected boolean stg_sus;
    protected boolean dr_sus;
    protected boolean stg_jos;
    protected boolean dr_jos;

    //animatia
    protected Animation animation;
    protected int actiuneCurenta;
    protected int animatiePrecedenta;
    protected boolean facingRight; //vedem daca sprite-ul se uita la stanga sau dreapta

    //movement (ce face obiectul in sine)
    protected boolean stanga; //se duce la stanga
    protected boolean dreapta; //se duce la dreapta
    protected boolean sus;
    protected boolean jos;
    protected boolean saritura; //sare
    protected boolean cazatura; //cade

    //atribute ale miscarii
    protected double vitezaMiscarii; //cat de repede accelereaza/merge
    protected double vitezaMaxima; //viteza maxima care poate fi atinsa de entitate
    protected double vitezaDeOprire; //viteza de incetinire (cand nu apas stg sau dr)
    protected double vitezaDeCadere; //simularea gravitatii
    protected double maxVitezaDeCadere;
    protected double inaltimeSaritura; // cat de sus poate sari obiectul
    protected double stopInailtimeSaritura; //ca sa nu sara spre infinit


    //constructor
    public MapObject(TileMap tm){
        tileMap = tm;
        tileSize = tm.getTileSize();
    }

    //verificare intersectare cu alte obiecte de pe harta
    public boolean intersects(MapObject o){ //rectangle collision
        Rectangle r1 = getRectangle();
        Rectangle r2 = o.getRectangle();
        return r1.intersects(r2); //Two rectangles intersect if their intersection is nonempty
    }

    public boolean contains(MapObject o) { //verificare daca contine elementul in zona respectiva
        Rectangle r1 = getRectangle();
        Rectangle r2 = o.getRectangle();
        return r1.contains(r2);
    }

    public Rectangle getRectangle(){
        return new Rectangle(
                (int)x - cwidth/2,
                (int)y - cheight/2,
                cwidth,
                cheight
        );
    }

    public void calculateCorners(double x, double y){

        int leftTile = (int)(x - cwidth / 2) / tileSize; //asta e coloana la stanga coloaneiCurente
        int rightTile = (int)(x + cwidth / 2 - 1) / tileSize; //la dreapta/ -1 ca saa nu intru in urmatoarea coloana
        int topTile = (int)(y - cheight / 2) / tileSize;
        int botTile = (int)(y + cheight / 2 - 1) / tileSize;

        //vedem ce tip avem
        int tl = tileMap.getType(topTile, leftTile);
        int tr = tileMap.getType(topTile, rightTile);
        int bl = tileMap.getType(botTile, leftTile);
        int br = tileMap.getType(botTile, rightTile);

        stg_sus = tl == Tile.BLOCKED;
        stg_jos = bl == Tile.BLOCKED;
        dr_sus = tr == Tile.BLOCKED;
        dr_jos = br == Tile.BLOCKED;

    }
    public void checkTileMapCollision(){ //functie de verificare daca suntem intr-un tile blocant sau nu

        coloanaCurenta = (int)x /tileSize;
        linieCurenta = (int)y / tileSize;

        next_x = x+dx;
        next_y = y+dy;

        temp_x = x;
        temp_y = y;

        //calculez in directia lui y
        calculateCorners(x, next_y);
        if(dy < 0){ //daca merg in sus
            if(dr_sus || stg_sus){//daca sunt Blocked vreuna dintre partile de sus
                dy = 0; //ma opresc din urcat
                temp_y = linieCurenta * tileSize + cheight / 2; //setez obiectul sub tile-ul pe care l-a lovit
            }
            else{ //altfel, continui sa urc
                temp_y += dy;
            }

        }

        if(dy > 0) { //suntem pe un tile de jos
            if (dr_jos || stg_jos) {//verific cele doua colturi de jos
                dy = 0;//ma opresc din cazut
                cazatura = false; //nu mai cad
                temp_y = (linieCurenta + 1) * tileSize - cheight / 2; //trebuie setat caracterul cu un pixel mai sus de coliziune
            } else {
                temp_y += dy;
            }
        }

        //calculez in directia lui x
        calculateCorners(next_x , y);
        if(dx < 0) {//merg la stanga
            if(stg_jos || stg_sus){//verifi ambele colturi din stanga
                dx = 0; //ma opresc din mers la stanga
                temp_x = coloanaCurenta * tileSize + cwidth/2; //setez x la dreapta tileului lovit
            }
            else{//daca nu m-am lovit, continui la stanga
                temp_x +=dx;
            }
        }

        if(dx > 0){//merg la dreapta
            if(dr_jos || dr_sus){
                dx = 0;
                temp_x = (coloanaCurenta + 1) * tileSize - cwidth /2 ; //pozitionez la stanga acelui zid/tile
            }
            else{
                temp_x +=dx;
            }
        }

        //verificare cazatura de pe prapastie
        if(!cazatura){ //daca nu cad, verific mereu ce am sub mine
            calculateCorners(x, next_y+1); //verific daca am pamant sub picioare
            if(!dr_jos && !stg_jos){ //daca nu exista coltuir la stanga si dreapta
                cazatura = true; //atuci cad
            }
        }

    }

    public  int getx() { return (int)x; }
    public  int gety() { return (int)y; }
    public  int getyWidth() { return width; }
    public  int getHeight() { return height; }
    public  int getCwidth() { return cwidth; }
    public  int getCheight() { return cheight; }

    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
    }
    public void setVector(double dx, double dy){
        this.dx = dx;
        this.dy = dy;
    }

    public void setMapPosition(){
        x_map = tileMap.getx();
        y_map = tileMap.gety();
    }

    public void setLeft(boolean b) {stanga = b;}
    public void setRight(boolean b) {dreapta = b;}
    public void setTop(boolean b) {sus = b;}
    public void setBot(boolean b) {jos = b;}
    public void setJumping(boolean b) {saritura = b;}

    public boolean notOnScreen(){ //fucntie verificare: aca obiectul NU ESTE PE ECRAN, e true
        return x + x_map + width < 0 || //daca e inafara ecranului din stanga
                x + x_map - width > GamePanel.WIDTH || //sau daca e inafara partii din dreapta
                y + y_map + height < 0 || //daca e inafara partii de sus
                y + y_map - height > GamePanel.HEIGHT; //daca e inafara pe jos
    }

    public void draw(Graphics2D g){
        setMapPosition();
        if(facingRight){ //daca playerul se uita la dreapta, desenez normal spriteurile
            g.drawImage(
                    animation.getImage(),
                    (int)(x + x_map - width / 2),
                    (int)(y + y_map - height / 2),
                    null
            );
        }
        else{//altfel, trebuie sa inversez sprite-urile
            g.drawImage(
                    animation.getImage(),
                    (int)(x + x_map - width/2 + width),
                    (int)(y + y_map - height/2),
                    -width,
                    height,
                    null
            );
        }
    }
}
