package atarraya;

import atarraya.element.*;
import atarraya.event.*;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.util.Vector;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.concurrent.Semaphore;
import javax.imageio.ImageIO;
import  javax.swing.DefaultListModel;
import  javax.swing.JOptionPane;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import sun.misc.BASE64Encoder;


/**
 *
 * @author  pedrow
 */
public class atarraya_frame extends javax.swing.JFrame implements constants{

    public boolean CHOOSE_BASE_STATION=false;
    public boolean DEPLOYMENT=true;
    public boolean graph_selection=false;

    public static int BASE_STATION_ID=0;
     public static int GRAPH_NODE_ID=0;

    public static int DEAD_NODES;
    public static HashMap<Integer,Integer> DEAD_NODE_MAP=new HashMap<Integer,Integer>();


    public static int MESSAGE_TYPE_BSHELLO=1;
    public static int MESSAGE_TYPE_CLUSTER_FORMATION=2;
    public static int MESSAGE_TYPE_CLUSTER_FORMATION_REPLY=3;
    public static int MESSAGE_TYPE_JOIN_REQUEST=4;
    public static int MESSAGE_TYPE_JOIN_REPLY=5;
    public static int MESSAGE_TYPE_DATA_TRANSFER=6;
  

    public int mobile_node;
    int temp_node=0;
    public int trusted_authority=0;
    boolean select_client,select_mesh_router,select_gateway,select_tauthority,select_sender,select_join,select_graph;
    int client_selected=0,mesh_selected=0,gateway_selected=0,authority_selected=0;

    ArrayList<String> ta_list=new ArrayList<String>();
    ArrayList<String> mesh_list=new ArrayList<String>();
    ArrayList<String> gateway_list=new ArrayList<String>();
    ArrayList<String> client_list=new ArrayList<String>();

    public static int data_sent=0;
    public static int data_received=0;

 

  

    /********************************************
     * Simulation agent and and Batch Simulation
     ********************************************/
    ArrayList<String> receiver_ids=new ArrayList<String>();
    the_sim my_sim;                                 // Instance of the the_sim class that executes the simulations
    double clock;                                      // Variable that stores the simulators clock
    boolean TC_Prot_final;                        // It marks the ending of the TC protocol, on the first run, on check_sim_finish()
    double max_clock_time;
    boolean topology_loaded;                    //Boolean variable that defines if a topology has been loaded.
    
    BatchExecutor the_batch_sim;        //Auxiliary thread for batch execution on background of frame operations
    
    //Simulation variables
    int simmode;                            // This variable is related to the first combobox on the other params tear in Deployment
    int sortingmode;                        // This variable is related to the second combobox on the other params tear in Deployment
    boolean all_trees;                      // Switch to start or not all trees in paralel on the SimulationStart() method
    int active_sinks;                           // Counter of sinks that have been initiated. It has to be global because of the possibility of pressing Start several times
    
    
    // Batch simulation variables
    boolean batch_simulation;
   public static boolean sat=false;           // Condition that determines if the simulator is in batch_execution mode.
    
    //Variables for batch simulation control of files and current execution
    int current_topology_from_batch;
    int current_instance_topology_from_batch;
    int num_files_topology_from_batch;
    int num_topology_from_batch;
    int total_num_topologies;
    
    int multiplicity;               // How many times do we want a single scenario to be simulated
    int total_alpha;

    int pm_step;
    int pm_sleep;
    
    
    /***********************************************
     * Protocol management
     ***********************************************/
    
    // Protocols handler and executer
    NodeHandler simulator;          
    
    //Index of the selected protocols
    int selected_TCprotocol;
    int selected_TMprotocol;
    int selected_SDprotocol;
    int selected_COMMprotocol;
    int selected_Motionprotocol;
    int selected_energy_model;

    //Boolean that signals if a protocol of each type were selected
    boolean sim_assigned;
    boolean TC_Selected;
    boolean TM_Selected;
    boolean SD_Selected;
    boolean COMM_Selected;
    
    
    /************************************************
     * Topology Information and Topology Creation variables
     ************************************************/
    
    // Main data structure. All information about the nodes is stored here.
    Vector<node> mynodes;
    
    int w, h;                        //Width and Height of the deployment area
    int numnodes;               //Number of regular nodes (no sink)
    int numsinks;                //Total number of sink nodes
    int numpoints;              //The total number of points in the area = nodes + sinks
    int[] num_sinks_per_tree;   //Number of sinks per tree
    
    double candidate_grouping_step;
    
    Semaphore sem2;                     //Grant access to the data structure Node
    
    // Temporary variables to be used as a reference for grid construction and other minor operations
    double diameter, sense_diameter,radius,sense_radius;
    
    double bit_error_rate;                  // Bit error rate assigned to the deploment area to simulate random packet loss
    
    boolean batch_creation;                 // Signal of being in batch_creation mode
    int start_index;                            // start value of the index of topology creation
    boolean checkTrials;                    // This variable enables Atarraya to avoid loops on batch creation when topology definition is impossible to create
    
    
    //Needed for the connectivity check
    Vector<Integer> temp_ids;
    
    // Vectors that store all the control words for nodes and sinks creation
    Vector<String> node_creation_words;
    Vector<String> sink_creation_words;
    // Interface versions of the previous, to be showed on the comboboxes
    DefaultListModel node_creation_list;
    DefaultListModel sink_creation_list;
    
    //Temporary variables useful for having control of node id's in nodes creation
    int current_node_count;
    int tempnumnodes;
    
    // Variables associated to energy and position distribution of the random variables, when nodes are created
    // Energy related
    int energy_distribution;
    double energy_distribution_parameter;
    double sigma_e;
    double MAX_ENERGY;

    //Position related
    int rand_distribution;
    Point p;
    double sigma;
    
    //Metric Weights
    double weight_metric1;
    double weight_metric2;
    
    
    double NEXT_RESET_TIMEOUT;
    double TM_ENERGY_THSLD_STEP;

    //Variables for Topology Construction protocols

    int kneigh_k;
    double alpha_coverage_value;

    public int sender_node;

    /************************************************
     *  Interface and visualization Variables
     ************************************************/
    
    // Class in charge of displaying the topology.
    newpanel myPanel;
    
    boolean show_events;        // signal of showing the event log on the interface
    int treeID;                          // Temporary variable
    int treeIDViz;                      // id of the tree being displayed, on the manual selection mode (no active_tree mode).
    boolean view_active_tree;   // signal the active tree view mode.
    int gridmode;                      // Index of the grid mode selected
    boolean no_paint;              // Signal of no painting a topology on the panel, to avoid errors
    

    DecimalFormat clock_format;

    /******************************************
     * Statistics variables
     ******************************************/

    boolean indiv_reports;
    boolean sumarized_reports;
    boolean excel_ready_reports;
    String report_descriptor;
    boolean save_stats;
    boolean save_all_stats;
    int save_all_stats_step;
    boolean save_lifetime_log;
    String temp_lifetime_string;
    String temp_lifetimeSurvivors_string;
    String temp_lifetimeBFS_string;
    String temp_lifetimeBFS_Coverage_c_string;
    String temp_lifetimeBFS_Coverage_s_string;
    boolean save_events_log;
    boolean save_results_log;
    
    // Gigant Component test and CTR related variables
    int num_nodes_ctr;                     //Number of nodes used on the Theoretical CTR tests
    int area_side_ctr;                        //Area Side
    int comm_radius_ctr;                  // Comm radius for CTR
    int num_topologies_ctr;                // Number of topologies for the Giant Component Test
    double initial_ctr;                        // Initial comm radius fo the Giant Component Test
    double step_ctr;                        // Step in comm radius fo the Giant Component Test
    Semaphore sem_GC;                     //Grant access to continue the process of GC after the topologies have been created
    boolean GC_Test;                      //Shows that the GC Test is being perfomed
    
    double initial_k_ctr;                    //Initial value of the parameter K
    double final_k_ctr;                     //Final value of the parameter K
    double step_k_ctr;                      //Rate of change of the parameter K
    int initial_i_exp_l_ctr;                    //Initial value of the parameter i on the formula for defining parameter L
    int final_i_exp_l_ctr;                       //Final value of the parameter i on the formula for defining parameter L
    
    
    
    
    int[] notCovered_Visited;               //Structure to store not covered and not visited nodes.
    double avgNeighb;                        // Average number of neighbors of a node on the topology
    double avgNeighbActiveNodes;      // Average number of neighbors of the active nodes on the topology
    int ReachableNeighbActiveNodes;      // Number of active neighbors reachbale from the sink node
    int contNeighb;                       // Temporary variable to calculate the average number of neighbors
    double avg_level;                          // Average level of the nodes on the topology
    int num_events;                            // Total number of events on the event log
    int num_messages;                        // Total number of short messages sent
    int num_messages_long;                // Total number of long messages sent
    int num_messages_r;                     // Total number of short messages received
    int num_messages_long_r;             // Total number of long messages received
    int num_messages_l;                     // Total number of short messages lost
    int num_messages_long_l;             // Total number of long messages lost
    int num_messages_data_r_sink;     // Total number of messages received by the sink
    int count_dead;                             // Total number of dead nodes
    int count_nodes_with_dead_parent; // Total number of nodes disconnected

    int count_TM_invocations;       //Counts how many times the TM protocol was invocked

    double covered_area;                     // Percentage of covered area by the active nodes
    double covered_area_sens;                     // Percentage of covered area by the active nodes
    double covered_area_from_sink_c;      // Percentage of covered area by the active nodes connected to the sink
    double covered_area_from_sink_s;      // Percentage of covered area by the active nodes connected to the sink
    boolean view_covered_area_stat;    // Signal to calculate the covered area on the stats
    byte[][] mat_area;                          // Auxiliary matrix to calculate the covered area

    double covered_area_sens_k;
    double covered_area_sens_k_all;

    double ctr;                                     //Critical Transmission Range calculated
    
    String stats;                                   // Statistics string
    String stats_from_panel;                  
    String sim_error;                             // String that includes on stats if there was an error on the execution of the sim
    
    
    int ideal_p;                                      // Number of cells on a grid that covers the deployment area. One ideal metric
    String grid_mode_text;                    // Shows which graphical grid was used
    int[] count_active;                          // Counts how many nodes are active
    double total_energy_spent;
    double total_energy;
    double total_energy_ini;
    double total_energy_tree;
    double energy_TX_regular;
    double energy_RX_regular;
    double energy_TX_long;
    double energy_RX_long;
    
    
    
    /*
     * File related variables
     */
    
    String the_parent_dir,the_name, the_name2;
    File myFile;
    File[] myFiles;
        
    FileWriter fout;			
    BufferedWriter mywrite;
    FileReader fin;			
    BufferedReader myread;

    //File readers for the results log file
    File myLogFile_RESULTS;
    FileWriter fout_log_results;
    BufferedWriter mywrite_log_results;

    //File readers for the stats log file
    File myLogFile_STAT;
    FileWriter fout_log_stat;			
    BufferedWriter mywrite_log_stat;
    FileReader fin_log_stat;			
    BufferedReader myread_log_stat;
    
    //File readers for the lifetime log file
    File myLogFile_STAT_LT;
    FileWriter fout_log_lt;			
    BufferedWriter mywrite_log_lt;
    FileReader fin_log_lt;			
    BufferedReader myread_log_lt;
    
    //File readers for the events log file
    File myLogFile_STAT_EV;
    FileWriter fout_log_ev;			
    BufferedWriter mywrite_log_ev;
    FileReader fin_log_ev;			
    BufferedReader myread_log_ev;
    
    FileNameExtensionFilter filter;
    
    
            
