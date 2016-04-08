
hypogylcemiaEvents <- function(data, hypo.threshold) { ## Function for calculating hypoglycemia events
  
  require(plyr, quietly=TRUE)
  
  dataList <- lapply(unique(data$genie.code), function(i) {
    subset(data, genie.code==i) -> x ## Subset the data by genie code
    subset(x, bglucose < hypo.threshold) -> y ## Subset all the readings below hypoglycemia threshold
    
    z <- sort(as.numeric(y$bglucose.timestamp)) ## most recent readings
    hypo.events <- vector("numeric", length(z)) ## Creating hypoglycemic events vector 
    
    for (i in seq(length(z))) { ## for each hypo reading per genie.code
      length(subset(hypo.events, (z[i]-7200) <= hypo.events & hypo.events <= z[i])) -> a
      ## Calculating number of readings within previous 2 hours
      hypo.events[i] <- ifelse(a==0, z[i], NA) ## If none, add readings to hypo events dataframe
    }
    
    hypo.events <- as.POSIXct(hypo.events, origin="1970-01-01") ##Convert timestamps, numeric -> POSIXct
    subset(x, bglucose >= hypo.threshold | bglucose.timestamp %in% hypo.events) -> hypo.final ##Exclude hypo readings outside of hypo events dataframe
    return(hypo.final)
  })
  
  ldply(dataList, rbind) -> data
  return(data)
}
