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
	private ClientModel clientmodel;
	
	protected ImageView card1, card2, card3, card4, card5, card6, card7;
	protected Button playCard, buildWorldWonder, discardCard;
	
	public CardOptionView (Stage primaryStage, CardOptionModel model) {
		this.stage = primaryStage;
		this.cardOptionModel = model;
		
		BorderPane borderPaneMain = new BorderPane();
		HBox cardBox = new HBox();
		VBox buttonBox = new VBox();
		
		Image image = new Image("file:./resource/images/cards/SCN_0150.jpg");
		
		card1 = new ImageView(image);
		card1.setFitHeight(130);
		card1.setFitWidth(86);
		
		card2 = new ImageView(image);
		card2.setFitHeight(130);
		card2.setFitWidth(86);
		
		card3 = new ImageView(image);
		card3.setFitHeight(130);
		card3.setFitWidth(86);
		
		card4 = new ImageView(image);
		card4.setFitHeight(130);
		card4.setFitWidth(86);
		
		card5 = new ImageView(image);
		card5.setFitHeight(130);
		card5.setFitWidth(86);
		
		card6 = new ImageView(image);
		card6.setFitHeight(130);
		card6.setFitWidth(86);
		
		card7 = new ImageView(image);
		card7.setFitHeight(130);
		card7.setFitWidth(86);
		
		cardBox.getChildren().addAll(card1, card2, card3, card4, card5, card6, card7);
		
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
