package client.view;

import java.util.Optional;

import client.model.LobbyModel;
import globals.Translator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
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
	
	private Button btnNewGame;
	private Button btnRules;
	private Button btnQuit;
	private Label player;
	private TextField playerName;
	
	Menu menuLanguage;
	
	MenuItem itemGerman, itemEnglish;
	
	TableColumn<ServerAction,String> tblcolNr;
	TableColumn<ServerAction,String> tblcolWaitingPlayer;
	
	public LobbyView(Stage primaryStage, LobbyModel model) {
		this.stage = primaryStage;
		this.model = model;
		
		TextInputDialog dialog = new TextInputDialog("192.168.1.");
		dialog.setTitle(translator.getString("title.ip"));
		dialog.setHeaderText(translator.getString("header.opponents"));
		dialog.setContentText(translator.getString("content.ip"));
		
		((Button)dialog.getDialogPane().lookupButton(ButtonType.OK)).setText(translator.getString("dlg.ok"));
		((Button)dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setText(translator.getString("dlg.cancel"));
		
		
		Optional<String> result = dialog.showAndWait();
		result.ifPresent(name -> {
			//TODO: Set Server IP in model
			System.out.println(name);
		});
		
		
		buildView();
		setTexts();
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
		//Erstellt die Grundmaske der Lobby
		BorderPane borderPaneMain = new BorderPane();
		
		HBox hBoxButton = new HBox();
		HBox hBoxPlayer = new HBox();

		
		borderPaneMain.setCenter(hBoxButton);
		borderPaneMain.setBottom(hBoxPlayer);
		
		this.btnNewGame = new Button();
		this.btnRules	= new Button();
		this.btnQuit = new Button();

		
		hBoxButton.getChildren().addAll(btnNewGame, btnRules, btnQuit);
		
		this.player = new Label();
		this.playerName = new TextField();
		
		hBoxPlayer.getChildren().addAll(player, playerName);
		
		//TODO: Wartende Personen anzeigen
		TableView<ServerAction> tableView = new TableView<ServerAction>();
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
		scene.getStylesheets().add(getClass().getResource("ClientStyle.css").toExternalForm());
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
		
		player.setText(translator.getString("label.player"));
		playerName.setText(translator.getString("textfield.player"));
		
		stage.setTitle(translator.getString("clientLobby.name"));
		
		itemGerman.setText(this.getLanguageDescription("language.german"));
		itemEnglish.setText(this.getLanguageDescription("language.english"));
		
		menuLanguage.setText(translator.getString("menu.language"));
		
		tblcolNr.setText(translator.getString("column.nr"));
		tblcolWaitingPlayer.setText(translator.getString("column.waitingplayers"));

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

	public ObservableList<ServerAction> serverActionData = FXCollections.observableArrayList(
		    new ServerAction("Nr", "Waiting Players", "test")
		);
	
}
