import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;
import util.AssociativeArray;

/**
 * Creates a set of mappings of an AAC that has two levels,
 * one for categories and then within each category, it has
 * images that have associated text to be spoken. This class
 * provides the methods for interacting with the categories
 * and updating the set of images that would be shown and handling
 * an interactions.
 * 
 * @author Catie Baker & Jake Bell
 *
 */
public class AACMappings implements AACPage {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The array that will hold all of the mapped(key:pair) categories.
   */
  AssociativeArray<String, AACCategory> mappedCategories;

  /** This is the home screen that lists the different categories */
  AACCategory homeScreen;

  /** This is to hold the current screen the user is on */
	AACCategory curScreen;
  
  // +-------------+------------------------------------------------------
  // | Constructor |
  // +-------------+
	/**
	 * Creates a set of mappings for the AAC based on the provided
	 * file. The file is read in to create categories and fill each
	 * of the categories with initial items. The file is formatted as
	 * the text location of the category followed by the text name of the
	 * category and then one line per item in the category that starts with
	 * > and then has the file name and text of that image
	 * 
	 * for instance:
	 * img/food/plate.png food
	 * >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon
	 * img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing
	 * and food has french fries and watermelon and clothing has a 
	 * collared shirt
	 * @param filename the name of the file that stores the mapping information
	 */
	public AACMappings(String filename) {
    // Initialize the categories.
    this.mappedCategories = new AssociativeArray<String, AACCategory>();
    this.homeScreen = new AACCategory("");
    this.curScreen = this.homeScreen;
    
    try {
      Scanner eyes = new Scanner(new File(filename));
      while (eyes.hasNextLine()) {
        String message = eyes.nextLine();
        //Split the string only once
        String[] lineSplit = message.split(" ", 2);

        if (lineSplit[0].substring(0,1).equals(">")) {
          this.curScreen.addItem(lineSplit[0].substring(1), lineSplit[1]);
        } else {
          this.homeScreen.addItem(lineSplit[0], lineSplit[1]);
          try {
            this.mappedCategories.set(lineSplit[0], new AACCategory(lineSplit[1]));
            this.curScreen = this.mappedCategories.get(lineSplit[0]);
          } catch (Exception e) {
            System.err.print("Unsucessful get and set");
          } // try/catch
        } // else
      } // while
      this.curScreen = this.homeScreen;
      eyes.close();
    } catch (Exception e) {
      // do nothing?
    } // try/catch
	} // AACMappings(String)
	
	/**
	 * Given the image location selected, it determines the action to be
	 * taken. This can be updating the information that should be displayed
	 * or returning text to be spoken. If the image provided is a category, 
	 * it updates the AAC's current category to be the category associated 
	 * with that image and returns the empty string. If the AAC is currently
	 * in a category and the image provided is in that category, it returns
	 * the text to be spoken.
	 * @param imageLoc the location where the image is stored
	 * @return if there is text to be spoken, it returns that information, otherwise
	 * it returns the empty string
	 * @throws NoSuchElementException if the image provided is not in the current 
	 * category
	 */
	public String select(String imageLoc) {
    try {
      if (this.mappedCategories.hasKey(imageLoc)) {
        // ImageLoc is a screen
        this.curScreen = this.mappedCategories.get(imageLoc);
        return "";
      } else if (this.curScreen.hasImage(imageLoc)) {
        // ImageLoc is an item
        return this.curScreen.select(imageLoc);
      }
    } catch (Exception e) {
      System.err.print("Error getting imageloc");
      return "";
    } // try/catch
		return "";
	} // select(imageLoc)
	
	/**
	 * Provides an array of all the images in the current category
	 * @return the array of images in the current category; if there are no images,
	 * it should return an empty array
	 */
	public String[] getImageLocs() {
    try {
      return this.curScreen.getImageLocs();
    } catch (Exception e) {
      return new String[0];
    } // try/catch
	} // getImageLocs()
	
	/**
	 * Resets the current category of the AAC back to the default
	 * category
	 */
	public void reset() {
    this.curScreen = this.homeScreen;
	} // reset()
	
	
	/**
	 * Writes the ACC mappings stored to a file. The file is formatted as
	 * the text location of the category followed by the text name of the
	 * category and then one line per item in the category that starts with
	 * > and then has the file name and text of that image
	 * 
	 * for instance:
	 * img/food/plate.png food
	 * >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon
	 * img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing
	 * and food has french fries and watermelon and clothing has a 
	 * collared shirt
	 * 
	 * @param filename the name of the file to write the
	 * AAC mapping to
	 */
	public void writeToFile(String filename) {
    try {
      FileWriter filePen = new FileWriter(filename);
      PrintWriter pen = new PrintWriter(filePen);
      String[] keys = this.mappedCategories.getAllKeys();
  
      // for each category.
      for (int i = 0; i < this.mappedCategories.size(); i++) {
        try {
          this.curScreen = this.mappedCategories.get(keys[i]);
          pen.println(keys[i] + this.getCategory());
          String[] imageLocs = this.curScreen.getImageLocs();
          // for each image in each category.
          for (String img : imageLocs) {
            pen.println(">" + img + " " + this.curScreen.select(img));
          }
        } catch (Exception e) {
          System.err.println("There is an error?");
        } // try/catch
      }
      pen.close();
    } catch (Exception e) {
    } // try/catch
	} // writeToFile(String)
	
	/**
	 * Adds the mapping to the current category (or the default category if
	 * that is the current category)
	 * @param imageLoc the location of the image
	 * @param text the text associated with the image
	 */
	public void addItem(String imageLoc, String text) {
		this.curScreen.addItem(imageLoc, text);
	} // additem(String, String)


	/**
	 * Gets the name of the current category
	 * @return returns the current category or the empty string if 
	 * on the default category
	 */
	public String getCategory() {
		return this.curScreen.getCategory();
	} // getCategory()


	/**
	 * Determines if the provided image is in the set of images that
	 * can be displayed and false otherwise
	 * @param imageLoc the location of the category
	 * @return true if it is in the set of images that
	 * can be displayed, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		return this.mappedCategories.hasKey(imageLoc);
	} // hasImage(String)
} // class AACMappings
