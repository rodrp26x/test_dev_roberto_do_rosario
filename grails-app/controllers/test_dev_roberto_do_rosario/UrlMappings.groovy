package test_dev_roberto_do_rosario

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
        
        "500"(view:'/error')
        "404"(view:'/notFound')

        
        
        /*"/user"(resources:"user") {
			"/save"(controller:"user", method:"POST")
			"/show"(controller:"user", method:"GET")
			"/update"(controller:"user", method:"PUT")
			"/delete"(controller:"user", method:"DELETE")
		}*/
		group "/user", {
			post "/"(controller:"user", action:"save")
			get "/"(controller:"user", action:"show")
			get "/$id"(controller:"user", action:"show")
			put "/$id"(controller:"user", action:"update")
			delete "/$id"(controller:"user", action:"delete")
		}
		group "/customer", {
			post "/"(controller:"customer", action:"save")
			get "/"(controller:"customer", action:"show")
			get "/$id"(controller:"customer", action:"show")
			put "/$id"(controller:"customer", action:"update")
			delete "/$id"(controller:"customer", action:"delete")
		}
    
    }
}
