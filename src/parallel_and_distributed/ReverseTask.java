package parallel_and_distributed;

import java.util.HashMap;
import java.util.concurrent.Callable;

class ReverseTask implements Callable<HashMap<Integer, String>> { 
	private String s;
	private int i;

	public ReverseTask(String str, int index){  
		s = str;
		i = index;
	}

	public HashMap<Integer, String> call() throws Exception {
		System.out.println(Thread.currentThread().getName() + " is now reversing \'" + s +"\'");
		Thread.sleep(100);
		StringBuilder sb = new StringBuilder(s);
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(i, sb.reverse().toString());
		return map;
	} 
} 