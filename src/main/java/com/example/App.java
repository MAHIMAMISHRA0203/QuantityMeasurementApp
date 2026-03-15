  
class Feet{
	 public double getFeet() {
		return value;
	}
	 public Feet(double value) {
		this.value = value;
	}

	 private final  double value;
	 @Override
	 public boolean equals(Object object) {
		 if(this==object)
			 return true;
		 if(this==null)
			 return false;
		 if(this.getClass()!=object.getClass())
                return false;
		 Feet otherFeet=(Feet)object;
		 return Double.compare(this.value, otherFeet.value)==0;
		 
	 }
	
	
}
public class App 
{
    public static void main( String[] args )
    {
    	Feet feet1=new Feet(1.0);
    	Feet feet2=new Feet(3.0);
    	if(feet1.equals(feet2))
    		System.out.println("equal");
    	else {
			System.out.println("not equal");
		}
    		
    	
    }
}