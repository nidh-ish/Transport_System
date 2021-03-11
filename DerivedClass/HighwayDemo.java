package demo19059;

import java.util.*;
import base.Highway;
import base.Truck;

class HighwayDemo extends Highway {

	public HighwayDemo() {
        trucks = new ArrayList<Truck> ();
	}

	@Override
	public boolean hasCapacity() {                        // This returns true if the highway is not full or the number of trucks is below capacity    
		if(this.trucks.size() < this.getCapacity()) {     // Condition to check if number of trucks is less than the capacity available
            return true;                                  // This return true if we can add more trucks
        }
		return false;                                     // returns false if the condition is not satisfied
	}

	@Override
	public synchronized boolean add(Truck truck) {        
		if(this.trucks.size() < this.getCapacity()) {    // If it is possible to add trucks then we add it to the list
            this.trucks.add(truck);
            return true;
        }
		return false;                                    // Else we return false 
	}

	@Override
	public synchronized void remove(Truck truck) {
        if(this.trucks.size() > 0) {                  
            this.trucks.remove(truck);                   // We remove the truck from the list
        }
	}

    private ArrayList<Truck> trucks;                     // This is a list of Truck

}
