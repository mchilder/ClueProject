package clueGame;
public class BadConfigFormatException extends Exception{
	public BadConfigFormatException() {
		super("Your config file is fucked up.");
	}
}
