/*
 * EventHandlerTCRuleK.java
 *
 * Created on March 8, 2007, 1:43 PM
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

import atarraya.*;
import atarraya.element.*;


/*      @author Pedro      */
public class EventHandlerTCRuleK implements EventHandlerSW,constants{
    
    int type= TC_PROTOCOL;
    
    atarraya_frame father;
    
    int active;
    int inactive;
    int initial;
    int sleeping;
    boolean TM_Selected;
    boolean SD_Selected;
    boolean COMM_Selected;
    int TMType;
    int temp_selectedTMprotocol;
    int temp_num_structures;
    
    
    /** Creates a new instance of EventHandlerCDS */
    public EventHandlerTCRuleK(atarraya_frame _frame)
    {
        father = _frame;
        
        TM_Selected=false;
        SD_Selected=false;
        COMM_Selected=false;
        TMType = NO_TM;
        
        if(father.getVariable(SELECTED_TM_PROTOCOL)!=TM_PROTOCOL_NO_SELECTED)
            TM_Selected=true;
        
        if(father.getVariable(SELECTED_SD_PROTOCOL)!=SENSOR_PROTOCOL_NO_SELECTED)
            SD_Selected=true;
        
        if(father.getVariable(SELECTED_COMM_PROTOCOL)!=COMM_PROTOCOL_NO_SELECTED)
            COMM_Selected=true;
        
        temp_selectedTMprotocol = (int)father.getVariable(SELECTED_TM_PROTOCOL);
        
        temp_num_structures = (int)father.getVariable(NUMINFRASTRUCTURES);
        
        active = 0;
        inactive = 0;
        initial = 0;
        sleeping = 0;
    }
    
    public int getTMType(){
        return TMType;
    }
    
    public void setLabels(int _initial,int _active, int _inactive, int _sleeping){
        active=_active;
        inactive=_inactive;
        initial=_initial;
        sleeping = _sleeping;
    }
    
    public int getTreeID()
    {
        return father.getTreeID();
    }
    
    public int getSortingMode(){
        return (int)father.getVariable(SORTINGMODE);
    }
    
    public int getSimMode()
    {
        return (int)father.getVariable(SIMMODE);
    }    
    
    
     public int getbatchMode(){
        return (int)father.getVariable(BATCH_SIMULATION);
    }
    
     public node getNode(int id)
    {
        return father.getNode(id);
    }
    
    public void pushEvent(event_sim e)
    {
        father.pushEvent(e);
    }
    
    public void AddTreeLine(int s, int d, int t)
    {
        //father.AddTreeLine(s,d,t);
    }
    
    public void RemoveTreeLine(int s, int d, int t)
    {
        //father.RemoveTreeLine(s,d,t);
    }
    
