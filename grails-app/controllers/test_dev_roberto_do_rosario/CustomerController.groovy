package test_dev_roberto_do_rosario
import grails.rest.RestfulController
import grails.converters.JSON
import groovy.json.JsonSlurper
import java.io.File
import java.io.FileOutputStream
import org.springframework.web.multipart.MultipartFile

class CustomerController extends RestfulController {
    static responseFormats = ['json', 'xml']
    
    def springSecurityService
    def customerService
    
    CustomerController(){
		super(Customer)
	}
	
	//GET /id
    def show(long id){
		println("SHOW Customer ${params}")
		String firstname
		String lastname
		
		//Parameters via URL
		if(params?.firstname){
			firstname = params?.firstname
		}
		if(params?.lastname){
			lastname = params?.lastname
		}
		
		//Parameter via JSON
		if(request?.JSON?.firstname){
			firstname = request?.JSON?.firstname
		}
		if(request?.JSON?.lastname){
			lastname = request?.JSON?.lastname
		}		
		
		def result = customerService.getCustomerList(id,firstname,lastname)
		
		render result as JSON
	}
	
	//POST
    def save(){
		println("SAVE Customer ${request?.JSON}")
		println("SAVE Customer ${params}")
		def result = [:]
		User currentUser = springSecurityService.currentUser
		String firstname
		String lastname
		String photoUrl = "asdad"
		def imageBytes
		//File imageFile = new File("C:\Users\rodrp\Desktop\fileUploaded.jpg")
		MultipartFile multipartFile
		File imageFile
		FileOutputStream fos
		
		//We retrieve all the inputs and prepare for the service
		if(params?.firstname){
			firstname = params?.firstname
		}
		if(params?.lastname){
			lastname = params?.lastname
		}
		if(params?.photoUrl){
			//photoUrl = request?.JSON?.photoUrl
			println("params?.photoUrl --- ${params?.photoUrl}")
			multipartFile = params?.photoUrl
		}
		
		result = customerService.save(0, firstname, lastname, multipartFile)
		
		response.status = result?.code
		render result?.message
	}
		
	//PUT
    def update(long id){
		println("UPDATE Customer ")
		def result = [:]
		User currentUser = springSecurityService.currentUser
		String firstname
		String lastname
		String photoUrl
				
		//We retrieve all the inputs and prepare for the service
		if(request?.JSON?.firstname){
			firstname = request?.JSON?.firstname
		}
		if(request?.JSON?.lastname){
			lastname = request?.JSON?.lastname
		}
		if(request?.JSON?.photoUrl){
			photoUrl = request?.JSON?.photoUrl
		}
		
		result = customerService.save(id, firstname, lastname, photoUrl)
		
		response.status = result?.code		
		render result?.message
	}
	
	//DELETE
	def delete(long id){
		println("DELETE Customer")
		def result = customerService.delete(id)
		
		response.status = result.code
		render result?.message
		
	}
	
}
