
package org.oastem.frc.assist2;


import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

import org.oastem.frc.Dashboard;
import org.oastem.frc.control.DriveSystem;
import org.oastem.frc.control.DriveSystemAccel;
import org.oastem.frc.sensor.QuadratureEncoder;
import org.oastem.frc.control.Accelerator;

/**
 * This is a demo program showing the use of the RobotDrive class.
 * The SampleRobot class is the base of a robot application that will automatically call your
 * Autonomous and OperatorControl methods at the right time as controlled by the switches on
 * the driver station or the field controls.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're inexperienced,
 * don't. Unless you know what you are doing, complex code will be much more difficult under
 * this system. Use IterativeRobot or Command-Based instead if you're new.
 */
public class Robot extends SampleRobot {
	// MOTOR PORTS
		private static final int DRIVE_RIGHT_FRONT_PORT = 0;
		private static final int DRIVE_RIGHT_BACK_PORT = 1;
		private static final int DRIVE_LEFT_FRONT_PORT = 2;
		private static final int DRIVE_LEFT_BACK_PORT = 3;

		// SENSOR PORTS
		//Jaguar CAN IDs
		private static final int SHOOTER_WHEEL_CAN_ID = 1;
		private static final int SHOOTER_ARM_CAN_ID = 5;
		//Encoders
		private static final int RIGHT_ENC_I = 4;
		private static final int RIGHT_ENC_A = 5;
		private static final int RIGHT_ENC_B = 3;
		private static final int LEFT_ENC_I = 1;
		private static final int LEFT_ENC_A = 2;
		private static final int LEFT_ENC_B = 0;
		
		//Autonomous 


		// JOYSTICK BUTTONS
		private static final int TOGGLE_WHEEL_BUTTON = 2;
		private static final int SHOOTER_ARM_UP_BUTTON = 3;
		private static final int SHOOTER_ARM_DOWN_BUTTON = 2;
		
		// CONSTANTS
		private static final double SHOOTER_ARM_POWER = 0.3;

		// instance variables
		private static double joyScale = 1.0;
		
		// MOTORS
		private CANJaguar shooterWheel;
		private CANJaguar shooterArm;

		// DECLARING OBJECTS
		private DriveSystemAccel drive;
		private Joystick joyjoyRight;
		private Joystick joyjoyLeft;
		private Dashboard dash;
		private PowerDistributionPanel power;
		private Accelerator accel;
		
		
		public void robotInit() {
			
			// Initialize Drive
			drive = DriveSystemAccel.getInstance();
			drive.initializeDrive(DRIVE_LEFT_FRONT_PORT, DRIVE_LEFT_BACK_PORT, DRIVE_RIGHT_FRONT_PORT, DRIVE_RIGHT_BACK_PORT);
			drive.setSafety(false);

			// Used to display PDP on Dashboard
			power = new PowerDistributionPanel();
			power.clearStickyFaults();

			shooterWheel = new CANJaguar(SHOOTER_WHEEL_CAN_ID);
			shooterArm = new CANJaguar(SHOOTER_ARM_CAN_ID);

			shooterWheel.setPercentMode();
			shooterWheel.enableControl();
			
			joyjoyRight = new Joystick(0);
			joyjoyLeft = new Joystick(1);
			
			accel = new Accelerator();
			
			dash = new Dashboard();
			dash.putString("Mode:", "Auto");
			System.out.println("Robot Initialized");
			
		
		}

		public void autonomous() {
			
		}
		



		/**
		 * Runs the motors with arcade steering.
		 */
		public void operatorControl() {
			boolean hasDrive = true;
			boolean shooterWheelOn = false;
			boolean canPressWheel = true;

			while (isOperatorControl() && isEnabled()) {
				
				if (hasDrive) {
					 drive.tankDrive(-joyjoyLeft.getY() * scaleZ(joyjoyRight.getZ()), -joyjoyRight.getY() * scaleZ(joyjoyRight.getZ()));
				}
				
				if (!joyjoyRight.getRawButton(TOGGLE_WHEEL_BUTTON)){
					canPressWheel = true;
				}
				else if (canPressWheel && joyjoyRight.getRawButton(TOGGLE_WHEEL_BUTTON)){
					canPressWheel = false;
					shooterWheelOn = !shooterWheelOn;
				}
				
				if (shooterWheelOn){
					shooterWheel.set(accel.accelerateValue(-scaleZ(joyjoyLeft.getZ())));
				}
				else {
					shooterWheel.set(0);
				}
				
				if (joyjoyLeft.getRawButton(SHOOTER_ARM_UP_BUTTON))
					shooterArm.set(SHOOTER_ARM_POWER);
				else if (joyjoyLeft.getRawButton(SHOOTER_ARM_DOWN_BUTTON))
					shooterArm.set(-SHOOTER_ARM_POWER);
				else
					shooterArm.set(0);
				
				
					
			}
		}
		
		private double scaleZ(double rawZ) {
			return Math.min(1.0, 0.5 - 0.5 * rawZ);
		}

		/**
		 * Runs during test mode
		 */
		 public void test() {
			 
		}
}
