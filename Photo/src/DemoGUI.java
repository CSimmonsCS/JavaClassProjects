import java.util.ArrayList;
import java.util.Scanner;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Frame;
import java.awt.Component;
import java.lang.Object;
import javax.swing.JFrame;
import javax.swing.border.*;
import javax.swing.BoxLayout;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;

import com.google.gson.*;
import com.google.gson.Gson;

import static java.nio.file.StandardOpenOption.*;
import java.nio.file.*;
import java.io.*;


public class DemoGUI extends JFrame implements ActionListener {

    JTextField searchTagField = new JTextField("");
    JTextField numResultsStr = new JTextField("10");
    JPanel onePanel;
    JScrollPane oneScrollPanel;
    JButton testButton = new JButton("Test");

    JButton searchButton = new JButton("Search");
    JButton saveButton = new JButton("Save");
    JButton exitButton = new JButton("Exit");
    JButton loadButton = new JButton("Load");
    JButton deleteButton = new JButton("Delete");

    static int frameWidth = 800;
    static int frameHeight = 600;

    public DemoGUI() {

	// create bottom subpanel with buttons, flow layout
	JPanel buttonsPanel = new JPanel();
	buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
	// add testButton to bottom subpanel
	buttonsPanel.add(testButton);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(loadButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(exitButton);
        //buttonsPanel.add();
	// add listener for testButton clicks
	testButton.addActionListener(this);
        saveButton.addActionListener(this);
        searchButton.addActionListener(this);
        loadButton.addActionListener(this);
        exitButton.addActionListener(this);
        //testButton.addActionListener(this);
        
        
        
        
	/*
	System.out.println("testButton at " +
			  testButton.getClass().getName() +
			  "@" + Integer.toHexString(hashCode()));
	System.out.println("Components: ");
	Component comp[] = buttonsPanel.getComponents();
	for (int i=0; i<comp.length; i++) {
	   System.out.println(comp[i].getClass().getName() +
			      "@" + Integer.toHexString(hashCode()));
	}
	*/

	// create middle subpanel with 2 text fields and button, border layout
	JPanel textFieldSubPanel = new JPanel(new FlowLayout());
	// create and add label to subpanel
	JLabel tl = new JLabel("Enter search tag:");
	textFieldSubPanel.add(tl);

	// set width of left text field
	searchTagField.setColumns(23);
	// add listener for typing in left text field
	searchTagField.addActionListener(this);
	// add left text field to middle subpanel
	textFieldSubPanel.add(searchTagField);
	// add search button to middle subpanel
	textFieldSubPanel.add(searchButton);
	// add listener for searchButton clicks
	//searchButton.addActionListener(this);

	// create and add label to middle subpanel, add to middle subpanel
	JLabel tNum = new JLabel("max search results:");
	numResultsStr.setColumns(2);
	textFieldSubPanel.add(tNum);
	textFieldSubPanel.add(numResultsStr);

	// create and add panel to contain bottom and middle subpanels
	/*
	JPanel textFieldPanel = new JPanel(new BorderLayout());
	textFieldPanel.add(buttonsPanel, BorderLayout.SOUTH);
	textFieldPanel.add(textFieldSubPanel, BorderLayout.NORTH);
	*/
	JPanel textFieldPanel = new JPanel();
	textFieldPanel.setLayout(new BoxLayout(textFieldPanel, BoxLayout.Y_AXIS));
	textFieldPanel.add(textFieldSubPanel);
	textFieldPanel.add(buttonsPanel);

	// create top panel
	onePanel = new JPanel();
	onePanel.setLayout(new BoxLayout(onePanel, BoxLayout.Y_AXIS));

	// create scrollable panel for top panel
	oneScrollPanel = new JScrollPane(onePanel,
				     JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				     JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	oneScrollPanel.setPreferredSize(new Dimension(frameWidth, frameHeight-100));
	setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
	// add scrollable panel to main frame
	add(oneScrollPanel);

	// add panel with buttons and textfields to main frame
	add(textFieldPanel);
        


    }
    
    public static void main(String [] args) throws Exception {
	DemoGUI frame = new DemoGUI();
	frame.setTitle("Swing GUI Demo");
	frame.setSize(frameWidth, frameHeight);
	frame.setLocationRelativeTo(null);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
    }
    
    int index=0;
    
    ArrayList <String> onScreen = new ArrayList<String>();
    
    public void actionPerformed(ActionEvent e) {

    	
	if (e.getSource() == searchButton) {
		System.out.println("SEARCH BUTTON");
		try {
			getSearchTag();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


	}
	else if (e.getSource() == testButton) {
			getPhotoURL();
	}
	else if (e.getSource() == searchTagField) {
		
		System.out.println("ENTER");
			try {
				getSearchTag();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}
	else if(e.getSource() == deleteButton)
	{
		
	}
	else if(e.getSource()== saveButton)
	{
		try {
			saveURL(onScreen);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	else if(e.getSource()== loadButton)
	{
		try {
			loadURL();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}	
    else if (e.getSource()==exitButton){
            System.exit(0);
        }
    
    }
    
    public void getSearchTag() throws Exception
    {
    	ArrayList <String> searches;
    	searches = new ArrayList <String>();
    	
    	String search = searchTagField.getText();
    	
    	System.out.println(numResultsStr.getText());
    	System.out.println();
    	
    	String api  = "https://api.flickr.com/services/rest/?method=flickr.photos.search";
    	// number of results per page
        String request = api + "&per_page=";
        request += numResultsStr.getText();
        request += "&format=json&nojsoncallback=1&extras=geo";
        request += "&api_key=" + "0781dd964148501324458f8a7524ac18";
        
    	if (search.length() != 0) {
    	    request += "&tags="+search;
    	}
    	System.out.println("searchTagField: " + searchTagField.getText());
    	
    	//System.out.println("Sending http GET request:");
    	System.out.println();
    	System.out.println(request);

   	// open http connection
    	URL obj = new URL(request);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

    	// send GET request
            con.setRequestMethod("GET");

    	// get response
            int responseCode = con.getResponseCode();
    	
    	System.out.println("Response Code : " + responseCode);

    	// read and construct response String
            BufferedReader in = new BufferedReader(new InputStreamReader
    					       (con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

    	System.out.println(response);

    	Gson gson = new Gson();
    	String s = response.toString();
    	//int x = 10;//number of photos
    	index = Integer.parseInt(numResultsStr.getText());
    	
    	for(int i = 0; i<Integer.parseInt(numResultsStr.getText()); i++)
    	{
    	Response responseObject = gson.fromJson(s, Response.class);
    	//System.out.println("# photos = " + responseObject.photos.photo.length);
    	//System.out.println("Photo "+ i+":");
    	int farm = responseObject.photos.photo[i].farm;
    	String server = responseObject.photos.photo[i].server;
    	String id = responseObject.photos.photo[i].id;
    	String secret = responseObject.photos.photo[i].secret;
    	String photoUrl = "http://farm"+farm+".static.flickr.com/"
    	    +server+"/"+id+"_"+secret+".jpg";
    	//System.out.println(photoUrl);
    	
    	searches.add(photoUrl);
    	
    	}
    	
    	printImages(searches);
    	
    	saveURL(searches);
    	
    	
    	
    	for(int i = 0; i<Integer.parseInt(numResultsStr.getText()); i++)
    	{
    		onScreen.add(searches.get(i));
    		//System.out.println(onScreen.get(i));
    	}
    	
    	//SHOULD BE FOR DELETE/SAVE
    	

        
    }
    

    
    public void getPhotoURL()
    {
    	
   	 	String urlHolder= searchTagField.getText();//for testing purposes
 	   System.out.println("Test");

       try{
           URL url=new URL(urlHolder);
           BufferedImage image = ImageIO.read(url);
           ImageIcon icon = new ImageIcon(image);
           
           icon = resizeImageIcon(icon);
           
           JButton imageButton = new JButton(icon);
           onePanel.add(imageButton);
           onePanel.revalidate();
       }catch(Exception exp){
       
       }
    	
    }
    
    public ImageIcon resizeImageIcon(ImageIcon img){
       // System.out.println("here 1");
       Image image=img.getImage();
       double newHeight =200;
       double oldHeight=image.getHeight(onePanel);
       double oldWidth=image.getWidth(onePanel);
       double scaleVal = newHeight/oldHeight;
       int newWidth=(int)(oldWidth*scaleVal);
       //System.out.println(scaleVal+" "+oldWidth + " "+newHeight+" "+newWidth + " "+oldHeight);
       
       Image newimg = image.getScaledInstance(newWidth, (int)newHeight,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
       //System.out.println("here 2");
       return img = new ImageIcon(newimg);  // transform it back
    }
    
    public void printImages(ArrayList<String> searches)
    {
    	
    	for(int i = 0; i<Integer.parseInt(numResultsStr.getText()); i++)
    	{
    		try{
    			URL url=new URL(searches.get(i));
    			BufferedImage image = ImageIO.read(url);
    			ImageIcon icon = new ImageIcon(image);
    			
    			icon = resizeImageIcon(icon);
    			
    			JButton imageButton = new JButton(icon);
    			onePanel.add(imageButton);
    			//imageButton.addActionListener(this);
    			onePanel.revalidate();
    			
    		}catch(Exception exp){
        
    		}
    	}
    	
    }
    
    public void saveURL(ArrayList<String> searches)throws FileNotFoundException{
        String [] arrayURL = searches.toArray(new String[searches.size()]);
        
        
       try
       {
            PrintWriter pr = new PrintWriter("savedURLS.txt");    

            for (int i=0; i<arrayURL.length ; i++)
            {
                pr.println(arrayURL[i]);
            }
            pr.close();
            }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("No such file exists.");
        }
    }
    
    public void loadURL()throws FileNotFoundException{
        String space = "";
        Scanner inFile = new Scanner(new File("savedURLS.txt")).useDelimiter("\\n");
        ArrayList<String> temps;
        temps = new ArrayList<String>();

        while (inFile.hasNext()) {
          space = inFile.next();
          temps.add(space);
          onScreen.add(space);
        }

        inFile.close();
      
        for(int i =0; i<temps.size(); i++){
           try{
               //System.out.println("test");
                URL url=new URL(temps.get(i));
                BufferedImage image = ImageIO.read(url);
                ImageIcon icon = new ImageIcon(image);
                icon = resizeImageIcon(icon);              
                JButton imageButton = new JButton(icon);
                onePanel.add(imageButton);
                onePanel.revalidate();
                
            }catch(Exception exp){
        
            } 
        }

    }
    
    
}

    
    
    
    




