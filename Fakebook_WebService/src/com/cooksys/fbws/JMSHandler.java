package com.cooksys.fbws;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cooksys.fbws.pojos.FriendRequestPojo;
import com.cooksys.fbws.pojos.LikePojo;
import com.cooksys.fbws.pojos.PagePojo;
import com.cooksys.fbws.pojos.UserPojo;
import com.cooksys.fbws.pojos.WallPostLikePojo;
import com.cooksys.fbws.pojos.WallPostPojo;

@Component
public class JMSHandler {

	private static final Logger LOG = Logger.getLogger(JMSHandler.class);
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	DBHandler dbHandler;

	Connection connection;
	Session session;
	MessageProducer producer;

	@PostConstruct
	public void init() {
		try {
			// Initiate ActiveMQ Session
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://10.160.64.37:61616");
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// Setup message handler
			Destination dest = session.createTopic("fakebook");
			MessageConsumer consumer = session.createConsumer(dest);
			consumer.setMessageListener((message) -> handleMsg((TextMessage) message));
			// Begin listening
			producer = session.createProducer(dest);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	/**
	 * Sends a new user to be registered 
	 * @param user The user to be registered
	 */
	public void saveNewUser(UserPojo user) {
		try {
			LOG.info("JMS Send: saveNewUser!");
			TextMessage message = session.createTextMessage("FROM Group Awesome");
			message.setStringProperty("method", "register");
			message.setStringProperty("username", user.getUsername());
			message.setStringProperty("firstname", user.getFirstName());
			message.setStringProperty("lastname", user.getLastName());
			message.setStringProperty("password", user.getPassword());
			producer.send(message);
		} catch (JMSException e) { e.printStackTrace(); }
	}

	public void sendFriendRequest(FriendRequestPojo friendRequest) {
		try {
			LOG.info("JMS Send: friendRequest!");
			TextMessage message = session.createTextMessage("FROM Group Awesome");
			message.setStringProperty("method", "friendrequest");
			message.setStringProperty("requestor", friendRequest.getRequestor().getUsername());
			message.setStringProperty("requestee", friendRequest.getRequestee().getUsername());
			producer.send(message);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void savePage(PagePojo page) {
		try {
			LOG.info("JMS Send: savePage!");
			TextMessage message = session.createTextMessage("FROM Group Awesome");
			message.setStringProperty("method", "createpage");
			message.setStringProperty("title", page.getTitle());
			message.setStringProperty("desc", page.getDescription());
			message.setStringProperty("owner", page.getOwner().getUsername());
			producer.send(message);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void submitWallPost(WallPostPojo post) {
		try {
			LOG.info("JMS Send: sumbitWallPost!");
			TextMessage message = session.createTextMessage("FROM Group Awesome");
			message.setStringProperty("method", "wallpost");
			message.setStringProperty("posttext", post.getMessage());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			message.setStringProperty("timestamp", sdf.format(new Date()));
			message.setStringProperty("sourcetype", post.getFriend() != null ? "user" : "page");
			message.setStringProperty("destinationtype", post.getUser() != null ? "user" : "page");
			message.setStringProperty("source", post.getFriend() != null ? post.getFriend().getUsername() : post
					.getPostingPage().getTitle());
			message.setStringProperty("destination", post.getUser() != null ? post.getUser().getUsername() : post
					.getPage().getTitle());
			producer.send(message);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	// Respond to Friend Request
	public void acceptFriendRequest(FriendRequestPojo frp) {
		try {
			LOG.info("JMS Send: friendapprove");
			TextMessage message = session.createTextMessage("FROM Group Awesome");
			message.setStringProperty("method", "friendapprove");
			message.setStringProperty("approver", frp.getRequestee().getUsername());
			message.setStringProperty("approvee", frp.getRequestor().getUsername());
			producer.send(message);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void denyFriendRequest(FriendRequestPojo frp) {
		try {
			LOG.info("JMS Send: frienddeny");
			TextMessage message = session.createTextMessage("FROM Group Awesome");
			message.setStringProperty("method", "frienddeny");
			message.setStringProperty("denier", frp.getRequestee().getUsername());
			message.setStringProperty("denied", frp.getRequestor().getUsername());
			producer.send(message);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Handles all messages received through JMS
	 * @param msg The message from JMS
	 */
	public void handleMsg(TextMessage msg) {
		try {
			LOG.info("Message Received:\n\tType: " + msg.getStringProperty("method") + "\tMessage:" + msg.getText());
			// Get the type of the message
			String command = msg.getStringProperty("method");
			UserPojo user = new UserPojo();
			// Decide how to handle the message and handle it accordingly
			switch (command) {
			case "register":
				// A user is being registered
				user.setUsername(msg.getStringProperty("username"));
				user.setFirstName(msg.getStringProperty("firstname"));
				user.setLastName(msg.getStringProperty("lastname"));
				user.setPassword(msg.getStringProperty("password"));
				user.setRegisteredDate(new Date());
				user.setActive(true);
				LOG.info("JMS Receive: register\n\tRESULT: " + dbHandler.saveNewUser(user, true));
			break;
			case "friendrequest":
				// Submit a friend request
				FriendRequestPojo frp = new FriendRequestPojo();
				UserPojo sender = new UserPojo();
				sender.setUsername(msg.getStringProperty("requestor"));
				frp.setRequestor(sender);
				UserPojo receiver = new UserPojo();
				receiver.setUsername(msg.getStringProperty("requestee"));
				frp.setRequestee(receiver);
				LOG.info("JMS Receive: friendrequest\n\tRESULT: " + dbHandler.sendFriendRequest(frp, true));
			break;
			case "friendapprove":
				// Approve a friend request
				FriendRequestPojo approved = new FriendRequestPojo();
				UserPojo approver = new UserPojo();
				approver.setUsername(msg.getStringProperty("approver"));
				approved.setRequestee(approver);
				UserPojo approvee = new UserPojo();
				approvee.setUsername(msg.getStringProperty("approvee"));
				approved.setRequestor(approvee);
				approved.setResponse(true);
				approved.setActive(true);
				LOG.info("JMS Receive: friendapprove\n\tRESULT: " + dbHandler.respondToFriendRequest(approved, true));
			break;
			case "frienddeny":
				// Deny a friend request
				FriendRequestPojo deny = new FriendRequestPojo();
				UserPojo denier = new UserPojo();
				denier.setUsername(msg.getStringProperty("denier"));
				deny.setRequestee(denier);
				UserPojo denied = new UserPojo();
				denied.setUsername(msg.getStringProperty("denied"));
				deny.setRequestor(denied);
				deny.setResponse(false);
				deny.setActive(true);
				LOG.info("Receive: frienddeny\n\tRESULT: " + dbHandler.respondToFriendRequest(deny, true));
			break;
			case "frienddelete":
				// Remove a friend
				FriendRequestPojo removal = new FriendRequestPojo();
				UserPojo deleter = new UserPojo();
				deleter.setUsername(msg.getStringProperty("deleter"));
				removal.setRequestee(deleter);
				UserPojo deleted = new UserPojo();
				deleted.setUsername(msg.getStringProperty("deleted"));
				removal.setRequestor(deleted);
				LOG.info("Receive: frienddelete\n\tRESULT: " + dbHandler.removeFriend(removal, true));
			break;
			case "likepage":
				// Like a page
				PagePojo likedPage = new PagePojo();
				likedPage.setTitle(msg.getStringProperty("page"));
				LikePojo like = new LikePojo();
				if (msg.getStringProperty("likertype").equals("user")) {
					UserPojo likingUser = new UserPojo();
					likingUser.setUsername(msg.getStringProperty("liker"));
					like.setLikingUser(likingUser);
				} else if (msg.getStringProperty("likertype").equals("page")) {
					PagePojo likingPage = new PagePojo();
					likingPage.setTitle(msg.getStringProperty("liker"));
					like.setLikingPage(likingPage);
				}
				LOG.info("Receive: likepage\n\tRESULT: " + dbHandler.likePage(likedPage, true));
			break;
			case "unlikepage":
				// Un-like a page
				LOG.fatal("Not Ready to handle Page Unlike");
				PagePojo unlikedPage = new PagePojo();
				unlikedPage.setTitle(msg.getStringProperty("page"));
				LikePojo unlike = new LikePojo();
				if (msg.getStringProperty("unlikertype").equals("user")) {
					UserPojo unlikingUser = new UserPojo();
					unlikingUser.setUsername(msg.getStringProperty("unliker"));
					unlike.setLikingUser(unlikingUser);
				} else if (msg.getStringProperty("unlikertype").equals("page")) {
					PagePojo unlikingPage = new PagePojo();
					unlikingPage.setTitle(msg.getStringProperty("unliker"));
					unlike.setLikingPage(unlikingPage);
				}
				LOG.info("JMS Receive: unlikepage\n\tRESULT: " + dbHandler.unlikePage(unlikedPage, true));
			break;
			case "ignore":
				// Ignore a user
				LOG.fatal("Not ready to handle Ignore");
				// NOT YET WORKING
				//IgnorePojo ignore = new IgnorePojo();
				//if(msg.getStringProperty(name)) dbHandler.ignoreUser(ignoredUser, true);
			break;
			case "unignore":
				LOG.fatal("Not Ready to handle Unignore");
				// NOT YET WORKING
				//dbHandler.unignoreUser(unignoredUser, true);
			break;
			case "createpage":
				// Create a page
				PagePojo page = new PagePojo();
				user.setUsername(msg.getStringProperty("owner"));
				page.setOwner(user);
				page.setTitle(msg.getStringProperty("pagename"));
				page.setDescription(msg.getStringProperty("description"));
				LOG.info("Receive: createpage\n\tRESULT: " + dbHandler.savePage(page, true));
			break;
			case "likepost":
				// Likes a page
				WallPostPojo likedPost = new WallPostPojo();
				if (msg.getStringProperty("sourcetype").equals("page")) {
					PagePojo postPage = new PagePojo();
					postPage.setTitle(msg.getStringProperty("source"));
					likedPost.setPostingPage(postPage);
				} else if (msg.getStringProperty("sourcetype").equals("user")) {
					UserPojo postSender = new UserPojo();
					postSender.setUsername(msg.getStringProperty("source"));
					likedPost.setFriend(postSender);
				}
				if (msg.getStringProperty("destinationtype").equals("page")) {
					PagePojo getPage = new PagePojo();
					getPage.setTitle(msg.getStringProperty("destination"));
					likedPost.setPage(getPage);
				} else if (msg.getStringProperty("destinationtype").equals("user")) {
					UserPojo getUser = new UserPojo();
					getUser.setUsername(msg.getStringProperty("destination"));
					likedPost.setUser(getUser);
				}
				likedPost.setPostedDate(SDF.parse(msg.getStringProperty("timestamp")));
				likedPost.setMessage(msg.getStringProperty("posttext"));
				ArrayList<WallPostLikePojo> likes = new ArrayList<>();
				if (msg.getStringProperty("likertype").equals("user")) {
					WallPostLikePojo likePojo = new WallPostLikePojo();
					UserPojo userPojo = new UserPojo();
					userPojo.setUsername(msg.getStringProperty("liker"));
					likePojo.setUser(userPojo);
					likes.add(likePojo);
				} else {
					WallPostLikePojo likePojo = new WallPostLikePojo();
					PagePojo pagePojo = new PagePojo();
					pagePojo.setTitle(msg.getStringProperty("page"));
					likePojo.setPage(pagePojo);
					likes.add(likePojo);
				}
				likedPost.setLikes(likes);
				LOG.info("Receive: likepost\n\tRESULT: " + dbHandler.likeWallPost(likedPost, true));
			break;
			case "unlikepost":
				// Un-likes a post
				LOG.fatal("Not ready to handle Unlike Post");
				WallPostPojo unlikedPost = new WallPostPojo();
				if (msg.getStringProperty("sourcetype").equals("page")) {
					PagePojo postPage = new PagePojo();
					postPage.setTitle(msg.getStringProperty("source"));
					unlikedPost.setPostingPage(postPage);
				} else if (msg.getStringProperty("sourcetype").equals("user")) {
					UserPojo postSender = new UserPojo();
					postSender.setUsername(msg.getStringProperty("source"));
					unlikedPost.setFriend(postSender);
				}
				if (msg.getStringProperty("destinationtype").equals("page")) {
					PagePojo getPage = new PagePojo();
					getPage.setTitle(msg.getStringProperty("destination"));
					unlikedPost.setPage(getPage);
				} else if (msg.getStringProperty("destinationtype").equals("user")) {
					UserPojo getUser = new UserPojo();
					getUser.setUsername(msg.getStringProperty("destination"));
					unlikedPost.setUser(getUser);
				}
				unlikedPost.setPostedDate(SDF.parse(msg.getStringProperty("timestamp")));
				unlikedPost.setMessage(msg.getStringProperty("posttext"));
				ArrayList<WallPostLikePojo> unlikes = new ArrayList<>();
				WallPostLikePojo unlikePojo = new WallPostLikePojo();
				if (msg.getStringProperty("unlikertype").equals("user")) {
					UserPojo userPojo = new UserPojo();
					userPojo.setUsername(msg.getStringProperty("unliker"));
					unlikePojo.setUser(userPojo);
				} else {
					PagePojo pagePojo = new PagePojo();
					pagePojo.setTitle(msg.getStringProperty("page"));
					unlikePojo.setPage(pagePojo);
				}
				unlikes.add(unlikePojo);
				unlikedPost.setLikes(unlikes);
				LOG.info("Receive: unlikepost\n\tRESULT: " + dbHandler.unlikeWallPost(unlikedPost, true));
			break;
			case "wallpost":
				// Post a message to a wall
				WallPostPojo wpp = new WallPostPojo();
				if (msg.getStringProperty("sourcetype").equals("page")) {
					PagePojo postPage = new PagePojo();
					postPage.setTitle(msg.getStringProperty("source"));
					wpp.setPostingPage(postPage);
				} else if (msg.getStringProperty("sourcetype").equals("user")) {
					UserPojo postSender = new UserPojo();
					postSender.setUsername(msg.getStringProperty("source"));
					wpp.setFriend(postSender);
				}
				if (msg.getStringProperty("destinationtype").equals("page")) {
					PagePojo getPage = new PagePojo();
					getPage.setTitle(msg.getStringProperty("destination"));
					wpp.setPage(getPage);
				} else if (msg.getStringProperty("destinationtype").equals("user")) {
					UserPojo getUser = new UserPojo();
					getUser.setUsername(msg.getStringProperty("destination"));
					wpp.setUser(getUser);
				}
				wpp.setPostedDate(SDF.parse(msg.getStringProperty("timestamp")));
				wpp.setMessage(msg.getStringProperty("posttext"));
				LOG.info("Receive: wallpost\n\tRESULT: " + dbHandler.submitWallPost(wpp, true));
			break;
			default:
				// Can't handle any other message type
				LOG.fatal("Unknown Message Type: " + msg.getText());
			break;
			}
		} catch (JMSException | ParseException ignored) {}
	}

	@PreDestroy
	public void dispose() {
		try {
			session.close();
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
