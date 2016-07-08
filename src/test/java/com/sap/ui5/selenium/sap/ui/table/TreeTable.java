package com.sap.ui5.selenium.sap.ui.table;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.ui.core.ScrollBar;


public class TreeTable extends Table{

	protected TreeTable(String id, Frame frame) {
		super(id, frame);
	}
	
	/**
	 * get Nodes of TreeTable
	 * @param columnName
	 *				column name of the tree belongs to
	 *  
	*/
	public Node getTreeStatus(String columnName) {
		ScrollBar scrollBar = this.getScrollBar();
		int colindex = this.getColumnIndex(columnName);
		colindex = colindex==-1?0:colindex;
		Node tree = new Node("root",-1,-1);
		List<Node> parents = new ArrayList<Node>();
		parents.add(tree);
		this.expandAllNodes();
		int pageSize = this.getTableRows().size();
		int start = pageSize;
		int page = 0;
		
		while(start >= 0) {
			this.expandAllNodes();
			List<WebElement> rows= this.getTableRows();
			for(int i=rows.size()-start; i<rows.size(); i++) {
				WebElement row = rows.get(i);
				String title = this.getCellValue(row.findElements(By.tagName("td")).get(colindex)).trim();
				int level = Integer.parseInt(row.getAttribute("aria-level"));
				Node currentNode = new Node(title, i, page);
				parents.get(level-1).children.add(currentNode);
				if(parents.size() == level) {
					parents.add(currentNode);
				}
				else {
					parents.set(level, currentNode);
				}
			}
			
			start = scrollBar==null?-1:scrollBar.scrollToNextPage(pageSize);
			page ++;
		}
		return tree;
	}
	
	/**
	 * scroll and select nodes of treeTable by paths
	 * @param paths
	 *				the paths of tree
	 *				eg:{"Roles/Viewer,Modeler","Files/public/file1,file2,file3"} 
	 *  
	*/
	public void selectRows(String columnName, String[] paths) {
		Node tree = getTreeStatus(columnName);
		ScrollBar scrollBar = this.getScrollBar();
		scrollBar.setScrollPosition(0);
		
		for(String path : paths){
			String[] pathLevel = path.split("/");
			Node current = tree;
			Node parent = tree;
			for(int i=0; i<pathLevel.length; i++) {
				String[] childNames = pathLevel[i].split(",");
				for(String childName : childNames) {
					current = parent.getChild(childName);
					if(current == null) {
						break;
					}
					if(i==pathLevel.length-1){
						this.selectRowByIndex(current.m_page, current.m_index, this.getTableRows().size());
					}
				}
				parent = current;
			}
		}
	}
	
	/**
	 * scroll and select a single row by the node index and page
	 * @param page
	 *				the row' page index in the table 
	 * @param index
	 * 				the row position in the page
	 * @param pageSize
	 * 				the row count in one page of the table
	*/
	private void selectRowByIndex(int page, int index, int pageSize) {
		ScrollBar scrollBar = this.getScrollBar();
		if(scrollBar!=null){
			while(page>0) {
				scrollBar.scrollToNextPage(pageSize);
				page--;
			}
		}
		this.selectSingleRow(index);
	}
}


/**
 * data struct to save the treeNode
 *  
*/
class Node {
	public String m_title;
	public int m_index;
	public int m_page;
	public ArrayList<Node> children;
	
	public Node() {
		m_title = "root";
		m_index = -1;
		m_page = -1;
		children = new ArrayList<Node>();
	}
	
	public Node(String title, int index, int page) {
		m_title = title;
		m_index = index;
		m_page = page;
		children = new ArrayList<Node>();
	}
	
	public void addChild(Node child) {
		this.children.add(child);
	}
	
	public Node getChild(String title) {
		for(int i=0; i<children.size(); i++) {
			if(children.get(i).m_title.equals(title)) {
				return children.get(i);
			}
		}
		return null;
	}
}


