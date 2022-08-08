package test_dev_roberto_do_rosario
import grails.rest.RestfulController
import grails.converters.JSON
import org.apache.commons.io.FilenameUtils
import org.springframework.web.multipart.MultipartFile

import java.nio.file.Files

class CustomerController extends RestfulController {
    static responseFormats = ['json', 'xml']
    
    def springSecurityService
    def customerService
	def amazonS3Service
    
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
		MultipartFile multipartFile
		
		//We retrieve all the inputs and prepare for the service
		if(params?.firstname){
			firstname = params?.firstname
		}
		if(params?.lastname){
			lastname = params?.lastname
		}
		if(params?.imageFile){
			println("params?.imageFile --- ${params?.imageFile}")
			multipartFile = params?.imageFile
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
		String imageFile
		MultipartFile multipartFile
				
		//We retrieve all the inputs and prepare for the service
		if(params?.firstname){
			firstname = params?.firstname
		}
		if(params?.lastname){
			lastname = params?.lastname
		}
		if(params?.imageFile){
			println("params?.imageFile --- ${params?.imageFile}")
			multipartFile = params?.imageFile
		}
		
		result = customerService.save(id, firstname, lastname, imageFile)
		
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
