library(shiny)

source("hypogylcemiaEvents.R") 
source("formattedReadings.R") 

readings <- read.csv("readings.csv")
readings <- formattedReadings(readings[,c(2:6)])

shinyServer(function(input, output) {
  
  
  output$hypoglycemia.readings <- renderPrint({
    input$action ## Input from the user
    
    ## Selecting glucose values
    hypoglycemia.readings <- subset(readings, bglucose <= input$InHypo)
    
    ## Returning the appropriate readings
    return(nrow(hypoglycemia.readings))
  })
  
  output$hypoglycemia.events <- renderPrint({    
    ## Selecting glucose values
    hypoglycemia.readings <- subset(readings, bglucose <= input$InHypo)
    # Passing values to the hypoglycemiaEvents function
    hypoglycemia.events <- hypogylcemiaEvents(data = hypoglycemia.readings, hypo.threshold = input$InHypo)
    ## Returning the appropriate events
    return(nrow(hypoglycemia.events))
  })
  
 
})