package telran.view;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

record User(String username, String password,
		LocalDate dateLastLogin, String phoneNumber, int numberOfLogins) {}

class InputOutputTest {
InputOutput io = new SystemInputOutput();
	@Test
	void readObjectTest() {
		User user = io.readObject("Enter user in format <username>#<password>#<dateLastLogin>"
				+ "#<phone number>#<number of logins>", "Wrong user input format", str -> {
					String[] tokens = str.split("#");
					return new User(tokens[0], tokens[1],
							LocalDate.parse(tokens[2]), tokens[3], Integer.parseInt(tokens[4]));
				});
		io.writeLine(user);
	}
	@Test
	void readUserByFields() {
		//TODO create User object from separate fields and display out
		//username at least 6 ASCII letters - first Capital, others Lower case
		//password at least 8 symbols, at least one capital letter,
		//at least one lower case letter, at least one digit, at least one symbol from "#$*&%"
		//phone number - Israel mobile phone
		//dateLastLogin not after current date
		//number of logins any positive number
		String username = io.readStringPredicate("Username at least 6 ASCII letters - first Capital, others Lower case", "Invalid format", str -> str.matches("^[A-Z][a-z]{5,}$"));
		
		String password = io.readStringPredicate("Password at least 8 symbols, at least one capital letter, "
				+ "at least one lower case letter, at least one digit, at least one symbol from \"#$*&%\"", "Invalid format", str -> passwordCheck(str));
		
		LocalDate dateLastLogin = io.readIsoDateRange("dateLastLogin not after current date", "Invalid foramat or date", LocalDate.of(1900, 1, 1), LocalDate.now().plusDays(1));
		
		String phoneNumber = io.readStringPredicate("Israel mobile phone", "Invalid format", 
				str -> str.matches("^(\\+972-?|0)5\\d-?\\d{3}-?\\d{2}-?\\d{2}$"));
		
		int numberOfLogins = (int) io.readNumberRange("number of logins any positive number", "Invalid format or number", 1, Integer.MAX_VALUE).intValue();
		
		io.writeLine(new User(username, password, dateLastLogin, phoneNumber, numberOfLogins));
	}
	 
	private boolean passwordCheck(String password) {
		boolean minLength = true;
		boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;
        String specialChars = "#$*&%";
        
        if (password.length() < 8) minLength = false;
        else {
        	for (char c : password.toCharArray()) {
                if (Character.isUpperCase(c)) {
                    hasUpperCase = true;
                } else if (Character.isLowerCase(c)) {
                    hasLowerCase = true;
                } else if (Character.isDigit(c)) {
                    hasDigit = true;
                } else if (specialChars.indexOf(c) >= 0) {
                    hasSpecialChar = true;
                }
            }
        }
       
		return minLength && hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
	}
}