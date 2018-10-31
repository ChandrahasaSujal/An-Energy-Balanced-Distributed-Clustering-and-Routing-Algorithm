

package atarraya.event;

import java.lang.Comparable;

/**
 *
 * @author pedrow
 */
public class event_sim implements Comparable{

    int ids[];
    
    int hop_count;
    double weight;

    int type;                          //Type of event, to be sent to the correct event_handler
    double time;                   //Time at which the event will be executed
    double time_ini;             //Time at which the event was generated
    
    int source;                      //Initial source of the message
    int destination;              //Intermediate destination of the message
    int sender;                     //Last sender of the message
    int final_destination;     //Final receiver of the message
    int receiver;                   //Receiver node due to the broadcast of the message
    int code;                        //Code of the event
    String data;                   // General data that the event/message might carry
    int tree;                         //Structure ID in which the event is valid
    boolean validity;            //Defines if the event is still valid for execution on the queue
    int sequence_number;
    int message_type; //Defines if the event is a message
    
    //temporary variables
    
    double td;
    String st;

    public int[] getIds()
    {
        return ids;
    }
    
    /** Creates a new instance of even */
    public event_sim() {
        type = -1;
        source = -1;
        sender = -1;
        destination = -1;
        receiver = -1;
        final_destination = -1;
        code = -1;
        time = 0.0;
        time_ini = 0.0;
        data = "";
        tree=-1;
        validity = true;
        sequence_number = 0;
        message_type = 0;
    }

    public int getHopCount()
    {
        return hop_count;
    }

     public double getWeight()
    {
        return weight;
    }

     public event_sim( int _destination,int _sender, int _receiver, int _code,double _time){
        this();
        time = _time;
        destination=_destination;
        sender = _sender;
        receiver = _receiver;
        code = _code;
    }

    public event_sim(int hop_cnt,double _time,int _sender, int _receiver, int _destination, int _code){
        this();
        hop_count=hop_cnt;
        time = _time;
        destination=_destination;
        sender = _sender;
        receiver = _receiver;
        code = _code;
    }

      public event_sim(double _time,int _sender, int _receiver, int _code,double _weight){
        this();
        weight=_weight;
        time = _time;
        sender = _sender;
        receiver = _receiver;
        code = _code;
    }

       public event_sim(int[] _ids,double _time,int _sender, int _receiver, int _code){
        this();
        ids=_ids;
        time = _time;
        sender = _sender;
        receiver = _receiver;
        code = _code;
    }

    public event_sim(double _time, double _time_ini, int _sender, int _receiver, int _code){
        this();
        time = _time;
        sender = _sender;
        receiver = _receiver;
        code = _code;
        time_ini = _time_ini;
    }
    
    public event_sim(double _time, double _time_ini, int _sender, int _receiver, int _code, String _data){
        this();
        time = _time;
        sender = _sender;
        receiver = _receiver;
        code = _code;
        time_ini = _time_ini;
        data = _data;
    }
    
    //This constructor includes the tree ID in the message
    public event_sim(double _time, double _time_ini, int _sender, int _receiver, int _code, String _data, int _tree, int _type){
        this();
        type=_type;
        time = _time;
        sender = _sender;
        receiver = _receiver;
        code = _code;
        time_ini = _time_ini;
        data = _data;
        tree = _tree;
    }
    
    public event_sim(double _time, double _time_ini, int _sender, int _destination, int _receiver, int _code, String _data){
        this();
        time = _time;
        sender = _sender;
        destination = _destination;
        receiver = _receiver;
        code = _code;
        time_ini = _time_ini;
        data = _data;
    }
    
   //This constructor includes the tree ID in the message
    public event_sim(double _time, double _time_ini, int _sender, int _destination, int _receiver, int _code, String _data, int _tree){
        this();
        time = _time;
        sender = _sender;
        destination = _destination;
        receiver = _receiver;
        code = _code;
        time_ini = _time_ini;
        data = _data;
        tree = _tree;
    }
   
