

package brakeData;

import java.util.ArrayList;
import java.util.Random;


public class BrakeDataGenerator {

	private int timestamp = 0;
	//zu Testzwecken auskommentiert
	//private int extremeUse = 0;
	private Random random = new Random();
	private int useage = 0;
	private boolean driving = false;
	private int currentSpeed = 0;
	private int previousSpeed = 0;
	private ArrayList<BrakeUsage> allDrivingData;
	private boolean resetBrakes = false;
	private String brakeNumber = new String();
	private boolean ttf = false;

	/**
	 * 
	 * @param currentBrakeNumber
	 * @param ttf
	 * @return random data series of one brake
	 */
	public ArrayList<BrakeUsage> generateRandomDataForOneBrake(int currentBrakeNumber, boolean ttf) {
		allDrivingData = new ArrayList<>();
		this.ttf = ttf;
		brakeNumber = "brakes"+currentBrakeNumber;
			while(!resetBrakes){
				DrivingLocation location = DrivingLocation.values()[random.nextInt(DrivingLocation.values().length)];
				for(int i=0; i < random.nextInt(20)+10*(location.ordinal()+1); i++){
					createDrivingData(location, false);
					if(resetBrakes) break;
				}
				
//				System.out.println(timestamp);
			}
		return allDrivingData;
	}
	
	/**
	 * hard coded story
	 * @param ttf
	 * @return the list of data series for the brake of the story
	 */
	public ArrayList<BrakeUsage> generateStory(boolean ttf){
		brakeNumber = "brakesStory";
		allDrivingData = new ArrayList<>();
		this.ttf = ttf;
		while(!resetBrakes){
			DrivingLocation currentLocation = DrivingLocation.TOWN;
			for(int i=0; i<10; i++){
				createDrivingData(currentLocation, true);
				if(resetBrakes) break;
			}
			currentLocation = DrivingLocation.COUNTRY;
			for(int i=0; i<10; i++){
				if(resetBrakes) break;
				createDrivingData(currentLocation, true);
			}
//			System.out.println("this is a timeskip");
			useage += 10000;
			timestamp += 52000;
		}
		return allDrivingData;
	}

	/**
	 * contains but is not limited to:
	 * timestamp, pedalForce, brakeForce, useageCount, speed
	 * For further details look into the class BrakeUsage
	 * 
	 * @param location
	 * @param story boolean for the check if the data series is a hard coded story or random data
	 * @return the driving datapoints to each timestamp 
	 * 
	 */
	private ArrayList<BrakeUsage> createDrivingData(DrivingLocation location, boolean story){
		
			BrakeUsage townUse;
			BrakeForce pedalForce = BrakeForce.PRESS_NONE;
			int brakeForce = 0;
			
			if(driving){
				pedalForce = BrakeForce.values()[random.nextInt(BrakeForce.values().length-1)+1];
				brakeForce = computeBrakeStrengthByWearOff(pedalForce.getStrength());
			}
			int totalDuration = (int)computeDurationOfDrivingState(location, pedalForce, story? story : random.nextBoolean());
				
			int time = 0;
//			System.out.println("previousSpeed: " + previousSpeed + " currentSpeed: " + currentSpeed);
			int curSpeed = 0;
			do{
				townUse = generateOneDataPoint(pedalForce, brakeForce, time, totalDuration);
				curSpeed = townUse.speedAfter;
				allDrivingData.add(townUse);
				if(resetBrakes & ttf) break;
			}while(++time < totalDuration & curSpeed != currentSpeed);				
			driving = !driving;
		
		//return allTownDriving;
		return allDrivingData;
	}
	
	/**
	 * 
	 * @param pedalForce
	 * @param brakeForce
	 * @param time
	 * @param totalDuration
	 * @return One Data entry of BrakeUseage depending on accelerating or braking
	 */
	private BrakeUsage generateOneDataPoint(BrakeForce pedalForce, int brakeForce, int time, int totalDuration){
		computeUseage(pedalForce.getStrength());
		double newspeeds[] = computeAccelerationDiff(time, totalDuration, driving);
		int curSpeed = (int)newspeeds[0];
		int prevSpeed = (int)newspeeds[1];
		return new BrakeUsage(brakeNumber, pedalForce.getStrength(), brakeForce, 
				prevSpeed, curSpeed, ++timestamp, useage, computeEstimatedTimeTillFailure());
	}
	
