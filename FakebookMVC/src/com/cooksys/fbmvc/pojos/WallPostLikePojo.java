package com.cooksys.fbmvc.pojos;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = {"wallPostLikeId", "user", "page", "likeDate", "active"})
public class WallPostLikePojo {
	
	private int wallPostLikeId;
    private UserPojo user;
    private PagePojo page;
    private Date likeDate;
    private boolean active;
	
    public int getWallPostLikeId() { return wallPostLikeId; }
	public void setWallPostLikeId(int wallPostLikeId) { this.wallPostLikeId = wallPostLikeId; }
	
	public UserPojo getUser() { return user; }
	public void setUser(UserPojo user) { this.user = user; }
	
	public PagePojo getPage() { return page; }
	public void setPage(PagePojo page) { this.page = page; }
	
	public Date getLikeDate() { return likeDate; }
	public void setLikeDate(Date likeDate) { this.likeDate = likeDate; }
	
	public boolean getActive() { return active; }
	public void setActive(boolean active) { this.active = active; }
	@Override
	public String toString() {
		return "WallPostLikePojo [wallPostLikeId=" + wallPostLikeId + ", user=" + user + ", page=" + page
				+ ", likeDate=" + likeDate + ", active=" + active + "]";
	}
}
