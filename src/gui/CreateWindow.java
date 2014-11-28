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

public class CreateWindow extends JFrame
{
	public static int instances = 0 ;
	private static final long serialVersionUID = 0;
	
	private JPanel panelParent;
	private JPanel panelInfoRecord;
	private JPanel panelButtons;
	private JPanel panelAddSub;
	private JPanel panelDateEdit;
	private JTable tableDates;
	private JScrollPane scrollPaneDateTable;
	private DefaultTableModel modelDefault;
	private JButton btnAdd;
	private JButton btnSub;
	private JButton btnClose;
	private JButton btnCreate;
	private JTextField txtFldID;
	private JTextField txtFldLast;
	private JTextField txtFldFirst;
	private JTextField txtFldEntries;
	private JLabel labelID;
	private JLabel labelLast;
	private JLabel labelFirst;
	private JLabel labelEntries;
	
	private static int PREF_HEIGHT = 190;
	private static int PREF_WIDTH = 380;
	private final static String windowName = "NoID: Create A New Record";
	
	private JComboBox comboMonth;
	private JComboBox comboDay;
	private JComboBox comboYear;
							
	public CreateWindow()
	{
		initFrameComponents();
		placeFrameComponents();
	}/////////////////////////////////////////////////////////////////////
	
	public String getID()
	{
		return txtFldID.getText();
	}/////////////////////////////////////////////////////////////////////
	
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
	/**
	 * Obtain the dates from the JTable.
	 */
	public String[] getDates()
	{
		String[] dates = null;
		if(modelDefault.getRowCount()==0)
		{//if there are no dates to obtain...
			return new String[0];
		}
		dates = new String[modelDefault.getRowCount()];
		for(int index=0;index<dates.length;index++)
		{//For all the dates...
			dates[index] = (String)modelDefault.getValueAt(index, 0);	//Add to a String[].
		}
		return dates;
	}/////////////////////////////////////////////////////////////////////
	
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
	
	private void setRowSelectedDefault()
	{
		if(tableDates.getRowCount()>0)
		{
			tableDates.addColumnSelectionInterval(0, tableDates.getColumnCount()-1);
			tableDates.addRowSelectionInterval(tableDates.getRowCount()-1,tableDates.getRowCount()-1);
			tableDates.scrollRectToVisible(tableDates.getCellRect(tableDates.getRowCount()-1, tableDates.getColumnCount(), true));
		}
	}
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
	
	private void initFrameComponents()
	{
		/*Initialize examination components.*/
		btnAdd = new JButton("Add");
			btnAdd.setMnemonic('A');
		btnSub = new JButton("Remove");
			btnSub.setMnemonic('R');
		btnClose = new JButton("Cancel");
			btnClose.setMnemonic('N');
		btnCreate = new JButton("Create");
			btnCreate.setMnemonic('C');
		btnAdd.setMargin(new Insets(0,5,0,5));
		btnSub.setMargin(new Insets(0,5,0,5));
		btnAdd.addActionListener(new BtnAddCreateListener());
		btnSub.addActionListener(new BtnSubCreateListener());
		btnClose.addActionListener(new BtnCloseCreateListener());
		btnCreate.addActionListener(new BtnCreateCreateListener());

		txtFldID = new JTextField(15);
		txtFldLast = new JTextField(15);
		txtFldFirst = new JTextField(15);
		txtFldEntries = new JTextField(15);
		txtFldID.setFocusAccelerator('S');
		txtFldEntries.setEnabled(false);
		txtFldEntries.setBackground(null);
		txtFldEntries.setText("0");
		
		labelID = new JLabel("StudentID:   ");
			labelID.setDisplayedMnemonic('S');
		labelLast = new JLabel("Last Name:   ");
		labelFirst = new JLabel("First Name:   ");
		labelEntries = new JLabel("# Entries:   ");
		
		labelID.setHorizontalTextPosition(JLabel.RIGHT);
		labelLast.setHorizontalTextPosition(JLabel.RIGHT);
		labelFirst.setHorizontalTextPosition(JLabel.RIGHT);
		labelEntries.setHorizontalTextPosition(JLabel.RIGHT);
		
		labelID.setHorizontalAlignment(JLabel.RIGHT);
		labelLast.setHorizontalAlignment(JLabel.RIGHT);
		labelFirst.setHorizontalAlignment(JLabel.RIGHT);
		labelEntries.setHorizontalAlignment(JLabel.RIGHT);
		labelID.setFocusable(false);
		labelLast.setFocusable(false);
		labelFirst.setFocusable(false);
		labelEntries.setFocusable(false);
		
		comboMonth = new JComboBox(Information.ARRMONTH);
		comboYear = new JComboBox(Information.ARRYEAR);
		comboDay = new JComboBox(Information.ARRDAY);
		
		modelDefault = new DefaultTableModel(){//Prohibits editing of cell.
			public static final long serialVersionUID = 0;
			public boolean isCellEditable(int row, int col)
			{//Overridden method for ease of editing.
				return false;
			}
		};//DefaultTableModel
		modelDefault.addColumn("Entry Date");
		
		tableDates = new JTable();
		tableDates.setModel(modelDefault);
		tableDates.setFillsViewportHeight(false);	
		tableDates.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableDates.setRowSelectionAllowed(true);
		tableDates.setPreferredScrollableViewportSize(new Dimension(50, labelID.getHeight()*4));//Glitches on repaint?
		tableDates.setFocusable(false);
		ToolTipManager.sharedInstance().unregisterComponent(tableDates);
		ToolTipManager.sharedInstance().unregisterComponent(tableDates.getTableHeader());
	}/////////////////////////////////////////////////////////////////////
	
