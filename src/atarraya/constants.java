






package atarraya;

/**
 *
 * @author pedrow
 */
public interface constants {

    public static int BEACON1=1;
    public static int BEACON2=2;
    public static int BEACON3=3;
    public static int BEACON4=4;
    
    public static int DATA_TRANSFER=5;
   
    

    public static int REGISTRATION_PACKET=11;
    public static int TICKET_GENERAION=5;
    public static int TICKET_DEPOSIT=6;
    public static int TICKET_REVOCATION=7;
    public static int REGISTRATION_REPLY=8;
    public static int TICKET_GENERAION_REPLY=9;
    public static int TICKET_DEPOSIT_REPLY=10;
    public static int TICKET_REVOCATION_REPLY=11;
    public static int DATA_REPLY_PACKET=12;

    //node_role
    public static int ROLE_REGULAR = 0;
    public static int ROLE_SINK = 1;
    public static int TYPE_LABEL_INITIAL = 0;
    public static int TYPE_LABEL_ACTIVE = 1;
    public static int TYPE_LABEL_INACTIVE = 2;
    public static int TYPE_LABEL_SLEEPING = 3;
            
    public static int MAX_ADDRESS = 390625;  //5^7
    public static int MAX_LEVEL = 8;
    
    public static int MAX_TRIALS = 100;
    public static int MAX_TREES = 5;
    
    public static double ENERGY_PER_TREE = 1000;
    public static double TEMP_ENERGY_PER_TREE = 100;
    
    public static int MAX_NUM_SINKS = 10;
    public static double MAX_TX_DELAY_RANDOM = 0.2;
    public static double MAX_SON_CONFIRM_DELAY_RANDOM = 0.7;
    public static double DELTA_TIME = 0.2;
    
    public static int SIZE_SHORT_PACKETS = 40; //bytes
    public static int SIZE_LONG_PACKETS = 100;

    public static double TX_RATE = 1000000; //1 Mbps

    public static double RADIO_STARTUP_TIME = 0.00035; // 350E^-6 SECONDS

    /*
     * Energy Constants
     */

    public static int SIMPLE_ENERGY_MODEL = 0;
    public static int MOTE_ENERGY_MODEL = 1;
    

    public static int OPER_TX_REGULAR= 0;       
    public static int OPER_TX_LONG = 1;           
    public static int OPER_RX_REGULAR = 2;    
    public static int OPER_RX_LONG= 3;
    public static int OPER_IDLE= 4;
    public static int OPER_READ_SENSOR = 5;
    
    public static int OPER_TX= 1;
    public static int OPER_RX= 2;

    public static int STATE_ACTIVE= 0;
    public static int STATE_READY= 1;
    public static int STATE_MONITOR= 2;
    public static int STATE_LOOK= 3;
    public static int STATE_SLEEP= 4;
    public static int STATE_JUST_RADIO = 5;
    public static int STATE_FULL_ACTIVE = 6;
    public static int STATE_FULL_ACTIVE2 = 7;

    
    // Data took from paper
    public static double ELECT_ENERGY = 0.00005; //mJoules
    public static double AMP_ENERGY = 0.00000001; //Amperes
        
    public static double TX_ENERGY = 0.02;
    public static double RX_ENERGY = 0.01;
    public static double DEATH_THSLD_ENERGY = 0.01;

    
    
    public static double MAX_ENERGY_MOTE = 3200;  //Chanrge in mA-hr on the sensor
    public static double MAX_ENERGY_SIMPLE = 1000;  //Chanrge in mJules on the sensor
    

     /*
     * Batch agent costants
     */
    
    public static int NO_WORK = 0;
    public static int GENERATE_TOPOS = 1;
    public static int SIMULATE = 2;
    public static int GIANTCOMPONENT = 3;
    public static int POSITIONFILES = 4;
    public static int OPTIMALSOLUTION = 5;
    public static int OPTIMALSOLUTIONBATCH = 6;
    
     /*
     * Utilities for EMST
     */
    
