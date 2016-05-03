Hi All,

Wanted add few points and some sample usage which can help you on this task. For these changes please check at the latest "https://github.com/bagedan/EPAM-JMP/tree/master "

I have added a sample Reader which can read accident data from files in batches. Also have created one sample test class which uses these reader classes concurrently and passes to some consumers. These code can be found in "src/test/java/com/epam/dataservice" and "src/main/java/com/epam". Please check this which will help you for this task.I have also added the PoliceForceService​ which can be used to get force contact no.


For little more clarification -- the task is to read accident data from files and then enrich the data by adding 2 more fields ForceContact, TimeOfDay as mentioned in previous email. Then write this RoadAccidents data into file. While writing, just write into 2 different files. One will contain MORNING and AFTERNOON data and other should contain NIGHT and EVENING data.

We have basically 3 type of tasks reading from file, enriching the records with additional 2 fields, writing the records into file which uses I/O operation and can be run concurrently. The test i have provided showcases how run similar 2 tasks in parallel. You can just refer to it for solution approach or solution.

​Please let me or your mentors know if you face any problem or have any query.

Hi All,

I am extremely sorry to publish the task this much late but couldn't do better with ​current project work pressure unfortunately. So the task is described below. Please read through it and let me know if you have any query regarding it.

Read accident files for different years and then we need to populate following 2 fields for each record.
1. ForceContact -- Use PoliceForceService.getContactNo(String forceName) to get contact no. 
Assume you dont have control on this method and this method can be time consuming or may even block randomly.
Assume this can be a I/O service for example some web service

2. TimeosDay -- Add 1 field DayTime which can have 4 different values as below based on accident time.
MORNING - 6 am to 12 pm
AFTERNOON - 12 pm to 6 pm
EVENING - 6 pm to 12 am
NIGHT - 12 am to 6 am

And then we need to generate 2 files 
1. File which contains all day time accdent data for the years (MORNING, AFTERNOON)  -- DaytimeAccidents.csv
2. File which contains all night time accdent data for the years (EVENING, NIGHT)    -- NighttimeAccidents.csv


Use multithreading to read multiple files or process them or writing them to the files. You can use as many threads as you want
to implement these tasks. But make sure that we can follow below memory constraints. As file read, record processing, file write
contain I/O operations, they can be executed conccurrently to get benifit of multithreding. But as the processing time for each
tasks may vary (for example write is faster than processing or reading or such combinations) the program may end up storing too
many results from one task in memory and waiting for other task.

1. The program should not be written such that it always requires huge amount of memory. If it has limited memory for example 256 mb it still
   should be able to run smoothly. It will take more time but it should not throw Out of Memory error.

2. The program should improve performance if available memory is increased.

3. Basically there should be some kind of control parameter based on which program can change behavior to meet above 2 criteria.

4. The program should be able to support any no of input files without any out of memory issue. It can be 5, 10, 100 or even 1000.

5. Also notice that the current CsvParser does not read the the whole file into memory. But it uses iterative approach. 
   So we can control how many records we want to load at a time in memory.

6. Handle scenarios if one thread executing some task get stuck or throw error. For example any thread running PoliceForceService.getContactNo(String forceName)
   might get stuck indefinietly or may throw error. There should be code to handle this and recover.


 
 For multithreaded code it is very important to design in good concurrent way. One can use all nice features of Java concurrent classes  but if the program
 is not written with a good concuurent design it may perform badly or it may even break. And all this happen very abruptly, unexpectedly. So please give it 
 a good amount of thinking around how to use multiple threads and how many threads can be used or how to control on memory.

I think its better to discuss about the approach before writing the actual code. So you can reach your mentors or me if you have any queries.​


Finally to get additional accident data files from Bogdan's repository you need to run these commands in your git bash: 

git fetch bogdan
git cherry-pick 50635b

Or download them directly from here https://github.com/bagedan/EPAM-JMP/tree/master/src/main/resources​


Thanks,
Tanmoy