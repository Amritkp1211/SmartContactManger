package com.smartcontact.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smartcontact.Entities.Contact;
import com.smartcontact.Entities.User;



@Repository
public interface ContactRepository extends JpaRepository<Contact,Integer>{
  //pagination
	
	//currentPage-page 1
	//contact per page -7
	@Query("from Contact as c where c.user.id =:userId")
	public Page<Contact> findContactsByUser(@Param("userId") int userId,Pageable pageable);

	
	//search result by name	
    public List<Contact> findByNameContainingAndUser(String name,User user);


	
}
