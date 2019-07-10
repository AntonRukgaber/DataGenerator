package brakeData;

/**
 * Displays the general idea of driving
 * One datapoint is either a brake or a drive time
 * Every datapoint displays a switch of the brakes
 * @author Anton
 *
 */
public class BrakeUsage {
	
	public static boolean ttf = false;
	
	public BrakeUsage(String brakeNumber, int pedalForce, int brakeForce,/* int brakeLiquid,*/
					int speedBefore, int speedAfter, int timestamp, int useageCount, String timeToFailure){
		this.brakeNumber = brakeNumber;
		this.pedalForce = pedalForce;
		this.brakeForce = brakeForce;
//		this.brakeLiquid = brakeLiquid;
		this.speedBefore = speedBefore;
		this.speedAfter = speedAfter;
		this.timestamp = timestamp;
		this.useageCount = useageCount;
		this.timeToFailure = timeToFailure;
		recognizeFailure();	
	}
	
	//Value between 0 and 100 in percent
	public int pedalForce;
	public int brakeForce;
//	public int brakeLiquid = 0;
	
	//in km/h before and after brakeing.
	public int speedBefore;
	public int speedAfter;
	
	//general idea of time progression
	public int timestamp;
	
	//Only counts if the pedalForce >10% or brakeDuration > 1s
	//Maybe a flag with high/low useage and recognize every brake useage
	public int useageCount;
	
	public boolean anomalie;
	public boolean failure = false;
	
	public String brakeNumber;
	public String timeToFailure;
	
	public void recognizeFailure(){
		if(((pedalForce > brakeForce*0.75) & (brakeForce != 100) )){
			anomalie = true;
		}
		else anomalie = false;
		if(brakeForce < pedalForce){
			anomalie = true;
			failure = true;
		}
	}
	
	@Override
	public String toString(){
		if(ttf){
			return brakeNumber + "," + pedalForce + "," + brakeForce + "," + speedBefore + "," + speedAfter
					+ "," + timestamp + "," + useageCount + "," + anomalie + "," + timeToFailure + ","  + failure;
		}
		return brakeNumber + "," + pedalForce + "," + brakeForce + "," + speedBefore + "," + speedAfter
				+ "," + timestamp + "," + useageCount + "," + anomalie;
	}
	
	public static String header(boolean timeToFailure){
		if(timeToFailure){
			ttf = true;
			return "Number,PedalForce,BrakeForce,SpeedBefore,SpeedAfter,Timestamp,useageCount,anomalie,TimeTillFailure,failure\n";
		}
		return "Number,PedalForce,BrakeForce,SpeedBefore,SpeedAfter,Timestamp,useageCount,anomalie\n";
	}
	
}
