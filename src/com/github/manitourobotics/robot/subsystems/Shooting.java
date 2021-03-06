/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.github.manitourobotics.robot.subsystems;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.github.manitourobotics.robot.OI;
import com.github.manitourobotics.robot.RobotMap;
import com.github.manitourobotics.robot.Team2945Robot;
import com.github.manitourobotics.robot.commands.ShootingOn;
import edu.wpi.first.wpilibj.Relay;

/**
 *
 * @author Justin
 */
public class Shooting extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private Relay relay = new Relay(RobotMap.RELAY_SHOOTER);
    private Relay relayPractice = new Relay(RobotMap.RELAY_SHOOTER_PRACTICE);  // only for practice robot

    public Shooting() {
        // The shooter should never go backwards
        // setting the direction only limits the possible range of motion
        relay.setDirection(Relay.Direction.kForward);
        relayPractice.setDirection(Relay.Direction.kForward);
    }

    public void setShootingMotors(Relay.Value value) {
        relay.set(value);
        relayPractice.set(value);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        //setDefaultCommand(new ShootingOn());
        /* setDefaultCommand(new ShootingOff()) */
    }
}
