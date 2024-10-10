import java.lang.reflect.Array;
import java.util.NoSuchElementException;
import util.AssociativeArray;

/**
 * Represents the mappings for a single category of items that should
 * be displayed
 * 
 * @author Catie Baker & Jacob Bell
 *
 */

public class AACCategory implements AACPage {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /** The name of the category */
  String category;

  /** The location and the name of item */
  AssociativeArray<String, String> itemsArr;


	/**
	 * Creates a new empty category with the given name
	 * @param name the name of the category
	 */
	public AACCategory(String name) {
    this.category = name;
    this.itemsArr = new AssociativeArray<String, String>();
	} // AACCategory
	
	/**
	 * Adds the image location, text pairing to the category
	 * @param imageLoc the location of the image
	 * @param text the text that image should speak
	 */
	public void addItem(String imageLoc, String text) {
    try {
      itemsArr.set(imageLoc, text);
    } catch (Exception e) {
      System.err.print("Error setting location/text");
    } // try/catch
	} // addItem(String, String)

	/**
	 * Returns an array of all the images in the category
	 * @return the array of image locations; if there are no images,
	 * it should return an empty array
	 */
	public String[] getImageLocs() {
		return this.itemsArr.getAllKeys();
	} // getImageLocs()

	/**
	 * Returns the name of the category
	 * @return the name of the category
	 */
	public String getCategory() {
		return this.category;
	} // getCategory()

	/**
	 * Returns the text associated with the given image in this category
	 * @param imageLoc the location of the image
	 * @return the text associated with the image
	 * @throws NoSuchElementException if the image provided is not in the current
	 * 		   category
	 */
	public String select(String imageLoc) throws NoSuchElementException {
    try {
      return this.itemsArr.get(imageLoc);
    } catch (Exception e) {
      throw new NoSuchElementException();
    } // try/catch
	} // select (String)

	/**
	 * Determines if the provided images is stored in the category
	 * @param imageLoc the location of the category
	 * @return true if it is in the category, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		return this.itemsArr.hasKey(imageLoc);
	} // hasImage
} // hasImage(String)
