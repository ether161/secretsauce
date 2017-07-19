import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.io.BufferedWriter;

public class CSVReader {

	private static final char DEFAULT_SEPARATOR = ',';
	private static final char DEFAULT_QUOTE = '"';
	private static final String inputFile  = "C:\\Users\\marazzj\\Desktop\\sample_names.csv";
	private static final String outputFile = "C:\\Users\\marazzj\\Desktop\\test.csv";

	public static void main(String[] args) throws Exception {
		String finalOutput = "";
		Scanner scanner = new Scanner(new File(inputFile));
		while (scanner.hasNext()) {
			List<String> line = parseLine(scanner.nextLine());
			String localCase = line.get(0);

			if (localCase.contains(",")) {
				int a = localCase.indexOf(",");
				// System.out.println(a);
				String first = localCase.substring(a);
				String last = localCase.substring(0, a);
				localCase = first + " " + last;
				localCase = localCase.substring(2);
			}

			else {
				localCase = line.get(0);
			}
			;
			finalOutput = finalOutput + localCase + "" + "\n";
			writeToFile(finalOutput);
		}
		scanner.close();

        String csvFile = outputFile;

        Scanner scanner1 = new Scanner(new File(csvFile));
        while (scanner1.hasNext()) {
            List<String> line = parseLine(scanner1.nextLine());
            
            String name = line.get(0);           
            int start = name.indexOf(' ');
            int end = name.lastIndexOf(' ');

            String firstName = "";
            String middleName = "";
            String lastName = "";

            if (start >= 0) {
                firstName = name.substring(0, start);
                if (end > start)
                    middleName = name.substring(start + 1, end);
                lastName = name.substring(end + 1, name.length());
            }    
            System.out.println("Full Name: "+ line.get(0));
            System.out.println("First: " + firstName);
            System.out.println("Middle: " + middleName);
            System.out.println("Last: " + lastName);
            
            //System.out.println(line.get(0));
    
        }
        scanner.close();

	}

	public static void writeToFile(String line) {
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			fw = new FileWriter(outputFile);
			bw = new BufferedWriter(fw);
			bw.write(line);

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}

	}

	public static List<String> parseLine(String cvsLine) {
		return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
	}

	public static List<String> parseLine(String cvsLine, char separators) {
		return parseLine(cvsLine, separators, DEFAULT_QUOTE);
	}

	public static List<String> parseLine(String cvsLine, char separators,
			char customQuote) {

		List<String> result = new ArrayList<>();

		// if empty, return!
		if (cvsLine == null && cvsLine.isEmpty()) {
			return result;
		}

		if (customQuote == ' ') {
			customQuote = DEFAULT_QUOTE;
		}

		if (separators == ' ') {
			separators = DEFAULT_SEPARATOR;
		}

		StringBuffer curVal = new StringBuffer();
		boolean inQuotes = false;
		boolean startCollectChar = false;
		boolean doubleQuotesInColumn = false;

		char[] chars = cvsLine.toCharArray();

		for (char ch : chars) {

			if (inQuotes) {
				startCollectChar = true;
				if (ch == customQuote) {
					inQuotes = false;
					doubleQuotesInColumn = false;
				} else {

					// Fixed : allow "" in custom quote enclosed
					if (ch == '\"') {
						if (!doubleQuotesInColumn) {
							curVal.append(ch);
							doubleQuotesInColumn = true;
						}
					} else {
						curVal.append(ch);
					}

				}
			} else {
				if (ch == customQuote) {

					inQuotes = true;

					// Fixed : allow "" in empty quote enclosed
					if (chars[0] != '"' && customQuote == '\"') {
						curVal.append('"');
					}

					// double quotes in column will hit this!
					if (startCollectChar) {
						curVal.append('"');
					}

				} else if (ch == separators) {

					result.add(curVal.toString());

					curVal = new StringBuffer();
					startCollectChar = false;

				} else if (ch == '\r') {
					// ignore LF characters
					continue;
				} else if (ch == '\n') {
					// the end, break!
					break;
				} else {
					curVal.append(ch);
				}
			}

		}

		result.add(curVal.toString());

		return result;
	}

}
