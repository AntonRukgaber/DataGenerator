package brakeData;

public enum BrakeForce {
	
	PRESS_NONE(0),
//	PRESS_GRIND(1),
	PRESS_SLOWSTOP(5),
	PRESS_SOME(10),
	PRESS_NORMAL(20),
	PRESS_MODERATE(30),
	PRESS_STRONG(40),
	PRESS_STRONGER(50),
	PRESS_VERYSTRONG(60),
	PRESS_SUDDEN(70),
	PRESS_PROBLEM(80),
	EMERGENCYBRAKE(100);
	
	private int strength;
	
	private BrakeForce(int str){
		strength = str;
	}
	
	public int getStrength(){
		return strength;
	}
	

}

