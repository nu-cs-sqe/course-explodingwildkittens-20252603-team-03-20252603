package ui;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

final class ViewMessages {

	static final ResourceBundle BUNDLE = ResourceBundle.getBundle("labels", Locale.getDefault());

	private ViewMessages() {
	}

	static String format(String key, Object... arguments) {
		return MessageFormat.format(BUNDLE.getString(key), arguments);
	}
}
