package client.view;

import client.model.ClientModel;
import globals.Translator;
import javafx.application.Application;
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
import server.ServiceLocator;

/**
 * 
 * @author philipp
 *
 */

public class ClientView extends Application{
	private Stage stage;
	private ClientModel model;
	private Translator translator;
	
	public static void main(String[] args) {
		launch(args);
	}

	public ClientView(Stage primaryStage, ClientModel model) {
		this.stage = primaryStage;
		this.model = model;
		translator = ServiceLocator.getTranslator();
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
		TableColumn ColStone	= new TableColumn("Stone");
		TableColumn ColOre		= new TableColumn("Ore");
		TableColumn ColWood		= new TableColumn("Wood");
		TableColumn ColGlass	= new TableColumn("Glass");
		TableColumn ColClay		= new TableColumn("Clay");
		TableColumn ColLoom		= new TableColumn("Loom");
		TableColumn ColPaper	= new TableColumn("Paper");
		TableColumn ColCoin		= new TableColumn("Coin");
		TableColumn ColGeom 	= new TableColumn("Geom");
		TableColumn ColWrit 	= new TableColumn("Writ");
		TableColumn ColEng		= new TableColumn("Eng");
		TableColumn ColShield	= new TableColumn("Shield");
		TableColumn ColMilitary	= new TableColumn("Military");
		TableColumn ColWinning	= new TableColumn("Winning");
		
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
		ImageView card1 = new ImageView(new Image("https://cf.geekdo-images.com/camo/6fd08fe60edcfe435528f3f3b9c369cc198a1faf/687474703a2f2f636f72726f6a6575782e66696c65732e776f726470726573732e636f6d2f323031322f31322f7a332d74656d706c652d696e746333613972696575722e6a70673f773d363035"));
		card1.setFitHeight(150);
		card1.setFitWidth(100);
		
		ImageView card2 = new ImageView(new Image("https://cf.geekdo-images.com/camo/6fd08fe60edcfe435528f3f3b9c369cc198a1faf/687474703a2f2f636f72726f6a6575782e66696c65732e776f726470726573732e636f6d2f323031322f31322f7a332d74656d706c652d696e746333613972696575722e6a70673f773d363035"));
		card2.setFitHeight(150);
		card2.setFitWidth(100);
		
		ImageView card3 = new ImageView(new Image("https://cf.geekdo-images.com/camo/6fd08fe60edcfe435528f3f3b9c369cc198a1faf/687474703a2f2f636f72726f6a6575782e66696c65732e776f726470726573732e636f6d2f323031322f31322f7a332d74656d706c652d696e746333613972696575722e6a70673f773d363035"));
		card3.setFitHeight(150);
		card3.setFitWidth(100);
		
		ImageView card4 = new ImageView(new Image("https://cf.geekdo-images.com/camo/6fd08fe60edcfe435528f3f3b9c369cc198a1faf/687474703a2f2f636f72726f6a6575782e66696c65732e776f726470726573732e636f6d2f323031322f31322f7a332d74656d706c652d696e746333613972696575722e6a70673f773d363035"));
		card4.setFitHeight(150);
		card4.setFitWidth(100);
		
		ImageView card5 = new ImageView(new Image("https://cf.geekdo-images.com/camo/6fd08fe60edcfe435528f3f3b9c369cc198a1faf/687474703a2f2f636f72726f6a6575782e66696c65732e776f726470726573732e636f6d2f323031322f31322f7a332d74656d706c652d696e746333613972696575722e6a70673f773d363035"));
		card5.setFitHeight(150);
		card5.setFitWidth(100);
		
		ImageView card6 = new ImageView(new Image("https://cf.geekdo-images.com/camo/6fd08fe60edcfe435528f3f3b9c369cc198a1faf/687474703a2f2f636f72726f6a6575782e66696c65732e776f726470726573732e636f6d2f323031322f31322f7a332d74656d706c652d696e746333613972696575722e6a70673f773d363035"));
		card6.setFitHeight(150);
		card6.setFitWidth(100);
		
		ImageView card7 = new ImageView(new Image("https://cf.geekdo-images.com/camo/6fd08fe60edcfe435528f3f3b9c369cc198a1faf/687474703a2f2f636f72726f6a6575782e66696c65732e776f726470726573732e636f6d2f323031322f31322f7a332d74656d706c652d696e746333613972696575722e6a70673f773d363035"));
		card7.setFitHeight(150);
		card7.setFitWidth(100);
		
		hBoxCards.getChildren().addAll(card1, card2, card3, card4, card5, card6, card7);
		
		//Points
		TableView tablePoints = new TableView();
		tablePoints.setEditable(false);
		hBoxPlayer.getChildren().addAll(tablePoints);
		
		TableColumn ColType		= new TableColumn("Type");
		ColType.setMinWidth(150);
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
		MenuItem itemM9 = new MenuItem("Rules");
		MenuItem itemM10 = new MenuItem("Hint");
		MenuItem itemM11 = new MenuItem("About");
		MenuItem itemM12 = new MenuItem("Quit");
		itemM12.setOnAction(this::processQuitItem);
		Menu menuHelp = new Menu("Help");
		menuHelp.getItems().addAll(itemM8, itemM9, itemM10, itemM11, itemM12);
		
		MenuBar menuBar = new MenuBar(menuGame, menuHelp);
		borderPaneMain.setTop(menuBar);
		
		//DropDown Menu
		ContextMenu contextMenu = new ContextMenu(menuGame, menuHelp);
		borderPaneMain.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                contextMenu.show(borderPaneMain, event.getScreenX(), event.getScreenY());
            }
        });
		
		Scene scene = new Scene(borderPaneMain);
//		scene.getStylesheets().add(getClass().getResource("./server/view/ServerStyle.css").toExternalForm());
		this.stage.sizeToScene();
		this.stage.setTitle("7 Wonders");
		this.stage.setScene(scene);
		this.stage.show();
	}

	public void start(Stage stage) {
		stage.show();
	}
	public void stop() {
		stage.hide();
	}
	public Stage getStage() {
		return this.stage;
	}
	public void processQuitItem(ActionEvent event) {
		Platform.exit();
	}
}
