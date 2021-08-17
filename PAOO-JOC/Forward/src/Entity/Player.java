package Entity;

import Audio.JukeBox;
import TileMap.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;


public class Player extends MapObject{

    // player stuff
    private int health;
    private int maxHealth;
    private int attack;
    private int maxAttack;
    private boolean mort; //daca este mort playerul
    private boolean flinching;
    private long flinchTimer;
    private long time;
    private long flinchCount;
    private boolean teleporting;
    private ArrayList<EnergyParticles> energyParticles;

    //pentru atac
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private boolean attacking; //pentru inputul de la tastatura
    private int attackCost; //pentru a scadea numarul de atacuri
    private int fireBallDamage; //cat damage da
    private ArrayList<FireBall> fireBalls;

    //zgarietura
    private boolean scratching;
    private int scratchDamage;
    private int scratchRange; //cat de departe poate ajunge

    //glide
    private boolean gliding;

    //animatii
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numOfFrames = { //numarul de frameuri pe fiecare animatie
        1, 6, 1, 1, 1, 4, 4, 1, 3
    };

    //state-uri ale animatiei/ actiuni aferente
    private static final int IDLE = 0; //inactivitatea caracterului
    private static final int WALKING = 1; //MERS
    private static final int JUMPING = 2;
    private static final int FALLING = 3;
    private static final int GLIDING = 4;
    private static final int FIREBALL = 5;
    private static final int SCRATCHING = 6;
    private static final int DEAD = 7;
    private static final int TELEPORT = 8;

    private ArrayList<Enemy> dusmani;

