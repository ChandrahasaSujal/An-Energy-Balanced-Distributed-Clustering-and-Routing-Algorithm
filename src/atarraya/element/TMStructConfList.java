/*
 * TMStructConfList class
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


/**
 *
 * @author pedro
 */
public class TMStructConfList {

   Vector<TMConfStructure> the_list;
    
    public TMStructConfList(){
        the_list = new Vector<TMConfStructure>();
    }
    
    public void addTmConfStructure(int index){
    
        if(!isValidIndex(index))
            the_list.add(new TMConfStructure(index));
        
    }
    
    public void clearTMConfStructList(){
        the_list.clear();
    }
    
    public void removeTmConfStructure(int index){
    
        int i=0, tam=the_list.size();
       
       while(i<tam){
            if(the_list.get(i).getIndex() == index){
                the_list.remove(i);
                break;
            }else
                i++;
       }      
        
    }
    
    
    public int getSize(){
        return the_list.size();
    }
    
   public TMConfStructure getTmConfStructure(int index){
   
       int i=0, tam=the_list.size();
       
       while(i<tam){
            if(the_list.get(i).getIndex() == index)
                return the_list.get(i);
            else
                i++;
       }      
       
       return null;
   }
    
   public TMConfStructure getTmConfStructureListIndex(int index){
   
       int tam=the_list.size();
       
       if(index<tam){
           return the_list.get(index);
       }
       
       return null;
   }
   
   public boolean isValidIndex(int index){
   
       int i=0, tam=the_list.size();
       
       while(i<tam){
            if(the_list.get(i).getIndex() == index)
                return true;
            else
                i++;
       }      
       
       return false;
       
   }
   
}
