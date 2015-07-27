package com.cooksys.fbws.pojos;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = {"requestId", "requestee", "requestor", "friendRequestDate", "active", "response"})
public class FriendRequestPojo {
	
	int requestId;
	UserPojo requestee, requestor;
	Date friendRequestDate;
	boolean active, response;
	
	public int getRequestId() { return requestId; }
	public void setRequestId(int requestId) { this.requestId = requestId; }
	
	public UserPojo getRequestee() { return requestee; }
	public void setRequestee(UserPojo requestee) { this.requestee = requestee; }
	
	public UserPojo getRequestor() { return requestor; }
	public void setRequestor(UserPojo requestor) { this.requestor = requestor; }
	
	public Date getFriendRequestDate() { return friendRequestDate; }
	public void setFriendRequestDate(Date friendRequestDate) { this.friendRequestDate = friendRequestDate; }
	
	public boolean isActive() { return active; }
	public void setActive(boolean active) { this.active = active; }
	
	public boolean isResponse()  {return response; }
	public void setResponse(boolean response) { this.response = response; }
	
	@Override
	public String toString() {
		return "FriendRequestPojo [requestId=" + requestId + ", requestee=" + requestee + ", requestor=" + requestor
				+ ", friendRequestDate=" + friendRequestDate + ", active=" + active + ", response=" + response + "]";
	}
}