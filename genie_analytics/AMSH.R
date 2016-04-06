read.csv("readings.csv", header=TRUE, sep=",") -> readings

#removing the first column
readings = subset(readings, select = -X)

processData <- function(data) {
  names(data) <- c("genie.code", "guid", "mse.timestamp", "bg", "bg.timestamp") ##Renaming the readings columns
  
  data$mse.timestamp <- as.character(data$mse.timestamp) ##Converting to date class
  data$mse.timestamp <- as.Date(data$mse.timestamp, format="%Y-%m-%d %H:%M:%S")
  data$bg.timestamp <- as.character(data$bg.timestamp) ##Converting to date class
  data$bg.timestamp <- as.Date(data$bg.timestamp, format="%Y-%m-%d %H:%M:%S")
  
  data <- subset(data, bg.timestamp <= Sys.Date()) ##Remove Future readings from the dataset 
  data$bg <- as.numeric(as.character(data$bg)) ##Convert blood glucose to numeric
  return(data)
}
####

meanBg <- function(data) {
  
  averageList <- lapply(unique(data$genie.code), function(i) { ##For each unique genie Code ##Subset the usr's data
    subset(data, genie.code==i) -> usr.data 
    
    ##And break it up into 30 day time segments before and after the first meter sync(time when the glucometer syncs with the online db) Event
    subset(usr.data, bg.timestamp <= (min(mse.timestamp) - 90) & bg.timestamp > (min(mse.timestamp) - 120)) -> onetwenty.retro   
    subset(usr.data, bg.timestamp <= (min(mse.timestamp) - 60) & bg.timestamp > (min(mse.timestamp) - 90)) -> ninety.retro
    subset(usr.data, bg.timestamp <= (min(mse.timestamp) - 30) & bg.timestamp > (min(mse.timestamp) - 60)) -> sixty.retro 
    subset(usr.data, bg.timestamp <= min(mse.timestamp) & bg.timestamp > (min(mse.timestamp)-30)) -> thirty.retro; print(thirty.retro)
    subset(usr.data, bg.timestamp > min(mse.timestamp) & bg.timestamp <= (min(mse.timestamp) + 30)) -> thirty.day; 
    subset(usr.data, bg.timestamp > (min(mse.timestamp) + 30) & bg.timestamp <= (min(mse.timestamp) + 60)) -> sixty.day
    subset(usr.data, bg.timestamp > (min(mse.timestamp) + 60) & bg.timestamp <= (min(mse.timestamp) + 90)) -> ninety.day
    subset(usr.data, bg.timestamp > (min(mse.timestamp) + 90) & bg.timestamp <= (min(mse.timestamp) + 120)) -> onetwenty.day
    subset(usr.data, bg.timestamp > (min(mse.timestamp) + 120) & bg.timestamp <= (min(mse.timestamp) + 150)) -> onefifty.day
    subset(usr.data, bg.timestamp > (min(mse.timestamp) + 150) & bg.timestamp <= (min(mse.timestamp) + 180)) -> oneeighty.day
    
    ##Create a list of all time
    list(onetwenty.retro, ninety.retro, sixty.retro, thirty.retro, thirty.day, sixty.day, ninety.day, onetwenty.day, onefifty.day, oneeighty.day) -> timeList 
    
    avgusr <- lapply(1:10, function (i) {
      if (nrow(timeList[[i]]) > 5) { ##if there are > 5 readings in the timebucket 
        return(mean(timeList[[i]]$bg)) ##return the average of the blood glucose readings within the timebucket 
      } else {return(NA)} ##if there are <= 5 readings, return NA for the timebucket
    })
    
   t(as.data.frame(as.matrix(avgusr))) -> averages
    
    c("-120-Day", "-90-Day", "-60-Day", "-30-Day", "30-Day", "60-Day", "90-Day", "120-Day", "150-Day", "180-Day") -> colnames(averages); i -> rownames(averages)
    as.data.frame(averages) -> averages
    return(averages)
  })
  ldply(averageList, rbind) -> average.df
  unique(data$genie.code) -> rownames(average.df)
  
  for (i in seq(ncol(average.df))) {
    average.df[,i] <- as.numeric(as.character(average.df[,i])) 
  } ##Convert data to numeric (ldply changes data class)
  
  return(average.df)
}

#############

