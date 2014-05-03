/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.oastem.frc.assist2;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;
import org.oastem.frc.control.*;
import org.oastem.frc.Debug;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotMain extends SimpleRobot {
    
    private DriveSystem ds;
    private Joystick js, js2;
    
    String[] debug = new String[6];
    
    private final int LEFT_DRIVE_PORT = 1;
    private final int RIGHT_DRIVE_PORT = 2;
    
    private final int FIRST_JOYSTICK = 1;
    private final int SECOND_JOYSTICK = 2;
    
    private final double AUTO_SPEED = 0.5;
    
    public void robotInit(){
        ds = DriveSystem.getInstance();
        ds.initializeDrive(LEFT_DRIVE_PORT, RIGHT_DRIVE_PORT);
        
        js = new Joystick(FIRST_JOYSTICK);
        js2 = new Joystick(SECOND_JOYSTICK);
        
        Debug.clear();
        Debug.log(1, 1, "Robot initialized.");
    }
    
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        //move forward
        ds.tankDrive(AUTO_SPEED, AUTO_SPEED);
        Timer.delay(5000);
        //turn clockwise
        ds.tankDrive(AUTO_SPEED, -AUTO_SPEED);
        Timer.delay(1000);
        //move forward
        ds.tankDrive(AUTO_SPEED, AUTO_SPEED);
        Timer.delay(2000);
        //stop
        ds.tankDrive(0, 0);
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        while(isEnabled() && isOperatorControl()){
            debug[1] = "Speed: " + js.getY() + ", " + js2.getY();
            ds.tankDrive(js.getY(), js2.getY());
            
            Debug.log(debug);
        }
    }
    
    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
    
    }
}
