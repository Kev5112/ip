# AlterEgo User Guide

![Product Screenshot](Ui.png)

AlterEgo is a **desktop chatbot app** for managing tasks and contacts, optimized for use via a **Command Line Interface (CLI)** while having the benefits of a **Graphical User Interface (GUI)**.

# Features

### 1. 📋 Task Management

#### a. Add task:
- **Todo**: `todo {description}`
    - Example: `todo read book`
- **Deadline**: `{description} /by {date, dd-MM-yyyy format}`
    - Example: `deadline return book /by 25-03-2024`
- **Event**: `{description} /from {date, dd-MM-yyyy format} /to {date, dd-MM-yyyy format}`
    - Example: `event camp /from 01-04-2024 /to 05-04-2024`
    - ⚠️ Note: Overlapping events will trigger a warning

#### b. Delete task:
- `delete t{index}`
    - Example: `delete t1`

#### c. Update task doneness:
- **Mark as done**: `mark {index}`
    - Example: `mark 1`
- **Mark as not done**: `unmark {index}`
    - Example: `unmark 1`

#### d. Find task:
- `find {keyword}`
    - Keyword can be any part of the task label, including date
    - Example: `find book`, `find 25-03`
#### e. View all task:
- `list`

### 2. 👥 Contact Management

#### a. Add contact:
- `contact {name} /as {relationship}`
    - Example: `contact John /as Friend`

#### b. Delete contact:
- `delete c{index}`
    - Example: `delete c1`

#### c. Assign contact for a task:
- `assign {index} /to {name}`
    - Example: `assign 1 /to John`

#### d. View all contacts:
- `contactlist`
    - Example: `contactlist`

### 3. 🛠️ Miscellaneous

#### a. Show all commands:
- `help`
    - Example: `help`

#### b. Clear all data:
- `clear`
    - ⚠️ Warning: This permanently deletes all tasks and contacts from storage
    - Example: `clear`

#### c. Exit application:
- `bye`
    - Example: `bye`

## Expected outcome

```
Got it. I've added this task:
 [T][ ] read book
Now you have 1 tasks in the list.
```
