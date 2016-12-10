package nrp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Datumsrechner {
	

	public static int tageBisZumGeburtstag(String datumString) throws ParseException {

		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");

		Date datum = df.parse(datumString);
		Date heute = new Date();
		datum.setYear(heute.getYear());
		if (datum.before(heute)) {
			datum.setYear(heute.getYear() + 1);
		}
		long ms = datum.getTime() - heute.getTime();
		float sek = ms / 1000;
		float min = sek / 60;
		float std = min / 60;
		int tage = (int) Math.ceil(std / 24);
		if(tage==365)return 0;
		return tage;
	}
}