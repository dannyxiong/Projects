package com.cooksys.fbws;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cooksys.fbws.pojos.FriendRequestPojo;
import com.cooksys.fbws.pojos.PagePojo;
import com.cooksys.fbws.pojos.UserPojo;
import com.cooksys.fbws.pojos.WallPostPojo;

@Controller
public class WebService {

	private static final Logger LOG = Logger.getLogger(WebService.class);
	
	@Autowired
	DBHandler dbHandler;
	@Autowired
	JMSHandler jmsHandler;

	// TODO: Decide what object these methods will reply to the MVC with
	// User Mappings
	@RequestMapping("/saveNewUser")
	@ResponseBody
	public String saveNewUser(@RequestBody UserPojo user) {
		String result = dbHandler.saveNewUser(user, false);
		jmsHandler.saveNewUser(user);
		return result;
	}

	@RequestMapping("/validateUser")
	@ResponseBody
	public String validateUser(@RequestBody UserPojo user) {
		return dbHandler.validateUser(user);
	}

	@RequestMapping("/retrieveUserInfo")
	@ResponseBody
	public UserPojo retrieveUserInformation(@RequestBody UserPojo user) {
		return dbHandler.retrieveUserInformation(user);
	}

	@RequestMapping("/retrieveUsers")
	@ResponseBody
	public Wrapper retrieveUsers(@RequestBody String search) {
		return dbHandler.retrieveUsers(search.trim());
	}

	@RequestMapping("/retrieveFriendsFor")
	@ResponseBody
	public Wrapper retrieveFriendsFor(@RequestBody UserPojo user) {
		return dbHandler.retrieveFriendsFor(user);
	}

	// --- Friend Request Mappings ---
	@RequestMapping("/sendFriendRequest")
	@ResponseBody
	public String sendFriendRequest(@RequestBody FriendRequestPojo friendRequest) {
		String result = dbHandler.sendFriendRequest(friendRequest, false);
		jmsHandler.sendFriendRequest(friendRequest);
		return result;
	}

	@RequestMapping("/respondToFriendRequest")
	@ResponseBody
	public String respondToFriendRequest(@RequestBody FriendRequestPojo friendRequest) {
		String result = dbHandler.respondToFriendRequest(friendRequest, false);
		LOG.info("WebService RESPOND TO REQUEST " + friendRequest);
		if (friendRequest.isResponse()) jmsHandler.acceptFriendRequest(friendRequest);
		else jmsHandler.denyFriendRequest(friendRequest);
		return result;
	}

	@RequestMapping("/retrieveFriendRequestFor")
	@ResponseBody
	public Wrapper retrieveFriendRequestFor(@RequestBody UserPojo user) {
		return dbHandler.retrieveFriendRequestsFor(user);
	}

	@RequestMapping("/retrieveFriendStatus")
	@ResponseBody
	public String retrieveFriendStatus(@RequestBody FriendRequestPojo friendRequest) {
		return dbHandler.retrieveFriendStatus(friendRequest);
	}

	@RequestMapping("/submitWallPost")
	@ResponseBody 
	public String submitWallPost(@RequestBody WallPostPojo post) {
		String result = dbHandler.submitWallPost(post, false);
		jmsHandler.submitWallPost(post);
		return result;
	}

	@RequestMapping("/retrieveWallPostsFor")
	@ResponseBody
	public Wrapper retrieveWallPostsFor(@RequestBody UserPojo user) {
		return dbHandler.retrieveWallPostsFor(user);
	}

	// --- Page Mappings ---
	@RequestMapping("/savePage")
	@ResponseBody
	public String savePage(@RequestBody PagePojo page) {
		String result = dbHandler.savePage(page, false);
		jmsHandler.savePage(page);
		return result;
	}

	@RequestMapping("/retrievePages")
	@ResponseBody
	public void retrievePages(@RequestBody String search) {
		// return dbHandler.retrievePages(search.trim());
	}

	// --- Like Mappings ---
	@RequestMapping("/likePage")
	@ResponseBody
	public void likePage(@RequestBody PagePojo page) {
		// return dbHandler.likePage(page);
	}

	@RequestMapping("/likeWallPost")
	@ResponseBody
	public void likeWallPost(@RequestBody WallPostPojo post) {
		// return dbHandler.likeWallPost(page);
	}
}