    /**
     * Creates new form atarraya_frame
     */
    public atarraya_frame(){
        int i=0;
        
        the_batch_sim = new BatchExecutor(this);
        the_batch_sim.start();
        
        simulator = new NodeHandler(this);
        mynodes = new Vector<node>();
        numnodes = 0;
        numsinks = 0;
        numpoints = numnodes + numsinks;
        clock = 0.0;
        simmode = 0;
        gridmode = 0;
        sortingmode = METRIC1_ONLY;
        candidate_grouping_step = 0.20;
        topology_loaded = false;
        max_clock_time =3000000.0;

        pm_step = 20;
        pm_sleep = 10;

        node_creation_words = new Vector<String>();
        sink_creation_words = new Vector<String>();
        node_creation_list = new DefaultListModel();
        sink_creation_list = new DefaultListModel();
        current_node_count = 0;
        tempnumnodes=0;
        TC_Prot_final = false;
        
        selected_TCprotocol = 0;
        selected_TMprotocol = 0;
        selected_SDprotocol = 0;
        selected_COMMprotocol = 0;
        selected_Motionprotocol = 0;
        selected_energy_model = 0;
        show_events = false;
        
        num_sinks_per_tree = new int[MAX_TREES];        
        count_active = new int[MAX_TREES];        
        for( i=0;i<MAX_TREES;i++){
            num_sinks_per_tree[i] = 0;
            count_active[i] = 0;
        }
        
        num_events = 0;
        all_trees = false;
        active_sinks = 0;
        
        bit_error_rate = 0;
        
        avgNeighb = 0;
        avgNeighbActiveNodes = 0;
        
        radius=100;
        sense_radius = 20;
        diameter=2*radius;
        sense_diameter=2*sense_radius;
        treeID = -1;
        treeIDViz = -1;
        view_active_tree = false;
        notCovered_Visited = new int[2];
        avg_level = 0;
        num_messages=0;
        num_messages_long=0;
        num_messages_r=0;
        num_messages_long_r=0;
        num_messages_data_r_sink=0;
        count_dead=0;
        count_nodes_with_dead_parent=0;
        
        sim_assigned = false;
        covered_area = 0;
        covered_area_sens = 0;
        covered_area_from_sink_c = 0;
        covered_area_from_sink_s = 0;
        view_covered_area_stat = false;
        temp_ids = new Vector<Integer>();
        covered_area_sens_k = 0;
        covered_area_sens_k_all = 0;
         
        energy_distribution=0;
        energy_distribution_parameter=1000;
        MAX_ENERGY = 1000;
        rand_distribution=0;
        p = new Point();
        sigma = 5;
        
        weight_metric1 = 0.5;
        weight_metric2 = 0.5;
        NEXT_RESET_TIMEOUT = 1000.0;
        TM_ENERGY_THSLD_STEP = 0.9;

        kneigh_k = 6;
        

        stats="";
        stats_from_panel="";
        
         /*
         * Parameters for the Visualization Panel
         */
        
        w = 600;
        h = 600;
        
        //myPanel = new mainPanel(this, w, h, diameter, sense_diameter);
        myPanel = new newpanel(w,h);
        myPanel.setBounds(0, 0, w, h);
        myPanel.setArea(w, h);
        myPanel.setActive_tree_viz(treeIDViz);
        
        save_stats = false;
        save_all_stats = false;
        save_all_stats_step = 20;
        save_lifetime_log = false;
        save_events_log = false;
        indiv_reports = true;
        sumarized_reports = false;
        excel_ready_reports = false;
        report_descriptor="";
        current_topology_from_batch=0;
        save_results_log = false;
        
        num_nodes_ctr=100;
        area_side_ctr=100;
        comm_radius_ctr=0;
        num_topologies_ctr = 100;
        initial_ctr = 0.01;
        step_ctr = 0.01;
        sem_GC = new Semaphore(0);
        GC_Test = false;
        
        filter = new FileNameExtensionFilter("ata","ATA");

        clock_format = new DecimalFormat("0.#######");

        initComponents();
        
        batch_simulation = false;
        batch_creation = false;
        no_paint = false;
        start_index = 0;
        checkTrials = true;
        
        sem2 = new Semaphore(1);
        current_topology_from_batch = 0;
        current_instance_topology_from_batch = 0;
        num_files_topology_from_batch = 0;
        num_topology_from_batch = 0;
        multiplicity = 1;
        total_alpha = 1;
        total_num_topologies = 0;
        sim_error = "no error";
        TC_Selected = false;
        TM_Selected = false;
        SD_Selected = false;
        COMM_Selected = false;
        count_TM_invocations = 0;
                
        jTextField16.setText("1");
        
        energy_TX_regular=0;
        energy_RX_regular=0;
        energy_TX_long=0;
        energy_RX_long=0;
        total_energy_spent=0;
        
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser2 = new javax.swing.JFileChooser();
        jFileChooser1 = new javax.swing.JFileChooser();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel15 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel16 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jTextField15 = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox();
        jTextField20 = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        jTextField21 = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        jCheckBox22 = new javax.swing.JCheckBox();
        jTextField23 = new javax.swing.JTextField();
        jTextField24 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jComboBox8 = new javax.swing.JComboBox();
        jLabel56 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jLabel57 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jTextField16 = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jCheckBox16 = new javax.swing.JCheckBox();
        jProgressBar2 = new javax.swing.JProgressBar();
        jLabel27 = new javax.swing.JLabel();
        jCheckBox24 = new javax.swing.JCheckBox();
        jTextField25 = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jLabel61 = new javax.swing.JLabel();
        jButton15 = new javax.swing.JButton();
        jTextField22 = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        jTextField31 = new javax.swing.JTextField();
        jTextField32 = new javax.swing.JTextField();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        jCheckBox8 = new javax.swing.JCheckBox();
        jComboBox13 = new javax.swing.JComboBox();
        jLabel96 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox();
        jTextField28 = new javax.swing.JTextField();
        jLabel79 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jTextField26 = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jTextField27 = new javax.swing.JTextField();
        jTextField30 = new javax.swing.JTextField();
        jLabel69 = new javax.swing.JLabel();
        jButton14 = new javax.swing.JButton();
        jCheckBox14 = new javax.swing.JCheckBox();
        jPanel21 = new javax.swing.JPanel();
        jTextField9 = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        jButton18 = new javax.swing.JButton();
        jPanel26 = new javax.swing.JPanel();
        jTextField38 = new javax.swing.JTextField();
        jLabel90 = new javax.swing.JLabel();
        jTextField39 = new javax.swing.JTextField();
        jLabel91 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jTextField40 = new javax.swing.JTextField();
        jLabel92 = new javax.swing.JLabel();
        jTextField41 = new javax.swing.JTextField();
        jLabel93 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jLabel95 = new javax.swing.JLabel();
        jTextField42 = new javax.swing.JTextField();
        jPanel29 = new javax.swing.JPanel();
        jTextField43 = new javax.swing.JTextField();
        jLabel97 = new javax.swing.JLabel();
        jTextField44 = new javax.swing.JTextField();
        jLabel98 = new javax.swing.JLabel();
        jTextField45 = new javax.swing.JTextField();
        jLabel99 = new javax.swing.JLabel();
        jCheckBox29 = new javax.swing.JCheckBox();
        jButton3 = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();
        jCheckBox7 = new javax.swing.JCheckBox();
        jButton26 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jComboBox7 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jComboBox10 = new javax.swing.JComboBox();
        jLabel67 = new javax.swing.JLabel();
        jComboBox11 = new javax.swing.JComboBox();
        jLabel68 = new javax.swing.JLabel();
        jCheckBox13 = new javax.swing.JCheckBox();
        jLabel86 = new javax.swing.JLabel();
        jComboBox9 = new javax.swing.JComboBox();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel94 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        jCheckBox20 = new javax.swing.JCheckBox();
        jButton11 = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jLabel47 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton8 = new javax.swing.JButton();
        jCheckBox6 = new javax.swing.JCheckBox();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextPane3 = new javax.swing.JTextPane();
        jLabel59 = new javax.swing.JLabel();
        jCheckBox11 = new javax.swing.JCheckBox();
        jButton6 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jCheckBox17 = new javax.swing.JCheckBox();
        jCheckBox18 = new javax.swing.JCheckBox();
        jCheckBox19 = new javax.swing.JCheckBox();
        jLabel48 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jCheckBox9 = new javax.swing.JCheckBox();
        jCheckBox12 = new javax.swing.JCheckBox();
        jCheckBox15 = new javax.swing.JCheckBox();
        jCheckBox23 = new javax.swing.JCheckBox();
        jTextField3 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jCheckBox28 = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel100 = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jCheckBox5 = new javax.swing.JCheckBox();
        jButton10 = new javax.swing.JButton();
        jCheckBox21 = new javax.swing.JCheckBox();
        jCheckBox10 = new javax.swing.JCheckBox();
        jLabel89 = new javax.swing.JLabel();
        jCheckBox26 = new javax.swing.JCheckBox();
        jPanel9 = new javax.swing.JPanel();
        jComboBox4 = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jLabel102 = new javax.swing.JLabel();
        jTextField46 = new javax.swing.JTextField();
        jButton25 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jButton4 = new javax.swing.JButton();
        jTextField8 = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jPanel23 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel12 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jTextField4 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jButton21 = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jTextField6 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jTextField10 = new javax.swing.JTextField();
        jLabel76 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jTextField29 = new javax.swing.JTextField();
        jLabel78 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        jButton19 = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jLabel71 = new javax.swing.JLabel();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel70 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jTextField33 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jTextField34 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jTextField35 = new javax.swing.JTextField();
        jLabel73 = new javax.swing.JLabel();
        jTextField36 = new javax.swing.JTextField();
        jLabel83 = new javax.swing.JLabel();
        jTextField37 = new javax.swing.JTextField();
        jLabel84 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        jButton16 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jButton17 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jCheckBox25 = new javax.swing.JCheckBox();
        jTextField11 = new javax.swing.JTextField();
        jLabel74 = new javax.swing.JLabel();
        jButton22 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jCheckBox27 = new javax.swing.JCheckBox();
        jPanel31 = new javax.swing.JPanel();
        jButton28 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        jButton31 = new javax.swing.JButton();
        jLabel103 = new javax.swing.JLabel();
        jButton32 = new javax.swing.JButton();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();

        jFileChooser1.setFileFilter(filter);
        jFileChooser1.setFileSelectionMode(javax.swing.JFileChooser.FILES_AND_DIRECTORIES);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Atarraya - A Topology Control Simulator");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel1.setMinimumSize(new java.awt.Dimension(500, 500));
        jPanel1.setPreferredSize(new java.awt.Dimension(500, 500));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jSplitPane1.setDividerLocation(610);
        jSplitPane1.setOneTouchExpandable(true);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.add(myPanel);
        jPanel2.setBounds(0,0,w,h);
        jPanel2.setLayout(new java.awt.BorderLayout());
        jSplitPane1.setLeftComponent(jPanel2);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Deployment", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 255))); // NOI18N
        jPanel7.setEnabled(false);
        jPanel7.setLayout(null);

        jLabel3.setText("# Nodes");
        jPanel7.add(jLabel3);
        jLabel3.setBounds(10, 20, 41, 14);

        jTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField1.setText("100");
        jPanel7.add(jTextField1);
        jTextField1.setBounds(10, 40, 50, 20);

        jTextField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField2.setText("100");
        jPanel7.add(jTextField2);
        jTextField2.setBounds(80, 40, 50, 20);

        jLabel4.setText("Comm Radius");
        jPanel7.add(jLabel4);
        jLabel4.setBounds(80, 20, 64, 14);

        jTextField7.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField7.setText("20");
        jPanel7.add(jTextField7);
        jTextField7.setBounds(160, 40, 50, 20);

        jLabel15.setText("Sensing Radius");
        jPanel7.add(jLabel15);
        jLabel15.setBounds(160, 20, 72, 14);

        jTextField13.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField13.setText("600");
        jPanel7.add(jTextField13);
        jTextField13.setBounds(10, 90, 40, 20);

        jLabel34.setText("Width");
        jPanel7.add(jLabel34);
        jLabel34.setBounds(60, 90, 28, 20);

        jTextField14.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField14.setText("600");
        jPanel7.add(jTextField14);
        jTextField14.setBounds(100, 90, 40, 20);

        jLabel35.setText("Height");
        jPanel7.add(jLabel35);
        jLabel35.setBounds(150, 90, 31, 14);

        jLabel38.setForeground(new java.awt.Color(0, 51, 255));
        jLabel38.setText("Deployment Visibility Area");
        jPanel7.add(jLabel38);
        jLabel38.setBounds(10, 70, 160, 14);

        jLabel42.setForeground(new java.awt.Color(51, 51, 255));
        jLabel42.setText("Node Location Distribution");
        jPanel7.add(jLabel42);
        jLabel42.setBounds(10, 130, 170, 14);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Uniform", "Normal", "Constant", "Center", "Grid H-V", "Grid H-V-D", "Opt. Strip", "Opt. Comb", "Opt. Square", "Opt. Triangle", "Grid Delta", "Grid Poisson" }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });
        jPanel7.add(jComboBox2);
        jComboBox2.setBounds(10, 150, 110, 20);

        jTextField15.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField15.setText("5");
        jTextField15.setEnabled(false);
        jPanel7.add(jTextField15);
        jTextField15.setBounds(150, 180, 40, 20);

        jLabel37.setText("Sigma");
        jLabel37.setEnabled(false);
        jPanel7.add(jLabel37);
        jLabel37.setBounds(200, 180, 28, 14);

        jLabel51.setForeground(new java.awt.Color(0, 0, 255));
        jLabel51.setText("Node Energy Distribution (mJoules)");
        jPanel7.add(jLabel51);
        jLabel51.setBounds(10, 210, 170, 14);

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Constant", "Gaussian", "Poisson", "Uniform" }));
        jComboBox6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox6ItemStateChanged(evt);
            }
        });
        jPanel7.add(jComboBox6);
        jComboBox6.setBounds(10, 230, 110, 20);

        jTextField20.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField20.setText("1000");
        jPanel7.add(jTextField20);
        jTextField20.setBounds(130, 250, 40, 20);

        jLabel52.setText("Mean");
        jPanel7.add(jLabel52);
        jLabel52.setBounds(180, 250, 26, 14);

        jTextField21.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField21.setText("5");
        jTextField21.setEnabled(false);
        jPanel7.add(jTextField21);
        jTextField21.setBounds(210, 250, 30, 20);

        jLabel53.setText("Sigma");
        jLabel53.setEnabled(false);
        jPanel7.add(jLabel53);
        jLabel53.setBounds(250, 250, 28, 14);

        jCheckBox22.setText("Sink?");
        jCheckBox22.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox22ItemStateChanged(evt);
            }
        });
        jCheckBox22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox22ActionPerformed(evt);
            }
        });
        jPanel7.add(jCheckBox22);
        jCheckBox22.setBounds(250, 40, 49, 23);

        jTextField23.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField23.setText("300");
        jPanel7.add(jTextField23);
        jTextField23.setBounds(10, 180, 40, 20);

        jTextField24.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField24.setText("300");
        jPanel7.add(jTextField24);
        jTextField24.setBounds(80, 180, 40, 20);

        jLabel10.setText("Y");
        jPanel7.add(jLabel10);
        jLabel10.setBounds(130, 180, 10, 14);

        jLabel55.setText("X");
        jPanel7.add(jLabel55);
        jLabel55.setBounds(60, 180, 10, 14);

        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Black", "Red", "Blue", "Green", "Orange", "Pink", "Yellow" }));
        jComboBox8.setEnabled(false);
        jPanel7.add(jComboBox8);
        jComboBox8.setBounds(300, 40, 70, 20);

        jLabel56.setText("<html>Infrastructure <br> Association</html>");
        jLabel56.setEnabled(false);
        jPanel7.add(jLabel56);
        jLabel56.setBounds(300, 10, 80, 30);

        jButton12.setText("Add");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton12);
        jButton12.setBounds(10, 440, 60, 23);

        jButton13.setText("Remove");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton13);
        jButton13.setBounds(75, 440, 71, 23);

        jLabel57.setForeground(new java.awt.Color(51, 51, 255));
        jLabel57.setText("Node Creation List");
        jPanel7.add(jLabel57);
        jLabel57.setBounds(10, 260, 90, 14);

        jButton5.setText("Manual Pos.");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton5);
        jButton5.setBounds(260, 177, 91, 23);

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Batch Creation", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 255))); // NOI18N
        jPanel12.setLayout(null);

        jTextField16.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField16.setText("1");
        jTextField16.setEnabled(false);
        jPanel12.add(jTextField16);
        jTextField16.setBounds(10, 50, 30, 20);

        jLabel45.setText("# of Topologies");
        jLabel45.setEnabled(false);
        jPanel12.add(jLabel45);
        jLabel45.setBounds(60, 50, 75, 14);

        jTextField17.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField17.setText("0");
        jTextField17.setEnabled(false);
        jPanel12.add(jTextField17);
        jTextField17.setBounds(10, 80, 30, 20);

        jLabel46.setText("Index start in:");
        jLabel46.setEnabled(false);
        jPanel12.add(jLabel46);
        jLabel46.setBounds(70, 80, 69, 14);

        jCheckBox16.setText("Batch Creation");
        jCheckBox16.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox16.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox16.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox16ItemStateChanged(evt);
            }
        });
        jPanel12.add(jCheckBox16);
        jCheckBox16.setBounds(10, 20, 89, 15);
        jPanel12.add(jProgressBar2);
        jProgressBar2.setBounds(10, 130, 130, 14);

        jLabel27.setText("Progress:");
        jPanel12.add(jLabel27);
        jLabel27.setBounds(10, 110, 130, 14);

        jCheckBox24.setSelected(true);
        jCheckBox24.setText("Check Max. Trials");
        jCheckBox24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox24ActionPerformed(evt);
            }
        });
        jPanel12.add(jCheckBox24);
        jCheckBox24.setBounds(10, 150, 120, 23);

        jPanel7.add(jPanel12);
        jPanel12.setBounds(220, 280, 150, 180);

        jTextField25.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField25.setText("1000");
        jTextField25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField25ActionPerformed(evt);
            }
        });
        jPanel7.add(jTextField25);
        jTextField25.setBounds(130, 230, 40, 20);

        jLabel60.setText("Max");
        jPanel7.add(jLabel60);
        jLabel60.setBounds(180, 230, 20, 14);

        jScrollPane7.setViewportView(jList1);

        jPanel7.add(jScrollPane7);
        jScrollPane7.setBounds(10, 280, 200, 60);

        jScrollPane4.setViewportView(jList2);

        jPanel7.add(jScrollPane4);
        jScrollPane4.setBounds(10, 370, 200, 60);

        jLabel61.setForeground(new java.awt.Color(0, 51, 255));
        jLabel61.setText("Sink Creation List");
        jPanel7.add(jLabel61);
        jLabel61.setBounds(10, 350, 100, 14);

        jButton15.setText("Clear");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton15);
        jButton15.setBounds(150, 440, 60, 23);

        jTextField22.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField22.setText("0");
        jPanel7.add(jTextField22);
        jTextField22.setBounds(190, 90, 40, 20);

        jLabel54.setText("Bit Error Rate");
        jPanel7.add(jLabel54);
        jLabel54.setBounds(240, 90, 65, 14);

        jTextField31.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField31.setText("600");
        jPanel7.add(jTextField31);
        jTextField31.setBounds(130, 150, 40, 20);

        jTextField32.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField32.setText("600");
        jPanel7.add(jTextField32);
        jTextField32.setBounds(220, 150, 40, 20);

        jLabel81.setText("Width");
        jPanel7.add(jLabel81);
        jLabel81.setBounds(180, 150, 28, 14);

        jLabel82.setText("Height");
        jPanel7.add(jLabel82);
        jLabel82.setBounds(270, 150, 31, 14);

        jCheckBox8.setText("Move?");
        jPanel7.add(jCheckBox8);
        jCheckBox8.setBounds(310, 145, 57, 23);

        jComboBox13.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Simple", "Mote" }));
        jComboBox13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox13ActionPerformed(evt);
            }
        });
        jPanel7.add(jComboBox13);
        jComboBox13.setBounds(210, 230, 100, 20);

        jLabel96.setText("<html>Energy <br> Model</html>");
        jPanel7.add(jLabel96);
        jLabel96.setBounds(320, 230, 40, 30);

        org.jdesktop.layout.GroupLayout jPanel16Layout = new org.jdesktop.layout.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 391, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Deployment", jPanel16);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Other Parameters", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 255))); // NOI18N
        jPanel8.setEnabled(false);
        jPanel8.setLayout(null);

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Metric Definition", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 255))); // NOI18N

        jLabel26.setText("Sorting mode");

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Metric1 only", "Metric2 only", "Metric1 Primary", "Metric2 Primary", "Linear Combination (W1*Metric1+W2*Metric1)/(W1+W2)" }));
        jComboBox5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox5ItemStateChanged(evt);
            }
        });

        jTextField28.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField28.setText("0.25");

        jLabel79.setText("Candidate Grouping Step");

        jLabel62.setForeground(new java.awt.Color(0, 0, 255));
        jLabel62.setText("Weights");

        jTextField26.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField26.setText("0.5");

        jLabel64.setText("W1");

        jLabel65.setText("W2");

        jTextField27.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField27.setText("0.5");

        jTextField30.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField30.setText("0.20");

        jLabel69.setText("Step");

        jButton14.setText("Set Weights Now");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jCheckBox14.setText("Create all steps");

        org.jdesktop.layout.GroupLayout jPanel19Layout = new org.jdesktop.layout.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel19Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel26)
                    .add(jComboBox5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 230, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel19Layout.createSequentialGroup()
                        .add(jTextField28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(10, 10, 10)
                        .add(jLabel79))
                    .add(jLabel62)
                    .add(jPanel19Layout.createSequentialGroup()
                        .add(jTextField26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(10, 10, 10)
                        .add(jLabel64))
                    .add(jPanel19Layout.createSequentialGroup()
                        .add(jTextField27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(10, 10, 10)
                        .add(jLabel65)
                        .add(18, 18, 18)
                        .add(jCheckBox14))
                    .add(jPanel19Layout.createSequentialGroup()
                        .add(jTextField30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(10, 10, 10)
                        .add(jLabel69)
                        .add(8, 8, 8)
                        .add(jButton14)))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel19Layout.createSequentialGroup()
                .add(jLabel26)
                .add(6, 6, 6)
                .add(jComboBox5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(jPanel19Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jTextField28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel79))
                .add(10, 10, 10)
                .add(jLabel62)
                .add(6, 6, 6)
                .add(jPanel19Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jTextField26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel64))
                .add(10, 10, 10)
                .add(jPanel19Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jTextField27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel19Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel65)
                        .add(jCheckBox14)))
                .add(10, 10, 10)
                .add(jPanel19Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jTextField30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel69)
                    .add(jButton14))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel19);
        jPanel19.setBounds(10, 30, 290, 220);

        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "S-D protocol Interquery time", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 255))); // NOI18N

        jTextField9.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField9.setText("10");

        jLabel63.setText("Interquery time");

        jButton18.setText("Set InterQ Time Now");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel21Layout = new org.jdesktop.layout.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .add(jTextField9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(jLabel63)
                .add(5, 5, 5)
                .add(jButton18)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel21Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jTextField9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel63)
                    .add(jButton18))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel21);
        jPanel21.setBounds(10, 250, 290, 70);

        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "TM Protocols Variables", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 255))); // NOI18N

        jTextField38.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField38.setText("1000");

        jLabel90.setText("Inter-Reset Period");

        jTextField39.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField39.setText("0.9");

        jLabel91.setText("Energy Threshold Step");

        org.jdesktop.layout.GroupLayout jPanel26Layout = new org.jdesktop.layout.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel26Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField39, 0, 0, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField38, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel26Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jLabel91, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel90, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel26Layout.createSequentialGroup()
                .add(jPanel26Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTextField38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel90))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel26Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTextField39, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel91))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel26);
        jPanel26.setBounds(10, 320, 200, 80);

        jPanel27.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Node Mobility parameters", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 255))); // NOI18N

        jTextField40.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField40.setText("20");

        jLabel92.setText("Step size");

        jTextField41.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField41.setText("10");

        jLabel93.setText("Sleep time");

        org.jdesktop.layout.GroupLayout jPanel27Layout = new org.jdesktop.layout.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel27Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField41, 0, 0, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField40, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel27Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel92)
                    .add(jLabel93))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel27Layout.createSequentialGroup()
                .add(jPanel27Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTextField40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel92))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 13, Short.MAX_VALUE)
                .add(jPanel27Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTextField41, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel93)))
        );

        jPanel8.add(jPanel27);
        jPanel27.setBounds(10, 400, 200, 80);

        jPanel28.setBorder(javax.swing.BorderFactory.createTitledBorder("KNeighTree"));

        jLabel95.setText("K variable");

        jTextField42.setText("6");

        org.jdesktop.layout.GroupLayout jPanel28Layout = new org.jdesktop.layout.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel28Layout.createSequentialGroup()
                .add(jTextField42, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 42, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel95)
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel28Layout.createSequentialGroup()
                .add(jPanel28Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTextField42, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel95))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel28);
        jPanel28.setBounds(210, 400, 170, 80);

        jPanel29.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Alpha Coverage", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 255))); // NOI18N

        jTextField43.setText("1.0");

        jLabel97.setText("Ini");

        jTextField44.setText("1.0");

        jLabel98.setText("Fin");

        jTextField45.setText("0.1");

        jLabel99.setText("Step");

        jCheckBox29.setText("All alphas?");

        org.jdesktop.layout.GroupLayout jPanel29Layout = new org.jdesktop.layout.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel29Layout.createSequentialGroup()
                .add(jPanel29Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField44, 0, 0, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField43, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel29Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel98)
                    .add(jLabel97))
                .add(10, 10, 10)
                .add(jPanel29Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jCheckBox29)
                    .add(jPanel29Layout.createSequentialGroup()
                        .add(jTextField45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel99)))
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel29Layout.createSequentialGroup()
                .add(jPanel29Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTextField43, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel97)
                    .add(jCheckBox29))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 6, Short.MAX_VALUE)
                .add(jPanel29Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTextField44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel98)
                    .add(jTextField45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel99))
                .addContainerGap())
        );

        jPanel8.add(jPanel29);
        jPanel29.setBounds(220, 320, 156, 80);

        org.jdesktop.layout.GroupLayout jPanel20Layout = new org.jdesktop.layout.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 391, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel20Layout.createSequentialGroup()
                .add(jPanel8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Other Params.", jPanel20);

        jButton3.setText("Create Deployment");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel36.setForeground(new java.awt.Color(255, 0, 0));
        jLabel36.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jCheckBox7.setSelected(true);
        jCheckBox7.setText("Check Connectivity Exhaustively");
        jCheckBox7.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox7.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jButton26.setText("Show Graph");
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel15Layout = new org.jdesktop.layout.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel15Layout.createSequentialGroup()
                        .add(jButton3)
                        .add(jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel15Layout.createSequentialGroup()
                                .add(190, 190, 190)
                                .add(jLabel36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 290, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel15Layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jCheckBox7)
                                .add(38, 38, 38)
                                .add(jButton26))))
                    .add(jTabbedPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 431, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jTabbedPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 523, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel15Layout.createSequentialGroup()
                        .add(7, 7, 7)
                        .add(jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jButton3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jCheckBox7)))
                    .add(jPanel15Layout.createSequentialGroup()
                        .add(18, 18, 18)
                        .add(jButton26)))
                .add(502, 502, 502)
                .add(jLabel36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(117, 117, 117))
        );

        jTabbedPane1.addTab("Deployment Options", jPanel15);

        jPanel3.setPreferredSize(new java.awt.Dimension(350, 350));
        jPanel3.setLayout(null);

        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select Protocol", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 255))); // NOI18N
        jPanel24.setLayout(null);

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No TC Protocol", "A3", "EECDS", "CDS Rule K", "SimpleTree", "Just Tree", "KNEIGH-Tree", "Energy Efficient Distributed", "Leach Energy Based Protocol" }));
        jComboBox7.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox7ItemStateChanged(evt);
            }
        });
        jPanel24.add(jComboBox7);
        jComboBox7.setBounds(10, 40, 210, 20);
        jPanel24.add(jLabel2);
        jLabel2.setBounds(0, 0, 0, 0);

        jLabel66.setForeground(new java.awt.Color(0, 0, 255));
        jLabel66.setText("TC protocol");
        jPanel24.add(jLabel66);
        jLabel66.setBounds(30, 20, 70, 14);

        jComboBox10.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No TM protocol", "Non Isolated Sink", "DGTTRec", "SGTTRot", "HGTTRecRot", "Energy Local Patching DSR", "DGETRec", "SGETRot", "HGETRecRot" }));
        jComboBox10.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox10ItemStateChanged(evt);
            }
        });
        jPanel24.add(jComboBox10);
        jComboBox10.setBounds(10, 80, 210, 20);

        jLabel67.setForeground(new java.awt.Color(0, 0, 255));
        jLabel67.setText("TM Protocol");
        jPanel24.add(jLabel67);
        jLabel67.setBounds(30, 65, 70, 14);

        jComboBox11.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No S&D protocol", "Simple S&D protocol" }));
        jComboBox11.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox11ItemStateChanged(evt);
            }
        });
        jPanel24.add(jComboBox11);
        jComboBox11.setBounds(230, 40, 210, 20);

        jLabel68.setForeground(new java.awt.Color(0, 0, 255));
        jLabel68.setText("Sensor & Data protocol");
        jPanel24.add(jLabel68);
        jLabel68.setBounds(230, 20, 150, 14);

        jCheckBox13.setText("<html>All sinks at once? <br>(if multiple infrastructure selected)");
        jCheckBox13.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox13.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox13.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox13ItemStateChanged(evt);
            }
        });
        jPanel24.add(jCheckBox13);
        jCheckBox13.setBounds(10, 110, 190, 29);

        jLabel86.setForeground(new java.awt.Color(0, 0, 255));
        jLabel86.setText("Routing / Forwarding Protocol");
        jPanel24.add(jLabel86);
        jLabel86.setBounds(230, 60, 200, 14);

        jComboBox9.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No Routing/Forwarding Protocol", "Simple Forwarding" }));
        jComboBox9.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox9ItemStateChanged(evt);
            }
        });
        jPanel24.add(jComboBox9);
        jComboBox9.setBounds(230, 80, 210, 20);

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No Mobility", "Simple Random Walk Mobility" }));
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });
        jPanel24.add(jComboBox3);
        jComboBox3.setBounds(230, 120, 210, 20);

        jLabel94.setForeground(new java.awt.Color(0, 0, 255));
        jLabel94.setText("Node Mobility");
        jPanel24.add(jLabel94);
        jLabel94.setBounds(230, 100, 120, 14);

        jPanel3.add(jPanel24);
        jPanel24.setBounds(0, 0, 450, 150);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Actions / Events", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 255))); // NOI18N
        jPanel5.setPreferredSize(new java.awt.Dimension(400, 160));
        jPanel5.setLayout(null);

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12));
        jButton1.setText("<html> Start Atarraya!");
        jButton1.setToolTipText("Start Simulation");
        jButton1.setActionCommand("Start Atarraya!");
        jButton1.setEnabled(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton1);
        jButton1.setBounds(10, 20, 130, 70);

        jButton2.setText("Pause");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton2);
        jButton2.setBounds(145, 60, 70, 30);

        jLabel33.setForeground(new java.awt.Color(0, 0, 255));
        jLabel33.setText("Events: ");
        jPanel5.add(jLabel33);
        jLabel33.setBounds(10, 120, 220, 14);

        jCheckBox20.setText("Show Events");
        jCheckBox20.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox20.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox20.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox20ItemStateChanged(evt);
            }
        });
        jPanel5.add(jCheckBox20);
        jCheckBox20.setBounds(150, 420, 79, 15);

        jButton11.setText("Clear events");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton11);
        jButton11.setBounds(10, 420, 93, 20);

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Batch Simulation", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 255))); // NOI18N
        jPanel14.setLayout(null);

        jButton7.setText("Select Files");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel14.add(jButton7);
        jButton7.setBounds(10, 20, 90, 30);

        jLabel47.setText("0 files selected");
        jPanel14.add(jLabel47);
        jLabel47.setBounds(10, 60, 80, 14);

        jTextField19.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField19.setText("1");
        jTextField19.setToolTipText("Multiplicity");
        jPanel14.add(jTextField19);
        jTextField19.setBounds(120, 60, 20, 20);

        jLabel49.setText("X           Replicates");
        jPanel14.add(jLabel49);
        jLabel49.setBounds(110, 60, 90, 20);

        jLabel50.setText("Progress");
        jPanel14.add(jLabel50);
        jLabel50.setBounds(10, 80, 200, 14);
        jPanel14.add(jProgressBar1);
        jProgressBar1.setBounds(10, 100, 190, 14);

        jButton8.setText("Clear Files");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel14.add(jButton8);
        jButton8.setBounds(110, 20, 90, 30);

        jPanel5.add(jPanel14);
        jPanel14.setBounds(220, 10, 220, 130);

        jCheckBox6.setText("Live stats");
        jCheckBox6.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox6.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox6ItemStateChanged(evt);
            }
        });
        jPanel5.add(jCheckBox6);
        jCheckBox6.setBounds(50, 160, 70, 15);

        jScrollPane5.setViewportView(jTextPane2);

        jPanel5.add(jScrollPane5);
        jScrollPane5.setBounds(10, 180, 220, 230);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(2);
        jScrollPane1.setViewportView(jTextArea1);

        jPanel5.add(jScrollPane1);
        jScrollPane1.setBounds(240, 390, 200, 50);

        jScrollPane6.setViewportView(jTextPane3);

        jPanel5.add(jScrollPane6);
        jScrollPane6.setBounds(240, 350, 200, 30);

        jLabel59.setForeground(new java.awt.Color(0, 0, 255));
        jLabel59.setText("Stats");
        jPanel5.add(jLabel59);
        jLabel59.setBounds(10, 160, 25, 14);

        jCheckBox11.setText("Covered Area too?");
        jCheckBox11.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox11.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox11.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox11ItemStateChanged(evt);
            }
        });
        jPanel5.add(jCheckBox11);
        jCheckBox11.setBounds(120, 160, 109, 15);

        jButton6.setText("Stop");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton6);
        jButton6.setBounds(145, 20, 70, 30);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Stats Repot", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 255))); // NOI18N
        jPanel11.setLayout(null);

        jCheckBox17.setText("Individual Report");
        jCheckBox17.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox17.setEnabled(false);
        jCheckBox17.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox17.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox17ItemStateChanged(evt);
            }
        });
        jPanel11.add(jCheckBox17);
        jCheckBox17.setBounds(20, 60, 99, 15);

        jCheckBox18.setSelected(true);
        jCheckBox18.setText("Summarized Report");
        jCheckBox18.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox18.setEnabled(false);
        jCheckBox18.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox18.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox18ItemStateChanged(evt);
            }
        });
        jPanel11.add(jCheckBox18);
        jCheckBox18.setBounds(20, 80, 111, 15);

        jCheckBox19.setSelected(true);
        jCheckBox19.setText("Excel ready format (CSV)");
        jCheckBox19.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox19.setEnabled(false);
        jCheckBox19.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox19.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox19ItemStateChanged(evt);
            }
        });
        jPanel11.add(jCheckBox19);
        jCheckBox19.setBounds(20, 40, 139, 15);

        jLabel48.setText("Description");
        jPanel11.add(jLabel48);
        jLabel48.setBounds(140, 160, 53, 14);
        jPanel11.add(jTextField18);
        jTextField18.setBounds(10, 160, 120, 20);

        jCheckBox9.setText("Save Lifetime log (Summ. Only)");
        jCheckBox9.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox9.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox9.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox9ItemStateChanged(evt);
            }
        });
        jPanel11.add(jCheckBox9);
        jCheckBox9.setBounds(10, 120, 167, 15);

        jCheckBox12.setText("Save events log (indiv only)");
        jCheckBox12.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox12.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox12.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox12ItemStateChanged(evt);
            }
        });
        jPanel11.add(jCheckBox12);
        jCheckBox12.setBounds(10, 100, 160, 15);

        jCheckBox15.setText("Save Stats");
        jCheckBox15.setAlignmentY(0.0F);
        jCheckBox15.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox15.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox15ItemStateChanged(evt);
            }
        });
        jPanel11.add(jCheckBox15);
        jCheckBox15.setBounds(10, 20, 73, 19);

        jCheckBox23.setText("All?");
        jCheckBox23.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox23.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox23ItemStateChanged(evt);
            }
        });
        jPanel11.add(jCheckBox23);
        jCheckBox23.setBounds(90, 20, 40, 19);

        jTextField3.setText("20");
        jTextField3.setEnabled(false);
        jPanel11.add(jTextField3);
        jTextField3.setBounds(160, 20, 40, 20);

        jLabel11.setText("Step");
        jPanel11.add(jLabel11);
        jLabel11.setBounds(130, 22, 22, 14);

        jCheckBox28.setText("Save Solutions");
        jCheckBox28.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox28ItemStateChanged(evt);
            }
        });
        jPanel11.add(jCheckBox28);
        jCheckBox28.setBounds(6, 135, 130, 23);

        jPanel5.add(jPanel11);
        jPanel11.setBounds(230, 140, 210, 190);

        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setText("Simulator Messages");
        jPanel5.add(jLabel1);
        jLabel1.setBounds(240, 330, 100, 14);

        jLabel43.setText("Queue Size: 0");
        jPanel5.add(jLabel43);
        jLabel43.setBounds(10, 140, 150, 14);

        jTextField12.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField12.setText("3000000");
        jPanel5.add(jTextField12);
        jTextField12.setBounds(145, 100, 70, 20);

        jLabel44.setForeground(new java.awt.Color(0, 0, 255));
        jLabel44.setText("Maximum Simulation Time");
        jPanel5.add(jLabel44);
        jLabel44.setBounds(10, 100, 120, 14);

        jPanel3.add(jPanel5);
        jPanel5.setBounds(0, 150, 450, 450);

        jTabbedPane1.addTab("Atarraya", jPanel3);

        jPanel25.setLayout(null);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Deployment Info", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 255))); // NOI18N
        jPanel6.setLayout(null);

        jLabel6.setText("100");
        jPanel6.add(jLabel6);
        jLabel6.setBounds(10, 30, 18, 14);

        jLabel29.setText("# of sinks");
        jPanel6.add(jLabel29);
        jLabel29.setBounds(70, 90, 47, 14);

        jLabel7.setText("100");
        jPanel6.add(jLabel7);
        jLabel7.setBounds(10, 50, 30, 14);

        jLabel8.setText("# of Nodes");
        jPanel6.add(jLabel8);
        jLabel8.setBounds(70, 30, 54, 14);

        jLabel9.setText("Comm Radius");
        jPanel6.add(jLabel9);
        jLabel9.setBounds(70, 50, 70, 14);

        jLabel28.setText("1");
        jPanel6.add(jLabel28);
        jLabel28.setBounds(10, 90, 6, 14);
        jPanel6.add(jLabel24);
        jLabel24.setBounds(10, 110, 40, 15);

        jLabel25.setText("Avg # of Ngb");
        jPanel6.add(jLabel25);
        jLabel25.setBounds(70, 110, 70, 14);

        jLabel100.setText("20");
        jPanel6.add(jLabel100);
        jLabel100.setBounds(10, 70, 12, 14);

        jLabel101.setText("Sensing Radius");
        jPanel6.add(jLabel101);
        jLabel101.setBounds(70, 70, 80, 14);

        jPanel25.add(jPanel6);
        jPanel6.setBounds(230, 90, 160, 150);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Visual Options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 255))); // NOI18N
        jPanel4.setPreferredSize(new java.awt.Dimension(160, 160));
        jPanel4.setLayout(null);

        jCheckBox1.setText("Atarraya");
        jCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jPanel4.add(jCheckBox1);
        jCheckBox1.setBounds(20, 20, 70, 15);

        jCheckBox2.setText("Area Comm");
        jCheckBox2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });
        jPanel4.add(jCheckBox2);
        jCheckBox2.setBounds(110, 20, 90, 15);

        jCheckBox3.setText("Active Nodes");
        jCheckBox3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox3ItemStateChanged(evt);
            }
        });
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });
        jPanel4.add(jCheckBox3);
        jCheckBox3.setBounds(20, 40, 81, 15);

        jCheckBox4.setText("Filled Area");
        jCheckBox4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox4.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });
        jPanel4.add(jCheckBox4);
        jCheckBox4.setBounds(110, 60, 70, 15);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All", "Black", "Red", "Blue", "Green", "Orange", "Pink", "Yellow" }));
        jComboBox1.setEnabled(false);
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jPanel4.add(jComboBox1);
        jComboBox1.setBounds(120, 150, 61, 20);

        jLabel5.setText("<html>\nVirtual Network <br> Infrastructure ID");
        jPanel4.add(jLabel5);
        jLabel5.setBounds(20, 140, 100, 30);

        jCheckBox5.setText("Node Id");
        jCheckBox5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox5.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox5ItemStateChanged(evt);
            }
        });
        jPanel4.add(jCheckBox5);
        jCheckBox5.setBounds(20, 60, 55, 15);

        jButton10.setText("Update View");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton10);
        jButton10.setBounds(20, 90, 110, 20);

        jCheckBox21.setText("Area Sensing");
        jCheckBox21.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox21.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox21ActionPerformed(evt);
            }
        });
        jPanel4.add(jCheckBox21);
        jCheckBox21.setBounds(110, 40, 90, 15);

        jCheckBox10.setText("Active VNI");
        jCheckBox10.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox10ItemStateChanged(evt);
            }
        });
        jCheckBox10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox10ActionPerformed(evt);
            }
        });
        jPanel4.add(jCheckBox10);
        jCheckBox10.setBounds(20, 170, 140, 23);

        jLabel89.setForeground(new java.awt.Color(0, 0, 255));
        jLabel89.setText("View VNI");
        jPanel4.add(jLabel89);
        jLabel89.setBounds(20, 120, 130, 14);

        jCheckBox26.setText("All Gateways");
        jCheckBox26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox26ActionPerformed(evt);
            }
        });
        jPanel4.add(jCheckBox26);
        jCheckBox26.setBounds(20, 190, 130, 23);

        jPanel25.add(jPanel4);
        jPanel4.setBounds(20, 20, 210, 220);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Grid Coverage", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 255))); // NOI18N
        jPanel9.setLayout(null);

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Square", "Diagonal" }));
        jComboBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox4ItemStateChanged(evt);
            }
        });
        jPanel9.add(jComboBox4);
        jComboBox4.setBounds(80, 30, 66, 20);

        jLabel17.setText("View Grid");
        jPanel9.add(jLabel17);
        jLabel17.setBounds(10, 30, 60, 14);

        jPanel25.add(jPanel9);
        jPanel9.setBounds(230, 20, 160, 70);

        jPanel30.setBorder(javax.swing.BorderFactory.createTitledBorder("Other variables"));

        jLabel102.setText("Max Energy");

        jTextField46.setText("1000");

        jButton25.setText("Update");
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel30Layout = new org.jdesktop.layout.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel30Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel30Layout.createSequentialGroup()
                        .add(jTextField46, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel102)
                        .add(25, 25, 25))
                    .add(jPanel30Layout.createSequentialGroup()
                        .add(jButton25)
                        .addContainerGap(71, Short.MAX_VALUE))))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel30Layout.createSequentialGroup()
                .add(jPanel30Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel102)
                    .add(jTextField46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton25)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel25.add(jPanel30);
        jPanel30.setBounds(230, 240, 160, 80);

        jTabbedPane1.addTab("Visualization", jPanel25);

        jScrollPane2.setViewportView(jTextPane1);

        jButton4.setText("Generate Stats");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel40.setText("Node ID");

        jLabel41.setForeground(new java.awt.Color(51, 51, 255));
        jLabel41.setText("Node Info");

        jButton9.setText("Switch Node State");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel10Layout = new org.jdesktop.layout.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel10Layout.createSequentialGroup()
                        .add(jButton4)
                        .add(20, 20, 20)
                        .add(jTextField8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(12, 12, 12)
                        .add(jLabel40))
                    .add(jLabel41)
                    .add(jButton9)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 491, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton4)
                    .add(jTextField8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel40))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel41)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 445, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jButton9)
                .addContainerGap(68, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Node Stats", jPanel10);

        jPanel23.setLayout(null);

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane3.setViewportView(jTextArea2);

        jPanel23.add(jScrollPane3);
        jScrollPane3.setBounds(10, 30, 360, 160);

        jLabel12.setForeground(new java.awt.Color(0, 0, 204));
        jLabel12.setText("Stats log");
        jPanel23.add(jLabel12);
        jLabel12.setBounds(10, 10, 42, 14);

        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Lifetime Report Analysis", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 255))); // NOI18N

        jTextField4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField4.setText("500");

        jLabel13.setText("Time step");

        jButton21.setText("Transform Report");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel22Layout = new org.jdesktop.layout.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .add(jButton21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 139, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jTextField4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel13)
                .addContainerGap(91, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel22Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 42, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel13)
                    .add(jTextField4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel23.add(jPanel22);
        jPanel22.setBounds(10, 210, 360, 90);

        jTabbedPane1.addTab("Report", jPanel23);

        jPanel13.setLayout(null);

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CTR Dense Topologies", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 255))); // NOI18N
        jPanel17.setLayout(null);

        jTextField6.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField6.setText("100");
        jPanel17.add(jTextField6);
        jTextField6.setBounds(10, 50, 50, 20);

        jLabel30.setText("Num Nodes");
        jPanel17.add(jLabel30);
        jLabel30.setBounds(70, 50, 60, 14);

        jLabel32.setText("0");
        jPanel17.add(jLabel32);
        jLabel32.setBounds(260, 70, 100, 14);

        jLabel58.setText("CTR Penrose-Santi");
        jPanel17.add(jLabel58);
        jLabel58.setBounds(260, 50, 100, 14);

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Dense");
        jPanel17.add(jRadioButton1);
        jRadioButton1.setBounds(300, 20, 80, 23);

        jTextField10.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField10.setText("0.01");
        jPanel17.add(jTextField10);
        jTextField10.setBounds(140, 80, 40, 20);

        jLabel76.setText("CTR Step");
        jPanel17.add(jLabel76);
        jLabel76.setBounds(190, 80, 60, 14);

        jLabel77.setText("Current:");
        jPanel17.add(jLabel77);
        jLabel77.setBounds(10, 120, 230, 14);

        jTextField29.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField29.setText("0.01");
        jPanel17.add(jTextField29);
        jTextField29.setBounds(140, 50, 40, 20);

        jLabel78.setText("Initial CTR");
        jPanel17.add(jLabel78);
        jLabel78.setBounds(190, 50, 60, 14);

        jLabel75.setText("CTR Santi and Blough");
        jPanel17.add(jLabel75);
        jLabel75.setBounds(260, 90, 110, 14);

        jLabel80.setText("0");
        jPanel17.add(jLabel80);
        jLabel80.setBounds(260, 110, 100, 14);

        jPanel13.add(jPanel17);
        jPanel17.setBounds(10, 10, 390, 150);

        jButton19.setText("MST");
        jButton19.setToolTipText("<html>Generates the Minimal Spanning Tree of the graph.<br>It also provides the total sum of edges of the MST.</html>");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });
        jPanel13.add(jButton19);
        jButton19.setBounds(20, 500, 90, 30);

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CTR Sparse Topologies - Experiment 3 for 2 dimensions", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 255))); // NOI18N
        jPanel18.setLayout(null);

        jLabel71.setText("CTR Pensore-Santi");
        jPanel18.add(jLabel71);
        jLabel71.setBounds(260, 50, 110, 14);

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Sparse");
        jPanel18.add(jRadioButton2);
        jRadioButton2.setBounds(300, 10, 80, 20);

        jLabel70.setText("0");
        jPanel18.add(jLabel70);
        jLabel70.setBounds(260, 70, 100, 14);

        jLabel19.setForeground(new java.awt.Color(0, 0, 255));
        jLabel19.setText("L - Area Side");
        jPanel18.add(jLabel19);
        jLabel19.setBounds(10, 30, 80, 14);

        jLabel20.setText("L=2^(2*i)");
        jPanel18.add(jLabel20);
        jLabel20.setBounds(10, 110, 130, 14);

        jTextField33.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField33.setText("4");
        jPanel18.add(jTextField33);
        jTextField33.setBounds(10, 50, 30, 20);

        jLabel21.setText("Initial i");
        jPanel18.add(jLabel21);
        jLabel21.setBounds(50, 50, 31, 14);

        jTextField34.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField34.setText("10");
        jPanel18.add(jTextField34);
        jTextField34.setBounds(10, 80, 30, 20);

        jLabel22.setText("Final i");
        jPanel18.add(jLabel22);
        jLabel22.setBounds(50, 80, 27, 14);

        jLabel23.setText("n = sqrt(l)");
        jPanel18.add(jLabel23);
        jLabel23.setBounds(10, 130, 130, 14);

        jLabel39.setText("r = k*l^(3/4)*sqrt(log2(l))");
        jPanel18.add(jLabel39);
        jLabel39.setBounds(10, 150, 230, 14);

        jLabel72.setForeground(new java.awt.Color(0, 0, 255));
        jLabel72.setText("K parameter");
        jPanel18.add(jLabel72);
        jLabel72.setBounds(140, 30, 70, 14);

        jTextField35.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField35.setText("0.5");
        jPanel18.add(jTextField35);
        jTextField35.setBounds(140, 50, 50, 20);

        jLabel73.setText("Initial K");
        jPanel18.add(jLabel73);
        jLabel73.setBounds(210, 50, 35, 14);

        jTextField36.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField36.setText("1");
        jPanel18.add(jTextField36);
        jTextField36.setBounds(140, 80, 50, 20);

        jLabel83.setText("Final K");
        jPanel18.add(jLabel83);
        jLabel83.setBounds(210, 80, 31, 14);

        jTextField37.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField37.setText("0.1");
        jPanel18.add(jTextField37);
        jTextField37.setBounds(140, 110, 50, 20);

        jLabel84.setText("Step");
        jPanel18.add(jLabel84);
        jLabel84.setBounds(210, 110, 22, 14);

        jLabel87.setText("CTR Santi and Blough");
        jPanel18.add(jLabel87);
        jLabel87.setBounds(260, 90, 120, 14);

        jLabel88.setText("0");
        jPanel18.add(jLabel88);
        jLabel88.setBounds(260, 110, 110, 14);

        jPanel13.add(jPanel18);
        jPanel18.setBounds(10, 160, 390, 180);

        jButton16.setText("Giant Component");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        jPanel13.add(jButton16);
        jButton16.setBounds(10, 350, 120, 40);
        jPanel13.add(jLabel18);
        jLabel18.setBounds(20, 440, 380, 20);

        jTextField5.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField5.setText("100");
        jPanel13.add(jTextField5);
        jTextField5.setBounds(140, 360, 50, 20);

        jLabel14.setText("Num Topologies");
        jPanel13.add(jLabel14);
        jLabel14.setBounds(200, 360, 90, 14);

        jButton17.setText("Update CTR");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });
        jPanel13.add(jButton17);
        jButton17.setBounds(300, 350, 100, 40);

        jButton20.setText("Save Neighborhoods");
        jButton20.setToolTipText("<html>Saves the neighborhoods of all the nodes in the topology. <br>This information can be useful for solving optimization problems with linear programming.</html>");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });
        jPanel13.add(jButton20);
        jButton20.setBounds(270, 470, 140, 30);

        jLabel31.setForeground(new java.awt.Color(0, 0, 204));
        jLabel31.setText("Other options");
        jPanel13.add(jLabel31);
        jLabel31.setBounds(20, 470, 100, 14);

        jLabel85.setText("Sum of Edges: ");
        jPanel13.add(jLabel85);
        jLabel85.setBounds(130, 510, 120, 14);

        jLabel16.setForeground(new java.awt.Color(0, 0, 255));
        jLabel16.setText("Progress");
        jPanel13.add(jLabel16);
        jLabel16.setBounds(20, 420, 90, 14);

        jCheckBox25.setText("Node Degree Ratio");
        jPanel13.add(jCheckBox25);
        jCheckBox25.setBounds(10, 390, 120, 23);

        jTextField11.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField11.setText("100");
        jPanel13.add(jTextField11);
        jTextField11.setBounds(140, 390, 50, 20);

        jLabel74.setText("Area Side");
        jPanel13.add(jLabel74);
        jLabel74.setBounds(200, 390, 60, 14);

        jButton22.setText("BFS");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });
        jPanel13.add(jButton22);
        jButton22.setBounds(270, 500, 140, 30);

        jButton23.setText("Save positions");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });
        jPanel13.add(jButton23);
        jButton23.setBounds(270, 530, 140, 30);

        jButton24.setText("Solve CDS");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });
        jPanel13.add(jButton24);
        jButton24.setBounds(20, 530, 90, 30);

        jCheckBox27.setText("Just 1 minimum?");
        jPanel13.add(jCheckBox27);
        jCheckBox27.setBounds(120, 535, 140, 23);

        jTabbedPane1.addTab("TC Theoretical", jPanel13);

        jButton28.setText("Choose Base Station");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        jButton29.setText("Select for Graph");
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        jButton27.setText("Total Energy Spent for Clustering");
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        jButton30.setText("Total Energy Spent for Data Transfer");
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        jButton31.setText("Energy Spent by Each Node");
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });

        jLabel103.setText("Result Analysis:");

        jButton32.setText("Dead Nodes");
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel31Layout = new org.jdesktop.layout.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel31Layout.createSequentialGroup()
                .add(jPanel31Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel31Layout.createSequentialGroup()
                        .add(75, 75, 75)
                        .add(jButton28, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                        .add(9, 9, 9))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel31Layout.createSequentialGroup()
                        .add(84, 84, 84)
                        .add(jPanel31Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jButton32, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jButton29, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                            .add(jButton31, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jButton30, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                            .add(jButton27, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                            .add(jLabel103))))
                .add(67, 67, 67))
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel31Layout.createSequentialGroup()
                .add(56, 56, 56)
                .add(jButton28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(64, 64, 64)
                .add(jLabel103)
                .add(37, 37, 37)
                .add(jButton29, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 41, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jButton27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 43, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jButton30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 43, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jButton31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 43, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(30, 30, 30)
                .add(jButton32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(98, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Topology Creation", jPanel31);

        jSplitPane1.setRightComponent(jTabbedPane1);

        jPanel1.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jMenu2.setText("Menu");

        jMenuItem2.setText("Save Deployment");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem1.setText("Load Deployment");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuItem5.setText("Reset Topology");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);
        jMenu2.add(jSeparator1);

        jMenuItem4.setText("Save deployment as jpeg");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuBar2.add(jMenu2);

        jMenu1.setText("Help");

        jMenuItem3.setText("About Atarraya");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar2.add(jMenu1);

        setJMenuBar(jMenuBar2);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 1001, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 653, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    public void update_ctr(){
        
        num_nodes_ctr=100;
        area_side_ctr=100;
        comm_radius_ctr=13;
        initial_ctr = 0.01;
        step_ctr = 0.01;
        
        initial_k_ctr= 0.5;
        final_k_ctr= 1;
        step_k_ctr= 0.1;
        initial_i_exp_l_ctr= 4;
        final_i_exp_l_ctr= 10;
        
        double dn=0;
        double dr=0;
        double dl=0;
        
        try{
            num_nodes_ctr = Integer.parseInt(jTextField6.getText());
            area_side_ctr = Integer.parseInt(jTextField11.getText());
            num_topologies_ctr = Integer.parseInt(jTextField5.getText());
            initial_ctr = Double.parseDouble(jTextField29.getText());
            step_ctr = Double.parseDouble(jTextField10.getText());
            
            initial_k_ctr= Double.parseDouble(jTextField35.getText());
            final_k_ctr= Double.parseDouble(jTextField36.getText());
            step_k_ctr= Double.parseDouble(jTextField37.getText());
            initial_i_exp_l_ctr= Integer.parseInt(jTextField33.getText());
            final_i_exp_l_ctr= Integer.parseInt(jTextField34.getText());
            
            
        }catch(Exception ex){ex.printStackTrace();}
        
        //CTR Dense Penrose - Santi
        ctr = java.lang.Math.sqrt((java.lang.Math.log(num_nodes_ctr)+java.lang.Math.log(java.lang.Math.log(num_nodes_ctr)))/(num_nodes_ctr*java.lang.Math.PI))*area_side_ctr;
        jLabel32.setText(""+ctr);
        
        ctr =  initial_k_ctr*java.lang.Math.pow(area_side_ctr,0.75)*java.lang.Math.sqrt(java.lang.Math.log(area_side_ctr)/java.lang.Math.log(2));
        jLabel80.setText(""+ctr);
        
        if(jRadioButton1.isSelected())
            comm_radius_ctr = (int)java.lang.Math.ceil(ctr);
     
        
        //CTR Santi-Blough
        
        //dl = java.lang.Math.pow(2,2*initial_i_exp_l_ctr);
        dl = area_side_ctr;
        //jLabel20.setText("L=2^(2*i) = "+dl);
        jLabel20.setText("L = "+dl);
        dn = java.lang.Math.sqrt(dl);
        jLabel23.setText("n = sqrt(l) = "+dn);
        ctr =  initial_k_ctr*java.lang.Math.pow(dl,0.75)*java.lang.Math.sqrt(java.lang.Math.log(dl)/java.lang.Math.log(2));
        jLabel39.setText("r = k*l^(3/4)*sqrt(log2(l)) = "+ctr);
        //ctr = java.lang.Math.pow(initial_k_ctr,2)*java.lang.Math.pow(dl, 2)*(java.lang.Math.log(dl)/java.lang.Math.log(2))/dn;
        jLabel88.setText(""+ctr);
        
        ctr = java.lang.Math.sqrt((java.lang.Math.log(dn)+java.lang.Math.log(java.lang.Math.log(dn)))/(dn*java.lang.Math.PI))*dl;
        jLabel70.setText(""+ctr);
        
        if(jRadioButton2.isSelected())
            comm_radius_ctr = (int)java.lang.Math.ceil(ctr);
        
    }
    
    private void jCheckBox11ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox11ItemStateChanged
// TODO add your handling code here:
        
        view_covered_area_stat = jCheckBox11.isSelected();
        //CountNotVisited();
        
    }//GEN-LAST:event_jCheckBox11ItemStateChanged

    private void jCheckBox21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox21ActionPerformed
// TODO add your handling code here:
        myPanel.setRangeArea(jCheckBox2.isSelected(),jCheckBox21.isSelected());
        
    }//GEN-LAST:event_jCheckBox21ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
// TODO add your handling code here:
        
        jTextArea1.setText("");
        
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
// TODO add your handling code here:
      
        myPanel.repaint();
        
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jCheckBox20ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox20ItemStateChanged
// TODO add your handling code here:
        
        show_events = jCheckBox20.isSelected();
        
    }//GEN-LAST:event_jCheckBox20ItemStateChanged

    private void jComboBox7ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox7ItemStateChanged
