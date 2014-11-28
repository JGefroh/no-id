package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Comparator;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ToolTipManager;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import program.Information;

public class SearchWindow extends JFrame
{
	//private JFrame this;
	private JPanel panelBasket;
	private JPanel panelUppermost;
	private JPanel panelForm;
	private JPanel panelResults;
	//Menu Bar
	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenu menuHelp;
	private JMenu menuRecord;
	private JMenuItem menuHelpAbout;
	private JMenuItem menuFileSave;
	private JMenuItem menuFileExitNoSave;
	private JMenuItem menuRecordDelete;
	private JMenuItem menuRecordCreate;
	
	//Status Bar
	private JPanel panelStatusBar;
	private JTextField statusBar;
	//Instructions and Logo
	private JPanel panelFormInstructions;
	private String instructionTxt;	
	private String windowImagePath;
	private JLabel windowImageLabel;
	private JTextPane instructionTxtPane;
	//Input and form submit button
	private JPanel panelFormInput;
	private JLabel inputFieldLabel;
	private JButton btnSubmit;
	private JTextField inputField;
	//Radio Buttons (ID, First, Last, Entries)
	private JPanel panelFormRBtn;
	private JRadioButton rBtnID;
	private JRadioButton rBtnLast;
	private JRadioButton rBtnFirst;
	private JRadioButton rBtnEntries;
	private ButtonGroup rBtnGroup;
	private JLabel rBtnGroupLabel;
	//Search Results
	private JPanel panelTable;
	private JTable recordTable;
	private JScrollPane scrollPaneRecordTable;
	private DefaultTableModel modelDefault;
	//Search Results - Buttons
	private JPanel panelButtons;
	private JButton btnExamine;
	private JButton btnCreate;
	//Misc information
	private final static int PREF_HEIGHT = 600;
	private final static int PREF_WIDTH = 550;
	private final static long serialVersionUID = 0;
	private final static String windowName = "NoID Version " + Information.PROGRAMVERSION;
	//Vital information (unnecessary to pass)
	private int selectedRB = Information.FIELDID;
	
