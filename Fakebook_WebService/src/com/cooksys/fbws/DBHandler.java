package com.cooksys.fbws;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cooksys.fbws.hibernate.FriendRequests;
import com.cooksys.fbws.hibernate.FriendRequestsId;
import com.cooksys.fbws.hibernate.Friends;
import com.cooksys.fbws.hibernate.FriendsId;
import com.cooksys.fbws.hibernate.Ignores;
import com.cooksys.fbws.hibernate.Likes;
import com.cooksys.fbws.hibernate.Pages;
import com.cooksys.fbws.hibernate.Users;
import com.cooksys.fbws.hibernate.WallPostLikes;
import com.cooksys.fbws.hibernate.WallPosts;
import com.cooksys.fbws.pojos.FriendRequestPojo;
import com.cooksys.fbws.pojos.IgnorePojo;
import com.cooksys.fbws.pojos.LikePojo;
import com.cooksys.fbws.pojos.PagePojo;
import com.cooksys.fbws.pojos.UserPojo;
import com.cooksys.fbws.pojos.WallPostLikePojo;
import com.cooksys.fbws.pojos.WallPostPojo;

@Component
@Transactional
@SuppressWarnings("unchecked")
public class DBHandler {

	private static final Logger LOG = Logger.getLogger(DBHandler.class);
	private static final String[] searchTypes = { "username", "firstName", "lastName" };

	@Autowired
	SessionFactory sessionFactory;

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	// TODO change queries to account for active flag (DONE but some may need to
	// account for additional active flags, such as users contained by a friend
	// request, so keep an eye out for any behavior that could be a result of
	// this)

