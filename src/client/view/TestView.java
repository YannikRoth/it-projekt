package client.view;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Woodstox;

import globals.ResourceType;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

    @Override
    public void start(Stage primaryStage) {
        ObservableList<Player> players = FXCollections.observableArrayList();
        
        for (int i = 0; i < 5; i++) {
			Player p = new Player("Player " + i);
			p.getResources().put(ResourceType.WOOD, 15);
			players.add(p);
		}
        
        TableView<ResourceType> studentTable = new TableView(players);
//        TableColumn<Player, String> nameCol = new TableColumn("name");
//        nameCol.setCellValueFactory(new PropertyValueFactory<Player, String>("playerName"));
//        studentTable.getColumns().add(nameCol);

        
        Callback<TableColumn.CellDataFeatures<ResourceType, String>, ObservableValue<String>> callBack = 
                new Callback<TableColumn.CellDataFeatures<ResourceType, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ResourceType, String> param) {
            	System.out.println(param.getTableColumn().getUserData());
                return new SimpleStringProperty(Integer.toString(players.get((int)param.getTableColumn().getUserData()).getResources().getResourcesObservable().get(
                		param.getValue())));
//                        return param.getValue().getResources().getResourcesObservable().containsKey(
//                        		Integer.toString((int)param.getTableColumn().getUserData()))
//                        		? new SimpleStringProperty(String.format("%.1f",100d*param.getValue().getResources().getResourcesObservable().get(
//                        				Integer.toString((int)param.getTableColumn().getUserData()))))
//                        				:new SimpleStringProperty("");
            }
        };

        ObservableList<TableColumn<ResourceType, String>> resCols = FXCollections.observableArrayList();
        int i = 0;
        for (ResourceType r : ResourceType.values()) {
        	TableColumn<ResourceType, String> tmpCol = new TableColumn(r.toStringTranslate());
        	tmpCol.setUserData(i);
        	tmpCol.setCellValueFactory(callBack);
        	resCols.add(tmpCol);
        	++i;
        }
        studentTable.getColumns().addAll(resCols);

        VBox root = new VBox(studentTable);
        Scene scene = new Scene(root, 500, 250);

        primaryStage.setTitle("Table with map");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public class Student {

        private final StringProperty firstName = new SimpleStringProperty();
        public StringProperty firstNameProperty(){return firstName;}
        public final HashMap<String, Double> map;

        public Student(String fn) {
            firstName.set(fn);
            map = new LinkedHashMap<>();
            for (int i = 1; i <= 10; i++) {
                double grade = Math.random();
                if (grade > .5) {
                    map.put("ass" + Integer.toString(i), grade);
                }
            }
        }
    }
}