// TODO add your handling code here:
        
        selected_TCprotocol = jComboBox7.getSelectedIndex();
        
        switch(selected_TCprotocol){
        
            case PROTOCOL_NO_SELECTED:
                    jButton1.setEnabled(false);
                    jButton8.setEnabled(false);
                    TC_Selected = false;
                    break;
            
            default:
                    jButton1.setEnabled(true);
                    jButton8.setEnabled(true);
                    TC_Selected = true;
                    break;
                   
        }
        
    }//GEN-LAST:event_jComboBox7ItemStateChanged

    private void jComboBox6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox6ItemStateChanged
// TODO add your handling code here:
        
        energy_distribution = jComboBox6.getSelectedIndex();
        
        switch(energy_distribution){
        
            case ENERGY_CONSTANT:
                    jLabel53.setText("Value");
                    jTextField21.setEnabled(false);
                    jLabel53.setEnabled(false);      
                    break;

            case ENERGY_NORMAL:
                    jLabel52.setText("Mean");
                    jTextField21.setEnabled(true);
                    jLabel53.setEnabled(true);
                    try{
                    sigma_e = Double.parseDouble(jTextField21.getText());
                    }catch(Exception ex){ex.printStackTrace();}       
                            
                    break;
            
            case ENERGY_POISSON:
                    jLabel53.setText("Lambda");
                    jTextField21.setEnabled(true);
                    jLabel53.setEnabled(true);
                    break;
                        
            case ENERGY_UNIFORM:
                    jLabel53.setText("Max Value");
                    jTextField21.setEnabled(false);
                    jLabel53.setEnabled(false);
                    break;
                    
        }
        
    }//GEN-LAST:event_jComboBox6ItemStateChanged

    private void jCheckBox19ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox19ItemStateChanged
// TODO add your handling code here:
        
         excel_ready_reports = jCheckBox19.isSelected();
        
    }//GEN-LAST:event_jCheckBox19ItemStateChanged

    private void jCheckBox18ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox18ItemStateChanged
// TODO add your handling code here:
        
        sumarized_reports = jCheckBox18.isSelected();
        
        if(sumarized_reports){
            indiv_reports  = false;
            jCheckBox17.setSelected(false);
            jTextField18.setEnabled(true);
            jLabel48.setEnabled(true);
        }else{
            indiv_reports  = true;
            jCheckBox17.setSelected(true);
            jTextField18.setEnabled(false);
            jLabel48.setEnabled(false);
        
        }
        
    }//GEN-LAST:event_jCheckBox18ItemStateChanged

    private void jCheckBox17ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox17ItemStateChanged
// TODO add your handling code here:
        
        indiv_reports = jCheckBox17.isSelected();
        if(indiv_reports){
            sumarized_reports = false;
            jCheckBox18.setSelected(false);
            jTextField18.setEnabled(false);
            jLabel48.setEnabled(false);
        }else{
            sumarized_reports  = true;
            jCheckBox18.setSelected(true);
            jTextField18.setEnabled(true);
            jLabel48.setEnabled(true);
        }
        
    }//GEN-LAST:event_jCheckBox17ItemStateChanged

    
    
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
// TODO add your handling code here:
        batchSimulationSelectFiles();
    }//GEN-LAST:event_jButton7ActionPerformed

    
    
    private void jCheckBox16ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox16ItemStateChanged
// TODO add your handling code here:
        
        batch_creation = jCheckBox16.isSelected();
        if(jCheckBox16.isSelected()){
            jTextField16.setEnabled(true);
            jLabel45.setEnabled(true);
            jTextField17.setEnabled(true);
            jLabel46.setEnabled(true);
            jButton3.setText("Create Batch");
        }else{
            jButton3.setText("Create Deployment");
            jTextField16.setEnabled(false);
            jLabel45.setEnabled(false);
            jTextField17.setEnabled(false);
            jLabel46.setEnabled(false);
        }
            
        
    }//GEN-LAST:event_jCheckBox16ItemStateChanged

    
    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
// TODO add your handling code here:
        
        rand_distribution = jComboBox2.getSelectedIndex();
        
        switch(rand_distribution){
        
            case UNIFORM_DIST:
                    jTextField23.setEnabled(true);
                    jTextField24.setEnabled(true);
                    jLabel10.setEnabled(false);
                    jLabel55.setEnabled(false);
                    jTextField1.setEnabled(true);
                    jTextField15.setEnabled(false);
                    jLabel37.setEnabled(false);                     
                    break;

            case NORMAL_DIST:
                    jTextField23.setEnabled(true);
                    jTextField24.setEnabled(true);
                    jLabel10.setEnabled(true);
                    jLabel55.setEnabled(true);
                    jTextField1.setEnabled(true);
                    jTextField15.setEnabled(true);
                    jLabel37.setEnabled(true);
                    jLabel37.setText("Sigma");
                    try{
                    sigma = Double.parseDouble(jTextField15.getText());
                    }catch(Exception ex){ex.printStackTrace();}       
                            
                    break;
            
            case GRID_H_V:
            case GRID_H_V_D:
                    jTextField1.setEnabled(false);
                    break;
                    
            case CONSTANT:
                 jTextField23.setEnabled(true);
                    jTextField24.setEnabled(true);
                    jLabel10.setEnabled(true);
                    jLabel55.setEnabled(true);
                    jTextField1.setEnabled(true);
                    jTextField15.setEnabled(false);
                    jLabel37.setEnabled(false);
                break;
                    
                case CENTER:
                    jTextField23.setEnabled(false);
                    jTextField24.setEnabled(false);
                    jLabel10.setEnabled(false);
                    jLabel55.setEnabled(false);
                    jTextField23.setText(Integer.toString(h/2));
                    jTextField24.setText(Integer.toString(w/2));
                    jTextField1.setEnabled(true);
                    jTextField15.setEnabled(false);
                    jLabel37.setEnabled(false);
                break;

            case GRID_LAMBDA:
            case GRID_POISSON:
                jTextField15.setEnabled(true);
                jLabel37.setEnabled(true);
                jLabel37.setText("Lambda");
                jTextField1.setEnabled(false);
                break;

        }
        
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jCheckBox13ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox13ItemStateChanged
// TODO add your handling code here:
        
        all_trees = jCheckBox13.isSelected();
        
    }//GEN-LAST:event_jCheckBox13ItemStateChanged

    private void jCheckBox6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox6ItemStateChanged
// TODO add your handling code here:
        
        if(jCheckBox6.isSelected()){
            UpdateStats(); 
        }
        
    }//GEN-LAST:event_jCheckBox6ItemStateChanged

    
    public void UpdateStats(){
            CountNotVisited();
            int i=0;
            if(jCheckBox6.isSelected()){
            stats="Clock: "+clock+
                                "\n# of Nodes: "+numnodes+
                                "\n# of Sinks: "+numsinks+
                                "\nNot Covered: "+notCovered_Visited[0]+
                                "\nRatio: "+(double)notCovered_Visited[0]/numnodes+
                                "\nNot Visited: "+notCovered_Visited[1]+
                                "\nAvg. Level: "+avg_level+
                                "\nAvg. Num. Neighb.: "+avgNeighb+
                                "\nReachable. Num. Active Neighb. from Sink: "+ReachableNeighbActiveNodes+
                                "\n# of Messages regular sent: "+num_messages+
                                "\n# of Messages long sent: "+num_messages_long+
                                "\n# of Messages regular received: "+num_messages_r+
                                "\n# of Messages long received: "+num_messages_long_r+
                                "\n# of Lost Messages regular: "+num_messages_l+
                                "\n# of Lost Messages long: "+num_messages_long_l+
                                "\n# of Data Messages received by sink: "+num_messages_data_r_sink+
                                "\n# of dead nodes: "+count_dead+
                                "\n# of unconnected nodes: "+count_nodes_with_dead_parent;

            
                                for(i=0;i<MAX_TREES;i++)
                                    stats = stats + "\nActive nodes in VNI "+i+" ="+count_active[i];
                                
                                stats = stats +"\nTotal Energy initial="+total_energy_ini+
                                "\nTotal Energy final="+total_energy+
                                "\nTotal Energy spent="+total_energy_spent+
                                "\nTotal Energy spent ratio="+(total_energy_ini-total_energy)/total_energy_ini+
                                "\nTotal Energy in tree="+total_energy_tree+
                                "\nRatio Energy="+(total_energy_tree/total_energy)+
                                "\nTM invocations="+count_TM_invocations+
                                "\nCovered Comm Area="+covered_area+
                                "\nCovered Sensing Area="+covered_area_sens+
                                "\nAverage K Sensing Coverage="+covered_area_sens_k+
                                "\nAverage K Sensing Area Coverage="+covered_area_sens_k_all+
                                "\nTotal Covered Comm Area="+covered_area_from_sink_c+
                                "\nTotal Covered Sensing Area="+covered_area_from_sink_s+
                                "\nAlpha Coverage Value="+alpha_coverage_value+
                                "\nError in simulation="+sim_error+
                                "";
                                showStats(stats);
            }
        
    }
    
    private void jCheckBox5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox5ItemStateChanged
// TODO add your handling code here:
        
        setViewNodeNumber(jCheckBox5.isSelected());
        
    }//GEN-LAST:event_jCheckBox5ItemStateChanged

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
// TODO add your handling code here:
        
        int i=-1;
        
        if(!jTextField8.getText().equals(""))
            try{
                i = Integer.parseInt(jTextField8.getText());
            }catch(Exception e){e.printStackTrace();}
        
        showNodeInfo(i);
        
    }//GEN-LAST:event_jButton4ActionPerformed

    public void showNodeInfo(int id){
        
        if(id<numpoints && id>-1){
            jTextField8.setText(""+id);
            jTextPane1.setText(getNode(id).toString4(treeID));
        }else{
                jTextField8.setText("");
                jTextPane1.setText("");
            }
        myPanel.setSelectedNode(id);
    }
    
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
// TODO add your handling code here:
        //I can use numnodes because I will use it for real just when the topology is being generated, not before.
        tempnumnodes=0;
        try{
            numnodes = Integer.parseInt(jTextField1.getText());
        }catch(Exception ex){ex.printStackTrace();}
        jButton5.setText("Nodes left: "+(numnodes));
        myPanel.setNumNodes(numnodes);
        myPanel.setlistenPosNode(true);
        
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jComboBox5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox5ItemStateChanged
// TODO add your handling code here:
        sortingmode = jComboBox5.getSelectedIndex();
    }//GEN-LAST:event_jComboBox5ItemStateChanged

    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged
// TODO add your handling code here:
        
        gridmode = jComboBox4.getSelectedIndex();
        myPanel.setGridmode(gridmode);
    }//GEN-LAST:event_jComboBox4ItemStateChanged

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
// TODO add your handling code here:
        
        treeIDViz = jComboBox1.getSelectedIndex()-1;
        myPanel.setActive_tree_viz(treeIDViz);
        if(jCheckBox6.isSelected())
            CountNotVisited();
        myPanel.repaint();
        
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
// TODO add your handling code here:
        
        
        int i = jFileChooser1.showOpenDialog(this);
        
        if (i == JFileChooser.APPROVE_OPTION) {
                myFile = jFileChooser1.getSelectedFile();
                if(myFile.length() > 0){
                    //This is where a real application would open the file.
                    showMessage("Opening: " + myFile.getName() + ".");
                    batch_simulation=false;
                    save_stats = false;
                    jCheckBox15.setSelected(false);
                    LoadDeployment(myFile.getPath());
                    jTextField1.setText(""+numnodes);
                    jTextField2.setText(""+radius);
                }else{
                    showMessage("No files selected by user.");
                }
            } else {
                showMessage("Open command cancelled by user.");
            }
        
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
// TODO add your handling code here:
        
        jFileChooser1.setMultiSelectionEnabled(false);
        int i = jFileChooser1.showSaveDialog(this);
        
        if (i == JFileChooser.APPROVE_OPTION) {
                myFile = jFileChooser1.getSelectedFile();
                //This is where a real application would open the file.
                showMessage("Opening: " + myFile.getName() + ".");
                
                SaveDeployment(myFile.getPath());
                
            } else {
                showMessage("Open command cancelled by user.");
            }
        
        
        
        
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
// TODO add your handling code here:
        
        myPanel.setFilledActiveNodes(jCheckBox4.isSelected());
        
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
// TODO add your handling code here:
        
        myPanel.setActiveNodes(jCheckBox3.isSelected());
        
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    public void clear_all_sim(){
        
        try{
            sim_assigned = false;
            //cleaning the structures
            mynodes.clear();
            myPanel.clearAll();
            topology_loaded=false;
            count_TM_invocations=0;
            
            radius = Double.parseDouble(jTextField2.getText());
            diameter = radius*2;
            
            sense_radius = Double.parseDouble(jTextField7.getText());
            sense_diameter = 2*sense_radius;
            
            sigma = Double.parseDouble(jTextField15.getText());
            start_index = Integer.parseInt(jTextField17.getText());
            
            energy_distribution_parameter = Double.parseDouble(jTextField20.getText());
            sigma_e = Double.parseDouble(jTextField21.getText());
                    
            bit_error_rate = Double.parseDouble(jTextField22.getText());
            
            candidate_grouping_step = Double.parseDouble(jTextField30.getText());

            alpha_coverage_value = Double.parseDouble(jTextField43.getText());

            //Draw only default gateways
            jCheckBox26.setSelected(false);
            
            w = Integer.parseInt(jTextField13.getText());
            h = Integer.parseInt(jTextField14.getText());
            myPanel.setArea(w, h);
            myPanel.setRangeArea(false, false);
            
            numnodes=0;
            numsinks=0;
            numpoints=0;
            
            //Updating labels
            jLabel6.setText(""+numnodes);
            jLabel7.setText(""+radius);
            jLabel28.setText(""+numsinks);
            
            sortingmode = jComboBox5.getSelectedIndex();



            //Parameter for the motion protocol
            pm_step = Integer.parseInt(jTextField40.getText());
            pm_sleep = Integer.parseInt(jTextField41.getText());
            

            // Parameter for the KNEIGH algorithm
            kneigh_k = Integer.parseInt(jTextField42.getText());

        }catch(Exception e){e.printStackTrace();}
        
        
        treeIDViz = jComboBox1.getSelectedIndex()-1;
        myPanel.setActive_tree_viz(treeIDViz);
        treeID=-1;
        jComboBox1.setSelectedIndex(0);
        
        myPanel.setActive_tree_viz(treeIDViz);
        myPanel.setNumNodes(0);
        
        //no_paint=true;
        
        myPanel.repaint();
        
    }
    
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
// TODO add your handling code here:
       //maximum number of nodes is equal to sender node
       int i,j=0;
       boolean sw;
       String the_path;
       int count_trials=0;
       int result=0;
       current_node_count=0;
       
       String[] options = new String[2];
       options[0] = "Yes";
       options[1] = "No";
       
        clear_all_sim();
        batch_simulation=false;
        
        if(!batch_creation){
            do{
                myPanel.clearAll();
                sw=NodeGenerator();
                count_trials++;
                
                if(count_trials>=MAX_TRIALS){
                    
                    result = JOptionPane.showOptionDialog( 
 		    this,                             // the parent that the dialog blocks 
 		    "The maximum amount of failed trials was reached. Want to try again?",                                    // the dialog message array 
 		    "Topology Creation Error", // the title of the dialog window 
 		    JOptionPane.DEFAULT_OPTION,                 // option type 
 		    JOptionPane.INFORMATION_MESSAGE,            // message type 
 		    null,                                       // optional icon, use null to use the default icon 
 		    options,                                    // options string array, will be made into buttons 
 		    options[1]                                  // option that should be made into a default button 
 		); 
 		switch(result) { 
 		   case 0: // yes 
 		     count_trials=0;
 		     break; 
 		   case 1: // no 
 		     //count_trials = MAX_TRIALS+1;
                     sw=true;
                     clear_all_sim();
 		     break; 
 		   default: 
 		     break; 
 		} 
                
                }else{
                    if(sw)
                        topology_loaded=true;
                }
                
            }while(!sw);
            myPanel.repaint();
        }else{
            
            try{
                j=Integer.parseInt(jTextField16.getText());
            }catch(Exception ex){ex.printStackTrace();}
            
            if(j>0){
            
             i = jFileChooser1.showOpenDialog(this);
             
            if (i == JFileChooser.APPROVE_OPTION) {
                myFile = jFileChooser1.getSelectedFile();
                
                if(myFile.isDirectory()){
                    the_path = myFile.getPath();
                    
                    //This is where a real application would open the file.
                    showMessage("Saving batch deployments in: " + the_path);           
                    the_batch_sim.setParamsForBatchCreation(j, the_path);
                    the_batch_sim.setWork(GENERATE_TOPOS);
                    jProgressBar2.setMaximum(j);
                    jProgressBar2.setValue(0);
                    
                }
                
            } else {
                showMessage("Save command cancelled by user.");
            }
             
        }else
            javax.swing.JOptionPane.showMessageDialog(this, "Non valid batch number!");
            
        }
        
    }//GEN-LAST:event_jButton3ActionPerformed

    public int getMatArea(int x, int y){
    
        if(x<0 || x>w-1 || y<0 || y>h-1)
            return -1;
        //Returning 1 will avoid the calc_coverage_area to evaluate the point x,y that is outside the area of coverage.
        
        return mat_area[x][y];
    }


    public double[] calc_coverage_area_k(int mode){

        int i=0,j=0,k=0, temp_tree=0;
        double[] count= new double[3];
        boolean sw=true;
        int x=0,y=0,range=0, dia=0;
        int tx=0,ty=0, temp=0;
        int desp_x=0, desp_y=0;


        for(i=0;i<w;i++){
            for(j=0;j<h;j++){
                mat_area[i][j] = 0;
            }
        }

        for(k=0;k<numpoints;k++){
            temp_tree = getNode(k).getActiveTree();

            if(getNode(k).isActive(temp_tree)){
                //Coordinates of the center of the comm range
                x = (int)getNode(k).getPosition().getX();
                y = (int)getNode(k).getPosition().getY();

                if(mode==0){
                    //Radius of the comm range
                    range = (int)getNode(k).getRadius(temp_tree);
                }else if(mode==1)
                    range = (int)getNode(k).getSense_Radius();

                dia = range*2-1;

                //coordinates od the upper corner of a square containing the comm area
                tx = x-range;
                ty = y-range;

                //Distance from a corner to the circunference
                temp = (int)java.lang.Math.floor(range*0.4147);

                for(i=0;i<=range;i++){
                    for(j=0;j<=i;j++){

                        //upper left corner checking
                        desp_y = ty+i-j;
                        desp_x = tx+j;
                        //upper left corner checking

                        if(getMatArea(desp_y, desp_x) >= 0)
                        if(getNode(k).covers(desp_x, desp_y,mode)){
                            mat_area[desp_y][desp_x]++;
                        }

                        desp_y = y-i+j;
                        desp_x = x-j;

                        if(getMatArea(desp_y, desp_x) >= 0){
                            mat_area[desp_y][desp_x]++;
                        }

                        //upper right corner checking
                        desp_y = ty+i-j;
                        desp_x = tx+dia-j;
                        //upper left corner checking
                        if(getMatArea(desp_y, desp_x) >= 0)
                        if(getNode(k).covers(desp_x, desp_y,mode)){
                            mat_area[desp_y][desp_x]++;
                        }
                        desp_y = y-i+j;
                        desp_x = x+j;
                        if(getMatArea(desp_y, desp_x) >= 0){
                            mat_area[desp_y][desp_x]++;
                        }

                        //lower left corner checking
                        desp_y = ty+dia-i+j;
                        desp_x = tx+j;
                        //upper left corner checking
                        if(getMatArea(desp_y, desp_x) >= 0)
                        if(getNode(k).covers(desp_x, desp_y,mode)){
                            mat_area[desp_y][desp_x]++;
                        }
                        desp_y = y+i-j;
                        desp_x = x-j;
                        if(getMatArea(desp_y, desp_x) >= 0){
                            mat_area[desp_y][desp_x]++;
                        }


                        //lower right corner checking
                        desp_y = ty+dia-i+j;
                        desp_x = tx+dia-j;
                        //upper left corner checking
                        if(getMatArea(desp_y, desp_x) >= 0)
                        if(getNode(k).covers(desp_x, desp_y,mode)){
                            mat_area[desp_y][desp_x]++;
                        }
                        desp_y = y+i-j;
                        desp_x = x+j;
                        if(getMatArea(desp_y, desp_x) >= 0){
                            mat_area[desp_y][desp_x]++;
                        }


                    }
                }

            }
        }



        for(i=0;i<w;i++){
            for(j=0;j<h;j++){
                count[2] += mat_area[i][j];
                //K coverage average
                if(mat_area[i][j] != 0){
                    count[0] += mat_area[i][j];
                    temp++;
                }
                //Just coverage
                if(mat_area[i][j] != 0)
                    count[1]++;
            }
        }

        count[0]/=((double)(temp));
        count[1]/=((double)(h*w));
        count[2]/=((double)(h*w));

        return count;
    }



    public double calc_coverage_area(int mode){
    
        int i=0,j=0,k=0, temp_tree=0;
        double count=0;
        boolean sw=true;
        int x=0,y=0,range=0, dia=0;
        int tx=0,ty=0, temp=0;
        int desp_x=0, desp_y=0;
        
        
        for(i=0;i<w;i++){
            for(j=0;j<h;j++){
                mat_area[i][j] = 0;
            }
        }

        for(k=0;k<numpoints;k++){
            temp_tree = getNode(k).getActiveTree();
            
            if(getNode(k).isActive(temp_tree)){
                //Coordinates of the center of the comm range
                x = (int)getNode(k).getPosition().getX();
                y = (int)getNode(k).getPosition().getY();

                if(mode==0){
                    //Radius of the comm range
                    range = (int)getNode(k).getRadius(temp_tree);
                }else if(mode==1)
                    range = (int)getNode(k).getSense_Radius();
                
                dia = range*2-1;
                
                //coordinates od the upper corner of a square containing the comm area
                tx = x-range;
                ty = y-range;
                
                //Distance from a corner to the circunference
                temp = (int)java.lang.Math.floor(range*0.4147);
                
                for(i=0;i<=range;i++){
                    for(j=0;j<=i;j++){
                        
                        //upper left corner checking
                        desp_y = ty+i-j;
                        desp_x = tx+j;
                        //upper left corner checking
                        if(getMatArea(desp_y, desp_x) == 0){
                            if(getNode(k).covers(desp_x, desp_y,mode)){
                                mat_area[desp_y][desp_x] = 1;
                            }
                        }
                        desp_y = y-i+j;
                        desp_x = x-j;
                        if(getMatArea(desp_y, desp_x) == 0){
                            mat_area[desp_y][desp_x] = 1;
                        }
                        
                        //upper right corner checking
                        desp_y = ty+i-j;
                        desp_x = tx+dia-j;
                        //upper left corner checking
                        if(getMatArea(desp_y, desp_x) == 0){
                            if(getNode(k).covers(desp_x, desp_y,mode)){
                                mat_area[desp_y][desp_x] = 1;
                            }
                        }
                        desp_y = y-i+j;
                        desp_x = x+j;
                        if(getMatArea(desp_y, desp_x) == 0){
                            mat_area[desp_y][desp_x] = 1;
                        }
                        
                        //lower left corner checking
                        desp_y = ty+dia-i+j;
                        desp_x = tx+j;
                        //upper left corner checking
                        if(getMatArea(desp_y, desp_x) == 0){
                            if(getNode(k).covers(desp_x, desp_y,mode)){
                                mat_area[desp_y][desp_x] = 1;
                            }
                        }
                        desp_y = y+i-j;
                        desp_x = x-j;
                        if(getMatArea(desp_y, desp_x) == 0){
                            mat_area[desp_y][desp_x] = 1;
                        }
                        
                        
                        //lower right corner checking
                        desp_y = ty+dia-i+j;
                        desp_x = tx+dia-j;
                        //upper left corner checking
                        if(getMatArea(desp_y, desp_x) == 0){
                            if(getNode(k).covers(desp_x, desp_y,mode)){
                                mat_area[desp_y][desp_x] = 1;
                            }
                        }
                        desp_y = y+i-j;
                        desp_x = x+j;
                        if(getMatArea(desp_y, desp_x) == 0){
                            mat_area[desp_y][desp_x] = 1;
                        }
                        
                        
                    }
                }
                
            }
        }
        
       

        for(i=0;i<w;i++){
            for(j=0;j<h;j++){
                count += mat_area[i][j];
            }
        }

        return count/((double)(h*w));
    }
    
    
    
    
    public double calc_coverage_area_BFS(int mode){
    
        int i,j,k=0, temp_tree=0;
        double count=0;
        boolean sw=true;
        int x=0,y=0,range=0, dia=0;
        int tx=0,ty=0, temp=0;
        int desp_x=0, desp_y=0;
        
        for(i=0;i<w;i++){
            for(j=0;j<h;j++){
                mat_area[i][j] = 0;
            }
        }
        
        for(k=0;k<numpoints;k++){
            temp_tree = getNode(k).getActiveTree();
            
            //if(getNode(k).isActive(temp_tree)){
            if(getNode(k).getStateBFS() == S_VISITED){
                //Coordinates of the center of the comm range
                x = (int)getNode(k).getPosition().getX();
                y = (int)getNode(k).getPosition().getY();

                if(mode==0){
                    //Radius of the comm range
                    range = (int)getNode(k).getRadius(temp_tree);
                }else if(mode==1)
                    range = (int)getNode(k).getSense_Radius();

                dia = range*2-1;
                
                //coordinates od the upper corner of a square containing the comm area
                tx = x-range;
                ty = y-range;
                
                //Distance from a corner to the circunference
                temp = (int)java.lang.Math.floor(range*0.4147);
                
                for(i=0;i<=range;i++){
                    for(j=0;j<=i;j++){
                        
                        //upper left corner checking
                        desp_y = ty+i-j;
                        desp_x = tx+j;
                        //upper left corner checking
                        if(getMatArea(desp_y, desp_x) == 0){
                            if(getNode(k).covers(desp_x, desp_y,mode)){
                                mat_area[desp_y][desp_x] = 1;
                            }
                        }
                        desp_y = y-i+j;
                        desp_x = x-j;
                        if(getMatArea(desp_y, desp_x) == 0){
                            mat_area[desp_y][desp_x] = 1;
                        }
                        
                        //upper right corner checking
                        desp_y = ty+i-j;
                        desp_x = tx+dia-j;
                        //upper left corner checking
                        if(getMatArea(desp_y, desp_x) == 0){
                            if(getNode(k).covers(desp_x, desp_y,mode)){
                                mat_area[desp_y][desp_x] = 1;
                            }
                        }
                        desp_y = y-i+j;
                        desp_x = x+j;
                        if(getMatArea(desp_y, desp_x) == 0){
                            mat_area[desp_y][desp_x] = 1;
                        }
                        
                        //lower left corner checking
                        desp_y = ty+dia-i+j;
                        desp_x = tx+j;
                        //upper left corner checking
                        if(getMatArea(desp_y, desp_x) == 0){
                            if(getNode(k).covers(desp_x, desp_y,mode)){
                                mat_area[desp_y][desp_x] = 1;
                            }
                        }
                        desp_y = y+i-j;
                        desp_x = x-j;
                        if(getMatArea(desp_y, desp_x) == 0){
                            mat_area[desp_y][desp_x] = 1;
                        }
                        
                        
                        //lower right corner checking
                        desp_y = ty+dia-i+j;
                        desp_x = tx+dia-j;
                        //upper left corner checking
                        if(getMatArea(desp_y, desp_x) == 0){
                            if(getNode(k).covers(desp_x, desp_y,mode)){
                                mat_area[desp_y][desp_x] = 1;
                            }
                        }
                        desp_y = y+i-j;
                        desp_x = x+j;
                        if(getMatArea(desp_y, desp_x) == 0){
                            mat_area[desp_y][desp_x] = 1;
                        }
                        
                        
                    }
                }
                
            }
        }
        
        for(i=0;i<w;i++){
            for(j=0;j<h;j++){
                count += mat_area[i][j];
            }
        }
        
        return count/((double)(h*w));
    }
    
    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
