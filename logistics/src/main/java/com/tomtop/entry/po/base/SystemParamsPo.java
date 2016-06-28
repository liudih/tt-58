package com.tomtop.entry.po.base;

import java.io.Serializable;

public class SystemParamsPo  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7601442430739083613L;
	/*{
	    "ret": 1,
	    "data": [
	        {
	            "id": 1,
	            "languageId": 1,
	            "clientId": 1,
	            "type": "STATUS",
	            "value": 1,
	            "name": "COM"
	        }
	    ]
	}*/
	private Integer id;
	private Integer languageId;
	private Integer clientId;
	private String type;
	private String value;
	private String name;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getLanguageId() {
		return languageId;
	}
	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}
	public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "SystemParamsPo [id=" + id + ", languageId=" + languageId
				+ ", clientId=" + clientId + ", type=" + type + ", value="
				+ value + ", name=" + name + "]";
	}
	
}
