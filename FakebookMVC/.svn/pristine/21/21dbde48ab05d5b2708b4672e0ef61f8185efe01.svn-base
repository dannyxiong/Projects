package com.cooksys.fbmvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cooksys.fbmvc.pojos.FriendRequestPojo;
import com.cooksys.fbmvc.pojos.PagePojo;
import com.cooksys.fbmvc.pojos.UserPojo;
import com.cooksys.fbmvc.pojos.WallPostPojo;

@Component
@Scope("session")
public class UserBean {
	private static final Logger LOG = Logger.getLogger(UserBean.class);

	@Autowired
	ApplicationBean app;

	public UserPojo user = new UserPojo();
	public UserPojo viewProfile = new UserPojo();
	public PagePojo viewPage = new PagePojo();
	public String post = "";
	public String viewingName = ""; // Page Title
	public String pageDesc = ""; // Page description
	public String searchFriendByName = "";
	public boolean disableButton = false;
	String fButton = "Add Friend";

	public List<UserPojo> searchList = new ArrayList<UserPojo>(); // done
	public List<FriendRequestPojo> fResponseList = new ArrayList<FriendRequestPojo>();
	public List<UserPojo> friendList = new ArrayList<UserPojo>();
	public List<WallPostPojo> wallPostList = new ArrayList<WallPostPojo>();
	public List<PagePojo> ownedPageList = new ArrayList<PagePojo>();

