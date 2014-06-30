Codefest 2014 team B repository
===========

# The team B

TEAM MEMBER | EXO TEAM
------------ | ------------- 
Vu Duy Tu | SC
Dang Van Binh | SC
Vi Quoc Hanh | SC
Nguyen Van Nghi | TQA

# How to build

	git clone git@github.com:exo-codefest/2014-team-B.git
	cd 2014-team-B
	mvn clean install
	
# Introduce

High effective Tasks Management Tool for individuals and collaboration
============

<img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/CodeFest3.1.jpg" width="800">

Overview:
--------------------
* High effective Tasks Management Tool for individual and collaboration aim to help users to have a simple, smart and helpful task management tool.
* The tool planned to be implemented in 2 best fit user interfaces: web interface, mobile application interface. Both usages should have the same behaviors and main features. Thus, it's always at one's finger tips or in one's pocket. 

* The tool's features target to be an effective tasks management tool for individuals by its smart algorithms:
    * algorithm to suggest things to be done via prioritizing methodologies
    * algorithm to optimize tasks should be done together
    * It also have on time reminding about: due date, task update,...
* The smart suggestions/organization will help assignees just to focus in tasks to do but not to think of organizing tasks queue which usually cause people to lost focus.
* While the tool success to be an effective assistant for individuals => many people will use it => they know about eXo.
* While it is well known to be a tool for individuals and collaboration => many groups of people will register and experiment eXo Cloud.

<img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/collaboration_2.jpg" width="800">

Noteworthy points:
--------------------
* Support Standalone (web interface or mobile app interface) & Collaboration
* Simple, Unique and friendliness interface; fit to mobile phones.
* Painless fully integrated to PLF, pluggable on PLF. Integrate and enhance eXo calendar, social space and activity also.
* Visualized timeline (like gantt chart) that is helpful for an over look of tasks done/running.
* Synchronization between: mobiles/computers.
* Smart tasks status checking and warning.
* Smart tasks suggestion.
* Smart tasks optimization.

Features:
--------------------


<table>
<thead>
  <tr>
    <th>#</th>
    <th>Subject</th>
    <th>Implementation Status</th>
  </tr>
