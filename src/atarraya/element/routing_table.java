/*
 * routing_table class
 *
This file is part of Atarraya.

Atarraya is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Atarraya is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Atarraya.  If not, see <http://www.gnu.org/licenses/>.

 Copyright 2009 Pedro M. Wightman
 */

package atarraya.element;

/**
 *
 * @author Pedro
 */

import java.util.Vector;
import atarraya.constants;

public class routing_table {

    Vector<routing_table_register> the_table;
    int default_gateway;
    boolean autoupdate;
    boolean routing_active;
    
    public routing_table(){
        the_table = new Vector<routing_table_register>();
        default_gateway=-1;
        autoupdate = true;
        routing_active = false;
    }

    public boolean isAutoupdate() {
        return autoupdate;
    }

    public void setAutoupdate(boolean _autoupdate) {
        autoupdate = _autoupdate;
    }

    public boolean isRoutingActive(){
        return routing_active;
    }
    
    public void setRoutingActive(boolean route_active){
        routing_active = route_active;
    }
    
    public void setDefaultGateway(int _default_gateway) {
        default_gateway = _default_gateway;
    }

    public int getDefaultGateway() {
        return default_gateway;
    }
    
    public routing_table_register getRegister(int index){
        if(index < the_table.size())
            return the_table.get(index);
        
        return null;
    }
    
    public void clearRoutingTable(){
        the_table.clear();
        default_gateway=-1;
        routing_active=false;
        autoupdate = true;
    }
    
    public int getClosestRegisterByAddress(int address){
        
        int n = the_table.size();
        int low, up, mid=n/2;

        //Binary search algorithm to find the address and to get the correspondent Gateway... Of course, the array is sorted!!
        if(n>0){
        low = 0;
        up = n-1;

        while(low < up){
            mid = (int)java.lang.Math.floor((low+up)/2);
            if(address > the_table.get(mid).getNodeAddress())
                low = mid+1;
            else if(address < the_table.get(mid).getNodeAddress())
                up =  mid;
            else
                low = up = mid;
        }
        
        return low;
        
        }
        
        return -1;
    }
    
    public int getGateway(int address){
        
        if(routing_active){
            //Get the closest adress in the routing to the one provided
            int temp_reg = getClosestRegisterByAddress(address);

            // If the address was found on the table and is the same to the one provided, get the proper gateway
           if(temp_reg != -1 && the_table.get(temp_reg).getNodeAddress() == address)
                return the_table.get(temp_reg).getGatewayID();
        }
        
        //If the address is not on the routing table, use the default gateway.
        return default_gateway;
    }
    
    
     
     public boolean addRoutingRegister(routing_table_register e){
        
        int n = the_table.size();
        int temp_reg=0;
        int temp_address = e.getNodeAddress();
        boolean sw=true;
        
        if(n==0)
            the_table.add(e);
        else{
        
            //Find the closest address in the table to the one provided
            temp_reg = getClosestRegisterByAddress(e.getNodeAddress());
            
            //Define if the provided address must be before or after the one in the table. If the addresses are equal, the method
            // determines the best route (lowest number of hops, if equal, greater metric) and replaces if the new entry is better.
            if(temp_address > the_table.get(temp_reg).getNodeAddress())
                    the_table.insertElementAt(e,temp_reg+1);
            else if(temp_address < the_table.get(temp_reg).getNodeAddress())
                    the_table.insertElementAt(e,temp_reg);
            //If the destination node is the same, and the entries are equal or the route is worse, there is no change.
            else if(the_table.get(temp_reg).compareRoute(e) < 0)
                    the_table.set(temp_reg, e);
            else   sw=false; //If the new register did not change the table
        }
        
        return sw;
    }
     
     public boolean remRoutingRegister(routing_table_register e){
        
        int n = the_table.size();
        int temp_reg=0;
        int temp_address = e.getNodeAddress();
        boolean sw=true;
        
        if(n==0)
            return sw;
        else{
        
            //Find the closest address in the table to the one provided
            temp_reg = getClosestRegisterByAddress(e.getNodeAddress());
            
            //Define if the provided address must be before or after the one in the table. If the addresses are equal, the method
            // determines the best route (lowest number of hops, if equal, greater metric) and replaces if the new entry is better.
            if(temp_address == the_table.get(temp_reg).getNodeAddress())
                    the_table.removeElementAt(temp_reg);
            else   sw=false; //If the new register did not change the table
        }
        
        return sw;
    }
     
     public int getRoutingTableSize(){
        return the_table.size();
     }
     
    @Override
     public String toString(){
     
         String t="";
         int i;
         int tam = the_table.size();
         
         for(i=0;i<tam;i++)
             t = t + the_table.get(i).toString()+"#";
         
         return t;
     }
     
    public String toString4TMLocal(){
     
         String t="";
         int i;
         int tam = the_table.size();
         
         for(i=0;i<tam;i++)
             t = t + "#" + the_table.get(i).toString4TMLocal();
         
         t = ""+tam+t;
         
         return t;
     }
    
    
     public String toString4TMLocal2(){
     
         String t="";
         int i;
         int tam = the_table.size();
         
         for(i=0;i<tam;i++)
             t = t + "#" +the_table.get(i).toString();
         
         t = ""+tam+t;
         
         return t;
     }
    
     /*
      
      * YOU ARE TRYING TO IMPLEMENT A BELLMAN-FORD ROUTING PROTOCOL THAT WILL FIT THE KIND OF STRUCTURE YOU HAVE NOW... WELL
      * IF THE NODES WANT TO SHARE THEIR TABLES AND EVERYTHING. BUT THAT IS THE IDEA... LOOK FOR THE ALGORITHM AND
      * IMPLEMENT IT!!
      * 
      */
     
     
}
