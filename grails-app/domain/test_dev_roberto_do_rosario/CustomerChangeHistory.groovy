package test_dev_roberto_do_rosario

class CustomerChangeHistory {
	
	Customer customer
	User modificationUser //The user who modified the Customer
	Date modificationDate //The Date of the modification
	String modificationDescription //A little description about what was modified

    static constraints = {
		customer nullable: true, blank: true
		modificationUser nullable: true, blank: true
		modificationDate nullable: true, blank: true
		modificationDescription nullable: true, blank: true
    }
}
