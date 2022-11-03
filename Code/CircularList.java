
/**
 * An implementation of a ADT List by a Circular List which holds Items: (key-info) in a circular array
 * (defined as an array in size maxLen, with a pointer to the start, and len which is the actual size of the array). <br>
 * Implement methods: retrieve(index), insert(index, key, val), delete(index)
 */


 public class CircularList{
    /**
     * will hold Items. The empty cells were point to null
     */
    private Item[] arr;
    /**
     * the size of the array - updated in insert and delete
     */
     private int maxLen;
    /**
     * the actual size of the array - how many items are there. Will be given to us.
     */
    private int len;
    /**
     * points to the index that is the 'start point reading' from the array. can be changed in insert
     * (in example if we insert an element as first the start will move back)
     */
    private int start;

    /**
     * Constructor of CircularList class. <br>
     * for every instance of the class building an array arr. <br>
     * fields: maxLen- array's length, arr - java's array, <br> len - num of items in the list,
     * start - index of the first item.
     * @param maxLen the length of the array - max number of items can be added to the list
     */
	public CircularList (int maxLen) {
		this.maxLen = maxLen;
		this.arr = new Item[maxLen];
		this.len = 0;
		this.start = 0;
	}


 /**
  * Get Item from the list which is in place of the given index.
  * @param i index of requested item in the list
  * @return
  * item in the ith position if it exists in the list.
  * otherwise, returns null
  */
  public Item retrieve(int i) {
      if (! validateRetrieveAndDelete(i)) {
          return null;
      }
      int pos = (this.start + i) % this.maxLen;
      return this.arr[pos];
  }

  /**
   * inserts an item to the ith position in list with key k and info s.
   * @param i index to be filled with the item inserted <br>
   * @param k the key of the item inserted <br>
   * @param s the value of the item inserted
   * @return
   * -1 if i smaller then 0 or greater the n  or n=maxLen, otherwise returns 0
   * @implNote
   * if i == 0 inserts as first by the mothod insertAsFirst(Item item) <br>
   * if i == len inserts as last by the method insertAsLast(Item item) <br>
   *  otherwise considers which elements to move by by calculating how many items will be moved -
   *  by the method isBetterToMoveBack(i)
   */
   public int insert(int i, int k, String s) {
       if (! validateInsert(i)) {
           return -1;
       }
       Item item = new Item(k, s);
       if (i == 0) {
           insertAsFirst(item);
       }
       else if (i == this.len) {
           insertAsLast(item);
       }
       else {
           if (isBetterToMoveBackInsert(i)) { // move back i element and change the start
               copyElementsInBackMoveInsert(i);
               updateElementInIndex(i, item); // do it before updating new start
               updateStartInMovingBack();
           } else { // no need to change start
               copyElementsInForwardMoveInsert(i);
               updateElementInIndex(i, item);
           }
       }
       this.len++;
       return 0;
   }


   /**
    * deletes an item in the ith posittion from the list.
    * @param i index of the item requested to be deleted
    * @return
    * returns -1 if ismaller then 0 or i greater then n-1, otherwise returns 0.
    * @implNote
    * if i == 0 delete the first by the method deleteFirst() <br>
    *  if i == len inserts as last be the method deleteLast() <br>
    *  otherwise considers which elements to move by calculating how many items will be moved -
    *  by the method deleteIsBetterToMoveBack(i)
   */
   public int delete(int i)
   {
       if (! validateRetrieveAndDelete(i)) {
           return -1;
       }
       if (i == 0) {
           deleteFirst();
       }
       else if (i == this.len - 1) {
           deleteLast();
       }
       else {
            if (deleteIsBetterToMoveBack(i)) {
                copyElementsInBackMoveDelete(i);
                this.arr[getPositionOfLastItem()] = null;
            }
            else { // better to move forward
                copyElementsInForwardMoveDelete(i);
                this.arr[this.start] = null;
                this.start = (this.start + 1) % this.maxLen;
            }
           this.len--;
       }
        if(this.len == 0){
            this.start = 0;
        }
	   return 0;
   }


// ----- helper methods -------

    /**
     * check if the index of the item requested to be inserted to the list is valid.
     * @param index index of requested item to be inserted to the list
     * @return
     * true if index valid, else false
     */
    private boolean validateInsert(int index) {
       int n = this.len;
       if (n == this.maxLen || index > n || index < 0) {
           return false;
       }
       return true;
    }

    /**
     * check if the index of the item requested to be retrieved or deleted is valid.
     * @param index index of requested item to be retrieved or deleted.
     * @return
     * true if index valid, else false
     */
    private boolean validateRetrieveAndDelete(int index) {
        int n = this.len;
        if (index > n-1 || index < 0) {
            return false;
        }
        return true;
    }

    /**
     * inserts an item with key and val to be first item in the list.
     * @param item item to be inserted as first in the list.
     */
    private void insertAsFirst(Item item) {
       if (this.len > 0) { // not adding the first element - change start. Else - do not change start
           updateStartInMovingBack();
       }
        arr[this.start] = item;
    }

    /**
     * inserts an item with key and val to be last item in the list.
     * @param item item to be inserted as last in the list.
     */
    private void insertAsLast(Item item) {
        updateElementInIndex(this.len, item);
    }

    /**
     * returns true if number of elements until (and include) the given index (i) is smaller than number of elements after (and include) i
     * if they are equal return false (better to move forward)
     * @param index index of the item requested to be inserted.
     * @return
     * true if its better to move the items before the index back for insertion complexity.
     * otherwise returns false
     */
    private boolean isBetterToMoveBackInsert(int index) {
        int countBeforeIndexAndInclude = getCountBeforeIndexAndInclude(index); // there are i elements which can move back
        int countAfterIndexAndInclude = getCountAfterIndexAndInclude(index);
        return countBeforeIndexAndInclude < countAfterIndexAndInclude;
    }

    /**
     * returns true if number of elements after the given index (i) is smaller than number of elements after (and include) i
     * if they are equal return false (better to move forward)
     * @param index index of the item requested to be deleted.
     * @return
     * true if its better to move the items after the index back for deletion complexity.
     * otherwise returns false
     */
    private boolean deleteIsBetterToMoveBack(int index) {
        int countBeforeIndex = getCountBeforeIndex(index);
        int countAfterIndex = getCountAfterIndex(index);
        return countBeforeIndex > countAfterIndex;
    }

    /**
     * help function for insert.
     * moves all items before the index and include one step back in the array arr.
     * @param index index of the item that its and his all previous items needs to move back one step.
     */
    private void copyElementsInBackMoveInsert(int index) {
        int currPos = this.start;
        int countBeforeIndexAndInclude = getCountBeforeIndexAndInclude(index);
        int newPos;
        while (countBeforeIndexAndInclude > 0) {
            newPos = currPos - 1;
            if (newPos == -1) {
                newPos = this.maxLen - 1;
            }
            arr[newPos] = arr[currPos];
            currPos = (currPos + 1) % this.maxLen;
            countBeforeIndexAndInclude--;
        }
    }

    /**
     * help function for insert.
     * moves all items after the index and include one step forward in the array arr.
     * @param index index of the item that its and his all forward items needs to move forward one step.
     */
    private void copyElementsInForwardMoveInsert(int index) {
        int countAfterIndexAndInclude = getCountAfterIndexAndInclude(index);
        int currPos = (getPosition(index) + countAfterIndexAndInclude - 1) % this.maxLen; // the currPos will be the last position in the block of the elements which will move forward
        int newPos;
        while (countAfterIndexAndInclude > 0) {
            newPos = (currPos + 1) % this.maxLen;
            arr[newPos] = arr[currPos];
            currPos--;
            if (currPos == -1) {
                currPos = this.maxLen - 1;
            }
            countAfterIndexAndInclude--;
        }

    }

    /**
     * help function for delete.
     * moves all items before the index not include, one step forward in the array arr.
     * @param index index of the item that its all previous items needs to move forward one step.
     */
    private void copyElementsInForwardMoveDelete(int index){
        int countBeforeIndex = getCountBeforeIndex(index);
        int currPos;
        int newPos = getPosition(index);
        while (countBeforeIndex > 0) {
            currPos = (newPos - 1);
            if (currPos == -1) {
                currPos = this.maxLen - 1;
            }
            arr[newPos] = arr[currPos];
            newPos  = newPos -1;
            if (newPos == -1) {
                newPos = this.maxLen - 1;
            }
            countBeforeIndex--;
        }
    }

    /**
     * help function for delete.
     * moves all items after the index not include, one step back in the array arr.
     * @param index index of the item that its all forward items needs to move back one step.
     */
    private void copyElementsInBackMoveDelete(int index){
        int countAfterIndex = getCountAfterIndex(index);
        int currPos =(getPosition(index) +1) % this.maxLen;
        int newPos;
        while (countAfterIndex > 0) {
            newPos = (currPos - 1);
            if (newPos == -1) {
                newPos = this.maxLen - 1;
            }
            arr[newPos] = arr[currPos];
            currPos = (currPos + 1) % this.maxLen;
            countAfterIndex--;
        }
    }

    /**
     * updates start field after moving items one step back in the array arr.
     */
    private void updateStartInMovingBack() {
       this.start = this.start == 0 ? this.maxLen - 1 : this.start - 1;
    }

    /**
     * returns the number of items before index-index in the list and include.
     * @param index index of the requested number of items before him and include.
     * @return
     * number of items before the index and include.
     */
    private int getCountBeforeIndexAndInclude(int index) {
       return index + 1;
    }

    /**
     * returns the number of items before index-index in the list not include.
     * @param index index of the requested number of items before him not include.
     * @return
     * number of items before the index not include.
     */
    private int getCountBeforeIndex(int index) {
        return index;
    }

    /**
     * returns the number of items after index-index in the list and include.
     * @param index index of the requested number of items after him and include.
     * @return
     * number of items after the index and include.
     */
    private int getCountAfterIndexAndInclude(int index) {
        int countBeforeIndexAndInclude = getCountBeforeIndexAndInclude(index);
        return this.len - countBeforeIndexAndInclude + 1; // plus 1 for including the item in the given index
    }

    /**
     * returns the number of items after index-index in the list not include.
     * @param index index of the requested number of items after him not include.
     * @return
     * number of items after the index not include.
     */
    private int getCountAfterIndex(int index) {
        int countBeforeIndex = getCountBeforeIndex(index);
        return this.len - countBeforeIndex -1;
    }

    /**
     * returns the position of the item in the array arr of the index- index of the list.
     * @param index index of the list of the requested position in the array arr.
     * @return
     * position of the item in the array arr of the index- index of the list.
     */
    private int getPosition(int index) {
       return (this.start + index) % this.maxLen;
    }

    /**
     * updates the list of index-index to fill the Item-item.
     * @param index index of the list to be updated
     * @param item- key and value to be filled in the list.
     *
     */
    private void updateElementInIndex(int index, Item item) {
        int pos = getPosition(index);
        this.arr[pos] = item;
    }

    /**
     * deletes the first item in the list
     */
    private void deleteFirst(){
       this.arr[start] = null;
      this.start = (this.start +1) % this.maxLen;
      this.len--;
    }
    /**
     * deletes the last item in the list
     */
    private void deleteLast(){
        this.arr[getPosition(this.len-1)] = null;
        this.len--;
    }

    /**
     * returns the index of last item in the list
     * @return
     * index of last item in the list
     */
    private int getPositionOfLastItem(){
       return getPosition(this.len-1);
    }

}
 
 
 
