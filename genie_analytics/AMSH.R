read.csv("readings.csv", header=TRUE, sep=",") -> readings

#removing the first column
readings = subset(readings, select = -X)

processData <- function(data) {
  names(data) <- c("glooko.code", "guid", "mse.timestamp", "bg", "bg.timestamp") ##Rename readings columns
  
  data$mse.timestamp <- as.character(data$mse.timestamp) ##Convert to date class
  data$mse.timestamp <- as.Date(data$mse.timestamp, format="%Y-%m-%d %H:%M:%S")
  data$bg.timestamp <- as.character(data$bg.timestamp) ##Convert to date class
  data$bg.timestamp <- as.Date(data$bg.timestamp, format="%Y-%m-%d %H:%M:%S")
  
  data <- subset(data, bg.timestamp <= Sys.Date()) ##Remove Future readings from dataset 
  data$bg <- as.numeric(as.character(data$bg)) ##Convert BG to numeric
  return(data)
}
#############

meanBg <- function(data) {
  
  averageList <- lapply(unique(data$glooko.code), function(i) { ##For each unique Glooko Code
    subset(data, glooko.code==i) -> user.data ##Subset the user's data
    
    ##And break it up into 30 day time segments before and after the first MeterSync Event
    subset(user.data, bg.timestamp <= (min(mse.timestamp) - 90) & bg.timestamp > (min(mse.timestamp) - 120)) -> onetwenty.retro   
    subset(user.data, bg.timestamp <= (min(mse.timestamp) - 60) & bg.timestamp > (min(mse.timestamp) - 90)) -> ninety.retro
    subset(user.data, bg.timestamp <= (min(mse.timestamp) - 30) & bg.timestamp > (min(mse.timestamp) - 60)) -> sixty.retro 
    subset(user.data, bg.timestamp <= min(mse.timestamp) & bg.timestamp > (min(mse.timestamp)-30)) -> thirty.retro; print(thirty.retro)
    subset(user.data, bg.timestamp > min(mse.timestamp) & bg.timestamp <= (min(mse.timestamp) + 30)) -> thirty.day; 
    subset(user.data, bg.timestamp > (min(mse.timestamp) + 30) & bg.timestamp <= (min(mse.timestamp) + 60)) -> sixty.day
    subset(user.data, bg.timestamp > (min(mse.timestamp) + 60) & bg.timestamp <= (min(mse.timestamp) + 90)) -> ninety.day
    subset(user.data, bg.timestamp > (min(mse.timestamp) + 90) & bg.timestamp <= (min(mse.timestamp) + 120)) -> onetwenty.day
    subset(user.data, bg.timestamp > (min(mse.timestamp) + 120) & bg.timestamp <= (min(mse.timestamp) + 150)) -> onefifty.day
    subset(user.data, bg.timestamp > (min(mse.timestamp) + 150) & bg.timestamp <= (min(mse.timestamp) + 180)) -> oneeighty.day
    
    ##Create a list of all time bucket objects
    list(onetwenty.retro, ninety.retro, sixty.retro, thirty.retro, thirty.day, sixty.day, ninety.day, onetwenty.day, onefifty.day, oneeighty.day) -> timeList 
    
    averageUser <- lapply(1:10, function (i) {
      if (nrow(timeList[[i]]) > 5) { ##if there are > 5 readings in the timebucket 
        return(mean(timeList[[i]]$bg)) ##return the average of the blood glucose readings within the timebucket 
      } else {return(NA)} ##if there are <= 5 readings, return NA for the timebucket
    })
    
    t(as.data.frame(as.matrix(averageUser))) -> averages
    
    c("-120-Day", "-90-Day", "-60-Day", "-30-Day", "30-Day", "60-Day", "90-Day", "120-Day", "150-Day", "180-Day") -> colnames(averages); i -> rownames(averages)
    as.data.frame(averages) -> averages
    return(averages)
  })
  ldply(averageList, rbind) -> average.df
  unique(data$glooko.code) -> rownames(average.df)
  
  for (i in seq(ncol(average.df))) {
    average.df[,i] <- as.numeric(as.character(average.df[,i])) 
  } ##Convert data to numeric (ldply changes data class)
  
  return(average.df)
}

#############

