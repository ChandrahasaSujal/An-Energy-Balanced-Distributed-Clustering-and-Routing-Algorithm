/*
 * routing_table_register class
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

import atarraya.constants;
import java.lang.Comparable;

/**
 *
 * @author Pedro
 */
public class routing_table_register implements constants, Comparable{

    int gateway_ID;             // ID of the outgoing gateway, first step on the path.
    int node_Address;         // Address of the destination node
    double node_metric;               // Routing Metric
    int num_hops;
    
    //This is an array that stores the last sequence numbers of the messages received by a node.
    // This is very useful for flooding techniques or packet forwarding.
    int sequence_array[];  
    int current_seq_num_index;
    
    public routing_table_register(int gw_id, int gw_address){        
        gateway_ID = gw_id;
        node_Address = gw_address;
        node_metric=Double.MAX_VALUE;
        num_hops = Integer.MAX_VALUE;
        
        sequence_array = new int[SEQUENCE_NUMBER_ARRAY_SIZE];
        current_seq_num_index=0;
        
        for(int i=0;i<SEQUENCE_NUMBER_ARRAY_SIZE;i++)
            sequence_array[i] = 0;
        
    }
    
    public routing_table_register(int gw_id, int gw_address, double _metric){
        this(gw_id,gw_address);
        node_metric = _metric;
    }
    
    public routing_table_register(int gw_id, int gw_address, double _metric, int _hops){
        this(gw_id,gw_address,_metric);
        num_hops = _hops;
    }

    public int getNum_hops() {
        return num_hops;
    }

    public int getGatewayID(){
        return gateway_ID;
    }
    
    public int getNodeAddress(){
        return node_Address;
    }
    
    public double getNodeMetric(){
        return node_metric;
    }
    
    public boolean checkSequenceNumber(int seq_numb){
        
        int i=0;
        
        // Given that the history is not going to be very big, is better to avoid the overhead of sorting and do
        // sequential search only.
        for(i=0;i<SEQUENCE_NUMBER_ARRAY_SIZE;i++){
            if(sequence_array[i]==seq_numb)
                return true;
        }
        
        return false;
    }
    
    public void setNodeAddress(int _address){
        node_Address = _address;
    }
    
    public void setGatewayID(int _id){
        gateway_ID = _id;
    }
    
    public void setNodeMetric(double _metric){
        node_metric = _metric;
    }

    public void setNum_hops(int _num_hops) {
        num_hops = _num_hops;
    }
    
    
    public void addSequenceNumber(int seq_numb){
        sequence_array[current_seq_num_index]=seq_numb;
        current_seq_num_index = (current_seq_num_index+1)%SEQUENCE_NUMBER_ARRAY_SIZE;
    }
    
    
    public String toString(){
        
        return ""+node_Address+"@"+gateway_ID+"@"+node_metric+"@"+num_hops;
    }
    
    
    public String toString4TMLocal(){
        
        return ""+node_Address+"@"+gateway_ID+"@"+0+"@"+0;
    }
    
    public int compareRoute(Object obj){
        int temp=0;
        
        if(obj instanceof routing_table_register){
            routing_table_register e = (routing_table_register)obj;
            
            temp = (int)java.lang.Math.round(e.getNum_hops()-num_hops);
            if(temp==0)
                return (int)java.lang.Math.round(node_metric-e.getNodeMetric());        
                else
                    return temp;
            
        }
        
        return -2000;
    }
    
    public int compareTo(Object obj){
        int temp=0;
        
        if(obj instanceof routing_table_register){
            routing_table_register e = (routing_table_register)obj;
            temp = node_Address-e.getNodeAddress();
            
            if(temp==0){
                temp = (int)java.lang.Math.round(node_metric-e.getNodeMetric());
                if(temp==0)
                        return (int)java.lang.Math.round(num_hops-e.getNum_hops());
                    else
                        return temp;
            }else
                return temp;
            
        }
        
        return -2000;
    }
    
}
