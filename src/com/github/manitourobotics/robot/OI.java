
package com.github.manitourobotics.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.DigitalIOButton;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.github.manitourobotics.robot.commands.AcquisitionForward;
import com.github.manitourobotics.robot.commands.AcquisitionReverse;
import com.github.manitourobotics.robot.commands.AcquisitionStop;
import com.github.manitourobotics.robot.commands.toggleControls;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    public static Joystick madcatz = new Joystick(RobotMap.JOYSTICK_MADCATZ);
    public static Joystick logitech = new Joystick(RobotMap.JOYSTICK_LOGITECH);
    
    
    static int mode;
    static Button buttonMode = new JoystickButton(logitech, RobotMap.LOGITECH_BUTTON_MODE_SWITCH);
    
    public OI () {
            // Refresh mode
            String modeName="None";
            SmartDashboard.putString("Logitech Mode", modeName);
    }

    //Reset/set controls every teleop init
    public static void setupControls() {
        mode = RobotMap.MODE_SHOOTING; // Shooting always starts
        buttonMode.whenPressed(new toggleControls());
    }

    public static void executeControls() {
        if(mode == RobotMap.MODE_SHOOTING) {
            executeShootingControls();
        }
        else if(mode == RobotMap.MODE_CLIMBING) {
            executeClimbingControls();
        }
    }

    private static void executeClimbingControls() {
        // nothing yet
    }

    private static void executeShootingControls() {
        if(logitech.getRawButton(RobotMap.LOGITECH_BUTTON_ACQUISITION_FORWARD)) {
            new AcquisitionForward().start();
        }
        if(logitech.getRawButton(RobotMap.LOGITECH_BUTTON_ACQUISITION_REVERSE)) {
            new AcquisitionReverse().start();
        }
        if(logitech.getRawButton(RobotMap.LOGITECH_BUTTON_ACQUISITION_STOP)) {
            new AcquisitionStop().start();
        }
    }

    private static void executeAcquisitionControls() {
    }

    public static void displayControls() {

        // since mode is an integer, I need to convert that integer to something meaningful to output to the SmartDashboard
        String modeName;
        if(mode == RobotMap.MODE_SHOOTING) {
            modeName="Shooting";
        }
        else if(mode == RobotMap.MODE_CLIMBING) {
            modeName="Climbing";
        }
        else {
            modeName="None"; // this should never happen
        }

        SmartDashboard.putString("Logitech Mode", modeName);
    }

    public static void toggleMode() {
        // switch current modes
        if(mode == RobotMap.MODE_SHOOTING) {
            mode=RobotMap.MODE_CLIMBING;
        }
        else if(mode == RobotMap.MODE_CLIMBING) {
            mode=RobotMap.MODE_SHOOTING;
        }
    }

    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);
    
    // Another type of button you can create is a DigitalIOButton, which is
    // a button or switch hooked up to the cypress module. These are useful if
    // you want to build a customized operator interface.
    // Button button = new DigitalIOButton(1);
    
    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.
    
    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:
    
    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());
    
    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());
    
    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());
}
