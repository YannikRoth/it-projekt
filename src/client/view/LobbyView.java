package client.view;

import client.model.LobbyModel;
import globals.Translator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
	LobbyModel model;
	private Stage stage;
	private Translator translator = Translator.getTranslator();
	
	private Button btnNewGame;
	private Button btnRules;
	private Button btnQuit;
	private Label player;
	private TextField playerName;
	
	TableColumn<ServerAction,String> tblcolNr;
	TableColumn<ServerAction,String> tblcolWaitingPlayer;
	
	public LobbyView(Stage primaryStage, LobbyModel model) {
		this.stage = primaryStage;
		this.model = model;
		buildView();
//		setTexts();
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

		
		borderPaneMain.setTop(hBoxButton);
		borderPaneMain.setCenter(hBoxPlayer);
		
		this.btnNewGame = new Button("New Game");
		this.btnRules	= new Button("Rules");
		this.btnQuit = new Button("Quit");
		this.btnQuit.setOnAction(this::processQuitButton);
		
		hBoxButton.getChildren().addAll(btnNewGame, btnRules, btnQuit);
		
		this.player = new Label("Your player name");
		this.playerName = new TextField("Name");
		
		hBoxPlayer.getChildren().addAll(player, playerName);
		
		TableView<ServerAction> tableView = new TableView<ServerAction>();
		tableView.setItems(serverActionData);
		
		tblcolNr	= new TableColumn<ServerAction,String>("Nr.");
		tblcolNr.setMinWidth(90);
		tblcolNr.setCellValueFactory(new PropertyValueFactory<ServerAction,String>("Nr."));
		
		tblcolWaitingPlayer	= new TableColumn<ServerAction,String>("Waiting Players");
		tblcolWaitingPlayer.setMinWidth(350);
		tblcolWaitingPlayer.setCellValueFactory(new PropertyValueFactory<ServerAction,String>("Waiting Players"));
		
		tableView.getColumns().addAll(tblcolNr, tblcolWaitingPlayer);
		borderPaneMain.setBottom(tableView);
		
		Scene scene = new Scene(borderPaneMain);
//		scene.getStylesheets().add(getClass().getResource("ServerStyle.css").toExternalForm());
		this.stage.sizeToScene();
		this.stage.setTitle("7 Wonders");
		this.stage.setScene(scene);
		this.stage.show();
	}
	
//	public void setTexts() {
//		btnNewGame.setText(translator.getString("button.newgame"));
//		btnRules.setText(translator.getString("button.rules"));
//		btnQuit.setText(translator.getString("button.quit"));
//		
//		tblcolNr.setText(translator.getString("column.nr"));
//		tblcolWaitingPlayer.setText(translator.getString("column.waitingplayers"));
//
//	}
	public void start(Stage stage) {
		stage.show();
	}
	
	public void stop() {
		stage.hide();
	}
	
	public Stage getStage() {
		return this.stage;
	}
	
	public void processNewGameButton(ActionEvent event) throws Exception {
	//TODO
	}
    public void processQuitButton(ActionEvent event) {
    	Platform.exit();
    }
	
	public ObservableList<ServerAction> serverActionData = FXCollections.observableArrayList(
		    new ServerAction("Nr", "Waiting Players", "test")
		);
}
