import java.io.*;
import java.util.*;


class Analytics 
{

    private static double[] array;             //incoming clean array

    private int      count;
    private double   sum;
    private double   avg;
    private double   median;
    private double   min;
    private double   max;
    private double   stdDev; 
	private double   mode; 
	private double 	 range;  

//----------------------------------------------------------------------------------
// compute method becomes constructor
//----------------------------------------------------------------------------------
    Analytics(double[] dataArray)
    {
        array = dataArray;
    	array = this.normalize();		//return an array without the Double.NIM_VALUEs   

        count  = this.count();  
        sum    = this.sum(); 
        avg    = this.avg();
        median = this.median(); 
        min    = this.min();
        max    = this.max();
        stdDev = this.stdDev();
        range  = this.range();
        mode   = this.mode(); 
    }

//--------------------------------------------------------------------------------
// getter methods
//--------------------------------------------------------------------------------
    int      getCount()  { return count; } 
    double   getSum()    { return sum;   } 
    double   getAvg()    { return avg;   } 
    double   getMedian() { return median;} 
    double   getMin()    { return min;   } 
    double   getMax()    { return max;   }
    double   getStdDev() { return stdDev;} 
    double 	 getRange()	 { return range; }
    double 	 getMode()	 { return mode;  }
    
//----------------------------------------------------------------------------------
// normalize: Returns a new array that has all the Double.MIN_VALUEs removed
//            All bad/no data was previously replaced with Double.MIN_VALUE 
//----------------------------------------------------------------------------------
    double[] normalize()
    {
        int i     = 0;
        int nCols = 0;

        for (double col : array)                  //loop through all columns                  
            if (col != Double.MIN_VALUE)            //if not smallest double value (assumed bad/no data)
                nCols += 1;                         //add 1 to number of columns

        double[] outArray = new double[nCols];      //create a new array
        
        for (double col : array)                  //loop through incoming array                  
            if (col != Double.MIN_VALUE)            //if not bad data
                outArray[i++] = col;                //copy into new output array                        

        return outArray;                              
    }
    
//--------------------------------------------------------------------------------
// count: Count all elements of a 1 dim array
//--------------------------------------------------------------------------------
    int count()
    {
        int count = array.length;                   //count = array.length
        return(count);                              
    }
//--------------------------------------------------------------------------------
// sum: Sum all elements of an array
//--------------------------------------------------------------------------------
    static double sum()
    {
        double sum = 0;
        for (double col : array)                     //loop through all columns                  
            sum += col;                              //add to total
        return(sum);
    }
//--------------------------------------------------------------------------------
// avg: Average all elements of an array
//--------------------------------------------------------------------------------
    double avg()
    {
    	double avg = sum/count;                 	//perform the average
        return(avg);
    }
//--------------------------------------------------------------------------------
// median: Returns the median value on an array
//         if odd number of array elements, then return the median, 
//         if even number, then return the average of the 2 mid points 
//--------------------------------------------------------------------------------
    double median()
    {        
        double[] array2 = Arrays.copyOf(array, array.length);   //copy the array into array2                   
        Arrays.sort(array2);                                    //sort array2
        
        int mid1 = count/2;
        int mid2 = (count-1)/2;
        double median = (count%2 == 1)              //if count is odd
            ?  array2[mid1]                         //median= mid point                                                               
            : (array2[mid1] + array2[mid2]) / 2;    //median= average of 2 mid points
        return(median);                                                                           
    }
//--------------------------------------------------------------------------------
// min: Returns the minimum value within an array
//--------------------------------------------------------------------------------
    double min()
    {
        double minimum = Double.POSITIVE_INFINITY;   //start with largest possible value
        for (double col : array)                     //loop through all columns                  
            if (col < minimum) minimum = col;        //if col is less than minimum, save it in mimimum
        return(minimum);                              
    }
//--------------------------------------------------------------------------------
// max: Returns the maximum value within an array
//--------------------------------------------------------------------------------
    double max()
    {
        double maximum = Double.NEGATIVE_INFINITY;   //start with lowest possible value
        for (double col : array)                     //loop through all columns                  
            if (col > maximum) maximum = col;        //if col is more than maximum, save it in maximum
        return(maximum);                              
    }
//--------------------------------------------------------------------------------
// stdDev: Returns the standard deviation of an array
//         It is a measure of the amount of variation of a set of data values
//         Low stdDev means the values are close to the average (or are tight) 
//         1. take average of array
//         2. take the difference (delta) of each element to the average
//         3. take the square of that delta
//         4. add all those square of deltas
//         5. divide the square of deltas by count of elements
//         6. take the square root of item 5.  
//--------------------------------------------------------------------------------
    double stdDev()
    {
        int    count   = count();              		//call count method
        double sum     = sum();                		//call sum method
        double average = sum/count;

        double sqDelta = 0;                         //square of deltas      
        for (double col : array)                    //loop through all columns                  
            sqDelta += Math.pow(col-average,2);     //add to square of delta

        double std_dev = Math.sqrt(sqDelta/count);  //square root of average(square of deltas)
        return std_dev;                              
    }
//--------------------------------------------------------------------------------
// range: returns the max number minus min number of the array
//--------------------------------------------------------------------------------
    double range()
    {
		return(this.max - this.min);
	}
    
//--------------------------------------------------------------------------------
// mode: returns the number that occurs most frequently within the array
//--------------------------------------------------------------------------------    
    double mode()
    {
		double maxValue = 0, maxCount = 0;

		for (int i = 0; i < array.length; ++i) {
			int count = 0;
			for (int j = 0; j < array.length; ++j) {
				if (array[j] == array[i]) ++count;
			}
			if (count > maxCount) {
				maxCount = count;
				maxValue = array[i];
			}
		}
		return maxValue;
	}
    
//--------------------------------------------------------------------------------
// toString: Returns the array as well as all the analytic computations 
//--------------------------------------------------------------------------------
    public String toString()
    {
        //double[] array2 = normalize(array);					//return an array without the Double.NIM_VALUEs   

        String str  = "Data points: "   + Arrays.toString(array);
               str += "\nCount......: " + count(); 
               str += "\nSum........: " + sum(); 
               str += "\nAverage....: " + avg(); 
               str += "\nMedian.....: " + median(); 
               str += "\nMode.......: " + mode();
               str += "\nMinimum....: " + min(); 
               str += "\nMaximum....: " + max(); 
               str += "\nRange......: " + range();
               str += "\nStd.Dev....: " + stdDev(); 
        return str;                              
    }

//--------------------------------------------------------------------------------
// slice: Takes  a 2 dimensional array, slice type (row/col/all), and index
//        Return a single dimension array for that row or column or all cells
//--------------------------------------------------------------------------------
    static double[] slice(double[][] array2dim, String type, int idx)
    {
        int      size  = 0;
        double[] array = null;

        if (type.equals("row"))                             //ROW slice
        {
            size  = array2dim[idx].length;                      //determine the needed array size           
            array = Arrays.copyOf(array2dim[idx], size);        //copy that row into a 1dim array
        }
        if (type.equals("col"))                             //COL slice
        {           
            size  = array2dim.length;                           //determine the needed array size           
            array = new double[size];                           //create a new array of that size
            for (int i=0; i < size ; i++)                       //loop through all rows
                array[i] = array2dim[i][idx];                   //add cell into a 1dim array                     
        }
        if (type.equals("all"))                             //ALL slice (turn a 2dim array to 1 dim)
        {           
            for (double[] row : array2dim)                      //loop through all rows                     
                size += row.length;                             //compute the needed array size                              
            array = new double[size];                           //create a new array of that size
            int i = 0;
            for (double[] row : array2dim)                      //loop through all rows
                for (double col : row)                          //loop through all columns
                    array[i++] = col;                           //add cell into a 1dim array                         
        }

//      System.out.println(size + Arrays.toString(array));      //debug only
        return array;
    }
//--------------------------------------------------------------------------------
// transpose: Takes  a 2 dimensional array
//            Return a transposed 2 dimensional array
//--------------------------------------------------------------------------------
    static double[][] transpose(double[][] array2dim)
    {
        int rowNum  = array2dim.length;                     //compute number of rows
        int colNum  = 0;                                    //compute number of columns
            
        for (double[] row : array2dim)                      //loop through all rows                     
            if (row.length > colNum)                        //take the size of the longest row                              
                colNum = row.length;                        //this becomes the number of columns        
        
        double[][] newArray = new double[colNum][rowNum];   //create new array
                                                            //notice [row][col] dimensions are transposed
        int colT = 0;
        for (int row=0; row < array2dim.length; row++)              //loop thru original rows
        {
            int rowT = 0;                                           
            for (int col=0; col < array2dim[row].length; col++)     //loop thru original columns
            {
                newArray[rowT][colT] = array2dim[row][col];         //copy into new array           
                rowT++;                                             //add 1 to row of new array
            }
            colT++;                                                 //add 1 to col of new array
        }   
        return newArray;
    }
//--------------------------------------------------------------------------------   
}


