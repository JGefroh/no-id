package program;
public class Information
{
	public static final int FIELDID 		= 0;
	public static final int FIELDLAST 		= 1;
	public static final int FIELDFIRST 		= 2;
	public static final int FIELDENTRIES 	= 3;
	public static final int FIELDDATES 		= 4;
	
	public static final String 		PROGRAMVERSION 	= "5";
	public static final String 		PROGRAMDATE	= "2012/01/15";
	public static final String[] 	PROGRAMAUTHOR 	= {"Joseph Gefroh", "Tyler Asuncion"};
	
	public static int debugMode = 1;	//Turn debug messages on or off (0 off, 1on)
	public static final int DBGMSG = 0;	//Debug message type (Message).
	public static final int DBGERR = 1;	//Debug message type (Error).
	public static final int DBGINF = 2;	//Debug message type (Information).
	
	public static final String[] ARRDAY = {"01", "02", "03", "04", "05","06","07","08","09","10",
			"11","12","13","14","15","16","17","18","19","20",
			"21","22","23","24","25","26","27","28","29","30","31"};
	public static final String[] ARRYEAR =  {"2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020",
				"2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030"};
	public static final String[] ARRMONTH = {"JAN", "FEB", "MAR", "APR", "MAY", "JUNE", "JULY", "OCT", "SEP", "AUG", "NOV", "DEC"};
}
