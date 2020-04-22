package studycom.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import studycom.web.domain.UsersPart.Task;
import studycom.web.domain.UsersPart.User;
import studycom.web.repos.TaskRepository;

@SessionAttributes(value = "user")
@Controller
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/deleteTask")
    public String deleteTask(@RequestParam(value = "taskId") Integer id) {
        taskRepository.deleteById(id);
        return "redirect:/home";
    }


    @GetMapping("/addTask")
    public String addTask(@ModelAttribute("user") User user,
                          @RequestParam(value = "taskName") String taskName) {
        ModelAndView model = new ModelAndView();
        Task task = new Task(taskName, user);
        task.setId(user.getId());
        taskRepository.save(task);
        return "redirect:/home";
    }
}
