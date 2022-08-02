package test_dev_roberto_do_rosario

import grails.gorm.transactions.Transactional
import java.io.File
import java.io.FileOutputStream
import org.springframework.web.multipart.MultipartFile

@Transactional
class CustomerService {

	def springSecurityService
    
    def getCustomerList(long customerId, String firstname=null, String lastname=null) {
		println("getCustomerList id ${customerId} firstname ${firstname} lastname ${lastname}")
		
		def customerMapList = []
		
		def customerCriteria = Customer.createCriteria()
		def customerList = customerCriteria.list{
			eq('enabled',true)
			if(customerId > 0){
				eq("id",customerId)
			}
			if(firstname){
				like("firstname","%${firstname}%")
			}
			if(lastname){
				like("lastname","%${lastname}%")
			}
						
		}
		customerList?.each{
			Customer customer = it
			customerMapList.add(customer.getMap())
		}
		
		//return a list of map with the user information
		return customerMapList		
    }
    /*
     * Save or Update the user with the inputs  
     * 
    */
    def save(long customerId, String firstname=null, String lastname=null, MultipartFile multipartFile=null){
		def code = 200
		boolean error = false
		String message = ""
		Customer customer
		User currentUser = springSecurityService.currentUser
		String changeHistory = ""
		boolean isNew = false
		//MultipartFile multipartFile
		File imageFile
		FileOutputStream fos
		
		
		//We search if the Customer exist, if its not exist, we create a new Customer object and validate the mandatory fields are informed
		customer = Customer.findById(customerId)
		if(!customer){
			//We check the mandatory field, in this case username and password, and the role for the Permissions
			if(!firstname){
				error = true
				message += "The firstname is a mandatory field"
			}
			if(!lastname){
				error = true
				message += "The lastname is a mandatory field"
			}
			if(!photoUrl){
				error = true
				message += "The photoUrl is a mandatory field"
			}
			if(error){
				code = 400 //If we have an Error, this is a bad request
			} else {
				customer = new Customer()
				customer.creationUser = currentUser
				customer.creationDate = new Date()
				changeHistory = "Customer Creation"
				isNew = true
			}
		}
		
		//Depending wich attribute we receibe, we set or update the user informatin
		if(customer){
			if(firstname){
				if(!isNew){
					changeHistory += "| Firstname (${customer?.firstname}) changed to (${firstname})"	
				}
				customer.firstname= firstname
			}
			if(lastname){
				if(!isNew){
					changeHistory += "| Lastname (${customer?.lastname}) changed to (${lastname})"	
				}
				customer.lastname= lastname	
			}
			if(multipartFile){
				if(isNew){
					changeHistory += "| Photo changed"	
				}
				imageFile = new File("uploadFile/prueba.jpg")
				fos = new FileOutputStream( imageFile );
				fos.write( multipartFile.getBytes() );
				fos.close();
				println("imageFile ${imageFile.getPath()}")
			}
			customer.modificationUser = currentUser
			customer.modificationDate = new Date()	
			//if all ok at this point, we validate the object with Hibernate and then save the User
			if(customer.validate() && !error){
				customer.save()
				message = "Customer ${customer?.firstname} ${customer?.lastname} has been saved!"
				customer.addToCustomerChangeHistories( saveCustomerHistory(customer, currentUser, changeHistory) )
			} else {
				//if an error occurs, we return a message with the user cant be saved at this moment
				code = 400 //If we have an Error, this is a bad request
				message += "Error: the customer can't be saved!, please try again"
			}			
		}		
		
		return [code:code, message:message]
	}
		
	/*
     * Disable the user, for the users will be deleted but we keep the data registered
     * 
    */
	def delete(long id){
		Customer customer = Customer.findById(id)
		def result = [code:200,message:"Customer ${customer?.firstname} ${customer?.lastname} deleted"]
		if(customer){
			customer.enabled = false
			if(customer.validate){
				customer.save()
				
			} else {
				result = [code:400,message:"Customer ${customer?.firstname} ${customer?.lastname} can't be deleted"]
			}
		} else {
			result = [code:400,message:"Customer id ${id} Not found"]
		}		
	}
	
	private def saveCustomerHistory(Customer customer, User modificationUser, String modificationDescription){
		CustomerChangeHistory customerChangeHistory = new CustomerChangeHistory()
		customerChangeHistory.customer = customer
		customerChangeHistory.modificationDate = new Date()
		customerChangeHistory.modificationUser = modificationUser
		customerChangeHistory.modificationDescription = modificationDescription
		
		if(customerChangeHistory.validate()){
			customerChangeHistory.save()
		}
		
		return customerChangeHistory		
	}
}
