package gwt.shared;

public class FieldVerifier {
	public static boolean isValidName(String s) {
		if (s == null) {
			return false;
		}
		return s.length() > 3;
	}
}
