package server.view;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.stream.Stream;

import globals.Globals;
import globals.Translator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
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
	
	MenuBar menuBar;
	Menu menuLanguage;
	MenuItem itemGerman;
	MenuItem itemEnglish;
	MenuItem itemFrench;
	
	private TextField fieldDomain;
	private TextField fieldIpAdress;
	private TextField fieldPort;
	private Label lblDomain;
	private Label lblIpAdress;
	private Label lblPort;
//	private Button btnChangePort;
//	private Button btnRestartServer;
	
	TableColumn<ServerAction,String> tblcolTimestamp;
	TableColumn<ServerAction,String> tblcolIpAdress;
	TableColumn<ServerAction,String> tblcolPlayer;
	TableColumn<ServerAction,String> tblcolAction;
	
	public ServerView(Stage primaryStage, ServerModel model) {
		this.stage = primaryStage;
		this.model = model;
		translator = Translator.getTranslator();
		buildView();
		setTexts();
	}
	
	/**
	 * builds view elements
	 * @author david
	 */
	public void buildView() {
		BorderPane pane = new BorderPane();
		
		//MenuBar Top
		menuBar = new MenuBar();
		pane.setTop(menuBar);
		
		menuLanguage = new Menu();
		menuBar.getMenus().add(menuLanguage);
		
		itemGerman = new MenuItem();
		itemEnglish = new MenuItem();
		itemFrench = new MenuItem();
		menuLanguage.getItems().addAll(itemGerman, itemEnglish, itemFrench);
		
		//HBox Center
		HBox hBox = new HBox();
		pane.setCenter(hBox);
		
		//TODO: Insert correct values from model
		fieldDomain	= new TextField();
		fieldDomain.setEditable(false);
		
		fieldIpAdress = new TextField();
		fieldIpAdress.setEditable(false);
		fieldIpAdress.setPrefWidth(100);
		fieldIpAdress.setText(model.getHostAddress());
		fieldDomain.setText(model.getHostName());
		fieldPort		= new TextField();
		fieldPort.setEditable(false);
		fieldPort.setText(Integer.toString(Globals.getPortNr()));
		fieldPort.setPrefWidth(50);
		
		lblDomain = new Label();
		lblIpAdress = new Label();
		lblPort = new Label();
		
//		this.btnChangePort		= new Button();
//		this.btnRestartServer	= new Button("Restart Server");
		
		/**
		 * disabled till the functionality can be implemented
		 * @author david
		 */
//		this.btnChangePort.setDisable(true);
//		this.btnRestartServer.setDisable(true);
		
//		hBox.getChildren().addAll(fieldDomain, fieldIpAdress, fieldPort, btnChangePort, btnRestartServer);
		hBox.getChildren().addAll(lblDomain, fieldDomain, lblIpAdress, fieldIpAdress, lblPort, fieldPort);
		hBox.setSpacing(5);
		hBox.setAlignment(Pos.CENTER);

		
		//TableView Bottom
		TableView<ServerAction> tableView = new TableView<ServerAction>();
		tableView.setItems(model.getServerActionData());
		pane.setBottom(tableView);
		
		tblcolTimestamp= new TableColumn<ServerAction,String>();
		tblcolTimestamp.setMinWidth(140);
		tblcolTimestamp.setCellValueFactory(new PropertyValueFactory<ServerAction,String>("timestamp"));
		
		tblcolIpAdress	= new TableColumn<ServerAction,String>();
		tblcolIpAdress.setMinWidth(80);
		tblcolIpAdress.setCellValueFactory(new PropertyValueFactory<ServerAction,String>("ipAdress"));
		
		tblcolPlayer	= new TableColumn<ServerAction,String>();
		tblcolPlayer.setMinWidth(90);
		tblcolPlayer.setCellValueFactory(new PropertyValueFactory<ServerAction,String>("userName"));
		
		tblcolAction	= new TableColumn<ServerAction,String>();
		tblcolAction.setMinWidth(290);
		tblcolAction.setCellValueFactory(new PropertyValueFactory<ServerAction,String>("action"));
		
		tableView.getColumns().addAll(tblcolTimestamp, tblcolIpAdress, tblcolPlayer, tblcolAction);
		
		//Final Initialisation
		this.stage.setResizable(false);
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("ServerStyle.css").toExternalForm());
		this.stage.sizeToScene();
		this.stage.setScene(scene);
	}
	
	/**
	 * set or refresh text elements ind view, based on locale object
	 * @author david
	 */
	public void setTexts() {
		stage.setTitle(translator.getString("server.name"));
		
		menuLanguage.setText(		translator.getString("menu.language"));
		itemGerman.setText(			this.getLanguageDescription("language.german"));
		itemEnglish.setText(		this.getLanguageDescription("language.english"));
		itemFrench.setText(		this.getLanguageDescription("language.french"));
		
		fieldDomain.setPromptText(	translator.getString("text.nodomain"));
		fieldIpAdress.setPromptText(translator.getString("text.noipadress"));
		fieldPort.setPromptText(translator.getString("text.noport"));
		lblDomain.setText(translator.getString("label.domain"));
		lblIpAdress.setText(translator.getString("label.ipadress"));
		lblPort.setText(translator.getString("label.port"));
//		btnChangePort.setText(		translator.getString("button.changeport"));
//		btnRestartServer.setText(	translator.getString("button.restartserver"));
		
		tblcolTimestamp.setText(	translator.getString("column.timestamp"));
		tblcolIpAdress.setText(		translator.getString("column.ipadress"));
		tblcolPlayer.setText(		translator.getString("column.player"));
		tblcolAction.setText(		translator.getString("column.action"));
	}
	
	/**
	 * Method to check if the language item is the default system setting and return it into description
	 * @param identifier
	 * @return
	 * @author david
	 */
	private String getLanguageDescription(String identifier) {
		if(Translator.getDefaultLocale().getLanguage().substring(0, 2).equalsIgnoreCase(translator.getString(identifier).substring(0, 2)))
			return translator.getString(identifier) + " " + translator.getString("language.default");
		return translator.getString(identifier);
	}

	/**
	 * Starts port input dialog
	 * @return Optional with inserted port as String
	 * @author david
	 */
	public Optional<String> startNewPortDialog() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setGraphic(null);
		dialog.setTitle(		translator.getString("dlg.port.changeport"));
		dialog.setHeaderText(	translator.getString("dlg.port.choosenew"));
		
		((Button)dialog.getDialogPane().lookupButton(ButtonType.OK)).setText(translator.getString("dlg.ok"));
		((Button)dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setText(translator.getString("dlg.cancel"));
		
		final String port;
		
		Optional<String> result = dialog.showAndWait();
		
		return result;
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
	
//	public Button getButtonChangePort() {
//		return this.btnChangePort;
//	}
//	public Button getButtonRestartServer() {
//		return this.btnRestartServer;
//	}
	
	public Menu getMenuLanguage() {
		return this.menuLanguage;
	}
}
