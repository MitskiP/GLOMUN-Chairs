import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;


public abstract class MyTab extends JPanel {

	public AbstractTableModel resultModel;
	public JTable resultPane;

	public abstract void removeSelectedEntryOnRight();

}
