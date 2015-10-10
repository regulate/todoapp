package todoapp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

public class LocalDateTimeConverter extends AbstractSingleValueConverter {

	private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E, dd-MM-yy, HH:mm");

	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
		return LocalDateTime.class.isAssignableFrom(type);
	}

	@Override
	public String toString(Object obj) {
		return dtf.format((LocalDateTime) obj);
	}

	@Override
	public Object fromString(String str) {
		LocalDateTime date = null;
		try {
			date = LocalDateTime.parse(str, dtf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

}
