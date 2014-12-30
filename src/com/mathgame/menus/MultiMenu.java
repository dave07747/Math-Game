package com.mathgame.menus;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import com.mathgame.math.MathGame;
import com.mathgame.math.SoundManager;
import com.mathgame.math.TypeManager;
import com.mathgame.network.Game;
import com.mathgame.network.GameManager;
import com.mathgame.network.User;

/**
 * The MultiMenu class represents the menu for setting up multiplayer games
 */

public class MultiMenu extends JPanel implements ActionListener, MouseMotionListener, MouseListener {
	
	private static final long serialVersionUID = -3036828086937465893L;

	TypeManager typeManager;
	
	static final String IMAGE_FILE = "/images/backMulti.png";
	static final String BUTTON_IMAGE_FILE = "/images/DefaultButtonImage1.png";
	static final String BUTTON_ROLLOVER_IMAGE_FILE = "/images/DefaultButtonImage2.png";
	static final String BUTTON_PRESSED_IMAGE_FILE = "/images/DefaultButtonImage3.png";
	
	static final int BUTTON_WIDTH = 130;
	static final int BUTTON_HEIGHT = 30;
	static final int WIDE_BUTTON_WIDTH = 150;
	
	static ImageIcon background;
	static ImageIcon buttonImage;
	static ImageIcon buttonRollOverImage;
	static ImageIcon buttonPressedImage;
	
	static {
		background = new ImageIcon(MultiMenu.class.getResource(IMAGE_FILE));
		buttonImage = new ImageIcon(MultiMenu.class.getResource(BUTTON_IMAGE_FILE));
		buttonRollOverImage = new ImageIcon(MultiMenu.class.getResource(BUTTON_ROLLOVER_IMAGE_FILE));
		buttonPressedImage = new ImageIcon(MultiMenu.class.getResource(BUTTON_PRESSED_IMAGE_FILE));
	}
	
	// Mouse coordinates
	int mx;
	int my;
	
	Font titleFont;
	Font buttonFont;
	
	JPanel gamesList;
	JButton home; // Press to enter a game
	JButton host; // Press to host a game
	JButton join; // Press to join a game
	JButton practice; // sends to practice mode
	JTextArea usersList;
	JTextArea userProfile; // displays info about the selected user (win/loss, etc)
	
	Panel innerPanel; 
	
	final int NUMBEROFPLAYERS = 2;//TOOD: get rid of this
	
	GameManager gameManager;
	HostMenu hostMenu;
	private ArrayList<String> usersArray;
	private ArrayList<Game> games;
	private ArrayList<GameCard> gameCards;
	
	Timer refreshTimer;
	
