package test_dev_roberto_do_rosario

import grails.gorm.transactions.Transactional

@Transactional
class UserService {

	/*
		Method who retrieves the list of user, we can filter this list with the next parameters
		long userId = Search for a explicit user
		String roleAuthority = Search All the users by his role
		String firstname = Filter All the users by his firstname
		String lastname = Filter All the users by his lastname
		String username = Filter All the users by his username
	 */
    def getUserList(long userId, String roleAuthority=null, String firstname=null, String lastname=null, String username=null) {
		println("getUserList id ${userId} roleAuthority ${roleAuthority} firstname ${firstname} lastname ${lastname} username ${username}")
		
		def userMapList = []
		
		def userCriteria = User.createCriteria()
		def userList = userCriteria.list{
			eq('enabled',true)
			if(userId > 0){
				eq("id",userId)
			}
			if(roleAuthority){
				//We search in the relation with his roles if have it
				userRoles{
					role {
						eq("authority", roleAuthority)
					}
				}
			}
			if(firstname){
				like("firstname","%${firstname}%")
			}
			if(lastname){
				like("lastname","%${lastname}%")
			}
			if(username){
				like("username","%${username}%")
			}
			
		}
		userList?.each{
			User user = it
			userMapList.add(user.getMap())
		}
		
		//return a list of map with the user information
		return userMapList
		
		
    }
    /*
     * Save or Update the user with the inputs  
     * long userId = if the userId is 0, a new User is created, if userId > 0, we update the user if we find it, else we create a new one
     * List roleAuthorityList = the list of role for the user, if this field is received, we delete all the current roles from the user, and insert the received ones
     * String firstname = the firstname of the user
     * String lastname = the lastname of the user
     * String username = the username of the user, only used in the creation of a new user,
     * String password = the password is cyphed when is saved
    */
    def save(long userId, def roleAuthorityList=null, String firstname=null, String lastname=null, String username=null, String password=null){
		def code = 200
		boolean error = false
		String message = ""
		User user
		
		def roleList = []
		println("SAVE ${roleAuthorityList}")
		//If the role is informed, we search it and have the object to create the relation with the user at the end!!
		if(roleAuthorityList){
			roleAuthorityList.each{
				String roleAuthority = it
				println("			roleAuthority ${roleAuthority}")
				roleList.add(Role.findByAuthority(roleAuthority))
			}
		}

		//if the passed Id is more than 0 is an update, then we search by that id, in the other case we search by username if not already exist on our database
		if(userId > 0){
			user = User.findById(userId)
		} else {
			//In a creation, cant be another User with the same username, so we return an error and a message with already exist the user
			user = User.findByUsername(username)
			if(user){
				error = true
				code = 400
				message = "Already Exist ${username}, please use a Put request to update it  "
			}
		}
		//We search if the user exist by id, if its not exist, we create a new User object and validate the mandatory fields are informed
		if(!user){
			//We check the mandatory field, in this case username and password, and the role for the Permissions
			if(!username){
				error = true
				message += "The username is a mandatory field"
			}
			if(!password){
				error = true
				message += "The password is a mandatory field"
			}
			if(!roleList){
				error = true
				message += "The Role ${roleAuthority} was not found"
			}
			if(!error){
				user = new User()
				user.username= username //The only time we set the username value is while we create a new user
			} else {
				code = 400 //If we have an Error, this is a bad request
			}
		}
		
		//Depending wich attribute we receive, we set or update the user information
		if(user){
			if(firstname){
				user.firstname= firstname
			}
			if(lastname){
				user.lastname= lastname	
			}
			if(password){
				user.password= password
			}
			user.enabled = true
			//if all ok at this point, we validate the object with Hibernate and then save the User
			if(user.validate() && !error){
				user.save(flush:true)
				message = "User ${user?.username} has been saved!"
			} else {
				//if an error occurs, we return a message with the user cant be saved at this moment
				code = 400 //If we have an Error, this is a bad request
				message += "Error: the user can't be saved!, please try again"
			}
			
			//At last we asign a role or permission to the user, if the user already have the Role, we dont do anything, else we assign the role
			roleList.each{
				user?.userRoles?.clear()
				user.save()
				Role role = it 
				println("INTENTAMOS CREAR ROLE ${user} ${role} ${roleList}")
				if(user && role && !UserRole.exists(user?.id, role?.id)){
					println("CREAMOS ROLE ${user} ${role}")
					UserRole.create(user, role) //The UserRole is a Java SET, so always be unique the roles the user have.	
				}
			}
			
		}		
		
		return [code:code, message:message]
	}
	
	
	/*
     * Disable the User, We do a Logical delete, we only not show the user info but we keep it
     * long id = the user id
    */
	def delete(long id){
		User user = User.findByIdAndEnabled(id, true)
		def result = [code:200,message:"User ${user?.username} deleted"]
		if(user){
			user.enabled = false
			if(user.validate()){
				user.save()
			} else {
				result = [code:400,message:"User ${user.username} can't be deleted"]
			}
		} else {
			result = [code:400,message:"User id ${id} Not found"]
		}
		
		return result
	}
}
