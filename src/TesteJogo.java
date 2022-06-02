import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import jaco.mp3.player.MP3Player;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.InputEvent;

public class TesteJogo extends JFrame {

	private JPanel contentPane;
	public JLabel sacola = new JLabel();
	private JButton pah;
	private Retangulo rectangleSacola;
	private Retangulo rectanglePah;
	private List<Retangulo> sacolas = new ArrayList<Retangulo>();
	private JMenuBar menu;
	private JMenu menuJogo;
	private JMenuItem menuItemJogar;
	int lastX, lastY;
	private JRadioButtonMenuItem rdPausarJogo;
	private JMenu menuInfo;
	private JMenuItem mntmNewMenuItem;
	private int pts = 0;
	private int mouseX;
	private int mouseY;
	private int mousePX = 0;
	private int mousePY = 0;
	private JLabel labelPts;
	private JLabel labelVidas;
	private final static int SACOLA_WIDTH = 65;
	private final static int SACOLA_HEIGHT = 65;
	private final static int PAH_WIDTH = 65;
	private final static int PAH_HEIGHT = 65;
	boolean entrou = false;
	private int frameWidth = 848;
	static TesteJogo frame;
	private float velocidadeSacola = 20;
	private int tempNovaSacola = 3000;
	boolean selectedMouse = false;
	boolean selectedPause = false;
	int contFaseUm = 0;
	int contFaseDois = 0;
	int vidas = 3;
	int levelGame = 0;
	static int cont = 0;
	JLabel tSacola;
	int x = 0;
	int index = 0;
	private ThreadMoveSacola threadMoverSacola;
	private ThreadCriaSacola threadCriaSacola;
	private Thread threadExplosaoMouse;
	private Thread threadExplosao;
	private Thread threadVerificaImpactoMouse;
	private Thread threadDrawMouse;
	int valorEncontrado = 0;
	private JLabel lblLevel;
	private JLabel lblTempSacola;
	private JLabel lblVelocidadeSacola;
	private Rectangle recMouse = new Rectangle();
	private Retangulo recSacola = new Retangulo();
	private JCheckBoxMenuItem chckbxmntmNewCheckItem;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new TesteJogo();
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
	public TesteJogo() {

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				// System.out.println("ComponentMoved");
				if (vidas == 0)
					novoLimparSacolas();
			}

			@Override
			public void componentResized(ComponentEvent e) {
				// System.out.println("ComponentResized");
				resizeImgPanel();
			}

