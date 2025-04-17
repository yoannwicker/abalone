rootProject.name = "pet-care"

include("backend:application")
findProject(":backend:application")?.name = "application"
include("backend:infrastructure")
findProject(":backend:infrastructure")?.name = "infrastructure"
include("backend:domain")
findProject(":backend:domain")?.name = "domain"
include("frontend")
