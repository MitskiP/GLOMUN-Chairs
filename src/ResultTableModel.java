import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class ResultTableModel extends AbstractTableModel implements TableRowTransferHandler.Reorderable {

	private static final long serialVersionUID = 440057476870729436L;

	private List<String>[] result;
	private JTable parent;

	public ResultTableModel() {
		result = new ArrayList[2];
		result[0] = new ArrayList<String>();
		result[1] = new ArrayList<String>();
		/*
		result[0].add("0"); result[1].add("0");
		result[0].add("1"); result[1].add("1");
		result[0].add("2"); result[1].add("2");
		result[0].add("3"); result[1].add("3");
		result[0].add("4"); result[1].add("4");
		result[0].add("5"); result[1].add("5");
		result[0].add("6"); result[1].add("6");
		result[0].add("7"); result[1].add("7");
		result[0].add("8"); result[1].add("8");
		result[0].add("9"); result[1].add("9");
		 */
	}
	public void setTable(JTable p) { parent = p; }
	@Override
	public int getColumnCount() { return 2; }
	@Override
	public int getRowCount() { return result[0].size(); }
	@Override
	public String getColumnName(int col) { if(col == 0) return "Motion"; else return "Country"; }
	@Override
	public Object getValueAt(int row, int col) { return result[col].get(row); }
	@Override
	public boolean isCellEditable(int row, int col) { return false; }
	@Override
	public void setValueAt(Object value, int row, int col) {
		String str = (String)value;
		while(row >= result[0].size()) result[0].add("ERROR");
		while(row >= result[1].size()) result[1].add("ERROR");
		result[col].set(row, str);
		//fireTableCellUpdated(row, col);
		fireTableRowsDeleted(row, row); // this way JScrollPane is updated too
	}
	public void insertValueAtRow(String a, String b, int row) {
		for(int r=getRowCount()-1;r>=row;r--) {
			setValueAt(getValueAt(r, 0), r+1, 0);
			setValueAt(getValueAt(r, 1), r+1, 1);
		}
		setValueAt(a, row, 0);
		setValueAt(b, row, 1);
	}
	public void deleteRow(int row) {
		for(int i=0;i<result.length;i++)
			result[i].remove(row);
		fireTableRowsDeleted(row, row);
	}
	@Override
	public void reorder(int fromIndex, int toIndex) {
		if(parent == null) return;
		int diff = toIndex - fromIndex;
		//System.out.println("moving: " + fromIndex + " --> " + toIndex + " diff: " + diff);
		int[] selected = parent.getSelectedRows();
		if(diff < 0) {
			for(int i=0;i<selected.length;i++) {
				//System.out.println("	selected: " + selected[i]);
				reorderOne(selected[i], selected[i] + diff);
			}
		} else {
			for(int i=selected.length-1;i>=0;i--) {
				//System.out.println("	selected: " + selected[i]);
				reorderOne(selected[i], selected[i] + diff - selected.length+1);
			}
		}
	}
	private void reorderOne(int fromIndex, int toIndex) {
		if(toIndex < 0 || toIndex > getRowCount()) return;
		String a = (String)getValueAt(fromIndex, 0);
		String b = (String)getValueAt(fromIndex, 1);
		if(fromIndex > toIndex) {
			for(int i=fromIndex;i>toIndex;i--) {
				setValueAt(getValueAt(i-1, 0), i, 0);
				setValueAt(getValueAt(i-1, 1), i, 1);
			}
		} else {
			toIndex--;
			for(int i=fromIndex;i<toIndex;i++) {
				setValueAt(getValueAt(i+1, 0), i, 0);
				setValueAt(getValueAt(i+1, 1), i, 1);
			}
		}
		setValueAt(a, toIndex, 0);
		setValueAt(b, toIndex, 1);
	}
}
