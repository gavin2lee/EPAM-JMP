package com.epam;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.epam.data.EnrichedRoadAccident;

public class AccidentsDataWriter implements Callable<Long> {
	private String targetFileName;
	private BlockingQueue<EnrichedRoadAccident> toWriteQueue;

	private int waitTime = 5;
	
	private Object [] headers = new Object[]{
			"Accident_Index",
			"Longitude",
			"Latitude",
			"Police_Force",
			"Accident_Severity",
			"Number_of_Vehicles",
			"Number_of_Casualties",
			"Date",
			"Day_of_Week",
			"Time",
			"Local_Authority_(District)",
			"Light_Conditions",
			"Weather_Conditions",
			"Road_Surface_Conditions",
			"ForceContact",
			"TimeosDay"
			};

	public AccidentsDataWriter(String targetFileName, BlockingQueue<EnrichedRoadAccident> inputQueue) {
		this.targetFileName = targetFileName;
		this.toWriteQueue = inputQueue;
		
		
		System.out.println("create "+AccidentsDataWriter.class.getName());
	}
	
	private void writeHeader() throws IOException{
		File targetFile = new File(targetFileName);
		if(!targetFile.exists()){
			targetFile.createNewFile();
			Writer writer = new FileWriter(targetFile);
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withRecordSeparator("\n"));
			csvPrinter.printRecord(headers);
			csvPrinter.flush();
			csvPrinter.close();
			writer.close();
			System.out.println("Write Headers");
		}
		
		
	}

	@Override
	public Long call() throws Exception {
		//writeHeader();
		
		Long count = 0L;
		Writer writer = new FileWriter(targetFileName);
		CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withRecordSeparator("\n"));
		
		csvPrinter.printRecord(headers);
		csvPrinter.flush();
		writer.flush();
		
		EnrichedRoadAccident era = null;
		while ((era = toWriteQueue.poll(waitTime, TimeUnit.MINUTES)) != null) {
			csvPrinter.printRecord(assembleRecord(era));
			csvPrinter.flush();
			writer.flush();
			count++;
		}

		csvPrinter.close();
		writer.close();
		return count;
	}
	
	/*
	 * "Accident_Index",
			"Longitude",
			"Latitude",
			"Police_Force",
			"Accident_Severity",
			"Number_of_Vehicles",
			"Number_of_Casualties",
			"Date",
			"Day_of_Week",
			"Time",
			"Local_Authority_(District)",
			"Light_Conditions",
			"Weather_Conditions",
			"Road_Surface_Conditions",
			"ForceContact",
			"TimeosDay"
	 */
	private List<String> assembleRecord(EnrichedRoadAccident a){
		List<String> r = new ArrayList<String>();
		r.add(a.getAccidentId());
		r.add(String.valueOf(a.getLongitude()));
		r.add(String.valueOf(a.getLatitude()));
		r.add(a.getPoliceForce());
		r.add(a.getAccidentSeverity());
		
		r.add(String.valueOf(a.getNumberOfVehicles()));
		r.add(String.valueOf(a.getNumberOfCasualties()));
		r.add(a.getDate().toString());
		r.add(a.getDayOfWeek().name());
		r.add(a.getTime().toString());
		
		r.add(a.getDistrictAuthority());
		r.add(a.getLightConditions());
		r.add(a.getWeatherConditions());
		r.add(a.getRoadSurfaceConditions());
		
		r.add(a.getForceContact());
		r.add(a.getTimeosDay().name());
		
		System.out.println(r);
		
		return r;
	}

}
