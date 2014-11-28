package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ToolTipManager;
import javax.swing.table.DefaultTableModel;

import program.Information;
import backend.StudentRecord;

public class ExamineWindow extends JFrame
{
	public enum eMonth {JAN, FEB, MAR, APR, MAY, JUNE, JULY, OCT, SEP, AUG, NOV, DEC}
	private final static long serialVersionUID=0;
	//private final static int PREF_HEIGHT = 190;
	//private final static int PREF_WIDTH = 380;
	private static int PREF_HEIGHT = 190;
	private static int PREF_WIDTH = 380;
	private final static String windowName = "Record Window";
	private static boolean editMode = false;
	public static int instances = 0 ;
	
	private JPanel panelParent;
	private JPanel panelInfoRecord;
	private JPanel panelButtons;
	private JTable tableDates;
	private JPanel panelDateEdit;
	private JPanel panelAddSub;
	private JScrollPane scrollPaneDateTable;
	private DefaultTableModel modelDefault;
	private JButton btnAdd;
	private JButton btnSub;
	private JButton btnClose;
	private JButton btnEdit;
	private JTextField txtFldID;
	private JTextField txtFldLast;
	private JTextField txtFldFirst;
	private JTextField txtFldEntries;
	private JLabel labelID;
	private JLabel labelLast;
	private JLabel labelFirst;
	private JLabel labelEntries;
	private StudentRecord currentlyExamined;
	
	private JComboBox comboMonth;
	private JComboBox comboDay;
	private JComboBox comboYear;
							
	public ExamineWindow()
	{
			initFrameComponents();
			placeFrameComponents();
	}
	public String getID()
	{
		return txtFldID.getText();
	}/////////////////////////////////////////////////////////////////////
	public StudentRecord getCurrentlyExamined()
	{
		return currentlyExamined;
	}
	public void setCurrentlyExamined(final StudentRecord currentlyExamined)
	{
		this.currentlyExamined = currentlyExamined;
	}
	public String getLast()
	{
		return txtFldLast.getText();
	}/////////////////////////////////////////////////////////////////////
	
	public String getFirst()
	{
		return txtFldFirst.getText();
	}/////////////////////////////////////////////////////////////////////
	
	public String getEntries()
	{
		return txtFldEntries.getText();
	}/////////////////////////////////////////////////////////////////////
	/**
	 * 
	 * @return dates if valid, null if invalid, empty String[] if empty.
	 */
	public String[] getDates()
	{
		String[] dates = null;
		if(modelDefault.getRowCount()==0)
		{
			return new String[0];
		}
		dates = new String[modelDefault.getRowCount()];
		for(int index=0;index<dates.length;index++)
		{//For all the dates...
			dates[index] = (String)modelDefault.getValueAt(index, 0);	//Add to a String[].
		}
		return dates;
	}/////////////////////////////////////////////////////////////////////
	
	public JButton getBtnClose()
	{
		return btnClose;
	}
	