	public String saveNewUser(UserPojo user, boolean save) {

		// Query by the user object's username; Validates if user's username
		// exists in db
		Query q = getCurrentSession().createQuery("from Users where username = :uname and active = 1");
		q.setParameter("uname", user.getUsername());
		List<Users> userDatabaseList = q.list();

		// Condition states if the user is not already in the database then
		// create the new user.
		if (userDatabaseList.isEmpty()) {
			Users saveNewUser = new Users(user.getUsername(), BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()),
					user.getFirstName(), user.getLastName(), new Date(), 1);
			if (save) getCurrentSession().save(saveNewUser);
			LOG.info("User created successfully!");
			return "User created successfully!";
		} else {
			// User already exists
			LOG.info("Username already exists");
			return "Username already exists";
		}
	}

	public String validateUser(UserPojo user) {
		Query q = getCurrentSession().createQuery("from Users where username = :uname and active = 1");
		q.setParameter("uname", user.getUsername());

		List<Users> userDatabaseList = q.list();

		if (!userDatabaseList.isEmpty()) {
			Users dbUser = (Users) q.list().get(0);
			if (BCrypt.checkpw(user.getPassword(), dbUser.getPassword())) {
				LOG.info("Validation Success!");
				return "Validation Success!";
			}
		}
		LOG.info("Validation Failure");
		return "Validation Failure";
	}

	public UserPojo retrieveUserInformation(UserPojo user) {
		Query q = getCurrentSession().createQuery("from Users where username = :uname and active = 1");
		q.setParameter("uname", user.getUsername());

		List<Users> userDatabaseList = q.list();

		if (!userDatabaseList.isEmpty()) {
			Users dbUser = (Users) q.list().get(0);
			user.setUserId(dbUser.getUserId());
			user.setFirstName(dbUser.getFirstName());
			user.setLastName(dbUser.getLastName());
			user.setRegisteredDate(dbUser.getRegisteredDate());
			user.setActive((dbUser.getActive() == 0) ? false : true);
			LOG.info("User found!");
		} else {
			LOG.info("Could not find user.");
		}
		return user;
	}

	public Wrapper retrieveUsers(String search) {
		Wrapper wrapper = new Wrapper();
		LOG.info("Received search: " + search);
		Query q;
		List<Users> searchedUser;
		for (String st : searchTypes) {
			q = getCurrentSession().createQuery("from Users where " + st + " like :search and active = 1");
			q.setParameter("search", search + "%");
			searchedUser = q.list();
			LOG.info("Immediately after q.list() searchedUser.size()=" + searchedUser.size());
			for (Users u : searchedUser) {
				UserPojo user = new UserPojo();
				user.setUserId(u.getUserId());
				user.setUsername(u.getUsername());
				user.setFirstName(u.getFirstName());
				user.setLastName(u.getLastName());
				user.setRegisteredDate(u.getRegisteredDate());
				user.setActive((u.getActive() == 0) ? false : true);
				if (!wrapper.getUsers().contains(user)) wrapper.getUsers().add(user);
				LOG.info("Finished adding user to list: " + user);
			}
		}
		LOG.info("Sending wrapper containing " + wrapper.getUsers().size() + " UserPojos");
		return wrapper;
	}

	public String sendFriendRequest(FriendRequestPojo friendRequest, boolean save) {
		FriendRequests friendRequestDB = new FriendRequests();
		FriendRequestsId frID = new FriendRequestsId();

		// Find requested user and set it in friendRequestDB
		Query q = getCurrentSession().createQuery("from Users where username = :uname  and active = 1");
		q.setParameter("uname", friendRequest.getRequestee().getUsername());
		if (q.list().isEmpty()) return "Requested user not found";
		Users requestedUser = (Users) q.list().get(0);

		// Find requesting user and set it in friendRequestDB
		q = getCurrentSession().createQuery("from Users where username = :uname and active = 1");
		q.setParameter("uname", friendRequest.getRequestor().getUsername());
		if (q.list().isEmpty()) return "Requesting user not found";
		Users requestingUser = (Users) q.list().get(0);

		// Determine if users are already friends
		Query q2 = getCurrentSession()
				.createQuery(
						"from Friends as friends where friends.usersByUserId.userId = :userId and friends.usersByFriendId.userId = :friendId and friends.active = 1");
		q2.setString("userId", String.valueOf(requestedUser.getUserId()));
		q2.setString("friendId", String.valueOf(requestingUser.getUserId()));

		if (!q2.list().isEmpty()) {
			LOG.debug("Already friends");
			return "Already friends";
		}

		// Determine if there is already an active friend request
		q2 = getCurrentSession()
				.createQuery(
						"from FriendRequests as fr where fr.usersByUserId.userId = :userId and fr.usersByFriendId.userId = :friendId and fr.active = 1");
		q2.setString("userId", String.valueOf(requestedUser.getUserId()));
		q2.setString("friendId", String.valueOf(requestingUser.getUserId()));

		if (!q2.list().isEmpty()) {
			LOG.info("Existing friend request already pending");
			return "Existing friend request already pending";
		}

		// Save friend request to database
		if (requestingUser != null && requestedUser != null) {
			// frID.setUserId(friendRequestDB.getUsersByUserId().getUserId());
			// frID.setFriendId(friendRequestDB.getUsersByFriendId().getUserId());
			frID.setUserId(requestedUser.getUserId());
			frID.setFriendId(requestingUser.getUserId());
			friendRequestDB.setId(frID);
			friendRequestDB.setUsersByUserId(requestedUser);
			friendRequestDB.setUsersByFriendId(requestingUser);
			friendRequestDB.setFriendRequestDate(new Date());
			friendRequestDB.setActive(1); // True

			if (save) getCurrentSession().save(friendRequestDB);
			LOG.info(requestingUser.getUsername() + " successfully requested friendship with " + requestedUser.getUsername());
			return "Success friend request";
		}
		LOG.info("Error sending friend request");
		return "Error sending friend request";
	}

	public String respondToFriendRequest(FriendRequestPojo friendRequest, boolean save) {
		LOG.info("Immediately upon starting response friendRequest=" + friendRequest);
		// Find requested user and set it in friendRequestDB
		Query q = getCurrentSession().createQuery("from Users where username = :uname and active = 1");
		q.setParameter("uname", friendRequest.getRequestee().getUsername());
		if (q.list().isEmpty()) return "User doesn't exist";
		Users requestedUser = (Users) q.list().get(0);

		// Find requesting user and set it in friendRequestDB
		q = getCurrentSession().createQuery("from Users where username = :uname and active = 1");
		q.setParameter("uname", friendRequest.getRequestor().getUsername());
		if (q.list().isEmpty()) return "Friend doesn't exist";
		Users requestingUser = (Users) q.list().get(0);

		// Determine if users are already friends
		Query q2 = getCurrentSession()
				.createQuery(
						"from Friends as friends where friends.usersByUserId.username = :username and friends.usersByFriendId.username = :friendname and friends.active = 1");
		q2.setString("username", requestedUser.getUsername());
		q2.setString("friendname", requestingUser.getUsername());

		if (!q2.list().isEmpty()) {
			LOG.info("Already friends");
			return "Already friends";
		}

		// Determine if there is already an active friend request
		q2 = getCurrentSession()
				.createQuery(
						"from FriendRequests as fr where fr.usersByUserId.username = :username and fr.usersByFriendId.username = :friendname and fr.active = 1");
		q2.setString("username", requestedUser.getUsername());
		q2.setString("friendname", requestingUser.getUsername());
		LOG.info("Immediately before failing condition requestedUser=" + requestedUser.getUsername() + " requestingUser=" + requestingUser.getUsername());
		if (!q2.list().isEmpty()) {
			LOG.info("!q2.list().isEmpty() condition met");
			// Set the friend request to inactive
			String respond = "Decline";
			FriendRequests friendRequestsDB = (FriendRequests) q2.list().get(0);
			if (save) {
				friendRequestsDB.setActive(0); // False
				getCurrentSession().saveOrUpdate(friendRequestsDB);
			}
			
			if (friendRequest.isResponse()) {
				LOG.info("friendRequest.isResponse() condition met");
				Date friendDate = new Date();

				// Create friend database entry
				Friends friendsDB = new Friends();
				FriendsId friendsId = new FriendsId();
				friendsId.setUserId(requestedUser.getUserId());
				friendsId.setFriendId(requestingUser.getUserId());
				friendsDB.setId(friendsId);
				friendsDB.setUsersByUserId(requestedUser);
				friendsDB.setUsersByFriendId(requestingUser);
				friendsDB.setActive(1); // True
				friendsDB.setFriendDate(friendDate);
				if (save) getCurrentSession().saveOrUpdate(friendsDB);

				// Create inverse friend database entry
				Friends friendsDB2 = new Friends();
				FriendsId friendsId2 = new FriendsId();
				friendsId2.setUserId(requestingUser.getUserId());
				friendsId2.setFriendId(requestedUser.getUserId());
				friendsDB2.setId(friendsId2);
				friendsDB2.setUsersByUserId(requestingUser);
				friendsDB2.setUsersByFriendId(requestedUser);
				friendsDB2.setActive(1); // True
				friendsDB2.setFriendDate(friendDate);
				if (save) getCurrentSession().saveOrUpdate(friendsDB2);
				respond = "Accepted";
			}
			LOG.info("Successfully responded to friend request!");
			return respond;
		}

		LOG.info("Error responding to friend request");
		return "Error responding to friend request";
	}

	public Wrapper retrieveFriendRequestsFor(UserPojo user) {
		Wrapper wrapper = new Wrapper();
		ArrayList<FriendRequestPojo> friendRequests = new ArrayList<>();

		Query q = getCurrentSession().createQuery(
				"from FriendRequests as fr where fr.usersByUserId.username = :username and fr.active = 1");
		q.setString("username", user.getUsername());
		List<FriendRequests> friendRequestList = q.list();

		// Populate FriendRequestPojoWrapper with results and convert them to
		// their POJO equivalents
		for (FriendRequests fr : friendRequestList) {
			FriendRequestPojo friendRequestPojo = new FriendRequestPojo();
			UserPojo friend = new UserPojo();

			friend.setUserId(fr.getUsersByFriendId().getUserId());
			friend.setUsername(fr.getUsersByFriendId().getUsername());
			friend.setFirstName(fr.getUsersByFriendId().getFirstName());
			friend.setLastName(fr.getUsersByFriendId().getLastName());
			friend.setRegisteredDate(fr.getUsersByFriendId().getRegisteredDate());
			friend.setActive((fr.getUsersByFriendId().getActive() == 0) ? false : true);

			friendRequestPojo.setRequestId(fr.getId().getFriendRequestId());
			friendRequestPojo.setRequestee(user);
			friendRequestPojo.setRequestor(friend);
			friendRequestPojo.setFriendRequestDate(fr.getFriendRequestDate());
			friendRequestPojo.setActive(true);

			friendRequests.add(friendRequestPojo);
			LOG.debug("Friend request added to wrapper");
		}
		LOG.debug("Sending FriendRequestPojoWrapper");
		wrapper.setFriendRequests(friendRequests);
		return wrapper;
	}

	public Wrapper retrieveFriendsFor(UserPojo user) {
		Wrapper wrapper = new Wrapper();
		ArrayList<UserPojo> users = new ArrayList<>();

		Query q = getCurrentSession().createQuery("from Users where username = :uname and active = 1");
		q.setParameter("uname", user.getUsername());

		if (!q.list().isEmpty()) {
			Users userDB = (Users) q.list().get(0);

			Query q2 = getCurrentSession().createQuery(
					"from Friends as friends where friends.usersByUserId.userId = :userId and active = 1");
			q2.setString("userId", String.valueOf(userDB.getUserId()));
			if (!q2.list().isEmpty()) {
				List<Friends> friendsList = q2.list();
				for (Friends f : friendsList) {
					q = getCurrentSession().createQuery("from Users where username = :uname and active = 1");
					q.setParameter("uname", f.getUsersByFriendId().getUsername());

					if (!q.list().isEmpty()) {
						Users friendDB = (Users) q.list().get(0);

						UserPojo userPojo = new UserPojo();
						userPojo.setUserId(friendDB.getUserId());
						userPojo.setUsername(friendDB.getUsername());
						userPojo.setFirstName(friendDB.getFirstName());
						userPojo.setLastName(friendDB.getLastName());
						userPojo.setRegisteredDate(friendDB.getRegisteredDate());
						userPojo.setActive(true);
						users.add(userPojo);
						LOG.debug("Adding " + userPojo.getUsername() + " to UserPojoWrapper");
					}
				}
			}
		}
		LOG.debug("Sending UserPojoWrapper");
		wrapper.setUsers(users);
		return wrapper;
	}

	public String retrieveFriendStatus(FriendRequestPojo friendRequest) {
		Query q = getCurrentSession()
				.createQuery(
						"from Friends as friends where friends.usersByUserId.username = :username and friends.usersByFriendId.username = :friendusername and friends.active = 1");
		q.setString("username", friendRequest.getRequestee().getUsername());
		q.setString("friendusername", friendRequest.getRequestor().getUsername());

		// If the list is empty, then they are not already friends, so check if
		// there is already a friend request pending
		if (q.list().isEmpty()) {
			q = getCurrentSession()
					.createQuery(
							"from FriendRequests as fr where fr.usersByUserId.username = :username and fr.usersByFriendId.username = :friendusername and fr.active = 1");
			q.setString("username", friendRequest.getRequestee().getUsername());
			q.setString("friendusername", friendRequest.getRequestor().getUsername());

			// If there is no friend request, check if there is a friend request
			// initiated by the other user
			if (q.list().isEmpty()) {
				q = getCurrentSession()
						.createQuery(
								"from FriendRequests as fr where fr.usersByUserId.username = :username and fr.usersByFriendId.username = :friendusername and fr.active = 1");
				q.setString("username", friendRequest.getRequestee().getUsername());
				q.setString("friendusername", friendRequest.getRequestor().getUsername());

				// If there are no friend requests in either direction, return
				// "Not friends"
				if (q.list().isEmpty()) { return "Add Friend"; }

				// There is a pending friend request in the opposite direction
				return "Pending";
			}

			// There is a pending friend request
			return "Pending";
		}

		return "Friends";
	}

	public String removeFriend(FriendRequestPojo removal, boolean save) {
		// Find the first Friend entry
		Query q = getCurrentSession()
				.createQuery(
						"from Friends as friends where friends.usersByUserId.username = :username and friends.usersByFriendId.username = :friendusername and friends.active = 1");
		q.setString("username", removal.getRequestee().getUsername());
		q.setString("friendusername", removal.getRequestor().getUsername());

		// If the list is empty, then they are not already friends, so check if
		// there is already a friend request pending
		if (!q.list().isEmpty()) {
			Friends notFriends = (Friends) q.list().get(0);
			notFriends.setActive(0);

			if (save) getCurrentSession().saveOrUpdate(notFriends);
			// Find the other Friend entry, which is reversed
			Query q2 = getCurrentSession()
					.createQuery(
							"from Friends as friends where friends.usersByUserId.username = :username and friends.usersByFriendId.username = :friendusername and friends.active = 1");
			q2.setString("username", removal.getRequestor().getUsername());
			q2.setString("friendusername", removal.getRequestee().getUsername());

			if (!q2.list().isEmpty()) {
				Friends notFriends2 = (Friends) q2.list().get(0);
				notFriends2.setActive(0);
				if (save) getCurrentSession().saveOrUpdate(notFriends);
				LOG.info("Friend removed!");
				return "Friend removed!";
			}
		}
		LOG.info("Users aren't friends");
		return "Users aren't friends";
	}

	public String ignoreUser(IgnorePojo ignore, boolean save) {
		Ignores ignoreDB = new Ignores();

		// Find ignoring user and set it in ignoreDB
		Query q = getCurrentSession().createQuery("from Users where username = :uname  and active = 1");
		q.setParameter("uname", ignore.getIgnorerUser().getUsername());
		if (q.list().isEmpty()) return "Ignoring user doesn't exist";
		Users ignoringUser = (Users) q.list().get(0);
		ignoreDB.setUsersByUserId(ignoringUser);

		boolean ignoredIsUser;
		String ignoreQuery;
		Users ignoredUser = null;
		Pages ignoredPage = null;
		// Determine if ignored is a user or a page and set it in ignoreDB
		if (ignore.getIgnoreeUser() != null && ignore.getIgnoreeUser().getUsername() != null
				&& !ignore.getIgnoreeUser().getUsername().equals("")) {
			Query q2 = getCurrentSession().createQuery("from Users where username = :uname  and active = 1");
			q2.setParameter("uname", ignore.getIgnoreeUser().getUsername());
			if (q2.list().isEmpty()) return "Ignored user doesn't exist";
			ignoredUser = (Users) q.list().get(0);
			ignoreDB.setUsersByIgnoredUserId(ignoredUser);
			ignoreQuery = " and ignores.usersByIgnoredUserId.username = :igusername";
			ignoredIsUser = true;
		} else if (ignore.getIgnoreePage() != null && ignore.getIgnoreePage().getTitle() != null
				&& !ignore.getIgnoreePage().getTitle().equals("")) {
			Query q2 = getCurrentSession().createQuery("from Pages where title = :title  and active = 1");
			q2.setParameter("title", ignore.getIgnoreePage().getTitle());
			if (q2.list().isEmpty()) return "Ignored page doesn't exist";
			ignoredPage = (Pages) q.list().get(0);
			ignoreDB.setPages(ignoredPage);
			ignoreQuery = " and ignores.pages.title = :igtitle";
			ignoredIsUser = false;
		} else {
			LOG.info("No ignored user or ignored page");
			return "No ignored user or ignored page";
		}

		// Determine if ignore already exists
		Query q2 = getCurrentSession().createQuery(
				"from Ignores as ignores where ignores.usersByUserId.username = :username and ignores.active = 1"
						+ ignoreQuery);
		q2.setString("userId", ignoringUser.getUsername());
		if (ignoredIsUser) {
			q2.setString("igusername", ignoredUser.getUsername());
		} else {
			q2.setString("igtitle", ignoredPage.getTitle());
		}

		if (!q2.list().isEmpty()) {
			LOG.debug("Already ignored");
			return "Already ignored";
		}

		// Save ignore to database
		if (ignoringUser != null && (ignoredUser != null || ignoredPage != null)) {
			ignoreDB.setIgnoreDate(new Date());
			ignoreDB.setActive(1);

			if (save) getCurrentSession().save(ignoreDB);

			String result;
			if (ignoredIsUser) {
				result = ignoringUser.getUsername() + " is ignoring " + ignoredUser.getUsername();
			} else {
				result = ignoringUser.getUsername() + " is ignoring " + ignoredPage.getTitle();
			}
			LOG.info(result);
			return result;
		}
		LOG.info("Error ignoring");
		return "Error ignoring";
	}

	public String unignoreUser(IgnorePojo ignore, boolean save) {
		Ignores ignoreDB = new Ignores();

		boolean ignoredIsUser;
		String ignoreQuery;
		// Determine if ignored is a user or a page and set it in ignoreDB
		if (ignore.getIgnoreeUser() != null && ignore.getIgnoreeUser().getUsername() != null
				&& !ignore.getIgnoreeUser().getUsername().equals("")) {
			Query q2 = getCurrentSession().createQuery("from Users where username = :uname  and active = 1");
			q2.setParameter("uname", ignore.getIgnoreeUser().getUsername());
			if (q2.list().isEmpty()) return "Ignored user doesn't exist";
			ignoreQuery = " and ignores.usersByIgnoredUserId.username = :igusername";
			ignoredIsUser = true;
		} else if (ignore.getIgnoreePage() != null && ignore.getIgnoreePage().getTitle() != null
				&& !ignore.getIgnoreePage().getTitle().equals("")) {
			Query q2 = getCurrentSession().createQuery("from Pages where title = :title  and active = 1");
			q2.setParameter("title", ignore.getIgnoreePage().getTitle());
			if (q2.list().isEmpty()) return "Ignored page doesn't exist";
			ignoreQuery = " and ignores.pages.title = :igtitle";
			ignoredIsUser = false;
		} else {
			LOG.info("No ignored user or ignored page");
			return "No ignored user or ignored page";
		}

		// Determine if ignore already exists
		Query q2 = getCurrentSession().createQuery(
				"from Ignores as ignores where ignores.usersByUserId.username = :username and ignores.active = 1"
						+ ignoreQuery);
		q2.setString("userId", ignore.getIgnorerUser().getUsername());
		if (ignoredIsUser) {
			q2.setString("igusername", ignore.getIgnoreeUser().getUsername());
		} else {
			q2.setString("igtitle", ignore.getIgnoreePage().getTitle());
		}

		if (q2.list().isEmpty()) {
			LOG.debug("Not ignored");
			return "Not ignored";
		}

		// Save ignore to database
		if (!q2.list().isEmpty()) {
			ignoreDB = (Ignores) q2.list().get(0);
			ignoreDB.setActive(0);

			if (save) getCurrentSession().saveOrUpdate(ignoreDB);

			String result;
			if (ignoredIsUser) {
				result = ignore.getIgnorerUser().getUsername() + " is no longer ignoring "
						+ ignore.getIgnoreeUser().getUsername();
			} else {
				result = ignore.getIgnorerUser().getUsername() + " is no longer ignoring "
						+ ignore.getIgnoreePage().getTitle();
			}
			LOG.info(result);
			return result;
		}
		LOG.info("Error unignoring");
		return "Error unignoring";
	}

	public String submitWallPost(WallPostPojo post, boolean save) {
		Query q;
		Users wallUser = null;
		Users postingUser = null;
		Pages wallPage = null;
		Pages postingPage = null;
		boolean wallOwnerIsUser;
		boolean posterIsUser;

		// Find wall's owner, and determine if it is a user or a page
		if ((post.getUser() != null) && (post.getUser().getUsername()) != null
				&& !(post.getUser().getUsername().equals(""))) {
			q = getCurrentSession().createQuery("from Users where username = :uname and active = 1");
			q.setParameter("uname", post.getUser().getUsername());
			wallUser = (Users) q.list().get(0);
			wallOwnerIsUser = true;
		} else if (post.getPage() != null && post.getPage().getTitle() != null && !post.getPage().getTitle().equals("")) {
			q = getCurrentSession().createQuery("from Pagesike where title = :title and active = 1");
			q.setParameter("title", post.getPage().getTitle());
			wallPage = (Pages) q.list().get(0);
			wallOwnerIsUser = false;
		} else {
			LOG.info("Wall owner doesn't exist");
			return "Wall owner doesn't exist";
		}

		// Find poster, and determine if it is a user or a page
		if (post.getFriend() != null && post.getFriend().getUsername() != null
				&& !post.getFriend().getUsername().equals("")) {
			q = getCurrentSession().createQuery("from Users where username = :uname and active = 1");
			q.setParameter("uname", post.getFriend().getUsername());
			postingUser = (Users) q.list().get(0);
			posterIsUser = true;
		} else if (post.getPostingPage() != null && post.getPostingPage().getTitle() != null
				&& !post.getPostingPage().getTitle().equals("")) {
			q = getCurrentSession().createQuery("from Pages where title = :title and active = 1");
			q.setParameter("title", post.getPostingPage().getTitle());
			postingPage = (Pages) q.list().get(0);
			posterIsUser = false;
		} else {
			LOG.info("Poster doesn't exist");
			return "Poster doesn't exist";
		}

		// Create database entry
		WallPosts wallPostDB = new WallPosts();
		wallPostDB.setMessage(post.getMessage());
		if (wallOwnerIsUser) {
			wallPostDB.setUsersByUserId(wallUser);
		} else {
			wallPostDB.setPagesByPageId(wallPage);
		}
		if (posterIsUser) {
			wallPostDB.setUsersByFriendId(postingUser);
		} else {
			wallPostDB.setPagesByPostingPageId(postingPage);
		}
		wallPostDB.setPostedDate(post.getPostedDate());
		wallPostDB.setActive(1);

		if (save) getCurrentSession().save(wallPostDB);

		LOG.info("Success!");
		return "Success!";
	}

	public Wrapper retrieveWallPostsFor(UserPojo user) {
		Wrapper wrapper = new Wrapper();
		ArrayList<WallPostPojo> posts = new ArrayList<>();

		// Find wall's owner
		Query q = getCurrentSession().createQuery("from Users where username = :uname and active = 1");
		q.setParameter("uname", user.getUsername());

		// If the user exists...
		if (!q.list().isEmpty()) {
			// wallUser = (Users) q.list().get(0);

			// Find list of users/pages that the currently signed in user has
			// ignored
			q = getCurrentSession().createQuery(
					"from Ignores as ignores where ignores.usersByUserId.username = :uname and ignores.active = 1");

			// The UserPojo parameter's username refers to the username of the
			// user whose page is being viewed, and the password refers the the
			// currently signed in user's username. We want to populate ignores
			// based on who is signed in, not whose page is being viewed
			q.setParameter("uname", user.getPassword());
			List<Ignores> ignoreList = q.list();

			// Clear password of UserPojo so as not to confuse client-side
			user.setPassword(null);

			// Remove posts from ignored users/pages from the upcoming query for
			// wall posts
			String ignoreQuery = "";
			for (Ignores ignore : ignoreList) {
				// Determine if the ignoree is a user or a page
				if (ignore.getUsersByIgnoredUserId() != null && ignore.getUsersByIgnoredUserId().getUsername() != null
						&& !ignore.getUsersByIgnoredUserId().getUsername().equals("")) {
					ignoreQuery += " and wp.usersByFriendId.username <> "
							+ ignore.getUsersByIgnoredUserId().getUsername();
				} else if (ignore.getPages() != null && ignore.getPages().getTitle() != null
						&& !ignore.getPages().getTitle().equals("")) {
					ignoreQuery += " and wp.pagesByPostingPageId.title <> " + ignore.getPages().getTitle();
				}
			}

			// Get wall posts
			q = getCurrentSession().createQuery(
					"from WallPosts as wp where wp.usersByUserId.username = :uname and wp.usersByUserId.active = 1 and wp.active = 1"
							+ ignoreQuery);
			q.setParameter("uname", user.getUsername());
			((ArrayList<WallPosts>) q.list())
					.forEach((wp) -> {
						WallPostPojo post = new WallPostPojo();
						UserPojo friend = new UserPojo();
						PagePojo postingPage = new PagePojo();
						boolean posterIsUser;
						boolean posterExists = true;

						// Determine if the poster is a user or a page, and
						// create the appropriate pojo
						if (wp.getUsersByFriendId() != null && wp.getUsersByFriendId().getUsername() != null
								&& !wp.getUsersByFriendId().getUsername().equals("")) {
							friend.setUserId(wp.getUsersByFriendId().getUserId());
							friend.setUsername(wp.getUsersByFriendId().getUsername());
							friend.setFirstName(wp.getUsersByFriendId().getFirstName());
							friend.setLastName(wp.getUsersByFriendId().getLastName());
							friend.setRegisteredDate(wp.getUsersByFriendId().getRegisteredDate());
							friend.setActive((wp.getUsersByFriendId().getActive() == 0) ? false : true);
							posterIsUser = true;
						} else if (wp.getPagesByPostingPageId() != null
								&& wp.getPagesByPostingPageId().getTitle() != null
								&& !wp.getPagesByPostingPageId().getTitle().equals("")) {
							postingPage.setPageId(wp.getPagesByPageId().getPageId());
							postingPage.setTitle(wp.getPagesByPageId().getTitle());
							postingPage.setDescription(wp.getPagesByPageId().getDescription());
							postingPage.setActive((wp.getPagesByPageId().getActive() == 0) ? false : true);
							postingPage.setCreatedDate(wp.getPagesByPageId().getCreatedDate());

							UserPojo owner = new UserPojo();
							owner.setUserId(wp.getPagesByPageId().getUsers().getUserId());
							owner.setUsername(wp.getPagesByPageId().getUsers().getUsername());
							owner.setFirstName(wp.getPagesByPageId().getUsers().getFirstName());
							owner.setLastName(wp.getPagesByPageId().getUsers().getLastName());
							owner.setRegisteredDate(wp.getPagesByPageId().getUsers().getRegisteredDate());
							owner.setActive((wp.getPagesByPageId().getUsers().getActive() == 0) ? false : true);

							postingPage.setOwner(owner);
							posterIsUser = false;
						} else {
							// If the poster isn't a page or a user, it must not
							// exist
							posterIsUser = false;
							posterExists = false;
						}

						if (posterExists) {
							post.setUser(user);
							if (posterIsUser) {
								post.setFriend(friend);
							} else {
								post.setPostingPage(postingPage);
							}
							post.setMessage(wp.getMessage());
							post.setPostedDate(wp.getPostedDate());
							post.setWallPostId(wp.getWallPostId());
							post.setActive(true);
							Query q2 = getCurrentSession()
									.createQuery(
											"from WallPostLikes as wpl where wpl.wallPosts.wallPostId = :postId and wpl.active = 1");
							q2.setParameter("postId", wp.getWallPostId());
							((ArrayList<WallPostLikes>) q2.list()).forEach((wpl) -> {
								WallPostLikePojo wplPojo = new WallPostLikePojo();
								UserPojo likingUser = new UserPojo();
								PagePojo likingPage = new PagePojo();
								boolean likingIsUser;
								boolean likingExists = true;

								if (wpl.getUsers() != null && wpl.getUsers().getUsername() != null
										&& !wpl.getUsers().getUsername().equals("")) {
									likingUser.setUserId(wpl.getUsers().getUserId());
									likingUser.setUsername(wpl.getUsers().getUsername());
									likingUser.setFirstName(wpl.getUsers().getFirstName());
									likingUser.setLastName(wpl.getUsers().getLastName());
									likingUser.setRegisteredDate(wpl.getUsers().getRegisteredDate());
									likingUser.setActive(true);
									likingIsUser = true;
								} else if (wpl.getUsers() != null && wpl.getUsers().getUsername() != null
										&& !wpl.getUsers().getUsername().equals("")) {
									UserPojo pageOwner = new UserPojo();
									pageOwner.setUserId(wpl.getPages().getUsers().getUserId());
									pageOwner.setUsername(wpl.getPages().getUsers().getUsername());
									pageOwner.setFirstName(wpl.getPages().getUsers().getFirstName());
									pageOwner.setLastName(wpl.getPages().getUsers().getLastName());
									pageOwner.setRegisteredDate(wpl.getPages().getUsers().getRegisteredDate());
									pageOwner.setActive(true);

									likingPage.setPageId(wpl.getPages().getPageId());
									likingPage.setOwner(pageOwner);
									likingPage.setTitle(wpl.getPages().getTitle());
									likingPage.setDescription(wpl.getPages().getDescription());
									likingPage.setCreatedDate(wpl.getPages().getCreatedDate());
									likingPage.setActive(true);

									likingIsUser = false;
								} else {
									likingIsUser = false;
									likingExists = false;
								}

								if (likingExists) {
									wplPojo.setWallPostLikeId(wpl.getWallPostLikeId());
									wplPojo.setLikeDate(wpl.getLikeDate());
									wplPojo.setActive(true);

									if (likingIsUser) {
										wplPojo.setUser(likingUser);
									} else {
										wplPojo.setPage(likingPage);
									}
									post.getLikes().add(wplPojo);
								}
							});
							posts.add(post);
						}
					});
		}
		wrapper.setWallPosts(posts);
		return wrapper;
	}

	public Wrapper retrieveWallPostsFor(PagePojo page) {
		Wrapper wrapper = new Wrapper();
		ArrayList<WallPostPojo> posts = new ArrayList<>();

		// Find wall's owner
		Query q = getCurrentSession().createQuery("from Pages where title = :title and active = 1");
		q.setParameter("title", page.getTitle());

		// If the user exists...
		if (!q.list().isEmpty()) {

			// Find list of users/pages that the currently signed in user has
			// ignored
			q = getCurrentSession().createQuery(
					"from Ignores as ignores where ignores.usersByUserId.username = :uname and ignores.active = 1");

			// The PagePojo's title refers to the title of the wall owning page,
			// and the PagePojo's description refers to the username of the user
			// who is currently signed in. We want to ignore posts based on the
			// ignores of the currently signed in user
			q.setParameter("uname", page.getDescription());
			List<Ignores> ignoreList = q.list();

			// Remove posts from ignored users/pages from the upcoming query for
			// wall posts
			String ignoreQuery = "";
			for (Ignores ignore : ignoreList) {
				// Determine if the ignoree is a user or a page
				if (ignore.getUsersByIgnoredUserId() != null && ignore.getUsersByIgnoredUserId().getUsername() != null
						&& !ignore.getUsersByIgnoredUserId().getUsername().equals("")) {
					ignoreQuery += " and wp.usersByFriendId.username <> "
							+ ignore.getUsersByIgnoredUserId().getUsername();
				} else if (ignore.getPages() != null && ignore.getPages().getTitle() != null
						&& !ignore.getPages().getTitle().equals("")) {
					ignoreQuery += " and wp.pagesByPostingPageId.title <> " + ignore.getPages().getTitle();
				}
			}

			// Get wall posts
			q = getCurrentSession().createQuery(
					"from WallPosts as wp where wp.pagesByPageId.title = :title and wp.pagesByPageId.active = 1 and wp.active = 1"
							+ ignoreQuery);
			q.setParameter("title", page.getTitle());
			((ArrayList<WallPosts>) q.list())
					.forEach((wp) -> {
						WallPostPojo post = new WallPostPojo();
						UserPojo friend = new UserPojo();
						PagePojo postingPage = new PagePojo();
						boolean posterIsUser;
						boolean posterExists = true;

						// Determine if the poster is a user or a page, and
						// create the appropriate pojo
						if (wp.getUsersByFriendId() != null && wp.getUsersByFriendId().getUsername() != null
								&& !wp.getUsersByFriendId().getUsername().equals("")) {
							friend.setUserId(wp.getUsersByFriendId().getUserId());
							friend.setUsername(wp.getUsersByFriendId().getUsername());
							friend.setFirstName(wp.getUsersByFriendId().getFirstName());
							friend.setLastName(wp.getUsersByFriendId().getLastName());
							friend.setRegisteredDate(wp.getUsersByFriendId().getRegisteredDate());
							friend.setActive((wp.getUsersByFriendId().getActive() == 0) ? false : true);
							posterIsUser = true;
						} else if (wp.getPagesByPostingPageId() != null
								&& wp.getPagesByPostingPageId().getTitle() != null
								&& !wp.getPagesByPostingPageId().getTitle().equals("")) {
							postingPage.setPageId(wp.getPagesByPageId().getPageId());
							postingPage.setTitle(wp.getPagesByPageId().getTitle());
							postingPage.setDescription(wp.getPagesByPageId().getDescription());
							postingPage.setActive((wp.getPagesByPageId().getActive() == 0) ? false : true);
							postingPage.setCreatedDate(wp.getPagesByPageId().getCreatedDate());

							UserPojo owner = new UserPojo();
							owner.setUserId(wp.getPagesByPageId().getUsers().getUserId());
							owner.setUsername(wp.getPagesByPageId().getUsers().getUsername());
							owner.setFirstName(wp.getPagesByPageId().getUsers().getFirstName());
							owner.setLastName(wp.getPagesByPageId().getUsers().getLastName());
							owner.setRegisteredDate(wp.getPagesByPageId().getUsers().getRegisteredDate());
							owner.setActive((wp.getPagesByPageId().getUsers().getActive() == 0) ? false : true);

							postingPage.setOwner(owner);
							posterIsUser = false;
						} else {
							// If the poster isn't a page or a user, it must not
							// exist
							posterIsUser = false;
							posterExists = false;
						}

						if (posterExists) {
							post.setPage(page);
							if (posterIsUser) {
								post.setFriend(friend);
							} else {
								post.setPostingPage(postingPage);
							}
							post.setMessage(wp.getMessage());
							post.setPostedDate(wp.getPostedDate());
							post.setWallPostId(wp.getWallPostId());
							post.setActive(true);
							Query q2 = getCurrentSession()
									.createQuery(
											"from WallPostLikes as wpl where wpl.wallPosts.wallPostId = :postId and wpl.active = 1");
							q2.setParameter("postId", wp.getWallPostId());
							((ArrayList<WallPostLikes>) q2.list()).forEach((wpl) -> {
								WallPostLikePojo wplPojo = new WallPostLikePojo();
								UserPojo likingUser = new UserPojo();
								PagePojo likingPage = new PagePojo();
								boolean likingIsUser;
								boolean likingExists = true;

								if (wpl.getUsers() != null && wpl.getUsers().getUsername() != null
										&& !wpl.getUsers().getUsername().equals("")) {
									likingUser.setUserId(wpl.getUsers().getUserId());
									likingUser.setUsername(wpl.getUsers().getUsername());
									likingUser.setFirstName(wpl.getUsers().getFirstName());
									likingUser.setLastName(wpl.getUsers().getLastName());
									likingUser.setRegisteredDate(wpl.getUsers().getRegisteredDate());
									likingUser.setActive(true);
									likingIsUser = true;
								} else if (wpl.getUsers() != null && wpl.getUsers().getUsername() != null
										&& !wpl.getUsers().getUsername().equals("")) {
									UserPojo pageOwner = new UserPojo();
									pageOwner.setUserId(wpl.getPages().getUsers().getUserId());
									pageOwner.setUsername(wpl.getPages().getUsers().getUsername());
									pageOwner.setFirstName(wpl.getPages().getUsers().getFirstName());
									pageOwner.setLastName(wpl.getPages().getUsers().getLastName());
									pageOwner.setRegisteredDate(wpl.getPages().getUsers().getRegisteredDate());
									pageOwner.setActive(true);

									likingPage.setPageId(wpl.getPages().getPageId());
									likingPage.setOwner(pageOwner);
									likingPage.setTitle(wpl.getPages().getTitle());
									likingPage.setDescription(wpl.getPages().getDescription());
									likingPage.setCreatedDate(wpl.getPages().getCreatedDate());
									likingPage.setActive(true);

									likingIsUser = false;
								} else {
									likingIsUser = false;
									likingExists = false;
								}

								if (likingExists) {
									wplPojo.setWallPostLikeId(wpl.getWallPostLikeId());
									wplPojo.setLikeDate(wpl.getLikeDate());
									wplPojo.setActive(true);

									if (likingIsUser) {
										wplPojo.setUser(likingUser);
									} else {
										wplPojo.setPage(likingPage);
									}
									post.getLikes().add(wplPojo);
								}
							});
							posts.add(post);
						}
					});
		}
		wrapper.setWallPosts(posts);
		return wrapper;
	}

	public String savePage(PagePojo page, boolean save) {
		// Checks if a page with the same title exists in the database
		Query q = getCurrentSession().createQuery("from Pages where title = :title and active = 1");
		q.setParameter("title", page.getTitle());
		List<Pages> pageDatabaseList = q.list();

		// If the page doesn't exist already, create a new one and save it to
		// the database
		if (pageDatabaseList.isEmpty()) {
			Pages saveNewPage = new Pages();

			// Find the database entry for the owner
			Query q2 = getCurrentSession().createQuery("from Users where username = :uname and active = 1");
			q2.setParameter("uname", page.getOwner().getUsername());

			if (!q2.list().isEmpty()) {
				Users pageOwner = (Users) q2.list().get(0);

				saveNewPage.setTitle(page.getTitle());
				saveNewPage.setUsers(pageOwner);
				saveNewPage.setDescription(page.getDescription());
				saveNewPage.setCreatedDate(new Date());
				saveNewPage.setActive(1); // True

				if (save) getCurrentSession().save(saveNewPage);
				LOG.info("Page created successfully!");
				return "Page created successfully!";
			}
			LOG.info("Could not find owner");
			return "Could not find owner";
		} else {
			// Page already exists
			LOG.info("Page already exists");
			return "Page already exists";
		}
	}

	public Wrapper retrievePages(String search) {
		Wrapper wrapper = new Wrapper();
		LOG.info("Received search: " + search);
		Query q;
		List<Pages> searchedPage;

		q = getCurrentSession().createQuery("from Pages where title like :search and active = 1");
		q.setParameter("search", search + "%");
		searchedPage = q.list();

		LOG.info("Immediately after q.list() searchedPage.size()=" + searchedPage.size());

		// For each Pages in the search results, create a PagePojo
		((ArrayList<Pages>) q.list()).forEach((p) -> {
			PagePojo page = new PagePojo();
			UserPojo owner = new UserPojo();
			owner.setUserId(p.getUsers().getUserId());
			owner.setUsername(p.getUsers().getUsername());
			owner.setFirstName(p.getUsers().getFirstName());
			owner.setLastName(p.getUsers().getLastName());
			owner.setRegisteredDate(p.getUsers().getRegisteredDate());
			owner.setActive(true);

			page.setPageId(p.getPageId());
			page.setTitle(p.getTitle());
			page.setOwner(owner);
			page.setDescription(p.getDescription());
			page.setActive((p.getActive() == 0) ? false : true);

			// TODO Remove the following comments once we've determined for sure
			// that they are unnecessary

				// Originally, upon fetching a page, it would also fetch that
				// page's
				// likes, but if those likes were pages, then we would need to
				// get
				// that page's likes too, and pretty soon we would be pulling
				// way
				// too much data to handle, so instead there is a separate
				// function
				// for retrieving likes, similar to the one that retrieves
				// friends.
				// I haven't deleted the code below yet as it is possible that
				// we
				// may decide to go back to this way

				// // Query a list of likes for this page
				// Query q2 = getCurrentSession().createQuery(
				// "from Likes as likes where likes.pagesByPageId.title = :title and likes.pagesByPageId.active = 1 and likes.active = 1");
				// q2.setParameter("title", p.getTitle());
				// ((ArrayList<Likes>)q2.list()).forEach((l) -> {
				// LikePojo likePojo = new LikePojo();
				// likePojo.setLikeId(l.getLikeId());
				// likePojo.setLikeDate(l.getLikeDate());
				// likePojo.setActive(true);
				//
				// // If a page is liking, set the liking page
				// if (l.getPagesByOtherId() != null) {
				// PagePojo likingPage = new PagePojo();
				// likingPage.setPageId(l.getPagesByOtherId().getPageId());
				// likingPage.setTitle(l.getPagesByOtherId().getTitle());
				// likingPage.setDescription(l.getPagesByOtherId().getDescription());
				// likingPage.setCreatedDate(l.getPagesByOtherId().getCreatedDate());
				// likingPage.setActive(true);
				//
				// UserPojo pageOwner = new UserPojo();
				// pageOwner.setUserId(l.getPagesByOtherId().getUsers().getUserId());
				// pageOwner.setUsername(l.getPagesByOtherId().getUsers().getUsername());
				// pageOwner.setFirstName(l.getPagesByOtherId().getUsers().getFirstName());
				// pageOwner.setLastName(l.getPagesByOtherId().getUsers().getLastName());
				// pageOwner.setRegisteredDate(l.getPagesByOtherId().getUsers().getRegisteredDate());
				// pageOwner.setActive(true);
				// likingPage.setOwner(pageOwner);
				//
				// likePojo.setLikingPage(likingPage);
				//
				// // Otherwise if a user is liking, set the liking user
				// }else if (l.getUsers() != null) {
				// UserPojo likingUser = new UserPojo();
				// likingUser.setUserId(l.getUsers().getUserId());
				// likingUser.setUsername(l.getUsers().getUsername());
				// likingUser.setFirstName(l.getUsers().getFirstName());
				// likingUser.setLastName(l.getUsers().getLastName());
				// likingUser.setRegisteredDate(l.getUsers().getRegisteredDate());
				// likingUser.setActive(true);
				//
				// likePojo.setLikingUser(likingUser);
				// }
				//
				// page.getLikes().add(likePojo);
				// });

				wrapper.getPages().add(page);
				LOG.info("Finished adding page to list: " + page);
			});

		LOG.info("Sending wrapper containing " + wrapper.getPages().size() + " PagePojos");
		return wrapper;
	}

	public PagePojo retrieveLikes(PagePojo page) {
		Query q = getCurrentSession().createQuery("from Pages where title = :title and active = 1");
		q.setParameter("title", page.getTitle());
		if (!q.list().isEmpty()) {
			// Query a list of likes for this page
			Query q2 = getCurrentSession()
					.createQuery(
							"from Likes as likes where likes.pagesByPageId.title = :title and likes.pagesByPageId.active = 1 and likes.active = 1");
			q2.setParameter("title", page.getTitle());
			((ArrayList<Likes>) q2.list()).forEach((l) -> {
				LikePojo likePojo = new LikePojo();
				likePojo.setLikeId(l.getLikeId());
				likePojo.setLikeDate(l.getLikeDate());
				likePojo.setActive(true);

				// If a page is liking, set the liking page
					if (l.getPagesByOtherId() != null) {
						PagePojo likingPage = new PagePojo();
						likingPage.setPageId(l.getPagesByOtherId().getPageId());
						likingPage.setTitle(l.getPagesByOtherId().getTitle());
						likingPage.setDescription(l.getPagesByOtherId().getDescription());
						likingPage.setCreatedDate(l.getPagesByOtherId().getCreatedDate());
						likingPage.setActive(true);

						UserPojo pageOwner = new UserPojo();
						pageOwner.setUserId(l.getPagesByOtherId().getUsers().getUserId());
						pageOwner.setUsername(l.getPagesByOtherId().getUsers().getUsername());
						pageOwner.setFirstName(l.getPagesByOtherId().getUsers().getFirstName());
						pageOwner.setLastName(l.getPagesByOtherId().getUsers().getLastName());
						pageOwner.setRegisteredDate(l.getPagesByOtherId().getUsers().getRegisteredDate());
						pageOwner.setActive(true);
						likingPage.setOwner(pageOwner);

						likePojo.setLikingPage(likingPage);

						// Otherwise if a user is liking, set the liking user
					} else if (l.getUsers() != null) {
						UserPojo likingUser = new UserPojo();
						likingUser.setUserId(l.getUsers().getUserId());
						likingUser.setUsername(l.getUsers().getUsername());
						likingUser.setFirstName(l.getUsers().getFirstName());
						likingUser.setLastName(l.getUsers().getLastName());
						likingUser.setRegisteredDate(l.getUsers().getRegisteredDate());
						likingUser.setActive(true);

						likePojo.setLikingUser(likingUser);
					}

					page.getLikes().add(likePojo);
				});
		}

		LOG.info("Returning page with " + page.getLikes().size() + " likes.");
		return page;
	}

	public String likePage(PagePojo page, boolean save) {
		if (page.getLikes() != null && !page.getLikes().isEmpty()) {
			Query q = getCurrentSession().createQuery("from Pages where title = :title and active = 1");
			q.setParameter("title", page.getTitle());

			// If the page being liked exists in the database then query if this
			// like already exists
			if (!q.list().isEmpty()) {
				Pages likedPage = (Pages) q.list().get(0);
				// Determine whether the like is by a user or a page, and refine
				// the like query based on this condition
				LikePojo pendingLike = page.getLikes().get(0);
				String likeQuery;
				boolean likingIsUser;
				if (pendingLike.getLikingUser() != null && pendingLike.getLikingUser().getUsername() != null
						&& !pendingLike.getLikingUser().getUsername().equals("")) {
					likeQuery = " and likes.users.username = :likingUsername";
					likingIsUser = true;
				} else if (pendingLike.getLikingPage() != null && pendingLike.getLikingPage().getTitle() != null
						&& !pendingLike.getLikingPage().getTitle().equals("")) {
					likeQuery = " and likes.pagesByOtherId.title = :likingTitle";
					likingIsUser = false;
				} else {
					LOG.info("No likingUser or likingPage");
					return "No likingUser or likingPage";
				}

				Query q2 = getCurrentSession().createQuery(
						"from Likes as likes where likes.pagesByPageId.title = :title and likes.pagesByPageId.active = 1 and likes.active = 1"
								+ likeQuery);
				q2.setParameter("title", page.getTitle());

				if (likingIsUser) {
					q2.setParameter("likingUsername", pendingLike.getLikingUser().getUsername());
				} else {
					q2.setParameter("likingTitle", pendingLike.getLikingPage().getTitle());
				}

				// If the like doesn't already exist, create the Likes for it,
				// and save to database
				if (q2.list().isEmpty()) {
					Likes newLike = new Likes();
					newLike.setPagesByPageId(likedPage);
					newLike.setLikeDate(new Date());
					newLike.setActive(1);

					if (likingIsUser) {
						// Query database for the liking user, and set it to the
						// like
						q = getCurrentSession().createQuery("from Users where username = :uname and active = 1");
						q.setParameter("uname", pendingLike.getLikingUser().getUsername());
						if (!q.list().isEmpty()) {
							newLike.setUsers((Users) q.list().get(0));
						} else {
							LOG.info("Liking user doesn't exist");
							return "Liking user doesn't exist";
						}
					} else {
						// Query database for the liking page, and set it to the
						// like
						q = getCurrentSession().createQuery("from Pages where title = :title and active = 1");
						q.setParameter("title", pendingLike.getLikingPage().getTitle());
						if (!q.list().isEmpty()) {
							newLike.setPagesByOtherId((Pages) q.list().get(0));
						} else {
							LOG.info("Liking page doesn't exist");
							return "Liking page doesn't exist";
						}
					}

					if (save) getCurrentSession().save(newLike);
					LOG.info("Page liked successfully");
					return "Page liked successfully";
				} else {
					LOG.info("Like already exists");
					return "Like already exists";
				}
			}
			LOG.info("Page doesn't exist");
			return "Page doesn't exist";
		}
		LOG.info("No like information received");
		return "No like information received";
	}

	public String unlikePage(PagePojo page, boolean save) {
		if (page.getLikes() != null && !page.getLikes().isEmpty()) {
			Query q = getCurrentSession().createQuery("from Pages where title = :title and active = 1");
			q.setParameter("title", page.getTitle());

			// If the page being liked exists in the database then query if this
			// like already exists
			if (!q.list().isEmpty()) {
				Pages likedPage = (Pages) q.list().get(0);
				// Determine whether the like is by a user or a page, and refine
				// the like query based on this condition
				LikePojo pendingLike = page.getLikes().get(0);
				String likeQuery;
				boolean likingIsUser;
				if (pendingLike.getLikingUser() != null && pendingLike.getLikingUser().getUsername() != null
						&& !pendingLike.getLikingUser().getUsername().equals("")) {
					likeQuery = " and likes.users.username = :likingUsername";
					likingIsUser = true;
				} else if (pendingLike.getLikingPage() != null && pendingLike.getLikingPage().getTitle() != null
						&& !pendingLike.getLikingPage().getTitle().equals("")) {
					likeQuery = " and likes.pagesByOtherId.title = :likingTitle";
					likingIsUser = false;
				} else {
					LOG.info("No likingUser or likingPage");
					return "No likingUser or likingPage";
				}

				Query q2 = getCurrentSession().createQuery(
						"from Likes as likes where likes.pagesByPageId.title = :title and likes.pagesByPageId.active = 1 and likes.active = 1"
								+ likeQuery);
				q2.setParameter("title", page.getTitle());

				if (likingIsUser) {
					q2.setParameter("likingUsername", pendingLike.getLikingUser().getUsername());
				} else {
					q2.setParameter("likingTitle", pendingLike.getLikingPage().getTitle());
				}

				// If the like doesn't already exist, create the Likes for it,
				// and save to database
				if (!q2.list().isEmpty()) {
					Likes newLike = (Likes) q2.list().get(0);
					newLike.setActive(1);

					if (save) getCurrentSession().saveOrUpdate(newLike);
					LOG.info("Page unliked successfully");
					return "Page unliked successfully";
				} else {
					LOG.info("Like doesn't exists");
					return "Like doesn't exists";
				}
			}
			LOG.info("Page doesn't exist");
			return "Page doesn't exist";
		}
		LOG.info("No like information received");
		return "No like information received";
	}

	public String likeWallPost(WallPostPojo post, boolean save) {
		if (post.getLikes() != null && !post.getLikes().isEmpty()) {
			String sourceQuery;
			String destinationQuery;
			boolean sourceIsUser;
			boolean destinationIsUser;

			// Determines if the source is a user or a page, and adds it to the
			// query
			if (post.getUser() != null && post.getUser().getUsername() != null
					&& !post.getUser().getUsername().equals("")) {
				sourceQuery = " and usersByUserId.username = :susername";
				sourceIsUser = true;
			} else if (post.getPage() != null && post.getPage().getTitle() != null
					&& !post.getPage().getTitle().equals("")) {
				sourceQuery = " and pagesByPageId.title = :stitle";
				sourceIsUser = false;
			} else {
				LOG.info("No source found");
				return "No source found";
			}

			// Determines if the destination is a user or a page, and adds it to
			// the query
			if (post.getFriend() != null && post.getFriend().getUsername() != null
					&& !post.getFriend().getUsername().equals("")) {
				destinationQuery = " and usersByFriendId.username = :dusername";
				destinationIsUser = true;
			} else if (post.getPostingPage() != null && post.getPostingPage().getTitle() != null
					&& !post.getPostingPage().getTitle().equals("")) {
				destinationQuery = " and pagesByPostingPageId.title = :dtitle";
				destinationIsUser = false;
			} else {
				LOG.info("No destination found");
				return "No destination found";
			}

			Query q = getCurrentSession().createQuery(
					"from WallPosts where postedDate = :pdate and active = 1" + sourceQuery + destinationQuery);
			q.setDate("pdate", post.getPostedDate());
			if (sourceIsUser) {
				q.setParameter("susername", post.getUser().getUsername());
			} else {
				q.setParameter("stitle", post.getPage().getTitle());
			}
			if (destinationIsUser) {
				q.setParameter("dusername", post.getFriend().getUsername());
			} else {
				q.setParameter("dtitle", post.getPostingPage().getTitle());
			}

			// If the post being liked exists in the database then query if this
			// like already exists
			if (!q.list().isEmpty()) {
				WallPosts likedPost = (WallPosts) q.list().get(0);

				// Determine whether the like is by a user or a page, and refine
				// the like query based on this condition
				WallPostLikePojo pendingLike = post.getLikes().get(0);
				String likeQuery;
				boolean likingIsUser;
				if (pendingLike.getUser() != null && pendingLike.getUser().getUsername() != null
						&& !pendingLike.getUser().getUsername().equals("")) {
					likeQuery = " and likes.users.username = :likingUsername";
					likingIsUser = true;
				} else if (pendingLike.getPage() != null && pendingLike.getPage().getTitle() != null
						&& !pendingLike.getPage().getTitle().equals("")) {
					likeQuery = " and likes.pages.title = :likingTitle";
					likingIsUser = false;
				} else {
					LOG.info("No likingUser or likingPage");
					return "No likingUser or likingPage";
				}

				Query q2 = getCurrentSession().createQuery(
						"from WallPostLikes as likes where likes.wallposts.wallPostId = :id and likes.active = 1"
								+ likeQuery);
				q2.setParameter("id", likedPost.getWallPostId());

				if (likingIsUser) {
					q2.setParameter("likingUsername", pendingLike.getUser().getUsername());
				} else {
					q2.setParameter("likingTitle", pendingLike.getPage().getTitle());
				}

				// If the like doesn't already exist, create the Likes for it,
				// and save to database
				if (q2.list().isEmpty()) {
					WallPostLikes newLike = new WallPostLikes();
					newLike.setWallPosts(likedPost);
					newLike.setLikeDate(new Date());
					newLike.setActive(1);

					if (likingIsUser) {
						// Query database for the liking user, and set it to the
						// like
						q = getCurrentSession().createQuery("from Users where username = :uname and active = 1");
						q.setParameter("uname", pendingLike.getUser().getUsername());
						if (!q.list().isEmpty()) {
							newLike.setUsers((Users) q.list().get(0));
						} else {
							LOG.info("Liking user doesn't exist");
							return "Liking user doesn't exist";
						}
					} else {
						// Query database for the liking page, and set it to the
						// like
						q = getCurrentSession().createQuery("from Pages where title = :title and active = 1");
						q.setParameter("title", pendingLike.getPage().getTitle());
						if (!q.list().isEmpty()) {
							newLike.setPages((Pages) q.list().get(0));
						} else {
							LOG.info("Liking page doesn't exist");
							return "Liking page doesn't exist";
						}
					}

					if (save) getCurrentSession().save(newLike);
					LOG.info("Post liked successfully");
					return "Post liked successfully";
				} else {
					LOG.info("Like already exists");
					return "Like already exists";
				}
			}
			LOG.info("Post doesn't exist");
			return "Post doesn't exist";
		}
		LOG.info("No like information received");
		return "No like information received";
	}

	public String unlikeWallPost(WallPostPojo post, boolean save) {
		if (post.getLikes() != null && !post.getLikes().isEmpty()) {
			String sourceQuery;
			String destinationQuery;
			boolean sourceIsUser;
			boolean destinationIsUser;

			// Determines if the source is a user or a page, and adds it to the
			// query
			if (post.getUser() != null && post.getUser().getUsername() != null
					&& !post.getUser().getUsername().equals("")) {
				sourceQuery = " and usersByUserId.username = :susername";
				sourceIsUser = true;
			} else if (post.getPage() != null && post.getPage().getTitle() != null
					&& !post.getPage().getTitle().equals("")) {
				sourceQuery = " and pagesByPageId.title = :stitle";
				sourceIsUser = false;
			} else {
				LOG.info("No source found");
				return "No source found";
			}

			// Determines if the destination is a user or a page, and adds it to
			// the query
			if (post.getFriend() != null && post.getFriend().getUsername() != null
					&& !post.getFriend().getUsername().equals("")) {
				destinationQuery = " and usersByFriendId.username = :dusername";
				destinationIsUser = true;
			} else if (post.getPostingPage() != null && post.getPostingPage().getTitle() != null
					&& !post.getPostingPage().getTitle().equals("")) {
				destinationQuery = " and pagesByPostingPageId.title = :dtitle";
				destinationIsUser = false;
			} else {
				LOG.info("No destination found");
				return "No destination found";
			}

			Query q = getCurrentSession().createQuery(
					"from WallPosts where postedDate = :pdate and active = 1" + sourceQuery + destinationQuery);
			q.setDate("pdate", post.getPostedDate());
			if (sourceIsUser) {
				q.setParameter("susername", post.getUser().getUsername());
			} else {
				q.setParameter("stitle", post.getPage().getTitle());
			}
			if (destinationIsUser) {
				q.setParameter("dusername", post.getFriend().getUsername());
			} else {
				q.setParameter("dtitle", post.getPostingPage().getTitle());
			}

			// If the post being liked exists in the database then query if this
			// like already exists
			if (!q.list().isEmpty()) {
				WallPosts likedPost = (WallPosts) q.list().get(0);

				// Determine whether the like is by a user or a page, and refine
				// the like query based on this condition
				WallPostLikePojo pendingLike = post.getLikes().get(0);
				String likeQuery;
				boolean likingIsUser;
				if (pendingLike.getUser() != null && pendingLike.getUser().getUsername() != null
						&& !pendingLike.getUser().getUsername().equals("")) {
					likeQuery = " and likes.users.username = :likingUsername";
					likingIsUser = true;
				} else if (pendingLike.getPage() != null && pendingLike.getPage().getTitle() != null
						&& !pendingLike.getPage().getTitle().equals("")) {
					likeQuery = " and likes.pages.title = :likingTitle";
					likingIsUser = false;
				} else {
					LOG.info("No likingUser or likingPage");
					return "No likingUser or likingPage";
				}

				Query q2 = getCurrentSession().createQuery(
						"from WallPostLikes as likes where likes.wallposts.wallPostId = :id and likes.active = 1"
								+ likeQuery);
				q2.setParameter("id", likedPost.getWallPostId());

				if (likingIsUser) {
					q2.setParameter("likingUsername", pendingLike.getUser().getUsername());
				} else {
					q2.setParameter("likingTitle", pendingLike.getPage().getTitle());
				}

				// If the like exists, set it inactive and save to database
				if (!q2.list().isEmpty()) {
					WallPostLikes wpl = (WallPostLikes) q2.list().get(0);
					wpl.setActive(0);
					if (save) getCurrentSession().save(wpl);
					LOG.info("Unliked successfully!");
					return "Unliked successfully!";
				} else {
					LOG.info("Like doesn't exist");
					return "Like doesn't exist";
				}
			}
			LOG.info("Post doesn't exist");
			return "Post doesn't exist";
		}
		LOG.info("No like information received");
		return "No like information received";
	}
}