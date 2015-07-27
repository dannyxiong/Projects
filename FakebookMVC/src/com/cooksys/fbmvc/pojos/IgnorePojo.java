package com.cooksys.fbmvc.pojos;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = {"ignoreId", "ignorerUser", "ignoreeUser", "ignoreePage", "ignoreDate", "active"})
public class IgnorePojo {
	
	int ignoreId;
	UserPojo ignorerUser, ignoreeUser;
	PagePojo ignoreePage;
	Date ignoreDate;
	boolean active;
	
	public int getIgnoreId() { return ignoreId; }
	public void setIgnoreId(int ignoreId) { this.ignoreId = ignoreId; }
	
	public UserPojo getIgnorerUser() { return ignorerUser; }
	public void setIgnorerUser(UserPojo ignorerUser) { this.ignorerUser = ignorerUser; }
	
	public UserPojo getIgnoreeUser() { return ignoreeUser; }
	public void setIgnoreeUser(UserPojo ignoreeUser) { this.ignoreeUser = ignoreeUser; }
	
	public PagePojo getIgnoreePage() { return ignoreePage; }
	public void setIgnoreePage(PagePojo ignoreePage) { this.ignoreePage = ignoreePage; }
	
	public Date getIgnoreDate() { return ignoreDate; }
	public void setIgnoreDate(Date ignoreDate) { this.ignoreDate = ignoreDate; }
	
	public boolean isActive() { return active; }
	public void setActive(boolean active) { this.active = active; }
	
	public String toString() {
		return "IgnorePojo [ignoreId=" + ignoreId + ", ignorerUser=" + ignorerUser + ", ignoreeUser=" + ignoreeUser
				+ ", ignoreePage=" + ignoreePage + ", ignoreDate=" + ignoreDate + ", active=" + active + "]";
	}
	
}
