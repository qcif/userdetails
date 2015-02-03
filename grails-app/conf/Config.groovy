import org.scribe.builder.api.FlickrApi

/******************************************************************************\
 *  CONFIG MANAGEMENT
\******************************************************************************/
def appName = 'userdetails'
def ENV_NAME = "${appName.toUpperCase()}_CONFIG"
default_config = "/data/${appName}/config/${appName}-config.properties"
if(!grails.config.locations || !(grails.config.locations instanceof List)) {
    grails.config.locations = []
}

if(System.getenv(ENV_NAME) && new File(System.getenv(ENV_NAME)).exists()) {
    println "[${appName}] Including configuration file specified in environment: " + System.getenv(ENV_NAME);
    grails.config.locations.add "file:" + System.getenv(ENV_NAME)
} else if(System.getProperty(ENV_NAME) && new File(System.getProperty(ENV_NAME)).exists()) {
    println "[${appName}] Including configuration file specified on command line: " + System.getProperty(ENV_NAME);
    grails.config.locations.add "file:" + System.getProperty(ENV_NAME)
} else if(new File(default_config).exists()) {
    println "[${appName}] Including default configuration file: " + default_config;
    grails.config.locations.add "file:" + default_config
} else {
    println "[${appName}] No external configuration file defined."
}

println "[${appName}] (*) grails.config.locations = ${grails.config.locations}"

/******************************************************************************\
 *  All configuration properties are stored in the template properties file in
 *  the ALA-INSTALL source repository:
 *  https://github.com/AtlasOfLivingAustralia/ala-install/blob/master/ansible/roles/userdetails/templates/userdetails-config.properties
\******************************************************************************/


/******************************************************************************/
grails.project.groupId = 'au.org.ala' // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text/plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// enable query caching by default
grails.hibernate.cache.queries = true

// convert empty strings to NULL in DB
grails.databinding.convertEmptyStringsToNull = false

// set per-environment serverURL stem for creating absolute links
environments {
    development {
        grails.serverURL = "http://devt.ala.org.au:8080/userdetails"
        grails.logging.jul.usebridge = true
        grails {
          mail {
            host = "localhost"
            port = 1025
            username = postie.emailSender
          }
        }
    }
    production {
        grails.serverURL = "http://auth.ala.org.au/userdetails"
        security.cas.appServerName = "http://auth.ala.org.au"
        grails.logging.jul.usebridge = false
        grails {
          mail {
            host = "localhost"
            port = 25
            username = postie.emailSender
          }
        }
    }
}

// log4j configuration
def loggingDir = (System.getProperty('catalina.base') ? System.getProperty('catalina.base') + '/logs' : './logs')

log4j = {
    appenders {
        environments {
            production {
                println "Log4j logs will be written to : ${loggingDir}"
                rollingFile name: "tomcatLog", maxFileSize: '1MB', file: "${loggingDir}/${appName}.log", threshold: Level.ERROR, layout: pattern(conversionPattern: "%d %-5p [%c{1}] %m%n")
            }
            development {
                console name: "stdout", layout: pattern(conversionPattern: "%d %-5p [%c{1}] %m%n"), threshold: Level.DEBUG
            }
            test {
                println "Log4j logs will be written to : ${loggingDir}"
                rollingFile name: "tomcatLog", maxFileSize: '1MB', file: "/tmp/${appName}", threshold: Level.DEBUG, layout: pattern(conversionPattern: "%d %-5p [%c{1}] %m%n")
            }
        }
    }
    root {
        // change the root logger to my tomcatLog file
        error 'tomcatLog'
        warn 'tomcatLog'
        additivity = true
    }

    debug 'grails.app',
            'grails.app.domain',
            'grails.app.controller',
            'grails.app.service',
            'grails.app.tagLib',
            'au.org.ala.userdetails',
            'grails.app.jobs'
}

// these placeholder values are overridden at runtime using the external configuration properties file
oauth {
    providers {
        flickr {
            api = FlickrApi
            key = "xxxxxxxxxxxxxxxxxxxxx"
            secret = "xxxxxxxxxxxxxxxxxxxxx"
            successUri = '/profile/flickrSuccess'
            failureUri = '/profile/flickrFail'
            callback = "xxxxxxxxxxxxx/yyyyyyyyyy/profile/flickrCallback"
        }
    }
//   debug = true
}
// Uncomment and edit the following lines to start using Grails encoding & escaping improvements

/* remove this line 
// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside null
                scriptlet = 'none' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        filteringCodecForContentType {
            //'text/html' = 'html'
        }
    }
}
remove this line */
