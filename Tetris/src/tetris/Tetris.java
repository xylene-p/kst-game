package tetris;

/**
 * Create and control the game Tetris.
 * 
 *
 *
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;

public class Tetris extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int IFW = JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;
	
	public static int WINDOWWIDTH = 1200;
	public static int WINDOWHEIGHT = 1000;
	public static int TETRISWIDTH = 600;
	
	private Game game;
	private JFrame f;
	private EventController ec;

	/** Sets up the parts for the Tetris game, display and user control
	 * @param f frame to draw the game on
	 */
	public Tetris()
	{
		// class to construct a panel with an image for background
		class BGPanel extends JPanel
		{
			private static final long serialVersionUID = 1L;

			public BGPanel()
			{
				super();
			}
			
			public void paintComponent(Graphics g)
			{
			    super.paintComponent(g);
			    
			    try
			    {
			    	Image bgImage = ImageIO.read(new File("images/tetris_bg.png"));
				    
				    if (bgImage != null) g.drawImage(bgImage,0,0,this);
			    }
			    
			    catch(IOException e){}
			}
		}
		
		// left panel
		JPanel panel_Left = new JPanel();
		panel_Left.setPreferredSize(new Dimension(300, WINDOWHEIGHT));
		panel_Left.setOpaque(false);		
		panel_Left.setLayout(new GridLayout(12,1));
		
		// right panel
		JPanel panel_Right = new JPanel();
		panel_Right.setPreferredSize(new Dimension(300, WINDOWHEIGHT));
		panel_Right.setOpaque(false);		
		panel_Right.setLayout(new GridLayout(12,1));
		
		// create game
		game = new Game(this);
		
		// generate window
		f = new JFrame("Tetris");
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setSize(WINDOWWIDTH, WINDOWHEIGHT);
		f.setVisible(true);
		f.setLayout(new BorderLayout());
		f.setResizable(false);
		

		// generate background panel
		BGPanel bg = new BGPanel();
		bg.setLayout(new BorderLayout());
		f.add(bg, BorderLayout.CENTER);
		
		// generate event controller
		ec = new EventController(game);
		
		// set active game play panel transparent
		setOpaque(false);
		
		// pause button
		Button btn_Pause = new Button("PAUSE", GameFont.font, 26f, Color.RED, Color.WHITE);
		btn_Pause.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if (game.isPaused())
						{
							game.setPausedState(false);
							btn_Pause.setSelected(false);
							btn_Pause.setText("PAUSE");
						}
						else
						{
							game.setPausedState(true);
							btn_Pause.setSelected(true);
							btn_Pause.setText("RESUME");
						}
					}
				});
		
		// group pause button
		ImageBGPanel layoutPause = new ImageBGPanel("images/menu_btn.png");
		layoutPause.setLayout(new GridLayout(1,1));
		layoutPause.add(btn_Pause);
		
		// menu button
		Button btn_Menu = new Button("MENU", GameFont.font, 26f, Color.WHITE, Color.WHITE);
		
		ImageBGPanel layoutMenu = new ImageBGPanel("images/menu_btn.png");
		layoutMenu.setLayout(new GridLayout(1,1));
		layoutMenu.add(btn_Menu);
		
		// left panel components
		panel_Left.add(new EmptyPanel());
		panel_Left.add(layoutPause);
		panel_Left.add(layoutMenu);
		
		// create group labels
		JLabel lbl_score = new JLabel("SCORE", JLabel.CENTER);
		lbl_score.setFont(GameFont.fontWithSize(26f));
		lbl_score.setForeground(Color.WHITE);
		
		JLabel lbl_un = new JLabel("UP NEXT", JLabel.CENTER);
		lbl_un.setFont(GameFont.fontWithSize(26f));
		lbl_un.setForeground(Color.WHITE);
		
		// right panel components
		panel_Right.add(new EmptyPanel());
		panel_Right.add(lbl_score);
		panel_Right.add(new EmptyPanel());
		panel_Right.add(lbl_un);
		
		attachActions(btn_Pause);
		
		bg.add(this, BorderLayout.CENTER);
		bg.add(panel_Left, BorderLayout.WEST);
		bg.add(panel_Right, BorderLayout.EAST);
	}

	/** Updates the display
	 */
	public void update()
	{
		repaint();
	}

	/** Paint the current state of the game
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	    
		game.draw(g);
		
		if (game.isGameOver()) {
			g.setFont(GameFont.fontWithSize(30f));
			g.setColor(Color.RED);
			g.drawString("GAME OVER!", (WINDOWWIDTH/2)-115, 60);
		}
	}
	
	public void attachActions(JButton btn)
	{
		// key actions, had to remove from EventController because KeyListener does not work with multiple panels within the same frame
		
		// up key
		btn.getInputMap(IFW).put(KeyStroke.getKeyStroke("UP"), "key_up");
		btn.getActionMap().put("key_up", new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) { if ((!game.isGameOver()) && (!game.isPaused())) game.rotatePieceCW(); }
		});
		
		// kp up key
		btn.getInputMap(IFW).put(KeyStroke.getKeyStroke("KP_UP"), "key_kp_up");
		btn.getActionMap().put("key_kp_up", new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) { if ((!game.isGameOver()) && (!game.isPaused())) game.rotatePieceCW(); }
		});
		
		// down key
		btn.getInputMap(IFW).put(KeyStroke.getKeyStroke("DOWN"), "key_down");
		btn.getActionMap().put("key_down", new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) { if ((!game.isGameOver()) && (!game.isPaused())) ec.handleMove(Direction.DOWN); }
		});
		
		// kp down key
		btn.getInputMap(IFW).put(KeyStroke.getKeyStroke("KP_DOWN"), "key_kp_down");
		btn.getActionMap().put("key_kp_down", new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) { if ((!game.isGameOver()) && (!game.isPaused())) ec.handleMove(Direction.DOWN);  }
		});
		
		// right key
		btn.getInputMap(IFW).put(KeyStroke.getKeyStroke("RIGHT"), "key_right");
		btn.getActionMap().put("key_right", new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) { if ((!game.isGameOver()) && (!game.isPaused())) ec.handleMove(Direction.RIGHT); }
		});
		
		// kp right key
		btn.getInputMap(IFW).put(KeyStroke.getKeyStroke("KP_RIGHT"), "key_kp_right");
		btn.getActionMap().put("key_kp_right", new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) { if ((!game.isGameOver()) && (!game.isPaused())) ec.handleMove(Direction.RIGHT); }
		});
		
		// left key
		btn.getInputMap(IFW).put(KeyStroke.getKeyStroke("LEFT"), "key_left");
		btn.getActionMap().put("key_left", new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) { if ((!game.isGameOver()) && (!game.isPaused())) ec.handleMove(Direction.LEFT); }
		});
		
		// kp left key
		btn.getInputMap(IFW).put(KeyStroke.getKeyStroke("KP_LEFT"), "key_kp_left");
		btn.getActionMap().put("key_kp_left", new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) { if ((!game.isGameOver()) && (!game.isPaused())) ec.handleMove(Direction.LEFT); }
		});
		
		// z key
		btn.getInputMap(IFW).put(KeyStroke.getKeyStroke("Z"), "key_Z");
		btn.getActionMap().put("key_Z", new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) { if ((!game.isGameOver()) && (!game.isPaused())) game.rotatePieceCCW(); }
		});
		
		// x key
		btn.getInputMap(IFW).put(KeyStroke.getKeyStroke("X"), "key_X");
		btn.getActionMap().put("key_X", new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) { if ((!game.isGameOver()) && (!game.isPaused())) game.rotatePieceCW(); }
		});

		// q (quit) key
		btn.getInputMap(IFW).put(KeyStroke.getKeyStroke("Q"), "key_Q");
		btn.getActionMap().put("key_Q", new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e)
			{
				ec.stopTimer();
				f.dispose();
			}
		});
	}
}