	public void updateFields(final StudentRecord cur)
	{
		resetWindow();
		txtFldID.setText(cur.getID());
		txtFldLast.setText(cur.getLastName());
		txtFldFirst.setText(cur.getFirstName());
		txtFldEntries.setText(cur.getEntriesTotal()+"");
		String[] recordDates = cur.datesToStringArray();
		if(recordDates!=null&&recordDates.length>0)
		{
			for(int index=0;index<recordDates.length;index++)
			{
				String[] data = new String[1];
				data[0]=recordDates[index];
				addData(data);
			}
		}
	}
	public void addData(final String[] data)
	{
		String formatted = "";
		String dataString;
		String[] newData = new String[1];
		if(data!=null&data.length>0)
		{
			dataString = data[0];
			if(dataString!=null&&dataString.length()==8)
			{
				for(int index=0;index<dataString.length();index++)
				{//For all the characters of the passed string...
					formatted+=dataString.charAt(index);
					if(index==1||index==3)
					{
						formatted+="-";
					}
				}
				newData = new String[1];
				newData[0] = formatted;
				modelDefault.addRow(newData);
			}
			else
			{
				modelDefault.addRow(newData);
			}
		}
	}
	private void initFrameComponents()
	{
		/*Buttons.*/
		btnAdd 	= new JButton("Add");
			btnAdd.addActionListener(new BtnAddExamineListener());
			btnAdd.setEnabled(false);
			btnAdd.setMargin(new Insets(0,5,0,5));
			btnAdd.setMnemonic('A');
		btnSub 	= new JButton("Remove");
			btnSub.addActionListener(new BtnSubExamineListener());
			btnSub.setEnabled(false);
			btnSub.setMargin(new Insets(0,5,0,5));
			btnSub.setMnemonic('R');
		btnClose = new JButton("Close Record");
			btnClose.addActionListener(new BtnCloseExamineListener());
			btnClose.setMnemonic('C');
		btnEdit = new JButton("Edit Record...");
			btnEdit.addActionListener(new BtnEditExamineListener());
			btnEdit.setMnemonic('E');
		
		/*Text Fields*/
		txtFldID = new JTextField(15);
			txtFldID.setEditable(false);
			txtFldID.setFocusAccelerator('S');
		txtFldLast 	= new JTextField(15);
			txtFldLast.setEditable(false);
		txtFldFirst = new JTextField(15);
			txtFldFirst.setEditable(false);
		txtFldEntries = new JTextField(15);		
			txtFldEntries.setEditable(false);
		
		/*Labels*/
		labelID = new JLabel("StudentID:   ");
			labelID.setDisplayedMnemonic('S');
			labelID.setFocusable(false);
			labelID.setHorizontalAlignment(JLabel.RIGHT);
			labelID.setHorizontalTextPosition(JLabel.RIGHT);
		labelLast = new JLabel("Last Name:   ");
			labelLast.setFocusable(false);
			labelLast.setHorizontalAlignment(JLabel.RIGHT);
			labelLast.setHorizontalTextPosition(JLabel.RIGHT);
		labelFirst = new JLabel("First Name:   ");
			labelFirst.setFocusable(false);
			labelFirst.setHorizontalAlignment(JLabel.RIGHT);
			labelFirst.setHorizontalTextPosition(JLabel.RIGHT);
		labelEntries = new JLabel("# Entries:   ");
			labelEntries.setFocusable(false);
			labelEntries.setHorizontalAlignment(JLabel.RIGHT);
			labelEntries.setHorizontalTextPosition(JLabel.RIGHT);

		/*Combo Boxes*/
		comboMonth = new JComboBox(Information.ARRMONTH);
			comboMonth.setEnabled(false);
		comboYear = new JComboBox(Information.ARRYEAR);
			comboYear.setEnabled(false);
		comboDay = new JComboBox(Information.ARRDAY);
			comboDay.setEnabled(false);
				
		modelDefault = new DefaultTableModel() {//Prohibits editing of cell.
			public static final long serialVersionUID = 0;
			public boolean isCellEditable(int row, int col)
			{//Overridden method for ease of editing.
				return false;
			}
		};//DefaultTableModel	
		modelDefault.addColumn("Entry Date");	
		String[] a = {""};
		modelDefault.addRow(a);
		
		tableDates = new JTable();
		tableDates.setFillsViewportHeight(false);
			tableDates.setFocusable(false);
			tableDates.setModel(modelDefault);
			tableDates.setPreferredScrollableViewportSize(new Dimension(140, labelID.getHeight()*4));
			tableDates.setRowSelectionAllowed(true);
			tableDates.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			ToolTipManager.sharedInstance().unregisterComponent(tableDates);
			ToolTipManager.sharedInstance().unregisterComponent(tableDates.getTableHeader());
	}
	private void placeFrameComponents()
	{//Places the labels and the text fields holding the student record information.
		/*Information Components*/
		panelInfoRecord = new JPanel(new GridBagLayout());
		panelDateEdit	=	new JPanel(new BorderLayout());
		panelButtons = new JPanel();
		panelAddSub = new JPanel();
		scrollPaneDateTable = new JScrollPane(tableDates); 				//Stick table into scrollpane
		panelParent = new JPanel(new GridBagLayout());
		GridBagConstraints gbcInner = new GridBagConstraints();
		
		/*Labels*/
		gbcInner.gridx=0;
		gbcInner.gridy=0;
		gbcInner.fill = GridBagConstraints.BOTH;
		panelInfoRecord.add(labelID, gbcInner);
		gbcInner.gridy++;
		panelInfoRecord.add(labelFirst, gbcInner);
		gbcInner.gridy++;
		panelInfoRecord.add(labelLast, gbcInner);
		gbcInner.gridy++;
		panelInfoRecord.add(labelEntries, gbcInner);
	
		/*Text fields*/
		gbcInner.gridx=1;
		gbcInner.gridy=0;
		panelInfoRecord.add(txtFldID, gbcInner);
		gbcInner.gridy++;
		panelInfoRecord.add(txtFldFirst, gbcInner);
		gbcInner.gridy++;
		panelInfoRecord.add(txtFldLast, gbcInner);
		gbcInner.gridy++;
		panelInfoRecord.add(txtFldEntries, gbcInner);
		
		/*JTable*/
	//	gbcInner.fill = GridBagConstraints.BP;	
		gbcInner.gridheight=4;
		gbcInner.gridx=2;
		gbcInner.gridy=0;
		panelInfoRecord.add(scrollPaneDateTable, gbcInner);
		
		/*Date Combo Boxes*/
		gbcInner.gridheight=1;
		gbcInner.gridy=4;
		gbcInner.gridx=1;
		panelDateEdit.add(comboMonth, BorderLayout.WEST);
		panelDateEdit.add(comboDay, BorderLayout.CENTER);
		panelDateEdit.add(comboYear, BorderLayout.EAST);
		panelInfoRecord.add(panelDateEdit, gbcInner);
		
		/*Add/Sub*/
		gbcInner.gridy=4;
		gbcInner.gridx=2;
		gbcInner.gridheight=1;
		panelAddSub.add(btnAdd);
		panelAddSub.add(btnSub);
		panelInfoRecord.add(panelAddSub, gbcInner);
		
		panelButtons.add(btnEdit);
		panelButtons.add(btnClose);
		
		GridBagConstraints gbcOuter = new GridBagConstraints();
		gbcOuter.fill = GridBagConstraints.HORIZONTAL;
		gbcOuter.gridx=0;
		gbcOuter.gridy=0;
		panelParent.add(panelInfoRecord, gbcOuter);
		gbcOuter.gridy++;
		panelParent.add(panelButtons, gbcOuter);
		
		/**/
		this.add(panelParent);	//Add panel to the frame.
		this.validate();
		this.pack();
		PREF_HEIGHT = this.getHeight();
		PREF_WIDTH = this.getWidth();
		
		panelInfoRecord.setMinimumSize(panelInfoRecord.getSize());
		panelAddSub.setMinimumSize(panelAddSub.getSize());
		panelButtons.setMinimumSize(panelButtons.getSize());
		panelParent.setMinimumSize(panelParent.getSize());
		panelDateEdit.setMinimumSize(panelDateEdit.getSize());
		resetWindow();
		setEditMode(false);
		this.addWindowListener(new ExamineWindowListener());
		//Center panel
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width/2)-(PREF_WIDTH/2);	//X Center
		int y = (screen.height/2)-(PREF_HEIGHT/2);	//Y Center
		this.setBounds(x, y, PREF_WIDTH, PREF_HEIGHT);	//Center
		//Set default window settings
		this.setTitle(windowName);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setMinimumSize(new Dimension(PREF_WIDTH, PREF_HEIGHT));
		this.setResizable(false);
		this.setVisible(false);
		this.setEnabled(false);
		
	}
	/**
	 * Revert the fields back to their default state.
	 */
	public void resetWindow()
	{
		txtFldID.setText("");
		txtFldLast.setText("");
		txtFldFirst.setText("");
		txtFldEntries.setText("");
		while(modelDefault.getRowCount()!=0)
		{
			modelDefault.removeRow(0);
		}
		setComboBoxSelectedDefault();
		this.repaint();
	}
	/**
	 * Allow/Disallow editing of the record
	 * @param newEditMode true to allow editing, false to disallow.
	 */
	public void setEditMode(final boolean newEditMode)
	{
		txtFldID.setEditable(newEditMode);
		txtFldLast.setEditable(newEditMode);
		txtFldFirst.setEditable(newEditMode);
		btnAdd.setEnabled(newEditMode);
		btnSub.setEnabled(newEditMode);
		comboMonth.setEnabled(newEditMode);
		comboYear.setEnabled(newEditMode);
		comboDay.setEnabled(newEditMode);
		
		if(newEditMode==true)
		{
			btnEdit.setText("Finish Edit.");
		}
		else
		{
			btnEdit.setText("Edit Record...");
		}
		editMode = newEditMode;
		setRowSelectedDefault();
	}
	private void setComboBoxSelectedDefault()
	{
		Calendar calendar = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		String currentDate = dateFormat.format(calendar.getTime());
		String[] parts = null;
		if(currentDate!=null)
		{
			parts = currentDate.split("-");
		}
		if(parts!=null&&parts.length==3)
		{
			try
			{
				comboMonth.setSelectedIndex(Integer.parseInt(parts[0])-1);
				comboDay.setSelectedIndex(Integer.parseInt(parts[1])-1);
				comboYear.setSelectedIndex(Integer.parseInt(parts[2])-2010);
			}
			catch(NumberFormatException e)
			{
				comboMonth.setSelectedIndex(comboMonth.getItemCount()-1);
				comboDay.setSelectedIndex(comboDay.getItemCount()-1);
				comboYear.setSelectedIndex(comboYear.getItemCount()-1);
			}
			catch(IndexOutOfBoundsException e)
			{
				comboMonth.setSelectedIndex(comboMonth.getItemCount()-1);
				comboDay.setSelectedIndex(comboDay.getItemCount()-1);
				comboYear.setSelectedIndex(comboYear.getItemCount()-1);
			}
			catch(NullPointerException e)
			{
				comboMonth.setSelectedIndex(comboMonth.getItemCount()-1);
				comboDay.setSelectedIndex(comboDay.getItemCount()-1);
				comboYear.setSelectedIndex(comboYear.getItemCount()-1);
			}
		}
	}
	private void setRowSelectedDefault()
	{
		if(tableDates.getRowCount()>0)
		{
			tableDates.addColumnSelectionInterval(0, tableDates.getColumnCount()-1);
			tableDates.addRowSelectionInterval(tableDates.getRowCount()-1,tableDates.getRowCount()-1);
			tableDates.scrollRectToVisible(tableDates.getCellRect(tableDates.getRowCount()-1, tableDates.getColumnCount(), true));
		}
	}
	public boolean getEditMode()
	{
		return editMode;
	}
	public void incEntries()
	{
		//Step 1: Increment the visible value
		try
		{
			int numEntries = Integer.parseInt(txtFldEntries.getText());
			numEntries++;
			txtFldEntries.setText(numEntries + "");
		}
		catch(NumberFormatException e)
		{
			txtFldEntries.setText("0");
		}
		//Step 2: Add a row to the table.
		String[] demoString = {""};
		int monthNum = comboMonth.getSelectedIndex()+1;
		String monthStr = "";
		if(monthNum<=9)
		{
			monthStr += "0" + monthNum;
		}
		else
		{
			monthStr = "" + monthNum;
		}
		demoString[0] = "" + monthStr
							+ (String)comboDay.getSelectedItem()
							+ (String)comboYear.getSelectedItem();
		addData(demoString);
		setRowSelectedDefault();
	}/////////////////////////////////////////////////////////////////////
	public void decEntries()
	{
		try
		{/*Attempt to decrement the number of entries and remove the selected date from the row.*/
			int numEntries = Integer.parseInt(txtFldEntries.getText());	//Make sure that the number of entries is
																		//	actually a number.
			if(numEntries>0)
			{//If there are still dates to remove...
				if(modelDefault.getRowCount()>0&&tableDates.getSelectedRow()!=-1)
				{//Check to see that there are dates to remove from the JTable.
					modelDefault.removeRow(tableDates.getSelectedRow());
					numEntries--;
					setRowSelectedDefault();
					txtFldEntries.setText(numEntries + "");
				}
			}
		}
		catch(NumberFormatException e)
		{
			txtFldEntries.setText("0");
		}
		catch(NullPointerException e)
		{
			
		}
	}/////////////////////////////////////////////////////////////////////
}//ExamineWindow