    //constructor
    public Player(TileMap tm){

        super(tm);

        width = 16; //asta citesc din sheet
        height = 22;

        cwidth = 16; //adevarata marime
        cheight = 22;

        vitezaMiscarii = 0.3;
        vitezaMaxima = 1.6;
        vitezaDeOprire = 0.4;
        vitezaDeCadere = 0.15;
        maxVitezaDeCadere = 4.0;
        stopInailtimeSaritura = 0.3;
        inaltimeSaritura = -4.8;

        facingRight = true;

        health = maxHealth = 5;
        attack = maxAttack = 2500;
        fireBallDamage = 5;
        attackCost = 200;
        fireBalls = new ArrayList<FireBall>();

        scratchDamage = 8;
        scratchRange = 40;

        //load la sprite-uri
        try{

            BufferedImage spriteSheet = ImageIO.read(
                    getClass().getResourceAsStream("/Sprites/Player/SPRITESHEET_vulpe.gif")
            );


            sprites = new ArrayList<BufferedImage[]>();
            for(int i=0; i<numOfFrames.length ; i++) { // i<numarul de linii din spritesheetul nostru
                BufferedImage[] bi = new BufferedImage[numOfFrames[i]];

                for(int j = 0; j < numOfFrames[i]; j++) {//pentru citire a fiecarui sprite in parte

                    if(i != SCRATCHING && i!=DEAD && i != FIREBALL) {
                        bi[j] = spriteSheet.getSubimage(
                                j * width,
                                i * height,
                                width,
                                height
                        );
                    }
                    else{ //pe ultima linie, animatiile de atac sunt cu width dublu, deci rebuie sa le citim separat
                        bi[j] = spriteSheet.getSubimage(
                                j * width * 2,
                                i * height,
                                width * 2,
                                height
                        );
                    }
                }
                sprites.add(bi);

            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        energyParticles = new ArrayList<EnergyParticles>();
        animation = new Animation();
        actiuneCurenta = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(400);

        //sfx
        JukeBox.load("jump","/SFX/jump.mp3");
        JukeBox.load("scratch","/SFX/scratch.mp3");
        JukeBox.load("fire", "/SFX/fire.mp3");
    }
    public void init(ArrayList<EnergyParticles> energyParticles) {
        this.energyParticles = energyParticles;
    }

    //gettere
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getAttack() { return attack; }
    public int getMaxAttack() { return maxAttack; }
    public long getTime() { return time; }
    public String getTimeToString() {
        int minutes = (int) (time / 3600);
        int seconds = (int) ((time % 3600) / 60);
        return seconds < 10 ? minutes + ":0" + seconds : minutes + ":" + seconds;
    }

    //settere
    public void setTime(long t) { time = t; }
    public void setHealth(int i) { health = i; }
    public void setTeleporting(boolean b) { teleporting = b; }

    public void loseHealth() { health--; }


    public void setAttacking(){
        attacking = true;
    }
    public void setScratching(){
        scratching = true;
    }
    public void setGliding(boolean b){
        gliding = b;
    }
    public void setDead() {
        health = 0;
        stop();
    }
    public void stop(){
        stanga = dreapta = sus = jos = flinching = saritura = attacking = false;
    }

    public void checkAttack(ArrayList<Enemy> dusmani){

        //verificare scratch
        //loop prin dusmani

        for(int i = 0; i < dusmani.size(); i++){

            Enemy e = dusmani.get(i);

            if(scratching) { //daca dau scratch
                if (facingRight) {///si dau face a dreapta
                    //trebuie sa verific daca inamicul este in rangeul de atac
                    if(e.getx() > x &&//daca inamcul este la dreapta noastra, adica exact in fata mea
                            e.getx() < x+scratchRange && //SI inamicul este la pozitie mai mica decat dreapta + rangeul a scratch
                            e.gety() > y - height/2 &&
                            e.gety() < y + height/2 ) {
                        e.hit(scratchDamage);
                    }
                }
                else {
                    if(e.getx() < x && e.getx() > x-scratchRange && e.gety()>y-height/2 && e.gety()<y+height/2){
                        e.hit(scratchDamage);
                    }
                }
            }

            //fireballs
            for(int j=0; j<fireBalls.size(); j++){
                if(fireBalls.get(j).intersects(e)){
                    e.hit(fireBallDamage);
                    fireBalls.get(j).setHit();
                    break;
                }
            }

            //dau check la coliziuni cu inamici
            if(intersects(e)){//daca ma intersectez cu inamicul
                hit(e.getDamage());
            }
        }

    }

    public void hit(int damage){
        if(flinching) return;
        health -= damage;
        if(health < 0) health = 0;
        if(health == 0) mort = true;
        flinching = true;
        flinchTimer = System.nanoTime();
    }

    public void reset(){
        health = maxHealth;
        facingRight = true;
        actiuneCurenta = -1;
        stop();
    }

    private void getNextPosition(){ //urmatoarea pozitie a caracterului din tastatura
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
        else{//trebuie sa ma opresc
            if(dx > 0) {
                dx -= vitezaDeOprire;
                if (dx < 0) {
                    dx = 0;
                }
            }
            else if(dx < 0){
                dx += vitezaDeOprire;
                if(dx > 0){
                    dx = 0;
                }
            }
        }

        // nu se poate misca in timpul atacului, exceptie in aer
        if((actiuneCurenta == SCRATCHING || actiuneCurenta == FIREBALL) && !(cazatura || saritura)){
            dx = 0;
        }

        //saritura
        if(saritura && !cazatura){
            JukeBox.play("jump");
            dy = inaltimeSaritura;
            cazatura = true;
        }

        //cazatura
        if(cazatura){

            if(dy > 0 && gliding) dy += vitezaDeCadere * 0.1;//viteza de cadere in gliding e mai mica
            else dy += vitezaDeCadere;

            if(dy>0) saritura = false;
            if(dy < 0 && !saritura) dy += stopInailtimeSaritura;

            if(dy > maxVitezaDeCadere) dy = maxVitezaDeCadere;

        }

    }

    public void update(){

        time++;

        //vad daca se teleporteaza la urmatorul nivel
        if(teleporting){
            energyParticles.add(
                    new EnergyParticles(tileMap, x, y, EnergyParticles.UP)
            );
        }
        //update la pozitie
        boolean cade = cazatura;
        getNextPosition();
        checkTileMapCollision();
        setPosition(temp_x, temp_y);
        if(dx == 0) x = (int)x;

        if(flinching) {
            flinchCount++;
            if(flinchCount > 120) {
                flinching = false;
                flinchCount = 0;
            }
        }

        // energy particles
        for(int i = 0; i < energyParticles.size(); i++) {
            energyParticles.get(i).update();
            if(energyParticles.get(i).shouldRemove()) {
                energyParticles.remove(i);
                i--;
            }
        }
        //cand dau scratch, merge la infinit pentru ca nu fac un update cum trebuie
        // verific deci daca atacul s-a oprit
        if(actiuneCurenta == SCRATCHING){
            if(animation.hasPlayedOnce())
                scratching = false; //ca sa mearge o data atacul
        }
        if(actiuneCurenta == FIREBALL){
            if(animation.hasPlayedOnce())
                attacking = false;
        }

        //fireball attack
        attack +=1;
        if(attack > maxAttack) attack = maxAttack;
        if(attacking && actiuneCurenta != FIREBALL){
            if(attack > attackCost){
                attack -= attackCost;
                FireBall fb = new FireBall(tileMap, facingRight);
                fb.setPosition(x, y);
                fireBalls.add(fb);
            }
        }

        //update fireball
        for(int i = 0; i< fireBalls.size() ; i++){
            fireBalls.get(i).update();
            if(fireBalls.get(i).shouldRemove()){
                fireBalls.remove(i);
                i--;
            }
        }

        //verific sa se termine flinching dupa ce am fost lovit
        /*if(flinching){
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed > 1000){
                flinching = false;
            }
        }*/

        //basic movement
        //set animation
        if(teleporting) {
            if(actiuneCurenta != TELEPORT) {
                actiuneCurenta = TELEPORT;
                animation.setFrames(sprites.get(TELEPORT));
                animation.setDelay(50);
                width = 16;
            }
        }
        else if(health == 0){
            if(actiuneCurenta != DEAD){
                actiuneCurenta = DEAD;
                animation.setFrames(sprites.get(DEAD));
                animation.setDelay(50);
                width = 32;
            }
        }
        else if(scratching){
            if(actiuneCurenta != SCRATCHING){
                JukeBox.play("scratch");
                actiuneCurenta = SCRATCHING;
                animation.setFrames(sprites.get(SCRATCHING));
                animation.setDelay(50);
                width = 32;
            }
        }
        else if(attacking){
            if(actiuneCurenta != FIREBALL){
                JukeBox.play("fire");
                actiuneCurenta = FIREBALL;
                animation.setFrames(sprites.get(FIREBALL));
                animation.setDelay(100);
                width = 32;
            }
        }
        else if(dy > 0){//daca cadem
            if(gliding){
                if(actiuneCurenta != GLIDING){
                    actiuneCurenta = GLIDING;
                    animation.setFrames(sprites.get(GLIDING));
                    animation.setDelay(100);
                    width = 16;
                }
            }
            else if(actiuneCurenta != FALLING){
                actiuneCurenta = FALLING;
                animation.setFrames(sprites.get(FALLING));
                animation.setDelay(100);
                width = 16;
            }
        }
        else if(dy < 0){//jump
            if(actiuneCurenta !=JUMPING){
                actiuneCurenta = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(-1);
                width = 16;
            }
        }
        else if(dreapta || stanga){//daca mergem la stanga sau dreapta,
            // fac animatia de mers
            if(actiuneCurenta != WALKING){
                actiuneCurenta = WALKING;
                animation.setFrames(sprites.get(WALKING));
                animation.setDelay(40);
                width = 16;
            }
        }
        else{
            if(actiuneCurenta != IDLE){
                actiuneCurenta = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(400);
                width = 16;
            }
        }


        animation.update();

        //set direction
        if(actiuneCurenta != SCRATCHING && actiuneCurenta != FIREBALL){
            if(dreapta) facingRight = true;
            if(stanga) facingRight = false;
        }

    }


    public void draw(Graphics2D g){

        setMapPosition(); //o functie care TREBUIE folosita la fiecare map object

        //draw fireball
        for(int i = 0; i < fireBalls.size(); i++){
            fireBalls.get(i).draw(g);
        }
        // draw energy particles
        for(int i = 0; i < energyParticles.size(); i++) {
            energyParticles.get(i).draw(g);
        }

        //draw player
        if(flinching){ //acest if este pentru momentul cand playerul a fost atins de atacurile inamice
            //ofera o senzatie de flinch pentru a fi un stimul vizual
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed / 100 % 2 == 0){
                return;
            }
        }

        super.draw(g);

    }


}
