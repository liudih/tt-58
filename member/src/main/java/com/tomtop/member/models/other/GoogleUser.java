package com.tomtop.member.models.other;

import java.io.Serializable;
import java.util.List;

import com.tomtop.member.models.bo.OAuthUserBo;

public class GoogleUser extends OAuthUserBo implements Serializable {
	
	String kind;
	String etag;
	String gender;
	List<GoogleUserEmail> emails;
	String objectType;
	String id;
	String displayName;
	GoogleUserName name;
	String url;
	GoogleUserImage image;
	Boolean isPlusUser;
	String language;
	GoogleAgeRange ageRange;
	Integer circledByCount;
	Boolean verified;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getEtag() {
		return etag;
	}
	public void setEtag(String etag) {
		this.etag = etag;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public List<GoogleUserEmail> getEmails() {
		return emails;
	}
	public void setEmails(List<GoogleUserEmail> emails) {
		this.emails = emails;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public GoogleUserName getName() {
		return name;
	}
	public void setName(GoogleUserName name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public GoogleUserImage getImage() {
		return image;
	}
	public void setImage(GoogleUserImage image) {
		this.image = image;
	}
	public Boolean getIsPlusUser() {
		return isPlusUser;
	}
	public void setIsPlusUser(Boolean isPlusUser) {
		this.isPlusUser = isPlusUser;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public GoogleAgeRange getAgeRange() {
		return ageRange;
	}
	public void setAgeRange(GoogleAgeRange ageRange) {
		this.ageRange = ageRange;
	}
	public Integer getCircledByCount() {
		return circledByCount;
	}
	public void setCircledByCount(Integer circledByCount) {
		this.circledByCount = circledByCount;
	}
	public Boolean getVerified() {
		return verified;
	}
	public void setVerified(Boolean verified) {
		this.verified = verified;
	}
}