class ExamineWindowListener extends WindowAdapter
{//Set the window to be invisible on X button press.
	public void windowClosing(WindowEvent e)
	{
		WindowController.getExamineWindow().setEditMode(false);
		WindowController.getExamineWindow().setEnabled(false);
		WindowController.getExamineWindow().setVisible(false);
		WindowController.getSearchWindow().setEnabled(true);
		WindowController.getSearchWindow().toFront();
	}
}


class BtnCloseExamineListener implements ActionListener
{
	public void actionPerformed(ActionEvent e)
	{
		WindowController.getExamineWindow().setEditMode(false);
		WindowController.getExamineWindow().setEnabled(false);
		WindowController.getExamineWindow().setVisible(false);
		WindowController.getSearchWindow().setEnabled(true);
		WindowController.getSearchWindow().toFront();
	}
}


class BtnEditExamineListener implements ActionListener
{
	public void actionPerformed(ActionEvent e)
	{
		if(WindowController.getExamineWindow().getEditMode()==false)
		{
			WindowController.getExamineWindow().setEditMode(true);
		}
		else
		{
			WindowController.getExamineWindow().setEditMode(false);				//Turn off edit mode.
		    WindowController.editRecord();										//Make changes to record.
			//Close window
			WindowController.getExamineWindow().setEnabled(false);
			WindowController.getExamineWindow().setVisible(false);
			WindowController.getSearchWindow().setEnabled(true);
			WindowController.getSearchWindow().toFront();
		}
	}
}


class BtnAddExamineListener implements ActionListener
{
	public void actionPerformed(ActionEvent e)
	{
		WindowController.getExamineWindow().incEntries();
	}
}


class BtnSubExamineListener implements ActionListener
{
	public void actionPerformed(ActionEvent e)
	{
		WindowController.getExamineWindow().decEntries();
	}
}
