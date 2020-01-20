import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.AbstractTableModel;

public class MUNChairs extends MyTab {
	private static final long serialVersionUID = -3196721296435449172L;

	public static final String VERSION = "2.4";


	public static final String MOTION[] = {
		"Point of Personal Privilege",
		"Right of Reply",
		"Point of Inquiry",
		"Point of Order",
		"Motion to Appeal the Decision of the President",
		"Point of Information",
		"Motion to Limit/Extend Speaking Time",
		"Introduction of a Draft Resolution",
		"Motion to submit an amendment", // 2
		"Motion for Moderated Caucus",
		"Motion for Unmoderated Caucus", // 2
		"Motion to Suspend the Meeting",
		"Motion to Close/Reopen Speaker’s List",
		"Motion to Close the Debate",
		//"Motion to Table the Debate",
		"Motion to Adjourn the Meeting/ Session",
	};
	public static final String COUNTRY[] = {
		"Afghanistan", "Algeria", "Bahrain", "Egypt", "Iraq", "Islamic Republic of Iran", "Jordan", "Lebanon", "Libyan Arab Jamahiriya", "Morocco", "Pakistan", "Palestine", "Quatar", "Saudi Arabia", "Syrian Arab Republic", "Tunisia", "Turkey", "United Arab Emirates", "Yemen",
		"Angola", "Botswana", "Burkina Faso", "Burundi", "Central African Republic", "Chad", "Democratic Republic of Congo", "Ethiopia", "Ghana", "Ivory Coast", "Kenya", "Liberia", "Mali", "Mauritania", "Namibia", "Niger", "Nigeria", "Rwanda", "Somalia", "South Africa", "South Sudan", "Sudan", "Tanzania", "Trinidad and Tobago", "Uganda", "Zimbabwe",
		"Argentina", "Barbados", "Bolivia", "Brazil", "Chile", "Colombia", "Costa Rica", "Cuba", "Dominican Republic", "Ecuador", "El Salvador", "Guatemala", "Haiti", "Honduras", "Jamaica", "Mexico", "Nicaragua", "Panama", "Paraguay", "Peru", "Bolivarian Republic of Venezuela",
		"Australia", " Bangladesh", "Buthan", "China", "Democratic People´s Republic of Korea", "India", "Indonesia", "Japan", "Kazakhstan", "Georgia", "Malaysia", "Myanmar", "Philippines", "Republic of Korea", "Russian Federation", "Sri Lanka", "Thailand", "Viet Nam",
		"Austria", "Belgium", "Canada", "Croatia", "Cyprus", "Czech Republic", "Denmark", "Finland", "France", "Germany", "Greece", "Holy See", "Ireland", "Italy", "Malta", "Norway", "Poland", "Portugal", "Serbia", "Slovenia", "Spain", "Sweden", "The Netherlands", "United Kingdom", "United States of America",
	};


	JSplitPane splitPaneSum;
	JSplitPane splitPaneAdd;
	JList<String> motionList;
	JList<String> countryList;
	StopWatchModel stopwatchmodel;

	public JTabbedPane tabbedPane;
	List<CountryOnly> tab2;

