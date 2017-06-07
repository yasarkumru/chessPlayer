package main;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import player.Player;
import recognition.ChessPlayerException;
import recognition.Recognizer;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class MainScreen extends JFrame {

	public static JLabel lblBestmove;
	private JPanel contentPane;
	private static Recognizer recognizer;
	private Player player;
	private JRadioButton rdbtnBlack;
	private boolean isPlayerWhite = true;
	private JTextField textField;
	private JTextField txtKqkq;
	private JTextField skillLevelTexr;
	private JLabel lblSkillLevel;

	public static Recognizer getRecognizer() {
		return recognizer;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		long eventMask = AWTEvent.KEY_EVENT_MASK;
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {

			@Override
			public void eventDispatched(AWTEvent event) {
				System.out.println(event.getID());
			}
		}, eventMask);
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainScreen frame = new MainScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainScreen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 164, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		lblBestmove = new JLabel("bestMove");

		JButton btnPlayForWhite = new JButton("Play");
		btnPlayForWhite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playClicked();
			}
		});

		ButtonGroup radioButtons = new ButtonGroup();

		JRadioButton rdbtnWhite = new JRadioButton("white");

		rdbtnBlack = new JRadioButton("black");

		radioButtons.add(rdbtnBlack);
		radioButtons.add(rdbtnWhite);

		JButton btnInit = new JButton("Init");
		btnInit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				initClicked();
			}
		});

		JButton btnDebug = new JButton("Debug");
		btnDebug.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				debugClicked();
			}
		});
		
		textField = new JTextField();
		textField.setText("500");
		textField.setColumns(10);
		
		txtKqkq = new JTextField();
		txtKqkq.setText("KQkq");
		txtKqkq.setColumns(10);
		
		skillLevelTexr = new JTextField();
		skillLevelTexr.setText("20");
		skillLevelTexr.setColumns(10);
		
		lblSkillLevel = new JLabel("skill level 0..20");

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
							.addComponent(rdbtnBlack)
							.addComponent(rdbtnWhite)
							.addComponent(lblBestmove, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnInit, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnPlayForWhite, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnDebug))
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtKqkq, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblSkillLevel)
						.addComponent(skillLevelTexr, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(26, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(lblBestmove)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnInit)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnPlayForWhite)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rdbtnWhite)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rdbtnBlack)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnDebug)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtKqkq, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblSkillLevel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(skillLevelTexr, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}

	protected void debugClicked() {
		
		try {
			player.end();
			player= new Player(recognizer.getBoard());
			player.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		playClicked();
	}

	protected void initClicked() {
		init();
	}

	protected void playClicked() {
		recognizer.getBoard().clearMoves();
		recognizer.getBoard().setCastlingRights(txtKqkq.getText());
		isPlayerWhite = !rdbtnBlack.isSelected();
		String playerString = isPlayerWhite ? "w" : "b";
		int playTime = Integer.parseInt(textField.getText());
		try {
			player.setEngineSkillLevel(skillLevelTexr.getText());
			while (true) {
				recognizer.refreshChessBoard();
				player.sendCommandAndGetBestMove(playerString, playTime);

			}

		} catch (AWTException | IOException e) {
			e.printStackTrace();
		} catch (ChessPlayerException e) {
			System.out.println("board checking ended!!!");
		}
	}

	protected void init() {
		recognizer = new Recognizer();

		try {
			recognizer.init();
			player = new Player(recognizer.getBoard());
			player.init();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (AWTException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
