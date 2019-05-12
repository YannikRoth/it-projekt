package client.view;

import java.util.HashMap;
import java.util.Map;

import client.model.ClientModel;
import globals.ResourceType;
import globals.Translator;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import server.model.gameplay.Card;
import server.model.gameplay.Player;

/**
 * 
 * @author philipp
 *
 */

public class ClientView {
	private Stage stage;
	private ClientModel model;
	private Translator translator = Translator.getTranslator();
	private Menu menuLanguage, menuHelp, menuGame;
	protected ImageView[] cards = new ImageView[7];
	protected ImageView[] cards2 = new ImageView[12];
	private Map<ImageView, Card> cardWithImages = new HashMap<>();
	private HBox playableCards;
	
	protected ImageView card1, card2, card3, card4, card5, card6, card7, card8, card9, card10, card11, card12, card13, card14, card15, card16, cardPlayable1, cardPlayable2, cardPlayable3, cardPlayable4, cardPlayable5, cardPlayable6, cardPlayable7, selectedCard = null;
	
	TableView<Player> tableOpponents = new TableView<>();
	TableColumn<Player, String> colPlayer;
	ObservableList<TableColumn<Player, String>> dynamicCols = FXCollections.observableArrayList();
	
	TableView<ResourceType> tablePoints = new TableView<>();
	TableColumn<ResourceType, String> ColType;
	TableColumn<ResourceType, Integer> ColAmount;
	
	MenuItem itemM1, itemM2, itemM3, itemM4, itemM5, itemM6, itemM7, itemM8, itemM9, itemM10, itemM11, itemM12, itemM13;
	private Button playCard;
	private Button buildWorldWonder;
	private Button discardCard;
	

	public ClientView(Stage primaryStage, ClientModel model) {
		this.stage = primaryStage;
		this.model = model;
		model.start();
		buildView();
		setTexts();
	}	
	
	
	/**
	 * Build TableView for other players
	 * @author david
	 */
	private void buildTableOpponents() {
		tableOpponents.setEditable(false);
		tableOpponents.setItems(model.getOtherPlayers());
		
		colPlayer = new TableColumn<Player, String>();
		colPlayer.setMinWidth(100);
		colPlayer.setCellValueFactory(new PropertyValueFactory<Player, String>("playerName"));
		tableOpponents.getColumns().add(colPlayer);
		
        Callback<TableColumn.CellDataFeatures<Player, String>, ObservableValue<String>> callBack = 
                new Callback<TableColumn.CellDataFeatures<Player, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Player, String> param) {
                return param.getValue().getResources().containsKey(
                        param.getTableColumn().getUserData())
                        ? new SimpleStringProperty(Integer.toString(param.getValue().getResources().get(
                        		param.getTableColumn().getUserData())))
                        :new SimpleStringProperty("");
            }
        };
        for (ResourceType t : ResourceType.values()) {
        	TableColumn<Player, String> tmpCol = new TableColumn<>(t.toStringTranslate());
        	tmpCol.setUserData(t);
        	tmpCol.setCellValueFactory(callBack);
        	dynamicCols.add(tmpCol);
		}
        tableOpponents.getColumns().addAll(dynamicCols);
        tableOpponents.setMaxHeight(200);
	}
	
	/**
	 * Build table view for my points
	 * @author david
	 */
	private void buildTablePoints() {
		tablePoints = new TableView<>(this.model.getMyPlayer().getResources().getResourcesListObservable());
		tablePoints.setEditable(false);
		
		ColType		= new TableColumn<>();
		ColType.setMinWidth(100);
		ColType.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().toStringTranslate() ));

		ColAmount	= new TableColumn<>();
		ColAmount.setMinWidth(100);
		ColAmount.setCellValueFactory(cd -> Bindings.valueAt(this.model.getMyPlayer().getResources().getResourcesObservable(), cd.getValue()));
		
		tablePoints.getColumns().addAll(ColType, ColAmount);
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
		buildTableOpponents();
		borderPaneMain.setCenter(tableOpponents);
		
		//Player Deck
		HBox hBoxPlayer = new HBox();
		borderPaneMain.setBottom(hBoxPlayer);
		
		BorderPane borderPanePlayer = new BorderPane();
		hBoxPlayer.getChildren().addAll(borderPanePlayer);
		
		HBox hBoxCards = new HBox();
		hBoxCards.setSpacing(10);
		hBoxCards.setPadding(new Insets(0,0,0,0));
		borderPanePlayer.setTop(hBoxCards);
		hBoxPlayer.setHgrow(borderPanePlayer, Priority.ALWAYS);
		
		//Cards
		Image image = new Image("file:./resource/images/cards/SCN_0150.jpg");
		Image image2 = new Image("file:./resource/images/cards/SCN_0151.jpg");