	private int computeUseage(int pedalForce){
		return pedalForce == 0? useage : ++useage;
	}

	/**
	 * Depending on the current speed and location computing the time to the maxSpeed
	 * @param location
	 * @return the time the system needs to accelerate to maxSpeed
	 */
	private int computeAccelerationDuration(DrivingLocation location){
		previousSpeed = currentSpeed;
		currentSpeed = location.getMaxSpeed()-10+random.nextInt(20);
		return (random.nextInt(location.getMaxSpeed())+currentSpeed)/2;			
	}
	
	/**
	 * 
	 * @param location
	 * @param force
	 * @param fullstop
	 * @return the time the system needs to brake to the new speed
	 */
	private int computeBrakeDuration(DrivingLocation location, BrakeForce force, boolean fullstop){
		previousSpeed = currentSpeed;
		currentSpeed = fullstop? 0 : (int)(currentSpeed-currentSpeed*random.nextDouble());
		return 2*(previousSpeed-currentSpeed)/force.getStrength();
	}
	
	//TODO: neue Klasse erstellen, tendenziell für jegliches computing
	/**
	 * 
	 * @param time
	 * @param duration
	 * @param driving
	 * @return speedBefore and SpeedAfter for datapoints in between the start- and endpoint
	 * of an acceleration or brake episode
	 */
	private double[] computeAccelerationDiff(double time, double duration, boolean driving){
		double dur = duration;
		if(!driving & duration > (currentSpeed-previousSpeed)/10){
			dur = duration/4;
		}
		double accel = (currentSpeed - previousSpeed)/dur;
		double curSpeed = accel*(time+1)+previousSpeed;
		if(accel > 0)	curSpeed = curSpeed>currentSpeed? currentSpeed : curSpeed;
		else curSpeed = curSpeed<currentSpeed? currentSpeed : curSpeed;
		double prevSpeed = accel*time+previousSpeed;	
//		if(prevSpeed < 0 | curSpeed < 0)
//		System.out.println("accel: " + accel + " curSpeed: " + curSpeed + " prevSpeed: "+ prevSpeed + "time: "  + time
//							+ " currentSpeed: " + currentSpeed + " previousSpeed: " + previousSpeed);
		return new double[]{curSpeed, prevSpeed};
	}
	
	/**
	 * 
	 * @param location
	 * @param force
	 * @param fullstop
	 * @return Duration for one acceleration or one brake 
	 */
	private double computeDurationOfDrivingState(DrivingLocation location, BrakeForce force, boolean fullstop){
		double brakeDuration;
		if(force.getStrength() < 2){
			brakeDuration = computeAccelerationDuration(location);			
		}
		else{
			brakeDuration = computeBrakeDuration(location, force, fullstop);
			//TODO if(force.getStrength() > 70) extremeUse += brakeDuration;
		}
		return brakeDuration==0? 1 : brakeDuration;
	}
	
	//TODO computing of details should have an extra class
	//TODO not hardcoding the start time of failure and remain factor (aka 64000 and 1000)
	/**
	 * Computes the remaining useage time of the system
	 * depending on current useage
	 * @return
	 */
	private String computeEstimatedTimeTillFailure(){
		if(resetBrakes) return "0";
		int useRemain;
		if(useage > 1000){
		useRemain =	useage/1000;
		useRemain = 64000 - (useRemain*1000);
		}
		else useRemain = 64000;
		return useRemain<10? "10" : String.valueOf(useRemain);
	}
	
	//TODO  Abhängigkeit von einem timeUsed, statt timestamp
	/**
	 *
	 * computes the relation of the strength between the current wear-off state of the break
	 * and the force of the pedal.
	 * Both parameters are in percent
	 * 
	 * @param pedalForce the force which is pushed on the pedal itself
	 * @return the force on the break on the wheels
	 *
	 */
	private int computeBrakeStrengthByWearOff(int pedalForce){
		int brakeStrength = (int)(pedalForce*(2.0-((double)(/*extremeUse +*/ timestamp)/315360.0)));
		if((brakeStrength < pedalForce) 
				/* TODO exteremeUse ... das Ganze wahrscheinlich nicht mehr gebraucht
				 * |  ((timestamp + extremeUse/2) > 315360)*/){
			resetBrakes = true;
		}
		return brakeStrength > 100? 100 : brakeStrength;
	}
	

}
