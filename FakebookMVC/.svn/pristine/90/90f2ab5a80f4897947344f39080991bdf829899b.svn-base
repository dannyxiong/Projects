package com.cooksys.fbmvc;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.cooksys.fbmvc.pojos.FriendRequestPojo;
import com.cooksys.fbmvc.pojos.LikePojo;
import com.cooksys.fbmvc.pojos.PagePojo;
import com.cooksys.fbmvc.pojos.UserPojo;
import com.cooksys.fbmvc.pojos.WallPostLikePojo;
import com.cooksys.fbmvc.pojos.WallPostPojo;

@XmlRootElement
@XmlType(propOrder = {"users", "friendRequests", "wallPosts", "wallPostLikes", "pages", "likes"})
public class Wrapper {
	
	ArrayList<UserPojo> users = new ArrayList<>();
	ArrayList<FriendRequestPojo> friendRequests = new ArrayList<>();
	ArrayList<WallPostPojo> wallPosts = new ArrayList<>();
	ArrayList<WallPostLikePojo> wallPostLikes = new ArrayList<>();
	ArrayList<PagePojo> pages = new ArrayList<>();
	ArrayList<LikePojo> likes = new ArrayList<>();
	
	public ArrayList<UserPojo> getUsers() { return users; }
	public void setUsers(ArrayList<UserPojo> users) { this.users = users; }
	
	public ArrayList<FriendRequestPojo> getFriendRequests() { return friendRequests; }
	public void setFriendRequests(ArrayList<FriendRequestPojo> friendRequests) { this.friendRequests = friendRequests; }
	
	public ArrayList<WallPostPojo> getWallPosts() { return wallPosts; }
	public void setWallPosts(ArrayList<WallPostPojo> wallPosts) { this.wallPosts = wallPosts; }
	
	public ArrayList<PagePojo> getPages() { return pages; }
	public void setPages(ArrayList<PagePojo> pages) { this.pages = pages; }
	
	public ArrayList<WallPostLikePojo> getWallPostLikes() { return wallPostLikes; }
	public void setWallPostLikes(ArrayList<WallPostLikePojo> wallPostLikes) { this.wallPostLikes = wallPostLikes; }
	
	public ArrayList<LikePojo> getLikes() { return likes; }
	public void setLikes(ArrayList<LikePojo> likes) { this.likes = likes; }
	
	public String toString() {
		return "Wrapper [users=" + users + ", friendRequests=" + friendRequests + ", wallPosts=" + wallPosts
				+ ", wallPostLikes=" + wallPostLikes + ", pages=" + pages + ", likes=" + likes + "]";
	}
}
