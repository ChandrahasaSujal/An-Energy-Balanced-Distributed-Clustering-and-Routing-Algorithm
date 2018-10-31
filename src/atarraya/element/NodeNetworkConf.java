/*
 * NodeNetworkConf.java
 *
 * Created on November 15, 2007, 3:13 PM
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
 * @author pedrow
 */
import java.util.Vector;
import atarraya.constants;

public class NodeNetworkConf implements constants{
    
    boolean started;                    // This infrastructure have been started;
    
    //Node related variables
    int address;                            //Address of the node on the selected tree
    int level;                                 //Level of the node on the selected tree
    int state;                               //Status of the node on the network (active, sleep, not visitedetc.)
    double comm_radius;                //Diameter of the communication unit disk
    boolean radio_on;               //Is the radio on this infrastructure
    boolean sensor_on;               //Is the sensor on this infrastructure
    double metric;
    boolean sensing_covered;        //Defines if the node has been sensing-covered in this VNI

    int energy_state;

    int count_data_messages;

    //Labels
    int initial_node_label;
    int active_node_label;
    int inactive_node_label;
    int sleeping_node_label;
    int node_role;
    
    //Network related variables
    int network_id;                       //ID of the network
    
    Vector<pair> gateways;          //ID and address of the active neighbors
    
    int parent_id;                         //ID of the parent node, if there is only one
    int parent_address;                //Address of the parent node, if there is only one
    
    int sink_address;                    //Address of the sink node
    
    //Data structures with local information
    Vector<candidate> neighbors;  //List of (active?) neighbor nodes that are part of this network
    Vector<candidate> candidates; //Temporary list for operations
    
    routing_table routes_table;
    
    int counter;                   // General purpose counter

    


    /** Creates a new instance of NodeNetworkConf */
    public NodeNetworkConf(int network_id_, double comm_radius_) {
        
        started = false;

        energy_state = STATE_ACTIVE;
        network_id = network_id_;
        
        address=-1;
        level=-1;
    
        radio_on = true;
        metric = 0.0;
   
        count_data_messages = 0;

        sensing_covered = false;

        gateways = new Vector<pair>();
        
        parent_id=-1;
        parent_address=-1;
        sink_address=-1;
   
        state=S_NOT_VISITED;                        //Active but without network
        comm_radius=comm_radius_;

        neighbors =  new Vector<candidate>() ;
        candidates =  new Vector<candidate>() ;
    
        routes_table = new routing_table();
        
        
         initial_node_label = S_NOT_VISITED;
         active_node_label = S_PARENT;
         inactive_node_label = S_SLEEPING;
         node_role = ROLE_REGULAR;
        
         counter = 0;

         

    }

    public int getEnergyState(){
        return energy_state;
    }

    public void setEnergyState(int energy_state_){
        energy_state = energy_state_;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter_) {
        counter = counter_;
    }

    public void addCounter(int counter_) {
        counter+= counter_;
    }
    
    public int getCountDataMessages() {
        return count_data_messages;
    }

    public void addCountDataMessages() {
        count_data_messages++;
    }