    public void frame_repaint()
    {
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
    
    public void InvalidateAllEventsFromIDFromTimeTOfTypeTyOfCodeC(int id, double t, int Ty, int c, int tree){
        father.InvalidateAllEventsFromIDFromTimeTOfTypeTyOfCodeC(id,t,Ty,c,tree);
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

    public double getRandom(double max_val)
    {
        return java.lang.Math.random()*max_val;
    }
 
    public void showMessage(String m)
    {
        father.showMessage(m);
    }

    
    public void init_nodes(int _treeID){
        int i;
        double _clock = father.getVariable(CLOCK);
        
        for(i=0;i<father.getVariable(NUMPOINTS);i++){
            
            getNode(i).setState(initial,_treeID);
            getNode(i).defineLabels(initial,active, inactive,sleeping);
            getNode(i).SetInfrastructureStarted(_treeID,true);
            
             if(TM_Selected){
                //Program the next topology creation
                pushEvent(new event_sim(_clock+getRandom(PROCESSING_DELAY), _clock, i, i, RESET_TM_PROTOCOL, "",_treeID,TM_PROTOCOL));
            }

            if(SD_Selected){
                //stop all future event from the sensor-data protocol
                pushEvent(new event_sim(_clock+getRandom(PROCESSING_DELAY), _clock, i, i, RESET_QUERY_SENSOR, "",_treeID,SENSOR_PROTOCOL));
            }
            
        }
    }
    
    public void init_node(int _treeID, int id_node){
        getNode(id_node).setState(S_NOT_VISITED,_treeID);
    }
    
    public void initial_event(int _id, int _treeID){
        getNode(_id).setState(C_BLACK,_treeID);
        getNode(_id).setLevel(0, _treeID);
        double _clock = father.getVariable(CLOCK);
        pushEvent(new event_sim(_clock+PROCESSING_DELAY, _clock, _id, _id, SEND_HELLO, "",_treeID,type));
        //father.simulation_pause();
        getNode(_id).addGateway(_treeID,_id,_id);
        getNode(_id).setSinkAddress(_treeID, _id);
        getNode(_id).setDefaultGateway(_treeID,_id);
        
        if(SD_Selected){
            //stop all future event from the sensor-data protocol
            pushEvent(new event_sim(_clock+getRandom(PROCESSING_DELAY), _clock, _id, _id, RESET_QUERY_SENSOR, "",_treeID,SENSOR_PROTOCOL));
        }
    }
    
    
    public boolean CheckIfDesiredFinalState(int s){
        //if(s==C_BLACK_FINAL || s==C_UNMARKED)
        //if(s==active || s==inactive || s==sleeping)
        if(s==active || s==sleeping)
            return true;
        
        return false;
    }
    
    public boolean CheckIfDesiredSleepingState(int s){
        //if(s==C_UNMARKED)
        if(s==sleeping)
            return true;
        
        return false;
    }
    
    public boolean verifIfNodeInCDS(int _id, int _treeID){
    
        //if(getNode(_id).getState(_treeID)==C_BLACK_FINAL || _treeID==-1)
        if(getNode(_id).getState(_treeID)==active || _treeID==-1)
            return true;
        
        return false;
    }
    
    public int GetMessageSize(int code){
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
        double temp_data_double=0.0;
        String[] temp_data_array = new String[50];
        String[] temp_data_array2;
        String data, data1 = "TRANSITION", data2 = "WHITE";
        
                
        int numneighb,i,x,temp_ID;
        int index,tam;
        int min_child;
        register temp_reg;
        int tempdist;
        int temp_level=0;
        int temptam=0;
        int tempmax,nmax;
        int temp=0;
        int temp2=0;
        int temp3=0;
        int temp4=0;
        int temp5=0;
        double temp_time;
        double metric=0;
        double weight;
        double temp_weight;
        boolean sw;
        boolean sw2;
        candidate temp_cand;
        double temp_data_double2;
       
        
        switch(code){
        
            case INIT_NODE:
                init_node(temp_tree, receiver);
                break;
            
            case INIT_EVENT:
                if(getNode(receiver).isSink())
                    initial_event(receiver,temp_tree);
                break;
            
            //The node receives a signal to send its first hello message
            case SEND_HELLO:
                    broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, sender, -1, HELLO, ""+getNode(sender).getLevel(temp_tree)+"#"+getNode(sender).getSinkAddress(temp_tree),temp_tree);
                    pushEvent(new event_sim(temp_clock+TIMEOUT_DELAY_SHORT, temp_clock, sender, sender, TIMEOUT_RECOGNITION,"",temp_tree,type));
                break;
            
            //The node receives a hello message
            case HELLO:
                
                temp=0;
                
                 temp_data_array = temp_data.split("#");
                try{
                    temp = Integer.parseInt(temp_data_array[0]);        //temp = Level of the node it received the Hello message
                    temp2 = Integer.parseInt(temp_data_array[1]);        //temp2 = sink's address'
                }catch(Exception ex){ex.printStackTrace();}
                
                getNode(receiver).addCandidate(temp_tree,new candidate(sender,sender,0,getSortingMode(),C_UNMARKED,temp));
                
                
                if(getNode(receiver).getState(temp_tree) == S_NOT_VISITED){
                    pushEvent(new event_sim(temp_clock+PROCESSING_DELAY, temp_clock, receiver, receiver, SEND_HELLO, "",temp_tree,type));
                    getNode(receiver).setState(C_UNMARKED,temp_tree);
                    getNode(receiver).setLevel(temp+1, temp_tree);  
                    //getNode(receiver).addGateway(temp_tree, -2,-2);
                    getNode(receiver).setSinkAddress(temp_tree, temp2);

                }
                break;
                
                //When the Hello timeout expires
            case TIMEOUT_RECOGNITION:
                
                 temp2=getNode(sender).getNumCandidates(temp_tree);
                 temp_data = "";
                 
                if(temp2>0){

                    for(i=0;i<temp2;i++){
                        
                        //Get the i th candidate
                        temp_cand = getNode(sender).getCandidate(temp_tree,i);
                        
                        temp_data = temp_data + temp_cand.getID()+"#"+ temp_cand.getLevel()+"-";
                    }
                }
                 
                //Building the packet to sent to the rest of candidates
                temp_data = ""+temp2+"@"+temp_data;

                //Sending the packet
                broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, receiver, -1, C_NEIGHBORHOOD_COUNT, temp_data,temp_tree);                    
                
                //Waiting for the replies
                pushEvent(new event_sim(temp_clock+TIMEOUT_DELAY, temp_clock, receiver, receiver, C_NEIGHBORHOOD_COUNT_TIMEOUT, "",temp_tree,type)); 
                break;
        
            case C_NEIGHBORHOOD_COUNT:

                temp3=0;                                                                        //Neighbor counter reset
                
                temp_data_array = temp_data.split("@");
                try{
                    temp = Integer.parseInt(temp_data_array[0]);        //temp = Number of brothers that this node has
                }catch(Exception ex){ex.printStackTrace();}

                if(temp_data_array.length > 1){
                  
                    temp_data = temp_data_array[1];
                    temp_data_array = temp_data.split("-");                     // temp_data_array = IDs ald levels of the brothers

                    for(i=0;i<temp;i++){
                        
                        temp_data_array2 = temp_data_array[i].split("#");
                        try{
                            temp2 = Integer.parseInt(temp_data_array2[0]);        //temp = ID from the list of candidates of the sender
                            temp4 = Integer.parseInt(temp_data_array2[1]);        //temp = Level from the list of candidates of the sender
                        }catch(Exception ex){ex.printStackTrace();}
                        
                        
                        if(temp2 != receiver && (getNode(receiver).isCandidate(temp_tree,temp2)))
                            temp3++;
                    }
                    
                    temp_cand = getNode(receiver).getCandidateFromID(temp_tree, sender);
                    if(temp_cand!=null)
                        temp_cand.setMetric(temp3);
                    
                    
                }
                
                break;
                
                
            case C_NEIGHBORHOOD_COUNT_TIMEOUT:
                
                i=0;
                temp = getNode(receiver).getNumCandidates(temp_tree);
                
                while(i<temp && getNode(receiver).getCandidate(temp_tree, i).getMetric()==temp)
                    i++;
                
                if(i<temp || getNode(receiver).isSink(temp_tree)){                
                    getNode(receiver).setState(C_BLACK,temp_tree);
                    broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, receiver,sender, C_BLACK_MARKER, "",temp_tree);
                    //pushEvent(new event_sim(temp_clock+TIMEOUT_DELAY_SHORT+getRandom(DELTA_TIME), temp_clock, receiver, receiver, C_BLACK_LIST_QUERY_TIMEOUT, "",temp_tree,type)); 
                }
                
                pushEvent(new event_sim(temp_clock+TIMEOUT_DELAY_SHORT+getRandom(DELTA_TIME), temp_clock, receiver, receiver, C_BLACK_LIST_QUERY_TIMEOUT, "",temp_tree,type)); 
                
                for(i=0;i<temp;i++)
                    getNode(receiver).getCandidate(temp_tree, i).setMetric(0);
                
                break;
                
                
            case C_BLACK_MARKER:

                if(!getNode(receiver).isSleeping(temp_tree)){
                    if(getNode(receiver).isCandidate(temp_tree, sender))
                        getNode(receiver).getCandidateFromID(temp_tree, sender).setState(C_BLACK);
                }
                
                break;
                
            case C_BLACK_LIST_QUERY_TIMEOUT:

                if(!getNode(sender).isSink(temp_tree)){
                    temp = getNode(receiver).getNumCandidates(temp_tree);
                    temp3 = getNode(sender).getLevel(temp_tree);

                    for(i=0;i<temp;i++){
                            temp_cand = getNode(receiver).getCandidate(temp_tree,i);
                            if(temp_cand.getState() == C_BLACK && temp_cand.getLevel() <temp3){
                                temp_data = temp_data + temp_cand.getID() + "#" + temp_cand.getLevel() + "-";
                                temp2++;
                            }
                        }

                    temp_data = temp2 + "@" + temp_data;
                }else{
                    
                    temp_data = 1 + "@" + sender+"#"+0+"-";

                    if(getNode(receiver).getState(temp_tree) == C_BLACK)
                        pushEvent(new event_sim(temp_clock+TIMEOUT_DELAY_SHORT, temp_clock, receiver, receiver, C_BLACK_LIST_REPLY_TIMEOUT, "",temp_tree,type));                                     
                }
                
                broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, receiver,-1, C_BLACK_LIST_QUERY, temp_data,temp_tree);
                