// TODO add your handling code here:
        myPanel.setRangeArea(jCheckBox2.isSelected(),jCheckBox21.isSelected());
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
// TODO add your handling code here:
        
        myPanel.setAtarraya(jCheckBox1.isSelected());
        
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
// TODO add your handling code here:
        //mainSW = false;
        if(my_sim!=null){
            if(my_sim.isSimulationStarted()){
                my_sim.simulation_pause();
                jButton2.setText("Restart agent");
            }else{
                my_sim.simulation_start();
                jButton2.setText("Pause agent");
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    public void StartSimulation(){
    
        int i,j;
        pm_step = 20;
        pm_sleep = 10;
        my_sim = new the_sim();
        active_sinks=0;
        num_events=0;
        num_messages=0;
        num_messages_long=0;
        num_messages_r=0;
        num_messages_long_r=0;
        num_messages_data_r_sink=0;
        total_energy_spent=0;
        count_dead=0;
        mat_area = new byte[w][h];
        TC_Prot_final=false;
        temp_lifetime_string = "";
        temp_lifetimeSurvivors_string = "";
        temp_lifetimeBFS_string = "";
        temp_lifetimeBFS_Coverage_c_string = "";
        temp_lifetimeBFS_Coverage_s_string = "";
        
        if(save_all_stats){
            try{
                save_all_stats_step = Integer.parseInt(jTextField3.getText());
            }catch(Exception ex){ex.printStackTrace();}

        }
        
        try{
            max_clock_time = Double.parseDouble(jTextField12.getText());
            TM_ENERGY_THSLD_STEP = Double.parseDouble(jTextField39.getText());
            NEXT_RESET_TIMEOUT = Double.parseDouble(jTextField38.getText());


            
            

        }catch(Exception ex){ex.printStackTrace();}
        
        //Redefining the sorting mode on the nodes
        for(i=0;i<numpoints;i++){
            getNode(i).setSortingMode(sortingmode);
            getNode(i).setTM_energy_step(TM_ENERGY_THSLD_STEP);
        }

        //Motion protocol parameters
        pm_step = Integer.parseInt(jTextField40.getText());
        pm_sleep = Integer.parseInt(jTextField41.getText());
        
        //If a motion protocol was selected, then start it on the nodes
        if(selected_Motionprotocol!=0){
          simulator.init_nodes(treeID,MOTION_PROTOCOL);
        }

        for(i=0;i<w;i++){
            for(j=0;j<h;j++){
                mat_area[i][j] = 0;
            }
        }
        
        if(!all_trees){                                                 //Each tree will start its sinks at the click of the Start atarraya button.
            treeID++;
            if(treeID <= MAX_TREES){
                simulator.init_nodes(treeID,TC_PROTOCOL);
                for(i=0;i<num_sinks_per_tree[treeID];i++){
                    simulator.initial_event(active_sinks+numnodes, treeID,TC_PROTOCOL);
                    active_sinks++;
                }
            }
            
        }else{                                                          //All trees will start their sinks at the same time.
            
            
            for(j=0;j<MAX_TREES;j++){
                if(num_sinks_per_tree[j] > 0){              //If there are sinks in that tree...
                    simulator.init_nodes(j,TC_PROTOCOL);
                    for(i=0;i<num_sinks_per_tree[j];i++){
                        clock = TREE_CONSTRUCTION_DELAY*j;
                        simulator.initial_event(active_sinks+numnodes, j,TC_PROTOCOL);
                        active_sinks++;
                    }
                }
            }
            clock=0;
        }
        
         try{
            
            if(!batch_simulation && (save_stats || save_events_log || save_lifetime_log || save_results_log)){
                timestamp t = new timestamp();
                jComboBox1.setSelectedIndex(0);
                treeIDViz=-1;
                myPanel.setActive_tree_viz(treeIDViz);
                view_active_tree=true;
                jCheckBox10.setSelected(true);
                report_descriptor = jTextField18.getText();
                save_all_stats_step = Integer.parseInt(jTextField3.getText());
            
                
                if(myFile!=null){
                
                    the_parent_dir = myFile.getParent();
                    the_name = myFile.getName();
                    the_name2 = myFile.getName() + "_" +report_descriptor + "_" + t.toString2();
                    
                }else{
                    
                     i = jFileChooser1.showSaveDialog(this);

                    if (i == JFileChooser.APPROVE_OPTION) {
                        myFile = jFileChooser1.getSelectedFile();

                        if(myFile.isDirectory()){
                            the_parent_dir = myFile.getPath();

                            //This is where a real application would open the file.
                            showMessage("Saving stats from simulation in: " + the_parent_dir);

                        }

                    } else {
                        showMessage("Save command cancelled by user.");
                    }
                    
                    the_name2 = report_descriptor + "_" + numnodes + "_" + numsinks + "_" + w + "_" + h + "_" + t.toString2()+".atar";
                }
                
                if(save_stats){
                    if(excel_ready_reports)
                        fout_log_stat = new FileWriter(the_parent_dir+"\\stats_"+the_name2+".csv");
                    else
                        fout_log_stat = new FileWriter(the_parent_dir+"\\stats_"+the_name2+".atar");
                    mywrite_log_stat = new BufferedWriter(fout_log_stat);
                }
                
                if(save_events_log){
                    fout_log_ev = new FileWriter(the_parent_dir+"\\events_"+the_name2+".csv");
                    mywrite_log_ev = new BufferedWriter(fout_log_ev);
                }
                
                if(save_lifetime_log){
                    fout_log_lt = new FileWriter(the_parent_dir+"\\lifetime_"+the_name2+".csv");
                    mywrite_log_lt = new BufferedWriter(fout_log_lt);
                }

                if(save_results_log){
                    fout_log_results = new FileWriter(the_parent_dir+"\\results_"+the_name2+".csv");
                    mywrite_log_results = new BufferedWriter(fout_log_results);
                }

            }
        
        }catch(Exception ex){ex.printStackTrace();}

        

        my_sim.simulation_start();
        my_sim.start();
        if(batch_simulation){
            try{
                my_sim.join();
            }catch(Exception ex){ex.printStackTrace();}
        }
    }
    
    public int getTMType(){
        return simulator.getTMType();
    }
    
    public void selectSimulator(){
    
        
         simulator.SetHandler(TC_PROTOCOL,selected_TCprotocol);
         
         if(selected_TCprotocol == PROTOCOL_ECDS ||selected_TCprotocol == PROTOCOL_CDS_RULEK){
            jComboBox5.setSelectedIndex(METRIC1_ONLY);
            sortingmode = METRIC1_ONLY;
         }
        
         if(selected_TMprotocol != TM_PROTOCOL_NO_SELECTED)
            simulator.SetHandler(TM_PROTOCOL,selected_TMprotocol);
         
         if(selected_SDprotocol != SENSOR_PROTOCOL_NO_SELECTED)
            simulator.SetHandler(SENSOR_PROTOCOL,selected_SDprotocol);
         
         if(selected_COMMprotocol != COMM_PROTOCOL_NO_SELECTED)
            simulator.SetHandler(COMM_PROTOCOL,selected_COMMprotocol);

         if(selected_Motionprotocol != MOTION_PROTOCOL_NO_SELECTED)
            simulator.SetHandler(MOTION_PROTOCOL,selected_Motionprotocol);
         
    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// TODO add your handling code here:

         getNode(sender_node).color=Color.RED;
        clearLines();
        for(int i=0;i<NUMPOINTS;i++)
        {
            if(i%2==0)
            {
                getNode(i).setEnergy(800);
            }
        }
        if(topology_loaded){
            clock=0;
            selectSimulator();
            jCheckBox3.setSelected(true);
            jCheckBox1.setSelected(true);
            jCheckBox10.setSelected(true);
            jComboBox1.setEnabled(true);
            sim_assigned = true;
            StartSimulation();
            topology_loaded = false;
            
        }else if(num_files_topology_from_batch>0){
            selectSimulator();
            the_batch_sim.setWork(SIMULATE);    
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    
    public void addDefaultSinkCreationWord(){
        jCheckBox22.setSelected(true);
        addNodeCreationWord();
        jCheckBox22.setSelected(false);
    }
    
    public void addNodeCreationWord(){
    
/*
         *  word format:
         * sink@                        {0,1}= no, yes
         * tree selection@          int
         * number of nodes@     int
         * comm rad@               int
         * sensing rad@            int
         * Interquery time@       double
         * position distrib@        {0-5} = uniform, normal, grid HV, grid HVD, constant, center
         * mean x@                   int
         * mean Y@                   int
         * parameter@               double
         * energy distrib@        {0-5} = uniform, normal,poisson, constant
         * max value@               int
         * mean value@              int
         * parameter@               double
         * motion@                   {0,1}= no, yes
         */
        
        int numnodes_=0;
        double comm_rad_=40.0, sense_rad_=20.0;
        int sink_=0, tree_sel_=0;
        double interquery_time_=10;
        int pos_dist_=0, mean_x=300, mean_y=300;
        int energy_dist_=0, energy_max=1000, energy_mean=1000;
        double pos_param=1, energy_param=1;
        int x=0,y=0,i,j,energy=1000;
        int step;
        int temp, limit_h, limit_w;
        int _width=600, _height=600;
        int motion = 0;
        int _energy_model = 0;
        
        
        String the_word;
        
        try{
            
            /*
             * Sink information
             */
            if(jCheckBox22.isSelected())
                sink_ = 1;

            tree_sel_ = jComboBox8.getSelectedIndex();

            
             /*
             * Number of nodes and ranges
             */
            
            numnodes_ = Integer.parseInt(jTextField1.getText());

            comm_rad_ = Double.parseDouble(jTextField2.getText());
            
            sense_rad_ = Double.parseDouble(jTextField7.getText());
            
            interquery_time_ = Double.parseDouble(jTextField9.getText());
            
             /*
             * Position information
             */
            
            pos_dist_ = jComboBox2.getSelectedIndex();
            
            mean_x = Integer.parseInt(jTextField23.getText());
            
            mean_y = Integer.parseInt(jTextField24.getText());
            
            pos_param = Double.parseDouble(jTextField15.getText());
            
            _width = Integer.parseInt(jTextField31.getText());
            
            _height = Integer.parseInt(jTextField32.getText());
            
             /*
             * Energy information
             */
            
            energy_dist_ = jComboBox6.getSelectedIndex();
            
            energy_max = Integer.parseInt(jTextField25.getText());
            
            energy_mean = Integer.parseInt(jTextField20.getText());
            
            energy_param = Double.parseDouble(jTextField21.getText());
            
            /*
             * Weights definition
             */
            
            weight_metric1 = Double.parseDouble(jTextField26.getText());
            
            weight_metric2 = Double.parseDouble(jTextField27.getText());

            /*
             * Motion activation definition
             */

            if(jCheckBox8.isSelected())
                motion = 1;

            _energy_model = jComboBox13.getSelectedIndex();

         the_word = ""+sink_+"@"+        
         tree_sel_+"@"+
         numnodes_+"@"+   
         comm_rad_+"@"+
         sense_rad_+"@"+
         interquery_time_+"@"+
         pos_dist_+"@"+
         mean_x+"@"+
         mean_y+"@"+
         pos_param+"@"+
         _width+"@"+
         _height+"@"+
         energy_dist_+"@"+
         energy_max+"@"+
         energy_mean+"@"+
         energy_param+"@"+
         weight_metric1+"@"+
         weight_metric2+"@"+
         motion+"@"+
         _energy_model+"@\n";
        
         if(sink_==0)
            node_creation_words.add(the_word);
         else
            sink_creation_words.add(the_word);
         
         UpdateNodeCreationWords();
         
        }catch(Exception ex){ex.printStackTrace();}
        
    }
    
    
    
    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        addNodeCreationWord();
    }//GEN-LAST:event_jButton12ActionPerformed

    
    
    public void UpdateNodeCreationWords(){
        
        int i;
        
        node_creation_list.removeAllElements();
        sink_creation_list.removeAllElements();
        
        for(i=0;i<node_creation_words.size();i++){
            node_creation_list.add(i,node_creation_words.get(i));
        }
        for(i=0;i<sink_creation_words.size();i++){
            sink_creation_list.add(i,sink_creation_words.get(i));
        }
        
        jList1.setModel(node_creation_list);
        jList2.setModel(sink_creation_list);
        
    }
    
    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
       int i = jList1.getSelectedIndex();
       
       if(i!=-1){
        node_creation_words.remove(i);
       }
       
       i = jList2.getSelectedIndex();
       if(i!=-1){
          sink_creation_words.remove(i);
       }
       
       UpdateNodeCreationWords();
       
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jCheckBox22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox22ActionPerformed
        // TODO add your handling code here:
        
         if(jCheckBox22.isSelected()){
            jTextField1.setText("1");
            jComboBox2.setSelectedIndex(3);
            jComboBox8.setEnabled(true);
        }else{
            jComboBox2.setSelectedIndex(0);
            jComboBox8.setEnabled(false);
            jComboBox8.setSelectedIndex(0);
        }
        
    }//GEN-LAST:event_jCheckBox22ActionPerformed

    private void jCheckBox22ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox22ItemStateChanged
        // TODO add your handling code here:
        
        if(jCheckBox22.isSelected()){
            jTextField1.setText("1");
            jComboBox2.setSelectedIndex(CENTER);
        }else{
            jComboBox2.setSelectedIndex(0);
        }
    }//GEN-LAST:event_jCheckBox22ItemStateChanged

    public void setWeights(){
      
        int i=0;
        
        try{
        
            weight_metric1= Double.parseDouble(jTextField26.getText());
            
            weight_metric2= Double.parseDouble(jTextField27.getText());
            
        }catch(Exception ex){ex.printStackTrace();}
        
        for(i=0;i<numpoints;i++){
            getNode(i).setWeight(WEIGHT_ENERGY, weight_metric1);
            getNode(i).setWeight(WEIGHT_DISTANCE, weight_metric1);
        }
        
    }
    
   public void setInterqueryTime(){
      
        int i=0;
        double interquery_time_=10;
        try{
        
            interquery_time_= Double.parseDouble(jTextField9.getText());
            
        }catch(Exception ex){ex.printStackTrace();}
        
        for(i=0;i<numpoints;i++){
            getNode(i).setInterqueryTime(interquery_time_);
        }
        
    } 
    
    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
    
        setWeights();
        
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jComboBox10ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox10ItemStateChanged
        // TODO add your handling code here:
        
        selected_TMprotocol = jComboBox10.getSelectedIndex();
        
        if(selected_TMprotocol == 0){
            TM_Selected=false;
            if(!SD_Selected && !COMM_Selected && TC_Selected)
                jButton1.setEnabled(true);
            else
                jButton1.setEnabled(false);
        }else{
            TM_Selected=true;
            if(SD_Selected && COMM_Selected && TC_Selected)
                jButton1.setEnabled(true);
            else
                jButton1.setEnabled(false);
        }
        
    }//GEN-LAST:event_jComboBox10ItemStateChanged

    private void jComboBox11ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox11ItemStateChanged
        // TODO add your handling code here:
        
        selected_SDprotocol = jComboBox11.getSelectedIndex();
        
        if(selected_SDprotocol == 0){
            SD_Selected=false;
            if(!TM_Selected && !COMM_Selected && TC_Selected)
                jButton1.setEnabled(true);
            else
                jButton1.setEnabled(false);
        }else{
            SD_Selected=true;
            if(TM_Selected && COMM_Selected && TC_Selected)
                jButton1.setEnabled(true);
            else
                jButton1.setEnabled(false);
        }
        
    }//GEN-LAST:event_jComboBox11ItemStateChanged

    public void clean_creation_words(){
    
        node_creation_words.clear();
        sink_creation_words.clear();
        sink_creation_list.clear();
        node_creation_list.clear();
        jList1.setModel(node_creation_list);
        jList2.setModel(sink_creation_list);
        
    }
    
    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
        
        clean_creation_words();
        
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jCheckBox10ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox10ItemStateChanged
        // TODO add your handling code here:
        
        view_active_tree = jCheckBox10.isSelected();
        myPanel.repaint();
        
    }//GEN-LAST:event_jCheckBox10ItemStateChanged

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        my_sim.simulation_end();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jCheckBox15ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox15ItemStateChanged
        // TODO add your handling code here:
        
        save_stats = jCheckBox15.isSelected();
        
        if(save_stats){
            
                jCheckBox17.setSelected(false);
                jCheckBox17.setEnabled(true);
                indiv_reports=false;
                jCheckBox18.setSelected(true);
                jCheckBox18.setEnabled(true);
                sumarized_reports=true;
                jCheckBox19.setSelected(true);
                jCheckBox19.setEnabled(true);
                excel_ready_reports=true;
                jCheckBox23.setSelected(false);
                jCheckBox23.setEnabled(true);
                save_all_stats=false;
                
        }else{
            jCheckBox17.setSelected(false);
            jCheckBox17.setEnabled(false);
            indiv_reports=false;
            jCheckBox18.setSelected(true);
            jCheckBox18.setEnabled(false);
            sumarized_reports=true;
            jCheckBox19.setSelected(true);
            jCheckBox19.setEnabled(false);
            excel_ready_reports=true;
            jCheckBox23.setSelected(false);
            jCheckBox23.setEnabled(false);
            save_all_stats=false;
        }
        
    }//GEN-LAST:event_jCheckBox15ItemStateChanged

    private void jCheckBox23ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox23ItemStateChanged
        // TODO add your handling code here:
        
        save_all_stats = jCheckBox23.isSelected();
        
        if(save_all_stats){
            jTextField3.setEnabled(true);
            try{
                save_all_stats_step = Integer.parseInt(jTextField3.getText());
            }catch(Exception ex){ex.printStackTrace();}

        }else{
            jTextField3.setEnabled(false);
        }
            
        
    }//GEN-LAST:event_jCheckBox23ItemStateChanged

    private void jCheckBox9ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox9ItemStateChanged
        // TODO add your handling code here:
        save_lifetime_log = jCheckBox9.isSelected();
        
    }//GEN-LAST:event_jCheckBox9ItemStateChanged

    private void jCheckBox12ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox12ItemStateChanged
        // TODO add your handling code here:
        save_events_log = jCheckBox12.isSelected();
    }//GEN-LAST:event_jCheckBox12ItemStateChanged

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
       
       //GiantComponentTest();
       the_batch_sim.setWork(GIANTCOMPONENT);
        
    }//GEN-LAST:event_jButton16ActionPerformed

    public int calc_BFS(){
        
        int i=0, t = 0;
        int temp=0, max=0, tam=0;
        
        if(numsinks>0)
            tam = numpoints;
        else
            tam = numnodes;
        
        for(i=0;i<tam;i++){
            if(getNode(i).getStateBFS() == S_NOT_VISITED){
                temp=BFS(BFS_MODE_STANDARD,i);
            }
            if(temp>max)
                max = temp;
        }
        
        for(i=0;i<numpoints;i++){
                getNode(i).setStateBFS(S_NOT_VISITED);
            }
        temp_ids.clear();
        
        return max;
    }
    
    
    public void GiantComponentTest(){
    
        int i=0,j=0,k=0,l=0;
        int ctr_int=0, ctr_old=0;
        double di=0, dk=0, dl=0, dr=0, dn=0;
        double temp_bfs=0.0;
        double count_connected_ctr=0;
        double avg_bfs_size=0, percent_count_connected_ctr=0;
        Vector<Double> GC_info1 = new Vector<Double>();
        Vector<Double> GC_info2 = new Vector<Double>();
        Vector<Double> GC_info3 = new Vector<Double>();
        ctr_int = (int)java.lang.Math.round(initial_ctr*area_side_ctr);
        ctr_old = ctr_int;
        double total_avg_neighbors=0;
        String the_path="";
        batch_simulation=true;
        GC_Test = true;
        boolean sw=false;
        double total_experiments=0;
        String tempString="";
        File tempFile;
        BatchExecutor smallexec = new BatchExecutor(this);
        smallexec.start();
        
        timestamp t;
        
                
                
        num_nodes_ctr=100;
        area_side_ctr=100;
        comm_radius_ctr=13;
        initial_ctr = 0.01;
        step_ctr = 0.01;

        initial_k_ctr= 0.5;
        final_k_ctr= 1;
        step_k_ctr= 0.1;
        initial_i_exp_l_ctr= 4;
        final_i_exp_l_ctr= 10;

        try{
            num_nodes_ctr = Integer.parseInt(jTextField6.getText());
            area_side_ctr = Integer.parseInt(jTextField11.getText());
            num_topologies_ctr = Integer.parseInt(jTextField5.getText());
            initial_ctr = Double.parseDouble(jTextField29.getText());
            step_ctr = Double.parseDouble(jTextField10.getText());

            initial_k_ctr= Double.parseDouble(jTextField35.getText());
            final_k_ctr= Double.parseDouble(jTextField36.getText());
            step_k_ctr= Double.parseDouble(jTextField37.getText());
            initial_i_exp_l_ctr= Integer.parseInt(jTextField33.getText());
            final_i_exp_l_ctr= Integer.parseInt(jTextField34.getText());


        }catch(Exception ex){ex.printStackTrace();}

                
                //This is where a real application would open the file.
               
                /*-------------------------------------------------------
                 * JUST IN CASE i WOULD LIKE TO CONTINUE WITH IT...
                 * this version calculatesthe averages topology by topology, saving time on the loading, but somethign is
                 * not working properly... more time to spend later on it...
                 * ------------------------------------------------------
                for(i=0;i<100;i++){
                    GC_info1.add(0.0);
                    GC_info2.add(0.0);
                    GC_info3.add(0.0);
                    GC_info4.add(0.0);
                }
                
                for(i=0;i<myFiles.length;i++){
                    LoadDeployment(myFiles[i].getPath());
                    k=0;
                    ctr_int = (int)java.lang.Math.round((initial_ctr+(k*step_ctr))*area_side_ctr);
                    avg_bfs_size=0;
                    while(avg_bfs_size<100){
                        avgNeighb=0;
                        for(j=0;j<numpoints;j++){
                            getNode(j).cleanNeighbors();
                            getNode(j).setRadius(ctr_int);
                            avgNeighb += BuildNeighborhood(j);
                        }
                        
                        temp_num_nodes_1 = GC_info4.get(k)+1.0;
                        GC_info4.set(k, temp_num_nodes_1);
                        
                        avg_bfs_size = (double)(calc_BFS()/(double)numpoints)*100.0;
                        temp_bfs = (double)((avg_bfs_size + GC_info1.get(k)*i)/temp_num_nodes_1);
                        GC_info1.set(k, temp_bfs);
                        
                        if(checkGraphConnected2()){
                            count_connected_ctr = (1.0+GC_info2.get(k));
                            GC_info2.set(k, count_connected_ctr);
                        }
                        
                        total_avg_neighbors = (double)((avgNeighb/(double)numpoints + GC_info3.get(k)*i)/temp_num_nodes_1);
                        GC_info3.set(k, total_avg_neighbors);
                        
                        while(ctr_old == ctr_int){
                            k++;
                            ctr_int = (int)java.lang.Math.round((initial_ctr+(k*step_ctr))*area_side_ctr);
                        }
                        ctr_old = ctr_int;
                        jTextField2.setText(""+ctr_int);
                        jLabel77.setText("Current CTR: "+ctr_int);
                        jTextArea2.append(""+ctr_int+","+GC_info1.lastElement()+","+GC_info2.lastElement()+","+GC_info3.lastElement()+"\n");
  
                    }
                    
                }
                
                for(i=0;i<100;i++){
                    if(GC_info4.get(i)==0.0){
                        GC_info1.set(i,GC_info1.get(i-1));
                        GC_info2.set(i,GC_info2.get(i-1));
                        GC_info3.set(i,GC_info3.get(i-1));
                        GC_info4.set(i,GC_info4.get(i-1));
                    }
                }
                
                for(i=0;i<100;i++){
                    count_connected_ctr = (double)(GC_info2.get(i)/GC_info4.get(i))*100.0;
                    GC_info2.set(i,count_connected_ctr);
                }
                */
                
                if(jRadioButton1.isSelected()){
                    //GC on Dense Networks

                     jFileChooser1.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
         
                     l = jFileChooser1.showOpenDialog(this);


                    if (l == JFileChooser.APPROVE_OPTION) {
                        myFile = jFileChooser1.getSelectedFile();
                
                        //Generate the topologies here according to the definitions
        
                    
                        //Regular n nodes
                        jTextField1.setText(""+(num_nodes_ctr));

                        //Communication radius
                        jTextField2.setText(""+(comm_radius_ctr));

                        //Setting the side of the square deployment area 
                        jTextField31.setText(""+area_side_ctr);
                        jTextField32.setText(""+area_side_ctr);

                        //Reseting the creations words
                        clean_creation_words();

                        //Adding the regular nodes
                        addNodeCreationWord();

                        //Adding one sink in a random position
                        jCheckBox22.setSelected(true);
                        //No sinks required for this operation
                        jTextField1.setText("0");
                        addNodeCreationWord();
                        jCheckBox22.setSelected(false);

                        //Set number of topologies in the Batch Creation field
                        jTextField16.setText(""+num_topologies_ctr);
                        //Enable the Batch Creation Field
                        jCheckBox16.setSelected(true);
                        //Disable the option for stoping creation after max trials
                        jCheckBox24.setSelected(false);
                        //Disable checking for connectivity
                        jCheckBox7.setSelected(false);



                        jLabel18.setText("Creating Topologies...");

                        if(myFile.isDirectory())    
                            the_path = myFile.getPath();
                        else
                            the_path = myFile.getParent();
                            
                        t = new timestamp();
                        tempString = the_path+"\\test_"+t.toString2();
                        tempFile = new File(tempString);

                        if(tempFile.mkdir())
                            the_path = tempFile.getPath();



                        smallexec.setParamsForBatchCreation(num_topologies_ctr, the_path);
                        smallexec.setWork(GENERATE_TOPOS);
                        smallexec.setEnd();
                        jProgressBar2.setMaximum(num_topologies_ctr);
                        jProgressBar2.setValue(0);
                        

                        try{
                            sem_GC.acquire();
                        }catch(Exception ex){ex.printStackTrace();}

                        myFiles = tempFile.listFiles();
                        
                        jLabel18.setText("Calculating GC test...");
                       

                        while(avg_bfs_size<100){
                            count_connected_ctr=0;
                            temp_bfs=0;
                            avg_bfs_size=0;
                            num_topologies_ctr=0;

                            for(i=0;i<myFiles.length;i++){
                                
                                if(filter.accept(myFiles[i])){
                                
                                    LoadDeployment(myFiles[i].getPath(), ctr_int);

                                    if(jCheckBox25.isSelected())
                                        total_avg_neighbors += avgNeighb/(double)numpoints;
                                    else
                                        total_avg_neighbors += avgNeighb;

                                    if(checkGraphConnected())
                                        count_connected_ctr++;
                                    temp_bfs += calc_BFS();
                                    
                                    num_topologies_ctr++;
                                    
                                }

                            }
                            
                            avg_bfs_size = (double)((temp_bfs/i)/(double)numpoints)*(double)(100.0);
                            percent_count_connected_ctr = (double)((count_connected_ctr/i)*(double)(100.0));
                            if(jCheckBox25.isSelected())
                                total_avg_neighbors = (total_avg_neighbors/((double)num_topologies_ctr))*100.0;
                            else
                                total_avg_neighbors = (total_avg_neighbors/((double)num_topologies_ctr));

                            GC_info1.add(avg_bfs_size);
                            GC_info2.add(percent_count_connected_ctr);
                            GC_info3.add(total_avg_neighbors);

                            while(ctr_old == ctr_int){
                                j++;
                                ctr_int = (int)java.lang.Math.round((initial_ctr+(j*step_ctr))*area_side_ctr);
                            }
                            ctr_old = ctr_int;
                            jTextField2.setText(""+ctr_int);
                            jLabel77.setText("Current CTR: "+ctr_int);
                            jTextArea2.append(""+ctr_int+","+GC_info1.lastElement()+","+GC_info2.lastElement()+","+GC_info3.lastElement()+"\n");

                        }

                        try{
                            
                            fout = new FileWriter(the_path+"\\GC_"+t.toString2()+".csv");
                            mywrite = new BufferedWriter(fout);
                            mywrite.write("Radius, MaxComponentSize, ConnectedTopologies, AverageNodeDegree\n");

                            temp_bfs = GC_info1.size();
                            for(i=0;i<temp_bfs;i++){
                                ctr_int = (int)java.lang.Math.round((initial_ctr+(i*step_ctr))*area_side_ctr);
                                mywrite.write(""+ctr_int+","+GC_info1.get(i)+","+GC_info2.get(i)+","+GC_info3.get(i)+"\n");
                            }

                            mywrite.flush();
                            mywrite.close();
                            fout.close();

                        }catch(Exception ex){ex.printStackTrace();}

                        printmessage("GC stat created!!");

                    } else {
                        showMessage("Open command cancelled by user.");
                    }
         
                     jFileChooser1.setFileSelectionMode(javax.swing.JFileChooser.FILES_AND_DIRECTORIES);
                
            } else if(jRadioButton2.isSelected()){
                //GC on Sparse Networks
        
                jFileChooser1.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
         
                 l = jFileChooser1.showOpenDialog(this);


                if (l == JFileChooser.APPROVE_OPTION) {
                    myFile = jFileChooser1.getSelectedFile();
                

                    total_experiments = java.lang.Math.round((final_i_exp_l_ctr-initial_i_exp_l_ctr+1)*((final_k_ctr-initial_k_ctr+step_k_ctr)/step_k_ctr));

                    di = initial_i_exp_l_ctr;
                    
                    while(di <= final_i_exp_l_ctr){

                        
                        //Updating parameters L y n, or dl and dn.
                        dl = java.lang.Math.pow(2,(2*di));
                        jLabel20.setText("L=2^(2*i) = "+dl);
                        dk = initial_k_ctr;
                        dn = java.lang.Math.round(java.lang.Math.sqrt(dl));
                        jLabel23.setText("n = sqrt(l) = "+dn);
                        
                        //Setting the side of the square deployment area 
                        jTextField31.setText(""+(int)dl);
                        jTextField32.setText(""+(int)dl);
                        
                        while (dk <= final_k_ctr){

                            //Reseting the counter values for the combination of l and k.
                            count_connected_ctr=0;
                            temp_bfs=0;
                            avg_bfs_size=0;
                            total_avg_neighbors=0;

                            //Updating the radius because it depends on K, or dk in this case    
                            dr = java.lang.Math.round(dk * java.lang.Math.pow(dl,0.75)*java.lang.Math.sqrt(java.lang.Math.log(dl)/java.lang.Math.log(2)));
                            jLabel39.setText("r = k*l^(3/4)*sqrt(log2(l)) = "+dr);
                            
                            //Communication radius
                            jTextField2.setText(""+(int)dr);

                            //Regular n nodes... need to update here because the 0 I had to put for the sink creation word.
                            jTextField1.setText(""+(int)dn);
                            
                            //Reseting the creations words
                            clean_creation_words();

                            //Adding the regular nodes
                            addNodeCreationWord();

                            //Adding one sink in a random position
                            jCheckBox22.setSelected(true);
                            //No sinks required for this operation
                            jTextField1.setText("0");
                            addNodeCreationWord();
                            jCheckBox22.setSelected(false);

                            //Disable the option for stoping creation after max trials
                            jCheckBox24.setSelected(false);
                            //Enable checking for connectivity, just in case it was disabled
                            jCheckBox7.setSelected(true);

                            for(i=0; i<num_topologies_ctr; i++){

                                clear_all_sim();
                                sw = NodeGenerator();

                                if(sw)
                                    count_connected_ctr++;

                                temp_bfs += calc_BFS();

                                if(jCheckBox25.isSelected())
                                        total_avg_neighbors += avgNeighb/(double)numpoints;
                                    else
                                        total_avg_neighbors += avgNeighb;

                                jLabel18.setText("Calculating GC test: Evaluating "+(GC_info1.size()+1)+" of "+total_experiments);
                                
                            }

                            avg_bfs_size = (temp_bfs/(dn*num_topologies_ctr))*(100.0);
                            percent_count_connected_ctr = (count_connected_ctr/((double)num_topologies_ctr))*(100.0);
                            if(jCheckBox25.isSelected())
                                total_avg_neighbors = (total_avg_neighbors/((double)num_topologies_ctr))*(100.0);
                            else
                                total_avg_neighbors = (total_avg_neighbors/((double)num_topologies_ctr));

                            GC_info1.add(avg_bfs_size);
                            GC_info2.add(percent_count_connected_ctr);
                            GC_info3.add(total_avg_neighbors);

                            dk+=step_k_ctr;
                        }
                    
                        di++;
                        
                    }


                try{
                    t = new timestamp();
                    fout = new FileWriter(myFile.getPath()+"\\GC_"+t.toString2()+".csv");
                    mywrite = new BufferedWriter(fout);
                    
                    i=0;
                    dk = initial_k_ctr;
                    while(dk <= final_k_ctr){
                        
                        tempString = tempString+",GC K="+dk;
                        
                        dk+=step_k_ctr;   
                    }
                    
                    dk = initial_k_ctr;
                    while(dk <= final_k_ctr){
                        
                        tempString = tempString+",CT K="+dk;
                        
                        dk+=step_k_ctr;   
                    }
                    
                    dk = initial_k_ctr;
                    while(dk <= final_k_ctr){
                        
                        tempString = tempString+",AvgNodeDegree K="+dk;
                        
                        dk+=step_k_ctr;   
                    }
                    
                    //Header with the values of K
                    mywrite.write(tempString+"\n");
                    
                    i=0;
                    j=0;
                    k=0;
                    
                    di = initial_i_exp_l_ctr;
                    while(di <= final_i_exp_l_ctr){
                        
                        dl = java.lang.Math.pow(2,(2*di));
                        
                        tempString="";
                        
                        dk = initial_k_ctr;
                        while (dk <= final_k_ctr){
    
                            tempString = tempString+","+GC_info1.get(i);
                            
                            i++;
                            dk+=step_k_ctr;    
                            
                        }
                        
                        dk = initial_k_ctr;
                        while (dk <= final_k_ctr){
    
                            tempString = tempString+","+GC_info2.get(j);
                            
                            j++;
                            dk+=step_k_ctr;   
                            
                        }
                        
                        dk = initial_k_ctr;
                        while (dk <= final_k_ctr){
    
                            tempString = tempString+","+GC_info3.get(k);
                            
                            k++;
                            dk+=step_k_ctr;   
                            
                        }
                        
                        mywrite.write(""+dl+tempString+"\n");
                        di++;
                        
                    }
                    
                    mywrite.write("\n\nTable of Communication Radius\n");
                    mywrite.write("\nL,n,K\n");
                    tempString="";
                    
                    dk = initial_k_ctr;
                    while (dk <= final_k_ctr){
                        tempString = tempString+dk+",";
                        dk+=step_k_ctr;   
                    }
                    mywrite.write(" , ,"+tempString+"\n");
                    
                    di = initial_i_exp_l_ctr;
                    while(di <= final_i_exp_l_ctr){
                        dk = initial_k_ctr;
                        dl = java.lang.Math.pow(2,(2*di));
                        dn = java.lang.Math.sqrt(dl);
                        
                        tempString="";
                        
                        while (dk <= final_k_ctr){
    
                            dr = java.lang.Math.round(dk * java.lang.Math.pow(dl,0.75)*java.lang.Math.sqrt(java.lang.Math.log(dl)/java.lang.Math.log(2)));
                            tempString = tempString+","+dr;
                            
                            dk+=step_k_ctr;   
                            
                        }
                        
                        mywrite.write(""+dl+","+dn+tempString+"\n");
                        di++;
                        
                    }
                    
                    
                    mywrite.flush();
                    mywrite.close();
                    fout.close();
                }catch(Exception ex){ex.printStackTrace();}

                printmessage("GC stat created!!");

            } else {
                        showMessage("Open command cancelled by user.");
                    }
            
            }

        batch_simulation=false;
        GC_Test = false;
        //no_paint = true;
        clear_all_sim();
        
    }
    
    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
        
        update_ctr();
        
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // TODO add your handling code here:
        
        setInterqueryTime();
        
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // TODO add your handling code here:
        
        ExecuteMST();
        jCheckBox26.setSelected(true);
        
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jCheckBox24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox24ActionPerformed
        // TODO add your handling code here:
        
        if(jCheckBox24.isSelected())
            checkTrials = true;
        else
            checkTrials = false;
        
    }//GEN-LAST:event_jCheckBox24ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // TODO add your handling code here:
        
        int i=0, j=0, tam=0, l=0;
        int[] vect = new int[numpoints];
        String neighb="";
        String the_path="";
        String the_name3="";
        timestamp t;

        jFileChooser1.setMultiSelectionEnabled(false);
         l = jFileChooser1.showOpenDialog(this);
         
        
        if (l == JFileChooser.APPROVE_OPTION) {
                myFile = jFileChooser1.getSelectedFile();
                
                if(myFile.isFile())
                    the_path = myFile.getParent();
                else
                    the_path = myFile.getPath();
                
                try{
                    t = new timestamp();
                    
                    the_name3 = report_descriptor + "_" + numnodes + "_" + numsinks + "_" + w + "_" + h + "_" + t.toString2();
                    
                    fout = new FileWriter(the_path+"\\neighbohoods"+the_name3+".csv");

                    mywrite = new BufferedWriter(fout);


                    for(i=0;i<numpoints;i++){

                        neighb="";
                        for(j=0;j<numpoints;j++)
                            vect[j]=0;

                        tam = mynodes.get(i).getNumNeighbors();
                        for(j=0;j<tam;j++)
                            vect[mynodes.get(i).getNeighbor(j).getID()]=1;

                        for(j=0;j<numpoints;j++)
                            neighb=neighb+vect[j]+",";

                        mywrite.write(neighb+"\n");
                    }

                    mywrite.flush();
                    mywrite.close();
                    fout.close();

                    printmessage("Neighborhoods saved");

            }catch(Exception ex){ex.printStackTrace();}

        
        }
        
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        
     
        
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        // TODO add your handling code here:
        
        myPanel.setAtarraya(jCheckBox1.isSelected());
        
    }//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void jCheckBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox3ItemStateChanged
        // TODO add your handling code here:
        myPanel.setActiveNodes(jCheckBox3.isSelected());
    }//GEN-LAST:event_jCheckBox3ItemStateChanged

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        // TODO add your handling code here:
        
        jFileChooser1.setMultiSelectionEnabled(true);
         int i = jFileChooser1.showOpenDialog(this);
         int j;
        
        if (i == JFileChooser.APPROVE_OPTION) {
                myFiles = jFileChooser1.getSelectedFiles();
                //This is where a real application would open the file.
                for(j=0;j<myFiles.length;j++)
                    TransformReport2(myFiles[j].getName(), myFiles[j].getParent());
                printmessage("Transfomed Reports!!");
                
            } else {
                showMessage("Open command cancelled by user.");
            }
         
         jFileChooser1.setMultiSelectionEnabled(false);
        
    }//GEN-LAST:event_jButton21ActionPerformed