	public SearchWindow()
	{
		initFrameComponents();
		placeFrameComponents();
	}/////////////////////////////////////////////////////////////////////
	/**
	 * Obtain the studentID of the selected JTable row.
	 * @return
	 */
	public String getSelectedRowID()
	{
		int selectedRow = recordTable.getSelectedRow();
		String returnData = null;
		if(selectedRow==-1)
		{//If there is no row selected...
			return null;
		}
		returnData = (String)recordTable.getValueAt(selectedRow, Information.FIELDID);
		return returnData;
	}/////////////////////////////////////////////////////////////////////
	public void setDefaultSelection()
	{
		if(recordTable.getRowCount()>0&&recordTable.getColumnCount()>0)
		{
			recordTable.addColumnSelectionInterval(0, recordTable.getColumnCount()-1);
			recordTable.addRowSelectionInterval(0,0);
		}
	}/////////////////////////////////////////////////////////////////////
	public void addData(final String[] data)
	{
		modelDefault.addRow(data);	//Add data to rows.
	}/////////////////////////////////////////////////////////////////////
	public void setEnabledExamine(final boolean val)
	{
		btnExamine.setEnabled(val);
		if(val==true)
		{
			btnExamine.setToolTipText(null);
		}
		else
		{
			btnExamine.setToolTipText("A record must be selected from the above table to examine it.");
		}

	}/////////////////////////////////////////////////////////////////////
	public String getInputText()
	{
		if(inputField!=null)
		{
			return inputField.getText();
		}
		return null;
	}
	public void setSelectedRB(final int selection)
	{
		selectedRB = selection;
	}
	public int getSelectedRB()
	{
		return selectedRB;
	}
	private void initFrameComponents()
	{
		/*Menu Bar - GUI Component that contains menu options including exiting the program and saving changes.*/
		menuBar 			= new JMenuBar();
		menuFile 			= new JMenu("File");
		menuFileSave		= new JMenuItem("Save/Commit Changes");
		menuFileExitNoSave 	= new JMenuItem("Exit Without Commiting Changes");
		menuHelp 			= new JMenu("Help");
		menuHelpAbout 		= new JMenuItem("About");
		menuRecord			= new JMenu("Record");
		menuRecordDelete	= new JMenuItem("Delete Selected Record...");
		menuRecordCreate	= new JMenuItem("Create a New Record...");
		/*Instruction Text*/
		instructionTxt 		= "     Select the criteria, enter search input, and press the 'Submit' button.         ";
		windowImagePath 	= "./images/NoID_resized.png";
		windowImageLabel 	= new JLabel(new ImageIcon(windowImagePath)); 
		instructionTxtPane 	= new JTextPane();
			instructionTxtPane.setText(instructionTxt);
			instructionTxtPane.setEditable(false);
			instructionTxtPane.setBackground(null);	
		/*Radio Buttons - Select category to search.*/
		rBtnID 		= new JRadioButton("1. Student ID", true);		
		rBtnLast 	= new JRadioButton("2. Last Name");
		rBtnFirst 	= new JRadioButton("3. First Name");
		rBtnEntries = new JRadioButton("4. # Entries (min,max)");	
		/*Input Fields*/
		inputFieldLabel = new JLabel("Search for: ");
			inputFieldLabel.setFocusable(false);		
		inputField 		= new JTextField(30);
		btnSubmit 		= new JButton("Submit.");
		/*Further Examination*/
		btnExamine 		= new JButton("Examine Record...");
		btnCreate 		= new JButton("Create Record...");
		/*Status Bar*/
		statusBar = new JTextField();
			statusBar.setEditable(false);
			statusBar.setBorder(null);
			
		inputFieldLabel.setDisplayedMnemonic('S');
		menuHelp.setMnemonic('H');
		menuFile.setMnemonic('F');
		menuRecord.setMnemonic('R');
		menuHelpAbout.setMnemonic('A');
		btnExamine.setMnemonic('E');
		btnCreate.setMnemonic('C');
		rBtnID.setMnemonic('1');
		rBtnLast.setMnemonic('2');
		rBtnFirst.setMnemonic('3');
		rBtnEntries.setMnemonic('4');
		
		inputField.setFocusAccelerator('S');
		menuFileSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
		menuFileExitNoSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Event.CTRL_MASK));
		menuRecordCreate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));
		menuRecordDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Event.CTRL_MASK));
		
		menuFile.add(menuFileSave);
		menuFile.add(menuFileExitNoSave);
		menuHelp.add(menuHelpAbout);
		menuBar.add(menuFile);
		menuBar.add(menuHelp);
		menuBar.add(menuRecord);
		menuRecord.add(menuRecordCreate);
		menuRecord.add(menuRecordDelete);
		
		menuFileExitNoSave.addActionListener(new MenuFileExitNoSaveListener());
		menuFileSave.addActionListener(new MenuFileSaveListener());
		menuHelpAbout.addActionListener(new MenuHelpAboutListener());
		menuRecordDelete.addActionListener(new MenuRecordDeleteListener());
		menuRecordCreate.addActionListener(new BtnCreateSearchListener());
		inputField.addActionListener(new BtnSubmitSearchListener());
		rBtnID.addActionListener(new RBtnListener());
		rBtnFirst.addActionListener(new RBtnListener());
		rBtnLast.addActionListener(new RBtnListener());
		rBtnEntries.addActionListener(new RBtnListener());
		btnCreate.addActionListener(new BtnCreateSearchListener());
		btnExamine.addActionListener(new BtnExamineSearchListener());
		btnSubmit.addActionListener(new BtnSubmitSearchListener());

		rBtnID.setActionCommand(Information.FIELDID+"");
		rBtnFirst.setActionCommand(Information.FIELDFIRST+"");
		rBtnLast.setActionCommand(Information.FIELDLAST+"");
		rBtnEntries.setActionCommand(Information.FIELDENTRIES+"");
	
		/*
		 * JTable - Contains the results of the search.
		 */
		recordTable = new JTable(){/*OVERRIDES METHODS FOR FUNCTIONALITY*/
			static final long serialVersionUID=0;	//Get rid of warning.
			public boolean getScrollableTracksViewportWidth()
			{
				return true;
			}
		};	//Makes it scrollable.
		modelDefault = new DefaultTableModel() {/*OVERRIDES METHODS FOR FUNCTIONALITY*/
			static final long serialVersionUID=0;	//Get rid of warning.
			public boolean isCellEditable(int row, int col)
			{
				return false;
			}
		};//DefaultTableModel
			recordTable.setModel(modelDefault);
			recordTable.setRowHeight(15);
			recordTable.setFillsViewportHeight(false);
			recordTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			recordTable.setRowSelectionAllowed(true);
			recordTable.getTableHeader().setReorderingAllowed(false);
			//recordTable.setAutoCreateRowSorter(true);
			TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(modelDefault);	//Create a sort
			recordTable.setRowSorter(sorter);							//Sort!
			ToolTipManager.sharedInstance().unregisterComponent(recordTable);
			ToolTipManager.sharedInstance().unregisterComponent(recordTable.getTableHeader());
		//Orders the columns according to global settings (prevents mismanipulation due to programmer oversight).
		for(int index=0;index<4;index++)
		{
			switch(index)
			{
			case Information.FIELDID:
				modelDefault.addColumn("Student ID #");
				break;
			case Information.FIELDFIRST:
				modelDefault.addColumn("First Name");
				break;
			case Information.FIELDLAST:
				modelDefault.addColumn("Last Name");
				break;
			case Information.FIELDENTRIES:
				modelDefault.addColumn("# Entries");
				break;
			}
		}
		sorter.setComparator(Information.FIELDID, new StudentIDComparator());
		sorter.setComparator(Information.FIELDLAST, new LastNameComparator());
		sorter.setComparator(Information.FIELDFIRST, new FirstNameComparator());
		sorter.setComparator(Information.FIELDENTRIES, new EntriesComparator());

		setEnabledExamine(false);
	}
	private void placeFrameComponents()
	{
		/*JMenuBar - Place the JMenuBar onto the frame.*/
		this.setJMenuBar(menuBar);
		/*Status Bar - Place the Status Bar onto the frame.*/
		panelStatusBar = new JPanel(new BorderLayout());
			panelStatusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
			panelStatusBar.add(statusBar, BorderLayout.CENTER);
		/*Form Instructions - Place the instructions onto its own panel*/
		panelFormInstructions = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx=0;	//
		gbc.gridy=0;	//
		panelFormInstructions.add(windowImageLabel, gbc);
		gbc.gridx++;
		panelFormInstructions.add(instructionTxtPane, gbc);
		/*Form Input - Place the input field and submit button onto its own panel.*/
		panelFormInput = new JPanel(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx=0;
		gbc.gridx=0;
			panelFormInput.add(inputFieldLabel, gbc);
		gbc.gridx++;
			panelFormInput.add(inputField, gbc);
		gbc.gridx++;
			panelFormInput.add(btnSubmit, gbc);
		/*Form Radio Buttons*/
		rBtnGroupLabel = new JLabel("Search by: ");
		rBtnGroup = new ButtonGroup();
		rBtnGroup.add(rBtnID);
		rBtnGroup.add(rBtnFirst);
		rBtnGroup.add(rBtnLast);
		rBtnGroup.add(rBtnEntries);
			
		panelFormRBtn = new JPanel();
		panelFormRBtn.add(rBtnGroupLabel);
		panelFormRBtn.add(rBtnID);		
		panelFormRBtn.add(rBtnLast);		
		panelFormRBtn.add(rBtnFirst);
		panelFormRBtn.add(rBtnEntries);
		/*panel Form*/
		panelForm = new JPanel(new BorderLayout());
		panelForm.add(panelFormInstructions, BorderLayout.NORTH);
		panelForm.add(panelFormRBtn, BorderLayout.CENTER);
		panelForm.add(panelFormInput, BorderLayout.SOUTH);
		/*Results Table*/
		panelTable = new JPanel();
		scrollPaneRecordTable = new JScrollPane(recordTable); 				//Stick table into scrollpane
		scrollPaneRecordTable.setPreferredSize(new Dimension(PREF_WIDTH-50, PREF_HEIGHT/2));
		panelTable.add(scrollPaneRecordTable);
		/*Results Buttons*/
		panelButtons = new JPanel();
		panelButtons.add(btnExamine);
		panelButtons.add(btnCreate);
		/*panel Results*/
		panelResults = new JPanel(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx=0;
		gbc.gridy=0;
			panelResults.add(panelTable, gbc);
		gbc.gridy++;
			panelResults.add(panelButtons, gbc);	
		/*panel Parent*/
		panelBasket = new JPanel(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx=0;
		gbc.gridy=0;
			panelBasket.add(panelForm,gbc);
		gbc.gridy++;
			panelBasket.add(panelResults, gbc);
			
		/*Frame Settings*/
		panelUppermost = new JPanel(new BorderLayout());
		panelUppermost.add(panelBasket, BorderLayout.CENTER);
		panelUppermost.add(panelStatusBar, BorderLayout.SOUTH);
		
		this.add(panelUppermost);	//Add panel to the frame.
		this.pack();
		this.setTitle(windowName);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(PREF_WIDTH, PREF_HEIGHT));
		this.setResizable(false);
		this.addWindowListener(new SearchWindowExitListener());
		
		//Center the window to the screen...
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width/2)-(PREF_WIDTH/2);	//X Center
		int y = (screen.height/2)-(PREF_HEIGHT/2);	//Y Center
		this.setBounds(x,y, PREF_WIDTH, PREF_HEIGHT);	//Center
		
		this.setVisible(true);
		this.setEnabled(true);
		updateStatus("Ready");
	}
	/**
	 * Add the finalized panels to the parent panel, and finalize the frame.
	 */
	public void updateStatus(final String newStatus)
	{
		if(statusBar!=null)
		{
			statusBar.setText("Program Status: " + newStatus);
		}
		//statusBar.repaint();
	}
	public void resetWindow()
	{
		rBtnID.setSelected(true);
		inputField.setText(null);
	}
	public void clearTable()
	{
		while(modelDefault.getRowCount()>0)
		{
			modelDefault.removeRow(0);
		}
	}
	public void revalidateTable()
	{
		recordTable.revalidate();
	}
	public boolean isRecordSelected()
	{
		if(recordTable.getSelectedRow()!=-1)
		{
			return true;
		}
		return false;
	}
}
class RBtnListener implements ActionListener
{
	public void actionPerformed(ActionEvent ev)
	{
		try
		{
			if(ev!=null)
			{
				int selectedRB = Integer.parseInt(ev.getActionCommand());	
				WindowController.getSearchWindow().setSelectedRB(selectedRB);
				switch(selectedRB)
				{
				case Information.FIELDID:
					WindowController.updateStatus("Search type changed to: StudentID.");
				break;
				case Information.FIELDLAST:
					WindowController.updateStatus("Search type changed to: Last Name.");
				break;
				case Information.FIELDFIRST:
					WindowController.updateStatus("Search type changed to: First Name.");
				break;
				case Information.FIELDENTRIES:
					WindowController.updateStatus("Search type changed to: # of Entries.");
				break;
				}
			}
		}
		catch(NumberFormatException e)
		{
			WindowController.updateStatus("Error changing search type.");
		}
	}
}/////////////////////////////////////////////////////////////////////

