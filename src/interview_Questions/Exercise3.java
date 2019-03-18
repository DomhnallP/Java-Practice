package interview_Questions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Exercise3  {
	public static int solution(Integer[] a, int k, int l){
		if(a.length<k+l)
		{
			return -1;
		}
		int totalApples = 0;
		
		//get the highest number of apples bob can collect, anywhere in the orchard
		int[] maxAndStartingIndex = getMaxFromArray(a, k);
		totalApples+=maxAndStartingIndex[0];
		
		//create new arrays from the remaining available trees, by removing the trees that bob is picking
		
		//get arrays of trees before bobs trees
		Integer[] leading = Arrays.copyOfRange(a, 0, maxAndStartingIndex[1]);
		//get array of trees after bobs trees
		Integer[] trailing = Arrays.copyOfRange(a, (maxAndStartingIndex[1]+k), a.length);
		//merge the 2 arrays 
		List<Integer> mergedAsList = new ArrayList<Integer>(Arrays.asList(leading));
		mergedAsList.addAll(Arrays.asList(trailing));
		Integer[] mergedArrays =  mergedAsList.toArray(new Integer[0]);
		
		//get the max apples that alice can pick
		maxAndStartingIndex = getMaxFromArray(mergedArrays, l);
		totalApples+=maxAndStartingIndex[0];
		
		//return the answer
		return totalApples;
	}
	static int[] getMaxFromArray(Integer[] a, int num){
		int sum = 0;
		int sumPrevious = 0;
		int startingIndex = 0;
		/*using this array to pass 2 separate values back from this function,
		 * the max number of apples that can be collected by that person,
		 * and the index of the tree that they start picking
		 */
		int[] returnArgs = new int[2];
		for(int i = 0; i < a.length; i++){
			if(i <= a.length - num) {
				for (int j = i; j < i + num; j++) {
					sum += a[j];
				}
			}
			if(sum >  sumPrevious) {
				sumPrevious = sum;
				startingIndex = i;
			}
			sum = 0;
		}
		returnArgs[0] = sumPrevious;
		returnArgs[1] = startingIndex;
		return returnArgs;
	}
	public static void main(String[] args) {
		Integer a[] = {10,19,15};
		System.out.println(solution(a, 2,2));
		Integer b[] = {6, 1, 3, 4, 5, 6, 8, 2, 7};
        System.out.println(solution(b, 3,2));

	}
}
