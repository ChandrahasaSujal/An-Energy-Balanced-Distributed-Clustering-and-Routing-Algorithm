/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package atarraya.element;

/**
 *
 * @author Research
 */
public interface EnergyModel {

    public int getEnergyState();

    public void setEnergyState(int energy_state);

    public double calcEnergy(int oper, double time);

    public double calcEnergyStateConsumption(int energy_state);

    public double calcEnergyStateConsumption();
    
}
