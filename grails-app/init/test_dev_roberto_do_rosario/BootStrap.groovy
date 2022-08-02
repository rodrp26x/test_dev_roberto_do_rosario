package test_dev_roberto_do_rosario

class BootStrap {
	def userService
    def init = { servletContext ->
		println("try save ROLE_ADMIN")
		Role adminRole = Role.findByAuthority("ROLE_ADMIN")
		if(!adminRole){
			adminRole = new Role()
			adminRole.authority = "ROLE_ADMIN"
			if(adminRole.validate()){
				adminRole.save()
				println("ROLE_ADMIN saved!")
				adminRole = Role.findByAuthority("ROLE_ADMIN")
			}
		}
		
		println("try save ROLE_WORKER")
		Role workerRole = Role.findByAuthority("ROLE_WORKER")
		if(!workerRole){
			workerRole = new Role()
			workerRole.authority = "ROLE_WORKER"
			if(workerRole.validate()){
				workerRole.save()
				println("ROLE_WORKER saved!")
				workerRole = Role.findByAuthority("ROLE_WORKER")
			}
		}
		
		
		User user = User.findByUsername("admin")
		if(!user){
			println( userService.save(0, ["ROLE_ADMIN"], "admin", "admin", "admin", "admin") )
		}
		
    }
    def destroy = {
    }
}
