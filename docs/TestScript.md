# Savvy To Do Test Script

## Loading the sample data file
> Load SampleData.xml

## Testing of commands
We will now go through the commands to be tested.

### Help command
We can open up the Help window by:
> help

### Add command
Now, we will add some tasks to the task manager:

> add My Test Floating Task <br />
> add My Test Event dt/now to tomorrow

We should see the tasks appearing on their respective list views.

### Find command

We can find tasks by their names.

> find floating task

We should see the task that we just added in the floating task list view.

### Edit command

We can edit the task to give it a description:
> edit F1 d/my new description

We can also change it's priority:
> edit F1 p/Low

or change its name:
> edit F1 My New Floating Task

The results should be updated upon successfully execution of the commands.

### List command
Next, we can list all our tasks by:
> list

We can also list by categories or priorities
> list p/high

This lists all the tasks with 'HIGH' priority
> list c/lifestyle

This lists all the tasks with 'lifestyle' priority

### Select command
We can also select a task to view more details:
> select 1

We should see the task card being highlighted and the task details being shown on the right pane.

We can also select tasks by clicking on their cards.

### Mark and Unmark command
To mark a task as completed:
> mark 1

The task should be updated as "completed".

Similarly, to unmark it:
> unmark 1

The same task should be reverted to as "ongoing".

We can also mark and unmark multiple tasks in a single command:
>mark 1 2 3

### Delete command
To delete a task:
> delete 1

The task originally with index 1 should be deleted.

### Clear command
We can also clear all the tasks in the task manager:
> clear

The task manager should be empty

### Undo and Redo command
To undo the previous change:
> undo

Similarly, to redo:
> redo

If there is nothing else to redo:
> redo

The task manager should indicate that there is nothing to redo.

### Keyboard shortcuts
We can also launch several commands through keyboard shortcuts.
> Ctrl+Shift+Z will undo the change again <br>
> Ctrl+Shift+Y will redo the change again <br>
> Ctrl+L will list all the tasks <br>
> Ctrl+D will clear all the tasks in the task manager <br>
> Ctrl+H or F1 will launch the Help window.

### Exit Command
Lastly, to quit the app, we can either type
> exit <br>

or use the keyboard shortcut <br />

> Ctrl+Q

End of Test Script <br />
By CS2103 F12-B1
* Goh Jing Loon
* Joel Lim jing
* Wang Si Qi
* Yee Jian Feng, Eric