package client.view;

import globals.ResourceType;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TestView extends Application {

    @Override
    public void start(Stage stage) {

    	ObservableMap<ResourceType, Integer> map = FXCollections.observableHashMap();

    	ObservableList<ResourceType> keys = FXCollections.observableArrayList();

    	map.addListener((MapChangeListener.Change<? extends ResourceType, ? extends Integer> change) -> {
    	    boolean removed = change.wasRemoved();
    	    if (removed != change.wasAdded()) {
    	        // no put for existing key
    	        if (removed) {
    	            keys.remove(change.getKey());
    	        } else {
    	            keys.add(change.getKey());
    	        }
    	    }
    	});

    	map.put(ResourceType.BRICK, 1);
    	map.put(ResourceType.COIN, 2);
    	map.put(ResourceType.FABRIC, 3);

    	final TableView<ResourceType> table = new TableView<>(keys);

    	TableColumn<ResourceType, ResourceType> column1 = new TableColumn<>("Key");
    	// display item value (= constant)
    	column1.setCellValueFactory(cd -> Bindings.createObjectBinding(() -> cd.getValue()));

    	TableColumn<ResourceType, Integer> column2 = new TableColumn<>("Value");
    	column2.setCellValueFactory(cd -> Bindings.valueAt(map, cd.getValue()));

    	table.getColumns().setAll(column1, column2);

        MenuItem mi = new MenuItem("Wert hinzufügen");
        mi.setOnAction(e -> {
        	int i = 0;
        	if(map.containsKey(ResourceType.WOOD))
        		i = map.get(ResourceType.WOOD);
        	i += 33;
        	map.put(ResourceType.WOOD, i);
        });
        Menu m = new Menu("Beispiel");
        m.getItems().add(mi);
        MenuBar mb = new MenuBar();
        mb.getMenus().add(m);
        
        BorderPane bp = new BorderPane();
        bp.setTop(mb);
        bp.setCenter(table);
        
        Scene scene = new Scene(bp);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}