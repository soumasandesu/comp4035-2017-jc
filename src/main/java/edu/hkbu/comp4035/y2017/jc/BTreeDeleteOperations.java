package edu.hkbu.comp4035.y2017.jc;

import java.util.Collection;

class BTreeDeleteOperations {
    /*

    This method deletes an entry (key, rid) from a leaf node. Deletion from a leaf node may cause one or more entries in
    the index node to be deleted. You should always check if a node underflows (less than 50% full) after deletion. If a
    node becomes underflows, merging or redistribution will occur (read and implement the algorithm in the notes).

    You should consider different scenarios separately (maybe write separate functions for them). You should consider
    deletion from a leaf node and index node separately. Deletion from the root should also be considered separately.

    The following code fragment may be helpful:

    Checking if a node is half full:

    if (node->AvailableEntries() < (Fanout-1)/2)
    {
        // Try to re-distribute, borrowing from sibling
        // (adjacent node with same parent as this node).
        // If re-distribution fails, merge this node and sibling.
    }

     */

    // THROW WHENEVER ERROR ENCOUNTERS!!! null IS NOT FOR ERROR THINGS!

    // Jacky, you may change all those names and signatures of all following methods.
    // I'm just plotting the determination of this class for your reference, according to the textbook. -- Charles
	static void doDelete(BTree bTree, int key,int key2) {
		Collection<Integer> result = BTreeSearchOperations.doRangeSearchInclusive(bTree,key, key2);
		for(int k:result){
			delete(bTree.getRootNode(),k);
		}
	}
	
    static void doDelete(BTree bTree, int key) {
        // search position
        // get parent-1
        // check size if hungry
        //     check right won't hungry
        //         borrow one from right
        //         write new index @ parent of borrowed
        //     else
        //         merge
        //     while parent is not null
        //         check upper won't hungry
    
    	delete(bTree.getRootNode(),key);
    	
    }

    private static <V> void delete(BTreeNode x, int k) {
        int i = x.n() - 1; //key size - -1
        while (i > -1 && k < x.getKeyAt(i)) {
            i--;
        }
       
        if (x.isLeaf()) {
        	System.out.println("in leaf and delete ");
        	
        
        	if(x.getKeyAt(i)==k){ 
        		//find and try to delete       	
	        	x.removeSubItemValueAt(i);
	        	x.removeKeyAt(i);
        	}else{
        		//not find
        		//return null
        		System.out.println("not find");
        	}
        	
        	
        } else {
        	//not the leaf , find in deeper level
        	System.out.println("not a leaf , go to deep....");
        	
        	//check the index key and the delete key
        	
        	if(i>-1 && x.getKeyAt(i)==k){
               System.out.println("index key same as delete key");
               if(((BTreeNode)x.getSubItemAt(i+1)).getKeys().size()>1){
            	   System.out.println("reset key");
	                x.removeKeyAt(i);
	                x.addKeyAt(i,  ((BTreeNode)x.getSubItemAt(i+1)).getKeyAt(1));
               }
               
            }
        	
            i++;
            BTreeNode xc = (BTreeNode) x.getSubItemAt(i);
            delete(xc, k);
            checkHungryOrMerge((BTreeIndexNode)x,i);
            if(x.getTree().getRootNode()==x && x.keysSize()==0){
            	System.out.println("root is empty");
            	x.getTree().setRootNode((BTreeNode)x.getSubItemAt(0));
            }
            
           
        }
        //delete may be success
        //checkHungryOrMerge!
           
    }
    

    //Non-leaf Re-distribution
    
    //Leaf Re-distribution
    private static void checkHungryOrMerge(BTreeIndexNode indexNode, int pos) {
        if(indexNode.getSubItemAt(pos).isSubItemsHungry()){
    		System.out.println("hungry!");
	    	if(indexNode.getSubItemAt(pos).isLeaf())
	    		LeafReDistribution(indexNode,pos);
	   		else
	   			NonLeafReDistribution(indexNode,pos);
    	}else{
    		System.out.println("not hungry!");
    	}
    }
    