	public void logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		redirect("welcome.xhtml");
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Logged Off", "Success"));
	}

	public void sendFriendRequest() {
		LOG.info(user.getUsername() + " is requesting friendship with " + viewProfile.getUsername());
		String response = app.sendFriendRequest(user, viewProfile);
		if (response.equals("Already friends") || response.equals("Existing friend request already pending")
				|| response.equals("Success friend request")) FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, response, ""));
		else FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Something went wrong", response));
	}

	public void likeWPost(WallPostPojo likedPost) {

		String response = app.likeWallPost(likedPost);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, response, ""));
	}

	public void goToProfile(UserPojo userProfile) {
		setSearchList(new ArrayList<UserPojo>()); // reset search friend list
		setViewProfile(userProfile); // set view
		friendButton(userProfile);
		setSearchFriendByName(""); // reset search field
		// setUserFriendList(); //set friend list
		// setFriendRequestList();
		redirect("profile.xhtml");
	}

	public void acceptRequest(FriendRequestPojo acceptUser) {
		acceptUser.setResponse(true);
		String response = app.respondToFriendRequest(acceptUser);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, response, ""));
	}

	public void declineRequest(FriendRequestPojo declineUser) {
		declineUser.setResponse(false);
		String response = app.respondToFriendRequest(declineUser);
		if (response.equals("Decline")) FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, response, "Declined request"));
		else FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, response, ""));
	}

	public void ignoreFriend() {

	}

	public void login() {
		String response = app.validateUser(user);
		if (response.equals("Validation Success!")) {
			setUser(app.retrieveUserInformation(user));
			user.setPassword("");
			goToProfile(user);
			LOG.warn("User was validated successfully");
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Login failed!", response));
			LOG.warn("User validation failed!");
		}
	}

	public void register() {
		String response = app.saveNewUser(user);
		user.setPassword("");
		setUser(app.retrieveUserInformation(user));
		if (response.equals("User created successfully!")) {
			goToProfile(user);
			LOG.info("User created!");
		} else FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Registration failed!", response));
	}

	/**
	 * @return Whether the user is the owner of the page they are viewing
	 */
	public boolean isOwner() {
		return true; // Testing purposes
	}

	public void setUserFriendList() {// (user) friend list
		List<UserPojo> fresults = new ArrayList<>();
		app.retrieveFriendsFor(viewProfile).getUsers().forEach((f) -> fresults.add((UserPojo) f));
		setFriendList(fresults);
	}

	public void setFriendRequestList() {
		setFriendList(new ArrayList<UserPojo>());
		List<FriendRequestPojo> frResults = new ArrayList<FriendRequestPojo>();
		app.retrieveFriendRequestsFor(getViewProfile()).getFriendRequests()
				.forEach((r) -> frResults.add((FriendRequestPojo) r));
		setfResponseList(frResults);
	}

	public void submitPost() {
		String response = "";

		LOG.info("Submit Post to " + getViewProfile().getUsername());
		WallPostPojo wPost = new WallPostPojo();
		wPost.setUser(getViewProfile());
		wPost.setMessage(getPost());
		wPost.setPostedDate(new Date());
		wPost.setActive(true);
		wPost.setFriend(this.user);
		response = app.submitWallPost(wPost);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, response, ""));

		setPost("");

	}

	public boolean canPost() {
		if (user.getUsername().equals(viewProfile.getUsername())) return true;
		FriendRequestPojo frp = new FriendRequestPojo();
		frp.setRequestee(user);
		frp.setRequestor(viewProfile);
		if (app.retrieveFriendStatus(frp).equals("Friends")) return true;
		return false;
	}

	public List<WallPostPojo> getWallPostList() {
		return wallPostList;
	}

	public void setWallPostList(List<WallPostPojo> wallPostList) {
		this.wallPostList = wallPostList;
	}

	public void retrieveWallPostsFor() {
		UserPojo tempUser = getViewProfile();
		tempUser.setPassword(this.user.getUsername());
		Wrapper wrapper = app.retrieveWallPostsFor(tempUser);
		wallPostList = wrapper.getWallPosts();
	}

	public void getSearch() {
		List<UserPojo> results = new ArrayList<>();
		String sendSearch = searchFriendByName;
		if (sendSearch.equals("")) {
			sendSearch = " ";
		}
		app.retrieveUsers(sendSearch).getUsers().forEach((u) -> results.add((UserPojo) u));
		LOG.debug("LIST: " + results);
		setSearchList(results);
		LOG.debug("Search Friend");
	}

	public void friendButton(UserPojo viewUser) {
		// setDisableButton(false);// reset
		// setfButton("Add Friend");// reset to default values
		// if (user.getUsername().equals(viewUser.getUsername())) // If your own
		// // your own
		// // profile
		// // cannot add
		// // yourself.
		// setDisableButton(true);
		//
		if (user.getUsername().equals(viewProfile.getUsername())) {
			setDisableButton(true);
		} else {
			FriendRequestPojo frp = new FriendRequestPojo();
			frp.setRequestee(user);
			frp.setRequestor(viewProfile);
			fButton = app.retrieveFriendStatus(frp);
			if (fButton.equals("Friends") || fButton.equals("Pending")) setDisableButton(true);
			else setDisableButton(false);
		}
	}

	public void redirect(String link) {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			ec.redirect(link);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public UserPojo getUser() {
		return user;
	}

	public void setUser(UserPojo user) {
		this.user = user;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getViewingName() {
		return viewingName;
	}

	public void setViewingName(String viewingName) {
		this.viewingName = viewingName;
	}

	public String getSearchFriendByName() {
		return searchFriendByName;
	}

	public void setSearchFriendByName(String searchFriendByName) {
		this.searchFriendByName = searchFriendByName;
	}

	public void setFriendList(List<UserPojo> friendList) {
		this.friendList = friendList;
	}

	public void setSearchList(List<UserPojo> friend) {
		this.searchList = friend;
	}

	public List<UserPojo> getSearchList() {
		return searchList;
	}

	public UserPojo getViewProfile() {
		return viewProfile;
	}

	public void setViewProfile(UserPojo viewProfile) {
		this.viewProfile = viewProfile;
	}

	public List<UserPojo> getFriendList() {
		return friendList;
	}

	public List<FriendRequestPojo> getfResponseList() {
		return fResponseList;
	}

	public void setfResponseList(List<FriendRequestPojo> fResponseList) {
		this.fResponseList = fResponseList;
	}

	public boolean isDisableButton() {
		return disableButton;
	}

	public void setDisableButton(boolean disableButton) {
		this.disableButton = disableButton;
	}

	public String getfButton() {
		return fButton;
	}

	public void setfButton(String fButton) {
		this.fButton = fButton;
	}

}
