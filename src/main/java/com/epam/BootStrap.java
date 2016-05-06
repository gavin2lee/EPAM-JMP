package com.epam;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.epam.data.EnrichedRoadAccident;
import com.epam.data.RoadAccident;

public class BootStrap {
	private String dayTimeAccidentsFileName = "DaytimeAccidents.csv";
	private String nightTimeAccidentsFileName = "NighttimeAccidents.csv";
	private Integer batchSize = 100;

	private Integer noOfAccidentsDataEnrichers = 3;
	private Integer noOfAccidentsDataSeparators = 2;

	private BlockingQueue<EnrichedRoadAccident> dayTimeAccidentsQueue = new ArrayBlockingQueue<EnrichedRoadAccident>(
			1000);
	private BlockingQueue<EnrichedRoadAccident> nightTimeAccidentsQueue = new ArrayBlockingQueue<EnrichedRoadAccident>(
			1000);

	private BlockingQueue<List<RoadAccident>> toEnrichQueue = new ArrayBlockingQueue<List<RoadAccident>>(100);
	private BlockingQueue<List<EnrichedRoadAccident>> toSeparateQueue = new ArrayBlockingQueue<List<EnrichedRoadAccident>>(
			100);

	private List<String> cvsSourceFileNames = new ArrayList<String>();

	public BootStrap() {
		init();
	}

	private void init() {
//		cvsSourceFileNames.add("DfTRoadSafety_Accidents_2009.csv");
//		cvsSourceFileNames.add("DfTRoadSafety_Accidents_2010.csv");
//		cvsSourceFileNames.add("DfTRoadSafety_Accidents_2011.csv");
//		cvsSourceFileNames.add("DfTRoadSafety_Accidents_2012.csv");
//		cvsSourceFileNames.add("DfTRoadSafety_Accidents_2013.csv");
		cvsSourceFileNames.add("DfTRoadSafety_Accidents_2014.csv");
	}

	private FutureTask<Long> builderDayTimeAccidentsDataWriter() {
		AccidentsDataWriter dayTimeAccidentsDataWriter = new AccidentsDataWriter(dayTimeAccidentsFileName,
				dayTimeAccidentsQueue);
		return new FutureTask<Long>(dayTimeAccidentsDataWriter);
	}

	private FutureTask<Long> builderNightTimeAccidentsDataWriter() {
		AccidentsDataWriter nightTimeAccidentsDataWriter = new AccidentsDataWriter(nightTimeAccidentsFileName,
				nightTimeAccidentsQueue);
		return new FutureTask<Long>(nightTimeAccidentsDataWriter);
	}

	private List<Callable<Long>> buildAccidentsDataSeparators() {
		List<Callable<Long>> tasks = new ArrayList<Callable<Long>>(noOfAccidentsDataSeparators);
		for (int i = 0; i < noOfAccidentsDataSeparators; i++) {
			tasks.add(new AccidentsDataSeparator(toSeparateQueue, dayTimeAccidentsQueue, nightTimeAccidentsQueue));
		}

		return tasks;
	}

