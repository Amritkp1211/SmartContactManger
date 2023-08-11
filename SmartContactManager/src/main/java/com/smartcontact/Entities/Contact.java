package com.smartcontact.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "Contact")
public class Contact {
    
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cid;
	
	@NotBlank(message = "name field required")
	private String name;
	
	@NotBlank(message = "nickname field required")
	private String nickname;
	
	
	@NotBlank(message = "work field required")
	@Column(length = 30)
	private String work;
	
	@NotBlank(message = "description field required")
	@Column(length = 1000)
	private String description;
	
	@NotBlank(message = "Password field required")
	private String email;
	
	@NotBlank(message = "phone field required")
	private String phone;
	
	@NotBlank(message = "image field required")
	private String image;
	
	@ManyToOne
	@JsonIgnore
	private User user;
	
	public Contact() {
		
	}

	

	public Contact(int cid, String name, String nickname, String work, String description, String email, String phone,
			String image, User user) {
		super();
		this.cid = cid;
		this.name = name;
		this.nickname = nickname;
		this.work = work;
		this.description = description;
		this.email = email;
		this.phone = phone;
		this.image = image;
		this.user = user;
	}

	

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}



	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.cid==((Contact)obj).getCid();
	}
	
}
