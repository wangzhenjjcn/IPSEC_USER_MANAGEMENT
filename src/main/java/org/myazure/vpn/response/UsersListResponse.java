package org.myazure.vpn.response;

import java.util.List;

import org.myazure.response.StatusResponse;
import org.myazure.vpn.entity.UserData;

import com.fasterxml.jackson.annotation.JsonProperty;


public class UsersListResponse extends StatusResponse {
	
	
	@JsonProperty("data")
	private List<UserData> users;
	
	public UsersListResponse(){
		super();
	}
	
	public UsersListResponse(String msg){
		super();
	}
	
	public UsersListResponse(String msg, boolean success){
		super();
	}
	public UsersListResponse(String msg, int code, boolean success){
		super();
	}

	public List<UserData> getUsers() {
		return users;
	}

	public void setUsers(List<UserData> users) {
		this.users = users;
	}
	
	

}
