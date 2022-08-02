

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'test_dev_roberto_do_rosario.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'test_dev_roberto_do_rosario.UserRole'
grails.plugin.springsecurity.authority.className = 'test_dev_roberto_do_rosario.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/',               access: ['permitAll']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/index',          access: ['permitAll']],
	[pattern: '/index.gsp',      access: ['permitAll']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/assets/**',      access: ['permitAll']],
	[pattern: '/**/js/**',       access: ['permitAll']],
	[pattern: '/**/css/**',      access: ['permitAll']],
	[pattern: '/**/images/**',   access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']],
	[pattern: '/h2-console/**',   access: ['ROLE_ADMIN']],
	[pattern: '/user/**',   access: ['ROLE_ADMIN']],
	[pattern: '/customer/**',   access: ['ROLE_ADMIN', 'ROLE_WORKER']],
	[pattern: '/api/**',   access: ['permitAll']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern: '/assets/**',      filters: 'none'],
	[pattern: '/**/js/**',       filters: 'none'],
	[pattern: '/**/css/**',      filters: 'none'],
	[pattern: '/**/images/**',   filters: 'none'],
	[pattern: '/**/favicon.ico', filters: 'none'],
	[pattern: '/**',             filters: 'JOINED_FILTERS'],
	[pattern: '/api/**', filters: 'JOINED_FILTERS,-anonymousAuthenticationFilter,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-rememberMeAuthenticationFilter']
]

grails.plugin.springsecurity.rest.login.active=true
grails.plugin.springsecurity.rest.login.endpointUrl='/api/login'
grails.plugin.springsecurity.rest.login.failureStatusCode=401
grails.plugin.springsecurity.rest.login.useJsonCredentials=true
grails.plugin.springsecurity.rest.login.usernamePropertyName='username'
grails.plugin.springsecurity.rest.login.passwordPropertyName='password'
grails.plugin.springsecurity.rest.logout.endpointUrl='/api/logout'
grails.plugin.springsecurity.rest.token.storage.useGorm=true
grails.plugin.springsecurity.rest.token.storage.gorm.tokenDomainClassName='test_dev_roberto_do_rosario.TokenAccess'
grails.plugin.springsecurity.rest.token.storage.gorm.tokenValuePropertyName='tokenValue'
grails.plugin.springsecurity.rest.token.storage.gorm.usernamePropertyName='username'
grails.plugin.springsecurity.rest.token.storage.jwt.secret = "gap83h13xH7F0t1QLg5eylxWrsPrJz4_r465h5NQ62xWHz2bI7v7tMnmBX57-vW9DFfXeiclvQ_6VMjK0RUsKXCjl4Jvb09HKx78vzleL6jv4ncSQdWNarU34objqvFCgKcb5kX3wBSMUEkkKU9bj3oLJQYtO7KI9YtAHf4WN_RcWaV50YrzssvcwGA_CIa1JIqK8-3Mk7i2KExlI2pJmxxQKGsVvKYoxYJrRaQ0k0J4S4GhH5xFbFk0cupvYOLtwCQAr2lb-nD0w0LnhFkKJRPOVfULLJpcMvW1gES3mtbxvktTJ2PO3r7yIU6U8GZnAn6a6YO8ENDeBVuOUOrtiQ"
grails.plugin.springsecurity.rest.token.storage.jwt.expiration=3600
grails.plugin.springsecurity.rest.token.generation.jwt.algorithm = "HS256"
grails.plugin.springsecurity.rest.token.generation.useSecureRandom=true
//grails.plugin.springsecurity.rest.token.validation.headerName='X-Auth-Token'
grails.plugin.springsecurity.rest.token.generation.useUUID=false
grails.plugin.springsecurity.rest.token.validation.active=true
grails.plugin.springsecurity.rest.token.validation.endpointUrl='/api/validate'
