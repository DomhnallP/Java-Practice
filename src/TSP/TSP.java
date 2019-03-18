package TSP;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
/*
 * Adapted from https://github.com/stubbedtoe/Travelling-Salesman--a-Genetic-Algorithm/.
 * Go check out his stuff.
 */
public class TSP {

	public static class University {
		int ranking;
		String Name;
		String country;
		double latitude;
		double longtitude;

		public University(int ranking, String Name, String country, double latitude, double longtitude) {
			this.ranking = ranking;
			this.Name = Name;
			this.country = country;
			this.latitude = latitude;
			this.longtitude = longtitude;
		}

		// Gets city's x coordinate
		public double getLat(){
			return this.latitude;
		}

		// Gets city's y coordinate
		public double getLong(){
			return this.longtitude;
		}
		
		public String getName() {
			return this.Name;
		}

		public double distanceTo(double lat2, double long2) {
			int radius=6371;

			double dLat  = Math.toRadians((lat2 - this.latitude));
			double dLong = Math.toRadians((long2 - this.longtitude));

			double lat1 = Math.toRadians(this.latitude);
			lat2   = Math.toRadians(lat2); 

			double a = haversin(dLat) + Math.cos(lat1) * Math.cos(lat2) * haversin(dLong);
			double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

			return radius * c; // <-- d
		}


		public static double haversin(double val) {
			return Math.pow(Math.sin(val / 2), 2);
		}


		@Override
		public String toString(){
			return getName();
		}
	}
	public static class TourManager {

		// Holds our cities
		private final static ArrayList<University> destinationUnis = new ArrayList<University>();

		// Adds a destination city
		public static void addUniversity(University uni) {
			destinationUnis.add(uni);
		}

		// Get a city
		public static University getUniversity(int index){
			return (University)destinationUnis.get(index);
		}

		// Get the number of destination cities
		public static  int numberOfUniversities(){
			return destinationUnis.size();
		}

	}

	public static class Tour{

		// Holds our tour of cities
		private ArrayList<University> tour = new ArrayList<University>();
		// Cache
		private int distance = 0;

		// Constructs a blank tour
		public Tour(){
			for (int i = 0; i < TourManager.numberOfUniversities(); i++) {
				tour.add(null);
			}
		}

		// Constructs a tour from another tour
		@SuppressWarnings("unchecked")
		public Tour(ArrayList<University> tour){
			this.tour = (ArrayList<University>) tour.clone();
		}

		// Returns tour information
		public ArrayList<University> getTour(){
			return tour;
		}

		// Creates a random individual
		public void generateIndividual() {
			// Loop through all our destination cities and add them to our tour
			for (int cityIndex = 0; cityIndex < TourManager.numberOfUniversities(); cityIndex++) {
				setUniversity(cityIndex, TourManager.getUniversity(cityIndex));
			}
			// Randomly reorder the tour
			Collections.shuffle(tour);
		}

		// Gets a city from the tour
		public University getUniversity(int tourPosition) {
			return (University)tour.get(tourPosition);
		}

		// Sets a city in a certain position within a tour
		public void setUniversity(int tourPosition, University city) {
			tour.set(tourPosition, city);
			// If the tours been altered we need to reset the fitness and distance
			distance = 0;
		}

		// Gets the total distance of the tour
		public int getDistance(){
			if (distance == 0) {
				int tourDistance = 0;
				// Loop through our tour's cities
				for (int cityIndex=0; cityIndex < tourSize(); cityIndex++) {
					// Get city we're traveling from
					University fromUniversity = getUniversity(cityIndex);
					// University we're traveling to
					University destinationUniversity;
					// Check we're not on our tour's last city, if we are set our 
					// tour's final destination city to our starting city
					if(cityIndex+1 < tourSize()){
						destinationUniversity = getUniversity(cityIndex+1);
					}
					else{
						destinationUniversity = getUniversity(0);
					}
					// Get the distance between the two cities
					tourDistance += fromUniversity.distanceTo(destinationUniversity.latitude, destinationUniversity.longtitude);
				}
				distance = tourDistance;
			}
			return distance;
		}

		// Get number of cities on our tour
		public int tourSize() {
			return tour.size();
		}

		@Override
		public String toString() {
			String geneString = "";
			for (int i = 0; i < tourSize(); i++) {
				geneString += "|--- " + getUniversity(i)+" ---|\n";
			}
			return geneString;
		}
	}

	public static double acceptanceProbability(int energy, int newEnergy, double temperature) {
		// If the new solution is better, accept it
		if (newEnergy < energy) {
			return 1.0;
		}
		// If the new solution is worse, calculate an acceptance probability
		return Math.exp((energy - newEnergy) / temperature);
	}

	public static ArrayList<University> getUniArray(String fileLocation) throws IOException {

		BufferedReader TSVFile = new BufferedReader(new InputStreamReader(new FileInputStream(fileLocation), "UTF-16"));
		String data = TSVFile.readLine();
		ArrayList<University> list = new ArrayList<University>();
		//FileIO reader = new FileIO();
		while(data!=null) {
			String[] ar1 = data.split("\t");
			list.add(new University(Integer.parseInt(ar1[0]), ar1[1], ar1[2] , Double.parseDouble(ar1[3]), Double.parseDouble( ar1[4])));
			data = TSVFile.readLine();
			if(data.isEmpty()) {
				break;
			}
		}

		TSVFile.close();

		return list;

	}
	
	public static void main(String[] args) throws IOException {
		// Create and add our cities

		ArrayList<University> uniList = getUniArray("src/TSP/UniLocationsAndRank.tsv");
		
		for(University uni : uniList) {
			TourManager.addUniversity(uni);
		}

		// Set initial temp
		double temp = 10000;

		// Cooling rate
		double coolingRate = 0.003;

		// Initialize intial solution
		Tour currentSolution = new Tour();
		currentSolution.generateIndividual();

		System.out.println("Initial solution distance: " + currentSolution.getDistance());

		// Set as current best
		Tour best = new Tour(currentSolution.getTour());

		// Loop until system has cooled
		while (temp > 1) {
			// Create new neighbour tour
			Tour newSolution = new Tour(currentSolution.getTour());

			// Get a random positions in the tour
			int tourPos1 = (int) (newSolution.tourSize() * Math.random());
			int tourPos2 = (int) (newSolution.tourSize() * Math.random());

			// Get the cities at selected positions in the tour
			University citySwap1 = newSolution.getUniversity(tourPos1);
			University citySwap2 = newSolution.getUniversity(tourPos2);

			// Swap them
			newSolution.setUniversity(tourPos2, citySwap1);
			newSolution.setUniversity(tourPos1, citySwap2);

			// Get energy of solutions
			int currentEnergy = currentSolution.getDistance();
			int neighbourEnergy = newSolution.getDistance();

			// Decide if we should accept the neighbour
			if (acceptanceProbability(currentEnergy, neighbourEnergy, temp) > Math.random()) {
				currentSolution = new Tour(newSolution.getTour());
			}

			// Keep track of the best solution found
			if (currentSolution.getDistance() < best.getDistance()) {
				best = new Tour(currentSolution.getTour());
			}

			// Cool system
			temp *= 1-coolingRate;
		}

		System.out.println("Final solution distance: " + best.getDistance());
		System.out.println("Tour: " + best);
	}

}




