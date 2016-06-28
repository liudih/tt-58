package com.tomtop.filter.wrapper;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.tomtop.utils.CommonsUtil;

public class ParameterRequestWrapper extends HttpServletRequestWrapper {

	  private Map<String, String[]> params;  
	  
	  public ParameterRequestWrapper(HttpServletRequest request,  
	            Map<String, String[]> newParams) {  
	        super(request);  
	        this.params = newParams;  
	        renewParameterMap(request);  
	  }  
	  
	  @Override  
	  public String getParameter(String name) {  
	        String result = "";  
	          
	        Object v = params.get(name);  
	        if (v == null) {  
	            result = null;  
	        } else if (v instanceof String[]) {  
	            String[] strArr = (String[]) v;  
	            if (strArr.length > 0) {  
	                result =  strArr[0];  
	            } else {  
	                result = null;  
	            }  
	        } else if (v instanceof String) {  
	            result = (String) v;  
	        } else {  
	            result =  v.toString();  
	        }  
	          
	        return result;  
	  }  
	  
	  @Override  
	  public Map<String, String[]> getParameterMap() {  
	      return params;  
	  }  
	  
	  @Override  
	  public Enumeration<String> getParameterNames() {  
	      return new Vector<String>(params.keySet()).elements();  
	  }  
	  
	  @Override  
	  public String[] getParameterValues(String name) {  
	      String[] result = null;  
	        
	      Object v = params.get(name);  
	      if (v == null) {  
	          result =  null;  
	      } else if (v instanceof String[]) {  
	          result =  (String[]) v;  
	      } else if (v instanceof String) {  
	          result =  new String[] { (String) v };  
	      } else {  
	          result =  new String[] { v.toString() };  
	      }  
	        
	      return result;  
	  }  
	  
	  private void renewParameterMap(HttpServletRequest req) {  
	      Map<String, String[]> map = req.getParameterMap();  
	        Set<Entry<String, String[]>> set = map.entrySet();  
	        Iterator<Entry<String, String[]>> it = set.iterator();  
	        String key = "";
	        while (it.hasNext()) {  
	            Entry<String, String[]> entry = it.next();  
	            key = entry.getKey();
	            //System.out.println("KEY:" + key);  
	            for (String i : entry.getValue()) {  
	               // System.out.println(i); 
	                this.params.put(key, new String[] { CommonsUtil.checkSpecialChar(i) });  
	            }
	        }
	  }  

}
