package test_dev_roberto_do_rosario

import grails.gorm.transactions.Transactional

@Transactional
class UserService {

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
     * 
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
		//We search if the user exist, if its not exist, we create a new User object and validate the mandatory fields are informed
		user = User.findById(userId)
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
		
		//Depending wich attribute we receibe, we set or update the user informatin
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
			//if all ok at this point, we validate the object with Hibernate and then save the User
			if(user.validate() && !error){
				user.save()
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
     * Disable the user, for the users will be deleted but we keep the data registered
     * 
    */
	def delete(long id){
		User user = User.findById(id)
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
