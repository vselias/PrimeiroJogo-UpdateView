import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextArea;
import java.awt.Font;

public class DialogInfo extends JDialog {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DialogInfo dialog = new DialogInfo();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DialogInfo() {
		setResizable(false);
		setTitle("Informações do jogo");
		setBounds(100, 100, 763, 459);
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 289, GroupLayout.PREFERRED_SIZE)
					.addGap(83))
		);
		
		JTextArea txtrInformaesMoverA = new JTextArea();
		txtrInformaesMoverA.setWrapStyleWord(true);
		txtrInformaesMoverA.setLineWrap(true);
		txtrInformaesMoverA.setEditable(false);
		txtrInformaesMoverA.setTabSize(5);
		txtrInformaesMoverA.setFont(new Font("Monospaced", Font.BOLD, 16));
		txtrInformaesMoverA.setText("Informações:\r\n\r\nMover a tela: Para arrastar a tela clique na barra de tarefas do menu onde possui botões como: jogar, modo entre outros, basta clicar e arrastar essa barra que a tela se move;\r\n\r\nRedimensionar a tela: clique no painel do jogo onde as sacolas caem e arraste para redimensionar o tamanho da tela;\r\n\r\n");
		scrollPane.setViewportView(txtrInformaesMoverA);
		getContentPane().setLayout(groupLayout);
	}
}
