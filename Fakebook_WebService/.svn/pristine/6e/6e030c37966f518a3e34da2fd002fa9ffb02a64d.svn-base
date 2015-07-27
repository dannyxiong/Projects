package com.cooksys.fbws.pojos;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = {"likeId", "likingUser", "likingPage", "likeDate", "active"})
public class LikePojo {
	
	private int likeId;
    private UserPojo likingUser;
    private PagePojo likingPage;
    private Date likeDate;
    private boolean active;
    
	public int getLikeId() { return likeId; }
	public void setLikeId(int likeId) { this.likeId = likeId; }
	
	public UserPojo getLikingUser() { return likingUser; }
	public void setLikingUser(UserPojo likingUser) { this.likingUser = likingUser; }
	
	public PagePojo getLikingPage() { return likingPage; }
	public void setLikingPage(PagePojo likingPage) { this.likingPage = likingPage; }
	
	public Date getLikeDate() { return likeDate; }
	public void setLikeDate(Date likeDate) { this.likeDate = likeDate; }
	
	public boolean isActive() { return active; }
	public void setActive(boolean active) { this.active = active; }
}
