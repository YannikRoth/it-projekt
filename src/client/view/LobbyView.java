package client.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;

import client.ClientMVC;
import client.controller.ClientController;
import client.controller.LobbyController;
import client.model.ClientModel;
import client.model.LobbyModel;
import globals.Globals;
import globals.Translator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import server.model.gameplay.ServerAction;

/**
 * 
 * @author philipp
 *
 */

public class LobbyView {
	private LobbyModel model;
	private Stage stage;
	private Translator translator = Translator.getTranslator();
	
	private Button btnNewGame, btnRules, btnQuit, btnConnect;
	private Label player, ipAdressLabel, portLabel;
	private TextField playerName, ipAdress, port;
	
	Menu menuLanguage;
	
	MenuItem itemGerman, itemEnglish;
	
	TableColumn<ServerAction,String> tblcolNr;
	TableColumn<ServerAction,String> tblcolWaitingPlayer;
	
	public LobbyView(Stage primaryStage, LobbyModel model) {
		this.stage = primaryStage;
		this.model = model;
		
//		TextInputDialog dialog = new TextInputDialog("127.0.0.1:8080");
//		dialog.setTitle(translator.getString("title.ip"));
//		dialog.setHeaderText(translator.getString("header.opponents"));
//		dialog.setContentText(translator.getString("content.ip"));
//		
//		((Button)dialog.getDialogPane().lookupButton(ButtonType.OK)).setText(translator.getString("dlg.ok"));
//		((Button)dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setText(translator.getString("dlg.cancel"));
//		
//		
//		Optional<String> result = dialog.showAndWait();
//		result.ifPresent(name -> {
//			LobbyController.handleIpInput(name);
//		});
//		
		
		buildView();
		setTexts();
	}	
		
