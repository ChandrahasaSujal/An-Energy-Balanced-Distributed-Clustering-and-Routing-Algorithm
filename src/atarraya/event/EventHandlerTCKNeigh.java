/*
 * EventHandlerKNeigh.java
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
public class EventHandlerTCKNeigh implements EventHandlerSW,constants{

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
    int k_value;
    int k_type;


    /** Creates a new instance of EventHandlerCDS */
    public EventHandlerTCKNeigh(atarraya_frame _frame)
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

        k_value = (int)father.getVariable(KNEIGH_K);

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
            pushEvent(new event_sim(_clock+(10*TIMEOUT_DELAY_SHORT), _clock, i, i, TIMEOUT_RECOGNITION,"",_treeID,type));

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
        if(s==active)
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
        double tempdist;
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
                    pushEvent(new event_sim(temp_clock+TIMEOUT_DELAY, temp_clock, sender, sender, TIMEOUT_RECOGNITION,"",temp_tree,type));
                break;

            //The node receives a hello message
            case HELLO:

                temp=0;

                 temp_data_array = temp_data.split("#");
                try{
                    temp = Integer.parseInt(temp_data_array[0]);        //temp = Level of the node it received the Hello message
                    temp2 = Integer.parseInt(temp_data_array[1]);        //temp2 = sink's address'
                }catch(Exception ex){ex.printStackTrace();}


                 //Get the distance from the sender node
                tempdist = getNode(receiver).getNeighborDistance(getNode(receiver).getNeighborIndex(sender));
                //Add the sender node as a candidate and include the distance between the nodes as the metric of this candidate
                getNode(receiver).addCandidate(temp_tree,new candidate(sender,sender,tempdist,METRIC1_ONLY_INV,KN_VISITED,temp));


                if(getNode(receiver).getState(temp_tree) == initial){
                    pushEvent(new event_sim(temp_clock+PROCESSING_DELAY, temp_clock, receiver, receiver, SEND_HELLO, "",temp_tree,type));
                    getNode(receiver).setState(KN_VISITED,temp_tree);
                    getNode(receiver).setLevel(temp+1, temp_tree);
                    //getNode(receiver).setDefaultGateway(temp_tree, sender);
                    getNode(receiver).setSinkAddress(temp_tree, temp2);
                    InvalidateAllEventsFromIDFromTimeTOfTypeTyOfCodeC(receiver, temp_clock, type, TIMEOUT_RECOGNITION, temp_tree);
                }
                break;

                //When the Hello timeout expires
            case TIMEOUT_RECOGNITION:

                temptam = getNode(sender).getNumCandidates(temp_tree);
                temp2=Math.min(temptam,k_value);
                temp_data = "";
                sw=true;

                if(temp2>0){

                    //Sort candidates based on the distance
                    getNode(sender).SortCandidates(temp_tree);


                    i=0;

                    while(i<temp2){

                        //Get the i th candidate
                        temp3 = getNode(sender).getCandidate(temp_tree,i).getID();

                        getNode(sender).addGateway(temp_tree, temp3,temp3);
                        AddTreeLine(sender,temp3,temp_tree);

                        if(getNode(sender).getCandidate(temp_tree,i).getLevel() < getNode(sender).getLevel(temp_tree)){
                            getNode(sender).setDefaultGateway(temp_tree, temp3);
                            sw=false;
                        }

                        i++;
                    }

                            
                    //Get the distance to the k th node or the parent node
                    temp_weight = getNode(sender).getCandidate(temp_tree, i-1).getMetric();

                    //Assign the greatest distance
                    getNode(sender).setRadius(temp_tree, Math.ceil(temp_weight));

                    father.BuildNeighborhood(sender);
                }

                getNode(sender).setState(KN_UPDATED,temp_tree);

                getNode(sender).setParentState(temp_tree, true);

                //Initiate the TM protocol only on the active nodes of the topology, except on the local
                if(TM_Selected && (father.getTMType() != TM_ENERGY || getNode(sender).isSink(temp_tree))){
                    pushEvent(new event_sim(temp_clock+DELTA_TIME, temp_clock, receiver, receiver, INIT_EVENT, "",temp_tree,TM_PROTOCOL));
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





        }



      }
}
