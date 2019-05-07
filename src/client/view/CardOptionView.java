package client.view;

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

/**
 * 
 * @author Roman Leuenberger
 *
 */

public class CardOptionView {
	
	private Stage stage;
	private CardOptionModel cardOptionModel;
	private ClientModel clientModel;
	
	protected ImageView[] cards;
	protected Button playCard, buildWorldWonder, discardCard;
	
	public CardOptionView (Stage primaryStage, CardOptionModel model) {
		this.stage = primaryStage;
		this.cardOptionModel = model;
		
		BorderPane borderPaneMain = new BorderPane();
		HBox cardBox = new HBox();
		VBox buttonBox = new VBox();
		
		cards = new ImageView[clientModel.getMyPlayer().getPlayableCards().size()];
		for (int i = 0; i < clientModel.getMyPlayer().getPlayableCards().size()-1; i++) {
			cards[i] = new ImageView(new Image("file:./resource/images/cards/SCN_0"+String.format("%03d", clientModel.getMyPlayer().getPlayableCards().get(i).getId()+".jpg")));
			cards[i].setFitHeight(130);
			cards[i].setFitWidth(86);
			cardBox.getChildren().add(cards[i]);
		}
		
		this.playCard = new Button("Karte spielen");
		this.buildWorldWonder = new Button("Weltwunder bauen");
		this.discardCard = new Button("Karte ablegen");
		buttonBox.getChildren().addAll(playCard, buildWorldWonder, discardCard);
		
		borderPaneMain.setTop(cardBox);
		borderPaneMain.setCenter(buttonBox);
		
		Scene scene = new Scene(borderPaneMain);
		this.stage.setScene(scene);
		
	}
	
	public void start () {
		stage.show();
	}

}
