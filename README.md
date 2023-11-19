# JournalApp
I've created a straightforward code resembling a personal journal. It's a personal project aimed at improving my Java skills.

10/9/2023: I successfully made the code function as intended. It allows the user to write a title and an entry, much like maintaining a personal journal. There are still some additional features that need to be incorporated, but this marks a step forward i think.

11/9/2023: I introduced password-based user login security. Users now need to register a password before gaining access. However, I encountered an issue where the 'Main' in the com/github/shawarmaland folder and 'MainApp' in the directory were somewhat isolated. My next step is to consolidate these components and outline what comes next.

12/9/2023: I managed to integrated the login and registration processes upon the terminal startup, followed by introducing additional functionalities for handling entries, including addition, editing, viewing, and deletion. It performed as per my expectations. However, the next task on is to implement data storage for these and more other stuff that will be added soon. hopefully this will also go smoothly.

17/9/2023: I made modifications to the project by both adding and moving methods to other places. I successfully managed to store user registration data and login functionality as intended. However, there are some issues with storing entries that require fixes and adjustments.

18/9/2023: I've successfully resolved the problem related to updating an Entry for the journal project. Following this, I conducted a review of the codebase and identified some files that were no longer in use. As a result, I now have fewer files than before after this cleanup.

26/9/2023: Implemented a pagination system for viewing journal entries, enhancing the user experience by allowing them to navigate through entries page by page. Also refined the navigation flow by ensuring users return to the journal menu after exiting the pagination view.

30/9/2023: Initiated the development of the GUI using JavaFX. Successfully launched the basic GUI window, displaying menu options like 'File', which includes 'New Entry', 'Save', and 'Exit'. Current GUI is preliminary and displays a blank canvas. Future enhancements will focus on integrating existing functionalities and improving the interface.

17/10/2023: Continued enhancing the GUI of the application. Added functionality to the 'New Entry' option in the 'File' menu, enabling the opening of a new window where users can input a title and content for their journal entries. Implemented the backend logic to save these new entries to the database, ensuring that the user's input is successfully stored and can be retrieved later. Encountered and resolved various issues, such as handling empty inputs and ensuring that the database interactions run smoothly. The database's contents were validated to confirm that entries were correctly saved. Future work will focus on refining the user interface, improving the user experience, and continuing to integrate the remaining functionalities into the GUI.

24/10/2023: Worked on refining the GUI of the journal application. Successfully synchronized the ListView with the database, enabling real-time updates of entries without requiring a restart. Resolved issues related to the display of entries, ensuring that the title and content are accurately presented in the interface. Various bugs and glitches were addressed, streamlining the user experience and improving the application’s overall functionality and reliability.

30/10/2023: Engaged in enhancing the journal application, focusing on troubleshooting and error resolution. Addressed crucial errors and exceptions to improve database interactions and user experience by making essential adjustments and refinements in method invocations and object initializations.

31/10/2023: Continued optimization efforts, primarily troubleshooting database interaction errors. Adjusted database queries and object creation processes, enhancing the application's robustness and user experience by ensuring successful database interactions and manipulations.

5/11/2023: Progressed further in refining the edit functionality. Now, selecting an entry to edit brings up a window pre-filled with the entry's existing content and title, streamlining the editing process. Furthermore, I implemented a search feature that allows for quick retrieval of entries through a search bar, enhancing navigation and accessibility.

12/11/2023: Implemented export and import functions to enable users to extract entries from the application. While some issues persist, such as duplication when importing files and a similar problem with deletions (which has been resolved), there is still a need to address the import options.  

14/11/2023: After encountering issues with the import and export features, I successfully resolved them. Now, when importing a file with the same title and content, the system will skip it as there is already a matching entry. Additionally, when importing a new file to the application, it will only import the selected file and not add entries that were previously saved within the app

19/11/2023: Implemented a feature in the project by integrating an HTML Editor, replacing the standard TextArea component. This enhancement provides a more user-friendly interface, enabling users to effortlessly add fonts and other formatting options, thereby enhancing the overall usability and aesthetic appeal of the application.
