package client.view;

import java.util.ArrayList;
import java.util.Map.Entry;

import client.model.ClientModel;
import globals.ResourceType;
import globals.Translator;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import server.model.gameplay.Player;
import server.model.gameplay.ResourceMap;

/**
 * 
 * @author philipp
 *
 */

public class ClientView {
	private Stage stage;
	private ClientModel model;
	private Translator translator = Translator.getTranslator();
	Menu menuLanguage, menuHelp, menuGame;
	
	TableColumn<Player, String> ColPlayer, ColStone, ColOre, ColWood, ColGlass, ColClay, ColLoom, ColPaper, ColCoin, ColGeom, ColWrit, ColEng, ColShield, ColMilitary, ColWinning;
	TableColumn<ResourceType, String> ColType;
	TableColumn<ResourceType, Integer> ColAmount;
	
	MenuItem itemM1, itemM2, itemM3, itemM4, itemM5, itemM6, itemM7, itemM8, itemM9, itemM10, itemM11, itemM12, itemM13;
	

	public ClientView(Stage primaryStage, ClientModel model) {
		this.stage = primaryStage;
		this.model = model;
		model.start();
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
		
		BorderPane borderPaneMain = new BorderPane();
		
		//Table View opponents
		TableView<Player> tableOpponents = new TableView();
		tableOpponents.setEditable(false);
		tableOpponents.setItems(model.getOtherPlayers());
		borderPaneMain.setCenter(tableOpponents);
		
		ColPlayer = new TableColumn<Player, String>("Player");
		ColPlayer.setMinWidth(100);
		ColPlayer.setCellValueFactory(new PropertyValueFactory<Player, String>("playerName"));
		ColStone = new TableColumn("Stone");
		ColStone.setMinWidth(100);
		ColOre = new TableColumn("Ore");
		ColOre.setMinWidth(100);
		ColWood = new TableColumn("Wood");
		ColWood.setMinWidth(100);
		ColGlass = new TableColumn("Glass");
		ColGlass.setMinWidth(100);
		ColClay = new TableColumn("Clay");
		ColClay.setMinWidth(100);
		ColLoom = new TableColumn("Loom");
		ColLoom.setMinWidth(100);
		ColPaper = new TableColumn("Paper");
		ColPaper.setMinWidth(100);
		ColCoin = new TableColumn("Coin");
		ColCoin.setMinWidth(100);
		ColGeom = new TableColumn("Geom");
		ColGeom.setMinWidth(100);
		ColWrit = new TableColumn("Writ");
		ColWrit.setMinWidth(100);
		ColEng = new TableColumn("Eng");
		ColEng.setMinWidth(100);
		ColShield = new TableColumn("Shield");
		ColShield.setMinWidth(100);
		ColMilitary = new TableColumn("Military");
		ColMilitary.setMinWidth(100);
		ColWinning = new TableColumn("Winning");
		ColWinning.setMinWidth(100);
		
		tableOpponents.getColumns().addAll(ColPlayer, ColStone, ColOre, ColWood, ColGlass, ColClay, ColLoom, ColPaper, ColCoin, ColGeom, ColWrit, ColEng, ColShield, ColMilitary, ColWinning);
		
		//Player Deck
		HBox hBoxPlayer = new HBox();
		borderPaneMain.setBottom(hBoxPlayer);
		
		BorderPane borderPanePlayer = new BorderPane();
		hBoxPlayer.getChildren().addAll(borderPanePlayer);
		
		HBox hBoxCards = new HBox();
		hBoxCards.setSpacing(10);
		hBoxCards.setPadding(new Insets(15,12,15,50));
		borderPanePlayer.setTop(hBoxCards);
		hBoxPlayer.setHgrow(borderPanePlayer, Priority.ALWAYS);
		
		//Cards
		Image image = new Image("file:./resource/images/cards/SCN_0150.jpg");
		Image image2 = new Image("file:./resource/images/cards/SCN_0151.jpg");
		
		ImageView card1 = new ImageView(image);
		card1.setFitHeight(130);
		card1.setFitWidth(86);
		card1.setOnMouseClicked((e) -> {
			System.out.println("Karte 1");
		});
		
		ImageView card2 = new ImageView(image);
		card2.setFitHeight(130);
		card2.setFitWidth(86);
		card2.setOnMouseClicked((e) -> {
			System.out.println("Karte 2");
		});
		
		ImageView card3 = new ImageView(image);
		card3.setFitHeight(130);
		card3.setFitWidth(86);
		card3.setOnMouseClicked((e) -> {
			System.out.println("Karte 3");
		});
		
		ImageView card4 = new ImageView(image);
		card4.setFitHeight(130);
		card4.setFitWidth(86);
		card4.setOnMouseClicked((e) -> {
			System.out.println("Karte 4");
		});
		
		ImageView card5 = new ImageView(image);
		card5.setFitHeight(130);
		card5.setFitWidth(86);
		card5.setOnMouseClicked((e) -> {
			System.out.println("Karte 5");
		});
		
		ImageView card6 = new ImageView(image);
		card6.setFitHeight(130);
		card6.setFitWidth(86);
		card6.setOnMouseClicked((e) -> {
			System.out.println("Karte 6");
		});
		
		ImageView card7 = new ImageView(image2);
		card7.setFitHeight(130);
		card7.setFitWidth(86);
		card7.setOnMouseClicked((e) -> {
			System.out.println("Karte 7");
		});
		
		ImageView card8 = new ImageView(image2);
		card8.setFitHeight(130);
		card8.setFitWidth(86);
		card8.setOnMouseClicked((e) -> {
			System.out.println("Karte 8");
		});
		
		ImageView card9 = new ImageView(image2);
		card9.setFitHeight(130);
		card9.setFitWidth(86);
		card9.setOnMouseClicked((e) -> {
			System.out.println("Karte 9");
		});
		
		ImageView card10 = new ImageView(image2);
		card10.setFitHeight(130);
		card10.setFitWidth(86);
		card10.setOnMouseClicked((e) -> {
			System.out.println("Karte 10");
		});
		
		ImageView card11 = new ImageView(image2);
		card11.setFitHeight(130);
		card11.setFitWidth(86);
		card11.setOnMouseClicked((e) -> {
			System.out.println("Karte 11");
		});
		
		ImageView card12 = new ImageView(image2);
		card12.setFitHeight(130);
		card12.setFitWidth(86);
		card12.setOnMouseClicked((e) -> {
			System.out.println("Karte 12");
		});
		
		hBoxCards.getChildren().addAll(card1, card2, card3, card4, card5, card6, card7, card8, card9, card10, card11, card12);
		
		
		//Points
		/**
		 * @author david
		 */
		TableView<ResourceType> tablePoints = new TableView<>(this.model.getMyPlayer().getResources().getResourcesListObservable());
		
		tablePoints.setEditable(false);
		hBoxPlayer.getChildren().addAll(tablePoints);
		
		ColType		= new TableColumn();
		ColType.setMinWidth(100);
		/**
		 * @author david
		 */
		ColType.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().toStringTranslate() ));

		ColAmount	= new TableColumn();
		ColAmount.setMinWidth(100);
		/**
		 * @author david
		 */
		ColAmount.setCellValueFactory(cd -> Bindings.valueAt(this.model.getMyPlayer().getResources().getResourcesObservable(), cd.getValue()));
		
		tablePoints.getColumns().addAll(ColType, ColAmount);
		
		//Deck
		HBox hBoxDeck = new HBox();
		hBoxDeck.setPadding(new Insets(15,12,15,390));
		//hBoxDeck.setSpacing(10);
		borderPanePlayer.setCenter(hBoxDeck);
		
		ImageView card13 = new ImageView(image2);
		card13.setFitHeight(100);
		card13.setFitWidth(66);
		card13.setOnMouseClicked((e) -> {
			System.out.println("Karte 13");
		});
		
		ImageView card14 = new ImageView(image2);
		card14.setFitHeight(100);
		card14.setFitWidth(66);
		card14.setOnMouseClicked((e) -> {
			System.out.println("Karte 14");
		});
		
		ImageView card15 = new ImageView(image2);
		card15.setFitHeight(100);
		card15.setFitWidth(66);
		card15.setOnMouseClicked((e) -> {
			System.out.println("Karte 15");
		});
		
		
		HBox hBoxWorldWonderCards = new HBox();
		hBoxWorldWonderCards.setSpacing(120);
		hBoxWorldWonderCards.setPadding(new Insets(15,12,15,475));
		hBoxWorldWonderCards.getChildren().addAll(card13, card14, card15);
		borderPanePlayer.setBottom(hBoxWorldWonderCards);
		
		
		ImageView deck = new ImageView(new Image("file:./resource/images/boards/Board_01_A.jpg"));
		deck.setFitHeight(250);
		deck.setFitWidth(600);
		
		hBoxDeck.getChildren().addAll(deck);
		
		//Menu "Game"
		itemM1 = new MenuItem();
		itemM2 = new MenuItem();
		itemM3 = new MenuItem();
		itemM4 = new MenuItem();
		itemM5 = new MenuItem();
		itemM6 = new MenuItem();
		itemM7 = new MenuItem();
		menuGame = new Menu();
		menuGame.getItems().addAll(itemM1, itemM2, itemM3, itemM4, itemM5, itemM6, itemM7);
		
		//Menu "Help"
		itemM8 = new MenuItem();
		itemM9 = new MenuItem();
		itemM10 = new MenuItem();
		itemM11 = new MenuItem();
		menuHelp = new Menu();
		menuHelp.getItems().addAll(itemM8, itemM9, itemM10, itemM11);
		
		//Menu "Language"
		itemM12 = new MenuItem();
		itemM13 = new MenuItem();
		menuLanguage = new Menu();
		menuLanguage.getItems().addAll(itemM12, itemM13);
		
		
		MenuBar menuBar = new MenuBar(menuHelp, menuLanguage);
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
		ColPlayer.setText(translator.getString("column.player"));
		ColStone.setText(translator.getString("column.stone"));
		ColOre.setText(translator.getString("column.ore"));
		ColWood.setText(translator.getString("column.wood"));
		ColGlass.setText(translator.getString("column.glas"));
		ColClay.setText(translator.getString("column.brick"));
		ColLoom.setText(translator.getString("column.loom"));
		ColPaper.setText(translator.getString("column.papyrus"));
		ColCoin.setText(translator.getString("column.coin"));
		ColGeom.setText(translator.getString("column.geom"));
		ColWrit.setText(translator.getString("column.writ"));
		ColEng.setText(translator.getString("column.fabric"));
		ColShield.setText(translator.getString("column.shield"));
		ColMilitary.setText(translator.getString("column.military"));
		ColWinning.setText(translator.getString("column.winning"));
		
		ColType.setText(translator.getString("column.type"));
		ColAmount.setText(translator.getString("column.amount"));
		
		itemM1.setText(translator.getString("item.one"));
		itemM2.setText(translator.getString("item.two"));
		itemM3.setText(translator.getString("item.three"));
		itemM4.setText(translator.getString("item.four"));
		itemM5.setText(translator.getString("item.five"));
		itemM6.setText(translator.getString("item.six"));
		itemM7.setText(translator.getString("item.seven"));
		itemM8.setText(translator.getString("item.rules"));
		itemM9.setText(translator.getString("item.hint"));
		itemM10.setText(translator.getString("item.about"));
		itemM11.setText(translator.getString("item.quit"));
		itemM12.setText(this.getLanguageDescription("language.german"));
		itemM13.setText(this.getLanguageDescription("language.english"));
		
		menuHelp.setText(translator.getString("menu.help"));
		menuGame.setText(translator.getString("menu.game"));
		menuLanguage.setText(translator.getString("menu.language"));
		
		stage.setTitle(translator.getString("clientGame.name"));

		//TODO fix translations
		/*
		this.model.getMyPlayer().getResources().getResourcesObservable().clear();
		for(Entry<ResourceType, Integer> entry : this.model.getMyPlayer().getResources().entrySet()) {
			ResourceType t = entry.getKey();
			Integer v = entry.getValue();
			this.model.getMyPlayer().getResources().getResourcesObservable().put(t, v);
		}*/
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
	
	public Menu getMenuLanguage() {
		return this.menuLanguage;
	}
	
	public MenuItem getRulesItem() {
		return this.itemM8;
	}
	
	public MenuItem getHintItem() {
		return this.itemM9;
	}
	
	public MenuItem getAboutItem() {
		return this.itemM10;
	}
	
	public MenuItem getQuitItem() {
		return this.itemM11;
	}
	
	public MenuItem getGermanItem() {
		return this.itemM12;
	}
	
	public MenuItem getEnglishItem() {
		return this.itemM13;
	}
}