private void jCheckBox26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox26ActionPerformed
// TODO add your handling code here:
    frame_repaint();
}//GEN-LAST:event_jCheckBox26ActionPerformed

private void jComboBox9ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox9ItemStateChanged
    // TODO add your handling code here:
    
    selected_COMMprotocol = jComboBox9.getSelectedIndex();
        
        if(selected_COMMprotocol == 0){
            COMM_Selected=false;
            if(!TM_Selected && !SD_Selected && TC_Selected)
                jButton1.setEnabled(true);
            else
                jButton1.setEnabled(false);
        }else{
            COMM_Selected=true;
            if(TM_Selected && SD_Selected && TC_Selected)
                jButton1.setEnabled(true);
            else
                jButton1.setEnabled(false);
        }
    
}//GEN-LAST:event_jComboBox9ItemStateChanged

private void jCheckBox10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox10ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_jCheckBox10ActionPerformed

private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
    // TODO add your handling code here:
    int temp=myPanel.selected_node;
    int temp2 = 0;

    if(temp!=-1){
        temp2 = getNode(temp).getActiveTree();
        if(getNode(temp).isActive(temp2)){
            getNode(temp).getInfrastructure(temp2).setSleepingNodeState();
        }else if(getNode(temp).isSleeping(temp2))
            getNode(temp).getInfrastructure(temp2).setActiveNodeState();

        myPanel.repaint();

    }

}//GEN-LAST:event_jButton9ActionPerformed

private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
    // TODO add your handling code here:

    int i=BFS(BFS_MODE_STANDARD, numnodes);
    int j=0, temp_level=0,l;
    String st="";

    for(i=0;i<numpoints;i++){
        temp_level=getNode(i).getLevelBFS();
        for(j=0;j<numpoints;j++){
            if(j!=temp_level)
                st = st+"0,";
            else
                st = st+"1,";
        }
        st = st+"\n";
    }

    String the_path="";
        String the_name3="";
        timestamp t;


        jFileChooser1.setMultiSelectionEnabled(false);
         l = jFileChooser1.showOpenDialog(this);


        if (l == JFileChooser.APPROVE_OPTION) {
                myFile = jFileChooser1.getSelectedFile();

                if(myFile.isFile())
                    the_path = myFile.getParent();
                else
                    the_path = myFile.getPath();

                try{
                    t = new timestamp();

                    the_name3 = report_descriptor + "_" + numnodes + "_" + numsinks + "_" + w + "_" + h + "_" + t.toString2();

                    fout = new FileWriter(the_path+"\\BFS_Matrix_"+the_name3+".csv");

                    mywrite = new BufferedWriter(fout);
                    mywrite.write(st);

                    mywrite.flush();
                    mywrite.close();
                    fout.close();
                    
                }catch(Exception ex){ex.printStackTrace();}

                printmessage("BFS matrix saved");

        }

}//GEN-LAST:event_jButton22ActionPerformed


public void CreatePositionFiles(){

    int i=0, k=0;
    int j=0,l=0;
    int numfiles=0;
    String the_path="";
    String the_name3="";
    timestamp t;
    String st="";
    String temp, the_file;
    String[] temp_read;
    int positionx, positiony, temp_trees, temp_num_sinks;


    jFileChooser1.setMultiSelectionEnabled(true);
     k = jFileChooser1.showOpenDialog(this);


    if (k == JFileChooser.APPROVE_OPTION) {
        myFiles = jFileChooser1.getSelectedFiles();

        numfiles = myFiles.length;

        for(i=0;i<numfiles; i++){

            try{
                fin = new FileReader(myFiles[i]);
                myread = new BufferedReader(fin);

                fout = new FileWriter(myFiles[i].getParent()+"\\Positions_" + myFiles[i].getName()+".csv");
                mywrite = new BufferedWriter(fout);

                temp = myread.readLine();

                if(temp!=null){

                    numnodes = Integer.parseInt(temp);
                    jLabel6.setText(""+numnodes);

                    temp = myread.readLine();
                    temp_trees = Integer.parseInt(temp);

                    numsinks=0;
                    for(l=0;l<temp_trees;l++){
                        temp = myread.readLine();
                        temp_num_sinks = Integer.parseInt(temp);
                        num_sinks_per_tree[l] = temp_num_sinks;
                        numsinks+=temp_num_sinks;
                    }

                    temp = myread.readLine();
                    radius = Double.parseDouble(temp);
                    //jLabel7.setText(""+radius);

                    temp = myread.readLine();
                    sense_radius = Double.parseDouble(temp);

                    temp = myread.readLine();
                    //h = Integer.parseInt(temp);

                    temp = myread.readLine();
                    //w = Integer.parseInt(temp);

                    //Updating range areas for visual effects
                    //myPanel.setRangeArea(false, false);

                    //Updating the total number of points in the area = nodes + sinks
                    numpoints = numnodes + numsinks;

                    st=""+numpoints+"\n"+radius+"\n"+sense_radius+"\n";

                    for(j=0;j<numpoints;j++){

                        temp = myread.readLine();
                        temp_read = temp.split("@");

                        positionx = Integer.parseInt(temp_read[0]);
                        positiony = Integer.parseInt(temp_read[1]);

                        st = st+positionx+","+positiony+"\n";

                    }

                    mywrite.write(st);

                    mywrite.flush();
                    mywrite.close();
                    fout.close();

                    fin.close();
                }

            }catch(Exception e){e.printStackTrace();}

        }

        printmessage("Positions List saved");

    }

}

private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
    // TODO add your handling code here:

    the_batch_sim.setWork(POSITIONFILES);
    
}//GEN-LAST:event_jButton23ActionPerformed

private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
    // TODO add your handling code here:

    selected_Motionprotocol = jComboBox3.getSelectedIndex();
    for(int i=1;i<sender_node;i++)
                     {

                     getNode(i).setMoving(true);
                    }

}//GEN-LAST:event_jComboBox3ItemStateChanged

private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed

    // TODO add your handling code here:
        // Try to export an image from JPanel
    jFileChooser2.setMultiSelectionEnabled(false);
    int i = jFileChooser2.showSaveDialog(this);

    if (i == JFileChooser.APPROVE_OPTION) {
        myFile = jFileChooser2.getSelectedFile();
        //This is where a real application would open the file.
        myPanel.saveComponentAsJPEG(myFile.getPath()+".jpeg");

    } else {
        showMessage("Open command cancelled by user.");
    }
    
}//GEN-LAST:event_jMenuItem4ActionPerformed

public void reset_topology(){

    int i=0;

    //Restart every node on the topology
    for(i=0;i<numpoints;i++){
        getNode(i).reset_all_node();
    }
    for(i=0;i<numpoints;i++){
        BuildNeighborhood(i);
    }
    //Repaint the topology
    myPanel.repaint();

    //Notify Atarraya that a new topology has been loaded
    topology_loaded = true;

    //Notify Atarraya that this will be the first atempt to start this topology... remember that
    // when the Start Atarraya button is pressed several times, it will try to start each of the
    // possibles VNI on the topology. Bu turning this value to -1, the first one will the VNI 0.
    treeID = -1;

}

private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
    // TODO add your handling code here:

    reset_topology();

}//GEN-LAST:event_jMenuItem5ActionPerformed

private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
    // TODO add your handling code here:

    num_topology_from_batch=0;
    myFiles = null;
    batch_simulation = false;
    jLabel47.setText("0 files selected");

}//GEN-LAST:event_jButton8ActionPerformed

private void jComboBox13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox13ActionPerformed
    // TODO add your handling code here:

    selected_energy_model = jComboBox13.getSelectedIndex();

    switch(selected_energy_model){

        case SIMPLE_ENERGY_MODEL:
            jLabel51.setText("Node Energy Distribution (mJoules)");
            jTextField25.setText("1000");
            jTextField20.setText("1000");
            MAX_ENERGY = MAX_ENERGY_SIMPLE;
            jTextField46.setText(""+MAX_ENERGY);
            break;

        case MOTE_ENERGY_MODEL:
            jLabel51.setText("Node Energy Distribution (mA-h)");
            jTextField25.setText("3200");
            jTextField20.setText("3200");
            MAX_ENERGY = MAX_ENERGY_MOTE;
            jTextField46.setText(""+MAX_ENERGY);
            break;
    

    }

}//GEN-LAST:event_jComboBox13ActionPerformed

private void jTextField25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField25ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField25ActionPerformed

private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
    // TODO add your handling code here:

     if(this.topology_loaded)
        the_batch_sim.setWork(OPTIMALSOLUTION);
    else
        the_batch_sim.setWork(OPTIMALSOLUTIONBATCH);

}//GEN-LAST:event_jButton24ActionPerformed

private void jCheckBox28ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox28ItemStateChanged

    // TODO add your handling code here:

    save_results_log = jCheckBox28.isSelected();

}//GEN-LAST:event_jCheckBox28ItemStateChanged

private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
    // TODO add your handling code here:

    try{
        MAX_ENERGY = Double.parseDouble(jTextField46.getText());
    }catch(Exception ex){ex.printStackTrace();}

    myPanel.repaint();

}//GEN-LAST:event_jButton25ActionPerformed

private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
    // TODO add your handling code here

                  
                   
}//GEN-LAST:event_jButton26ActionPerformed

private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
    // TODO add your handling code here:
    CHOOSE_BASE_STATION=true;
}//GEN-LAST:event_jButton28ActionPerformed

private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
    // TODO add your handling code here:
    graph_selection=true;
    CHOOSE_BASE_STATION=false;
}//GEN-LAST:event_jButton29ActionPerformed

private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
    // TODO add your handling code here:
    
    double spent=0;
    for(int i=0;i<getVariable(NUMPOINTS);i++)
    {
        spent=spent+getNode(i).ENERGY_SPENT_FOR_CLUSTRING;
    }
    BarChart chart=new BarChart("Total Energy Consumption for Clustering:",new String[]{"Energy Spent for Clustering"},"", new double[]{spent});
    chart.setVisible(true);
}//GEN-LAST:event_jButton27ActionPerformed

private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
    // TODO add your handling code here:
      double spent=0;
    for(int i=0;i<getVariable(NUMPOINTS);i++)
    {
        spent=spent+getNode(i).ENERGY_SPENT_FOR_DATA_TRANSFER;
    }
    BarChart chart=new BarChart("Total Energy Consumption for Data Transfer:",new String[]{"Energy Spent for Data Transfer"},"", new double[]{spent});
    chart.setVisible(true);
}//GEN-LAST:event_jButton30ActionPerformed

private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
    // TODO add your handling code here:
    double total=getNode(GRAPH_NODE_ID).TOTAL_ENERGY;
    double energy_clustering=getNode(GRAPH_NODE_ID).ENERGY_SPENT_FOR_CLUSTRING;
    double energy_data=getNode(GRAPH_NODE_ID).ENERGY_SPENT_FOR_DATA_TRANSFER;
    double spent=total-energy_clustering-energy_data;
    BarChart chart=new BarChart("Energy Consumption Analyis of Node:"+GRAPH_NODE_ID,new String[]{"Total Initial Energy","Energy Spent for Clustering","Energy Spent for Data Transfer","Total Energy Spent"},"Node:"+GRAPH_NODE_ID+" Remaining Energy",new double[]{total,energy_clustering,energy_data,spent});
    chart.setVisible(true);



}//GEN-LAST:event_jButton31ActionPerformed

private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
    // TODO add your handling code here:

    BarChart1 chart=new BarChart1("Dead Nodes List:"+GRAPH_NODE_ID,new String[]{"Dead Nodes"},"",new double[]{atarraya_frame.DEAD_NODES});
    chart.setVisible(true);


}//GEN-LAST:event_jButton32ActionPerformed

public void applySolution(solution sol){

        int i=0;

        for(i=0; i<numpoints; i++){
            getNode(i).setNodeStateBFS(sol.get(i));
        }

    }

    public void showSolution(solution sol){

        int i=0, numneigh=0, j=0, temp_id=0;


        // Updating the state in tree 0 to show on the interface
        for(i=0; i<numpoints; i++){
            getNode(i).setNodeStateBFS(sol.get(i));
            getNode(i).defineLabels(S_INACTIVE_BFS, S_ACTIVE_BFS, S_INACTIVE_BFS, S_INACTIVE_BFS);
            if(sol.get(i) == S_ACTIVE_BFS){
                getNode(i).setParentState(0, true);
            }else
                getNode(i).setParentState(0, false);
        }

        for(i=0;i<numpoints;i++){
            if(getNode(i).isActive(0)){
                numneigh = getNode(i).getNumNeighbors();
                for(j=0;j<numneigh;j++){
                temp_id = getNode(i).getNeighborID(j);
                    getNode(i).addGateway(0, temp_id, temp_id);
                    getNode(temp_id).setDefaultGateway(0, i);
                }
            }
        }
        treeIDViz=0;
        jCheckBox26.setSelected(true);
        myPanel.repaint();
    }

public void SolveBatchCDS(){

         jFileChooser1.setMultiSelectionEnabled(true);
         int i = jFileChooser1.showOpenDialog(this);
         int j,k;
         Vector<solution> temp_sols;
         timestamp time = new timestamp();
         batch_simulation = true;

        if (i == JFileChooser.APPROVE_OPTION) {
                myFiles = jFileChooser1.getSelectedFiles();

                try{
                    fout = new FileWriter(myFiles[0].getParent()+"\\CDS_Optimal_"+myFiles.length+"_"+time.toString2()+".csv");
                    mywrite = new BufferedWriter(fout);

                    //This is where a real application would open the file.
                    for(j=0;j<myFiles.length;j++){
                        LoadDeployment(myFiles[j].getPath());
                        temp_sols = SolveCDS2();

                        for(k=0;k<temp_sols.size();k++){
                            mywrite.write(myFiles[j].getName()+","+temp_sols.get(k).toString()+temp_sols.get(k).getSum());
                            mywrite.newLine();
                        }
                    }

                    mywrite.flush();
                    fout.flush();
                    fout.close();

                    printmessage("All experiments done!");
               }catch(Exception ex){ex.printStackTrace();}

            } else {
                showMessage("Open command cancelled by user.");
            }

         jFileChooser1.setMultiSelectionEnabled(false);
        batch_simulation = false;
        clear_all_sim();

    }

public double fact(int n){

    int i=1;
    double temp=1;

    for(i=1;i<n;i++)
        temp*=i;

    return temp;
}

