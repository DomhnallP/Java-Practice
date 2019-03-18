package parallel_and_distributed; 

import java.util.concurrent.ExecutorService; 
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream; 
  
class Task implements Runnable { 
    private int i;
    private AtomicLong atomicLong;
      
    public Task(int s, AtomicLong a){ 
        i = s; 
        atomicLong = a;
    } 
 
    public void run() { 
    	System.out.println("Adding "  +  i + " to Atomic Integer " + atomicLong.get() + " on " + Thread.currentThread().getName());
        atomicLong.accumulateAndGet(i, (n, m) -> n + m);
        try {
			Thread.sleep(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    } 
} 
public class ThreadPoolTest{
    static final int MAX_T = 16;              
  
    public static void main(String[] args) throws InterruptedException 
    { 
    	AtomicLong atomicLong = new AtomicLong(0);
    	ExecutorService executor = Executors.newFixedThreadPool(MAX_T);
    	
    	IntStream.range(0, 500000).forEach(i -> {
    	        Runnable task = new Task(i+1, atomicLong);
    	        executor.submit(task);
    	    });
    	
    	executor.shutdown();
    	if (executor.awaitTermination(60, TimeUnit.SECONDS)) {
    		  System.out.println("task completed");
    		} else {
    		  System.out.println("Forcing shutdown...");
    		  executor.shutdownNow();
    		}
    	System.out.println(atomicLong.get());  
    }
} 