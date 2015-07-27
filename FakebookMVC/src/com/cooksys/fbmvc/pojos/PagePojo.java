package com.cooksys.fbmvc.pojos;

import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = { "pageId", "owner", "title", "description", "createdDate", "active", "likes" })
public class PagePojo {

	@Override
	public String toString() {
		return "PagePojo [pageId=" + pageId + ", owner=" + owner + ", title=" + title + ", description=" + description
				+ ", createdDate=" + createdDate + ", active=" + active + ", likes=" + likes + "]";
	}

	private int pageId;
	private UserPojo owner;
	private String title;
	private String description;
	private Date createdDate;
	private boolean active;
	private ArrayList<LikePojo> likes = new ArrayList<LikePojo>();

	public int getPageId() {
		return pageId;
	}

	public void setPageId(int pageId) {
		this.pageId = pageId;
	}

	public UserPojo getOwner() {
		return owner;
	}

	public void setOwner(UserPojo owner) {
		this.owner = owner;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public ArrayList<LikePojo> getLikes() {
		return likes;
	}

	public void setLikes(ArrayList<LikePojo> likes) {
		this.likes = likes;
	}
}
