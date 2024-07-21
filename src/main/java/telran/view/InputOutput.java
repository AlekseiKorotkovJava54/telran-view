package telran.view;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Predicate;

public interface InputOutput {
	String readString(String prompt);

	void writeString(String str);

	default void writeLine(Object obj) {
		writeString(obj.toString() + "\n");
	}

	default <T> T readObject(String prompt, String errorPrompt, Function<String, T> mapper) {
		T res = null;
		boolean running = false;
		do {
			String str = readString(prompt);
			running = false;
			try {
				res = mapper.apply(str);
			} catch (RuntimeException e) {
				writeLine(errorPrompt + " " + e.getMessage());
				running = true;
			}

		} while (running);
		return res;
	}

	/**
	 * 
	 * @param prompt
	 * @param errorPrompt
	 * @return Integer number
	 */
	default Integer readInt(String prompt, String errorPrompt) {
		// TODO
		// Entered string must be a number otherwise, errorPrompt with cycle
		return readObject(prompt, errorPrompt, str -> Integer.parseInt(str));

	}

	default Long readLong(String prompt, String errorPrompt) {
		// TODO
		// Entered string must be a number otherwise, errorPrompt with cycle
		return readObject(prompt, errorPrompt, str -> Long.parseLong(str));

	}

	default Double readDouble(String prompt, String errorPrompt) {
		// TODO
		// Entered string must be a number otherwise, errorPrompt with cycle
		return readObject(prompt, errorPrompt, str -> Double.parseDouble(str));

	}

	default Double readNumberRange(String prompt, String errorPrompt, double min, double max) {
		// TODO
		// Entered string must be a number in range (min <= number < max) otherwise,
		// errorPrompt with cycle
		return readObject(prompt, errorPrompt, str -> {
			double numberRange = Double.parseDouble(str);
			if(numberRange < min || numberRange > max) throw new IllegalArgumentException("the number is out of range");
			return numberRange;
		});
	}
	
	default String readStringPredicate(String prompt, String errorPrompt,
			Predicate<String> predicate) {
		//TODO
		//Entered String must match a given predicate
		return readObject(prompt, errorPrompt, str -> {
			if(!predicate.test(str)) throw new IllegalArgumentException("the string does not match the format");
			return str;
		});
	}
	
	default String readStringOptions(String prompt, String errorPrompt,
			HashSet<String> options) {
		//TODO
		//Entered String must be one out of a given options
		return readStringPredicate(prompt, errorPrompt, str -> options.contains(str));
	}
	
	default LocalDate readIsoDate(String prompt, String errorPrompt) {
		//TODO
		//Entered String must be a LocalDate in format (yyyy-mm-dd)
		return readObject(prompt, errorPrompt, str -> LocalDate.parse(str));
	}
	
	default LocalDate readIsoDateRange(String prompt, String errorPrompt, LocalDate from,
			LocalDate to) {
		//Entered String must be a LocalDate in format (yyyy-mm-dd) in the (from, to)(after from and before to)
		return readObject(prompt, errorPrompt, str -> {
			LocalDate dateRange = LocalDate.parse(str);
			if(dateRange.isBefore(from) || dateRange.isAfter(to)) throw new IllegalArgumentException("the date is out of range");
			return dateRange;
		});
	}
	

}