import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

public class CountryOnly extends MyTab {
	private static final long serialVersionUID = 4343323363975627353L;
	JSplitPane splitPaneSum;
	JList<String> countryList;

	StopWatchModel stopwatchmodel;

	public CountryOnly(StopWatchModel sm) {
		stopwatchmodel = sm;
		countryList = new JList<String>(MUNChairs.COUNTRY);
		countryList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				if(me.getClickCount()%2 == 0) {
					addSelectedEntryFromLeft();
				}
			}
		});

		resultPane = new JTable();
		resultModel = new AbstractTableModel() {
			private static final long serialVersionUID = -8663234439800078238L;
			ArrayList<String> content;
			{
				content = new ArrayList<String>();
			}
			@Override
			public String getColumnName(int col) { return "Country"; }
			@Override
			public boolean isCellEditable(int row, int col) { return false; }
			@Override
			public void setValueAt(Object value, int row, int col) {
				String str = (String)value;
				while(row >= content.size()) content.add("ERROR");
				if(str.equals("REMOVE"))
					content.remove(row);
				else
					content.set(row, str);
				fireTableRowsDeleted(row, row);
			}
			@Override
			public Object getValueAt(int arg0, int arg1) {
				return content.get(arg0);
			}
			@Override
			public int getRowCount() {
				return content.size();
			}
			@Override
			public int getColumnCount() {
				return 1;
			}
		};
		resultPane = new JTable(resultModel);
		resultPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				if(me.getClickCount()%2 == 0) {
					removeSelectedEntryOnRight();
				}
			}
		});
		final JTextField search2  = new JTextField();
		JPanel left = new JPanel() {
			private static final long serialVersionUID = -6848878280721370756L;
			{
				setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				search2.getDocument().addDocumentListener(new SearchListDocumentListener(search2, countryList));
				search2.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						addSelectedEntryFromLeft();
					}
				});

				c.anchor = GridBagConstraints.CENTER;
				c.fill = GridBagConstraints.BOTH;
				c.weightx = 1; c.weighty = 1;
				c.gridx = 0; c.gridy = 1;
				add(new JScrollPane(countryList), c);
				c.weighty = 0;
				c.gridy = 0;
				add(search2, c);

				JButton Button1 = new JButton(">>");
				Button1.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						addSelectedEntryFromLeft();
					}
				});
				Button1.setEnabled(true);
				c.weightx = 1; c.weighty = 0;
				c.gridx = 0; c.gridy = 2;
				add(Button1, c);
			}
		};
		JPanel right = new JPanel() {
			private static final long serialVersionUID = -7648083421062895585L;
			{
				setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();

				final JSplitPane splitPaneWatch = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(resultPane), new StopWatch(CountryOnly.this, stopwatchmodel, false));
				splitPaneWatch.setResizeWeight(0.9);
				splitPaneWatch.addComponentListener(new ComponentAdapter() {
					boolean firstResize = true;
					@Override
					public void componentResized(ComponentEvent e) {
						if(firstResize) {
							splitPaneWatch.setDividerLocation(0.9);
							firstResize = false;
						}
					}
				});

				JButton Button1 = new JButton("<<");
				Button1.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						removeSelectedEntryOnRight();
					}
				});
				Button1.setEnabled(true);
				c.anchor = GridBagConstraints.CENTER;
				c.fill = GridBagConstraints.BOTH;
				c.weightx = 1; c.weighty = 1;
				c.gridx = 0; c.gridy = 0;
				add(splitPaneWatch, c);
				c.weighty = 0;
				c.gridy = 1;
				add(Button1, c);
			}
		};
		splitPaneSum = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
		splitPaneSum.setResizeWeight(0.5);
		splitPaneSum.addComponentListener(new ComponentAdapter(){
			boolean firstResize = true;
			@Override
			public void componentResized(ComponentEvent e) {
				if(firstResize) {
					splitPaneSum.setDividerLocation(0.5);
					firstResize = false;
				}
			}
		});
		setView(splitPaneSum);
	}
	private void addSelectedEntryFromLeft() {
		if(countryList.isSelectionEmpty()) return;
		String country = countryList.getSelectedValue();
		int nextRow = resultPane.getRowCount();

		resultModel.setValueAt(country, nextRow, 0);
	}
	@Override
	public void removeSelectedEntryOnRight() {
		int[] selected = resultPane.getSelectedRows();
		if(selected.length == 0) return;
		for(int i=0;i<selected.length;i++)
			resultModel.setValueAt("REMOVE", selected[i]-i, 0);
		if(selected[0] >= resultPane.getRowCount())
			selected[0] = resultPane.getRowCount()-1;
		if(selected[0] < 0) return;
		resultPane.setRowSelectionInterval(selected[0], selected[0]);
	}
	private void setView(JComponent jc) {
		removeAll();
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 1;
		c.gridy = 1;
		add(jc, c);
	}
}