	public void buildView() {
		//Erstellt die Grundmaske der Lobby
		BorderPane borderPaneMain = new BorderPane();
		
		VBox buttonAndPlayer = new VBox();
		HBox hBoxButton = new HBox();
		HBox hBoxPlayer = new HBox();

		borderPaneMain.setCenter(buttonAndPlayer);
		
		FileInputStream input = null;
		try {
			input = new FileInputStream("resource/images/playlogo.jpg");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Image image = new Image(input);
        ImageView imageViewStart = new ImageView(image);
        imageViewStart.setFitHeight(30);
		imageViewStart.setFitWidth(30);
        this.btnNewGame = new Button("Play", imageViewStart);
        
        FileInputStream input2 = null;
		try {
			input2 = new FileInputStream("resource/images/regelnlogo.jpg");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Image image2 = new Image(input2);
        ImageView imageViewRules = new ImageView(image2);
        imageViewRules.setFitHeight(30);
		imageViewRules.setFitWidth(30);
		this.btnRules	= new Button("Regeln", imageViewRules);
		
		FileInputStream input3 = null;
		try {
			input3 = new FileInputStream("resource/images/beendenlogo.jpg");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Image image3 = new Image(input3);
        ImageView imageViewQuit = new ImageView(image3);
        imageViewQuit.setFitHeight(30);
		imageViewQuit.setFitWidth(30);
		this.btnQuit = new Button("Quit", imageViewQuit);

		hBoxButton.setPadding(new Insets(0, 0, 10, 0));
		
		hBoxButton.getChildren().addAll(btnNewGame, btnRules, btnQuit);
		
		this.player = new Label();
		this.playerName = new TextField();
		playerName.setText(System.getProperty("user.name"));

		
		hBoxPlayer.getChildren().addAll(player, playerName);
		hBoxPlayer.setPadding(new Insets(0, 0, 10, 0));
		
		HBox hBoxIpAdress = new HBox();
		HBox hBoxConnect = new HBox();
		this.ipAdressLabel = new Label();
		this.ipAdress = new TextField("127.0.0.1");
		this.portLabel = new Label();
		this.port = new TextField("8080");
		port.setPrefWidth(50);
		ipAdress.setPrefWidth(80);
		
		hBoxIpAdress.getChildren().addAll(ipAdressLabel, ipAdress, portLabel, port);
		hBoxIpAdress.setSpacing(5);
		
		this.btnConnect = new Button();
		hBoxConnect.getChildren().add(btnConnect);
		
		buttonAndPlayer.getChildren().addAll(hBoxIpAdress, hBoxButton, hBoxPlayer);
//		buttonAndPlayer.getChildren().addAll(hBoxIpAdress, hBoxButton, hBoxPlayer, hBoxConnect);
		hBoxButton.setAlignment(Pos.CENTER);
		hBoxPlayer.setAlignment(Pos.CENTER);
		hBoxIpAdress.setAlignment(Pos.CENTER);
		hBoxConnect.setAlignment(Pos.CENTER);
		
		hBoxIpAdress.setPadding(new Insets(0, 0, 10, 0));
		hBoxConnect.setPadding(new Insets(0, 0, 10, 0));
		
		//TODO: Wartende Personen anzeigen
		TableView<ServerAction> tableView = new TableView<ServerAction>();
		tableView.setPrefHeight(250);
		tableView.setItems(serverActionData);
		
		tblcolNr	= new TableColumn<ServerAction,String>();
		tblcolNr.setMinWidth(90);
		tblcolNr.setCellValueFactory(new PropertyValueFactory<ServerAction,String>("Nr."));
		
		tblcolWaitingPlayer	= new TableColumn<ServerAction,String>();
		tblcolWaitingPlayer.setMinWidth(350);
		tblcolWaitingPlayer.setCellValueFactory(new PropertyValueFactory<ServerAction,String>("Waiting Players"));
		
		tableView.getColumns().addAll(tblcolNr, tblcolWaitingPlayer);
		borderPaneMain.setBottom(tableView);
		
		//Menu "Language"
		itemGerman = new MenuItem();
		itemEnglish = new MenuItem();
		menuLanguage = new Menu();
		menuLanguage.getItems().addAll(itemGerman, itemEnglish);
		
		MenuBar menuBar = new MenuBar(menuLanguage);
		borderPaneMain.setTop(menuBar);
		
		
		this.stage.setResizable(false);
		Scene scene = new Scene(borderPaneMain);
		scene.getStylesheets().add(getClass().getResource("LobbyStyle.css").toExternalForm());
		this.stage.sizeToScene();
		this.stage.setScene(scene);
		this.stage.show();
	}
	
	private String getLanguageDescription(String identifier) {
		if(Translator.getDefaultLocale().getLanguage().substring(0, 2).equalsIgnoreCase(translator.getString(identifier).substring(0, 2)))
			return translator.getString(identifier) + " " + translator.getString("language.default");
		return translator.getString(identifier);
	}
	
	public void setTexts() {
		btnNewGame.setText(translator.getString("button.newgame"));
		btnRules.setText(translator.getString("button.rules"));
		btnQuit.setText(translator.getString("button.quit"));
		btnConnect.setText(translator.getString("button.connect"));
		
		player.setText(translator.getString("label.player"));
		playerName.setPromptText(translator.getString("textfield.player"));
		ipAdressLabel.setText(translator.getString("label.ipadress"));
		portLabel.setText(translator.getString("label.port"));
		
		stage.setTitle(translator.getString("clientLobby.name"));
		
		itemGerman.setText(this.getLanguageDescription("language.german"));
		itemEnglish.setText(this.getLanguageDescription("language.english"));
		
		menuLanguage.setText(translator.getString("menu.language"));
		
		tblcolNr.setText(translator.getString("column.nr"));
		tblcolWaitingPlayer.setText(translator.getString("column.waitingplayers"));

	}
	
	/**
	 * disables functionality when the game has started
	 * @author david
	 */
	public void disableDialogElements() {
		playerName.setEditable(false);
		ipAdress.setEditable(false);
		port.setEditable(false);
		btnNewGame.setDisable(true);
		btnQuit.setDisable(true);
	}
	
	public void start() {
		stage.show();
	}
	
	public void stop() {
		stage.hide();
	}
	
	public Menu getMenuLanguage() {
		return this.menuLanguage;
	}
	
	public Stage getStage() {
		return this.stage;
	}
	
	public Button getNewGameButton() {
		return this.btnNewGame;
	}

	public Button getRulesButton() {
		return this.btnRules;
	}
	
	public Button getQuitButton() {
		return this.btnQuit;
	}
	
	public MenuItem getGermanItem2() {
		return this.itemGerman;
	}
	
	public MenuItem getEnglishItem2() {
		return this.itemEnglish;
	}
	
	public String getIpAdress() {
		return this.ipAdress.getText();
	}
	
	public int getPort() {
		return Integer.parseInt(this.port.getText());
	}

	public ObservableList<ServerAction> serverActionData = FXCollections.observableArrayList(
		    new ServerAction("Nr", "Waiting Players", "test")
		);
	
}
