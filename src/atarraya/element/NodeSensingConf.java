/*
 * NodeSensingConf.java
 *
 * Created on November 15, 2007, 3:51 PM
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

package atarraya.element;

import java.util.Vector;
import atarraya.constants;


/**
 *
 * @author pedrow
 */
public class NodeSensingConf {
    
    int sensor_type;                         //Type of event that is being monitored (temp, light, etc.)
    int sens_diameter;                      //Diameter of the sensing unit disk
    Vector readings;                          //Data structure to save readings
    
    /** Creates a new instance of NodeSensingConf */
    public NodeSensingConf(int sensor_type_, int sens_diameter_) {
        
        sensor_type = sensor_type_;               
        readings = new Vector();
        sens_diameter = sens_diameter_;
    }
    
}
