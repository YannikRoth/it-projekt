package server.view;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import server.model.ServerModel;
import server.model.gameplay.ServerAction;

public class ServerView {
	ServerModel model;
	private Stage stage;
	private Button btnChangePort;
	private Button btnRestartServer;
	
	public ServerView(Stage primaryStage, ServerModel model) {
		this.stage = primaryStage;
		this.model = model;
		buildView();
	}
	
	public void buildView() {
		//Damit beim schliessen die Threads "gekillt" werden
		this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				Platform.exit();
				System.exit(0);
			}
		});
		
		BorderPane pane = new BorderPane();
		
		//HBox Top line
		HBox hBox = new HBox();
		pane.setTop(hBox);
		
		//TODO: Insert correct values from model
		TextField fieldDomain	= new TextField();
		fieldDomain.setPromptText("No domain name");
		fieldDomain.setEditable(false);
		
		TextField fieldIpAdress = new TextField();
		fieldIpAdress.setPromptText("No IP-Adress");
		fieldIpAdress.setEditable(false);
		try {
			fieldIpAdress.setText(Inet4Address.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		TextField fieldPort		= new TextField();
		fieldPort.setPromptText("No Port");
		fieldPort.setEditable(false);
		
		this.btnChangePort		= new Button("Change Port");
		this.btnRestartServer	= new Button("Restart Server");
		
		hBox.getChildren().addAll(fieldDomain, fieldIpAdress, fieldPort, btnChangePort, btnRestartServer);
		
		//TableView Center
		TableView<ServerAction> tableView = new TableView<ServerAction>();
		tableView.setItems(serverActionData);
		pane.setCenter(tableView);
		
		TableColumn<ServerAction,String> tblcolTimestamp= new TableColumn<ServerAction,String>("Timestamp");
		tblcolTimestamp.setMinWidth(130);
		tblcolTimestamp.setCellValueFactory(new PropertyValueFactory<ServerAction,String>("timestamp"));
		
		TableColumn<ServerAction,String> tblcolIpAdress	= new TableColumn<ServerAction,String>("IP-Adress");
		tblcolIpAdress.setMinWidth(80);
		tblcolIpAdress.setCellValueFactory(new PropertyValueFactory<ServerAction,String>("ipAdress"));
		
		TableColumn<ServerAction,String> tblcolPlayer	= new TableColumn<ServerAction,String>("Player");
		tblcolPlayer.setMinWidth(90);
		tblcolPlayer.setCellValueFactory(new PropertyValueFactory<ServerAction,String>("userName"));
		
		TableColumn<ServerAction,String> tblcolAction	= new TableColumn<ServerAction,String>("Action");
		tblcolAction.setMinWidth(350);
		tblcolAction.setCellValueFactory(new PropertyValueFactory<ServerAction,String>("action"));
		
		tableView.getColumns().addAll(tblcolTimestamp, tblcolIpAdress, tblcolPlayer, tblcolAction);
		
		//Final Initialisation
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("ServerStyle.css").toExternalForm());
		this.stage.sizeToScene();
		this.stage.setTitle("7wonders Server");
		this.stage.setScene(scene);
	}

	public void start() {
		stage.show();
	}
	
	public void stop() {
		stage.hide();
	}
	public Stage getStage() {
		return this.stage;
	}
	
	public Button getButtonChangePort() {
		return this.btnChangePort;
	}
	public Button getButtonRestartServer() {
		return this.btnRestartServer;
	}
	
	public ObservableList<ServerAction> serverActionData = FXCollections.observableArrayList(
	    new ServerAction("localhost", "Server", "StartUp")
	);
}
