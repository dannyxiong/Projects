package com.cooksys.fbmvc;

import java.util.Date;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.cooksys.fbmvc.pojos.FriendRequestPojo;
import com.cooksys.fbmvc.pojos.UserPojo;
import com.cooksys.fbmvc.pojos.WallPostPojo;

@Component
public class ApplicationBean {
	
	private static final Logger LOG = Logger.getLogger(ApplicationBean.class);
	private static final String URL = "http://localhost:8080/Fakebook_WebService/"; 
	RestTemplate restTemplate = new RestTemplate();

	public String likeWallPost(WallPostPojo likedPost){
		String reponse = retrieveResponse(PostCommand.LIKE_WALL_POST, likedPost, String.class);
		return reponse;
	}
	public String saveNewUser(UserPojo user) {
		String response = retrieveResponse(PostCommand.SAVE_NEW_USER, user, String.class);
		LOG.info(PostCommand.SAVE_NEW_USER + " -=- " + response);
		return response;
	}
	
	public String validateUser(UserPojo user) {
		String response = retrieveResponse(PostCommand.VALIDATE_USER, user, String.class);
		LOG.info(PostCommand.VALIDATE_USER + " -=- " + response);
		return response;
	}
	
	public UserPojo retrieveUserInformation(UserPojo user) {
		user = retrieveResponse(PostCommand.RETRIEVE_USER_INFO, user, UserPojo.class);
		LOG.info(PostCommand.RETRIEVE_USER_INFO + " -=- " + user);
		return user;
	}
	
	public Wrapper retrieveUsers(String search) {
		LOG.info("Before retrieving users: " + search);
		return retrieveResponse(PostCommand.RETRIEVE_USERS, search, Wrapper.class);
	}
	
	public Wrapper retrieveFriendsFor(UserPojo user) {
		return retrieveResponse(PostCommand.RETRIEVE_FRIENDS, user, Wrapper.class);
	}
	
	public String sendFriendRequest(UserPojo user, UserPojo receiver) {
		FriendRequestPojo fRequest = new FriendRequestPojo();
			fRequest.setRequestor(user);
			fRequest.setFriendRequestDate(new Date());
			fRequest.setRequestee(receiver);
			fRequest.setResponse(false);
			fRequest.setActive(true);
		LOG.info(fRequest.getRequestor().getUsername() + " is requesting friendship with " + fRequest.getRequestee().getUsername());
		return retrieveResponse(PostCommand.SEND_FRIEND_REQUEST, fRequest, String.class);	
	}
	
	public String respondToFriendRequest(FriendRequestPojo friendRequest) {
		LOG.info("ApplicationBean RESPOND TO REQUEST: " + friendRequest);
		return retrieveResponse(PostCommand.RESPOND_FRIEND_REQUEST, friendRequest, String.class);
	}
	
	public Wrapper retrieveFriendRequestsFor(UserPojo user) {
		LOG.debug("GET all friend requests");
		return retrieveResponse(PostCommand.RETRIEVE_FRIEND_REQUEST, user, Wrapper.class);
	}
	
	public String retrieveFriendStatus(FriendRequestPojo friendRequest) {
		return retrieveResponse(PostCommand.RETRIEVE_FRIEND_STATUS, friendRequest, String.class);
	}
	
	public String submitWallPost(WallPostPojo post) {
		String response = retrieveResponse(PostCommand.SUBMIT_WALL_POST, post, String.class);
		LOG.info(PostCommand.SUBMIT_WALL_POST + " -=- " + response);
		return response;
	}
	
	public Wrapper retrieveWallPostsFor(UserPojo user) {
		return retrieveResponse(PostCommand.RETREIVE_WALL_POSTS, user, Wrapper.class);
	}
	
	/**
	 * Sends a request for object of type {@code type} using the parameter {@code obj}.
	 * The command to specify what method the server will execute is denoted by {@code cmd}.
	 * 
	 * @param cmd The PostCommand to use
	 * @param obj The parameter that the server will take for the method
	 * @param type The return type of the server's method
	 * @return The object the server returns
	 */
	public <T> T retrieveResponse(String cmd, Object obj, Class<T> type) {
		return restTemplate.postForObject(URL + cmd, obj, type);
	}
	
	/** An interface of server method names that can be called */
	private interface PostCommand {
		String SAVE_NEW_USER = "saveNewUser", VALIDATE_USER = "validateUser",
				RETRIEVE_USER_INFO = "retrieveUserInfo", RETRIEVE_USERS = "retrieveUsers",
				SEND_FRIEND_REQUEST = "sendFriendRequest", RESPOND_FRIEND_REQUEST = "respondToFriendRequest",
				RETRIEVE_FRIEND_REQUEST = "retrieveFriendRequestFor", RETRIEVE_FRIEND_STATUS = "retrieveFriendStatus",
				SUBMIT_WALL_POST = "submitWallPost", RETREIVE_WALL_POSTS = "retrieveWallPostsFor",
				RETRIEVE_FRIENDS = "retrieveFriendsFor", LIKE_WALL_POST = "likeWallPost";
	}
	
}
