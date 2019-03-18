package interview_Questions;

public class Excercise1 {

	public static void main(String args[]) {
		int arr[] = {1,4,-1,3,2};
		System.out.println(solution(arr));
	}
	
	public static int solution(int[] A) {
		
		return getCount(A,0);
	}
	
	public static int getCount(int[] A, int node) {
		
		if(A[node]==-1) {
			return 1;			
		}
		else {
			return 1 + getCount(A, A[node]);
		}
	}
}