    public void setCountDataMessages(int val) {
        count_data_messages= val;
    }
    
    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean _started) {
        started = _started;
    }
    
    public boolean isAutoupdate() {
        return routes_table.isAutoupdate();
    }

    public void setAutoupdate(boolean _autoupdate) {
        routes_table.setAutoupdate(_autoupdate);
    }

    public void setDefaultGateway(int _default_gateway) {
        routes_table.setDefaultGateway(_default_gateway);
    }

    public int getDefaultGateway() {
        return routes_table.getDefaultGateway();
    }
    
    public boolean isGateway(int ID){
        
        int i=0, tam = gateways.size();
        
        while(i<tam){
            if(gateways.get(i).getX() != ID)
                i++;
            else
                return true;
        }
        
        return false;
    }
    
    public routing_table getRoutingTable(){
        return routes_table;
    }
    
    public void setMetric(double m){
        metric = m;
    }
    
    public double getMetric(){
        return metric;
    }
    
    public void setRadioOn(boolean sw){
        radio_on = sw;
    }
    
    public boolean isRadioOn(){
        return radio_on;
    }

    public void defineLabels(int initial, int active, int inactive, int sleeping){
        initial_node_label = initial;
        active_node_label = active;
        inactive_node_label = inactive;
        sleeping_node_label = sleeping;
    }
    
    public void setActiveNodeState(){
        state = active_node_label;
    }
    
    public void setInactiveNodeState(){
        state = inactive_node_label;
    }
    
    public void setInitialNodeState(){
        state = initial_node_label;
    }
    
    public void setSleepingNodeState(){
        state = sleeping_node_label;
    }
    
    public int getLabel(int type_){
        
        int temp=0;
        
        switch(type_){
            
            case TYPE_LABEL_INITIAL:
                temp= initial_node_label;
                break;
                
            case TYPE_LABEL_ACTIVE:
                temp= active_node_label;
                break;
                
            case TYPE_LABEL_INACTIVE:
                temp= inactive_node_label;
                break;
                
            case TYPE_LABEL_SLEEPING:
                temp= sleeping_node_label;
                break;
                
        }
        
        return temp;
    }
    
    public void setRole(int role){
        node_role = role;
    }
    
    public int getRole(){
        return node_role;
    }
    
    public int getNetworkID(){
        return network_id;
    }
    
    public int getAddress(){
        return address;
    }
    
    public void setAddress(int address_){
        address=address_;
    }
    
    public int getLevel(){
        return level;
    }
    
    public void setLevel(int level_){
        level=level_;
    }
    
    
    public int getNumGateways(){
        return gateways.size();
    }
    
    public int getGatewayID(int index){
        if(gateways.size()>index)
            return gateways.get(index).getX();
        else
            return -1;
    }
    
    public void setGatewayID(int index, int _id){
        if(gateways.size()>index)
            gateways.get(index).setX(_id);
        
    }
    
    public int getGatewayAddress(int index){
        if(gateways.size()>index)
            return gateways.get(index).getY();
        else
            return -1;
    }
    
    public void setGatewayAddress(int index, int _address){
        if(gateways.size()>index)
            gateways.get(index).setY(_address);
    }
    
    public void addGateway(int _ID, int _address){
        gateways.add(new pair(_ID,_address));
    }
    
    public void clear_gateways(){
        gateways.clear();
    }
    
    public void RemoveGatewayFromID(int id){
        
         int i=0, tam = getNumGateways();
        boolean found = false;
        
        while(i<tam && !found){
            if(gateways.get(i).getX()==id){
                found=true;
                gateways.remove(i);
            }
            else
                i++;
        }
    }
    
    public String getGatewaysListNoDefGW(){
        String t="";
        int i=0, tam=0, j=0, def_gw=0,c=0;
        
        def_gw = getDefaultGateway();
        tam = getNumGateways();
        
        for(i=0;i<tam;i++){
            j=this.getGatewayID(i);
            if(j!=def_gw){
                t=t+"_"+j;
                c++;
            }
        }
        
        //If tam = 0, then the final chain will be "0"
        t=""+c+t;
        
        return t;
    }
    
    public String getGatewaysList(){
        String t="";
        int i=0, tam=0, j=0;
        
        tam = getNumGateways();
        
        for(i=0;i<tam;i++){
            j=this.getGatewayID(i);
            t=t+"_"+j;
        }
        
        //If tam = 0, then the final chain will be "0"
        t=""+tam+t;
        
        return t;
    }
    
    public int getSinkAddress(){
        return sink_address;
    }
    
    
    public void setSinkAddress(int sink_address_){
        sink_address=sink_address_;
    }
    
    
    public int getState(){
        return state;
    }
    
    public void setState(int state_){
        state=state_;
    }


    public double getComm_radius(){
        return comm_radius;
    }

    public void setComm_radius(double comm_radius_){
        comm_radius = comm_radius_;
    }
    
    
    //****************NEIGHBORS OPERATIONS*****************
    
    public int getNumNeighbors(){
        return neighbors.size();
    }
    
    public int getNeighborID(int n){
        return neighbors.get(n).getID();
    }

    public candidate getNeighbor(int n){
        return neighbors.get(n);
    }
    
    public candidate getNeighborFromID(int id){
        int i=0, tam = getNumNeighbors();
        boolean found = false;
        
        while(i<tam && !found){
            if(neighbors.get(i).getID()==id){
                found=true;
                return neighbors.get(i);
            }
            else
                i++;
        }
        return null;
    }
    
     public String getNeighborsList(){
        String s=""+neighbors.size()+"@\n";
        int i;
    
        /*
         * Format of the list: <# of neighbors>@index-ID-metric*index-ID-metric*...
         */
        
        for(i=0;i<neighbors.size();i++){
            s=s+neighbors.get(i).toString2();
        }
    
        return s;
    }
    
    public void addNeighbor(candidate n){
        neighbors.add(n);
    }
     
    
    
    public void RemoveNeighborFromID(int id){
        
         int i=0, tam = getNumNeighbors();
        boolean found = false;
        
        while(i<tam && !found){
            if(neighbors.get(i).getID()==id){
                found=true;
                neighbors.remove(i);
            }
            else
                i++;
        }
    }
    
    public void RemoveNeighborFromIndex(int index){
        neighbors.remove(index);
    }
    
    public void SortNeighbors(){
        java.util.Collections.sort(neighbors);
    }
    
     public boolean isNeighbor(int id){
        int i=0, tam = neighbors.size();
        boolean sw;
        
        while(i<tam){
            if(neighbors.get(i).getID()==id)
                return true;
            i++;
        }
        
        return false;
    }
    
 //****************CANDIDATES OPERATIONS*****************
    
    public int getNumCandidates(){
        return candidates.size();
    }
    
    public int getCandidateID(int n){
        return candidates.get(n).getID();
    }

    public candidate getCandidate(int n){
        return candidates.get(n);
    }
    
    public candidate getCandidateFromID(int id){
        int i=0, tam = getNumCandidates();
        boolean found = false;
        
        while(i<tam && !found){
            if(candidates.get(i).getID()==id){
                found=true;
                return candidates.get(i);
            }
            else
                i++;
        }
        return null;
    }
    
    public String getCandidateList(){
        String s=""+candidates.size()+"@";
        int i;
    
        /*
         * Format of the list: <# of candidates>@index-ID-metric*index-ID-metric*...
         */
        
        for(i=0;i<candidates.size();i++){
            s=s+candidates.get(i).toString()+"#";
        }
    
        return s;
    }
    
    public String getCandidateList2(){
        String s=""+candidates.size()+"@\n";
        int i;
    
        /*
         * Format of the list: <# of candidates>@index-ID-metric*index-ID-metric*...
         */
        
        for(i=0;i<candidates.size();i++){
            s=s+candidates.get(i).toString2();
        }
    
        return s;
    }
   
    public String getCandidateIDList(){
        int i, tam;
        
        tam = candidates.size();
        String s="";
        
        for(i=0;i<tam;i++){
            s=s+"_"+candidates.get(i).getID();
        }
        
        s=""+tam+s;
        
        return s;
    
    }
    
    public void addCandidate(candidate n){
        candidates.add(n);
    }
       
    public void RemCandidate(){
        candidates.removeElementAt(0);
    }

    public void RemCandidateFromIndex(int index){
        candidates.removeElementAt(index);
    }
    
    public void RemoveCandidateFromID(int id){
        
         int i=0, tam = getNumCandidates();
        boolean found = false;
        
        while(i<tam && !found){
            if(candidates.get(i).getID()==id){
                found=true;
                candidates.remove(i);
            }
            else
                i++;
        }
    }
      
    public void SortCandidates(){
        java.util.Collections.sort(candidates);
    }

    
    public boolean isCandidate(int id){
        int i=0, tam = candidates.size();
        boolean sw;
        
        while(i<tam){
            if(candidates.get(i).getID()==id)
                return true;
            i++;
        }
        
        return false;
    }
    
    public void cleanCandidates(){
        candidates.clear();
    }
    
    public void cleanNeighbors(){
        neighbors.clear();
    }
    
    public void reset_infrastructure(){
        setInitialNodeState();
        cleanCandidates();
        cleanNeighbors();
        setLevel(-1);
        setMetric(0);
        clear_gateways();
        routes_table.clearRoutingTable();
        setCountDataMessages(0);
        setCounter(0);
        setRadioOn(true);
        setSensing_covered(false);
    }

    public boolean isSensing_covered() {
        return sensing_covered;
    }

    public void setSensing_covered(boolean sensing_covered) {
        this.sensing_covered = sensing_covered;
    }
    
    public boolean isFinalState(){
        
        if(isSleeping() || isActive() || isInactive())
            return true;
        
        return false;
    }
    
    public boolean isActive(){
        if(state==active_node_label || node_role == ROLE_SINK)
            return true;
        
        return false;
    }
    
    public boolean isSink(){
        if(node_role == ROLE_SINK)
            return true;
        
        return false;
    }
    
    public boolean isInitialState(){
        if(state==initial_node_label)
            return true;
        
        return false;
    }
    
    public boolean isInactive(){
        if(state==inactive_node_label)
            return true;
        
        return false;
    }
    
    public boolean isVisited(){
        if(state!=getLabel(TYPE_LABEL_INITIAL))
            return true;
        
        return false;
    }
    
    public boolean isCovered(){    
        
        //If it has a parent in the tree, it is covered
        if(getNumGateways()>0)
            return true;
       
        return false;
    }
  
    public boolean isSelected(){
        if(state==S_SELECTED)
            return true;
        
        return false;
    }
    
    public boolean isAlive(){
        if(state!=S_DEAD)
            return true;
        
        return false;
    }
    
    public boolean isSleeping(){
        if(state == sleeping_node_label)
            return true;
        
        return false;
    }
    
}
