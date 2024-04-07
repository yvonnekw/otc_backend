package com.otc.backend.models;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="role_id")
    private Integer roleId;
    private String authority;

    //new
   // @ManyToMany(mappedBy = "roles")
    //private Collection<Users> users = new HashSet<>();

    public Role(){
        super();
    }

    /* 
    public Collection<Users> getUsers() {
        return users;
    }

    public void setUsers(Collection<Users> users) {
        this.users = users;
    }

    */

    public Role(String authority) {
        this.authority = authority;
    }
    
    public Role(Integer roleId, String authority) {
        this.roleId = roleId;
        this.authority = authority;
    }

    /* 
    public String getName() {
        return name != null ? name : "";
    }
    */
    

    @Override
    public String getAuthority() {
        // TODO Auto-generated method stub
        return this.authority;
    }

    public void setAuthority(String authority){
        this.authority = authority;
    }

    public Integer getRoleId(){
        return this.roleId;
    }

    public void setRoleId(Integer roleId){
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "Role [roleId=" + roleId + ", authority=" + authority + "]";
    }
}
