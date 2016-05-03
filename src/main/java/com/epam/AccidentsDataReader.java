package com.epam;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.epam.data.RoadAccident;
import com.epam.dataservice.RoadAccidentParser;

public class AccidentsDataReader implements Callable<Long> {
	private String dataFileName;
	private BlockingQueue<List<RoadAccident>> toEnrichQueue;
	private Integer batchSize;

	private RoadAccidentParser roadAccidentParser = new RoadAccidentParser();

	public AccidentsDataReader(String dataFileName, BlockingQueue<List<RoadAccident>> outputQueue, Integer batchSize) {
		super();
		this.dataFileName = dataFileName;
		this.toEnrichQueue = outputQueue;
		this.batchSize = batchSize;
		
		System.out.println("AccidentsDataReader:"+ dataFileName);
	}

	private List<RoadAccident> getNextBatch(Iterator<CSVRecord> recordIterator) {
		List<RoadAccident> roadAccidentBatch = new ArrayList<RoadAccident>();
		int recordCount = 0;
		RoadAccident roadAccidentItem = null;
		while (recordCount < batchSize && recordIterator.hasNext()) {
			roadAccidentItem = roadAccidentParser.parseRecord(recordIterator.next());
			if (roadAccidentItem != null) {
				roadAccidentBatch.add(roadAccidentItem);
				recordCount++;
			}
		}
		return roadAccidentBatch;
	}

	@Override
	public Long call() throws Exception {
		Long readSize = 0L;
		String fullName = AccidentsDataReader.class.getClassLoader().getResource(dataFileName).toString();
		fullName = fullName.substring(fullName.indexOf("/"));
		Reader reader = new FileReader(
				new File(fullName));
		CSVParser csvParser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
		Iterator<CSVRecord> recordIterator = csvParser.iterator();
		boolean isDataLoadFinished = false;
		while (!isDataLoadFinished) {
			List<RoadAccident> roadAccidents = getNextBatch(recordIterator);
			toEnrichQueue.put(roadAccidents);
			readSize += roadAccidents.size();

			if (roadAccidents.isEmpty()) {
				isDataLoadFinished = true;
			}
		}
		
		csvParser.close();
		reader.close();

		return readSize;
	}

}
