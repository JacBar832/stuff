package robotui;
	
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.scene.layout.BorderPane;

//Top Menu
import javafx.scene.layout.HBox;	
import javafx.scene.layout.VBox;	
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;



//Driver View
//This is going to use a scheduled executor service as well,
//it'll be similar to the linechart, where a frame from the camera will be
//used instead of voltage values

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


//Robot Data [Temporary]
	
	//These classes are going to be used to make time stamps that'll be on user error reports.
	//On the left side of the main screen there's a textfield where they can write in any problems they've run into,
	//and whenever they open the app again, hopefully they'll get to see a list of those problems. I still need to add that list

import java.time.Clock;							
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.time.ZoneId;							
import java.time.format.DateTimeFormatter;		
import java.time.format.FormatStyle;			


//I need this to update the linechart in real time
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


//User Reporting
import java.util.Formatter;
import java.util.Locale;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;


	@Author(
			name = "Jacob Barba",
			date = "1/6/2019"
			)

/*
 * 
 * 							**NOTE**
 * 		
 * 
 * 					REGARDING CODE ORGANIZATION:
 * 
 * 		The Code's organized a little weird right now,
 * 		I'm learning how to work with JavaFX, so until
 * 		I've worked out the kinks here, each section on 
 * 		the app is organized as a method in the code,
 * 		an example of this would be the top menu, which is
 * 		currently a method called topMenu(), this is mentioned
 * 		later, but I'm stating that here for the record.  
 * 		I will go back and clean this up later on, but
 * 		for now it's just a temporary measure for troubleshooting
 * 		convenience.
 * 
 * 
 * 					
 * 	
 * 
 * 
 */


public class Main extends Application {
	
	
	private static Scene scene1, sceneTwo, sceneThree;
	
	//Top Menu Stuff
	private static HBox topMenu;
	private static Label mainTitle;
	private static MenuBar menubar;
	private static Menu menufile;
	private static MenuItem mainScreen, powerMonitor, robotStatus;
	
	
	
	//The Stuff the Driver's going to look at 
	private static VBox middleView, leftSide;
	private static HBox robotUpdates;
	private static ImageView videoFeed;
	
		//
		
	
	
	
	//Left Side Problem Reporting System
	private static TextField userError, summaryTag;
	private static Clock userGen = Clock.systemDefaultZone();
	private static Instant currentTime;
	private static VBox report;
	private static Label purpose;
	private static Formatter errorReport;
	private static DateTimeFormatter formatter;
	private static String timeStamp;
	private static CheckBox includeCommands, includePowerReports;
	private static TableView reports;
	private static TableColumn resCol, sumTagCol, repDateCol;
	
	

	private static Clock timer = Clock.systemUTC();
	private static ScheduledExecutorService task;	//voltageMeter Stuff
	
