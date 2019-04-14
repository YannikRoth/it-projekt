package server.model.gameplay;

import java.util.Collections;
import java.util.Comparator;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

/**
 * ObservableList for TableView of other players
 * Use method refreshPlayer(... to add/edit a Player in List, this sorts the list by toString() of Player Object
 * @author david
 *
 * @param <E>
 */
public class ObservablePlayerList<E extends Player> extends SimpleListProperty<E> {
	
	public ObservablePlayerList() {
		super(FXCollections.observableArrayList());
		
		this.addListener((ob, o, n) -> {
			//TODO: Implement change sort if we have time
//			sort();
		});
	 }

	/**
	 * add or the object to list
	 * @param e
	 * @author david
	 */
	public void refreshPlayer(E e) {
		if(!this.contains(e))
			this.add(e);
		E o = this.get(this.indexOf(e));
		o = e;
		
		sort();
	}
	
	/**
	 * sort the ObservableList
	 * @author david
	 */
	private void sort() {
		Collections.sort(this, new Comparator<E>() {
			@Override
			public int compare(E o1, E o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});
	}
	
	
}