    public static double TIMEOUT_DELAY_EMST = 3;
    
    /*
     * BFS modes
     */
    
    public static int BFS_MODE_STANDARD= 0;
    public static int BFS_MODE_ACTIVE_ALIVE= 1;
    public static int BFS_MODE_STANDARD_FROM_SINKS= 2;
    public static int BFS_MODE_ACTIVE_ALIVE_FROM_SINKS= 3;
    public static int BFS_MODE_ACTIVE_ALIVE_FROM_SINKS_CHILDREN_ONLY= 4;
    public static int BFS_MODE_ACTIVE_ALIVE_OPTIMAL= 5;
    /*
     * Weights indeces
     */
    
    public static int WEIGHT_DISTANCE= 0;
    public static int WEIGHT_ENERGY = 1;
    
     /*
     * Sorting costants
     */
    
    public static int METRIC1_ONLY = 0;
    public static int METRIC2_ONLY = 1;
    public static int METRIC1_PRIMARY = 2;
    public static int METRIC2_PRIMARY = 3;
    public static int LINEAR_COMB_1_2 = 4;
    public static int METRIC1_ONLY_INV = 5;
    
    
    public static int MODE_DISTANCE= 0;
    public static int MODE_ENERGY = 1;
    
    /*
     * Utilities constants
     */
    
    public static double TREE_CONSTRUCTION_DELAY = 30.0;
    public static double RADIO_OFF_DELAY_PER_TREE = 30.0;
    public static double TX_DELAY = 0.1;
    public static double TIMEOUT_DELAY = 3;
    public static double TIMEOUT_DELAY_SHORT = 1.5;
    public static double PROCESSING_DELAY = 0.05;
    public static double TIMEOUT_NO_PARENT = 30.0;
    public static double TIMEOUT_NO_VISITED = 100.0;
    public static double SECOND_PARENTHOOD_DELAY = 15.0;
    public static double CLOCK_TIC = 0.01;
    public static double FIRST_TIMEOUT_CHECK_TREE = 100;
    public static double TIMEOUT_CHECK_TREE = 1000;
    public static double TIMEOUT_PAUSE_SEND_HELLO = 100;
    public static int UNIFORM_DIST = 0;
    public static int NORMAL_DIST = 1;
    public static int CONSTANT = 2;
    public static int CENTER = 3;
    public static int GRID_H_V = 4;
    public static int GRID_H_V_D = 5;
    public static int GRID_OPT_STRIP = 6;
    public static int GRID_OPT_COMB = 7;
    public static int GRID_OPT_SQUARE = 8;
    public static int GRID_OPT_TRIANGLE = 9;
    public static int GRID_LAMBDA = 10;
    public static int GRID_POISSON = 11;

    public static int ENERGY_CONSTANT = 0;
    public static int ENERGY_NORMAL = 1;
    public static int ENERGY_POISSON = 2;
    public static int ENERGY_UNIFORM = 3;
    public static double SENSING_COVERED_TIMEOUT = 15;

    
    /*
     * Node state
     */
    //public static int S_SLEEPING = 0;
    public static int S_FIND = 1;
    public static int S_FOUND = 2;
    public static int S_CORE = 3;
    
    
    /*
     * Edge state
     */
    public static int BASIC = 0;
    public static int BRANCH = 1;
    public static int REJECTED = 2;
    public static int ACCEPTED = 3;
    public static int CORE = 4;
    
    
    /*
     * Types of events     
     */
    
    public static int TC_PROTOCOL = 0;
    public static int TM_PROTOCOL = 1;
    public static int COMM_PROTOCOL = 2;
    public static int SENSOR_PROTOCOL = 3;
    public static int MOTION_PROTOCOL = 4;
    
    
    
    /*TC Protocols*/
    public static int PROTOCOL_NO_SELECTED = 0;
    public static int PROTOCOL_A3 = 1;
    public static int PROTOCOL_ECDS = 2;
    public static int PROTOCOL_CDS_RULEK = 3;
    public static int PROTOCOL_SIMPLE_TREE = 4;
    public static int PROTOCOL_JUST_TREE = 5;
    public static int PROTOCOL_KNEIGH = 6;
    public static int PROTOCOL_STATELESS_MULTICAST = 7;
    public static int PROTOCOL_LEACH = 8;

    
    
    
    /*TM Protocols*/
    
