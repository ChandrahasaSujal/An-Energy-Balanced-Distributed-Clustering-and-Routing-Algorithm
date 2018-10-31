/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package atarraya.event;

/**
 *
 * @author Kitty
 */
public class NeibourRoutingTable {

    public int node_id;
    public double x;
    public double y;
    public int region;
    public double remaining_energy;
    public double distance;

    public NeibourRoutingTable(int node_id,int region,double x,double y,double remaining_energy,double distance)
    {
        this.node_id=node_id;
        this.region=region;
        this.x=x;
        this.y=y;
        this.remaining_energy=remaining_energy;
        this.distance=distance;

    }

}
