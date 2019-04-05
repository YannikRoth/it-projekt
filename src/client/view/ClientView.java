package client.view;

import client.model.ClientModel;
import javafx.application.Application;
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

/**
 * Test version of ClientView. 
 * TODO Main method has to be extracted
 * @author David Baumann
 *
 */

public class ClientView extends Application{
	
	private ClientModel model;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		BorderPane borderPaneMain = new BorderPane();
		
		//Table View opponents
		TableView tableOpponents = new TableView();
		tableOpponents.setEditable(false);
		borderPaneMain.setCenter(tableOpponents);
		
		TableColumn ColPlayer	= new TableColumn("Player");
		TableColumn ColStone	= new TableColumn("Stone");
		TableColumn ColOre		= new TableColumn("Ore");
		
		tableOpponents.getColumns().addAll(ColPlayer, ColStone, ColOre);
		
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
		hBoxCards.getChildren().addAll(card1);
		
		//Points
		TableView tablePoints = new TableView();
		tablePoints.setEditable(false);
		hBoxPlayer.getChildren().addAll(tablePoints);
		
		TableColumn ColType		= new TableColumn("Type");
		TableColumn ColAmount	= new TableColumn("Amount");
		
		tablePoints.getColumns().addAll(ColType, ColAmount);
		
		//Deck
		HBox hBoxDeck = new HBox();
		borderPanePlayer.setBottom(hBoxDeck);
		
		ImageView deck = new ImageView(new Image("https://www.theboardgamefamily.com/wp-content/uploads/2012/05/Ending-civilization-200x129.jpg"));
		deck.setFitHeight(250);
		deck.setFitWidth(600);
		
		hBoxDeck.getChildren().addAll(deck);
		
		
		//Menu
		MenuItem itemM1 = new MenuItem("Move one");
		MenuItem itemM2 = new MenuItem("Move two");
		MenuItem itemM3 = new MenuItem("Move three");
		Menu menuGame = new Menu("Game");
		menuGame.getItems().addAll(itemM1, itemM2, itemM3);
		
		MenuBar menuBar = new MenuBar(menuGame);
		borderPaneMain.setTop(menuBar);
		
		
		//DropDown Menu
		ContextMenu contextMenu = new ContextMenu(menuGame);
		borderPaneMain.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                contextMenu.show(borderPaneMain, event.getScreenX(), event.getScreenY());
            }
        });
		
		Scene scene = new Scene(borderPaneMain);
//		scene.getStylesheets().add(getClass().getResource("TTTStyle.css").toExternalForm());
		primaryStage.sizeToScene();
		primaryStage.setTitle("7 Wonders");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	//TODO -> above code needs to be changed to constructor mehtod!
	public ClientView(ClientModel model) {
		this.model = model;
	}
}
