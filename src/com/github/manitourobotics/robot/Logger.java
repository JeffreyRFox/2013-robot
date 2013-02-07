/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.manitourobotics.robot;

import com.github.manitourobotics.robot.commands.ElbowControl;
import com.github.manitourobotics.robot.commands.MoveSmallArmsDown;
import com.github.manitourobotics.robot.commands.MoveSmallArmsUp;
import com.github.manitourobotics.robot.commands.ShoulderControl;
import com.github.manitourobotics.robot.commands.StopSmallArms;
import com.sun.squawk.microedition.io.FileConnection;
import com.sun.squawk.util.StringTokenizer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import javax.microedition.io.Connector;

/**
 *
 * @author robotics
 */
public class Logger {
    private static boolean recording;
    private static boolean playing; 
    private static String pausedFilename;
    private static int pausedFileReadPosition;
    private static DataInputStream in;
    private static DataOutputStream out;
    private static Timer timer = new Timer();
    private static FileConnection fileConnection;
    private static String oldMode;

    public static final int SMALL_ARMS = 1;
    public static final int SHOULDER_ARMS = 2;
    public static final int ELBOW_ARMS = 3;

    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int STOP = 3;

    private static double timeStamp = 0;
    private static int commandName;
    private static String content;
    private static int fileNumber = 1;
    

    public Logger() {
        try {
            // Find a unique filename test<x>.txt where x is a number. Never erase a log
            // One must move the log to final.txt (Get a ftp client under /ni-rt/system/) 
            // to actually read the log 
            do {
            fileConnection = (FileConnection) Connector.open("file://test" + Integer.toString(fileNumber) + ".txt", Connector.READ_WRITE);

            fileNumber += 1;
            } while (!fileConnection.exists());

            fileConnection.create();


            SmartDashboard.putString("Logger", "init");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isRecording() {
        return recording;
    }
    public static void resume() {
    }
    public static void play(String filename) {
    }
    public static void startPlay() {
        if(recording) {
            return;
        }
        try {
        in = fileConnection.openDataInputStream();
        } catch (IOException e) {
            System.out.println("cannot read file");
        }

        OI.togglePlayMode();
        SmartDashboard.putString("Logger", "Playing");
        playing = true;
        timer.reset();
        timer.start();
        timeStamp = 0;
    }
    public static void stopPlay() {
        OI.togglePlayMode();
        timer.reset();
        try {
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        playing = false;
        SmartDashboard.putString("Logger", "Done Playing");

    }
    public static void togglePlay() {
        if(recording) {
            return;
        }
        if(!playing) {
            startPlay();
        }  else {
            stopPlay();
        }
    }
    public static void play() {
        if(!playing) {
            return;
        }
        if (timer.get() <= timeStamp) {
            // wait until the timer is triggered
            return;
        }
        String data;
        StringTokenizer tok;

        try {
            Watchdog.getInstance().feed();
            data = in.readUTF();
            System.out.println(data);
            tok = new StringTokenizer(data, ":");
            timeStamp = Double.parseDouble(tok.nextToken());
            commandName = Integer.parseInt(tok.nextToken());
            content = tok.nextToken();

            if(commandName == SMALL_ARMS) {
                int contentSmallArms = Integer.parseInt(content);
                if (contentSmallArms == UP) {
                    Scheduler.getInstance().add(new MoveSmallArmsUp());
                } else if (contentSmallArms == DOWN) {
                    Scheduler.getInstance().add(new MoveSmallArmsDown());
                } else if (contentSmallArms == STOP) {
                    Scheduler.getInstance().add(new StopSmallArms());
                }
            } else if(commandName == SHOULDER_ARMS) {
                double contentShoulderArms = Double.parseDouble(content);
                Scheduler.getInstance().add(new ShoulderControl(contentShoulderArms));

            } else if(commandName == ELBOW_ARMS) {
                double contentElbowArms = Double.parseDouble(content);
                Scheduler.getInstance().add(new ElbowControl(contentElbowArms));
            }
        } catch (EOFException eof) {
            System.out.println("End of file");
            eof.printStackTrace();
            stopPlay();
            return;
        } catch (Exception e){
            stopPlay();
            e.printStackTrace(); 
            return;
        }
    }

    public static void loggingToggle() {
        if(playing) {
            return;
        }
        if(!recording) { // start logging
            SmartDashboard.putString("Logger", "Logging");
            timer.start();
            recording = true;
            try {
                out = fileConnection.openDataOutputStream();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else { // clean up from logging
            SmartDashboard.putString("Logger", "Done Logging");
            recording = false; 
            timer.stop(); 
        }
    }

    public static void log(int commandName, String content ) {
        if (!recording) {
            return;
        }
        try {

            String timestamp = Double.toString(timer.get());
            out.writeUTF(timestamp + ":" + Integer.toString(commandName) + ":" + content );
            out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    
    
}