varBg <- function(data) {
  
  vList <- lapply(unique(data$genie.code), function(i) { ##For each unique genie Code ##Subset the usr's data
    subset(data, genie.code==i) -> usr.data 
    
    ##And break it up into 30 day time segments before and after the first MeterSync Event
    subset(usr.data, bg.timestamp <= (min(mse.timestamp) - 90) & bg.timestamp > (min(mse.timestamp) - 120)) -> onetwenty.retro   
    subset(usr.data, bg.timestamp <= (min(mse.timestamp) - 60) & bg.timestamp > (min(mse.timestamp) - 90)) -> ninety.retro
    subset(usr.data, bg.timestamp <= (min(mse.timestamp) - 30) & bg.timestamp > (min(mse.timestamp) - 60)) -> sixty.retro 
    subset(usr.data, bg.timestamp <= min(mse.timestamp) & bg.timestamp > (min(mse.timestamp)-30)) -> thirty.retro
    subset(usr.data, bg.timestamp > min(mse.timestamp) & bg.timestamp <= (min(mse.timestamp) + 30)) -> thirty.day
    subset(usr.data, bg.timestamp > (min(mse.timestamp) + 30) & bg.timestamp <= (min(mse.timestamp) + 60)) -> sixty.day
    subset(usr.data, bg.timestamp > (min(mse.timestamp) + 60) & bg.timestamp <= (min(mse.timestamp) + 90)) -> ninety.day
    subset(usr.data, bg.timestamp > (min(mse.timestamp) + 90) & bg.timestamp <= (min(mse.timestamp) + 120)) -> onetwenty.day
    subset(usr.data, bg.timestamp > (min(mse.timestamp) + 120) & bg.timestamp <= (min(mse.timestamp) + 150)) -> onefifty.day
    subset(usr.data, bg.timestamp > (min(mse.timestamp) + 150) & bg.timestamp <= (min(mse.timestamp) + 180)) -> oneeighty.day
    
    ##Create a list of all time bucket objects
    list(onetwenty.retro, ninety.retro, sixty.retro, thirty.retro, thirty.day, sixty.day, ninety.day, onetwenty.day, onefifty.day, oneeighty.day) -> timeList 
    
    varusr <- lapply(1:10, function (i) {
      if (nrow(timeList[[i]]) > 5) { ##if there are > 5 readings within the timebucket 
        return(sd(timeList[[i]]$bg)) ##return the standard deviation of the blood glucose readings within the timebucket
      } else {return(NA)} ##if there are <= 5 readings, return NA for the timebucket
    })
    
    t(as.data.frame(as.matrix(varusr)))  -> variability
    
    c("-120-Day", "-90-Day", "-60-Day", "-30-Day", "30-Day", "60-Day", "90-Day", "120-Day", "150-Day", "180-Day") -> colnames(variability); i -> rownames(variability)
    as.data.frame(variability) -> variability
    return(variability)
  })
  
  ldply(vList, rbind) -> var.df
  unique(data$genie.code) -> rownames(var.df)
  
  for (i in seq(ncol(var.df))) {
    var.df[,i] <- as.numeric(as.character(var.df[,i])) ##Convert data to numeric (ldply changes data class)
  } 
  
  return(var.df)
}


##hypr Blood Glucose Function
hyBg <- function(data) {
  
  hyprList <- lapply(unique(data$genie.code), function(i) { ##For each unique genie Code
    subset(data, genie.code==i) -> usr.data ##Subset the usr's data
    
    ##And break it up into 30 day time segments before and after the first MeterSync Event    
    subset(usr.data, bg.timestamp <= (min(mse.timestamp) - 90) & bg.timestamp > (min(mse.timestamp) - 120)) -> onetwenty.retro    
    subset(usr.data, bg.timestamp <= (min(mse.timestamp) - 60) & bg.timestamp > (min(mse.timestamp) - 90)) -> ninety.retro
    subset(usr.data, bg.timestamp <= (min(mse.timestamp) - 30) & bg.timestamp > (min(mse.timestamp) - 60)) -> sixty.retro 
    subset(usr.data, bg.timestamp <= min(mse.timestamp) & bg.timestamp > (min(mse.timestamp)-30)) -> thirty.retro
    subset(usr.data, bg.timestamp > min(mse.timestamp) & bg.timestamp <= (min(mse.timestamp) + 30)) -> thirty.day
    subset(usr.data, bg.timestamp > (min(mse.timestamp) + 30) & bg.timestamp <= (min(mse.timestamp) + 60)) -> sixty.day
    subset(usr.data, bg.timestamp > (min(mse.timestamp) + 60) & bg.timestamp <= (min(mse.timestamp) + 90)) -> ninety.day
    subset(usr.data, bg.timestamp > (min(mse.timestamp) + 90) & bg.timestamp <= (min(mse.timestamp) + 120)) -> onetwenty.day
    subset(usr.data, bg.timestamp > (min(mse.timestamp) + 120) & bg.timestamp <= (min(mse.timestamp) + 150)) -> onefifty.day
    subset(usr.data, bg.timestamp > (min(mse.timestamp) + 150) & bg.timestamp <= (min(mse.timestamp) + 180)) -> oneeighty.day
    
    ##Create a list of all time bucket objects
    list(onetwenty.retro, ninety.retro, sixty.retro, thirty.retro, thirty.day, sixty.day, ninety.day, onetwenty.day, onefifty.day, oneeighty.day) -> timeList 
    
    ##For each time bucket
    timeList250 <- lapply(1:10, function (i) {
      if (nrow(timeList[[i]]) > 5) { ##if there are > 5 readings in the timebucket
        return(subset(timeList[[i]], bg >= 250)) ##return the readings that are above 250
      } else {return(NA)} ##if there are <= 5 readings, return NA for the timebucket
    })
    
    lapply(timeList250, nrow) -> timeListrow ##Count hypr events per time bucket and return value
    t(as.data.frame(as.matrix(timeListrow))) -> hypr
    
    c("-120-Day", "-90-Day", "-60-Day", "-30-Day", "30-Day", "60-Day", "90-Day", "120-Day", "150-Day", "180-Day") -> colnames(hypr); i -> rownames(hypr)
    as.data.frame(hypr) -> hypr
    return(hypr)
  })
  
  ldply(hyprList, rbind) -> hypr.df
  unique(data$genie.code) -> rownames(hypr.df)
  
  for (i in seq(ncol(hypr.df))) {
    hypr.df[,i] <- as.numeric(as.character(hypr.df[,i])) 
  } ##Convert data to numeric (ldply changes data class)
  
  return(hypr.df)
}