			@Override
			public void componentShown(ComponentEvent e) {
				// System.out.println("ComponentShown");
				resizeImgPanel();
				moverPah();
			}
		});
		addKeyListener(new KeyAdapter() {

			int velocidadeX = 25;

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {

					if (pah.getX() >= pah.getWidth()) {
						pah.setBounds(pah.getX() - velocidadeX, contentPane.getHeight() - pah.getHeight(),
								pah.getWidth(), pah.getHeight());
					}
				}

				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					if (pah.getX() <= (contentPane.getWidth() - (pah.getWidth() * 1.5))) {
						pah.setBounds(pah.getX() + velocidadeX, contentPane.getHeight() - pah.getHeight(),
								pah.getWidth(), pah.getHeight());
					}
				}
			}
		});
		setUndecorated(true);
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		setTitle("Meu primeiro jogo ;)");
		setFocusable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 600);
		setLocationRelativeTo(null);
		menu = new JMenuBar();
		menu.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				setLocation(frame.getX() + e.getX() - mousePX, frame.getY() + e.getY() - mousePY);

			}
		});
		menu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				desativarCirculoMouse();
			}

			@Override
			public void mousePressed(MouseEvent e) {

				mousePX = e.getX();
				mousePY = e.getY();
			}
		});
		setJMenuBar(menu);

		menuJogo = new JMenu("Jogar");
		menuJogo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {

				desativarCirculoMouse();
			}
		});
		menu.add(menuJogo);
		menuItemJogar = new JMenuItem("Nogo jogo");
		menuItemJogar.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		menuItemJogar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startGame();

			}
		});
		menuJogo.add(menuItemJogar);

		rdPausarJogo = new JRadioButtonMenuItem("Pausar jogo");
		rdPausarJogo.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		rdPausarJogo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AbstractButton abstractButton = (AbstractButton) e.getSource();
				selectedPause = abstractButton.getModel().isSelected();
				if (selectedPause) {
					threadMoverSacola.suspend();
					threadCriaSacola.suspend();
					JOptionPane.showMessageDialog(frame, "Jogo pausado!");
				} else {
					threadMoverSacola.resume();
					threadCriaSacola.resume();
					JOptionPane.showMessageDialog(frame, "Jogo despausado!");
				}

			}
		});
		menuJogo.add(rdPausarJogo);

		JMenu menuModo = new JMenu("Modo");
		menuModo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				desativarCirculoMouse();
			}
		});
		menu.add(menuModo);

		chckbxmntmNewCheckItem = new JCheckBoxMenuItem("Ativar mouse");
		chckbxmntmNewCheckItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		chckbxmntmNewCheckItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				AbstractButton abstractButton = (AbstractButton) e.getSource();
				selectedMouse = abstractButton.getModel().isSelected();

				if (selectedMouse) {
					ativarMouse();
				} else {
					desativarMouse();
				}
			}

		});
		menuModo.add(chckbxmntmNewCheckItem);

		menuInfo = new JMenu("Info");
		menu.add(menuInfo);

		mntmNewMenuItem = new JMenuItem("Funcionalidades");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DialogInfo info = new DialogInfo();
				info.setLocationRelativeTo(frame);
				info.setVisible(true);
			}
		});
		mntmNewMenuItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		menuInfo.add(mntmNewMenuItem);

		contentPane = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				Image img = Toolkit.getDefaultToolkit().getImage(TesteJogo.class.getResource("jogo3.png"));
				g.drawImage(img, 0, 0, getWidth(), getHeight(), null);

			}

		};
		contentPane.setSize(935, 935);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				lastX = e.getX();
				lastY = e.getY();
			}
		});
		contentPane.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int xOnScreen = e.getXOnScreen();
				int yOnScreen = e.getYOnScreen();
				setSize(e.getXOnScreen() + (e.getX() - lastX), e.getYOnScreen() + (e.getY() - lastY));
				if (selectedMouse) {
					removerPah();
				} else {
					moverPah();
				}
			}
		});

		JPanel panel_1 = new JPanel();
		panel_1.setOpaque(false);

		JPanel panel = new JPanel();
		panel.setOpaque(false);

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane
				.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING,
						gl_contentPane.createSequentialGroup()
								.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, 570, Short.MAX_VALUE)
								.addComponent(panel, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE)
								.addComponent(panel, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
						.addContainerGap(426, Short.MAX_VALUE)));

		JLabel lblClose = new JLabel("X");
		lblClose.setPreferredSize(new Dimension(20, 20));
		lblClose.setOpaque(true);
		lblClose.setBackground(Color.GRAY);
		lblClose.setHorizontalAlignment(SwingConstants.CENTER);
		lblClose.setFocusable(false);
		lblClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int showConfirmDialog = JOptionPane.showConfirmDialog(frame, "Deseja sair?", "Confirmação",
						JOptionPane.YES_NO_OPTION);
				if (showConfirmDialog == JOptionPane.YES_OPTION) {
					System.exit(0);

				} else {
					JOptionPane.showMessageDialog(frame, "Você cancelou!");
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				lblClose.setBackground(Color.red);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblClose.setBackground(Color.GRAY);
			}
		});

		JLabel lblMinimize = new JLabel("–");
		lblMinimize.setPreferredSize(new Dimension(20, 20));
		lblMinimize.setBackground(Color.GRAY);
		lblMinimize.setOpaque(true);
		lblMinimize.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.setState(Frame.ICONIFIED);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				lblMinimize.setBackground(Color.green);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblMinimize.setBackground(Color.GRAY);
			}
		});
		lblMinimize.setHorizontalAlignment(SwingConstants.CENTER);
		lblMinimize.setForeground(Color.LIGHT_GRAY);
		lblMinimize.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblClose.setForeground(Color.LIGHT_GRAY);
		lblClose.setFont(new Font("Tahoma", Font.BOLD, 16));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				gl_panel.createSequentialGroup().addContainerGap(24, Short.MAX_VALUE)
						.addComponent(lblMinimize, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(lblClose, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGap(5).addGroup(gl_panel
						.createParallelGroup(Alignment.BASELINE).addComponent(lblClose).addComponent(lblMinimize))
						.addGap(12)));
		panel.setLayout(gl_panel);

		lblTempSacola = new JLabel("Tempo:" + tempNovaSacola);
		lblTempSacola.setFont(new Font("Dialog", Font.BOLD, 14));
		lblTempSacola.setForeground(Color.LIGHT_GRAY);

		lblVelocidadeSacola = new JLabel("Velocidade: " + velocidadeSacola);
		lblVelocidadeSacola.setFont(new Font("Dialog", Font.BOLD, 14));
		lblVelocidadeSacola.setForeground(Color.LIGHT_GRAY);

		labelVidas = new JLabel("Vidas: 3");
		labelVidas.setForeground(Color.LIGHT_GRAY);
		labelVidas.setFont(new Font("Dialog", Font.BOLD, 14));

		lblLevel = new JLabel("Nível: 0");
		lblLevel.setForeground(Color.LIGHT_GRAY);
		lblLevel.setFont(new Font("Dialog", Font.BOLD, 14));

		labelPts = new JLabel("Pontos: 0");
		labelPts.setForeground(Color.LIGHT_GRAY);
		labelPts.setFont(new Font("Dialog", Font.BOLD, 14));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addComponent(lblTempSacola, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
								.addComponent(lblVelocidadeSacola, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
								.addComponent(labelPts, GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
								.addComponent(labelVidas, GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
								.addComponent(lblLevel, GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
						.addContainerGap()));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap().addComponent(lblTempSacola)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblVelocidadeSacola)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(labelPts)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(labelVidas)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblLevel)
						.addContainerGap(104, Short.MAX_VALUE)));
		panel_1.setLayout(gl_panel_1);
		contentPane.setLayout(gl_contentPane);

		setContentPane(contentPane);
		carregarThreadGame();
		playSongControlVol("songs/song.mp3", -10f);
		definirPosicaoRetanguloImpacto();
	}

	protected void moverPah() {
		this.pah.setBounds(contentPane.getWidth() / 2, (contentPane.getHeight() - pah.getHeight()), PAH_WIDTH,
				PAH_HEIGHT);
		contentPane.add(this.pah);
		refreshPanel();
	}

	public void carregarThreadGame() {
		try {
			criarSacolaThread();
			moverSacolaThread();
			criarPah();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

	}

	private void moverSacolaThread() throws InterruptedException {
		threadMoverSacola = new ThreadMoveSacola();
		threadMoverSacola.start();
	}

	private void definirLVLGame() {

		boolean desativarLVL = true;

		if (!desativarLVL) {
			if (pts > 0 && pts != valorEncontrado) {

				if (velocidadeSacola > 3.5 && pts != valorEncontrado) {
					levelGame += 1;
					lblLevel.setText("Nivel: " + levelGame);
					velocidadeSacola -= (0.3 * velocidadeSacola);
				}
				if (tempNovaSacola > (700) && pts != valorEncontrado) {
					tempNovaSacola -= (0.3 * tempNovaSacola);
				}
				valorEncontrado = pts;
				exibirStatusVelocidade();

			}
		}

	}

	private void exibirStatusVelocidade() {
		lblTempSacola.setText("Tempo: " + tempNovaSacola);
		lblVelocidadeSacola.setText("Velocidade: " + velocidadeSacola);
	}

	public void resetLevelGame() {
		contFaseDois = 0;
		contFaseUm = 0;
		velocidadeSacola = 20;
		tempNovaSacola = 3000;
	}

	public void verificarImpactoSacola() throws InterruptedException {

		for (int i = 0; i < contentPane.getComponentCount(); i++) {
			if (contentPane.getComponent(i) instanceof JLabel) {
				sacola = (JLabel) contentPane.getComponent(i);
				definirPosicaoRetanguloImpacto();
				if (rectanglePah.intersects(rectangleSacola)) {
					removerSacolaImpacto();
					criarExplosaoImpacto();
					definirPontuacao();
				} else if (sacola.getY() > contentPane.getHeight()) {
					vidas--;
					labelVidas.setText("Vidas: " + vidas);
				}

				definirDerrota();
			}
		}

	}

	public void novoLimparSacolas() {
		List indices = new ArrayList<JLabel>();
		for (int i = 0; i < contentPane.getComponentCount(); i++) {
			if (contentPane.getComponent(i) instanceof JLabel) {
				indices.add(contentPane.getComponent(i));
			}
		}
		for (int i = 0; i < indices.size(); i++) {
			JLabel sacola = (JLabel) indices.get(i);
			sacola = null;
			contentPane.remove((JLabel) indices.get(i));
			System.gc();
			refreshPanel();
		}

	}

	public void limparSacolasEncontradas(List<JLabel> sacolas) {
		if (sacolas != null && !sacolas.isEmpty()) {
			for (int i = 0; i < sacolas.size(); i++) {
				contentPane.remove((JLabel) sacolas.get(i));
				refreshPanel();
			}

		}

	}

	public void definirDerrota() {

		boolean desativarDerrota = true;

		if (!desativarDerrota) {
			if (vidas == 0) {
				JOptionPane.showMessageDialog(frame, "Voc� perdeu! tente na proxima vez... :( ");
				suspendGame();
			}

		}
	}

	private void suspendGame() {
		if (threadCriaSacola.isAlive())
			threadCriaSacola.stop();
		if (threadMoverSacola.isAlive())
			threadMoverSacola.stop();
		resetLevelGame();

	}

	private void startGame() {
		try {
			resetLabelsNovoGame();
			suspendGame();
			novoLimparSacolas();
			Thread.sleep(500);
			startThreadGame();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public JLabel encontrarSacolaDif(JLabel sacolaEncontrada, Iterator<Component> iterator) {

		for (Iterator iter = iterator; iter.hasNext();) {
			if (iter.next() instanceof JLabel) {
				JLabel sacolaList = (JLabel) iter.next();
				if (!sacolaList.getText().equals(sacolaEncontrada.getText())) {
					return sacolaList;
				} else {
					return (JLabel) iter.next();
				}
			}

		}

		return null;
	}

	private void startThreadGame() {
		if (!threadMoverSacola.isAlive()) {
			threadMoverSacola = new ThreadMoveSacola();
			threadMoverSacola.start();
		}
		if (!threadCriaSacola.isAlive()) {
			threadCriaSacola = new ThreadCriaSacola();
			threadCriaSacola.start();
		}
	}

	private void resetLabelsNovoGame() {
		pts = 0;
		vidas = 3;
		levelGame = 0;
		labelPts.setText("Pontos: " + pts);
		labelVidas.setText("Vidas: " + vidas);
		lblLevel.setText("Nível: 0");
	}

	private void definirPontuacao() throws InterruptedException {
		pts += 10;
		labelPts.setText("Pontos: " + pts);
		// adicionarInfoPts();
	}

	private void definirPontuacaoMouse(int qtd) throws InterruptedException {
		pts += qtd * 10;
		labelPts.setText("Pontos: " + pts);
		// adicionarInfoPts();
	}

	private void criarExplosaoImpacto() throws InterruptedException {

		threadExplosao = new Thread(() -> {
			try {

				JLabel explosao = new JLabel();
				explosao.setIcon(new ImageIcon("src/explosao.png"));
				explosao.setBounds(pah.getX() - pah.getWidth(), pah.getY() - 15, 64, 64);
				contentPane.add(explosao);
				Thread.sleep(20);
				contentPane.remove(explosao);
				explosao = null;
				playSong("songs/saco-estouro.mpeg");
				refreshPanel();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		});
		threadExplosao.start();

	}

	private void criarExplosaoImpactoMouse(int x, int y) throws InterruptedException {

		threadExplosaoMouse = new Thread(() -> {
			try {
				JLabel explosao = new JLabel();
				explosao.setIcon(new ImageIcon("src/explosao.png"));
				explosao.setSize(54, 54);
				explosao.setBounds(mouseX - 70, mouseY - 70, 54, 54);
				explosao.setFocusable(false);
				contentPane.add(explosao);
				// playSong("songs/saco-estouro.mpeg");
				Thread.sleep(20);
				contentPane.remove(explosao);
				explosao = null;
				refreshPanel();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		});
		threadExplosaoMouse.start();

	}

	private void adicionarInfoPts() throws InterruptedException {
		JLabel pontos = new JLabel();
		pontos.setIcon(new ImageIcon("src/10pts.png"));
		pontos.setBounds(pah.getX() + 20, pah.getY() - 30, 100, 100);
		contentPane.add(pontos);
		// Thread.currentThread().sleep(50);
		contentPane.remove(pontos);
		contentPane.repaint();
	}

	private void removerSacolaImpacto(JLabel sac) {
		contentPane.remove(sac);
		sac = null;
		System.gc();
		refreshPanel();
	}

	private void removerSacolaImpacto() {
		contentPane.remove(sacola);
		sacola = null;
		System.gc();
		refreshPanel();
	}

	private void removerSacolaImpactoMouse(JLabel tSacola) {
		try {
			System.out.println("sacola removida " + sacola.getText());
			for (int i = 0; i < contentPane.getComponentCount(); i++) {
				if (contentPane.getComponent(i) instanceof JLabel) {
					sacola = (JLabel) contentPane.getComponent(i);
					if (sacola.getText().equals(tSacola.getText())) {
						contentPane.remove(sacola);
						definirPontuacao();
					}
				}
			}
			refreshPanel();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Deprecated
	// antigo remover usando lista com retangulo
	private void removerSacolaImpacto(Retangulo sacola) {

		for (int i = 0; i < contentPane.getComponentCount(); i++) {
			if (contentPane.getComponent(i) instanceof JLabel) {
				JLabel sacolaRemover = (JLabel) contentPane.getComponent(i);
				if (sacolaRemover.getText().equals(sacola.getToken())) {
					contentPane.remove(sacolaRemover);
					sacolaRemover.setBounds(-6000, contentPane.getHeight() + 6000, 0, 0);
					sacolaRemover = null;
					// refreshPanel();
				}
			}
		}

	}

	@Deprecated
	// antigo remover com array porem da pau por falta de index alterando tamanho do
	// array
	public void limparSacolasDerrota() {
		refreshPanel();
		int sacolaRemovida = 0;
		// System.out.println("Count >>> " + contentPane.getComponentCount());
		for (int x = 0; x < contentPane.getComponentCount(); x++) {
			if (contentPane.getComponent(x) instanceof JLabel) {
				sacola = (JLabel) contentPane.getComponent(x);
				// if (sacola.getText().length() == 0 || sacola.getText().isBlank() ||
				// sacola.getText().isEmpty()) {
				contentPane.remove(sacola);
				sacola.setBounds(-1000, -1000, 0, 0);
				refreshPanel();
				sacolaRemovida++;
				// System.out.println("Count >>> " + contentPane.getComponentCount());
				// System.out.println("Qtd sacola removida: " + sacolaRemovida);
				// }
			}
		}
		refreshPanel();

	}

	@Deprecated
	// antigo remover com array porem da pau por falta de index alterando tamanho do
	// array
	private void removerSacolaPerdida() {

		// remover componentes da tela
		for (int i = 0; i < contentPane.getComponentCount(); i++) {
			if (contentPane.getComponent(i) != null && contentPane.getComponent(i) instanceof JLabel) {
				sacola = (JLabel) contentPane.getComponent(i);
				if (sacola.getY() > contentPane.getHeight()) {
					contentPane.remove(sacola);
					sacola.setBounds(-6000, contentPane.getHeight() + 6000, 0, 0);
					sacola = null;
					// refreshPanel();
				}
			}

		}

		// tentativa de remover sacolas perdidas com iterator
		for (Iterator<Retangulo> i = sacolas.iterator(); i.hasNext();) {
			Retangulo sacolaRemover = i.next();
			if (sacolaRemover.getHeight() > contentPane.getHeight()) {
				sacolaRemover.setBounds(-5000, -1000, 0, 0);
				sacolaRemover = null;
				i.remove();
			}
		}
		// System.out.println("Qtd de sacolas lista: " + sacolas.size());

	}

	public void novoRemoverSacolaPerdida() {

		List<JLabel> listSacolaPerdida = new ArrayList<JLabel>();
		for (Component c : contentPane.getComponents()) {
			if (c instanceof JLabel) {
				sacola = (JLabel) c;
				if (sacola.getY() > contentPane.getHeight())
					listSacolaPerdida.add(sacola);
			}
		}
		for (JLabel sac : listSacolaPerdida) {
			contentPane.remove(sac);
			sac = null;
			System.gc();
			refreshPanel();
			System.out.println("Chamou o metodo de remover sacolas nao estouradas!");
		}
	}

	private void desativarMouse() {
		JOptionPane.showMessageDialog(frame, "Desativado");
		contentPane.removeMouseMotionListener(verificaMouseImpacto);
		contentPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		criarPah();

	}

	private void ativarMouse() {
		contentPane.addMouseMotionListener(verificaMouseImpacto);
		contentPane.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		JOptionPane.showMessageDialog(frame, "Ativado");
		removerPah();
	}

	private void removerPah() {
		this.pah.setBounds(-500, -500, 0, 0);
		contentPane.remove(this.pah);
		refreshPanel();
	}

	public void removerSacolasPerdidasIterator(Iterator<JLabel> iterator) {

		while (iterator.hasNext()) {
			JLabel sacola = iterator.next();
			if (sacola.getY() > contentPane.getHeight()) {
				iterator.remove();
				System.out.println(" sacola y " + sacola.getY() + " pane y " + contentPane.getHeight());
				refreshPanel();
			}
		}

	}

	private void definirPosicaoSacola() throws InterruptedException {
		int sacolaX = 0;
		int velocidadeY = 2;
		int posicaoInicialX = 10;
		int posicaoFinalX = contentPane.getWidth() - SACOLA_WIDTH;

		for (int i = 0; i < contentPane.getComponentCount(); i++) {
			// gera numero posicao X da sacola
			int randomNum = ThreadLocalRandom.current().nextInt(-1, 2);

			// cast cada sacola
			if (contentPane.getComponent(i) != null && contentPane.getComponent(i) instanceof JLabel) {
				sacola = (JLabel) contentPane.getComponent(i);

				if (randomNum < 0) {
					sacolaX = sacola.getX() <= posicaoInicialX ? sacola.getX() + (randomNum * (-1))
							: sacola.getX() + randomNum;
					// seta aposicao da sacola
					// System.out.println("Posicao sacola X < 0 " + sacola.getX() + " N�mero
					// random:
					// " + randomNum);
					sacola.setBounds(sacolaX, (sacola.getY() + velocidadeY), sacola.getWidth(), sacola.getHeight());
				} else {
					if (randomNum == 0) {
						randomNum = 1;
					}
					sacolaX = (sacola.getX() > posicaoFinalX ? sacola.getX() - randomNum : sacola.getX() + randomNum);
					// System.out.println("Posicao sacola X > 0 " + sacola.getX() + " N�mero
					// random:
					// " + randomNum);
					// seta a posicao da sacola
					sacola.setBounds(sacolaX, (sacola.getY() + velocidadeY), sacola.getWidth(), sacola.getHeight());
				}
			}
			// definirPosicaoRetanguloImpacto();
		}

	}

	private void definirPosicaoRetanguloImpacto() {
		rectangleSacola = new Retangulo();
		rectanglePah = new Retangulo();
		rectangleSacola.setBounds(sacola.getX(), sacola.getY(), sacola.getWidth(), sacola.getHeight());
		rectanglePah.setBounds(pah.getX(), pah.getY(), pah.getWidth(), pah.getHeight());

	}

	private void refreshPanel() {
		contentPane.revalidate();
		contentPane.repaint();
	}

	private void criarSacolaThread() throws InterruptedException {
		threadCriaSacola = new ThreadCriaSacola();
		threadCriaSacola.start();

	}

	public void criarPah() {
		this.pah = new JButton(new ImageIcon("src/pa.png"));
		this.pah.setSize(PAH_WIDTH, PAH_HEIGHT);
		pah.setBounds(contentPane.getWidth() / 2, (contentPane.getHeight() - pah.getHeight()), pah.getWidth(),
				pah.getHeight());
		buttonTransparent();
		contentPane.add(pah);
		refreshPanel();

	}

	public void desativarSacola(JLabel tSacola, Rectangle recSacola) {
		recSacola = new Rectangle();
		recSacola.setBounds(0, 0, 0, 0);
		tSacola = new JLabel("removida");
		tSacola.setEnabled(false);
	};

	private void playSong(String pathSong) {
		new Thread(() -> {
			try {
				MP3Player mp3Player = new MP3Player(new File(pathSong));
				mp3Player.play();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}).start();
	}

	private void buttonTransparent() {
		pah.setOpaque(false);
		pah.setContentAreaFilled(false);
		pah.setBorderPainted(false);
		Border emptyBorder = BorderFactory.createEmptyBorder();
		pah.setBorder(emptyBorder);
	}

	public void playSongControlVol(String pathSong, float volume) {

		try {
			AudioInputStream audioStream2 = audioInputStream(pathSong);
			clipAudio(volume, audioStream2);
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	private void clipAudio(float volume, AudioInputStream audioStream2) throws LineUnavailableException, IOException {
		Clip clip = AudioSystem.getClip();
		clip.open(audioStream2);
		clip.start();
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(volume);
	}

	private AudioInputStream audioInputStream(String pathSong) throws UnsupportedAudioFileException, IOException {
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(pathSong));
		AudioFormat baseFormat = audioStream.getFormat();
		AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16,
				baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
		AudioInputStream audioStream2 = AudioSystem.getAudioInputStream(decodedFormat, audioStream);
		return audioStream2;
	}

	private void resizeImgPanel() {
		Image img = Toolkit.getDefaultToolkit().getImage(TesteJogo.class.getResource("jogo3.png"));
		System.out.println("pane w " + contentPane.getWidth() + " pane h " + contentPane.getWidth());
		Image scaledInstance = img.getScaledInstance(contentPane.getWidth(), contentPane.getHeight(),
				Image.SCALE_DEFAULT);

		Graphics graphics = contentPane.getGraphics();
		graphics.drawImage(scaledInstance, 0, 0, contentPane.getWidth(), contentPane.getHeight(), contentPane);
	}

	private void desativarCirculoMouse() {
		Graphics g = contentPane.getGraphics();
		g.drawOval(mouseX = -500, mouseY = -500, 0, 0);
		refreshPanel();
	}

	public enum Derrota {
		SIM, NAO
	}

	class ThreadMoveSacola extends Thread {

		@Override
		public void run() {
			try {

				while (true) {
					definirPosicaoSacola();
					verificarImpactoSacola();
					novoRemoverSacolaPerdida();
					definirLVLGame();
					Thread.sleep((long) velocidadeSacola);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	class ThreadCriaSacola extends Thread {
		boolean terminate = false;
		int posicaoStartSacolaY = 10;

		@Override
		public void run() {

			while (!terminate) {
				String token = "";
				try {
					int posicaoStartSacolaX = ThreadLocalRandom.current().nextInt(SACOLA_WIDTH,
							(frameWidth - SACOLA_WIDTH));
					token = UUID.randomUUID().toString();
					++cont;
					String nome = "sacola" + cont;
					sacola = new JLabel(nome);
					sacola.setIcon(new ImageIcon("src/saco-de-lixo.png"));
					sacola.setBounds(posicaoStartSacolaX, posicaoStartSacolaY, SACOLA_WIDTH, SACOLA_HEIGHT);
					contentPane.add(sacola);
					Thread.sleep(tempNovaSacola);
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}

			}
		}

		public void cancel() {
			this.terminate = true;
		}

		public void open() {
			this.terminate = false;
		}

	}

	MouseMotionAdapter verificaMouseImpacto = new MouseMotionAdapter() {

		@Override
		public void mouseMoved(MouseEvent e) {
			Graphics graphics = contentPane.getGraphics();
			mouseX = e.getX();
			mouseY = e.getY();
			int width = 30;
			int height = 30;

			threadVerificaImpactoMouse = new Thread(() -> {
				recMouse = new Rectangle();
				recSacola = new Retangulo();
				tSacola = new JLabel();
				recMouse.setBounds(mouseX, mouseY, 30, 30);
				try {

					refreshPanel();
					for (Component c : contentPane.getComponents()) {
						if (c instanceof JLabel) {
							tSacola = (JLabel) c;
							recSacola.setBounds(tSacola.getX(), tSacola.getY(), 65, 65);
							if (recMouse.intersects(recSacola) && !selectedPause) {
								removerSacolaImpacto(tSacola);
								definirPontuacao();
								criarExplosaoImpactoMouse(mouseX, mouseY);
								refreshPanel();
								break;
							} else {
								continue;
							}

						}
					}
					Thread.sleep(500);
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			});
			threadVerificaImpactoMouse.start();

		}
	};

}