	private void placeFrameComponents()
	{
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
		
		panelButtons.add(btnCreate);
		panelButtons.add(btnClose);
		
		GridBagConstraints gbcOuter = new GridBagConstraints();
		gbcOuter.fill = GridBagConstraints.HORIZONTAL;
		gbcOuter.gridx=0;
		gbcOuter.gridy=0;
		panelParent.add(panelInfoRecord, gbcOuter);
		gbcOuter.gridy++;
		panelParent.add(panelButtons, gbcOuter);
		
		/*Anti-Squish measures*/
		panelInfoRecord.setMinimumSize(panelInfoRecord.getSize());
		panelAddSub.setMinimumSize(panelAddSub.getSize());
		panelButtons.setMinimumSize(panelButtons.getSize());
		panelParent.setMinimumSize(panelParent.getSize());
		panelDateEdit.setMinimumSize(panelDateEdit.getSize());
		this.add(panelParent);	//Add panel to the frame.
		this.validate();
		this.pack();
		
		/*Frame Finalization*/
		this.add(panelParent);	//Add panel to the frame.
		PREF_HEIGHT = this.getHeight();
		PREF_WIDTH = this.getWidth();
		this.addWindowListener(new CreateWindowListener());
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
	}/////////////////////////////////////////////////////////////////////
	
	/**
	 * Revert the fields back to original.
	 */
	public void resetWindow()
	{
		txtFldID.setText("");
		txtFldLast.setText("");
		txtFldFirst.setText("");
		txtFldEntries.setText("0");
		setComboBoxSelectedDefault();
		while(modelDefault.getRowCount()!=0)
		{
			modelDefault.removeRow(0);
		}
		this.repaint();
	}/////////////////////////////////////////////////////////////////////
	/**
	 * Sets the Month, Day, and Year combo boxes to be set to the current month, day, and year.
	 */
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
}/* * Class Closing Brace * */


class CreateWindowListener extends WindowAdapter
{//Set the window to be invisible on X button press.
	public void windowClosing(WindowEvent e)
	{
		WindowController.getCreateWindow().setEnabled(false);
		WindowController.getCreateWindow().setVisible(false);
		WindowController.getSearchWindow().setEnabled(true);
		WindowController.getSearchWindow().toFront();
	}
}


class BtnCloseCreateListener implements ActionListener
{
	public void actionPerformed(ActionEvent e)
	{
		WindowController.getCreateWindow().setEnabled(false);
		WindowController.getCreateWindow().setVisible(false);
		WindowController.getSearchWindow().setEnabled(true);
		WindowController.getSearchWindow().toFront();
	}
}


class BtnAddCreateListener implements ActionListener
{
	public void actionPerformed(ActionEvent e)
	{
		WindowController.getCreateWindow().incEntries();
	}
}


class BtnSubCreateListener implements ActionListener
{
	public void actionPerformed(ActionEvent e)
	{
		WindowController.getCreateWindow().decEntries();
	}
}


class BtnCreateCreateListener implements ActionListener
{
	public void actionPerformed(ActionEvent e)
	{
		WindowController.createRecord();
		WindowController.getCreateWindow().setEnabled(false);
		WindowController.getCreateWindow().setVisible(false);
		WindowController.getSearchWindow().setEnabled(true);
		WindowController.getSearchWindow().toFront();	
	}
}