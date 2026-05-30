package ui;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

final class ViewMessages {

	private ViewMessages() {
	}

	static String format(String key, Object... arguments) {
		ResourceBundle bundle = ResourceBundle.getBundle("labels", Locale.getDefault());
		return MessageFormat.format(bundle.getString(key), arguments);
	}
}
