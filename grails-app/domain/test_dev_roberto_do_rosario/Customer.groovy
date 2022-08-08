package test_dev_roberto_do_rosario
import java.text.SimpleDateFormat

class Customer {
	
	String firstname
	String lastname
	boolean enabled = true  //This field indicates if the user is active (true) or "Deleted" (false)
	//the User who create this Customer
	User creationUser
	Date creationDate
	//The last User who modificated this Customer
	User modificationUser
	Date modificationDate
	String imageFileName //This field is the name file inside the S3 bucket
	
	static hasMany = [customerChangeHistories : CustomerChangeHistory]

    static constraints = {
        firstname nullable: true, blank: true
        lastname nullable: true, blank: true
        creationUser nullable: true, blank: true
        creationDate nullable: true, blank: true
        modificationUser nullable: true, blank: true
        modificationDate nullable: true, blank: true
		imageFileName nullable: true, blank: true

    }
    
    def getMap(){
		def map = [:]
		SimpleDateFormat sdp = new SimpleDateFormat("dd/MM/yyyy HH:mm")
		
		map.id = this.id
		map.firstname = this.firstname
		map.lastname = this.lastname
		map.creationUser = this.creationUser?.getMap()
		map.creationDate = sdp.format(this.creationDate)
		map.modificationUser = this.modificationUser?.getMap()
		map.modificationDate = sdp.format(this.modificationDate)
		
		return map
	}
}
