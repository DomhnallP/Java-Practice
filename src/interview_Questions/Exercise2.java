package interview_Questions;

public class Exercise2 {

	public static void main(String[] args) {
		System.out.println(solution("00-44  48 5555 8361"));
		System.out.println(solution("0 - 22 1985--324"));
		System.out.println(solution("555372654"));
	}
	public static String solution(String S) {
		String cleanedStr =S.replaceAll("[^0-9]","");
		if(cleanedStr.length()%3==2) {
			return cleanedStr.replaceAll("(.{3})", "$1-");
		}
		else if(cleanedStr.length()%3==0) {
			String temp = cleanedStr.replaceAll("(.{3})", "$1-"); 
			return temp.substring(0, temp.length()-1);
		}
		else {
			String part1 = cleanedStr.substring(0, cleanedStr.length()-4);
			String part2 = cleanedStr.substring(cleanedStr.length()-4, cleanedStr.length());
			String temp =  part1.replaceAll("(.{3})", "$1-")+part2.replaceAll("(.{2})", "$1-"); 
			return temp.substring(0, temp.length()-1);
		}

    }

}
