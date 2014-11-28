package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import program.Information;

/**
 * This is the "About" window that pops up when users click on menu bar options Help->About.
 * @author Joseph Gefroh
 * @since 2011/12/02 - 22:47 HST
 */
public class AboutWindow extends JFrame
{
	private JPanel panelParent;
	private JPanel panelAboutInfo;
	private JPanel panelAboutBanner;
	private JPanel panelAboutButtons;
	private String strAboutProgram;
	private String strAboutBannerPath;
	private String strAboutVersion;
	private String strAboutAuthor;
	private String strAboutCopyright;
	private JTextField txtFieldVersion;
	private JTextField txtFieldAuthor;
	private JTextField txtFieldCopyright;
	private JTextField txtFieldProgram;
	private JLabel labelAboutBanner;
	private JButton btnOK;
	//Layout
	private GridBagConstraints gbc;
	//Misc
	private final int PREF_HEIGHT = 250;
	private final int PREF_WIDTH = 300;
	private final String windowName = "About NoID";
	private final String versionNumber = Information.PROGRAMVERSION;
	public static final long serialVersionUID = 0;
	public static int instances = 0 ;	//Used to prevent multiple instances of the ABOUT window.
	
	public AboutWindow()
	{
			initAboutBanner();
			initAboutInfo();
			initAboutButtons();
			placeAbout();
			finalizeWindow();
	}
	private void initAboutBanner()
	{//Create the banner component.
		strAboutBannerPath = "./images/aboutBanner.png";
		labelAboutBanner = new JLabel(new ImageIcon(strAboutBannerPath));
		placeAboutBanner();
	}
	private void placeAboutBanner()
	{//Place the banner in the panel.
		panelAboutBanner = new JPanel();
		panelAboutBanner.add(labelAboutBanner);
	}
	private void initAboutInfo()
	{
		//Create the text.
		strAboutProgram = "The NoID Student Tracker";
		strAboutVersion = "Version: " + versionNumber;
		strAboutAuthor = "Author: Joseph Gefroh | Input: Tyler Asuncion";
		strAboutCopyright = "(c) Copyright Joseph Gefroh, 2011-2012.";
		//Initialize text
		txtFieldProgram = new JTextField(strAboutProgram);
		txtFieldVersion = new JTextField(strAboutVersion);
		txtFieldAuthor = new JTextField(strAboutAuthor);
		txtFieldCopyright = new JTextField(strAboutCopyright);	
		//Disable field editing (functional)
		txtFieldProgram.setEditable(false);
		txtFieldAuthor.setEditable(false);
		txtFieldVersion.setEditable(false);
		txtFieldCopyright.setEditable(false);
		//Disable backgrounds (appearance)
		txtFieldProgram.setBackground(null);
		txtFieldAuthor.setBackground(null);
		txtFieldVersion.setBackground(null);
		txtFieldCopyright.setBackground(null);
		//Disable borders (appearance)
		txtFieldProgram.setBorder(null);
		txtFieldAuthor.setBorder(null);
		txtFieldVersion.setBorder(null);
		txtFieldCopyright.setBorder(null);
		
		placeAboutInfo();
	}
	private void placeAboutInfo()
	{//Place the text.
		panelAboutInfo = new JPanel(new GridBagLayout());
		//Initialize layout manager and defaults.
		gbc = new GridBagConstraints();
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		//Add program info
			panelAboutInfo.add(txtFieldProgram, gbc);
		//Add version info
		gbc.gridy++;
			panelAboutInfo.add(txtFieldVersion, gbc);
		//Add author info
		gbc.gridy++;
			panelAboutInfo.add(txtFieldAuthor, gbc);
		//Add copyright info
		gbc.gridy++;
			panelAboutInfo.add(txtFieldCopyright, gbc);
	}
	private void initAboutButtons()
	{//Create the OK button.
		btnOK = new JButton("OK");
		btnOK.addActionListener(new BtnOKListener());
		placeAboutButtons();
	}
	private void placeAboutButtons()
	{//Place the OK button.
		panelAboutButtons = new JPanel();
		panelAboutButtons.add(btnOK);
	}
	private void placeAbout()
	{//Place all created panels onto the parent panel.
		panelParent = new JPanel(new BorderLayout());
			panelParent.add(panelAboutBanner,BorderLayout.PAGE_START);
			panelParent.add(panelAboutInfo,BorderLayout.CENTER);
			panelParent.add(panelAboutButtons, BorderLayout.PAGE_END);
	}
	private void finalizeWindow()
	{//Place parent panel onto the frame, and set frame settings.
		this.add(panelParent);	//Add panel to the frame.
		this.addWindowListener(new AboutWindowListener());
		this.pack();
		this.setTitle(windowName);
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setMinimumSize(new Dimension(PREF_WIDTH, PREF_HEIGHT));
		this.setResizable(false);
		//Center the window.
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width/2)-(PREF_WIDTH/2);	//X Center
		int y = (screen.height/2)-(PREF_HEIGHT/2);	//Y Center
		this.setBounds(x, y, PREF_WIDTH, PREF_HEIGHT);	//Center
		
		this.setVisible(false);
		this.setEnabled(false);
		this.validate();
	}
}
class BtnOKListener implements ActionListener
{//Set the window to be invisible on OK button press.
	public void actionPerformed(ActionEvent e)
	{
		WindowController.getAboutWindow().setEnabled(false);
		WindowController.getAboutWindow().setVisible(false);
		WindowController.getSearchWindow().setEnabled(true);
		WindowController.getSearchWindow().toFront();
	}
}
class AboutWindowListener extends WindowAdapter
{//Set the window to be invisible on X button press.
	public void windowClosing(WindowEvent e)
	{
		WindowController.getAboutWindow().setEnabled(false);
		WindowController.getAboutWindow().setVisible(false);
		WindowController.getSearchWindow().setEnabled(true);
		WindowController.getSearchWindow().toFront();
	}
}


