
package org.oastem.frc.assist2;


import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Solenoid;
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
import org.oastem.frc.sensor.ADXL345Accelerometer;

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
		private static final int TOGGLE_WHEEL_BUTTON = 1;
		private static final int FIRE_FRISBEE_BUTTON = 1;
		private static final int SHOOTER_ARM_UP_BUTTON = 3;
		private static final int SHOOTER_ARM_DOWN_BUTTON = 2;
		private static final int ACTUATOR_UP_BUTTON = 3;
		private static final int ACTUATOR_DOWN_BUTTON = 2;
		private static final int FIRST_SOLENOID_FORWARD = 3;
		private static final int FIRST_SOLENOID_REVERSE = 2;
		private static final int SECOND_SOLENOID_FORWARD = 3;
		private static final int SECOND_SOLENOID_REVERSE = 2;
		
		// CONSTANTS
		private static final double SHOOTER_ARM_POWER = 0.3;

		// instance variables
		private static double joyScale = 1.0;
		private static double accelAvg = 0.0;
		private static double[] accelAvgs = new double[15];
		
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
		private DoubleSolenoid actuator;
		private Solenoid actuator2;
		private ADXL345Accelerometer accelerometer;
		
		
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
			accelerometer = new ADXL345Accelerometer(I2C.Port.kOnboard);
			
			actuator2 = new Solenoid(2);
			actuator = new DoubleSolenoid(0, 1);
			
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
			boolean isShooting = false;
			boolean reverseArmPressed = true;
			boolean pistonMoving = false;
			boolean pressCheck = false;

			while (isOperatorControl() && isEnabled()) {
				
				// drive
				if (hasDrive) {
					 drive.tankDrive(-joyjoyLeft.getY() * scaleZ(joyjoyRight.getZ()), -joyjoyRight.getY() * scaleZ(joyjoyRight.getZ()));
				}

				
				// Firing frisbee
				/*
				if (joyjoyRight.getRawButton(FIRE_FRISBEE_BUTTON))
					isShooting = true;
				
				if (isShooting)
				{
					reverseArmPressed = false;
					shooterArm.set(SHOOTER_ARM_POWER);
					if (!shooterArm.getForwardLimitOK())
						isShooting = false;
				}
				else if (!reverseArmPressed)
				{
					shooterArm.set(-SHOOTER_ARM_POWER);
					if (!shooterArm.getReverseLimitOK())
						reverseArmPressed = true;
				}
				else
					shooterArm.set(0);
				//*/
				
				// shooting wheel
				/*
				if (!joyjoyLeft.getRawButton(TOGGLE_WHEEL_BUTTON)){
					canPressWheel = true;
				}
				else if (canPressWheel && joyjoyLeft.getRawButton(TOGGLE_WHEEL_BUTTON)){
					canPressWheel = false;
					shooterWheelOn = !shooterWheelOn;
				}
				
				if (shooterWheelOn){
					shooterWheel.set(accel.accelerateValue(-scaleZ(joyjoyLeft.getZ())));
				}
				else {
					shooterWheel.set(0);
				}
				
				// backup manual shooting arm
				if (joyjoyLeft.getRawButton(SHOOTER_ARM_UP_BUTTON))
					shooterArm.set(SHOOTER_ARM_POWER);
				else if (joyjoyLeft.getRawButton(SHOOTER_ARM_DOWN_BUTTON))
					shooterArm.set(-SHOOTER_ARM_POWER);
				else
					shooterArm.set(0);
				*/
				
				// actuators
				/* if (joyjoyRight.getRawButton(ACTUATOR_UP_BUTTON))
					actuator.set(DoubleSolenoid.Value.kForward);
				else if (joyjoyRight.getRawButton(ACTUATOR_DOWN_BUTTON))
					actuator.set(DoubleSolenoid.Value.kReverse);
				else
					actuator.set(DoubleSolenoid.Value.kOff);
				*/
				
				//This block uses the second solenoid to stop air from going out. The first solenoid is then turned off.
				/*if (pressCheck && pistonMoving && (joyjoyRight.getRawButton(ACTUATOR_UP_BUTTON) || joyjoyRight.getRawButton(ACTUATOR_DOWN_BUTTON))){
					System.out.println("Piston is not moving");
					pressCheck = false;
					actuator.set(DoubleSolenoid.Value.kOff);
					actuator2.set(DoubleSolenoid.Value.kReverse);
					pistonMoving = false;
				}*/
				
				//This block uses the buttons in order to control the motion of the piston and then recognizes the piston as moving.
				//else 
				/*
					if (pressCheck && (joyjoyRight.getRawButton(ACTUATOR_UP_BUTTON) || joyjoyRight.getRawButton(ACTUATOR_DOWN_BUTTON))) {
					pressCheck = false;
					if (joyjoyRight.getRawButton(ACTUATOR_UP_BUTTON)){
						System.out.println("Piston is going up");
						actuator.set(DoubleSolenoid.Value.kForward);
						pistonMoving = true;
					}
					else if (joyjoyRight.getRawButton(ACTUATOR_DOWN_BUTTON)){
						System.out.println("Piston is going down");
						actuator2.set(DoubleSolenoid.Value.kForward);
						actuator.set(DoubleSolenoid.Value.kReverse);
						pistonMoving = true;
					}
				}
				
				//This provides a check so that the robot will not continuously cycle through loops and constantly stop moving and begin moving.
				if (!(joyjoyRight.getRawButton(ACTUATOR_UP_BUTTON) || joyjoyRight.getRawButton(ACTUATOR_DOWN_BUTTON))){
					pressCheck = true;
				}
				*/
				
				//TESTING
				dash.putNumber("X Value", accelerometer.getX());
				dash.putNumber("Y Value", accelerometer.getY());
				dash.putNumber("Z Value", accelerometer.getZ());
				dash.putNumber("First average (equation):", accelAvgOne());
				dash.putNumber("Second average (array):", accelAvgTwo());
				
				if (joyjoyRight.getRawButton(FIRST_SOLENOID_FORWARD))
					actuator.set(DoubleSolenoid.Value.kForward);
				else if (actuator.get().equals(DoubleSolenoid.Value.kReverse) || actuator.get().equals(DoubleSolenoid.Value.kOff))//joyjoyRight.getRawButton(FIRST_SOLENOID_REVERSE))
					actuator.set(DoubleSolenoid.Value.kOff);//Reverse);
				else
					actuator.set(DoubleSolenoid.Value.kReverse);

				if (joyjoyLeft.getRawButton(SECOND_SOLENOID_FORWARD))
					actuator2.set(true);
				else// if (joyjoyLeft.getRawButton(SECOND_SOLENOID_REVERSE))
					actuator2.set(false);
				
				dash.putBoolean("pressCheck", pressCheck);
				dash.putBoolean("pistonMoving", pistonMoving);
				dash.putBoolean("ACTUATOR_UP_BUTTON", joyjoyRight.getRawButton(ACTUATOR_UP_BUTTON));
				dash.putBoolean("ACTUATOR_DOWN_BUTTON", joyjoyRight.getRawButton(ACTUATOR_DOWN_BUTTON));
				
					
			}
		}
		
		private double accelAvgOne()
		{
			accelAvg = (accelAvg * 49 + accelerometer.getZ()) / 50;
			return accelAvg;	
		}
		
		private double accelAvgTwo()
		{
			for (int i = accelAvgs.length - 1; i > 0; i--)
			{
				accelAvgs[i] = accelAvgs[i - 1];
			}
			accelAvgs[0] = accelerometer.getZ();
			return calcAvg();
		}
		
		private double calcAvg()
		{
			double sum = 0;
			for (int i = 0; i < accelAvgs.length; i++) 
			{
				sum += accelAvgs[i];
			}
			return sum / 15;
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
