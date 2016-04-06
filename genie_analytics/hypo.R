
prsHypoData <- function(data) {
  
  names(data) <- c("genie.code", "guid", "mse.timestamp", "bg", "bg.timestamp")
  
  data$mse.timestamp <- as.character(data$mse.timestamp) ##Convert to date class
  data$mse.timestamp <- as.POSIXct(data$mse.timestamp, format="%Y-%m-%d %H:%M:%S")
  data$bg.timestamp <- as.character(data$bg.timestamp) ##Convert to date class
  data$bg.timestamp <- as.POSIXct(data$bg.timestamp, format="%Y-%m-%d %H:%M:%S")
  
  data <- subset(data, bg.timestamp <= as.POSIXct(Sys.Date())) ##Remove Future readings from dataset 
  data$bg <- as.numeric(as.character(data$bg)) ##Convert BG to numeric
  return(data)
}

#Function to find the Hypo Events
hypoEvents <- function(data) {
  
  dataList <- lapply(unique(data$genie.code), function(i) {
    subset(data, genie.code==i) -> x ##Subset data by genie code ##Subset readings below 70  3564    5
    subset(x, bg < 70) -> y 
    
    z <- sort(as.numeric(y$bg.timestamp)) ##Order readings from most distant -> most recent
    hypo.events <- vector("numeric", length(z))  
    
    for (i in seq(length(z))) { ##for each hypo reading per genie.code
      length(subset(hypo.events, (z[i]-7200) <= hypo.events & hypo.events <= z[i])) -> a
      hypo.events[i] <- ifelse(a==0, z[i], NA) ##If none, add reading to hypo events dataframe
    }
    
    hypo.events <- as.POSIXct(hypo.events, origin="1970-01-01") ##Convert timestamps, numeric -> POSIXct
    subset(x, bg >= 70 | bg.timestamp %in% hypo.events) -> hypo.final ##Exclude hypo readings outside of hypo events dataframe
    return(hypo.final)
  })
  
  ldply(dataList, rbind) -> data
 #dataList ->data
  return(data)
}

hypoBg <- function(data) {
  
  hypoList <- lapply(unique(data$genie.code), function(i) { ##For each unique genieCode ##Create a dataframe with that genieCode's readings data
    subset(data, genie.code==i) -> user.data 
    
    ##Subset this dataframe into time buckets
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
    
    
    timeList70 <- lapply(1:10, function (i) {
      if (nrow(timeList[[i]]) > 5) { ##if there are > 5 readings ##return the readings that are below 70
        return(subset(timeList[[i]], bg < 70)) 
      } else {return(NA)} ##if there are < 5 readings, return NA for the timebucket
    })
    
    lapply(timeList70, nrow) -> timeListrow ##Count hyper events per time bucket
    t(as.data.frame(as.matrix(timeListrow))) -> hypo
    
    c("-120-Day", "-90-Day", "-60-Day", "-30-Day", "30-Day", "60-Day", "90-Day", "120-Day", "150-Day", "180-Day") -> colnames(hypo); i -> rownames(hypo)
    as.data.frame(hypo) -> hypo
    return(hypo)
  })
  
  ldply(hypoList, rbind) -> hypo.df
  unique(data$genie.code) -> rownames(hypo.df)
  
  for (i in seq(ncol(hypo.df))) {
    hypo.df[,i] <- as.numeric(as.character(hypo.df[,i])) 
  }
  
  return(hypo.df)
}
