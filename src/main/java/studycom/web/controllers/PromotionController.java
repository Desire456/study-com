package studycom.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import studycom.web.domain.UsersPart.Promotion;
import studycom.web.domain.UsersPart.User;
import studycom.web.repos.PromotionRepository;

@SessionAttributes(value = "user")
@Controller
public class PromotionController {
    @Autowired
    PromotionRepository promotionRepository;

    @GetMapping("/addPromotion")
    public ModelAndView sendPromotion(@ModelAttribute("user") User user, @RequestParam(value = "cause") String cause) {
        String[] causeAndExp = cause.split(" ");
        int expNumber = Integer.parseInt(causeAndExp[1]);
        String senderName = user.getSurname() + " " + user.getName();
        promotionRepository.save(new Promotion(expNumber, senderName, causeAndExp[0], user.getGroup(), user.getId()));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("profile");
        return modelAndView;
    }
}
