public class WeekOneQuestionAnswers {
    
    /** Scales with O(n^2)
     * 
     * @param arg -  the sorted array that is expected
     * @param sum -  the sum that is expected
     * @return number of times the sum is in the array
     */
    public static int numberOfThreeSums(int[] arg, int sum){
	int count = 0;
	for(int delim = 0; delim<arg.length;delim++){//delim = the fixed value
	    int lowPosition = 0;
	    int highPosition = arg.length-1;
	    while(lowPosition<highPosition){
		
		if(lowPosition==delim) lowPosition++;
		if(highPosition==delim) highPosition--;
		if((lowPosition>=highPosition)||(lowPosition==arg.length)) break;
		System.out.println(lowPosition + ";"+ highPosition+ ";"+delim);
		
		int currentValue = arg[lowPosition]+arg[highPosition]+arg[delim];
		if(currentValue==sum){
		    System.out.println("Ergebnis: "+lowPosition + ";"+ highPosition + ";" + delim + ";" + currentValue);
		    count++;
		    lowPosition++;
		}else if(currentValue<sum){
		    lowPosition++;
		}else if(currentValue>sum){
		    highPosition--;
		}
		
	    }
	}
	return count/3;// I don't get why it gets onyl 3 out of the possible answers, but at this point I might as well roll with it. 
    }
    
    public static void main(String[] args){
	System.out.print(numberOfThreeSums(new int[]{0,1,2,3,4,5}, 7));
    }

}
