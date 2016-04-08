library(shiny)

# UI for the application
shinyUI(fluidPage(
  
  # Title
  titlePanel("Genie Hypoglycemic Calculator"),
  
  # Slider input
  sidebarLayout(
    sidebarPanel(
      
      helpText("Calculate the number of Hypoglycemic Events versus Hypogycemic readings experienced by the Genie diabetic population"),
      
      numericInput("InHypo", "Blood Glucose Level considered Hypoglycemic (mg/dL)", 70),
      
     
      actionButton("action", label = "Go!")
    ),
    
    mainPanel(
      tabsetPanel(
        tabPanel("Summary", 
                 h5("Total Hypoglycemic Readings", align="center"),
                 verbatimTextOutput("hypoglycemia.readings"),
                 h5("Total Hypoglycemic Events", align="center"),
                 verbatimTextOutput("hypoglycemia.events")),
        tabPanel("Documentation",
                 HTML("The Hypoglyemia Events Calculator measures and returns the number of hypoglyemic readings and events at a particular hypoglycemic threshold. The American Diabetes Association's standard definition of hypoglycemia is 70 mg/dL or lower. In order to use this application, enter a blood glucose value ( between 20-500 ) and press Go.")
        )
        
      )
    )
  ))
)