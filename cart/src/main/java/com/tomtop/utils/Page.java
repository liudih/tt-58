package com.tomtop.utils;

import java.io.Serializable;

/**
 * @ClassName: Page 
 * @Description: 页面对象 用于列表分页 
 * @author 文龙
 * @date  2015-11-6 上午11:25:29 
 */
public class Page implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public static final int TOTAL_PAGE_DEFAULT = 0;    //总页数 ,缺省为0
	public static final int TOTAL_RECORD_DEFAULT = 0;  //总记录数 ,缺省为0
	public static final int START_REC_DEFAULT = 0;     //用于排序起始行,缺省为0(包含0)
	public static final int END_REC_DEFAULT = 10;      //用于排序结束行，缺省为10（不包含10）
	public static final int CURRENT_PAGE_DEFAULT = 1;  //当前页  ,缺省值1
	public static final int PAGE_SIZE_DEFAULT = 15;    //每页显示的条数,缺省值10
	
	/**
	 * 总记录数
	 */
    private int totalRecord;
    
    /**
     * 总页数
     */
    private int totalPage;
    
    /**
     * 起始行
     */
    private int startRec;
    
    /**
     * 结束行
     */
    private int endRec;
    
    /**
     * 当前页
     */
    private int currentPage;
    
    /**
     * 每页显示的条数
     */
	private int pageSize;
    
    public Page(){
    	super();
    	this.totalPage = TOTAL_PAGE_DEFAULT;    
    	this.totalRecord = TOTAL_RECORD_DEFAULT;  
    	this.startRec = START_REC_DEFAULT;    
		this.endRec = END_REC_DEFAULT;      
		this.currentPage = CURRENT_PAGE_DEFAULT;  
		this.pageSize = PAGE_SIZE_DEFAULT;    
    }
    
    /**
     * page对象
     */
    public static Page page = new Page();

    public void setCurrentPage(int currentPage) {
		if (currentPage > totalPage && totalPage != 0) {
			this.currentPage = totalPage;
		} else if (currentPage < 1) {
			this.currentPage = 1;
		} else {
			this.currentPage = currentPage;
		}
	}

    public int getCurrentPage(){
        return currentPage;
    }
    
    public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public void setStartRec(int startRec) {
		this.startRec = startRec;
	}

	public void setEndRec(int endRec) {
		this.endRec = endRec;
	}

	public void setTotalRecord(int totalRecord){
        this.totalRecord = totalRecord;
    }

    public int getTotalRecord(){
        return totalRecord;
    }

    public void setTotalPage(){
        if(totalRecord % pageSize != 0){
            totalPage = totalRecord / pageSize + 1;
        }else{
            totalPage = totalRecord / pageSize;
        }
    }

    public int getTotalPage(){
        return totalPage;
    }

    public void setStartRec(){
        if(totalPage == 0){
            startRec = 0;
        }else{
            startRec = (currentPage - 1) * pageSize;
        }
    }

    public int getStartRec(){
        return startRec;
    }

    public void setEndRec(){
        if(totalPage <= 1){
            endRec = totalRecord;
        }else if(currentPage == totalPage){
            endRec = totalRecord;
        }else{
            endRec = (startRec + pageSize);
        }
    }

    public int getEndRec(){
        return endRec;
    }
    
    /**
     * 传入当前页，总记录数，每页显示的条数，计算出起始行和结束行，存放在page对象中
     * 
     * @Title: getPage 
     * @param currentPage 当前页
     * @param totalRecord 总记录数
     * @param pageSize 每页显示的记录数
     * @return Page    返回类型 
     * @throws
     */
    public static Page getPage(int currentPage,int totalRecord,int pageSize){
    	
    	// 填充总记录数
		page.setTotalRecord(totalRecord);
		
		// 填充每页显示的条数
		page.setPageSize(pageSize);
    	
    	// 计算总页数
		page.setTotalPage();
    	
		// 填充当前页
		page.setCurrentPage(currentPage);
		
		// 计算起始行
		page.setStartRec();
		
		// 计算结束行
		page.setEndRec();
		
		return page;
    }
    
    public static void main(String args[]){
    	Page page = Page.getPage(2, 11, 3);
    	int startRec = page.getStartRec();
    	int endRec = page.getEndRec();
		int currentPage = page.getCurrentPage();
		int pageSize = page.getPageSize();
		int totalPage = page.getTotalPage();
		int totalRecord = page.getTotalRecord();
    	System.out.println("startRec="+startRec);
    	System.out.println("endRec="+endRec);
    	System.out.println("totalPage="+totalPage);
    	System.out.println("currentPage="+currentPage);
    	System.out.println("totalRecord="+totalRecord);
    	System.out.println("pageSize="+pageSize);
    	
    }
}
