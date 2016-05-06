package com.epam;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.epam.data.EnrichedRoadAccident;
import com.epam.data.EnrichedRoadAccidentBuilder;
import com.epam.data.RoadAccident;
import com.epam.dataservice.PoliceForceService;

public class AccidentsDataEnricher implements Callable<Long> {
	private BlockingQueue<List<RoadAccident>> toEnrichQueue;
	private BlockingQueue<List<EnrichedRoadAccident>> toSeparateQueue;

	private static final PoliceForceService policeForceService;

	static {
		synchronized (AccidentsDataEnricher.class) {
			policeForceService = new PoliceForceService();
		}
	}

	public static final String LOCAL_TIME_FORMAT = "HH:mm:ss:SSS";

	public static final LocalTime ZERO_MIN = LocalTime.parse("00:00:00:000",
			DateTimeFormatter.ofPattern(LOCAL_TIME_FORMAT));
	public static final LocalTime ZERO_MAX = LocalTime.parse("23:59:59:999",
			DateTimeFormatter.ofPattern(LOCAL_TIME_FORMAT));
	public static final LocalTime SIX = LocalTime.parse("06:00:00:000", DateTimeFormatter.ofPattern(LOCAL_TIME_FORMAT));
	public static final LocalTime TWELVE = LocalTime.parse("12:00:00:000",
			DateTimeFormatter.ofPattern(LOCAL_TIME_FORMAT));
	public static final LocalTime EIGHTEEN = LocalTime.parse("18:00:00:000",
			DateTimeFormatter.ofPattern(LOCAL_TIME_FORMAT));

	public AccidentsDataEnricher(BlockingQueue<List<RoadAccident>> toEnrichQueue,
			BlockingQueue<List<EnrichedRoadAccident>> toSeparateQueue) {
		super();
		this.toEnrichQueue = toEnrichQueue;
		this.toSeparateQueue = toSeparateQueue;
		
		System.out.println("create "+AccidentsDataEnricher.class.getName());
	}

	@Override
	public Long call() throws Exception {

		Long count = 0L;

		List<RoadAccident> toEnrichList = null;
		while (((toEnrichList = toEnrichQueue.poll(5, TimeUnit.MINUTES)) != null) && (!toEnrichList.isEmpty())) {
			List<EnrichedRoadAccident> toSeparateList = new ArrayList<EnrichedRoadAccident>(toEnrichList.size());

			for (RoadAccident ra : toEnrichList) {
				String contractNo = policeForceService.getContactNo(ra.getPoliceForce());
				TimeosDay timeosDay = decideTimeosDay(ra);
				EnrichedRoadAccident era = new EnrichedRoadAccidentBuilder(ra).withForceContact(contractNo)
						.withTimeosDay(timeosDay).build();

				toSeparateList.add(era);
				count++;
			}

			toSeparateQueue.put(toSeparateList);
		}
		return count;
	}

	/*
	 * MORNING - 6:0:0 000 am to 12:0:0 000 pm AFTERNOON - 12 pm to 18 EVENING -
	 * 18 pm to 23:59:59 999 am NIGHT - 0 am to 6 am
	 */
	private TimeosDay decideTimeosDay(RoadAccident ra) {

		LocalTime time = ra.getTime();

		if ((time.compareTo(ZERO_MIN) >= 0) && (time.compareTo(SIX) < 0)) {
			return TimeosDay.NIGHT;
		} else if ((time.compareTo(SIX) >= 0) && (time.compareTo(TWELVE) < 0)) {
			return TimeosDay.MORNING;
		} else if ((time.compareTo(TWELVE) >= 0) && (time.compareTo(EIGHTEEN) < 0)) {
			return TimeosDay.AFTERNOON;
		} else if ((time.compareTo(EIGHTEEN) >= 0) && (time.compareTo(ZERO_MAX) <= 0)) {
			return TimeosDay.EVENING;
		} else {
			return null;
		}
	}

}
