package robotui;

import javafx.beans.property.SimpleStringProperty;							
import java.time.Instant;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.time.ZoneId;							
import java.time.format.DateTimeFormatter;		
import java.time.format.FormatStyle;	


public class userReport{
	
	private final SimpleStringProperty reportTitle;
	private final SimpleStringProperty reportDate;
	private static DateTimeFormatter format;
	private static Instant time;
	
	private userReport(String title){
		
		
		userReport.time = Instant.now();
		userReport.format = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.US).withZone(ZoneId.systemDefault());
		
		
		
		this.reportTitle = new SimpleStringProperty(title);
		this.reportDate = new SimpleStringProperty(format.format(userReport.time));
		
		
	}
	
	public SimpleStringProperty getTitle() {
		return this.reportTitle;
	}
	public SimpleStringProperty getDate() {
		return this.reportDate;
	}
	
	private void writeReport() {
		
		
	}
}