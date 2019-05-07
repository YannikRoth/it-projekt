package client.view;

import java.util.logging.Logger;

import client.controller.CardOptionController;
import client.controller.ClientController;
import client.model.CardOptionModel;
import client.model.ClientModel;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import server.ServiceLocator;

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
	
	protected ImageView[] cards;
	protected Button playCard, buildWorldWonder, discardCard;
	
	public CardOptionView (ClientModel model) {
		this.stage = new Stage();
		this.clientModel = model;
		
		BorderPane borderPaneMain = new BorderPane();
		HBox cardBox = new HBox();
		VBox buttonBox = new VBox();
		cards = new ImageView[clientModel.getMyPlayer().getPlayableCards().size()];
		for (int i = 0; i < clientModel.getMyPlayer().getPlayableCards().size()-1; i++) {
			cards[i] = new ImageView(new Image("file:./resource/images/cards/"+clientModel.getMyPlayer().getPlayableCards().get(i).getImageFileName()));
			cards[i].setFitHeight(130);
			cards[i].setFitWidth(86);
			cardBox.getChildren().add(cards[i]);
		}
		//TODO implement translations
		this.playCard = new Button("Karte spielen");
		this.buildWorldWonder = new Button("Weltwunder bauen");
		this.discardCard = new Button("Karte ablegen");
		buttonBox.getChildren().addAll(playCard, buildWorldWonder, discardCard);
	
		borderPaneMain.setTop(cardBox);
		borderPaneMain.setCenter(buttonBox);
		
		Scene scene = new Scene(borderPaneMain);
		this.stage.setScene(scene);
		
		stage.show();
		
		this.control = new CardOptionController (model, this);
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
	
}
