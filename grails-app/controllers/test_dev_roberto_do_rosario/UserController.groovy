package test_dev_roberto_do_rosario
import grails.rest.RestfulController
import grails.converters.JSON
import groovy.json.JsonSlurper

class UserController extends RestfulController {
    static responseFormats = ['json', 'xml']
    
    def springSecurityService
    def userService
    
    UserController(){
		super(User)
	}
    	
	//GET /id
    def show(long id){
		println("SHOW ${params}")
		String roleAuthority 
		String firstname
		String lastname
		String username
		
		//Parameters via URL
		if(params?.roleAuthority){
			roleAuthority = params?.roleAuthority
		}
		if(params?.firstname){
			firstname = params?.firstname
		}
		if(params?.lastname){
			lastname = params?.lastname
		}
		if(params?.username){
			username = params?.username
		}
		
		//Parameter via JSON
		if(params?.roleAuthority){
			roleAuthority = params?.roleAuthority
		}
		if(params?.firstname){
			firstname = params?.firstname
		}
		if(params?.lastname){
			lastname = params?.lastname
		}
		if(params?.username){
			username = params?.username
		}			
		
		def result = userService.getUserList(id,roleAuthority,firstname,lastname,username)
		
		render result as JSON
	}
	
	//POST
    def save(){
		println("SAVE")
		def result = [:]
		User currentUser = springSecurityService.currentUser
		def roleAuthorityList
		String firstname
		String lastname
		String username
		String password
		
		
		//We retrieve all the inputs and prepare for the service
		if(params?.roleAuthority){
			roleAuthorityList = params?.list("roleAuthority")
		}
		if(params?.firstname){
			firstname = params?.firstname
		}
		if(params?.lastname){
			lastname = params?.lastname
		}
		if(params?.username){
			username = params?.username
		}	
		if(params?.password){
			password = params?.password
		}


		if(currentUser?.hasRole("ROLE_ADMIN")){
			result = userService.save(0, roleAuthorityList, firstname, lastname, username, password)
		} else {
			result = [code: 401, message: "Access Denied", params: params]
		}
		
		
		response.status = result?.code
		render result?.message
	}
		
	//PUT
    def update(long id){
		println("UPDATE")
		def result = [:]
		User currentUser = springSecurityService.currentUser
		def roleAuthorityList = []
		String firstname
		String lastname
		String username
		String password
				
		//We retrieve all the inputs and prepare for the service
		if(params?.roleAuthority){
			roleAuthorityList = params?.list("roleAuthority")
		}
		if(params?.firstname){
			firstname = params?.firstname
		}
		if(params?.lastname){
			lastname = params?.lastname
		}
		if(params?.username){
			username = params?.username
		}	
		if(params?.password){
			password = params?.password
		}
		
		if(currentUser.hasRole("ROLE_ADMIN")){
			result = userService.save(id, roleAuthorityList, firstname, lastname, username, password)
		} else {
			result = [code: 400, message: "Access Denied"]
		}
		
	
		response.status = result?.code		
		render result?.message
	}
	
	//DELETE
	def delete(long id){
		println("DELETE")
		def result = userService.delete(id)
		
		response.status = result.code
		render result?.message
		
	}
}
