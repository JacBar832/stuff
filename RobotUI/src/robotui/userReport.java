package robotui;

import javafx.beans.property.SimpleStringProperty;							
import java.time.Instant;
import java.util.Formatter;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.time.ZoneId;							
import java.time.format.DateTimeFormatter;		
import java.time.format.FormatStyle;	
import javafx.scene.control.TextField;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

public class userReport{
	
	public final SimpleStringProperty reportTitle;
	public final SimpleStringProperty reportDate;
	private static DateTimeFormatter format;
	private static Instant timeStamp;
	private Formatter errorReport;
	private static Path filePath;
	
	userReport(String title, String userError){
		
		
		userReport.timeStamp = Instant.now();
		userReport.format = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.US).withZone(ZoneId.systemDefault());
		
		filePath = Paths.get("C:/Users/../Documents/User Reports");
		
		//writeReport(title, userError);
		this.reportTitle = new SimpleStringProperty(title);
		this.reportDate = new SimpleStringProperty(format.format(timeStamp));
		
		
	}
	
	public SimpleStringProperty getTitle() {
		return this.reportTitle;
	}
	public SimpleStringProperty getDate() {
		return this.reportDate;
	}
	
	private void writeReport(TextField summaryTag, TextField userError) {
		
		try {
			errorReport = new Formatter(filePath + summaryTag.getText() + ".txt");
			errorReport.format("%s1$035 %s %s",summaryTag.getText(), timeStamp, userError.getText());
			errorReport.close();
			}catch(Exception e) {
				
				System.err.println("Error: " + e);
				
			}	
		
	}
}