/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package atarraya.element;

/**
 *
 * @author Kitty
 */
public class AccountDetails {

   private String NODE_ID;
   private String ACC_NO;
  

   public AccountDetails(String node_id,String ACC_NO)
    {
       this.NODE_ID=node_id;
       this.ACC_NO=ACC_NO;
      
   }

   public String getNodeID()
    {
       return this.NODE_ID;
   }

   public String getACCNumber()
    {
       return this.ACC_NO;
   }

  


}
