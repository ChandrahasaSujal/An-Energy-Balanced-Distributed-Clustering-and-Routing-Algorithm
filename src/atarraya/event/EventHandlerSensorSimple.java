/*
 * EventHandlerA3.java
 *
 * Created on February 8, 2007, 2:48 PM
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

package atarraya.event;

import atarraya.constants;
import atarraya.element.candidate;
import atarraya.element.node;
import atarraya.element.register;
import atarraya.atarraya_frame;

/**
 *
 * @author Pedro
 */


public class EventHandlerSensorSimple implements EventHandlerSW,constants{
    
    atarraya_frame father;
    
    int type = SENSOR_PROTOCOL;
    
    int TMType;
    
    int temp_address=0;
            
    /** Creates a new instance of EventHandlerA3 */
    public EventHandlerSensorSimple(atarraya_frame _father) {
        
        father = _father;  
        TMType = NO_TM;
    }
    
    public int getTMType(){
        return TMType;
    }
    
    public void setLabels(int _initial,int _active, int _inactive, int _sleeping){ }   
    
    public int getTreeID(){
        return father.getTreeID();
    }
    
    public int getSortingMode(){
        return (int)father.getVariable(SORTINGMODE);
    }
    
    public int getSimMode(){
        return (int)father.getVariable(SIMMODE);
    }    
    
    
    public int getbatchMode(){
        return (int)father.getVariable(BATCH_SIMULATION);
    }
    
 public node getNode(int id){
        return father.getNode(id);
    }
    
    public void pushEvent(event_sim e){
        father.pushEvent(e);
    }
    
    public void AddTreeLine(int s, int d, int t){
        if(getbatchMode()==1){}
            //father.AddTreeLine(s,d,t);
    }
    
    public void RemoveTreeLine(int s, int d, int t){
        if(getbatchMode()==1){}
            //father.RemoveTreeLine(s,d,t);
    }
    
    public void frame_repaint(){
        if(getbatchMode()==1)
            father.frame_repaint();
        
    }
 
    
    public void InvalidateAllEventsFromIDFromTimeTOfCodeC(int id, double t, int c, int tree){
        father.InvalidateAllEventsFromIDFromTimeTOfCodeC(id,t,c,tree);
    }
    
    public void InvalidateAllEventsFromIDFromTimeT(int id, double t, int tree){
        father.InvalidateAllEventsFromIDFromTimeT(id,t,tree);
    }
    
    public void InvalidateAllEventsFromIDFromTimeTOfTypeTy(int id, double t, int Ty, int tree){
        father.InvalidateAllEventsFromIDFromTimeTOfTypeTy(id,t,Ty,tree);
    }
 
    
 public void broadcast(double final_time, double current_time, int _sender, int _destination, int _code){
        father.broadcast(final_time, current_time, _sender, _destination, _code,"",-1,type);
    }

 public void broadcast(double final_time, double current_time, int _sender, int _destination, int _code, int _tree){
        father.broadcast(final_time, current_time, _sender, _destination, _code,"",_tree,type);
    }
 
 public void broadcast(double final_time, double current_time, int _sender, int _destination, int _code, String msg){
        father.broadcast(final_time, current_time, _sender, _destination, _code,msg,type);
    }
    
 public void broadcast(double final_time, double current_time, int _sender, int _destination, int _code, String msg, int _tree){
        father.broadcast(final_time, current_time,-1,-1, _sender, _destination, _code,msg,_tree,type);
    }
 
 public void broadcast(double final_time, double current_time, int _source, int _final_destination,int _sender, int _destination, int _code, String msg, int _tree){
        father.broadcast(final_time, current_time,_source,_final_destination, _sender, _destination, _code,msg,_tree,type);
    }
 
 public double getRandom(double max_val){
        return java.lang.Math.random()*max_val;
    }
 
 public void showMessage(String m){
        father.showMessage(m);
    }

 public void showEventList(String m){
    father.showEventList(m);
 } 
 
  public void init_node(int _treeID, int _id){
    getNode(_id).setState(S_NOT_VISITED,_treeID);
  }
  
    public void init_nodes(int _treeID){
        int i;
        
        for(i=0;i<father.getVariable(NUMPOINTS);i++){
            getNode(i).setState(S_NOT_VISITED,_treeID);
        }
        
    }
    