public double calc_combinations(int k, int n){

    return fact(n)/(fact(k)*fact(n-k));

}

     public Vector<solution> SolveCDS2(){

        long count_eval_sols=0;
        long count_eval_sols_2=0;
        int count_temp_sols=0;
        int i=0;
        int min_size = numpoints;
        solution new_sol = new solution(numpoints);
        Vector<solution> best_solutions = new Vector<solution>();

        java.text.DecimalFormat df = new java.text.DecimalFormat("#,##0.##E0");
        java.text.DecimalFormat df2 = new java.text.DecimalFormat("#,##0");

        double temp_combs;
        
        boolean sw=true, sw2=true, sw_just_min = jCheckBox27.isSelected();

        //Vector<Integer> pointers = new Vector<Integer>(numpoints);
        int[] pointers = new int[numpoints];

        for(i=0; i<numpoints; i++)
            pointers[i]=-1;

        int j=0;

        int higher_elem = numnodes;
        int lower_elem = 1;
        int current_elements = (numnodes+1)/2;
        int temp_pointer = current_elements-1;

        while(higher_elem >= lower_elem){
            temp_combs = calc_combinations(current_elements,numnodes);
            if(best_solutions.size() == 0)
                jLabel18.setText("Current n: "+current_elements+", Combs: "+df.format(temp_combs));
            else
                jLabel18.setText("Current n: "+current_elements+", Combs: "+df.format(temp_combs)+", Best n found: "+best_solutions.get(0).getSum());


            //Initializing the values of the pointers: 0,1,2,3,...
            for(j=0;j<current_elements;j++){
                pointers[j]= j;
            }
            sw=true;
            count_temp_sols=0;
            while(sw){

                //Fill up with zeros
                new_sol.clear();

                //in here I create the solution based on the values in the pointers, according to the number of elements
                for(j=0;j<current_elements;j++){
                    new_sol.set(pointers[j],1);
                }

                if(new_sol.getSum() <= min_size){
                    // Update the node states of the nodes, based on the solution
                    applySolution(new_sol);
                    //Check the solution for coverage and connectivity
                    count_eval_sols++;
                    if(checkSolution(new_sol.getSum())){
                        count_eval_sols_2++;
                        if(new_sol.getSum() < min_size){
                            // The new solution was better than the previous one
                            best_solutions.clear();
                            // Update the size of the minimum solution
                            min_size = new_sol.getSum();

                            if(sw_just_min)
                                sw=false;
                        }
                        // Add the new optimal solution
                        best_solutions.add(new solution(new_sol));
                        count_temp_sols++;

                    }
                }

                //Update the
                pointers[temp_pointer]++;

                // If the pointer reached the limit
                if(pointers[temp_pointer] == numnodes){
                    j=2;
                    temp_pointer--;
                    sw2=true;
                    while(temp_pointer >=0 && sw2){
                        if(pointers[temp_pointer] == numnodes - j){
                            temp_pointer--;
                            j++;
                        }else{
                            sw2=false;
                        }
                    }

                    if(temp_pointer == -1)
                        sw=false;
                    else{
                        pointers[temp_pointer]++;
                        for(i=temp_pointer+1; i<current_elements;i++){
                            pointers[i] = pointers[i-1]+1;
                        }
                        temp_pointer=current_elements-1;
                    }
                }


            }// While(sw) which explores all the combinations of sets of suze current_element

            

            if(count_temp_sols==0){
                lower_elem = current_elements+1;
            }else{
                higher_elem = current_elements-1;
            }

            current_elements = (higher_elem + lower_elem)/2;

            temp_pointer = current_elements-1;
            

        }
        if(best_solutions.size() > 0 && !batch_simulation){
            jLabel18.setText("Current n: "+best_solutions.get(0).getSum()+", Combs: "+calc_combinations(current_elements,numnodes)+", Best n found: "+best_solutions.get(0).getSum());
            printmessage("<html>Finished - Found "+best_solutions.size()+" solutions with "+best_solutions.get(0).getSum()+" active nodes, <br> after evaluating "+
                df2.format(count_eval_sols)+" potential solutions and found "+df2.format(count_eval_sols_2)+" candidate solutions</html>");
            showSolution(best_solutions.get(0));
            jCheckBox1.setSelected(true);
            jCheckBox3.setSelected(true);
        }

        return best_solutions;

    }


    public void SolveCDS(){

        int count_eval_sols=0;
        int count_eval_sols_2=0;
        int i=0;
        int min_size = numpoints;
        int max_solutions = (int)java.lang.Math.pow(2, numnodes);
        solution new_sol;
        Vector<solution> best_solutions = new Vector<solution>();

        for(i=0;i<max_solutions;i++){

            new_sol = generateSolution(i);
            if(new_sol.getSum() <= min_size){
                // Update the node states of the nodes, based on the solution
                applySolution(new_sol);
                //Check the solution for coverage and connectivity
                count_eval_sols++;
                if(checkSolution(new_sol.getSum())){
                    count_eval_sols_2++;
                    if(new_sol.getSum() < min_size){
                        // The new solution was better than the previous one
                        best_solutions.clear();
                        // Update the size of the minimum solution
                        min_size = new_sol.getSum();
                    }
                    // Add the new optimal solution
                    best_solutions.add(new_sol);

                }
            }

        }
        this.printmessage("<html>Finished - Found "+best_solutions.size()+" solutions with "+min_size+" active nodes, <br> after evaluating "+
                count_eval_sols+" potential solutions and found "+count_eval_sols_2+" candidate solutions</html>");
        if(best_solutions.size() > 0)
            showSolution(best_solutions.get(0));
    }

    public solution generateSolution(int num){

        solution new_sol = new solution(numpoints);

        int temp=0;
        int temp_num = num;

        // Generate Solution!!

        for(int i=numnodes-1; i>=0; i--){
            temp = temp_num/(int)java.lang.Math.pow(2, i);
            temp_num-= temp*(int)java.lang.Math.pow(2, i);
            new_sol.set(i, temp);
        }

        return new_sol;

    }

    public boolean checkSolution(int total_active){

        int count=0, i=0, numneigh=0, j=0, temp=0, temp_id=0;
        boolean sw=true;


        //Checking if every node has at least one active node on its neghborhood
        while(i<numnodes && sw){
            temp=0;
            numneigh = getNode(i).getNumNeighbors();
            for(j=0;j<numneigh;j++){
                temp_id = getNode(i).getNeighborID(j);
                if(getNode(temp_id).isActive(-1))
                    temp++;
            }
            //There is a node with no active neighbors, so it is not covered!
            if(temp == 0)
                sw=false;
            i++;
        }

        if(sw){
            //Check the size of the BFS of active nodes.
            count = BFS(BFS_MODE_ACTIVE_ALIVE_OPTIMAL);
            if(count < total_active)
                return false;
            else
                return true;
        }
        return false;
    }
    
    public void ExecuteMST(){
    
        int i,j,tam,temp_id;
        double temp_dist;
        edge temp_edge;
        
        Vector<edge> edges_list = new Vector<edge>();
        Vector<edge> selected_edges_list = new Vector<edge>();
        
        for(i=0;i<numpoints;i++){
            getNode(i).setFragmentID(i);
            
            tam = getNode(i).getNumNeighbors();
            
            for(j=0;j<tam;j++){
            
                temp_id = getNode(i).getNeighborID(j);
                
                if(temp_id>i){
                    temp_dist = getNode(i).getNeighborDistance(j);
                    
                    edges_list.add(new edge(i,temp_id, temp_dist));
                }
                
                
            }
            
        }
        
        java.util.Collections.sort(edges_list);
    
        while(edges_list.size() > 0){
            
            temp_edge = edges_list.firstElement();
            
            if(getNode(temp_edge.getX()).getFragmentID() != getNode(temp_edge.getY()).getFragmentID()){
                selected_edges_list.add(temp_edge);
                updateFragment(getNode(temp_edge.getX()).getFragmentID(), getNode(temp_edge.getY()).getFragmentID());
                getNode(temp_edge.getX()).addGateway(0, temp_edge.getY(),0);
                getNode(temp_edge.getY()).addGateway(0, temp_edge.getX(),0);
            }
            
            edges_list.removeElementAt(0);
        }
        
        temp_dist=0;
        for(i=0;i<selected_edges_list.size();i++){
            temp_dist+= selected_edges_list.get(i).getZ();
        }
        
        jLabel85.setText("Sum of Edges: "+temp_dist);
        jCheckBox1.setSelected(true);
        
        
        myPanel.repaint();
    }
    
    public void updateFragment(int oldFragment, int newFragment){
        int i;
        
        for(i=0; i<numpoints ; i++){
            if(getNode(i).getFragmentID() == oldFragment)
                getNode(i).setFragmentID(newFragment);
        }
        
    }
    
    
    public void TransformReport2(String the_file, String the_parent_dir){
    
        String temp;
        String temp_time[], temp_bfs[], temp_bfs_coverage_c[], temp_bfs_coverage_s[];
        double temp_ratio,temp_max, temp_val=0, max_time=0, prev_value=0, prev_time=0, temp_time_val=0;
        double prev_value_coverage_c=0, prev_value_coverage_s=0, temp_ratio_coverage_c = 0, temp_ratio_coverage_s = 0;
        double step_time=500.0;
        int i=0, j=0, k=0, temp_lenght, size_r, size_c;
        int count_rows=0;
        double mat[][],mat2[][];
        
        try{
           
            fin = new FileReader(the_parent_dir+"\\"+the_file);
            myread = new BufferedReader(fin);

            // Finding the highest time
            while((temp = myread.readLine()) != null){
                temp_time = temp.split(",");
                temp = myread.readLine();
                temp = myread.readLine();
                temp = myread.readLine();
                temp = myread.readLine();
                
                i = temp_time.length;
                temp_max =  Double.parseDouble(temp_time[i-1]);
                if(temp_max>max_time)
                    max_time = temp_max;

                count_rows++;
            }

            myread.close();
            fin.close();

            step_time = Double.parseDouble(jTextField4.getText());
            size_r = (int)java.lang.Math.ceil(max_time/step_time);
            mat = new double[size_r][4];
            mat2 = new double[size_r][4];
            
            for(i=0;i<size_r;i++){
                mat[i][0]=step_time*i;
                mat2[i][0]=step_time*i;
                //Active nodes
                mat[i][1]=0;
                mat2[i][1]=0;
                //Covered area comm
                mat[i][2]=0;
                mat2[i][2]=0;
                //Covered area sensing
                mat[i][3]=0;
                mat2[i][3]=0;
            }
            
            fin = new FileReader(the_parent_dir+"\\"+the_file);
            myread = new BufferedReader(fin);

            // Finding the average fot each time step
            while((temp = myread.readLine()) != null){
                temp_time = temp.split(",");
                temp = myread.readLine();
                temp = myread.readLine();
                temp_bfs = temp.split(",");
                temp = myread.readLine();
                temp_bfs_coverage_c = temp.split(",");
                temp = myread.readLine();
                temp_bfs_coverage_s = temp.split(",");
                
                temp_lenght =temp_time.length;

                temp_max=1;
                
                
                /*
                 * Based on the assumption that all not-covered nodes will be covered, we considered that if an
                 * active node died before the TC protocol ended, then the nodes that it was going to cover in the future
                 * will be covered by someone else, so we can ignore them from the count.
                 */

                 prev_value = 1;
                 prev_value_coverage_c = 1;
                 prev_value_coverage_s = 1;
                 prev_time = 0;

                 for(i=0;i<size_r;i++){
                    mat2[i][1]=0;
                    mat2[i][2]=0;
                    mat2[i][3]=0;
                 }
                 
                 //For each chunk of time, we will add a proportion of the values based on the time or occurrence in the chunk
                 i=1; //Index for the point in the series
                 j=0; //Index for the row in the matrix mat

                 while(i<temp_lenght){

                    //Getting values of ratio and time from the point
                     
                     temp_ratio = Double.parseDouble(temp_bfs[i]);
                     if(i==1)
                        prev_value = temp_ratio;
                     
                     
                     temp_ratio_coverage_c = Double.parseDouble(temp_bfs_coverage_c[i]);
                     if(i==1)
                            prev_value_coverage_c = temp_ratio_coverage_c;

                     temp_ratio_coverage_s = Double.parseDouble(temp_bfs_coverage_s[i]);
                     if(i==1)
                            prev_value_coverage_s = temp_ratio_coverage_s;

                     
                    temp_time_val = Double.parseDouble(temp_time[i]);

                    
                    //Getting the time chunk in which thid event happened
                    k = (int)(temp_time_val/step_time);
                    if(k==size_r)
                            k--;

                    if(j<k){

                        //Finishing the
                        temp_val = 1-prev_time;
                        mat2[j][1] += prev_value*temp_val;
                        mat2[j][2] += prev_value_coverage_c*temp_val;
                        mat2[j][3] += prev_value_coverage_s*temp_val;
                        j++;

                        while(j<k){
                            //Include the ratio on the chunk on the sum
                            mat2[j][1] = mat2[j-1][1];
                            mat2[j][2] = mat2[j-1][2];
                            mat2[j][3] = mat2[j-1][3];
                            j++;
                        }
                        prev_time=0;
                    }

                    //Including the initial chunk of the value, previous to the current point
                    temp_val = (temp_time_val - (step_time*k))/step_time;
                    mat2[k][1] += prev_value*(temp_val -prev_time);
                    mat2[k][2] += prev_value_coverage_c*(temp_val -prev_time);
                    mat2[k][3] += prev_value_coverage_s*(temp_val -prev_time);

                    prev_value = temp_ratio;
                    prev_value_coverage_c = temp_ratio_coverage_c;
                    prev_value_coverage_s = temp_ratio_coverage_s;
                    prev_time = temp_val;

                    i++;
                 }

                 if(k<size_r){

                     //Including the initial chunk of the value, previous to the current point

                    mat2[k][1] += prev_value*(1-prev_time);
                    mat2[k][2] += prev_value_coverage_c*(1-prev_time);
                    mat2[k][3] += prev_value_coverage_s*(1-prev_time);
                    k++;

                    while(k<size_r){
                        //Include the ratio on the chunk on the sum
                        //mat2[k]=mat2[k-1];
                        mat2[k][1]=0;
                        mat2[k][2]=0;
                        mat2[k][3]=0;
                        k++;
                    }

                }

                 for(i=0;i<size_r;i++){
                    mat[i][1] += mat2[i][1];
                    mat[i][2] += mat2[i][2];
                    mat[i][3] += mat2[i][3];
                 }

            }
            myread.close();
            fin.close();
            
            fout = new FileWriter(the_parent_dir+"\\"+the_file+"_sumary.csv");
            
            mywrite = new BufferedWriter(fout);

            mywrite.write("Time, Active Nodes, Comm Coverage, Sensing Coverage\n");
            for(i=0;i<size_r;i++)
                mywrite.write(""+(mat[i][0])+","+(mat[i][1]/count_rows)+","+(mat[i][2]/count_rows)+","+(mat[i][3]/count_rows)+"\n");

            mywrite.flush();
            mywrite.close();
            fout.close();
            
        }catch(Exception ex){ex.printStackTrace();}
    }
    
    
    public void SaveDeployment(String the_file){
    
        int i;
        int count_trees=0;    
        try{
            
            fout = new FileWriter(the_file);
            mywrite = new BufferedWriter(fout);
        
            mywrite.write(""+numnodes);
            mywrite.newLine();
            
            //Calculating the number of active trees
            for(i=0;i<MAX_TREES;i++){
                if(num_sinks_per_tree[i]!=0)
                    count_trees++;
            }
            mywrite.write(""+count_trees);
            mywrite.newLine();
            
            //Calculating the number of sinks on each active tree
            for(i=0;i<MAX_TREES;i++){
                if(num_sinks_per_tree[i]!=0){
                    mywrite.write(""+num_sinks_per_tree[i]);
                    mywrite.newLine();
                }
            }
            
            mywrite.write(""+radius);
            mywrite.newLine();
            mywrite.write(""+sense_radius);
            mywrite.newLine();
            mywrite.write(""+w);
            mywrite.newLine();
            mywrite.write(""+h);
            mywrite.newLine();
            
            for(i=0;i<numpoints;i++){
                mywrite.write(getNode(i).toStringforDeployment());
                mywrite.newLine();
            }
            mywrite.flush();
            fout.flush();
            fout.close();
            
            
        }catch(Exception e){e.printStackTrace();}
        
        
        if(!batch_creation)
            javax.swing.JOptionPane.showMessageDialog(this, "Deployment saved!");
        
    }
    
    public void LoadDeployment(String the_file){
    
        LoadDeployment(the_file, -1);
        
    }
    
    
    public void LoadDeployment(String the_file, double rad){
    
        String temp="";
        String[] temp_read;
        int i,j;
        myFile = new File(the_file);
        int positionx;
        int positiony;
        double energy;
        
        total_energy_ini = 0;
        int countNeighbs=0;
        int cont=0,cont2=0;
        double temp_radius, temp_sense_radius;
        myPanel.clearAll();
        int temp_trees=0;
        int temp_num_sinks=0;
        numsinks=0;
        double interquery_time_=10,weight_e=0.5, weight_d=0.5;
        int motion = 0;
        int energy_model=0;

        try{
            
            fin = new FileReader(the_file);
            myread = new BufferedReader(fin);
            
        
            temp = myread.readLine();
                
            if(temp!=null){
                    
                try{
                    
                    clear_all_sim();
                    
                    numnodes = Integer.parseInt(temp);
                    jLabel6.setText(""+numnodes);
                    
                    temp = myread.readLine();
                    temp_trees = Integer.parseInt(temp);
                    
                    for(i=0;i<temp_trees;i++){
                        temp = myread.readLine();
                        temp_num_sinks = Integer.parseInt(temp);
                        num_sinks_per_tree[i] = temp_num_sinks;
                        numsinks+=temp_num_sinks;
                    }
                    
                    temp = myread.readLine();
                    radius = Double.parseDouble(temp);
                    jLabel7.setText(""+radius);
                    
                    temp = myread.readLine();
                    sense_radius = Double.parseDouble(temp);
                    
                    temp = myread.readLine();
                    h = Integer.parseInt(temp);
                    
                    temp = myread.readLine();
                    w = Integer.parseInt(temp);
                    
                    myPanel.setArea(w,h);
                    
                    //Updating range areas for visual effects
                    myPanel.setRangeArea(false, false);
                    
                    //Updating the total number of points in the area = nodes + sinks
                    numpoints = numnodes + numsinks;
                    
                    for(i=0;i<numpoints;i++){
            
                        temp = myread.readLine();
                        temp_read = temp.split("@");    
                        
                        positionx = Integer.parseInt(temp_read[0]);
                        positiony = Integer.parseInt(temp_read[1]);
                        energy = Double.parseDouble(temp_read[2]);
                        
                        if(temp_read.length > 3){
                            if(rad == -1)
                                temp_radius = Double.parseDouble(temp_read[3]);
                            else
                                temp_radius = rad;
                            
                            temp_sense_radius = Double.parseDouble(temp_read[4]);
                            interquery_time_= Double.parseDouble(temp_read[5]);
                            weight_e = Double.parseDouble(temp_read[6]);
                            weight_d = Double.parseDouble(temp_read[7]);
                            
                            if(temp_read.length > 8)
                                motion = Integer.parseInt(temp_read[8]);
                            if(temp_read.length > 9)
                                energy_model = Integer.parseInt(temp_read[9]);

                            mynodes.add(new node(i, new Point(positionx,positiony), energy, temp_radius, temp_sense_radius,interquery_time_,weight_e,weight_d,motion,energy_model));
                        }else
                            mynodes.add(new node(i, new Point(positionx,positiony), energy, radius, sense_radius));
                        
                        total_energy_ini += energy;
                    }    
                    
                    }catch(Exception e){e.printStackTrace();}
            
            fin.close();
            }
            topology_loaded=true;
        }catch(Exception e){e.printStackTrace();}
        
        
        //Defining the role of SINKS to all sinks on each of the trees.
        cont = numnodes;
        for(j=0;j<MAX_TREES;j++){
            temp_num_sinks= num_sinks_per_tree[j];
            if(temp_num_sinks >0){
                for(i=0;i<numpoints;i++){
                    getNode(i).SetInfrastructureStarted(j, true);
                }
            }
            for(i=0;i<temp_num_sinks;i++){
                getNode(cont).setRole(j,ROLE_SINK);
                cont++;
            }
        }
        

        //Calculate the neighborhood of the nodes
        for(i=0;i<numpoints;i++){
            getNode(i).setSortingMode(sortingmode);
            avgNeighb += BuildNeighborhood(i);
        }
     
        avgNeighb = avgNeighb/numpoints;
        jLabel24.setText(""+avgNeighb);
        
        if(!batch_simulation){
            myPanel.repaint();
            jTabbedPane1.setSelectedIndex(1);
            javax.swing.JOptionPane.showMessageDialog(this, "Deployment loaded!");
        }
        
    }
   
    public void setViewNodeNumber(boolean sw){
        myPanel.setViewNodeNumber(sw);
    }
    
    
    public boolean verifpa(int n, int t){
        return getNode(n).getInfrastructure(t).isActive();    
    }
    
    
    public void setNodePos(int _x, int _y){
    
        jTextField1.setText("1");
        jComboBox2.setSelectedIndex(4);
        jTextField23.setText(""+_x);
        jTextField24.setText(""+_y);
        addNodeCreationWord();
        
        jButton5.setText("Nodes left: "+(numnodes-tempnumnodes-1));
        
        
        if(numnodes==tempnumnodes){
            clear_all_sim();
            NodeGenerator();
            myPanel.setActive_tree_viz(treeIDViz);
            myPanel.repaint();
            jButton5.setText("Locate Sinks");
            jTabbedPane1.setSelectedIndex(1);
        }
        
    }
   
    
    
    /*
     * BFS graph visiting technique. If the count of the nodes visited is less that the number of points, is not connected.
     */
    public int BFS(int mode_BFS){
        int t = 0,i=0, max=0,tam=0,j=0, temp2=0, k=0;
        
        if(numsinks>0)
            tam = numpoints;
        else
            tam = numnodes;
        
        if(mode_BFS >1){
             i=numnodes;
             //BFS in order to find the greates component, starting only from a sink node
            while(i<tam){
                temp2 = num_sinks_per_tree[j];
                t=0;
                for(k=0;k<temp2;k++){

                    if(getNode(i+k).getStateBFS() == S_NOT_VISITED){
                        t+= BFS(mode_BFS, i+k);

                        if(t > max)
                            max=t;
                    }
                }        
                i+=temp2;
                j++;
            }
        }else{
            //BFS in order to find the greates component, not only starting from a sink node
            for(i=0; i<tam;i++){
                if(getNode(i).getStateBFS() == S_NOT_VISITED){
                    t = BFS(mode_BFS, i);

                    if(t > max)
                        max=t;

                }
            }
        }
        
        if(save_lifetime_log || view_covered_area_stat || save_stats){
            covered_area_from_sink_c = calc_coverage_area_BFS(0);
            covered_area_from_sink_s = calc_coverage_area_BFS(1);
        }

        for(i=0;i<tam;i++){
                getNode(i).setStateBFS(S_NOT_VISITED);
            }
        temp_ids.clear();
        
        return max;
    }
        
        public int BFS(int mode_BFS, int node){
        
        temp_ids.clear();
        int temp_id, temp_id2,temp_num_neighb,i,count=0, temp_tree,templevel=0;
               
        temp_ids.add(node);
        getNode(node).setStateBFS(S_VISITED);
        getNode(node).setLevelBFS(templevel);

        if(mode_BFS == BFS_MODE_ACTIVE_ALIVE_OPTIMAL)
            temp_tree = -1;
        else
            temp_tree = getNode(node).getActiveTree();
        
        while(temp_ids.size()>0){
            count++;
            temp_id = temp_ids.get(0);
            temp_ids.remove(0);

            if(mode_BFS == BFS_MODE_ACTIVE_ALIVE_FROM_SINKS_CHILDREN_ONLY){
                temp_num_neighb = getNode(temp_id).getNumNeighbors();
                templevel = getNode(temp_id).getLevelBFS();

                for(i=0;i<temp_num_neighb;i++){
                    temp_id2 = getNode(temp_id).getNeighborID(i);
                    if(getNode(temp_id2).getStateBFS()!=S_VISITED && getNode(temp_id2).isActive(temp_tree) && getNode(temp_id2).isAlive() && getNode(temp_id2).getDefaultGateway(temp_tree)== temp_id){
                        temp_ids.add(temp_id2);
                        getNode(temp_id2).setStateBFS(S_VISITED);
                        getNode(temp_id2).setLevelBFS(templevel+1);
                    }

                }

            
            }else{
                temp_num_neighb = getNode(temp_id).getNumNeighbors();
                templevel = getNode(temp_id).getLevelBFS();

                for(i=0;i<temp_num_neighb;i++){
                    temp_id2 = getNode(temp_id).getNeighborID(i);
                    if(mode_BFS == BFS_MODE_ACTIVE_ALIVE || mode_BFS == BFS_MODE_ACTIVE_ALIVE_FROM_SINKS || mode_BFS == BFS_MODE_ACTIVE_ALIVE_OPTIMAL){
                        if(getNode(temp_id2).getStateBFS()!=S_VISITED && getNode(temp_id2).isActive(temp_tree) && getNode(temp_id2).isAlive()){
                            temp_ids.add(temp_id2);
                            getNode(temp_id2).setStateBFS(S_VISITED);
                            getNode(temp_id2).setLevelBFS(templevel+1);
                        }
                    }else{
                            if(getNode(temp_id2).getStateBFS()!=S_VISITED){
                            temp_ids.add(temp_id2);
                            getNode(temp_id2).setStateBFS(S_VISITED);
                            getNode(temp_id2).setLevelBFS(templevel+1);
                        }
                    }
                }
            }
        }
        
        return count;
    }
    
    public boolean checkGraphConnected(){
        boolean _sw=false;
        int count = BFS(BFS_MODE_STANDARD);
        if(count<numpoints)
                jLabel36.setText("Disconnected Topology! "+count);
            else{
                jLabel36.setText("Connected  Topology! "+count);
                _sw=true;
            }
        return _sw;
    }
    
    
    public int PoissonRandomGenerator(double lambda){
        /*
         * Taken from wikipedia, cited from:
         * Donald E. Knuth (1969). Seminumerical Algorithms, The Art of Computer Programming, Volume 2. Addison Wesley. 
         */
        double l,_p;
        int k,i;
        double u;

        l = java.lang.Math.exp(-lambda);

        _p=1;
        k=0;

        do{
            k = k+1;
            u = getRandom(1);
            _p=_p*u;
        }while(_p>=l);             

        return k-1;
    }
    
    public Point GaussianRandomGenerator(double mean1, double mean2, double _sigma){
         /*
        Box-Muller generator (1958)
        Box, G.E.P, M.E. Muller 1958; A note on the generation of random normal deviates, Annals 
        Math. Stat, V. 29, pp. 610-611

        Generate a random value from a starndard gaussian Z and then, 
        apply the transformation of Z into X = sigma*Z + Mu
         
         */

         Point tp = new Point();
        double x1, x2, w1, y1, y2;
        int x,y;
        
        do {
                 x1 = 2.0 * getRandom(1) - 1.0;
                 x2 = 2.0 * getRandom(1) - 1.0;
                 w1 = x1 * x1 + x2 * x2;
         } while ( w1 >= 1.0 );
        
         w1 = java.lang.Math.sqrt( (-2.0 * java.lang.Math.log( w1 ) ) / w1 );
         y1 = x1 * w1;
         y2 = x2 * w1;
        
        //transformation of Z into X = sigma*Z + Mu
        tp.x=(int)java.lang.Math.round(y1*_sigma+mean1);
        tp.y=(int)java.lang.Math.round(y2*_sigma+mean2);
        
        return tp;
    }
    
    public int NodeGeneratorForbatch(int n, String the_path){
    
        jLabel36.setText("");
        int in=0,count_trials=0,total_trials=0,j=0;
        boolean sw=false,sw2=false;
        String nom="";
        String extension=".ata";
        double we=0, wd=1, step =0.25;
        int result;
        String[] options = new String[2];
        options[0]="Yes";
        options[1]="No";
        timestamp t;
        
        
        jLabel27.setText("Progress: 0 of "+n+" topologies.");
        while(in<n && !sw2){
            sw=false;
            count_trials=0;
            do{
                sw = NodeGenerator(); 
                count_trials++;
                total_trials++;
            }while(!sw && count_trials<MAX_TRIALS);
            
            if(count_trials<MAX_TRIALS){
                if(!jCheckBox14.isSelected()){
                    t = new timestamp();
                    if(numsinks>0)
                        nom = numnodes+"_"+numsinks+"_"+getNode(numnodes).getRadius()+"_"+w+"_"+h+"_"+t.toString2()+"_";
                    else
                        nom = numnodes+"_"+numsinks+"_"+getNode(0).getRadius()+"_"+w+"_"+h+"_"+t.toString2()+"_";
                    
                    SaveDeployment(the_path+"\\"+nom+(in+start_index)+extension);
                }else{
                    we=0.0;
                    wd=1.0;
                    try{
                        step = Double.parseDouble(jTextField28.getText());
                    }catch(Exception ex){ex.printStackTrace();}
                    while(we<=1){
                        jTextField26.setText(""+we);
                        jTextField27.setText(""+wd);
                        setWeights();
                        t = new timestamp();
                        nom = numnodes+"_"+numsinks+"_"+getNode(numnodes).getRadius()+"_"+w+"_"+h+"_"+we+"_"+wd+"_"+t.toString2()+"_";
                        SaveDeployment(the_path+"\\"+nom+(in+start_index)+extension);
                        we+=step;
                        wd=1.0-we;
                    }
                    if(we>1){
                        jTextField26.setText("1");
                        jTextField27.setText("0");
                        setWeights();
                        t = new timestamp();
                        nom = numnodes+"_"+numsinks+"_"+getNode(numnodes).getRadius()+"_"+w+"_"+h+"_1.0_0.0_"+t.toString2()+"_";
                        SaveDeployment(the_path+"\\"+nom+(in+start_index)+extension);
                    }
                }
                in++;
                jProgressBar2.setValue(in+1);
                jLabel27.setText("Progress: "+in+" of "+n+" topologies.");
            }else{
                //javax.swing.JOptionPane.showMessageDialog(this, "Impossible deployment!");
                //sw2=true;
                
                if(checkTrials){
                    result = JOptionPane.showOptionDialog( 
                        this,                             // the parent that the dialog blocks 
                        "The maximum amount of failed trials was reached. Want to try again?",                                    // the dialog message array 
                        "Topology Creation Error", // the title of the dialog window 
                        JOptionPane.DEFAULT_OPTION,                 // option type 
                        JOptionPane.INFORMATION_MESSAGE,            // message type 
                        null,                                       // optional icon, use null to use the default icon 
                        options,                                    // options string array, will be made into buttons 
                        options[1]                                  // option that should be made into a default button 
                    ); 
                    switch(result) { 
                       case 0: // yes 
                         count_trials=0;
                         break; 
                       case 1: // no 
                         count_trials = MAX_TRIALS+1;
                         sw2=true;
                         break; 
                       default: 
                         break; 
                    } 
                }else{
                    count_trials=0;
                }
                
                
            }
            
        }
        
        if(!GC_Test)
            javax.swing.JOptionPane.showMessageDialog(this, ""+in+" saved deployments after "+total_trials+" trials!");
        
        if(GC_Test){
            try{
                sem_GC.release();
            }catch(Exception ex){ex.printStackTrace();}
        }
        return total_trials;
    }
    
    public Point setPointLocation(int x, int y){
        return new Point(x,y);
    }
    
    
    public double GenerateEnergy(int energy_dist, int max_energy, int mean_energy, double parameter ){
    
        double energy=max_energy;
        
        switch(energy_dist){
                
                //Constant distribution for energy
                case ENERGY_CONSTANT:
                       energy = java.lang.Math.min(energy, mean_energy);          
                       break;

                //Normal distribution for energy
                case ENERGY_NORMAL:
                       p=GaussianRandomGenerator(mean_energy,0,parameter);
                       energy = java.lang.Math.min(max_energy,p.getX());          
                       break;

                //Poisson distribution for energy
                case ENERGY_POISSON:
                       energy = java.lang.Math.min(max_energy,PoissonRandomGenerator(parameter));
                       break;
                       
                //Uniform distribution for energy between 0 and max_energy          
                case ENERGY_UNIFORM:
                        energy = java.lang.Math.min(max_energy,getRandom(max_energy));
                        break;

                }
        return energy;
    
    }
    
    public double GenerateEnergy(){
    
        double energy=MAX_ENERGY;
        
        switch(energy_distribution){
                
                //Constant distribution for energy
                case ENERGY_CONSTANT:
                       energy = java.lang.Math.min(MAX_ENERGY, energy_distribution_parameter);          
                       break;

                //Normal distribution for energy
                case ENERGY_NORMAL:
                       p=GaussianRandomGenerator(energy_distribution_parameter,0,sigma_e);
                       energy = java.lang.Math.min(MAX_ENERGY,p.getX());          
                       break;

                //Poisson distribution for energy
                case ENERGY_POISSON:
                       energy = java.lang.Math.min(MAX_ENERGY,PoissonRandomGenerator(energy_distribution_parameter));
                       break;
                           
                case ENERGY_UNIFORM:
                        energy = java.lang.Math.min(MAX_ENERGY,getRandom(energy_distribution_parameter));
                        break;

                }
        return energy;
    }
    
    
    public int NodeGeneratorByWord(String s){
    
        /*
         *  word format:
         * sink@                    {0,1}= no, yes
         * tree selection@          int
         * number of nodes@         int
         * comm rad@                double
         * sensing rad@             double
         * interquery time@         double
         * position distrib@        {0-5} = uniform, normal, grid HV, grid HVD, constant, center
         * width@                      width of the location distribution square
         * height@                     height of the location distribution square
         * mean x@                  int
         * mean Y@                  int
         * parameter@               double
         * energy distrib@          {0-5} = uniform, normal,poisson, constant
         * max value@               int
         * mean value@              int
         * parameter@               double
         * moving@                    {0,1}= no, yes
         */
    
        int numnodes_=0;
        double comm_rad_=40.0, sense_rad_=20.0;
        int sink_=0, tree_sel_=0;
        double interquery_time_=10;
        int pos_dist_=0, mean_x=300, mean_y=300;
        int energy_dist_=0, energy_max=1000, energy_mean=1000;
        double pos_param=1, energy_param=1;
        int x=0,y=0,i,j,energy=1000;
        double step_v=40, step_h=40;
        int step=40;
        int temp, limit_h, limit_w;
        double weight_e=0.5, weight_d=0.5;
        int _width=600, _height=600;
        int moving = 0;
        int energy_model = 0;
        
        String the_word[];
        
        the_word = s.split("@");
        
        try{
        
            sink_ = Integer.parseInt(the_word[0]);
            tree_sel_ = Integer.parseInt(the_word[1]);
            numnodes_ = Integer.parseInt(the_word[2]);
            comm_rad_ = Double.parseDouble(the_word[3]);
            sense_rad_ = Double.parseDouble(the_word[4]);
            interquery_time_= Double.valueOf(the_word[5]);
            pos_dist_ = Integer.parseInt(the_word[6]);
            mean_x = Integer.parseInt(the_word[7]);
            mean_y = Integer.parseInt(the_word[8]);
            pos_param = Double.valueOf(the_word[9]);
            _width = Integer.parseInt(the_word[10]);
            _height = Integer.parseInt(the_word[11]);
            energy_dist_ = Integer.parseInt(the_word[12]);
            energy_max = Integer.parseInt(the_word[13]);
            energy_mean = Integer.parseInt(the_word[14]);
            energy_param = Double.valueOf(the_word[15]);
            weight_e= Double.valueOf(the_word[16]);
            weight_d= Double.valueOf(the_word[17]);
            moving= Integer.parseInt(the_word[18]);
            energy_model = Integer.parseInt(the_word[19]);
            
        }catch(Exception ex){ex.printStackTrace();}
        
        if((pos_dist_ < GRID_H_V) || sink_==1){
        
            for(i=0;i<numnodes_;i++){

                switch(pos_dist_){

                    //This option will generate uniform nodes in a rectangle with center in (mean_x, mean_y) and side _width and _height.
                    case UNIFORM_DIST:
                        x = (int)java.lang.Math.round(java.lang.Math.random()*_width)+(mean_x-(_width/2));
                        y = (int)java.lang.Math.round(java.lang.Math.random()*_height)+(mean_y-(_height/2));
                        break;

                    //This option will generate a node in the normally distributed random position with mean in (mean_x, mean_y)
                    // and a standar deviation of _pos_param.
                    case NORMAL_DIST:
                        p=GaussianRandomGenerator(mean_x,mean_y,pos_param);
                        x=(int)p.getX();
                        y=(int)p.getY();
                        break;

                    //This option will generate a node in the position defined by (mean_x, mean_y) in the deployment area
                    case CONSTANT:
                        x=mean_x;
                        y=mean_y;
                        break;

                    //This option will generate a node in the center of the deployment area
                    case CENTER:
                        x=(w/2);
                        y=(h/2);
                        break;

                }

                energy = (int)GenerateEnergy(energy_dist_, energy_max, energy_mean, energy_param );
                total_energy_ini += energy;
                
                mynodes.add(new node(current_node_count, new Point(x,y), energy, comm_rad_, sense_rad_,interquery_time_,weight_e,weight_d,moving, energy_model));
                if(sink_==1){
                    getNode(current_node_count).setRole(tree_sel_, ROLE_SINK);    
                }
                current_node_count++;
        
                
                
            }
        
            if(sink_==1 && numnodes_>0){
                num_sinks_per_tree[tree_sel_]+=numnodes_;
            }
            
        }else{

            switch(pos_dist_){

                case GRID_H_V:
                    step = (int)comm_rad_;
                    break;
                    
                case GRID_H_V_D:
                    step = (int)java.lang.Math.floor(comm_rad_/java.lang.Math.sqrt(2));
                    break;

                case GRID_OPT_STRIP:
                     step_h = java.lang.Math.round(java.lang.Math.min(comm_rad_, sense_rad_*java.lang.Math.sqrt(3)));
                     step_v = sense_rad_ + java.lang.Math.ceil( java.lang.Math.sqrt(sense_rad_*sense_rad_ - step_h*step_h/4));
                    break;

                case GRID_OPT_COMB:
                    step_h = sense_rad_*(1.732);
                    //step_h = sense_rad_*2;
                    //step_h = java.lang.Math.min(comm_rad_, sense_rad_*(1.732));
                    step_v = sense_rad_/2;
                    break;

                case GRID_OPT_TRIANGLE:
                    step_h = sense_rad_/2;
                    step_v = sense_rad_*(1.732)/2;
                    break;

                case GRID_OPT_SQUARE:
                    step = (int)sense_rad_;
                    break;

                case GRID_POISSON:
                case GRID_LAMBDA:
                    step = (int)sense_rad_;
                    break;


                default:
                    step=40;
                    break;
            
            }

            switch(pos_dist_){

                case GRID_H_V:
                case GRID_H_V_D:
                case GRID_OPT_SQUARE:

                    limit_h = (h/step)+1;
                    limit_w = (w/step)+1;
                    numnodes_ = limit_h*limit_w;
                    jTextField1.setText(""+numnodes);
                    jLabel6.setText(""+numnodes);

                    for(i=0;i<limit_h;i++){
                        for(j=0;j<limit_w;j++){

                            energy = (int)GenerateEnergy(energy_dist_, energy_max, energy_mean, energy_param );
                            total_energy_ini += energy;

                            p=setPointLocation(j*step, i*step);
                            mynodes.add(new node(current_node_count, p, energy, comm_rad_, sense_rad_,interquery_time_,weight_e,weight_d,moving, energy_model));
                            current_node_count++;

                        }
                    }
                break;

                case GRID_OPT_STRIP:

                    int temp2 = -1;
                    
                    //Create the strips
//                    limit_h = (int)(h/step_v);
  //                  limit_w = (int)(w/step_h);

                    limit_h = (int)(java.lang.Math.ceil(h/(step_v)));
                    limit_w = (int)(java.lang.Math.ceil(w/step_h));


                    for(i=0;i<limit_h;i++){
                        for(j=0;j<limit_w;j++){

                            energy = (int)GenerateEnergy(energy_dist_, energy_max, energy_mean, energy_param );
                            total_energy_ini += energy;

                            p=setPointLocation((int)(step_h/4+ j*step_h + (java.lang.Math.pow(temp2, i))*(step_h/4)), (int)(i*step_v));


                            mynodes.add(new node(current_node_count, p, energy, comm_rad_, sense_rad_,interquery_time_,weight_e,weight_d,moving, energy_model));
                            current_node_count++;

                        }

                        if(i%2 == 1){

                            energy = (int)GenerateEnergy(energy_dist_, energy_max, energy_mean, energy_param );
                            total_energy_ini += energy;

                            p=setPointLocation((int)(step_h/4+ j*step_h + (java.lang.Math.pow(temp2, i))*(step_h/4)), (int)(i*step_v));


                            mynodes.add(new node(current_node_count, p, energy, comm_rad_, sense_rad_,interquery_time_,weight_e,weight_d,moving, energy_model));
                            current_node_count++;
                        }

                    }

                    //Initial deviation
                    temp = (int)(step_v/2.0);
                    
                    //step_v = java.lang.Math.sqrt(step_h*step_h/4 + step_v*step_v);

                    //limit_h = (int)(w/step_v)+1;

                    if(((double)comm_rad_/(double)sense_rad_) <= java.lang.Math.sqrt(3)){
                        //Generate the nodes to connect the strips
                        for(i=0;i<limit_h-1;i++){

                            energy = (int)GenerateEnergy(energy_dist_, energy_max, energy_mean, energy_param );
                            total_energy_ini += energy;

                            //p=setPointLocation((int)(2*step_h + (java.lang.Math.pow(temp2, i))*(step_h/4)), (int)(i*step_v + temp));
                            p=setPointLocation((int)(step_h + step_h/4), (int)(i*step_v + temp));

                            mynodes.add(new node(current_node_count, p, energy, comm_rad_, sense_rad_,interquery_time_,weight_e,weight_d,moving, energy_model));
                            current_node_count++;


                        }
                    }
                    numnodes_ = current_node_count;
                    jTextField1.setText(""+numnodes);
                    jLabel6.setText(""+numnodes);

                break;

                case GRID_OPT_COMB:

                    temp2 = 0;

                    //Create the strips
                    limit_h = (int)(java.lang.Math.round(h/(step_v*3)));
                    limit_w = (int)(java.lang.Math.round(w/step_h));

                    for(i=0;i<limit_h;i++){

                        
                        for(j=0;j<limit_w;j++){

                            energy = (int)GenerateEnergy(energy_dist_, energy_max, energy_mean, energy_param );
                            total_energy_ini += energy;

                            p=setPointLocation((int)(step_h/4+ j*step_h + temp2*(step_h/2)), (int)(i*step_v*3));

                            mynodes.add(new node(current_node_count, p, energy, comm_rad_, sense_rad_,interquery_time_,weight_e,weight_d,moving, energy_model));
                            current_node_count++;

                        }

                        
                        if(temp2 == 0)
                            temp2=1;
                        else
                            temp2=0;
                        
                        //if(i+1<limit_h){
                            for(j=0;j<limit_w;j++){

                                energy = (int)GenerateEnergy(energy_dist_, energy_max, energy_mean, energy_param );
                                total_energy_ini += energy;

                                p=setPointLocation((int)(step_h/4+ j*step_h + temp2*(step_h/2)), (int)((i+1)*step_v*3 - 2*step_v));

                                mynodes.add(new node(current_node_count, p, energy, comm_rad_, sense_rad_,interquery_time_,weight_e,weight_d,moving, energy_model));
                                current_node_count++;

                            }
                        //}
                    }

                    numnodes_ = current_node_count;
                    jTextField1.setText(""+numnodes);
                    jLabel6.setText(""+numnodes);
                break;

                case GRID_OPT_TRIANGLE:

                    temp2 = 0;

                    //Create the strips
                    limit_h = (int)(java.lang.Math.ceil(h/(step_v)));
                    limit_w = (int)(java.lang.Math.ceil(w/step_h));

                    for(i=0;i<limit_h;i++){
                      
                        j=0;
                        while(j<limit_w){

                            temp2=i%2;

                            //first point
                            energy = (int)GenerateEnergy(energy_dist_, energy_max, energy_mean, energy_param );
                            total_energy_ini += energy;

                            p=setPointLocation((int)((j+temp2)*step_h),(int)(i*step_v));

                            mynodes.add(new node(current_node_count, p, energy, comm_rad_, sense_rad_,interquery_time_,weight_e,weight_d,moving, energy_model));
                            current_node_count++;

                            if(temp2==1){
                                    //To separate tthe nodes 1*sense_rad
                                    j+=2;
                                }else{
                                    j+=4;
                                }

                            if(j<limit_w){
                                energy = (int)GenerateEnergy(energy_dist_, energy_max, energy_mean, energy_param );
                                total_energy_ini += energy;

                                p=setPointLocation((int)((j+temp2)*step_h),(int)(i*step_v));

                                mynodes.add(new node(current_node_count, p, energy, comm_rad_, sense_rad_,interquery_time_,weight_e,weight_d,moving, energy_model));
                                current_node_count++;

                                if(temp2==0){
                                    //To separate tthe nodes 1*sense_rad
                                    j+=2;
                                }else{
                                    j+=4;
                                }
                            }
                        }



                    }
                    
                    numnodes_ = current_node_count;
                    jTextField1.setText(""+numnodes);
                    jLabel6.setText(""+numnodes);

                    break;

                case GRID_POISSON:
                case GRID_LAMBDA:

                    temp=0;
                    temp2 = 0;
                    int delta = 1;
                    int temp_delta = delta;
                    int k=0;

                    try{
                        delta = Integer.parseInt(jTextField15.getText());
                        temp_delta = delta;
                    }catch(Exception ex){ex.printStackTrace();}

                    //Create the strips
                    limit_h = (int)(java.lang.Math.ceil(h/(step)));
                    limit_w = (int)(java.lang.Math.ceil(w/step));

                    for(i=0;i<limit_h;i++){

                        for(j=0;j<limit_w;j++){

                            if(pos_dist_ == GRID_POISSON)
                                temp_delta = PoissonRandomGenerator(delta);

                            for(k=0;k<temp_delta;k++){

                                energy = (int)GenerateEnergy(energy_dist_, energy_max, energy_mean, energy_param );
                                total_energy_ini += energy;

                                temp = getRandomInt(step);
                                temp2 = getRandomInt(step);

                                p=setPointLocation((int)((j*step)+temp),(int)((i*step)+temp2));

                                mynodes.add(new node(current_node_count, p, energy, comm_rad_, sense_rad_,interquery_time_,weight_e,weight_d,moving, energy_model));
                                current_node_count++;

                            }
                            
                        }
                    }

                    numnodes_ = current_node_count;
                    jTextField1.setText(""+numnodes);
                    jLabel6.setText(""+numnodes);
                    

                    break;


                default:
                    break;

            }

        }
        
        return numnodes_;
    }
    
    public boolean NodeGenerator(){
    
        int i;
        boolean sw=false;
        contNeighb=0;
        
        UpdateNodeCreationWords();
        mynodes.clear();
        numnodes=0;
        numsinks=0;
        numpoints=0;
        current_node_count=0;
        total_energy_ini=0;
        
        for(i=0;i<MAX_TREES;i++)
            num_sinks_per_tree[i] = 0;
        
        if(node_creation_words.size()==0){
            addNodeCreationWord();
        }
        
        if(sink_creation_words.size()==0){
            addDefaultSinkCreationWord();
        }
        
        
        for(i=0;i<node_creation_words.size();i++){
            numnodes += NodeGeneratorByWord(node_creation_words.get(i));
        }
        
        java.util.Collections.sort(sink_creation_words);
        for(i=0;i<sink_creation_words.size();i++){
            numsinks += NodeGeneratorByWord(sink_creation_words.get(i));
        }

        jLabel6.setText(""+numnodes);
        jLabel28.setText(""+numsinks);
        
        
        numpoints = numnodes + numsinks;
        
         for(i=0;i<numpoints;i++){
            getNode(i).setSortingMode(sortingmode);
            avgNeighb += BuildNeighborhood(i);
        }
     
        avgNeighb = avgNeighb/numpoints;
        jLabel24.setText(""+avgNeighb);
        
         if(jCheckBox7.isSelected()){
            sw=checkGraphConnected();
        }else
            sw=true;
        
        if(sw && !batch_creation && !GC_Test)
            jTabbedPane1.setSelectedIndex(1);
        
        no_paint = !sw;
        return sw;
    }
    
    
    
    public int BuildNeighborhood(int nodeID){
        int i;
        contNeighb=0;
        
        //contNeighb = getNode(nodeID).getNumNeighbors();
        
        //Remove all previous neighbors from the max power graph
        //for(i=0;i<contNeighb;i++){
          //  myPanel.removeLine(nodeID,getNode(nodeID).getNeighborID(i));
        //}

        myPanel.removeAllLinesFromSource(nodeID);

        //Remove all previous neighbors from the node's data structure
        getNode(nodeID).cleanNeighbors();
        
        //Regenerate the neighborhood on the max power graph
        for(i=0;i<numpoints;i++){
            if(i!=nodeID){
            if(getNode(nodeID).isNeighbor2(getNode(i))){
                if(!batch_creation)
                    //myPanel.addLine(nodeID,i);
                contNeighb++;
                } 
            }
        }
        getNode(nodeID).SortNeighbors();
        return (int)contNeighb;
    }
    
    
    public void resetCommRange(int nodeID, int new_range){
        
        getNode(nodeID).setRadius(new_range);
        BuildNeighborhood(nodeID);
    }
    
    
    public void CountNotVisited(){
        
        int contNotVis=0, contNotCov=0 ,i,j;
        int temp_level=0, temp_tree=0;
        int countNeighbActiveNodes=0;
        double[] temp_coverage_k = new double[3];
        int temp=0;
        count_nodes_with_dead_parent=0;
        total_energy=0;
        total_energy_tree=0;
        for(i=0;i<MAX_TREES;i++)
            count_active[i]=0;
        
        if(mynodes.size()>0){
        
        for(i=0;i<numpoints;i++){
            
            temp_tree = getNode(i).getActiveTree();
            if(!getNode(i).isCovered(temp_tree)){
                contNotCov++;
            }
            if(!getNode(i).isVisited(temp_tree)){
                contNotVis++;
            }
            for(j=0;j<MAX_TREES;j++){
                if(getNode(i).isActive(j)){
                    count_active[j]++;
                    total_energy_tree+= getNode(i).getEnergy();        
                }
            }
            
            if(getNode(i).isActive(0)){
                countNeighbActiveNodes+= getNode(i).getNumGateways(temp_tree);
            }
            
            if(!getNode(i).isSink(temp_tree)){
                temp = getNode(i).getDefaultGateway(temp_tree);
                if(temp!=-1){
                   if(!getNode(temp).isActive(temp_tree)){
                     count_nodes_with_dead_parent++;
                   }
                }else
                    count_nodes_with_dead_parent++;
            }
            
            temp_level += getNode(i).getLevel(temp_tree);
            total_energy += getNode(i).getEnergy();
            
        }
        
        if(count_active[0]>0)
                avgNeighbActiveNodes = ((double)countNeighbActiveNodes/(double)count_active[0]);
            else
                avgNeighbActiveNodes = 0;
        
        notCovered_Visited[0]=contNotCov;
        notCovered_Visited[1]=contNotVis;
        avg_level = (double)temp_level/(double)numpoints;
        
        ideal_p = myPanel.getGridParents();
        grid_mode_text = myPanel.getGridMode();

        ReachableNeighbActiveNodes = BFS(BFS_MODE_ACTIVE_ALIVE_FROM_SINKS_CHILDREN_ONLY);

        if(view_covered_area_stat || save_stats){
            covered_area = calc_coverage_area(0);
            temp_coverage_k = calc_coverage_area_k(1);
            //covered_area_sens = calc_coverage_area(1);
            covered_area_sens = temp_coverage_k[1];
            covered_area_sens_k  = temp_coverage_k[0];
            covered_area_sens_k_all = temp_coverage_k[2];
        }else{
            covered_area=0;
            covered_area_sens=0;
        }
        
        
        }
    }
    
    public void UpdateStats(String stats){
        stats_from_panel = stats;
    }
    
    public void batchSimulation(){
        if(num_files_topology_from_batch>0){
            timestamp t = new timestamp();
            String the_path;
            double alpha_ini=1, alpha_fin=0, alpha_step=0;
            int k=0;

            if(jCheckBox29.isSelected()){

            try{
                    alpha_ini = Double.parseDouble(jTextField43.getText()); //Alpha initial
                    alpha_fin = Double.parseDouble(jTextField44.getText()); // Alpha final
                    alpha_step = Double.parseDouble(jTextField45.getText()); // Alpha step

                    alpha_coverage_value = alpha_ini;
                    total_alpha = (int)java.lang.Math.round(java.lang.Math.abs((alpha_fin-alpha_ini)/alpha_step))+1;

                    if(total_alpha < 1)
                        total_alpha =1;

                }catch(Exception ex){ex.printStackTrace();}
            }else{

                try{
                    alpha_ini = Double.parseDouble(jTextField43.getText()); //Alpha initial
                }catch(Exception ex){ex.printStackTrace();}
                
                alpha_coverage_value = alpha_ini;
                alpha_fin = alpha_ini;
                total_alpha =1;

            }


            batch_simulation=true;
            multiplicity = 1;
            try{
                multiplicity = Integer.parseInt(jTextField19.getText());
            }catch(Exception ex){ex.printStackTrace();}

            total_num_topologies = num_files_topology_from_batch*multiplicity;
            jProgressBar1.setMaximum(total_num_topologies);
            jProgressBar1.setValue(0);
            jLabel50.setText("Progress: 0 of "+total_num_topologies+" files.");


            current_topology_from_batch=0;
            jTextArea2.setText("");

            report_descriptor = jTextField18.getText();
            the_parent_dir = myFiles[0].getParent();
            the_name2 = report_descriptor + "_" + t.toString2();

              try{
                if(save_stats){
                    if(excel_ready_reports)
                        fout_log_stat = new FileWriter(the_parent_dir+"\\stats_"+the_name2+".csv");
                    else
                        fout_log_stat = new FileWriter(the_parent_dir+"\\stats_"+the_name2+".atar");
                    mywrite_log_stat = new BufferedWriter(fout_log_stat);
                    mywrite_log_stat.write(getStatsHeaderForExcel());
                }else
                    jTextArea2.append(getStatsHeaderForExcel());

                //Save the lifetime log of several simulations
                if(save_lifetime_log){
                    fout_log_lt = new FileWriter(the_parent_dir+"\\lifetime_"+total_num_topologies+"_"+the_name2+".csv");
                    mywrite_log_lt = new BufferedWriter(fout_log_lt);
                }

                if(save_results_log){
                    fout_log_results = new FileWriter(the_parent_dir+"\\results_"+total_num_topologies+"_"+the_name2+".csv");
                    mywrite_log_results = new BufferedWriter(fout_log_results);
                }

            }catch(Exception ex){ex.printStackTrace();}

            int j, i;
            for(j=0;j<myFiles.length;j++){

                        if(!myFiles[j].isDirectory()){

                            /*
                             * This cycle works for multiple executions on the same network
                             */

                            the_path = myFiles[j].getPath();
                            the_parent_dir = myFiles[j].getParent();
                            the_name = myFiles[j].getName();
                            the_name2 = myFiles[j].getName() + "_" + t.toString2();

                            /*for(i=0;i<multiplicity;i++){

                                LoadDeployment(the_path);

                                view_active_tree=true;
                                myPanel.setActiveNodes(true);
                                myPanel.setAtarraya(true);

                                jCheckBox1.setSelected(true);
                                jCheckBox3.setSelected(true);
                                jCheckBox10.setSelected(true);

                                try{

                                    if(save_events_log){
                                        fout_log_ev = new FileWriter(the_parent_dir+"\\events_"+the_name2+".csv");
                                        mywrite_log_ev = new BufferedWriter(fout_log_ev);
                                    }

                                }catch(Exception ex){ex.printStackTrace();}

                                selectSimulator();
                                sim_assigned = true;
                                clock=0;
                                StartSimulation();

                            }*/
                            
                            LoadDeployment(the_path);
                            for(i=0;i<multiplicity;i++){
                                
                                alpha_coverage_value = alpha_ini;
                                for(k=0;k<total_alpha;k++){
                                
                                view_active_tree=true;
                                myPanel.setActiveNodes(true);
                                myPanel.setAtarraya(true);

                                jCheckBox1.setSelected(true);
                                jCheckBox3.setSelected(true);
                                jCheckBox10.setSelected(true);

                                try{

                                    if(save_events_log){
                                        fout_log_ev = new FileWriter(the_parent_dir+"\\events_"+the_name2+".csv");
                                        mywrite_log_ev = new BufferedWriter(fout_log_ev);
                                    }

                                }catch(Exception ex){ex.printStackTrace();}

                                selectSimulator();
                                sim_assigned = true;
                                clock=0;
                                StartSimulation();

                                reset_topology();
                                alpha_coverage_value = alpha_coverage_value + alpha_step;

                                }

                            }

                        }
                    }
        }
        clear_all_sim();
    }
    
    
    
    public void batchSimulationSelectFiles(){
        jFileChooser1.setMultiSelectionEnabled(true);
        int i=0;
                
        i = jFileChooser1.showOpenDialog(this);
             
        if (i == JFileChooser.APPROVE_OPTION) {

            myFiles = jFileChooser1.getSelectedFiles();
            if(myFiles!=null){
                jLabel47.setText(""+myFiles.length+" files selected");
                num_files_topology_from_batch = myFiles.length;
                jFileChooser1.setSelectedFile(null);
            }
        } else {
            showMessage("Save command cancelled by user.");
        }
        
        
    }
    
    
    public void SaveReport(){
        
        try{
        
            mywrite_log_stat.flush();
            mywrite_log_stat.close();
            fout_log_stat.close();
            
        }catch(Exception ex){ex.printStackTrace();}
        
    }
    
    public void SaveReportLifetime(){
        
        try{
            mywrite_log_lt.flush();
            mywrite_log_lt.close();
            fout_log_lt.close();
            
        }catch(Exception ex){ex.printStackTrace();}
        
    }

    public void SaveReportResults(){

        try{
            mywrite_log_results.flush();
            mywrite_log_results.close();
            fout_log_results.close();

        }catch(Exception ex){ex.printStackTrace();}

    }
    
    /****************************************************************
     * Methods useful for the Simulation Agent
     ****************************************************************/
    
    public boolean checkSimFinish(){
        
        int i=0;
        int count_finish=0;
        int count_total=0;
        
        if(my_sim.myqueue_.getSize() == 1)
                i=i+1-1;
        
        while(i<MAX_TREES){
            if(num_sinks_per_tree[i]>0){
                if(checkSimFinish(i)){
                    count_finish++;
                }
                count_total++;
            }
            i++;
        }
      
        if(count_total==count_finish){
            
            //If it is just TC
            if(!TM_Selected && !SD_Selected){
                myPanel.repaint();
                return true;
            }else{
                //If it is not just TC
                
                //If the TC has already finished
                if(TC_Prot_final){
                    myPanel.repaint();
                    return true;
                    
                }else{
                    //If this is the ending of the TC an it is not only TC
                    TC_Prot_final = true;
                    
                    //Save stats when the TC algorithm finished
                    if(save_lifetime_log){
                        addStatToLifetimeLog(""+clock);
                    }

                    if(save_stats){
                        CountNotVisited();
                        addStatToReport(the_name);
                    }
                    
                    
                }
            }
        }
        
        return false;
    }
    
    public boolean checkSimFinish(int tree){
        
        int i=0;
        boolean sw=false;
        
        if((!TM_Selected && !SD_Selected) || !TC_Prot_final){
        
        while(i<numpoints && simulator.CheckIfDesiredFinalState(getNode(i).getState(tree),TC_PROTOCOL)){
            i++;
        }
        
        if(i==numpoints)
            return true;
        
        return false;
        
        }
        
        return sw;
    }
    
    
    public String getStats(){
        return jTextPane2.getText();
    }
    
    public String getReport(){
        return jTextArea2.getText();
    }
    
    public String getStatsHeaderForExcel(){
        int i;
        stats="Filename"+
                                ",Clock"+
                                ",# of Nodes"+
                                ",# of Sinks"+
                                ",Not Covered"+
                                ",Ratio"+
                                ",Not Visited"+
                                ",Avg. Level"+
                                ",Avg. Num. Neighb"+
                                ",Avg. Num. Active Neighb"+
                                ",# of Reachable Active Neighb. from Sink"+
                                ",# of Messages regular sent"+
                                ",# of Messages long sent"+
                                ",# of Messages regular received"+
                                ",# of Messages long received"+
                                ",# of Lost Messages regular"+
                                ",# of Lost Messages long"+
                                ",# of dead nodes"+
                                ",# of unconnected nodes"+
                                ",# of Data Messages received by sink";
                                
                                for(i=0;i<MAX_TREES;i++)
                                    stats = stats + ",Active Nodes VNI "+i;
                                        
                                stats = stats +",Total Energy initial"+
                                ",Total Energy final"+
                                ",Total Energy spent ratio"+
                                ",Total Energy in tree"+
                                ",Ratio Energy"+
                                ",TM invocations"+
                                ",Total Covered Comm Area"+
                                ",Total Covered Sensing Area"+
                                ",Average K Sensing Coverage"+
                                ",Average K Sensing Area Coverage"+
                                ",Total Connected Covered Comm Area"+
                                ",Total Connected Covered Sensing Area"+
                                ",alpha coverage value"+
                                ",Error in Simulation\n";
        return stats;
    }

    public void addTMInvocation(){
        count_TM_invocations++;
    }

    public String getStatsRowForExcel(){
        return getStatsRowForExcel("none");
    }
    
    public String getStatsRowForExcel(String filename){
        return getStatsRowForExcel(filename,"");
    }
    
    public String getStatsRowForExcel(String filename, String extra){
    int i;
         stats=             filename+
                                ","+clock+
                                ","+numnodes+
                                ","+numsinks+
                                ","+notCovered_Visited[0]+
                                ","+(double)notCovered_Visited[0]/numnodes+
                                ","+notCovered_Visited[1]+
                                ","+avg_level+
                                ","+avgNeighb+
                                ","+avgNeighbActiveNodes+
                                ","+ReachableNeighbActiveNodes+
                                ","+num_messages+
                                ","+num_messages_long+
                                ","+num_messages_r+
                                ","+num_messages_long_r+
                                ","+num_messages_l+
                                ","+num_messages_long_l+
                                ","+count_dead+
                                ","+count_nodes_with_dead_parent+
                                ","+num_messages_data_r_sink;
         
                                for(i=0;i<MAX_TREES;i++)
                                    stats = stats + ","+count_active[i];
                                        
                                stats = stats +","+total_energy_ini+
                                ","+total_energy+
                                ","+(total_energy_ini-total_energy)/total_energy_ini+
                                ","+total_energy_tree+
                                ","+(total_energy_tree/total_energy)+
                                ","+count_TM_invocations+
                                ","+covered_area+
                                ","+covered_area_sens+
                                ","+covered_area_sens_k+
                                ","+covered_area_sens_k_all+
                                ","+covered_area_from_sink_c+
                                ","+covered_area_from_sink_s+
                                ","+alpha_coverage_value+
                                ","+sim_error+
                                 ","+extra+
                                "\n";
        return stats;
    }
    
    public void addStatToReport(String s){
        addStatToReport(s, "");
    }
    
    public void addStatToReport(String s, String extra){
        
        try{
            if(extra.equals("")){
                if(excel_ready_reports)
                        mywrite_log_stat.write(getStatsRowForExcel(s,extra));
                    else
                        mywrite_log_stat.write("Stats from file: "+s+"\n"+getStats()+"\n"+extra+"\n\n");
            }else{
                if(excel_ready_reports)
                        mywrite_log_stat.write(getStatsRowForExcel(s));
                    else
                        mywrite_log_stat.write("Stats from file: "+s+"\n"+getStats()+"\n\n");
            }    
        }catch(Exception ex){ex.printStackTrace();}
        
    }
    
    public void addStatToLifetimeLog(String s){
        
        temp_lifetime_string = temp_lifetime_string + s +",";
        temp_lifetimeBFS_string = temp_lifetimeBFS_string + BFS(BFS_MODE_ACTIVE_ALIVE_FROM_SINKS_CHILDREN_ONLY) +",";
        temp_lifetimeSurvivors_string = temp_lifetimeSurvivors_string + (numpoints-count_dead) + ",";
        temp_lifetimeBFS_Coverage_c_string = temp_lifetimeBFS_Coverage_c_string+covered_area_from_sink_c+",";
        temp_lifetimeBFS_Coverage_s_string = temp_lifetimeBFS_Coverage_s_string+covered_area_from_sink_s+",";
        
    }
    
    public void addLineToLifetimeLog(String s){
        try{
            mywrite_log_lt.write(s+","+temp_lifetime_string+"\n");
            mywrite_log_lt.write(s+","+temp_lifetimeSurvivors_string+"\n");
            mywrite_log_lt.write(s+","+temp_lifetimeBFS_string+"\n");
            mywrite_log_lt.write(s+","+temp_lifetimeBFS_Coverage_c_string+"\n");
            mywrite_log_lt.write(s+","+temp_lifetimeBFS_Coverage_s_string+"\n");
        }catch(Exception ex){ex.printStackTrace();}
    }

    public void addLineToResultsLog(String s){

        solution sol = new solution(numpoints);
        int i, t=0, at=0;

        for(i=0;i<numpoints;i++){
            at = getNode(i).getActiveTree();
            if(getNode(i).isActive(at))
                t=1;
            else
                t=0;

            sol.set(i, t);
        }

        try{
            mywrite_log_results.write(s+","+sol.toString()+sol.getSum()+"\n");
        }catch(Exception ex){ex.printStackTrace();}
    }

    public void showMessage(String m){
        jTextPane3.setText(m);
    }

    public void showEventList(String m){
        if(show_events)
            jTextArea1.append(m+"\n");
    }
    
    public void showStats(String m){
        jTextPane2.setText(m);
    }
    
    public void showEventCount(String m){
        jLabel33.setText(m);
    }
    
    public double getRandom(double max_val){
        return java.lang.Math.random()*max_val;
    }

    public int getRandomInt(int max_val){
        return (int)java.lang.Math.floor(java.lang.Math.random()*max_val);
    }
    
    public void TicTac(){
        clock+=CLOCK_TIC;
    }
      
    public void broadcast(double final_time, double current_time, int _sender, int _destination, int _code,int _type){
        broadcast(final_time, current_time, -1,-1,_sender, _destination, _code, "",-1,_type);
    }
    
    public void broadcast(double final_time, double current_time, int _sender, int _destination, int _code, int _tree,int _type){
        broadcast(final_time, current_time, -1,-1,_sender, _destination, _code, "",_tree,_type);
    }
    
    public void broadcast(double final_time, double current_time, int _sender, int _destination, int _code, String _data,int _type){
        broadcast(final_time, current_time,-1, -1,_sender, _destination, _code, _data,-1,_type);
    }
    
    public void broadcast(double final_time, double current_time, int _sender, int _destination, int _code, String _data, int _tree,int _type){
        broadcast(final_time, current_time, -1,-1, _sender, _destination, _code, _data,_tree,_type);
    }
    
            
    public void broadcast(double final_time, double current_time, int _source, int _final_destination, int _sender, int _destination, int _code, String _data, int _tree, int _type){
    
        int sender = _sender;
        int numneighb, i, temp_ID;
        int oper=0;
        int state;
        
        double temp_radius;
        double prob_error;
        int size_packet;
     
        
        // If the node is alive and has its radio on...
        //if(getNode(sender).isAlive() && getNode(sender).isRadioOn(_tree)){
        if(getNode(sender).isAlive()){

            my_sim.Push(new event_sim(final_time, current_time, _source,_final_destination, _sender, _destination, -1, _code, _data,_tree,_type, OPER_TX));
            //Obtain the radius of the particular transmission range in which the node is working on a particular tree

            temp_radius=getNode(_sender).getRadius(_tree);

            size_packet = simulator.GetMessageSize(_code, _type);

            prob_error = 1-java.lang.Math.pow(1-bit_error_rate,8*size_packet);

            numneighb = getNode(sender).getNumNeighbors();

            //The sender is defining the next step on the path. Having destination == -2 only happens in DATA messages events.
            if(_destination == -2){
                    _destination = getNode(sender).getGateway(_tree, _final_destination);
                }

            for(i=0;i<numneighb;i++){
                temp_ID = getNode(sender).getNeighborID(i);
                //Program the event of a node receiving the message if the receiver is awake
                my_sim.Push(new event_sim(final_time, current_time, _source,_final_destination, _sender, _destination, temp_ID, _code, _data,_tree,_type, OPER_RX));
            }

    }
        
    }
    public int getOperType_TX_RX(int _code, int op){
    
        int oper=0;
        
        switch(op){
            case OPER_TX:
                if(_code==SON_RECOGNITION || _code==DATA)
                    oper = OPER_TX_LONG;
               else
                    oper = OPER_TX_REGULAR;
            break;
            
            case OPER_RX:
                if(_code==SON_RECOGNITION || _code==DATA)
                    oper = OPER_RX_LONG;
                else
                    oper = OPER_RX_REGULAR;
            break;
            
        }
        
        
        
        return oper;
    }
    
    
    
    
    
    
    
    public void addStatToLifetimeLogEvent(){
        if(save_lifetime_log){
              addStatToLifetimeLog(""+clock);
        }
    }
    
    
    
    public void checkEnergy(int id){
        
        
        //if(getNode(id).isAlive()){
        
            if(TM_Selected && simulator.getTMType() == TM_ENERGY){
                if(getNode(id).checkTMEnergy()){
                    int tree=getNode(id).getActiveTree();
                    pushEvent(new event_sim(clock+getRandom(PROCESSING_DELAY), clock,id,id,id, SEND_REACTIVATE_MESSAGE,"",tree,TM_PROTOCOL));
                    addStatToLifetimeLogEvent();
                }
            }

            if(getNode(id).getEnergyRatio()<DEATH_THSLD_ENERGY && !getNode(id).isSink()){
                getNode(id).setDead();
                count_dead++;
                addStatToLifetimeLogEvent();

            }
            
        //}
        
        
    }
    
    
    public node getNode(int n){
        
        node temp_node=null;
        
        try{
            //sem2.acquire();
            temp_node = mynodes.get(n);
            //sem2.release();
        }catch(Exception ex){ex.printStackTrace();}
        
        return temp_node;
    }
    
    public void pushEvent(event_sim e){
        //myqueue.Push(e);
        my_sim.Push(e);
    }
    
    /*
    public void AddTreeLine(int s, int d, int t){
        myPanel.addTreeLine(s,d,t);
    }
    
    public void RemoveTreeLine(int s, int d, int t){
        myPanel.RemoveTreeLine(s,d,t);
    }
    */
    
   public void simulation_pause(){
            if(my_sim != null)
                my_sim.simulation_pause();
    }
    
    public void simulation_end(){
            if(my_sim != null)
                my_sim.simulation_end();
    }
    
    public void frame_repaint(){
        myPanel.repaint();
    }
    
    public int getTreeID(){
        return treeID;
    }
    
    public int getRandomActiveNodeID(int tree){
        
        int i;
        int temp;
        
        temp = getRandomInt(numpoints);
        
        while(!getNode(temp).isActive(tree)){
            temp = getRandomInt(numpoints);
        }
        
        return temp;
    }
    
    public void InvalidateAllEventsFromIDFromTimeTOfCodeC(int id, double t, int c, int tree){
        my_sim.InvalidateAllEventsFromIDFromTimeTOfCodeC(id,t,c,tree);
    }
    
    public void InvalidateAllEventsFromIDFromTimeTOfTypeTy(int id, double t, int Ty, int tree){
        my_sim.InvalidateAllEventsFromIDFromTimeTOfTypeTy(id,t,Ty,tree);
    }
    
    public void InvalidateAllEventsFromIDFromTimeTOfTypeTyOfCodeC(int id, double t, int Ty, int c, int tree){
        my_sim.InvalidateAllEventsFromIDFromTimeTOfTypeTyOfCodeC(id,t,Ty,c, tree);
    }
    
    public void InvalidateAllEventsFromIDFromTimeT(int id, double t, int tree){
        my_sim.InvalidateAllEventsFromIDFromTimeT(id,t, tree);
    }
    
    public double getVariable(int code){
    double val=0;
    int i=0;
    
        switch(code){
            
            case(NUMNODES):
                return numnodes;
            
            case(NUMPOINTS):
                return numpoints;
                
            case(NUMSINKS):
                return numsinks;
            
            case(NUMINFRASTRUCTURES):
                for(i=0;i<MAX_TREES;i++){
                    if(num_sinks_per_tree[i]>0)
                        val++;
                }
                return val;
                
            case(CLOCK):
                return clock;
                
                
            case(SORTINGMODE):
                return sortingmode;
                        
                
            case(SIMMODE):
                return simmode;
            
                
            case(BATCH_SIMULATION):
                if(batch_simulation)
                    val=0;
                else
                    val=1;
                return val;
                
                
            case SELECTED_TM_PROTOCOL:
                    return selected_TMprotocol;
                
            case SELECTED_SD_PROTOCOL:
                    return selected_SDprotocol;
            
            case CANDIDATE_GROUP_STEP:
                    return candidate_grouping_step;
            
            case WEIGHT_METRIC1:
                return weight_metric1;
                     
            case WEIGHT_METRIC2:
                return weight_metric2;
                    
            case NEXT_RESET_PERIOD:
                return NEXT_RESET_TIMEOUT;
                
            case SELECTED_COMM_PROTOCOL:
                return selected_COMMprotocol;

            case PM_STEP:
                return pm_step;

            case PM_SLEEP:
                return pm_sleep;

            case KNEIGH_K:
                return kneigh_k;


            case ALPHA_COVERAGE:
                return alpha_coverage_value;

            default:
                break;
        }
        
        return 0;
        
    }


      public void drawRedLines(int s,int d)
    {
        myPanel.redlines.add(new pair(s, d));
    }

      public void drawBlackLines(int s,int d)
    {
        myPanel.blacklines.add(new pair(s, d));
    }

     public void drawBlueLines(int s,int d)
    {
      myPanel.bluelines.add(new pair(s, d));
    }

      public void drawGreenLines(int s,int d)
    {
       myPanel.greenlines.add(new pair(s, d));
    }

      public void drawPinkLines(int s,int d)
    {
       myPanel.pinklines.add(new pair(s, d));
    }

    public void clearRedLines()
    {
        myPanel.redlines.clear();

    }

    public void clearBlueLines()
    {
        myPanel.bluelines.clear();

    }

    public void clearGreenLines()
    {
        myPanel.greenlines.clear();

    }

    public void clearPinkLines()
    {
        myPanel.pinklines.clear();

    }

    public void clearBlackLines()
    {
        myPanel.blacklines.clear();

    }
    
    public void clearLines()
    {
        myPanel.lines.clear();
       
    }

    public void clearQueueandLines()
    {
        my_sim.myqueue_.clear();
    }
    
   /*
    ****************************************************************
    */
    
    protected class newpanel extends JPanel implements MouseListener, MouseMotionListener, constants{
        
        Graphics2D g2;
        int h,w;
        int x,y;
        int active_tree_viz;
        boolean listenPosNode;
        String node_num;
        int selected_node;
        int gridmode;
        int numnodes;
        boolean atarraya,areaC,areaS,active_nodes,filled_active_nodes,viewnumnodes;

        Vector<pair> lines;
        
        Vector<pair> blacklines=new Vector<pair>();
        Vector<pair> redlines=new Vector<pair>();
        Vector<pair> bluelines=new Vector<pair>();
        Vector<pair> pinklines=new Vector<pair>();
        Vector<pair> greenlines=new Vector<pair>();


        Color[] palette;

        int ls;
        int intl;
        int num_cells_h;
        int num_cells_w;
        int temp_cell_point;
        int ideal_p;
        
        int i,j,k;
        int d;
        int dia_point, med_diapoint;
        double temp_rad, temp_dia;
        boolean temp_active;
        
        int offset = 2;

        public newpanel(int _x, int _y){
            
            super();
            
            active_tree_viz = 0;
            listenPosNode = false;
            lines = new Vector<pair>();
            
            jLabel1 = new javax.swing.JLabel();

            setLayout(null);

            setToolTipText("");
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    formMouseReleased(evt);
                }
            });
            addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                @Override
                public void mouseMoved(java.awt.event.MouseEvent evt) {
                    formMouseMoved(evt);
                }
            });

            add(jLabel1);
            jLabel1.setBounds(10, 580, 300, 20);
            
            h=_y;
            w=_x;
            x=20;
            y=20;
            active_tree_viz = 0;
            gridmode = 0;
            selected_node = -1;
            

            atarraya = false;
            areaC = false;
            areaS = false;
            active_nodes = false;
            listenPosNode = false;
            viewnumnodes=false;
            numnodes = 1;

            node_num="Node: ";

            total_energy = 0;

            lines = new Vector<pair>();
            

            palette = new Color[12];

            palette[0] = java.awt.Color.DARK_GRAY;
            palette[1] = java.awt.Color.RED;
            palette[2] = java.awt.Color.BLUE;
            palette[3] = java.awt.Color.GREEN;
            palette[4] = java.awt.Color.ORANGE;
            palette[5] = java.awt.Color.PINK;
            palette[6] = java.awt.Color.MAGENTA;
            palette[7] = java.awt.Color.CYAN;
            palette[8] = java.awt.Color.BLACK;
            palette[9] = java.awt.Color.YELLOW;
            palette[10] = java.awt.Color.GRAY;
            palette[11] = java.awt.Color.LIGHT_GRAY;

            this.setBackground(java.awt.Color.WHITE);

            ls=0;
            intl=1;
            num_cells_h=0;
            num_cells_w=0;
            temp_cell_point=0;
            ideal_p=0;
            
        }

        /*********************************************************************/
        public void saveComponentAsJPEG(String filename) {
            //Dimension size = this.getSize();
            Dimension size = new Dimension(h+10,w+10);
            BufferedImage myImage =
                    new BufferedImage(size.width, size.height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g3 = myImage.createGraphics();
            this.paint(g3);
            try {
                OutputStream out = new FileOutputStream(filename);
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                encoder.encode(myImage);
                out.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        /*********************************************************************/



        public void clearAll(){
            lines.clear();
        }
        
        public void addLine(int _s, int _d){
            lines.add(new pair(_s, _d));
        }

        public void removeAllLinesFromSource(int s){
            int v;
            for(v=0;v<lines.size();v++){
                if(lines.get(v).getX() == s){
                    lines.remove(v);
                    v--;
                }
            }
        }

        public void removeLine(int _s, int _d){
            lines.remove(new pair(_s, _d));
        }

        public int getW(){
            return w;
        }
        
        public int getH(){
            return h;
        }
        
        public void setArea(int _x, int _y){
            setBounds(0,0, _x+100, _y+100);
            h=_y;
            w=_x;
            repaint();
        }
        
        public void setActive_tree_viz(int active_tree_){
            active_tree_viz=active_tree_;
        }
        
        public void setRangeArea(boolean s, boolean s2){
            areaC = s;
            areaS = s2;
            repaint();
        }
        
        private void formMouseMoved(java.awt.event.MouseEvent evt) {                                
    // TODO add your handling code here:

        if(listenPosNode){
            x = evt.getX();
            y = evt.getY();
            //showMessage("X="+x+" Y="+y);
            jTextField23.setText(""+x);
            jTextField24.setText(""+y);
        }

        }                               

        private void formMouseReleased(java.awt.event.MouseEvent evt) {                                   
    // TODO add your handling code here:
            x = evt.getX()-5;
            y = evt.getY()-5;
            int temp=0;

            if(listenPosNode){
                
                setNodePos(x, y);
                tempnumnodes++;
                
                if(tempnumnodes==numnodes)
                    listenPosNode = false;

            }else{
                temp = getNodeNum(x,y);
                if(temp!=-1){
                    node_num = "Node: " + temp;
                }else{
                    node_num = "No node selected";
                }
                selected_node = temp;
                showNodeInfo(temp);
                this.setToolTipText(node_num);
                
                if(CHOOSE_BASE_STATION)
                {
                   CHOOSE_BASE_STATION=false;
                   BASE_STATION_ID=selected_node;
                   getNode(BASE_STATION_ID).color=Color.GREEN;
                    
                }
               else if(graph_selection)
               {
                  GRAPH_NODE_ID=selected_node;
               }
              

                


            

            showMessage("");

            }
        }

        public void mouseClicked(MouseEvent e){}
        public void mouseEntered(MouseEvent e){}
        public void mouseExited(MouseEvent e){}
        public void mousePressed(MouseEvent e){}
        public void mouseReleased(MouseEvent e){}

        public void mouseDragged(MouseEvent e){}
        public void mouseMoved(MouseEvent e){}
        
        
        public void setSelectedNode(int id){
            if(id<numpoints){
                selected_node = id;
                node_num = "Node: " + id;
            } else{
                selected_node = -1;
                node_num = "No node selected";
            }
            this.setToolTipText(node_num);
            repaint();
        }

        public int getNodeNum(int x_, int y_){

            boolean found=false;
            int x1,y1;

            i=0;
            while(!found && i<numpoints){
                x1 = (int)getNode(i).getPosition().getX();
                y1 = (int)getNode(i).getPosition().getY();

                if(x_>=x1-offset && x_<=x1+offset && y_>=y1-offset && y_<=y1+offset){
                    found=true;
                    return i;
                }
                i++;
            }

            return -1;
        }
    
        public void setGridmode(int val){
            gridmode = val;
            repaint();
        }

        public String getGridMode(){
            if(gridmode==1)
                return "H-V";
            else
                if(gridmode==2)
                    return "Diagonal";

            return "No grid";
        }
        
        public int CalcGridSide(){

            ls=0;
            intl=1;
            num_cells_h=0;
            num_cells_w=0;
            temp_cell_point=0;
            ideal_p=0;

            //Calculating parameters of the grid
                if(gridmode>0){

                    if(gridmode==1){
                        ls = (int)radius;
                    }

                    if(gridmode==2){
                        ls = (int)((radius/java.lang.Math.sqrt(2)));
                    }

                    intl = (int)java.lang.Math.ceil(ls);

                    num_cells_h = h/intl;
                    num_cells_w = w/intl;
                    temp_cell_point=0;
                }

                ideal_p = java.lang.Math.round((h/intl)*(w/intl));
            return 0;
        }

        public int getGridParents(){
            return ideal_p;
        }
        
        public void setNumNodes(int _numnodes){
            numnodes = _numnodes;
        }

        public void setlistenPosNode(boolean l){
            listenPosNode=l;
            tempnumnodes = 0;
        }
        
        public void setAtarraya(boolean s){
            atarraya = s;
            repaint();
        }

        public void setActiveNodes(boolean s){
            active_nodes = s;
            repaint();
        }

        public void setFilledActiveNodes(boolean s){
            filled_active_nodes = s;
            repaint();
        }

        public void setViewNodeNumber(boolean sw){
            viewnumnodes = sw;
            repaint();
        }

        @Override
        public void paint(Graphics g){
            
            if(!batch_simulation && !batch_creation){
                super.paint(g);
                paint_(g);
            }
        
        }
        
        public void paint_(Graphics g){
            int temp = 0, temp2=0;
            node temp_node;
            
            if(!batch_simulation && !batch_creation && !no_paint){
                //super.paint(g);
                g2 = (Graphics2D) g;

                g2.translate(5,5);

                CalcGridSide();
                
                i=0;
                j=0;
                k=0;
                dia_point=6;
                med_diapoint=3;
                temp_active=false;

                
                //Drawing the grid
                if(gridmode>0){
                    for(i=0;i<=num_cells_h;i++){
                        g2.drawLine(0,temp_cell_point,w,temp_cell_point);
                        temp_cell_point+=intl;
                    }

                    temp_cell_point=0;

                    for(i=0;i<=num_cells_w;i++){
                        g2.drawLine(temp_cell_point,0,temp_cell_point,h);
                        temp_cell_point+=intl;
                    }

                }


                //If the Ataraya mode is false, then the program will show the max graph.
                if(!atarraya){
                    for(i=0;i<numpoints;i++){
                        if(DEPLOYMENT)
                        {
                            if(i<20)
                            {
                            getNode(i).setEnergy(1000.0);
                            }
                            else if(i>20&&i<40)
                            {
                                getNode(i).setEnergy(600.0);
                            }
                             else if(i>40&&i<60)
                            {
                                getNode(i).setEnergy(400.0);
                            }
                             else if(i>60&&i<80)
                            {
                                getNode(i).setEnergy(800.0);
                            }
                            else
                            {
                                getNode(i).setEnergy(500.0);
                            }

                        }
                        dia_point = (int)java.lang.Math.round(6*((double)getNode(i).getEnergy()/MAX_ENERGY));
                        med_diapoint = dia_point/2;
                        g2.fillOval((int)getNode(i).getPosition().getX()-med_diapoint,(int)getNode(i).getPosition().getY()-med_diapoint,dia_point,dia_point);
                        if(viewnumnodes){
                            g2.drawString(""+i,(int)getNode(i).getPosition().getX(),(int)getNode(i).getPosition().getY()-2);
                        }
                    }
                    DEPLOYMENT=false;

                    for(i=0;i<lines.size();i++){
                        g2.drawLine((int)getNode(lines.get(i).getX()).getPosition().getX(),(int)getNode(lines.get(i).getX()).getPosition().getY(),(int)getNode(lines.get(i).getY()).getPosition().getX(),(int)getNode(lines.get(i).getY()).getPosition().getY());
                    }



                }else{

                    if(atarraya){
                        if(active_tree_viz != -1 && !view_active_tree){
                            //******************************************
                            // DRAWING TOPOLOGY IN INDIVIDUAL SELECTED TREE MODE
                            //******************************************

                            for(i=0;i<numpoints;i++){
                                
                                //Save the active/non-active state of the node i
                                temp_active = getNode(i).isActive(active_tree_viz);

                                //Draw communication area
                                if(areaC && temp_active){
                                    g2.setColor(palette[8]);
                                    temp_rad = getNode(i).getRadius(active_tree_viz);
                                    temp_dia = temp_rad*2;

                                    if(filled_active_nodes)
                                        g2.fillOval((int)(getNode(i).getPosition().getX()-temp_rad),(int)(getNode(i).getPosition().getY()-temp_rad),(int)temp_dia,(int)temp_dia);
                                    else
                                        g2.drawOval((int)(getNode(i).getPosition().getX()-temp_rad),(int)(getNode(i).getPosition().getY()-temp_rad),(int)temp_dia,(int)temp_dia);
                                }
                            }

                            for(i=0;i<numpoints;i++){
                                //Save the active/non-active state of the node i
                                temp_active = getNode(i).isActive(active_tree_viz);

                                //Draw sensing area
                                if(areaS && temp_active){
                                    g2.setColor(palette[5]);
                                    temp_rad = getNode(i).getSense_Radius();
                                    temp_dia = temp_rad*2;
                                    if(filled_active_nodes)
                                        g2.fillOval((int)(getNode(i).getPosition().getX()-temp_rad),(int)(getNode(i).getPosition().getY()-temp_rad),(int)temp_dia,(int)temp_dia);
                                    else
                                        g2.drawOval((int)(getNode(i).getPosition().getX()-temp_rad),(int)(getNode(i).getPosition().getY()-temp_rad),(int)temp_dia,(int)temp_dia);
                                }

                            }

                            for(i=0;i<numpoints;i++){
                                
                                // if the node is not alive, then draw nothing
                                if(getNode(i).isAlive()){
                                    
                                    //Save the active/non-active state of the node i
                                    temp_active = getNode(i).isActive(active_tree_viz);
                                
                                    g2.setColor(palette[active_tree_viz]);
                                    
                                    
                                    if(jCheckBox26.isSelected()){
                                        //All gateways
                                        temp=getNode(i).getInfrastructure(active_tree_viz).getNumGateways();

                                        for(j=0;j<temp;j++){
                                            d = getNode(i).getGatewayID(active_tree_viz,j);
                                            if(d>=0){
                                                if(getNode(d).isAlive() && getNode(i).isNeighbor(d)){
                                                    if(d>=0 && d!=i)
                                                        g2.drawLine((int)getNode(i).getPosition().getX(),(int)getNode(i).getPosition().getY(),(int)getNode(d).getPosition().getX(),(int)getNode(d).getPosition().getY());
                                                }
                                            }
                                        }
                                    
                                    }
                                        //Just the default gateway
                                        d = getNode(i).getDefaultGateway(active_tree_viz);
                                        if(d>=0){
                                            if(getNode(d).isAlive() && getNode(i).isNeighbor(d)){
                                                if(d>=0 && d!=i)
                                                    g2.drawLine((int)getNode(i).getPosition().getX(),(int)getNode(i).getPosition().getY(),(int)getNode(d).getPosition().getX(),(int)getNode(d).getPosition().getY());
                                            }
                                        }
                                    
                                    
                                    g2.setColor(palette[0]);
                                    if(!active_nodes){
                                        //See the Id of every node
                                        if(viewnumnodes){
                                                g2.drawString(""+i,(int)getNode(i).getPosition().getX(),(int)getNode(i).getPosition().getY()-2);
                                            }
                                    }else{
                                        if(temp_active){
                                            //See the Id of the active node
                                            if(viewnumnodes){
                                                g2.drawString(""+i,(int)getNode(i).getPosition().getX(),(int)getNode(i).getPosition().getY()-2);
                                            }
                                            // Asign a different color to parents compared to the tree color
                                            g2.setColor(palette[active_tree_viz+1]);
                                        }else
                                            if(getNode(i).isSleeping(active_tree_viz))
                                                g2.setColor(palette[7]);
                                            else if(getNode(i).isInactive(active_tree_viz))
                                                g2.setColor(palette[11]);
                                            else
                                                g2.setColor(palette[0]);
                                    }

                                    dia_point = (int)java.lang.Math.round(6*((double)getNode(i).getEnergy()/MAX_ENERGY));
                                    med_diapoint = dia_point/2;
                                    g2.fillOval((int)getNode(i).getPosition().getX()-med_diapoint,(int)getNode(i).getPosition().getY()-med_diapoint,dia_point,dia_point);
                                }
                            }
                        }else{
                            if(!view_active_tree){
                                //******************************************
                                // DRAWING ALL TOPOLOGIES MODE
                                //******************************************
                                for(k=0;k<MAX_TREES;k++){
                                    for(i=0;i<numpoints;i++){
                                        if(getNode(i).isAlive()){
                                           //Draw all the lines of the trees 
                                            g2.setColor(palette[k]);
                                            
                                            if(jCheckBox26.isSelected()){
                                                //All gateways
                                                temp=getNode(i).getNumGateways(k);
                                                for(j=0;j<temp;j++){
                                                    d = getNode(i).getGatewayID(k,j);
                                                    if(d>=0){
                                                        if(getNode(d).isAlive() && getNode(i).isNeighbor(d)){
                                                            if(d>=0 && d!=i)
                                                                g2.drawLine((int)getNode(i).getPosition().getX(),(int)getNode(i).getPosition().getY(),(int)getNode(d).getPosition().getX(),(int)getNode(d).getPosition().getY());
                                                        }
                                                    }
                                                }
                                            }
                                                //Just default gateways
                                                d = getNode(i).getDefaultGateway(k);
                                                if(d>=0){
                                                    if(getNode(d).isAlive() && getNode(i).isNeighbor(d)){
                                                        if(d>=0 && d!=i)
                                                            g2.drawLine((int)getNode(i).getPosition().getX(),(int)getNode(i).getPosition().getY(),(int)getNode(d).getPosition().getX(),(int)getNode(d).getPosition().getY());
                                                    }
                                                }
                                            
                                            
                                        }
                                    }

                                }
                                
                                for(i=0;i<numpoints;i++){
                                
                                    if(getNode(i).isAlive()){
                                         temp = getNode(i).getActiveTree();
                                         temp_active = getNode(i).isActive(temp);

                                         g2.setColor(palette[0]);
                                            if(!active_nodes){
                                                //See the Id of every node
                                                if(viewnumnodes){
                                                        g2.drawString(""+i,(int)getNode(i).getPosition().getX(),(int)getNode(i).getPosition().getY()-2);
                                                    }
                                            }else{
                                                if(temp_active){
                                                    //See the Id of the active node
                                                    if(viewnumnodes){
                                                        g2.drawString(""+i,(int)getNode(i).getPosition().getX(),(int)getNode(i).getPosition().getY()-2);
                                                    }
                                                    // Asign a different color to parents compared to the tree color
                                                    g2.setColor(palette[temp+1]);
                                                }else
                                                    if(getNode(i).isSleeping(temp))
                                                        g2.setColor(palette[7]);
                                                    else if(getNode(i).isInactive(temp))
                                                        g2.setColor(palette[11]);
                                                    else
                                                        g2.setColor(palette[0]);
                                            }

                                        dia_point = (int)java.lang.Math.round(6*((double)getNode(i).getEnergy()/MAX_ENERGY));
                                        med_diapoint = dia_point/2;
                                        g2.fillOval((int)getNode(i).getPosition().getX()-med_diapoint,(int)getNode(i).getPosition().getY()-med_diapoint,dia_point,dia_point);
                                        }
                                }
                                
                            }else{
                              //******************************************
                              // DRAWING INDIVIDUAL TOPOLOGY IN ACTIVE TREE MODE
                              //******************************************
                                    for(i=0;i<numpoints;i++){

                                        temp = getNode(i).getActiveTree();
                                        //Save the active/non-active state of the node i
                                        temp_active = getNode(i).isActive(temp);

                                        //Draw communication area
                                        if(areaC && temp_active){
                                            g2.setColor(palette[8]);
                                            temp_rad = getNode(i).getRadius();
                                            temp_dia = temp_rad*2;

                                            if(filled_active_nodes)
                                                g2.fillOval((int)(getNode(i).getPosition().getX()-temp_rad),(int)(getNode(i).getPosition().getY()-temp_rad),(int)temp_dia,(int)temp_dia);
                                            else
                                                g2.drawOval((int)(getNode(i).getPosition().getX()-temp_rad),(int)(getNode(i).getPosition().getY()-temp_rad),(int)temp_dia,(int)temp_dia);
                                        }
                                    }

                                    for(i=0;i<numpoints;i++){
                                        //Save the active/non-active state of the node i
                                        //temp_active = getNode(i).isActive();
                                        temp_active = getNode(i).isActive(temp);

                                        //Draw sensing area
                                        if(areaS && temp_active){
                                            g2.setColor(palette[5]);
                                            temp_rad = getNode(i).getSense_Radius();
                                            temp_dia = temp_rad*2;
                                            if(filled_active_nodes)
                                                g2.fillOval((int)(getNode(i).getPosition().getX()-temp_rad),(int)(getNode(i).getPosition().getY()-temp_rad),(int)temp_dia,(int)temp_dia);
                                            else
                                                g2.drawOval((int)(getNode(i).getPosition().getX()-temp_rad),(int)(getNode(i).getPosition().getY()-temp_rad),(int)temp_dia,(int)temp_dia);
                                        }

                                    }

                                    for(i=0;i<numpoints;i++){
                                        
                                        if(getNode(i).isAlive()){
                                            k=getNode(i).getActiveTree();
                                            //Save the active/non-active state of the node i
                                            temp_active = getNode(i).isActive(k);

                                            g2.setColor(palette[k]);
                                            
                                            if(jCheckBox26.isSelected()){
                                                //All gateways
                                                temp=getNode(i).getInfrastructure(k).getNumGateways();

                                                for(j=0;j<temp;j++){
                                                    d = getNode(i).getGatewayID(k,j);
                                                    if(d>=0){
                                                        if(getNode(d).isAlive() && getNode(i).isNeighbor(d)){
                                                            if(d>=0 && d!=i)
                                                                g2.drawLine((int)getNode(i).getPosition().getX(),(int)getNode(i).getPosition().getY(),(int)getNode(d).getPosition().getX(),(int)getNode(d).getPosition().getY());
                                                        }
                                                    }
                                                }
                                           }
                                                //just default gateways
                                                d = getNode(i).getDefaultGateway(k);
                                                if(d>=0){
                                                    if(getNode(d).isAlive() && getNode(i).isNeighbor(d)){
                                                        if(d>=0 && d!=i)
                                                            g2.drawLine((int)getNode(i).getPosition().getX(),(int)getNode(i).getPosition().getY(),(int)getNode(d).getPosition().getX(),(int)getNode(d).getPosition().getY());
                                                    }
                                                }
                                           
                                            
                                            g2.setColor(palette[0]);
                                            if(!active_nodes){
                                                //See the Id of every node
                                                if(viewnumnodes){
                                                        g2.drawString(""+i,(int)getNode(i).getPosition().getX(),(int)getNode(i).getPosition().getY()-2);
                                                    }
                                            }else{
                                                if(temp_active){
                                                    //See the Id of the active node
                                                    if(viewnumnodes){
                                                        g2.drawString(""+i,(int)getNode(i).getPosition().getX(),(int)getNode(i).getPosition().getY()-2);
                                                    }
                                                    // Asign a different color to parents compared to the tree color
                                                    g2.setColor(palette[k+1]);
                                                }else
                                                    if(getNode(i).isSleeping(k))
                                                        g2.setColor(palette[7]);
                                                    else if(getNode(i).isInactive(k))
                                                        g2.setColor(palette[11]);
                                                    else
                                                        g2.setColor(palette[0]);
                                                        
                                            }


                                            dia_point = (int)java.lang.Math.round(6*((double)getNode(i).getEnergy()/MAX_ENERGY));
                                            med_diapoint = dia_point/2;
                                            g2.fillOval((int)getNode(i).getPosition().getX()-med_diapoint,(int)getNode(i).getPosition().getY()-med_diapoint,dia_point,dia_point);

                                        }

                            }
                                
                            }
                        }
                        }


                    }

                if(sat)
                 {
                     g2.setColor(Color.BLACK);
                     for(i=0;i<blacklines.size();i++){


                        g2.drawLine((int)getNode(blacklines.get(i).getX()).getPosition().getX(),(int)getNode(blacklines.get(i).getX()).getPosition().getY(),(int)getNode(blacklines.get(i).getY()).getPosition().getX(),(int)getNode(blacklines.get(i).getY()).getPosition().getY());
                    }

                     g2.setColor(Color.RED);
                 for(i=0;i<redlines.size();i++){


                        g2.drawLine((int)getNode(redlines.get(i).getX()).getPosition().getX(),(int)getNode(redlines.get(i).getX()).getPosition().getY(),(int)getNode(redlines.get(i).getY()).getPosition().getX(),(int)getNode(redlines.get(i).getY()).getPosition().getY());
                    }

                     g2.setColor(Color.BLUE);
                 for(i=0;i<bluelines.size();i++){


                        g2.drawLine((int)getNode(bluelines.get(i).getX()).getPosition().getX(),(int)getNode(bluelines.get(i).getX()).getPosition().getY(),(int)getNode(bluelines.get(i).getY()).getPosition().getX(),(int)getNode(bluelines.get(i).getY()).getPosition().getY());
                    }

                      g2.setColor(Color.YELLOW);
                 for(i=0;i<pinklines.size();i++){


                        g2.drawLine((int)getNode(pinklines.get(i).getX()).getPosition().getX(),(int)getNode(pinklines.get(i).getX()).getPosition().getY(),(int)getNode(pinklines.get(i).getY()).getPosition().getX(),(int)getNode(pinklines.get(i).getY()).getPosition().getY());
                    }

                       g2.setColor(Color.GREEN);
                 for(i=0;i<greenlines.size();i++){


                        g2.drawLine((int)getNode(greenlines.get(i).getX()).getPosition().getX(),(int)getNode(greenlines.get(i).getX()).getPosition().getY(),(int)getNode(greenlines.get(i).getY()).getPosition().getX(),(int)getNode(greenlines.get(i).getY()).getPosition().getY());
                    }


                }

                    if(selected_node!=-1){
                      //  g2.setColor(palette[4]);
                        //g2.fillOval((int)getNode(selected_node).getPosition().getX()-4,(int)getNode(selected_node).getPosition().getY()-4,8,8);

                        //Draw comm area on the selected node
                        g2.setColor(palette[8]);
                        temp_rad = getNode(selected_node).getRadius(getNode(selected_node).getActiveTree());
                        temp_dia = temp_rad*2;
                        g2.drawOval((int)(getNode(selected_node).getPosition().getX()-temp_rad),(int)(getNode(selected_node).getPosition().getY()-temp_rad),(int)temp_dia,(int)temp_dia);

                        //Draw sensing area on the selected node
                        g2.setColor(palette[5]);
                        temp_rad = getNode(selected_node).getSense_Radius();
                        temp_dia = temp_rad*2;
                        g2.drawOval((int)(getNode(selected_node).getPosition().getX()-temp_rad),(int)(getNode(selected_node).getPosition().getY()-temp_rad),(int)temp_dia,(int)temp_dia);

                    }
              
                
                //area lines
                if(filled_active_nodes)
                    g2.setColor(palette[11]);
                else
                    g2.setColor(palette[0]);
                g2.drawLine(0,w,h,w);
                g2.drawLine(h,0,h,w);
                g2.drawLine(0,0,0,w);
                g2.drawLine(0,0,h,0);

                 if(sat)
                {


                     for(int i=0;i<getVariable(NUMPOINTS);i++)
                     {

                      g2.setColor(getNode(i).color);
                      g2.fillOval((int)getNode(i).getPosition().getX()-4,(int)getNode(i).getPosition().getY()-4,8,8);
                    }
                }
                 
                
                }
            
            g.dispose();
            
        }
            
        }

     public byte[] Decrypt(byte[] data,SecretKey key)
    {
        byte[] original=null;
        try
        {

        Cipher cipher = Cipher.getInstance("AES");
          cipher.init(Cipher.DECRYPT_MODE, key);
       original = cipher.doFinal(data);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
       return original;
    }

    public String Encrypt(byte[] data,SecretKey key)
    {
        byte[] original=null;
        try
        {
        Cipher cipher = Cipher.getInstance("AES");
          cipher.init(Cipher.ENCRYPT_MODE, key);
       original = cipher.doFinal(data);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }

       return new BASE64Encoder().encode(original);
    }
        
    
    /*
     * ***********************************
     */
    
    protected class the_sim extends Thread{
        
        eventQueue myqueue_;
        boolean mainSW_, simstart_, simfin_;
        int count_end;
        double prev_clock;
        
        public the_sim(){
            myqueue_ = new eventQueue();
            mainSW_=true;
            simstart_=false;
            simfin_=false;
            count_end = numsinks;
        }
        
        public void Push(event_sim ev){
            myqueue_.Push(ev);
        }
        
        public void InvalidateAllEventsFromIDFromTimeTOfCodeC(int id, double t, int c, int tree){
            myqueue_.InvalidateAllEventsFromIDFromTimeTOfCodeC(id,t,c,tree);
        }

        public void InvalidateAllEventsFromIDFromTimeT(int id, double t, int tree){
            myqueue_.InvalidateAllEventsFromIDFromTimeT(id,t,tree);
        }
        
        public void InvalidateAllEventsFromIDFromTimeTOfTypeTy(int id, double t, int Ty, int tree){
            myqueue_.InvalidateAllEventsFromIDFromTimeTOfTypeTy(id,t,Ty,tree);
        }
        
        public void InvalidateAllEventsFromIDFromTimeTOfTypeTyOfCodeC(int id, double t, int Ty, int c, int tree){
            myqueue_.InvalidateAllEventsFromIDFromTimeTOfTypeTyOfCodeC(id,t,Ty,c,tree);
        }

        public void InvalidateRxAllEventsFromIDFromTimeTOfTypeTyOfCodeC(int id, double t, int Ty, int c, int tree){
            myqueue_.InvalidateRxAllEventsFromIDFromTimeTOfTypeTyOfCodeC(id,t,Ty,c,tree);
        }

        public void simulation_end_by_user(){
            mainSW_ = false;
        }
        
        public void simulation_end(){
            //The simulation will end when all sinks say that the simulation has ended
            if(count_end>1)
                count_end--;
            else
                simfin_ = true;
        }
        
        public void simulation_start(){
            simstart_ = true;
        }
        
        public void simulation_pause(){
            simstart_ = false;
        }
        
        public boolean isSimulationStarted(){
            return simstart_;
        } 
        
        public boolean isSimulationOn(){
            return mainSW_;
        }

        public void updateEnergy(double time){

            int i, temp_tree;

            //Just regular nodes will have their energy updated (0 to numnodes-1)
            //The sink nodes are assumed to have infinite energy resources
            for(i=0; i<numnodes; i++){
                if(getNode(i).isAlive()){
                    getNode(i).useEnergy(OPER_IDLE, time);
                    checkEnergy(i);
                }
            }

        }

        @Override
        public void run(){
            sim_error = "no error";
            event_sim tempevent_;
            boolean sw=false;
            long c=0;            
            clock=0;
            prev_clock=0;
            double temp_clock_for_step=0;
            int i=0,j=0;
            double prob_error;
            int size_packet;
            double temp_radius = 0;

            boolean rx_message = true;
            boolean tx_message = true;

            while(mainSW_){
                /*
                 * 1. Check for the next event on the queue
                 * If event is not null,
                 *   2. Update the clock
                 *   3. Send the event to the event handler
                 * end if
                 * 4. Back to step 1.
                 */
                
                if(simstart_){
                    /*try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(atarraya_frame.class.getName()).log(Level.SEVERE, null, ex);
                    }*/

                    tempevent_ = myqueue_.Pop();
                    if(tempevent_!=null)
                    {
                         simulator.HandleEvent(tempevent_);
                    }
                    
                    sw = checkSimFinish();
                    if(tempevent_ != null && !sw && !simfin_){

                        if(tempevent_.isValid()){

                            //If the event is a Message....
                            if(tempevent_.getType()==SENSOR_PROTOCOL && tempevent_.getCode()==DATA)
                                    i=0;

                            if(tempevent_.getMessageType() != 0){

                                

                                if(tempevent_.getMessageType() == OPER_RX){
                                    //Boolean that defined if the node is able to receive the message
                                    //rx_message =  getNode(tempevent_.getReceiver()).isAlive() && getNode(tempevent_.getReceiver()).isRadioOn(tempevent_.getTree());
                                    //rx_message =  getNode(tempevent_.getReceiver()).isAlive() && ((getNode(tempevent_.getReceiver()).isRadioOn(tempevent_.getTree()) && getNode(tempevent_.getReceiver()).getEnergyState() != STATE_JUST_RADIO) || tempevent_.getType() == TM_PROTOCOL);
                                    rx_message =  getNode(tempevent_.getReceiver()).isAlive() && (getNode(tempevent_.getReceiver()).isRadioOn() || (tempevent_.getType() == TM_PROTOCOL && getNode(tempevent_.getSender()).getEnergyState() == STATE_SLEEP));
                                    //Comm radius of the sender
                                    temp_radius = getNode(tempevent_.getSender()).getRadius(tempevent_.getTree());
                                } else if(tempevent_.getMessageType() == OPER_TX){
                                    //If the event was the transmission of the packet, it will not be executed. It will be used to use the energy on the sender node.
                                    tempevent_.setInvalid();
                                }
                                //Boolean that defined if the node is able to send the message
                                //tx_message =  getNode(tempevent_.getSender()).isAlive() && getNode(tempevent_.getSender()).isRadioOn(tempevent_.getTree());
                                //tx_message =  getNode(tempevent_.getSender()).isAlive() && ((getNode(tempevent_.getSender()).isRadioOn(tempevent_.getTree()) && getNode(tempevent_.getSender()).getEnergyState() != STATE_JUST_RADIO) || tempevent_.getType() == TM_PROTOCOL);
                                tx_message =  getNode(tempevent_.getSender()).isAlive() && (getNode(tempevent_.getSender()).isRadioOn(tempevent_.getTree()) || (tempevent_.getType() == TM_PROTOCOL && getNode(tempevent_.getSender()).getEnergyState() == STATE_SLEEP));
                                size_packet = simulator.GetMessageSize(tempevent_.getCode(), tempevent_.getType());
                                prob_error = 1-java.lang.Math.pow(1-bit_error_rate,8*size_packet);
                                

                                // If the event is the reception of a packet and both sender and receiver can send and receive the message, then this event will be counted and executed
                                if(tempevent_.getMessageType() == OPER_RX && rx_message && tx_message){

                                    // If the package did not get lost and if radius of the sender can reach the receiver
                                    if(getRandom(1) >= prob_error && temp_radius>=getNode(tempevent_.getSender()).calcDistance(getNode(tempevent_.getReceiver()))){

                                        //Calculating the number of DATA packages
                                        if(tempevent_.getCode() == DATA){
                                            if(getNode(tempevent_.getReceiver()).isSink(tempevent_.getTree())){
                                                num_messages_data_r_sink++;
                                            }
                                            //If the message was ment to be received by temp_ID, then it will count.
                                            if(tempevent_.getDestination() == tempevent_.getReceiver())
                                                getNode(tempevent_.getReceiver()).addCountDataMessages(tempevent_.getTree());
                                        }


                                        //If the node is not a sink, use energy... Sinks are assumed tp have infinite energy!!
                                        if(!getNode(tempevent_.getReceiver()).isSink()){
                                            //Use of the energy for reception in the regular radio
                                            getNode(tempevent_.getReceiver()).useEnergy(getOperType_TX_RX(tempevent_.getCode(),OPER_RX), tempevent_.getTree());
                                           
                                            //Checking if the receiver node is dead after this operation
                                            //checkEnergy(tempevent_.getReceiver());
                                        }

                                        //Register a received packet
                                        getNode(tempevent_.getReceiver()).addPacketsReceived(tempevent_.getCode());
                                        total_energy_spent = total_energy_spent + getNode(tempevent_.getReceiver()).calcEnergy(getOperType_TX_RX(tempevent_.getCode(),OPER_RX), tempevent_.getTree());

                                        //Counting received packets
                                        if(size_packet == SIZE_LONG_PACKETS){
                                            num_messages_long_r++;
                                        }else{
                                            num_messages_r++;
                                        }

                                    }else{
                                        //Lost packets count
                                         if(size_packet == SIZE_LONG_PACKETS)
                                            num_messages_long_l++;
                                         else
                                            num_messages_l++;
                                    } //End of the reception of the packet

                                 // If the event was de transmission of the packet and the sender can indeed send it
                                }else if(tempevent_.getMessageType() == OPER_TX && tx_message){

                                        //Counting sent packets
                                        if(size_packet == SIZE_LONG_PACKETS){
                                            num_messages_long++;
                                        }else{
                                            num_messages++;
                                        }

                                        //Updating the total energy spent on the network
                                        total_energy_spent = total_energy_spent + getNode(tempevent_.getSender()).calcEnergy(getOperType_TX_RX(tempevent_.getCode(),OPER_TX),tempevent_.getTree());

                                        //Register a sent packet
                                        getNode(tempevent_.getSender()).addPacketsSent(tempevent_.getCode());

                                        //Use of the energy for transmission, in the node is not a Sink
                                        if(!getNode(tempevent_.getSender()).isSink()){
                                                //Use of the energy for transmission in the regular radio
                                                getNode(tempevent_.getSender()).useEnergy(getOperType_TX_RX(tempevent_.getCode(),OPER_TX), tempevent_.getTree());
                                                //Checking if the sender node is dead
                                                //checkEnergy(tempevent_.getSender());
                                        }

                                    }
                            }

                            if(tempevent_.isValid()){

                            if(show_events){
                                showEventList(tempevent_.toString());
                            }

                            
                            //Saving all stats if the option was selected
                            if(save_all_stats){
                                try{
                                    if(clock >= temp_clock_for_step){
                                        temp_clock_for_step+=save_all_stats_step;
                                        CountNotVisited();
                                        addStatToReport(the_name);
                                    }
                                }catch(Exception ex){ex.printStackTrace();}
                            }
                            
                            
                            num_events++;

                            if(clock > 500*j){
                              j++;
                              showEventCount("Events: "+num_events+" Clock: "+clock_format.format(clock));
                              jLabel43.setText("Queue Size: "+ myqueue_.getSize());
                              myPanel.repaint();
                            }
                            
//                            showEventCount("Events: "+num_events+" Clock: "+clock_format.format(clock));
  //                          jLabel43.setText("Queue Size: "+ myqueue_.getSize());

                            //If there is such a thing like time travel, then this condition will become true!
                            if(clock > tempevent_.getTime())
                                i=0;
                            
                            clock = tempevent_.getTime();

                            //Updating the energy of all nodes
                            if(clock - prev_clock >0){
                                updateEnergy(clock - prev_clock);
                                //Update the time on the previous clock
                                prev_clock = clock;
                            }
                            

                            simulator.HandleEvent(tempevent_);
                            
                            if(clock > max_clock_time)
                                simfin_ = true;
                            
                            //Online stats shown on the interace
                            if(jCheckBox6.isSelected()){
                                UpdateStats();
                            }

                            //Saving all events if the option was selected
                            if(save_events_log){
                                try{
                                    mywrite_log_ev.write(tempevent_.toString());
                                    mywrite_log_ev.newLine();
                                }catch(Exception ex){ex.printStackTrace();}
                            }
                        }
                        }

                    }else{
                        if(clock>0){
                            simstart_=false;
                            mainSW_=false;
                            //sw = checkSimFinish();
                            if(sw || simfin_){
                                jLabel1.setText("Simulation Finished");
                                 if(!batch_simulation){
                                    printmessage("Simulation Finished in "+clock);
                                }
                                
                            }else{
                                if(myqueue_.getSize()==0){
                                    if(!batch_simulation)
                                        printmessage("ERROR IN SIMULATION!! Event queue is empty and algorithm did not finish!!");
                                    sim_error = "error";
                                }
                            }
                            
                            //Saving the last image of the stats of a simulation not in a batch execution
                                if(!batch_simulation){
                                    if(save_stats){
                                        try{
                                            CountNotVisited();
                                            addStatToReport(the_name2);
                                            SaveReport();
                                        }catch(Exception ex){ex.printStackTrace();}
                                    }else{
                                        //Add the line to the report area, just in case no stats are saved.
                                        jTextArea2.append(getStatsHeaderForExcel());
                                        CountNotVisited();
                                        jTextArea2.append(getStatsRowForExcel());
                                    }
                                    
                                    if(save_lifetime_log){
                                        addStatToLifetimeLog(""+clock);
                                        addLineToLifetimeLog(the_name2);
                                        SaveReportLifetime();
                                    }
                                    
                                    if(save_results_log){
                                        addLineToResultsLog(the_name2);
                                        SaveReportResults();
                                    }

                                }
                                
                                //Saving the log of events of a simulation. This is only for individual topologies.
                                if(save_events_log){
                                    try{
                                        mywrite_log_ev.flush();
                                        mywrite_log_ev.close();
                                        fout_log_ev.close();
                                    }catch(Exception ex){ex.printStackTrace();}
                                }
                            
                                if(batch_simulation){                      
                                        
                                    if(save_stats){
                                        try{
                                            CountNotVisited();
                                            addStatToReport(the_name,"final");
                                        }catch(Exception ex){ex.printStackTrace();}
                                    }else{
                                        //Add the line to the report area, just in case no stats are saved.
                                        CountNotVisited();
                                        jTextArea2.append(getStatsRowForExcel());
                                    }
                                    
                                    
                                    if(indiv_reports && save_stats){
                                        SaveReport();
                                    }else{
                                        if(!save_stats){
                                            CountNotVisited();
                                            jTextArea2.append(getStats());
                                        }
                                    }

                                    if(save_lifetime_log){
                                        addStatToLifetimeLog(""+clock);
                                        addLineToLifetimeLog(the_name);
                                    }
                                    
                                    
                                    if(save_results_log){
                                        addLineToResultsLog(the_name2);
                                    }
                                    

                                    current_instance_topology_from_batch++;
                                    if(current_instance_topology_from_batch == multiplicity*total_alpha){
                                        current_topology_from_batch++;
                                        current_instance_topology_from_batch=0;
                                    }

                                    jProgressBar1.setValue(current_topology_from_batch*multiplicity*total_alpha + current_instance_topology_from_batch);
                                    jLabel50.setText("Progress: "+current_instance_topology_from_batch+" instance of file "+current_topology_from_batch+" of "+num_files_topology_from_batch+" files total.");


                                    if(current_topology_from_batch==num_files_topology_from_batch){
                                        current_topology_from_batch=0;
                                        current_instance_topology_from_batch=0;
                                        if(sumarized_reports && save_stats)
                                            SaveReport();
                                        if(save_lifetime_log)
                                            SaveReportLifetime();
                                        
                                        printmessage("All "+num_files_topology_from_batch+" simulation are finished!");
                                    }

                                }


                            
                        }  
                        c++; 
                    }
                }
            }
            showMessage("Agent stoped!"+c);
            showEventCount("Events: "+num_events+" Clock: "+clock);
            //batch_simulation = false;
        }
        
    }
    
    public void printmessage(String mes){
            javax.swing.JOptionPane.showMessageDialog(this, mes);
        }
    

    
    /*
    ****************************************************************
    */
    
    
    
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox10;
    private javax.swing.JCheckBox jCheckBox11;
    private javax.swing.JCheckBox jCheckBox12;
    private javax.swing.JCheckBox jCheckBox13;
    private javax.swing.JCheckBox jCheckBox14;
    private javax.swing.JCheckBox jCheckBox15;
    private javax.swing.JCheckBox jCheckBox16;
    private javax.swing.JCheckBox jCheckBox17;
    private javax.swing.JCheckBox jCheckBox18;
    private javax.swing.JCheckBox jCheckBox19;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox20;
    private javax.swing.JCheckBox jCheckBox21;
    private javax.swing.JCheckBox jCheckBox22;
    private javax.swing.JCheckBox jCheckBox23;
    private javax.swing.JCheckBox jCheckBox24;
    private javax.swing.JCheckBox jCheckBox25;
    private javax.swing.JCheckBox jCheckBox26;
    private javax.swing.JCheckBox jCheckBox27;
    private javax.swing.JCheckBox jCheckBox28;
    private javax.swing.JCheckBox jCheckBox29;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox10;
    private javax.swing.JComboBox jComboBox11;
    private javax.swing.JComboBox jComboBox13;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JComboBox jComboBox7;
    private javax.swing.JComboBox jComboBox8;
    private javax.swing.JComboBox jComboBox9;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JFileChooser jFileChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JProgressBar jProgressBar2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField27;
    private javax.swing.JTextField jTextField28;
    private javax.swing.JTextField jTextField29;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField30;
    private javax.swing.JTextField jTextField31;
    private javax.swing.JTextField jTextField32;
    private javax.swing.JTextField jTextField33;
    private javax.swing.JTextField jTextField34;
    private javax.swing.JTextField jTextField35;
    private javax.swing.JTextField jTextField36;
    private javax.swing.JTextField jTextField37;
    private javax.swing.JTextField jTextField38;
    private javax.swing.JTextField jTextField39;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField40;
    private javax.swing.JTextField jTextField41;
    private javax.swing.JTextField jTextField42;
    private javax.swing.JTextField jTextField43;
    private javax.swing.JTextField jTextField44;
    private javax.swing.JTextField jTextField45;
    private javax.swing.JTextField jTextField46;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane2;
    private javax.swing.JTextPane jTextPane3;
    // End of variables declaration//GEN-END:variables
    
}