class BtnSubmitSearchListener implements ActionListener
{
	/**
	 * Submit a search and display the results in the JTable.
	 */
	public void actionPerformed(ActionEvent e)
	{
		WindowController.searchFor();
	}
}/////////////////////////////////////////////////////////////////////
class BtnCreateSearchListener implements ActionListener
{
	public void actionPerformed(ActionEvent e)
	{
		WindowController.getCreateWindow().setEnabled(true);
		WindowController.getCreateWindow().setVisible(true);
		WindowController.getSearchWindow().setEnabled(false);
		
		WindowController.getCreateWindow().resetWindow();
	}
}
class BtnExamineSearchListener implements ActionListener
{
	public void actionPerformed(ActionEvent e)
	{
		if(WindowController.getSearchWindow().isRecordSelected()==true)
		{
			WindowController.getExamineWindow().setEnabled(true);
			WindowController.getExamineWindow().setVisible(true);
			WindowController.getSearchWindow().setEnabled(false);
			WindowController.getExamineWindow().resetWindow();
			WindowController.examineSelected();
		}
		else
		{
			WindowController.getSearchWindow().setDefaultSelection();
		}
	}
}
class MenuHelpAboutListener implements ActionListener
{
	public void actionPerformed(ActionEvent e)
	{
		WindowController.getAboutWindow().setEnabled(true);
		WindowController.getAboutWindow().setVisible(true);
		WindowController.getSearchWindow().setEnabled(false);
	}
}
class MenuFileExitNoSaveListener implements ActionListener
{
	public void actionPerformed(ActionEvent e)
	{
		System.exit(0);
	}
}
class MenuFileExitSaveListener implements ActionListener
{
	public void actionPerformed(ActionEvent e)
	{
		WindowController.saveChanges();
		System.exit(0);
	}
}
class MenuFileSaveListener implements ActionListener
{
	public void actionPerformed(ActionEvent e)
	{
		WindowController.saveChanges();
	}
}
class SearchWindowExitListener extends WindowAdapter
{
	public void windowClosing(WindowEvent e)
	{
		//Confirm save all changes.
		System.out.println("Aced.");
	}
}
class MenuRecordDeleteListener implements ActionListener
{
	public void actionPerformed(ActionEvent e)
	{
		if(WindowController.isEmpty()==false)
		{
			Object[] options = {"Delete Record.", "Cancel."};
			int confirm = JOptionPane.showOptionDialog(null, 
					"Are you sure you want to delete the selected record?",
					"Delete record?",
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[0]);
			if(confirm==JOptionPane.YES_OPTION)
			{
					WindowController.deleteRecord();
			}
		}
		else
		{
			Object[] options = {"Continue."};
			JOptionPane.showOptionDialog(null, 
					"A record must be selected in the table to delete.",
					"No Record Selected",
					JOptionPane.OK_OPTION, 
					JOptionPane.INFORMATION_MESSAGE,
					null,
					options,
					options[0]);
		}
	}
}
/*Sort just got REAL - used to sort JTable.*/
class StudentIDComparator implements Comparator<String>
{//-, 0, + <first arg <, =, > 2nd>
	public int compare(String record1, String record2)
	{
		int id1=0;
		int id2=0;
		try
		{
			id1 = Integer.parseInt(record1);
			id2 = Integer.parseInt(record2);
		}
		catch(NumberFormatException e)
		{
		}
		return id1-id2;
	}
}
class LastNameComparator implements Comparator<String>
{
	public int compare(String record1, String record2)
	{
		return record1.compareToIgnoreCase(record2);
	}
}
class FirstNameComparator implements Comparator<String>
{
	public int compare(String record1, String record2)
	{
		return record1.compareToIgnoreCase(record2);
	}
}
class EntriesComparator implements Comparator<String>
{
	public int compare(String record1, String record2)
	{
		int id1=0;
		int id2=0;
		try
		{
			id1 = Integer.parseInt(record1);
			id2 = Integer.parseInt(record2);
		}
		catch(NumberFormatException e)
		{
		}
		return id1-id2;
	}
}
