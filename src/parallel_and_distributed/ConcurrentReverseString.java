package parallel_and_distributed; 

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService; 
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger; 

public class ConcurrentReverseString{
	static final int MAX_T = 4;              

	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException
	{ 
		URL url = new URL("http://txt2html.sourceforge.net/sample.txt");
		InputStream in = url.openStream();

		String str = "This is a test to see if reversing a string concurrently will work ";
		Scanner sc = new Scanner(in);

		while(sc.hasNext()) {
			str+=sc.nextLine();	
		}
		List<String> strList = Arrays.asList(str.split(" "));

		Set<Future<HashMap<Integer,String>>> set = new HashSet<Future<HashMap<Integer,String>>>();
		ExecutorService executor = Executors.newFixedThreadPool(MAX_T);
		AtomicInteger wordCount = new AtomicInteger(0);
		strList.forEach((item) -> {
			Callable<HashMap<Integer, String>> task = new ReverseTask(item,wordCount.getAndIncrement());
			Future<HashMap<Integer, String>> f = executor.submit(task);
			set.add(f);
		});
		executor.shutdown();
		if (executor.awaitTermination(60, TimeUnit.SECONDS)) {
			System.out.println("task completed");
		} else {
			System.out.println("Forcing shutdown...");
			executor.shutdownNow();
		}
		HashMap<Integer,String> sortedList = new HashMap<Integer, String>();
		for(Future<HashMap<Integer, String>> future : set) {
			try {
				HashMap<Integer, String> reversedWords = future.get();
				for(HashMap.Entry<Integer, String> entry : reversedWords.entrySet()) {
					int key = (int) entry.getKey();
					String value = entry.getValue();
					sortedList.put(key, value);
				}	
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		} 
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<sortedList.size();i++) {
			sb.append(sortedList.get(i) + " ");
		}
		System.out.println(sb.toString());
		sc.close();
	}
} 