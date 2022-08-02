package test_dev_roberto_do_rosario

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class CustomerSpec extends Specification implements DomainUnitTest<Customer> {

    def setup() {
		Customer customer = new Customer()
		customer.firstname = "prueba"
		customer.lastname = "prueba"
		if(customer.validate()){
			return true
		} else {
			return false 
		}
		
    }

    def cleanup() {
    }

    void "test something"() {
        expect:"fix me"
            true == true
    }
}
