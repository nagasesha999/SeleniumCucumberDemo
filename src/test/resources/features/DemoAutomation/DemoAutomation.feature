Feature: Demo Automation scenarios
  @DAP_001
  Scenario Outline: Demo Automation register page
    Given the user open "<demo>" application
    When the user verify page title "Automation Demo Site"
    Then the user verify rigister page fields
      | Full Name        |
      | Address          |
      | Email address    |
      | Phone            |
      | Gender           |
      | Hobbies          |
      | Languages        |
      | Skills           |
      | Country          |
      | Select Country   |
      | Date Of Birth    |
      | Password         |
      | Confirm Password |
    Examples:
      | demo                                           |
      | http://demo.automationtesting.in/Register.html |
      | http://demo.automationtesting.in/Register.html |
      | http://demo.automationtesting.in/Register.html |
      | http://demo.automationtesting.in/Register.html |
  @DAP_002
  Scenario Outline: Demo Automation register page
    Given the user open "<demo>" application
    When the user verify page title "Automation Demo Site"
    Then the user verify rigister page fields
      | Full Name        |
      | Address          |
      | Email address    |
      | Phone            |
      | Gender           |
      | Hobbies          |
      | Languages        |
      | Skills           |
      | Country          |
      | Select Country   |
      | Date Of Birth    |
      | Password         |
      | Confirm Password |
    
    Examples:
      | demo                                           |
      | http://demo.automationtesting.in/Register.html |