    public static int NO_TM = 100;
    public static int TM_ENERGY = 101;
    public static int TM_TIME = 102;
    public static int TM_FAILURE = 103;
    
    public static int TM_PROTOCOL_NO_SELECTED = 0;
    public static int TM_PROTOCOL_SINK_ISOLATED = 1;
    public static int TM_PROTOCOL_TIME_RESET = 2;
    public static int TM_PROTOCOL_TIME_MULTIPLE_STRUCT = 3;
    public static int TM_PROTOCOL_TIME_MULTIPLE_STRUCT_REGEN = 4;
    public static int TM_PROTOCOL_ENERGY_LOCAL_PATCHING_DSR = 5;
    public static int TM_PROTOCOL_ENERGY_RESET = 6;
    public static int TM_PROTOCOL_ENERGY_MULTIPLE_STRUCT = 7;
    public static int TM_PROTOCOL_ENERGY_MULTIPLE_STRUCT_REGEN = 8;
    
    /*COMM Protocols*/
    public static int COMM_PROTOCOL_NO_SELECTED = 0;
    public static int COMM_PROTOCOL_SIMPLE_FORWARDING = 1;
    
    /*SENSOR Protocols*/
    public static int SENSOR_PROTOCOL_NO_SELECTED = 0;
    public static int SENSOR_PROTOCOL_BASIC = 1;
    
    /*MOTION Protocols*/
    public static int MOTION_PROTOCOL_NO_SELECTED = 0;
    public static int MOTION_PROTOCOL_SIMPLE_MOTION = 1;
    public static int MOVE = 0;
    
    /*
     * Events
     */
    
    public static int WAKEUP = 100;
    public static int TEST_MSG = 101;
    public static int E_TEST_TIME_UP = 102;
    public static int ACCEPTED_MSG = 103;
    public static int REJECTED_MSG = 104;
    public static int REPORT_MSG = 105;
    public static int E_CORE_TIME_UP = 106;
    public static int REPORT_CORE_MSG = 107;
    public static int CONFIRMATION_MSG = 108;
    public static int CORE_CHANGE_MSG = 109;
    public static int INITIALIZE_MSG = 110;
    public static int CONNECT_MSG = 111;
    
    public static double TIMEOUT_TEST = 2.0;
    public static double TIMEOUT_CORE = 15.0;
    
    /*
     * Codes for simulator variables
     */

    public static int SIMMODE = 5;
    public static int BATCH_SIMULATION = 6;
    public static int MAX_COVERAGE = 0;
    public static int BEST_EFFORT_COVERAGE = 1;
    public static int SORTINGMODE = 7;
    public static int NUMNODES = 8;
    public static int NUMSINKS = 9;
    public static int NUMPOINTS = 10;
    public static int CLOCK = 11;
    public static int SELECTED_TM_PROTOCOL = 12;
    public static int SELECTED_SD_PROTOCOL = 13;
    public static int SELECTED_COMM_PROTOCOL = 14;
    public static int CANDIDATE_GROUP_STEP = 15;
    public static int WEIGHT_METRIC1 = 16;
    public static int  WEIGHT_METRIC2 = 17;
    public static int NUMINFRASTRUCTURES = 18;
    public static int NEXT_RESET_PERIOD = 19;
    public static int PM_STEP = 20;
    public static int PM_SLEEP = 21;
    public static int KNEIGH_K = 22;
    public static int KNEIGH_TYPE = 23;
    public static int ALPHA_COVERAGE = 24;
    
    /*
     * Node states
     */
    
