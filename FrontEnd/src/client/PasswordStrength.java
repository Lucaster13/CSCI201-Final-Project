package client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class PasswordStrength {
	private static Vector<String> commonPasswords;
	
	public PasswordStrength(String filename) {
		commonPasswords = new Vector<String>();
		BufferedReader reader = null;
		try {
		    reader = new BufferedReader(new FileReader(filename));
		    String line = reader.readLine();
		    while(line != null) {
		        String[] wordsLine = line.split(" ");
		        for(String word : wordsLine) {
		            commonPasswords.add(word);
		        }
		        line = reader.readLine();
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	/*
	 * Takes password and gives it a score (GOOD SECURITY: trim password before passing to function)
	 * Determine which strength meter to show based on scores
	 * 
	 * RETURN VALUES:
	 * 
	 * (FULL REJECTS)
	 * -1 = inccorect size
	 * -2 = common password
	 * -3 = contains spaces
	 * -4 = has 3 sequential characters 
	 * 
	 * (ACCEPTABLE PASSWORDS)
	 * +1 meets size constraint
	 * +1 for special character
	 * +1 for having 1 upper
	 * +1 for having 1 lower
	 * +1 for having 1 number
	 * 
	 * total = 5
	 */
	public int check(String password) {
		int score = 0;
		
		//check length
		int length = password.length();
		if(length < 8 || length > 64) {
			return -1;
		}else {
			score++;
		}
		
		//check against common passwords
		for (String common : commonPasswords) {
			if (password.equalsIgnoreCase(common)) {
				return -2;
			}
		}
		
		//check characters
		int numNumbers = 0;
		int numUpper = 0;
		int numLower = 0;
		int numSpecial = 0;
		int numSpaces = 0;
		int sequentialCount = 1;
		char prevChar = '\0';
		for(char character : password.toCharArray()) {
			
			//check for sequential characters
			if(prevChar == character) {
				sequentialCount++;
			}else {
				sequentialCount = 1;
			}
			
			if(sequentialCount == 3) {
				return -4;
			}
			
			if(Character.isWhitespace(character)) {
				numSpaces++;
			}else if(Character.isDigit(character)) {
				numNumbers++;
			}else if(Character.isUpperCase(character)) {
				numUpper++;
			}else if(Character.isLowerCase(character)) {
				numLower++;
			}else {
				numSpecial++;
			}
			
			prevChar = character;
		}
		
		if(numSpaces > 0) {
			return -3;
		}
		
		if(numNumbers > 0) {
			score++;
		}
		
		if(numUpper > 0) {
			score++;
		}
		
		if(numLower > 0) {
			score++;
		}
		
		if(numSpecial > 0) {
			score++;
		}
		
		return score;
	}

}
