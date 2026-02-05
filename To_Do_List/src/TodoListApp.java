import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

// Task class (same as before)
class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String description;
    private boolean isCompleted;
    private LocalDate dueDate;
    private String category;
    private int priority; // 1 = High, 2 = Medium, 3 = Low
    
    public Task(int id, String description, LocalDate dueDate, String category, int priority) {
        this.id = id;
        this.description = description;
        this.isCompleted = false;
        this.dueDate = dueDate;
        this.category = category;
        this.priority = priority;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    
    @Override
    public String toString() {
        String status = isCompleted ? "[✓]" : "[ ]";
        String priorityStr = "";
        switch(priority) {
            case 1: priorityStr = "HIGH"; break;
            case 2: priorityStr = "MEDIUM"; break;
            case 3: priorityStr = "LOW"; break;
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String dueDateStr = (dueDate != null) ? dueDate.format(formatter) : "No due date";
        
        return String.format("%-4s %-50s %-12s %-15s %-10s", 
                            status, description, dueDateStr, category, priorityStr);
    }
}

// Main application class with dummy data
public class TodoListApp {
    private static List<Task> tasks = new ArrayList<>();
    private static int nextId = 1;
    private static final String FILE_NAME = "todo_list_data.ser";
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        loadTasksFromFile();
        
        // If no tasks were loaded from file, load dummy data
        if (tasks.isEmpty()) {
            loadDummyData();
            System.out.println("Loaded dummy data for demonstration!");
        } else {
            System.out.println("Loaded existing tasks from file.");
        }
        
        System.out.println("=========================================");
        System.out.println("   JAVA TODO LIST APP (WITH DUMMY DATA)  ");
        System.out.println("=========================================");
        
        boolean running = true;
        while (running) {
            displayMenu();
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    viewTasks();
                    break;
                case "2":
                    addTask();
                    break;
                case "3":
                    editTask();
                    break;
                case "4":
                    deleteTask();
                    break;
                case "5":
                    markTaskCompleted();
                    break;
                case "6":
                    filterTasks();
                    break;
                case "7":
                    saveTasksToFile();
                    System.out.println("Tasks saved successfully!");
                    break;
                case "8":
                    running = false;
                    saveTasksToFile();
                    System.out.println("Goodbye! Your tasks have been saved.");
                    break;
                case "9":  // Hidden option to reload dummy data
                    reloadDummyData();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
    
    private static void loadDummyData() {
        tasks.clear();
        nextId = 1;
        
        // Current date and future dates for dummy tasks
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate nextWeek = today.plusDays(7);
        LocalDate yesterday = today.minusDays(1);
        LocalDate nextMonth = today.plusMonths(1);
        
        // Add various dummy tasks
        tasks.add(new Task(nextId++, "Complete Java project assignment", nextWeek, "Education", 1));
        tasks.add(new Task(nextId++, "Buy groceries for the week", tomorrow, "Shopping", 2));
        tasks.add(new Task(nextId++, "Schedule dentist appointment", nextMonth, "Health", 2));
        tasks.add(new Task(nextId++, "Prepare presentation for team meeting", today, "Work", 1));
        tasks.add(new Task(nextId++, "Read 'Clean Code' book chapter", null, "Personal", 3));
        tasks.add(new Task(nextId++, "Pay electricity bill", yesterday, "Finance", 1));
        tasks.add(new Task(nextId++, "Call mom for her birthday", tomorrow, "Family", 1));
        tasks.add(new Task(nextId++, "Clean the apartment", null, "Home", 3));
        tasks.add(new Task(nextId++, "Plan weekend hiking trip", nextWeek, "Leisure", 2));
        tasks.add(new Task(nextId++, "Update resume for job applications", nextMonth, "Career", 2));
        tasks.add(new Task(nextId++, "Fix leaking faucet in bathroom", null, "Home", 2));
        tasks.add(new Task(nextId++, "Research investment options", nextWeek, "Finance", 3));
        tasks.add(new Task(nextId++, "Finish reading novel", tomorrow, "Personal", 3));
        tasks.add(new Task(nextId++, "Organize digital files and folders", null, "Work", 3));
        tasks.add(new Task(nextId++, "Buy birthday gift for friend", today, "Shopping", 2));
        
        // Mark some tasks as completed
        tasks.get(0).setCompleted(true);  // Complete Java project
        tasks.get(1).setCompleted(true);  // Buy groceries
        tasks.get(5).setCompleted(true);  // Pay electricity bill
        tasks.get(6).setCompleted(true);  // Call mom
        
        System.out.println("Loaded " + tasks.size() + " dummy tasks!");
    }
    
    private static void reloadDummyData() {
        System.out.print("This will replace all current tasks with dummy data. Continue? (yes/no): ");
        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("yes")) {
            loadDummyData();
            System.out.println("Dummy data reloaded successfully!");
        } else {
            System.out.println("Operation cancelled.");
        }
    }
    
    private static void displayMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. View All Tasks");
        System.out.println("2. Add New Task");
        System.out.println("3. Edit Task");
        System.out.println("4. Delete Task");
        System.out.println("5. Mark Task as Completed/Incomplete");
        System.out.println("6. Filter Tasks");
        System.out.println("7. Save Tasks to File");
        System.out.println("8. Exit");
        System.out.println("===============================");
        System.out.print("Enter your choice (1-8): ");
    }
    
    private static void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("\nNo tasks found. Your to-do list is empty!");
            return;
        }
        
        System.out.println("\n========================================== YOUR TODO LIST ==========================================");
        System.out.printf("%-4s %-50s %-12s %-15s %-10s\n", "STAT", "DESCRIPTION", "DUE DATE", "CATEGORY", "PRIORITY");
        System.out.println("---------------------------------------------------------------------------------------------------");
        
        // Sort tasks by priority (High first) and then by due date
        tasks.sort(Comparator.comparingInt(Task::getPriority)
                .thenComparing(task -> task.getDueDate() != null ? task.getDueDate() : LocalDate.MAX));
        
        for (Task task : tasks) {
            System.out.println(task);
        }
        
        int completedCount = (int) tasks.stream().filter(Task::isCompleted).count();
        System.out.println("\nTotal tasks: " + tasks.size() + " | Completed: " + completedCount + " | Pending: " + (tasks.size() - completedCount));
        System.out.println("\nSample filters to try:");
        System.out.println("- Filter by 'Completed' tasks (option 6, then 1)");
        System.out.println("- Filter by 'Work' category (option 6, then 3)");
        System.out.println("- Filter by 'High' priority (option 6, then 4)");
    }
    
    private static void addTask() {
        System.out.println("\n========== ADD NEW TASK ==========");
        
        System.out.print("Enter task description: ");
        String description = scanner.nextLine();
        
        if (description.trim().isEmpty()) {
            System.out.println("Task description cannot be empty!");
            return;
        }
        
        LocalDate dueDate = null;
        System.out.print("Enter due date (MM/dd/yyyy) or press Enter for none: ");
        String dateInput = scanner.nextLine();
        
        if (!dateInput.trim().isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                dueDate = LocalDate.parse(dateInput, formatter);
                
                if (dueDate.isBefore(LocalDate.now())) {
                    System.out.println("Warning: Due date is in the past!");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Task will be saved without a due date.");
            }
        }
        
        System.out.print("Enter category (Work, Personal, Shopping, etc.): ");
        String category = scanner.nextLine();
        if (category.trim().isEmpty()) {
            category = "General";
        }
        
        int priority = 2; // Default to medium
        System.out.print("Enter priority (1=High, 2=Medium, 3=Low): ");
        String priorityInput = scanner.nextLine();
        try {
            priority = Integer.parseInt(priorityInput);
            if (priority < 1 || priority > 3) {
                priority = 2;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid priority. Setting to Medium (2).");
        }
        
        Task newTask = new Task(nextId++, description, dueDate, category, priority);
        tasks.add(newTask);
        System.out.println("Task added successfully! Task ID: " + newTask.getId());
    }
    
    private static void editTask() {
        if (tasks.isEmpty()) {
            System.out.println("\nNo tasks to edit. Your to-do list is empty!");
            return;
        }
        
        System.out.println("\n========== EDIT TASK ==========");
        viewTasks();
        
        System.out.print("\nEnter the ID of the task you want to edit: ");
        try {
            int taskId = Integer.parseInt(scanner.nextLine());
            Task taskToEdit = findTaskById(taskId);
            
            if (taskToEdit == null) {
                System.out.println("Task with ID " + taskId + " not found.");
                return;
            }
            
            System.out.println("\nEditing Task:");
            System.out.println("1. Description: " + taskToEdit.getDescription());
            System.out.println("2. Due Date: " + (taskToEdit.getDueDate() != null ? 
                taskToEdit.getDueDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) : "None"));
            System.out.println("3. Category: " + taskToEdit.getCategory());
            System.out.println("4. Priority: " + 
                (taskToEdit.getPriority() == 1 ? "High" : 
                 taskToEdit.getPriority() == 2 ? "Medium" : "Low"));
            
            System.out.print("\nWhat would you like to edit? (1-4): ");
            String editChoice = scanner.nextLine();
            
            switch (editChoice) {
                case "1":
                    System.out.print("Enter new description: ");
                    String newDescription = scanner.nextLine();
                    if (!newDescription.trim().isEmpty()) {
                        taskToEdit.setDescription(newDescription);
                        System.out.println("Description updated.");
                    }
                    break;
                case "2":
                    System.out.print("Enter new due date (MM/dd/yyyy) or press Enter to remove: ");
                    String newDateInput = scanner.nextLine();
                    if (newDateInput.trim().isEmpty()) {
                        taskToEdit.setDueDate(null);
                        System.out.println("Due date removed.");
                    } else {
                        try {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                            LocalDate newDueDate = LocalDate.parse(newDateInput, formatter);
                            taskToEdit.setDueDate(newDueDate);
                            System.out.println("Due date updated.");
                        } catch (DateTimeParseException e) {
                            System.out.println("Invalid date format. Due date not changed.");
                        }
                    }
                    break;
                case "3":
                    System.out.print("Enter new category: ");
                    String newCategory = scanner.nextLine();
                    if (!newCategory.trim().isEmpty()) {
                        taskToEdit.setCategory(newCategory);
                        System.out.println("Category updated.");
                    }
                    break;
                case "4":
                    System.out.print("Enter new priority (1=High, 2=Medium, 3=Low): ");
                    try {
                        int newPriority = Integer.parseInt(scanner.nextLine());
                        if (newPriority >= 1 && newPriority <= 3) {
                            taskToEdit.setPriority(newPriority);
                            System.out.println("Priority updated.");
                        } else {
                            System.out.println("Invalid priority. Must be 1, 2, or 3.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Priority not changed.");
                    }
                    break;
                default:
                    System.out.println("Invalid choice. No changes made.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Please enter a number.");
        }
    }
    
    private static void deleteTask() {
        if (tasks.isEmpty()) {
            System.out.println("\nNo tasks to delete. Your to-do list is empty!");
            return;
        }
        
        System.out.println("\n========== DELETE TASK ==========");
        viewTasks();
        
        System.out.print("\nEnter the ID of the task you want to delete: ");
        try {
            int taskId = Integer.parseInt(scanner.nextLine());
            Task taskToDelete = findTaskById(taskId);
            
            if (taskToDelete == null) {
                System.out.println("Task with ID " + taskId + " not found.");
                return;
            }
            
            System.out.println("Are you sure you want to delete this task?");
            System.out.println(taskToDelete);
            System.out.print("Enter 'yes' to confirm: ");
            String confirmation = scanner.nextLine();
            
            if (confirmation.equalsIgnoreCase("yes")) {
                tasks.remove(taskToDelete);
                System.out.println("Task deleted successfully.");
            } else {
                System.out.println("Deletion cancelled.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Please enter a number.");
        }
    }
    
    private static void markTaskCompleted() {
        if (tasks.isEmpty()) {
            System.out.println("\nNo tasks to mark. Your to-do list is empty!");
            return;
        }
        
        System.out.println("\n========== MARK TASK COMPLETED/INCOMPLETE ==========");
        viewTasks();
        
        System.out.print("\nEnter the ID of the task you want to mark: ");
        try {
            int taskId = Integer.parseInt(scanner.nextLine());
            Task taskToMark = findTaskById(taskId);
            
            if (taskToMark == null) {
                System.out.println("Task with ID " + taskId + " not found.");
                return;
            }
            
            String newStatus = taskToMark.isCompleted() ? "incomplete" : "completed";
            System.out.println("Mark task as " + newStatus + "? (yes/no): ");
            String confirmation = scanner.nextLine();
            
            if (confirmation.equalsIgnoreCase("yes")) {
                taskToMark.setCompleted(!taskToMark.isCompleted());
                System.out.println("Task marked as " + newStatus + ".");
            } else {
                System.out.println("Operation cancelled.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Please enter a number.");
        }
    }
    
    private static void filterTasks() {
        if (tasks.isEmpty()) {
            System.out.println("\nNo tasks to filter. Your to-do list is empty!");
            return;
        }
        
        System.out.println("\n========== FILTER TASKS ==========");
        System.out.println("1. View Completed Tasks");
        System.out.println("2. View Pending Tasks");
        System.out.println("3. View Tasks by Category");
        System.out.println("4. View Tasks by Priority");
        System.out.println("5. View Tasks Due Today");
        System.out.println("6. View Overdue Tasks");
        System.out.print("Enter your choice (1-6): ");
        
        String choice = scanner.nextLine();
        List<Task> filteredTasks = new ArrayList<>();
        LocalDate today = LocalDate.now();
        
        switch (choice) {
            case "1":
                filteredTasks = tasks.stream()
                    .filter(Task::isCompleted)
                    .toList();
                System.out.println("\n========== COMPLETED TASKS ==========");
                break;
            case "2":
                filteredTasks = tasks.stream()
                    .filter(task -> !task.isCompleted())
                    .toList();
                System.out.println("\n========== PENDING TASKS ==========");
                break;
            case "3":
                System.out.print("Enter category to filter by: ");
                String category = scanner.nextLine();
                filteredTasks = tasks.stream()
                    .filter(task -> task.getCategory().equalsIgnoreCase(category))
                    .toList();
                System.out.println("\n========== TASKS IN CATEGORY: " + category.toUpperCase() + " ==========");
                break;
            case "4":
                System.out.print("Enter priority to filter by (1=High, 2=Medium, 3=Low): ");
                try {
                    int priority = Integer.parseInt(scanner.nextLine());
                    if (priority >= 1 && priority <= 3) {
                        filteredTasks = tasks.stream()
                            .filter(task -> task.getPriority() == priority)
                            .toList();
                        String priorityStr = priority == 1 ? "HIGH" : priority == 2 ? "MEDIUM" : "LOW";
                        System.out.println("\n========== " + priorityStr + " PRIORITY TASKS ==========");
                    } else {
                        System.out.println("Invalid priority. Returning to menu.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Returning to menu.");
                    return;
                }
                break;
            case "5":
                filteredTasks = tasks.stream()
                    .filter(task -> task.getDueDate() != null && task.getDueDate().equals(today))
                    .toList();
                System.out.println("\n========== TASKS DUE TODAY ==========");
                break;
            case "6":
                filteredTasks = tasks.stream()
                    .filter(task -> task.getDueDate() != null && 
                             task.getDueDate().isBefore(today) && 
                             !task.isCompleted())
                    .toList();
                System.out.println("\n========== OVERDUE TASKS ==========");
                break;
            default:
                System.out.println("Invalid choice. Returning to main menu.");
                return;
        }
        
        if (filteredTasks.isEmpty()) {
            System.out.println("No tasks found with the selected filter.");
        } else {
            System.out.printf("%-4s %-50s %-12s %-15s %-10s\n", "STAT", "DESCRIPTION", "DUE DATE", "CATEGORY", "PRIORITY");
            System.out.println("---------------------------------------------------------------------------------------------------");
            for (Task task : filteredTasks) {
                System.out.println(task);
            }
            System.out.println("\nTotal tasks found: " + filteredTasks.size());
        }
    }
    
    private static Task findTaskById(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }
    
    private static void saveTasksToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(tasks);
            oos.writeInt(nextId);
        } catch (IOException e) {
            System.out.println("Error saving tasks to file: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private static void loadTasksFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            tasks = (List<Task>) ois.readObject();
            nextId = ois.readInt();
            System.out.println("Previous tasks loaded successfully!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading tasks from file. Starting with empty list.");
            tasks = new ArrayList<>();
        }
    }
}