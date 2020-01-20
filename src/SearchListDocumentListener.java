import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class SearchListDocumentListener implements DocumentListener {

	private JList<String> list;
	private ArrayList<String> entry;
	private JTextField tf;

	public SearchListDocumentListener(JTextField p1, JList<String> p2) {
		tf = p1;
		list = p2;
		entry = new ArrayList<String>();
		ListModel<String> lm = list.getModel();
		for(int i=0;i<lm.getSize();i++)
			entry.add(lm.getElementAt(i));
		tf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tf.setText("");
			}
		});
	}
	private void handleEvent(DocumentEvent e) {
		//System.out.print("text: " + tf.getText());
		String str = tf.getText().toLowerCase();
		if(str.equals("")) return;
		int select = -1;
		if(select == -1) {
			for(int i=0;i<entry.size();i++) {
				if(entry.get(i).toLowerCase().matches(str + ".*")) {
					select = i; break;
				}
			}
		}
		if(select == -1) {
			for(int i=0;i<entry.size();i++) {
				if(entry.get(i).toLowerCase().contains(str)) {
					select = i; break;
				}
			}
		}
		//System.out.println(" --> " + select);
		if(select == -1) select = list.getSelectedIndex();
		list.setSelectedIndex(select);
		list.ensureIndexIsVisible(list.getSelectedIndex());
	}
	@Override
	public void removeUpdate(DocumentEvent arg0) {
		handleEvent(arg0);
	}
	@Override
	public void insertUpdate(DocumentEvent arg0) {
		handleEvent(arg0);
	}
	@Override
	public void changedUpdate(DocumentEvent arg0) {
		handleEvent(arg0);
	}
}
