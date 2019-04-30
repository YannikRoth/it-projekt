package client.view;

import java.util.HashMap;
import java.util.Map;

import globals.ResourceType;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TestView extends Application {

    @Override
    public void start(Stage stage) {

        // sample data
        Map<ResourceType, Integer> map = new HashMap<>();
        map.put(ResourceType.BRICK, 1);
        map.put(ResourceType.FABRIC, 2);
        map.put(ResourceType.ORE, 3);


        // use fully detailed type for Map.Entry<String, String> 
        TableColumn<Map.Entry<ResourceType, Integer>, String> column1 = new TableColumn<>("Key");
        column1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<ResourceType, Integer>, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<ResourceType, Integer>, String> p) {
                // this callback returns property for just one cell, you can't use a loop here
                // for first column we use key
                return new SimpleStringProperty(p.getValue().getKey().name());
            }
        });

        TableColumn<Map.Entry<ResourceType, Integer>, String> column2 = new TableColumn<>("Value");
        column2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<ResourceType, Integer>, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<ResourceType, Integer>, String> p) {
                // for second column we use value
                return new SimpleStringProperty(p.getValue().getValue().toString());
            }
        });

        ObservableList<Map.Entry<ResourceType, Integer>> items = FXCollections.observableArrayList(map.entrySet());
        final TableView<Map.Entry<ResourceType, Integer>> table = new TableView<>(items);

        table.getColumns().setAll(column1, column2);

        Scene scene = new Scene(table, 400, 400);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}