    private static void LeafReDistribution(BTreeIndexNode indexNode, int pos) {
    	System.out.println("Leaf Re-Distribution");
    	// Try to re-distribute, borrowing from sibling - right silbling
		System.out.println((pos+1) +" - "+ indexNode.getSubItems().size());
		if(pos+1<indexNode.getSubItems().size()){//have right?
			System.out.println("check right won't hungry....");
			BTreeNode SliblingNode = indexNode.getSubItemAt(pos+1);
			if(!SliblingNode.isSubItemsHungry(-1)){//check right won't hungry
				System.out.println("borrow one from right....");
				// borrow one from right
				BTreeNode b = indexNode.getSubItemAt(pos);
				int insertIndex  = b.getSubItems().size();//last index 
				b.addKeyAt(insertIndex, SliblingNode.getKeyAt(0));
				b.addSubItemValueAt(insertIndex, SliblingNode.getSubItemAt(0));
				
				SliblingNode.removeKeyAt(0);
				SliblingNode.removeSubItemValueAt(0);
				
				// write new index @ parent of borrowed
				indexNode.removeKeyAt(pos);
				indexNode.addKeyAt(pos, SliblingNode.getKeyAt(0));
			}else{
				//merge right
				//Merge leaf nodes?
				//Merge Index nodes?
				System.out.println("merge right....");
				indexNode.getSubItemAt(pos).addKeys((indexNode.getSubItemAt(pos+1)).getKeys());
				indexNode.getSubItemAt(pos).addSubItemValues((indexNode.getSubItemAt(pos+1)).getSubItems());
				indexNode.removeSubItemValueAt(pos+1);
				indexNode.removeKeyAt(pos);
			}
		}else{//do not have right
			//try borrow form left , check left won't hungry
			System.out.println("check left won't hungry....");
			BTreeNode SliblingNode = indexNode.getSubItemAt(pos-1);
			if(!SliblingNode.isSubItemsHungry(-1)){//check left won't hungry
				System.out.println("borrow one from left....");
				// borrow one from left
				BTreeNode b = indexNode.getSubItemAt(pos);
				
				int insertIndex  = 0;//first index 
				int borrowIndex = SliblingNode.getSubItems().size()-1;
				b.addKeyAt(insertIndex, SliblingNode.getKeyAt(borrowIndex));
				b.addSubItemValueAt(insertIndex, SliblingNode.getSubItemAt(borrowIndex));
				// write new index @ parent of borrowed
				SliblingNode.removeKeyAt(borrowIndex);
				SliblingNode.removeSubItemValueAt(borrowIndex);
				
				indexNode.removeKeyAt(pos-1);
				indexNode.addKeyAt(pos-1, b.getKeyAt(0));
			}else{
				System.out.println("merge left....");
				indexNode.getSubItemAt(pos-1).addKeys((indexNode.getSubItemAt(pos)).getKeys());
				indexNode.getSubItemAt(pos-1).addSubItemValues((indexNode.getSubItemAt(pos)).getSubItems());
				indexNode.removeSubItemValueAt(pos);
				indexNode.removeKeyAt(pos-1);
			}
		}
    }
    
    private static void NonLeafReDistribution(BTreeIndexNode indexNode, int pos) {
    	System.out.println("Non Leaf Re-Distribution");
    	if(pos+1<indexNode.getSubItems().size()){//have right?
    		BTreeNode SliblingNode = indexNode.getSubItemAt(pos+1);
			if(!SliblingNode.isSubItemsHungry(-1)){//check right won't hungry
				System.out.println("borrow one from right....");
				// borrow one from right
				BTreeNode b = indexNode.getSubItemAt(pos);//hungry node
				int insertIndex  = b.getSubItems().size()-1;//last index 
				
				b.addKeyAt(insertIndex,indexNode.getKeyAt(pos));
				b.addSubItemValueAt(insertIndex+1, SliblingNode.getSubItemAt(0));
				
				// write new index @ parent of borrowed
				indexNode.removeKeyAt(pos);
				indexNode.addKeyAt(pos, SliblingNode.getKeyAt(0));
				
				SliblingNode.removeKeyAt(0);
				SliblingNode.removeSubItemValueAt(0);
				
			}else{//right is hungry
				indexNode.getSubItemAt(pos).addKeyAt(pos+1, indexNode.getKeyAt(pos));//pull down
				indexNode.getSubItemAt(pos).addKeys((indexNode.getSubItemAt(pos+1)).getKeys());
				indexNode.getSubItemAt(pos).addSubItemValues((indexNode.getSubItemAt(pos+1)).getSubItems());
				indexNode.removeSubItemValueAt(pos+1);
				indexNode.removeKeyAt(pos);
			}
    	}else{//left
    		System.out.println("check left won't hungry....");
			BTreeNode SliblingNode = indexNode.getSubItemAt(pos-1);
    		if(!SliblingNode.isSubItemsHungry(-1)){//check left won't hungry
    			System.out.println("borrow one from left....");
				// borrow one from left
				BTreeNode b = indexNode.getSubItemAt(pos);//hungry node
				
				int insertIndex  = 0;//first index 
				int borrowIndex = SliblingNode.getSubItems().size()-1;
				
				b.addKeyAt(insertIndex, indexNode.getKeyAt(pos-1));
				b.addSubItemValueAt(insertIndex, SliblingNode.getSubItemAt(borrowIndex));
				
				// write new index @ parent of borrowed
				indexNode.removeKeyAt(pos-1);
				indexNode.addKeyAt(pos-1, SliblingNode.getKeyAt(borrowIndex-1));
				
				
				SliblingNode.removeKeyAt(borrowIndex-1);
				SliblingNode.removeSubItemValueAt(borrowIndex);
				
				
    			
    		}else{//left is hungry
    			indexNode.getSubItemAt(pos-1).addKeyAt(pos+1,indexNode.getKeyAt(pos-1));
    			indexNode.getSubItemAt(pos-1).addKeys((indexNode.getSubItemAt(pos)).getKeys());
				indexNode.getSubItemAt(pos-1).addSubItemValues((indexNode.getSubItemAt(pos)).getSubItems());
				
				indexNode.removeSubItemValueAt(pos);
				indexNode.removeKeyAt(pos-1);
    		}
    	}
    }
   
    
}
