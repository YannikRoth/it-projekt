package client.view;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import client.controller.CardOptionController;
import client.controller.ClientController;
import client.model.CardOptionModel;
import client.model.ClientModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import server.ServiceLocator;
import server.model.gameplay.Card;

/**
 * 
 * @author Roman Leuenberger
 *
 */

public class CardOptionView {
	
	private Stage stage;
	private ClientModel clientModel;
	private CardOptionController control;
	private Logger logger = ServiceLocator.getLogger();
	
	private Map<ImageView, Card> cardWithImages = new HashMap<>();
	protected ImageView[] cards;
	protected Button playCard, buildWorldWonder, discardCard;
	
	public CardOptionView (ClientModel model) {
		this.stage = new Stage();
		this.clientModel = model;
		
		BorderPane borderPaneMain = new BorderPane();
		HBox cardBox = new HBox();
		VBox buttonBox = new VBox();
		cards = new ImageView[clientModel.getMyPlayer().getPlayableCards().size()];
		System.out.println(clientModel.getMyPlayer().getPlayableCards());
		for (int i = 0; i < clientModel.getMyPlayer().getPlayableCards().size(); i++) {
			cards[i] = new ImageView(new Image("file:./resource/images/cards/"+clientModel.getMyPlayer().getPlayableCards().get(i).getImageFileName()));
			cards[i].setFitHeight(130);
			cards[i].setFitWidth(86);
			cardBox.getChildren().add(cards[i]);
			cardWithImages.put(cards[i], clientModel.getMyPlayer().getPlayableCards().get(i));
		}
		//TODO implement translations
		this.playCard = new Button("Karte spielen");
		this.buildWorldWonder = new Button("Weltwunder bauen");
		this.discardCard = new Button("Karte ablegen");
		buttonBox.getChildren().addAll(playCard, buildWorldWonder, discardCard);
		
		playCard.setMaxWidth(200);
		buildWorldWonder.setMaxWidth(200);
		discardCard.setMaxWidth(200);
		
		//disable Buttons as they get only activated if action is possible
		playCard.setDisable(true);
		buildWorldWonder.setDisable(true);
		discardCard.setDisable(true);

		cardBox.setSpacing(10);
		cardBox.setPadding(new Insets(10,10,10,10));
		buttonBox.setSpacing(10);
		buttonBox.setAlignment(Pos.CENTER);
		
		borderPaneMain.setTop(cardBox);
		borderPaneMain.setCenter(buttonBox);
		
		Scene scene = new Scene(borderPaneMain);
		this.stage.setScene(scene);
		
		stage.setTitle("Player: " + clientModel.getMyPlayer().getPlayerName());
		stage.show();
		
		this.control = new CardOptionController (this.clientModel, this);
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
	
	public void stopView() {
		this.stage.close();
	}
	
	public ImageView[] getShownCards() {
		return this.cards;
	}
	
	public ImageView getImageView (int i) {
		return cards[i];
	}
	
	public Map<ImageView, Card> getCardsWithImages(){
		return this.cardWithImages;
	}
	
}
