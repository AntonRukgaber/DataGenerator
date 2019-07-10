package brakeData;

public enum DrivingLocation {

	
	PEDESTRIANZONE(10),
	SLOWSTREET(30),
	TOWN(50),
	COUNTRY(80),
	HIGHWAY(120),
	UNLIMITED(220)
	;
	
	private int maxSpeed;
	private DrivingLocation(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	public int getMaxSpeed(){
		return maxSpeed;
	}
}
