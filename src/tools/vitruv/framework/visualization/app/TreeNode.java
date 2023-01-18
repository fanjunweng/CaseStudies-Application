package tools.vitruv.framework.visualization.app;


public class TreeNode<EObject> {

    private EObject mData;   //stored eobject 
    private int mParent;   //index of the parent node

    public TreeNode(EObject data, int parent) {
        mData = data;
        mParent = parent;
    }

    public Object getData() {
        return mData;
    }

    public void setData(EObject data) {
        mData = data;
    }

    public int getParent() {
        return mParent;
    }

    public void setParent(int parent) {
        mParent = parent;
    }
}