//		Image image3 = new Image ("file:./resource/images/cards/"+model.getMyPlayer().getPlayedCards().get(0).getImageFileName());
		Image tempImage = new Image("file:./resource/images/cards/SCN_0150.jpg");
		
		cardPlayable1 = new ImageView(tempImage);
		cardPlayable1.setFitHeight(200);
		cardPlayable1.setFitWidth(133);
		cards[0] = cardPlayable1;
		//cardWithImages.put(cards[0], model.getMyPlayer().getPlayableCards().get(0));
		
		cardPlayable2 = new ImageView(tempImage);
		cardPlayable2.setFitHeight(200);
		cardPlayable2.setFitWidth(133);
		cards[1] = cardPlayable2;
		//cardWithImages.put(cards[1], model.getMyPlayer().getPlayableCards().get(1));
		
		cardPlayable3 = new ImageView(tempImage);
		cardPlayable3.setFitHeight(200);
		cardPlayable3.setFitWidth(133);
		cards[2] = cardPlayable3;
		//cardWithImages.put(cards[2], model.getMyPlayer().getPlayableCards().get(2));
		
		cardPlayable4 = new ImageView(tempImage);
		cardPlayable4.setFitHeight(200);
		cardPlayable4.setFitWidth(133);
		cards[3] = cardPlayable4;
		//cardWithImages.put(cards[3], model.getMyPlayer().getPlayableCards().get(3));
		
		cardPlayable5 = new ImageView(tempImage);
		cardPlayable5.setFitHeight(200);
		cardPlayable5.setFitWidth(133);
		cards[4] = cardPlayable5;
		//cardWithImages.put(cards[4], model.getMyPlayer().getPlayableCards().get(4));
		
		cardPlayable6 = new ImageView(tempImage);
		cardPlayable6.setFitHeight(200);
		cardPlayable6.setFitWidth(133);
		cards[5] = cardPlayable6;
		//cardWithImages.put(cards[5], model.getMyPlayer().getPlayableCards().get(5));
		
		cardPlayable7 = new ImageView(tempImage);
		cardPlayable7.setFitHeight(200);
		cardPlayable7.setFitWidth(133);
		cards[6] = cardPlayable7;
		//cardWithImages.put(cards[6], model.getMyPlayer().getPlayableCards().get(6));
		
		card1 = new ImageView(image);
		card1.setFitHeight(130);
		card1.setFitWidth(86);
		cards2[0] = card1;
		card1.setOnMouseClicked((e) -> {
			System.out.println("Karte 1");
			updatePlayedCardView(card1, 0);

			if(card1 != this.selectedCard) {
				card1.setEffect(new DropShadow(5, Color.RED));
				card2.setEffect(null);
				selectedCard = card1;
				}

			
//			cardPlayable1.setImage(new Image("file:./resource/images/cards/"+model.getMyPlayer().getPlayedCards().get(0).getImageFileName()));
		});
		
		card2 = new ImageView(image);
		card2.setFitHeight(130);
		card2.setFitWidth(86);
		cards2[1] = card2;
		card2.setOnMouseClicked((e) -> {
			System.out.println("Karte 2");
			updatePlayedCardView(card2, 1);
			
			if(card2 != this.selectedCard) {
				card2.setEffect(new DropShadow(5, Color.RED));
				card1.setEffect(null);
				selectedCard = card2;
				}
			
//			cardPlayable1.setImage(new Image("file:./resource/images/cards/"+model.getMyPlayer().getPlayedCards().get(0).getImageFileName()));
		});
		
		card3 = new ImageView(image);
		card3.setFitHeight(130);
		card3.setFitWidth(86);
		cards2[2] = card3;
		card3.setOnMouseClicked((e) -> {
			System.out.println("Karte 3");
			updatePlayedCardView(card3, 2);
			
//			cardPlayable1.setImage(new Image("file:./resource/images/cards/"+model.getMyPlayer().getPlayedCards().get(0).getImageFileName()));
		});
		
		card4 = new ImageView(image);
		card4.setFitHeight(130);
		card4.setFitWidth(86);
		cards2[3] = card4;
		card4.setOnMouseClicked((e) -> {
			System.out.println("Karte 4");
			updatePlayedCardView(card4, 3);
			
//			cardPlayable1.setImage(new Image("file:./resource/images/cards/"+model.getMyPlayer().getPlayedCards().get(0).getImageFileName()));
		});
		
		card5 = new ImageView(image);
		card5.setFitHeight(130);
		card5.setFitWidth(86);
		cards2[4] = card5;
		card5.setOnMouseClicked((e) -> {
			System.out.println("Karte 5");
			updatePlayedCardView(card5, 4);
			
//			cardPlayable1.setImage(new Image("file:./resource/images/cards/"+model.getMyPlayer().getPlayedCards().get(0).getImageFileName()));
		});
		
		card6 = new ImageView(image);
		card6.setFitHeight(130);
		card6.setFitWidth(86);
		cards2[5] = card6;
		card6.setOnMouseClicked((e) -> {
			System.out.println("Karte 6");
			updatePlayedCardView(card6, 5);
			
//			cardPlayable1.setImage(new Image("file:./resource/images/cards/"+model.getMyPlayer().getPlayedCards().get(0).getImageFileName()));
		});
		
		card7 = new ImageView(image2);
		card7.setFitHeight(130);
		card7.setFitWidth(86);
		cards2[6] = card7;
		card7.setOnMouseClicked((e) -> {
			System.out.println("Karte 7");
			updatePlayedCardView(card7, 6);
			
//			cardPlayable1.setImage(new Image("file:./resource/images/cards/"+model.getMyPlayer().getPlayedCards().get(0).getImageFileName()));
		});
		
		card8 = new ImageView(image2);
		card8.setFitHeight(130);
		card8.setFitWidth(86);
		cards2[7] = card8;
		card8.setOnMouseClicked((e) -> {
			System.out.println("Karte 8");
			updatePlayedCardView(card8, 7);
			
//			cardPlayable1.setImage(new Image("file:./resource/images/cards/"+model.getMyPlayer().getPlayedCards().get(0).getImageFileName()));
		});
		
		card9 = new ImageView(image2);
		card9.setFitHeight(130);
		card9.setFitWidth(86);
		cards2[8] = card9;
		card9.setOnMouseClicked((e) -> {
			System.out.println("Karte 9");
			updatePlayedCardView(card9, 8);
			
//			cardPlayable1.setImage(new Image("file:./resource/images/cards/"+model.getMyPlayer().getPlayedCards().get(0).getImageFileName()));
		});
		
		card10 = new ImageView(image2);
		card10.setFitHeight(130);
		card10.setFitWidth(86);
		cards2[9] = card10;
		card10.setOnMouseClicked((e) -> {
			System.out.println("Karte 10");
			updatePlayedCardView(card10, 9);
			
//			cardPlayable1.setImage(new Image("file:./resource/images/cards/"+model.getMyPlayer().getPlayedCards().get(0).getImageFileName()));
		});
		
		card11 = new ImageView(image2);
		card11.setFitHeight(130);
		card11.setFitWidth(86);
		cards2[10] = card11;
		card11.setOnMouseClicked((e) -> {
			System.out.println("Karte 11");
			updatePlayedCardView(card11, 10);
			
//			cardPlayable1.setImage(new Image("file:./resource/images/cards/"+model.getMyPlayer().getPlayedCards().get(0).getImageFileName()));
		});
		
		card12 = new ImageView(image2);
		card12.setFitHeight(130);
		card12.setFitWidth(86);
		cards2[11] = card12;
		card12.setOnMouseClicked((e) -> {
			System.out.println("Karte 12");
			updatePlayedCardView(card12, 11);
			
//			cardPlayable1.setImage(new Image("file:./resource/images/cards/"+model.getMyPlayer().getPlayedCards().get(0).getImageFileName()));
		});
		
		hBoxCards.getChildren().addAll(card1, card2, card3, card4, card5, card6, card7, card8, card9, card10, card11, card12);
		
		
		//Points
		buildTablePoints();
		hBoxPlayer.getChildren().addAll(tablePoints);
		
		//Deck
		VBox hBoxDeck = new VBox();
		hBoxDeck.setPadding(new Insets(55,0,15, 55));
		hBoxDeck.setSpacing(10);
		borderPanePlayer.setCenter(hBoxDeck);
		
		this.playCard = new Button("Karte spielen");
		this.buildWorldWonder = new Button("Weltwunder bauen");
		this.discardCard = new Button("Karte ablegen");
		HBox buttonBox = new HBox();
		buttonBox.getChildren().addAll(playCard, buildWorldWonder, discardCard);

		
		playCard.setMaxWidth(200);
		buildWorldWonder.setMaxWidth(200);
		discardCard.setMaxWidth(200);
		
		playCard.setDisable(true);
		buildWorldWonder.setDisable(true);
		discardCard.setDisable(true);
		
		//disable Buttons as they get only activated if action is possible
		playCard.setDisable(true);
		buildWorldWonder.setDisable(true);
		discardCard.setDisable(true);
		buttonBox.setSpacing(10);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setPadding(new Insets(0,0,10,0));
		
		card13 = new ImageView(image2);
		card13.setFitHeight(100);
		card13.setFitWidth(66);
		card13.setOnMouseClicked((e) -> {
			System.out.println("Karte 13");
		});
		
		card14 = new ImageView(image2);
		card14.setFitHeight(100);
		card14.setFitWidth(66);
		card14.setOnMouseClicked((e) -> {
			System.out.println("Karte 14");
		});
		
		card15 = new ImageView(image2);
		card15.setFitHeight(100);
		card15.setFitWidth(66);
		card15.setOnMouseClicked((e) -> {
			System.out.println("Karte 15");
		});
		
		
		HBox hBoxWorldWonderCards = new HBox();
		hBoxWorldWonderCards.setSpacing(120);
		hBoxWorldWonderCards.setPadding(new Insets(0,0,0,500));
		hBoxWorldWonderCards.getChildren().addAll(card13, card14, card15);
		borderPanePlayer.setBottom(hBoxWorldWonderCards);
		
		