    public static int S_NOT_VISITED = 0;
    public static int S_VISITED = 1;
    public static int S_SELECTED = 2;
    public static int S_PARENT = 3;
    public static int S_CLUSTERHEAD = 4;
    public static int S_SLEEPING = 5;
    public static int S_TX = 6;
    public static int S_RX = 7;
    public static int S_SINGLE_PARENT = 8;
    public static int S_PAUSED = 9;
    public static int S_IN_SEARCH = 10;
    public static int S_SINK = 11;
    public static int S_DEAD = 12;
    public static int S_COVERED = 13;
    
    
    //New state definition
    public static int S_INITIAL_STATE = 14;
    public static int S_CHILD = 15;
    public static int S_CANDIDATE = 16;
    public static int S_SLEEP_CANDIDATE = 17;
    public static int S_ACTIVE_CANDIDATE = 18;
    public static int S_WAITING_ACTIVE = 19;
    public static int S_ACTIVE = 20;
    public static int S_SLEEP = 21;
    public static int S_SLEEP_PRECANDIDATE = 22;

    public static int S_INACTIVE_BFS = 0;
    public static int S_ACTIVE_BFS = 1;

    /*
     * Event codes
     */
    public static final int HELLO = 0;
    public static final int PARENT_RECOGNITION = 1;
    public static final int SON_RECOGNITION = 2;
    public static final int PARENT_RECOG_TIME_OUT = 3;
    public static final int PARENT_RECOGNITION_EVENT = 16;
    public static final int SEND_HELLO = 4;
    public static final int TIMEOUT_RECOGNITION = 5;
    
    public static final int CHECK_4_CHILDREN = 11;
    public static final int CHECK_4_BROTHER = 12;
    public static final int BROTHER_BLOCKAGE_MESSAGE = 13;
    
    public static final int END_TIMEOUT_NO_PARENT = 7;
    public static final int REMOVE_CHILD = 8;
    
    
    public static final int ADD_CHILDREN = 6;
    public static final int PARENT_CANDIDATE = 9;
    public static final int PARENT_RECOG_TIME_OUT_2 = 10;
    
    public static final int FINAL_CHECK_QUERY = 14;
    public static final int FINAL_CHECK_EVENT = 15;
    
    public static final int DATA = 17;
    public static final int CHECK_TREE = 18;
    
    public static final int GOING_SLEEP = 19;
    public static final int DELAYED_RADIO_OFF = 20;
    public static final int GOING_SLEEP_2 = 21;
    public static final int DELAYED_RADIO_OFF_2 = 23;

    public static final int SENSING_COVERED_TIMEOUT_EVENT = 30;
    public static final int SENSING_COVERED_MESSAGE = 31;

    public static final double ALPHA_COVERAGE_RATE = 1.0;

    
    /*
     * Generic protocol Events
     */
    
    public static final int INIT_NODE = 1000;
    public static final int INIT_EVENT = 1001;
    public static final int RESET_TM_PROTOCOL = 1002;
    
    /*
     * Simple Sensor Events
     */
    public static final int QUERY_SENSOR = 0;
    public static final int RESET_QUERY_SENSOR = 1;
    public static final double TIMEOUT_FOR_FIRST_QUERY_SENSOR = 30.0;
    
    
     /*
     * Simple Topology Maintenance Events
     */
    public static final int RESET_MESSAGE = 0;
    public static final int SEND_RESET_MESSAGE = 1;
    public static final int RESET_TOPOLOGY = 2;
    public static final int SEND_HELLO_MESSAGE_TM = 3;
    public static final int HELLO_MESSAGE_TM = 4;
    public static final int REPLY_MESSAGE_TM = 5;
    public static final int HELLO_MESSAGE_WAIT_TIMEOUT_TM = 6;
    public static final int RESET_MESSAGE_SINK = 15;
    
    public static final int RESET_MESSAGE_LISTENING_TIMEOUT_EVENT = 10;
    
    public static final int SEND_HELLO_MESSAGE_TM_INI = 13;
    public static final int HELLO_MESSAGE_TM_INI = 14;
    public static final int HELLO_MESSAGE_WAIT_TIMEOUT_TM_INI = 16;
    
