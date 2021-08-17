package Entity;

public class SaverPlayer {
    public static int health = 5;
    public static long
    time = 0;

    public static void init(){
        health = 5;
        time = 0;
    }

    //gettere
    public static int getHealth(){
        return health;
    }

    public static long getTime() {
        return time;
    }

    //settere
    public static void setHealth(int h){
        health = h;
    }

    public static void setTime(long t) {
        time = t;
    }
}
