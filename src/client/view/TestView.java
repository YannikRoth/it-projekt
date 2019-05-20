package client.view;

import java.util.Random;

import globals.ResourceType;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import server.model.gameplay.Player;

public class TestView extends Application {

	public static void main(String[] args) {
		launch();
	}
	
    @Override
    public void start(Stage primaryStage) {
        ObservableList<Player> players = FXCollections.observableArrayList(
            new Player("jack")
            ,new Player("john")
            ,new Player("jill")
            ,new Player("jane")
        );
        
        TableView<Player> playerTable = new TableView<>(players);
        
        TableColumn<Player, String> nameCol = new TableColumn<>("name");
        	nameCol.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        	playerTable.getColumns().add(nameCol);

        Random rand = new Random();
        for (Player p:players)
            for (ResourceType t:ResourceType.values()) 
            	p.getResources().put(t, rand.nextInt(99));

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

        ObservableList<TableColumn<Player, String>> cols = FXCollections.observableArrayList();
        for (ResourceType t : ResourceType.values()) {
        	TableColumn<Player, String> tmpCol = new TableColumn<>(t.toStringTranslate());
        	tmpCol.setUserData(t);
        	tmpCol.setCellValueFactory(callBack);
        	cols.add(tmpCol);
			
		}
        playerTable.getColumns().addAll(cols);

        VBox root = new VBox(playerTable);
        Scene scene = new Scene(root, 900, 250);

        primaryStage.setTitle("Table with map");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}