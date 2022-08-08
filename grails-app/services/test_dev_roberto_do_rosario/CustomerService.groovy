package test_dev_roberto_do_rosario

import grails.gorm.transactions.Transactional
import java.io.File
import java.io.FileOutputStream
import org.springframework.web.multipart.MultipartFile
import org.apache.commons.io.FilenameUtils;
import java.util.Base64

@Transactional
class CustomerService {

	private String imageFolder = "customerImages"

	def springSecurityService
	def amazonS3Service

	/*
		Method who retrieves the list of Customer, we can filter this list with the next parameters
		long customerId = Search for a explicit customer
		String firstname = Filter All the customers by his firstname
		String lastname = Filter All the customers by his lastname
	 */
    def getCustomerList(long customerId, String firstname=null, String lastname=null) {
		println("getCustomerList id ${customerId} firstname ${firstname} lastname ${lastname}")
		
		def customerMapList = []
		File tempCustomerImageFile
		
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
		//We map it and encode the image in b64 to retrieved,
		//the decision of encode the image in b64 is a last minute change, and this can be changed or improved
		customerList?.each{
			Customer customer = it
			Map customerMap = customer.getMap()
			if(customer?.imageFileName){
				def fileName =  customer?.imageFileName.split("\\.")
				tempCustomerImageFile = File.createTempFile(fileName[0], fileName[1])
				println("tempCustomerImageFile ${tempCustomerImageFile.getAbsoluteFile()}")
				tempCustomerImageFile = amazonS3Service.getFile("${imageFolder}/${customer?.imageFileName}","${tempCustomerImageFile.getAbsoluteFile()}")
				customerMap.imageFile = java.util.Base64.getEncoder().encodeToString(tempCustomerImageFile.getBytes())
			}
			customerMapList.add(customerMap)
		}
		
		//return a list of map with the user information
		return customerMapList		
    }
	/*
     * Save or Update the customer with the inputs
     * long customerId = if the customerId is 0, a new Customer is created, if customerId > 0, we update the customer if we find it, else we create a new one
     * String firstname = the firstname of the user
     * String lastname = the lastname of the user
     * MultipartFile multipartFile = The uploaded image file for the customer
    */
    def save(long customerId, String firstname=null, String lastname=null, MultipartFile multipartFile=null){
		println("SAVE multipartFile ${multipartFile.class}")
		def code = 200
		boolean error = false
		String message = ""
		Customer customer
		User currentUser = springSecurityService.currentUser
		String changeHistory = ""
		boolean isNew = false
		String imageFileName
		def extension

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
		
		//Depending wich attribute we receibe, we set or update the user information
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
			customer.modificationUser = currentUser
			customer.modificationDate = new Date()	
			//if all ok at this point, we validate the object with Hibernate and then save the User
			if(customer.validate()){
				customer.save(flush:true)
				message = "Customer ${customer?.firstname} ${customer?.lastname} has been saved!"
				customer.addToCustomerChangeHistories( saveCustomerHistory(customer, currentUser, changeHistory) )

				if(multipartFile && customer){
					//We get the file extension
					extension = multipartFile.getOriginalFilename()
					extension = extension.split("\\.")
					extension = extension[extension.size()-1]
					//And Create the file name and the route to save
					imageFileName = "customer_${customer?.id}.${extension}"
					amazonS3Service.storeMultipartFile("testdevroberto","${imageFolder}/${imageFileName}", multipartFile)
					//We save the image name to retrieve the image later
					customer.imageFileName = imageFileName
					if(customer.validate()) {
						customer.save(flush: true)
					}
				}
			} else {
				//if an error occurs, we return a message with the user cant be saved at this moment
				code = 400 //If we have an Error, this is a bad request
				message += "Error: the customer can't be saved!, please try again"
			}			
		}		
		
		return [code:code, message:message, customer: customer]
	}
		
	/*
     * Disable the customer, We do a Logical delete, we only not show the customer info but we keep it
     * long id = Customer id
    */
	def delete(long id){
		Customer customer = Customer.findByIdAndEnabled(id, true)
		def result = [code:200,message:"Customer ${customer?.firstname} ${customer?.lastname} deleted"]
		if(customer){
			customer.enabled = false
			if(customer.validate()){
				customer.save()
			} else {
				result = [code:400,message:"Customer ${customer?.firstname} ${customer?.lastname} can't be deleted"]
			}
		} else {
			result = [code:400,message:"Customer id ${id} Not found"]
		}
		return result
	}

	//Private method to save the customer changes, this is a add to the functionality of the project, this feacture was not require
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