	@Override
	public void start(Stage primaryStage) {
		
		try {
			
			BorderPane root = new BorderPane();
			root.setId("main");
			
			BorderPane scene2 = new BorderPane();
			scene2.setId("scene2");
			
			BorderPane scene3 = new BorderPane();
			scene3.setId("scene3");
			
			
			scene1 = new Scene(root,1400,800);
			sceneTwo = new Scene(scene2, 1400, 800);
			sceneThree = new Scene(scene3, 1400, 800);
			
			//adding elements
			root.setTop(topMenu(primaryStage));
			scene2.setTop(topMenu(primaryStage));
			scene3.setTop(topMenu(primaryStage));
			
			
			
			root.setCenter(driverView(primaryStage));
			root.setLeft(sideStuffLeft(primaryStage));
			root.setRight(sideStuffRight(primaryStage));
			
			
			scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			sceneTwo.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			sceneThree.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			
			
			primaryStage.setScene(scene1);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	//I'm separating the different boxes into functions to make it easier to edit
	//their individual elements, it's a temporary measure until the logic's sorted out
	
	public static HBox topMenu(Stage stage) {
		//Define the top Menu
			//Setting general spacing and size
			topMenu = new HBox();
			topMenu.setPadding(new Insets(10));	//this refers to spacing between the box's borders and the stuff inside
			topMenu.setSpacing(8);	//this refers to spacing between the stuff that's inside
			topMenu.setId("topMenu");	//this sets the ID we can use to edit the css
			topMenu.setMinWidth(400.0);
			topMenu.setMaxWidth(1800.0);
			//Originally, I was going to set a minimum and maximum size, however, the MenuBar class
			//already automatically sizes itself.
		
		//Adding Actual Menu
		menubar = new MenuBar();	//this is just the bar at the top
			menubar.setId("diagnosticMenu");
			//This is the actual menu to be interacted with
			menufile = new Menu("Diagnostic Tools");
			//menufile.setId("diagnosticMenu");
			menubar.getMenus().add(menufile);
			
			//Titles for each scene
			
			
			

			//
			//
			//			***MENU ITEMS***			
			//
			
			mainScreen = new MenuItem("Main Screen");
				//What happens when you click it:
				mainScreen.setOnAction((event)->{stage.setScene(scene1);});
				
			powerMonitor = new MenuItem("Power Monitor");
				//What happens when you click it:
				powerMonitor.setOnAction((event)->{stage.setScene(sceneTwo);});
				
			robotStatus = new MenuItem("Robot Status");
				//What happens when you click it:
				robotStatus.setOnAction((event)->{stage.setScene(sceneThree);});
			
			
			
			
			menufile.getItems().addAll(mainScreen, powerMonitor, robotStatus);
		
			
		//I personally like the icon, to avoid the issue of using a non-static method,
		//I'm just going to reference the actual file
		
		
		
		Button exitProgram = new Button("Exit Program");
		exitProgram.setOnAction(new EventHandler<ActionEvent>(){
			
			@Override
			public void handle(ActionEvent event) {
				
				System.exit(0);
				
			}
			
		}); 
		
		exitProgram.setAlignment(Pos.TOP_RIGHT);
		
		topMenu.getChildren().addAll(menubar, exitProgram);
		
		return topMenu;	//This is a personal reminder; from my understanding, Lambda Functions let objects be used
						//as variables, I think, so if I just make this return the top menu, I should still be able
						//to use the top menu I defined in this function, and, since I made the function static,
						//it shouldn't be any different regardless of where I use it, like, this is the one menu I have,
						//it wont be made twice
	}
	
	
	public VBox driverView(Stage stage) {
		
		middleView = new VBox();
		middleView.setPadding(new Insets(10, 125, 10, 125));
		middleView.setSpacing(10);
		middleView.setId("driverView");
		middleView.setMinWidth(400);
		
		//Video Feed
		
		Image image = new Image(getClass().getResourceAsStream("steveharvey.jpeg"));//Stand in for eventual feed
		
		ImageView videoFeed = new ImageView(image);	//this is the actual videofeed
		
		videoFeed.setId("video-feed");//css stuff
		
		
		
		//Robot Status updates
		
		//The first row should show the current commands being used by the robot,
		//The second row should show the 
		VBox command = new VBox();
		command.setId("HBox");
		Label currentCommand = new Label("Current Command: " );
		currentCommand.setStyle("-fx-text-fill: #ffffff;");
		
		
		
		Label robotStatus = new Label("Robot Status: ");
		robotStatus.setStyle("-fx-text-fill: #ffffff;");
		
		command.getChildren().addAll(currentCommand, robotStatus);
		
		
		
		middleView.getChildren().addAll(videoFeed, command);
		
		//middleView.getChildren().addAll(videoFeed, command);
		return middleView;
	}
	
	
public static VBox sideStuffRight(Stage stage) {
		
		leftSide = new VBox();
		leftSide.setPadding(new Insets(10));
		leftSide.setSpacing(20);
		//leftSide.setMinWidth(50);
		leftSide.setMinWidth(250);
		
		VBox labels = new VBox();
				
		Label powerIssues = new Label("Power Status: ");
		powerIssues.setId("powerIssues");
		
		Label potIssues = new Label("Potential Issues: ");
		potIssues.setId("potIssues");
		
		labels.setId("labelbox");
		labels.getChildren().addAll(powerIssues, potIssues);
		
		leftSide.getChildren().addAll(voltageMeter(), labels);
		
		
		leftSide.setId("sideMenu");
		
		return leftSide;
	}
	
public static VBox sideStuffLeft(Stage stage) {
		
		leftSide = new VBox();
		leftSide.setPadding(new Insets(10));
		leftSide.setSpacing(10);
		leftSide.setMinWidth(250);
		
		
		//
		//	ERROR REPORTING SERVICE:
		//
		
		report = new VBox();
		report.setPadding(new Insets(10));
		report.setSpacing(10);
		
		purpose = new Label("Report Issues Here: ");	//Section Title
		
		userError = new TextField();					//User report here
		userError.setPromptText("Problem Report Here");
		
		summaryTag = new TextField();					//.txt file title here for the reports
		summaryTag.setPromptText("Report Title Here");
		
		
		currentTime = Instant.now(userGen);				//Stuff needed to make timestamps for reports
		formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.US).withZone(ZoneId.systemDefault());
		
		timeStamp = formatter.format(currentTime);
		
		includeCommands = new CheckBox("Include Commands of Last 5min");
		includeCommands.setSelected(false);
		includeCommands.setIndeterminate(false);
		
		includePowerReports = new CheckBox("Include Power Reports of Last 5min");
		includePowerReports.setSelected(false);
		includePowerReports.setIndeterminate(false);
		
		
		
		Button rep = new Button("Submit Report");
		
		
		
		//Here Users will submit their reports,
		
		//	Note, the only reason this button doesn't use a lambda function is
		//	because I need it to actually write the report; however, I think the
		//	best course of action here will be using a new thread to do this, like
		//	the oracle tutorial recommends, however, I'm not sure what will be the
		//	best way to do this, instantiating an object here or making a class for
		//	it that implements "Runnable", or if there's another way.
		
		
		rep.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				
				
			}
			
		});
		
		
		report.getChildren().addAll(purpose, summaryTag, userError, includeCommands, includePowerReports, rep);
		
		Label curRep = new Label("Current Reports: ");
		
		leftSide.getChildren().addAll(report, curRep, reports());
		
		
		leftSide.setId("sideMenu");
		
		return leftSide;
	}

	
	public static LineChart voltageMeter() {
		
		
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		
		xAxis.setLabel("Time");
		yAxis.setLabel("Voltage");
		
		final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis,yAxis);
		
		lineChart.setTitle("Voltage Meter?");
		
		XYChart.Series series = new XYChart.Series();
		
		
		try {
			
		Runnable dataUpdate = new Runnable() {
		
		int i = 0, j = 1;
		
			@Override
			public void run() {
				
				
				//Explanation:
				//Here individual data points are added,
				//
				
				i++;
				j++; //Stand in for actual Voltage
					
				series.getData().add(new XYChart.Data<>(i, j)); 
				
				System.out.println("Time(seconds): " + j + "\n" + "Iteration: " + i);
				
				
					if(i == 130) {task.shutdown();}	//Temporary--I need this to automatically stop the actual feed until I work out the kinks
					
			}
			
		};
		
		
		lineChart.getData().add(series);
		lineChart.setCreateSymbols(false);
		
		task = Executors.newSingleThreadScheduledExecutor();
		task.scheduleAtFixedRate(dataUpdate, 0, 500, TimeUnit.MILLISECONDS);
		
		
		}catch(Exception e) {
			
			System.err.println("Exception: " + e);
			
		}
		return lineChart;
	}
	
	
	
	
	
	public static TableView reports() {
		
		reports = new TableView();
		reports.setEditable(true);
		
		resCol = new TableColumn("Resolved?");
		sumTagCol = new TableColumn("Report Title");
		repDateCol = new TableColumn("Report Date");
		
		
		reports.getColumns().addAll(resCol, sumTagCol, repDateCol);
		
		return reports;
	}

	}