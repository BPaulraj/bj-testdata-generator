Feature: Sample Google Search

  Scenario: Search for Copilot on Google
    Given I open Google homepage
    When I search for "Copilot"
    Then the results page should show results for "Copilot"