    public static final int SEND_RESET_INFRASTRUCTURE_MESSAGE = 7;
    public static final int RESET_INFRASTRUCTURE_MESSAGE = 8;
    
    public static final int REPAINT_FRAME_TM = 9;
    
    public static final int TM_DEATH_NOTIFICATION = 17;
    
    public static final double RESET_TIMEOUT = 15.0;
    //public static final double NEXT_RESET_TIMEOUT = 5000.0;
    public static final double RESET_MESSAGE_LISTENING_TIMEOUT= 300.0;
    public static final double NEXT_HELLO_TIMEOUT = 300.0;
    public static final double WAIT_HELLO_REPLY_TIMEOUT = 5.0;
    public static final double REPAINT_FRAME_TIMEOUT_TM = 200;
    
    public static final int BACK_TO_NORMAL_EVENT = 18;
    public static final double BACK_TO_NORMAL_EVENT_TIMEOUT_TM = 30.0;
    
    /*
     * Local Topology Maintenance Events
     */
    
    public static final int SEND_ROUTE_REQUEST_MESSAGE = 10;
    public static final int ROUTE_REQUEST_MESSAGE = 11;
    public static final int SEND_ROUTE_REPLY_MESSAGE = 12;
    public static final int ROUTE_REPLY_MESSAGE = 13;
    
    public static final int TIMEOUT_LISTENING_RRQ_MESSAGE = 14;
    public static final int TIMEOUT_SEND_RRQ_MESSAGE = 15;
    public static final int TIMEOUT_BACK_TO_NORMAL_EVENT = 16;
    public static final int TIMEOUT_LISTENING_RRQ_MESSAGE_ON_ACTIVE = 21;
    public static final int TIMEOUT_GO_DEAD_EVENT = 22;
    
    public static final int REACTIVATE_MESSAGE_I = 17;
    public static final int REACTIVATE_MESSAGE_II = 18;
    public static final int REACTIVATE_MESSAGE_III = 19;
    
    public static final int SEND_REACTIVATE_MESSAGE = 20;
    
    public static final double TIMEOUT_SENDING_RRQ = 3.0;
    public static final double TIMEOUT_LISTENING_RRQ = 3.0;
    public static final double TIMEOUT_LISTENING_RRQ_ON_ACTIVE = 9.0;
    public static final double TIMEOUT_BACK_TO_NORMAL = 36.0;
    public static final double TIMEOUT_GO_DEAD = 5.0;
    
    
    public static final int TM_INITIAL = 0;
    public static final int TM_INACTIVE = 1;
    public static final int TM_SEMI_ACTIVE = 2;
    public static final int TM_ACTIVE = 3;
    public static final int TM_GRANDPA = 4;
    public static final int TM_VISITED = 5;
    public static final int TM_READY = 6;
    public static final int TM_DEAD = 7;
    
     /*
     * Routing Table util variable
     */
    public static final int RESET_COMM_PROTOCOL = 0;
    public static final int UPDATE_ROUTING_TABLE = 1;
    public static final double NEXT_UPDATE_ROUTING_TABLE_TIMEOUT = 100.0;
    
     /*
     * Routing Table util variable
     */
    public static final int SEQUENCE_NUMBER_ARRAY_SIZE = 3;
    
    /*
     * EMST messages
     */
    public static final int ADOPT_ME = 19;
  
    
    
      /************************************************************
     *                                 Constants for ECDS tree
     *************************************************************/
    
    /* CDS Node State codes */
    public static int WHITE = 100;
    public static int GREY = 101;
    public static int BLACK = 102;
    public static int BTRANS = 103;
    public static int BLUE = 104;
    public static int TRANSITION = 105;
    public static int GTRANS = 106;
    public static int GREY_FINAL = 107;
    
    /* CDS Message codes */
    
