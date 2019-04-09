package client.view;

import client.model.ClientModel;
import globals.Translator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * 
 * @author philipp
 *
 */

public class ClientView {
	private Stage stage;
	private ClientModel model;
	private Translator translator = Translator.getTranslator();

	public ClientView(Stage primaryStage, ClientModel model) {
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
		
		BorderPane borderPaneMain = new BorderPane();
		
		//Table View opponents
		TableView tableOpponents = new TableView();
		tableOpponents.setEditable(false);
		borderPaneMain.setCenter(tableOpponents);
		
		TableColumn ColPlayer	= new TableColumn("Player");
		ColPlayer.setMinWidth(100);
		TableColumn ColStone	= new TableColumn("Stone");
		ColStone.setMinWidth(100);
		TableColumn ColOre		= new TableColumn("Ore");
		ColOre.setMinWidth(100);
		TableColumn ColWood		= new TableColumn("Wood");
		ColWood.setMinWidth(100);
		TableColumn ColGlass	= new TableColumn("Glass");
		ColGlass.setMinWidth(100);
		TableColumn ColClay		= new TableColumn("Clay");
		ColClay.setMinWidth(100);
		TableColumn ColLoom		= new TableColumn("Loom");
		ColLoom.setMinWidth(100);
		TableColumn ColPaper	= new TableColumn("Paper");
		ColPaper.setMinWidth(100);
		TableColumn ColCoin		= new TableColumn("Coin");
		ColCoin.setMinWidth(100);
		TableColumn ColGeom 	= new TableColumn("Geom");
		ColGeom.setMinWidth(100);
		TableColumn ColWrit 	= new TableColumn("Writ");
		ColWrit.setMinWidth(100);
		TableColumn ColEng		= new TableColumn("Eng");
		ColEng.setMinWidth(100);
		TableColumn ColShield	= new TableColumn("Shield");
		ColShield.setMinWidth(100);
		TableColumn ColMilitary	= new TableColumn("Military");
		ColMilitary.setMinWidth(100);
		TableColumn ColWinning	= new TableColumn("Winning");
		ColWinning.setMinWidth(100);
		
		tableOpponents.getColumns().addAll(ColPlayer, ColStone, ColOre, ColWood, ColGlass, ColClay, ColLoom, ColPaper, ColCoin, ColGeom, ColWrit, ColEng, ColShield, ColMilitary, ColWinning);
		
		//Player Deck
		HBox hBoxPlayer = new HBox();
		borderPaneMain.setBottom(hBoxPlayer);
		
		BorderPane borderPanePlayer = new BorderPane();
		hBoxPlayer.getChildren().addAll(borderPanePlayer);
		
		HBox hBoxCards = new HBox();
		borderPanePlayer.setTop(hBoxCards);
		hBoxPlayer.setHgrow(borderPanePlayer, Priority.ALWAYS);
		
		//Cards
		Image image = new Image("https://cf.geekdo-images.com/camo/6fd08fe60edcfe435528f3f3b9c369cc198a1faf/687474703a2f2f636f72726f6a6575782e66696c65732e776f726470726573732e636f6d2f323031322f31322f7a332d74656d706c652d696e746333613972696575722e6a70673f773d363035");
		ImageView card1 = new ImageView(image);
		card1.setFitHeight(150);
		card1.setFitWidth(100);
		
		ImageView card2 = new ImageView(image);
		card2.setFitHeight(150);
		card2.setFitWidth(100);
		
		ImageView card3 = new ImageView(image);
		card3.setFitHeight(150);
		card3.setFitWidth(100);
		
		ImageView card4 = new ImageView(image);
		card4.setFitHeight(150);
		card4.setFitWidth(100);
		
		ImageView card5 = new ImageView(image);
		card5.setFitHeight(150);
		card5.setFitWidth(100);
		
		ImageView card6 = new ImageView(image);
		card6.setFitHeight(150);
		card6.setFitWidth(100);
		
		ImageView card7 = new ImageView(image);
		card7.setFitHeight(150);
		card7.setFitWidth(100);
		
		hBoxCards.getChildren().addAll(card1, card2, card3, card4, card5, card6, card7);
		
		
		//Points
		TableView tablePoints = new TableView();
		tablePoints.setEditable(false);
		hBoxPlayer.getChildren().addAll(tablePoints);
		
		TableColumn ColType		= new TableColumn("Type");
		ColType.setMinWidth(100);
		TableColumn ColAmount	= new TableColumn("Amount");
		ColAmount.setMinWidth(100);
		
		tablePoints.getColumns().addAll(ColType, ColAmount);
		
		//Deck
		HBox hBoxDeck = new HBox();
		borderPanePlayer.setBottom(hBoxDeck);
		
		ImageView deck = new ImageView(new Image("https://www.theboardgamefamily.com/wp-content/uploads/2012/05/Ending-civilization-200x129.jpg"));
		deck.setFitHeight(250);
		deck.setFitWidth(600);
		
		hBoxDeck.getChildren().addAll(deck);
		
		
		
		//Menu "Game"
		MenuItem itemM1 = new MenuItem("Move one");
		MenuItem itemM2 = new MenuItem("Move two");
		MenuItem itemM3 = new MenuItem("Move three");
		MenuItem itemM4 = new MenuItem("Move four");
		MenuItem itemM5 = new MenuItem("Move five");
		MenuItem itemM6 = new MenuItem("Move six");
		MenuItem itemM7 = new MenuItem("Move seven");
		Menu menuGame = new Menu("Game");
		menuGame.getItems().addAll(itemM1, itemM2, itemM3, itemM4, itemM5, itemM6, itemM7);
		
		//Menu "Help"
		MenuItem itemM8 = new MenuItem("New Game");
		itemM8.setOnAction(this::processNewGameItem);
		MenuItem itemM9 = new MenuItem("Rules");
		itemM9.setOnAction(this::processRulesItem);
		MenuItem itemM10 = new MenuItem("Hint");
		itemM10.setOnAction(this::processHintItem);
		MenuItem itemM11 = new MenuItem("About");
		itemM11.setOnAction(this::processAboutItem);
		MenuItem itemM12 = new MenuItem("Quit");
		itemM12.setOnAction(this::processQuitItem);
		Menu menuHelp = new Menu("Help");
		menuHelp.getItems().addAll(itemM8, itemM9, itemM10, itemM11, itemM12);
		
		MenuBar menuBar = new MenuBar(menuHelp);
		borderPaneMain.setTop(menuBar);
		
		//DropDown Menu
		ContextMenu contextMenu = new ContextMenu(menuGame);
		borderPaneMain.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                contextMenu.show(borderPaneMain, event.getScreenX(), event.getScreenY());
            }
        });
		
		this.stage.setResizable(false);
		Scene scene = new Scene(borderPaneMain);
//		scene.getStylesheets().add(getClass().getResource("./server/view/ServerStyle.css").toExternalForm());
		this.stage.sizeToScene();
		this.stage.setTitle("7 Wonders");
		this.stage.setScene(scene);
		this.stage.show();
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
	
	public void processNewGameItem(ActionEvent event) {
	//TODO	
	}
	
	public void processQuitItem(ActionEvent event) {
		Platform.exit();
	}
	
	public void processRulesItem(ActionEvent event) {
	//TODO	
	}
	
	public void processHintItem(ActionEvent event) {
	//TODO	
	}
	
	public void processAboutItem(ActionEvent event) {
	//TODO	
	}
}
