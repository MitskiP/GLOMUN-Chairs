import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;


public class StopWatch extends JPanel implements ComponentListener, AncestorListener {

	private static final long serialVersionUID = 7565267929854482698L;

	private StopWatchModel model;
	private boolean presentation;
	private JPanel parent;

	private JLabel label;
	//private JButton buttonPause;
	private JButton buttonRunForward;
	private JButton buttonRunBackward;
	private JButton buttonResetTo;
	private JButton buttonReset;

	public StopWatch(JPanel mc, StopWatchModel m, boolean b) {
		parent = mc;
		model = m;
		presentation = b;

		if(presentation)
			setPreferredSize(new Dimension(200, 100));
		else
			setMinimumSize(new Dimension(0, 0));

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		label = new JLabel("00:00:00", SwingConstants.CENTER);

		//		buttonPause = new JButton("||");
		//		buttonPause.addActionListener(new ActionListener() {
		//			@Override
		//			public void actionPerformed(ActionEvent e) { toggle(1); }
		//		});
		//		buttonPause.setVisible(false);

		buttonRunForward = new JButton(">");
		buttonRunForward.setMinimumSize(new Dimension(0, buttonRunForward.getPreferredSize().height));
		buttonRunForward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(buttonRunForward.getText().equals(">")) {
					start(1);
				} else {
					pause();
				}
			}
		});
		buttonRunForward.setVisible(true);
		buttonRunBackward = new JButton("<");
		buttonRunBackward.setMinimumSize(new Dimension(0, buttonRunBackward.getPreferredSize().height));
		buttonRunBackward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(buttonRunBackward.getText().equals("<")) {
					start(-1);
				} else {
					pause();
				}
			}
		});
		buttonRunBackward.setVisible(true);
		buttonResetTo = new JButton("Save");
		buttonResetTo.setMinimumSize(new Dimension(0, buttonResetTo.getPreferredSize().height));
		buttonResetTo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { model.setResetTo(model.getTime()); }
		});
		buttonResetTo.setVisible(true);
		buttonReset = new JButton("Reset");
		buttonReset.setMinimumSize(new Dimension(0, buttonReset.getPreferredSize().height));
		buttonReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { model.setResetTo(0); model.reset(); }
		});
		buttonReset.setVisible(true);

		JButton buttonStop = new JButton("Restore");
		buttonStop.setMinimumSize(new Dimension(0, buttonStop.getPreferredSize().height));
		buttonStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { model.reset(); }
		});
		buttonStop.setEnabled(true);
		JButton buttonInc = new JButton("+10s");
		buttonInc.setMinimumSize(new Dimension(0, buttonInc.getPreferredSize().height));
		buttonInc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { model.addTime(10); }
		});
		buttonInc.setEnabled(true);
		JButton buttonDec = new JButton("-10s");
		buttonDec.setMinimumSize(new Dimension(0, buttonDec.getPreferredSize().height));
		buttonDec.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { model.addTime(-10);; }
		});
		buttonDec.setEnabled(true);


		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.5; c.weighty = 1;
		c.gridwidth = 7;
		c.gridx = 0; c.gridy = 0;
		add(label, c);

		if(!presentation) {
			label.addMouseWheelListener(new MouseWheelListener() {
				@Override
				public void mouseWheelMoved(MouseWheelEvent event) {
					int steps = event.getWheelRotation();
					if (steps < 0) {
						model.addTime(1);
					} else {
						model.addTime(-1);
					}
				}
			});
			label.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent me) {
					if(me.getClickCount()%2 == 0) {
						toggle(model.getSign());
					}
				}
			});
			c.weighty = 0; c.gridwidth = 1; c.gridy = 1;
			c.gridx = 0;
			add(buttonRunBackward, c);
			c.gridx = 1;
			add(buttonRunForward, c);
			c.gridx = 2;
			add(buttonReset, c);
			c.gridx = 3;
			add(buttonStop, c);
			c.gridx = 4;
			add(buttonResetTo, c);
			c.gridx = 5;
			add(buttonInc, c);
			c.gridx = 6;
			add(buttonDec, c);
		}

		addComponentListener(this);
		addAncestorListener(this);
	}
	private void toggle(int i) { model.setSign(i); model.toggle(); }
	private void start(int i) { model.setSign(i); model.start(); }
	private void pause() { model.pause(); }
	public void updateUi() {
		if(model.isRunning()) {
			if(model.getSign() > 0) {
				buttonRunForward.setText("||");
				buttonRunBackward.setText("<");
			} else {
				buttonRunForward.setText(">");
				buttonRunBackward.setText("||");
			}
		} else {
			buttonRunForward.setText(">");
			buttonRunBackward.setText("<");
		}
		//		if(model.isRunning())
		//			buttonPause.setText("||");
		//		else
		//			buttonPause.setText(">");
	}
	public void timeIsZero() {
		if(presentation) return;
		pause();
		//if(parent instanceof MUNChairs)
		//	((MUNChairs)parent).tabbedPane.setSelectedIndex(0);
	}
	public void update() {
		//System.out.println("got update");
		NumberFormat nf = new DecimalFormat("00");
		String str = "";
		int i = model.getHour();
		if(i > 0) {
			str += nf.format(i) + ":";
			if(i == 1) adjustLabelSize();
		}
		label.setText(str + nf.format(model.getMinute()) + ":" + nf.format(model.getSecond()) + ":" + nf.format(model.getMillSecond()));
	}
	private void adjustLabelSize() {
		if(label.getWidth() == 0 || label.getHeight() == 0) return;

		Font labelFont = label.getFont();
		String labelText = label.getText() + " ";

		int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
		int componentWidth = label.getWidth();

		// Find out how much the font can grow in width.
		double widthRatio = (double)componentWidth / (double)stringWidth;

		int newFontSize = (int)(labelFont.getSize() * widthRatio);
		int componentHeight = label.getHeight();

		// Pick a new font size so it will not be larger than the height of label.
		int fontSizeToUse = Math.min(newFontSize, componentHeight);

		if(fontSizeToUse <= 0) fontSizeToUse = 1;

		// Set the label's font size to the newly determined size.
		label.setFont(new Font(labelFont.getFontName(), Font.PLAIN, fontSizeToUse));
	}
	@Override
	public void componentHidden(ComponentEvent arg0) { }
	@Override
	public void componentMoved(ComponentEvent arg0) { }
	@Override
	public void componentResized(ComponentEvent event) {
		adjustLabelSize();
	}
	@Override
	public void componentShown(ComponentEvent arg0) { }
	@Override
	public void ancestorAdded(AncestorEvent arg0) {
		if(!model.containsParent(this))
			model.addParent(this);
	}
	@Override
	public void ancestorMoved(AncestorEvent arg0) { }
	@Override
	public void ancestorRemoved(AncestorEvent arg0) {
		if(model.containsParent(this) && presentation)
			model.removeParent(StopWatch.this);
	}
}