    public void initial_event(int _id, int _treeID){
        
        //Obtain the time from the simulator
        double _clock = father.getVariable(CLOCK);
        
        //stop all future event from the sensor-data protocol
        InvalidateAllEventsFromIDFromTimeTOfTypeTy(_id, _clock, type,_treeID);

        getNode(_id).setSensorOn(true);
        
        //Program a new Query Sensor on the tree_ID received
        pushEvent(new event_sim(_clock+TIMEOUT_FOR_FIRST_QUERY_SENSOR, _clock, _id, _id, QUERY_SENSOR,"",_treeID,type));
        
    }
    
    public boolean CheckIfDesiredFinalState(int s){
        if(s==S_PARENT || s==S_SLEEPING)
            return true;
        
        return false;
    }
    
    public boolean CheckIfDesiredSleepingState(int s){
        if(s==S_SLEEPING)
            return true;
        
        return false;
    }
    
    public boolean verifIfNodeInCDS(int _id, int _treeID){
    
        if(getNode(_id).isActive(_treeID) || getNode(_id).isSink(_treeID) || _treeID==-1)
            return true;
        
        return false;
    }
    
    public int GetMessageSize(int code){
        
        if(code==DATA)
            return SIZE_LONG_PACKETS;
            
        return SIZE_SHORT_PACKETS;
    }
    
    public void HandleEvent(event_sim e){
    
        int code = e.getCode();
        int sender = e.getSender();
        int source = e.getSource();
        int final_destination = e.getFinalDestination();
        int receiver = e.getReceiver();
        int destination = e.getDestination();
        double temp_clock = e.getTime();
        String temp_data = e.getData();
        int temp_tree = e.getTree();
        int temp_data_int=0;
        int temp_data_int2=0;
        int temp_data_int3=0;
        String[] temp_data_array = new String[50];
        String[] temp_data_array2;
        
        int numneighb,i,j,temp_ID;
        int index,tam;
        int min_child;
        register temp_reg;
        int tempdist;
        int temp_level=0;
        int temptam=0;
        int tempmax,nmax;
        int temp=0;
        int temp2=0;
        double temp_time;
        double metric=0;
        double y;
        boolean sw=true;
        boolean sw2;
        boolean sw3 = true;
        candidate temp_cand;

        if(receiver != -1)
            sw3 = getNode(receiver).isAlive();
        

        if(sw3){
            
        switch(code){
            
            case INIT_NODE:
                init_node(temp_tree, receiver);
                break;
            
            case INIT_EVENT:
                initial_event(sender,temp_tree);
                break;
            
            case RESET_QUERY_SENSOR:
                //stop all future event from the sensor-data protocol
                InvalidateAllEventsFromIDFromTimeTOfTypeTy(receiver, temp_clock+DELTA_TIME, type,temp_tree);
                break;
                
            case QUERY_SENSOR:
                
                temp = getNode(receiver).getActiveTree();
                
                //If the node is not the sink and is active on its active tree
                if(!getNode(receiver).isSink(temp) && getNode(receiver).isActive(temp)){
                    
                    //Obtain the reading from the sensor at the time of the execution
                    temp_data = getNode(receiver).getSensorReading(temp_clock);
                    
                    //Obtain the sink's address... In this protocol the only final destination is the sink.
                    temp_ID = getNode(receiver).getSinkAddress(temp_tree);
                    
                    //Send the message
                    broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, receiver, temp_ID, receiver, -2, DATA, temp_data,temp);
                    
                    
                    
                    //Obtain the interquery time of the node to schedule the next reading
                    temp_time = getNode(receiver).getInterqueryTime();
                    
                    //Program the next query of the sensor
                    pushEvent(new event_sim(temp_clock+temp_time, temp_clock, sender, sender, QUERY_SENSOR, "",temp,type));
                }
                
                break;
                
            case DATA:
                
                //If the node that received the data is the next step or if it was a flooding
                if(receiver == destination || destination == -1){
                        
                    if(receiver!=final_destination && destination != -1){
                            broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, source, final_destination, receiver, -2, DATA, temp_data,temp_tree);
                        }else if(receiver==final_destination){
                            showEventList("Node "+receiver+" received this message: "+temp_data+"from node "+source);
                    }else if(destination == -1){
                        //IT IS A FLOODING MESSAGE... NO PROTOCOL DEFINED YET!!
                    }
                }
                
                break;
              
                
                
            default:
                break;
        }
        }
        
    }
}