varBg <- function(data) {
  
  varList <- lapply(unique(data$glooko.code), function(i) { ##For each unique Glooko Code
    subset(data, glooko.code==i) -> user.data ##Subset the user's data
    
    ##And break it up into 30 day time segments before and after the first MeterSync Event
    subset(user.data, bg.timestamp <= (min(mse.timestamp) - 90) & bg.timestamp > (min(mse.timestamp) - 120)) -> onetwenty.retro   
    subset(user.data, bg.timestamp <= (min(mse.timestamp) - 60) & bg.timestamp > (min(mse.timestamp) - 90)) -> ninety.retro
    subset(user.data, bg.timestamp <= (min(mse.timestamp) - 30) & bg.timestamp > (min(mse.timestamp) - 60)) -> sixty.retro 
    subset(user.data, bg.timestamp <= min(mse.timestamp) & bg.timestamp > (min(mse.timestamp)-30)) -> thirty.retro
    subset(user.data, bg.timestamp > min(mse.timestamp) & bg.timestamp <= (min(mse.timestamp) + 30)) -> thirty.day
    subset(user.data, bg.timestamp > (min(mse.timestamp) + 30) & bg.timestamp <= (min(mse.timestamp) + 60)) -> sixty.day
    subset(user.data, bg.timestamp > (min(mse.timestamp) + 60) & bg.timestamp <= (min(mse.timestamp) + 90)) -> ninety.day
    subset(user.data, bg.timestamp > (min(mse.timestamp) + 90) & bg.timestamp <= (min(mse.timestamp) + 120)) -> onetwenty.day
    subset(user.data, bg.timestamp > (min(mse.timestamp) + 120) & bg.timestamp <= (min(mse.timestamp) + 150)) -> onefifty.day
    subset(user.data, bg.timestamp > (min(mse.timestamp) + 150) & bg.timestamp <= (min(mse.timestamp) + 180)) -> oneeighty.day
    
    ##Create a list of all time bucket objects
    list(onetwenty.retro, ninety.retro, sixty.retro, thirty.retro, thirty.day, sixty.day, ninety.day, onetwenty.day, onefifty.day, oneeighty.day) -> timeList 
    
    varUser <- lapply(1:10, function (i) {
      if (nrow(timeList[[i]]) > 5) { ##if there are > 5 readings within the timebucket 
        return(sd(timeList[[i]]$bg)) ##return the standard deviation of the blood glucose readings within the timebucket
      } else {return(NA)} ##if there are <= 5 readings, return NA for the timebucket
    })
    
    t(as.data.frame(as.matrix(varUser)))  -> variability
    
    c("-120-Day", "-90-Day", "-60-Day", "-30-Day", "30-Day", "60-Day", "90-Day", "120-Day", "150-Day", "180-Day") -> colnames(variability); i -> rownames(variability)
    as.data.frame(variability) -> variability
    return(variability)
  })
  
  ldply(varList, rbind) -> var.df
  unique(data$glooko.code) -> rownames(var.df)
  
  for (i in seq(ncol(var.df))) {
    var.df[,i] <- as.numeric(as.character(var.df[,i])) 
  } ##Convert data to numeric (ldply changes data class)
  
  return(var.df)
}


##Hyper Blood Glucose Function - use on processed data
hyperBg <- function(data) {
  
  hyperList <- lapply(unique(data$glooko.code), function(i) { ##For each unique Glooko Code
    subset(data, glooko.code==i) -> user.data ##Subset the user's data
    
    ##And break it up into 30 day time segments before and after the first MeterSync Event    
    subset(user.data, bg.timestamp <= (min(mse.timestamp) - 90) & bg.timestamp > (min(mse.timestamp) - 120)) -> onetwenty.retro    
    subset(user.data, bg.timestamp <= (min(mse.timestamp) - 60) & bg.timestamp > (min(mse.timestamp) - 90)) -> ninety.retro
    subset(user.data, bg.timestamp <= (min(mse.timestamp) - 30) & bg.timestamp > (min(mse.timestamp) - 60)) -> sixty.retro 
    subset(user.data, bg.timestamp <= min(mse.timestamp) & bg.timestamp > (min(mse.timestamp)-30)) -> thirty.retro
    subset(user.data, bg.timestamp > min(mse.timestamp) & bg.timestamp <= (min(mse.timestamp) + 30)) -> thirty.day
    subset(user.data, bg.timestamp > (min(mse.timestamp) + 30) & bg.timestamp <= (min(mse.timestamp) + 60)) -> sixty.day
    subset(user.data, bg.timestamp > (min(mse.timestamp) + 60) & bg.timestamp <= (min(mse.timestamp) + 90)) -> ninety.day
    subset(user.data, bg.timestamp > (min(mse.timestamp) + 90) & bg.timestamp <= (min(mse.timestamp) + 120)) -> onetwenty.day
    subset(user.data, bg.timestamp > (min(mse.timestamp) + 120) & bg.timestamp <= (min(mse.timestamp) + 150)) -> onefifty.day
    subset(user.data, bg.timestamp > (min(mse.timestamp) + 150) & bg.timestamp <= (min(mse.timestamp) + 180)) -> oneeighty.day
    
    ##Create a list of all time bucket objects
    list(onetwenty.retro, ninety.retro, sixty.retro, thirty.retro, thirty.day, sixty.day, ninety.day, onetwenty.day, onefifty.day, oneeighty.day) -> timeList 
    
    ##For each time bucket
    timeList250 <- lapply(1:10, function (i) {
      if (nrow(timeList[[i]]) > 5) { ##if there are > 5 readings in the timebucket
        return(subset(timeList[[i]], bg >= 250)) ##return the readings that are above 250
      } else {return(NA)} ##if there are <= 5 readings, return NA for the timebucket
    })
    
    lapply(timeList250, nrow) -> timeListrow ##Count hyper events per time bucket and return value
    t(as.data.frame(as.matrix(timeListrow))) -> hyper
    
    c("-120-Day", "-90-Day", "-60-Day", "-30-Day", "30-Day", "60-Day", "90-Day", "120-Day", "150-Day", "180-Day") -> colnames(hyper); i -> rownames(hyper)
    as.data.frame(hyper) -> hyper
    return(hyper)
  })
  
  ldply(hyperList, rbind) -> hyper.df
  unique(data$glooko.code) -> rownames(hyper.df)
  
  for (i in seq(ncol(hyper.df))) {
    hyper.df[,i] <- as.numeric(as.character(hyper.df[,i])) 
  } ##Convert data to numeric (ldply changes data class)
  
  return(hyper.df)
}

