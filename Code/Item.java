
/**
 * Stores (key-info). Will be used by AVLNode or by element in CircularList
 */
public class Item{
	/**
	 * the key of the node
	 */
	private int key;
	/**
	 * the info of the node
	 */
	private String info;

	/**
	 * default constructor
	 */
	public Item() { }

	/**
	 * constructor by key-info
	 * @param key the key
	 * @param info the info
	 */
	public Item (int key, String info){
		this.key = key;
		this.info = info;
	}

	/**
	 * @return
	 * key value
	 */
	public int getKey(){
		return key;
	}

	/**
	 * @return
	 * info value
	 */
	public String getInfo(){
		return info;
	}

}