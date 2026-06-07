package ui;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

final class ViewMessages {

	private static Locale locale = Locale.getDefault();

	private ViewMessages() {
	}

	static void setLocale(Locale newLocale) {
		if (newLocale == null) {
			locale = Locale.getDefault();
		} else {
			locale = newLocale;
		}
	}

	static String format(String key, Object... arguments) {
		ResourceBundle bundle = ResourceBundle.getBundle("labels", locale);
		return MessageFormat.format(bundle.getString(key), arguments);
	}
}
