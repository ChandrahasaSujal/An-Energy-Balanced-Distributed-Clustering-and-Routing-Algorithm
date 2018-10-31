/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package atarraya.event;

/**
 *
 * @author Kitty
 */
public class ClusterDistance
{
    public double distance;
    public int id;
    public double y;
    public double x;
    public boolean below_base_station;


    public ClusterDistance(int _id,double _distance,double _x,double _y,boolean below_base_station)
    {
        this.distance=_distance;
        this.id=_id;
        this.x=_x;
        this.y=_y;
        this.below_base_station=below_base_station;
    }


}