	public void kickoff() {
		// Read
		// Runnable reader = () -> {
		// ExecutorService readerPool = Executors.newFixedThreadPool(3);
		//
		// for(String csvSourceFileName : cvsSourceFileNames){
		// System.out.println("Create reader for file:" + csvSourceFileName);
		// Callable<Long> readerTask = new
		// AccidentsDataReader(csvSourceFileName, toEnrichQueue, batchSize);
		// try {
		// readerPool.submit(readerTask).get();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// };
		//
		// new Thread(reader).start();

		ExecutorService readerPool = Executors.newFixedThreadPool(3, (r) -> {
			Thread t = new Thread(r);
			t.setName("reader");

			return t;
		});

		for (String csvSourceFileName : cvsSourceFileNames) {
			System.out.println("Create reader for file:" + csvSourceFileName);
			Callable<Long> readerTask = new AccidentsDataReader(csvSourceFileName, toEnrichQueue, batchSize);
			try {
				// readerPool.submit(readerTask).get();
				readerPool.submit(readerTask);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Enrich
		// Runnable enricher = () -> {
		// ExecutorService enrichPool = Executors.newFixedThreadPool(10);
		// int i = 0;
		// while (i < noOfAccidentsDataEnrichers) {
		// System.out.println(i);
		// Callable<Long> enricherTask = new
		// AccidentsDataEnricher(toEnrichQueue, toSeparateQueue);
		// try {
		// enrichPool.submit(enricherTask).get();
		// } catch (InterruptedException | ExecutionException e) {
		// e.printStackTrace();
		// }
		// i++;
		// }
		// };
		//
		// new Thread(enricher).start();

		ExecutorService enrichPool = Executors.newFixedThreadPool(noOfAccidentsDataEnrichers, (r)->{
			Thread t = new Thread(r);
			t.setName("enricher");
			
			return t;
		});
		int i = 0;
		while (i < noOfAccidentsDataEnrichers) {
			Callable<Long> enricherTask = new AccidentsDataEnricher(toEnrichQueue, toSeparateQueue);
			enrichPool.submit(enricherTask);
			i++;
		}

		// Separate
		// Runnable separator = () -> {
		// List<Callable<Long>> accidentsDataSeparatorTasks =
		// buildAccidentsDataSeparators();
		// ExecutorService separatorPool = Executors.newFixedThreadPool(10);
		// for (Callable<Long> separateTask : accidentsDataSeparatorTasks) {
		// try {
		// separatorPool.submit(separateTask).get();
		// } catch (InterruptedException | ExecutionException e) {
		// e.printStackTrace();
		// }
		// }
		// };
		//
		// new Thread(separator).start();
		List<Callable<Long>> accidentsDataSeparatorTasks = buildAccidentsDataSeparators();
		ExecutorService separatorPool = Executors.newFixedThreadPool(noOfAccidentsDataSeparators,  (r)->{
			Thread t = new Thread();
			t.setName("separator");
			return t;
		});
		for (Callable<Long> separateTask : accidentsDataSeparatorTasks) {
			separatorPool.submit(separateTask);
		}

		// WriteDayTime
		// Runnable dayTiemWriter = () -> {
		// try {
		// Executors.newSingleThreadExecutor().submit(builderDayTimeAccidentsDataWriter()).get();
		// } catch (InterruptedException | ExecutionException e) {
		// e.printStackTrace();
		// }
		// };
		//
		// new Thread(dayTiemWriter).start();

		Executors.newSingleThreadExecutor((r)->new Thread(r, "dayTimeWriter")).submit(builderDayTimeAccidentsDataWriter());

		// WriteNightTime
		// Runnable nightTimeWriter = () -> {
		// try {
		// Executors.newSingleThreadExecutor().submit(builderNightTimeAccidentsDataWriter()).get();
		// } catch (InterruptedException | ExecutionException e) {
		// e.printStackTrace();
		// }
		// };
		//
		// new Thread(nightTimeWriter).start();
		Executors.newSingleThreadExecutor((r)->new Thread(r, "nightTimeWriter")).submit(builderNightTimeAccidentsDataWriter());

	}
	
	public void kickoff2() {
		
//
//		ExecutorService pool = Executors.newFixedThreadPool(3, (r) -> {
//			Thread t = new Thread(r);
//			t.setName("bootstrap");
//
//			return t;
//		});
		
		ExecutorService pool = Executors.newCachedThreadPool((r) -> {
			Thread t = new Thread(r);
			t.setName("bootstrap");

			return t;
		});

		for (String csvSourceFileName : cvsSourceFileNames) {
			System.out.println("Create reader for file:" + csvSourceFileName);
			Callable<Long> readerTask = new AccidentsDataReader(csvSourceFileName, toEnrichQueue, batchSize);
			try {
				// readerPool.submit(readerTask).get();
				pool.submit(readerTask);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		
		int i = 0;
		while (i < noOfAccidentsDataEnrichers) {
			Callable<Long> enricherTask = new AccidentsDataEnricher(toEnrichQueue, toSeparateQueue);
			pool.submit(enricherTask);
			i++;
		}

		List<Callable<Long>> accidentsDataSeparatorTasks = buildAccidentsDataSeparators();
		
		for (Callable<Long> separateTask : accidentsDataSeparatorTasks) {
			pool.submit(separateTask);
		}


		pool.submit(builderDayTimeAccidentsDataWriter());

		pool.submit(builderNightTimeAccidentsDataWriter());
	}

	public static void main(String[] args) {
//		new BootStrap().kickoff();
		new BootStrap().kickoff2();
	}

}
