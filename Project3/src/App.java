import info.Summary;

public class App {
    public static void main(String[] args) throws Exception {
        String inputFile = "weather.csv"; // Default input file name
        String outputFile = "summary.csv"; // Default output file name

        if (args.length >= 1) {
            inputFile = args[0]; // Use provided input file name or URL
        }
        if (args.length >= 2) {
            outputFile = args[1]; // Use provided output file name
        }

        try {
            Summary sum = new Summary(inputFile); // Initialize Summary object with input file name
            sum.getData(inputFile); // Get data from input file
            sum.writeData(outputFile); // Write summary data to output file
            System.out.println("Summary data written to " + outputFile);
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for any exceptions
        }
    }
}
