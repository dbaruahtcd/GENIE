formattedReadings <- function(data) {
  
  names(data) <- c("genie.code", "guid", "input.timestamp", "bglucose", "bglucose.timestamp")
  
  if (class(data$input.timestamp)=="Date" | class(data$bglucose.timestamp)=="Date") stop("sync/bglucose timestamps cannot be 'Date' class. Hours, Minutes, Seconds must be present.")
  
  data$input.timestamp <- as.character(data$input.timestamp) ## Convertion to date class
  data$input.timestamp <- as.POSIXct(data$input.timestamp, format="%Y-%m-%d %H:%M:%S")
  data$bglucose.timestamp <- as.character(data$bglucose.timestamp) ## Convertion to date class
  data$bglucose.timestamp <- as.POSIXct(data$bglucose.timestamp, format="%Y-%m-%d %H:%M:%S")
  
  data <- subset(data, bglucose.timestamp <= as.POSIXct(Sys.Date())) ## Removing redundant readings 
  data$bglucose <- as.numeric(as.character(data$bglucose)) ## Converting to numeric form
  
  return(data)
}