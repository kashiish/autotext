package main.java.kashiish.autotext;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

@SuppressWarnings("serial")
public class AutoTextGUI extends JFrame implements ActionListener {
	
	/* Height of JFrame window */
	private static final int WINDOW_HEIGHT = 300;
	/* Width of JFrame window */
	private static final int WINDOW_WIDTH = 500;
	
	/* Text field object */
	private JTextField textField;
	/* Scroll Pane for autocomplete suggestions */
	private JScrollPane scrollPane;
	/* Autocomplete suggestions list */
	private JList<String> suggestionsList;
	/* Label for entered text */
	private JLabel submitLabel;
	/* Label for autocorrected words */
	private JLabel autocorrectionLabel;
	/* AutoText instance */
	private AutoText autotext;
	/* textField.setText interferes with DocumentListener, so we need a flag for when text field text 
	 * is updated with selected value from autocomplete suggestions. This makes sure that DocumentListener's
	 * update method is not called. 
	 */
	private boolean updateFromSelectedValue = false;
	/* Document Listener for text field */
	private DocumentListener documentListener;
	/* List Selection Listener for suggestions list */
	private ListSelectionListener listListener;
	
	public AutoTextGUI() {
		
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setLayout(new FlowLayout());
		this.getContentPane().setBackground(Color.WHITE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		try {
			
			this.autotext = new AutoText("src/main/resources/20k.txt");
	
			initializeTextField();
			
			initializeAutocompleteSuggestionsList();
			
			addComponents();

		} catch (IOException e) {
			JLabel errorLabel = new JLabel(e.getMessage());
			this.add(errorLabel);
		}
		
        this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String text = this.textField.getText();
		String[] words = text.split(" ");
		this.submitLabel.setText("You entered: " + text);
		StringBuilder autocorrectionLabelText = new StringBuilder("<html><body>");
		for(String word : words) {
			ArrayList<String> corrections = autotext.autocorrect(word);
			if(corrections == null) continue;
			autocorrectionLabelText.append("Found corrections for " + word + ": " + String.join(", ", corrections) + "<br>");
		}
		
		autocorrectionLabelText.append("</body></html>");
		
		this.autocorrectionLabel.setText(autocorrectionLabelText.toString());
		
	}
	
	private void addComponents() {
		
		JPanel autocompletePanel = new JPanel();
        autocompletePanel.setLayout(new BoxLayout(autocompletePanel, BoxLayout.Y_AXIS));
        autocompletePanel.setPreferredSize(new Dimension(WINDOW_WIDTH, 200));
        
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        
        this.submitLabel = new JLabel();
		this.autocorrectionLabel = new JLabel();
		
		autocompletePanel.add(this.textField);
		autocompletePanel.add(this.scrollPane);
		resultPanel.add(this.submitLabel);
		resultPanel.add(this.autocorrectionLabel);
		
		add(autocompletePanel);
		add(resultPanel);
	}
	
	private void initializeAutocompleteSuggestionsList() {
		
		listListener = new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting() && suggestionsList.getSelectedValue() != null) {
					String[] words = textField.getText().split(" ");
					words[words.length - 1] = suggestionsList.getSelectedValue();
					//textField.setText() interferes with DocumentListener so we set updateFromSelectedValue to true
					SwingUtilities.invokeLater(new Runnable() {
					    public void run() { 
					    	updateFromSelectedValue = true;
							textField.setText(String.join(" ", words)); 
							updateFromSelectedValue = false;
					    }
					});
				}
				
			}
			
		};
		
		this.suggestionsList = new JList<String>();
		this.suggestionsList.addListSelectionListener(listListener);
		
		this.scrollPane = new JScrollPane(suggestionsList);
		this.scrollPane.setBorder(BorderFactory.createEmptyBorder());
		this.suggestionsList.setFocusable(false);
	}
	
	private void initializeTextField() {
		
		documentListener = new DocumentListener() {
			private Document doc;
			//document listener should not update when updatedFromSelectedValue is set to true
			@Override
			public void insertUpdate(DocumentEvent e) {if(!updateFromSelectedValue) updateAutocomplete(e);}

			@Override
			public void removeUpdate(DocumentEvent e) {if(!updateFromSelectedValue) updateAutocomplete(e);}

			@Override
			public void changedUpdate(DocumentEvent e) {if(!updateFromSelectedValue) updateAutocomplete(e);}
			
			private void updateAutocomplete(DocumentEvent e) {
				doc = (Document) e.getDocument();
				try {
					String text = doc.getText(0, doc.getLength());
					String[] words = text.split(" ");
					ArrayList<String> suggestions = autotext.autocomplete(words[words.length - 1].trim());
					DefaultListModel<String> model = new DefaultListModel<String>();
					for(String suggestion : suggestions) { model.addElement(suggestion); }
					suggestionsList.setModel(model);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
			}
			
		};
		
		this.textField = new JTextField(50);
		this.textField.addActionListener(this);
		this.textField.getDocument().addDocumentListener(documentListener);		
	}
	
	public static void main(String[] args) {
		new AutoTextGUI();
	}

}
