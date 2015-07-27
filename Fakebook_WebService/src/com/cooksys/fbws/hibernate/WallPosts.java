package com.cooksys.fbws.hibernate;
// Generated Jul 9, 2015 2:35:58 PM by Hibernate Tools 3.4.0.CR1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * WallPosts generated by hbm2java
 */
@Entity
@Table(name="wall_posts"
    ,catalog="fakebook"
)
public class WallPosts  implements java.io.Serializable {


     private Integer wallPostId;
     private Users usersByFriendId;
     private Pages pagesByPageId;
     private Users usersByUserId;
     private Pages pagesByPostingPageId;
     private String message;
     private Date postedDate;
     private int active;
     private Set<WallPostLikes> wallPostLikeses = new HashSet<WallPostLikes>(0);

    public WallPosts() {
    }

	
    public WallPosts(Users usersByFriendId, Pages pagesByPageId, Users usersByUserId, Pages pagesByPostingPageId, Date postedDate, int active) {
        this.usersByFriendId = usersByFriendId;
        this.pagesByPageId = pagesByPageId;
        this.usersByUserId = usersByUserId;
        this.pagesByPostingPageId = pagesByPostingPageId;
        this.postedDate = postedDate;
        this.active = active;
    }
    public WallPosts(Users usersByFriendId, Pages pagesByPageId, Users usersByUserId, Pages pagesByPostingPageId, String message, Date postedDate, int active, Set<WallPostLikes> wallPostLikeses) {
       this.usersByFriendId = usersByFriendId;
       this.pagesByPageId = pagesByPageId;
       this.usersByUserId = usersByUserId;
       this.pagesByPostingPageId = pagesByPostingPageId;
       this.message = message;
       this.postedDate = postedDate;
       this.active = active;
       this.wallPostLikeses = wallPostLikeses;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="wall_post_id", unique=true, nullable=false)
    public Integer getWallPostId() {
        return this.wallPostId;
    }
    
    public void setWallPostId(Integer wallPostId) {
        this.wallPostId = wallPostId;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="friend_id", nullable=false)
    public Users getUsersByFriendId() {
        return this.usersByFriendId;
    }
    
    public void setUsersByFriendId(Users usersByFriendId) {
        this.usersByFriendId = usersByFriendId;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="page_id", nullable=false)
    public Pages getPagesByPageId() {
        return this.pagesByPageId;
    }
    
    public void setPagesByPageId(Pages pagesByPageId) {
        this.pagesByPageId = pagesByPageId;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    public Users getUsersByUserId() {
        return this.usersByUserId;
    }
    
    public void setUsersByUserId(Users usersByUserId) {
        this.usersByUserId = usersByUserId;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="posting_page_id", nullable=false)
    public Pages getPagesByPostingPageId() {
        return this.pagesByPostingPageId;
    }
    
    public void setPagesByPostingPageId(Pages pagesByPostingPageId) {
        this.pagesByPostingPageId = pagesByPostingPageId;
    }

    
    @Column(name="message", length=1024)
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="posted_date", nullable=false, length=19)
    public Date getPostedDate() {
        return this.postedDate;
    }
    
    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    
    @Column(name="active", nullable=false)
    public int getActive() {
        return this.active;
    }
    
    public void setActive(int active) {
        this.active = active;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="wallPosts")
    public Set<WallPostLikes> getWallPostLikeses() {
        return this.wallPostLikeses;
    }
    
    public void setWallPostLikeses(Set<WallPostLikes> wallPostLikeses) {
        this.wallPostLikeses = wallPostLikeses;
    }




}


