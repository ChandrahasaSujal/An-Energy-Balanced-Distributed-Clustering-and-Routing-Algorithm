/*
 * timestamp.java
 *
 * Created on 14 de abril de 2005, 19:34
 *
 * This file is part of Atarraya.

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
 *
 */

package atarraya.event;

import java.util.*;
import java.lang.Comparable;
import java.io.Serializable;

/**
 *
 * @author Administrador
 */
public class timestamp implements Comparable, Serializable{
    
    /**Fecha que contiene la estampa de tiempo*/
    private Calendar fecha;
    public static final int HOUR_OF_DAY = 11;
    public static final int MINUTE = 12;
    public static final int SECOND = 13;
    public static final int DAY_OF_MONTH = 5;
    public static final int MONTH = 2;
    public static final int YEAR = 1;
    
    public static final long serialVersionUID=1234526978;
    
    /**
     * Este método es el constructor del objeto timestamp. Este objeto tiene un atributo tipo
     * Calendar que es iniciado para que tenga la fecha y hora actuales.
     * @param   void
     * @return  void
     *          
     *
     * @since APCUN 0.3
     */
    public timestamp() {
        this.fecha = Calendar.getInstance();
    }

        
    /**
     * Este método retorna la fecha almacenada en un tipo de dato Calendar.
     * Calendar que es iniciado para que tenga la fecha y hora actuales.
     * @param   void
     * @return  void
     *          
     *
     * @since APCUN 0.3
     */
    
    public Calendar getTimestamp(){
    
        return this.fecha;
    }
    
    /**
     * Este método compara dos objetos timestamp y arroja el resultado de su comparación.
     * 
     * @param   obj - Object que contiene el timestamp
     * @return  1 - si el actual es mayor
     *          0 - si son iguales
     *          -1 - si el actual es menor
     *          -2 - si no son de la misma clase
     *          
     *
     * @since APCUN 0.3
     */
    
    public int compareTo(Object obj){
    
        int sw=-2;
        
        if (obj instanceof timestamp){

            sw=0;
            int[] constantes = new int[6];
            constantes[0]=1;
            constantes[1]=2;
            constantes[2]=5;
            constantes[3]=11;
            constantes[4]=12;
            constantes[5]=13;
            int i=0;
            timestamp fe = (timestamp)obj;
            
            while(sw==0 && i<6){
                if(fecha.get(constantes[i])<fe.fecha.get(constantes[i])){
                    return -1;
                }else{
                    if(fecha.get(constantes[i])>fe.fecha.get(constantes[i])){
                        return 1;
                    }
                }     
            }
        }  
        return sw;
    }
    
    /**
     * Este método retorna un String con la fecha y hora de la marca de tiempo. El formato con el que
     * devuelve la marca de tiempo es el siguiente:
     *
     * dd/mm/yyyy hh:mm:ss
     *
     * @param   void
     * @return  String que tiene la marca de tiempo
     *
     * @since APCUN 0.3
     */
    public String toString(){
    
        String tiempo="";
        tiempo=fecha.get(DAY_OF_MONTH)+"/"
                +fecha.get(MONTH)+"/"
                +fecha.get(YEAR)+" "
                +fecha.get(HOUR_OF_DAY)+":"
                +fecha.get(MINUTE)+":"
                +fecha.get(SECOND);
        
        return tiempo;
    }
    
    public String toString2(){
    
        String tiempo="";
        tiempo=fecha.get(DAY_OF_MONTH)+"_"
                +fecha.get(MONTH)+"_"
                +fecha.get(YEAR)+"@"
                +fecha.get(HOUR_OF_DAY)+"_"
                +fecha.get(MINUTE)+"_"
                +fecha.get(SECOND);
        
        return tiempo;
    }

    /**
     * Este método compara un objeto y define si es igual o no al objeto actual.
     *
     * @param   obj Object que tiene el objeto a comparar.
     * @return  true si son el mismo objeto, o tiene el mismo nombre.
     *          false en otro caso
     *
     * @since APCUN 0.3
     */
    public boolean equals(Object obj){

        if(obj instanceof timestamp){
            timestamp tiempo = (timestamp)obj;
            if(tiempo==this){
                return true;
            }else{
                if(tiempo.fecha.equals(this.fecha)){
                    return true;
                }
            }
        }
        return false;

    }    
}