	public MUNChairs() {
		//parent = p;
		setPreferredSize(new Dimension(1280, 720));

		stopwatchmodel = new StopWatchModel();

		motionList = new JList<String>(MOTION);
		motionList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				if(me.getClickCount()%2 == 0) {
					addSelectedEntryFromLeft();
				}
			}
		});
		//motionList.setSelectedIndex(0);
		//motionList.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_A,InputEvent.CTRL_MASK,false), "none");
		countryList = new JList<String>(COUNTRY);
		countryList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				if(me.getClickCount()%2 == 0) {
					addSelectedEntryFromLeft();
				}
			}
		});
		//countryList.setSelectedIndex(0);
		//countryList.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_A,InputEvent.CTRL_MASK,false), "none");
		//countryList.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_A,InputEvent.CTRL_MASK,false), "none");
		//countryList.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A,InputEvent.CTRL_MASK,false), "none");

		resultPane = new JTable();
		resultModel = new ResultTableModel();
		resultPane = new JTable(resultModel);
		((ResultTableModel)resultModel).setTable(resultPane);
		resultPane.setDragEnabled(true);
		resultPane.setDropMode(DropMode.INSERT_ROWS);
		resultPane.setTransferHandler(new TableRowTransferHandler(resultPane));
		resultPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				//JTable table = (JTable)me.getSource();
				//Point p = me.getPoint();
				//int row = table.rowAtPoint(p);
				if(me.getClickCount()%2 == 0) {
					removeSelectedEntryOnRight();
				}
			}
		});
		final JTextField search  = new JTextField(); final JTextField search2  = new JTextField();
		JPanel leftOfLeft = new JPanel() {
			private static final long serialVersionUID = 4422071370390494420L;
			{
				setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				search.getDocument().addDocumentListener(new SearchListDocumentListener(search, motionList));
				search.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						search2.requestFocus();
					}
				});

				c.anchor = GridBagConstraints.CENTER;
				c.fill = GridBagConstraints.BOTH;
				c.weightx = 1; c.weighty = 1;
				c.gridx = 0; c.gridy = 1;
				add(new JScrollPane(motionList), c);
				c.weighty = 0;
				c.gridy = 0;
				add(search, c);
			}
		};
		JPanel rightOfLeft = new JPanel() {
			private static final long serialVersionUID = 8778691948471328223L;
			{
				setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				search2.getDocument().addDocumentListener(new SearchListDocumentListener(search2, countryList));
				search2.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						addSelectedEntryFromLeft();
						search.requestFocus();
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
			}
		};
		splitPaneAdd = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftOfLeft, rightOfLeft);
		splitPaneAdd.setResizeWeight(0.5);
		JPanel left = new JPanel() {
			private static final long serialVersionUID = 6105397674173609877L;
			{
				setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				JButton Button1 = new JButton(">>");
				Button1.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						addSelectedEntryFromLeft();
					}
				});
				Button1.setEnabled(true);
				c.anchor = GridBagConstraints.CENTER;
				c.fill = GridBagConstraints.BOTH;
				c.weightx = 1; c.weighty = 1;
				c.gridx = 0; c.gridy = 0;
				add(splitPaneAdd, c);
				c.weighty = 0;
				c.gridy = 1;
				add(Button1, c);
			}
		};
		JPanel right = new JPanel() {
			private static final long serialVersionUID = -9098489541052443012L;
			{
				setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();

				final JSplitPane splitPaneWatch = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new  JScrollPane(resultPane), new StopWatch(MUNChairs.this, stopwatchmodel, false));
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

		tab2 = new ArrayList<CountryOnly>();
		for(int i=0;i<3;i++)
			tab2.add(new CountryOnly(stopwatchmodel));

		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Motions/Countries", splitPaneSum);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		for(int i=0;i<tab2.size();i++) {
			tabbedPane.addTab("Countries only " + (i+1), tab2.get(i));
			//tabbedPane.setMnemonicAt(i+1, KeyEvent.VK_2);
		}
		setView(tabbedPane);
	}
	private void addSelectedEntryFromLeft() {
		if(motionList.isSelectionEmpty() || countryList.isSelectionEmpty()) return;
		String motion = motionList.getSelectedValue();
		String country = countryList.getSelectedValue();
		int nextRow = resultPane.getRowCount();

		// sort
		int index = getMotionIndex(motion);
		for(int r=0;r<resultPane.getRowCount();r++) {
			int indexr = getMotionIndex((String)resultPane.getValueAt(r, 0));
			if(index < indexr) {
				nextRow = r;
				break;
			}
		}
		((ResultTableModel)resultModel).insertValueAtRow(motion, country, nextRow);
	}
	private int getMotionIndex(String s) {
		for(int i=0;i<MOTION.length;i++)
			if(s.equals(MOTION[i]))
				return i;
		return -1;
	}
	@Override
	public void removeSelectedEntryOnRight() {
		int[] selected = resultPane.getSelectedRows();
		if(selected.length == 0) return;
		for(int i=0;i<selected.length;i++)
			((ResultTableModel)resultModel).deleteRow(selected[i] - i);
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
	private void duplicateStopWatch(boolean b) {
		JFrame frame = new JFrame("GLOMUN-Stopwatch");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.setContentPane(new StopWatch(this, stopwatchmodel, b));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	private void duplicateThisTable() {
		AbstractTableModel rtm = null;
		Component c = tabbedPane.getSelectedComponent();
		if(c == splitPaneSum) {
			rtm = resultModel;
		} else {
			rtm = ((MyTab)c).resultModel;
		}
		JFrame frame = new JFrame("GLOMUN-Table");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		final JTable table = new JTable(rtm);
		table.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent event) {
				int steps = event.getWheelRotation();
				if (steps < 0) {
					//table.setRowHeight(table.getRowHeight() + table.getRowHeight()/10);
					table.setRowHeight(table.getRowHeight() + 1);
				} else {
					if(table.getRowHeight() > 10)
						//table.setRowHeight(table.getRowHeight() - table.getRowHeight()/10);
						table.setRowHeight(table.getRowHeight() - 1);
				}
				Font f = table.getFont();
				table.setFont(new Font(f.getFontName(), Font.PLAIN, table.getRowHeight() - 4));
			}
		});

		frame.setContentPane(new JScrollPane(table));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	public void about() {
		String text = "v." + VERSION + "<br>" + "<br>" +
				"© Mitski Piekenbrock 2014-2015" + "<br>" + // ©
				"&lt;mitski.piekenbrock@gmx.de&gt;";
		JLabel label = new JLabel("<html><div style=\"text-align: center;\">" + text + "</html>", JLabel.CENTER);
		JOptionPane.showMessageDialog(this, label, "About", JOptionPane.PLAIN_MESSAGE);
	}
	public void help() {
		String text = "Select items from the left to add them to the the right;" + "<br>" +
				"double click on one of them or press '&gt;&gt;' button" + "<br>" +
				"Removing 1 row in the list(right): Double click or press '&lt;&lt;' button." + "<br>" + "<br>" +
				"Bottom right: Stopwatch --> key 'Ctrl+s' / button '<' '>' '||' to start/stop, key 'Ctrl+r' or 'Reset' to reset" + "<br>" + "<br>" +
				"Open new windows for presentations --> menu 'Duplicate'" + "<br>" +
				"Increasing font size of 'duplicate-list': scroll above table using mouse wheel" + "<br>";
		JLabel label = new JLabel("<html><div style=\"text-align: center;\">" + text + "</html>", JLabel.CENTER);
		JOptionPane.showMessageDialog(this, label, "Help", JOptionPane.PLAIN_MESSAGE);
	}
	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("GLOMUN-Chairs");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Create and set up the content pane.
		final MUNChairs newContentPane = new MUNChairs();
		newContentPane.setOpaque(true); //content panes must be opaque
		frame.setContentPane(newContentPane);

		//Display the window.
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		JMenu menu;
		JMenuItem menuItem;
		JMenuBar menuBar = new JMenuBar();
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menuItem = new JMenuItem("New Tab", KeyEvent.VK_T);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK));
		menuItem.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) {
			newContentPane.tab2.add(new CountryOnly(newContentPane.stopwatchmodel));
			newContentPane.tabbedPane.addTab("Countries only " + newContentPane.tab2.size(), newContentPane.tab2.get(newContentPane.tab2.size()-1));
		}});
		menu.add(menuItem);
		menu.addSeparator();
		menuItem = new JMenuItem("Stopwatch - Pause/Continue", KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		menuItem.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { newContentPane.stopwatchmodel.toggle(); }});
		menu.add(menuItem);
		menuItem = new JMenuItem("Stopwatch - Reset", KeyEvent.VK_R);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		menuItem.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { newContentPane.stopwatchmodel.reset(); }});
		menu.add(menuItem);
		menu.addSeparator();
		menuItem = new JMenuItem("List - Select all", KeyEvent.VK_A);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
		menuItem.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { ((MyTab)newContentPane.tabbedPane.getSelectedComponent()).resultPane.selectAll(); }});
		menu.add(menuItem);
		menuItem = new JMenuItem("List - Delete selected", KeyEvent.VK_D);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));
		menuItem.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { ((MyTab)newContentPane.tabbedPane.getSelectedComponent()).removeSelectedEntryOnRight(); }});
		menu.add(menuItem);
		menu.addSeparator();
		menuItem = new JMenuItem("Close", KeyEvent.VK_Q);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		menuItem.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { System.exit(0); }});
		menu.add(menuItem);
		menuBar.add(menu);
		menu = new JMenu("Duplicate");
		menu.setMnemonic(KeyEvent.VK_D);
		menuItem = new JMenuItem("Table of current tab", KeyEvent.VK_F2);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, InputEvent.CTRL_MASK));
		menuItem.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { newContentPane.duplicateThisTable(); }});
		menu.add(menuItem);
		menuBar.add(menu);
		menuItem = new JMenuItem("Stopwatch", KeyEvent.VK_F3);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, InputEvent.CTRL_MASK));
		menuItem.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { newContentPane.duplicateStopWatch(true); }});
		menu.add(menuItem);
		menuItem = new JMenuItem("Controllable Stopwatch", KeyEvent.VK_F5);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, InputEvent.CTRL_MASK));
		menuItem.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { newContentPane.duplicateStopWatch(false); }});
		menu.add(menuItem);
		menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_D);
		menuItem = new JMenuItem("Show Help", KeyEvent.VK_F1);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.CTRL_MASK));
		menuItem.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { newContentPane.help(); }});
		menu.add(menuItem);
		menuItem = new JMenuItem("About", KeyEvent.VK_F4);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.CTRL_MASK));
		menuItem.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { newContentPane.about(); }});
		menu.add(menuItem);
		//menu.add(menuItem);
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);
	}
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