public class AnalyticsUse
{
    static double[][] dataPoints = null;
	
	public static void main(String[ ] args) 
    {
        String input_file = "dataset1.csv";
//      String input_file = "/home/s/sultans/web/java/demo/8inpout/analytics/dataset1.csv";
    
        if (args.length > 0)                                    //if argument is provided 
            input_file = args[0];                               //use it as an input file

        String[][] dim2Array  = readFile(input_file);           //read the file into a 2 dim array
        double[][] dataPoints = convert(dim2Array);             //convert 2 dim String array to double
//      display(dataPoints);                                    //print the 2 dim array
        process(dataPoints);                                    //compute and print

        System.out.print("\nTRANSPOSED");
        double[][] trsposed = Analytics.transpose(dataPoints);  //transpose the 2 dim array 
        display(trsposed);                                      //print the transposed 2 dim array

        process(trsposed);                                      //compute and print
    }

//--------------------------------------------------------------------------------
// Read the input file, and store into a 2 dimensional String array
//--------------------------------------------------------------------------------
    static String[][] readFile(String filename)
    { 
        int nRows = 0;                                  //number of lines/rows in file
        int nCols = 0;                                  //number of columns for each line
        String[]   dim1Array = null;                    //1 dim array to hold data for each row
        String[][] dim2Array = null;                    //2 dim array to hold all the data points

        System.out.println("SOURCE INPUT...");
        try 
        {
            File    f1   = new File(filename);                                  //create a file object               
            Scanner file = new Scanner(f1);                                     //scanner for the input file

            LineNumberReader lnr = new LineNumberReader(new FileReader(f1));    //code to get the number of lines in file
            lnr.skip(Long.MAX_VALUE);                                           //skip to end of file
//          nRows = lnr.getLineNumber() +1;                                     //obtain the num of lines/rows      
            nRows = lnr.getLineNumber();                                        //for CSV there is an extra blank line      
            lnr.close();                                                        //close the file                                      

            dim2Array = new String[nRows][];                //create a 2 dim array with as many rows as lines in file 

            int row=0;
            int col=0;

            while (file.hasNextLine())                      //while there are lines in the file
            {
                String line = file.nextLine();              //get next line
                System.out.println(line);
                dim1Array       = line.split(",");          //split it on ,
                nCols           = dim1Array.length;         //get number of columns
                String[] cols   = new String[nCols];        //create a 1 dim array with as many cols as the line in file 
                dim2Array[row]  = cols;                     //append the 1 dim array into the 2 dim array

                for (col=0; col < nCols; col++)             //loop for all columns
                    dim2Array[row][col] = dim1Array[col];   //populate the 2 dim table              

                row++;                                      //next row
            }
        }            
        catch (Exception e)
        {
            System.out.println(e);
        }

        return(dim2Array);
    }
    

//--------------------------------------------------------------------------------
// Convert 2 dim String array into 2 dim double array
// If cell value has bad/no data, replace it with Double.MIN_VALUE
//--------------------------------------------------------------------------------
    static double[][] convert(String[][] data)     		//create a 2D array to hold all data points
    { 
        int nRows = 0;                                  //number of lines/rows in file
        int nCols = 0;                                  //number of columns for each line
        double[][] dataPoints = null;                   //2 dim array to hold all the data points

        nRows = data.length;                            //number of rows     
        dataPoints = new double[nRows][];               //create a 2 dim array with as many rows  

        int row=0;
        int col=0;

        for (row=0; row < nRows; row++)                 //for as many rows
        {
            nCols = data[row].length;                   //get number of columns
            double[] cols   = new double[nCols];        //create a 1 dim array with as many cols as the line in file 
            dataPoints[row] = cols;                     //append the 1 dim array into the 2 dim array

            for (col=0; col < nCols; col++)             //loop for all columns
            {
                try
                {
                    double num = Double.parseDouble(data[row][col]);    //convert from String to double 
                    dataPoints[row][col] = num;                         //populate the 2 dim table              
                } 
                catch(Exception e)                                      //if error in conversion
                {
//                  System.out.print  ("row:" + (row+1) + ", col:" + (col+1));                      //display error
//                  System.out.println(", value not numeric: " + data[col] + ", assuming null");    //display error

                    dataPoints[row][col] = Double.MIN_VALUE;            //assume bad/no data - replace with smallest double value 
                }   
            }
        }            
        return(dataPoints);
    }
//--------------------------------------------------------------------------------
// Display the 2 dimensional data array
//--------------------------------------------------------------------------------
    static void display(double[][] dataPoints)
    {
        System.out.println("\nCOLUMN ORIENTED...");

        for (double[] row : dataPoints)                  //loop through all rows of 2 dim array
        {
            for (double col : row)                       //loop through all the columns
                if (col == Double.MIN_VALUE)             //if smallest double value (assume bad/no data)
                    System.out.print("\t");              //skip
                else
                    System.out.printf("%7.2f ", col);    //print the column value

            System.out.print("\n");
        }
    }
                
//--------------------------------------------------------------------------------
// Process
//--------------------------------------------------------------------------------
    static void process(double[][] dataPoints)
    {
        System.out.println("\nCOMPUTATION...");
        processRows(dataPoints);                        //process all rows                            

        System.out.println("\t --------------------------------------------------------------------------------");
        processColumns(dataPoints);                     //process all columns                         

        System.out.println("\n\n--------OVERALL-------------");
        processEntireSet(dataPoints);                   //process entire dataset                           

        System.out.println("\n------COLUMN 1 AGAIN----------");
        processColumn1(dataPoints);                     //process column 1 again                        
    }

//--------------------------------------------------------------------------------
// Compute and print analytics for all rows
//--------------------------------------------------------------------------------
    static void processRows(double[][] dataPoints)
    {
        for (double[] row : dataPoints)                 //loop through every rows                 
        {           
            System.out.print("\t");

            for (double col : row)                      //loop through all the columns
            {
                if (col == Double.MIN_VALUE)            //if smallest double value (assume bad/no data)
                    System.out.print("\t");             //bypass
                else
                    System.out.printf("%7.2f ", col);   //print the column value
            }
            
            // Row Analytics: instantiate Analytics object and call getter methods
            Analytics rowAnalytics = new Analytics(row);  
           
            System.out.print (" | Count="    + rowAnalytics.getCount() );      //print row count
            System.out.printf("\tSum=%.2f"   , rowAnalytics.getSum() );        //print row sum   
            System.out.printf("\tAvg=%.2f"   , rowAnalytics.getAvg() );        //print row average
            System.out.printf("\tMedian=%.2f", rowAnalytics.getMedian() );     //print row median
            System.out.printf("\tMode=%.2f"  , rowAnalytics.getMode() );       //print row mode
            System.out.printf("\tMin=%.2f"   , rowAnalytics.getMin() );        //print row minimum value
            System.out.printf("\tMax=%.2f"   , rowAnalytics.getMax() );        //print row maximum value
            System.out.printf("\tRange=%.2f" , rowAnalytics.getRange() );      //print row range
            System.out.printf("\tstdDev=%.2f", rowAnalytics.getStdDev() );     //print row standard deviation
            System.out.println();
        }
    }       

//-----------------------------------------------------------------------------------------
// Compute and print analytics for all columns
// I have to iterate through all columns for each analytics computation to print properly
//-----------------------------------------------------------------------------------------
    static void processColumns(double[][] dataPoints)
    {
        int maxCols = 0;                        //determine the maximum number of cols for entire dataset
        for (double[] row : dataPoints)         //loop through all rows                 
            if (row.length > maxCols)           //if number of columns for the row > max
                maxCols = row.length;           //take it

        System.out.print("COUNT..:");            
        for (int col=0; col < maxCols; col++)                           	//loop through all columns                  
        {           
            double[] colArray = Analytics.slice(dataPoints,"col",col);  	//slice the array vertically
            Analytics colAnalytics = new Analytics (colArray);         
            System.out.print("   " + (int) colAnalytics.getCount() + "\t"); //print column count as an int
        }
        System.out.print("\nSUM....:");            
        for (int col=0; col < maxCols; col++)                           	//loop through all columns                  
        {           
            double[] colArray = Analytics.slice(dataPoints,"col",col);  	//slice the array vertically
            Analytics colAnalytics = new Analytics (colArray);          
            System.out.printf("%7.2f ", colAnalytics.getSum());           	//print column sum
        }
        System.out.print("\nAVERAGE:");            
        for (int col=0; col < maxCols; col++)                           	//loop through all columns                  
        {           
            double[] colArray = Analytics.slice(dataPoints,"col",col);  	//slice the array vertically
            Analytics colAnalytics = new Analytics (colArray);         
            System.out.printf("%7.2f ", colAnalytics.getAvg());             //print column average
        }
        System.out.print("\nMEDIAN.:");            
        for (int col=0; col < maxCols; col++)                           	//loop through all columns                  
        {           
            double[] colArray = Analytics.slice(dataPoints,"col",col);      //slice the array vertically
            Analytics colAnalytics = new Analytics (colArray);          
            System.out.printf("%7.2f ", colAnalytics.getMedian());        	//print column median value
        }
        System.out.print("\nMODE:");            
        for (int col=0; col < maxCols; col++)                           	//loop through all columns                  
        {           
            double[] colArray = Analytics.slice(dataPoints,"col",col);  	//slice the array vertically
            Analytics colAnalytics = new Analytics (colArray);          
            System.out.printf("%7.2f ", colAnalytics.getMode());        	//print column standard deviation
        } 
        System.out.print("\nMIN....:");            
        for (int col=0; col < maxCols; col++)                           	//loop through all columns                  
        {           
            double[] colArray = Analytics.slice(dataPoints,"col",col);  	//slice the array vertically
            Analytics colAnalytics = new Analytics (colArray);         
            System.out.printf("%7.2f ", colAnalytics.getMin());       		//print column minimum value
        }
        System.out.print("\nMAX....:");            
        for (int col=0; col < maxCols; col++)                           	//loop through all columns                  
        {           
            double[] colArray = Analytics.slice(dataPoints,"col",col);  	//slice the array vertically
            Analytics colAnalytics = new Analytics (colArray);         
            System.out.printf("%7.2f ", colAnalytics.getMax());             //print column maximum value
        }
        System.out.print("\nRANGE:");            
        for (int col=0; col < maxCols; col++)                           	//loop through all columns                  
        {           
            double[] colArray = Analytics.slice(dataPoints,"col",col);  	//slice the array vertically
            Analytics colAnalytics = new Analytics (colArray);          
            System.out.printf("%7.2f ", colAnalytics.getRange());       	//print column standard deviation
        } 
        System.out.print("\nSTD-DEV:");            
        for (int col=0; col < maxCols; col++)                           	//loop through all columns                  
        {           
            double[] colArray = Analytics.slice(dataPoints,"col",col);  	//slice the array vertically
            Analytics colAnalytics = new Analytics (colArray);          
            System.out.printf("%7.2f ", colAnalytics.getStdDev());          //print column standard deviation
        }  

    }       

//--------------------------------------------------------------------------------
// Compute and print analytics for entire dataset
//--------------------------------------------------------------------------------
    static void processEntireSet(double[][] dataPoints)
    {
        double[] dataset  = Analytics.slice(dataPoints,"all",0);        	//convert entire dataset to 1dim array
        Analytics allAnalytics = new Analytics(dataset);        			// create new object 'allAnalytics'
        System.out.println(allAnalytics.toString());                		//call the static toString() method
    } 

//--------------------------------------------------------------------------------
// Compute and print analytics for column 1 again
//--------------------------------------------------------------------------------
    static void processColumn1(double[][] dataPoints)
    {
        double[] colArray = Analytics.slice(dataPoints,"col",0);        	//slice the 1st column
        Analytics COLAnalytics = new Analytics(colArray);         			// create new object 'COLAnalytics'
        System.out.println(COLAnalytics.toString());               			//call the static toString() method 
    } 
    
}