package info; // Package declaration

import java.io.File; // Import for File class
import java.io.FileNotFoundException; // Import for FileNotFoundException class
import java.io.FileOutputStream; // Import for FileOutputStream class
import java.io.PrintWriter; // Import for PrintWriter class
import java.util.ArrayList; // Import for ArrayList class
import java.util.Scanner; // Import for Scanner class

public class Summary { // Class declaration
    /**
     * Constructor for Summary class.
     * 
     * @param fileName The name of the file containing weather data.
     */
    private String fileName; // Private variable for file name
    public ArrayList<String> dates; // Public ArrayList for dates
    private ArrayList<Double> max_temps; // Private ArrayList for maximum temperatures
    private ArrayList<Double> min_temps; // Private ArrayList for minimum temperatures
    private ArrayList<Double> windspeeds; // Private ArrayList for windspeeds
    private ArrayList<String> stations; // Private ArrayList for stations
    /**
     * Reads weather data from a file and populates ArrayLists with the data.
     * 
     * @param fileName The name of the file to read.
     */
    public Summary(String fileName) { // Constructor with fileName parameter
        this.fileName = fileName; // Assign fileName parameter to instance variable
        this.dates = new ArrayList<>(); // Initialize dates ArrayList
        this.max_temps = new ArrayList<>(); // Initialize max_temps ArrayList
        this.min_temps = new ArrayList<>(); // Initialize min_temps ArrayList
        this.windspeeds = new ArrayList<>(); // Initialize windspeeds ArrayList
        this.stations = new ArrayList<>(); // Initialize stations ArrayList
    }

    public void getData(String fileName) { // Method to get data from a file
        try (Scanner scan = new Scanner(new File(fileName))) { // Try-with-resources block
            scan.nextLine(); // Skip header line
            scan.nextLine(); // Skip line indicating number of observations
            while (scan.hasNextLine()) { // Loop until no more lines
                String line = scan.nextLine(); // Read next line
                String[] tokens = line.split(","); // Split line into tokens using comma delimiter
                String date = tokens[0]; // Extract date from tokens
                double max_temp = Double.parseDouble(tokens[1]); // Extract max_temp from tokens and convert to double
                double min_temp = Double.parseDouble(tokens[2]); // Extract min_temp from tokens and convert to double
                double windspeed = Double.parseDouble(tokens[3]); // Extract windspeed from tokens and convert to double
                String station = tokens[4]; // Extract station from tokens

                holdData(date, max_temp, min_temp, windspeed, station); // Call holdData method to add data to ArrayLists
            }
        } catch (FileNotFoundException e) { // Catch block for FileNotFoundException
            e.printStackTrace(); // Print stack trace for exception
        }
    }
    /**
     * Adds weather data to the ArrayLists.
     * 
     * @param date     The date of the weather data.
     * @param max_temp The maximum temperature.
     * @param min_temp The minimum temperature.
     * @param windspeed The windspeed.
     * @param station  The weather station name.
     */
    public void holdData(String date, double max_temp, double min_temp, double windspeed, String station) {
        dates.add(date); // Add date to dates ArrayList
        max_temps.add(max_temp); // Add max_temp to max_temps ArrayList
        min_temps.add(min_temp); // Add min_temp to min_temps ArrayList
        windspeeds.add(windspeed); // Add windspeed to windspeeds ArrayList
        stations.add(station); // Add station to stations ArrayList
    }
    /**
     * Writes summary data to a file.
     * 
     * @param filename The name of the file to write the summary data.
     * @throws FileNotFoundException If the file is not found.
     */
    public void writeData(String filename) throws FileNotFoundException { // Method to write summary data to a file
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(filename))) { // Try-with-resources block
            String currentMonth = ""; // Variable to store current month
            String currentStation = ""; // Variable to store current station
            double totalTemp = 0; // Variable to store total temperature
            double totalWindChill = 0; // Variable to store total wind chill
            double totalDays = 0; // Variable to store total days
            String next = ""; // Variable to store next month
            for (int i = 0; i < dates.size(); i++) { // Loop through all data points
                String[] parts = dates.get(i).split("/"); // Split the date string to get month and year
                String current = parts[0]; // Extract current month from date
                String year = parts[2]; // Extract year from date
                currentMonth = current; // Update currentMonth variable
                currentStation = stations.get(i); // Update currentStation variable
                if (i + 1 < dates.size()) { // Check if there is a next data point
                    String[] nextParts = dates.get(i + 1).split("/"); // Split next date string to get next month and year
                    next = nextParts[0]; // Extract next month
                }
                if (!current.equals(next) || i == dates.size() - 1) { // Check if current month is different or it's the last data point for the month
                    // Calculate average temperature and wind speed for the current month and station
                    double avgTemp = totalTemp / totalDays;
                    double avgWindChill = totalWindChill / totalDays;
                    avgTemp = ((avgTemp * 9) / 5) + 32; // Convert average temperature from Celsius to Fahrenheit

                    // Write the summary data for the current month and station to the file
                    pw.printf("%s/%s,%.1f,%.1f,%s\n", currentMonth, year, avgTemp, avgWindChill, currentStation);

                    // Reset variables for the next month and station
                    year = "";
                    currentMonth = "";
                    currentStation = "";
                    totalTemp = 0;
                    totalWindChill = 0;
                    totalDays = 0;

                    if (!current.equals(next)) { // Check if current month is different from next month
                        currentMonth = current; // Update currentMonth variable for next month
                        currentStation = stations.get(i); // Update currentStation variable for next month
                    }
                }
                totalTemp += (max_temps.get(i) + min_temps.get(i)) / 2; // Calculate total temperature for the month
                double aveDailyTemp = (max_temps.get(i) + min_temps.get(i)) / 2; // Calculate average daily temperature
                aveDailyTemp = (((aveDailyTemp * 9) / 5) + 32); // Convert average daily temperature from Celsius to Fahrenheit
                totalWindChill += 35.74 + (0.6215 * aveDailyTemp) - (35.75 * Math.pow(windspeeds.get(i), 0.16)) + 0.4275 * (aveDailyTemp) * (Math.pow(windspeeds.get(i), 0.16)); // Calculate total wind chill for the month
                totalDays++; // Increment total days counter
            }
        }
    }
}
