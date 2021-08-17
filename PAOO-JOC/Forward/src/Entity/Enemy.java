package Entity;

import TileMap.TileMap;

public class Enemy extends MapObject{

    protected int viata;
    protected int maxViata;
    protected boolean mort; //daca sunt sau nu morti
    protected int damage; //cat damage dau

    protected boolean flinching;
    protected long flinchTimer;

    public Enemy(TileMap tm){
        super(tm);
    }

    public boolean isMort(){
        return mort;
    }

    public int getDamage() {
        return damage;
    }

    public void hit(int damage){//inamicul este lovit cu x damage
        if(mort || flinching){
            return;
        }
        viata -= damage;
        if(viata < 0) viata = 0;
        if(viata == 0) mort = true;
        flinching = true;
        flinchTimer = System.nanoTime();
    }

    public void update(){

    }
}
