import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;


public class Main  implements Runnable {
    static TargetDataLine line;
    static ArrayList<Integer> PreviousVoices;
    static JFrame frame;

    static String location = "C:\\Users\\Akhil\\IdeaProjects\\NanaBirthdayGift\\src\\";


    public static void main(String[] args) {
        final int WIDTH = 1200;
        final int HEIGHT = 600;
        int[][] dimensions = {{441,299},{100,100}};
        int[][] dimensions1 = {{602,299},{100,100}};
        int[][] dimensions3 = {{602+161,299},{100,100}};
        frame = CreateFrame("Audio Recorder", WIDTH,HEIGHT);

        PreviousVoices = new ArrayList<>();

        CreateButton("Record Your Audio", frame, dimensions,"MicIcon", ".png", 0);
        CreateButton("Stop your audio", frame, dimensions1,"circle", ".png", 2);
        CreateButton("Play Audio", frame,dimensions3,"play-button",".png",1, "output");


        // FIXME: Adding Mouse Listener for Debugging purposes, remove if debugging is finished
//        frame.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                System.out.println(e.getX() + " " + e.getY());
//
//            }
//        });

        //CreateButton("sup bro??", frame, dimensions1,"MicIcon", ".png", 0);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
    }


    public static void CreateButton(String text, JFrame frame, int[][] dimensions, String IconName,String fileType, int type)  {
        //if we are playing audio throw an exception if path to file is not specified
        if (type == 1)
        {
            // if 1 then use a different overload
            throw new RuntimeException("Path to Audio File is not Specified");

        }else {

            ImageIcon icon = new ImageIcon(location + IconName + fileType);
            Image image = icon.getImage().getScaledInstance(dimensions[1][0], dimensions[1][1], Image.SCALE_SMOOTH);

            JButton button = new JButton(new ImageIcon(image));
            button.setBounds(dimensions[0][0] - dimensions[1][0], dimensions[0][1] - dimensions[1][1], dimensions[1][0], dimensions[1][1]);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    if (type == 2)
                    {
                        line.stop();
                        line.close();

                    }else{



                        Thread thread = new Thread(new Main());
                        thread.start();
                    }


                }
            }

            );
            frame.add(button);
        }
    }



    // Playing Audio
    public static void CreateButton(String text, JFrame frame, int[][] dimensions, String IconName,String fileType, int type,String FilePath)  {


        ImageIcon icon = new ImageIcon(location+IconName+fileType);
        Image image = icon.getImage().getScaledInstance(dimensions[1][0], dimensions[1][1], Image.SCALE_SMOOTH);

        JButton button = new JButton(new ImageIcon(image));


        button.setBounds(dimensions[0][0]-dimensions[1][0],dimensions[0][1]-dimensions[1][1],dimensions[1][0],dimensions[1][1]);

        button.addActionListener(e -> {
            PlayAudio(FilePath+(PreviousVoices.size() - 1));
        });


        frame.add(button);
    }

    public static JFrame CreateFrame(String title, int width, int height)
    {
        JFrame frame = new JFrame(title);
        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        return frame;


    }

    public static void GetAudioFromMic()
    {

        try
        {
            AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class,format);
            line = (TargetDataLine) AudioSystem.getLine(info);

            // Open the target data line
            line.open(format);

            // Start capturing audio
            line.start();

            File outputFile = new File(location+"output"+PreviousVoices.size()+".wav");
            PreviousVoices.add(0);
            AudioSystem.write(new AudioInputStream(line), AudioFileFormat.Type.WAVE, outputFile);


        }catch (Exception e)
        {
            System.out.println(e);
        }



    }

    public static void PlayAudio(String wavFile)
    {
        try {
            File audioFile = new File(location + wavFile + ".wav");
            AudioInputStream AudioStream = AudioSystem.getAudioInputStream(audioFile);

            Clip clip = AudioSystem.getClip();
            clip.open(AudioStream);

            clip.start();

            Thread.sleep(clip.getMicrosecondLength()/1000);
            clip.close();
            AudioStream.close();
        }catch (Exception e)
        {
            System.out.println(e);
        }

    }


    @Override
    public void run() {
        GetAudioFromMic();
    }


}
