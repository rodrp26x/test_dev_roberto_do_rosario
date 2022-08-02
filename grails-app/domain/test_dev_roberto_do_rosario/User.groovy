package test_dev_roberto_do_rosario

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
//@Resource(uri='/user', formats=['json', 'xml'])
class User implements Serializable {

    private static final long serialVersionUID = 1

	String firstname
	String lastname
    String username
    String password
    boolean enabled = true //This field indicates if the user is active (true) or "Deleted" (false)
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired


    Set<Role> getAuthorities() {
        (UserRole.findAllByUser(this) as List<UserRole>)*.role as Set<Role>
    }

    static constraints = {
        firstname nullable: true, blank: true
        lastname nullable: true, blank: true
        password nullable: false, blank: false, password: true
        username nullable: false, blank: false, unique: true
    }

    static mapping = {
	    password column: '`password`'
    }
    
    static hasMany = [userRoles:UserRole]
    
    def hasRole(String roleAuthority){
		def userRoleList = UserRole.findByUserAndRole(this, Role.findByAuthority(roleAuthority))
		def hasRole = false
		
		if(userRoleList){
			hasRole = true
		}
		
		return hasRole
	}
	
	def getMap(){
		def map = [:]
		def roleList = []
		
		def roleSetList = this.getAuthorities()
		roleSetList?.each{
			Role role = it
			roleList.add(role?.authority)
		}
		
		map.id = this.id
		map.firstname = this.firstname
		map.lastname = this.lastname
		map.username = this.username
		map.roles = roleList
		
		return map
	}
	
}