##Test Rate function - used on processed data
testRate <- function(data) {
  
  testList <- lapply(unique(data$glooko.code), function(i) { ##For each unique Glooko Code
    subset(data, glooko.code==i) -> user.data ##Subset user's data
    
    ##And break it up into 30 day time segments before and after the first MeterSync Event    
    subset(user.data, bg.timestamp <= (min(mse.timestamp) - 90) & bg.timestamp > (min(mse.timestamp) - 120)) -> onetwenty.retro   
    subset(user.data, bg.timestamp <= (min(mse.timestamp) - 60) & bg.timestamp > (min(mse.timestamp) - 90)) -> ninety.retro
    subset(user.data, bg.timestamp <= (min(mse.timestamp) - 30) & bg.timestamp > (min(mse.timestamp) - 60)) -> sixty.retro 
    subset(user.data, bg.timestamp <= min(mse.timestamp) & bg.timestamp > (min(mse.timestamp)-30)) -> thirty.retro
    subset(user.data, bg.timestamp > min(mse.timestamp) & bg.timestamp <= (min(mse.timestamp) + 30)) -> thirty.day
    subset(user.data, bg.timestamp > (min(mse.timestamp) + 30) & bg.timestamp <= (min(mse.timestamp) + 60)) -> sixty.day
    subset(user.data, bg.timestamp > (min(mse.timestamp) + 60) & bg.timestamp <= (min(mse.timestamp) + 90)) -> ninety.day
    subset(user.data, bg.timestamp > (min(mse.timestamp) + 90) & bg.timestamp <= (min(mse.timestamp) + 120)) -> onetwenty.day
    subset(user.data, bg.timestamp > (min(mse.timestamp) + 120) & bg.timestamp <= (min(mse.timestamp) + 150)) -> onefifty.day
    subset(user.data, bg.timestamp > (min(mse.timestamp) + 150) & bg.timestamp <= (min(mse.timestamp) + 180)) -> oneeighty.day
    
    list(onetwenty.retro, ninety.retro, sixty.retro, thirty.retro, thirty.day, sixty.day, ninety.day, onetwenty.day, onefifty.day, oneeighty.day) -> timeList 
    
    ##Create a list of all time bucket objects
    timeList <- lapply(1:10, function (i) {
      if (nrow(timeList[[i]]) > 5) { ##if there are > 5 readings in the timebucekt
        as.numeric(difftime(max(timeList[[i]]$bg.timestamp), min(timeList[[i]]$bg.timestamp), "days")) -> x ##Find the # of days tests occured
        return(length(timeList[[i]]$bg)/x) ##Divide total # of tests by # of days of testing
      } else {return(NA)} ##if there are < 5 readings, return NA for the timebucket
    })
    
    t(as.data.frame(as.matrix(timeList))) -> test
    
    c("-120-Day", "-90-Day", "-60-Day", "-30-Day", "30-Day", "60-Day", "90-Day", "120-Day", "150-Day", "180-Day") -> colnames(test); i -> rownames(test)
    as.data.frame(test) -> test
    return(test)
  })
  
  ldply(testList, rbind) -> test.df
  unique(data$glooko.code) -> rownames(test.df)
  
  for (i in seq(ncol(test.df))) {
    test.df[,i] <- as.numeric(as.character(test.df[,i])) 
  } ##Convert data to numeric (ldply changes data class)
  
  return(test.df)
}



