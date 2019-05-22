package server.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;

import globals.Globals;
import globals.Translator;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import server.ServiceLocator;
import server.model.ServerModel;
import server.model.gameplay.ServerActionLog;
/**
 * Server view for Server MVC
 * @author david
 *
 */
public class ServerView {
	ServerModel model;
	private Stage stage;
	private Translator translator;
	
	MenuBar menuBar;
	Menu menuLanguage;
	MenuItem itemGerman;
	MenuItem itemEnglish;
	MenuItem itemFrench;
	
	private TextField fieldDomain;
	private TextField fieldIpAdress;
	private TextField fieldPort;
	private Label lblDomain;
	private Label lblIpAdress;
	private Label lblPort;
	private Button btnLeaderboard;
	
	TableColumn<ServerActionLog,String> tblcolTimestamp;
	TableColumn<ServerActionLog,String> tblcolIpAdress;
	TableColumn<ServerActionLog,String> tblcolPlayer;
	TableColumn<ServerActionLog,String> tblcolAction;
	
	public ServerView(Stage primaryStage, ServerModel model) {
		this.stage = primaryStage;
		this.model = model;
		translator = Translator.getTranslator();
		buildView();
		setTexts();
	}
	
	/**
	 * builds view elements
	 * @author david
	 */
	public void buildView() {
		BorderPane pane = new BorderPane();
		
		//MenuBar Top
		menuBar = new MenuBar();
		pane.setTop(menuBar);
		
		menuLanguage = new Menu();
		menuBar.getMenus().add(menuLanguage);
		
		itemGerman = new MenuItem();
		itemEnglish = new MenuItem();
		itemFrench = new MenuItem();
		menuLanguage.getItems().addAll(itemGerman, itemEnglish, itemFrench);
		
		//HBox Center
		HBox hBox = new HBox();
		pane.setCenter(hBox);
		
		fieldDomain	= new TextField();
		fieldDomain.setEditable(false);
		
		fieldIpAdress = new TextField();
		fieldIpAdress.setEditable(false);
		fieldIpAdress.setPrefWidth(100);
		fieldIpAdress.setText(model.getHostAddress());
		fieldDomain.setText(model.getHostName());
		fieldPort		= new TextField();
		fieldPort.setEditable(false);
		fieldPort.setText(Integer.toString(Globals.getPortNr()));
		fieldPort.setPrefWidth(50);
		
		lblDomain = new Label();
		lblIpAdress = new Label();
		lblPort = new Label();
		
		FileInputStream input = null;
		try {
			input = new FileInputStream("resource/images/information.jpg");
		} catch (FileNotFoundException e) {
			ServiceLocator.getLogger().warning(e.getLocalizedMessage());
		}
		Image image = new Image(input);
		ImageView imageViewLeaderboard = new ImageView(image);
		imageViewLeaderboard.setFitHeight(30);
		imageViewLeaderboard.setFitWidth(30);
		btnLeaderboard = new Button("Leaderboard", imageViewLeaderboard);
		
		/**
		 * disabled till the functionality can be implemented
		 * @author david
		 */
		hBox.getChildren().addAll(lblDomain, fieldDomain, lblIpAdress, fieldIpAdress, lblPort, fieldPort, btnLeaderboard);
		hBox.setSpacing(5);
		hBox.setAlignment(Pos.CENTER);

		
		//TableView Bottom
		TableView<ServerActionLog> tableView = new TableView<ServerActionLog>();
		tableView.setItems(model.getServerActionData());
		pane.setBottom(tableView);
		
		tblcolTimestamp= new TableColumn<ServerActionLog,String>();
		tblcolTimestamp.setMinWidth(160);
		tblcolTimestamp.setCellValueFactory(new PropertyValueFactory<ServerActionLog,String>("timestamp"));
		
		tblcolIpAdress	= new TableColumn<ServerActionLog,String>();
		tblcolIpAdress.setMinWidth(90);
		tblcolIpAdress.setCellValueFactory(new PropertyValueFactory<ServerActionLog,String>("ipAdress"));
		
		tblcolPlayer	= new TableColumn<ServerActionLog,String>();
		tblcolPlayer.setMinWidth(100);
		tblcolPlayer.setCellValueFactory(new PropertyValueFactory<ServerActionLog,String>("userName"));
		
		tblcolAction	= new TableColumn<ServerActionLog,String>();
		tblcolAction.setMinWidth(300);
		tblcolAction.setCellValueFactory(new PropertyValueFactory<ServerActionLog,String>("action"));
		
		tableView.getColumns().addAll(tblcolTimestamp, tblcolIpAdress, tblcolPlayer, tblcolAction);
		
		//Final Initialisation
		this.stage.getIcons().add(ServiceLocator.getSevenLogo());
		this.stage.setResizable(false);
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("ServerStyle.css").toExternalForm());
		this.stage.sizeToScene();
		this.stage.setScene(scene);
	}
	
	/**
	 * set or refresh text elements ind view, based on locale object
	 * @author david
	 */
	public void setTexts() {
		stage.setTitle(translator.getString("server.name"));
		
		menuLanguage.setText(		translator.getString("menu.language"));
		itemGerman.setText(			this.getLanguageDescription("language.german"));
		itemEnglish.setText(		this.getLanguageDescription("language.english"));
		itemFrench.setText(		this.getLanguageDescription("language.french"));
		
		fieldDomain.setPromptText(	translator.getString("text.nodomain"));
		fieldIpAdress.setPromptText(translator.getString("text.noipadress"));
		fieldPort.setPromptText(translator.getString("text.noport"));
		lblDomain.setText(translator.getString("label.domain"));
		lblIpAdress.setText(translator.getString("label.ipadress"));
		lblPort.setText(translator.getString("label.port"));
		btnLeaderboard.setText(translator.getString("button.leaderboard"));
		
		tblcolTimestamp.setText(	translator.getString("column.timestamp"));
		tblcolIpAdress.setText(		translator.getString("column.ipadress"));
		tblcolPlayer.setText(		translator.getString("column.player"));
		tblcolAction.setText(		translator.getString("column.action"));
	}
	
	/**
	 * Method to check if the language item is the default system setting and return it into description
	 * @param identifier
	 * @return
	 * @author david
	 */
	private String getLanguageDescription(String identifier) {
		if(Translator.getDefaultLocale().getLanguage().substring(0, 2).equalsIgnoreCase(translator.getString(identifier).substring(0, 2)))
			return translator.getString(identifier) + " " + translator.getString("language.default");
		return translator.getString(identifier);
	}

	/**
	 * Starts port input dialog
	 * @return Optional with inserted port as String
	 * @author david
	 */
	public Optional<String> startNewPortDialog() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setGraphic(null);
		dialog.setTitle(		translator.getString("dlg.port.changeport"));
		dialog.setHeaderText(	translator.getString("dlg.port.choosenew"));
		
		((Button)dialog.getDialogPane().lookupButton(ButtonType.OK)).setText(translator.getString("dlg.ok"));
		((Button)dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setText(translator.getString("dlg.cancel"));
		
		final String port;
		
		Optional<String> result = dialog.showAndWait();
		
		return result;
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
	
	public Button getButtonLeaderboard() {
		return this.btnLeaderboard;
	}
	
	public Menu getMenuLanguage() {
		return this.menuLanguage;
	}
}