                break;
                
                
            case C_BLACK_LIST_QUERY:
                
                if(getNode(receiver).getState(temp_tree) == C_BLACK){

                    temp3=0;                                                                        //Neighbor counter reset
                    temp_data_int = getNode(receiver).getLevel(temp_tree);
                    
                    if(getNode(receiver).isCandidate(temp_tree, sender)){

                    temp_level = getNode(receiver).getCandidateFromID(temp_tree, sender).getLevel();

                    //Only if the level of the node is higuer, I will consider its real metric. In other words, it is not responsability of a node to
                    // cover nodes of its same level. The node that said Hello shoudl be in charge.
                    if(temp_level > temp_data_int){

                    temp_data_array = temp_data.split("@");
                    try{
                        temp = Integer.parseInt(temp_data_array[0]);        //temp = Number of brothers that the sender node has
                    }catch(Exception ex){ex.printStackTrace();}

                    if(temp_data_array.length > 1){

                        temp_data = temp_data_array[1];
                        temp_data_array = temp_data.split("-");                     // temp_data_array = IDs ald levels of the brothers

                        for(i=0;i<temp;i++){

                            temp_data_array2 = temp_data_array[i].split("#");
                            try{
                                temp2 = Integer.parseInt(temp_data_array2[0]);        //temp = ID from the list of candidates of the sender
                                temp4 = Integer.parseInt(temp_data_array2[1]);        //temp = Level from the list of candidates of the sender
                            }catch(Exception ex){ex.printStackTrace();}


                            if(temp2 != receiver && (temp4 <= temp_level)){
                                temp3++;
                            }
                            
                            //Add the competitor if it have not been added before. Based on this list and the position of the receiver in it,
                            // it will define thw waiting time for the C_BLACK_LIST_REPLY_TIMEOUT once they received the C_SET_FINAL_PARENT.
                            if(getNode(receiver).getChildIndexFromID(temp_tree, temp2) == -1 && (temp4 <= temp_data_int))
                                getNode(receiver).addChild(temp_tree, new candidate(temp2,temp2,temp2));
                            
                        }
                    }
                    }else
                        temp3 = 1000;

                    if(getNode(receiver).isCandidate(temp_tree, sender))
                        getNode(receiver).getCandidateFromID(temp_tree, sender).setMetric(temp3);

                }else
                    temp=1;
                
                }
                