//		ImageView deck = new ImageView(new Image("file:./resource/images/boards/Board_06_A.jpg"));
//		deck.setFitHeight(250);
//		deck.setFitWidth(600);

		playableCards = new HBox();
		playableCards.getChildren().addAll(cardPlayable1, cardPlayable2, cardPlayable3, cardPlayable4, cardPlayable5, cardPlayable6, cardPlayable7);
				
		hBoxDeck.getChildren().addAll(playableCards, buttonBox);
		playableCards.setAlignment(Pos.BASELINE_CENTER);
		playableCards.setPadding(new Insets(30, 0, 0, 0));
		playableCards.setSpacing(10);
		playableCards.setPrefHeight(230);
		
		cardPlayable1.setOnMouseClicked((e) -> {
			System.out.println("Karte kann gespielt werden");
			updatePlayableCardView();
			
//			cardPlayable1.setImage(new Image("file:./resource/images/cards/"+model.getMyPlayer().getPlayedCards().get(0).getImageFileName()));
		});
		
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
	
	public void updatePlayedCardView(ImageView v, int i) {
		if(i >= model.getMyPlayer().getPlayedCards().size()) {
			//do nothing
		}else {
			v.setImage(new Image("file:./resource/images/cards/"+model.getMyPlayer().getPlayedCards().get(i).getImageFileName()));
		}
		
	}
	
	public void updatePlayedCardView() {
		System.out.println(model.getMyPlayer().getPlayedCards());
		for(int i = 0; i < model.getMyPlayer().getPlayedCards().size(); i++) {
			this.cards2[i].setImage(new Image("file:./resource/images/cards/"+model.getMyPlayer().getPlayedCards().get(i).getImageFileName()));
		}
	}
	
