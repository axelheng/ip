# UI Test Plan

Add one `##` section per console UI test. Tests run in document order and stop at the first failure.

## Create and list typed tasks

**Aim:** Verify that Jarvis creates ToDos, Deadlines, and Events, displays their type and date/time strings, lists them polymorphically, and exits correctly.

**Command:**
```sh
java -cp out/production/ip Jarvis
```

**Inputs:**
```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
bye
```

**Expected output:**
```text
____________________________________________________________
Jarvis
Hello! I'm Jarvis.
What can I do for you?
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [T][ ] borrow book
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [D][ ] return book (by: Sunday)
     Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [E][ ] project meeting (from: Mon 2pm to: 4pm)
     Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] borrow book
     2.[D][ ] return book (by: Sunday)
     3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Reject invalid delete requests

**Aim:** Verify that delete reports missing, non-numeric, and out-of-range task numbers without changing the task list, including when the list is empty.

**Command:**
```sh
java -cp out/production/ip Jarvis
```

**Inputs:**
```text
delete
delete nope
delete 1
todo keep this
delete 2
list
delete 1
list
bye
```

**Expected output:**
```text
____________________________________________________________
Jarvis
Hello! I'm Jarvis.
What can I do for you?
____________________________________________________________
____________________________________________________________
     Oops: Please provide a task number after delete.
____________________________________________________________
____________________________________________________________
     Oops: Please provide a valid task number after delete.
____________________________________________________________
____________________________________________________________
     Oops: There is no task with that number.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [T][ ] keep this
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Oops: There is no task with that number.
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] keep this
____________________________________________________________
____________________________________________________________
     Noted. I've removed this task:
       [T][ ] keep this
     Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Continue using the list after deletion

**Aim:** Verify that tasks can be added after deletion and that a later delete uses the current list numbering.

**Command:**
```sh
java -cp out/production/ip Jarvis
```

**Inputs:**
```text
todo first task
todo second task
delete 1
todo replacement task
list
delete 2
list
bye
```

**Expected output:**
```text
____________________________________________________________
Jarvis
Hello! I'm Jarvis.
What can I do for you?
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [T][ ] first task
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [T][ ] second task
     Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
     Noted. I've removed this task:
       [T][ ] first task
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [T][ ] replacement task
     Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] second task
     2.[T][ ] replacement task
____________________________________________________________
____________________________________________________________
     Noted. I've removed this task:
       [T][ ] replacement task
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] second task
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Recover from invalid input

**Aim:** Verify that Jarvis reports specific input errors, does not add invalid tasks, and continues processing later valid commands.

**Command:**
```sh
java -cp out/production/ip Jarvis
```

**Inputs:**
```text
todo
blah
deadline report
event meeting /from 2pm
mark
mark nope
todo buy milk
list
bye
```

**Expected output:**
```text
____________________________________________________________
Jarvis
Hello! I'm Jarvis.
What can I do for you?
____________________________________________________________
____________________________________________________________
     Oops: A todo description cannot be empty.
____________________________________________________________
____________________________________________________________
     Oops: I don't recognize that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.
____________________________________________________________
____________________________________________________________
     Oops: A deadline needs a description and a date, for example: deadline report /by Friday
____________________________________________________________
____________________________________________________________
     Oops: An event needs a description, start time, and end time, for example: event meeting /from 2pm /to 3pm
____________________________________________________________
____________________________________________________________
     Oops: Please provide a task number after mark.
____________________________________________________________
____________________________________________________________
     Oops: Please provide a valid task number after mark.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [T][ ] buy milk
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] buy milk
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Delete tasks and renumber the list

**Aim:** Verify that Jarvis deletes the selected task, reports the removed task and new count, and renumbers the remaining tasks.

**Command:**
```sh
java -cp out/production/ip Jarvis
```

**Inputs:**
```text
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
todo join sports club
todo borrow book
mark 1
mark 2
list
delete 3
list
bye
```

**Expected output:**
```text
____________________________________________________________
Jarvis
Hello! I'm Jarvis.
What can I do for you?
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [D][ ] return book (by: June 6th)
     Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
     Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [T][ ] join sports club
     Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [T][ ] borrow book
     Now you have 5 tasks in the list.
____________________________________________________________
____________________________________________________________
     Nice! I've marked this task as done:
       [X] read book
____________________________________________________________
____________________________________________________________
     Nice! I've marked this task as done:
       [X] return book
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] read book
     2.[D][X] return book (by: June 6th)
     3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
     4.[T][ ] join sports club
     5.[T][ ] borrow book
____________________________________________________________
____________________________________________________________
     Noted. I've removed this task:
       [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
     Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] read book
     2.[D][X] return book (by: June 6th)
     3.[T][ ] join sports club
     4.[T][ ] borrow book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
