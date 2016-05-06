package com.epam;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.epam.data.EnrichedRoadAccident;

public class AccidentsDataSeparator implements Callable<Long> {
	private BlockingQueue<List<EnrichedRoadAccident>> toSeparateQueue;
	private BlockingQueue<EnrichedRoadAccident> dayTimeAccidentsQueue;
	private BlockingQueue<EnrichedRoadAccident> nightTimeAccidentsQueue;

	public AccidentsDataSeparator(BlockingQueue<List<EnrichedRoadAccident>> inputQueue,
			BlockingQueue<EnrichedRoadAccident> dayTimeAccidentsQueue,
			BlockingQueue<EnrichedRoadAccident> nightTimeAccidentsQueue) {
		super();
		this.toSeparateQueue = inputQueue;
		this.dayTimeAccidentsQueue = dayTimeAccidentsQueue;
		this.nightTimeAccidentsQueue = nightTimeAccidentsQueue;
		
		System.out.println("create "+ AccidentsDataSeparator.class.getName());
	}

	@Override
	public Long call() throws Exception {
		Long count = 0L;

		List<EnrichedRoadAccident> toSeparateList = null;

		while ((toSeparateList = toSeparateQueue.poll(5, TimeUnit.MINUTES)) != null) {
			for (EnrichedRoadAccident era : toSeparateList) {
				if(isDayTime(era)){
					dayTimeAccidentsQueue.put(era);
				}else if(isNightTime(era)){
					nightTimeAccidentsQueue.put(era);
				}else{
					throw new RuntimeException("Wrong TimeosDay");
				}
				
				count++;
			}
		}

		return count;
	}

	private boolean isDayTime(EnrichedRoadAccident e) {
		return ((e.getTimeosDay() == TimeosDay.MORNING) 
				|| (e.getTimeosDay() == TimeosDay.AFTERNOON));
	}
	
	private boolean isNightTime(EnrichedRoadAccident e){
		return ((e.getTimeosDay() == TimeosDay.EVENING) 
				|| (e.getTimeosDay() == TimeosDay.NIGHT));
	}

}