//	protected void updatePlayableCardView() {
//		System.out.println(model.getMyPlayer().getPlayedCards());
//		for(int i = 0; i < model.getMyPlayer().getPlayedCards().size(); i++) {
//			this.cards2[i].setImage(new Image("file:./resource/images/cards/"+model.getMyPlayer().getPlayedCards().get(i).getImageFileName()));
//		}
//	}
	
	
	
	public void updatePlayableCardView() {
		this.playableCards.getChildren().clear();
		cards = new ImageView[model.getMyPlayer().getPlayableCards().size()];
		System.out.println(model.getMyPlayer().getPlayableCards());
		for (int i = 0; i < model.getMyPlayer().getPlayableCards().size(); i++) {
			cards[i] = new ImageView(new Image("file:./resource/images/cards/"+model.getMyPlayer().getPlayableCards().get(i).getImageFileName()));
			cards[i].setFitHeight(200);
			cards[i].setFitWidth(133);
			this.playableCards.getChildren().add(cards[i]);
			cardWithImages.put(cards[i], model.getMyPlayer().getPlayableCards().get(i));
		}
	}
	
	public void disableCards() {
		this.playableCards.getChildren().clear();
	}
	
	private String getLanguageDescription(String identifier) {
		if(Translator.getDefaultLocale().getLanguage().substring(0, 2).equalsIgnoreCase(translator.getString(identifier).substring(0, 2)))
			return translator.getString(identifier) + " " + translator.getString("language.default");
		return translator.getString(identifier);
	}
	
	public void setTexts() {
		//Table opponents translations
		colPlayer.setText(translator.getString("column.player"));
		dynamicCols.forEach(c -> c.setText(((ResourceType)c.getUserData()).toStringTranslate()));
		
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

		//TODO fix translations in right bottom corner
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
	
	public ImageView getImageView (int i) {
		return cards[i];
	}
	
	public ImageView[] getShownCards() {
		return this.cards;
	}
	
	public Button getPlayCardButton() {
		return this.playCard;
	}
	
	public Button getBuildWorldWonderButton() {
		return this.buildWorldWonder;
	}
	
	public Button getDiscardCardButton() {
		return this.discardCard;
	}
	
	public Map<ImageView, Card> getCardsWithImages(){
		return this.cardWithImages;
	}
	
}