    //This constructor includes the tree ID in the message, and type
    public event_sim(double _time, double _time_ini, int _sender, int _destination, int _receiver, int _code, String _data, int _tree, int _type){
        this();
        type = _type;
        time = _time;
        sender = _sender;
        destination = _destination;
        receiver = _receiver;
        code = _code;
        time_ini = _time_ini;
        data = _data;
        tree = _tree;
    }
    
    //This constructor is designed specifically for data messages!!
    public event_sim(double _time, double _time_ini, int _source, int _final_destination, int _sender, int _destination, int _receiver, int _code, String _data, int _tree){
        this();
        time = _time;
        source = _source;
        final_destination = _final_destination;
        sender = _sender;
        destination = _destination;
        receiver = _receiver;
        code = _code;
        time_ini = _time_ini;
        data = _data;
        tree = _tree;
    }
    
    //This constructor includes the type of the protocol this event belongs to.
    public event_sim(double _time, double _time_ini, int _source, int _final_destination, int _sender, int _destination, int _receiver, int _code, String _data, int _tree, int type_){
        this();
        type = type_;
        time = _time;
        source = _source;
        final_destination = _final_destination;
        sender = _sender;
        destination = _destination;
        receiver = _receiver;
        code = _code;
        time_ini = _time_ini;
        data = _data;
        tree = _tree;
    }

    //This constructor includes the type of the protocol this event belongs to.
    public event_sim(double _time, double _time_ini, int _source, int _final_destination, int _sender, int _destination, int _receiver, int _code, String _data, int _tree, int type_, int message_type_){
        this();
        type = type_;
        time = _time;
        source = _source;
        final_destination = _final_destination;
        sender = _sender;
        destination = _destination;
        receiver = _receiver;
        code = _code;
        time_ini = _time_ini;
        data = _data;
        tree = _tree;
        message_type = message_type_;
    }
    
    public event_sim(double _time, double _time_ini, int _source, int _final_destination, int _sender, int _destination, int _receiver, int _code, String _data, int _seq_num, int _tree, int type_, int message_type_){
        this();
        type = type_;
        time = _time;
        source = _source;
        final_destination = _final_destination;
        sender = _sender;
        destination = _destination;
        receiver = _receiver;
        code = _code;
        time_ini = _time_ini;
        sequence_number = _seq_num;
        data = _data;
        tree = _tree;
    }
    
    public event_sim(event_sim e){
        this();
        if(e!=null){
            type = e.getType();
            time = e.getTime();
            time_ini = e.getTimeIni();
            source = e.getSource();
            final_destination = e.getFinalDestination();
            sender = e.getSender();
            receiver = e.getReceiver();
            destination = e.getDestination();
            code = e.getCode();
            data = e.getData();
            tree = e.getTree();
        }
    }

    public int getMessageType() {
        return message_type;
    }

    public void setMessageType(int message_type_) {
        message_type = message_type_;
    }



    public int getType(){
        return type;
    }
    
    public double getTime(){
        return time;
    }

    public double getTimeIni(){
        return time_ini;
    }
    
    public int getSource(){
        return source;
    }
    
    public int getFinalDestination(){
        return final_destination;
    }
    
    public int getSender(){
        return sender;
    }
    
    public int getReceiver(){
        return receiver;
    }
    
    public int getCode(){
        return code;
    }

    public String getData(){
        return data;
    }
    
    public int getDestination(){
        return destination;
    }
    
    public int getTree(){
        return tree;
    }
    
    public void setData(String d){
        data = d;
    }
    
    public void setInvalid(){
        validity = false;
    }
    
    public boolean isValid(){
        return validity;
    }
    
    public String toString(){
        
        st = ""+time+","+time_ini+","+source+","+final_destination+","+sender+","+receiver+","+destination+","+code+","+data+" ,"+tree+","+type;
        
        return st;
    }
    
    public int compareTo(Object obj){
        if(obj instanceof event_sim){
            event_sim e = (event_sim)obj;
            return (int)java.lang.Math.round(1000*(time-e.getTime()));
        }else{
            if(obj instanceof Double){
                td = (Double)obj;
                return (int)java.lang.Math.round(1000*(time-td));
            }
        }
            
        return -2000;
    }
    
}
