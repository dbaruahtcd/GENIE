##Data to be processed by hypo function must be formatted as: 
##Col1: Glooko Code, Col2: MSE GUID, Col3: MSE Timestamp, Col4: BG Value, Col5: BG Timestamp

processHypoData <- function(data) {
  
  names(data) <- c("glooko.code", "guid", "mse.timestamp", "bg", "bg.timestamp")
  
  data$mse.timestamp <- as.character(data$mse.timestamp) ##Convert to date class
  data$mse.timestamp <- as.POSIXct(data$mse.timestamp, format="%Y-%m-%d %H:%M:%S")
  data$bg.timestamp <- as.character(data$bg.timestamp) ##Convert to date class
  data$bg.timestamp <- as.POSIXct(data$bg.timestamp, format="%Y-%m-%d %H:%M:%S")
  
  data <- subset(data, bg.timestamp <= as.POSIXct(Sys.Date())) ##Remove Future readings from dataset 
  data$bg <- as.numeric(as.character(data$bg)) ##Convert BG to numeric
  return(data)
}

#Function to narrow down Hypo Events - run this on processed hypo data
hypoEvents <- function(data) {
  
  dataList <- lapply(unique(data$glooko.code), function(i) {
    subset(data, glooko.code==i) -> x ##Subset data by Glooko code
    subset(x, bg < 70) -> y ##Subset readings below 70  3564    5
    
    z <- sort(as.numeric(y$bg.timestamp)) ##Order readings from most distant -> most recent
    hypo.events <- vector("numeric", length(z)) ##Create empty hypo events vector 
    
    for (i in seq(length(z))) { ##for each hypo reading per glooko.code
      length(subset(hypo.events, (z[i]-7200) <= hypo.events & hypo.events <= z[i])) -> a
      ##Find number of hypo readings within previous 2 hours in the hypo events dataframe
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


##Hypo BG function - use this on data processed by Hypo Events function
##Note: Timestamp data from Hypo Events processed object needs to be converted from 'posix' -> 'date' for function hypoBg function

hypoBg <- function(data) {
  
  hypoList <- lapply(unique(data$glooko.code), function(i) { ##For each unique GlookoCode
    subset(data, glooko.code==i) -> user.data ##Create a dataframe with that GlookoCode's readings data
    
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
    
    ##For each time bucket
    timeList70 <- lapply(1:10, function (i) {
      if (nrow(timeList[[i]]) > 5) { ##if there are > 5 readings
        return(subset(timeList[[i]], bg < 70)) ##return the readings that are below 70
      } else {return(NA)} ##if there are < 5 readings, return NA for the timebucket
    })
    
    lapply(timeList70, nrow) -> timeListrow ##Count hyper events per time bucket
    t(as.data.frame(as.matrix(timeListrow))) -> hypo
    
    c("-120-Day", "-90-Day", "-60-Day", "-30-Day", "30-Day", "60-Day", "90-Day", "120-Day", "150-Day", "180-Day") -> colnames(hypo); i -> rownames(hypo)
    as.data.frame(hypo) -> hypo
    return(hypo)
  })
  
  ldply(hypoList, rbind) -> hypo.df
  unique(data$glooko.code) -> rownames(hypo.df)
  
  for (i in seq(ncol(hypo.df))) {
    hypo.df[,i] <- as.numeric(as.character(hypo.df[,i])) 
  }
  
  return(hypo.df)
}
