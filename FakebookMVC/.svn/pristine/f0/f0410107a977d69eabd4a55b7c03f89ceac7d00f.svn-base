package com.cooksys.fbmvc.pojos;

import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = {"wallPostId", "user", "friend", "page", "postingPage", "message", "postedDate", "active", "likes"})
public class WallPostPojo {
	
	int wallPostId;
	UserPojo user, friend;
	PagePojo page, postingPage;
	String message;
	Date postedDate;
	boolean active;
	ArrayList<WallPostLikePojo> likes = new ArrayList<WallPostLikePojo>();
	
	public int getWallPostId() { return wallPostId; }
	public void setWallPostId(int wallPostId) { this.wallPostId = wallPostId; }
	
	public UserPojo getUser() { return user; }
	public void setUser(UserPojo user) { this.user = user; }
	
	public UserPojo getFriend() { return friend; }
	public void setFriend(UserPojo friend) { this.friend = friend; }
	
	public PagePojo getPage() { return page; }
	public void setPage(PagePojo page) { this.page = page; }
	
	public PagePojo getPostingPage() { return postingPage; }
	public void setPostingPage(PagePojo postingPage) { this.postingPage = postingPage; }
	
	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }
	
	public Date getPostedDate() { return postedDate; }
	public void setPostedDate(Date postedDate) { this.postedDate = postedDate; }
	
	public boolean isActive() { return active; }
	public void setActive(boolean active) { this.active = active; }
	
	public ArrayList<WallPostLikePojo> getLikes() { return likes; }
	public void setLikes(ArrayList<WallPostLikePojo> likes) { this.likes = likes; }
	
	public String toString() {
		return "WallPostPojo [wallPostId=" + wallPostId + ", user=" + user
				+ ", friend=" + friend + ", page=" + page + ", postingPage="
				+ postingPage + ", message=" + message + ", postedDate="
				+ postedDate + ", active=" + active + ", likes=" + likes + "]";
	}
	
	
}