

package atarraya.event;

import atarraya.constants;
import atarraya.element.candidate;
import atarraya.element.node;
import atarraya.element.register;
import atarraya.atarraya_frame;
import java.util.Random;

/**
 *
 * @author Pedro
 */


public class EventHandlerMotionSimple implements EventHandlerSW,constants{
    
    atarraya_frame father;
    Random generator;

    int type = SENSOR_PROTOCOL;
    
    int TMType;
    
    int temp_address=0;

    int sleep_time;
    int step_size;
            
    /** Creates a new instance of EventHandlerA3 */
    public EventHandlerMotionSimple(atarraya_frame _father) {
        
        father = _father;  
        TMType = NO_TM;
        generator = new Random();
        sleep_time = (int)father.getVariable(PM_SLEEP);
        step_size = (int)father.getVariable(PM_STEP);

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
        //Start the protocol just if the nodes is able to move
        for(i=0;i<father.getVariable(NUMPOINTS);i++){
            if(getNode(i).isMoving())
                pushEvent(new event_sim(sleep_time, sleep_time+getRandom(DELTA_TIME), i,i, i,i, -1, MOVE, ""+step_size+"@"+sleep_time,0,MOTION_PROTOCOL,0));
        }
    }
    
    public void initial_event(int _id, int _treeID){

        //Have not thought about somthing to put in here.

        //Obtain the time from the simulator
        //double _clock = father.getVariable(CLOCK);
        
        //stop all future event from the sensor-data protocol
     //   InvalidateAllEventsFromIDFromTimeTOfTypeTy(_id, _clock, type,_treeID);
        
        //Program a new Query Sensor on the tree_ID received
       // pushEvent(new event_sim(_clock+TIMEOUT_FOR_FIRST_QUERY_SENSOR, _clock, _id, _id, QUERY_SENSOR,"",_treeID,type));
        
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
        
        if(code==SON_RECOGNITION || code==DATA)
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

        double temp_data_double=0;
        double temp_data_double2=0;

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

        int numnodes=0;
        int dir=0;
        int x_=0;
        int y_=0;
        int tx=0;
        int ty=0;
        int step;

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
            
            case MOVE:

                //Decompress the data in the message
                    temp_data_array = temp_data.split("@");

                    
                try{
                        temp_data_int = Integer.parseInt(temp_data_array[0]);  //The maximum step
                         temp_data_int2 = Integer.parseInt(temp_data_array[1]); //The sink address of this tree
//                         temp_data_int3 = Integer.parseInt(temp_data_array[2]); //The address of this father
                    }catch(Exception ex){ex.printStackTrace();}


                dir = generator.nextInt(4);
                step = generator.nextInt(temp_data_int);

                switch(dir){

                    //left
                    case 0:
                        x_=-step;
                        y_=0;
                        break;

                        //right
                    case 1:
                        x_=step;
                        y_=0;
                        break;

                        //up
                    case 2:
                        x_=0;
                        y_=-step;
                        break;

                        //down
                    case 3:
                        x_=0;
                        y_=step;
                        break;

                }

                tx = getNode(sender).getPosition().x;
                ty = getNode(sender).getPosition().y;

                getNode(sender).setPos(tx+x_, ty+y_);

             //   father.BuildNeighborhood(sender);

                father.repaint();

                pushEvent(new event_sim(temp_clock+temp_data_int2+getRandom(DELTA_TIME), temp_clock, sender,sender, sender,sender, -1, MOVE, temp_data,0,MOTION_PROTOCOL,0));

                break;
              
                
                
            default:
                break;
        }
        }
        
    }
}
