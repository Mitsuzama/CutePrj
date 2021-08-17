package GameState;

import Audio.JukeBox;
import Entity.*;
import Entity.Enemies.Slugger;
import Main.GamePanel;
import TileMap.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

//@SuppressWarnings("Commented out code")
public class Nivel1 extends  GameState{

    private TileMap tileMap;
    private Background bg;

    private ArrayList<Enemy> dusmani;
    private ArrayList<Explosion> bumbum;
    private Player player;
    private HeadsUpDisplay hud;
    private int eventCount = 0;
    private ArrayList<Rectangle> tb;

    //evenimente
    private boolean eventFinish;
    private boolean eventDead;
    private boolean eventStart;
    private boolean blockInput = false;

    //teleport
    private Teleport teleportare;


    private BufferedImage hageonText;
    private Titlu subtitlu;
    private Titlu titlu;


    public Nivel1(GameStateManager gsm){
        super(gsm);
        init();
    }

    @Override
    public void init() {

        //tilemap
        tileMap = new TileMap(30);
        tileMap.loadTiles("/Tilesets/grasstileset.gif");
        tileMap.loadMap("/Maps/harta1.map");
        tileMap.setPosition(0,0);
        tileMap.setSmootharoo(1);

        //background
        bg = new Background("/Backgrounds/grassbg1.gif",0.1);

        //player
        player = new Player(tileMap);
        player.setPosition(100, 100);
        player.setHealth(SaverPlayer.getHealth());
        player.setTime(SaverPlayer.getTime());


        //dusmani
        saAparaDusmanii(); //functie care spawneaza dusmani

        //exploziile
        bumbum = new ArrayList<Explosion>();

        //hud
        hud = new HeadsUpDisplay(player);

        //titlul
        try {
            hageonText = ImageIO.read(
                    getClass().getResourceAsStream("/Titlu/titlu_nivel_1.gif")
            );
            titlu = new Titlu(hageonText.getSubimage(0, 0, 80, 16));
            titlu.setY(60);
            subtitlu = new Titlu(hageonText.getSubimage(0, 17, 50, 15));
            subtitlu.setY(85);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        //teleportare
        teleportare = new Teleport(tileMap);
        teleportare.setPosition(2400, 190);

        // start event
        eventStart = true;
        tb = new ArrayList<Rectangle>();
        eventStart();

        //sfx
        JukeBox.load("jump", "/SFX/jump.mp3");
        JukeBox.load("fire", "/SFX/fire.mp3");
        JukeBox.load("teleport", "/SFX/teleport.mp3");
        JukeBox.load("scratch", "/SFX/scratch.mp3");
        JukeBox.load("nivel1", "/Melodie/music.mp3");
        JukeBox.loop("nivel1", 600, JukeBox.getFrames("nivel1") - 2200);

    }

    //startul nivelului
    private void eventStart() {
        eventCount++;
        if(eventCount == 1){ //aca suntem la prima iteratie, creem dreptunghiurile
            tb.clear();
            tb.add(new Rectangle(
                    0,
                    0,
                    GamePanel.WIDTH,
                    GamePanel.HEIGHT / 2
                    )
            );
            tb.add(new Rectangle(
                    0,
                    0,
                    GamePanel.WIDTH / 2,
                    GamePanel.HEIGHT
                    )
            );
            tb.add(new Rectangle(
                    0,
                    GamePanel.HEIGHT / 2,
                    GamePanel.WIDTH,
                    GamePanel.HEIGHT / 2
                    )
            );
            tb.add(new Rectangle(
                    GamePanel.WIDTH / 2,
                    0,
                    GamePanel.WIDTH / 2,
                    GamePanel.HEIGHT
                    )
            );
        }

        if(eventCount < 60 && eventCount >1){
             tb.get(0).height -= 4;
             tb.get(1).width -= 6;
             tb.get(2).y +=4;
             tb.get(3).x +=6;
        }

        if(eventCount == 30){
            titlu.begin();
        }

        if(eventCount == 60){
            eventStart = blockInput = false;
            eventCount = 0;
            subtitlu.begin();;
            tb.clear();
        }
    }

    private void saAparaDusmanii(){
        dusmani = new ArrayList<Enemy>();
        Slugger s;
        Point[] spawns = new Point[]{
                new Point(860, 200),
                new Point(1525, 200),
                new Point(1680, 200),
                new Point(1800, 200)
        };

        for(int i = 0; i< spawns.length ; i++){
            s = new Slugger(tileMap);
            s.setPosition(spawns[i].x, spawns[i].y);
            dusmani.add(s);
        }

    }

    @Override
    public void update() {

        //verificare daca suntem la sfarsitul nivelului
        if(teleportare.contains(player)) {
            eventFinish = blockInput = true;
        }

        //verificare daca am murit
        if(player.getHealth() == 0 || player.gety() > tileMap.getHeight()){
            eventDead = blockInput = true;
        }

        // rulez evenimentele
        if(eventStart) eventStart();
        if(eventDead) eventDead();
        if(eventFinish) eventFinish();

        // move titlu and subtitlu
        if(titlu != null) {
            titlu.update();
            if(titlu.shouldRemove()) titlu = null;
        }
        if(subtitlu != null) {
            subtitlu.update();
            if(subtitlu.shouldRemove()) subtitlu = null;
        }

        //update la player
        player.update();

        //update la tilmap
        tileMap.setPosition(
                GamePanel.WIDTH / 2 - player.getx(),
                GamePanel.HEIGHT / 2 - player.gety()
        );
        tileMap.update();
        tileMap.fixBounds();

        //imaginea de fundal care da scroll odata cu caracterul
        bg.setPosition(tileMap.getx(), tileMap.gety());

        //verfic daca dau atac la dusmani
        player.checkAttack(dusmani);

        //dau update la dusmani
        for(int i = 0; i<dusmani.size(); i++){
            Enemy e = dusmani.get(i);
            e.update();
            if(e.isMort()){
                dusmani.remove(i);
                i--;
                bumbum.add(new Explosion(e.getx(), e.gety()));
            }
        }

        //update la explozii
        for(int i = 0; i<bumbum.size(); i++){
            bumbum.get(i).update();
            if(bumbum.get(i).shouldRemove()){
                bumbum.remove(i);
                i--;
            }
        }

        //update la teleportare
        teleportare.update();
    }

    @Override
    public void draw(Graphics2D g) {

        //instantiez imaginea de fundal
        bg.draw(g);

        //draw tilemap
        tileMap.draw(g);

        //draw player
        player.draw(g);

        //desenez dusmanii
        for(int i = 0; i < dusmani.size() ; i++){
            dusmani.get(i).draw(g);
        }

        //desenez explozia
        for(int i = 0; i<bumbum.size(); i++){
            bumbum.get(i).setMapPosition((int)tileMap.getx(), (int)tileMap.gety());
            bumbum.get(i).draw(g);
        }

        //desenez hud-ul
        hud.draw(g);

        //desenez teleporterul
        teleportare.draw(g);

        //desenez titlul
        if(titlu != null) titlu.draw(g);
        if(subtitlu != null) subtitlu.draw(g);

        //tranzitii
        g.setColor(Color.BLACK);
        for(int i = 0; i< tb.size(); i++){
            g.fill(tb.get(i));
        }
    }

    @Override
    public void keyPressed(int k) {
        if(k == KeyEvent.VK_LEFT) player.setLeft(true);
        if(k == KeyEvent.VK_RIGHT) player.setRight(true);
        if(k == KeyEvent.VK_UP) player.setTop(true);
        if(k == KeyEvent.VK_DOWN) player.setBot(true);
        if(k == KeyEvent.VK_W) player.setJumping(true);
        if(k == KeyEvent.VK_E) player.setGliding(true);
        if(k == KeyEvent.VK_R) player.setScratching();
        if(k == KeyEvent.VK_F) player.setAttacking();
        if(k == KeyEvent.VK_Q) gsm.setState(0);
    }

    @Override
    public void keyReleased(int k) {
        if(k == KeyEvent.VK_LEFT) player.setLeft(false);
        if(k == KeyEvent.VK_RIGHT) player.setRight(false);
        if(k == KeyEvent.VK_UP) player.setTop(false);
        if(k == KeyEvent.VK_DOWN) player.setBot(false);
        if(k == KeyEvent.VK_W) player.setJumping(false);
        if(k == KeyEvent.VK_E) player.setGliding(false);
    }

    // daca am murit
    private void eventDead() {
        eventCount++;
        if(eventCount == 1) {
            player.setDead();
            player.stop();
        }
        if(eventCount == 60) {
            tb.clear();
            tb.add(new Rectangle(
                    GamePanel.WIDTH / 2,
                    GamePanel.HEIGHT / 2,
                    0,
                    0
                    )
            );
        }
        else if(eventCount > 60) {
            tb.get(0).x -= 6;
            tb.get(0).y -= 4;
            tb.get(0).width += 12;
            tb.get(0).height += 8;
        }
        if(eventCount >= 120) {
            if(player.getHealth() == 0) {
                gsm.setState(GameStateManager.MENU_STATE);
            }
            else {
                eventDead = blockInput = false;
                eventCount = 0;
                player.loseHealth();
                reset();
            }
        }
    }

    private void reset(){ //functia de resetare a jucatorului dupa moarte
        player.reset();
        player.setPosition(300, 161);
        saAparaDusmanii();
        blockInput = true;
        eventCount = 0;
        tileMap.setShaking(false, 0);
        eventStart = true;
        eventStart();
        titlu = new Titlu(hageonText.getSubimage(0, 0, 80, 16));
        titlu.setY(60);
        subtitlu = new Titlu(hageonText.getSubimage(0, 17, 50, 13));
        subtitlu.setY(85);

    }
    // finished level
    private void eventFinish() {
        eventCount++;
        if(eventCount == 1) {
            JukeBox.play("teleport");
            player.setTeleporting(true);
            player.stop();
        }
        else if(eventCount == 120) {
            tb.clear();
            tb.add(new Rectangle(
                    GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
        }
        else if(eventCount > 120) {
            tb.get(0).x -= 6;
            tb.get(0).y -= 4;
            tb.get(0).width += 12;
            tb.get(0).height += 8;
            JukeBox.stop("teleport");
        }
        if(eventCount == 180) {
            SaverPlayer.setHealth(player.getHealth());
            SaverPlayer.setTime(player.getTime());
            gsm.setState(GameStateManager.NIVEL2_STATE);
        }

    }
}
