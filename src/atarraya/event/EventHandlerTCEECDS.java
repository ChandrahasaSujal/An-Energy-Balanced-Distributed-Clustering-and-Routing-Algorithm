/*
 * EventHandlerEECDS.java
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
public class EventHandlerTCEECDS implements EventHandlerSW,constants{
    
    int type= TC_PROTOCOL;
    
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
    
    atarraya_frame father;
    
    /** Creates a new instance of EventHandlerCDS */
    public EventHandlerTCEECDS(atarraya_frame _frame)
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
        father.InvalidateAllEventsFromIDFromTimeTOfTypeTyOfCodeC(id, t, Ty, c, tree);
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
            /*getNode(i).setState(WHITE,_treeID);
            getNode(i).defineLabels(WHITE, BLUE, GREY);*/
            
            getNode(i).setState(initial,_treeID);
            getNode(i).defineLabels(initial,active, inactive, sleeping);
            getNode(i).SetInfrastructureStarted(_treeID,true);
            
            if(TM_Selected){
            //stop all future event from the TM protocol
                pushEvent(new event_sim(_clock+getRandom(PROCESSING_DELAY), _clock, i, i, RESET_TM_PROTOCOL, "",_treeID,TM_PROTOCOL));
            }

            if(SD_Selected){
                //stop all future event from the sensor-data protocol
                pushEvent(new event_sim(_clock+getRandom(PROCESSING_DELAY), _clock, i, i, RESET_QUERY_SENSOR, "",_treeID,SENSOR_PROTOCOL));
            }
            
        }
    }
    
    public void init_node(int _treeID, int id_node){
        getNode(id_node).setState(initial,_treeID);
    }
    
    public void initial_event(int _id, int _treeID){
        getNode(_id).setState(BLACK,_treeID);
        double _clock = father.getVariable(CLOCK);
        broadcast(_clock+getRandom(MAX_TX_DELAY_RANDOM), _clock, _id, -1, BLACKMESSAGE, ""+_id,_treeID);
        pushEvent(new event_sim(_clock+PHASE2_TIMEOUT_DELAY, _clock, _id, _id, PHASE2_TIMEOUT, "",_treeID,type));
        
          if(TM_Selected){
            //stop all future event from the TM protocol
            pushEvent(new event_sim(_clock+getRandom(PROCESSING_DELAY), _clock, _id, _id, RESET_TM_PROTOCOL, "",_treeID,TM_PROTOCOL));
        }
        
        if(SD_Selected){
            //stop all future event from the sensor-data protocol
            pushEvent(new event_sim(_clock+getRandom(PROCESSING_DELAY), _clock, _id, _id, RESET_QUERY_SENSOR, "",_treeID,SENSOR_PROTOCOL));
        }
        
    }
    
    
    public boolean CheckIfDesiredFinalState(int s){
        //if(s==BLUE || s==GREY)
        if(s==active || s==sleeping)
            return true;
        
        
        return false;
    }
    
    public boolean CheckIfDesiredSleepingState(int s){
        //if(s==GREY_FINAL)
        if(s==sleeping)
            return true;
        
        return false;
    }
    
    public boolean verifIfNodeInCDS(int _id, int _treeID){
    
        if(getNode(_id).getState(_treeID)==BLACK || getNode(_id).getState(_treeID)==BLUE ||getNode(_id).isSink(_treeID) || _treeID==-1)
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
        double temp_val_double3=0;
        String[] temp_data_array = new String[50];
        String[] temp_data_array2;
        String data, data1 = "TRANSITION", data2 = "WHITE";
        
                
        int numneighb,i,x,temp_ID,j;
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
        double weight;
        double temp_weight;
        boolean sw;
        boolean sw2;
        boolean sw3 = true;
        candidate temp_cand;
        int state;

        
        switch(code){
        case INIT_NODE:
                init_node(temp_tree, receiver);
                sw3=false;
                break;
            
            case INIT_EVENT:
                initial_event(receiver,temp_tree);
                sw3 = false;
                break;
           
        }
        
        if(sw3){
        state = getNode(receiver).getState(temp_tree);

            switch(state){

                case BLUE:
                    
                    switch(code){
                    case BLUEMESSAGE:
                        temp_cand = getNode(receiver).getCandidateFromID(temp_tree, sender);
                        if(temp_cand != null){
                            getNode(receiver).addChild(temp_tree, temp_cand);
                            getNode(receiver).addGateway(temp_tree, sender, sender);
                        }
                    break;
                    
                    case PARENT_RECOGNITION_EVENT_EECDS:

                        getNode(sender).cleanCandidates(temp_tree);
                        temp = getNode(sender).getNumChildren(temp_tree);

                        for(i=0;i<temp;i++){
                            getNode(sender).addCandidate(temp_tree, getNode(sender).getChild(temp_tree, i));
                        }

                        break;
                    
                    case DELAYED_RADIO_OFF:
                            // Do nothing
                            break;
                    }
                        
                    break;

                case BLACK:

                    switch(code){
                        // Initiate the Phase 2 - this event is only generated for the sinks or initiators        
                        case PHASE2_TIMEOUT:
                            //father.simulation_pause();
                            pushEvent(new event_sim(temp_clock+PROCESSING_DELAY, temp_clock, sender, sender, BLUEMESSAGE, temp_data,temp_tree,type));
                            break;

                        case GREYMESSAGE:
                            if(receiver == destination)
                                getNode(receiver).addChild(temp_tree, new candidate(sender, sender, 0, getSortingMode()));
                            break;
                            
                        case BLUEMESSAGE:
                            
                            if(getNode(receiver).getNumChildren(temp_tree)>0){
                                
                            
                            getNode(receiver).setState(BLUE,temp_tree);
                            //getNode(receiver).setParentState(temp_tree,true);
                            getNode(receiver).addGateway(temp_tree, sender, sender);
                            if(getNode(receiver).getDefaultGateway(temp_tree) == -1)
                                getNode(receiver).setDefaultGateway(temp_tree, sender);
                            InvalidateAllEventsFromIDFromTimeTOfTypeTy(receiver, temp_clock, type,temp_tree);
                            broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, receiver, -1, BLUEMESSAGE, temp_data,temp_tree);
                            pushEvent(new event_sim(temp_clock+3*TIMEOUT_DELAY, temp_clock, receiver,receiver, PARENT_RECOGNITION_EVENT_EECDS, "",temp_tree,type));
                            
                            //if(SD_Selected)
                            //    pushEvent(new event_sim(temp_clock+TIMEOUT_FOR_FIRST_QUERY_SENSOR, temp_clock, receiver, receiver, QUERY_SENSOR, "",temp_tree,SENSOR_PROTOCOL));

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
                            
                            }else{
                                getNode(receiver).setState(GREY_FINAL, temp_tree);
                                getNode(receiver).addGateway(temp_tree, sender, sender);
                                getNode(receiver).setDefaultGateway(temp_tree, sender);
                                InvalidateAllEventsFromIDFromTimeTOfTypeTy(receiver, temp_clock, type,temp_tree);
                                getNode(receiver).setEnergyState(STATE_JUST_RADIO);
                            }
                            break;

                        case PREINQUIRYMESSAGE2:
                            broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, receiver, sender, PREINQUIRYREPLY2, ""+getNode(receiver).getState(temp_tree),temp_tree);
                            break;

                        case UPDATEMESSAGE:
                            if(receiver == 45)
                                temp=1;
                            getNode(receiver).cleanCandidates(temp_tree);
                            InvalidateAllEventsFromIDFromTimeTOfTypeTy(receiver, temp_clock, type,temp_tree);
                            getNode(receiver).setState(BTRANS,temp_tree);
                            temp_weight = 1;

                                try{
                                    temp_weight = Double.parseDouble(temp_data);
                                }catch(Exception ex){ex.printStackTrace();}

                            getNode(receiver).addCandidate(temp_tree, new candidate(sender, sender, temp_weight, getSortingMode()));
                            pushEvent(new event_sim(temp_clock+TIMEOUT_DELAY, temp_clock, receiver,receiver, TIMEOUT, "",temp_tree,type));
                            break;

                    }

                    break; //End state BLACK


                case GREY:

                    switch(code){

                        case BLUEMESSAGE:
                            getNode(receiver).cleanCandidates(temp_tree);
                            getNode(receiver).setState(GTRANS,temp_tree);
                            broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, receiver, -1, PREINQUIRYMESSAGE2, "",temp_tree);
                            pushEvent(new event_sim(temp_clock+TIMEOUT_DELAY_SHORT, temp_clock, receiver,receiver, PREINQUIRY_TIMEOUT2, "",temp_tree,type));
                            InvalidateAllEventsFromIDFromTimeTOfTypeTyOfCodeC(receiver, temp_clock,type,DELAYED_RADIO_OFF,temp_tree);
                            break;


                        case INVITEMESSAGE:
                            if(receiver  == destination){
                                getNode(receiver).setState(BLUE,temp_tree);
                                getNode(receiver).setParentState(temp_tree,true);
                                InvalidateAllEventsFromIDFromTimeTOfTypeTy(receiver, temp_clock, type,temp_tree);
                                broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, receiver, -1, BLUEMESSAGE, temp_data,temp_tree);
                                //if(SD_Selected)
                                //    pushEvent(new event_sim(temp_clock+TIMEOUT_FOR_FIRST_QUERY_SENSOR, temp_clock, receiver, receiver, QUERY_SENSOR, "",temp_tree,SENSOR_PROTOCOL));

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

                            }
                            break;

                        case DELAYED_RADIO_OFF:
                            //getNode(sender).setRadioOn(temp_tree, false);

                            getNode(sender).setEnergyState(STATE_JUST_RADIO);

                            getNode(sender).setState(GREY_FINAL, temp_tree);
                            
                            
                            
                            break;
                            
                    }
                    break; //End state GREY


                case GTRANS:

                    switch(code){

                        case BLUEMESSAGE:
    //                        getNode(receiver).setState(GREY,temp_tree);
                            InvalidateAllEventsFromIDFromTimeTOfTypeTyOfCodeC(receiver, temp_clock, type, GOBACKTOGREY, temp_tree);
                            pushEvent(new event_sim(temp_clock+TIMEOUT_DELAY, temp_clock, receiver,receiver, GOBACKTOGREY, "",temp_tree,type));
                            break;

                        case INVITEMESSAGE:
                            if(receiver  == destination){
                                getNode(receiver).setState(BLUE,temp_tree);
                                getNode(receiver).setParentState(temp_tree,true);
                                InvalidateAllEventsFromIDFromTimeTOfTypeTy(receiver, temp_clock, type,temp_tree);
                                broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, receiver, -1, BLUEMESSAGE, temp_data,temp_tree);
                                //if(SD_Selected)
                                //    pushEvent(new event_sim(temp_clock+TIMEOUT_FOR_FIRST_QUERY_SENSOR, temp_clock, receiver, receiver, QUERY_SENSOR, "",temp_tree,SENSOR_PROTOCOL));

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

                            }
                            break;

                        case PREINQUIRYREPLY2:
                            if(receiver == destination){
                                temp = BLACK;
                                try{
                                    temp = Integer.parseInt(temp_data);
                                }catch(Exception ex){ex.printStackTrace();}

                                getNode(receiver).addCandidate(temp_tree, new candidate(sender, sender, 0, getSortingMode(), temp));
                            }
                            break;

                        case PREINQUIRY_TIMEOUT2:
                            temp = getNode(sender).getNumCandidates(temp_tree);
                            temp2=0;
                            //Counting the number of black neighbors
                            for(i=0;i<temp;i++){
                                if(getNode(sender).getCandidate(temp_tree, i).getState() == BLACK)
                                    temp2++;
                            }
                            
                            temp_val_double3 = java.lang.Math.sqrt(temp2)*getNode(sender).getEnergy();
                            
                            //In order to reduce the metric when the node have been selected by multiple trees.
                                temp_data_int2 = getNode(receiver).getNumTrees();
                                if(temp_data_int2>0)
                                    temp_val_double3 = temp_val_double3*java.lang.Math.pow(0.5, temp_data_int2);
                                
                            
                            //Sending the update message for all black neighbors on range
                            broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, sender, -1, UPDATEMESSAGE, ""+temp_val_double3,temp_tree);
                            pushEvent(new event_sim(temp_clock+(3*TIMEOUT_DELAY), temp_clock, sender,sender, GOBACKTOGREY, "",temp_tree,type));                            
                            
                            if(temp==0){
                                pushEvent(new event_sim(temp_clock+TIMEOUT_DELAY, temp_clock, sender,sender, GOBACKTOGREY, "",temp_tree,type));
                                //getNode(sender).setState(GREY,temp_tree);    
                            }

                            break;

                        case GOBACKTOGREY:
                                //All neighbors are some other color than black, so it might be interesting for the node to go back to grey
                                getNode(sender).setState(GREY,temp_tree);

                                getNode(sender).setEnergyState(STATE_JUST_RADIO);

                                //pushEvent(new event_sim(temp_clock+3*TIMEOUT_DELAY, temp_clock, sender,sender,DELAYED_RADIO_OFF, "",temp_tree,type));
                                pushEvent(new event_sim(temp_clock+(RADIO_OFF_DELAY_PER_TREE*(temp_num_structures))+getRandom(PROCESSING_DELAY), temp_clock, sender, sender, DELAYED_RADIO_OFF, "",temp_tree,type));
                                
                            break;

                        case DELAYED_RADIO_OFF:
                            // Do nothing
                            break;
                            
                    }

                    break; // End state GTRANS


                case BTRANS:
                    switch(code){

                        case BLUEMESSAGE:
                            getNode(receiver).setState(BLUE,temp_tree);
                            //getNode(receiver).setParentState(temp_tree,true);
                            getNode(receiver).addGateway(temp_tree, sender, sender);
                            getNode(receiver).setDefaultGateway(temp_tree, sender);
                            InvalidateAllEventsFromIDFromTimeTOfTypeTy(receiver, temp_clock, type,temp_tree);
                            broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, receiver, -1, BLUEMESSAGE, temp_data,temp_tree);
                            //if(SD_Selected)
                            //    pushEvent(new event_sim(temp_clock+TIMEOUT_FOR_FIRST_QUERY_SENSOR, temp_clock, receiver, receiver, QUERY_SENSOR, "",temp_tree,SENSOR_PROTOCOL));

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

                        case UPDATEMESSAGE:
                            temp_weight = 1.0;
                                try{
                                    temp_weight = Double.parseDouble(temp_data);
                                }catch(Exception ex){ex.printStackTrace();}

                            getNode(receiver).addCandidate(temp_tree, new candidate(sender, sender, temp_weight, getSortingMode()));
                            break;

                        case TIMEOUT:

                            temp = getNode(sender).getNumCandidates(temp_tree);

                            if(temp>0){
                                getNode(sender).SortCandidates(temp_tree);
                                temp2 = getNode(sender).getCandidate(temp_tree, 0).getID();
                                broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, sender, temp2, INVITEMESSAGE, "",temp_tree);
                            }

                            getNode(sender).setState(BLACK,temp_tree);

                            break;

                    }
                    break; // End state BTRANS


                case WHITE:

                    switch(code)
                    {
                        // This event is the when a black node announces itself to its neighborhood
                        case BLACKMESSAGE:
                            temp=0;
                            try{
                                temp = Integer.parseInt(temp_data);
                            }catch(Exception ex){ex.printStackTrace();}         
                            getNode(receiver).setSinkAddress(temp_tree, temp);
                            
                            getNode(receiver).setState(GREY,temp_tree);
                            getNode(receiver).addGateway(temp_tree, sender, sender);
                            getNode(receiver).setDefaultGateway(temp_tree, sender);
                            broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, receiver, sender, GREYMESSAGE, ""+temp,temp_tree);
                            break;

                        // This event is the when a gray node announces itself to its neighborhood
                        case GREYMESSAGE:
                            temp=0;
                            try{
                                temp = Integer.parseInt(temp_data);
                            }catch(Exception ex){ex.printStackTrace();}         
                            getNode(receiver).setSinkAddress(temp_tree, temp);
                            
                            getNode(receiver).cleanCandidates(temp_tree);
                            getNode(receiver).setState(TRANSITION,temp_tree);
                            broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, receiver, -1, PREINQUIRYMESSAGE, temp_data,temp_tree);
                            pushEvent(new event_sim(temp_clock+TIMEOUT_DELAY_SHORT, temp_clock, receiver, receiver, PREINQUIRY_TIMEOUT, temp_data,temp_tree,type));
                            break;

                             //The nodes will respond to this HELLO message by including its state.
                         case PREINQUIRYMESSAGE:
                             broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, receiver, sender, PREINQUIRYREPLY, ""+getNode(receiver).getState(temp_tree),temp_tree);
                            break;  

                    }

                    break; //End state WHITE

                case TRANSITION:

                     switch(code)
                    {
                        case BLACKMESSAGE:
                            getNode(receiver).setState(GREY,temp_tree);
                            getNode(receiver).addGateway(temp_tree, sender, sender);
                            getNode(receiver).setDefaultGateway(temp_tree, sender);
                            InvalidateAllEventsFromIDFromTimeTOfTypeTy(receiver, temp_clock, type,temp_tree);
                            broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, receiver, sender, GREYMESSAGE, temp_data,temp_tree);
                            break;

                        case GREYMESSAGE:
                            break;


                        case INQUIRYMESSAGE:
                            if(getNode(receiver).getMetric(temp_tree) != 0){
                                
                                temp_val_double3 = getNode(receiver).getMetric(temp_tree);
                                
                                //In order to reduce the metric when the node have been selected by multiple trees.
                                temp_data_int2 = getNode(receiver).getNumTrees();
                                if(temp_data_int2>0)
                                    temp_val_double3 = temp_val_double3*java.lang.Math.pow(0.5, temp_data_int2);
                                
                                //temp_data = ""+getNode(receiver).getState(temp_tree)+"#"+getNode(receiver).getMetric(temp_tree);
                                temp_data = ""+getNode(receiver).getState(temp_tree)+"#"+temp_val_double3;
                                broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, receiver, sender, INQUIRYREPLY, temp_data,temp_tree);
                            }else{
                                pushEvent(new event_sim(temp_clock+getRandom(TIMEOUT_DELAY_SHORT*0.5), temp_clock, sender, receiver, INQUIRYMESSAGE, temp_data,temp_tree,type));
                            }
                            break;


                            //I must check first who of my neighbors are white or transition
                         case PREINQUIRYMESSAGE:
                            broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, receiver, sender, PREINQUIRYREPLY, ""+getNode(receiver).getState(temp_tree),temp_tree);
                            break;


                        case PREINQUIRYREPLY:
                                if(receiver == destination){

                                    try{
                                        temp = Integer.parseInt(temp_data);
                                    }catch(Exception ex){ex.printStackTrace();}

                                    getNode(receiver).addCandidate(temp_tree, new candidate(sender,sender,0,getSortingMode(),temp));
                                }
                            break;


                         case PREINQUIRY_TIMEOUT:

                            temp = getNode(sender).getNumCandidates(temp_tree);
                            temp2=0;

                            //if(temp > 0){
                                for(i=0;i<temp;i++){
                                    if(getNode(sender).getCandidate(temp_tree, i).getState() == WHITE || getNode(sender).getCandidate(temp_tree, i).getState() == TRANSITION){
                                        temp2++;
                                    }
                                }

                                metric = java.lang.Math.sqrt(temp2)*getNode(sender).getEnergy();
                                
                                //In order to reduce the metric when the node have been selected by multiple trees.
                                temp_data_int2 = getNode(receiver).getNumTrees();
                                if(temp_data_int2>0)
                                    metric = metric*java.lang.Math.pow(0.5, temp_data_int2);
                                
                                
                                temp_data = ""+getNode(sender).getState(temp_tree)+"#"+metric;
                                getNode(sender).setMetric(metric, temp_tree);
                                broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, sender, -1, INQUIRYMESSAGE, temp_data,temp_tree);
                                pushEvent(new event_sim(temp_clock+TIMEOUT_DELAY, temp_clock, sender, sender, INQUIRY_TIMEOUT, "",temp_tree,type));
                            //} else
                              //  getNode(sender).setState(GREY, temp_tree);
                            break;


                            //Inquiry reply
                        case INQUIRYREPLY:
                            if(receiver == destination){

                                temp_data_array = temp_data.split("#");

                                //state of the node
                                temp = Integer.parseInt(temp_data_array[0]);
                                //weight onf the node
                                weight = Double.parseDouble(temp_data_array[1]);

                                temp2 = getNode(receiver).getCandidateIndexFromID(temp_tree, sender);

                                if(temp2 != -1){
                                    getNode(receiver).getCandidate(temp_tree, temp2).setMetric(weight);
                                }

                            }
                            break;

                        case INQUIRY_TIMEOUT:

                            getNode(sender).addCandidate(temp_tree, new candidate(sender,sender,getNode(sender).getMetric(temp_tree)));
                            getNode(sender).SortCandidates(temp_tree);

                            temp = getNode(sender).getSinkAddress(temp_tree);

                            //if(getNode(sender).getCandidate(temp_tree, 0).getID() == sender && getNode(sender).getNumCandidates(temp_tree)>1)
                            if(getNode(sender).getCandidate(temp_tree, 0).getID() == sender)
                            {
                                getNode(receiver).setState(BLACK,temp_tree);
                                getNode(receiver).addNumTree(1);
                                broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, sender, -1, BLACKMESSAGE, ""+temp,temp_tree);
                            }
                            else
                            {
                                getNode(sender).setState(WHITE,temp_tree);
                                //pushEvent(new event_sim(temp_clock+TIMEOUT_DELAY, temp_clock, sender, sender, GREYMESSAGE, ""+temp,temp_tree,type));
                            }
                            break;

                    }

                    break; // End state TRANSITION

          }
    }
}
    
}