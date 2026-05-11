# BVA Analysis for Card



## Public interface for this class
- constructor(type, name) --> returns a Card object
- isType(type) --> boolean
- isName(type) --> boolean
- execute(gameState) --> calls the action



## Assumptions and notes:
- N/A



### Method under test: `isType()`
spaces: boolean

cases:
- returns False
- returns True
- some true other than 1 (N/A)
- neither true or false (N/A)

| test_Name                         | State of the System | Expected output | Implemented?       |
|-----------------------------------|---------------------|-----------------|--------------------|
| isType_SameType_ReturnsTrue       | same Type           | true            | :white_check_mark: |
| isType_DifferentType_ReturnsFalse | different Type      | false           | :white_check_mark: |



### Method under test: `isName()`
spaces: boolean

cases:
- returns False
- returns True
- some true other than 1 (N/A)
- neither true or false (N/A)

| test_Name                         | State of the System | Expected output | Implemented?       |
|-----------------------------------|---------------------|-----------------|--------------------|
| isType_SameName_ReturnsTrue       | same Name           | true            | :white_check_mark: |
| isType_DifferentName_ReturnsFalse | different Name      | false           | :white_check_mark: |



### Method under test: `execute()`
spaces: calls method (not in BVA catalog)

cases:
- calls the card action

| test_Name                         | State of the System           | Expected output | Implemented?       |
|-----------------------------------|-------------------------------|-----------------|--------------------|
| execute_CallsCardAction           | Card object calls Card action | void            | :white_check_mark: |



### Method under test: `setAction()`
spaces: sets attribute (not in BVA catalog)

cases:
- sets the card action

| test_Name                | State of the System          | Expected output | Implemented?       |
|--------------------------|------------------------------|-----------------|--------------------|
| setAction_setsCardAction | Card object sets Card action | void            | :white_check_mark: |
