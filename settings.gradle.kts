rootProject.name = "kafka-for-developers"

include("work2")
include("work2:producer-service")
include("work2:consumer-service")
findProject(":work2:producer-service")?.name = "producer-service"
findProject(":work2:consumer-service")?.name = "consumer-service"