    public static int INQUIRYREPLY = 207;
    public static final int BLACKMESSAGE = 208;
    public static final int GREYMESSAGE = 209;
    public static final int INQUIRYMESSAGE = 210;
    public static final int BLUEMESSAGE = 211;
    public static final int INVITEMESSAGE = 212;
    public static final int UPDATEMESSAGE = 213;
    public static final int PREINQUIRYMESSAGE = 216;
    public static final int PREINQUIRYREPLY = 217;
    public static final int PREINQUIRYMESSAGE2 = 220;
    public static final int PREINQUIRYREPLY2 = 221;
    public static final int CHECK_ACTIVE_NODES = 222;
    public static final int PARENT_DERECOGNITION = 227;
    
    /* CDS Event codes */
    public static final int INQUIRY_TIMEOUT = 214;
    public static final int PREINQUIRY_TIMEOUT = 218;
    public static final int PREINQUIRY_TIMEOUT2 = 223;
    public static final int TIMEOUT = 215;
    public static final int PHASE2_TIMEOUT = 219;
    public static final int BUSY = 224;
    public static final int NOT_BUSY = 225;
    public static final double PHASE2_TIMEOUT_DELAY = 100.0;
    public static final int GOBACKTOGREY = 226;
    
    public static final int PARENT_RECOGNITION_EVENT_EECDS = 227;
    
    
       /************************************************************
     *                                 Constants for CDS-RuleK tree
     *************************************************************/
    
    public static int C_BLACK_LIST_REPLY_UPDATE = 214;
    public static int C_BLACK_MARKER_FINAL = 213;
    public static int C_COVERED = 212;
    public static int C_BLACK_FINAL = 211;
    public static int C_BLACK_FINAL_TEMP = 250;
    public static int C_UNMARKED = 200;
    public static int C_UNMARKED_FINAL = 215;
    public static int C_UNMARKED_TEMP = 251;
    public static int C_UNMARKED_TEMP_TIMEOUT = 252;
    public static int C_SET_FINAL_PARENT = 216;
    public static int C_BLACK = 201;
    public static int C_NEIGHBORHOOD_COUNT = 202;
    public static int C_NEIGHBORHOOD_COUNT_REPLY = 203;
    public static int C_NEIGHBORHOOD_COUNT_TIMEOUT = 204;
    public static int C_BLACK_MARKER = 205;
    public static int C_BLACK_LIST_QUERY = 206;
    public static int C_BLACK_LIST_REPLY = 207;
    public static int C_BLACK_LIST_QUERY_TIMEOUT = 208;
    public static int C_BLACK_LIST_REPLY_TIMEOUT = 209;
    public static int C_BLACK_UNMARKER = 210;
    public static int C_BLACK_FINAL_TIMEOUT = 217;
    public static int C_BLACK_WAITING = 218;
       
    public static final int PARENT_RECOGNITION_EVENT_RULEK = 220;


      /************************************************************
     *                                 Constants for KNeigh-Tree
     *************************************************************/

    public static int KN_NOT_VISITED = 300;
    public static int KN_VISITED = 301;
    public static int KN_UPDATED = 302;


 /************************************************************
     *                                 Constants for A3Level
     *************************************************************/

    public static int LEV_NOT_VISITED = 300;
    public static int LEV_VISITED = 301;
    public static int LEV_COVERED = 302;
    public static int LEV_UPDATED = 303;
    public static int LEV_ACTIVE = 304;
    public static int LEV_SLEEP = 305;
    public static int LEV_ACTIVE_CANDIDATE = 306;
    
    public static int LEV_METRIC_DISTRIB_MESSAGE = 310;
    public static int LEV_SELECTED_MESSAGE = 311;
    public static int LEV_METRIC_DISTRIB_TIMEOUT = 312;
    public static int LEV_SECOND_OPPORTUNITY = 313;
    public static int LEV_CHECK_4_UNCOVERED_MESSAGE = 314;
    public static int LEV_CHECK_4_UNCOVERED_REPLY = 315;
    public static int LEV_CHECK_4_UNCOVERED_TIMEOUT = 316;
    public static int LEV_CHECK_4_CHILDREN = 319;

    public static int LEV_SEND_HELLO = 317;
    public static int LEV_HELLO_MESSAGE = 318;




}
