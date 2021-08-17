package Audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.HashMap;

public class JukeBox {

    private static HashMap<String, Clip> clips;
    private static boolean mut = false;
    private static int gap;

    public static void init(){
        clips = new HashMap<String, Clip>();
    }

    public static void load(String n, String s){
        if(clips.get(n) != null) return;
        Clip clip;
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(JukeBox.class.getResourceAsStream(s));
            AudioFormat format = ais.getFormat();
            AudioFormat decode = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    format.getSampleRate(),
                    16,
                    format.getChannels(),
                    format.getChannels() * 2,
                    format.getSampleRate(),
                    false
            );
            AudioInputStream convert = AudioSystem.getAudioInputStream(decode, ais);

            clip = AudioSystem.getClip();
            clip.open(convert);
            clips.put(n, clip);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void play(String s){
        play(s, JukeBox.gap);
    }

    public static void play(String s, int g){
        if(mut) return;
        Clip temp = clips.get(s);
        if(temp == null)return; //ca sa fiu sigur ca pot da play la ceva
        if(temp.isRunning()) temp.stop();
        temp.setFramePosition(gap);
        while (!temp.isRunning()) temp.start();
    }

    public static void stop(String s){ //functie de oprire a sunetului
        Clip temp = clips.get(s);
        if(temp == null)return; //nu am ce opri
        if(temp.isRunning()) temp.stop();
    }

    public static void resume(String s){
        Clip temp = clips.get(s);
        if(mut)return;
        if(temp.isRunning()) return;
        temp.start();
    }

    public static void loop(String s){
        loop(s, gap, gap, clips.get(s).getFrameLength()-1);
    }
    public static void loop(String s, int frame){
        loop(s, frame, gap, clips.get(s).getFrameLength()-1);
    }
    public static void loop(String s, int start, int end){
        loop(s, gap, start, end);
    }
    public static void loop(String s, int frame, int start, int end){
        stop(s);
        if(mut) return;
        clips.get(s).setLoopPoints(start, end);
        clips.get(s).setFramePosition(frame);
        clips.get(s).loop(Clip.LOOP_CONTINUOUSLY);
    }

    public static void setPosition(String s, int frame){
        clips.get(s).setFramePosition(frame);
    }

    public static int getFrames(String s) {
        return clips.get(s).getFrameLength();
    }

    public static int getPosition(String s){
        return clips.get(s).getFramePosition();
    }

    public static void close(String s) {
        stop(s);
        clips.get(s).close();
    }
}