	public void init(TypeManager tn) {
		
		this.setLayout(null);
		Dimension size = getPreferredSize();
		size.width = 900;
		size.height = 620;
		setPreferredSize(size);
		
		typeManager = tn;
		gameManager = MathGame.getGameManager();
		hostMenu = new HostMenu();
		
		titleFont = MathGame.eurostile24;
		buttonFont = new Font("Arial", Font.PLAIN, 20);
		
		home = new JButton("Back");
		home.setFont(buttonFont);
		home.setBounds(50, 535, BUTTON_WIDTH, BUTTON_HEIGHT);
	    home.setHorizontalTextPosition(JButton.CENTER);
	    home.setVerticalTextPosition(JButton.CENTER);
	    home.setBorderPainted(false);
	    
		host = new JButton("Host");
		host.setFont(buttonFont);
		host.setBounds(273, 535,  BUTTON_WIDTH, BUTTON_HEIGHT);
		host.setHorizontalTextPosition(JButton.CENTER);
		host.setVerticalTextPosition(JButton.CENTER);
		host.setBorderPainted(false);
	    
		join = new JButton("Join");
		join.setFont(buttonFont);
		join.setBounds(496, 535,  BUTTON_WIDTH, BUTTON_HEIGHT);
		join.setHorizontalTextPosition(JButton.CENTER);
		join.setVerticalTextPosition(JButton.CENTER);
		join.setBorderPainted(false);
	    
		practice = new JButton("Practice");
		practice.setFont(buttonFont);
		practice.setBounds(720, 535, BUTTON_WIDTH, BUTTON_HEIGHT);
		practice.setHorizontalTextPosition(JButton.CENTER);
		practice.setVerticalTextPosition(JButton.CENTER);
		practice.setBorderPainted(false);
		
	    gamesList = new JPanel();
	    gamesList.setBounds(50, 50, 600, 450);
		gamesList.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.GREEN, 2),
				"Lobby", TitledBorder.CENTER, TitledBorder.BELOW_TOP, 
				titleFont, Color.BLACK));
	    gamesList.setBackground(Color.WHITE);
	    gamesList.setVisible(true);
		
		usersList = new JTextArea();
		usersList.setBounds(650, 200, 200, 300);
		usersList.setBackground(Color.WHITE);
		usersList.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.GREEN, 2),
				"Users", TitledBorder.CENTER, TitledBorder.BELOW_TOP, 
				titleFont, Color.BLACK));
		usersList.setEditable(false);
		usersList.setVisible(true);
		
		userProfile = new JTextArea();
		userProfile.setBounds(650, 50, 200, 150);
		userProfile.setBackground(Color.WHITE);
		userProfile.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.GREEN, 2), 
				"User Profile", TitledBorder.CENTER, TitledBorder.BELOW_TOP, 
				titleFont, Color.BLACK));
		userProfile.setEditable(false);
		userProfile.setVisible(true);
		
		usersArray = new ArrayList<String>();
		
		games = GameManager.getMatchesAccess().getCurrentGames();
		gameCards = new ArrayList<GameCard>();
		
		for(Game game : games) {
			// For each game, create a gamecard
			GameCard gc = new GameCard(game.getID(), "Game "+String.valueOf(game.getID()), NUMBEROFPLAYERS, 
					game.getType(), game.getScoring(), game.getDiff(), game.getRounds());
			gameCards.add(gc);
		}

		for(GameCard card : gameCards) {
			gamesList.add(card);
		}
	    
		try {
		    home.setIcon(buttonImage);
		    home.setRolloverIcon(buttonRollOverImage);
		    home.setPressedIcon(buttonPressedImage);
		    
		    host.setIcon(buttonImage);
		    host.setRolloverIcon(buttonRollOverImage);
		    host.setPressedIcon(buttonPressedImage);
		    
		    join.setIcon(buttonImage);
		    join.setRolloverIcon(buttonRollOverImage);
		    join.setPressedIcon(buttonPressedImage);
		    
		    practice.setIcon(buttonImage);					
		    practice.setRolloverIcon(buttonRollOverImage);	
		    practice.setPressedIcon(buttonPressedImage);		
		    
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//TODO Get the text in the label to wrap if it is longer than the label width
		
		// Info Box for Enter Box
		add(home);
		add(host);
		add(join);
		add(practice);
		add(gamesList);
		add(usersList);
		add(userProfile);
		
		home.addActionListener(this);
		home.addMouseMotionListener(this);
		home.addMouseListener(this);
		host.addMouseMotionListener(this);
		host.addMouseListener(this);
		host.addActionListener(this);
		
		join.addMouseMotionListener(this);
		join.addMouseListener(this);
		join.addActionListener(this);
		
		practice.addActionListener(this);
		practice.addMouseMotionListener(this);
		practice.addMouseListener(this);
		
		// Start refresh thread
		refreshTimer = new Timer(2000, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				refresh();				
			}			
		});
		refreshTimer.setInitialDelay(0);
		//refreshTimer.start();
		
		System.out.println("Menu Init Complete");
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			SoundManager.playSound(SoundManager.SoundType.BUTTON);
		}
		if(e.getSource() == home) {
			MathGame.showMenu(MathGame.Menu.MAINMENU); // Return to the main menu
			refreshTimer.stop();
			// choosefraction();
			// startgame();
		} else if(e.getSource() == host) {
			MathGame.showMenu(MathGame.Menu.HOSTMENU);
			// startgame();
		} else if(e.getSource() == join) {
			// choosedecimal();
			// startgame();
		}
		else if(e.getSource() == practice) {
			MathGame.showMenu(MathGame.Menu.OPTIONMENU);// select practice options
			refreshTimer.stop();
		}
	}
	
	/**
	 * Refresh the list of games
	 */
	public void refresh() {
		refreshDatabase();

		games = gameManager.getCurrentGames();
		gameCards.clear();
		
		for(Game game : games) {
			GameCard gc = new GameCard(game.getID(), "Game "+String.valueOf(game.getID()), NUMBEROFPLAYERS, 
					game.getType(), game.getScoring(), game.getDiff(), game.getRounds());
			gameCards.add(gc);
		}
		gamesList.removeAll();
		
		for(GameCard card : gameCards)
		{
			gamesList.add(card);
			System.out.println(card.gameID);
		}
		gamesList.revalidate();
		gamesList.repaint();
		
		System.out.println("updated currentgames");
		
		// startgame();
	}
	
	/**
	 * Add a new game to the list of games
	 * @param g - The Game to be added
	 */
	public void addGame(Game g)	{
		//TODO Later consider users naming their games...		
		
		gameManager.setGame(g);
		int gameID = gameManager.hostGame(); // Needed so the game manager knows what game it's managing
		g.setID(gameID);
		games.add(g);
		gameCards.add(new GameCard(gameID, "Game "+gameID, NUMBEROFPLAYERS,
				g.getType(), g.getScoring(), g.getDiff(), g.getRounds()));
		gamesList.add(gameCards.get(games.size() - 1));
		GameManager.getMatchesAccess().setMatchNum(gameID); 
		
		ArrayList<Game> test = gameManager.getCurrentGames();
		for (int i = 0; i < test.size(); i++) {
			System.out.println("GAMEs are " + test.get(i).getID());
		}
		GameManager.getMatchesAccess().checkForFullGame();
	}
	
	/**
	 * Add the current user to the list of users
	 */
	public void addThisUser(){
		try {
			if(MathGame.getMySQLAccess().getConnection() == null)
				MathGame.getMySQLAccess().connect();
			MathGame.getMySQLAccess().addUser();
			// mathGame.sql.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	/**
	 * Refresh the database
	 */
	public void refreshDatabase() {
		try {
			if (MathGame.getMySQLAccess().getConnection() == null) {
				MathGame.getMySQLAccess().connect();
			}
			usersArray = MathGame.getMySQLAccess().getUsersGame();
			updateUsersList();
			// mathGame.sql.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Updates the list of users
	 */
	public void updateUsersList() {
		System.out.println("updating users " + usersArray.size());
		usersList.setText("");
		for (int i = 0; i < usersArray.size(); i++) {
			//System.out.println(usersArray.get(i));
			usersList.append(usersArray.get(i)+'\n');
		}
		
		usersList.revalidate();
		usersList.repaint();

	}
	
	/**
	 * Starts the game
	 */
	public void startGame() {
		// this.setVisible(false);
		MathGame.showMenu(MathGame.Menu.GAME);
		System.out.println("ENTER GAME");
		System.out.println("type1 " + typeManager.getType());
		typeManager.init(MathGame.getCardPanel());
		typeManager.randomize();
	}
	
	/**
	 * When you choose the fraction option
	 */
	public void chooseFraction() {
		//this.setVisible(false);
		
		typeManager.setType(TypeManager.GameType.FRACTIONS);
		System.out.println("Selected: fraction");
	}
	
	/**
	 * When you choose the decimal option
	 */ 
	public void chooseDecimal() {
		// this.setVisible(false);

		typeManager.setType(TypeManager.GameType.DECIMALS);
		System.out.println("Selected: decimal");
	}
	
	/**
	 * When you choose the integer option
	 */
	public void chooseInteger() {
		// this.setVisible(false);

		typeManager.setType(TypeManager.GameType.INTEGERS);
		System.out.println("Selected: integer");
	}
	
	/**
	 * When you choose the mixed option
	 */
	public void chooseMixed() {
		//this.setVisible(false);

		//TODO Implement a mixed mode; currently sets to Fraction mode
		typeManager.setType(TypeManager.GameType.FRACTIONS);
		System.out.println("Selected: mixed");
	}
	
	//TODO Implement the modeInfo functions
	/**
	 * Displays info on the fraction mode
	 */
	public void fractionInfo() {
		// info.setText("Choose this mode to work with fractions");
		// JOptionPane.showMessageDialog(this, "We need help in putting something that is worthwhile in this box.");
	}
	
	/**
	 * Displays info on the decimal mode
	 */
	public void decimalInfo() {
		//JOptionPane.showMessageDialog(this, "We need help in putting something that is worthwhile in this box.");
	}
	
	/**
	 * Displays info on the integer mode
	 */
	public void integerinfo() {
		// info.setText("Choose this mode to work with integers");
		// JOptionPane.showMessageDialog(this, "Game created by Academy Math Games Team. Menu created by Roland Fong and David Schildkraut.");
	}
	
	/**
	 * Displays info on the mixed mode
	 */
	public void mixedinfo() {
		// info.setText("Choose this mode to work with all of the types");
		// JOptionPane.showMessageDialog(this, "We need help in putting something that is worthwhile in this box.");
	}
	
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		g.drawImage(background.getImage(), 0, 0, MultiMenu.this);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		System.out.println("Mouse Exited Button");
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	/**
	 * The GameCard class displays information about a game
	 */
	private class GameCard extends JLabel {

		//TODO Use unused method
		
		private static final long serialVersionUID = 2993530244820621535L;
		
		int gameID;
		int numPlayers; // 2 for now, but may introduce a solo mode or more than 2 players
		String name;
		String type;
		String scoring;
		String diff;
		int rounds; //Number of rounds
		ArrayList<User> players;
		
		/**
		 * @param ID - The ID of the game (the row number in database)
		 * @param name - The name of the game
		 * @param type - The type of the game (as a string)
		 * @param scoring - How the game is scored 
		 * @param diff - The difficulty of the game (as a Difficulty enumm)
		 */
		public GameCard(int ID, String name, int numPlayers, String type, String scoring, String diff, int rounds) {
			super();
			this.gameID = ID;
			this.name = name;
			this.numPlayers = numPlayers;
			this.type = type;
			this.scoring = scoring;
			this.diff = diff;
			this.rounds = rounds;
			
			this.setLayout(null);
			Dimension size = getPreferredSize();
			size.width = 100;
			size.height = 100;
			setPreferredSize(size);
			setOpaque(true);
			this.setText("<html>"+name+"<br>"+type+"<br>"+scoring+"<br>"+diff+"<br>Rounds: "+rounds+"</html>");
			
			this.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					GameCard tempCard = (GameCard)(e.getComponent());
					System.out.println("game card clicked " + tempCard.gameID);
					
					GameManager.getMatchesAccess().setMatchNum(tempCard.getGameID());
					
					if(!GameManager.getMatchesAccess().checkForFullGame()) {
						// If the game is not full
						refreshTimer.stop();
						MathGame.getUser().setPlayerID(2);//TODO: Update this for any number of players
						MathGame.showMenu(MathGame.Menu.GAME);
						
						gameManager.joinGame(tempCard.getGameID());
						System.out.println("GAME SET: " + tempCard.getGameID());						
						gameManager.setGame(GameManager.getMatchesAccess().getGame(tempCard.getGameID()));
						
						typeManager.setType(gameManager.getGame().getType());
						typeManager.randomize();
						
						GameManager.getMatchesAccess().setMatchNum(tempCard.getGameID()); 
						System.out.println("MATCHNUM " + GameManager.getMatchesAccess().getMatchNum());
						
						MathGame.getSidePanel().startTimer(tempCard.getType());
						MathGame.getSidePanel().setUpMultiplayer();
					} else {
						JOptionPane.showMessageDialog(MathGame.getMenu(MathGame.Menu.MULTIMENU).getTopLevelAncestor(), "This game is full");
						GameManager.getMatchesAccess().setMatchNum(-1); // The game is full, so do not join
					}
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}

				@Override
				public void mouseExited(MouseEvent e) {
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseReleased(MouseEvent e) {
				}
				
			});
		}
		
		/**
		 * Add a player to a game
		 * @param u - The User to be added
		 */
		@SuppressWarnings("unused")
		public void addPlayer(User u)	{
			players.add(u);
			numPlayers++;
		}
		
		/**
		 * @return The name of the game
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * @param name - The name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		
		/**
		 * @return The type of game (as a string)
		 */
		public String getType() {
			return type;
		}
		
		/**
		 * @param type - The type to set (as a string)
		 */
		@SuppressWarnings("unused")
		public void setType(String type) {
			this.type = type;
		}
		
		/**
		 * @return The number of players
		 */
		@SuppressWarnings("unused")
		public int getNumberOfPlayers() {
			return numPlayers;
		}
		
		/**
		 * @param numberOfPlayers - The number of players to set
		 */
		@SuppressWarnings("unused")
		public void setNumberOfPlayers(int numberOfPlayers) {
			this.numPlayers = numberOfPlayers;
		}
		
		/**
		 * @return The game's ID
		 */
		public int getGameID() {
			return gameID;
		}
		
		public String getScoring() {
			return scoring;
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			setBackground(Color.lightGray);	// green before			
		}
		
	}
}