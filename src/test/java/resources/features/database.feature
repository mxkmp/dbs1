Feature: Database Import
  As a user
  I want to update the table "test_zinsen" with a template

  Scenario: The Database is empty and i want to add 2 entries
    Given Template
    """
    t:test_zinsen
    i:1|Zins Plus-Konto|1.25|2013-01-01
    i:2|Zins Minus-Konto|0.01|2017-01-01
    """
    When Template is executed
    Then table "test_zinsen" should have 2 entries

  Scenario: Database contains 2 entries, i want to update one
    Given Template
        """
    t:test_zinsen
    u:1|Zins Plus-Konto|2.00|2013-01-01
    """
    When Template is executed
    Then Amount from Entry id = 1 is now 2

  Scenario: The Database contains 2 entries, i want to delete 1
    Given Template
    """
    t:test_zinsen
    d:2|Zins Minus-Konto|0.01|2017-01-01
    """
    When Template is executed
    Then the entry is deleted

  Scenario: The Database contains 1 entries, i want to add 2 more
    Given Template
      """
      t:test_zinsen
      i:2|Zins Minus-Konto|0.01|2017-01-01
      i:1|Zins Plus-Konto|1.25|2013-01-01
      """
    When Template is executed
    Then Exception will be fired and nothing will be written, count of the database is still 1

  Scenario: Database contains 1 Entry, i want to update a non existing one and add one
    Given Template
      """
      t:test_zinsen
      u:200|Zins Minus-Konto|0.01|2017-01-01
      i:200|Zins Minus-Konto|-0.01|2017-01-01
      """
    When Template is executed
    Then the database still contains 1 entry and no entry with id: 200
    #TODO: Nachfragen ob das in Ordnung ist, dass es klappt
    #Update wird nicht ausgeführt, gibt aber keinen Fehler aus, Insert wird ausgeführt




