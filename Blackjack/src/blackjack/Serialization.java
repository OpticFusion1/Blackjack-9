package blackjack;

import java.io.*;

/**
 * Class: Serialization.java
 * Description: Class used to write and read serializable objects to 
 *  and from a file.
 * Author: Paulo Jorge.
 */
public class Serialization {
    /**
     * Method to write and save a serializable object to a file.
     * @param object Serializable object to write to file.
     * @param fileName String file name of file to write to.
     */
    public static void writeToFile(Serializable object, 
            String fileName) {
        try {
            // Create new ouput stream isntances
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream objectOut 
                    = new ObjectOutputStream(fileOut);
            
            // Write object to file
            objectOut.writeObject(object);
            
            // Close streams
            objectOut.close();
            fileOut.close();
            System.out.println("Serialization successful. Data saved "
                    + "to /" + fileName + ".");
        } catch(IOException exception) {
            exception.printStackTrace();
        }
    }
    
    /**
     * Method to read and return a object from the argument file.
     * @param fileName String file name of file to find.
     * @return Object read and found using the file name passed in or
     *  null if object was unable to load.
     */
    public static Object readFromFile(String fileName) {  
        try {
            // Create new input stream isntances
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            
            // Read object from file
            Object temp = objectIn.readObject();
            
            // Close streams
            objectIn.close();
            fileIn.close();           
            
            System.out.println("Deserialization successful. Data "
                    + "loaded from /" + fileName + ".");
            
            // Return object found
            return temp;
        } catch(IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        
        // Return null if expception occured
        return null;
    }
}