</thead>
<tbody>
<tr><td>I.</td><td>Enhancement and Integration to eXo Calendar, Platform and Portal services</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/apply.png" alt="Implemented"/> </td></tr>
<tr><td>&nbsp;1.</td><td>-- Use all eXo Calendar standard services</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/apply.png" alt="Implemented"/> </td></tr>
<tr><td>&nbsp;1.1</td><td>-- Create/Read/Update/Delete personal tasks in personal calendars</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/apply.png" alt="Implemented"/> </td></tr>
<tr><td>&nbsp;1.2</td><td>-- Create/Read/Update/Delete space tasks in spaces calendars</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/apply.png" alt="Implemented"/> </td></tr>
<tr><td>&nbsp;1.2</td><td>-- Read spaces informations/personal information</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/apply.png" alt="Implemented"/> </td></tr>
<tr><td>&nbsp;1.3</td><td>-- Provide services to query calendars by views' filters (assignee, calendar group/type)</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/apply.png" alt="Implemented"/> </td></tr>
<tr><td>II.</td><td>On Web</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/apply.png" alt="Implemented"/> </td></tr>
<tr><td>&nbsp;1.</td><td>-- Add new tasks form &nbgt;&nbgt; update to calendar</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/apply.png" alt="Implemented"/> </td></tr>
<tr><td>&nbsp;2.</td><td>-- Switch view (Day/Week/Month): reload data from calendar repository, update the view</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/apply.png" alt="Implemented"/> </td></tr>
<tr><td>&nbsp;3.</td><td>-- Refresh view: reload data from calendar repository, update the view</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/apply.png" alt="Implemented"/> </td></tr>
<tr><td>&nbsp;4.</td><td>-- Change group: reload data from calendar repository, update the view with selected group</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/apply.png" alt="Implemented"/> </td></tr>
<tr><td>&nbsp;5.</td><td>-- Switch to full featured calendar</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/apply.png" alt="Implemented"/> </td></tr>
<tr><td>&nbsp;6.</td><td>-- Drag and drop, update status from Open to Inprogress and vice versa</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/apply.png" alt="Implemented"/> </td></tr>
<tr><td>&nbsp;7.</td><td>-- Drag and drop, update status from Inprogress to Done and vice versa</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/apply.png" alt="Implemented"/> </td></tr>
<tr><td>&nbsp;8.</td><td>-- Reflect done and inprogress in the Timeline view</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/apply.png" alt="Implemented"/> </td></tr>
<tr><td>&nbsp;9.</td><td>-- View task detail info</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/apply.png" alt="Implemented"/> </td></tr>
<tr><td>&nbsp;10.</td><td>-- Edit task detail info</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/apply.png" alt="Implemented"/> </td></tr>
<tr><td>&nbsp;11.</td><td>-- Delete task</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/apply.png" alt="Implemented"/> </td></tr>
<tr><td>&nbsp;12.</td><td>-- Smart tasks status checking and warning</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/stop-hand.png" alt="Wait to be implemented"/> </td></tr>
<tr><td>&nbsp;13.</td><td>-- Smart tasks suggestion</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/stop-hand.png" alt="Wait to be implemented"/> </td></tr>
<tr><td>&nbsp;13.</td><td>-- Smart tasks optimization</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/stop-hand.png" alt="Wait to be implemented"/> </td></tr>
<tr><td>III.</td><td>Mobile App (focus to personal task management)</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/stop-hand.png" alt="Wait to be implemented"/> </td></tr>
<tr><td>&nbsp;1.</td><td>-- Add new tasks form >> update to calendar</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/stop-hand.png" alt="Wait to be implemented"/> </td></tr>
<tr><td>&nbsp;2.</td><td>-- Switch view (Day/Week/Month): reload data from calendar repository, update the view</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/stop-hand.png" alt="Wait to be implemented"/> </td></tr>
<tr><td>&nbsp;3.</td><td>-- Refresh view: reload data from calendar repository, update the view</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/stop-hand.png" alt="Wait to be implemented"/> </td></tr>
<tr><td>&nbsp;4.</td><td>-- Change group: reload data from calendar repository, update the view with selected group</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/stop-hand.png" alt="Wait to be implemented"/> </td></tr>
<tr><td>&nbsp;5.</td><td>-- Switch to full featured calendar &nbgt; web browser</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/stop-hand.png" alt="Wait to be implemented"/> </td></tr>
<tr><td>&nbsp;6.</td><td>-- Drag and drop, update status from Open to Inprogress and vice versa</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/stop-hand.png" alt="Wait to be implemented"/> </td></tr>
<tr><td>&nbsp;7.</td><td>-- Drag and drop, update status from Inprogress to Done and vice versa</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/stop-hand.png" alt="Wait to be implemented"/> </td></tr>
<tr><td>&nbsp;8.</td><td>-- Reflect done and inprogress in the Timeline view</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/stop-hand.png" alt="Wait to be implemented"/> </td></tr>
<tr><td>&nbsp;9.</td><td>-- View task detail info</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/stop-hand.png" alt="Wait to be implemented"/> </td></tr>
<tr><td>&nbsp;10.</td><td>-- Edit task detail info</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/stop-hand.png" alt="Wait to be implemented"/> </td></tr>
<tr><td>&nbsp;11.</td><td>-- Delete task</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/stop-hand.png" alt="Wait to be implemented"/> </td></tr>
<tr><td>&nbsp;12.</td><td>-- Smart tasks status checking and warning</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/stop-hand.png" alt="Wait to be implemented"/> </td></tr>
<tr><td>&nbsp;13.</td><td>-- Smart tasks suggestion</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/stop-hand.png" alt="Wait to be implemented"/> </td></tr>
<tr><td>&nbsp;13.</td><td>-- Smart tasks optimization</td><td><img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/status_png/stop-hand.png" alt="Wait to be implemented"/> </td></tr>
                  </tbody>
                </table>

<img src="https://raw.githubusercontent.com/nghinv/CodeFest2014/master/documentation/images/CodeFest3.2.jpg" width="800">

Usages:
--------------------

Before being able to use the tool, you need to join a group (space) which tasks will be managed.
So: look for your collaboration group, join to that group. Or: create your own collaboration group, invite members and manage tasks within the group.

* Basic function
    * Create a new task:
        * Tasks Management Tool home
        * Click to Add Task
        * Input details into form 
        * Save
    * Start a task:
        * Tasks Management Tool home
        * In the Open list
        * Drag and Drop an item to Inprogress list
    * Finish a task:
        * Tasks Management Tool home
        * In the Inprogress list
        * Drag and Drop an item to Done list; set time spent, set note
    * Switch to this Week view
        * Tasks Management Tool home
        * Click to WEEK
    * Switch to this Month view
        * Tasks Management Tool home
        * Click to MONTH
    * Filter assigned task by group
        * Tasks Management Tool home
        * Click to Group box
        * Select a suitable group
    * View/Update task detail
        * Tasks Management Tool home
        * Click to item's setting button
    * View tasks in calendar page
        * Tasks Management Tool home
        * Click to Calendar button
* For collaboration groups:
    * Create a space for a collaboration group first, calendar of the group will be come the group collaboration calendar
    * A group owner will able to create tasks belong to that group and assign to one member of each group
    * set the Tasks Management Tool filter group to the correct collaboration group
    * create/manage/review/do tasks as normal

    
Note:
--------------------
* Groups: All/Private/GroupXYZ
* Create new task should use current selected group as default