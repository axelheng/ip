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
     Oops: I don't recognize that command. Try todo, deadline, event, list, mark, unmark, or bye.
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
