

package atarraya;

import javax.swing.UIManager;

/**
 *
 * @author pedrow
 */
public class Main{
    
    
    /** Creates a new instance of Main */
    public Main() {
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
        UIManager.setLookAndFeel(
	    "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }catch(Exception e){e.printStackTrace();}
        
        
        atarraya_frame mySim = new atarraya_frame();
        mySim.setBounds(10,0,1100,700);
        
        try{
            java.lang.Thread.sleep(2000);
        }catch(Exception e){e.printStackTrace();}
               
        mySim.setVisible(true);
        
        
    }
    
}
