/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package atarraya.event;

/**
 *
 * @author Kitty
 */
public class Receiver
{
    public int NODE_ID;
    public int GATEWAY_ID;

    public Receiver(int node_id,int gate_id)
    {
        this.NODE_ID=node_id;
        this.GATEWAY_ID=gate_id;
    }

}