                break;
                
                
            case C_BLACK_LIST_REPLY_TIMEOUT:
                
                //father.simulation_pause();
                
                temp = getNode(receiver).getNumCandidates(temp_tree);
                i=0;
                temp2=0;

                while(i<temp){
                    temp_cand = getNode(receiver).getCandidate(temp_tree,i);
                    metric = temp_cand.getMetric();
                    if(metric>0){
                        i++;
                        if(metric==1.0)
                            temp2++;
                        
                        if(temp_cand.getLevel() > getNode(receiver).getLevel(temp_tree))
                            temp_data = temp_data+temp_cand.getID()+"#";
                        
                    }else
                        break;
                }
                
                    if(i == temp && !getNode(sender).isSink(temp_tree)){
                        
                        //Level of the node that will go to sleep, and te list of nodes that it will leave uncovered
                        temp_data = ""+getNode(receiver).getLevel(temp_tree)+"@"+temp_data;

                        broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, receiver,-1, C_BLACK_UNMARKER, temp_data,temp_tree);
                        getNode(receiver).setState(C_UNMARKED_FINAL,temp_tree);
                        getNode(receiver).setParentState(temp_tree,false);
                        getNode(receiver).setEnergyState(STATE_JUST_RADIO);
                        pushEvent(new event_sim(temp_clock+2*RADIO_OFF_DELAY_PER_TREE+getRandom(PROCESSING_DELAY), temp_clock, sender, sender, DELAYED_RADIO_OFF, "",temp_tree,type));
                        
                    }else
                    {
                    
                        InvalidateAllEventsFromIDFromTimeTOfTypeTy(receiver,temp_clock, type,temp_tree);
                        
                        pushEvent(new event_sim(temp_clock+2*TIMEOUT_DELAY, temp_clock, receiver, receiver, C_BLACK_FINAL_TIMEOUT, "",temp_tree,type));
                        broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, receiver,-1, C_SET_FINAL_PARENT, temp_data,temp_tree);
                        
                        getNode(receiver).setState(C_BLACK_FINAL_TEMP,temp_tree);
                        
                    }
                
                
                break;
                
                
            case DELAYED_RADIO_OFF:
                getNode(sender).setState(sleeping, temp_tree);
                //getNode(sender).setRadioOn(temp_tree, false);
                break;


            case PARENT_RECOGNITION_EVENT_RULEK:
                
                if(getNode(sender).getCounter(temp_tree)>0){
                    getNode(sender).cleanCandidates(temp_tree);
                    temp2 = getNode(sender).getNumChildren(temp_tree);

                    for(i=0;i<temp2;i++){
                        getNode(sender).addCandidate(temp_tree, getNode(sender).getChild(temp_tree, i));
                    }
                }else{
                    if(!getNode(sender).isSink(temp_tree)){
                        getNode(sender).setState(C_UNMARKED_FINAL,temp_tree);
                        getNode(sender).setParentState(temp_tree,false);
                        getNode(sender).setEnergyState(STATE_JUST_RADIO);
                        pushEvent(new event_sim(temp_clock+2*RADIO_OFF_DELAY_PER_TREE+getRandom(PROCESSING_DELAY), temp_clock, sender, sender, DELAYED_RADIO_OFF, "",temp_tree,type));
                    }
                }
                break;
            
            case PARENT_DERECOGNITION:
                if(!getNode(receiver).isSleeping(temp_tree)){
                    if(receiver == destination){
                        getNode(receiver).addCounter(temp_tree, -1);
                    }
                }
                break;
                
            case PARENT_RECOGNITION:

                if(!getNode(receiver).isSleeping(temp_tree)){
                    if(receiver == destination)
                        getNode(receiver).addCounter(temp_tree, 1);
                }
                break;
                
                
                case C_BLACK_FINAL_TIMEOUT:
                        getNode(receiver).setState(C_BLACK_FINAL,temp_tree);
                        getNode(receiver).setParentState(temp_tree,true);
                        getNode(receiver).cleanChildren(temp_tree);
                        
                        pushEvent(new event_sim(temp_clock+5*TIMEOUT_DELAY, temp_clock, sender, sender, PARENT_RECOGNITION_EVENT_RULEK, "",temp_tree,type));
                        
                        temp = getNode(sender).getLevel(temp_tree);
                        getNode(sender).getInfrastructure(temp_tree).cleanNeighbors();
                        temp2 = getNode(sender).getNumCandidates(temp_tree);
                        
                        for(i=0;i<temp2;i++){
                            temp_cand = getNode(sender).getCandidate(temp_tree, i);
                            if(temp_cand.getLevel() >=temp){
                                getNode(sender).addChild(temp_tree, temp_cand);
                            }
                        }
                        
                        //Initiate the TM protocol only on the active nodes of the topology!!
                        if(TM_Selected && (father.getTMType() != TM_ENERGY || getNode(sender).isSink(temp_tree))){
                            pushEvent(new event_sim(temp_clock+DELTA_TIME, temp_clock, receiver, receiver, INIT_EVENT, "",temp_tree,TM_PROTOCOL));
                            //pushEvent(new event_sim(temp_clock+DELTA_TIME, temp_clock, receiver, receiver, INIT_EVENT, "",getNode(receiver).getActiveTree(),TM_PROTOCOL));
                        }

                        //Initiate the sensor querying protocol only on the active nodes of the topology!!
                        if(SD_Selected  && getNode(receiver).getActiveTree() == temp_tree){
                            pushEvent(new event_sim(temp_clock+DELTA_TIME, temp_clock, receiver, receiver, INIT_EVENT, "",temp_tree,SENSOR_PROTOCOL));
                        }

                        //Initiate the COMM  protocol only on the active nodes of the topology!!
                        if(COMM_Selected){
                            pushEvent(new event_sim(temp_clock+DELTA_TIME, temp_clock, receiver, receiver, INIT_EVENT, "",temp_tree,COMM_PROTOCOL));
                        }
                        
                        
                    break;
                    
                    
            case C_BLACK_UNMARKER:

                if(!getNode(receiver).isSleeping(temp_tree)){
                     //Level of the node that stoped covering the receiver
                    try{
                        temp_data_array = temp_data.split("@");
                        temp_level = Integer.parseInt(temp_data_array[0]);
                    }catch(Exception ex){ex.printStackTrace();}


                    if(temp_level < getNode(receiver).getLevel(temp_tree)){
                        broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, sender, -1, receiver,-1, C_BLACK_LIST_REPLY_UPDATE, temp_data,temp_tree);

                        temp = getNode(receiver).getChildIndexFromID(temp_tree, sender);
                        if(temp != -1)
                            getNode(receiver).getInfrastructure(temp_tree).RemoveNeighborFromIndex(temp);

                    }
                }
                break;
        
                
            case C_BLACK_LIST_REPLY_UPDATE:

                if(!getNode(receiver).isSleeping(temp_tree)){
                    //if(getNode(receiver).getState(temp_tree) != C_UNMARKED){

                     //Level of the node that stoped covering the receiver
                    try{
                        temp_data_array = temp_data.split("@");
                        temp_level = Integer.parseInt(temp_data_array[0]);
                    }catch(Exception ex){ex.printStackTrace();}

                    if(getNode(receiver).isCandidate(temp_tree, sender) && temp_level <= getNode(receiver).getLevel(temp_tree)){

                        temp_data_double = getNode(receiver).getCandidateFromID(temp_tree, sender).getMetric();
                        getNode(receiver).getCandidateFromID(temp_tree, sender).setMetric(temp_data_double-1);

                        temp = getNode(receiver).getChildIndexFromID(temp_tree, source);
                        if(temp != -1)
                            getNode(receiver).getInfrastructure(temp_tree).RemoveNeighborFromIndex(temp);

                        //if(getNode(receiver).getState(temp_tree) == C_BLACK)
                            if(temp_data_double == 0){

                                getNode(receiver).setState(C_BLACK,temp_tree);

                                InvalidateAllEventsFromIDFromTimeTOfCodeC(receiver,temp_clock,C_BLACK_LIST_REPLY_TIMEOUT,temp_tree);

                                getNode(receiver).SortChildren(temp_tree);

                                temp_data_double2 = getNode(receiver).getChildIndexFromID(temp_tree, receiver);

                                temp_data_double = temp_data_double2 + getRandom(TIMEOUT_DELAY);

                                //temp_data_double = getRandom(15*getNode(receiver).getLevel(temp_tree));

                                temp_data_double2 = temp_clock+(temp_data_double);
                                pushEvent(new event_sim(temp_data_double2, temp_clock, receiver, receiver, C_BLACK_LIST_REPLY_TIMEOUT, "",temp_tree,type));

                            }
                        //}

                    }
                }
                //}
                break;
                
            case C_SET_FINAL_PARENT:
                
                if(getNode(receiver).isCandidate(temp_tree, sender) && !getNode(receiver).isSleeping(temp_tree)){
                    
                    if(getNode(receiver).getState(temp_tree) == C_BLACK){
                        InvalidateAllEventsFromIDFromTimeTOfCodeC(receiver,temp_clock,C_BLACK_LIST_REPLY_TIMEOUT,temp_tree);
                        //temp_data_double = (double)receiver*0.5;
                        
                        getNode(receiver).SortChildren(temp_tree);
                        
                        temp_data_double2 = getNode(receiver).getChildIndexFromID(temp_tree, receiver);
                        
                        //if(temp_data_double2 == -1)
                          //  temp_data_double2 = (double)receiver*0.5;

                        temp_data_double = temp_data_double2 + getRandom(TIMEOUT_DELAY);
                        
                        //temp_data_double = getRandom(15*getNode(receiver).getLevel(temp_tree));
                        
                        temp_data_double2 = temp_clock+(temp_data_double);
                        pushEvent(new event_sim(temp_data_double2, temp_clock, receiver, receiver, C_BLACK_LIST_REPLY_TIMEOUT, "",temp_tree,type));                 
                    }
                    
                    temp2=getNode(receiver).getCandidateFromID(temp_tree, sender).getLevel();

                    temp3 = getNode(receiver).getDefaultGateway(temp_tree);
                    temp = 1000;
                    
                    if( temp3 != -1 && getNode(receiver).isCandidate(temp_tree, temp3))
                        temp = getNode(receiver).getCandidateFromID(temp_tree, temp3).getLevel();

                    //getNode(receiver).setDefaultGateway(temp_tree,sender);
                    getNode(receiver).addGateway(temp_tree, sender,sender);

                    //If the level of the new black final is better than my current gateway, then change it
                    if(temp > temp2 && !getNode(receiver).isSink(temp_tree)){
                        if(temp3 != -1)
                            broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, receiver,temp3, PARENT_DERECOGNITION, "",temp_tree);
                        getNode(receiver).setDefaultGateway(temp_tree,sender);
                        broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, receiver,sender, PARENT_RECOGNITION, "",temp_tree);
                    }
                }
                break;
                
        }
        
        
        
      }
}
