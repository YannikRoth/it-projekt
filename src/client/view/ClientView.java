package client.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import client.ServicelocatorClient;
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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	private HBox playableCards, hBoxCards, hBoxWorldWonderCards;
	
	protected ImageView card1, card2, card3, card4, card5, card6, card7, card8, card9, card10, card11, card12, card13, card14, card15, card16, cardPlayable1, cardPlayable2, cardPlayable3, cardPlayable4, cardPlayable5, cardPlayable6, cardPlayable7, selectedCard = null;
	
	private TableView<Player> tableOpponents = new TableView<>();
	private TableColumn<Player, String> colPlayer;
	private ObservableList<TableColumn<Player, String>> dynamicCols = FXCollections.observableArrayList();
	
	private TableView<ResourceType> tablePoints = new TableView<>();
	private TableColumn<ResourceType, String> ColType;
	private TableColumn<ResourceType, Integer> ColAmount;
	
	MenuItem itemM1, itemM2, itemM3, itemM4, itemM5, itemM6, itemM7, itemM8, itemM9, itemM10, itemM11, itemM12, itemM13, itemM14;
	private Button playCard, buildWorldWonder, discardCard, okButton;
	private Label playerName;
	
	public ClientView(Stage primaryStage, ClientModel model) {
		this.stage = primaryStage;
		this.model = model;
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
		colPlayer.setPrefWidth(260);
		colPlayer.setCellValueFactory(new PropertyValueFactory<Player, String>("playerName"));
		tableOpponents.getColumns().add(colPlayer);
		
		/**
		 * Idea from https://stackoverflow.com/questions/21639108/javafx-tableview-objects-with-maps
		 */
        Callback<TableColumn.CellDataFeatures<Player, String>, ObservableValue<String>> callBack = 
                new Callback<TableColumn.CellDataFeatures<Player, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Player, String> param) {
                return param.getValue().getResources().containsKey(
                        param.getTableColumn().getUserData())
                        ? new SimpleStringProperty(Integer.toString(param.getValue().getResources().get(
                        		param.getTableColumn().getUserData())))
                        :new SimpleStringProperty("-");
            }
        };
        for (ResourceType t : ResourceType.values()) {
        	TableColumn<Player, String> tmpCol = new TableColumn<>(t.toStringTranslate());
        	tmpCol.setUserData(t);
        	tmpCol.setCellValueFactory(callBack);
        	dynamicCols.add(tmpCol);
        	tmpCol.setPrefWidth(100);
        	tmpCol.setStyle("-fx-alignment: CENTER");
		}
        tableOpponents.getColumns().addAll(dynamicCols);
        tableOpponents.setPrefHeight(200);
	}
	
	/**
	 * Build table view for my points
	 * @author david
	 */
	private void buildTablePoints() {
		tablePoints = new TableView<>();
//		if(this.model.getMyPlayer() != null &&
//				this.model.getMyPlayer().getResources() != null &&
//				this.model.getMyPlayer().getResources().getResourcesListObservable() != null)
//			tablePoints.setItems(this.model.getMyPlayer().getResources().getResourcesListObservable());
		tablePoints.setEditable(false);
		
		ColType		= new TableColumn<>();
		ColType.setMinWidth(130);
		ColType.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().toStringTranslate() ));

		ColAmount	= new TableColumn<>();
		ColAmount.setMinWidth(130);
		ColAmount.setStyle("-fx-alignment: CENTER");
		ColAmount.setCellValueFactory(cd -> Bindings.valueAt(this.model.getMyPlayer().getResources().getResourcesObservable(), cd.getValue()));
		
		tablePoints.getColumns().addAll(ColType, ColAmount);
	}
	
	public void buildView() {
		BorderPane borderPaneMain = new BorderPane();
		
		//Table View opponents
		buildTableOpponents();
		borderPaneMain.setCenter(tableOpponents);
		
		//Player Deck
		VBox vBoxPlayer = new VBox();
		HBox hBoxPlayer = new HBox();
		borderPaneMain.setBottom(vBoxPlayer);
		
		BorderPane borderPanePlayer = new BorderPane();
		
		this.hBoxCards = new HBox();
		hBoxCards.setPadding(new Insets(0,30,0,230));
		hBoxCards.setSpacing(-10);
		hBoxCards.setMinHeight(130);
		hBoxCards.setAlignment(Pos.CENTER);
		borderPanePlayer.setPrefWidth(980);
		hBoxPlayer.setHgrow(borderPanePlayer, Priority.ALWAYS);
		
		//Cards
		Image tempImage = new Image("file:./resource/images/cards/SCN_0150.jpg");
		
		//initiates back of the cards as default before loading playable cards of player
		cardPlayable1 = new ImageView(tempImage);
		cardPlayable1.setFitHeight(200);
		cardPlayable1.setFitWidth(133);
		cards[0] = cardPlayable1;
		
		cardPlayable2 = new ImageView(tempImage);
		cardPlayable2.setFitHeight(200);
		cardPlayable2.setFitWidth(133);
		cards[1] = cardPlayable2;
		
		cardPlayable3 = new ImageView(tempImage);
		cardPlayable3.setFitHeight(200);
		cardPlayable3.setFitWidth(133);
		cards[2] = cardPlayable3;
		
		cardPlayable4 = new ImageView(tempImage);
		cardPlayable4.setFitHeight(200);
		cardPlayable4.setFitWidth(133);
		cards[3] = cardPlayable4;
		
		cardPlayable5 = new ImageView(tempImage);
		cardPlayable5.setFitHeight(200);
		cardPlayable5.setFitWidth(133);
		cards[4] = cardPlayable5;
		
		cardPlayable6 = new ImageView(tempImage);
		cardPlayable6.setFitHeight(200);
		cardPlayable6.setFitWidth(133);
		cards[5] = cardPlayable6;
		
		cardPlayable7 = new ImageView(tempImage);
		cardPlayable7.setFitHeight(200);
		cardPlayable7.setFitWidth(133);
		cards[6] = cardPlayable7;
		
//		VBox vBoxTablePoints = new VBox();
//		HBox hBoxEmpty = new HBox();
//		hBoxEmpty.setPrefHeight(130);
		
		//Points
		buildTablePoints();
//		vBoxTablePoints.getChildren().addAll(tablePoints);
		VBox vBoxPlayerPoints = new VBox();
		playerName = new Label();
		playerName.getStyleClass().add("vBoxEndGame");
		vBoxPlayerPoints.getChildren().addAll(playerName, tablePoints);
		hBoxPlayer.getChildren().addAll(borderPanePlayer, vBoxPlayerPoints);
		vBoxPlayer.setPrefHeight(570);
		vBoxPlayer.setMinWidth(1350);
		vBoxPlayer.getStyleClass().add("vBoxPlayer");
		
		//Deck
		VBox vBoxDeck = new VBox();
		vBoxDeck.setPadding(new Insets(30,0,0, 0));
		vBoxDeck.setSpacing(5);
		borderPanePlayer.setCenter(vBoxDeck);
		
		/**
		 * @author Roman Leuenberger
		 */
		
		//buttons for client action
		this.playCard = new Button();
		this.buildWorldWonder = new Button();
		this.discardCard = new Button();
		HBox buttonBox = new HBox();
		buttonBox.getChildren().addAll(playCard, buildWorldWonder, discardCard);

		
		playCard.setMaxWidth(200);
		buildWorldWonder.setMaxWidth(200);
		discardCard.setMaxWidth(200);
		
		//disable Buttons as they get only activated if action is possible
		playCard.setDisable(true);
		buildWorldWonder.setDisable(true);
		discardCard.setDisable(true);
		buttonBox.setSpacing(10);
		buttonBox.setAlignment(Pos.CENTER);
//		buttonBox.setPadding(new Insets(0,0,10,0));
		
		/**
		 * @author philipp
		 */
		
		//hbox for world wonder (back of the card)
		this.hBoxWorldWonderCards = new HBox();
		hBoxWorldWonderCards.setSpacing(330);
		hBoxWorldWonderCards.setPadding(new Insets(0,0,0,10));
		hBoxWorldWonderCards.setMinHeight(125);
		hBoxWorldWonderCards.setAlignment(Pos.BOTTOM_LEFT);
		vBoxPlayer.getChildren().addAll(hBoxCards, hBoxPlayer, hBoxWorldWonderCards);
		
		//hbox for player's playable card
		playableCards = new HBox();
		playableCards.getChildren().addAll(cardPlayable1, cardPlayable2, cardPlayable3, cardPlayable4, cardPlayable5, cardPlayable6, cardPlayable7);
				
		vBoxDeck.getChildren().addAll(playableCards, buttonBox);
		playableCards.setAlignment(Pos.BASELINE_CENTER);
		playableCards.setSpacing(2);
		playableCards.setPadding(new Insets(0, 0, 0, 0));
		playableCards.setPrefHeight(210);
//		playableCards.setMinWidth(1000);
		
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
		itemM14 = new MenuItem();
		menuLanguage = new Menu();
		menuLanguage.getItems().addAll(itemM12, itemM13, itemM14);
		
		
		MenuBar menuBar = new MenuBar(menuHelp, menuLanguage);
		borderPaneMain.setTop(menuBar);
		
		this.stage.setResizable(false);
		Scene scene = new Scene(borderPaneMain);
		scene.getStylesheets().add(getClass().getResource("ClientStyle.css").toExternalForm());
		this.stage.sizeToScene();
		this.stage.setScene(scene);
		this.stage.show();
	}
	
	public void addImageView(ImageView v) {
		this.hBoxCards.getChildren().add(v);
		v.setFitHeight(130);
		v.setFitWidth(86);
		v.setEffect(new DropShadow(30, Color.GOLD));
	}
	
	public void addImageViewWorldWonderOne() {
		ImageView v = new ImageView(new Image("file:./resource/images/cards/SCN_0150.jpg"));
		this.hBoxWorldWonderCards.getChildren().add(v);
		v.setFitHeight(125);
		v.setFitWidth(83);
		v.setEffect(new DropShadow(30, Color.GOLD));
	}
	
	public void addImageViewWorldWonderTwo() {
		ImageView v = new ImageView(new Image("file:./resource/images/cards/SCN_0151.jpg"));
		this.hBoxWorldWonderCards.getChildren().add(v);
		v.setFitHeight(125);
		v.setFitWidth(83);
		v.setEffect(new DropShadow(30, Color.GOLD));
	}
	
	/**
	 * @author Roman Leuenberger
	 */
	
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
	/**
	 * @author Roman Leuenberger
	 */
	
	public void disableButtons() {
		this.playCard.setDisable(true);
		this.buildWorldWonder.setDisable(true);
		this.discardCard.setDisable(true);
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
		itemM14.setText(this.getLanguageDescription("language.french"));
		
		menuHelp.setText(translator.getString("menu.help"));
		menuGame.setText(translator.getString("menu.game"));
		menuLanguage.setText(translator.getString("menu.language"));
		
		playCard.setText(translator.getString("button.playCard"));
		buildWorldWonder.setText(translator.getString("button.buildWorldWonder"));
		discardCard.setText(translator.getString("button.discardCard"));
		
		stage.setTitle(translator.getString("clientGame.name"));

		if(this.okButton != null)
			this.okButton.setText(translator.getString("button.quitgame"));
		
		playerName.setText(translator.getString("column.player") + ": " + model.getMyPlayer().getPlayerName());
		
		if(model.getMyPlayer() != null) {
			//Alte Liste leeren
			getTablePoints().getItems().clear();
			//Liste neu aufbauen
			model.getMyPlayer().getResources().refreshObservableMap();
			//Listener setzen
			getTablePoints().setItems(
					model.getMyPlayer().getResources().getResourcesListObservable());
			System.out.println(model.getMyPlayer().getResources().getResourcesListObservable());
		}
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
	public MenuItem getFrenchItem() {
		return this.itemM14;
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
	
	public Button getOkButton() {
		return this.okButton;
	}
	
	public Map<ImageView, Card> getCardsWithImages(){
		return this.cardWithImages;
	}
	
	public TableView<Player> getTableOpponents() {
		return tableOpponents;
	}
	
	public TableView<ResourceType> getTablePoints() {
		return tablePoints;
	}

	public Label getPlayerName() {
		return playerName;
	}
	public void setPlayerName(Label playerName) {
		this.playerName = playerName;
	}

	public void updateClientViewEndGame(ArrayList<Player> winner) {
		VBox vBoxEndGame = new VBox();
		vBoxEndGame.setAlignment(Pos.CENTER);
		this.okButton = new Button(translator.getString("button.quitgame"));
		for (int i = 0; i< winner.size(); i++) {
			Label lblPlace = new Label(i+1 +". Platz: "+winner.get(i).getPlayerName());
			vBoxEndGame.getChildren().add(lblPlace);
		}
		this.playableCards.getChildren().add(vBoxEndGame);
		vBoxEndGame.getChildren().add(okButton);
		vBoxEndGame.setSpacing(15);
		vBoxEndGame.setPadding(new Insets(30, 0, 0, 0));
		vBoxEndGame.getStyleClass().add("vBoxEndGame");
		this.okButton.setOnAction(e -> {
			ServicelocatorClient.getClientController().handleCloseRequest();
		});
	}
}
