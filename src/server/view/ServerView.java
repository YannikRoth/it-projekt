package server.view;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import globals.Translator;
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
import server.ServiceLocator;
import server.model.ServerModel;
import server.model.gameplay.ServerAction;
/**
 * Server view for Server MVC
 * @author david
 *
 */
public class ServerView {
	ServerModel model;
	private Stage stage;
	private Translator translator;
	
	private TextField fieldDomain;
	private TextField fieldIpAdress;
	private TextField fieldPort;
	private Button btnChangePort;
	private Button btnRestartServer;
	
	TableColumn<ServerAction,String> tblcolTimestamp;
	TableColumn<ServerAction,String> tblcolIpAdress;
	TableColumn<ServerAction,String> tblcolPlayer;
	TableColumn<ServerAction,String> tblcolAction;
	
	public ServerView(Stage primaryStage, ServerModel model) {
		this.stage = primaryStage;
		this.model = model;
		translator = ServiceLocator.getTranslator();
		buildView();
		setTexts();
	}
	
	/**
	 * builds view elements
	 * @author david
	 */
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
		fieldDomain	= new TextField();
		fieldDomain.setEditable(false);
		
		fieldIpAdress = new TextField();
		fieldIpAdress.setEditable(false);
		try {
			fieldIpAdress.setText(Inet4Address.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		fieldPort		= new TextField();
		fieldPort.setEditable(false);
		
		this.btnChangePort		= new Button();
		this.btnRestartServer	= new Button("Restart Server");
		
		hBox.getChildren().addAll(fieldDomain, fieldIpAdress, fieldPort, btnChangePort, btnRestartServer);
		
		//TableView Center
		TableView<ServerAction> tableView = new TableView<ServerAction>();
		tableView.setItems(serverActionData);
		pane.setCenter(tableView);
		
		tblcolTimestamp= new TableColumn<ServerAction,String>();
		tblcolTimestamp.setMinWidth(130);
		tblcolTimestamp.setCellValueFactory(new PropertyValueFactory<ServerAction,String>("timestamp"));
		
		tblcolIpAdress	= new TableColumn<ServerAction,String>();
		tblcolIpAdress.setMinWidth(80);
		tblcolIpAdress.setCellValueFactory(new PropertyValueFactory<ServerAction,String>("ipAdress"));
		
		tblcolPlayer	= new TableColumn<ServerAction,String>();
		tblcolPlayer.setMinWidth(90);
		tblcolPlayer.setCellValueFactory(new PropertyValueFactory<ServerAction,String>("userName"));
		
		tblcolAction	= new TableColumn<ServerAction,String>();
		tblcolAction.setMinWidth(350);
		tblcolAction.setCellValueFactory(new PropertyValueFactory<ServerAction,String>("action"));
		
		tableView.getColumns().addAll(tblcolTimestamp, tblcolIpAdress, tblcolPlayer, tblcolAction);
		
		//Final Initialisation
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("ServerStyle.css").toExternalForm());
		this.stage.sizeToScene();
		this.stage.setTitle(ServiceLocator.getTranslator().getString("server.name"));
		this.stage.setScene(scene);
	}
	
	/**
	 * set or refresh text elements ind view, based on locale object
	 * @author david
	 */
	public void setTexts() {
		fieldDomain.setPromptText(translator.getString("text.nodomain"));
		fieldIpAdress.setPromptText(translator.getString("text.noipadress"));
		fieldPort.setPromptText(translator.getString("text.noport"));
		btnChangePort.setText(translator.getString("button.changeport"));
		btnRestartServer.setText(translator.getString("button.restartserver"));
		
		tblcolTimestamp.setText(translator.getString("column.timestamp"));
		tblcolIpAdress.setText(translator.getString("column.ipadress"));
		tblcolPlayer.setText(translator.getString("column.player"));
		tblcolAction.setText(translator.getString("column.action"));
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
