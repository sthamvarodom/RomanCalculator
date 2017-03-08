import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

public class RomanCalculator {

    private static int result = 0;

    public static void main(String[] args) {

        String newline = System.lineSeparator(); //"\n\r";

        System.out.print("Welcome to Roman Calculator. Please type file location and press enter." + newline + "> ");

        try {

            // Get path
            BufferedReader inputBuffer = new BufferedReader(new InputStreamReader(System.in));
            String path = inputBuffer.readLine();
            System.out.println("");

            // Read file
            System.out.println(newline + "Processing..." + newline);
            StringBuilder input = new StringBuilder("");
            StringBuilder output = new StringBuilder("");
            String validChar = new String("MDCLXVI+ ");
            int product = 0;
            BufferedReader br = null;
            try {

                br = new BufferedReader(new FileReader(path));

                String line = null;
                int lineCount = 1;

                do {
                    line = br.readLine();

                    // Validate character
                    int i = 0;
                    while (line != null && i < line.length()) {
                        //System.out.print(String.valueOf(line.charAt(i)).toUpperCase());
                        if (validChar.indexOf(String.valueOf(line.charAt(i)).toUpperCase().charAt(0)) < 0) {
                            throw new IOException("Invalid character at line: " + String.valueOf(lineCount));
                        }

                        i++;
                    }
                    //System.out.println();

                    if (line != null) {
                        line = line.trim().replace(" ", "").toUpperCase();
                        if (!line.equals("")) {
                            // start with number and end with number
                            // separate with only one plus(+) sign

                            product = 0;

                            String[] list = line.split("[+]");
                            for (int j = 0; j < list.length; j++) {

                                if (list[j].equals("")) {
                                    // Validate operator
                                    throw new Exception("Invalid expression at line: " + String.valueOf(lineCount));
                                } else {
                                    // Validate number
                                    result = 0;
                                    if (getProduct(list[j], lineCount)) {
                                        product += result;
                                    } else {
                                        throw new Exception("Invalid expression at line: " + String.valueOf(lineCount));
                                    }
                                }
                            }

                            input.append(line + newline);
                            output.append(toRoman(product) + newline);
                            System.out.println(line + " = " + String.valueOf(product) + " = " + toRoman(product));
                        }
                    }

                    lineCount++;

                } while (line != null);

                System.out.println(newline + ":: Input ::" + newline + input.toString() + ":: end of file ::");
                System.out.println(newline + ":: Output ::" + newline + output.toString());

            } catch (FileNotFoundException e) {
                System.out.println(newline + "Calculator error: File not found.");
            } catch (IOException e) {
                System.out.println(newline + "Calculator error: " + e.getMessage());
            } catch (Exception e) {
                throw e;
            } finally {
                if (br != null) {
                    br.close();
                    System.gc();
                }
            }
			
			 System.out.println(newline + "Bye bye.");
			System.in.read();

        } catch (Exception e) {
            System.out.println(newline + "Calculator error: " + e.getMessage());
        }
    }

    private static int toNumber(char roman) {
        char[] ro = new char[]{'I', 'V', 'X', 'L', 'C', 'D', 'M'};
        char[] nu = new char[]{1, 5, 10, 50, 100, 500, 1000};
        for (int i = 0; i < 7; i++) {
            if (roman == ro[i]) {
                return nu[i];
            }
        }

        return 0;
    }

    private static String toRoman(int number) {
        String result = "";

        String[] ro = new String[]{"I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M"};
        int[] no = new int[]{1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000};

        for (int i = 12; i >= 0; ) {
            if (no[i] <= number) {
                result += ro[i];
                number -= no[i];
            } else {
                i--;
            }
        }

        return result;
    }

    private static boolean isValidSequence(String roman) {

        String[] pattern = new String[]{"IV", "IX", "XL", "XC", "CD", "CM"};

        for (int i = 0; i < pattern.length; i++) {
            if (pattern[i].equals(roman)) {
                return true;
            }
        }

        return false;
    }

    private static boolean getProduct(String roman, int lineCount) throws Exception {
        for (int i = 0; i < roman.length(); i++) {
            char r1 = roman.charAt(i);
            int n1 = toNumber(r1);
            char r2 = 0;
            int n2 = -1;

            boolean subtract = false;
            if (i < roman.length() - 1) {
                r2 = roman.charAt(i + 1);
                n2 = toNumber(r2);
                if (n1 < n2) {
                    if (isValidSequence(String.valueOf(r1) + String.valueOf(r2))) {
                        subtract = true;
                    } else {
                        throw new Exception("Invalid sequence at line: " + String.valueOf(lineCount));
                    }
                }
            }

            // invalid Roman character
            if (n1 == 0 || n2 == 0) {
                return false;
            }

            if (!subtract) {
                result += n1;
            } else {
                result -= n1;
            }
        }

        // check min/max
        if (result < 1 || result > 3999) {
            throw new Exception("Value out of bound [1-3999] at line: " + String.valueOf(lineCount));
        }

        return true;
    }
}
