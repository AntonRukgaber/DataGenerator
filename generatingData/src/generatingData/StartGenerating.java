package generatingData;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import brakeData.BrakeDataGenerator;
import brakeData.BrakeUsage;

public class StartGenerating {

	public static void main(String[] args) {
		
		//TODO create UI for more easy way to configure created data 
		boolean ttf = true;
		boolean storytime = false;
		
		Path path = Paths.get(storytime? "./story.csv" : "./data.csv");
		BufferedWriter writer;
		try {
			writer = Files.newBufferedWriter(path);
			String header = BrakeUsage.header(ttf);
			writer.write(header);
			for(int i=0; i<1; i++){
				BrakeDataGenerator myDataOrigin = new BrakeDataGenerator();
				ArrayList<BrakeUsage> myWriteList;
				if(storytime){
					i=100;
					myWriteList = myDataOrigin.generateStory(ttf);
				}
				else{
					myWriteList = myDataOrigin.generateRandomDataForOneBrake(i,ttf);
				}
				System.out.println("I reach the "+ i + " part with entries: " + myWriteList.size());
				writeDataToFile(myWriteList, writer);
			}
			writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		System.out.println("done");

	}

	private static void writeDataToFile(ArrayList<BrakeUsage> allData, BufferedWriter writer){
		try {		
			for (BrakeUsage use : allData) {
				writer.append(use.toString() + "\n");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}
